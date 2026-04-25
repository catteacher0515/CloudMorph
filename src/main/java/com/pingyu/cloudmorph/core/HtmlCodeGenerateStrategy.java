package com.pingyu.cloudmorph.core;

import com.pingyu.cloudmorph.ai.AiCodeGeneratorService;
import com.pingyu.cloudmorph.ai.model.HtmlCodeResult;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.File;

/**
 * HTML 单文件模式代码生成策略
 */
@Component
public class HtmlCodeGenerateStrategy extends CodeGenerateTemplate {

    @Override
    protected Object doGenerate(String userMessage) {
        return aiCodeGeneratorService.generateHtmlCode(userMessage);
    }

    @Override
    protected Object doGenerate(String userMessage, AiCodeGeneratorService service) {
        return service.generateHtmlCode(userMessage);
    }

    @Override
    protected Flux<String> doGenerateStream(String userMessage, AiCodeGeneratorService service) {
        return service.generateHtmlCodeStream(userMessage);
    }

    @Override
    protected File doSave(Object result, Long appId) {
        return CodeFileSaver.saveHtmlCodeResult((HtmlCodeResult) result, appId);
    }

    @Override
    public Flux<String> generateStream(String userMessage) {
        return aiCodeGeneratorService.generateHtmlCodeStream(userMessage);
    }
}
