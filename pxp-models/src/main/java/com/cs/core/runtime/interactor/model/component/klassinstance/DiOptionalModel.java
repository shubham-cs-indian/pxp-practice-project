package com.cs.core.runtime.interactor.model.component.klassinstance;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class DiOptionalModel implements IDiOptionalModel {
  
  private static final long  serialVersionUID = 1L;
  private IDiContextModel    context;
  private IDiPropertiesModel properties;
  
  @Override
  public IDiContextModel getContext()
  {
    if (this.context == null) {
      this.context = new DiContextModel();
    }
    return this.context;
  }
  
  @Override
  @JsonDeserialize(as = DiContextModel.class)
  public void setContext(IDiContextModel context)
  {
    this.context = context;
  }
  
  @Override
  public IDiPropertiesModel getProperties()
  {
    if (this.properties == null) {
      this.properties = new DiPropertiesModel();
    }
    return this.properties;
  }
  
  @Override
  @JsonDeserialize(as = DiPropertiesModel.class)
  public void setProperties(IDiPropertiesModel properties)
  {
    this.properties = properties;
  }
}
