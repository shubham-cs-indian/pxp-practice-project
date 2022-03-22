package com.cs.dam.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IAssetPropertyInstanceFilterModel<T extends IFilterValueModel> extends IModel {
  
  public String getId();
  
  public void setId(String id);
  
  public String getType();
  
  public void setType(String type);
  
  public List<T> getMandatory();
  
  public void setMandatory(List<T> mandatory);
  
  public List<T> getShould();
  
  public void setShould(List<T> should);
}
