package com.cs.core.dataintegration.idto;

import com.cs.config.idto.IConfigTranslationDTO;
import com.cs.config.idto.IConfigTranslationDTO.EntityType;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IRootConfigDTO;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.rdbms.idto.ISimpleDTO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * It represents PXON Exporter Specification for Configuration and Runtime Data
 *
 * @author cs194
 */
public interface IPXONExportScopeDTO extends ISimpleDTO {

  /**
   * @return the CS Expression Format
   */
  public ICSEElement.ExportFormat getCSEFormat();

  /**
   * @param format the CS Expression format for object export By default, the SYSTEM format is chosen
   * @param blockSeparator the separator characters used between blocks (don't include new-line there)
   */
  public void setCSEFormat(ICSEElement.ExportFormat format, String blockSeparator);

  /**
   * @param localeID the locale ID in which to export data (mandatory)
   * @param catalogCode the catalog from when to export runtime data
   * @param organizationCode, the organization from when to export runtime data
   * @param localeInheritanceSchema the locale hierarchy to consider when exporting runtime data By default, the PIM catalog with null
   * locale ID is considered
   */
  public void setLocaleCatalog(String localeID, String catalogCode, String organizationCode,
          List<String> localeInheritanceSchema);

  /**
   * @return the locale catalog from where to export runtime data
   */
  public ILocaleCatalogDTO getLocaleCatalog();

  /**
   * @return the locale inheritance schema to consider when exporting
   */
  public List<String> getLocaleInheritanceSchema();
  
  /**
   * @return set of languages to export translation
   */
  public Set<String> getLanguages();
  
  /**
   * @return sort language
   */
  public String getSortLanguage();
  
  /**
   * @param sortLanguage
   */
  public void setSortLanguage(String sortLanguage);
  
  /**
   * @return the sort by field
   */
  public String getSortBy();
  
  /**
   * @param sortBy field
   */
  public void setSortBy(String sortBy);
  
  /**
   * @param status overwritten status of all entities
   */
  public void includeAllEntities(boolean status);

  /**
   * @return true if the export includes all entities
   */
  public boolean includeAllEntities();

  /**
   * @param status overwritten status of embedded entities
   */
  public void includeEmbeddedEntities(boolean status);

  /**
   * @return true if the scope includes embedded children
   */
  public boolean includeEmbeddedEntities();

  /**
   * @return the set of base entity IIDs to export
   */
  public Collection<Long> getBaseEntityIIDs();

  /**
   * Define the set of base entity IIDs to export (in that case the catalog definition may be superseded)
   *
   * @param iids /!\ If a set of base entity IDs is already defined, using this method cancel specification by IDs
   */
  public void setBaseEntityIIDs(Collection<Long> iids);

  /**
   * @return the set of base entity IDs to export from the defined catalog
   */
  public Collection<String> getBaseEntityIDs();

  /**
   * Define the set of base entity IDs to export
   *
   * @param ids /!\ If a set of base entity IIDs is already defined, using this method cancel specification by IIDs
   */
  public void setBaseEntityIDs(Collection<String> ids);

  /**
   * Define the set of properties that must be exported with base entities
   *
   * @param propertyCodes By default, all applicable properties will be exported with entities
   */
  public void setBaseEntityPropertiesByCodes(Collection<String> propertyCodes);

  /**
   * @return the set of properties to export (an empty set means all)
   */
  public Collection<IPropertyDTO> getBaseEntityProperties();

  /**
   * Define the set of properties that must be exported with base entities
   *
   * @param propertyIIDs By default, all applicable properties will be exported with entities
   */
  public void setBaseEntityProperties(Collection<Long> propertyIIDs);

  /**
   * @return the type of configuration item to export along with runtime data
   */
  public Collection<IRootConfigDTO.ItemType> getConfigItemTypes();

  /**
   * Define the set of configuration items that must be exported along with runtime data
   *
   * @param itemTypes By default, none are exported
   */
  public void setConfigItemTypes(Collection<IRootConfigDTO.ItemType> itemTypes);

  /**
   * @return the type of properties to export along with runtime data
   */
  public Collection<IPropertyDTO.PropertyType> getPropertyItemTypes();

  /**
   * If the set of configuration items contains PROPERTY, filters the type of properties to export
   *
   * @param propertyTypes By default, all are exported
   */
  public void setPropertyItemTypes(Collection<IPropertyDTO.PropertyType> propertyTypes);

  /**
   * @return the type of classifiers to export along with runtime data
   */
  public Collection<IClassifierDTO.ClassifierType> getClassifierItemTypes();

  /**
   * If the set of configuration items contains CLASSIFIER, filters the type of classifiers to export
   *
   * @param classifierTypes By default, all are exported
   */
  public void setClassifierItemTypes(Collection<IClassifierDTO.ClassifierType> classifierTypes);

  /**
   * Defines the set of configuration items to export by IIDs
   *
   * @param iids a map of iids by type of item to export /!\ In a first version only configuration items that are present into the RDBMS can
   * be reached by this way
   */
  public void setConfigItemIIDs(Map<IRootConfigDTO.ItemType, Collection<Long>> iids);

  /**
   * Defines the set of configuration items to export by Codes
   *
   * @param codes a map of codes by type of item to export /!\ In a first version only configuration items that are present into the RDBMS
   * can be reached by this way
   */
  public void setConfigItemByCodes(Map<IRootConfigDTO.ItemType, Collection<String>> codes);

  /**
   * @param type the concerned type of items
   * @return the set of configuration object IIDs to export along with runtime data
   */
  public Collection<Long> getConfigItemIIDs(IRootConfigDTO.ItemType type);

  /**
   * @param type the concerned type of items
   * @return the set of configuration object Codes to export along with runtime data
   */
  public Collection<String> getConfigItemCodes(IRootConfigDTO.ItemType type);

  /**
   * @return the type of entity to export of translation
   */
  public IConfigTranslationDTO.EntityType getEntityType();
  
  /**
   * 
   * @param entityType type of entity to export translation
   */
  public void setEnitytType(EntityType entityType);
  
  
  /**
   * @return the searchCriteria which is used to fetch set of base entity IDs to export
   */
  public Map<String, Object> getSearchCriteria();
  
  /**
   * Defines the search criteria on the basis of which
   * set of base entity IDs to export will be fetched
   * 
   * @param searchCriteria to fetch baseEntityiids 
   */
  public void setSearchCriteria(Map<String, Object> searchQuery);

  /**
   * 
   * @return PartnerAuthorizationId
   */
  public String getPartnerAuthorizationId();
  /**
   * 
   * @param partnerAuthorizationId
   */
  public void setPartnerAuthorizationId(String partnerAuthorizationId);
}
