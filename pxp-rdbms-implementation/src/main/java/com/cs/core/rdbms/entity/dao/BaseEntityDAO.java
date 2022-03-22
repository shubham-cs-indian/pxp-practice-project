package com.cs.core.rdbms.entity.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import com.cs.core.data.Text;
import com.cs.core.eventqueue.dao.EventQueueDAS;
import com.cs.core.eventqueue.idto.IEventDTO.EventType;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.dto.CatalogDTO;
import com.cs.core.rdbms.config.dto.ClassifierDTO;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idao.IConfigurationDAO;
import com.cs.core.rdbms.config.idto.ICatalogDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.config.idto.ITagValueDTO;
import com.cs.core.rdbms.coupling.dto.CouplingDTO;
import com.cs.core.rdbms.coupling.idto.ICouplingDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dto.BaseEntityChildrenDTO;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.dto.BaseEntityIDDTO;
import com.cs.core.rdbms.entity.dto.EntityRelationDTO.EntityRelationDTOBuilder;
import com.cs.core.rdbms.entity.dto.LanguageTranslationDTO;
import com.cs.core.rdbms.entity.dto.PropertyRecordDTO;
import com.cs.core.rdbms.entity.dto.RelationsSetDTO.RelationsSetDTOBuilder;
import com.cs.core.rdbms.entity.dto.TagDTO;
import com.cs.core.rdbms.entity.dto.TagsRecordDTO;
import com.cs.core.rdbms.entity.dto.TagsRecordDTO.TagsRecordDTOBuilder;
import com.cs.core.rdbms.entity.dto.ValueRecordDTO;
import com.cs.core.rdbms.entity.dto.ValueRecordDTO.ValueRecordDTOBuilder;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityChildrenDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.EmbeddedType;
import com.cs.core.rdbms.entity.idto.IContextualDataDTO;
import com.cs.core.rdbms.entity.idto.IEntityRelationDTOBuilder;
import com.cs.core.rdbms.entity.idto.ILanguageTranslationDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTOBuilder;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTOBuilder;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTOBuilder;
import com.cs.core.rdbms.entity.idto.IValueRecordNotificationDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ParameterType;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ResultType;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.services.records.IRecordDAS;
import com.cs.core.rdbms.services.records.RecordDASFactory;
import com.cs.core.rdbms.services.records.RelationsSetDAS;
import com.cs.core.rdbms.services.records.ValueRecordDAS;
import com.cs.core.rdbms.tracking.dto.TimelineDTO;
import com.cs.core.rdbms.tracking.dto.UserSessionDTO;
import com.cs.core.rdbms.tracking.idto.ITimelineDTO.ChangeCategory;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.Keyword;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * Implementation of method to access base entity data
 *
 * @author vallee
 */
public class BaseEntityDAO implements IBaseEntityDAO {
  
  private final UserSessionDTO   userSession;
  private final BaseEntityDTO    entity;
  private final LocaleCatalogDAO parentCatalog;
  private static final String    DELETE_LOCALE_ID       = "delete from pxp.baseentitylocaleidlink where baseentityiid = ? and localeid = ?";
  private static final String    Q_DELETE_ALL_REVISIONS = "Delete from pxp.objectrevision where objectiid in (%s) ";

  public LocaleCatalogDAO getLocaleCatalog()
  {
    return parentCatalog;
  }

  public BaseEntityDAO(LocaleCatalogDAO localeCatalog, UserSessionDTO userSessionDTO,
      IBaseEntityIDDTO entity)
  {
    this.userSession = userSessionDTO;
    if (entity instanceof BaseEntityDTO) {
      this.entity = (BaseEntityDTO) entity;
    } 
    else {
      this.entity = new BaseEntityDTO((BaseEntityIDDTO) entity);
    }
    this.entity.setLocaleCatalog((LocaleCatalogDTO) localeCatalog.getLocaleCatalogDTO());
    this.parentCatalog = localeCatalog;
  }
  
  @Override
  public IBaseEntityDTO getBaseEntityDTO()
  {
    return entity;
  }
  
  @Override
  public IClassifierDTO newClassifierDTO(long classifierIID, String classifierCode,
      IClassifierDTO.ClassifierType classifierType) throws RDBMSException
  {
    return RDBMSAppDriverManager.getDriver()
        .newConfigurationDAO()
        .createClassifier(classifierCode, classifierType);
  }
  
  @Override
  public ITagValueDTO newTagValueDTO(String tagValueCode, long propertyIID) throws RDBMSException
  {
    return RDBMSAppDriverManager.getDriver()
        .newConfigurationDAO()
        .createTagValue(tagValueCode, propertyIID);
  }
  
  @Override
  public ITagDTO newTagDTO(int relevance, String tagValueCode) throws RDBMSException
  {
    return new TagDTO(tagValueCode, relevance);
  }
  
  @Override
  public IValueRecordDTOBuilder newValueRecordDTOBuilder(IPropertyDTO property, String value)
  {
    return new ValueRecordDTOBuilder(entity.getIID(), (PropertyDTO) property, value);
  }
  
  @Override
  public ITagsRecordDTOBuilder newTagsRecordDTOBuilder(IPropertyDTO property)
  {
    return new TagsRecordDTOBuilder(entity.getIID(), property);
  }
  
  @Override
  public IEntityRelationDTOBuilder newEntityRelationDTOBuilder()
  {
    return new EntityRelationDTOBuilder();
  }
  
  
  @Override
  public IContextDTO newContextDTO(String contextCode, IContextDTO.ContextType type)
      throws RDBMSException
  {
    return RDBMSAppDriverManager.getDriver()
        .newConfigurationDAO()
        .createContext(contextCode, type);
  }
  
  @Override
  public IPropertyDTO newPropertyDTO(long propertyIID, String propertyCode,
      IPropertyDTO.PropertyType propertyType) throws RDBMSException
  {
    return parentCatalog.newPropertyDTO(propertyIID, propertyCode, propertyType);
  }
  
  @Override
  public IRelationsSetDTOBuilder newEntityRelationsSetDTOBuilder(IPropertyDTO relationship,
      IPropertyDTO.RelationSide side)
  {
    return new RelationsSetDTOBuilder(entity.getIID(), (PropertyDTO) relationship, side);
  }
  
  /**
   * Execute a transaction on current entity with access to implicit data/event services
   * @param transaction a formalized entity transaction
   * @throws RDBMSException 
   */
  public void executeTransaction( IEntityTransaction transaction) throws RDBMSException {
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
      BaseEntityDAS entityDAS = new BaseEntityDAS( currentConn);
      EntityEventDAS eventDAS = new EntityEventDAS( currentConn, userSession, parentCatalog.getLocaleCatalogDTO(), entity);
      transaction.execute(currentConn, entityDAS, eventDAS);
    });  
  }
  
  /**
   * Execute a transaction on current entity with access to implicit data services without events
   * @param transaction a formalized entity transaction
   * @throws RDBMSException 
   */
  public void executeReadOnlyTransaction( IEntityReadOnlyTransaction transaction) throws RDBMSException {
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
      BaseEntityDAS entityDAS = new BaseEntityDAS( currentConn);
      transaction.execute(currentConn, entityDAS);
    });  
  }
   
  @Override
  public void addClassifiers(IClassifierDTO... classifiers) throws RDBMSException
  {
    // prealable classifier synchronization (taking advantage of the cache)
    Set<Long> classifierIIDs = new HashSet<>();
    for (IClassifierDTO classifier : classifiers) {
      IClassifierDTO existingClass = ConfigurationDAO.instance()
          .createClassifier(classifier.getClassifierCode(), classifier.getClassifierType());
      ((ClassifierDTO) classifier).setClassifierType(existingClass.getClassifierType());
      ((ClassifierDTO) classifier).setIID(existingClass.getIID());
      classifierIIDs.add(existingClass.getIID());
    }
    executeTransaction((currentConn, entityDAS, eventDAS) -> {
      entityDAS.addOtherClassifiers(entity.getIID(), classifierIIDs);
      // synchronize the entity
      entity.getOtherClassifiers().addAll(Arrays.asList(classifiers));
      // post classifiers change
      eventDAS.postAddedClassifiers( classifiers);
    });
   }
  
  @Override
  public void removeClassifiers(IClassifierDTO... classifiers) throws RDBMSException
  {
    executeTransaction((currentConn, entityDAS, eventDAS) -> {
      entityDAS.removeOtherClassifiers(entity.getIID(), ClassifierDTO.getConfigCodes(classifiers));
      // synchronize the entity
      entity.getOtherClassifiers().removeAll(Arrays.asList(classifiers));
      // post classifiers change
      eventDAS.postRemovedClassifiers( classifiers);
    });
  }
  
  @Override
  public List<IClassifierDTO> getClassifiers() throws RDBMSException
  {
    List<IClassifierDTO> classifierDTOs = new ArrayList<>();
    executeReadOnlyTransaction( (currentConn, entityDAS) -> {
          IResultSetParser rs = entityDAS.getOtherClassifiers(entity.getIID());
          while (rs.next()) {
            IClassifierDTO classifierDTO = ConfigurationDAO.instance().getClassifierByIID(rs.getLong("ClassifierIID"));
            classifierDTOs.add(classifierDTO);
          }
    });
    return classifierDTOs;
  }
  
  @Override
  public IBaseEntityDTO loadPropertyRecords(IPropertyDTO... properties)
      throws RDBMSException, CSFormatException
  {
    long startClock = System.currentTimeMillis();
    // Before loading, all properties are cleared => this is mandatory because previously loaded properties may hide new ones
    entity.getPropertyRecords().clear();
    // call for lazy initialization of the requested properties
    for (IPropertyDTO property : properties) {
      if ( property.isNull() ) {
        property = newPropertyDTO(0, property.getPropertyCode(), property.getPropertyType());
      }
    }
    // load tracking properties when required
    for (IPropertyDTO property : properties) {
      if (property.isTrackingProperty())
        entity.addPropertyRecord( entity.getTrackingValueRecord(property));
    }
    // check consistency of catalog locale at minimum in locale inheritance schema
    parentCatalog.checkLocaleInheritance();
    // load records from RecordServices
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      Set<IPropertyRecordDTO> records = RecordDASFactory.loadRecords(currentConn,
              parentCatalog, entity.getBaseEntityIID(), properties);
      entity.getPropertyRecords().addAll(records);
    });
    RDBMSLogger.instance().debug( 
            "NA|INTERNAL-RDBMS|BaseEntityDAO|loadPropertyRecords (%d properties)| %d ms",
            properties.length, System.currentTimeMillis() - startClock);
    return entity;
  }
  
  @Override
  public IBaseEntityDTO updatePropertyRecords(IPropertyRecordDTO... records) throws RDBMSException
  {
    long startClock = System.currentTimeMillis();
    List<IPropertyRecordDTO> propertyRecords = new ArrayList<>();
    // Before updating, all properties are cleared => this is mandatory because previously loaded properties may hide new ones
    entity.setPropertyRecords(records);
    executeTransaction( ( currentConn, entityDAS, eventDAS) -> {
      // Update entity content before updating records
      entityDAS.updateBaseEntity(entity, userSession.getUserIID());
      // Track entity changes for default image and extension
      eventDAS.registerAndPostUpdatedEntity();
      // Update entity records
      for (IPropertyRecordDTO record : records) { 
        if (record.getProperty().isTrackingProperty()) {
          RDBMSLogger.instance()
                  .warn("Discarding improper attempt to update tracking attribute: %s", record.toCSExpressID());
          continue; 
        }
        IRecordDAS recordService = RecordDASFactory.instance() 
                .newService(currentConn, parentCatalog, record, entity.getBaseEntityIID());
        // Manage the targets of this record by transfer or tightly coupling before change
         //if (!hasTargetEntitieForCoupling.isEmpty()) {
          recordService.separateCoupledTargets(record.getProperty().getPropertyIID(), record.getEntityIID());
        //}
        if (record.isCalculated()) {
          continue; // Calculated attributes cannot be updated but only refreshed after all updates in a second loop
        }
        // Coupled records management
        if (record.isCoupled()) {
          recordService.updateCoupledRecord();
        } else {
          recordService.updateRecord();
        }
        // Register all changes regarding this record (coupled/calculated and side effects on relations)
        eventDAS.registerUpdatedRecord(recordService);
        fillChangedCalculatedSourceRecords(recordService, propertyRecords);
      }
      // A second loop is required to update calculated attributes
      for (IPropertyRecordDTO record : records) {
        if (!record.isCalculated()) {
          continue;
        }
        IRecordDAS recordService = RecordDASFactory.instance().newService(currentConn, parentCatalog, record, entity.getBaseEntityIID());
        recordService.updateCalculatedRecord();
         // Register all changes regarding this record (coupled/calculated and side effects on relations)
        eventDAS.registerUpdatedRecord(recordService);
        fillChangedCalculatedSourceRecords(recordService, propertyRecords);
      }
      // Post in bulk all changes related to the records
      eventDAS.postRegisteredChanges();
     });
    try {
      updateCalculationTargets(propertyRecords);
    }
    catch (RDBMSException | CSFormatException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      RDBMSLogger.instance().exception(e);
    }
    RDBMSLogger.instance().debug( "NA|INTERNAL-RDBMS|BaseEntityDAO|updatePropertyRecords (%d records)| %d ms",
            records.length, System.currentTimeMillis() - startClock);
    return entity;
  }
  
  @Override
  public Boolean updateCalculatedPropertyRecords(ILocaleCatalogDAO catalog, IPropertyRecordDTO record, IPropertyRecordDTO recordArchived) throws RDBMSException
  {
    AtomicBoolean isUpdate = new AtomicBoolean();
    executeTransaction( ( currentConn, entityDAS, eventDAS) -> {

        IRecordDAS recordService = RecordDASFactory.instance() 
                .newService(currentConn, parentCatalog, record, entity.getBaseEntityIID());
        
        if (record.isCalculated()) {
          ValueRecordDAS valueDAS= new ValueRecordDAS(currentConn, (LocaleCatalogDAO)catalog, record, record.getEntityIID());
          valueDAS.refreshCaclulation((LocaleCatalogDAO)catalog);
          if(!((IValueRecordDTO) record).getValue().equals(((IValueRecordDTO) recordArchived).getValue())) {
            recordService.updateCalculatedRecord();
            eventDAS.registerUpdatedRecord(recordService);
            isUpdate.set(true);
          }
        }
      eventDAS.postRegisteredChanges();
     });
    return isUpdate.get();
  }
  
  
  @Override
  public Map<Long, Set<Long>> findTargets(Set<IPropertyRecordDTO> records) throws RDBMSException
  {
    Map<Long, Set<Long>> targetMap = new HashMap<>();

    executeTransaction( ( currentConn, entityDAS, eventDAS) -> {

      for (IPropertyRecordDTO record : records) { 
        IRecordDAS recordService = RecordDASFactory.instance() 
                .newService(currentConn, parentCatalog, record, entity.getBaseEntityIID());
        if(recordService.isSourceOfCalculation()) {
          Set<String> signatures = recordService.getTargetNodesOfCalculation(true);
          for (String signature : signatures) {
            Long baseEntityIID = Long.parseLong(signature.split(":")[0]);
            Long propertyIID = Long.parseLong(signature.split(":")[1]);
            if (!targetMap.containsKey(baseEntityIID)) {
              targetMap.put(baseEntityIID, new HashSet<>());
            }
            targetMap.get(baseEntityIID).add(propertyIID);
          }
        }
      }
     });
    return targetMap;
  }
  
  protected void fillChangedCalculatedSourceRecords(IRecordDAS recordService, List<IPropertyRecordDTO> propertyRecords) throws CSFormatException, RDBMSException, SQLException {
    IPropertyRecordDTO record = recordService.getRecord();
    if (record != null && recordService.isSourceOfCalculation()) {
      IBaseEntityDAO baseEntityDao = this; // take this by default
      ICSEElement element = record.toCSExpressID();
      if (element.getSpecification(Keyword.$type).equals(SuperType.TAGS.name())) {
        TagsRecordDTO tagsRecord = new TagsRecordDTO();
        tagsRecord.fromCSExpressID(element);
//       // fetch full information of tags record from base entity and add it into records
        propertyRecords.add(baseEntityDao.getBaseEntityDTO().getPropertyRecord(tagsRecord.getProperty().getPropertyIID()));
      }
      else {
        ValueRecordDTO valueRecord = new ValueRecordDTO();
        valueRecord.fromCSExpressID(element);
//          // fetch full information of value record from base entity and add it into records
        propertyRecords.add(baseEntityDao.getBaseEntityDTO().getPropertyRecord(valueRecord.getProperty().getPropertyIID()));
      }
    }
  }
  
  /**
   * Update calculation targets from a list of changed sources /!\ This
   * procedure is aimed to be run by event handler
   *
   * @param records the sources of change
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public void updateCalculationTargets(List<IPropertyRecordDTO> records)
      throws RDBMSException, CSFormatException
  {
    IConfigurationDAO configDao = RDBMSAppDriverManager.getDriver().newConfigurationDAO();
    // Identify the map of target base entity IIDs and targetproperty IIDs:
    Map<Long, Set<Long>> targetMap = new HashMap<>();
    executeReadOnlyTransaction((currentConn, entityDAS) -> {
      for (IPropertyRecordDTO record : records) {
        IRecordDAS recordService = RecordDASFactory.instance()
                .newService(currentConn, parentCatalog, record, entity.getBaseEntityIID());
        // Identify the targets for each source of calculation:
        Set<String> signatures = recordService.getTargetNodesOfCalculation(true);
        for (String signature : signatures) {
          Long baseEntityIID = Long.parseLong(signature.split(":")[0]);
          Long propertyIID = Long.parseLong(signature.split(":")[1]);
          if (!targetMap.containsKey(baseEntityIID)) {
            targetMap.put(baseEntityIID, new HashSet<>());
          }
          targetMap.get(baseEntityIID).add(propertyIID);
        }
      }
    });
    
    // Once targets are identified, then update them
    Map<Long, IPropertyDTO> propertyMap = new HashMap<>(); // cache target properties
    for (Long baseEntityIID : targetMap.keySet()) {
      IBaseEntityDAO targetDao = this; // take this by default
      // It is expected calculated attributes has no source outside the current catalog 
      if (baseEntityIID != entity.getBaseEntityIID()) { 
        IBaseEntityDTO targetEntity = parentCatalog.getEntityByIID(baseEntityIID);
        targetDao = parentCatalog.openBaseEntity(targetEntity);
      }
      IPropertyDTO[] properties = new IPropertyDTO[targetMap.get(baseEntityIID).size()];
      int i = 0;
      for (Long propertyIID : targetMap.get(baseEntityIID)) {
        if (!propertyMap.containsKey(propertyIID)) {
          propertyMap.put(propertyIID, configDao.getPropertyByIID(propertyIID));
        }
        properties[i++] = propertyMap.get(propertyIID);
      }
      targetDao.loadPropertyRecords(properties); // Load the target records
      Set<IPropertyRecordDTO> targetRecords = new TreeSet<>();
      targetRecords.addAll(targetDao.getBaseEntityDTO().getPropertyRecords());
      // Then update the calculated targets
      targetDao.updatePropertyRecords(targetRecords.toArray(new IPropertyRecordDTO[0]));
    }
  }
  
  /**
   * create the current base entity as top level within current transaction
   *
   * @throws RDBMSException
   */
  private long createBaseEntity(BaseEntityDAS entityDAS, EntityEventDAS eventDAS) throws RDBMSException, SQLException, CSFormatException {
    if (entity.getBaseEntityID().isEmpty()
            || entity.getLocaleCatalog().isNull() || entity.getBaseLocaleID().isEmpty()) {
      throw new RDBMSException(100, "Inconsistency",
              "A new base entity must have a non-empty ID, a defined catalog and a defined base locale");
    }
    // when an entity is created, it is compulsory a top level entity attached to the current locale catalog
    entity.setLocaleCatalog((LocaleCatalogDTO) parentCatalog.getLocaleCatalogDTO());
    entity.setChildLevel(1); 
    entity.setTopParent(0);
    entityDAS.createBaseEntity(entity, userSession.getUserIID());
    return eventDAS.postCreatedEntity();
  }
  
  @Override
  public IBaseEntityDTO createPropertyRecords(IPropertyRecordDTO... records) throws RDBMSException
  {
    long startClock = System.currentTimeMillis();
    List<IPropertyRecordDTO> changedCalculatedSourceRecords = new ArrayList<>();
    // before creation, all properties are cleared => this is mandatory because previously loaded properties may hide new ones
    entity.getPropertyRecords().clear();
    boolean newEntityCreation = (entity.getIID() == 0);
    long[] createTrackIID = { 0L, 0L };
    executeTransaction( (currentConn, entityDAS, eventDAS) -> {
      // Create the base entity when no IID is declared
      if (newEntityCreation) {
        createTrackIID[0] = createBaseEntity(entityDAS, eventDAS);
      } 
      // First, create simple and coupled records :
      for (IPropertyRecordDTO record : records) {
        if (record.getProperty().isTrackingProperty()) {
          RDBMSLogger.instance().warn("Discarding improper attempt to create tracking attribute: " + record.toPXON());
          continue; 
        }
        entity.addPropertyRecord(record); // add all records to the entity during the first loop
        if (record.isCalculated()) {
          continue; // discard calculated attributes from 1st loop
        }
        IRecordDAS recordService = RecordDASFactory.instance()
                .newService(currentConn, parentCatalog, record, entity.getBaseEntityIID());
        fillChangedCalculatedSourceRecords(recordService, changedCalculatedSourceRecords);
        eventDAS.registerCreatedRecord( recordService);
        if (record.isCoupled()) {
          recordService.createCoupledRecord();
        } else {
          recordService.createSimpleRecord();
        }
      }
      // Second, create calculated records only:
      for (IPropertyRecordDTO record : records) {
        if (record.isCalculated()) {
          IRecordDAS recordService = RecordDASFactory.instance()
                  .newService(currentConn, parentCatalog, record, entity.getBaseEntityIID());
          eventDAS.registerCreatedRecord( recordService);
          fillChangedCalculatedSourceRecords(recordService, changedCalculatedSourceRecords);
          recordService.createCalculatedRecord();
        }
      }
      // post into eventqueue for further process + tracking information
      createTrackIID[1] = eventDAS.postRegisteredChanges();
      /*if (newEntityCreation) {
        // Implement a direct first revision creation at this level with records content
        (new RevisionDAS(currentConn)).createObjectRevision(entity.getBaseEntityIID(), entity.getNatureClassifier().getClassifierIID(),
                System.currentTimeMillis(), "",
                createTrackIID[1] > 0 ? createTrackIID[1] : createTrackIID[0], eventDAS.getRegisteredChanges(),
                entity.toPXON(), false);
      }*/
    });
    try {
      updateCalculationTargets(changedCalculatedSourceRecords);
    }
    catch (RDBMSException | CSFormatException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      RDBMSLogger.instance().exception(e);
    }
    RDBMSLogger.instance().debug("NA|INTERNAL-RDBMS|BaseEntityDAS|createPropertyRecords (%d records)| %d ms",
            records.length, System.currentTimeMillis() - startClock);
    return entity;
  }
  
  
  @Override
  public void createCalulatedPropertyRecord(List<IPropertyRecordDTO> records) throws RDBMSException
  {
    executeTransaction( ( currentConn, entityDAS, eventDAS) -> {

      for(IPropertyRecordDTO record : records) {
        IRecordDAS recordService = RecordDASFactory.instance() 
            .newService(currentConn, parentCatalog, record, entity.getBaseEntityIID());
       // eventDAS.registerCreatedRecord( recordService);
        recordService.createCalculatedRecord();
      //  eventDAS.postRegisteredChanges();
      }
     });
  }
  
  
  @Override
  public IBaseEntityDTO getParent() throws RDBMSException
  {
    executeReadOnlyTransaction((currentConn, entityDAS) -> {
      EntityRelationDAS erDAS = new EntityRelationDAS(currentConn);
      IResultSetParser rsParser = currentConn.getResultSetParser(erDAS.queryParent(entity.getIID()));
      if (rsParser.next()) {
        entity.setParent(rsParser);
      }
    });
    if (entity.getParentIID() == 0)
      return new BaseEntityDTO(); // empty
    BaseEntityIDDTO baseParent = (BaseEntityIDDTO) parentCatalog.getEntityByIID(entity.getParentIID());
    return new BaseEntityDTO(baseParent);
  }
  
  @Override
  public void addChildren(EmbeddedType childType, IBaseEntityIDDTO... entities)
      throws RDBMSException
  {
    Set<Long> newChildrenIIDs = new TreeSet<>();
    executeTransaction((currentConn, entityDAS, eventDAS) -> {
      EntityRelationDAS erDAS = new EntityRelationDAS(currentConn);
      for (IBaseEntityIDDTO child : entities) {
        newChildrenIIDs.add(child.getBaseEntityIID());
      }
      erDAS.addChildren(childType.ordinal(), entity.getIID(), newChildrenIIDs);
      // post into eventqueue for further process
      
      eventDAS.postAddedChildren(entities);
      
      entity.setChildren(childType, entities);
      
    });
  }
  
  @Override
  public void removeChildren(EmbeddedType childType, IBaseEntityIDDTO... entities)
      throws RDBMSException
  {
    Set<IBaseEntityChildrenDTO> oldChildren = new TreeSet<>();
    Set<Long> oldChildrenIIDs = new TreeSet<>();
    executeTransaction((currentConn, entityDAS, eventDAS) -> {
      EntityRelationDAS erDAS = new EntityRelationDAS(currentConn);
      for (IBaseEntityIDDTO child : entities) {
        oldChildren.add(new BaseEntityChildrenDTO(child.getBaseEntityID(), child.getBaseEntityIID()));
        oldChildrenIIDs.add(child.getBaseEntityIID());
      }
      erDAS.removeChildren(entity.getIID(), oldChildrenIIDs);
      entity.setChildren(childType);
      eventDAS.postRemovedChildren(entities);
    });
    entity.getChildren().removeAll(oldChildren);
  }
  
  @Override
  public List<IBaseEntityIDDTO> getChildren(EmbeddedType childType) throws RDBMSException
  {
    List<IBaseEntityIDDTO> children = new ArrayList<>();
    entity.getChildren().clear();
    executeReadOnlyTransaction((currentConn, entityDAS) -> {
      EntityRelationDAS erDAS = new EntityRelationDAS(currentConn);
      IResultSetParser rsParser = currentConn.getResultSetParser(
              erDAS.queryChildren(childType.ordinal(), entity.getIID()));
      while (rsParser.next()) {
        long childIID = rsParser.getLong("childBaseEntityIID");
        IBaseEntityDTO child = parentCatalog.getEntityByIID(childIID);
        if (child != null && !child.isNull()) {
          children.add(child);
          entity.getChildren().add(new BaseEntityChildrenDTO(child.getBaseEntityID(), childIID));
        } else {
          throw new RDBMSException(100, "Inconsistent Data", String.format(
                  "child IID %d of entity %d not found!", childIID, entity.getBaseEntityIID()));
        }
      }
    });
    return children;
  }
  
  /**
   * Delete records inside the current transaction
   */
  private void deletePropertyRecords( RDBMSConnection currentConn, BaseEntityDAS entityDAS, EntityEventDAS eventDAS, 
          boolean fullDelete, IPropertyRecordDTO... records) throws RDBMSException, SQLException, CSFormatException
  {
    entity.getPropertyRecords().clear(); // clear the entity from any remaining records first
    if (!fullDelete) // Update entity modifed date and user before deletion
    {
      entityDAS.updateBaseEntity(entity, userSession.getUserIID());
    }
    for (IPropertyRecordDTO record : records) {
      IRecordDAS recordService = RecordDASFactory.instance()
              .newService(currentConn, parentCatalog, record, entity.getBaseEntityIID());
      /*if (recordService.isSourceOfCoupling()) {
        recordService.deleteAsSourceOfCoupling();
      }*/
      recordService.deleteRecord();
      eventDAS.registerDeletedRecord(recordService);
    }
    eventDAS.postRegisteredChanges();
  }
  
  /** Deletes property record inside the current transaction.
   * @param currentConn
   * @param entityDAS
   * @param eventDAS
   * @param fullDelete
   * @param properties
   * @throws SQLException
   * @throws RDBMSException
   * @throws CSFormatException
   */
  private void deletePropertyRecords(RDBMSConnection currentConn, BaseEntityDAS entityDAS, EntityEventDAS eventDAS, boolean fullDelete,
      IValueRecordDTO[] properties) throws SQLException, RDBMSException, CSFormatException
  {
    if (!fullDelete) // Update entity modifed date and user before deletion
    {
      entityDAS.updateBaseEntity(entity, userSession.getUserIID());
    }
    for (IValueRecordDTO property : properties) {
      ValueRecordDAS recordService = (ValueRecordDAS) RecordDASFactory.instance()
          .newService(currentConn, parentCatalog, property, entity.getBaseEntityIID());
      eventDAS.registerDeletedRecord(recordService);
      recordService.deleteLanguageTranslationRecord();
      eventDAS.postRegisteredChanges();
    }
  }

  @Override
  public void delete(Boolean isDeleteFromDICatalog) throws RDBMSException, SQLException, CSFormatException
  {
    long startTime = System.currentTimeMillis();
    // Delete involves first to delete all records one by one in order to respect rules of coupling and calculation
    Collection<IPropertyDTO> properties = parentCatalog.getAllEntityProperties(entity.getBaseEntityIID());
    parentCatalog.applyLocaleInheritanceSchema(entity.getLocaleIds());
    loadAllPropertyRecords(properties.toArray(new IPropertyDTO[0]));
    getAllImmediateChildren(entity.getBaseEntityIID());
    
    deleteEntity(isDeleteFromDICatalog);
    RDBMSLogger.instance().debug("NA|INTERNAL-RDBMS|BaseEntityDAO|delete (%d records) %d ms", 
            properties.size(), System.currentTimeMillis() - startTime);
  }

  @Override
  public void deleteVariants(Boolean isDeleteFromDICatalog) throws RDBMSException, CSFormatException, SQLException
  {
    long startTime = System.currentTimeMillis();
    
    deleteEntity(isDeleteFromDICatalog);
    
    RDBMSLogger.instance()
        .debug("NA|INTERNAL-RDBMS|BaseEntityDAO|delete variants %d ms",
            System.currentTimeMillis() - startTime);
  }
  
  private void deleteEntity(Boolean isDeleteFromDICatalog) throws RDBMSException
  {
    executeTransaction((currentConn, entityDAS, eventDAS) -> {

      Boolean shouldMaintainVersions = false;
      Boolean isMerged = entity.isMerged();
      if(!isDeleteFromDICatalog && !isMerged) {
        try {
          Integer shouldMaintainArchive = CSProperties.instance().getInt("shouldMaintainArchive");
          if(shouldMaintainArchive != 0) {
            String assetObjectKey = entity.getEntityExtension().getInitField("assetObjectKey", "");
            moveToArchival(entity.getBaseEntityIID(), entity.toPXON(), assetObjectKey);
            shouldMaintainVersions = true;
          }
          else {
            moveAssetToPurge();
          }
        }
        catch (CSInitializationException e) {
          e.printStackTrace();
        }
      }
      else {
        moveAssetToPurge();
      }
      
      // Delete first records
      deletePropertyRecords(currentConn, entityDAS, eventDAS, true,
              entity.getPropertyRecords().toArray(new IPropertyRecordDTO[0]));
      
      if (isDeleteFromDICatalog || !shouldMaintainVersions || isMerged) {
        // deleting the object revisions of baseentity
        String deleteRevisionsQuery = String.format(Q_DELETE_ALL_REVISIONS, entity.getBaseEntityIID());
        PreparedStatement statement = currentConn.prepareStatement(deleteRevisionsQuery);
        statement.execute();
      }
      
      currentConn.getProcedure("sp_deleteBaseEntityByIID")
              .setInput(ParameterType.IID, entity.getIID())
              .execute();
      // post into eventqueue for deletion
      eventDAS.postDeletedEntity();
    });
  }
  
  @Override
  public void moveAssetToPurge() throws RDBMSException
  {
    long baseEntityIID = entity.getBaseEntityIID();
    IJSONContent entityExtension = entity.getEntityExtension();
    String container = entityExtension.getInitField("type", "");
    String assetObjectKey = entityExtension.getInitField("assetObjectKey", "");
    String thumbKey = entityExtension.getInitField("thumbKey", "");
    String previewImageKey = entityExtension.getInitField("previewImageKey", "");
    
    if (!container.isEmpty() && !assetObjectKey.isEmpty()) {
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
        BaseEntityDAS entityDAS = new BaseEntityDAS(currentConn);
        entityDAS.moveAssetToPurge(baseEntityIID, container, assetObjectKey, thumbKey, previewImageKey);
      });
    }
  }
  
  @Override
  public void prepareAssetPurgeDTOListForVariants(Long iid, String contextIIDsString) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      BaseEntityDAS entityDAS = new BaseEntityDAS(currentConn);
      IResultSetParser resultSet = entityDAS.prepareAssetPurgeDTOListForVariants(iid, contextIIDsString);
      while(resultSet.next()) {
        entityDAS.moveAssetToPurge(resultSet.getLong("baseentityiid"), resultSet.getString("container"), resultSet.getString("assetObjectKey"), resultSet.getString("thumbKey"), resultSet.getString("previewImageKey"));
       }
    });
  }

  @Override
  public void deleteLanguageTranslation(long baseEntityIID, String localeId) throws RDBMSException
  {
    long startTime = System.currentTimeMillis();

    Set<IValueRecordDTO> properties = parentCatalog.getAllEntityPropertiesByLocaleId(baseEntityIID, localeId);
    executeTransaction((currentConn, entityDAS, eventDAS)->{
      deletePropertyRecords(currentConn, entityDAS, eventDAS, false, properties.toArray(new IValueRecordDTO[0]));
      PreparedStatement query = currentConn.prepareStatement(DELETE_LOCALE_ID);
      query.setLong(1, baseEntityIID);
      query.setString(2, localeId);
      query.execute();
      
      List<String> localeIds = new ArrayList<>(entity.getLocaleIds());
      localeIds.remove(localeId);
      entity.setLocaleIds(localeIds);
      eventDAS.postDeletedLanguageTranslations(new LanguageTranslationDTO(localeId, baseEntityIID));
    });
    RDBMSLogger.instance().debug("NA|INTERNAL-RDBMS|BaseEntityDAO|deleteLanguageTranslation %d ms",
            System.currentTimeMillis() - startTime);
  }
  
  @Override
  public void deletePropertyRecords(IPropertyRecordDTO... records) throws RDBMSException
  {
    long startTime = System.currentTimeMillis();
    // Delete records only
    executeTransaction((currentConn, entityDAS, eventDAS) -> {
      deletePropertyRecords( currentConn, entityDAS, eventDAS, false, records);
    });
     RDBMSLogger.instance().debug("NA|INTERNAL-RDBMS|BaseEntityDAO|deletePropertyRecords (%d records) %d ms", 
            records.length, System.currentTimeMillis() - startTime);
 }
  
  @Override
  public Set<IBaseEntityDTO> getContextualLinkedEntities() throws RDBMSException
  {
    Set<IBaseEntityDTO> baseEntitesDTOs = new TreeSet<>();
    executeReadOnlyTransaction((currentConn, entityDAS) -> {
      IResultSetParser parser = entityDAS
              .getContextualLinkedEntities(entity.getContextualObject().getContextualObjectIID());
      while (parser.next()) {
        ClassifierDTO natureClassifier = (ClassifierDTO) ConfigurationDAO.instance()
                .getClassifierByIID(parser.getLong("classifierIID"));
        ICatalogDTO catalog = new CatalogDTO(parser.getString("catalogcode"), parser.getString("organizationCode"));
        
        BaseEntityIDDTO baseEntityIDDTO = new BaseEntityIDDTO(parser.getString("baseEntityID"), BaseType.valueOf(parser.getInt("basetype")),
            parser.getString("baseLocaleID"), catalog, natureClassifier);
        baseEntityIDDTO.setIID(parser.getLong("baseEntityIID"));
        IBaseEntityDTO baseEntityDTO = new BaseEntityDTO(baseEntityIDDTO, parser.getString("baseEntityName"));
        baseEntitesDTOs.add(baseEntityDTO);
      }
    });
    return baseEntitesDTOs;
  }
  
  @Override
  public void removeValueRecords(IValueRecordDTO... records) throws RDBMSException
  {
    executeTransaction((currentConn, entityDAS, eventDAS) -> {
      // Update entity modified date and user before removing
      entityDAS.updateBaseEntity(entity, userSession.getUserIID());
      entityDAS.removeValueRecords(records);
      // TODO: source of coupling/calculation is not managed there, one must review the way of calculation dependencies across 
      // contexts and locales
      EventQueueDAS.postEvent(currentConn, EventType.OBJECT_UPDATE, entity.getIID(), entity.getNatureClassifier().getClassifierIID(),
              userSession.getUserIID(), new TimelineDTO(ChangeCategory.DeletedRecord, records),
              entity.toPXON().getBytes(), parentCatalog.getLocaleCatalogDTO().getLocaleID(), userSession.getTransactionID());
    });
  }
  
  @Override
  public IBaseEntityDTO loadCouplingNotifications(IPropertyDTO... properties) throws RDBMSException
  {
    
    List<Long> propertyIIDs = new ArrayList<>();
    for (IPropertyDTO property : properties) {
      propertyIIDs.add(property.getPropertyIID());
    }

    if (propertyIIDs.isEmpty())
      return entity;
    Set<Long> notifiedPropertyIIDs = new HashSet<>();
    
    executeReadOnlyTransaction((currentConn, entityDAS) -> {
      
      
      IResultSetParser resultSet = currentConn.getFunction(ResultType.IID_ARRAY, "pxp.fn_loadNotificationStatus")
              .setInput(ParameterType.IID, this.entity.getIID())
              .setInput(ParameterType.IID_ARRAY, propertyIIDs)
              .execute();
      notifiedPropertyIIDs.addAll(Arrays.asList(resultSet.getIIDArray()));
    });
    
    for (IPropertyRecordDTO propertyRecord : this.entity.getPropertyRecords()) {
      long propertyIID = propertyRecord.getProperty().getIID();
      if (notifiedPropertyIIDs.contains(propertyIID)) {
        ((PropertyRecordDTO) propertyRecord).setNotifiedByCoupling(true);
      }
    }
    return entity;
  }
  
  @Override
  public void resolveTightlyCoupledRecords(IPropertyRecordDTO... records) throws RDBMSException
  {
    executeTransaction((currentConn, entityDAS, eventDAS) -> {
      for (IPropertyRecordDTO record : records) {
        IRecordDAS recordService = RecordDASFactory.instance()
                .newService(currentConn, parentCatalog, record, entity.getBaseEntityIID());
        recordService.resolveNotification();
        eventDAS.registerUpdatedRecord(recordService);
      }
      eventDAS.postRegisteredChanges();
    });
  }
  
  @Override
  public void registerForCoupledEvent(IPropertyRecordDTO... records) throws RDBMSException
  {
    executeTransaction((currentConn, entityDAS, eventDAS) -> {
      for (IPropertyRecordDTO record : records) {
        IRecordDAS recordService = RecordDASFactory.instance()
                .newService(currentConn, parentCatalog, record, entity.getBaseEntityIID());
        eventDAS.registerUpdatedRecord(recordService);
      }
      eventDAS.postRegisteredChanges();
    });
  }

  @Override
  public Map<Long, IValueRecordNotificationDTO> getLanguageNotifiedProperties(Set<Long> PropertyIIDs) throws RDBMSException
  {
    Map<Long, IValueRecordNotificationDTO> notificationProperties = new HashMap<>();
    if ( parentCatalog.getLocaleInheritanceSchema().size() <= 1 || PropertyIIDs.isEmpty() ) {
      return notificationProperties;
    }
    executeReadOnlyTransaction((currentConn, entityDAS) -> {
      entityDAS.getLanguageNotifiedProperties(
              entity.getBaseEntityIID(), parentCatalog.getLocaleInheritanceSchema(), 
              PropertyIIDs, notificationProperties, parentCatalog.getLocaleCatalogDTO().getLocaleID());
    });
    return notificationProperties;
  }

  private void generateGenericDuplicateQuery(StringBuilder baseDuplicateQuery, IContextualDataDTO contextualDataDTO)
  {
    Set<ITagDTO> contextTagValues = contextualDataDTO.getContextTagValues();
    // check if time enabled, as these fields are not empty when time enabled.
    Boolean isTimeEnabled = contextualDataDTO.getContextStartTime() != 0 && contextualDataDTO.getContextEndTime() != 0;

    if (isTimeEnabled) {
      //the third parameter of function used to generate tstzrange in this query is used to denote
      // mutual inclusion or exclusion.
      baseDuplicateQuery.append(String.format(" and cxttimerange && int8range(%s, %s, '[]')",
          contextualDataDTO.getContextStartTime(), contextualDataDTO.getContextEndTime()));
    }

    if (contextTagValues.isEmpty()) {
      //context tags are empty for empty tags
      baseDuplicateQuery.append("and (co.cxttags is null or co.cxttags = '') ");
    }
    else {
      Function<ITagDTO, String> function = (ITagDTO tagValue) -> tagValue.getTagValueCode();
      String tagQuery = String.format(" and array_length(avals(cxttags), 1) = %d ", contextTagValues.size());

      Iterator<ITagDTO> tagValuesIterator = contextTagValues.iterator();
      String tagValuesQuery = "";
      while (tagValuesIterator.hasNext()) {
        ITagDTO tagValue = tagValuesIterator.next();
        String tagValueMatchQuery = String.format(" %s=>%s ", tagValue.getTagValueCode(), tagValue.getRange());

        if (tagValuesIterator.hasNext()) {
          tagValuesQuery = tagValuesQuery + tagValueMatchQuery + " , ";
        }
        else {
          tagValuesQuery = tagValuesQuery + tagValueMatchQuery;
        }
      }
      String format = String.format(" and cxttags @> '%s' ", tagValuesQuery);
      baseDuplicateQuery.append(tagQuery + format);
    }
  }

  public static final String ATTRIBUTE_CONTEXT_DUPLICATE_BASE_QUERY = "select count(1) "
      + " from pxp.valueRecord vr join pxp.contextualObject co"
      + " on vr.contextualObjectIID = co.contextualObjectIID where vr.entityIID = ? and vr.propertyIID  = ? ";
  @Override
  public Boolean isPropertyRecordDuplicate(IPropertyRecordDTO propertyRecord) throws RDBMSException
  {
    AtomicBoolean isDuplicate = new AtomicBoolean(false);
    switch (propertyRecord.getProperty().getSuperType()) {
      case ATTRIBUTE:
        StringBuilder duplicateQuery = new StringBuilder(ATTRIBUTE_CONTEXT_DUPLICATE_BASE_QUERY);
        IValueRecordDTO valueRecordDTO = (IValueRecordDTO) propertyRecord;
        Boolean islanguagedependent = !StringUtils.isEmpty(valueRecordDTO.getLocaleID());
        if(islanguagedependent) {
          duplicateQuery.append("and vr.localeid = ?");
        }
        else {
          duplicateQuery.append("and vr.localeid IS NULL");
        }
        IContextualDataDTO contextualDataDTO = valueRecordDTO.getContextualObject();
        Set<ITagDTO> contextTagValues = contextualDataDTO.getContextTagValues();
        Boolean isTimeEnabled = contextualDataDTO.getContextStartTime() != 0 && contextualDataDTO.getContextEndTime() != 0;
        if (contextTagValues.isEmpty() && !isTimeEnabled) {
          return isDuplicate.get();
        }
        if (!contextualDataDTO.getAllowDuplicate()) {
          generateGenericDuplicateQuery(duplicateQuery, contextualDataDTO);
          //own value should not be considered for duplication
          long valueIID = valueRecordDTO.getValueIID();
          if (valueIID != 0) {
            duplicateQuery.append(String.format(" and vr.valueiid <> %d", valueIID));
          }

          RDBMSConnectionManager.instance()
              .runTransaction((RDBMSConnection currentConn) -> {
                
                PreparedStatement stmt = currentConn.prepareStatement(duplicateQuery.toString());
                stmt.setLong(1, propertyRecord.getEntityIID());
                stmt.setLong(2, propertyRecord.getProperty().getPropertyIID());
                if(islanguagedependent) {
                  stmt.setString(3, valueRecordDTO.getLocaleID());
                }
                stmt.execute();
                
                IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());
                result.next();
                isDuplicate.set(result.getLong("count") > 0);
              });
        }
        break;
      default:
        throw new UnsupportedOperationException("Not Supported Yet.");
    }
    return isDuplicate.get();
  }

  public static final String EMBEDDED_CONTEXT_DUPLICATE_BASE_QUERY = "select count(1) from pxp.baseEntity be join " +
      "pxp.contextualObject co ON  be.contextualObjectIID = co.contextualObjectIID where" + " be.parentIID = ? " +
      "and co.contextCode = ? ";
  @Override
  public Boolean isEmbeddedEntityDuplicate(long parentIID) throws RDBMSException
  {
    AtomicBoolean isDuplicate = new AtomicBoolean(false);
        StringBuilder duplicateQuery = new StringBuilder(EMBEDDED_CONTEXT_DUPLICATE_BASE_QUERY);
        IContextualDataDTO contextualDataDTO = this.entity.getContextualObject();
        if (!contextualDataDTO.getAllowDuplicate()) {
          generateGenericDuplicateQuery(duplicateQuery, contextualDataDTO);
          //own value should not be considered for duplication
          long baseEntityIID = this.entity.getBaseEntityIID();
          if (baseEntityIID != 0) {
            duplicateQuery.append(String.format(" and be.baseEntityIID <> %d", baseEntityIID));
          }

          RDBMSConnectionManager.instance()
              .runTransaction((RDBMSConnection currentConn) -> {

                PreparedStatement stmt = currentConn.prepareStatement(duplicateQuery.toString());
                stmt.setLong(1, parentIID);
                stmt.setString(2,  entity.getContextualObject().getContextCode());
                stmt.execute();

                IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());
                result.next();
                isDuplicate.set(result.getLong("count") > 0);
              });
        }
    return isDuplicate.get();
  }
  
  public List<Long> getContextualObjectIIdsForVariantsToDelete(long assetInstanceId,
      List<String> autoCreateContextCodes) throws RDBMSException
  {
    List<Long> contextualObjectIids = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
      BaseEntityDAS entityDAS = new BaseEntityDAS( currentConn);
      IResultSetParser result = entityDAS.getContextualObjectIIdsForVariantsToDelete(assetInstanceId, autoCreateContextCodes);
      while(result.next()) {
        contextualObjectIids.add(result.getLong(1));
      }
    });
    return contextualObjectIids;
  }
  
  public List<Long> checkIfDuplicateAssetExist(String hashKey, long idToExclude,
      String physicalCatalogId, String organizationId, String endpointId) throws RDBMSException
  {
    List<Long> duplicateIIds = new ArrayList<>();
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          BaseEntityDAS entityDAS = new BaseEntityDAS(currentConn);
          IResultSetParser result = entityDAS.getDuplicateAssetsIfExist(hashKey, idToExclude,
              physicalCatalogId, organizationId, endpointId);
          while (result.next()) {
            duplicateIIds.add(result.getLong("baseentityiid"));
          }
        });
    return duplicateIIds;
  }
  
  @Override
  public List<IBaseEntityDTO> getDuplicateBaseEntities(String hashKey, long idToExclude,
      String physicalCatalogId, String organizationId) throws RDBMSException
  {
    List<IBaseEntityDTO> duplicateBaseEntities = new ArrayList<>();
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          BaseEntityDAS entityDAS = new BaseEntityDAS(currentConn);
          IResultSetParser result = entityDAS.getDuplicateAssetsIfExist(hashKey, idToExclude,
              physicalCatalogId, organizationId, null);
          while (result.next()) {
            IBaseEntityDTO baseEntityDTO = parentCatalog.getEntityByIID(result.getLong("baseentityiid"));
            duplicateBaseEntities.add(baseEntityDTO);
          }
        });
    return duplicateBaseEntities;
  }
 
  @Override
  public Set<Long> detectAllDuplicateAssets(String organizationId) throws RDBMSException
  {
    Set<Long> duplicateAssetIIDs = new HashSet<Long>();
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          BaseEntityDAS entityDAS = new BaseEntityDAS(currentConn);
          IResultSetParser result = entityDAS.detectAllDuplicateAssets(organizationId);
          while (result.next()) {
            duplicateAssetIIDs.add(result.getLong("baseentityiid"));
          }
        });
    return duplicateAssetIIDs;
  }
  @Override
  public void handleDeletedDuplicateAssets(Set<Long> baseEntityIIDs) throws RDBMSException
  {
    RDBMSConnectionManager.instance()
    .runTransaction((RDBMSConnection currentConn) -> {
      BaseEntityDAS entityDAS = new BaseEntityDAS(currentConn);
      entityDAS.handleDeletedDuplicateAssets(baseEntityIIDs);
    });
  }

  @Override
  public void updateExpiryStatusForAssetByIID(long baseEntityIID, boolean status) throws RDBMSException
  {
    RDBMSConnectionManager.instance()
    .runTransaction((RDBMSConnection currentConn) -> {
      BaseEntityDAS entityDAS = new BaseEntityDAS(currentConn);
      entityDAS.updateExpiryStatusForAssetByIID(baseEntityIID, status);
    });
  }

  @Override
  public void createLanguageTranslation() throws RDBMSException
  {
    executeTransaction((currentConn, entityDAS, eventDAS) -> {
      entityDAS.createLanguageTranslation(entity, userSession.getUserIID(), parentCatalog.getLocaleCatalogDTO().getLocaleID());
      entity.getLocaleIds().add(parentCatalog.getLocaleCatalogDTO().getLocaleID());
      ILanguageTranslationDTO translationDTO = new LanguageTranslationDTO(parentCatalog.getLocaleCatalogDTO().getLocaleID(), entity.getBaseEntityIID());
      eventDAS.postAddedLanguageTranslations(new ILanguageTranslationDTO[] {translationDTO});
    });
  }
  
  @Override
  public void createLanguageTranslation(List<String> localeIds)
      throws RDBMSException
  {
    executeTransaction( (currentConn, entityDAS, eventDAS) -> {
      entityDAS.addLanguageTranslation(entity, localeIds);
      entity.getLocaleIds().addAll(localeIds);
      List<ILanguageTranslationDTO> translationDTOs = new ArrayList<ILanguageTranslationDTO>();
      long iid = entity.getBaseEntityIID();
      localeIds.stream().forEach(s -> translationDTOs.add(new LanguageTranslationDTO(s, iid)));
      eventDAS.postAddedLanguageTranslations(translationDTOs.toArray(new ILanguageTranslationDTO[0]));
    });
  }
  
  @Override
  public List<Long> getIIdsExistInLocale(List<Long> baseEntityIIds, String localeId)
      throws RDBMSException
  {
    List<Long> filteredBaseEntityIIds = new ArrayList<>();
    executeTransaction( (currentConn, entityDAS, eventDAS) -> {
      IResultSetParser result = entityDAS.getIIdsExistInLocale(baseEntityIIds, localeId);
      while(result.next()) {
        filteredBaseEntityIIds.add(result.getLong("baseentityiid"));
      }
    });
    return filteredBaseEntityIIds;
  }

  @Override
  public List<Long> filterIIdsByClassifierIIds(List<Long> baseEntityIIds, List<Long> classifierIIds)
      throws RDBMSException
  {
    List<Long> filteredBaseEntityIIds = new ArrayList<>();
    executeTransaction( (currentConn, entityDAS, eventDAS) -> {
      IResultSetParser result = entityDAS.filterIIdsByClassifierIIds(baseEntityIIds, classifierIIds);
      while(result.next()) {
        filteredBaseEntityIIds.add(result.getLong("baseentityiid"));
      }
    });
    return filteredBaseEntityIIds;
  }

  @Override
  public void markAssetsDuplicateByIIds(Set<Long> baseEntityIIds, boolean isDuplicate) throws RDBMSException
  {
    executeTransaction((currentConn, entityDAS, eventDAS) -> {
      entityDAS.markAssetsDuplicateByIIds(baseEntityIIds, isDuplicate);
    });
  }
  
  @Override
  public Long getNewDefaultImage() throws RDBMSException
  {
    Long[] newDefaultImageIID = {0l};
    executeTransaction((currentConn, entityDAS, eventDAS) -> {
      newDefaultImageIID[0] = entityDAS.getNewDefaultImage(entity.getBaseEntityIID());
    });
    return newDefaultImageIID[0];
  }
  
  @Override
  public List<Long> getIIdsByParentIIdAndClassifierCode(long baseEntityIId, String classifierCode)
      throws RDBMSException
  {
    List<Long> baseEntityIIds = new ArrayList<>();
    executeTransaction((currentConn, entityDAS, eventDAS) -> {
      IResultSetParser result = entityDAS.getIIdsByParentIIdAndClassifierCode(baseEntityIId, classifierCode);
      while (result.next()) {
        baseEntityIIds.add(result.getLong("baseentityiid"));
      }
    });
    return baseEntityIIds;
  }
  
  public static String COUPLING_CONFLICTS = "select * from pxp.conflictingvalues where targetentityiid = ? and "
      + "recordstatus = 6 and propertyiid = ? ";
  
  @Override
  public List<ICouplingDTO> loadCouplingConflicts(Long baseEntity, Long propertyIID) throws RDBMSException
  {
    List<ICouplingDTO> conflictsValues = new ArrayList<>();
    
    executeTransaction( (currentConn, entityDAS, eventDAS) -> {
      PreparedStatement stmt = currentConn.prepareStatement(COUPLING_CONFLICTS);
      stmt.setLong(1, baseEntity);
      stmt.setLong(2, propertyIID);
      
      stmt.execute();
      
      IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());
      while (result.next()) {
        conflictsValues.add(new CouplingDTO(result));
      }
    });
    return conflictsValues;
  }

  public static String ALL_COUPLING_CONFLICTS = "select * from pxp.conflictingValues where targetEntityIID = %s and propertyIID in (%s) and recordStatus = 6 ";
  @Override
  public Map<Long, List<ICouplingDTO>> loadCouplingConflicts(List<Long> propertyIIDs) throws RDBMSException
  {
    Map<Long,List<ICouplingDTO>> conflictsValues = new HashMap<>();
    String finalQuery = String.format(ALL_COUPLING_CONFLICTS, getBaseEntityDTO().getBaseEntityIID(), Text.join(",", propertyIIDs));

    executeTransaction( (currentConn, entityDAS, eventDAS) -> {
      PreparedStatement stmt = currentConn.prepareStatement(finalQuery);
      stmt.execute();
      IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());
      while (result.next()) {
        CouplingDTO couplingDTO = new CouplingDTO(result);
        if(conflictsValues.containsKey(couplingDTO.getPropertyIID())){
          conflictsValues.get(couplingDTO.getPropertyIID()).add(couplingDTO);
        }
        else{
          List<ICouplingDTO> value = new ArrayList<>();
          value.add(couplingDTO);
          conflictsValues.put(couplingDTO.getPropertyIID(), value);
        }
      }
    });
    return conflictsValues;
  }
  
  public void createBaseEntityWithPreDefiniedIID() throws RDBMSException{
    executeTransaction( (currentConn, entityDAS, eventDAS) -> {
      createBaseEntity(entityDAS, eventDAS);
    });
  }

  public static String GET_ALL_IMMEDIATE_CHILDREN = "select baseentityID, baseentityIID from pxp.baseentity where parentIID = ?";
  
  @Override
  public IBaseEntityDTO getAllImmediateChildren(Long parentbaseEntityIID) throws RDBMSException
  {
    Set<IBaseEntityChildrenDTO> children = new HashSet<>();
    
    executeTransaction( (currentConn, entityDAS, eventDAS) -> {
      PreparedStatement stmt = currentConn.prepareStatement(GET_ALL_IMMEDIATE_CHILDREN);
      stmt.setLong(1, parentbaseEntityIID);
      stmt.execute();
      
      IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());
      while (result.next()) {
        children.add(new BaseEntityChildrenDTO(result.getString("baseentityID"), result.getLong("baseentityIID")));
      }
    });
    
    entity.getChildren().addAll(children);
    return entity;
  }


  public static String FETCH_CHILDREN = "select * from pxp.baseEntity where parentIID = ?";
  @Override
  public Set<IBaseEntityDTO> fetchChildren() throws RDBMSException
  {
    Set<IBaseEntityDTO> children = new HashSet<>();

    executeTransaction( (currentConn, entityDAS, eventDAS) -> {
      PreparedStatement stmt = currentConn.prepareStatement(FETCH_CHILDREN);
      stmt.setLong(1, entity.getBaseEntityIID());
      stmt.execute();

      IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());
      while (result.next()) {
        BaseEntityDTO child = new BaseEntityDTO();
        child.mapFromBaseEntityForImport(result);
        children.add(child);
      }
    });
    return children;
  }


  
  @Override
  public void moveToArchival(Long baseEntityIID, String baseEntityPXON, String assetObjectKey) throws RDBMSException
  {
    executeTransaction( (currentConn, entityDAS, eventDAS) -> {
      BaseEntityDAS d = new BaseEntityDAS(currentConn);
      d.moveToArchive(baseEntityIID, baseEntityPXON, assetObjectKey);
  });
  }
  
  @Override
  public IBaseEntityDTO loadAllPropertyRecords(IPropertyDTO... properties)
      throws RDBMSException, SQLException, CSFormatException
  {
    entity.getPropertyRecords().clear();

    for (IPropertyDTO property : properties) {
      if ( property.isNull() ) {
        property = newPropertyDTO(0, property.getPropertyCode(), property.getPropertyType());
      }
    }
    for (IPropertyDTO property : properties) {
      if (property.isTrackingProperty())
        entity.addPropertyRecord( entity.getTrackingValueRecord(property));
    }
    Set<IPropertyRecordDTO> records = new HashSet<>();
    long baseEntityIID = entity.getBaseEntityIID();
    Map<Long, List<IValueRecordDTO>> valueRecords = parentCatalog.getAllValueRecords(baseEntityIID);
    Map<Long, List<ITagsRecordDTO>> tagRecords = parentCatalog.getAllTagsRecords(baseEntityIID);
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      records.addAll(RelationsSetDAS.loadRecords(currentConn, parentCatalog, baseEntityIID, properties));
    });
    if(!valueRecords.isEmpty()){
      records.addAll(valueRecords.get(baseEntityIID));
    }
    if(!tagRecords.isEmpty()) {
      records.addAll(tagRecords.get(baseEntityIID));
    }

    entity.getPropertyRecords().addAll(records);
    return entity;
  }

  public static final String GET_CONFLICTING_PROPERTIES = " select propertyIID from pxp.conflictingValues where targetEntityIID = ? group by propertyIID";
  @Override
  public List<IPropertyRecordDTO> getConflictingProperties() throws RDBMSException
  {
    List<IPropertyRecordDTO> conflictingProperties = new ArrayList<>();
    executeTransaction( (currentConn, entityDAS, eventDAS) -> {
      PreparedStatement stmt = currentConn.prepareStatement(GET_CONFLICTING_PROPERTIES);
      stmt.setLong(1, getBaseEntityDTO().getBaseEntityIID());
      stmt.execute();

      IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());
      while (result.next()) {
        long propertyIID = result.getLong("propertyIID");
        IPropertyDTO propertyByIID = ConfigurationDAO.instance().getPropertyByIID(propertyIID);
        if(propertyByIID.getSuperType().equals(SuperType.ATTRIBUTE)){
          conflictingProperties.add(newValueRecordDTOBuilder(propertyByIID, "").build());
        } else if (propertyByIID.getSuperType().equals(SuperType.TAGS)){
          conflictingProperties.add(newTagsRecordDTOBuilder(propertyByIID).build());
        }
      }
    });
    return conflictingProperties;
  }
  
  @Override
  public void updateBaseEntity(long userIID) throws RDBMSException
  {
    this.entity.setChanged(true);
    executeTransaction((currentConn, entityDAS, eventDAS) -> {
      entityDAS.updateBaseEntity(this.entity, userIID);
        eventDAS.registerAndPostUpdatedEntity();
    });
  }
}


