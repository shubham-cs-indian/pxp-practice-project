package com.cs.core.config.interactor.entity.klass;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

public interface IKlassStructurePermissionSetting extends IConfigEntity {
  
  public Boolean getIsHeaderEditable();
  
  public void setIsHeaderEditable(Boolean isHeaderEditable);
  
  public Boolean getIsHeaderVisible();
  
  public void setIsHeaderVisible(Boolean isHeaderVisible);
  
  public Boolean getCanAddSiblings();
  
  public void setCanAddSiblings(Boolean canAddSiblings);
  
  public Boolean getCanAddChildren();
  
  public void setCanAddChildren(Boolean canAddChildren);
  
  public Boolean getCanMove();
  
  public void setCanMove(Boolean canMove);
  
  public Boolean getCanDelete();
  
  public void setCanDelete(Boolean canDelete);
  
  public Boolean getIsVariantAllowed();
  
  public void setIsVariantAllowed(Boolean isVariantAllowed);
  
  public Boolean getShouldVersion();
  
  public void setShouldVersion(Boolean shouldVersion);
}
