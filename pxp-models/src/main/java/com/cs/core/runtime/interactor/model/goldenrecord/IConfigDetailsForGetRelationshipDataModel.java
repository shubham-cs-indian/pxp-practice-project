package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IConfigDetailsForGetRelationshipDataModel extends IModel {
  
  public static final String REFERENCED_TAGS = "referencedTags";
  
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
}
