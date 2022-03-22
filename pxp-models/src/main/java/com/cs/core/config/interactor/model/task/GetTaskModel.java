package com.cs.core.config.interactor.model.task;

import java.util.Map;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTypeModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IdLabelTypeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetTaskModel extends ConfigResponseWithAuditLogModel implements IGetTaskModel {
  
  private static final long                serialVersionUID = 1L;
  protected ITaskModel                     task;
  protected Map<String, IIdLabelTypeModel> referencedTags;
  
  @Override
  public ITaskModel getTask()
  {
    return task;
  }
  
  @Override
  @JsonDeserialize(as = TaskModel.class)
  public void setTask(ITaskModel task)
  {
    this.task = task;
  }
  
  @Override
  public Map<String, IIdLabelTypeModel> getReferencedTags()
  {
    return referencedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelTypeModel.class)
  public void setReferencedTags(Map<String, IIdLabelTypeModel> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
}
