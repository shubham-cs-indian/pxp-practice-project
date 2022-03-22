package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import java.util.List;

import com.cs.core.base.interactor.model.IModuleEntitiesWithUserIdModel;
import com.cs.core.runtime.interactor.model.instancetree.IPaginationInfoFilterModel;
import com.cs.core.runtime.interactor.model.instancetree.IPaginationInfoSortModel;

public interface IConfigDetailsForFilterAndSortInfoRequestModel extends IModuleEntitiesWithUserIdModel {
  
  public static final String TAXONOMY_IDS          = "taxonomyIds";
  public static final String ATTRIBUTE_IDS         = "attributeIds";
  public static final String TAG_IDS               = "tagIds";
  public static final String PAGINATED_SORT_INFO   = "paginatedSortInfo";
  public static final String PAGINATED_FILTER_INFO = "paginatedFilterInfo";
  public static final String KPI_ID                = "kpiId";
  public static final String SELECTED_TYPES        = "selectedTypes";
  public static final String IS_BOOKMARK           = "isBookmark";
  
  public List<String> getTaxonomyIds();
  public void setTaxonomyIds(List<String> taxonomyIds);
  
  public List<String> getAttributeIds();
  public void setAttributeIds(List<String> attributeIds);
  
  public List<String> getTagIds();
  public void setTagIds(List<String> tagIds);
  
  public IPaginationInfoSortModel getPaginatedSortInfo();
  public void setPaginatedSortInfo(IPaginationInfoSortModel paginatedSortInfo);
  
  public IPaginationInfoFilterModel getPaginatedFilterInfo();
  public void setPaginatedFilterInfo(IPaginationInfoFilterModel paginatedFilterInfo);
  
  public String getKpiId();
  public void setKpiId(String kpiId);
  
  public List<String> getSelectedTypes();
  public void setSelectedTypes(List<String> selectedTypes);
  
  public Boolean getIsBookmark();
  public void setIsBookmark(Boolean isBookmark);
}
