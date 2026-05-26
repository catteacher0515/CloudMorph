package com.pingyu.cloudmorph.core.builder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Vue 项目构建结果。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VueProjectBuildResult implements Serializable {

    /**
     * 是否构建成功。
     */
    private boolean success;

    /**
     * 失败阶段：project_dir/package_json/npm_install/npm_build/dist_missing。
     */
    private String failedStage;

    /**
     * 错误摘要。
     */
    private String errorMessage;

    /**
     * 构建目录。
     */
    private String projectPath;

    public static VueProjectBuildResult success(String projectPath) {
        return new VueProjectBuildResult(true, null, null, projectPath);
    }

    public static VueProjectBuildResult failure(String failedStage, String errorMessage, String projectPath) {
        return new VueProjectBuildResult(false, failedStage, errorMessage, projectPath);
    }
}
