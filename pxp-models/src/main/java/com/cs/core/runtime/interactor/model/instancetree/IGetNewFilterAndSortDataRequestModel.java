package com.cs.core.runtime.interactor.model.instancetree;

import java.util.List;

public interface IGetNewFilterAndSortDataRequestModel extends INewInstanceTreeRequestModel {
  
  public static final String FILTER_DATA           = "filterData";
  public static final String PAGINATED_SORT_INFO   = "paginatedSortInfo";
  public static final String PAGINATED_FILTER_INFO = "paginatedFilterInfo";
  public static final String IS_ARCHIVE_PORTAL     = "isArchivePortal";
  public static final String CLICKED_TAXONOMY_ID   = "clickedTaxonomyId";
  public static final String IS_BOOKMARK           = "isBookmark";

  public void setFilterData(List<INewApplicableFilterModel> filterData); 
  public List<INewApplicableFilterModel> getFilterData();
  
  public IPaginationInfoSortModel getPaginatedSortInfo();
  public void setPaginatedSortInfo(IPaginationInfoSortModel paginatedSortInfo);
  
  public IPaginationInfoFilterModel getPaginatedFilterInfo();
  public void setPaginatedFilterInfo(IPaginationInfoFilterModel paginatedFilterInfo);
  
  public Boolean getIsArchivePortal();
  public void setIsArchivePortal(Boolean isArchivalPortal);

  public String getClickedTaxonomyId();
  public void setClickedTaxonomyId(String clickedTaxonomyId);
  
  public Boolean getIsBookmark();
  public void setIsBookmark(Boolean isBookmark);
}

