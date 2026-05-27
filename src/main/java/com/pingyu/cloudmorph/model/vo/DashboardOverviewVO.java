package com.pingyu.cloudmorph.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 首页控制台聚合数据。
 */
@Data
public class DashboardOverviewVO implements Serializable {

    private DashboardStatVO stats;

    private List<AppVO> recentApps;

    private List<DashboardFeedVO> generationFeed;

    private List<DashboardWorkflowStepVO> workflowSteps;

    private List<DashboardTimelineVO> activityTimeline;

    private List<DashboardShortcutVO> quickActions;
}
