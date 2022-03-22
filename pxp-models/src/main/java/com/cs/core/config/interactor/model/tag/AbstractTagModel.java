package com.cs.core.config.interactor.model.tag;

import com.cs.core.config.interactor.entity.concrete.tagtype.TagValue;
import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.ITagValue;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, defaultImpl = TagModel.class,
    include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
public abstract class AbstractTagModel implements ITagModel {
  
  private static final long serialVersionUID = 1L;
  
  protected ITag            entity;
  
  public AbstractTagModel(Tag tag)
  {
    entity = tag;
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
  
  @JsonIgnore
  @Override
  public IEntity getEntity()
  {
    return entity;
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
  public String getColor()
  {
    return entity.getColor();
  }
  
  @Override
  public void setColor(String color)
  {
    entity.setColor(color);
  }
  
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
  public ITreeEntity getParent()
  {
    return this.entity.getParent();
  }
  
  @JsonDeserialize(as = Tag.class)
  @Override
  public void setParent(ITreeEntity parent)
  {
    this.entity.setParent(parent);
  }
  
  @Override
  public List<? extends ITreeEntity> getChildren()
  {
    return this.entity.getChildren();
  }
  
  @JsonDeserialize(contentAs = Tag.class)
  @Override
  public void setChildren(List<? extends ITreeEntity> children)
  {
    this.entity.setChildren(children);
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
  public Boolean getIsMultiselect()
  {
    return this.entity.getIsMultiselect();
  }
  
  @Override
  public void setIsMultiselect(Boolean isMultiselect)
  {
    this.entity.setIsMultiselect(isMultiselect);
  }
  
  @Override
  public List<ITagValue> getTagValues()
  {
    return this.entity.getTagValues();
  }
  
  @JsonDeserialize(contentAs = TagValue.class)
  @Override
  public void setTagValues(List<ITagValue> tagValues)
  {
    this.entity.setTagValues(tagValues);
  }
  
  @Override
  public ITag getDefaultValue()
  {
    return entity.getDefaultValue();
  }
  
  @JsonDeserialize(as = Tag.class)
  @Override
  public void setDefaultValue(ITag defaultValue)
  {
    entity.setDefaultValue(defaultValue);
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
  public String getTagType()
  {
    return entity.getTagType();
  }
  
  @Override
  public void setTagType(String tagType)
  {
    entity.setTagType(tagType);
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
  public Boolean getIsDimensional()
  {
    return entity.getIsDimensional();
  }
  
  @Override
  public void setIsDimensional(Boolean isDimensional)
  {
    entity.setIsDimensional(isDimensional);
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
  public Boolean getShouldDisplay()
  {
    return entity.getShouldDisplay();
  }
  
  @Override
  public void setShouldDisplay(Boolean shouldDisplay)
  {
    entity.setShouldDisplay(shouldDisplay);
  }
  
  @Override
  public Boolean getIsFilterable()
  {
    return entity.getIsFilterable();
  }
  
  @Override
  public void setIsFilterable(Boolean isFilterable)
  {
    entity.setIsFilterable(isFilterable);
  }
  
  @Override
  public Boolean getIsForRelevance()
  {
    return entity.getIsForRelevance();
  }
  
  @Override
  public void setIsForRelevance(Boolean isForRelevance)
  {
    entity.setIsForRelevance(isForRelevance);
  }
  
  @Override
  public String getMappedTo()
  {
    return entity.getMappedTo();
  }
  
  @Override
  public void setMappedTo(String mappedToId)
  {
    entity.setMappedTo(mappedToId);
  }
  
  @Override
  public String getKlass()
  {
    
    return entity.getKlass();
  }
  
  @Override
  public void setKlass(String klass)
  {
    entity.setKlass(klass);
  }
  
  @Override
  public List<String> getAllowedTags()
  {
    return entity.getAllowedTags();
  }
  
  @Override
  public void setAllowedTags(List<String> allowedTags)
  {
    entity.setAllowedTags(allowedTags);
  }
  
  @Override
  public List<String> getTagValuesSequence()
  {
    return entity.getTagValuesSequence();
  }
  
  @Override
  public void setTagValuesSequence(List<String> tagValuesSequence)
  {
    entity.setTagValuesSequence(tagValuesSequence);
  }
  
  @Override
  public Boolean getIsGridEditable()
  {
    return entity.getIsGridEditable();
  }
  
  @Override
  public void setIsGridEditable(Boolean isGridEditable)
  {
    entity.setIsGridEditable(isGridEditable);
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
  public List<String> getAvailability()
  {
    return entity.getAvailability();
  }
  
  @Override
  public void setAvailability(List<String> availability)
  {
    entity.setAvailability(availability);
  }
  
  @Override
  public Integer getImageResolution()
  {
    return entity.getImageResolution();
  }
  
  @Override
  public void setImageResolution(Integer imageResolution)
  {
    entity.setImageResolution(imageResolution);
  }
  
  @Override
  public String getImageExtension()
  {
    return entity.getImageExtension();
  }
  
  @Override
  public void setImageExtension(String imageExtension)
  {
    entity.setImageExtension(imageExtension);
  }
  
  @Override
  public Boolean getIsVersionable()
  {
    return entity.getIsVersionable();
  }
  
  @Override
  public void setIsVersionable(Boolean isVersionable)
  {
    entity.setIsVersionable(isVersionable);
  }
  
  @Override
  public long getPropertyIID()
  {
    return entity.getPropertyIID();
  }
  
  @Override
  public void setPropertyIID(long IID)
  {
    entity.setPropertyIID(IID);
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
  
  @Override
  public Boolean getIsDisabled()
  {
    return entity.getIsDisabled();
  }
  
  @Override
  public void setIsDisabled(Boolean isDisabled)
  {
    entity.setIsDisabled(isDisabled);
  }
}
