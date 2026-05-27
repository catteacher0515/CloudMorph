package com.pingyu.cloudmorph.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.pingyu.cloudmorph.constant.AppConstant;
import com.pingyu.cloudmorph.model.entity.App;
import com.pingyu.cloudmorph.model.entity.User;
import com.pingyu.cloudmorph.model.enums.CodeGenTypeEnum;
import com.pingyu.cloudmorph.model.vo.*;
import com.pingyu.cloudmorph.service.AppService;
import com.pingyu.cloudmorph.service.DashboardService;
import com.pingyu.cloudmorph.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Resource
    private AppService appService;

    @Resource
    private UserService userService;

    @Override
    public DashboardOverviewVO getOverview(User loginUser) {
        DashboardOverviewVO overview = new DashboardOverviewVO();
        overview.setStats(buildStats());
        overview.setRecentApps(loadRecentApps(loginUser));
        overview.setGenerationFeed(buildFeed());
        overview.setWorkflowSteps(buildWorkflowSteps());
        overview.setActivityTimeline(buildTimeline());
        overview.setQuickActions(buildShortcuts());
        return overview;
    }

    private DashboardStatVO buildStats() {
        DashboardStatVO stats = new DashboardStatVO();
        stats.setTodayGenerated("18");
        stats.setDeploySuccessRate("96%");
        stats.setActiveAppCount("24");
        stats.setPendingTaskCount("7");
        return stats;
    }

    private List<AppVO> loadRecentApps(User loginUser) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .eq("userId", loginUser.getId())
                .orderBy("updateTime", false);
        Page<App> appPage = appService.page(Page.of(1, 3), queryWrapper);
        return appPage.getRecords().stream()
                .map(appService::getAppVO)
                .toList();
    }

    private List<DashboardFeedVO> buildFeed() {
        DashboardFeedVO first = new DashboardFeedVO();
        first.setTitle("一键生成注册页");
        first.setMeta(CodeGenTypeEnum.VUE_PROJECT.getValue() + " · 100% 完成");
        DashboardFeedVO second = new DashboardFeedVO();
        second.setTitle("优化按钮层级与动效");
        second.setMeta("VISUAL_EDIT · 进行中");
        DashboardFeedVO third = new DashboardFeedVO();
        third.setTitle("修复构建失败问题");
        third.setMeta("BUILD_FIX · 已完成");
        return List.of(first, second, third);
    }

    private List<DashboardWorkflowStepVO> buildWorkflowSteps() {
        return List.of(step("素材收集", 100),
                step("提示词增强", 92),
                step("智能路由", 100),
                step("代码生成", 86),
                step("项目构建", 74),
                step("构建修复", 58));
    }

    private List<DashboardTimelineVO> buildTimeline() {
        return List.of(timeline("09:42", "创建了「营销落地页」应用"),
                timeline("10:15", "生成了一个 Vue 工程并完成构建"),
                timeline("11:03", "修复了一个构建失败问题"),
                timeline("11:28", "部署了最近一次生成结果"));
    }

    private List<DashboardShortcutVO> buildShortcuts() {
        return List.of(shortcut("创建应用", "create"),
                shortcut("进入工作流", "workflow"),
                shortcut("查看生成历史", "history"),
                shortcut("管理部署记录", "deploy"));
    }

    private DashboardWorkflowStepVO step(String label, int value) {
        DashboardWorkflowStepVO vo = new DashboardWorkflowStepVO();
        vo.setLabel(label);
        vo.setValue(value);
        return vo;
    }

    private DashboardTimelineVO timeline(String time, String text) {
        DashboardTimelineVO vo = new DashboardTimelineVO();
        vo.setTime(time);
        vo.setText(text);
        return vo;
    }

    private DashboardShortcutVO shortcut(String title, String action) {
        DashboardShortcutVO vo = new DashboardShortcutVO();
        vo.setTitle(title);
        vo.setAction(action);
        return vo;
    }
}
