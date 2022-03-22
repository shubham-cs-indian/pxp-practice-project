package com.cs.core.runtime.interactor.model.variants;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.klass.GetFilterInfoModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.ReferencedContextModel;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.entity.timerange.InstanceTimeRange;
import com.cs.core.runtime.interactor.model.filter.IPropertyInstanceFilterModel;
import com.cs.core.runtime.interactor.model.filter.ISortModel;
import com.cs.core.runtime.interactor.model.filter.PropertyInstanceValueTypeFilterModer;
import com.cs.core.runtime.interactor.model.filter.SortModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.*;

public class GetPropertiesVariantInstancesInTableViewStrategyModel
    implements IGetPropertiesVariantInstancesInTableViewRequestStrategyModel {
  
  private static final long                              serialVersionUID = 1L;
  protected String                                       contentId;
  protected String                                       contextId;
  protected String                                       variantInstanceId;
  protected List<ISortModel>                             sortOptions;
  protected String                                       allSearch;
  protected List<? extends IPropertyInstanceFilterModel> attributes;
  protected List<? extends IPropertyInstanceFilterModel> tags;
  protected Integer                                      from;
  protected Integer                                      size;
  protected Map<String, ITag>                            referencedTags;
  protected IReferencedContextModel                      referencedVariantContexts;
  protected Map<String, IReferencedSectionElementModel>  referencedElements;
  protected String                                       currentUserId;
  protected IGetFilterInfoModel                          filterInfo;
  protected List<String>                                 columnIds;
  protected String                                       attributeId;
  protected IInstanceTimeRange                           timeRange;
  protected List<String>                                 entities;
  protected Set<String>                                  klassIdsHavingRP = new HashSet<>();
  protected Set<String>                                  taxonomyIdsHavingRP;
  
  @Override
  public List<String> getColumnIds()
  {
    if (columnIds == null) {
      columnIds = new ArrayList<String>();
    }
    return columnIds;
  }
  
  @Override
  public void setColumnIds(List<String> columnIds)
  {
    this.columnIds = columnIds;
  }
  
  @Override
  public String getCurrentUserId()
  {
    return currentUserId;
  }
  
  @Override
  public void setCurrentUserId(String currentUserId)
  {
    this.currentUserId = currentUserId;
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
  public String getVariantInstanceId()
  {
    return variantInstanceId;
  }
  
  @Override
  public void setVariantInstanceId(String parentVariantId)
  {
    this.variantInstanceId = parentVariantId;
  }
  
  @Override
  public List<ISortModel> getSortOptions()
  {
    if (sortOptions == null) {
      sortOptions = new ArrayList<>();
      ISortModel sortModel = new SortModel();
      sortModel.setSortField(IStandardConfig.StandardProperty.createdonattribute.toString());
      sortModel.setSortOrder("asc");
      sortOptions.add(sortModel);
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
  public Map<String, ITag> getReferencedTags()
  {
    return referencedTags;
  }
  
  @JsonDeserialize(contentAs = Tag.class)
  @Override
  public void setReferencedTags(Map<String, ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
  
  @Override
  public IReferencedContextModel getReferencedVariantContexts()
  {
    return referencedVariantContexts;
  }
  
  @Override
  @JsonDeserialize(as = ReferencedContextModel.class)
  public void setReferencedVariantContexts(IReferencedContextModel referencedVariantContexts)
  {
    this.referencedVariantContexts = referencedVariantContexts;
  }
  
  @Override
  public Map<String, IReferencedSectionElementModel> getReferencedElements()
  {
    return referencedElements;
  }
  
  @Override
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements)
  {
    this.referencedElements = referencedElements;
  }
  
  @Override
  public IGetFilterInfoModel getFilterInfo()
  {
    return filterInfo;
  }
  
  @Override
  @JsonDeserialize(as = GetFilterInfoModel.class)
  public void setFilterInfo(IGetFilterInfoModel filterInfo)
  {
    this.filterInfo = filterInfo;
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
  public List<String> getEntities()
  {
    return entities;
  }
  
  @Override
  public void setEntities(List<String> entities)
  {
    this.entities = entities;
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
  
  @Override
  public Set<String> getKlassIdsHavingRP()
  {
    return this.klassIdsHavingRP;
  }
  
  @Override
  public void setKlassIdsHavingRP(Set<String> klassIdsHavingRP)
  {
    this.klassIdsHavingRP = klassIdsHavingRP;
  }
  
  @Override
  public Set<String> getTaxonomyIdsHavingRP()
  {
    if (taxonomyIdsHavingRP == null) {
      taxonomyIdsHavingRP = new HashSet<>();
    }
    return taxonomyIdsHavingRP;
  }
  
  @Override
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP)
  {
    this.taxonomyIdsHavingRP = taxonomyIdsHavingRP;
  }
}
