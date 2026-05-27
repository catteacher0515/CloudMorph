package com.pingyu.cloudmorph.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class DashboardShortcutVO implements Serializable {

    private String title;

    private String action;
}
