package com.cs.core.config.interactor.model.translations;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ISaveStandardTranslationsRequestModel extends IModel {
  
  public static String DATA = "data";
  
  public List<ISaveStandardTranslationModel> getData();
  
  public void setData(List<ISaveStandardTranslationModel> data);
}
