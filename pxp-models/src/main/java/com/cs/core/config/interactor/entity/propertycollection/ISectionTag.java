package com.cs.core.config.interactor.entity.propertycollection;

import com.cs.core.config.interactor.entity.klass.IKlassTagValues;
import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.config.interactor.entity.tag.ITag;

import java.util.List;

public interface ISectionTag extends ISectionElement {
  
  String TAG                 = "tag";
  String TAG_TYPE            = "tagType";
  String IS_MULTI_SELECT     = "isMultiselect";
  String DEFAULT_VALUE       = "defaultValue";
  String SELECTED_TAG_VALUES = "selectedTagValues";
  String IS_VERSIONABLE      = "isVersionable";
  
  public ITag getTag();
  
  public void setTag(ITag tag);
  
  public String getTagType();
  
  public void setTagType(String tagType);
  
  public Boolean getIsMultiselect();
  
  public void setIsMultiselect(Boolean isMultiselect);
  
  public List<IIdRelevance> getDefaultValue();
  
  public void setDefaultValue(List<IIdRelevance> defaultValue);
  
  public List<IKlassTagValues> getSelectedTagValues();
  
  public void setSelectedTagValues(List<IKlassTagValues> selectedTagValues);
  
  public Boolean getIsVersionable();
  
  public void setIsVersionable(Boolean isVersionable);
}
