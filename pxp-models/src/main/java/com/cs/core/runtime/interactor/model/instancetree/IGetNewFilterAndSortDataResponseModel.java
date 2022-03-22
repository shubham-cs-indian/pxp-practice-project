package com.cs.core.runtime.interactor.model.instancetree;

import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;

public interface IGetNewFilterAndSortDataResponseModel extends IConfigDetailsForNewInstanceTreeModel {
  
  public static final String COUNT                 = "count";
  public static final String SORT_DATA             = "sortData";
  public static final String FILTER_DATA           = "filterData";
  public static final String REFERENCED_TAXONOMIES = "referencedTaxonomies";
  public static final String REFERENCED_KLASSES    = "referencedKlasses";
  
  public Integer getCount();
  public void setCount(Integer count);
  
  public void setSortData(List<IAppliedSortModel> sortData); 
  public List<IAppliedSortModel> getSortData();
  
  public void setFilterData(List<INewApplicableFilterModel> filterData); 
  public List<INewApplicableFilterModel> getFilterData();
  
  public void setReferencedTaxonomies(Map<String, IReferencedKlassOrTaxonomyModel> referencedTaxonomies);
  public Map<String, IReferencedKlassOrTaxonomyModel> getReferencedTaxonomies();

  public void setReferencedKlasses(Map<String, IReferencedKlassOrTaxonomyModel> referencedKlasses);
  public Map<String, IReferencedKlassOrTaxonomyModel> getReferencedKlasses();
}
