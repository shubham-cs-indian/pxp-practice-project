package com.cs.core.config.interactor.model.articleimportcomponent;

import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ComponentInstanceModel implements IComponentInstanceModel {
  
  private static final long                     serialVersionUID = 1L;
  protected String                              id;
  protected String                              componentId;
  protected Long                                startTime;
  protected Long                                endTime;
  protected List<? extends IContentTagInstance> tags;
  protected IStatusModel                        status;
  protected String                              systemComponentId;
  protected String                              componentLabel;
  protected Long                                jobID;
  protected int                                 taskStatus;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getComponentId()
  {
    return componentId;
  }
  
  @Override
  public void setComponentId(String componentId)
  {
    this.componentId = componentId;
  }
  
  @Override
  @JsonDeserialize(as = StatusModel.class)
  public IStatusModel getStatus()
  {
    return status;
  }
  
  @Override
  public void setStatus(IStatusModel status)
  {
    this.status = status;
  }
  
  public Long getStartTime()
  {
    return startTime;
  }
  
  public void setStartTime(Long startTime)
  {
    this.startTime = startTime;
  }
  
  public Long getEndTime()
  {
    return endTime;
  }
  
  public void setEndTime(Long endTime)
  {
    this.endTime = endTime;
  }
  
  @Override
  public List<? extends IContentTagInstance> getTags()
  {
    if (tags == null) {
      tags = new ArrayList<>();
    }
    return tags;
  }
  
  @Override
  public void setTags(List<? extends IContentTagInstance> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public String getSystemComponentId()
  {
    return systemComponentId;
  }
  
  @Override
  public void setSystemComponentId(String systemComponentId)
  {
    this.systemComponentId = systemComponentId;
  }
  
  @Override
  public String getComponentLabel()
  {
    return componentLabel;
  }
  
  @Override
  public void setComponentLabel(String componentLabel)
  {
    this.componentLabel = componentLabel;
  }
  
  @Override
  public Long getJobID()
  {
    return jobID;
  }
  
  @Override
  public void setJobID(Long jobID)
  {
    this.jobID = jobID;
    
  }

  @Override
  public int getTaskStatus()
  {
    return taskStatus;
  }

  @Override
  public void setTaskStatus(int taskStatus)
  {
    this.taskStatus = taskStatus;
    
  }
}
