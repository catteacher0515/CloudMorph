package com.pingyu.cloudmorph.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 工作流中的图片素材。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowImageResource implements Serializable {

    private String category;

    private String name;

    private String url;

    public static WorkflowImageResource of(WorkflowImageCategoryEnum categoryEnum, String name, String url) {
        return new WorkflowImageResource(categoryEnum.getText(), name, url);
    }
}
