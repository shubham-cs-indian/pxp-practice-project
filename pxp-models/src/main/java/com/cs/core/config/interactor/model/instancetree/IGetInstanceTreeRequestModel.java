package com.cs.core.config.interactor.model.instancetree;

import java.util.List;

import com.cs.core.runtime.interactor.model.instancetree.IAppliedSortModel;
import com.cs.core.runtime.interactor.model.instancetree.INewApplicableFilterModel;
import com.cs.core.runtime.interactor.model.instancetree.INewInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IPaginationInfoFilterModel;

public interface IGetInstanceTreeRequestModel extends INewInstanceTreeRequestModel {
  
  public static final String SIDE2_LINKED_VARIANT_KR_IDS = "side2LinkedVariantKrIds";
  public static final String X_RAY_ATTRIBUTES            = "xrayAttributes";
  public static final String X_RAY_TAGS                  = "xrayTags";
  public static final String SORT_OPTIONS                = "sortOptions";
  public static final String FILTER_DATA                 = "filterData";
  public static final String PAGINATED_FILTER_INFO       = "paginatedFilterInfo";
  public static final String IS_FILTER_DATA_REQUIRED     = "isFilterDataRequired";
  
  public List<String> getSide2LinkedVariantKrIds();
  public void setSide2LinkedVariantKrIds(List<String> side2LinkedVariantKrIds);
  
  public void setXRayAttributes(List<String> xRayAttributes);
  public List<String> getXRayAttributes();
  
  public void setXRayTags(List<String> xRayTags);
  public List<String> getXRayTags();
  
  public List<IAppliedSortModel> getSortOptions();
  public void setSortOptions(List<IAppliedSortModel> sortOptions);
  
  public void setFilterData(List<INewApplicableFilterModel> filterData); 
  public List<INewApplicableFilterModel> getFilterData();
  
  public IPaginationInfoFilterModel getPaginatedFilterInfo();
  public void setPaginatedFilterInfo(IPaginationInfoFilterModel paginatedFilterInfo);
  
  public Boolean getIsFilterDataRequired();
  public void setIsFilterDataRequired(Boolean isFilterDataRequired);
}

