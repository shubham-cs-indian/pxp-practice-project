package com.cs.core.config.interactor.model.translations;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ISaveBasicTranslationRequestModel extends IModel {
  
  public static String DATA = "data";
  
  public List<ISaveBasicTranslationModel> getData();
  
  public void setData(List<ISaveBasicTranslationModel> data);
}
