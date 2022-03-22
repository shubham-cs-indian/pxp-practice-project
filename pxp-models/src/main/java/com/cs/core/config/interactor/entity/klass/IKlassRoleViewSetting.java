package com.cs.core.config.interactor.entity.klass;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

public interface IKlassRoleViewSetting extends IConfigEntity {
  
  public Integer getVisibleLevelsInTree();
  
  public void setVisibleLevelsInTree(Integer visibleLevelsInTree);
  
  public Integer getVisibleLevelsInEditor();
  
  public void setVisibleLevelsInEditor(Integer visibleLevelsInEditor);
  
  public Boolean getIsCommentVisible();
  
  public void setIsCommentVisible(Boolean isCommentVisible);
  
  public Boolean getIsInstructionVisible();
  
  public void setIsInstructionVisible(Boolean isInstructionVisible);
  
  public Boolean getVisibleInEditor();
  
  public void setVisibleInEditor(Boolean visibleInEditor);
  
  public Boolean getVisibleInTree();
  
  public void setVisibleInTree(Boolean visibleInTree);
  
  public Boolean getIndexVisible();
  
  public void setIndexVisible(Boolean indexVisible);
  
  public Boolean getIconVisible();
  
  public void setIconVisible(Boolean iconVisible);
  
  public Boolean getItalics();
  
  public void setItalics(Boolean italics);
  
  public Boolean getBorderBottom();
  
  public void setBorderBottom(Boolean borderBottom);
  
  public Boolean getHeaderVisible();
  
  public void setHeaderVisible(Boolean headerVisible);
  
  /*  public Map<String, IKlassStructureSetting> getStructures();
  
  public void setStructures(Map<String, IKlassStructureSetting> structures);*/
  
}
