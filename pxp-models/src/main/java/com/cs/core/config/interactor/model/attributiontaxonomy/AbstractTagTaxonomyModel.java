package com.cs.core.config.interactor.model.attributiontaxonomy;

import com.cs.core.config.interactor.entity.attributiontaxonomy.ITagLevelEntity;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ITagTaxonomy;
import com.cs.core.config.interactor.entity.attributiontaxonomy.TagLevelEntity;
import com.cs.core.config.interactor.entity.concrete.tagtype.TagValue;
import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.matchandmerge.AbstractSection;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.ITagValue;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Map;

public abstract class AbstractTagTaxonomyModel implements ITagTaxonomy, IModel {
  
  private static final long serialVersionUID = 1L;
  
  protected ITagTaxonomy    entity;
  
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
    return entity.getIconKey();
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    entity.setIconKey(iconKey);
  }
  
  @Override
  public List<? extends ITreeEntity> getChildren()
  {
    return entity.getChildren();
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
  @JsonDeserialize(contentAs = AbstractSection.class)
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
  public List<String> getDataRules()
  {
    return entity.getDataRules();
  }
  
  @Override
  public void setDataRules(List<String> dataRules)
  {
    entity.setDataRules(dataRules);
  }
  
  @Override
  public List<String> getTasks()
  {
    return entity.getTasks();
  }
  
  @Override
  public void setTasks(List<String> tasks)
  {
    entity.setTasks(tasks);
  }
  
  @Override
  public List<String> getEmbeddedKlassIds()
  {
    return entity.getEmbeddedKlassIds();
  }
  
  @Override
  public void setEmbeddedKlassIds(List<String> embeddedKlassIds)
  {
    entity.setEmbeddedKlassIds(embeddedKlassIds);
  }
  
  @Override
  public List<ITagLevelEntity> getTagLevels()
  {
    return entity.getTagLevels();
  }
  
  @Override
  @JsonDeserialize(contentAs = TagLevelEntity.class)
  public void setTagLevels(List<ITagLevelEntity> tagLevels)
  {
    entity.setTagLevels(tagLevels);
  }
  
  @Override
  public Boolean getIsTag()
  {
    return entity.getIsTag();
  }
  
  @Override
  public void setIsTag(Boolean isTaxonomyAndTag)
  {
    entity.setIsTag(isTaxonomyAndTag);
  }
  
  @Override
  public List<String> getTagLevelSequence()
  {
    return entity.getTagLevelSequence();
  }
  
  @Override
  public void setTagLevelSequence(List<String> tagLevelSequence)
  {
    entity.setTagLevelSequence(tagLevelSequence);
  }
  
  @Override
  public Boolean getIsTaxonomy()
  {
    return entity.getIsTaxonomy();
  }
  
  @Override
  public void setIsTaxonomy(Boolean isTaxonomyAndTag)
  {
    entity.setIsTaxonomy(isTaxonomyAndTag);
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
  public Boolean getIsFilterable()
  {
    return entity.getIsFilterable();
  }
  
  @Override
  public void setIsFilterable(Boolean isSortable)
  {
    entity.setIsFilterable(isSortable);
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
