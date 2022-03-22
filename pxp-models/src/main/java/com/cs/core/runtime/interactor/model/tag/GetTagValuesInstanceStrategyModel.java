package com.cs.core.runtime.interactor.model.tag;

import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;

import java.util.ArrayList;
import java.util.List;

public class GetTagValuesInstanceStrategyModel implements IGetTagValuesInstanceStrategyModel {
  
  private static final long    serialVersionUID = 1L;
  protected List<ITagInstance> tags;
  
  @Override
  public List<ITagInstance> getTags()
  {
    if (tags == null) {
      tags = new ArrayList<ITagInstance>();
    }
    return tags;
  }
  
  @Override
  public void setTags(List<ITagInstance> tags)
  {
    this.tags = tags;
  }
}
