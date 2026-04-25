package com.pingyu.cloudmorph.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.pingyu.cloudmorph.model.dto.chathistory.ChatHistoryQueryRequest;
import com.pingyu.cloudmorph.model.entity.ChatHistory;
import com.pingyu.cloudmorph.model.entity.User;

import java.time.LocalDateTime;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;

public interface ChatHistoryService extends IService<ChatHistory> {

    boolean addChatMessage(Long appId, String message, String messageType, Long userId);

    boolean deleteByAppId(Long appId);

    Page<ChatHistory> listAppChatHistoryByPage(Long appId, int pageSize, LocalDateTime lastCreateTime, User loginUser);

    QueryWrapper getQueryWrapper(ChatHistoryQueryRequest chatHistoryQueryRequest);

    /**
     * 从数据库加载历史对话到 Redis 记忆中（懒加载，创建 AI Service 时调用）
     */
    int loadChatHistoryToMemory(Long appId, MessageWindowChatMemory chatMemory, int maxCount);
}
