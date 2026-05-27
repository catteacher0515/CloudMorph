package com.pingyu.cloudmorph.service;

import com.pingyu.cloudmorph.model.entity.User;
import com.pingyu.cloudmorph.model.vo.DashboardOverviewVO;

/**
 * 首页控制台数据服务。
 */
public interface DashboardService {

    /**
     * 获取首页控制台聚合数据。
     */
    DashboardOverviewVO getOverview(User loginUser);
}
