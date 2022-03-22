package com.cs.core.config.interactor.entity.attribute;

import com.cs.core.config.interactor.entity.tag.IPropertyTag;
import com.cs.core.config.interactor.entity.tag.PropertyTag;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAttribute implements IAttribute {
  
  private static final long    serialVersionUID = 1L;
  
  protected String             id;
  protected String             label;
  protected String             description;
  protected String             tooltip;
  protected String             defaultValue     = "";
  protected Boolean            isMandatory;
  protected Boolean            isStandard       = false;
  protected String             placeholder;
  protected String             icon;
  protected String             iconKey;
  protected String             type             = this.getClass()
      .getName();
  protected Long               versionId;
  protected Long               versionTimestamp;
  protected String             lastModifiedBy;
  protected String             mappedToId;
  protected Boolean            isDisabled;
  protected Boolean            isSearchable;
  protected String             rendererType;
  protected List<IPropertyTag> attributeTags;
  protected String             code;
  protected Boolean            isSortable       = false;
  protected Boolean            isFilterable     = false;
  protected List<String>       availability     = new ArrayList<>();
  protected Boolean            isGridEditable   = false;
  protected Boolean            isTranslatable   = false;
  protected String             valueAsHtml      = "";
  protected long               propertyIID;
  protected Boolean            isVersionable    = true;
  
  public AbstractAttribute(String id, String label)
  {
    this.id = id;
    this.label = label;
  }
  
  public AbstractAttribute()
  {
    // Do Nothing
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
  public String getDescription()
  {
    return this.description;
  }
  
  @Override
  public void setDescription(String description)
  {
    this.description = description;
  }
  
  @Override
  public String getTooltip()
  {
    return this.tooltip;
  }
  
  @Override
  public void setTooltip(String tooltip)
  {
    this.tooltip = tooltip;
  }
  
  @Override
  public Boolean getIsMandatory()
  {
    return this.isMandatory;
  }
  
  @Override
  public void setIsMandatory(Boolean isMandatory)
  {
    this.isMandatory = isMandatory;
  }
  
  @Override
  public String getPlaceholder()
  {
    return this.placeholder;
  }
  
  @Override
  public void setPlaceholder(String placeholder)
  {
    this.placeholder = placeholder;
  }
  
  @Override
  public String getLabel()
  {
    return this.label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getIcon()
  {
    return this.icon;
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
    return this.id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getDefaultValue()
  {
    return this.defaultValue;
  }
  
  @Override
  public void setDefaultValue(String defaultValue)
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
  public Boolean getIsStandard()
  {
    return this.isStandard;
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    this.isStandard = isStandard;
  }
  
  @Override
  public String getMappedTo()
  {
    return mappedToId;
  }
  
  @Override
  public void setMappedTo(String mappedToId)
  {
    this.mappedToId = mappedToId;
  }
  
  @Override
  public Boolean getIsDisabled()
  {
    return isDisabled;
  }
  
  @Override
  public void setIsDisabled(Boolean isDisabled)
  {
    this.isDisabled = isDisabled;
  }
  
  @Override
  public Boolean getIsSearchable()
  {
    return isSearchable;
  }
  
  @Override
  public void setIsSearchable(Boolean isSearchable)
  {
    this.isSearchable = isSearchable;
  }
  
  @Override
  public List<IPropertyTag> getAttributeTags()
  {
    return attributeTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = PropertyTag.class)
  public void setAttributeTags(List<IPropertyTag> attributeTags)
  {
    this.attributeTags = attributeTags;
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
  public Boolean getIsSortable()
  {
    return isSortable;
  }
  
  @Override
  public void setIsSortable(Boolean isSortable)
  {
    this.isSortable = isSortable;
  }
  
  @Override
  public List<String> getAvailability()
  {
    if (this.availability == null) {
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
  public String getValueAsHtml()
  {
    return valueAsHtml;
  }
  
  @Override
  public void setValueAsHtml(String valueAsHtml)
  {
    this.valueAsHtml = valueAsHtml;
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
  public Boolean getIsTranslatable()
  {
    return isTranslatable;
  }
  
  @Override
  public void setIsTranslatable(Boolean isTranslatable)
  {
    this.isTranslatable = isTranslatable;
  }
  
  @Override
  public long getPropertyIID()
  {
    return propertyIID;
  }
  
  @Override
  public void setPropertyIID(long propertyIID)
  {
    this.propertyIID = propertyIID;
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
}
