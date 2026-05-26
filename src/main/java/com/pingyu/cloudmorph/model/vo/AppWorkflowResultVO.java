package com.pingyu.cloudmorph.model.vo;

import com.pingyu.cloudmorph.model.enums.CodeGenTypeEnum;
import com.pingyu.cloudmorph.workflow.model.WorkflowImageResource;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 工作流执行结果。
 */
@Data
public class AppWorkflowResultVO implements Serializable {

    private Long appId;

    private String prompt;

    private String enhancedPrompt;

    private String recommendedCodeGenType;

    private String actualCodeGenType;

    private List<WorkflowImageResource> imageResources = new ArrayList<>();

    private String outputPath;

    private boolean projectBuilt;

    private boolean qualityPassed;

    private String qualityMessage;

    public void setRecommendedCodeGenType(CodeGenTypeEnum codeGenTypeEnum) {
        this.recommendedCodeGenType = codeGenTypeEnum == null ? null : codeGenTypeEnum.getValue();
    }

    public void setActualCodeGenType(CodeGenTypeEnum codeGenTypeEnum) {
        this.actualCodeGenType = codeGenTypeEnum == null ? null : codeGenTypeEnum.getValue();
    }
}
