package com.cs.core.runtime.interactor.model.instancetree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cs.core.config.interactor.model.instancetree.IGetInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.filter.IPropertyInstanceFilterModel;
import com.cs.core.runtime.interactor.model.filter.PropertyInstanceValueTypeFilterModer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@SuppressWarnings("rawtypes")
public class GetInstanceTreeRequestModel implements IGetInstanceTreeRequestModel {

  private static final long                    serialVersionUID = 1L;
  protected Integer                            from;
  protected Integer                            size;
  protected List<String>                       searchableAttributes;
  protected List<String>                       moduleEntities;
  protected List<String>                       side2LinkedVariantKrIds;
  protected Set<String>                        klassIdsHavingRP;
  protected Set<String>                        taxonomyIdsHavingRP;
  protected String                             moduleId;
  protected List<IPropertyInstanceFilterModel> attributes;
  protected List<IPropertyInstanceFilterModel> tags;
  protected List<String>                       xRayAttributes;
  protected List<String>                       xRayTags;
  protected String                             allSearch;
  protected List<IAppliedSortModel>            sortOptions;
  protected List<String>                       translatableAttributesIds;
  protected String                             kpiId;
  protected List<ISpecialUsecaseFiltersModel>  specialUsecaseFilters;
  protected Boolean                            isArchivalPortal = false;
  protected List<String>                       selectedTypes;
  protected List<String>                       selectedTaxonomyIds;
  protected List<INewApplicableFilterModel>    filterData;
  protected IPaginationInfoFilterModel         paginatedFilterInfo;
  protected List<String>                       idsToExclude;
  protected Boolean                            isFilterDataRequired = false;
  protected List<String>                       majorTaxonomyIds;
  protected Boolean                            xrayEnabled          = false;
  
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
  public List<String> getSide2LinkedVariantKrIds()
  {
    if (side2LinkedVariantKrIds == null) {
      side2LinkedVariantKrIds = new ArrayList<>();
    }
    return side2LinkedVariantKrIds;
  }

  @Override
  public void setSide2LinkedVariantKrIds(List<String> side2LinkedVariantKrIds)
  {
    this.side2LinkedVariantKrIds = side2LinkedVariantKrIds;
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
  public void setXRayAttributes(List<String> xRayAttributes)
  {
    this.xRayAttributes = xRayAttributes;
  }

  @Override
  public List<String> getXRayAttributes()
  {
    if (xRayAttributes == null) {
      xRayAttributes = new ArrayList<>();
   }
    return xRayAttributes;
  }

  @Override
  public void setXRayTags(List<String> xRayTags)
  {
    this.xRayTags = xRayTags;
  }

  @Override
  public List<String> getXRayTags()
  {
    if (xRayTags == null) {
      xRayTags = new ArrayList<>();
   }
    return xRayTags;
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
  public List<IAppliedSortModel> getSortOptions()
  {
    if (sortOptions == null) {
      sortOptions = new ArrayList<>();
   }
    return sortOptions;
  }

  @Override
  @JsonDeserialize(contentAs = AppliedSortModel.class)
  public void setSortOptions(List<IAppliedSortModel> sortOptions)
  {
    this.sortOptions = sortOptions;
  }

  @Override
  public List<String> getTranslatableAttributeIds()
  {
    if (translatableAttributesIds == null) {
      translatableAttributesIds = new ArrayList<>();
   }
    return translatableAttributesIds;
  }

  @Override
  public void setTranslatableAttributeIds(List<String> translatableAttributesIds)
  {
    this.translatableAttributesIds = translatableAttributesIds;
  }
  
  @Override
  public List<String> getSearchableAttributeIds()
  {
    if (searchableAttributes == null) {
      searchableAttributes = new ArrayList<>();
   }
    return searchableAttributes;
  }

  @Override
  public void setSearchableAttributeIds(List<String> searchableAttributes)
  {
    this.searchableAttributes = searchableAttributes;
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
  public List<ISpecialUsecaseFiltersModel> getSpecialUsecaseFilters()
  {
    if(specialUsecaseFilters == null) {
      specialUsecaseFilters = new ArrayList<ISpecialUsecaseFiltersModel>();
    }
    return specialUsecaseFilters;
  }

  @Override
  @JsonDeserialize(contentAs=SpecialUsecaseFiltersModel.class)
  public void setSpecialUsecaseFilters(List<ISpecialUsecaseFiltersModel> specialUsecaseFilters)
  {
    this.specialUsecaseFilters = specialUsecaseFilters;
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
  public Boolean getIsFilterDataRequired()
  {
    return isFilterDataRequired;
  }

  @Override
  public void setIsFilterDataRequired(Boolean isFilterDataRequired)
  {
    this.isFilterDataRequired = isFilterDataRequired;
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
