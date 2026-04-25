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
        Object result = doGenerate(userMessage);
        return doSave(result, appId);
    }

    /**
     * 使用指定 AI Service 生成并保存代码（用于对话记忆隔离）
     */
    public final File generateAndSave(String userMessage, Long appId, AiCodeGeneratorService service) {
        Object result = doGenerate(userMessage, service);
        return doSave(result, appId);
    }

    /**
     * 使用指定 AI Service 流式生成（用于对话记忆隔离）
     */
    public Flux<String> generateStream(String userMessage, AiCodeGeneratorService service) {
        return doGenerateStream(userMessage, service);
    }

    /**
     * 流式生成（子类实现，使用默认 service）
     */
    @Override
    public abstract Flux<String> generateStream(String userMessage);

    /**
     * 调 AI 生成代码（使用默认 service，子类实现）
     */
    protected abstract Object doGenerate(String userMessage);

    /**
     * 调 AI 生成代码（使用指定 service，子类实现）
     */
    protected abstract Object doGenerate(String userMessage, AiCodeGeneratorService service);

    /**
     * 流式生成（使用指定 service，子类实现）
     */
    protected abstract Flux<String> doGenerateStream(String userMessage, AiCodeGeneratorService service);

    /**
     * 保存代码到文件（子类实现）
     */
    protected abstract File doSave(Object result, Long appId);
}
