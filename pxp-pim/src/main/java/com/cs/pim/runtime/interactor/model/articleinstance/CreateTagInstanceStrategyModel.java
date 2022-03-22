package com.cs.pim.runtime.interactor.model.articleinstance;

import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class CreateTagInstanceStrategyModel implements ICreateTagInstanceStrategyModel {
  
  private static final long           serialVersionUID = 1L;
  protected List<IContentTagInstance> tags;
  
  @Override
  public List<? extends IContentTagInstance> getTags()
  {
    if (tags == null) {
      tags = new ArrayList<>();
    }
    return this.tags;
  }
  
  @Override
  public void setTags(List<? extends IContentTagInstance> attributes)
  {
    this.tags = (List<IContentTagInstance>) attributes;
  }
}
