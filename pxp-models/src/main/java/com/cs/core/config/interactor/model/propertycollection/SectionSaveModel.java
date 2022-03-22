package com.cs.core.config.interactor.model.propertycollection;

import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.propertycollection.Section;
import com.cs.core.config.interactor.model.configdetails.AbstractSectionSaveModel;

public class SectionSaveModel extends AbstractSectionSaveModel {
  
  private static final long serialVersionUID = 1L;
  
  public SectionSaveModel()
  {
    this.entity = new Section();
  }
  
  public SectionSaveModel(ISection section)
  {
    this.entity = section;
  }
  
  @Override
  public Boolean getIsSkipped()
  {
    return this.entity.getIsSkipped();
  }
  
  @Override
  public void setIsSkipped(Boolean isSkipped)
  {
    this.entity.setIsSkipped(isSkipped);
  }
  
  @Override
  public Boolean getIsInherited()
  {
    return null;
  }
  
  @Override
  public void setIsInherited(Boolean isInherited)
  {
  }
}
