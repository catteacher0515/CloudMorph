package com.pingyu.cloudmorph.service.impl;

import com.pingyu.cloudmorph.core.AiCodeGeneratorFacade;
import com.pingyu.cloudmorph.model.enums.CodeGenTypeEnum;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.pingyu.cloudmorph.exception.BusinessException;
import com.pingyu.cloudmorph.exception.ErrorCode;
import com.pingyu.cloudmorph.exception.ThrowUtils;
import com.pingyu.cloudmorph.mapper.AppMapper;
import com.pingyu.cloudmorph.model.dto.app.AppQueryRequest;
import com.pingyu.cloudmorph.model.entity.App;
import com.pingyu.cloudmorph.model.entity.User;
import com.pingyu.cloudmorph.model.vo.AppVO;
import com.pingyu.cloudmorph.service.AppService;
import com.pingyu.cloudmorph.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 应用 服务层实现。
 *
 * @author pingyu
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {

    @Resource
    private UserService userService;

    @Resource
    private AiCodeGeneratorFacade aiCodeGeneratorFacade;

    @Override
    public Long createApp(App app, HttpServletRequest request) {
        // 获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        app.setUserId(loginUser.getId());
        boolean result = this.save(app);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return app.getId();
    }

    @Override
    public AppVO getAppVO(App app) {
        if (app == null) {
            return null;
        }
        AppVO appVO = new AppVO();
        BeanUtils.copyProperties(app, appVO);
        return appVO;
    }

    @Override
    public Page<AppVO> getAppVOPage(Page<App> appPage) {
        List<AppVO> appVOList = appPage.getRecords()
                .stream()
                .map(this::getAppVO)
                .collect(Collectors.toList());
        Page<AppVO> appVOPage = new Page<>(appPage.getPageNumber(), appPage.getPageSize(), appPage.getTotalRow());
        appVOPage.setRecords(appVOList);
        return appVOPage;
    }

    @Override
    public QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest) {
        if (appQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = appQueryRequest.getId();
        String appName = appQueryRequest.getAppName();
        String codeGenType = appQueryRequest.getCodeGenType();
        String deployKey = appQueryRequest.getDeployKey();
        Integer priority = appQueryRequest.getPriority();
        Long userId = appQueryRequest.getUserId();
        String sortField = appQueryRequest.getSortField();
        String sortOrder = appQueryRequest.getSortOrder();

        QueryWrapper queryWrapper = QueryWrapper.create();
        if (ObjUtil.isNotNull(id)) {
            queryWrapper.eq("id", id);
        }
        if (StrUtil.isNotEmpty(appName)) {
            queryWrapper.like("appName", appName);
        }
        if (StrUtil.isNotEmpty(codeGenType)) {
            queryWrapper.eq("codeGenType", codeGenType);
        }
        if (StrUtil.isNotEmpty(deployKey)) {
            queryWrapper.eq("deployKey", deployKey);
        }
        if (ObjUtil.isNotNull(priority)) {
            queryWrapper.eq("priority", priority);
        }
        if (ObjUtil.isNotNull(userId)) {
            queryWrapper.eq("userId", userId);
        }
        if (StrUtil.isNotEmpty(sortField)) {
            queryWrapper.orderBy(sortField, "ascend".equals(sortOrder));
        }
        return queryWrapper;
    }

    @Override
    public void generateApp(Long appId, HttpServletRequest request) {
        // 1. 校验应用是否存在且属于当前用户
        App app = this.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);
        User loginUser = userService.getLoginUser(request);
        if (!app.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 2. 获取生成类型
        CodeGenTypeEnum codeGenTypeEnum = CodeGenTypeEnum.getEnumByValue(app.getCodeGenType());
        ThrowUtils.throwIf(codeGenTypeEnum == null, ErrorCode.PARAMS_ERROR, "不支持的代码生成类型");
        // 3. 调用 AI 生成并保存文件
        aiCodeGeneratorFacade.generateAndSaveCode(app.getInitPrompt(), codeGenTypeEnum, appId);
        // 4. 更新 deployKey 标记生成完成
        App updateApp = new App();
        updateApp.setId(appId);
        updateApp.setDeployKey(String.valueOf(appId));
        this.updateById(updateApp);
    }
}
