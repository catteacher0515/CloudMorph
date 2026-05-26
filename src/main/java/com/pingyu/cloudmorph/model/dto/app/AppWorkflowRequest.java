package com.pingyu.cloudmorph.model.dto.app;

import lombok.Data;

import java.io.Serializable;

/**
 * 工作流执行请求。
 */
@Data
public class AppWorkflowRequest implements Serializable {

    /**
     * 应用 ID。
     */
    private Long appId;

    /**
     * 用户需求描述。
     */
    private String prompt;

    /**
     * 前端选中的元素信息。
     */
    private String selectedElement;

    private static final long serialVersionUID = 1L;
}
