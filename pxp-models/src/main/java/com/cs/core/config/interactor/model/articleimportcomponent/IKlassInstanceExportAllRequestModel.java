package com.cs.core.config.interactor.model.articleimportcomponent;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;

import java.util.List;

public interface IKlassInstanceExportAllRequestModel extends IModel {
  
  public static final String SIZE                = "size";
  public static final String FROM                = "from";
  public static final String NATURE_TYPE         = "natureType";
  public static final String NON_NATURE_TYPES    = "nonNatureTypes";
  public static final String FILTER_INFO         = "filterInfo";
  public static final String IS_FILTERED_APPLIED = "isFilterApplied";
  
  public List<String> getIds();
  
  public void setIds(List<String> ids);
  
  public int getSize();
  
  public void setSize(int size);
  
  public int getFrom();
  
  public void setFrom(int from);
  
  public List<String> getTypes();
  
  public void setTypes(List<String> types);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
  
  public String getEndpointId();
  
  public void setEndpointId(String endpointId);
  
  public String getDocType();
  
  public void setDocType(String docType);
  
  public String getPhysicalCatalogId();
  
  public void setPhysicalCatalogId(String physicalCatalogId);
  
  public String getOrganizationId();
  
  public void setOrganizationId(String organizationId);
  
  public String getNatureType();
  
  public void setNatureType(String naturetype);
  
  public List<String> getNonNatureTypes();
  
  public void setNonNatureTypes(List<String> nonNatureTypes);
  
  public IGetKlassInstanceTreeStrategyModel getFilterInfo();
  
  public void setFilterInfo(IGetKlassInstanceTreeStrategyModel filterInfo);
  
  public Boolean getIsFilterApplied();
  
  public void setIsFilterApplied(Boolean isFilteredExport);
}
