package com.pingyu.cloudmorph.model.dto.app;

import com.pingyu.cloudmorph.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class AppQueryRequest extends PageRequest implements Serializable {

    private Long id;

    private String appName;

    private String codeGenType;

    private String deployKey;

    private Integer priority;

    private Long userId;

    private static final long serialVersionUID = 1L;
}
