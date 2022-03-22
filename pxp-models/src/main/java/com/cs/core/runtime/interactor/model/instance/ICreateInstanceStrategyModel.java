package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;

public interface ICreateInstanceStrategyModel extends IModel {
  
  public static final String KLASS_INSTANCE = "klassInstance";
  public static final String CONFIG_DETAILS = "configDetails";
  public static final String IS_AUTO_CREATE = "isAutoCreate";
  
  public IKlassInstance getKlassInstance();
  
  public void setKlassInstance(IKlassInstance articleInstance);
  
  public IGetConfigDetailsForCustomTabModel getConfigDetails();
  
  public void setConfigDetails(IGetConfigDetailsForCustomTabModel configDetails);
  
  public Boolean getIsAutoCreate();
  
  public void setIsAutoCreate(Boolean isAutoCreate);
}
