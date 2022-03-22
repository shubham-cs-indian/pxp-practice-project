package com.cs.core.runtime.interactor.model.searchable;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.taxonomy.IFilterDataModel;
import com.cs.core.config.interactor.model.taxonomy.ISortDataModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForFilterInfoModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.io.Serializable;
import java.util.List;

public interface IGetFilterInformationModel extends IModel, Serializable {
  
  public static final String SORT_DATA                   = "sortData";
  public static final String FILTER_DATA                 = "filterData";
  public static final String DEFAULT_FILTER_TAGS         = "defaultFilterTags";
  public static final String SEARCHABLE_ATTRIBUTES       = "searchableAttributes";
  public static final String CONFIG_DETAILS              = "configDetails";
  public static final String TRANSLATABLE_ATTRIBUTES_IDS = "translatableAttributesIds";
  
  public ISortDataModel getSortData();
  
  public void setSortData(ISortDataModel sortData);
  
  public IFilterDataModel getFilterData();
  
  public void setFilterData(IFilterDataModel filterData);
  
  public List<ITag> getDefaultFilterTags();
  
  public void setDefaultFilterTags(List<ITag> defaultFilters);
  
  public List<String> getSearchableAttributes();
  
  public void setSearchableAttributes(List<String> searchableAttributes);
  
  public IConfigDetailsForFilterInfoModel getConfigDetails();
  
  public void setConfigDetails(IConfigDetailsForFilterInfoModel configDetails);
  
  public List<String> getTranslatableAttributesIds();
  
  public void setTranslatableAttributesIds(List<String> translatableAttributesIds);
}
