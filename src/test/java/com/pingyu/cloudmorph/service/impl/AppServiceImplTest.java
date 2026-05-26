package com.pingyu.cloudmorph.service.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppServiceImplTest {

    @Test
    void smartSelectCodeGenTypeShouldPreferVueProjectForVueLikePrompt() {
        AppServiceImpl appService = new AppServiceImpl();
        String result = appService.smartSelectCodeGenType("请生成一个 Vue3 项目，包含组件和路由");
        assertEquals("vue_project", result);
    }

    @Test
    void smartSelectCodeGenTypeShouldPreferMultiFileForSplitPrompt() {
        AppServiceImpl appService = new AppServiceImpl();
        String result = appService.smartSelectCodeGenType("请生成一个多文件的 html css js 页面");
        assertEquals("multi_file", result);
    }

    @Test
    void smartSelectCodeGenTypeShouldFallbackToHtml() {
        AppServiceImpl appService = new AppServiceImpl();
        String result = appService.smartSelectCodeGenType("做一个简单的落地页");
        assertEquals("html", result);
    }
}
