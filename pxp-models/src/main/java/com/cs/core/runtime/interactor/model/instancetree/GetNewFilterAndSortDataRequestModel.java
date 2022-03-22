package com.cs.core.runtime.interactor.model.instancetree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cs.core.runtime.interactor.model.filter.IPropertyInstanceFilterModel;
import com.cs.core.runtime.interactor.model.filter.PropertyInstanceValueTypeFilterModer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@SuppressWarnings("rawtypes")
public class GetNewFilterAndSortDataRequestModel implements IGetNewFilterAndSortDataRequestModel{
  
  private static final long serialVersionUID = 1L;
  protected Integer from = 0;
  protected Integer size = 0;
  protected List<String>                       moduleEntities;
  protected Set<String>                        klassIdsHavingRP;
  protected Set<String>                        taxonomyIdsHavingRP;
  protected List<IPropertyInstanceFilterModel> attributes;
  protected List<IPropertyInstanceFilterModel> tags;
  protected String                             allSearch;
  protected List<String>                       translatableAttributeIds;
  protected Boolean                            isArchivalPortal = false;
  protected List<String>                       searchableAttributes;
  protected List<String>                       selectedTypes;
  protected List<String>                       selectedTaxonomyIds;
  protected List<INewApplicableFilterModel>    filterData;
  protected IPaginationInfoSortModel           paginatedSortInfo;
  protected IPaginationInfoFilterModel         paginatedFilterInfo;
  protected List<ISpecialUsecaseFiltersModel>  specialUsecaseFilters;
  protected String                             moduleId;
  protected String                             kpiId;
  protected List<String>                       idsToExclude;
  protected String                             clickedTaxonomyId;
  protected Boolean                            isBookmark; 
  protected List<String>                       majorTaxonomyIds;
  protected Boolean                            xrayEnabled      = false;
  
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
  public List<String> getModuleEntities()
  {
    if(moduleEntities == null) {
      moduleEntities = new ArrayList<>();
    }
    return moduleEntities;
  }

  @Override
  public void setModuleEntities(List<String> moduleEntities)
  {
    this.moduleEntities = moduleEntities;
  }

  @Override
  public Set<String> getKlassIdsHavingRP()
  {
    if (klassIdsHavingRP == null) {
      klassIdsHavingRP = new HashSet<>();
   }
    return klassIdsHavingRP;
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

  @Override
  public List<IPropertyInstanceFilterModel> getAttributes()
  {
    if (attributes == null) {
      attributes = new ArrayList<>();
   }
    return attributes;
  }

  @Override
  @JsonDeserialize(contentAs = PropertyInstanceValueTypeFilterModer.class)
  public void setAttributes(List<IPropertyInstanceFilterModel> attributes)
  {
    this.attributes = attributes;
  }

  @Override
  public List<IPropertyInstanceFilterModel> getTags()
  {
    if (tags == null) {
      tags = new ArrayList<>();
   }
    return tags;
  }

  @Override
  @JsonDeserialize(contentAs = PropertyInstanceValueTypeFilterModer.class)
  public void setTags(List<IPropertyInstanceFilterModel> tags)
  {
    this.tags = tags;
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
  public List<String> getTranslatableAttributeIds()
  {
    if(translatableAttributeIds == null) {
      translatableAttributeIds = new ArrayList<>();
    }
    return translatableAttributeIds;
  }

  @Override
  public void setTranslatableAttributeIds(List<String> translatableAttributeIds)
  {
    this.translatableAttributeIds = translatableAttributeIds;
  }

  @Override
  public Boolean getIsArchivePortal()
  {
    return isArchivalPortal;
  }

  @Override
  public void setIsArchivePortal(Boolean isArchivalPortal)
  {
    this.isArchivalPortal = isArchivalPortal;
  }

  @Override
  public List<String> getSearchableAttributeIds()
  {
    if(searchableAttributes == null) {
      searchableAttributes = new ArrayList<>();
    }
    return searchableAttributes;
  }

  @Override
  public void setSearchableAttributeIds(List<String> searchableAttributes)
  {
    this.searchableAttributes = searchableAttributes;
  }
  
  public List<String> getSelectedTypes()
  {
    if (selectedTypes == null) {
      selectedTypes = new ArrayList<>();
    }
    return selectedTypes;
  }
  
  @Override
  public void setSelectedTypes(List<String> selectedTypes)
  {
    this.selectedTypes = selectedTypes;
  }
  
  @Override
  public List<String> getSelectedTaxonomyIds()
  {
    if (selectedTaxonomyIds == null) {
      selectedTaxonomyIds = new ArrayList<>();
    }
    return selectedTaxonomyIds;
  }
  
  @Override
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds)
  {
    this.selectedTaxonomyIds = selectedTaxonomyIds;
  }
  
  @Override
  @JsonDeserialize(contentAs = NewApplicableFilterModel.class)
  public void setFilterData(List<INewApplicableFilterModel> filterData)
  {
    this.filterData = filterData;
  }
  
  @Override
  public List<INewApplicableFilterModel> getFilterData()
  {
    return this.filterData;
  }
  
  @Override
  public IPaginationInfoSortModel getPaginatedSortInfo()
  {
    return paginatedSortInfo;
  }
  
  @Override
  @JsonDeserialize(as=PaginationInfoSortModel.class)
  public void setPaginatedSortInfo(IPaginationInfoSortModel paginatedSortInfo)
  {
    this.paginatedSortInfo = paginatedSortInfo;
  }
  
  @Override
  public IPaginationInfoFilterModel getPaginatedFilterInfo()
  {
    return paginatedFilterInfo;
  }
  
  @Override
  @JsonDeserialize(as=PaginationInfoFilterModel.class)
  public void setPaginatedFilterInfo(IPaginationInfoFilterModel paginatedFilterInfo)
  {
    this.paginatedFilterInfo = paginatedFilterInfo;
  }

  @Override
  public List<ISpecialUsecaseFiltersModel> getSpecialUsecaseFilters()
  {
    if(specialUsecaseFilters == null) {
      specialUsecaseFilters = new ArrayList<>();
    }
    return specialUsecaseFilters;
  }

  @Override
  public void setSpecialUsecaseFilters(List<ISpecialUsecaseFiltersModel> specialUsecaseFilters)
  {
    this.specialUsecaseFilters = specialUsecaseFilters;
  }

  @Override
  public String getModuleId()
  {
    return moduleId;
  }

  @Override
  public void setModuleId(String moduleId)
  {
    this.moduleId = moduleId;
  }

  @Override
  public String getKpiId()
  {
    return kpiId;
  }

  @Override
  public void setKpiId(String kpiId)
  {
    this.kpiId = kpiId;
  }
  
  @Override
  public List<String> getIdsToExclude()
  {
    if (idsToExclude == null) {
      idsToExclude = new ArrayList<>();
    }
    return idsToExclude;
  }
  
  @Override
  public void setIdsToExclude(List<String> idsToExclude)
  {
    this.idsToExclude = idsToExclude;
  }

  @Override
  public String getClickedTaxonomyId()
  {
    return clickedTaxonomyId;
  }

  @Override
  public void setClickedTaxonomyId(String clickedTaxonomyId)
  {
    this.clickedTaxonomyId = clickedTaxonomyId;
  }

  @Override
  public Boolean getIsBookmark()
  {
    if (isBookmark == null) {
      isBookmark = false;
    }
    return isBookmark;
  }

  @Override
  public void setIsBookmark(Boolean isBookmark)
  {
    this.isBookmark = isBookmark;
  }
  
  @Override
  public List<String> getMajorTaxonomyIds() {
  if(majorTaxonomyIds == null) {
     majorTaxonomyIds = new ArrayList<String>();
  }
  return majorTaxonomyIds;
  }

  @Override
  public void setMajorTaxonomyIds(List<String> majorTaxonomyIds) {
  this.majorTaxonomyIds = majorTaxonomyIds;
  }

  @Override
  public Boolean getXrayEnabled()
  {
    return this.xrayEnabled;
  }

  @Override
  public void setXrayEnabled(Boolean xrayEnabled)
  {
    this.xrayEnabled = xrayEnabled;
  }
}
