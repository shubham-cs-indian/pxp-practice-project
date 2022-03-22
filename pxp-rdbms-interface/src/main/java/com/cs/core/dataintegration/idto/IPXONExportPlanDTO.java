package com.cs.core.dataintegration.idto;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.idto.IConfigTranslationDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IRootConfigDTO;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.rdbms.idto.ISimpleDTO;

/**
 * @author vallee
 */
public interface IPXONExportPlanDTO extends ISimpleDTO {

  /**
   * @return the CS Expression Format
   */
  public ICSEElement.ExportFormat getCSEFormat();

  /**
   * @return the catalog code of export
   */
  public String getCatalogCode();

  /**
   * @return the locale of export
   */
  public String getLocaleID();

  /**
   * @return the export locale inheritance
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
   * @return true if the export includes all entities
   */
  public boolean includeAllEntities();

  /**
   * @return true if the export includes embedded children
   */
  public boolean includeEmbeddedEntities();

  /**
   * @return true if the export includes all properties
   */
  public boolean includeAllPropertyEntities();

  /**
   * @return the list of base entity IIDs to export
   */
  Collection<Long> getBaseEntityIIDs();

  /**
   * @return the list of property IIDs to export with entities
   */
  Collection<Long> getEntityPropertyIIDs();

  /**
   * @return the type of configuration item to export along with runtime data
   */
  public Collection<IRootConfigDTO.ItemType> getConfigItemTypes();

  /**
   * @return the type of properties to export along with runtime data
   */
  public Collection<IPropertyDTO.PropertyType> getPropertyItemTypes();

  /**
   * @return the type of classifiers to export along with runtime data
   */
  public Collection<IClassifierDTO.ClassifierType> getClassifierItemTypes();
  
  /**
   * @return the type of entity to export with translation
   */
  public IConfigTranslationDTO.EntityType getEntityType();
  
  /**
   * 
   * @param entityType type of entity to export translation
   */
  public void setEntityType(IConfigTranslationDTO.EntityType entityType);
  
  /**
   * @param type the concerned type of items
   * @return the set of configuration object combined Codes and Types to export along with runtime data
   */
  public Collection<String> getConfigItemCodes(IRootConfigDTO.ItemType type);
  
  /**
   * @return  the searchCriteria which is used to fetch set of base entity IDs to export 
   */
  public Map<String, Object> getSearchCriteria();
  
  /**
   * Defines the search criteria on the basis of which
   * set of base entity IDs to export will be fetched
   * 
   * @param  the searchCriteria to fetch baseEntityiids 
   */
  public void setSearchCriteria(Map<String, Object> searchQuery);
  
  /**
   * 
   * @return PartnerAuthorization
   */
  public String getPartnerAuthorizationId();
  
  /**
   * set PartnerAuthorization
   * @param partnerAuthorizationId
   */
  public void setPartnerAuthorizationId(String partnerAuthorizationId);
  
  /**
   * @return organizationCode
   */
  public String getOrganizationCode();
  
  /**
   * Defines the organizationCode
   * 
   * @param organizationCode
   */
  public void setOrganizationCode(String organizationCode);
}
