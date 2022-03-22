package com.cs.core.config.interactor.model.objectCount;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetConfigEntityResponseModel implements IGetConfigEntityResponseModel{
  
  
  private static final long serialVersionUID = 1L;
  public List<IGetEntityCountModel> dataModel = new ArrayList<>();

  @Override 
  public List<IGetEntityCountModel> getDataModel()
  {
    return dataModel;
  }

  @Override
  @JsonDeserialize(contentAs = GetEntityCountModel.class)
  public void setDataModel(List<IGetEntityCountModel> dataModel)
  {
    this.dataModel = dataModel;
  }
  
}
