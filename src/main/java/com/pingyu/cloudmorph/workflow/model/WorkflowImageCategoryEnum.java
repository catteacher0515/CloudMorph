package com.pingyu.cloudmorph.workflow.model;

import lombok.Getter;

/**
 * 工作流素材分类。
 */
@Getter
public enum WorkflowImageCategoryEnum {

    COVER("封面图"),
    SELECTED_ELEMENT("选中元素"),
    OTHER("其他");

    private final String text;

    WorkflowImageCategoryEnum(String text) {
        this.text = text;
    }
}
