package com.cs.core.config.interactor.entity.klass;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

import java.util.Map;

public interface IKlassRoleSetting extends IConfigEntity {
  
  public Map<String, IKlassRoleViewSetting> getViews();
  
  public void setViews(Map<String, IKlassRoleViewSetting> views);
  
  IKlassStructureTreeSetting getTree();
  
  void setTree(IKlassStructureTreeSetting tree);
  
  IKlassStructureEditorSetting getEditor();
  
  void setEditor(IKlassStructureEditorSetting editor);
  
  Map<String, IKlassRoleSetting> getStructures();
  
  void setStructures(Map<String, IKlassRoleSetting> structures);
}
