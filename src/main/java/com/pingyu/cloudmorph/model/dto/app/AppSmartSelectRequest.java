package com.pingyu.cloudmorph.model.dto.app;

import lombok.Data;

import java.io.Serializable;

@Data
public class AppSmartSelectRequest implements Serializable {

    /**
     * 用户输入的需求描述。
     */
    private String prompt;

    private static final long serialVersionUID = 1L;
}
