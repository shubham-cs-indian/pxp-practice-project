package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.configdetails.ConfigDetailsForFilterInfoModel;
import com.cs.core.config.interactor.model.taxonomy.FilterDataModel;
import com.cs.core.config.interactor.model.taxonomy.IFilterDataModel;
import com.cs.core.config.interactor.model.taxonomy.ISortDataModel;
import com.cs.core.config.interactor.model.taxonomy.SortDataModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForFilterInfoModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class GetFilterInformationModel implements IGetFilterInformationModel {
  
  private static final long                  serialVersionUID = 1L;
  
  protected ISortDataModel                   sortData;
  protected IFilterDataModel                 filterData;
  protected List<ITag>                       defaultFilters;
  protected List<String>                     searchableAttributes;
  protected IConfigDetailsForFilterInfoModel configDetails;
  protected List<String>                     translatableAttributesIds;
  
  @Override
  public List<String> getTranslatableAttributesIds()
  {
    if (translatableAttributesIds == null) {
      translatableAttributesIds = new ArrayList<>();
    }
    return translatableAttributesIds;
  }
  
  @Override
  public void setTranslatableAttributesIds(List<String> translatableAttributesIds)
  {
    this.translatableAttributesIds = translatableAttributesIds;
  }
  
  @Override
  public ISortDataModel getSortData()
  {
    return sortData;
  }
  
  @JsonDeserialize(as = SortDataModel.class)
  @Override
  public void setSortData(ISortDataModel sortData)
  {
    this.sortData = sortData;
  }
  
  @Override
  public IFilterDataModel getFilterData()
  {
    return filterData;
  }
  
  @JsonDeserialize(as = FilterDataModel.class)
  @Override
  public void setFilterData(IFilterDataModel filterData)
  {
    this.filterData = filterData;
  }
  
  @JsonDeserialize(as = FilterDataModel.class)
  @Override
  public List<ITag> getDefaultFilterTags()
  {
    if (defaultFilters == null) {
      defaultFilters = new ArrayList<>();
    }
    return defaultFilters;
  }
  
  @JsonDeserialize(contentAs = Tag.class)
  @Override
  public void setDefaultFilterTags(List<ITag> defaultFilters)
  {
    this.defaultFilters = defaultFilters;
  }
  
  @Override
  public List<String> getSearchableAttributes()
  {
    return searchableAttributes;
  }
  
  @Override
  public void setSearchableAttributes(List<String> searchableAttributes)
  {
    this.searchableAttributes = searchableAttributes;
  }
  
  @Override
  public IConfigDetailsForFilterInfoModel getConfigDetails()
  {
    return configDetails;
  }
  
  @JsonDeserialize(as = ConfigDetailsForFilterInfoModel.class)
  @Override
  public void setConfigDetails(IConfigDetailsForFilterInfoModel configDetails)
  {
    this.configDetails = configDetails;
  }
}
