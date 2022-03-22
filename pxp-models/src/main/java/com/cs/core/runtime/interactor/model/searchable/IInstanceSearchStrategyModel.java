package com.cs.core.runtime.interactor.model.searchable;

import java.util.List;
import java.util.Set;

public interface IInstanceSearchStrategyModel extends IInstanceSearchModel {
  
  public static final String FILTER_INFO            = "filterInfo";
  public static final String SEARCHABLE_ATTRIBUTES  = "searchableAttributes";
  public static final String KLASS_IDS_HAVING_RP    = "klassIdsHavingRP";
  public static final String TAXONOMY_IDS_HAVING_RP = "taxonomyIdsHavingRP";
  
  public IGetFilterInfoModel getFilterInfo();
  
  public void setFilterInfo(IGetFilterInfoModel filterInfo);
  
  public List<String> getSearchableAttributes();
  
  public void setSearchableAttributes(List<String> searchableAttributes);
  
  public Set<String> getTaxonomyIdsHavingRP();
  
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP);
  
  public Set<String> getKlassIdsHavingRP();
  
  public void setKlassIdsHavingRP(Set<String> klassIdsHavingRP);
}
