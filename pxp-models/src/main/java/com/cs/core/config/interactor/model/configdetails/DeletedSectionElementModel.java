package com.cs.core.config.interactor.model.configdetails;

public class DeletedSectionElementModel implements IDeletedSectionElementModel {
  
  protected String sectionElementId;
  protected String sectionId;
  
  @Override
  public String getSectionElementId()
  {
    return sectionElementId;
  }
  
  @Override
  public void setSectionElementId(String sectionElementId)
  {
    this.sectionElementId = sectionElementId;
  }
  
  @Override
  public String getSectionId()
  {
    return sectionId;
  }
  
  @Override
  public void setSectionId(String sectionId)
  {
    this.sectionId = sectionId;
  }
}
