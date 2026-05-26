package com.pingyu.cloudmorph.model.dto.app;

import lombok.Data;

import java.io.Serializable;

@Data
public class AppCoverUpdateRequest implements Serializable {

    /**
     * 应用 ID。
     */
    private Long appId;

    /**
     * 封面图片 URL。
     */
    private String cover;

    private static final long serialVersionUID = 1L;
}
