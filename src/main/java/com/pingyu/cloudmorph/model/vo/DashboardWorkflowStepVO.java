package com.pingyu.cloudmorph.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class DashboardWorkflowStepVO implements Serializable {

    private String label;

    private Integer value;
}
