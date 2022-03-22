package com.cs.core.rdbms.entity.dao;

import java.sql.SQLException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.eventqueue.dao.EventQueueDAS;
import com.cs.core.eventqueue.idto.IEventDTO.EventType;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.coupling.dto.CouplingSourceDTO;
import com.cs.core.rdbms.coupling.idto.ICouplingSourceDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSDataAccessService;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.dto.RelationsSetDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO;
import com.cs.core.rdbms.entity.idto.ILanguageTranslationDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.rdbms.services.records.IRecordDAS;
import com.cs.core.rdbms.services.records.RelationsSetDAS;
import com.cs.core.rdbms.tracking.dto.TimelineDTO;
import com.cs.core.rdbms.tracking.dto.UserSessionDTO;
import com.cs.core.rdbms.tracking.idto.ITimelineDTO;
import com.cs.core.rdbms.tracking.idto.ITimelineDTO.ChangeCategory;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * Service of posting events service on entity changes
 * @author vallee
 */
public class EntityEventDAS extends RDBMSDataAccessService {

  private final UserSessionDTO userSession;
  private final LocaleCatalogDTO catalog;
  private final BaseEntityDTO entity;
  private final EventQueueDAS eventDAS;
  private final TimelineDTO changedData = new TimelineDTO();
  
  @Autowired
  protected ISessionContext                  context;

  /**
   * DAS constructor
   *
   * @param currentConn the current transaction session in which the service takes place
   * @param session the user session
   * @param catalog the current locale catalog in which changes take place
   * @param entity the entity that holds the changes
   */
  public EntityEventDAS( RDBMSConnection currentConn, IUserSessionDTO session, ILocaleCatalogDTO catalog, IBaseEntityDTO entity)
  {
    super(currentConn);
    this.userSession = (UserSessionDTO) session;
    this.catalog = (LocaleCatalogDTO) catalog;
    this.entity = (BaseEntityDTO) entity;
    this.eventDAS = new EventQueueDAS( currentConn, userSession);
  }

  /**
   * @return the JSON trace of registered changes
   * @throws CSFormatException
   */
  JSONContent getRegisteredChanges() throws CSFormatException {
    return new JSONContent( changedData.toJSON());
  }

  /**
   * Added classifiers event posting
   * @param classifiers added classifiers
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public void postAddedClassifiers( IClassifierDTO... classifiers) throws RDBMSException, SQLException, CSFormatException {
      eventDAS.postClassifierAdded(entity,
              catalog, new TimelineDTO( ChangeCategory.AddedClassifier, classifiers));

  }

  /**
   * Removed classifiers event posting
   * @param classifiers
   * @throws RDBMSException
   * @throws SQLException
   * @throws CSFormatException
   */
  public void postRemovedClassifiers(IClassifierDTO[] classifiers) throws RDBMSException, SQLException, CSFormatException {
      eventDAS.postClassifierRemoved(entity, catalog,
              new TimelineDTO( ChangeCategory.RemovedClassifier, classifiers));
  }

  /**
   * Added children event posting
   * @param entities
   * @throws RDBMSException
   * @throws SQLException
   * @throws CSFormatException
   */
  public void postAddedChildren(IBaseEntityIDDTO... entities) throws RDBMSException, SQLException, CSFormatException {
      eventDAS.postEntityEvent( entity,
              EventType.CHILDREN_ADDED,
              new TimelineDTO( ChangeCategory.AddedChild, entities), catalog.getLocaleID());
  }

  /**
   * Removed children event posting
   * @param entities
   * @throws RDBMSException
   * @throws SQLException
   * @throws CSFormatException
   */
  public void postRemovedChildren(IBaseEntityIDDTO... entities) throws RDBMSException, SQLException, CSFormatException {
      eventDAS.postEntityEvent( entity,
              EventType.CHILDREN_REMOVED,
              new TimelineDTO( ChangeCategory.RemovedChild, entities), catalog.getLocaleID());
  }

   /**
   * Post an event concerning the creation of an entity
   * @return the track IID of the event
   * @throws RDBMSException
   * @throws SQLException
   * @throws CSFormatException
   */
  public long postCreatedEntity() throws RDBMSException, SQLException, CSFormatException {
    return eventDAS.postEntityEvent(entity, EventType.OBJECT_CREATION,
              new TimelineDTO(ITimelineDTO.ChangeCategory.CreatedEntity, entity), catalog.getLocaleID());
  }

   /**
   * Post an event concerning the deletion of an entity
   * @return the track IID of the event
   * @throws RDBMSException
   * @throws SQLException
   * @throws CSFormatException
   */
  public long postDeletedEntity() throws RDBMSException, SQLException, CSFormatException {
    return eventDAS.postEntityEvent(entity, EventType.OBJECT_DELETE,
              new TimelineDTO(ITimelineDTO.ChangeCategory.DeletedEntity, entity), catalog.getLocaleID());
  }

  /**
   * Post an event in case of change of default image and register other changes on the entity
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public void registerAndPostUpdatedEntity() throws RDBMSException, SQLException, CSFormatException {
    // there are two posobilities where base entity is changed 1. defaultImageIID changed 2. entityExtension changed
    if (entity.isChanged()) {
      // just put entry in object tracking when we changed defaultImageIID
      if (entity.getDefaultImageIID() > 0) {
        CSEObject defaultImage = new CSEObject(CSEObjectType.Entity);
        defaultImage.setIID(entity.getDefaultImageIID());
        changedData.register( ChangeCategory.NewDefaultImageIID, defaultImage);
      } else {
        changedData.register( ChangeCategory.UpdatedEntity, entity);
      }
      eventDAS.postEntityEvent(entity, EventType.OBJECT_UPDATE, changedData, catalog.getLocaleID());
      changedData.clear();
    }
  }

  /**
   * Register a change on record for future event notification
   * @param changeCategory
   * @param record
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public void registerChange(ITimelineDTO.ChangeCategory changeCategory, IPropertyRecordDTO record) throws CSFormatException {
    changedData.register( changeCategory, record);
  }

  /**
   * Register all changes related to an updated record
   * @param recordService the data access service to the changed record
   */
  void registerUpdatedRecord(IRecordDAS recordService) throws SQLException, RDBMSException, CSFormatException {
    IPropertyRecordDTO record = recordService.getRecord();
    registerChange(ChangeCategory.UpdatedRecord, record);
    // Identify sources of calculation and coupling for notification
//    if (recordService.isSourceOfCalculation()) {
//      registerChange(ChangeCategory.CalculationSource, record);
//    }
    if (recordService.isSourceOfCoupling()) {
      changedData.register(ITimelineDTO.ChangeCategory.CouplingSource, record);
    }
    if (record.getProperty().getSuperType() == IPropertyDTO.SuperType.RELATION_SIDE) {
      relationTimeLine((RelationsSetDAS) recordService, (RelationsSetDTO) record);
    }
  }

  public void relationTimeLine(RelationsSetDAS relationService, RelationsSetDTO relationSet) throws CSFormatException
  {
    Collection<IRelationsSetDTO> addedRelationsSet = relationService.getAddedRelationsSet();
    if (!addedRelationsSet.isEmpty()) {
      changedData.register(ChangeCategory.AddedRelation, addedRelationsSet);
      changedData.register(ChangeCategory.AddedRelation, IPXON.PXONMeta.Content, relationSet.joinSideBaseEntityIIDs());
    }
    Collection<IRelationsSetDTO> removedRelationsSet = relationService.getRemovedRelationsSet();
    if (!removedRelationsSet.isEmpty()) {
      changedData.register(ChangeCategory.RemovedRelation, removedRelationsSet);
      changedData.register(ChangeCategory.RemovedRelation, IPXON.PXONMeta.Content, relationSet.joinSideBaseEntityIIDs());
    }
  }

  /**
   * Register all changes related to a created record
   * @param recordService the data access service to the changed record
   */
  public void registerCreatedRecord(IRecordDAS recordService) throws SQLException, RDBMSException, CSFormatException {
    IPropertyRecordDTO record = recordService.getRecord();
    registerChange(ChangeCategory.CreatedRecord, record);
    // Identify sources of calculation and coupling for notification
//    if (recordService.isSourceOfCalculation()) {
//      changedData.register(ChangeCategory.CalculationSource, record);
//    }
    if (recordService.isSourceOfCoupling()) {
      changedData.register(ChangeCategory.CouplingSource, record);
    }
    if (record.getProperty().getSuperType() == IPropertyDTO.SuperType.RELATION_SIDE) {
      RelationsSetDTO relationSet = (RelationsSetDTO) record;
      // identify relation sides in the first loop
      changedData.register(ChangeCategory.CreatedRelation, record);
      changedData.register(ChangeCategory.CreatedRelation, IPXON.PXONMeta.Content, relationSet.joinSideBaseEntityIIDs());
    }
  }

  public void registerCreatedCouplingRecord(IRecordDAS recordService) throws CSFormatException
  {
    IPropertyRecordDTO record = recordService.getRecord();
    changedData.register(ChangeCategory.CouplingSource, record);
  }

  public void registerDeletedCouplingRecord(IRecordDAS recordService) throws CSFormatException
  {
    IPropertyRecordDTO record = recordService.getRecord();
    changedData.register(ChangeCategory.RemovedCouplingSource, record);
  }
  /**
   * Register all changes related to a deleted record
   * @param recordService the data access service to the changed record
   */
  void registerDeletedRecord(IRecordDAS recordService) throws SQLException, RDBMSException, CSFormatException
  {
    IPropertyRecordDTO record = recordService.getRecord();
    changedData.register(ChangeCategory.DeletedRecord, record);
//    if (recordService.isSourceOfCalculation()) {
//      changedData.register(ChangeCategory.CalculationSource, record);
//    }
    if (recordService.isSourceOfCoupling()) {
      ICouplingSourceDTO source = new CouplingSourceDTO();
      source.setCouplingTargets(recordService.getCouplingTargetNodeIDs(false));
      source.setId(record.getProperty().getCode());
      changedData.register(ChangeCategory.RemovedCouplingSource, source);
    }
    /*if (record instanceof RelationsSetDTO) {
      changedData.register(ChangeCategory.RemovedRelation, record);
    }*/
  }

  /**
   * Post in bulk all changes previously registered through method
   * registerUpdatedRecord/
   *
   * @return the created track IID
   * @throws RDBMSException
   * @throws SQLException
   * @throws CSFormatException
   */
 public long postRegisteredChanges() throws RDBMSException, SQLException, CSFormatException
  {
    long trackIID = 0;
    if (changedData.contains(ChangeCategory.UpdatedRecord))
      trackIID = eventDAS.postRecordsUpdate(entity, catalog, changedData);
    else if (changedData.contains(ChangeCategory.CreatedRecord))
      trackIID = eventDAS.postRecordsCreation(entity, catalog, changedData);
    else if (changedData.contains(ChangeCategory.DeletedRecord))
      trackIID = eventDAS.postRecordsDeletion(entity, catalog, changedData);
    else if (changedData.contains(ChangeCategory.RemovedCouplingSource))
      trackIID = eventDAS.postCouplingDelete(entity, catalog, changedData);
    else if (changedData.contains(ChangeCategory.CouplingSource))
      trackIID = eventDAS.postCouplingUpdate(entity, catalog, changedData);
    RDBMSLogger.instance().warn("Registered record change(s) have not been posted %s", changedData.toJSON());
    
    return trackIID;
  }

  /**
   * Post an event concerning the creation of an entity
   * @return the track IID of the event
   * @throws RDBMSException
   * @throws SQLException
   * @throws CSFormatException
   */
  public long pushUpdateEntityEvent(EventType event) throws RDBMSException, SQLException, CSFormatException {
    return eventDAS.postEntityEvent(entity, event, new TimelineDTO(ChangeCategory.UpdatedEntity, entity), catalog.getLocaleID());
  }
  
 /**
  * Post an event for added language translations to an entity
  * @param translations
  * @throws RDBMSException
  * @throws SQLException
  * @throws CSFormatException
  */
  public void postAddedLanguageTranslations(ILanguageTranslationDTO[] translations) throws RDBMSException, SQLException, CSFormatException {
    eventDAS.postAddedLanguageTranslations(entity, catalog,
        new TimelineDTO( ChangeCategory.CreatedTranslation, translations));
  }
  
  /**
   * Post an event for deleted language translation from an entity
   * @param translation
   * @throws RDBMSException
   * @throws SQLException
   * @throws CSFormatException
   */
  public void postDeletedLanguageTranslations(ILanguageTranslationDTO translation) throws RDBMSException, SQLException, CSFormatException {
    eventDAS.postDeletedLanguageTranslations(entity, catalog,
        new TimelineDTO( ChangeCategory.DeletedTranslation, translation));
  }

  /**
   * Post an event concerning the creation of an entity
   * @return the track IID of the event
   * @throws RDBMSException
   * @throws SQLException
   * @throws CSFormatException
   */
  public long pushUpdateEntityEvent() throws RDBMSException, SQLException, CSFormatException
  {
    return eventDAS.postEntityEvent(entity, EventType.END_OF_TRANSACTION, new TimelineDTO(ChangeCategory.UpdatedEntity, entity),
        catalog.getLocaleID());
  }
  
}


