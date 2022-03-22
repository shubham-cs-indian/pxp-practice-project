package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.klass.IKlassTagValues;
import com.cs.core.config.interactor.entity.klass.KlassTagValues;
import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.IdRelevance;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ReferencedSectionTagModel extends AbstractReferencedSectionElementModel
    implements IReferencedSectionTagModel {
  
  private static final long    serialVersionUID  = 1L;
  
  protected String             tagType;
  protected Boolean            isMultiselect;
  protected List<IIdRelevance> defaultValue      = new ArrayList<>();
  protected Boolean            isVersionable;
  List<IKlassTagValues>        selectedTagValues = new ArrayList<>();
  
  @Override
  public String getTagType()
  {
    return tagType;
  }
  
  @Override
  public void setTagType(String tagType)
  {
    this.tagType = tagType;
  }
  
  @Override
  public Boolean getIsMultiselect()
  {
    return isMultiselect;
  }
  
  @Override
  public void setIsMultiselect(Boolean isMultiselect)
  {
    this.isMultiselect = isMultiselect;
  }
  
  @Override
  public List<IIdRelevance> getDefaultValue()
  {
    if (defaultValue == null) {
      defaultValue = new ArrayList<>();
    }
    return defaultValue;
  }
  
  @JsonDeserialize(contentAs = IdRelevance.class)
  @Override
  public void setDefaultValue(List<IIdRelevance> defaultValue)
  {
    this.defaultValue = defaultValue;
  }
  
  @Override
  public List<IKlassTagValues> getSelectedTagValues()
  {
    
    return selectedTagValues;
  }
  
  @Override
  @JsonDeserialize(contentAs = KlassTagValues.class)
  public void setSelectedTagValues(List<IKlassTagValues> selectedTagValues)
  {
    this.selectedTagValues = selectedTagValues;
  }
  
  /**
   * ************************* ignored properties
   * ********************************
   */
  @JsonIgnore
  @Override
  public ITag getTag()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setTag(ITag tag)
  {
    // TODO Auto-generated method stub
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
