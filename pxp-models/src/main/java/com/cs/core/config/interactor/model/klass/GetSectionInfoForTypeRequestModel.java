package com.cs.core.config.interactor.model.klass;

import java.util.List;

public class GetSectionInfoForTypeRequestModel implements IGetSectionInfoForTypeRequestModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          typeId;
  protected List<String>    sectionIds;
  
  @Override
  public String getTypeId()
  {
    return typeId;
  }
  
  @Override
  public void setTypeId(String typeId)
  {
    this.typeId = typeId;
  }
  
  @Override
  public List<String> getSectionIds()
  {
    return sectionIds;
  }
  
  @Override
  public void setSectionIds(List<String> sectionIds)
  {
    this.sectionIds = sectionIds;
  }
}
