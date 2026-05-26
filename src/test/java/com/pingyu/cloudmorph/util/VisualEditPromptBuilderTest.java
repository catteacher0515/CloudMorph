package com.pingyu.cloudmorph.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class VisualEditPromptBuilderTest {

    @Test
    void buildShouldContainPromptAndSelectedElement() {
        String result = VisualEditPromptBuilder.build("修改标题颜色", "{\"tagName\":\"h1\"}", "html");
        assertTrue(result.contains("修改标题颜色"));
        assertTrue(result.contains("h1"));
        assertTrue(result.contains("html"));
    }
}
