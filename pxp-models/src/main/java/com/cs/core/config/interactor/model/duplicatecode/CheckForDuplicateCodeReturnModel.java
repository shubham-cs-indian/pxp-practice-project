package com.cs.core.config.interactor.model.duplicatecode;

import com.cs.core.config.interactor.entity.datarule.ICheckForDuplicateCodeReturnModel;

public class CheckForDuplicateCodeReturnModel implements ICheckForDuplicateCodeReturnModel {
  
  private static final long serialVersionUID = 1L;
  protected Boolean         found;
  
  @Override
  public Boolean getFound()
  {
    return found;
  }
  
  @Override
  public void setFound(Boolean found)
  {
    this.found = found;
  }
}
