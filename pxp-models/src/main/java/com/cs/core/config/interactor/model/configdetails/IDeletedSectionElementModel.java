package com.cs.core.config.interactor.model.configdetails;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IDeletedSectionElementModel extends IModel {
  
  public static final String SECTION_ELEMENT_ID = "sectionElementId";
  public static final String SECTION_ID         = "sectionId";
  
  public String getSectionElementId();
  
  public void setSectionElementId(String sectionElementId);
  
  public String getSectionId();
  
  public void setSectionId(String sectionId);
}
