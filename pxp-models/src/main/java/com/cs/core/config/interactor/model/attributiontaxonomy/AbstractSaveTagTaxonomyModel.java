package com.cs.core.config.interactor.model.attributiontaxonomy;

import com.cs.core.config.interactor.entity.attributiontaxonomy.ITagLevelEntity;
import com.cs.core.config.interactor.entity.attributiontaxonomy.MasterTaxonomy;
import com.cs.core.config.interactor.entity.attributiontaxonomy.TagLevelEntity;
import com.cs.core.config.interactor.entity.concrete.tagtype.TagValue;
import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.matchandmerge.AbstractSection;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.ITagValue;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.configdetails.DeletedSectionElementModel;
import com.cs.core.config.interactor.model.configdetails.IDeletedSectionElementModel;
import com.cs.core.config.interactor.model.configdetails.IModifiedSectionModel;
import com.cs.core.config.interactor.model.configdetails.ModifiedSectionModel;
import com.cs.core.config.interactor.model.klass.*;
import com.cs.core.config.interactor.model.template.IModifiedSequenceModel;
import com.cs.core.config.interactor.model.template.ModifiedSequenceModel;
import com.cs.core.runtime.interactor.model.context.ModifiedContextKlassModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractSaveTagTaxonomyModel extends AbstractTagTaxonomyModel
    implements ISaveTagTaxonomyModel {
  
  private static final long                              serialVersionUID = 1L;
  
  protected List<String>                                 addedKlasses;
  protected List<String>                                 deletedKlasses;
  
  protected List<? extends ISection>                     addedSections    = new ArrayList<>();
  protected List<IModifiedSectionModel>                  modifiedSections = new ArrayList<>();
  protected List<String>                                 deletedSections  = new ArrayList<>();
  protected List<? extends IModifiedSectionElementModel> modifiedElements = new ArrayList<>();
  protected List<IAddedSectionElementModel>              addedElements    = new ArrayList<>();
  protected List<IDeletedSectionElementModel>            deletedElements  = new ArrayList<>();
  protected List<String>                                 deletedTasks;
  protected List<String>                                 addedTasks;
  protected List<String>                                 deletedDataRules;
  protected List<String>                                 addedDataRules;
  protected IModifiedSequenceModel                       modifiedSequence;
  protected IAddedTaxonomyLevelModel                     addedLevel;
  protected String                                       deletedLevel;
  protected List<IContextKlassModel>                     addedContextKlasses;
  protected List<String>                                 deletedContextKlasses;
  protected List<IModifiedContextKlassModel>             modifiedContextKlasses;
  
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
  
  @JsonDeserialize(as = MasterTaxonomy.class)
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
  @JsonDeserialize(contentAs = MasterTaxonomy.class)
  public void setChildren(List<? extends ITreeEntity> children)
  {
    entity.setChildren(children);
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
  public List<String> getAddedDataRules()
  {
    if (addedDataRules == null) {
      addedDataRules = new ArrayList<>();
    }
    return addedDataRules;
  }
  
  @Override
  public void setAddedDataRules(List<String> addedDataRules)
  {
    this.addedDataRules = addedDataRules;
  }
  
  @Override
  public List<String> getDeletedDataRules()
  {
    if (deletedDataRules == null) {
      deletedDataRules = new ArrayList<>();
    }
    return deletedDataRules;
  }
  
  @Override
  public void setDeletedDataRules(List<String> deletedDataRules)
  {
    this.deletedDataRules = deletedDataRules;
  }
  
  @Override
  public List<String> getAddedTasks()
  {
    if (addedTasks == null) {
      addedTasks = new ArrayList<>();
    }
    return addedTasks;
  }
  
  @Override
  public void setAddedTasks(List<String> addedTasks)
  {
    this.addedTasks = addedTasks;
  }
  
  @Override
  public List<String> getDeletedTasks()
  {
    if (deletedTasks == null) {
      deletedTasks = new ArrayList<>();
    }
    return deletedTasks;
  }
  
  @Override
  public void setDeletedTasks(List<String> deletedTasks)
  {
    this.deletedTasks = deletedTasks;
  }
  
  @Override
  public List<String> getAddedAppliedKlasses()
  {
    if (addedKlasses == null) {
      addedKlasses = new ArrayList<>();
    }
    return addedKlasses;
  }
  
  @Override
  public void setAddedAppliedKlasses(List<String> addedKlasses)
  {
    this.addedKlasses = addedKlasses;
  }
  
  @Override
  public List<String> getDeletedAppliedKlasses()
  {
    if (deletedKlasses == null) {
      deletedKlasses = new ArrayList<>();
    }
    return deletedKlasses;
  }
  
  @Override
  public void setDeletedAppliedKlasses(List<String> deletedKlasses)
  {
    this.deletedKlasses = deletedKlasses;
  }
  
  @Override
  public List<? extends ISection> getAddedSections()
  {
    return this.addedSections;
  }
  
  @JsonDeserialize(contentAs = AbstractSection.class)
  @Override
  public void setAddedSections(List<? extends ISection> addedSections)
  {
    this.addedSections = addedSections;
  }
  
  @Override
  public List<String> getDeletedSections()
  {
    return this.deletedSections;
  }
  
  @Override
  public void setDeletedSections(List<String> deletedSectionIds)
  {
    this.deletedSections = deletedSectionIds;
  }
  
  @Override
  public List<IModifiedSectionModel> getModifiedSections()
  {
    if (modifiedSections == null) {
      modifiedSections = new ArrayList<>();
    }
    return modifiedSections;
  }
  
  @JsonDeserialize(contentAs = ModifiedSectionModel.class)
  @Override
  public void setModifiedSections(List<IModifiedSectionModel> modifiedSections)
  {
    this.modifiedSections = modifiedSections;
  }
  
  @Override
  public List<? extends IModifiedSectionElementModel> getModifiedElements()
  {
    return modifiedElements;
  }
  
  @JsonDeserialize(contentAs = AbstractModifiedSectionElementModel.class)
  @Override
  public void setModifiedElements(List<? extends IModifiedSectionElementModel> modifiedElements)
  {
    this.modifiedElements = modifiedElements;
  }
  
  @Override
  public List<IDeletedSectionElementModel> getDeletedElements()
  {
    if (deletedElements == null) {
      return new ArrayList<>();
    }
    return deletedElements;
  }
  
  @JsonDeserialize(contentAs = DeletedSectionElementModel.class)
  @Override
  public void setDeletedElements(List<IDeletedSectionElementModel> deletedElements)
  {
    this.deletedElements = deletedElements;
  }
  
  @Override
  public List<IAddedSectionElementModel> getAddedElements()
  {
    if (addedElements == null) {
      addedElements = new ArrayList<>();
    }
    return addedElements;
  }
  
  @JsonDeserialize(contentAs = AddedSectionElementModel.class)
  @Override
  public void setAddedElements(List<IAddedSectionElementModel> addedElements)
  {
    this.addedElements = addedElements;
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
  public IModifiedSequenceModel getModifiedSequence()
  {
    return modifiedSequence;
  }
  
  @Override
  @JsonDeserialize(as = ModifiedSequenceModel.class)
  public void setModifiedSequence(IModifiedSequenceModel modifiedSequence)
  {
    this.modifiedSequence = modifiedSequence;
  }
  
  @Override
  public IAddedTaxonomyLevelModel getAddedLevel()
  {
    return addedLevel;
  }
  
  @Override
  @JsonDeserialize(as = AddedTaxonomyLevelModel.class)
  public void setAddedLevel(IAddedTaxonomyLevelModel addedLevel)
  {
    this.addedLevel = addedLevel;
  }
  
  @Override
  public String getDeletedLevel()
  {
    return deletedLevel;
  }
  
  @Override
  public void setDeletedLevel(String deletedLevel)
  {
    this.deletedLevel = deletedLevel;
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
  public List<IContextKlassModel> getAddedContextKlasses()
  {
    if (addedContextKlasses == null) {
      addedContextKlasses = new ArrayList<>();
    }
    return addedContextKlasses;
  }
  
  @Override
  @JsonDeserialize(contentAs = ContextKlassModel.class)
  public void setAddedContextKlasses(List<IContextKlassModel> addedContextKlasses)
  {
    this.addedContextKlasses = addedContextKlasses;
  }
  
  @Override
  public List<String> getDeletedContextKlasses()
  {
    if (deletedContextKlasses == null) {
      deletedContextKlasses = new ArrayList<>();
    }
    return deletedContextKlasses;
  }
  
  @Override
  public void setDeletedContextKlasses(List<String> deletedContextKlasses)
  {
    this.deletedContextKlasses = deletedContextKlasses;
  }
  
  @Override
  public List<IModifiedContextKlassModel> getModifiedContextKlasses()
  {
    if (modifiedContextKlasses == null) {
      modifiedContextKlasses = new ArrayList<>();
    }
    return modifiedContextKlasses;
  }
  
  @Override
  @JsonDeserialize(contentAs = ModifiedContextKlassModel.class)
  public void setModifiedContextKlasses(List<IModifiedContextKlassModel> modifiedContextKlasses)
  {
    this.modifiedContextKlasses = modifiedContextKlasses;
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
  public long getPropertyIID()
  {
    return this.entity.getPropertyIID();
  }
  
  @Override
  public void setPropertyIID(long iid)
  {
    this.entity.setPropertyIID(iid);
  }
}
