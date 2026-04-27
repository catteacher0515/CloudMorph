package com.pingyu.cloudmorph.service.impl;

import com.pingyu.cloudmorph.core.AiCodeGeneratorFacade;
import com.pingyu.cloudmorph.model.enums.CodeGenTypeEnum;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.io.FileUtil;
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
import com.pingyu.cloudmorph.service.ChatHistoryService;
import com.pingyu.cloudmorph.model.enums.ChatHistoryMessageTypeEnum;
import com.pingyu.cloudmorph.constant.AppConstant;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

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

    @Resource
    private ChatHistoryService chatHistoryService;

    @Resource
    private com.pingyu.cloudmorph.core.handler.StreamHandlerExecutor streamHandlerExecutor;

    @Resource
    private com.pingyu.cloudmorph.core.builder.VueProjectBuilder vueProjectBuilder;

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

    @Override
    public Flux<String> chatToGenCode(Long appId, String message, User loginUser) {
        // 1. 参数校验
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "应用 ID 不能为空");
        ThrowUtils.throwIf(StrUtil.isBlank(message), ErrorCode.PARAMS_ERROR, "用户消息不能为空");
        // 2. 查询应用信息
        App app = this.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        // 3. 仅本人可生成代码
        if (!app.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限访问该应用");
        }
        // 4. 获取代码生成类型
        CodeGenTypeEnum codeGenTypeEnum = CodeGenTypeEnum.getEnumByValue(app.getCodeGenType());
        ThrowUtils.throwIf(codeGenTypeEnum == null, ErrorCode.SYSTEM_ERROR, "不支持的代码生成类型");
        // 5. 保存用户消息到对话历史
        chatHistoryService.addChatMessage(appId, message, ChatHistoryMessageTypeEnum.USER.getValue(), loginUser.getId());
        // 6. 调用 AI 生成代码（流式）
        Flux<String> codeStream = aiCodeGeneratorFacade.generateAndSaveCodeStream(message, codeGenTypeEnum, appId);
        // 7. 根据生成类型调用对应的流处理器
        return streamHandlerExecutor.doExecute(codeStream, chatHistoryService, appId, loginUser, codeGenTypeEnum);
    }

    @Override
    public String deployApp(Long appId, User loginUser) {
        // 1. 参数校验
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "应用 ID 不能为空");
        // 2. 查询应用信息
        App app = this.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        // 3. 仅本人可部署
        if (!app.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限部署该应用");
        }
        // 4. 已有 deployKey 则复用，否则生成 6 位随机标识
        String deployKey = app.getDeployKey();
        if (StrUtil.isBlank(deployKey)) {
            deployKey = RandomUtil.randomString(6);
        }
        // 5. 构建源目录路径（code_output/{codeGenType}_{appId}）
        String codeGenType = app.getCodeGenType();
        String sourceDirPath = AppConstant.CODE_OUTPUT_ROOT_DIR + java.io.File.separator
                + codeGenType + "_" + appId;
        java.io.File sourceDir = new java.io.File(sourceDirPath);
        if (!sourceDir.exists() || !sourceDir.isDirectory()) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "应用代码不存在，请先生成代码");
        }
        // 6. Vue 项目特殊处理：执行构建，将 dist 目录作为部署源
        CodeGenTypeEnum codeGenTypeEnum = CodeGenTypeEnum.getEnumByValue(codeGenType);
        if (codeGenTypeEnum == CodeGenTypeEnum.VUE_PROJECT) {
            boolean buildSuccess = vueProjectBuilder.buildProject(sourceDirPath);
            ThrowUtils.throwIf(!buildSuccess, ErrorCode.SYSTEM_ERROR, "Vue 项目构建失败，请检查代码和依赖");
            java.io.File distDir = new java.io.File(sourceDirPath, "dist");
            ThrowUtils.throwIf(!distDir.exists(), ErrorCode.SYSTEM_ERROR, "构建完成但 dist 目录未生成");
            sourceDir = distDir;
        }
        // 7. 复制文件到部署目录（code_deploy/{deployKey}）
        String deployDirPath = AppConstant.CODE_DEPLOY_ROOT_DIR + java.io.File.separator + deployKey;
        try {
            FileUtil.copyContent(sourceDir, new java.io.File(deployDirPath), true);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "部署失败：" + e.getMessage());
        }
        // 7. 更新 deployKey 和部署时间
        App updateApp = new App();
        updateApp.setId(appId);
        updateApp.setDeployKey(deployKey);
        updateApp.setDeployedTime(java.time.LocalDateTime.now());
        boolean updated = this.updateById(updateApp);
        ThrowUtils.throwIf(!updated, ErrorCode.OPERATION_ERROR, "更新部署信息失败");
        // 8. 返回可访问的 URL
        return String.format("%s/%s/", AppConstant.CODE_DEPLOY_HOST, deployKey);
    }

    @Override
    public boolean removeById(java.io.Serializable id) {
        if (id == null) {
            return false;
        }
        Long appId = Long.valueOf(id.toString());
        if (appId <= 0) {
            return false;
        }
        try {
            chatHistoryService.deleteByAppId(appId);
        } catch (Exception e) {
            // 关联删除失败不阻止应用删除
        }
        return super.removeById(id);
    }
}
