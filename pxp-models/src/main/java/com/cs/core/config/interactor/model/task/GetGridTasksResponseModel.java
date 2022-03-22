package com.cs.core.config.interactor.model.task;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetGridTasksResponseModel implements IGetGridTasksResponseModel {
  
  private static final long                            serialVersionUID = 1L;
  
  protected List<ISaveTaskResponseModel>               tasksList;
  protected Long                                       count;
  protected Map<String, IConfigEntityInformationModel> referncedTags;
  
  @Override
  public List<ISaveTaskResponseModel> getTasksList()
  {
    if (tasksList == null) {
      tasksList = new ArrayList<>();
    }
    return tasksList;
  }
  
  @JsonDeserialize(contentAs = SaveTaskResponseModel.class)
  @Override
  public void setTasksList(List<ISaveTaskResponseModel> taskList)
  {
    this.tasksList = taskList;
  }
  
  @Override
  public Long getCount()
  {
    if (count == null) {
      count = new Long(0);
    }
    return count;
  }
  
  @Override
  public void setCount(Long count)
  {
    this.count = count;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedTags()
  {
    return referncedTags;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  @Override
  public void setReferencedTags(Map<String, IConfigEntityInformationModel> referncedTags)
  {
    this.referncedTags = referncedTags;
  }
}
