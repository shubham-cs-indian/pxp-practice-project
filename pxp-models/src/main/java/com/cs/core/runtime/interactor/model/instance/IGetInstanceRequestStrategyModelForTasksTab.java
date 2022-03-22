package com.cs.core.runtime.interactor.model.instance;

import java.util.List;

public interface IGetInstanceRequestStrategyModelForTasksTab
    extends IGetInstanceRequestStrategyModel {
  
  public static final String REFERENCED_LIFECYCLE_STATUS_TAGS = "referencedLifeCycleStatusTags";
  
  public List<String> getReferencedLifeCycleStatusTags();
  
  public void setReferencedLifeCycleStatusTags(List<String> referencedLifeCycleStatusTags);
}
