package com.cs.core.config.interactor.entity.taxonomy;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

import java.util.List;

public interface IAppliedFilterData extends IEntity {
  
  public static final String TAGS       = "tags";
  public static final String ATTRIBUTES = "attributes";
  
  public List<String> getTags();
  
  public void setTags(List<String> tags);
  
  public List<String> getAttributes();
  
  public void setAttributes(List<String> attributes);
}
