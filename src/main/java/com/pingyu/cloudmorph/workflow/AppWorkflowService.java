package com.pingyu.cloudmorph.workflow;

import com.pingyu.cloudmorph.model.entity.User;
import com.pingyu.cloudmorph.model.vo.AppWorkflowResultVO;

/**
 * 应用工作流服务。
 */
public interface AppWorkflowService {

    /**
     * 运行工作流。
     */
    AppWorkflowResultVO runWorkflow(Long appId, String prompt, String selectedElement, User loginUser);
}
