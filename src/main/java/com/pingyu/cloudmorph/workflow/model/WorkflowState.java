package com.pingyu.cloudmorph.workflow.model;

import org.bsc.langgraph4j.state.AgentState;

import java.util.Map;

/**
 * 工作流状态。
 */
public class WorkflowState extends AgentState {

    public static final String APP_ID = "appId";
    public static final String PROMPT = "prompt";
    public static final String SELECTED_ELEMENT = "selectedElement";
    public static final String IMAGE_RESOURCES = "imageResources";
    public static final String ENHANCED_PROMPT = "enhancedPrompt";
    public static final String RECOMMENDED_CODE_GEN_TYPE = "recommendedCodeGenType";
    public static final String ACTUAL_CODE_GEN_TYPE = "actualCodeGenType";
    public static final String OUTPUT_PATH = "outputPath";
    public static final String PROJECT_BUILT = "projectBuilt";
    public static final String QUALITY_PASSED = "qualityPassed";
    public static final String QUALITY_MESSAGE = "qualityMessage";

    public WorkflowState(Map<String, Object> data) {
        super(data);
    }
}
