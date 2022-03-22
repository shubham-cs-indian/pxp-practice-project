package com.cs.core.config.interactor.entity.tag;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type",
    visible = true)
public interface ITag extends ITagBasic {
  
  public static final String COLOR                = "color";
  public static final String IS_MULTI_SELECT      = "isMultiselect";
  public static final String DEFAULT_VALUE        = "defaultValue";
  public static final String TAG_TYPE             = "tagType";
  public static final String TAG_VALUES           = "tagValues";
  public static final String IS_DIMENSIONAL       = "isDimensional";
  public static final String SHOULD_DISPLAY       = "shouldDisplay";
  public static final String FOR_PIM              = "forPim";
  public static final String FOR_MAM              = "forMam";
  public static final String FOR_TARGET           = "forTarget";
  public static final String FOR_EDITORIAL        = "forEditorial";
  public static final String IS_FOR_RELEVANCE     = "isForRelevance";
  public static final String KLASS                = "klass";
  public static final String ALLOWED_TAGS         = "allowedTags";
  public static final String TAG_VALUES_SEQUENCE  = "tagValuesSequence";
  public static final String LINKED_MASTER_TAG_ID = "linkedMasterTagId";
  public static final String IS_FILTERABLE        = "isFilterable";
  public static final String AVAILABILITY         = "availability";
  public static final String IS_GRID_EDITABLE     = "isGridEditable";
  public static final String IMAGE_RESOLUTION     = "imageResolution";
  public static final String IMAGE_EXTENSION      = "imageExtension";
  public static final String PROPERTY_IID         = "propertyIID";
  public static final String IS_VERSIONABLE       = "isVersionable";
  public static final String IS_ROOT              = "isRoot";
  public static final String IS_DISABLED          = "isDisabled";
  
  public String getColor();
  
  public void setColor(String color);
  
  public Boolean getIsMultiselect();
  
  public void setIsMultiselect(Boolean isMultiselect);
  
  public ITag getDefaultValue();
  
  public void setDefaultValue(ITag defaultValue);
  
  public String getTagType();
  
  public void setTagType(String tagType);
  
  public List<ITagValue> getTagValues();
  
  public void setTagValues(List<ITagValue> tagValues);
  
  public Boolean getIsDimensional();
  
  public void setIsDimensional(Boolean isDimensional);
  
  public Boolean getShouldDisplay();
  
  public void setShouldDisplay(Boolean shouldDisplay);
  
  public Boolean getIsForRelevance();
  
  public void setIsForRelevance(Boolean isForRelevance);
  
  public String getMappedTo();
  
  public void setMappedTo(String mappedToId);
  
  public String getKlass();
  
  public void setKlass(String klass);
  
  public List<String> getAllowedTags();
  
  public void setAllowedTags(List<String> allowedTags);
  
  public List<String> getTagValuesSequence();
  
  public void setTagValuesSequence(List<String> tagValuesSequence);
  
  public Boolean getIsFilterable();
  
  public void setIsFilterable(Boolean isFilterable);
  
  public List<String> getAvailability();
  
  public void setAvailability(List<String> availability);
  
  public String getLinkedMasterTagId();
  
  public void setLinkedMasterTagId(String linkedMasterTagId);
  
  public Boolean getIsGridEditable();
  
  public void setIsGridEditable(Boolean isGridEditable);
  
  public String getImageExtension();
  
  public void setImageExtension(String imageExtension);
  
  public Integer getImageResolution();
  
  public void setImageResolution(Integer imageResolution);
  
  public Boolean getIsVersionable();
  
  public void setIsVersionable(Boolean isVersionable);
  
  public long getPropertyIID();
  
  public void setPropertyIID(long propertyIID);
  
  public Boolean getIsRoot();
  
  public void setIsRoot(Boolean isRoot);
  
  Boolean getIsDisabled();
  void setIsDisabled(Boolean isDisabled);
}
