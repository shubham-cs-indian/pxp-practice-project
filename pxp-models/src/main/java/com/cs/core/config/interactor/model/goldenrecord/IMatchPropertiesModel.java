package com.cs.core.config.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IMatchPropertiesModel extends IModel {
  
  public static final String ATTRIBUTES = "attributes";
  public static final String TAGS       = "tags";
  
  public List<IAttributeInstance> getAttributes();
  
  public void setAttributes(List<IAttributeInstance> attributes);
  
  public List<ITagInstance> getTags();
  
  public void setTags(List<ITagInstance> tags);
}
