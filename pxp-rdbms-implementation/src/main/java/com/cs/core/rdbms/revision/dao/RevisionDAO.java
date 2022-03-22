package com.cs.core.rdbms.revision.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.dto.BaseEntityMerger;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.revision.dto.ObjectRevisionDTO;
import com.cs.core.rdbms.revision.dto.RevisionTimelineBuilder;
import com.cs.core.rdbms.revision.idao.IRevisionDAO;
import com.cs.core.rdbms.revision.idto.IObjectRevisionDTO;
import com.cs.core.rdbms.tracking.dto.ObjectTrackingDTO;
import com.cs.core.rdbms.tracking.dto.UserSessionDTO;
import com.cs.core.rdbms.tracking.idto.IObjectTrackingDTO;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * @author vallee
 */
public class RevisionDAO implements IRevisionDAO {
  
  private final UserSessionDTO userSession;
  
  /**
   * Create a revision interface with attached user session
   *
   * @param userSession
   */
  public RevisionDAO(UserSessionDTO userSession)
  {
    this.userSession = userSession;
  }
  
  @Override
  public List<IObjectRevisionDTO> getObjectRevisions(long baseEntityIID, Boolean isArchived) throws RDBMSException
  {
    List<IObjectRevisionDTO> revisions = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currConnection) -> {
      RevisionDAS revDas = new RevisionDAS(currConnection);
      IResultSetParser rs = revDas.getBaseEntityObjectRevision(baseEntityIID, isArchived);
      while (rs.next())
        revisions.add(new ObjectRevisionDTO(rs));
    });
    return revisions;
  }
  
  @Override
  public void moveObjectRevisionsToArchive(long baseEntityIID, int nbOfRevision, int offset, Boolean isArchived) throws RDBMSException
  {
    List<Integer> revisions = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currConnection) -> {
      RevisionDAS revDas = new RevisionDAS(currConnection);
      IResultSetParser rs = revDas.getLastObjectRevisions(baseEntityIID, nbOfRevision, offset, isArchived);
      while (rs.next()) {
        revisions.add(rs.getInt("revisionno"));
      }
      revDas.moveObjectRevisionsToArchive(baseEntityIID, revisions, !isArchived);
    });
  }
  
  @Override
  public void deleteObjectRevision(long baseEntityIID, int nbOfRevision, int offset, Boolean isArchived) throws RDBMSException
  {
    List<Integer> revisions = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currConnection) -> {
      RevisionDAS revDas = new RevisionDAS(currConnection);
      IResultSetParser rs = revDas.getLastObjectRevisions(baseEntityIID, nbOfRevision, offset, isArchived);
      while (rs.next()) {
        revisions.add(rs.getInt("revisionno"));
      }
      revDas.deleteObjectRevisions(baseEntityIID, revisions);
    });
  }
  
  @Override
  public IObjectRevisionDTO getLastObjectRevision(long baseEntityIID) throws RDBMSException
  {
    ObjectRevisionDTO[] lastRevision = { null };
    
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currConnection) -> {
      RevisionDAS revDas = new RevisionDAS(currConnection);
      lastRevision[0] = revDas.getLastObjectRevision(baseEntityIID);
    });
    return lastRevision[0];
  }
  
  @Override
  public void deleteBaseEntityRevisions(long baseEntityIID, List<Integer> revisionNos, Boolean isDeleteFromArchival)
      throws RDBMSException, CSInitializationException
  {
    List<IObjectRevisionDTO> revisions = new ArrayList<>();
    Integer shouldMaintainArchive = CSProperties.instance().getInt("shouldMaintainArchive");
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currConnection) -> {
      RevisionDAS revDas = new RevisionDAS(currConnection);
      IResultSetParser rs = revDas.getObjectRevisionsAssetsToPurge(baseEntityIID, new HashSet<Integer>(revisionNos));
      while (rs.next()) {
        revisions.add(new ObjectRevisionDTO(rs));
      }
      if (isDeleteFromArchival) {
        moveAssetToPurge(revisions);
        revDas.deleteObjectRevisions(baseEntityIID, revisionNos);
      }
      else {
        revDas.moveObjectRevisionsToArchive(baseEntityIID, revisionNos, !isDeleteFromArchival);
      } 
    });
    if (!isDeleteFromArchival && shouldMaintainArchive >= 0) {
      moveAssetToPurge(revisions);
      deleteObjectRevision(baseEntityIID, Integer.MAX_VALUE, shouldMaintainArchive, !isDeleteFromArchival);
    }
  }
  
  /**
   * collect all tracking data from the last revision trackIID up to the last
   * track provided by the user
   * 
   * @param entity
   * @param lastRevision
   * @throws RDBMSException
   */
  private List<IObjectTrackingDTO> getLastObjectTrackings(IBaseEntityDTO entity, ObjectRevisionDTO[] lastRevision) throws RDBMSException
  {
    List<IObjectTrackingDTO> lastChanges = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currConnection) -> {
      RevisionDAS revDas = new RevisionDAS(currConnection);
      // fetch last revision of base entity
      lastRevision[0] = revDas.getLastObjectRevisionWithoutTimeline(entity.getBaseEntityIID());
      lastChanges.addAll(revDas.getLastObjectTrackings(lastRevision[0].getTrackIID(), entity.getBaseEntityIID(), userSession.getUserIID()));
    });
    return lastChanges;
  }
  
  
  @Override
  public List<IObjectTrackingDTO> getLastObjectTrackings(long entityIID, IObjectRevisionDTO lastRevision) throws RDBMSException
  {
    List<IObjectTrackingDTO> lastChanges = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currConnection) -> {
      RevisionDAS revDas = new RevisionDAS(currConnection);
      lastChanges.addAll(revDas.getLastObjectTrackings(lastRevision.getTrackIID(), entityIID, userSession.getUserIID()));
    });
    return lastChanges;
  }
  
  /**
   * 
   * @param entity
   * @param revisionComment
   * @param lastChanges
   * @param merger
   * @param rtBuilder
   * @throws RDBMSException
   */
  private void createRevision(IBaseEntityDTO entity, String revisionComment, List<IObjectTrackingDTO> lastChanges, BaseEntityMerger merger,
      RevisionTimelineBuilder rtBuilder, Boolean isArchived) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currConnection) -> {
      String assetObjectKey = entity.getEntityExtension().getInitField("assetObjectKey", "");
      RevisionDAS revDas = new RevisionDAS(currConnection);
      revDas.createObjectRevision(entity.getBaseEntityIID(), entity.getNatureClassifier().getClassifierIID(), System.currentTimeMillis(),
          revisionComment, lastChanges.get(lastChanges.size() - 1).getTrackIID(), rtBuilder.toJSON(), merger.getEntity().toPXON(),
          isArchived, assetObjectKey);
    });
  }
  
  @Override
  public int createNewRevision(IBaseEntityDTO entity, String revisionComment, Integer versionsToMaintain)
      throws RDBMSException, CSFormatException, CSInitializationException
  {
    Integer shouldMaintainArchive = CSProperties.instance().getInt("shouldMaintainArchive");
    ObjectRevisionDTO[] lastRevision = { null };
    // fetch last object trackings for base entity
    List<IObjectTrackingDTO> lastChanges = getLastObjectTrackings(entity, lastRevision);
    boolean shouldPurgeAssetsFromSwift = false;
    // nothing added since the last revision then just return the last revision
    // number
    if (lastChanges.isEmpty())
      return lastRevision[0].getRevisionNo();
    
    if (lastRevision[0].getTrackIID() == 0) {
      return createZeroRevision(entity, versionsToMaintain, shouldMaintainArchive, lastChanges);
    }
    
    IBaseEntityDTO baseEntityDTOArchive = lastRevision[0].getBaseEntityDTOArchive();
    
    // Merge the DTO from last changes and timeline data into a revision
    // timeline
    BaseEntityMerger entityMerger = new BaseEntityMerger(baseEntityDTOArchive);
    RevisionTimelineBuilder rtBuilder = new RevisionTimelineBuilder();
    for (IObjectTrackingDTO change : lastChanges) {
      BaseEntityDTO changedEntity = new BaseEntityDTO();
      changedEntity.fromPXON(change.getJSONExtract().toString());
      entityMerger.synchronizeChange(change.getJSONTimelineData(), changedEntity);
      rtBuilder.addTrackingTimeline(change);
    }
    
    if (versionsToMaintain.equals(0)) {
      if (shouldMaintainArchive.equals(0)) {
        shouldPurgeAssetsFromSwift = true;
        deleteObjectRevision(entity.getBaseEntityIID(), Integer.MAX_VALUE, versionsToMaintain, false);
      }
      else {
        createRevision(entity, revisionComment, lastChanges, entityMerger, rtBuilder, true);
        if (shouldMaintainArchive > 0) {
          shouldPurgeAssetsFromSwift = true;
          deleteObjectRevision(entity.getBaseEntityIID(), Integer.MAX_VALUE, shouldMaintainArchive, true);
        }
        if (shouldMaintainArchive == -1) {
          deleteObjectRevision(entity.getBaseEntityIID(), Integer.MAX_VALUE, versionsToMaintain, false);
        }
      }
    }
    else {
      createRevision(entity, revisionComment, lastChanges, entityMerger, rtBuilder, false);
      if (shouldMaintainArchive.equals(0)) {
        shouldPurgeAssetsFromSwift = true;
        deleteObjectRevision(entity.getBaseEntityIID(), Integer.MAX_VALUE, versionsToMaintain, false);
      }
      else {
        moveObjectRevisionsToArchive(entity.getBaseEntityIID(), Integer.MAX_VALUE, versionsToMaintain, false);
        if (shouldMaintainArchive > 0) {
          shouldPurgeAssetsFromSwift = true;
          deleteObjectRevision(entity.getBaseEntityIID(), Integer.MAX_VALUE, shouldMaintainArchive, true);
        }
      }
    }
    if (entity.getBaseType().equals(BaseType.ASSET) && shouldPurgeAssetsFromSwift) {
      List<IObjectRevisionDTO> revisions = new ArrayList<>();
      revisions.add(lastRevision[0]);
      moveAssetToPurge(revisions);
    }
    return lastRevision[0].getRevisionNo() + 1;
  }
  
  //TODO: Temporary Method
  public int createNewRevisionAfterRollback(IBaseEntityDTO entity, String revisionComment, Integer versionsToMaintain)
      throws RDBMSException, CSFormatException, CSInitializationException, SQLException
  {
    Integer shouldMaintainArchive = CSProperties.instance().getInt("shouldMaintainArchive");
    ObjectRevisionDTO[] lastRevision = { null };
    // fetch last object trackings for base entity
    List<IObjectTrackingDTO> lastChanges = getLastObjectTrackings(entity, lastRevision);
    boolean shouldPurgeAssetsFromSwift = false;
    // nothing added since the last revision then just return the last revision
    // number
    if (lastChanges.isEmpty())
      return lastRevision[0].getRevisionNo();
    
    if (lastRevision[0].getTrackIID() == 0) {
      return createZeroRevision(entity, versionsToMaintain, shouldMaintainArchive, lastChanges);
    }
    IBaseEntityDTO baseEntityDTOArchive = getRollBackDTO(lastRevision[0], true);
    
    // Merge the DTO from last changes and timeline data into a revision
    // timeline
    BaseEntityMerger entityMerger = new BaseEntityMerger(baseEntityDTOArchive);
    RevisionTimelineBuilder rtBuilder = new RevisionTimelineBuilder();
    for (IObjectTrackingDTO change : lastChanges) {
      BaseEntityDTO changedEntity = new BaseEntityDTO();
      changedEntity.fromPXON(change.getJSONExtract().toString());
      entityMerger.synchronizeChange(change.getJSONTimelineData(), changedEntity);
      rtBuilder.addTrackingTimeline(change);
    }
    
    if (versionsToMaintain.equals(0)) {
      if (shouldMaintainArchive.equals(0)) {
        shouldPurgeAssetsFromSwift = true;
        deleteObjectRevision(entity.getBaseEntityIID(), Integer.MAX_VALUE, versionsToMaintain, false);
      }
      else {
        createRevision(entity, revisionComment, lastChanges, entityMerger, rtBuilder, true);
        if (shouldMaintainArchive > 0) {
          shouldPurgeAssetsFromSwift = true;
          deleteObjectRevision(entity.getBaseEntityIID(), Integer.MAX_VALUE, shouldMaintainArchive, true);
        }
        if (shouldMaintainArchive == -1) {
          deleteObjectRevision(entity.getBaseEntityIID(), Integer.MAX_VALUE, versionsToMaintain, false);
        }
      }
    }
    else {
      createRevision(entity, revisionComment, lastChanges, entityMerger, rtBuilder, false);
      if (shouldMaintainArchive.equals(0)) {
        shouldPurgeAssetsFromSwift = true;
        deleteObjectRevision(entity.getBaseEntityIID(), Integer.MAX_VALUE, versionsToMaintain, false);
      }
      else {
        moveObjectRevisionsToArchive(entity.getBaseEntityIID(), Integer.MAX_VALUE, versionsToMaintain, false);
        if (shouldMaintainArchive > 0) {
          shouldPurgeAssetsFromSwift = true;
          deleteObjectRevision(entity.getBaseEntityIID(), Integer.MAX_VALUE, shouldMaintainArchive, true);
        }
      }
    }
    if (entity.getBaseType().equals(BaseType.ASSET) && shouldPurgeAssetsFromSwift) {
      List<IObjectRevisionDTO> revisions = new ArrayList<>();
      revisions.add(lastRevision[0]);
      moveAssetToPurge(revisions);
    }
    return lastRevision[0].getRevisionNo() + 1;
  }
  
  //TODO: Temporary Method
  private IBaseEntityDTO getRollBackDTO(IObjectRevisionDTO revision, Boolean remove)
      throws CSFormatException, RDBMSException, CSInitializationException, SQLException
  {
    IJSONContent objectArchive = revision.getObjectArchive();
    JSONContentParser jsonContentParser = new JSONContentParser(objectArchive.toString());
    JSONArray propertyRecordJsons = jsonContentParser.getJSONArray("Lrecord");
    Map<String, JSONObject> codeVsJson = new HashMap<>();
    for(Object propertyRecordObject: propertyRecordJsons) {
      JSONObject jsonObject = (JSONObject) propertyRecordObject;
      JSONContentParser jsonContentForPropertyObject = new JSONContentParser(jsonObject.toString());
      CSEObject cseElement = (CSEObject)jsonContentForPropertyObject.getCSEElement("csid");
      codeVsJson.put(cseElement.getCode(), jsonObject);
    }
    List<IPropertyDTO> propertyDTOs = ConfigurationDAO.instance().getPropertyDTOs(codeVsJson.keySet());
    for(IPropertyDTO dto : propertyDTOs) {
      if(codeVsJson.containsKey(dto.getCode())){
        codeVsJson.remove(dto.getCode());
      }
    }
    if(remove) {
      propertyRecordJsons.removeAll(codeVsJson.values());
    }
    return revision.getBaseEntityDTOArchive(jsonContentParser.toString());
  }
  
  
  public int createZeroRevision(IBaseEntityDTO entity, Integer versionsToMaintain, Integer archived, List<IObjectTrackingDTO> lastChanges)
      throws CSFormatException, RDBMSException
  {
    RevisionTimelineBuilder rtBuilder = new RevisionTimelineBuilder();
    for (IObjectTrackingDTO change : lastChanges) {
      rtBuilder.addTrackingTimeline(change);
    }
    AtomicBoolean isArchived = new AtomicBoolean(false);
    
    if (versionsToMaintain != 0) {
      isArchived.set(false);
    }
    else if (archived != 0) {
      isArchived.set(true);
    }
    else {
      return -1;
    }
    
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currConnection) -> {
      String assetObjectKey = entity.getEntityExtension().getInitField("assetObjectKey", "");
      RevisionDAS revDas = new RevisionDAS(currConnection);
      revDas.createObjectRevision(entity.getBaseEntityIID(), entity.getNatureClassifier().getClassifierIID(), System.currentTimeMillis(),
          "", lastChanges.get(lastChanges.size() - 1).getTrackIID(), rtBuilder.toJSON(), entity.toPXON(), isArchived.get(), assetObjectKey);
    });
    return 0;
  }

  
  

  public int bulkCreateZeroRevision(List<IBaseEntityDTO> entities, Map<Long, IObjectTrackingDTO> lastChanges)
      throws CSFormatException, RDBMSException
  {
    for (IBaseEntityDTO entity : entities) {
      IObjectTrackingDTO objectTrackingDTO = lastChanges.get(entity.getBaseEntityIID());
      RevisionTimelineBuilder rtBuilder = new RevisionTimelineBuilder();
      rtBuilder.addTrackingTimeline(objectTrackingDTO);
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currConnection) -> {

        String assetObjectKey = entity.getEntityExtension().getInitField("assetObjectKey", "");
        RevisionDAS revDas = new RevisionDAS(currConnection);
        revDas.bulkCreateZeroRevision(entity.getBaseEntityIID(), entity.getNatureClassifier().getClassifierIID(), System.currentTimeMillis(),
            objectTrackingDTO.getTrackIID(), rtBuilder.toJSON(), entity.toPXON(), assetObjectKey);
      });
    }

    return 0;
  }
  /*  @Override
  public int restoreFromRevision(IBaseEntityDTO entity, int revisionNb)
      throws RDBMSException, CSFormatException, CSInitializationException
  {
    
    IBaseEntityDTO[] restoredBaseEntityDTO = new IBaseEntityDTO[1];
    
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currConnection) -> {
          RevisionDAS revDas = new RevisionDAS(currConnection);
          EventQueueDAS eqDas = new EventQueueDAS( currConnection, userSession);
          IResultSetParser rs = revDas.getObjectRevisions(entity.getBaseEntityIID(), new HashSet<Integer>(0));
          if (rs.next()) {
            IObjectRevisionDTO restoredObject = new ObjectRevisionDTO(rs);
            restoredBaseEntityDTO[0] = (BaseEntityDTO) restoredObject.getBaseEntityDTOArchive();
            IResultSetParser rs1 = revDas.getLastObjectRevisions(entity.getBaseEntityIID(), 1, 0, false);
            rs1.next();
            IObjectRevisionDTO currentObject = new ObjectRevisionDTO(rs1);
            IBaseEntityDTO currentBaseEntityDTO = currentObject.getBaseEntityDTOArchive();
            
            Map<Long, IPropertyRecordDTO> restoredPropertyMap = new HashMap<>();
            for (IPropertyRecordDTO restoredProperty : restoredBaseEntityDTO[0]
                .getPropertyRecords()) {
              // restoredPropertyMap.put(restoredProperty.getPropertyRecordIID(),
              // restoredProperty);
            }
            
            Map<Long, IPropertyRecordDTO> currentPropertyMap = new HashMap<>();
            for (IPropertyRecordDTO currentProperty : currentBaseEntityDTO.getPropertyRecords()) {
              // currentPropertyMap.put(currentProperty.getPropertyRecordIID(),
              // currentProperty);
            }
            
            ILocaleCatalogDTO localeCatalog = currentBaseEntityDTO.getLocaleCatalog();
            LocaleCatalogDAO parentCatalog = new LocaleCatalogDAO(userSession, localeCatalog);
            
            Set<IPropertyRecordDTO> addedPropertyRecords = new HashSet<>();
            Set<IPropertyRecordDTO> modifiedPropertyRecords = new HashSet<>();
            if (restoredBaseEntityDTO[0].getPropertyRecords()
                .size() > 0) {
              for (IPropertyRecordDTO restoredPropertycord : restoredBaseEntityDTO[0]
                  .getPropertyRecords()) {
                // IPropertyRecordDTO currentPropertyRecord =
                // currentPropertyMap.get(restoredPropertycord.getPropertyRecordIID());
                IRecordDAS recordManager = RecordDASFactory.instance()
                    .newService( currConnection, parentCatalog, restoredPropertycord,
                        currentBaseEntityDTO.getBaseEntityIID());
                        if (currentPropertyRecord == null) {
                  recordManager.createSimpleRecord();
                  addedPropertyRecords.add(restoredPropertycord);
                }
                else {
                    recordManager.updateRecord();
                    entity.getPropertyRecords().add(restoredPropertycord);
                    modifiedPropertyRecords.add(restoredPropertycord);
                }
              }
            }
            
            if (addedPropertyRecords.size() > 0) {
              EventQueueDAS.postEvent(EventType.OBJECT_UPDATE, entity.getBaseEntityIID(), userSession.getUserIID(),
                  TimelineDataFactory.newRecordsData(TimelineEvent.createdRecords, addedPropertyRecords.toArray(new IPropertyRecordDTO[0])),
                  restoredBaseEntityDTO[0].toPXON().getBytes());
            }
            ;
            if (modifiedPropertyRecords.size() > 0) {
              eqDas.postEvent(EventType.OBJECT_UPDATE, entity.getBaseEntityIID(), userSession.getUserIID(),
                  TimelineDataFactory.newRecordsData(TimelineEvent.updatedRecords, modifiedPropertyRecords.toArray(new IPropertyRecordDTO[0])),
                  restoredBaseEntityDTO[0].toPXON().getBytes());
            } 
            
            Set<IClassifierDTO> currentOtherClassifiers = currentBaseEntityDTO
                .getOtherClassifiers();
            Set<IClassifierDTO> restoredOtherClassifiers = restoredBaseEntityDTO[0]
                .getOtherClassifiers();
            Map<Long, IClassifierDTO> currentOtherClassifiersMap = new HashMap<>();
            Map<Long, IClassifierDTO> restoredOtherClassifiersMap = new HashMap<>();
            
            Set<String> removedClassifierCodes = new HashSet<>();
            Set<Long> addedClassifierIIDs = new TreeSet<>();
            Set<String> addedClassifierCodes = new TreeSet<>();
            Set<Integer> addedClassifierTypes = new TreeSet<>();
            Set<IClassifierDTO> addedClassifiers = new HashSet<>();
            Set<IClassifierDTO> removedClassifiers = new HashSet<>();
            
            for (IClassifierDTO currentOtherClassifier : currentOtherClassifiers) {
              currentOtherClassifiersMap.put(currentOtherClassifier.getClassifierIID(),
                  currentOtherClassifier);
            }
            for (IClassifierDTO restoredOtherClassifier : restoredOtherClassifiers) {
              restoredOtherClassifiersMap.put(restoredOtherClassifier.getClassifierIID(),
                  restoredOtherClassifier);
            }
            
            for (IClassifierDTO currentOtherClassifier : currentOtherClassifiers) {
              IClassifierDTO restoredOtherClassifier = restoredOtherClassifiersMap
                  .get(currentOtherClassifier.getClassifierIID());
              if (restoredOtherClassifier == null) {
                removedClassifierCodes.add(currentOtherClassifier.getClassifierCode());
                removedClassifiers.add(currentOtherClassifier);
              }
            }
            for (IClassifierDTO restoredOtherClassifier : restoredOtherClassifiers) {
              IClassifierDTO currentOtherClassifier = currentOtherClassifiersMap
                  .get(restoredOtherClassifier.getClassifierIID());
              if (currentOtherClassifier == null) {
                addedClassifiers.add(restoredOtherClassifier);
                
                addedClassifierIIDs.add(restoredOtherClassifier.getIID());
                addedClassifierTypes.add(restoredOtherClassifier.getClassifierType()
                    .ordinal());
              }
            }
            
            BaseEntityDAS baseEntityDAS = new BaseEntityDAS( currConnection);
            
            if (!addedClassifierIIDs.isEmpty()) {
              baseEntityDAS.addOtherClassifiers(restoredBaseEntityDTO[0].getBaseEntityIID(),
                  addedClassifierIIDs);
              // eqDas.postEvent(EventType.CLASSIFIER_ADDED,
              // entity.getBaseEntityIID(),
              //
              // userSession.getUserIID(),TimelineDataFactory.newClassifiersData(TimelineEvent.addedClassifiers,
              // addedClassifiers.toArray(new
              // IClassifierDTO[addedClassifiers.size()])),
              // entity.toPXON().getBytes());
            }
            
            if (!removedClassifierCodes.isEmpty()) {
              
              baseEntityDAS.removeOtherClassifiers(restoredBaseEntityDTO[0].getBaseEntityIID(),
                  removedClassifierCodes);
              // eqDas.postEvent(EventType.CLASSIFIER_REMOVED,
              // entity.getBaseEntityIID(), userSession.getUserIID(),
              //
              // TimelineDataFactory.newClassifiersData(TimelineEvent.removedClassifiers,
              // removedClassifiers.toArray(new IClassifierDTO[0])),
              // entity.toPXON().getBytes());
            }
          }
        });
    
    return createNewRevision(restoredBaseEntityDTO[0],
        "Base entity restored on version  " + revisionNb,1);
  }*/
  
  @Override
  public List<IObjectRevisionDTO> getRevisions(long baseEntityIID, Set<Integer> revisionNos) throws RDBMSException
  {
    
    List<IObjectRevisionDTO> revisions = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currConnection) -> {
      RevisionDAS revDas = new RevisionDAS(currConnection);
      IResultSetParser rs = revDas.getObjectRevisions(baseEntityIID, revisionNos);
      while (rs.next())
        revisions.add(new ObjectRevisionDTO(rs));
    });
    return revisions;
  }
  
  @Override
  public List<IObjectTrackingDTO> getAllObjectTrackingsOfTransaction(long trackIID) throws RDBMSException
  {
    List<IObjectTrackingDTO> objectTrackings = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currConnection) -> {
      RevisionDAS revDas = new RevisionDAS(currConnection);
      IResultSetParser rs = revDas.getObjectTrackingsOfSameTransaction(trackIID);
      while (rs.next()) {
        ObjectTrackingDTO objectTrackingDTO = new ObjectTrackingDTO();
        objectTrackingDTO.set(rs);
        objectTrackings.add(objectTrackingDTO);
      }
    });
    return objectTrackings;
    
  }

  @Override
  public Integer getNumberOfVersionsInTimeline(long baseEntityIID) throws RDBMSException
  {
    AtomicInteger count = new AtomicInteger(0);
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currConnection) -> {
      RevisionDAS revDas = new RevisionDAS(currConnection);
      count.set(revDas.getNumberOfVersionsInTimeline(baseEntityIID));
    });
    return count.get();
  }

  @Override
  public void restoreRevisions(long baseEntityIID, List<Integer> revisionNos) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currConnection) -> {
      RevisionDAS revDas = new RevisionDAS(currConnection);
      revDas.restoreRevisions(baseEntityIID, revisionNos);
    });
  }
  
  /**
   * Insert entry of asset in assettobepurged table for purging.
   * @param revisions
   * @throws RDBMSException
   */
  private void moveAssetToPurge(List<IObjectRevisionDTO> revisions) throws RDBMSException
  {
    for (IObjectRevisionDTO revision : revisions) {
      IBaseEntityDTO baseEntityDTOArchive = null;
      try {
        baseEntityDTOArchive = revision.getBaseEntityDTOArchive();
        if (!baseEntityDTOArchive.getBaseType().equals(BaseType.ASSET)) {
          return;
        }
        IJSONContent entityExtension = baseEntityDTOArchive.getEntityExtension();
        if (entityExtension == null || entityExtension.isEmpty()) {
          return;
        }
        String container = entityExtension.getInitField("type", "");
        String assetObjectKey = entityExtension.getInitField("assetObjectKey", "");
        String thumbKey = entityExtension.getInitField("thumbKey", "");
        String previewImageKey = entityExtension.getInitField("previewImageKey", "");
        
        if (!container.isEmpty() && !assetObjectKey.isEmpty()) {
          RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
            String Q_INSERT_INTO_PURGED_ASSET = "insert into pxp.assetstobepurged (assetObjectKey,thumbKey,previewImageKey,type) values (?,?,?,?)";
            PreparedStatement ps = currentConn.prepareStatement(Q_INSERT_INTO_PURGED_ASSET);
            ps.setString(1, assetObjectKey);
            ps.setString(2, thumbKey.isEmpty() ? null : thumbKey);
            ps.setString(3, previewImageKey.isEmpty() ? null : previewImageKey);
            ps.setString(4, container);
            ps.executeUpdate();
          });
        }
      }
      catch (CSFormatException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public List<IObjectTrackingDTO> getAllObjectTtrackingForBaseEntity(long baseEntityIID) throws RDBMSException
  {
    List<IObjectTrackingDTO> objectTrackings = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currConnection) -> {
      RevisionDAS revDas = new RevisionDAS(currConnection);
      IResultSetParser rs = revDas.getAllTrackings(baseEntityIID);
      while (rs.next()) {
        ObjectTrackingDTO objectTrackingDTO = new ObjectTrackingDTO();
        objectTrackingDTO.set(rs);
        objectTrackings.add(objectTrackingDTO);
      }
    });
    return objectTrackings;
  }
}
