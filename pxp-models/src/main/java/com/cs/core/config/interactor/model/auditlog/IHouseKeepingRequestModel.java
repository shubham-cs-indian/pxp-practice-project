package com.cs.core.config.interactor.model.auditlog;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IHouseKeepingRequestModel extends IModel {
  
  public static final String HOUSEKEEPING_OFFSET = "offset";
  
  public int getOffset();
  public void setOffset(int offset);
}
