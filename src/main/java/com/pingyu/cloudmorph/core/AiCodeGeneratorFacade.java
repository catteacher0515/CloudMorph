package com.pingyu.cloudmorph.core;

import com.pingyu.cloudmorph.exception.BusinessException;
import com.pingyu.cloudmorph.exception.ErrorCode;
import com.pingyu.cloudmorph.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;

/**
 * AI 代码生成外观类，组合生成和保存功能
 */
@Service
public class AiCodeGeneratorFacade {

    @Resource
    private CodeGenerateStrategyRegistry strategyRegistry;

    /**
     * 统一入口：根据类型生成并保存代码
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 生成类型
     * @return 保存的目录
     */
    public File generateAndSaveCode(String userMessage, CodeGenTypeEnum codeGenTypeEnum, Long appId) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }
        CodeGenerateStrategy strategy = strategyRegistry.getStrategy(codeGenTypeEnum);
        if (strategy == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的生成类型：" + codeGenTypeEnum.getValue());
        }
        return strategy.generateAndSave(userMessage, appId);
    }

    /**
     * 统一入口：根据类型流式生成代码
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 生成类型
     * @return 流式数据
     */
    public Flux<String> generateCodeStream(String userMessage, CodeGenTypeEnum codeGenTypeEnum) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }
        CodeGenerateStrategy strategy = strategyRegistry.getStrategy(codeGenTypeEnum);
        if (strategy == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的生成类型：" + codeGenTypeEnum.getValue());
        }
        return strategy.generateStream(userMessage);
    }
}
