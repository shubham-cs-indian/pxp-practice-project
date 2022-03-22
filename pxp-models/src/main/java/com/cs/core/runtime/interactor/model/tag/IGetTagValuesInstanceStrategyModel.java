package com.cs.core.runtime.interactor.model.tag;

import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetTagValuesInstanceStrategyModel extends IModel {
  
  public static final String TAGS = "tags";
  
  public List<ITagInstance> getTags();
  
  public void setTags(List<ITagInstance> tags);
}
