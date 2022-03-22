package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.entity.timerange.InstanceTimeRange;
import com.cs.core.runtime.interactor.model.filter.IPropertyInstanceFilterModel;
import com.cs.core.runtime.interactor.model.filter.ISortModel;
import com.cs.core.runtime.interactor.model.filter.PropertyInstanceValueTypeFilterModer;
import com.cs.core.runtime.interactor.model.filter.SortModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class GetPropertiesVariantInstancesInTableViewRequestModel
    implements IGetPropertiesVariantInstanceInTableViewRequestModel {
  
  private static final long                              serialVersionUID = 1L;
  
  protected String                                       contentId;
  protected String                                       contextId;
  protected IVariantFilterRequestModel                   filterInfo;
  protected String                                       parentId;
  protected List<ISortModel>                             sortOptions;
  protected String                                       allSearch;
  protected List<? extends IPropertyInstanceFilterModel> attributes;
  protected List<? extends IPropertyInstanceFilterModel> tags;
  protected Integer                                      from;
  protected Integer                                      size;
  protected String                                       attributeId;
  protected String                                       templateId;
  protected List<String>                                 columnIds;
  protected IInstanceTimeRange                           timeRange;
  
  @Override
  public List<String> getColumnIds()
  {
    return columnIds;
  }
  
  @Override
  public void setColumnIds(List<String> columnIds)
  {
    this.columnIds = columnIds;
  }
  
  @Override
  public String getTemplateId()
  {
    return templateId;
  }
  
  @Override
  public void setTemplateId(String templateId)
  {
    this.templateId = templateId;
  }
  
  @Override
  public String getAttributeId()
  {
    return attributeId;
  }
  
  @Override
  public void setAttributeId(String attributeId)
  {
    this.attributeId = attributeId;
  }
  
  @Override
  public String getKlassInstanceId()
  {
    return contentId;
  }
  
  @Override
  public void setKlassInstanceId(String contentId)
  {
    this.contentId = contentId;
  }
  
  @Override
  public String getContextId()
  {
    return contextId;
  }
  
  @Override
  public void setContextId(String contextId)
  {
    this.contextId = contextId;
  }
  
  @Override
  public IVariantFilterRequestModel getFilterInfo()
  {
    return filterInfo;
  }
  
  @JsonDeserialize(as = VariantFilterRequestModel.class)
  @Override
  public void setFilterInfo(IVariantFilterRequestModel filterInfo)
  {
    this.filterInfo = filterInfo;
  }
  
  @Override
  public String getParentId()
  {
    return parentId;
  }
  
  @Override
  public void setParentId(String parentId)
  {
    this.parentId = parentId;
  }
  
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
  public IInstanceTimeRange getTimeRange()
  {
    return timeRange;
  }
  
  @Override
  @JsonDeserialize(as = InstanceTimeRange.class)
  public void setTimeRange(IInstanceTimeRange timeRange)
  {
    this.timeRange = timeRange;
  }
  
  @Override
  public String getAllSearch()
  {
    return allSearch;
  }
  
  @Override
  public void setAllSearch(String allSearch)
  {
    this.allSearch = allSearch;
  }
}
