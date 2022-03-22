package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.base.interactor.model.ModuleEntitiesWithUserIdModel;
import com.cs.core.runtime.interactor.model.instancetree.IPaginationInfoFilterModel;
import com.cs.core.runtime.interactor.model.instancetree.IPaginationInfoSortModel;
import com.cs.core.runtime.interactor.model.instancetree.PaginationInfoFilterModel;
import com.cs.core.runtime.interactor.model.instancetree.PaginationInfoSortModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ConfigDetailsForFilterAndSortInfoRequestModel extends ModuleEntitiesWithUserIdModel
      implements IConfigDetailsForFilterAndSortInfoRequestModel {
  
  private static final long         serialVersionUID = 1L;
  protected List<String>               attributeIds;
  protected List<String>               tagIds;
  protected IPaginationInfoSortModel   paginatedSortInfo;
  protected IPaginationInfoFilterModel paginatedFilterInfo;
  protected List<String>               taxonomyIds;
  protected String                     kpiId;
  protected List<String>               selectedTypes;
  protected Boolean                    isBookmark;
  
  public List<String> getAttributeIds()
  {
    if(attributeIds ==  null) {
      attributeIds = new ArrayList<String>();
    }
    return attributeIds;
  }
  
  public void setAttributeIds(List<String> attributeIds)
  {
    this.attributeIds = attributeIds;
  }
  
  public List<String> getTagIds()
  {
    if(tagIds ==  null) {
      tagIds = new ArrayList<String>();
    }
    return tagIds;
  }
  
  public void setTagIds(List<String> tagIds)
  {
    this.tagIds = tagIds;
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
  public List<String> getTaxonomyIds()
  {
    if(taxonomyIds == null) {
      taxonomyIds = new ArrayList<>();
    }
    return taxonomyIds;
  }

  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    this.taxonomyIds = taxonomyIds;
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
  public List<String> getSelectedTypes()
  {
    if(selectedTypes == null) {
      selectedTypes = new ArrayList<String>();
    }
    return selectedTypes;
  }

  @Override
  public void setSelectedTypes(List<String> selectedTypes)
  {
    this.selectedTypes = selectedTypes;
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
}
