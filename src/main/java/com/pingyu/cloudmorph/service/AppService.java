package com.pingyu.cloudmorph.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.pingyu.cloudmorph.model.dto.app.AppQueryRequest;
import com.pingyu.cloudmorph.model.entity.App;
import com.pingyu.cloudmorph.model.vo.AppVO;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 应用 服务层。
 *
 * @author pingyu
 */
public interface AppService extends IService<App> {

    /**
     * 创建应用
     */
    Long createApp(App app, HttpServletRequest request);

    /**
     * 获取应用 VO
     */
    AppVO getAppVO(App app);

    /**
     * 分页获取应用 VO
     */
    Page<AppVO> getAppVOPage(Page<App> appPage);

    /**
     * 构建查询条件
     */
    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

    /**
     * 生成应用代码
     */
    void generateApp(Long appId, HttpServletRequest request);
}
