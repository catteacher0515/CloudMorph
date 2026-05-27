package com.pingyu.cloudmorph.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class DashboardTimelineVO implements Serializable {

    private String time;

    private String text;
}
