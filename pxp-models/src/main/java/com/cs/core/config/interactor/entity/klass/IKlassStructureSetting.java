package com.cs.core.config.interactor.entity.klass;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

import java.util.Map;

public interface IKlassStructureSetting extends IConfigEntity {
  
  public Boolean getIsEditable();
  
  public void setIsEditable(Boolean isEditable);
  
  public Boolean getShowSections();
  
  public void setShowSections(Boolean showSections);
  
  public Map<String, IKlassStructureSectionSetting> getSections();
  
  public void setSections(Map<String, IKlassStructureSectionSetting> sections);
}
