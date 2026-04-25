package com.pingyu.cloudmorph.core;

import com.pingyu.cloudmorph.ai.AiCodeGeneratorService;
import com.pingyu.cloudmorph.config.AiCodeGeneratorServiceFactory;
import com.pingyu.cloudmorph.exception.BusinessException;
import com.pingyu.cloudmorph.exception.ErrorCode;
import com.pingyu.cloudmorph.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;

/**
 * AI 代码生成外观类，组合生成和保存功能
 */
@Slf4j
@Service
public class AiCodeGeneratorFacade {

    @Resource
    private CodeGenerateStrategyRegistry strategyRegistry;

    @Resource
    private AiCodeGeneratorServiceFactory aiCodeGeneratorServiceFactory;

    /**
     * 统一入口：根据类型生成并保存代码
     */
    public File generateAndSaveCode(String userMessage, CodeGenTypeEnum codeGenTypeEnum, Long appId) {
        CodeGenerateTemplate strategy = getTemplate(codeGenTypeEnum);
        AiCodeGeneratorService service = aiCodeGeneratorServiceFactory.getAiCodeGeneratorService(appId);
        return strategy.generateAndSave(userMessage, appId, service);
    }

    /**
     * 统一入口：流式生成代码，完成后保存文件
     */
    public Flux<String> generateAndSaveCodeStream(String userMessage, CodeGenTypeEnum codeGenTypeEnum, Long appId) {
        CodeGenerateTemplate strategy = getTemplate(codeGenTypeEnum);
        AiCodeGeneratorService service = aiCodeGeneratorServiceFactory.getAiCodeGeneratorService(appId);
        return strategy.generateStream(userMessage, service)
                .doOnComplete(() -> {
                    try {
                        strategy.generateAndSave(userMessage, appId, service);
                    } catch (Exception e) {
                        log.error("流式生成完成后保存文件失败，appId={}，原因：{}", appId, e.getMessage());
                    }
                });
    }

    private CodeGenerateTemplate getTemplate(CodeGenTypeEnum codeGenTypeEnum) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }
        CodeGenerateStrategy strategy = strategyRegistry.getStrategy(codeGenTypeEnum);
        if (strategy == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的生成类型：" + codeGenTypeEnum.getValue());
        }
        if (!(strategy instanceof CodeGenerateTemplate)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "策略类型不支持对话记忆");
        }
        return (CodeGenerateTemplate) strategy;
    }
}
