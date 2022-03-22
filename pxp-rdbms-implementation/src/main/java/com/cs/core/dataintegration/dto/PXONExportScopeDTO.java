package com.cs.core.dataintegration.dto;

import com.cs.config.idto.IConfigTranslationDTO;
import com.cs.config.idto.IConfigTranslationDTO.EntityType;
import com.cs.config.standard.IStandardConfig;
import com.cs.core.data.IdentifierSet;
import com.cs.core.dataintegration.idto.IPXONExportScopeDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.dto.CatalogDTO;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idto.*;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;

import java.util.*;

/**
 * Implementation of the PXON Export Scope
 *
 * @author vallee
 */
public class PXONExportScopeDTO extends SimpleDTO implements IPXONExportScopeDTO {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  static final String                                       CSE_FORMAT                = "csidFormat";
  private ICSEElement.ExportFormat                          cseFormat                 = ICSEElement.ExportFormat.SYSTEM;   // default
  static final String                                       CATALOG_CODE              = "catalogCode";
  static final String                                       LOCALE_INHERITANCE_SCHEMA = "localeInheritance";
  private LocaleCatalogDTO                                  localeCatalog             = new LocaleCatalogDTO("",
      new CatalogDTO(IStandardConfig.StandardCatalog.pim.toString(), IStandardConfig.STANDARD_ORGANIZATION_CODE));
  static final String                                       ALL_ENTITIES              = "allEntities";
  private boolean                                           allBaseEntities           = false;
  static final String                                       INCLUDE_EMBEDDED          = "includeEmbeddedEntities";
  private boolean                                           includeEmbeddedEntities   = false;
  static final String                                       BASE_ENTITY_IIDS          = "baseEntityIIDs";
  private static final String                               BASE_ENTITY_IDS           = "baseEntityIDs";
  private final IdentifierSet baseEntities
          = new IdentifierSet( BASE_ENTITY_IIDS, BASE_ENTITY_IDS);
  static final String                                       ENTITY_PROPERTY_IIDS      = "entityPropertyIIDs";
  private static final String                               ENTITY_PROPERTY_CODES     = "entityPropertyCodes";
  private final IdentifierSet entityProperties
          = new IdentifierSet( ENTITY_PROPERTY_IIDS, ENTITY_PROPERTY_CODES);
  static final String                                       EXPORT_SUB_TYPE           = "exportSubType";
  static final String                                       SELECTED_TAXONOMY_IDS     = "selectedTaxonomyIds";
  static final String                                       SELECTED_TYPES            = "selectedTypes";
  static final String                                       SELECTED_BASE_TYPES       = "selectedBaseTypes";
  static final String                                       SEARCH_CRITERIA           = "searchCriteria";
  static final String                                       COLLECTION_ID             = "collectionId";
  static final String                                       BOOKMARK_ID               = "bookmarkId";
  private Map<String, Object>                               searchCriteria            = new HashMap<>();
  
  // config data scope
  static final String                                       CONFIG_TYPES              = "itemTypes";
  private final Set<IRootConfigDTO.ItemType>                itemTypes                 = new HashSet<>();
  private static final String                               HIERARCHY_IIDS            = "hierarchyIIDs";
  static final String                                       PROPERTY_TYPES            = "propertyTypes";
  private final Set<IPropertyDTO.PropertyType>              propertyTypes             = new HashSet<>();
  static final String                                       CLASSIFIER_TYPES          = "classifierTypes";
  private final Set<IClassifierDTO.ClassifierType>          classifierTypes           = new HashSet<>();
  private static final String                               ITEM_IIDS                 = "itemIIDs";
  private static final String                               ITEM_CODES                = "itemCodes";
  private final Map<IRootConfigDTO.ItemType, IdentifierSet> itemMap                   = new HashMap<>();
 
  static final String                                       ENTITY_TYPE               = "entityType";
  private IConfigTranslationDTO.EntityType                  entityType                = EntityType.UNDEFINED;
  
  static final String                                       LANGUAGES                 = "languages";
  private final Set<String>                                 languages                 = new HashSet<>();
  static final String                                       SORT_LANGUAGE             = "sortLanguage";
  private String                                            sortLanguage              = "";
  static final String                                       SORT_BY                   = "sortBy";
  private String                                            sortBy                    = "";
  private static final String                               PARTNER_AUTHORIZATION_ID  = "partnerAuthorizationId";
  private String                                            partnerAuthorizationId;
  
  
  
  /**
   * Minimal default configuration
   */
  public PXONExportScopeDTO()
  {
  }
  
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    cseFormat = json.getEnum(ICSEElement.ExportFormat.class, CSE_FORMAT);
    cseFormat = (cseFormat == ICSEElement.ExportFormat.UNDEFINED ? ICSEElement.ExportFormat.SYSTEM : cseFormat);
    JSONContentParser jsonCatalog = json.getJSONParser(CATALOG_CODE);
    if (!jsonCatalog.isEmpty())
      localeCatalog.fromPXON(json.getJSONParser(CATALOG_CODE));
      json.getJSONArray(LOCALE_INHERITANCE_SCHEMA).forEach((localeID) -> {
          localeCatalog.getLocaleInheritanceSchema().add((String) localeID);
        });
    allBaseEntities = json.getBoolean(ALL_ENTITIES);
    includeEmbeddedEntities = json.getBoolean(INCLUDE_EMBEDDED);
    baseEntities.fromJSON(json);
    entityProperties.fromJSON(json);
    itemTypes.clear();
    json.getJSONArray(CONFIG_TYPES).forEach((Object configType) -> {
          itemTypes.add(IRootConfigDTO.ItemType.valueOf((String) configType));
        });
    propertyTypes.clear();
    json.getJSONArray(PROPERTY_TYPES).forEach((Object propertyType) -> {
          propertyTypes.add(IPropertyDTO.PropertyType.valueOf((String) propertyType));
        });
    classifierTypes.clear();
    json.getJSONArray(CLASSIFIER_TYPES).forEach((Object classifierType) -> {
          classifierTypes.add(IClassifierDTO.ClassifierType.valueOf((String) classifierType));
        });
    for (IRootConfigDTO.ItemType type : IRootConfigDTO.ItemType.values()) {
      JSONContentParser typeParser = json.getJSONParser(type.toString());
      if (!typeParser.isEmpty()) {
        itemTypes.add(type);
        itemMap.put(type, new IdentifierSet(ITEM_IIDS, ITEM_CODES));
        itemMap.get(type).fromJSON(typeParser);
      }
    }
    entityType =  json.getEnum(IConfigTranslationDTO.EntityType.class, ENTITY_TYPE);
    sortLanguage = json.getString(SORT_LANGUAGE);
    sortBy = json.getString(SORT_BY);
    languages.clear();
    json.getJSONArray(LANGUAGES).forEach((Object locale) -> {
         languages.add((String) locale);
        });
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
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    HashSet<String> itemTypesStr = new HashSet<>();
    itemTypes.forEach((item) -> {
      itemTypesStr.add(item.toString());
    });
    HashSet<String> propertyTypesStr = new HashSet<>();
    propertyTypes.forEach((item) -> {
      propertyTypesStr.add(item.toString());
    });
    HashSet<String> classifierTypesStr = new HashSet<>();
    classifierTypes.forEach((item) -> {
      classifierTypesStr.add(item.toString());
    });
    StringBuffer jsonItemsMap = new StringBuffer();
    itemMap.keySet().forEach((type) -> {
          jsonItemsMap.append(JSONBuilder.newJSONField(type.toString(), itemMap.get(type)
              .toJSONField()));
          jsonItemsMap.append(",");
        });
    if (itemMap.size() > 0)
      jsonItemsMap.setLength(jsonItemsMap.length() - 1);
    StringBuffer jsonSearchCriteriaMap = new StringBuffer();
    searchCriteria.keySet().forEach((criteria) -> {
      if (EXPORT_SUB_TYPE.equals(criteria.toString())) {
        jsonSearchCriteriaMap.append(JSONBuilder.newJSONField(EXPORT_SUB_TYPE, (String) searchCriteria.get(criteria)));
      }else {
        jsonSearchCriteriaMap.append(JSONBuilder.newJSONStringArray(criteria.toString(), (Collection<String>) searchCriteria.get(criteria)));
      }
      jsonSearchCriteriaMap.append(",");
    });
    if (searchCriteria.size() > 0) {
      jsonSearchCriteriaMap.setLength(jsonSearchCriteriaMap.length() - 1);
    }
    
    return JSONBuilder.assembleJSONBuffer(
        JSONBuilder.newJSONField(CSE_FORMAT, cseFormat),
        JSONBuilder.newJSONField(CATALOG_CODE, localeCatalog.toPXONBuffer()),
        JSONBuilder.newJSONStringArray(LOCALE_INHERITANCE_SCHEMA, localeCatalog.getLocaleInheritanceSchema()),
        JSONBuilder.newJSONField(ALL_ENTITIES, allBaseEntities),
        JSONBuilder.newJSONField(INCLUDE_EMBEDDED, includeEmbeddedEntities),
        baseEntities.toJSONField(), entityProperties.toJSONField(),
        itemTypesStr.size() > 0 ? JSONBuilder.newJSONStringArray(CONFIG_TYPES, itemTypesStr) : JSONBuilder.VOID_FIELD,
        propertyTypesStr.size() > 0
            ? JSONBuilder.newJSONStringArray(PROPERTY_TYPES, propertyTypesStr) : JSONBuilder.VOID_FIELD,
        classifierTypesStr.size() > 0
            ? JSONBuilder.newJSONStringArray(CLASSIFIER_TYPES, classifierTypesStr) : JSONBuilder.VOID_FIELD,
        itemMap.size() > 0 ? jsonItemsMap : JSONBuilder.VOID_FIELD, 
        JSONBuilder.newJSONField(ENTITY_TYPE, entityType),
        languages.size() > 0 ? JSONBuilder.newJSONStringArray(LANGUAGES, languages) : JSONBuilder.VOID_FIELD,
        JSONBuilder.newJSONField(SORT_LANGUAGE, sortLanguage),
        JSONBuilder.newJSONField(SORT_BY, sortBy),
        searchCriteria.size() > 0 ? JSONBuilder.newJSONField(SEARCH_CRITERIA, jsonSearchCriteriaMap) : JSONBuilder.VOID_FIELD,
        JSONBuilder.newJSONField(PARTNER_AUTHORIZATION_ID, partnerAuthorizationId));
  }
  
  @Override
  public void setCSEFormat(ICSEElement.ExportFormat format, String separator)
  {
    cseFormat = format;
  }
  
  @Override
  public ICSEElement.ExportFormat getCSEFormat()
  {
    return cseFormat;
  }
  
  @Override
  public void setLocaleCatalog(String localeID, String catalogCode, String organizationCode, List<String> localeInheritance)
  {
    localeCatalog = new LocaleCatalogDTO(localeID, catalogCode, organizationCode);
    localeCatalog.setLocaleInheritanceSchema(localeInheritance);
  }
  
  @Override
  public ILocaleCatalogDTO getLocaleCatalog()
  {
    return localeCatalog;
  }
  
  @Override
  public List<String> getLocaleInheritanceSchema()
  {
    return localeCatalog.getLocaleInheritanceSchema();
  }
  
  @Override
  public void includeAllEntities(boolean status)
  {
    baseEntities.clear();
    allBaseEntities = status;
  }
  
  @Override
  public boolean includeAllEntities()
  {
    return allBaseEntities;
  }
  
  @Override
  public void includeEmbeddedEntities(boolean status)
  {
    includeEmbeddedEntities = status;
  }
  
  @Override
  public boolean includeEmbeddedEntities()
  {
    return includeEmbeddedEntities;
  }
  
  @Override
  public void setBaseEntityIIDs(Collection<Long> iids)
  {
    baseEntities.define(BASE_ENTITY_IIDS, iids);
    if (allBaseEntities)
      allBaseEntities = !(baseEntities.size() > 0);
  }
  
  @Override
  public Collection<Long> getBaseEntityIIDs()
  {
    return baseEntities.getByKey(BASE_ENTITY_IIDS);
  }
  
  @Override
  public void setBaseEntityIDs(Collection<String> ids)
  {
    baseEntities.define(BASE_ENTITY_IDS, ids);
    if (allBaseEntities)
      allBaseEntities = !(baseEntities.size() > 0);
  }
  
  @Override
  public Collection<String> getBaseEntityIDs()
  {
    return baseEntities.getByKey(BASE_ENTITY_IDS);
  }
  
  @Override
  public void setBaseEntityProperties(Collection<Long> propertyIIDs)
  {
    entityProperties.define(ENTITY_PROPERTY_IIDS, propertyIIDs);
  }
  
  @Override
  public void setBaseEntityPropertiesByCodes(Collection<String> propertyCodes)
  {
    entityProperties.define(ENTITY_PROPERTY_CODES, propertyCodes);
  }
  
  @Override
  public Collection<IPropertyDTO> getBaseEntityProperties()
  {
    Set<IPropertyDTO> properties = new TreeSet<>();
    if (entityProperties.getKey()
        .equals(ENTITY_PROPERTY_IIDS)) {
      entityProperties.toLongs()
          .forEach((iid) -> {
            properties.add(new PropertyDTO(iid, "", IPropertyDTO.PropertyType.UNDEFINED));
          });
    }
    else if (entityProperties.getKey()
        .equals(ENTITY_PROPERTY_CODES)) {
      entityProperties.toStrings()
          .forEach((code) -> {
            properties.add(new PropertyDTO(0L, code, IPropertyDTO.PropertyType.UNDEFINED));
          });
    }
    return properties;
  }
  
  @Override
  public void setConfigItemTypes(Collection<IRootConfigDTO.ItemType> configItemTypes)
  {
    itemTypes.clear();
    itemTypes.addAll(configItemTypes);
  }
  
  @Override
  public Collection<IRootConfigDTO.ItemType> getConfigItemTypes()
  {
    return itemTypes;
  }
  
  @Override
  public void setPropertyItemTypes(Collection<IPropertyDTO.PropertyType> propTypes)
  {
    propertyTypes.clear();
    if (propTypes.size() > 0)
      itemTypes.add(IRootConfigDTO.ItemType.PROPERTY);
    propertyTypes.addAll(propTypes);
  }
  
  @Override
  public Collection<IPropertyDTO.PropertyType> getPropertyItemTypes()
  {
    return propertyTypes;
  }
  
  @Override
  public void setClassifierItemTypes(Collection<IClassifierDTO.ClassifierType> classTypes)
  {
    classifierTypes.clear();
    if (classTypes.size() > 0)
      itemTypes.add(IRootConfigDTO.ItemType.CLASSIFIER);
    classifierTypes.addAll(classTypes);
  }
  
  @Override
  public Collection<IClassifierDTO.ClassifierType> getClassifierItemTypes()
  {
    return classifierTypes;
  }
  
  @Override
  public void setConfigItemIIDs(Map<IRootConfigDTO.ItemType, Collection<Long>> iidMap)
  {
    iidMap.keySet()
        .forEach((type) -> {
          if (!itemMap.containsKey(type)) {
            itemMap.put(type, new IdentifierSet(ITEM_IIDS, ITEM_CODES));
            itemTypes.add(type);
          }
          itemMap.get(type)
              .define(ITEM_IIDS, iidMap.get(type));
        });
  }
  
  @Override
  public void setConfigItemByCodes(Map<IRootConfigDTO.ItemType, Collection<String>> codes)
  {
    codes.keySet()
        .forEach((type) -> {
          if (!itemMap.containsKey(type)) {
            itemMap.put(type, new IdentifierSet(ITEM_IIDS, ITEM_CODES));
            itemTypes.add(type);
          }
          itemMap.get(type)
              .define(ITEM_CODES, codes.get(type));
        });
  }
  
  @Override
  public Collection<Long> getConfigItemIIDs(IRootConfigDTO.ItemType type)
  {
    if (!itemMap.containsKey(type))
      return new HashSet<>();
    return itemMap.get(type)
        .getByKey(ITEM_IIDS);
  }
  
  @Override
  public Collection<String> getConfigItemCodes(IRootConfigDTO.ItemType type)
  {
    if (!itemMap.containsKey(type))
      return new HashSet<>();
    return itemMap.get(type)
        .getByKey(ITEM_CODES);
  }

  @Override
  public EntityType getEntityType()
  {
    return entityType;
  }
  
  @Override
  public void setEnitytType(EntityType entityType)
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

  public String getPartnerAuthorizationId()
  {
    return partnerAuthorizationId;
  }

  @Override
  public void setPartnerAuthorizationId(String partnerAuthorizationId)
  {
    this.partnerAuthorizationId = partnerAuthorizationId;
  }

}
