package com.cs.core.config.interactor.model.task;

import java.util.Map;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTypeModel;

public interface IGetTaskModel extends IConfigResponseWithAuditLogModel {
  
  public static final String TASK            = "task";
  public static final String REFERENCED_TAGS = "referencedTags";
  
  public ITaskModel getTask();
  
  public void setTask(ITaskModel task);
  
  public Map<String, IIdLabelTypeModel> getReferencedTags();
  
  public void setReferencedTags(Map<String, IIdLabelTypeModel> referencedTags);
}
