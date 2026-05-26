package com.pingyu.cloudmorph.model.dto.app;

import lombok.Data;

import java.io.Serializable;

@Data
public class AppVisualEditRequest implements Serializable {

    /**
     * 应用 ID。
     */
    private Long appId;

    /**
     * 用户修改需求。
     */
    private String prompt;

    /**
     * 前端选中的元素信息，JSON 字符串透传给后端即可。
     */
    private String selectedElement;

    private static final long serialVersionUID = 1L;
}
