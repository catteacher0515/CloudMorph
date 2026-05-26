package com.pingyu.cloudmorph.controller;

import com.pingyu.cloudmorph.annotation.AuthCheck;
import com.pingyu.cloudmorph.common.BaseResponse;
import com.pingyu.cloudmorph.common.ResultUtils;
import com.pingyu.cloudmorph.constant.UserConstant;
import com.pingyu.cloudmorph.exception.ErrorCode;
import com.pingyu.cloudmorph.exception.ThrowUtils;
import com.pingyu.cloudmorph.model.dto.app.AppWorkflowRequest;
import com.pingyu.cloudmorph.model.entity.User;
import com.pingyu.cloudmorph.model.vo.AppWorkflowResultVO;
import com.pingyu.cloudmorph.service.UserService;
import com.pingyu.cloudmorph.workflow.AppWorkflowService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 应用工作流接口。
 */
@RestController
@RequestMapping("/app/workflow")
public class AppWorkflowController {

    @Resource
    private AppWorkflowService appWorkflowService;

    @Resource
    private UserService userService;

    @PostMapping("/run")
    public BaseResponse<AppWorkflowResultVO> runWorkflow(@RequestBody AppWorkflowRequest requestBody,
                                                         HttpServletRequest request) {
        ThrowUtils.throwIf(requestBody == null || requestBody.getAppId() == null, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        AppWorkflowResultVO result = appWorkflowService.runWorkflow(
                requestBody.getAppId(),
                requestBody.getPrompt(),
                requestBody.getSelectedElement(),
                loginUser
        );
        return ResultUtils.success(result);
    }
}
