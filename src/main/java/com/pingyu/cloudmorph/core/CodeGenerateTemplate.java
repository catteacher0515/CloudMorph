package com.pingyu.cloudmorph.core;

import com.pingyu.cloudmorph.ai.AiCodeGeneratorService;
import jakarta.annotation.Resource;
import reactor.core.publisher.Flux;

import java.io.File;

/**
 * 代码生成模板抽象类，定义生成和保存的骨架流程
 */
public abstract class CodeGenerateTemplate implements CodeGenerateStrategy {

    @Resource
    protected AiCodeGeneratorService aiCodeGeneratorService;

    /**
     * 模板方法：生成并保存代码（骨架固定，细节由子类实现）
     */
    @Override
    public final File generateAndSave(String userMessage, Long appId) {
        // 第一步：调 AI 生成
        Object result = doGenerate(userMessage);
        // 第二步：保存文件
        return doSave(result, appId);
    }

    /**
     * 流式生成（子类实现）
     */
    @Override
    public abstract Flux<String> generateStream(String userMessage);

    /**
     * 调 AI 生成代码（子类实现）
     */
    protected abstract Object doGenerate(String userMessage);

    /**
     * 保存代码到文件（子类实现）
     */
    protected abstract File doSave(Object result, Long appId);
}
