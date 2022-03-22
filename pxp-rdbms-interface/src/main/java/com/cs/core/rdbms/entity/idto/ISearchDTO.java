package com.cs.core.rdbms.entity.idto;

import java.util.List;
import java.util.Map;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface ISearchDTO extends ISimpleDTO, Comparable {

  enum FilterOnRoot {
    root, children, both;
  }

  boolean shouldSimpleSearch();
  
  boolean shouldCheckPermissions();

  Map<String, Boolean> getSearchableAttribute();

  List<ISortDTO> getOrder();

  public String getOrganizationCode();

  public String getCatalogCode();

  public String getEndpointCode();

  public String getLocaleId();

  public List<IBaseEntityIDDTO.BaseType> getBaseTypes();

  public List<String> getClassesWithReadPermission();

  public List<String> getTaxonomiesWithReadPermission();

  public int getFrom();

  public int getSize();

  ISearchDTOBuilder.RootFilter getRootFilter();

  public String getStringToSearch();

  public List<String> getTranslatableAttributes();

  public List<IFilterPropertyDTO> getPropertyFilters();

  List<String> getClassIds();

  List<String> getTaxonomyIds();
  
  List<String> getRuleViolationFilters();

  List<Long> getIdsToExclude();

  public ICollectionFilterDTO getCollectionFilter();
  
  Boolean getIsExpired();
  
  Boolean getIsDuplicate();
 
  public Boolean getIsArchivePortal();
  
  public String getKpiId();
  
  public List<String> getMajorTaxonomyIds();
  
  public Boolean getXrayEnabled();
  
  public boolean getIsFromChooseTaxonomy();
  
  public void setIsFromChooseTaxonomy(boolean isFromChooseTaxonomy);

}
