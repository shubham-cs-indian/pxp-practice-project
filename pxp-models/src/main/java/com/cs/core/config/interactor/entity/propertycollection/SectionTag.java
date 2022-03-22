package com.cs.core.config.interactor.entity.propertycollection;

import com.cs.core.config.interactor.entity.klass.IKlassTagValues;
import com.cs.core.config.interactor.entity.klass.KlassTagValues;
import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.IdRelevance;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class SectionTag extends AbstractSectionElement implements ISectionTag {
  
  private static final long       serialVersionUID  = 1L;
  
  protected Tag                   tag;
  protected String                tagType;
  protected Boolean               isMultiselect;
  protected List<IIdRelevance>    defaultValue      = new ArrayList<>();
  protected List<IKlassTagValues> selectedTagValues = new ArrayList<>();
  protected Boolean               isVersionable;
  
  @Override
  public ITag getTag()
  {
    return tag;
  }
  
  @JsonDeserialize(as = Tag.class)
  @Override
  public void setTag(ITag tag)
  {
    this.tag = (Tag) tag;
  }
  
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
  
  @JsonDeserialize(contentAs = KlassTagValues.class)
  @Override
  public void setSelectedTagValues(List<IKlassTagValues> selectedTagValues)
  {
    this.selectedTagValues = selectedTagValues;
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
