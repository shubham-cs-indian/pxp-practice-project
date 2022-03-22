package com.cs.core.config.interactor.model.task;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IGetGridTasksResponseModel extends IModel {
  
  public static final String TASKS_LIST      = "tasksList";
  public static final String COUNT           = "count";
  public static final String REFERENCED_TAGS = "referencedTags";
  
  public List<ISaveTaskResponseModel> getTasksList();
  
  public void setTasksList(List<ISaveTaskResponseModel> taskList);
  
  public Long getCount();
  
  public void setCount(Long count);
  
  public Map<String, IConfigEntityInformationModel> getReferencedTags();
  
  public void setReferencedTags(Map<String, IConfigEntityInformationModel> referencedTags);
}
