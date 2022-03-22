package com.cs.core.config.interactor.entity.attributiontaxonomy;

import com.cs.core.config.interactor.entity.concrete.tagtype.TagValue;
import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.ITagValue;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTagTaxonomy extends AbstractTaxonomy implements ITagTaxonomy {
  
  private static final long       serialVersionUID      = 1L;
  
  protected Boolean               isTag                 = false;
  protected Boolean               isTaxonomy            = false;
  
  // properties from taxonomy
  protected List<String>          tasks;
  protected List<String>          dataRules;
  protected List<String>          embeddedKlassIds;
  protected List<ITagLevelEntity> tagLevels;
  
  // properties from tag
  protected String                description;
  protected String                tooltip;
  protected Boolean               isMandatory           = false;
  protected Boolean               isStandard            = false;
  protected String                placeholder;
  protected String                type;
  protected String                tagType;
  protected String                color;
  protected List<ITagValue>       tagValues             = new ArrayList<ITagValue>();
  protected Boolean               isMultiselect         = false;
  protected ITag                  defaultValue;
  protected Long                  versionId;
  protected Long                  versionTimestamp;
  protected String                lastModifiedBy;
  protected Boolean               isDimensional         = false;
  protected Boolean               shouldDisplay         = false;
  protected Boolean               isForRelevance        = false;
  protected Boolean               forEditorial          = true;
  protected String                mappedTo;
  protected String                klass;
  protected String                linkedMasterTag;
  protected List<String>          allowedTags           = new ArrayList<>();
  protected List<String>          tagValuesSequence     = new ArrayList<>();
  protected List<String>          tagLevelSequence;
  protected Boolean               isAttributionTaxonomy = true;
  protected Boolean               isGridEditable        = false;
  protected Boolean               isFilterable;
  protected List<String>          availability          = new ArrayList<>();
  protected String                linkedLevelId;
  protected String                imageExtension;
  protected Integer               imageResolution;
  protected long                  iid;
  protected Boolean               isRoot                = false;
  protected Boolean               isDisabled            = false;
  
  /**
   * ***********************Tag Taxonomy****************************
   */
  @Override
  public List<String> getDataRules()
  {
    if (dataRules == null) {
      dataRules = new ArrayList<>();
    }
    return dataRules;
  }
  
  @Override
  public void setDataRules(List<String> dataRules)
  {
    this.dataRules = dataRules;
  }
  
  @Override
  public List<String> getTasks()
  {
    if (tasks == null) {
      tasks = new ArrayList<>();
    }
    return tasks;
  }
  
  @Override
  public void setTasks(List<String> tasks)
  {
    this.tasks = tasks;
  }
  
  @Override
  public List<String> getEmbeddedKlassIds()
  {
    return embeddedKlassIds;
  }
  
  @Override
  public void setEmbeddedKlassIds(List<String> embeddedKlassIds)
  {
    if (embeddedKlassIds == null) {
      embeddedKlassIds = new ArrayList<>();
    }
    this.embeddedKlassIds = embeddedKlassIds;
  }
  
  @Override
  public List<ITagLevelEntity> getTagLevels()
  {
    return tagLevels;
  }
  
  @Override
  @JsonDeserialize(contentAs = TagLevelEntity.class)
  public void setTagLevels(List<ITagLevelEntity> tagLevels)
  {
    this.tagLevels = tagLevels;
  }
  
  /**
   * ******************Tags**************************
   */
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
  public String getDescription()
  {
    return description;
  }
  
  @Override
  public void setDescription(String description)
  {
    this.description = description;
  }
  
  @Override
  public String getTooltip()
  {
    return tooltip;
  }
  
  @Override
  public void setTooltip(String tooltip)
  {
    this.tooltip = tooltip;
  }
  
  @Override
  public Boolean getIsMandatory()
  {
    return isMandatory;
  }
  
  @Override
  public void setIsMandatory(Boolean isMandatory)
  {
    this.isMandatory = isMandatory;
  }
  
  @Override
  public String getPlaceholder()
  {
    return placeholder;
  }
  
  @Override
  public void setPlaceholder(String placeholder)
  {
    this.placeholder = placeholder;
  }
  
  @Override
  public String getColor()
  {
    return color;
  }
  
  @Override
  public void setColor(String color)
  {
    this.color = color;
  }
  
  @Override
  public List<? extends ITreeEntity> getChildren()
  {
    return this.children;
  }
  
  @Override
  public String getType()
  {
    return this.type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public Boolean getIsMultiselect()
  {
    return this.isMultiselect;
  }
  
  @Override
  public void setIsMultiselect(Boolean isMultiselect)
  {
    this.isMultiselect = isMultiselect;
  }
  
  @Override
  public ITag getDefaultValue()
  {
    return defaultValue;
  }
  
  @JsonDeserialize(as = Tag.class)
  @Override
  public void setDefaultValue(ITag defaultValue)
  {
    this.defaultValue = defaultValue;
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
  public String getTagType()
  {
    return this.tagType;
  }
  
  @Override
  public void setTagType(String tagType)
  {
    this.tagType = tagType;
  }
  
  @Override
  public List<ITagValue> getTagValues()
  {
    return this.tagValues;
  }
  
  @JsonDeserialize(contentAs = TagValue.class)
  @Override
  public void setTagValues(List<ITagValue> tagValues)
  {
    this.tagValues = tagValues;
  }
  
  @Override
  public Boolean getIsDimensional()
  {
    return isDimensional;
  }
  
  @Override
  public void setIsDimensional(Boolean isDimensional)
  {
    this.isDimensional = isDimensional;
  }
  
  @Override
  public Boolean getIsStandard()
  {
    return isStandard;
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    this.isStandard = isStandard;
  }
  
  @Override
  public Boolean getShouldDisplay()
  {
    return shouldDisplay;
  }
  
  @Override
  public void setShouldDisplay(Boolean shouldDisplay)
  {
    this.shouldDisplay = shouldDisplay;
  }
  
  @Override
  public Boolean getIsForRelevance()
  {
    return isForRelevance;
  }
  
  @Override
  public void setIsForRelevance(Boolean isForRelevance)
  {
    this.isForRelevance = isForRelevance;
  }
  
  @Override
  public String getMappedTo()
  {
    return mappedTo;
  }
  
  @Override
  public void setMappedTo(String mappedToId)
  {
    mappedTo = mappedToId;
  }
  
  @Override
  public String getKlass()
  {
    
    return klass;
  }
  
  @Override
  public void setKlass(String klass)
  {
    this.klass = klass;
  }
  
  @Override
  public List<String> getAllowedTags()
  {
    return allowedTags;
  }
  
  @Override
  public void setAllowedTags(List<String> allowedTags)
  {
    this.allowedTags = allowedTags;
  }
  
  @Override
  public List<String> getTagValuesSequence()
  {
    return tagValuesSequence;
  }
  
  @Override
  public void setTagValuesSequence(List<String> tagValuesSequence)
  {
    this.tagValuesSequence = tagValuesSequence;
  }
  
  @Override
  public Boolean getIsTag()
  {
    return isTag;
  }
  
  @Override
  public void setIsTag(Boolean isTaxonomyAndTag)
  {
    this.isTag = isTaxonomyAndTag;
  }
  
  @Override
  public List<String> getTagLevelSequence()
  {
    if (tagLevelSequence == null) {
      tagLevelSequence = new ArrayList<String>();
    }
    return tagLevelSequence;
  }
  
  @Override
  public void setTagLevelSequence(List<String> tagLevelSequence)
  {
    this.tagLevelSequence = tagLevelSequence;
  }
  
  @Override
  public Boolean getIsTaxonomy()
  {
    return isTaxonomy;
  }
  
  @Override
  public void setIsTaxonomy(Boolean isTaxonomy)
  {
    this.isTaxonomy = isTaxonomy;
  }
  
  @Override
  public Boolean getIsGridEditable()
  {
    return isGridEditable;
  }
  
  @Override
  public void setIsGridEditable(Boolean isGridEditable)
  {
    this.isGridEditable = isGridEditable;
  }
  
  @Override
  public Boolean getIsFilterable()
  {
    return isFilterable;
  }
  
  @Override
  public void setIsFilterable(Boolean isFilterable)
  {
    this.isFilterable = isFilterable;
  }
  
  @Override
  public List<String> getAvailability()
  {
    return availability;
  }
  
  @Override
  public void setAvailability(List<String> availability)
  {
    this.availability = availability;
  }
  
  @Override
  public Integer getImageResolution()
  {
    return imageResolution;
  }
  
  @Override
  public void setImageResolution(Integer imageResolution)
  {
    this.imageResolution = imageResolution;
  }
  
  @Override
  public String getImageExtension()
  {
    return imageExtension;
  }
  
  @Override
  public void setImageExtension(String imageExtension)
  {
    this.imageExtension = imageExtension;
  }
  
  @Override
  public long getPropertyIID()
  {
    return this.iid;
  }
  
  @Override
  public void setPropertyIID(long iid)
  {
    this.iid = iid;
  }
  
  @Override
  public Boolean getIsRoot()
  {
    return this.isRoot;
  }
  
  @Override
  public void setIsRoot(Boolean isRoot)
  {
    this.isRoot = isRoot;
  }
  
  @Override
  public Boolean getIsDisabled()
  {
    return this.isDisabled;
  }
  
  @Override
  public void setIsDisabled(Boolean isDisabled)
  {
    this.isDisabled = isDisabled;
  }
  
}
