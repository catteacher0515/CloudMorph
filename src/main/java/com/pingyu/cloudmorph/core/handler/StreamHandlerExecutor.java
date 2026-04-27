package com.pingyu.cloudmorph.core.handler;

import com.pingyu.cloudmorph.model.entity.User;
import com.pingyu.cloudmorph.model.enums.CodeGenTypeEnum;
import com.pingyu.cloudmorph.service.ChatHistoryService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Slf4j
@Component
public class StreamHandlerExecutor {

    @Resource
    private JsonMessageStreamHandler jsonMessageStreamHandler;

    public Flux<String> doExecute(Flux<String> originFlux,
                                  ChatHistoryService chatHistoryService,
                                  long appId, User loginUser, CodeGenTypeEnum codeGenType) {
        return switch (codeGenType) {
            case VUE_PROJECT -> jsonMessageStreamHandler.handle(originFlux, chatHistoryService, appId, loginUser);
            case HTML, MULTI_FILE -> new SimpleTextStreamHandler().handle(originFlux, chatHistoryService, appId, loginUser);
        };
    }
}
