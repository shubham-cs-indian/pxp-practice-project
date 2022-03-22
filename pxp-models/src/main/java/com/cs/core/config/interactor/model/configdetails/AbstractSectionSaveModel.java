package com.cs.core.config.interactor.model.configdetails;

import java.util.ArrayList;
import java.util.List;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.propertycollection.AbstractSectionElement;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.config.interactor.model.asset.LinkedInstancesSectionSaveModel;
import com.cs.core.config.interactor.model.datarule.MatchAndMergeSectionSaveModel;
import com.cs.core.config.interactor.model.propertycollection.SectionSaveModel;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type",
    visible = true, defaultImpl = SectionSaveModel.class)
@JsonSubTypes({ @Type(value = SectionSaveModel.class, name = CommonConstants.SECTION_TYPE),
    @Type(value = LinkedInstancesSectionSaveModel.class,
        name = CommonConstants.LINKED_INSTANCE_SECTION),
    @Type(value = MatchAndMergeSectionSaveModel.class,
        name = CommonConstants.MATCH_AND_MERGE_SECTION) })
public abstract class AbstractSectionSaveModel implements ISectionSaveModel {
  
  private static final long               serialVersionUID = 1L;
  
  protected ISection                      entity;
  
  private List<? extends ISectionElement> addedElements    = new ArrayList<>();
  private List<? extends ISectionElement> modifiedElements = new ArrayList<>();
  private List<String>                    deletedElements  = new ArrayList<>();
  
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
  public List<ISectionElement> getElements()
  {
    return entity.getElements();
  }
  
  @Override
  public void setElements(List<ISectionElement> elements)
  {
    entity.setElements(elements);
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
    return entity.getIconKey();
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    entity.setIconKey(iconKey);
  }
  
  @Override
  public Integer getRows()
  {
    return entity.getRows();
  }
  
  @Override
  public void setRows(Integer rows)
  {
    entity.setRows(rows);
  }
  
  @Override
  public Integer getColumns()
  {
    return entity.getColumns();
  }
  
  @Override
  public void setColumns(Integer columns)
  {
    entity.setColumns(columns);
  }
  
  @Override
  public IEntity getEntity()
  {
    return this.entity;
  }
  
  @Override
  public List<? extends ISectionElement> getAddedElements()
  {
    return addedElements;
  }
  
  @JsonDeserialize(contentAs = AbstractSectionElement.class)
  @Override
  public void setAddedElements(List<? extends ISectionElement> addedElements)
  {
    this.addedElements = addedElements;
  }
  
  @Override
  public List<? extends ISectionElement> getModifiedElements()
  {
    return modifiedElements;
  }
  
  @JsonDeserialize(contentAs = AbstractSectionElement.class)
  @Override
  public void setModifiedElements(List<? extends ISectionElement> modifiedElements)
  {
    this.modifiedElements = modifiedElements;
  }
  
  @Override
  public List<String> getDeletedElements()
  {
    return deletedElements;
  }
  
  @Override
  public void setDeletedElements(List<String> deletedElementIds)
  {
    this.deletedElements = deletedElementIds;
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
  public Boolean getIsCollapsed()
  {
    return entity.getIsCollapsed();
  }
  
  @Override
  public void setIsCollapsed(Boolean isCollapsed)
  {
    entity.setIsCollapsed(isCollapsed);
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
  public Integer getSequence()
  {
    return entity.getSequence();
  }
  
  @Override
  public void setSequence(Integer sequence)
  {
    entity.setSequence(sequence);
  }
  
  @Override
  public String getPropertyCollectionId()
  {
    return entity.getPropertyCollectionId();
  }
  
  @Override
  public void setPropertyCollectionId(String properyCollectionId)
  {
    entity.setPropertyCollectionId(properyCollectionId);
  }
  
  @Override
  public Boolean getIsHidden()
  {
    return entity.getIsHidden();
  }
  
  @Override
  public void setIsHidden(Boolean isHidden)
  {
    entity.setIsHidden(isHidden);
  }
}
