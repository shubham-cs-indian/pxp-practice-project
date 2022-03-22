package com.cs.config.idao;

import java.util.Collection;

import com.cs.config.idto.IConfigAttributeDTO;
import com.cs.config.idto.IConfigClassifierDTO;
import com.cs.config.idto.IConfigContextDTO;
import com.cs.config.idto.IConfigDataRuleDTO;
import com.cs.config.idto.IConfigGoldenRecordRuleDTO;
import com.cs.config.idto.IConfigLanguageDTO;
import com.cs.config.idto.IConfigOrganizationDTO;
import com.cs.config.idto.IConfigPermissionDTO;
import com.cs.config.idto.IConfigPropertyCollectionDTO;
import com.cs.config.idto.IConfigRelationshipDTO;
import com.cs.config.idto.IConfigTabDTO;
import com.cs.config.idto.IConfigTagDTO;
import com.cs.config.idto.IConfigTagValueDTO;
import com.cs.config.idto.IConfigTaskDTO;
import com.cs.config.idto.IConfigTranslationDTO;
import com.cs.config.idto.IConfigTranslationDTO.EntityType;
import com.cs.config.idto.IConfigUserDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;

/**
 * @author vallee
 */
public interface IRefConfigurationDAO {

  /**
   * Get property collection configuration data
   *
   * @param localeID the language of labels
   * @param collectionCodes the set of property collections or all property collections if empty
   * @return the corresponding set of DTOs
   */
  public Collection<IConfigPropertyCollectionDTO> getPropertyCollections(String localeID,
          String... collectionCodes) throws CSFormatException, CSInitializationException;

  /**
   * Get attribute data
   *
   * @param localeID the language of labels
   * @param attributeCodes the set of attributes or all attributes if empty
   * @return the corresponding set of DTOs
   */
  public Collection<IConfigAttributeDTO> getAttributes(String localeID, String... attributeCodes)
          throws CSFormatException, CSInitializationException;

  /**
   * Get attribute data
   *
   * @param localeID the language of labels
   * @param attributeCodes the set of attributes or all attributes if empty
   * @return the corresponding set of DTOs
   */
  public Collection<IConfigAttributeDTO> getAttributesByType(String localeID,
          IPropertyDTO.PropertyType... attributeCodes);

  /**
   * Get tag data
   *
   * @param localeID the language of labels
   * @param tagCodes the set of group tags or all tags if empty
   * @return the corresponding set of DTOs
   */
  public Collection<IConfigTagDTO> getTags(String localeID, String... tagCodes)
          throws CSFormatException, CSInitializationException;

  /**
   * Get tagValue data
   *
   * @param localeID the language of labels
   * @param tagCode the tagValue
   * @return the corresponding tagValue DTO
   */
  public IConfigTagValueDTO getTagValueByCode(String localeID, String tagCode)
          throws CSFormatException, CSInitializationException;

  /**
   * Get relationships data
   *
   * @param localeID the language of labels
   * @param relationCodes the set of relationships or all relationships if empty
   * @return the corresponding set of DTOs
   * @throws CSInitializationException
   * @throws CSFormatException
   */
  public Collection<IConfigRelationshipDTO> getRelationships(String localeID,
          String... relationCodes) throws CSFormatException, CSInitializationException;

  /**
   * Get context data
   *
   * @param localeID the language of labels
   * @param contextCodes the set of contexts or all contexts if empty
   * @return the corresponding set of DTOs
   */
  public Collection<IConfigContextDTO> getContexts(String localeID, String... contextCodes)
          throws CSFormatException, CSInitializationException;

  /**
   * Get class data
   *
   * @param includeChildren true to export all embedded class in the same output
   * @param localeID the language of labels
   * @param classifierCodes the set of classes to export or all contexts if empty
   * @return the corresponding set of DTOs
   */
  public Collection<IConfigClassifierDTO> getClasses(boolean includeChildren, String localeID,
      EntityType klassType, String... classifierCodes) throws CSFormatException, CSInitializationException;

  /**
   * Get taxonomy data
   *
   * @param includeChildren true to export all embedded class in the same output
   * @param localeID the language of labels
   * @param classifierCodes the set of taxonomies to export or all contexts if empty
   * @return the corresponding set of DTOs
   */
  public Collection<IConfigClassifierDTO> getTaxonomies(boolean includeChildren, String localeID,
          String... classifierCodes) throws CSFormatException, CSInitializationException;

  /**
   * Get user data
   *
   * @param localeID the language of labels
   * @param userNames the set of user or all users if empty
   * @return the corresponding set of DTOs
   */
  public Collection<IConfigUserDTO> getUsers(String localeID, String... userNames) throws CSFormatException, CSInitializationException;

  /**
   * Get task data
   *
   * @param localeID the language of labels
   * @param taskCodes the set of task or all tasks if empty
   * @return the corresponding set of DTOs
   */
  public Collection<IConfigTaskDTO> getTasks(String localeID, String... taskCodes) throws CSFormatException, CSInitializationException;

  /**
   * Get rule data
   *
   * @param localeID the language of labels
   * @param ruleCodes the set of rule or all rules if empty
   * @return the corresponding set of DTOs
   */
  public Collection<IConfigDataRuleDTO> getRules(String localeID, String... rulesCodes) throws CSFormatException, CSInitializationException;

  /**
   * Get organization data
   *
   * @param localeID the language of labels
   * @param organizationCodes the set of organization or all organizations if empty
   * @return the corresponding set of DTOs
   */
  public Collection<IConfigOrganizationDTO> getOrganizations(String localeID, String... organizationCodes) throws CSFormatException, CSInitializationException;
  
  /**
   * Get language data
   *
   * @param localeID the language of labels
   * @param languageCodes the set of language or all languages if empty
   * @return the corresponding set of DTOs
   */
  public Collection<IConfigLanguageDTO> getLanguages(String localeID, String... languageCodes) throws CSFormatException, CSInitializationException;

  /**
   * Get tab data
   *
   * @param localeID the language of labels
   * @param tabCodes the set of tab or all tabs if empty
   * @return the corresponding set of DTOs
   */
  public Collection<IConfigTabDTO> getTabs(String localeID, String... tabsCodes) throws CSFormatException, CSInitializationException;
  
  /**
   * Get golden record rule data
   *
   * @param localeID the language of labels
   * @param ruleCodes the set of rule or all rules if empty
   * @return the corresponding set of DTOs
   */
  public Collection<IConfigGoldenRecordRuleDTO> getGoldenRecordRules(String localeID, String... rulesCodes)
      throws CSFormatException, CSInitializationException;  
  
  /**
   * Get translation data
   *
   * @param entityType type of entity
   * @param sortLanguage, sort language
   * @param sortBy, sort by field
   * @param languages , set of locale for translation
   * @return the corresponding set of DTOs
   */
  public Collection<IConfigTranslationDTO> getTranslations(String entityType, String sortLanguage, String sortBy, String... languages)
      throws CSFormatException, CSInitializationException;

  /**
   * Get Hierarchy taxonomy data
   *
   * @param includeChildren true to export all embedded class in the same output
   * @param localeID the language of labels
   * @param taxonomyCodes the set of taxonomy to export or all contexts if empty
   * @return the corresponding set of DTOs
   */
  public Collection<IConfigClassifierDTO> getHierarchyTaxonomies(boolean includeChildren, String localeID, String... taxonomyCodes)
      throws CSFormatException, CSInitializationException;
  
  /**
   * 
   * @param localeID
   * @param permissionCodes
   * @return
   * @throws CSFormatException
   * @throws CSInitializationException
   */
  public Collection<IConfigPermissionDTO> getPermissions(String localeID, String...permissionCodes) 
      throws CSFormatException, CSInitializationException;
  
}

