package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.model.filter.IPropertyInstanceFilterModel;
import com.cs.core.runtime.interactor.model.filter.ISortModel;
import com.cs.core.runtime.interactor.model.filter.PropertyInstanceValueTypeFilterModer;
import com.cs.core.runtime.interactor.model.filter.SortModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class VariantFilterRequestModel implements IVariantFilterRequestModel {
  
  // Done
  protected List<ISortModel>                             sortOptions;
  protected String                                       allSearch;
  protected List<? extends IPropertyInstanceFilterModel> attributes;
  protected List<? extends IPropertyInstanceFilterModel> tags;
  protected Boolean                                      isRed;
  protected Boolean                                      isYellow;
  protected Boolean                                      isOrange;
  protected Boolean                                      isGreen;
  protected Integer                                      from;
  protected Integer                                      size;
  
  @Override
  public List<ISortModel> getSortOptions()
  {
    if (sortOptions == null) {
      sortOptions = new ArrayList<>();
    }
    return sortOptions;
  }
  
  @JsonDeserialize(contentAs = SortModel.class)
  @Override
  public void setSortOptions(List<ISortModel> sortOptions)
  {
    this.sortOptions = sortOptions;
  }
  
  @Override
  public Integer getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(Integer from)
  {
    this.from = from;
  }
  
  @Override
  public Integer getSize()
  {
    return size;
  }
  
  @Override
  public void setSize(Integer size)
  {
    this.size = size;
  }
  
  @Override
  public List<? extends IPropertyInstanceFilterModel> getAttributes()
  {
    if (attributes == null) {
      attributes = new ArrayList<>();
    }
    return attributes;
  }
  
  @JsonDeserialize(contentAs = PropertyInstanceValueTypeFilterModer.class)
  @Override
  public void setAttributes(List<? extends IPropertyInstanceFilterModel> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public List<? extends IPropertyInstanceFilterModel> getTags()
  {
    if (tags == null) {
      tags = new ArrayList<>();
    }
    return tags;
  }
  
  @JsonDeserialize(contentAs = PropertyInstanceValueTypeFilterModer.class)
  @Override
  public void setTags(List<? extends IPropertyInstanceFilterModel> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public Boolean getIsGreen()
  {
    return isGreen;
  }
  
  @Override
  public void setIsGreen(Boolean isGreen)
  {
    this.isGreen = isGreen;
  }
  
  @Override
  public Boolean getIsRed()
  {
    return isRed;
  }
  
  @Override
  public void setIsRed(Boolean isRed)
  {
    this.isRed = isRed;
  }
  
  @Override
  public Boolean getIsOrange()
  {
    return isOrange;
  }
  
  @Override
  public void setIsOrange(Boolean isOrange)
  {
    this.isOrange = isOrange;
  }
  
  @Override
  public Boolean getIsYellow()
  {
    return isYellow;
  }
  
  @Override
  public void setIsYellow(Boolean isYellow)
  {
    this.isYellow = isYellow;
  }
}
