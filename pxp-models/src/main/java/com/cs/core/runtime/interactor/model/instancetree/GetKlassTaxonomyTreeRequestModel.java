package com.cs.core.runtime.interactor.model.instancetree;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.cs.core.runtime.interactor.model.configdetails.CategoryInformationModel;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.cs.core.runtime.interactor.model.filter.IPropertyInstanceFilterModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetKlassTaxonomyTreeRequestModel extends GetInstanceTreeRequestModel implements IGetKlassTaxonomyTreeRequestModel {
  
  private static final long                    serialVersionUID = 1L;
  protected List<ICategoryInformationModel>    taxonomyInfo;
  protected String                             clickedTaxonomyId;
  protected Integer                            from = 0;
  protected Integer                            size = 0;
  protected List<String>                       searchableAttributes;
  protected List<String>                       moduleEntities;
  protected Set<String>                        klassIdsHavingRP;
  protected Set<String>                        taxonomyIdsHavingRP;
  protected String                             moduleId;
  protected List<IPropertyInstanceFilterModel> attributes;
  protected List<IPropertyInstanceFilterModel> tags;
  protected String                             allSearch;
  protected List<String>                       translatableAttributesIds;
  protected String                             kpiId;
  protected List<ISpecialUsecaseFiltersModel>  specialUsecaseFilters;
  protected Boolean                            isArchivalPortal = false;
  protected List<String>                       selectedTypes;
  protected List<String>                       selectedTaxonomyIds;
  protected List<String>                       idsToExclude;
  protected String                             parentTaxonomyId; 
  protected List<String>                       majorTaxonomyIds;
  protected Boolean                            xrayEnabled      = false;
  protected String                             searchText;
  protected String                             selectedCategory;

  @Override
  public List<ICategoryInformationModel> getKlassTaxonomyInfo()
  {
    if (taxonomyInfo == null) {
      taxonomyInfo = new ArrayList<>();
    }
    return taxonomyInfo;
  }

  @Override
  @JsonDeserialize(contentAs = CategoryInformationModel.class)
  public void setKlassTaxonomyInfo(List<ICategoryInformationModel> taxonomyInfo)
  {
    this.taxonomyInfo = taxonomyInfo;
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
  public String getSelectedCategory()
  {
    return selectedCategory;
  }

  @Override
  public void setSelectedCategory(String selectedCategory)
  {
    this.selectedCategory = selectedCategory;
  }

  @Override
  public String getSearchText()
  {
    return searchText;
  }

  @Override
  public void setSearchText(String searchText)
  {
    this.searchText = searchText;
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
