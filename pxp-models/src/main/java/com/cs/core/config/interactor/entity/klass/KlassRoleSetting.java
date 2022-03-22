package com.cs.core.config.interactor.entity.klass;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

public class KlassRoleSetting implements IKlassRoleSetting {
  
  private static final long                    serialVersionUID = 1L;
  
  protected String                             id;
  
  protected Map<String, IKlassRoleViewSetting> views            = new HashMap<String, IKlassRoleViewSetting>();
  
  protected IKlassStructureTreeSetting         tree             = new KlassStructureTreeSetting();
  
  protected IKlassStructureEditorSetting       editor           = new KlassStructureEditorSetting();
  
  protected Map<String, IKlassRoleSetting>     structures       = new HashMap<>();
  
  protected Long                               versionId;
  
  protected Long                               versionTimestamp;
  
  protected String                             lastModifiedBy;
  
  protected String                             code;
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
  }
  
  @Override
  public String getId()
  {
    return this.id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public Map<String, IKlassRoleViewSetting> getViews()
  {
    return this.views;
  }
  
  @JsonDeserialize(contentAs = KlassRoleViewSetting.class)
  @Override
  public void setViews(Map<String, IKlassRoleViewSetting> views)
  {
    this.views = views;
  }
  
  @Override
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return versionTimestamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
  }
  
  @Override
  public IKlassStructureTreeSetting getTree()
  {
    return tree;
  }
  
  @Override
  @JsonDeserialize(as = KlassStructureTreeSetting.class)
  public void setTree(IKlassStructureTreeSetting tree)
  {
    this.tree = tree;
  }
  
  @Override
  public IKlassStructureEditorSetting getEditor()
  {
    return editor;
  }
  
  @Override
  @JsonDeserialize(as = KlassStructureEditorSetting.class)
  public void setEditor(IKlassStructureEditorSetting editor)
  {
    this.editor = editor;
  }
  
  @Override
  public Map<String, IKlassRoleSetting> getStructures()
  {
    return structures;
  }
  
  @Override
  @JsonDeserialize(contentAs = KlassRoleSetting.class)
  public void setStructures(Map<String, IKlassRoleSetting> structures)
  {
    this.structures = structures;
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
  }
}
