package com.pingyu.cloudmorph.model.dto.chathistory;

import com.pingyu.cloudmorph.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChatHistoryQueryRequest extends PageRequest implements Serializable {

    private Long id;

    private String message;

    private String messageType;

    private Long appId;

    private Long userId;

    /**
     * 游标查询 - 最后一条记录的创建时间
     */
    private LocalDateTime lastCreateTime;

    private static final long serialVersionUID = 1L;
}
