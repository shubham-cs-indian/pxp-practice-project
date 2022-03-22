package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.config.interactor.entity.attributiontaxonomy.AbstractTaxonomy;
import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Map;

public abstract class AbstractTaxonomyModel implements ITaxonomy, IModel {
  
  private static final long serialVersionUID = 1L;
  
  protected ITaxonomy       entity;
  
  @Override
  public String getDescription()
  {
    return entity.getDescription();
  }
  
  @Override
  public void setDescription(String description)
  {
    entity.setDescription(description);
  }
  
  @Override
  public String getTooltip()
  {
    return entity.getTooltip();
  }
  
  @Override
  public void setTooltip(String tooltip)
  {
    entity.setTooltip(tooltip);
  }
  
  @Override
  public String getLabel()
  {
    return entity.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    entity.setLabel(label);
  }
  
  @Override
  public ITreeEntity getParent()
  {
    return entity.getParent();
  }
  
  // FIXME
  @JsonDeserialize(as = AbstractTaxonomy.class)
  @Override
  public void setParent(ITreeEntity parent)
  {
    entity.setParent(parent);
  }
  
  @Override
  public Boolean getIsMandatory()
  {
    return entity.getIsMandatory();
  }
  
  @Override
  public void setIsMandatory(Boolean isMandatory)
  {
    entity.setIsMandatory(isMandatory);
  }
  
  @Override
  public String getIcon()
  {
    return entity.getIcon();
  }
  
  @Override
  public void setIcon(String icon)
  {
    entity.setIcon(icon);
  }
  
  @Override
  public String getIconKey()
  {
    return this.entity.getIconKey();
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    this.entity.setIconKey(iconKey);
  }
  
  @Override
  public String getId()
  {
    return entity.getId();
  }
  
  @Override
  public void setId(String id)
  {
    entity.setId(id);
  }
  
  @Override
  public Boolean getIsStandard()
  {
    return entity.getIsStandard();
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    entity.setIsStandard(isStandard);
  }
  
  @Override
  public String getType()
  {
    return entity.getType();
  }
  
  @Override
  public void setType(String type)
  {
    entity.setType(type);
  }
  
  @Override
  public Long getVersionId()
  {
    return entity.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    entity.setVersionId(versionId);
  }
  
  @Override
  public String getPlaceholder()
  {
    return entity.getPlaceholder();
  }
  
  @Override
  public void setPlaceholder(String placeholder)
  {
    entity.setPlaceholder(placeholder);
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return entity.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    entity.setVersionTimestamp(versionTimestamp);
  }
  
  @Override
  public List<String> getAppliedKlasses()
  {
    return entity.getAppliedKlasses();
  }
  
  @Override
  public void setAppliedKlasses(List<String> klasses)
  {
    entity.setAppliedKlasses(klasses);
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return entity.getLastModifiedBy();
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    entity.setLastModifiedBy(lastModifiedBy);
  }
  
  @Override
  public List<? extends ISection> getSections()
  {
    return entity.getSections();
  }
  
  @Override
  public void setSections(List<? extends ISection> sections)
  {
    entity.setSections(sections);
  }
  
  @Override
  public String getCode()
  {
    return entity.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    entity.setCode(code);
  }
  
  @Override
  public Integer getChildCount()
  {
    return entity.getChildCount();
  }
  
  @Override
  public void setChildCount(Integer childCount)
  {
    entity.setChildCount(childCount);
  }
  
  @Override
  public String getLinkedMasterTagId()
  {
    return entity.getLinkedMasterTagId();
  }
  
  @Override
  public void setLinkedMasterTagId(String linkedMasterTagId)
  {
    entity.setLinkedMasterTagId(linkedMasterTagId);
  }
  
  @Override
  public Map<String, String> getLinkedLevels()
  {
    return entity.getLinkedLevels();
  }
  
  @Override
  public void setLinkedLevels(Map<String, String> linkedLevels)
  {
    entity.setLinkedLevels(linkedLevels);
  }
  
  @Override
  public String getTaxonomyType()
  {
    return entity.getTaxonomyType();
  }
  
  @Override
  public void setTaxonomyType(String taxonomyType)
  {
    entity.setTaxonomyType(taxonomyType);
  }
  
  @Override
  public String getLinkedLevelId()
  {
    return entity.getLinkedLevelId();
  }
  
  @Override
  public void setLinkedLevelId(String linkedLevelId)
  {
    entity.setLinkedLevelId(linkedLevelId);
  }
  
  @Override
  public String getBaseType()
  {
    return entity.getBaseType();
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    entity.setBaseType(baseType);
  }
  
  @Override
  public List<? extends ITreeEntity> getChildren()
  {
    return entity.getChildren();
  }
  
  @Override
  public Long getClassifierIID()
  {
    return entity.getClassifierIID();
  }
  
  @Override
  public void setClassifierIID(Long classifierIID)
  {
    entity.setClassifierIID(classifierIID);
  }
  
  @Override
  public Boolean getIsRoot()
  {
    return entity.getIsRoot();
  }

  @Override
  public void setIsRoot(Boolean isRoot)
  {
    entity.setIsRoot(isRoot);
  }
}
