package com.cs.core.runtime.interactor.model.instance;

import java.util.ArrayList;
import java.util.List;

public class GetInstanceRequestStrategyModelForTasksTab extends
    AbstractGetInstanceRequestStrategyModel implements IGetInstanceRequestStrategyModelForTasksTab {
  
  private static final long serialVersionUID              = 1L;
  
  protected List<String>    referencedLifeCycleStatusTags = new ArrayList<>();
  
  public GetInstanceRequestStrategyModelForTasksTab(IGetInstanceRequestModel model)
  {
    super(model);
  }
  
  public GetInstanceRequestStrategyModelForTasksTab()
  {
  }
  
  @Override
  public List<String> getReferencedLifeCycleStatusTags()
  {
    if (referencedLifeCycleStatusTags == null) {
      referencedLifeCycleStatusTags = new ArrayList<>();
    }
    return referencedLifeCycleStatusTags;
  }
  
  @Override
  public void setReferencedLifeCycleStatusTags(List<String> referencedLifeCycleStatusTags)
  {
    this.referencedLifeCycleStatusTags = referencedLifeCycleStatusTags;
  }
}
