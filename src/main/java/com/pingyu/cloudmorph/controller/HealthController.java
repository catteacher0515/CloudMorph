package com.pingyu.cloudmorph.controller;

import com.pingyu.cloudmorph.common.BaseResponse;
import com.pingyu.cloudmorph.common.ResultUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "系统服务", description = "包含健康检查等基础接口")
@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping("/")
    public BaseResponse<String> healthCheck() {
        return ResultUtils.success("ok");
    }
}
