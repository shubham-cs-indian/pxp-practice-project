package com.cs.core.runtime.interactor.model.process;

import java.util.List;

public class GetTranslatableAttributesModel implements IGetTranslatableAttributesModel {
  
  protected String       moduleId;
  protected List<String> attributeIds;
  
  @Override
  public String getModuleId()
  {
    return moduleId;
  }
  
  @Override
  public void setModuleId(String moduleId)
  {
    this.moduleId = moduleId;
  }
  
  @Override
  public List<String> getAttributeIds()
  {
    return attributeIds;
  }
  
  @Override
  public void setAttributeIds(List<String> attributeIds)
  {
    this.attributeIds = attributeIds;
  }
}
