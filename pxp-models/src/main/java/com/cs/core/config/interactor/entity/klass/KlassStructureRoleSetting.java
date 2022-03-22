package com.cs.core.config.interactor.entity.klass;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KlassStructureRoleSetting implements IKlassStructureRoleSetting {
  
  private static final long              serialVersionUID = 1L;
  
  protected String                       id;
  
  protected IKlassStructureTreeSetting   tree             = new KlassStructureTreeSetting();
  
  protected IKlassStructureEditorSetting editor           = new KlassStructureEditorSetting();
  
  protected Long                         versionId;
  
  protected Long                         versionTimestamp;
  
  protected String                       lastModifiedBy;
  protected String                       code;
  
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
    return this.tree;
  }
  
  @JsonDeserialize(as = KlassStructureTreeSetting.class)
  @Override
  public void setTree(IKlassStructureTreeSetting tree)
  {
    this.tree = tree;
  }
  
  @Override
  public IKlassStructureEditorSetting getEditor()
  {
    return this.editor;
  }
  
  @JsonDeserialize(as = KlassStructureEditorSetting.class)
  @Override
  public void setEditor(IKlassStructureEditorSetting editor)
  {
    this.editor = editor;
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
