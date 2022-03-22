package com.cs.core.config.interactor.entity.klass;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

import java.util.Map;

public interface IKlassStructureSectionSetting extends IConfigEntity {
  
  public Boolean getIsVisible();
  
  public void setIsVisible(Boolean isVisible);
  
  public Boolean getIsCollapsed();
  
  public void setIsCollapsed(Boolean isCollapsed);
  
  public Map<String, IKlassStructureSectionElementSetting> getElements();
  
  public void setElements(Map<String, IKlassStructureSectionElementSetting> elements);
}
