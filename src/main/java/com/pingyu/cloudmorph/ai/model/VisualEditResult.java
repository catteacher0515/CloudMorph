package com.pingyu.cloudmorph.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

@Description("可视化修改返回结果")
@Data
public class VisualEditResult {

    @Description("修改后的 HTML 代码")
    private String htmlCode;

    @Description("修改后的 CSS 代码")
    private String cssCode;

    @Description("修改后的 JS 代码")
    private String jsCode;

    @Description("修改说明")
    private String description;
}
