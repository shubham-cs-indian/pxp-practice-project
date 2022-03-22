package com.cs.core.config.interactor.model.asset;

import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.propertycollection.LinkedInstancesSection;
import com.cs.core.config.interactor.model.configdetails.AbstractSectionSaveModel;

public class LinkedInstancesSectionSaveModel extends AbstractSectionSaveModel {
  
  private static final long serialVersionUID = 1L;
  
  public LinkedInstancesSectionSaveModel()
  {
    this.entity = new LinkedInstancesSection();
  }
  
  public LinkedInstancesSectionSaveModel(ISection section)
  {
    this.entity = section;
  }
  
  @Override
  public Boolean getIsSkipped()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setIsSkipped(Boolean isSkipped)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Boolean getIsInherited()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setIsInherited(Boolean isInherited)
  {
    // TODO Auto-generated method stub
    
  }
}
