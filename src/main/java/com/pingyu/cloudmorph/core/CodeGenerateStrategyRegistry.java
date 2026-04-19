package com.pingyu.cloudmorph.core;

import com.pingyu.cloudmorph.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 代码生成策略注册器，管理所有策略的映射关系
 */
@Component
public class CodeGenerateStrategyRegistry {

    @Resource
    private HtmlCodeGenerateStrategy htmlCodeGenerateStrategy;

    @Resource
    private MultiFileCodeGenerateStrategy multiFileCodeGenerateStrategy;

    private final Map<CodeGenTypeEnum, CodeGenerateStrategy> strategyMap = new HashMap<>();

    @jakarta.annotation.PostConstruct
    public void init() {
        strategyMap.put(CodeGenTypeEnum.HTML, htmlCodeGenerateStrategy);
        strategyMap.put(CodeGenTypeEnum.MULTI_FILE, multiFileCodeGenerateStrategy);
    }

    /**
     * 根据生成类型获取对应策略
     */
    public CodeGenerateStrategy getStrategy(CodeGenTypeEnum codeGenTypeEnum) {
        return strategyMap.get(codeGenTypeEnum);
    }
}
