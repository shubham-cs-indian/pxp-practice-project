package com.cs.core.rdbms.config.idao;

import com.cs.core.rdbms.config.idto.*;
import com.cs.core.rdbms.entity.idto.IEntityRelationDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.rule.ICSERule.RuleType;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.technical.rdbms.idto.IUserDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Configuration data synchronization services
 *
 * @author vallee
 */
public interface IConfigurationDAO {

  /**
   * Automatically create an unique readable code from a configuration object name
   *
   * @param <T>
   * @param configType the concerned class of DTO
   * @param name the configuration object name
   * @return an unique readable user code
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public <T extends IRootConfigDTO> String getCode(Class<T> configType, String name)
          throws RDBMSException;

  /**
   * Declare a new user in RDBMS
   *
   * @param userName the user name from configuration
   * @return the DTO created for the new object
   * @throws RDBMSException
   */
  public IUserDTO createUser(String userName) throws RDBMSException;
  
  /**
   * -ALERT- Strictly use this method in case of standard user only
   * Declare a new user in RDBMS
   *
   * @param userIID , internal IID of user
   * @param userName the user name from configuration
   * @return the IUserDTO
   * @throws RDBMSException in case of data access error
   */
  public IUserDTO createStandardUser(long userIID, String userName) throws RDBMSException;

  /**
   * Declare a new context in RDBMS - do nothing when a context with same code already exists
   *
   * @param contextCode code used in configuration DB
   * @param type physical or DI catalog
   * @return the DTO created for the new object
   * @throws RDBMSException
   */
  public IContextDTO createContext(String contextCode, IContextDTO.ContextType type)
          throws RDBMSException;

  /**
   * Return a context from its code
   *
   * @param contextCode the context identifier code
   * @return the context DTO corresponding to the code
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public IContextDTO getContextByCode(String contextCode) throws RDBMSException;

  /**
   * Declare a new property in RDBMS - do nothing when a property with same ID already exists
   *
   * @param propertyCode the property code from configuration DB
   * @param propertyType type of attribute or tag
   * @return the DTO created for the new object
   * @throws RDBMSException
   */
  public IPropertyDTO createProperty(String propertyCode, IPropertyDTO.PropertyType propertyType)
          throws RDBMSException;
  
  /**
   * -ALERT- Strictly use this method in case of standard property only.
   * 
   * Declare a new standard property in RDBMS - do nothing when a property with same ID already exists
   * 
   * @param propertyIID, internal IID
   * @param propertyCode the property code from configuration DB
   * @param propertyType type of attribute or tag
   * @return the DTO created for the new object
   * @throws RDBMSException in case of data access error
   */
  public IPropertyDTO createStandardProperty(long propertyIID, String propertyCode, IPropertyDTO.PropertyType propertyType)
          throws RDBMSException;

  /**
   * Return a property from its internal IID
   *
   * @param propertyIID the property internal identifier
   * @return the DTO created for the corresponding object
   * @throws RDBMSException
   */
  public IPropertyDTO getPropertyByIID(long propertyIID) throws RDBMSException;

  /**
   * Return a property from its code
   *
   * @param propertyCode the property external identifier
   * @return the DTO created for the corresponding object
   * @throws RDBMSException
   */
  public IPropertyDTO getPropertyByCode(String propertyCode) throws RDBMSException;

  /**
   * Declare a new tag value in RDBMS - do nothing when a tag value with same ID already exists /!\ the propertyIID attached to the tag
   * value is mandatory information
   *
   * @param tagValueCode code of the tag value
   * @param propertyIID the property Tag identifier attached to that value (or 0 for automatic update)
   * @return the DTO created for the new object
   * @throws RDBMSException
   */
  public ITagValueDTO createTagValue(String tagValueCode, long propertyIID) throws RDBMSException;

  /**
   * Declare a new tag value in RDBMS - do nothing when a tag value with same ID already exists
   *
   * @param tagValueCode tag value code used in configuration DB
   * @param propertyCode the property Code identifier from configuration DB (or null for automatic update)
   * @return the DTO created for the new object
   * @throws RDBMSException - a FK exception is raised whenever propertyID has not been created before
   */
  public ITagValueDTO createTagValue(String tagValueCode, String propertyCode)
          throws RDBMSException;

  /**
   * Declare a new classifier in RDBMS - do nothing when a tag value with same ID already exists
   *
   * @param classifierCode code used for readability in configuration DB
   * @param classifierType the type of classifier: class, taxonomy, hierarchy...
   * @return the RDBMS IID created for the new object
   * @throws RDBMSException
   */
  public IClassifierDTO createClassifier(String classifierCode,
          IClassifierDTO.ClassifierType classifierType) throws RDBMSException;
  
  /**
   * -ALERT- Strictly use this method in case of standard classifier only.
   * Declare a new classifier in RDBMS  - do nothing when a classifier with same ID already exists
   * 
   * @param classifierIID internal IID of classifier
   * @param classifierCode code used for readability in configuration DB
   * @param classifierType the type of classifier: class, taxonomy, hierarchy...
   * @return the RDBMS IID created for the new object
   * @throws RDBMSException in case of data access error
   */
   public IClassifierDTO createStandardClassifier(long classifierIID, String classifierCode, IClassifierDTO.ClassifierType classifierType)
       throws RDBMSException;
  
  public List<Long> getBaseentityIIDsForTypes(List<Long> klassIds) throws RDBMSException;
  
  public List<Long> getBaseentityIIDsForTaxonomies(List<Long> classifierIIDs) throws RDBMSException;
  
  public List<Long> getBaseentityIIDsForLanguage(List<String> languageIds) throws RDBMSException;

  /**
   * Return a classifier from its internal identifier
   *
   * @param classifierIID the classifier internal identifier
   * @return the DTO created for the requested object
   * @throws RDBMSException
   */
  public IClassifierDTO getClassifierByIID(long classifierIID) throws RDBMSException;

  /**
   * Return a classifier from its code
   *
   * @param classifierCode the classifier external identifier
   * @return the DTO created for the requested object
   * @throws RDBMSException
   */
  public IClassifierDTO getClassifierByCode(String classifierCode) throws RDBMSException;

  /**
   * Declare a new task in RDBMS
   *
   * @param taskCode the code used in configuration DB
   * @param taskType the type of task: personal or shared
   * @return
   * @throws RDBMSException
   */
  public ITaskDTO createTask(String taskCode, ITaskDTO.TaskType taskType) throws RDBMSException;

  public IRuleExpressionDTO upsertRule(String code, long ruleExpressionIID, String ruleExpression, RuleType ruleType )
          throws RDBMSException, CSFormatException;

  /**
   * retrieve a full rule information from its configuration code
   *
   * @param code the configuration identifier of the rule
   * @return the full rule information
   * @throws RDBMSException
   */
  public IConfigRuleDTO getRuleByCode(String code) throws RDBMSException;
  
  /**
   * delete a set of particular contextualObjects records. This call delete the associated value records also
   *
   * @param contexts  the set of Context to be removed.
   * @throws RDBMSException
   */
  public void deleteContextualObjects(IContextDTO... contexts) throws RDBMSException;

  /**
   *
   * @param classifierCode code of classifier to create
   * @param classifierType enum defining type of classifier
   * @param parentIIds  iids of parent in heirarchy
   * @return DTO of classifier
   * @throws RDBMSException
   */

  public IClassifierDTO createTaxonomyClassifier(String classifierCode, IClassifierDTO.ClassifierType classifierType,
      List<Long> parentIIds) throws RDBMSException;
  
  /**
   * 
   * @param languageCode
   * @param parentCode
   * @throws RDBMSException
   */
  public void createLanguageConfig(String languageCode, String parentCode) throws RDBMSException;
  
  /**
   * 
   * @param languageCode
   * @return
   * @throws RDBMSException
   */
  public ILanguageConfigDTO getLanguageConfig(String languageCode) throws RDBMSException;
  
  /**
   * 
   * @param languageIID
   * @return
   * @throws RDBMSException
   */
  public ILanguageConfigDTO getLanguageConfigByLanguageIID(Long languageIID) throws RDBMSException;
  
  /**
   * 
   * @param languageIID
   * @return
   * @throws RDBMSException
   */
  public List<Long> getChildLanguageByLanguageIID(Long languageiid) throws RDBMSException;
  
  /**
   * 
   * @param code
   * @throws RDBMSException 
   */
  public void deleteLanguageConfigByCodes(List<String> code) throws RDBMSException;
 
  public Map<String, Long>  getTagPropertyIdByCode(IEntityRelationDTO relation) throws RDBMSException;
  
  /** Returns Set of Classifier IIDs for the given classifiercodes. This codes can be of nature as well as of other types.
   * @param classifierCodes 
   * @return Set of Classifier IIDs.
   * @throws RDBMSException
   */
  public Set<Long> getClassifierIIDsFromCodes(Set<String> classifierCodes) throws RDBMSException;
  
  /**
   * Get a list of organizationId's having runtime instances.
   * 
   * @param partnerIdsToBeDeleted list of organizationIds to be deleted
   * @return List of organizationIds having runtime instance
   * @throws RDBMSException
   */
  public List<String> getOrganizationIdHavingRuntimeInstance(List<String> partnerIdsToBeDeleted) throws RDBMSException;
  
  /**
   * Get List of tagValueCode by propertyIID(tag value IID)
   * @param propertyIID
   * @return
   * @throws RDBMSException
   */
  public List<String> getTagValueByIID(long propertyIID) throws RDBMSException;
}
