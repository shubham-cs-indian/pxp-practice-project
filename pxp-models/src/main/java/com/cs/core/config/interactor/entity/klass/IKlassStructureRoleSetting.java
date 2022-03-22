package com.cs.core.config.interactor.entity.klass;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

public interface IKlassStructureRoleSetting extends IConfigEntity {
  
  IKlassStructureTreeSetting getTree();
  
  void setTree(IKlassStructureTreeSetting tree);
  
  IKlassStructureEditorSetting getEditor();
  
  void setEditor(IKlassStructureEditorSetting editor);
}
