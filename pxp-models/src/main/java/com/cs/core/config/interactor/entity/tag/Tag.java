package com.cs.core.config.interactor.entity.tag;

import com.cs.core.config.interactor.entity.concrete.tagtype.TagValue;
import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class Tag implements ITag {
  
  private static final long serialVersionUID  = 1L;
  
  protected String          id;
  
  protected String          description;
  
  protected String          tooltip;
  
  protected Boolean         isMandatory       = false;
  
  protected Boolean         isStandard        = false;
  
  protected String          placeholder;
  
  protected String          label;
  
  protected String          type;
  
  protected String          tagType;
  
  protected String          icon;
  
  protected String          iconKey;
  
  protected String          color;
  
  protected Tag             parent;
  
  protected List<Tag>       children          = new ArrayList<>();
  
  protected List<ITagValue> tagValues         = new ArrayList<ITagValue>();
  
  protected Boolean         isMultiselect     = false;
  
  protected ITag            defaultValue;
  
  protected Long            versionId;
  
  protected Long            versionTimestamp;
  
  protected String          lastModifiedBy;
  
  protected Boolean         isDimensional     = false;
  
  protected Boolean         shouldDisplay     = false;
  
  protected Boolean         isForRelevance    = false;
  
  protected Boolean         forEditorial      = true;
  
  protected String          mappedTo;
  
  protected String          klass;
  
  protected String          linkedMasterTagId;
  
  protected List<String>    allowedTags       = new ArrayList<>();
  
  protected List<String>    tagValuesSequence = new ArrayList<>();
  
  protected String          code;
  
  protected Boolean         isFilterable      = false;
  
  protected Boolean         isGridEditable    = false;
  
  protected List<String>    availability      = new ArrayList<>();
  
  protected Integer         imageResolution;
  
  protected String          imageExtension;
  
  protected Boolean         isVersionable     = false;
  
  protected long            propertyIID;
  
  protected Boolean         isRoot            = false;
  
  protected Boolean         isDisabled        = false;
  
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
  public ITreeEntity getParent()
  {
    return this.parent;
  }
  
  @JsonDeserialize(as = Tag.class)
  @Override
  public void setParent(ITreeEntity parent)
  {
    this.parent = (Tag) parent;
  }
  
  @Override
  public List<? extends ITreeEntity> getChildren()
  {
    return this.children;
  }
  
  @JsonDeserialize(contentAs = Tag.class)
  @SuppressWarnings("unchecked")
  @Override
  public void setChildren(List<? extends ITreeEntity> children)
  {
    this.children = (List<Tag>) children;
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
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Tag other = (Tag) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    }
    else if (!id.equals(other.id))
      return false;
    return true;
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
    if (availability == null) {
      return new ArrayList<>();
    }
    return availability;
  }
  
  @Override
  public void setAvailability(List<String> availability)
  {
    this.availability = availability;
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
  public Boolean getIsVersionable()
  {
    return isVersionable;
  }
  
  @Override
  public void setIsVersionable(Boolean isVersionable)
  {
    this.isVersionable = isVersionable;
  }
  
  @Override
  public long getPropertyIID()
  {
    return this.propertyIID;
  }
  
  @Override
  public void setPropertyIID(long iid)
  {
    this.propertyIID = iid;
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
