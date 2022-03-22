package com.cs.core.dataintegration.dto;

import static com.cs.core.dataintegration.dto.PXONExportScopeDTO.ALL_ENTITIES;
import static com.cs.core.dataintegration.dto.PXONExportScopeDTO.BASE_ENTITY_IIDS;
import static com.cs.core.dataintegration.dto.PXONExportScopeDTO.BOOKMARK_ID;
import static com.cs.core.dataintegration.dto.PXONExportScopeDTO.CATALOG_CODE;
import static com.cs.core.dataintegration.dto.PXONExportScopeDTO.CLASSIFIER_TYPES;
import static com.cs.core.dataintegration.dto.PXONExportScopeDTO.COLLECTION_ID;
import static com.cs.core.dataintegration.dto.PXONExportScopeDTO.CONFIG_TYPES;
import static com.cs.core.dataintegration.dto.PXONExportScopeDTO.CSE_FORMAT;
import static com.cs.core.dataintegration.dto.PXONExportScopeDTO.ENTITY_PROPERTY_IIDS;
import static com.cs.core.dataintegration.dto.PXONExportScopeDTO.ENTITY_TYPE;
import static com.cs.core.dataintegration.dto.PXONExportScopeDTO.EXPORT_SUB_TYPE;
import static com.cs.core.dataintegration.dto.PXONExportScopeDTO.INCLUDE_EMBEDDED;
import static com.cs.core.dataintegration.dto.PXONExportScopeDTO.LANGUAGES;
import static com.cs.core.dataintegration.dto.PXONExportScopeDTO.LOCALE_INHERITANCE_SCHEMA;
import static com.cs.core.dataintegration.dto.PXONExportScopeDTO.PROPERTY_TYPES;
import static com.cs.core.dataintegration.dto.PXONExportScopeDTO.SEARCH_CRITERIA;
import static com.cs.core.dataintegration.dto.PXONExportScopeDTO.SELECTED_BASE_TYPES;
import static com.cs.core.dataintegration.dto.PXONExportScopeDTO.SELECTED_TAXONOMY_IDS;
import static com.cs.core.dataintegration.dto.PXONExportScopeDTO.SELECTED_TYPES;
import static com.cs.core.dataintegration.dto.PXONExportScopeDTO.SORT_BY;
import static com.cs.core.dataintegration.dto.PXONExportScopeDTO.SORT_LANGUAGE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.cs.config.idto.IConfigTranslationDTO;
import com.cs.config.idto.IConfigTranslationDTO.EntityType;
import com.cs.config.standard.IStandardConfig;
import com.cs.core.bgprocess.dao.BGProcessDAS;
import com.cs.core.dataintegration.idto.IPXONExportPlanDTO;
import com.cs.core.dataintegration.idto.IPXONExportScopeDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IRootConfigDTO;
import com.cs.core.rdbms.config.idto.IRootConfigDTO.ItemType;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * @author vallee
 */
public class PXONExportPlanDTO extends SimpleDTO implements IPXONExportPlanDTO {
  // the format of CSIDs
  private ICSEElement.ExportFormat cseFormat = ICSEElement.ExportFormat.SYSTEM;
  // defines the catalog from where extracting base entities
  private String catalogCode = "";
  private String organizationCode = IStandardConfig.STANDARD_ORGANIZATION_RCODE;  
  private final List<String> localeInheritanceSchema = new ArrayList<>();
  // true when embedded entities are exported
  private boolean includeChildren = false;                          
  // true when all base entities are exported
  private boolean allBaseEntities = false;  
  // defines the list of base entity to export (allBaseEntities = false)
  private final Set<Long> baseEntityIIDs = new TreeSet<>();
  // true when all available properties are exported
  private boolean allPropertyEntities = false;                          
  // defines the list of base entity properties to export (allEntityProperties  = false)
  private Set<Long> entityPropertyIIDs = new TreeSet<>();                
  // define the search criteria on the basis of which baseEntityiids will be fetched
  private Map<String, Object> searchCriteria = new HashMap<>();
  private static final String PARTNER_AUTHORIZATION_ID = "partnerAuthorizationId";
  private static final String ORGANIZATION_CODE = "organizationCode";
  private String partnerAuthorizationId;

  /*
  The idea of the export plan is the following for runtime data:
  - Run the export by batches of x entities (ex: x = 1000)
  - fetch concerned base entity informations => only base entity IIDs
      - concerned classifiers of entities: nature class + other classifiers CSIDs
      - source catalog CSID
      - origin entity CSID
      - parent and top parent CSID (only when includeChildren = true)
      - default image CSID
      - entity contextual object CSID
          - context CSID
          - context tags CSIDs
          - context linked entities CSIDs
  - fetch concerned tags
      - tag record CSID
      - tags CSIDs
  - fetch concerned value records
      - value record CSID
      - contents obtained with current locale inheritance schema
      - value record contextual object CSID
          - context CSID
          - context tags CSIDs
  - fetch concerned relation records
      - relation record CSID
      - other side entity CSIDs
      - holder side contextual object CSID
          - context CSID
          - context tags CSIDs
      - other side contextual object CSID
          - context CSID
          - context tags CSIDs
  
   */
  // the types of items to export
  private final Set<IRootConfigDTO.ItemType> itemTypes = new TreeSet<>();   
  private final Set<IPropertyDTO.PropertyType> propertyTypes = new TreeSet<>();
  private final Set<IClassifierDTO.ClassifierType> classifierTypes = new TreeSet<>();
  
  private IConfigTranslationDTO.EntityType                entityType              = EntityType.UNDEFINED;
  // locales to export translations
  private final Set<String>                               languages               = new HashSet<>();
  // the codes of items to export
  private final Map<IRootConfigDTO.ItemType, Set<String>> itemIDMap = new HashMap<>();
  
  private String                                          sortLanguage            = "";
  private String                                          sortBy                  = "";

  /*
  The idea of the export plan is the following for configuration data:
  - fetch PROPERTIES (i.e. attribute, tags, relations) by subtypes
      - Property CSID
      - Tag value CSID for LOVs
  - fetch PROPERTIES by Codes (select all, and filter out)
      - Property CSID
  - fetch CLASSIFIER by Codes (select all, and filter out?)
      - Classifier CSID
  - fetch PROPERTY_COLLECTION by Codes (select all, and filter out)
      - Property collection CSID (how to implement it? or nothing for first version)
  - fetch CONTEXT
      - Context CSID
   */
  public PXONExportPlanDTO() { // default empty constructor
  }
  
  public PXONExportPlanDTO(IPXONExportScopeDTO exportScope) throws RDBMSException {
    cseFormat = exportScope.getCSEFormat();
    ILocaleCatalogDTO localeCatalog = exportScope.getLocaleCatalog();
    catalogCode = localeCatalog.getCatalogCode();
    organizationCode = localeCatalog.getOrganizationCode();
    localeInheritanceSchema.addAll(exportScope.getLocaleInheritanceSchema());
    includeChildren = exportScope.includeEmbeddedEntities();
    allBaseEntities = exportScope.includeAllEntities();
    // if user want to export all entities no need to check for baseEntity
    if (!allBaseEntities) {
      baseEntityIIDs.addAll(exportScope.getBaseEntityIIDs());
    }
    allPropertyEntities = exportScope.getBaseEntityProperties().isEmpty();

    if (!allPropertyEntities) {
      entityPropertyIIDs = getAllPropertyIIDs(exportScope.getBaseEntityProperties());
    }

    itemTypes.addAll(exportScope.getConfigItemTypes());
    propertyTypes.addAll(exportScope.getPropertyItemTypes());
    classifierTypes.addAll(exportScope.getClassifierItemTypes());
    entityType = exportScope.getEntityType();
    languages.addAll(exportScope.getLanguages());
    sortLanguage = exportScope.getSortLanguage();
    sortBy = exportScope.getSortBy();
    partnerAuthorizationId = exportScope.getPartnerAuthorizationId(); 
    getItemIdMap(exportScope);
    searchCriteria = exportScope.getSearchCriteria();
  }

  private void getItemIdMap(IPXONExportScopeDTO exportScope) throws RDBMSException {
    BGProcessDAS bgpDAS = new BGProcessDAS();
    if (!itemTypes.isEmpty()) {
      for (ItemType itemType : itemTypes) {
        Set<String> itemCodes = (Set<String>) exportScope.getConfigItemCodes(itemType);
        Set<Long> itemIIDs = (Set<Long>) exportScope.getConfigItemIIDs(itemType);
        itemIDMap.put(itemType, new TreeSet<String>());
        switch (itemType) {
          case PROPERTY:
            if (!itemCodes.isEmpty()) {
              itemIDMap.get(ItemType.PROPERTY).addAll(bgpDAS.getPropertyInfoByCodes(itemCodes));
            } else if (!itemIIDs.isEmpty()) {
              itemIDMap.get(ItemType.PROPERTY).addAll(bgpDAS.getPropertyCodesByIID(itemIIDs));
            }
            if (!propertyTypes.isEmpty()) {
              itemIDMap.get(ItemType.PROPERTY).addAll(bgpDAS.getPropertyCodesByType(propertyTypes));
            }
            break;

          case CLASSIFIER:
            if (!itemCodes.isEmpty()) {
              itemIDMap.get(ItemType.CLASSIFIER).addAll(bgpDAS.getClassifierInfoByCodes(itemCodes));
            } else if (!itemIIDs.isEmpty()) {
              itemIDMap.get(ItemType.CLASSIFIER).addAll(bgpDAS.getClassifierCodesByIID(itemIIDs));
            }
            if (!classifierTypes.isEmpty()) {
              itemIDMap.get(ItemType.CLASSIFIER).addAll(bgpDAS.getClassifierCodesByType(classifierTypes));
            }
            break;

          case MASTER_TAXONOMY:
            if (!itemCodes.isEmpty()) {
              itemIDMap.get(ItemType.MASTER_TAXONOMY).addAll(bgpDAS.getClassifierInfoByCodes(itemCodes));
            } else if (!itemIIDs.isEmpty()) {
              itemIDMap.get(ItemType.MASTER_TAXONOMY).addAll(bgpDAS.getClassifierCodesByIID(itemIIDs));
            }
            if (!classifierTypes.isEmpty()) {
              itemIDMap.get(ItemType.MASTER_TAXONOMY).addAll(bgpDAS.getClassifierCodesByType(classifierTypes));
            }
            break;

          case CONTEXT:
            if (!itemCodes.isEmpty()) {
              itemIDMap.get(ItemType.CONTEXT).addAll(bgpDAS.getContextInfoByCodes(itemCodes));
            } else if (!itemIIDs.isEmpty()) {
              itemIDMap.get(ItemType.CONTEXT).addAll(bgpDAS.getContextCodesByIID(itemIIDs));
            }
            break;

          case PROPERTY_COLLECTION:
            if (!itemCodes.isEmpty()) {
              itemIDMap.put(ItemType.PROPERTY_COLLECTION, itemCodes);
            }
            break;

          case USER:
            if (!itemCodes.isEmpty()) {
              itemIDMap.put(ItemType.USER, itemCodes);
            }
            break;

          case PARTNER:
            if (!itemCodes.isEmpty()) {
              itemIDMap.put(ItemType.PARTNER, itemCodes);
            }
            break;
            
          case RULE:
            if (!itemCodes.isEmpty()) {
              itemIDMap.put(ItemType.RULE, itemCodes);
            }
            break;
            
          case GOLDEN_RECORD_RULE:
            if (!itemCodes.isEmpty()) {
              itemIDMap.put(ItemType.GOLDEN_RECORD_RULE, itemCodes);
            }
            break;
            
          case TASK:
            if (!itemCodes.isEmpty()) {
              itemIDMap.put(ItemType.TASK, itemCodes);
            }
            break;
            
          case LANGUAGE:
            if (!itemCodes.isEmpty()) {
              itemIDMap.put(ItemType.LANGUAGE, itemCodes);
            }
            break;

          case TAB:
            if (!itemCodes.isEmpty()) {
              itemIDMap.put(ItemType.TAB, itemCodes);
            }
            break;
            
          case PERMISSION:
            if (!itemCodes.isEmpty()) {
              itemIDMap.put(ItemType.PERMISSION, itemCodes);
            }
            break;
          
          default:
            break;
        }
      }
    }
  }

  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException {
    cseFormat = json.getEnum(ICSEElement.ExportFormat.class, CSE_FORMAT);
    catalogCode = json.getString(CATALOG_CODE);
    organizationCode = json.getString(ORGANIZATION_CODE);
    localeInheritanceSchema.clear();
    json.getJSONArray(LOCALE_INHERITANCE_SCHEMA).forEach((localeID) -> {
              localeInheritanceSchema.add((String) localeID);
            });
    allBaseEntities = json.getBoolean(ALL_ENTITIES);
    includeChildren = json.getBoolean(INCLUDE_EMBEDDED);
    baseEntityIIDs.clear();
    json.getJSONArray(BASE_ENTITY_IIDS).forEach((baseEntityIID) -> {
              baseEntityIIDs.add((Long) baseEntityIID);
            });
//    allBaseEntities = allBaseEntities ? !baseEntityIIDs.isEmpty() : allBaseEntities;
    entityPropertyIIDs.clear();
    json.getJSONArray(ENTITY_PROPERTY_IIDS).forEach((propertyIID) -> {
              entityPropertyIIDs.add((Long) propertyIID);
            });
    allPropertyEntities = allPropertyEntities ? !entityPropertyIIDs.isEmpty() : allPropertyEntities;
    itemTypes.clear();
    json.getJSONArray(CONFIG_TYPES).forEach((Object configType) -> {
              itemTypes.add(IRootConfigDTO.ItemType.valueOf((String) configType));
            });
    propertyTypes.clear();
    json.getJSONArray(PROPERTY_TYPES).forEach((Object propertyType) -> {
              propertyTypes.add(IPropertyDTO.PropertyType.valueOf((String) propertyType));
            });
    classifierTypes.clear();
    json.getJSONArray(CLASSIFIER_TYPES).forEach((Object classType) -> {
              classifierTypes.add(IClassifierDTO.ClassifierType.valueOf((String) classType));
            });
    entityType = json.getEnum(IConfigTranslationDTO.EntityType.class, ENTITY_TYPE);
    
    languages.clear();
    json.getJSONArray(LANGUAGES).forEach((Object locale) -> {
      languages.add((String) locale);
    });
    
    sortLanguage = json.getString(SORT_LANGUAGE);
    sortBy = json.getString(SORT_BY);
    
    for (IRootConfigDTO.ItemType type : IRootConfigDTO.ItemType.values()) {
      Boolean hasKey = json.toJSONObject().containsKey(type.toString());
      if (hasKey) {
        itemTypes.add(type);
        itemIDMap.put(type, new HashSet<>());
        json.getJSONArray(type.toString()).forEach((Object ID) -> {
                  itemIDMap.get(type)
                          .add((String) ID);
                });
      }
    }
    
    JSONContentParser parser = json.getJSONParser(SEARCH_CRITERIA);
    if(!parser.isEmpty()) {
      searchCriteria.put(EXPORT_SUB_TYPE, parser.getString(EXPORT_SUB_TYPE));
      ArrayList<String>  selectedModules = new ArrayList<>();
      parser.getJSONArray(SELECTED_BASE_TYPES).forEach(type->selectedModules.add((String) type));
      searchCriteria.put(SELECTED_BASE_TYPES, selectedModules);
      ArrayList<String>  selectedTypes = new ArrayList<>();
      parser.getJSONArray(SELECTED_TYPES).forEach(type->selectedTypes.add((String) type));
      searchCriteria.put(SELECTED_TYPES, selectedTypes);
      ArrayList<String> selectedTaxonomyIds = new ArrayList<>();
      parser.getJSONArray(SELECTED_TAXONOMY_IDS).forEach(type->selectedTaxonomyIds.add((String) type));
      searchCriteria.put(SELECTED_TAXONOMY_IDS, selectedTaxonomyIds);
      searchCriteria.put(COLLECTION_ID, parser.getString(COLLECTION_ID));
      searchCriteria.put(BOOKMARK_ID, parser.getString(BOOKMARK_ID));
    }
    partnerAuthorizationId = json.getString(PARTNER_AUTHORIZATION_ID);
  }

  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException {
    HashSet<String> itemTypesStr = new HashSet<>();
    itemTypes.forEach((item) -> {
      itemTypesStr.add(item.toString());
    });
    StringBuffer jsonItemsMap = new StringBuffer();
    itemIDMap.keySet().forEach((type) -> {
              jsonItemsMap.append(JSONBuilder.newJSONStringArray(type.toString(), itemIDMap.get(type)));
              jsonItemsMap.append(",");
            });
    if (itemIDMap.size() > 0) {
      jsonItemsMap.setLength(jsonItemsMap.length() - 1);
    }
    StringBuffer jsonSearchCriteriaMap = new StringBuffer();
    searchCriteria.keySet().forEach((criteria) -> {
      Object criteriaValue = searchCriteria.get(criteria);
      if(criteriaValue instanceof Collection) {
        jsonSearchCriteriaMap.append(JSONBuilder.newJSONStringArray(criteria.toString(), (Collection<String>) criteriaValue));
      } else {
        jsonSearchCriteriaMap.append(JSONBuilder.newJSONField(criteria.toString(), (String)criteriaValue));
      }
      jsonSearchCriteriaMap.append(",");
    });
    if (searchCriteria.size() > 0) {
      jsonSearchCriteriaMap.setLength(jsonSearchCriteriaMap.length() - 1);
    }
    return JSONBuilder.assembleJSONBuffer(
            JSONBuilder.newJSONField(CSE_FORMAT, cseFormat),
            JSONBuilder.newJSONField(CATALOG_CODE, catalogCode),
            JSONBuilder.newJSONField(ORGANIZATION_CODE, organizationCode),
            JSONBuilder.newJSONStringArray(LOCALE_INHERITANCE_SCHEMA, localeInheritanceSchema),
            JSONBuilder.newJSONField(ALL_ENTITIES, allBaseEntities),
            JSONBuilder.newJSONField(INCLUDE_EMBEDDED, includeChildren),
            baseEntityIIDs.size() > 0 ? JSONBuilder.newJSONLongArray(BASE_ENTITY_IIDS, baseEntityIIDs)
            : JSONBuilder.VOID_FIELD,
            entityPropertyIIDs.size() > 0
            ? JSONBuilder.newJSONLongArray(ENTITY_PROPERTY_IIDS, entityPropertyIIDs)
            : JSONBuilder.VOID_FIELD,
            itemTypesStr.size() > 0 ? JSONBuilder.newJSONStringArray(CONFIG_TYPES, itemTypesStr)
            : JSONBuilder.VOID_FIELD,
            itemIDMap.size() > 0 ? jsonItemsMap : JSONBuilder.VOID_FIELD, 
            JSONBuilder.newJSONField(ENTITY_TYPE, entityType),
            languages.size() > 0 ? JSONBuilder.newJSONStringArray(LANGUAGES, languages) : JSONBuilder.VOID_FIELD,
            JSONBuilder.newJSONField(SORT_LANGUAGE, sortLanguage),
            JSONBuilder.newJSONField(SORT_BY, sortBy),
            searchCriteria.size() > 0 ? JSONBuilder.newJSONField(SEARCH_CRITERIA, jsonSearchCriteriaMap) : JSONBuilder.VOID_FIELD,
            JSONBuilder.newJSONField(PARTNER_AUTHORIZATION_ID, partnerAuthorizationId));
  }

  @Override
  public ICSEElement.ExportFormat getCSEFormat() {
    return cseFormat;
  }
  
  @Override
  public String getCatalogCode() {
    return catalogCode;
  }

  @Override
  public String getLocaleID() {
    return localeInheritanceSchema.get(localeInheritanceSchema.size() - 1);
  }

  @Override
  public List<String> getLocaleInheritanceSchema() {
    return localeInheritanceSchema;
  }

  @Override
  public boolean includeAllEntities() {
    return allBaseEntities;
  }

  @Override
  public boolean includeEmbeddedEntities() {
    return includeChildren;
  }

  @Override
  public boolean includeAllPropertyEntities() {
    return allPropertyEntities;
  }

  @Override
  public Collection<Long> getBaseEntityIIDs() {
    return baseEntityIIDs;
  }

  @Override
  public Collection<Long> getEntityPropertyIIDs() {
    return entityPropertyIIDs;
  }

  @Override
  public Collection<IRootConfigDTO.ItemType> getConfigItemTypes() {
    return itemTypes;
  }

  @Override
  public Collection<IPropertyDTO.PropertyType> getPropertyItemTypes() {
    return propertyTypes;
  }

  @Override
  public Collection<IClassifierDTO.ClassifierType> getClassifierItemTypes() {
    return classifierTypes;
  }

  @Override
  public Collection<String> getConfigItemCodes(IRootConfigDTO.ItemType type) {
    return itemIDMap.get(type);
  }

  private Set<Long> getAllPropertyIIDs(Collection<IPropertyDTO> baseEntityProperties) {
    Set<Long> propertyIID = new TreeSet<>();
    for (IPropertyDTO property : baseEntityProperties) {
      if (property.getPropertyIID() != 0) {
        propertyIID.add(property.getPropertyIID());
      }
    }
    return propertyIID;
  }

  @Override
  public EntityType getEntityType()
  {
    return entityType;
  }

  @Override
  public void setEntityType(EntityType entityType)
  {
    this.entityType = entityType;
  }

  @Override
  public Set<String> getLanguages()
  {
    return languages;
  }

  @Override
  public String getSortLanguage()
  {
    return sortLanguage;
  }
  
  @Override
  public void setSortLanguage(String sortLanguage)
  {
    this.sortLanguage = sortLanguage;
  }
  
  @Override
  public String getSortBy()
  {
    return sortBy;
  }
  
  @Override
  public void setSortBy(String sortBy)
  {
    this.sortBy = sortBy;
  }
  
  @Override
  public Map<String, Object> getSearchCriteria()
  {
    return this.searchCriteria;
  }

  @Override
  public void setSearchCriteria(Map<String, Object> searchCriteria)
  {
    this.searchCriteria.putAll(searchCriteria);
  }

  @Override
  public String getPartnerAuthorizationId()
  {
    return partnerAuthorizationId;
  }

  @Override
  public void setPartnerAuthorizationId(String partnerAuthorizationId)
  {
    this.partnerAuthorizationId = partnerAuthorizationId;
  }
  
  @Override
  public String getOrganizationCode()
  {
    return organizationCode;
  }

  @Override
  public void setOrganizationCode(String organizationCode)
  {
    this.organizationCode = organizationCode;
  }
}
