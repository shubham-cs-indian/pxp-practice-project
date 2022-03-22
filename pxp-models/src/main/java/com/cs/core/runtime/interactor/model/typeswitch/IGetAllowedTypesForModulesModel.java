package com.cs.core.runtime.interactor.model.typeswitch;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetAllowedTypesForModulesModel extends IModel {
  
  public static final String STANDARD_KLASS_IDS = "standardKlassIds";
  public static final String ID                 = "id";
  
  public List<String> getStandardKlassIds();
  
  public void setStandardKlassIds(List<String> standardKlassIds);
  
  public String getId();
  
  public void setId(String id);
}
