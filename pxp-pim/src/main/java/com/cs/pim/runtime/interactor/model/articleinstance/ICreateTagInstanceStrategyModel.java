package com.cs.pim.runtime.interactor.model.articleinstance;

import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ICreateTagInstanceStrategyModel extends IModel {
  
  public static final String TAGS = "tags";
  
  public List<? extends IContentTagInstance> getTags();
  
  public void setTags(List<? extends IContentTagInstance> tags);
}
