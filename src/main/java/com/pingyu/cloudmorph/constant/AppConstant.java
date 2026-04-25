package com.pingyu.cloudmorph.constant;

public interface AppConstant {

    /**
     * 精选应用优先级
     */
    int FEATURED_PRIORITY = 99;

    /**
     * 用户分页查询每页最大数量
     */
    int USER_PAGE_SIZE_LIMIT = 20;

    /**
     * 应用生成目录（临时）
     */
    String CODE_OUTPUT_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_output";

    /**
     * 应用部署目录
     */
    String CODE_DEPLOY_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_deploy";

    /**
     * 应用部署域名
     */
    String CODE_DEPLOY_HOST = "http://localhost";
}
