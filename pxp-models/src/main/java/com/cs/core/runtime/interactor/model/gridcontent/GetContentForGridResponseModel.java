package com.cs.core.runtime.interactor.model.gridcontent;

import com.cs.core.runtime.interactor.entity.klassinstance.AbstractContentInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetContentForGridResponseModel implements IGetContentForGridResponseModel {
  
  private static final long  serialVersionUID = 1L;
  protected IContentInstance klassInstance;
  
  public IContentInstance getKlassInstance()
  {
    return klassInstance;
  }
  
  @Override
  @JsonDeserialize(as = AbstractContentInstance.class)
  public void setKlassInstance(IContentInstance klassInstance)
  {
    this.klassInstance = klassInstance;
  }
}
