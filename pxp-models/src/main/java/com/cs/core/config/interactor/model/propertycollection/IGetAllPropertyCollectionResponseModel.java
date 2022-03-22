package com.cs.core.config.interactor.model.propertycollection;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.propertycollection.IPropertyCollectionEntityInformationModel;

import java.util.List;

public interface IGetAllPropertyCollectionResponseModel extends IModel {
  
  public static final String LIST  = "list";
  public static final String COUNT = "count";
  
  public Long getCount();
  
  public void setCount(Long count);
  
  public List<IPropertyCollectionEntityInformationModel> getList();
  
  public void setList(List<IPropertyCollectionEntityInformationModel> list);
}
