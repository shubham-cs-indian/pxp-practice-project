package com.cs.core.runtime.interactor.model.taskinstance;

import java.util.List;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;

public interface IBulkDeleteTaskInstanceReturnModel extends IBulkResponseModel, IConfigResponseWithAuditLogModel {
  
  public static String IDS        = "ids";
  public static String TASK_COUNT = "taskCount";
  
  public void setTaskCount(long taskCount);
  
  public void setSuccess(List<String> ids);
  
}
