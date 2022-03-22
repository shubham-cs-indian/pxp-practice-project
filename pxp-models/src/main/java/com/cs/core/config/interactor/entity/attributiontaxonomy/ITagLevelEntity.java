package com.cs.core.config.interactor.entity.attributiontaxonomy;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface ITagLevelEntity extends IEntity {
  
  public static final String TAG = "tag";
  
  public ITagAndTagValuesIds getTag();
  
  public void setTag(ITagAndTagValuesIds tags);
}
