package com.cs.core.config.interactor.entity.klass;

public class KlassRoleViewSetting implements IKlassRoleViewSetting {
  
  private static final long serialVersionUID     = 1L;
  
  protected String          id;
  
  protected Integer         visibleLevelsInTree;
  
  protected Integer         visibleLevelsInEditor;
  
  protected Boolean         isCommentVisible     = false;
  
  protected Boolean         isInstructionVisible = false;
  
  // protected Map<String, IKlassStructureSetting> structures = new
  // HashMap<String, IKlassStructureSetting>();
  
  protected Long            versionId;
  
  protected Long            versionTimestamp;
  
  protected String          lastModifiedBy;
  
  protected Boolean         visibleInEditor;
  
  protected Boolean         visibleInTree;
  
  protected Boolean         indexVisible;
  
  protected Boolean         iconVisible;
  
  protected Boolean         italics;
  
  protected Boolean         borderBottom;
  
  protected Boolean         headerVisible;
  
  protected String          code;
  
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
  public Boolean getItalics()
  {
    return italics;
  }
  
  @Override
  public void setItalics(Boolean italics)
  {
    this.italics = italics;
  }
  
  @Override
  public Boolean getBorderBottom()
  {
    return borderBottom;
  }
  
  @Override
  public void setBorderBottom(Boolean borderBottom)
  {
    this.borderBottom = borderBottom;
  }
  
  @Override
  public Boolean getHeaderVisible()
  {
    return headerVisible;
  }
  
  @Override
  public void setHeaderVisible(Boolean headerVisible)
  {
    this.headerVisible = headerVisible;
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
  public Integer getVisibleLevelsInTree()
  {
    return this.visibleLevelsInTree;
  }
  
  @Override
  public void setVisibleLevelsInTree(Integer visibleLevelsInTree)
  {
    this.visibleLevelsInTree = visibleLevelsInTree;
  }
  
  @Override
  public Integer getVisibleLevelsInEditor()
  {
    return this.visibleLevelsInEditor;
  }
  
  @Override
  public void setVisibleLevelsInEditor(Integer visibleLevelsInEditor)
  {
    this.visibleLevelsInEditor = visibleLevelsInEditor;
  }
  
  @Override
  public Boolean getIsCommentVisible()
  {
    return this.isCommentVisible;
  }
  
  @Override
  public void setIsCommentVisible(Boolean isCommentVisible)
  {
    this.isCommentVisible = isCommentVisible;
  }
  
  @Override
  public Boolean getIsInstructionVisible()
  {
    return this.isInstructionVisible;
  }
  
  @Override
  public void setIsInstructionVisible(Boolean isInstructionVisible)
  {
    this.isInstructionVisible = isInstructionVisible;
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
  
  @Override
  public Boolean getVisibleInEditor()
  {
    return visibleInEditor;
  }
  
  @Override
  public void setVisibleInEditor(Boolean visibleInEditor)
  {
    this.visibleInEditor = visibleInEditor;
  }
  
  @Override
  public Boolean getVisibleInTree()
  {
    return visibleInTree;
  }
  
  @Override
  public void setVisibleInTree(Boolean visibleInTree)
  {
    this.visibleInTree = visibleInTree;
  }
  
  @Override
  public Boolean getIndexVisible()
  {
    return indexVisible;
  }
  
  @Override
  public void setIndexVisible(Boolean indexVisible)
  {
    this.indexVisible = indexVisible;
  }
  
  @Override
  public Boolean getIconVisible()
  {
    return iconVisible;
  }
  
  @Override
  public void setIconVisible(Boolean iconVisible)
  {
    this.iconVisible = iconVisible;
  }
  
  /*@Override
  public Map<String, IKlassStructureSetting> getStructures()
  {
    return this.structures;
  }
  
  @JsonDeserialize(contentAs = KlassStructureSetting.class)
  @Override
  public void setStructures(Map<String, IKlassStructureSetting> structures)
  {
    this.structures = structures;
  }*/
  
}
