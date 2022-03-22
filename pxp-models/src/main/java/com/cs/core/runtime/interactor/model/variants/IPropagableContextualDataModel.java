package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.entity.datarule.IAttributeConflictingValue;
import com.cs.core.runtime.interactor.entity.tag.ITagConflictingValue;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IPropagableContextualDataModel extends IModel {
  
  public static final String ATTRIBUTES = "attributes";
  public static final String TAGS       = "tags";
  
  public Map<String, IAttributeConflictingValue> getAttributes();
  
  public void setAttributes(Map<String, IAttributeConflictingValue> attributes);
  
  public Map<String, ITagConflictingValue> getTags();
  
  public void setTags(Map<String, ITagConflictingValue> attributes);
}
