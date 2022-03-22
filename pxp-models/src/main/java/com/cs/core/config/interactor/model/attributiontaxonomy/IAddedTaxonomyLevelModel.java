package com.cs.core.config.interactor.model.attributiontaxonomy;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAddedTaxonomyLevelModel extends IModel {
  
  public static final String ID        = "id";
  public static final String ADDED_TAG = "addedTag";
  
  public String getId();
  
  public void setId(String id);
  
  public IAddedTagModel getAddedTag();
  
  public void setAddedTag(IAddedTagModel addedTag);
}
