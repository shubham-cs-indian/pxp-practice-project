package com.cs.core.config.interactor.model.version;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IVersionCountModel extends IModel {
  
  public static final String Max_VERSION_COUNT = "maxVersionCount";
  
  public Integer getMaxVersionCount();
  
  public void setMaxVersionCount(Integer versionCount);
}
