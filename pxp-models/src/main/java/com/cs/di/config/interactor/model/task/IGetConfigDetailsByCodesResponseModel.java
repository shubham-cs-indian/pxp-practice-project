package com.cs.di.config.interactor.model.task;

import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetConfigDetailsByCodesResponseModel extends IModel {
  
  public static final String KLASS    = "klass";
  public static final String TAXONOMY = "taxonomy";
  
  public Map<String, Object> getKlass();
  public void setKlass(Map<String, Object> klass);
  
  public Map<String, String> getTaxonomy();
  public void setTaxonomy(Map<String, String> taxonomy);
  
}
