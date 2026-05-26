package com.pingyu.cloudmorph.workflow;

import com.pingyu.cloudmorph.core.AiCodeGeneratorFacade;
import com.pingyu.cloudmorph.core.builder.VueProjectBuilder;
import com.pingyu.cloudmorph.model.entity.App;
import com.pingyu.cloudmorph.model.entity.User;
import com.pingyu.cloudmorph.model.enums.CodeGenTypeEnum;
import com.pingyu.cloudmorph.model.vo.AppWorkflowResultVO;
import com.pingyu.cloudmorph.service.AppService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppWorkflowServiceImplTest {

    @Mock
    private AppService appService;

    @Mock
    private AiCodeGeneratorFacade aiCodeGeneratorFacade;

    @Mock
    private VueProjectBuilder vueProjectBuilder;

    @Test
    void runWorkflowShouldGenerateBuildAndReturnResultForVueProject() {
        AppWorkflowServiceImpl service = new AppWorkflowServiceImpl(appService, aiCodeGeneratorFacade, vueProjectBuilder);
        service.init();

        User loginUser = new User();
        loginUser.setId(1L);

        App app = new App();
        app.setId(100L);
        app.setUserId(1L);
        app.setAppName("demo");
        app.setCodeGenType(CodeGenTypeEnum.VUE_PROJECT.getValue());
        app.setCover("https://example.com/cover.png");

        File outputDir = new File("tmp/code_output/vue_project_100");
        File distDir = new File(outputDir, "dist");
        distDir.mkdirs();
        when(appService.getById(100L)).thenReturn(app);
        when(appService.smartSelectCodeGenType(anyString())).thenReturn(CodeGenTypeEnum.VUE_PROJECT.getValue());
        when(aiCodeGeneratorFacade.generateAndSaveCode(anyString(), eq(CodeGenTypeEnum.VUE_PROJECT), eq(100L))).thenReturn(outputDir);
        when(vueProjectBuilder.buildProjectResult(outputDir.getAbsolutePath()))
                .thenReturn(new com.pingyu.cloudmorph.core.builder.VueProjectBuildResult(true, null, null, outputDir.getAbsolutePath()));

        AppWorkflowResultVO result = service.runWorkflow(100L, "请生成一个 Vue 项目", "", loginUser);

        assertEquals(100L, result.getAppId());
        assertEquals(CodeGenTypeEnum.VUE_PROJECT.getValue(), result.getActualCodeGenType());
        assertTrue(result.isProjectBuilt());
        assertTrue(result.isQualityPassed());
        assertEquals(outputDir.getAbsolutePath(), result.getOutputPath());
    }

    @Test
    void runWorkflowShouldUseRecommendedTypeWhenAppTypeIsBlank() {
        AppWorkflowServiceImpl service = new AppWorkflowServiceImpl(appService, aiCodeGeneratorFacade, vueProjectBuilder);
        service.init();

        User loginUser = new User();
        loginUser.setId(2L);

        App app = new App();
        app.setId(200L);
        app.setUserId(2L);
        app.setAppName("landing");

        File outputDir = new File("tmp/code_output/html_200");
        outputDir.mkdirs();
        when(appService.getById(200L)).thenReturn(app);
        when(appService.smartSelectCodeGenType(anyString())).thenReturn(CodeGenTypeEnum.HTML.getValue());
        when(aiCodeGeneratorFacade.generateAndSaveCode(anyString(), eq(CodeGenTypeEnum.HTML), eq(200L))).thenReturn(outputDir);

        AppWorkflowResultVO result = service.runWorkflow(200L, "做一个简单落地页", "", loginUser);

        assertEquals(CodeGenTypeEnum.HTML.getValue(), result.getActualCodeGenType());
        assertEquals(CodeGenTypeEnum.HTML.getValue(), result.getRecommendedCodeGenType());
        assertTrue(result.isQualityPassed());
    }

    @Test
    void runWorkflowShouldExposeBuildFailureDetailsWhenBuildFails() {
        AppWorkflowServiceImpl service = new AppWorkflowServiceImpl(appService, aiCodeGeneratorFacade, vueProjectBuilder);
        service.init();

        User loginUser = new User();
        loginUser.setId(3L);

        App app = new App();
        app.setId(300L);
        app.setUserId(3L);
        app.setAppName("broken-vue");
        app.setCodeGenType(CodeGenTypeEnum.VUE_PROJECT.getValue());

        File outputDir = new File("tmp/code_output/vue_project_300");
        outputDir.mkdirs();
        when(appService.getById(300L)).thenReturn(app);
        when(appService.smartSelectCodeGenType(anyString())).thenReturn(CodeGenTypeEnum.VUE_PROJECT.getValue());
        when(aiCodeGeneratorFacade.generateAndSaveCode(anyString(), eq(CodeGenTypeEnum.VUE_PROJECT), eq(300L))).thenReturn(outputDir);
        when(vueProjectBuilder.buildProjectResult(outputDir.getAbsolutePath()))
                .thenReturn(new com.pingyu.cloudmorph.core.builder.VueProjectBuildResult(false, "npm_build", "npm run build failed", outputDir.getAbsolutePath()));

        AppWorkflowResultVO result = service.runWorkflow(300L, "请生成一个 Vue 项目", "", loginUser);

        assertEquals("npm_build", result.getBuildFailedStage());
        assertEquals("npm run build failed", result.getBuildErrorMessage());
        assertTrue(!result.isQualityPassed());
        assertTrue(!result.isProjectBuilt());
    }
}
