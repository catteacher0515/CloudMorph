package com.pingyu.cloudmorph.controller;

import com.pingyu.cloudmorph.common.BaseResponse;
import com.pingyu.cloudmorph.common.ResultUtils;
import com.pingyu.cloudmorph.model.entity.User;
import com.pingyu.cloudmorph.model.vo.DashboardOverviewVO;
import com.pingyu.cloudmorph.service.DashboardService;
import com.pingyu.cloudmorph.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页控制台接口。
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Resource
    private DashboardService dashboardService;

    @Resource
    private UserService userService;

    @GetMapping("/overview")
    public BaseResponse<DashboardOverviewVO> getOverview(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(dashboardService.getOverview(loginUser));
    }
}
