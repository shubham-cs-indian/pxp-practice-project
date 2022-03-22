package com.cs.core.rdbms.process.idao;

import com.cs.core.elastic.das.ISearchDAO;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.rdbms.collection.idao.ICollectionDAO;
import com.cs.core.rdbms.config.idto.*;
import com.cs.core.rdbms.coupling.idao.ICouplingDAO;
import com.cs.core.rdbms.coupling.idto.ICouplingDTOBuilder;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.*;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.ICollectionDTO.CollectionType;
import com.cs.core.rdbms.entity.idto.ISearchDTOBuilder.RootFilter;
import com.cs.core.rdbms.process.idto.IRuleResultDTO;
import com.cs.core.rdbms.relationship.idao.IRelationCoupleRecordDAO;
import com.cs.core.rdbms.rsearch.idto.IValueCountDTO;
import com.cs.core.rdbms.task.idao.ITaskRecordDAO;
import com.cs.core.rdbms.taxonomyinheritance.idao.ITaxonomyInheritanceDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.rdbms.uniqueViolation.idao.IUniquenessViolationDAO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.runtime.entity.dao.IEntityViolationDAO;
import com.cs.di.runtime.entity.dao.INotificationDAO;
import com.cs.di.runtime.entity.dao.IWorkflowStatusDAO;

import java.sql.SQLException;
import java.util.*;

/**
 * Represents a catalog in a locale (e.g. Onboarding in fr_FR) Notice: DI and catalogs are here proceeded the same way.
 *
 * @author vallee
 */
public interface ILocaleCatalogDAO {

  /**
   * @return the current locale and catalog identification of the current interface
   */
  public ILocaleCatalogDTO getLocaleCatalogDTO();

  /**
   * Build a IBaseEntityIDDTOBuilder
   * @param baseEntityID the ID of the object
   * @param baseType the base type of the object
   * @param natureClass then nature classifier of the object
   * @return IBaseEntityIDDTOBuilder with minimal information
   */
  @SuppressWarnings("rawtypes")
  public IBaseEntityIDDTOBuilder newBaseEntityIDDTOBuilder(String baseEntityID, BaseType baseType,
      IClassifierDTO natureClass);
  
  /** Build a IBaseEntityDTOBuilder
   * @param baseEntityID  the ID of the object
   * @param baseType the base type of the object
   * @param natureClass  then nature classifier of the object
   * @return a new base entity DTO for creation purposes  base
   *         Locale ID and Catalog initialized
   */
  public IBaseEntityDTOBuilder newBaseEntityDTOBuilder(String baseEntityID, BaseType baseType,
      IClassifierDTO natureClass);
  
  /**
   * Build a PropertyDTO and create automatically a new one if propertyIID is 0
   *
   * @param propertyIID the RDBMS internal key of the property or 0 for creation
   * @param propertyCode the configuration code
   * @param propertyType the type of property
   * @return a property DTO initialized structure
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public IPropertyDTO newPropertyDTO(long propertyIID, String propertyCode,
          IPropertyDTO.PropertyType propertyType) throws RDBMSException;

  /**
   * Build a ClassifierDTO and create automatically a new one if classifierIID is 0
   *
   * @param classifierIID the RDBMS internal key of the property or 0 for creation
   * @param classifierCode the configuration code
   * @param classType the type of classifier
   * @return a classifier DTO initialized structure
   * @throws RDBMSException
   */
  public IClassifierDTO newClassifierDTO(long classifierIID, String classifierCode,
          IClassifierDTO.ClassifierType classType) throws RDBMSException;

  /**
   * Build a ContextDTO and create automatically a new one if contextIID is 0
   *
   * @param contextCode the context code
   * @param type the type of context
   * @return a context DTO initialized structure
   * @throws RDBMSException
   */
  public IContextDTO newContextDTO(String contextCode, IContextDTO.ContextType type)
          throws RDBMSException;

  /**
   * Build a taskDTO and create automatically a new one if taskIID is 0
   *
   * @param taskCode the configuration code
   * @param taskType the type of task like personal or shared
   * @return a task DTO initialized structure
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public ITaskDTO newTaskDTO(String taskCode,
          ITaskDTO.TaskType taskType) throws RDBMSException;

  /**
   * 
   * @param collectionType
   * @param collectionCode
   * @param organizationCode
   * @return a collection DTO initialized structure
   * @throws RDBMSException
   * @throws CSFormatException 
   */
 public ICollectionDTOBuilder newCollectionDTOBuilder(CollectionType collectionType, String collectionCode, 
      String organizationCode) throws RDBMSException, CSFormatException;

 /**
  * 
  * @param targetIID the IID of violated baseEntity
  * @param propertyIID the IID of Product Identifier 
  * @param classifierIID the IID of classifier
  * @return UniqueViolationDTO 
  */
 
 public IUniquenessViolationDTOBuilder newUniquenessViolationBuilder(long sourceIID, long targetIID,long propertyIID, 
     long classifierIID) throws RDBMSException;
  /**
   * Open a base entity DAO based on a new or retrieved DTO information. 
   * Notice: for consistency, it is checked the entity is correctly
   * initialized with the localeCatalogDTO of this DAO if not, an exception is raised
   *
   * @param entity the DTO information
   * @return an instance of DAO for operations on a base entity
   * @throws RDBMSException
   */
  public IBaseEntityDAO openBaseEntity(IBaseEntityIDDTO entity) throws RDBMSException;

  /**
   * Open a base entity DAO based on a new or retrieved DTO information.
   * If ever the catalog code of this entity is not corresponding to the current catalogDAO,
   * then the corresponding catalogDAO is opened.
   *
   * @param entity the DTO information
   * @return an instance of DAO for operations on a base entity
   * @throws RDBMSException
   */
  public IBaseEntityDAO openAnyBaseEntity(IBaseEntityIDDTO entity) throws RDBMSException;

  /**
   * Open a rule DAO to access to dashboard services
   *
   * @return an instance of DAO to drive dashboards
   */
  public IRuleCatalogDAO openRuleDAO();

  /**
   * 
   * @return an instance of CollectionDAO
   * @throws RDBMSException
   */
  public ICollectionDAO openCollection() throws RDBMSException;
  
  /**
   * 
   * @return an instance of UniquenessDAO
   * @throws RDBMSException
   */
  public IUniquenessViolationDAO openUniquenessDAO() throws RDBMSException;
  
  /**
   * Open a task DAO to access all services related to task
   *
   * @return an instance of taskrecord DAO
   */
  public ITaskRecordDAO openTaskDAO();

  /**
   * For current RDBMS operations, make this locale inheritance schema as applicable As for instance, one wants to consider the following
   * inheritance schema: French for Canada inherits English for Canada which inherits English for USA Then the corresponding chained list
   * contains by order: "fr_CA", "en_CA", "en_US" Notice: if this application is done when a localeID is already defined (e.g. when working
   * from an openLocaleCatalog), then this localeID will be automatically added in last position to the list. As for instance, working from
   * a fr_CA locale catalog, the schema is not changed, working from a fr_FR locale catalog, the schema is changed into: "fr_FR", "fr_CA",
   * "en_CA", "en_US" But when working from a en_CA locale catalog, the schema is automatically changed into: "en_CA", "en_US"
   *
   * @param localeInheritance is the chained list of applicable schema, from the less to the most common ancestor This declaration is
   * immutable until an other call to applyLocaleInheritanceSchema()
   */
  public void applyLocaleInheritanceSchema(List<String> localeInheritance);

  /**
   * Retrieve an entity by IID at any level /!\ the returned object may not belong to the current catalog
   *
   * @param objectIID the object IID to be found in any catalog
   * @return the object DTO or null if not found
   * @throws RDBMSException
   */
  public IBaseEntityDTO getEntityByIID(long objectIID) throws RDBMSException;

  /**
   * Retrieve an entity by IID at any level with minimal information without name attribute and only contain base locale.
   *
   * @param objectIID the object IID to be found in any catalog
   * @return the base entity DTO with minimal information or null if not found,
   * @throws RDBMSException
   */
  public IBaseEntityDTO getBaseEntityDTOByIID(long objectIID) throws RDBMSException;
  
  public List<IBaseEntityDTO> getBaseEntityDTOsByIIDs(Set<Long> objectIIDs) throws RDBMSException;
  
  /**
   * Retrieve an entity by ID from the current catalog without name attribute and only contain base locale.
   *
   * @param objectID the id of the object to be found
   * @return the base entity DTO with minimal information or null if not found,
   * @throws RDBMSException
   */
  
  public IBaseEntityDTO getBaseEntityDTOByID(String objectID) throws RDBMSException;
  
  
  /**
   * Retrieve an entity by ID from the current catalog excluding embedded entities and relationship extensions
   *
   * @param objectID the id of the object to be found
   * @return the object DTO or null if not found
   * @throws RDBMSException
   */
  public IBaseEntityDTO getEntityByID(String objectID) throws RDBMSException;

  /**
   * Retrieve all existing properties of a base entity
   *
   * @param objectIID the object IID to be found in any catalog
   * @return the set of existing properties for this object
   * @throws RDBMSException
   */
  public Collection<IPropertyDTO> getAllEntityProperties(long objectIID) throws RDBMSException;

  /** Retrieves all existing properties of a base entity according to supplied locale id.
   * @param entityiid iid of the base entity
   * @param localeid
   * @return all value records of the base entity as per locale id.
   * @throws RDBMSException 
   */
  public Set<IValueRecordDTO> getAllEntityPropertiesByLocaleId(long entityiid, String localeid) throws RDBMSException;
  
  /** This method will retrieve all locale ids for the given baseEntityDTO.
   * @param baseEntityDTO
   * @return baseEntityDTO filled with locale ids.
   * @throws RDBMSException
   */
  public IBaseEntityDTO loadLocaleIds(IBaseEntityDTO baseEntityDTO) throws RDBMSException;

  /**
   * Create a cloned base entity into the same catalog
   *
   * @param originBaseEntityIID the identifier of the origin
   * @param classifiers the set of classifiers to be cloned (if empty clone all)
   * @param properties the set of properties that are cloned (if null, all properties are considered)
   * @return the cloned base entity DTO
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public IBaseEntityDTO cloneEntityByIID( long originBaseEntityIID, Set<IClassifierDTO> classifiers,  IPropertyDTO... properties)
      throws RDBMSException, CSFormatException, CSInitializationException;

  public IBaseEntityDTO createManualLinkedVariant(long originBaseEntityIID, IClassifierDTO natureClassifier,
      Set<IClassifierDTO> classifiers, String linkedVariantEntityID, BaseType linkedVariantBaseType, IPropertyDTO... properties)
      throws RDBMSException, CSFormatException;
  /**
   * Retrieve all objects of a defined type from the current catalog
   *
   * @param baseType the base type of object
   * @return a cursor opened on existing objects ordered by last modified date descending
   * @throws RDBMSException
   */
  public IRDBMSOrderedCursor<IBaseEntityDTO> getAllEntities(IBaseEntityIDDTO.BaseType baseType)
          throws RDBMSException;

  /**
   * Retrieve all objects of a defined type from the current catalog and filtered by name
   *
   * @param baseType the base type of object
   * @param nameLike at least the 3 first letters of the object name to be searched
   * @return a cursor opened on existing objects ordered by last modified date descending
   * @throws RDBMSException
   */
  public IRDBMSOrderedCursor<IBaseEntityDTO> getAllEntitiesByNameLike(
          IBaseEntityIDDTO.BaseType baseType, String nameLike) throws RDBMSException;

  /**
   * Retrieve all objects of a defined type from the current catalog and filtered by text attribute value The retrieved objects are returned
   * with pre-loaded value records that match the search
   *
   * @param baseType the base type of object
   * @param searchText text attribute value to be searched
   * @return a cursor opened on existing objects ordered by last modified date descending
   * @throws RDBMSException
   */
  public IRDBMSOrderedCursor<IBaseEntityDTO> getAllEntitiesByText(
          IBaseEntityIDDTO.BaseType baseType, String searchText) throws RDBMSException;

  /**Retrieve all base entity according to search expression generated.
   * 
   * @param searchExpression expression to search
   * @return Ordered Cursor to get base entities
   * @throws RDBMSException
   */
  public IRDBMSOrderedCursor<IBaseEntityDTO> getAllEntitiesBySearchExpression(String searchExpression) 
		throws RDBMSException;
  /**
   * 
   * @param allowChildren if children should be considered
   * @param searchExpression expression to search
   * @param classifierIds
   * @return Map of classifier code against count of occurence of classifier.
   * @throws RDBMSException
   */
  public Map<String, Long> getClassCount(Boolean allowChildren, String searchExpression, List<String> classifierIds) throws RDBMSException;

  /**
   *
   * @param allowChildren if children should be considered
   * @param searchExpression expression to search
   * @param classifierIds
   * @return Map of classifier code against count of occurence of classifier.
   * @throws RDBMSException
   */
  public Map<String, Long> getTaxonomyCount(Boolean allowChildren, String searchExpression, List<String> classifierIds) throws RDBMSException;

  /**
   * 
   * @param allowChildren if child base entity should be allowed
   * @param searchExpression expression to be searched
   * @param propertyCodes propertyCode of property  to Filter
   * @return
   * @throws RDBMSException
   */
  public List<IValueCountDTO> getPropertyCount(Boolean allowChildren, String searchExpression,
      Collection<IPropertyDTO> propertyCodes) throws RDBMSException;
  

  /**
   * 
   * @param allowChildren if child base entity should be allowed
   * @param searchExpression expression to be searched
   * @return
   * @throws RDBMSException
   */
  public IRuleResultDTO getDataQualityCount(Boolean allowChildren, String searchExpression) throws RDBMSException;

  /**
   * 
   * @param areChildrenAllowed if child base entity should be allowed
   * @param searchExpression expression to be searched
   * @return
   * @throws RDBMSException
   */
  public IRDBMSOrderedCursor<IBaseEntityDTO> getAllEntitiesBySearchExpression(String searchExpression, Boolean areChildrenAllowed) 
      throws RDBMSException;

  public List<IBaseEntityDTO> getAllEntitiesByIIDList(String masterAssetIds, String assetIdListTIV, boolean shouldFetchMasterAssets) throws RDBMSException;

  public IWorkflowStatusDAO openWorkflowStatusDAO();



  List<Long> updateAssetExpiryStatus(long currentTimeStamp) throws RDBMSException;

  /**
   *
   * @param baseEntityIIDs entities of which linked variant IIDs are required
   * @param linkedRelationshipIds relationships which have linkedRelationshipIds
   * @return
   * @throws RDBMSException
   */
  public Map<Long, Long> getLinkedVariantIds(Collection<IBaseEntityDTO> baseEntityIIDs , Collection<String> linkedRelationshipIds) throws RDBMSException;

  public List<Long> getOtherSideInstanceIIds(String side, String id, Long relationPropertyIId) throws RDBMSException;

  public INotificationDAO openNotificationDAO();
  
  public IRelationCoupleRecordDAO openRelationCoupleRecordDAO();

  /**
   *  create new TaxonomyInheritanceDTOBuilder
   * @param entityIID
   * @param sourceEntityIID
   * @param relationshipIID
   * @return ITaxonomyInheritanceDTOBuilder
   * @throws RDBMSException
   * @throws CSFormatException
   */
  public ITaxonomyInheritanceDTOBuilder newTaxonomyInheritanceDTOBuilder(long entityIID, long sourceEntityIID, long relationshipIID) throws RDBMSException, CSFormatException;

  /**
   * Open a Taxonomy Inheritance DAO based on a new or retrieved DTO information.
   * @param entity
   * @return an instance of TaxonomyInheritanceDAO
   * @throws RDBMSException
   */
  public ITaxonomyInheritanceDAO openTaxonomyInheritance(ITaxonomyInheritanceDTO entity) throws RDBMSException;

  /**
   * 
   * @param entityIID
   * @return the Taxonomy Inheritance DTO or null if not found
   * @throws RDBMSException
   */
  public ITaxonomyInheritanceDTO getTaxonomyConflict(long entityIID) throws RDBMSException;
  
  public ICouplingDTOBuilder newCouplingDTOBuilder() throws RDBMSException;
  
  public ICouplingDAO openCouplingDAO() throws RDBMSException;

  /**
   * Post an end event to update elastic document.
   * @param baseEntityIID entity whose elastic document needs to be updated.
   * @param event Event that is to be raised.
   * @throws RDBMSException
   */
  public void postUsecaseUpdate(long baseEntityIID, IEventDTO.EventType event) throws RDBMSException;

  /**
   * Post an end event to update elastic document.
   * @param baseEntityDTO entity whose elastic document needs to be updated.
   * @throws RDBMSException
   */
  public void postUsecaseUpdate(IBaseEntityDTO baseEntityDTO) throws RDBMSException;

  
 /**
  * Post an end event to update elastic document.
  *
  * @param baseEntity entity whose elastic document needs to be deleted.
  * @throws RDBMSException
  */
 public void postDeleteUpdate(IBaseEntityDTO baseEntity) throws RDBMSException;

  /**
   * Get List of recently created duplicate baseEntityiids
   * @param size
   * @param physicalCatalogId
   * @param organisationId
   * @param endPointId
   * @param sortOrder
   * @return
   * @throws RDBMSException
   */
  public List<String> getDuplicateAssetInstances(int size, String physicalCatalogId, String organisationId, String endPointId, String sortOrder) throws RDBMSException;

  /**
   * Gives all Article IIDs whose default image is same as the given assetIIDs.
   * @param assetIIDs iids of the asset 
   * @return List of iids of all articles with same default image.
   * @throws RDBMSException
   */
  public List<Long> getArticlesWithGivenDefaultImage(List<String> assetIIDs) throws RDBMSException;
  
  /**
   * @return
   * @throws RDBMSException
   */
  public IEntityViolationDAO openEntityViolationDAO() throws RDBMSException;

  /**
  *
  * @param baseEntityIIDs ids of baseentities to get
  * @return
  * @throws RDBMSException
  */
 public List<IBaseEntityDTO> getBaseEntitiesByIIDs(List<String> baseEntityIIDs) throws RDBMSException;

 /**
  *
  * @param typesToFilter
  * @return
  */
 ISearchDTOBuilder getSearchDTOBuilder(List<BaseType> typesToFilter, RootFilter root, Boolean isArchivePortal);

 /**
  *
  * @param searchDTO DTO filled with specifics of search
  * @return searchDAO A class that provides functionality for search.
  */
 public ISearchDAO openSearchDAO(ISearchDTO searchDTO);

 /**
 *
 * @param searchDTO DTO filled with specifics of search
 * @return searchDAO A class that provides functionality for golden record bucket search.
 */
 public ISearchDAO openGoldenRecordBucketSearchDAO(ISearchDTO searchDTO);

 /**
  * Returns a map of Rule-Violation color and corresponding list of locales in which given Entity is violated.
 * @param baseEntityIID IID of the Entity.
 * @return Map of Color VS Locales.
 * @throws RDBMSException
 */
public Map<String, List<String>> getViolationVSLocalesMap(Long baseEntityIID) throws RDBMSException;

/**
 * Returns all Base Entity IIDs present in the table.
 * @return Base Entity IIDs.
 * @throws RDBMSException
 */
public Set<Long> getAllBaseEntityIIDs() throws RDBMSException;

/**
 * @param taxonomyIds
 * @return
 * @throws RDBMSException
 */
public Set<String> getAllTaxonomyParents(Set<String> taxonomyIds) throws RDBMSException;

/**
 * Returns map of baseEntityIID VS value records of all locales in which given Base Entity is present.
 * It will also fetch records that are coupled from other Entity.
 * @param baseEntityIIDs iids of base Entities.
 * @return Map of iid VS value records.
 * @throws RDBMSException
 */
public Map<Long, List<IValueRecordDTO>> getAllValueRecords(Long... baseEntityIIDs) throws RDBMSException;

/**
 * Returns map of baseEntityIID VS all Tag records of given Base Entity.It will also fetch records that are coupled
 *  from other Entities.
 * @return Map of iid VS Tag records.
 * @throws RDBMSException
 */
public Map<Long, List<ITagsRecordDTO>> getAllTagsRecords(Long... baseEntityIIDs) throws RDBMSException;

/**
 * Returns map of baseEntityIID VS all Side 1 Relation records of given Base Entity.
 * @return Map of iid VS Relation records.
 * @throws RDBMSException
 */
public Map<Long, List<IRelationsSetDTO>> getAllRelationshipRecords(Long... baseEntityIIDs) throws RDBMSException;

/**
 * Returns IID of Base Entities who are childrens of given base Entity. Note that it returns nested children IIDs too.
 * @param baseEntityIID parent's IID.
 * @return List of children IIDs.
 * @throws RDBMSException
 */
public List<Long> getAllChildrenIIDs(Long baseEntityIID) throws RDBMSException;

 /***
  * Retrieve all base entity iids according to search expression.
  * @param searchExpression
  * @return
  * @throws RDBMSException
  */
 public List<Long> getAllEntityIIdsBySearchExpression(String searchExpression) throws RDBMSException;
 
 /**
  * Retrieve all base entity using params
  * @param entityID
  * @param catalogCode
  * @param organizationCode
  * @param endpointCode
  * @return
  * @throws RDBMSException
  */
 public long getEntityIID(String entityID, String catalogCode, String organizationCode, String endpointCode) throws RDBMSException;
 
 /**
  * Retrieve base entity using iid
  * @param entityIID
  * @return
  * @throws RDBMSException
  */
 public String getEntityIdByIID(long entityIID) throws RDBMSException;
 
 /** Retrieve base entity iid using id
 * @param id
 * @param endpointCode
 * @return
 * @throws RDBMSException
 */
public long getEntityIIDByID(String id, String endpointCode) throws RDBMSException;

 /**
  *
  * @param contextualCodes
  * @return
  * @throws RDBMSException
  */
 public List<Long> loadContextualEntityIIDs(List<String> contextualCodes, Long parentIID) throws RDBMSException;

 /**
  *
  * @return userSessionDTO
  */
 public IUserSessionDTO getUserSessionDTO();

 public List<IBaseEntityDTO> getBaseEntitiesFromArchive(List<String> baseEntityIIDs) throws RDBMSException;

 public void deleteFromArchive(Long baseEntityIIDs) throws RDBMSException;
 
 /** Returns a map of BaseEntity IID VS its 'Other' classifier codes. 
  * @param baseEntityIID IIDs of base entity.
  * @return Map of IID vs Classifier codes.
  * @throws RDBMSException
  */
 Map<Long, Map<String, List<String>>> getEntityVSotherClassifierCodes(Long... baseEntityIID) throws RDBMSException;

 /**
  * 
  * @param baseEntityIIds
  * @return
  * @throws RDBMSException
  */
 public Map<Long,String> getBaseEntityNamesByIID(Set<Long> baseEntityIIds) throws RDBMSException;

 /**
  * 
  * @param creationLanguage
  * @param creationBaseType
  * @param baseType
  * @param baseEntityId
  * @param endpoint 
  * @return
  */
 public IBaseEntityDTO createGoldenRecordArticle(String creationLanguage, IClassifierDTO creationBaseType, BaseType baseType,
     String baseEntityId, String endpoint);

 public List<IClassifierDTO> getOtherClassifiers(Long baseEntityIID)
     throws RDBMSException;
 
 public  Map<Long,Set<IPropertyRecordDTO>> getPropertyRecordsForEntities(Set<Long> entityIIds, IPropertyDTO... properties)
     throws RDBMSException, CSFormatException;

 List<IPropertyDTO> getReferencedPropertiesByCodes(HashSet<String> union) throws RDBMSException, SQLException;
 
 public Set<Long> getOtherSideIIdsFromRelations(Long baseEntityIID) throws RDBMSException;

 public Map<Long, IJSONContent> getEntityExtensionByIIDs(Set<Long> objectIIDs) throws RDBMSException;
 
 public Map<Long,List<Long>> getAllChildrenIIDsForEntities(List<Long> baseEntityIIDs) throws RDBMSException;

 public void fillAllProperties(List<IBaseEntityDTO> baseEntityDTOs)
     throws RDBMSException, SQLException, CSFormatException;

 Map<Long, List<IBaseEntityDTO>> getAllChildren(List<IBaseEntityDTO> entities) throws RDBMSException;

 public Map<Long, Map<Long,IRelationsSetDTO>> getAllRelationRecords(Long... baseEntityIIDs) throws RDBMSException;

 public void postUsecaseUpdateWithEntity(IBaseEntityDTO baseEntityDTO, IEventDTO.EventType event) throws RDBMSException;

 /**
  * 
  * @param baseEntityIIDs
  * @throws RDBMSException
  */
void deleteEntityFromArchive(Long baseEntityIIDs) throws RDBMSException;

 Map<Long, Set<Long>> getLinkedInstancesForContexts(Set<Long> contextualObjectIIDs) throws RDBMSException;

 /**
  * This method returns Set of linked instance IIDs of the given contextual object IID and basetype.
 * @param contextualObjectIID iid of the contextual object.
 * @param baseType basetype of entity.
 * @return Set of linked instances.
 * @throws RDBMSException
 */
Set<Long> getLinkedInstancesByBaseType(Long contextualObjectIID, BaseType baseType) throws RDBMSException;

 /**
  *
  * @param baseEntityDTO
  * @throws RDBMSException
  */
 public void postUsecaseUpdateWithLoadRecords(IBaseEntityDTO baseEntityDTO) throws RDBMSException;

}
