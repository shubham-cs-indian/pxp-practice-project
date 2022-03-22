package com.cs.core.rdbms.entity.idao;

import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.ITagValueDTO;
import com.cs.core.rdbms.coupling.idto.ICouplingDTO;
import com.cs.core.rdbms.entity.idto.*;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.EmbeddedType;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Offers the data operations of a base entity
 *
 * @author vallee
 */
public interface IBaseEntityDAO {

  /**
   * @return the current base entity DTO information represented by this interface
   */
  public IBaseEntityDTO getBaseEntityDTO();

  /**
   * Build a ClassifierDTO and create automatically a new one if classifierIID is 0
   *
   * @param classifierIID the RDBMS internal key of the classifier or 0 for creation
   * @param classifierCode the configuration code
   * @param classifierType the type of classifier
   * @return a new classifier structure
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public IClassifierDTO newClassifierDTO(long classifierIID, String classifierCode,
          IClassifierDTO.ClassifierType classifierType) throws RDBMSException;

  /**
   * Build an ValueRecordDTOBuilder with value in it
   * 
   * @param property the concerned property
   * @param value the value
   * @return a new value record builder with minimal information
   */
  public IValueRecordDTOBuilder newValueRecordDTOBuilder(IPropertyDTO property, String value);

  /**
   * Build a TagValueDTO and create automatically a new one if tagValueIID is 0
   *
   * @param tagValueCode the configuration tag value code
   * @param propertyIID the property internal identifier of the related LOV (cannot be 0 here)
   * @return a tag value DTO initialized structure
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public ITagValueDTO newTagValueDTO(String tagValueCode, long propertyIID) throws RDBMSException;

  /**
   * Build a TagRecordDTO and create a new TagValueDTO if tagValueIID is 0
   *
   * @param relevance the relevance attached to this tag value
   * @param tagValueCode the configuration tag value code
   * @return a tag record DTO initialized structure
   * @throws RDBMSException
   */
  public ITagDTO newTagDTO(int relevance, String tagValueCode) throws RDBMSException;

  /**Build an  empty TagsRecordDTOBuilder
   * @param property the concerned property
   * @return a new TagsRecordDTOBuilder structure with minimal information
   */
  public ITagsRecordDTOBuilder newTagsRecordDTOBuilder(IPropertyDTO property);
  
  /**Build an entity relation DTO builder with sideBaseEntityIID
   * 
   * @return a new IEntityRelationDTOBuilder structure with minimal information
   */
  public IEntityRelationDTOBuilder newEntityRelationDTOBuilder();

  /**
   * Build an empty EntityRelationsSetDTOBuilder where the current base entity is holder
   *
   * @param relationship defines the type of relation
   * @param side defines the side taken by the current entity
   * @return the corresponding entity relations set DTO builder
   */
  public IRelationsSetDTOBuilder newEntityRelationsSetDTOBuilder(IPropertyDTO relationship, IPropertyDTO.RelationSide side);

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
   * Build a ContextDTO and create automatically a new one if contextIID is 0
   *
   * @param contextCode the configuration code
   * @param type the type of context
   * @return a new context structure
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public IContextDTO newContextDTO(String contextCode, IContextDTO.ContextType type)
          throws RDBMSException;

  /**
   * Create the current base entity when its iid is 0, and create the declared records When the current base entity is created, its first
   * revision is created at the same time When an entity is created by this way, it is forced as a top level entity attached to the current
   * locale catalog When a coupled record is created and its source is not found, it is returned with null property record IID
   *
   * @param records the collection of property records to be created
   * @return the base entity as result of creation
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public IBaseEntityDTO createPropertyRecords(IPropertyRecordDTO... records) throws RDBMSException;

  /**
   * Update extension and contextual information of the current base entity and update the declared records No property synchronization
   * here, propertyIIDs are valued or the new property value is ignored
   *
   * @param records the collection of property records to be updated
   * @return the base entity as result of update
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public IBaseEntityDTO updatePropertyRecords(IPropertyRecordDTO... records) throws RDBMSException;

  /**
   * Update the coupling status of records when they are notified by tightly coupling rule If the record rule has to be broken, a call to
   * resolveCoupling on the record is required If not, the coupling to the source value takes place again.
   *
   * @param records the collection of property records to be resolved
   * @throws RDBMSException
   */
  public void resolveTightlyCoupledRecords(IPropertyRecordDTO... records) throws RDBMSException;

  /**
   * Remove a set of particular value records. This call doesn't delete the associated property records but it is used to remove a value
   * record attached to a specific context or locale.
   *
   * @param records the set of particular value records to be removed
   * @throws RDBMSException
   */
  public void removeValueRecords(IValueRecordDTO... records) throws RDBMSException;

  /**
   * Delete records from the current base entity
   *
   * @param records the collection of property records to be deleted
   * @throws RDBMSException
   */
  public void deletePropertyRecords(IPropertyRecordDTO... records) throws RDBMSException;

  /**
   * delete a base entity and manage deletion of all related information in the current locale
   *
   * @param isDeleteFromDICatalog if true, entity will be directly deleted.
   * @throws RDBMSException
   * @throws com.cs.core.technical.exception.CSFormatException
   * @throws SQLException 
   */
  public void delete(Boolean isDeleteFromDICatalog) throws RDBMSException, CSFormatException, SQLException;
 
  /** Deletes all variants of the entity.
   * 
   * @param isDeleteFromDICatalog if true, entity will be deleted permanently.
   * @throws RDBMSException
   * @throws CSFormatException
   * @throws SQLException
   */
  public void deleteVariants(Boolean isDeleteFromDICatalog) throws RDBMSException, CSFormatException, SQLException;

  /** Deletes Language translation of an entity.
   * @param baseEntityIID iid of entity whose translation has to be deleted.
   * @param localeId locale id to be deleted.
   * @throws RDBMSException
   */
  
  public void deleteLanguageTranslation(long baseEntityIID, String localeId) throws RDBMSException;

  /**
   * Partially load the entity of the current entity
   *
   * @param properties the list of properties to be fetched from data base
   * @return the base entity with loaded records
   * @throws RDBMSException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public IBaseEntityDTO loadPropertyRecords(IPropertyDTO... properties)
          throws RDBMSException, CSFormatException;

  /**
   * Load the coupling notification status of the current entity
   *
   * @param properties the list of properties that must be checked
   * @return the base entity with updated records (containing their transient coupling information)
   * @throws RDBMSException
   */
  public IBaseEntityDTO loadCouplingNotifications(IPropertyDTO... properties) throws RDBMSException;
  
  /**
   * 
   * @param baseEntity
   * @param propertyIID
   * @return
   * @throws RDBMSException
   */
  List<ICouplingDTO> loadCouplingConflicts(Long baseEntity, Long propertyIID) throws RDBMSException;
  

  /**
   * Retrieve all other classifiers attached to the current entity /!\ this doesn't include the nature class of the base entity
   *
   * @return the list of classifier information (including classes and taxonomies)
   * @throws RDBMSException
   */
  public List<IClassifierDTO> getClassifiers() throws RDBMSException;

  /**
   * Add new classifiers to the current entity
   *
   * @param classifiers the classifiers to be added
   * @throws RDBMSException
   */
  public void addClassifiers(IClassifierDTO... classifiers) throws RDBMSException;

  /**
   * Remove a classifier from the current entity
   *
   * @param classifiers the classifiers information to be removed
   * @throws RDBMSException
   */
  public void removeClassifiers(IClassifierDTO... classifiers) throws RDBMSException;

  /**
   * access to any parent of this entity when it is involved in an embedded part relation
   *
   * @return the parent DTO or empty DTO is not existing
   * @throws RDBMSException
   */
  public IBaseEntityDTO getParent() throws RDBMSException;

  /**
   * Add embedded entities to the current entity
   *
   * @param childType the type of embedded children
   * @param entities the embedded parts to be added
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public void addChildren(EmbeddedType childType, IBaseEntityIDDTO... entities)
          throws RDBMSException;

  /**
   * Remove embedded entities from the current entity
   *
   * @param childType the type of embedded children
   * @param entities the embedded parts to be removed
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public void removeChildren(EmbeddedType childType, IBaseEntityIDDTO... entities)
          throws RDBMSException;

  /**
   * get embedded entities from the current entity
   *
   * @param childType the type of embedded children
   * @return the embedded parts of the current object
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public List<IBaseEntityIDDTO> getChildren(EmbeddedType childType) throws RDBMSException;


  /**
   * @return the contextual linked entities of an embedded variant
   * @throws RDBMSException
   */
  Set<IBaseEntityDTO> getContextualLinkedEntities() throws RDBMSException;
  
  public Map<Long, IValueRecordNotificationDTO> getLanguageNotifiedProperties(Set<Long> propertIIDs) throws RDBMSException ;
  
  /**
   * Check whether the given property record is duplicate.
   * @param propertyRecord property record for which duplicate check is required.
   * @return True if the record is duplicate else false.
   * @throws RDBMSException
   */
  public Boolean isPropertyRecordDuplicate(IPropertyRecordDTO propertyRecord) throws RDBMSException;

  /**
   * Check whether the given Base Entity record is duplicate.
   * @param parentIID , parentEntityIID for duplicate check 
   * @return True if the record is duplicate else false.
   * @throws RDBMSException
   */
  public Boolean isEmbeddedEntityDuplicate(long parentIID) throws RDBMSException;
  
  public List<Long> getContextualObjectIIdsForVariantsToDelete(long assetInstanceId,
      List<String> autoCreateContextCodes) throws RDBMSException;
  
  /***
   * Check whether the given hash exist for given catalog, organization &
   * endpoint, returns the list of IIds with same hash.
   * 
   * @param hashKey
   * @param idToExclude
   * @param physicalCatalogId
   * @param organizationId
   * @param endpointId
   * @return list of IIds with same hash
   * @throws RDBMSException
   */
  public List<Long> checkIfDuplicateAssetExist(String hashKey, long idToExclude,
      String physicalCatalogId, String organizationId, String endpointId) throws RDBMSException;
  
  /**
   * Get all duplicate asset instamces with given hash code, catalogcode,
   * organizationcode
   * 
   * @param hashKey
   * @param idsToExclude
   * @param physicalCatalogId
   * @param organizationId
   * @return all assets excluding main asset if hash already exist
   * @throws RDBMSException
   */

  public List<IBaseEntityDTO> getDuplicateBaseEntities(String hashCode, long idToExclude,
      String catalogCode, String organizationCode) throws RDBMSException;

  public void updateExpiryStatusForAssetByIID(long baseEntityIID, boolean status) throws RDBMSException;
  
  /**
   * Create Language Translation and update baseEntity(last modified by)
   * @throws RDBMSException
   */
  public void createLanguageTranslation() throws RDBMSException;
  
  /** creates Language Translation.
  /*
   * @param localeIds Locale ids.
   * @throws RDBMSException
   */
  public void createLanguageTranslation(List<String> localeIds) throws RDBMSException;
  
  /**
   * Check and return baseEntityIIds which exist in given locale.
   * @param baseEntityIIds
   * @param localeId
   * @return
   * @throws RDBMSException
   */
  public List<Long> getIIdsExistInLocale(List<Long> baseEntityIIds, String localeId)
      throws RDBMSException;
  
  /**
   * Filter baseEntityIIds on the basis of classifierIIds.
   * @param baseEntityIIds
   * @param classifierIIds
   * @return
   * @throws RDBMSException
   */
  public List<Long> filterIIdsByClassifierIIds(List<Long> baseEntityIIds, List<Long> classifierIIds)
      throws RDBMSException;
  
  /**
   * Update the passed IIds instance's isDuplicate column to true.
   * @param baseEntityIIds
   * @return
   * @throws RDBMSException
   */
  public void markAssetsDuplicateByIIds(Set<Long> baseEntityIIds, boolean isDuplicate) throws RDBMSException;

  /** Detect  all duplicate assets in baseentity table
   * @param organizationId
   * @return IIDs of deteted duplicate assets
   * @throws SQLException
   * @throws RDBMSException
   */
  Set<Long> detectAllDuplicateAssets(String organizationId) throws RDBMSException;

  /**
   * Handle already deleted duplicate asset by setting isDuplicate = false
   * @param baseEntityIIDs
   * @throws RDBMSException
   */
  void handleDeletedDuplicateAssets(Set<Long> baseEntityIID) throws RDBMSException;
  
  /**
   * Returns a new Default image for the Article from its existing relationships.  
   * While searching, priority is given to Standard Article-Asset Relationship. If not found there,
   * it will return any random image from other available relationships. 
   * @return new Default image.
   * @throws RDBMSException
   */
  public Long getNewDefaultImage() throws RDBMSException;

  /***
   * Returns the list of TIV's baseentityiids filtered on basis of passed parentIId and classifierCode.
   * 
   * @param baseEntityIId
   * @param classifierCode
   * @return
   * @throws RDBMSException
   */
  public List<Long> getIIdsByParentIIdAndClassifierCode(long baseEntityIId, String classifierCode) throws RDBMSException;
   
  /** 
   * @param records
   * @throws RDBMSException
   */
  public void registerForCoupledEvent(IPropertyRecordDTO... records) throws RDBMSException;
  
  
  /**
   * @return LocaleCatalog of the base Entity.
   * @throws RDBMSException
   */
  public ILocaleCatalogDAO getLocaleCatalog();

  /**
   * Create an empty base entity with pre defined IID
   * @throws RDBMSException
   */
  public void createBaseEntityWithPreDefiniedIID() throws RDBMSException;

  public IBaseEntityDTO getAllImmediateChildren(Long parentbaseEntityIID) throws RDBMSException;
  
  public void moveToArchival(Long baseEntityIID, String baseEntityPXON, String assetObjectKey) throws RDBMSException;
 
  public IBaseEntityDTO loadAllPropertyRecords(IPropertyDTO... properties) throws RDBMSException, SQLException, CSFormatException;

  /**put entry into assetstobepurged
   * @throws RDBMSException
   */
  public void moveAssetToPurge() throws RDBMSException;
  
  /**
   * Get auto created TIV details for purging.
   * @param parentiid
   * @param contextIIDsString
   * @throws RDBMSException
   */
  public void prepareAssetPurgeDTOListForVariants(Long parentiid, String contextIIDsString) throws RDBMSException;

  public List<IPropertyRecordDTO> getConflictingProperties() throws RDBMSException;
  
  
  /**
   * Update Base entity
   * @param baseEntityDTO
   * @param userIID
   * @throws RDBMSException
   */
  public void updateBaseEntity(long userIID) throws RDBMSException;

  Map<Long, Set<Long>> findTargets(Set<IPropertyRecordDTO> records) throws RDBMSException;

  Boolean updateCalculatedPropertyRecords(ILocaleCatalogDAO catalog, IPropertyRecordDTO record, IPropertyRecordDTO recordArchived)
      throws RDBMSException;

  public Set<IBaseEntityDTO> fetchChildren() throws RDBMSException;
  public void createCalulatedPropertyRecord(List<IPropertyRecordDTO> records) throws RDBMSException;

  public Map<Long, List<ICouplingDTO>> loadCouplingConflicts(List<Long> propertyIIDs) throws RDBMSException;
}
