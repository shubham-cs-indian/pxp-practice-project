package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.config.interactor.entity.klass.ProjectKlass;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class CreateInstanceStrategyModel implements ICreateInstanceStrategyModel {
  
  private static final long                    serialVersionUID = 1L;
  protected IKlassInstance                     klassInstance;
  protected IGetConfigDetailsForCustomTabModel configDetails;
  protected Boolean                            isAutoCreate;
  
  @Override
  public IKlassInstance getKlassInstance()
  {
    return klassInstance;
  }
  
  @Override
  public void setKlassInstance(IKlassInstance klassInstance)
  {
    this.klassInstance = klassInstance;
  }
  
  @Override
  public Boolean getIsAutoCreate()
  {
    if (isAutoCreate == null) {
      isAutoCreate = false;
    }
    return isAutoCreate;
  }
  
  @Override
  public void setIsAutoCreate(Boolean isAutoCreate)
  {
    this.isAutoCreate = isAutoCreate;
  }
  
  @Override
  public IGetConfigDetailsForCustomTabModel getConfigDetails()
  {
    return configDetails;
  }
  
  @Override
  @JsonDeserialize(as = ProjectKlass.class)
  public void setConfigDetails(IGetConfigDetailsForCustomTabModel configDetails)
  {
    this.configDetails = configDetails;
  }
}
