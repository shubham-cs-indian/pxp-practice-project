package com.cs.core.config.interactor.model.datarule;

import com.cs.core.config.interactor.entity.matchandmerge.MatchAndMergeSection;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.model.configdetails.AbstractSectionSaveModel;

public class MatchAndMergeSectionSaveModel extends AbstractSectionSaveModel {
  
  private static final long serialVersionUID = 1L;
  
  public MatchAndMergeSectionSaveModel()
  {
    this.entity = new MatchAndMergeSection();
  }
  
  public MatchAndMergeSectionSaveModel(ISection section)
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
