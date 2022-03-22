package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetSectionInfoForTypeRequestModel extends IModel {
  
  public static final String TYPE_ID     = "typeId";
  public static final String SECTION_IDS = "sectionIds";
  
  public String getTypeId();
  
  public void setTypeId(String typeId);
  
  public List<String> getSectionIds();
  
  public void setSectionIds(List<String> sectionIds);
}
