package com.cs.di.config.interactor.model.task;

import java.util.Map;

public class GetConfigDetailsByCodesResponseModel implements IGetConfigDetailsByCodesResponseModel {
  
  private static final long serialVersionUID = 1L;
  
  protected Map<String, Object> klass;
  protected Map<String, String> taxonomy;
  
  @Override
  public Map<String, Object> getKlass()
  {
    return klass;
  }
  
  @Override
  public void setKlass(Map<String, Object> klass)
  {
    this.klass = klass;
  }
  
  @Override
  public Map<String, String> getTaxonomy()
  {
    return taxonomy;
  }
  
  @Override
  public void setTaxonomy(Map<String, String> taxonomy)
  {
    this.taxonomy = taxonomy;
  }
}
