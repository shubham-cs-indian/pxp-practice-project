package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.runtime.interactor.model.taxonomy.ITaxonomyChildrenModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class TaxonomyChildrenModel implements ITaxonomyChildrenModel {
  
  private static final long             serialVersionUID = 1L;
  protected String                      id;
  protected String                      label;
  protected List<? extends ITreeEntity> children;
  protected String                      linkedMasterTagId;
  protected String                      taxonomyType;
  protected String                      icon;
  private String                        iconKey;
  protected String                      type;
  protected String                      code;
  protected Long                        iid;
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public String getIcon()
  {
    return icon;
  }
  
  @Override
  public void setIcon(String icon)
  {
    this.icon = icon;
  }
  
  @Override
  public String getIconKey()
  {
    return iconKey;
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    this.iconKey = iconKey;
  }
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  @JsonIgnore
  public ITreeEntity getParent()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setParent(ITreeEntity parent)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public List<? extends ITreeEntity> getChildren()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setChildren(List<? extends ITreeEntity> children)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getLinkedMasterTagId()
  {
    return linkedMasterTagId;
  }
  
  @Override
  public void setLinkedMasterTagId(String linkedMasterTagId)
  {
    this.linkedMasterTagId = linkedMasterTagId;
  }
  
  @Override
  public String getTaxonomyType()
  {
    return taxonomyType;
  }
  
  @Override
  public void setTaxonomyType(String taxonomyType)
  {
    this.taxonomyType = taxonomyType;
  }
  
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
  public Long getIid()
  {
    return iid;
  }

  @Override
  public void setIid(Long iid)
  {
    this.iid = iid;
  }
}
