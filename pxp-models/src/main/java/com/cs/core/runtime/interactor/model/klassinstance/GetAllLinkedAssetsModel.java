package com.cs.core.runtime.interactor.model.klassinstance;

import java.util.ArrayList;
import java.util.List;

public class GetAllLinkedAssetsModel implements IGetAllLinkedAssetsModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected List<String>    configIds        = new ArrayList();
  
  @Override
  public String getId()
  {
    
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public List<String> getConfigIds()
  {
    
    return configIds;
  }
  
  @Override
  public void setConfigIds(List<String> configIds)
  {
    this.configIds = configIds;
  }
}
