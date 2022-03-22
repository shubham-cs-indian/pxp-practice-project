package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;
import com.cs.core.runtime.interactor.model.searchable.IInstanceSearchStrategyModel;
import com.cs.core.runtime.interactor.model.searchable.InstanceSearchModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InstanceSearchStrategyModel extends InstanceSearchModel
    implements IInstanceSearchStrategyModel {
  
  private static final long     serialVersionUID = 1L;
  protected IGetFilterInfoModel filterInfo;
  protected List<String>        searchableAttributes;
  protected Set<String>         taxonomyIdsHavingRP;
  protected Set<String>         klassIdsHavingRP;
  
  @Override
  public IGetFilterInfoModel getFilterInfo()
  {
    return filterInfo;
  }
  
  @Override
  public void setFilterInfo(IGetFilterInfoModel filterInfo)
  {
    this.filterInfo = filterInfo;
  }
  
  @Override
  public List<String> getSearchableAttributes()
  {
    if (searchableAttributes == null) {
      searchableAttributes = new ArrayList<>();
    }
    return searchableAttributes;
  }
  
  @Override
  public void setSearchableAttributes(List<String> searchableAttributes)
  {
    this.searchableAttributes = searchableAttributes;
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
}
