package com.cs.core.config.interactor.entity.structure;

import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterEntity;
import com.cs.core.config.interactor.entity.klass.IKlassViewSetting;

import java.util.List;

public interface IStructure extends IConfigMasterEntity {
  
  public static final String VALIDATOR          = "validator";
  public static final String STRUCTURE_CHILDREN = "structureChildren";
  public static final String IS_GHOST           = "isGhost";
  public static final String STRUCTURE_ID       = "structureId";
  public static final String POSITION           = "position";
  public static final String VIEW_SETTINGS      = "viewSettings";
  public static final String IS_INHERITED       = "isInherited";
  public static final String LINK_PATH          = "linkPath";
  
  public IStructureValidator getValidator();
  
  public void setValidator(IStructureValidator validator);
  
  public List<IStructure> getStructureChildren();
  
  public void setStructureChildren(List<IStructure> elements);
  
  public Boolean getIsGhost();
  
  public void setIsGhost(Boolean isGhost);
  
  public String getStructureId();
  
  public void setStructureId(String structureId);
  
  public Integer getPosition();
  
  public void setPosition(Integer position);
  
  public IKlassViewSetting getViewSettings();
  
  public void setViewSettings(IKlassViewSetting viewSettings);
  
  public Boolean getIsInherited();
  
  public void setIsInherited(Boolean isInherited);
  
  public String getLinkPath();
  
  public void setLinkPath(String linkPath);
  
  public Object clone() throws CloneNotSupportedException;
}
