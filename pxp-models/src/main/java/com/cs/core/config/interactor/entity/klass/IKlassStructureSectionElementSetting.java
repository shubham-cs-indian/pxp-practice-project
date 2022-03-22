package com.cs.core.config.interactor.entity.klass;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

public interface IKlassStructureSectionElementSetting extends IConfigEntity {
  
  public Boolean getIsVisible();
  
  public void setIsVisible(Boolean isVisible);
  
  public Boolean getIsEditable();
  
  public void setIsEditable(Boolean isEditable);
}
