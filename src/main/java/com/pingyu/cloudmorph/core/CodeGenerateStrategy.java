package com.pingyu.cloudmorph.core;

import reactor.core.publisher.Flux;

import java.io.File;

/**
 * 代码生成策略接口
 */
public interface CodeGenerateStrategy {

    /**
     * 生成并保存代码
     *
     * @param userMessage 用户提示词
     * @return 保存的目录
     */
    File generateAndSave(String userMessage);

    /**
     * 流式生成代码
     *
     * @param userMessage 用户提示词
     * @return 流式数据
     */
    Flux<String> generateStream(String userMessage);
}
