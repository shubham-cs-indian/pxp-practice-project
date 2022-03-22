package com.cs.core.rdbms.entity.dto;

import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.*;
import com.cs.core.rdbms.entity.idto.IFilterDTO.FilterType;
import com.cs.core.rdbms.entity.idto.IFilterDTO.ValueType;
import com.cs.core.rdbms.entity.idto.ISearchDTOBuilder.RootFilter;
import com.cs.core.technical.exception.CSFormatException;
import org.apache.commons.lang3.StringUtils;

import java.util.*;


public class SearchDTO extends SimpleDTO implements ISearchDTO {

  private final String organizationCode;
  private final String catalogCode;
  private final String endpointCode;
  private final String localeId;
  private final RootFilter rootFilter;
  private final Boolean   isArchivePortal;
  private final Boolean xrayEnabled;


  //permission and entities
  private final List<BaseType> baseTypes;
  private final List<String>   classesWithReadPermission;
  private final List<String>   taxonomiesWithReadPermission;
  private final List<String>   taxonomyIds;
  private final List<String>   classIds;
  private final List<Long>     idsToExclude;
  private final List<String>   majorTaxonomyIds;
  private final String         kpiId;


  //pagination
  private final int from;
  private final int size;

  //search
  private final String                   stringToSearch;
  private final List<String>             translatableAttributes;
  private final Map<String, Boolean>     searchableAttributes; //searchableAttribute Vs IsNumeric
  private final List<IFilterPropertyDTO> propertyFilters;
  private final List<String>             ruleViolationFilters;
  private final ICollectionFilterDTO     collectionFilter;
  
  // Special use case filters for DAM
  private final Boolean isExpired;
  private final Boolean isDuplicate;

  //order
  private final List<ISortDTO> order;
  
  //Check if from ChooseTaxonomy Window
   private boolean isFromChooseTaxonomy = false;
   
  protected SearchDTO(SearchDTOBuilder builder)
  {
    this.organizationCode = builder.organizationCode;
    this.catalogCode = builder.catalogCode;
    this.endpointCode = builder.endpointCode;
    this.localeId = builder.localeId;
    this.baseTypes = builder.baseTypes;
    this.classesWithReadPermission = builder.classesWithReadPermission;
    this.taxonomiesWithReadPermission = builder.taxonomiesWithReadPermission;
    this.from = builder.from;
    this.size = builder.size;
    this.stringToSearch = builder.stringToSearch;
    this.translatableAttributes = builder.translatableAttributeCodes;
    this.propertyFilters = new ArrayList<>(builder.propertyFilters.values());
    this.order = builder.order;
    this.searchableAttributes = builder.searchableAttributes;
    this.classIds = builder.classIds;
    this.taxonomyIds = builder.taxonomyIds;
    this.rootFilter = builder.rootFilter;
    this.ruleViolationFilters = builder.ruleViolationFilters;
    this.idsToExclude = builder.idsToExclude;
    this.collectionFilter = builder.collectionFilter;
    this.isDuplicate = builder.isDuplicate;
    this.isExpired = builder.isExpired;
    this.isArchivePortal = builder.isArchivePortal;
    this.kpiId = builder.kpiId;
    this.majorTaxonomyIds = builder.majorTaxonomyIds;
    this.xrayEnabled = builder.xrayEnabled;
    this.isFromChooseTaxonomy = builder.isFromChooseTaxonomy;
  }

  @Override
  public Map<String, Boolean> getSearchableAttribute()
  {
    return searchableAttributes;
  }

  @Override
  public List<ISortDTO> getOrder()
  {
    return order;
  }

  @Override
  public String getOrganizationCode()
  {
    return organizationCode;
  }

  @Override
  public String getCatalogCode()
  {
    return catalogCode;
  }

  @Override
  public String getEndpointCode()
  {
    return endpointCode;
  }

  @Override
  public String getLocaleId()
  {
    return localeId;
  }

  @Override
  public List<BaseType> getBaseTypes()
  {
    return baseTypes;
  }

  @Override
  public List<String> getClassesWithReadPermission()
  {
    return classesWithReadPermission;
  }

  @Override
  public List<String> getTaxonomiesWithReadPermission()
  {
    return taxonomiesWithReadPermission;
  }

  @Override
  public int getFrom()
  {
    return from;
  }

  @Override
  public int getSize()
  {
    return size;
  }

  @Override
  public RootFilter getRootFilter()
  {
    return rootFilter;
  }

  @Override
  public String getStringToSearch()
  {
    return stringToSearch;
  }

  @Override
  public List<String> getTranslatableAttributes()
  {
    return translatableAttributes;
  }

  @Override
  public List<IFilterPropertyDTO> getPropertyFilters()
  {
    return propertyFilters;
  }

  @Override
  public List<String> getClassIds()
  {
    return classIds;
  }

  @Override
  public List<String> getTaxonomyIds()
  {
    return taxonomyIds;
  }
  
  @Override
  public List<String> getRuleViolationFilters()
  {
    return ruleViolationFilters;
  }

  public ICollectionFilterDTO getCollectionFilter()
  {
    return collectionFilter;
  }
  
  @Override
  public List<Long> getIdsToExclude()
  {
    return idsToExclude;
  }
  
  public Boolean getIsExpired()
  {
    return isExpired;
  }

  @Override
  public Boolean getIsDuplicate()
  {
    return isDuplicate;
  }

  @Override
  public Boolean getIsArchivePortal()
  {
    return isArchivePortal;
  }
  
  @Override
  public List<String> getMajorTaxonomyIds()
  {
    return majorTaxonomyIds;
  }
  
  @Override
  public Boolean getXrayEnabled()
  {
    return this.xrayEnabled;
  }
  
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {

  }

  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return null;
  }

  @Override
  public int compareTo(Object o)
  {
    return 0;
  }

  public static class SearchDTOBuilder implements ISearchDTOBuilder {

    private final String       organizationCode;
    private final String       catalogCode;
    private final String       localeId;
    private final RootFilter   rootFilter;
    private final Boolean      isArchivePortal;
    private Boolean            xrayEnabled = false;

    
    //permission and entities
    private String       endpointCode;
    private List<BaseType> baseTypes                    = new ArrayList<>();
    private List<String>   classesWithReadPermission    = new ArrayList<>();
    private List<String>   taxonomiesWithReadPermission = new ArrayList<>();
    private List<Long>     idsToExclude                 = new ArrayList<>();
    private List<String>   classIds = new ArrayList<>();
    private List<String>   taxonomyIds = new ArrayList<>();
    private List<String>   majorTaxonomyIds = new ArrayList<>();
    private String         kpiId;
   


    //pagination
    private int from;
    private int size;

    //search
    private String                          stringToSearch;
    private List<String>                    translatableAttributeCodes = new ArrayList<>();
    private Map<String, IFilterPropertyDTO> propertyFilters            = new HashMap<>();
    private List<String>                    ruleViolationFilters       = new ArrayList<>();
    private List<ISortDTO>                  order = new ArrayList<>();
    private Map<String, Boolean>            searchableAttributes = new HashMap<>();
    private ICollectionFilterDTO            collectionFilter;
    
    // Special Use case Filters
    private Boolean isExpired;
    private Boolean isDuplicate;

    //Check if from ChooseTaxonomy Window
    private boolean isFromChooseTaxonomy = false;
    
    public SearchDTOBuilder(String organizationCode, String catalogCode, String localeId, List<BaseType> baseTypes, 
        RootFilter rootFilter, Boolean isArchivePortal)
    {
      this.organizationCode = organizationCode;
      this.catalogCode = catalogCode;
      this.localeId = localeId;
      this.baseTypes = baseTypes;
      this.rootFilter = rootFilter;
      this.isArchivePortal = isArchivePortal;
    }

    @Override
    public ISearchDTOBuilder setEndpointCode(String endpointCode)
    {
      this.endpointCode = endpointCode;
      return this;
    }

    @Override
    public ISearchDTOBuilder setIdsToExclude(Collection<Long> idsToExclude)
    {
      this.idsToExclude.addAll(idsToExclude);
      return this;
    }

    @Override
    public ISearchDTOBuilder addSort(List<ISortDTO> sortInfo)
    {
     order.addAll(sortInfo);
     return this;
    }

    @Override
    public ISearchDTOBuilder addClassesWithReadPermission(Collection<String> classCodes)
    {
      this.classesWithReadPermission.addAll(classCodes);
      return this;
    }

    @Override
    public ISearchDTOBuilder addTaxonomiesWithReadPermission(Collection<String> classCodes)
    {
      this.taxonomiesWithReadPermission.addAll(classCodes);
      return this;
    }

    @Override
    public ISearchDTOBuilder setPagination(int from, int size)
    {
      this.from = from;
      this.size = size;
      return this;
    }

    @Override
    public ISearchDTOBuilder setStringToSearch(String search)
    {
      this.stringToSearch = search.toLowerCase();
      return this;
    }

    @Override
    public ISearchDTOBuilder setTranslatableAttributeCodes(Collection<String> searchableAttributes)
    {
      this.translatableAttributeCodes.addAll(searchableAttributes);
      return this;
    }

    @Override
    public ISearchDTOBuilder addSearchableAttributes(String attributeCode, Boolean isNumeric)
    {
      this.searchableAttributes.put(attributeCode, isNumeric);
      return this;
    }

    @Override
    public ISearchDTOBuilder addClassFilters(String... classifierFilters)
    {
      this.classIds.addAll(List.of(classifierFilters));
      return this;
    }

    @Override
    public ISearchDTOBuilder addTaxonomyFilters(String... classifierFilters)
    {
      this.taxonomyIds.addAll(List.of(classifierFilters));
      return this;
    }
    
    @Override
    public ISearchDTOBuilder addCollectionFilters(ICollectionFilterDTO collectionFilterDto )
    {
      this.collectionFilter = collectionFilterDto;
      return this;
    }
    
    @Override
    public ISearchDTOBuilder addFilter(IPropertyDTO propertyDTO, FilterType type, ValueType valueType, Object value)
    {
      IFilterPropertyDTO filterPropertyDTO = propertyFilters.get(propertyDTO.getCode());
      if (filterPropertyDTO == null) {
        filterPropertyDTO = new FilterPropertyDTO(propertyDTO.getCode(), propertyDTO.getPropertyType());
        propertyFilters.put(propertyDTO.getCode(), filterPropertyDTO);
      }
      filterPropertyDTO.addFilter(type, valueType, value);
      return this;
    }
    
    public ISearchDTOBuilder addRuleViolationFilters(List<String> ruleViolationFilters) // 1st parameter will be of type SpecialUseCaseFilterType
    {      
      this.ruleViolationFilters.addAll(ruleViolationFilters);
      return this;
    }
    
    @Override
    public ISearchDTOBuilder addAssetExpiryFilter(Boolean isExpired)
    {
      this.isExpired = isExpired;
      return this;
    }
    
    @Override
    public ISearchDTOBuilder addIsDuplicateFilter(Boolean isDuplicate)
    {
      this.isDuplicate = isDuplicate;
      return this;
    }
    
    @Override
    public ISearchDTOBuilder addKpiId(String kpiId)
    {
      this.kpiId = kpiId;
      return this;
    }

    public SearchDTO build()
    {
      return new SearchDTO(this);
    }

    @Override
    public ISearchDTOBuilder addMajorTaxonomyIds(List<String> majorTaxonomyIds)
    {
      this.majorTaxonomyIds = majorTaxonomyIds;
      return this;
    }
    
    @Override
    public ISearchDTOBuilder setXrayEnabled(Boolean xrayEnabled)
    {
      this.xrayEnabled = xrayEnabled;
      return this;
    }
    
    @Override
    public boolean getIsFromChooseTaxonomy()
    {
      return isFromChooseTaxonomy;
    }
    
    @Override
    public void setIsFromChooseTaxonomy(boolean isFromChooseTaxonomy)
    {
      this.isFromChooseTaxonomy = isFromChooseTaxonomy;
    }
  }

  @Override
  public boolean shouldSimpleSearch()
  {
    return !StringUtils.isEmpty(stringToSearch) && !searchableAttributes.isEmpty();
  }
  

  @Override
  public boolean shouldCheckPermissions()
  {
    return !classesWithReadPermission.isEmpty() || !taxonomiesWithReadPermission.isEmpty();
  }


  @Override
  public String getKpiId()
  {
    return kpiId;
  }
  
  @Override
  public boolean getIsFromChooseTaxonomy()
  {
    return isFromChooseTaxonomy;
  }
  
  @Override
  public void setIsFromChooseTaxonomy(boolean isFromChooseTaxonomy)
  {
    this.isFromChooseTaxonomy = isFromChooseTaxonomy;
  }
}
