package com.cs.core.rdbms.entity.idto;

import java.util.Collection;
import java.util.List;

import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idto.IFilterDTO.FilterType;
import com.cs.core.rdbms.entity.idto.IFilterDTO.ValueType;
import com.cs.core.technical.rdbms.idto.IRootDTOBuilder;

/**
 * The Builder interface to construct IBaseEntityDTO
 * 
 * @author janak
 */
public interface ISearchDTOBuilder extends IRootDTOBuilder {

  ISearchDTOBuilder addSort(List<ISortDTO> sortInfo);

  ISearchDTOBuilder addClassesWithReadPermission(Collection<String> classCodes);

  ISearchDTOBuilder addTaxonomiesWithReadPermission(Collection<String> classCodes);

  ISearchDTOBuilder setPagination(int from, int size);

  ISearchDTOBuilder setStringToSearch(String search);

  ISearchDTOBuilder setTranslatableAttributeCodes(Collection<String> searchableAttributes);

  ISearchDTOBuilder addSearchableAttributes(String attributeCode, Boolean isNumeric);

  ISearchDTOBuilder addClassFilters(String... classifierFilters);

  ISearchDTOBuilder addTaxonomyFilters(String... classifierFilters);
  
  ISearchDTOBuilder addCollectionFilters(ICollectionFilterDTO collectionFilterDto);

  ISearchDTOBuilder addFilter(IPropertyDTO propertyDTO, FilterType type, ValueType valueType, Object value);

  ISearchDTOBuilder setEndpointCode(String endpointCode);
  
  ISearchDTOBuilder addRuleViolationFilters(List<String> ruleViolationFilters);

  ISearchDTOBuilder setIdsToExclude(Collection<Long> idsToExclude);

  ISearchDTOBuilder addAssetExpiryFilter(Boolean isExpired);
  
  ISearchDTOBuilder addIsDuplicateFilter(Boolean isDuplicate);
  
  ISearchDTOBuilder addKpiId(String kpiId);
  
  ISearchDTOBuilder addMajorTaxonomyIds(List<String> majorTaxonomyIds);
  
  ISearchDTOBuilder setXrayEnabled(Boolean xrayEnabled);
  
  public boolean getIsFromChooseTaxonomy();
  
  public void setIsFromChooseTaxonomy(boolean isFromChooseTaxonomy);

  /**
  * method to build search DTO
  *
  * @return ISearchDTO
  */
 @Override
 public ISearchDTO build();

  public enum RootFilter {
    TRUE, FALSE, BOTH;
  }
}
