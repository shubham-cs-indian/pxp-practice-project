package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ITagIdValueModel extends IModel {
  
  public static final String TAG_ID     = "tagId";
  public static final String TAG_VALUES = "tagValues";
  
  public String getTagId();
  
  public void setTagId(String tagId);
  
  public List<IIdRelevance> getTagValues();
  
  public void setTagValues(List<IIdRelevance> tagValues);
}
