package com.pingyu.cloudmorph.model.dto.app;

import lombok.Data;

import java.io.Serializable;

@Data
public class AppDownloadRequest implements Serializable {

    /**
     * 应用 ID。
     */
    private Long appId;

    private static final long serialVersionUID = 1L;
}
