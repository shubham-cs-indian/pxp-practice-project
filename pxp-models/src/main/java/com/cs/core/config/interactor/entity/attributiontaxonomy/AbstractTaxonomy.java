package com.cs.core.config.interactor.entity.attributiontaxonomy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.matchandmerge.AbstractSection;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public abstract class AbstractTaxonomy implements ITaxonomy {
  
  private static final long             serialVersionUID = 1L;
  
  protected String                      id;
  protected String                      label;
  protected String                      icon;
  protected String                      iconKey;
  protected String                      code;
  protected String                      taxonomyType;
  protected String                      baseType         = this.getClass()
      .getName();
  protected List<String>                klasses;
  protected List<? extends ISection>    sections;
  protected Integer                     childCount;
  protected String                      linkedMasterTagId;
  protected Map<String, String>         linkedLevels;
  protected ITreeEntity                 parent;
  protected List<? extends ITreeEntity> children;
  protected String                      linkedLevelId;
  protected Long                        classifierIID;
  protected Boolean                     isRoot = false;
  
  public String getBaseType()
  {
    return baseType;
  }
  
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
  
  @Override
  public List<? extends ISection> getSections()
  {
    if (sections == null) {
      sections = new ArrayList<>();
    }
    return sections;
  }
  
  @JsonDeserialize(contentAs = AbstractSection.class)
  @Override
  public void setSections(List<? extends ISection> sections)
  {
    this.sections = sections;
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
  public Integer getChildCount()
  {
    return childCount;
  }
  
  @Override
  public void setChildCount(Integer childCount)
  {
    this.childCount = childCount;
  }
  
  @Override
  public Map<String, String> getLinkedLevels()
  {
    if (linkedLevels == null) {
      linkedLevels = new HashMap<String, String>();
    }
    return linkedLevels;
  }
  
  @Override
  public void setLinkedLevels(Map<String, String> linkedLevels)
  {
    this.linkedLevels = linkedLevels;
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
  public String getLinkedLevelId()
  {
    return linkedLevelId;
  }
  
  @Override
  public void setLinkedLevelId(String linkedLevelId)
  {
    this.linkedLevelId = linkedLevelId;
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
  public List<String> getAppliedKlasses()
  {
    if (klasses == null) {
      klasses = new ArrayList<>();
    }
    return klasses;
  }
  
  @Override
  public void setAppliedKlasses(List<String> klasses)
  {
    this.klasses = klasses;
  }
  
  @Override
  public ITreeEntity getParent()
  {
    return parent;
  }
  
  @Override
  public List<? extends ITreeEntity> getChildren()
  {
    return children;
  }
  
  @Override
  public void setIsRoot(Boolean isRoot)
  {
    this.isRoot = isRoot;
  }

  @Override
  public Boolean getIsRoot()
  {
    return isRoot;
  }
  
  /**
   * 
   * ****************************** ignore properties
   * **********************************
   */
  @Override
  @JsonIgnore
  public String getDescription()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setDescription(String description)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public String getTooltip()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setTooltip(String tooltip)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public Boolean getIsMandatory()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setIsMandatory(Boolean isMandatory)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public Boolean getIsStandard()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public String getPlaceholder()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setPlaceholder(String placeholder)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public String getType()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setType(String type)
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
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Long getClassifierIID()
  {
    return classifierIID;
  }
  
  @Override
  public void setClassifierIID(Long classifierIID)
  {
    this.classifierIID = classifierIID;
  }
}
