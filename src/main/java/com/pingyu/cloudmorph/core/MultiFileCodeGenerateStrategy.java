package com.pingyu.cloudmorph.core;

import com.pingyu.cloudmorph.ai.AiCodeGeneratorService;
import com.pingyu.cloudmorph.ai.model.MultiFileCodeResult;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.File;

/**
 * 多文件模式代码生成策略
 */
@Component
public class MultiFileCodeGenerateStrategy extends CodeGenerateTemplate {

    @Override
    protected Object doGenerate(String userMessage) {
        return aiCodeGeneratorService.generateMultiFileCode(userMessage);
    }

    @Override
    protected Object doGenerate(String userMessage, AiCodeGeneratorService service) {
        return service.generateMultiFileCode(userMessage);
    }

    @Override
    protected Flux<String> doGenerateStream(String userMessage, AiCodeGeneratorService service) {
        return service.generateMultiFileCodeStream(userMessage);
    }

    @Override
    protected File doSave(Object result, Long appId) {
        return CodeFileSaver.saveMultiFileCodeResult((MultiFileCodeResult) result, appId);
    }

    @Override
    public Flux<String> generateStream(String userMessage) {
        return aiCodeGeneratorService.generateMultiFileCodeStream(userMessage);
    }
}
