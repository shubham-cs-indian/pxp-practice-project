package com.cs.core.runtime.interactor.model.typeswitch;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetAllowedTypesModel extends IModel {
  
  public static final String STANDARD_KLASS_ID = "standardKlassId";
  public static final String ID                = "id";
  public static final String MODE              = "mode";
  
  public String getStandardKlassId();
  
  public void setStandardKlassId(String standardKlassId);
  
  public String getId();
  
  public void setId(String id);
  
  public String getMode();
  
  public void setMode(String mode);
}
