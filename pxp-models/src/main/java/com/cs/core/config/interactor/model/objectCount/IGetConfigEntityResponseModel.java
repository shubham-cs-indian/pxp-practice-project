package com.cs.core.config.interactor.model.objectCount;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetConfigEntityResponseModel extends IModel{
  
  public static String DATA_MODEL = "dataModel";
  
  //TODO : Rename to Enities
  public List<IGetEntityCountModel> getDataModel();
  
  public void setDataModel(List<IGetEntityCountModel> dataModel);
}
