package com.cs.core.config.interactor.model.articleimportcomponent;

import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IComponentInstanceModel extends IModel {
  
  public static final String ID                  = "id";
  public static final String COMPONENT_ID        = "componentId";
  public static final String STATUS              = "status";
  public static final String START_TIME          = "startTime";
  public static final String END_TIME            = "endTime";
  public static final String TAGS                = "tags";
  public static final String SYSTEM_COMPONENT_ID = "systemComponentId";
  public static final String COMPONENT_LABEL     = "componentLabel";
  
  public String getId();
  
  public void setId(String id);
  
  public String getComponentId();
  
  public void setComponentId(String componentId);
  
  public IStatusModel getStatus();
  
  public void setStatus(IStatusModel status);
  
  public int getTaskStatus();
  
  public void setTaskStatus(int status);
  
  public List<? extends IContentTagInstance> getTags();
  
  public void setTags(List<? extends IContentTagInstance> tags);
  
  public Long getStartTime();
  
  public void setStartTime(Long startTime);
  
  public Long getEndTime();
  
  public void setEndTime(Long endTime);
  
  public String getSystemComponentId();
  
  public void setSystemComponentId(String systemComponentId);
  
  public String getComponentLabel();
  
  public void setComponentLabel(String componentLabel);

  public Long getJobID();

  public void setJobID(Long jobID);
}
