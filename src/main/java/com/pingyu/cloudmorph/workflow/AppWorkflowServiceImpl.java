package com.pingyu.cloudmorph.workflow;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.pingyu.cloudmorph.ai.model.VisualEditResult;
import com.pingyu.cloudmorph.constant.AppConstant;
import com.pingyu.cloudmorph.core.AiCodeGeneratorFacade;
import com.pingyu.cloudmorph.core.builder.VueProjectBuilder;
import com.pingyu.cloudmorph.exception.BusinessException;
import com.pingyu.cloudmorph.exception.ErrorCode;
import com.pingyu.cloudmorph.exception.ThrowUtils;
import com.pingyu.cloudmorph.model.entity.App;
import com.pingyu.cloudmorph.model.entity.User;
import com.pingyu.cloudmorph.model.enums.CodeGenTypeEnum;
import com.pingyu.cloudmorph.model.vo.AppWorkflowResultVO;
import com.pingyu.cloudmorph.service.AppService;
import com.pingyu.cloudmorph.workflow.model.WorkflowImageCategoryEnum;
import com.pingyu.cloudmorph.workflow.model.WorkflowImageResource;
import com.pingyu.cloudmorph.workflow.model.WorkflowState;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.action.AsyncEdgeAction;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 应用工作流服务实现。
 */
@Slf4j
@Service
public class AppWorkflowServiceImpl implements AppWorkflowService {

    private static final String NODE_IMAGE_COLLECTOR = "image_collector";
    private static final String NODE_PROMPT_ENHANCER = "prompt_enhancer";
    private static final String NODE_ROUTER = "router";
    private static final String NODE_CODE_GENERATOR = "code_generator";
    private static final String NODE_PROJECT_BUILDER = "project_builder";
    private static final String NODE_QUALITY_CHECK = "quality_check";
    private static final String EDGE_CODE_GENERATOR = "code_generator";
    private static final String EDGE_END = "end";

    private final AppService appService;
    private final AiCodeGeneratorFacade aiCodeGeneratorFacade;
    private final VueProjectBuilder vueProjectBuilder;
    private CompiledGraph<WorkflowState> compiledGraph;

    public AppWorkflowServiceImpl(AppService appService,
                                  AiCodeGeneratorFacade aiCodeGeneratorFacade,
                                  VueProjectBuilder vueProjectBuilder) {
        this.appService = appService;
        this.aiCodeGeneratorFacade = aiCodeGeneratorFacade;
        this.vueProjectBuilder = vueProjectBuilder;
    }

    @PostConstruct
    public void init() {
        try {
            StateGraph<WorkflowState> graph = new StateGraph<>(WorkflowState::new);
            graph.addNode(NODE_IMAGE_COLLECTOR, AsyncNodeAction.node_async(this::collectImageResources));
            graph.addNode(NODE_PROMPT_ENHANCER, AsyncNodeAction.node_async(this::enhancePrompt));
            graph.addNode(NODE_ROUTER, AsyncNodeAction.node_async(this::routeCodeGenerationType));
            graph.addNode(NODE_CODE_GENERATOR, AsyncNodeAction.node_async(this::generateCode));
            graph.addNode(NODE_PROJECT_BUILDER, AsyncNodeAction.node_async(this::buildProject));
            graph.addNode(NODE_QUALITY_CHECK, AsyncNodeAction.node_async(this::qualityCheck));

            graph.addEdge(StateGraph.START, NODE_IMAGE_COLLECTOR);
            graph.addEdge(NODE_IMAGE_COLLECTOR, NODE_PROMPT_ENHANCER);
            graph.addEdge(NODE_PROMPT_ENHANCER, NODE_ROUTER);
            graph.addConditionalEdges(NODE_ROUTER, AsyncEdgeAction.edge_async(this::routeAfterRouter), Map.of(
                    EDGE_CODE_GENERATOR, NODE_CODE_GENERATOR,
                    EDGE_END, StateGraph.END
            ));
            graph.addEdge(NODE_CODE_GENERATOR, NODE_PROJECT_BUILDER);
            graph.addEdge(NODE_PROJECT_BUILDER, NODE_QUALITY_CHECK);
            graph.addEdge(NODE_QUALITY_CHECK, StateGraph.END);

            this.compiledGraph = graph.compile();
        } catch (GraphStateException e) {
            throw new IllegalStateException("初始化工作流失败: " + e.getMessage(), e);
        }
    }

    @Override
    public AppWorkflowResultVO runWorkflow(Long appId, String prompt, String selectedElement, User loginUser) {
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "应用 ID 不能为空");
        ThrowUtils.throwIf(StrUtil.isBlank(prompt), ErrorCode.PARAMS_ERROR, "需求描述不能为空");
        ThrowUtils.throwIf(loginUser == null, ErrorCode.PARAMS_ERROR, "登录用户不能为空");

        App app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        if (!Objects.equals(app.getUserId(), loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限执行工作流");
        }

        Map<String, Object> initialData = Map.of(
                WorkflowState.APP_ID, appId,
                WorkflowState.PROMPT, prompt,
                WorkflowState.SELECTED_ELEMENT, StrUtil.nullToEmpty(selectedElement),
                "appName", StrUtil.nullToEmpty(app.getAppName()),
                "codeGenType", StrUtil.nullToEmpty(app.getCodeGenType()),
                "cover", StrUtil.nullToEmpty(app.getCover())
        );

        WorkflowState finalState = compiledGraph.invoke(initialData)
                .orElseThrow(() -> new BusinessException(ErrorCode.SYSTEM_ERROR, "工作流执行失败"));

        return toResult(finalState);
    }

    private Map<String, Object> collectImageResources(WorkflowState state) {
        String cover = state.value("cover", "");
        List<WorkflowImageResource> resources = new ArrayList<>();
        if (StrUtil.isNotBlank(cover)) {
            resources.add(WorkflowImageResource.of(WorkflowImageCategoryEnum.COVER, "应用封面", cover));
        }
        return Map.of(WorkflowState.IMAGE_RESOURCES, resources);
    }

    private Map<String, Object> enhancePrompt(WorkflowState state) {
        String prompt = state.value(WorkflowState.PROMPT, "");
        String selectedElement = state.value(WorkflowState.SELECTED_ELEMENT, "");
        List<WorkflowImageResource> imageResources = state.value(WorkflowState.IMAGE_RESOURCES, List.of());
        StringBuilder builder = new StringBuilder();
        builder.append("用户需求：").append(prompt);
        if (StrUtil.isNotBlank(selectedElement)) {
            builder.append("\n选中元素：").append(selectedElement);
        }
        if (!imageResources.isEmpty()) {
            builder.append("\n素材信息：");
            for (WorkflowImageResource resource : imageResources) {
                builder.append("[").append(resource.getCategory()).append("]").append(resource.getUrl()).append(" ");
            }
        }
        return Map.of(WorkflowState.ENHANCED_PROMPT, builder.toString().trim());
    }

    private Map<String, Object> routeCodeGenerationType(WorkflowState state) {
        String originalType = state.value("codeGenType", "");
        String prompt = state.value(WorkflowState.ENHANCED_PROMPT, state.value(WorkflowState.PROMPT, ""));
        String recommendedType = appService.smartSelectCodeGenType(prompt);
        String actualType = StrUtil.isNotBlank(originalType) ? originalType : recommendedType;
        return Map.of(
                WorkflowState.RECOMMENDED_CODE_GEN_TYPE, recommendedType,
                WorkflowState.ACTUAL_CODE_GEN_TYPE, actualType
        );
    }

    private String routeAfterRouter(WorkflowState state) {
        String actualType = state.value(WorkflowState.ACTUAL_CODE_GEN_TYPE, "");
        return StrUtil.isBlank(actualType) ? EDGE_END : EDGE_CODE_GENERATOR;
    }

    private Map<String, Object> generateCode(WorkflowState state) {
        Long appId = state.value(WorkflowState.APP_ID, 0L);
        String prompt = state.value(WorkflowState.ENHANCED_PROMPT, state.value(WorkflowState.PROMPT, ""));
        CodeGenTypeEnum codeGenTypeEnum = CodeGenTypeEnum.getEnumByValue(state.value(WorkflowState.ACTUAL_CODE_GEN_TYPE, ""));
        ThrowUtils.throwIf(codeGenTypeEnum == null, ErrorCode.PARAMS_ERROR, "不支持的代码生成类型");
        File outputDir = aiCodeGeneratorFacade.generateAndSaveCode(prompt, codeGenTypeEnum, appId);
        return Map.of(
                WorkflowState.OUTPUT_PATH, outputDir.getAbsolutePath()
        );
    }

    private Map<String, Object> buildProject(WorkflowState state) {
        String actualType = state.value(WorkflowState.ACTUAL_CODE_GEN_TYPE, "");
        if (!CodeGenTypeEnum.VUE_PROJECT.getValue().equals(actualType)) {
            return Map.of(
                    WorkflowState.PROJECT_BUILT, false
            );
        }
        Long appId = state.value(WorkflowState.APP_ID, 0L);
        String sourceDirPath = AppConstant.CODE_OUTPUT_ROOT_DIR + File.separator + actualType + "_" + appId;
        boolean success = vueProjectBuilder.buildProject(sourceDirPath);
        return Map.of(
                WorkflowState.PROJECT_BUILT, success
        );
    }

    private Map<String, Object> qualityCheck(WorkflowState state) {
        String outputPath = state.value(WorkflowState.OUTPUT_PATH, "");
        boolean projectBuilt = state.value(WorkflowState.PROJECT_BUILT, false);
        boolean passed = StrUtil.isNotBlank(outputPath);
        if (passed) {
            passed = FileUtil.exist(outputPath);
        }
        if (projectBuilt) {
            passed = passed && FileUtil.exist(new File(outputPath, "dist"));
        }
        String message = passed ? "工作流执行成功" : "工作流执行后未找到预期产物";
        return Map.of(
                WorkflowState.QUALITY_PASSED, passed,
                WorkflowState.QUALITY_MESSAGE, message
        );
    }

    private AppWorkflowResultVO toResult(WorkflowState state) {
        AppWorkflowResultVO result = new AppWorkflowResultVO();
        result.setAppId(state.value(WorkflowState.APP_ID, 0L));
        result.setPrompt(state.value(WorkflowState.PROMPT, ""));
        result.setEnhancedPrompt(state.value(WorkflowState.ENHANCED_PROMPT, ""));
        result.setRecommendedCodeGenType(CodeGenTypeEnum.getEnumByValue(state.value(WorkflowState.RECOMMENDED_CODE_GEN_TYPE, "")));
        result.setActualCodeGenType(CodeGenTypeEnum.getEnumByValue(state.value(WorkflowState.ACTUAL_CODE_GEN_TYPE, "")));
        result.setImageResources(state.value(WorkflowState.IMAGE_RESOURCES, List.of()));
        result.setOutputPath(state.value(WorkflowState.OUTPUT_PATH, ""));
        result.setProjectBuilt(state.value(WorkflowState.PROJECT_BUILT, false));
        result.setQualityPassed(state.value(WorkflowState.QUALITY_PASSED, false));
        result.setQualityMessage(state.value(WorkflowState.QUALITY_MESSAGE, ""));
        return result;
    }
}
