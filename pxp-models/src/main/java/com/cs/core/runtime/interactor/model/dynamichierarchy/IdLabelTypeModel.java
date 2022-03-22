package com.cs.core.runtime.interactor.model.dynamichierarchy;

import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;

public class IdLabelTypeModel extends IdLabelCodeModel implements IIdLabelTypeModel {
  
  private static final long serialVersionUID = 1L;
  protected String          type;
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
}
