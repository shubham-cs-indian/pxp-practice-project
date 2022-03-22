package com.cs.core.runtime.interactor.model.purge;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IPurgeModel extends IModel {
  
  public static final String PURGE_PROCESS_TIME = "purgeProcessTime";
  public static final String PURGE_TIME         = "purgeTime";
  
  public Double getPurgeProcessTime();
  
  public void setPurgeProcessTime(Double purgeProcessTime);
  
  public Long getPurgeTime();
  
  public void setPurgeTime(Long purgeTime);
}
