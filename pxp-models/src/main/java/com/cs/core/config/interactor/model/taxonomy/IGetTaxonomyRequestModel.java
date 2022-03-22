package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetTaxonomyRequestModel extends IModel {
  
  public static String ID            = "id";
  public static String TAXONOMY_TYPE = "taxonomyType";
  
  public String getId();
  
  public void setId(String id);
  
  public String getTaxonomyType();
  
  public void setTaxonomyType(String taxonomyType);
}
