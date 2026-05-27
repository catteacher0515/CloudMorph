package com.pingyu.cloudmorph.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class DashboardStatVO implements Serializable {

    private String todayGenerated;

    private String deploySuccessRate;

    private String activeAppCount;

    private String pendingTaskCount;
}
