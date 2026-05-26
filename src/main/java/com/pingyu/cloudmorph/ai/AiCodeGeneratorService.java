package com.pingyu.cloudmorph.ai;

import com.pingyu.cloudmorph.ai.model.HtmlCodeResult;
import com.pingyu.cloudmorph.ai.model.MultiFileCodeResult;
import com.pingyu.cloudmorph.ai.model.VisualEditResult;
import dev.langchain4j.service.SystemMessage;
import reactor.core.publisher.Flux;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;

public interface AiCodeGeneratorService {

    @SystemMessage(fromResource = "prompt/codegen-html-system-prompt.txt")
    HtmlCodeResult generateHtmlCode(String userMessage);

    @SystemMessage(fromResource = "prompt/codegen-multi-file-system-prompt.txt")
    MultiFileCodeResult generateMultiFileCode(String userMessage);

    @SystemMessage(fromResource = "prompt/codegen-html-system-prompt.txt")
    Flux<String> generateHtmlCodeStream(String userMessage);

    @SystemMessage(fromResource = "prompt/codegen-multi-file-system-prompt.txt")
    Flux<String> generateMultiFileCodeStream(String userMessage);

    @SystemMessage(fromResource = "prompt/codegen-vue-project-system-prompt.txt")
    Flux<String> generateVueProjectCodeStream(@MemoryId long appId, @UserMessage String userMessage);

    @SystemMessage(fromResource = "prompt/codegen-vue-project-system-prompt.txt")
    VisualEditResult editVueProjectCode(@MemoryId long appId, @UserMessage String userMessage);

    @SystemMessage(fromResource = "prompt/codegen-vue-project-visual-edit-system-prompt.txt")
    Flux<String> editVueProjectCodeStream(@MemoryId long appId, @UserMessage String userMessage);
}
