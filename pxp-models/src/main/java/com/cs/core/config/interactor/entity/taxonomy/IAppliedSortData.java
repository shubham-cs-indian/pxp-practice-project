package com.cs.core.config.interactor.entity.taxonomy;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

import java.util.List;

public interface IAppliedSortData extends IEntity {
  
  public static final String ATTRIBUTES = "attributes";
  
  public List<String> getAttributes();
  
  public void setAttributes(List<String> attributes);
}
