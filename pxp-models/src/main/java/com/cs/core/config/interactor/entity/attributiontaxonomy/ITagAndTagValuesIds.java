package com.cs.core.config.interactor.entity.attributiontaxonomy;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

import java.util.List;

public interface ITagAndTagValuesIds extends IEntity {
  
  public static final String ID            = "id";
  public static final String TAG_VALUE_IDS = "tagValueIds";
  
  public String getId();
  
  public void setId(String id);
  
  public List<String> getTagValueIds();
  
  public void setTagValueIds(List<String> tagValueIds);
}
