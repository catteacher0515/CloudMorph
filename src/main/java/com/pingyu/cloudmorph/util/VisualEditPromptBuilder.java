package com.pingyu.cloudmorph.util;

import cn.hutool.core.util.StrUtil;

/**
 * 可视化修改提示词拼接器。
 */
public final class VisualEditPromptBuilder {

    private VisualEditPromptBuilder() {
    }

    public static String build(String userPrompt, String selectedElement, String codeGenType) {
        StringBuilder builder = new StringBuilder();
        builder.append("你是一位资深前端开发，请根据用户需求修改现有项目代码。").append("\n\n");
        builder.append("约束：").append("\n");
        builder.append("1. 必须保持现有项目可运行。").append("\n");
        builder.append("2. 优先最小化修改，不要重写无关部分。").append("\n");
        builder.append("3. 只输出最终需要写回文件的代码结果。").append("\n\n");
        builder.append("代码生成类型：").append(codeGenType).append("\n\n");
        if (StrUtil.isNotBlank(selectedElement)) {
            builder.append("用户选中的元素信息：").append("\n");
            builder.append(selectedElement).append("\n\n");
        }
        builder.append("用户修改需求：").append("\n");
        builder.append(userPrompt).append("\n");
        return builder.toString();
    }
}
