package com.pingyu.cloudmorph.core;

import cn.hutool.json.JSONUtil;
import com.pingyu.cloudmorph.ai.AiCodeGeneratorService;
import com.pingyu.cloudmorph.ai.model.message.AiResponseMessage;
import com.pingyu.cloudmorph.ai.model.message.ToolExecutedMessage;
import com.pingyu.cloudmorph.ai.model.message.ToolRequestMessage;
import com.pingyu.cloudmorph.config.AiCodeGeneratorServiceFactory;
import com.pingyu.cloudmorph.exception.BusinessException;
import com.pingyu.cloudmorph.exception.ErrorCode;
import com.pingyu.cloudmorph.model.enums.CodeGenTypeEnum;
import dev.langchain4j.service.TokenStream;
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
        AiCodeGeneratorService service = aiCodeGeneratorServiceFactory.getAiCodeGeneratorService(appId, codeGenTypeEnum);
        return strategy.generateAndSave(userMessage, appId, service);
    }

    /**
     * 统一入口：流式生成代码，完成后保存文件
     */
    public Flux<String> generateAndSaveCodeStream(String userMessage, CodeGenTypeEnum codeGenTypeEnum, Long appId) {
        AiCodeGeneratorService service = aiCodeGeneratorServiceFactory.getAiCodeGeneratorService(appId, codeGenTypeEnum);
        return switch (codeGenTypeEnum) {
            case VUE_PROJECT -> {
                // Vue 工程模式：使用 TokenStream 获取工具调用流式输出
                TokenStream tokenStream = (TokenStream) service.generateVueProjectCodeStream(appId, userMessage);
                yield processTokenStream(tokenStream);
            }
            case HTML, MULTI_FILE -> {
                // 原生模式：使用 Flux<String> 流式输出，完成后保存文件
                CodeGenerateTemplate strategy = getTemplate(codeGenTypeEnum);
                yield strategy.generateStream(userMessage, service)
                        .doOnComplete(() -> {
                            try {
                                strategy.generateAndSave(userMessage, appId, service);
                            } catch (Exception e) {
                                log.error("流式生成完成后保存文件失败，appId={}，原因：{}", appId, e.getMessage());
                            }
                        });
            }
        };
    }

    /**
     * 将 TokenStream 适配为 Flux<String>，统一封装为 JSON 消息格式
     */
    private Flux<String> processTokenStream(TokenStream tokenStream) {
        return Flux.create(sink -> tokenStream
                .onPartialResponse(partialResponse -> {
                    AiResponseMessage msg = new AiResponseMessage(partialResponse);
                    sink.next(JSONUtil.toJsonStr(msg));
                })
                .onToolExecuted(toolExecution -> {
                    ToolExecutedMessage msg = new ToolExecutedMessage(toolExecution);
                    sink.next(JSONUtil.toJsonStr(msg));
                })
                .onCompleteResponse(response -> sink.complete())
                .onError(error -> {
                    log.error("TokenStream 处理异常: {}", error.getMessage(), error);
                    sink.error(error);
                })
                .start()
        );
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
