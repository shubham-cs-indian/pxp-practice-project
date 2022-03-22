package com.cs.core.bgprocess.services.relationshipinheritance;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections4.ListUtils;

import com.cs.bds.runtime.usecase.relationshipInheritance.RelationshipInheritance;
import com.cs.constants.CommonConstants;
import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.dto.RelationshipInheritanceOnConfigChangeDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.idto.IRelationshipInheritanceModifiedRelationshipDTO;
import com.cs.core.bgprocess.idto.IRelationshipInheritanceOnConfigChangeDTO;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.config.strategy.usecase.relationship.IGetRelationshipInfoOnConfigChangeStartegy;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dao.BaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.relationship.dto.RelationCoupleRecordDTO.RelationCoupleRecordDTOBuilder;
import com.cs.core.rdbms.relationship.idao.IRelationCoupleRecordDAO;
import com.cs.core.rdbms.relationship.idto.IRelationCoupleRecordDTO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.entity.relationshipinstance.ConfigChangeRelationshipInheritanceModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IGetNatureRelationshipInfoForRelationshipInheritanceResponseModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IReferencedRelationshipInheritanceModel;
import com.cs.core.runtime.interactor.model.configuration.IIdAndCouplingTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IdAndCouplingTypeModel;
import com.cs.core.runtime.interactor.model.instance.IContentRelationshipInstanceModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class InitiateRelationshipInheritanceOnConfigChange extends AbstractBGProcessJob implements IBGProcessJob {
  
  private static final String               PROCESSED_IIDS                         = "processedIIDs";
  private int                               batchSize;
  protected int                             nbBatches                              = 1;
  int                                       currentBatchNo                         = 0;
  IRelationshipInheritanceOnConfigChangeDTO relationshipInheritanceConfigChangeDTO = new RelationshipInheritanceOnConfigChangeDTO();
  protected Set<Long>                       entityIIDs                             = new HashSet<>();
  protected Map<Long, List<Long>>           side1vsSide2EntityIIDs                 = new HashMap<Long, List<Long>>();
  protected Set<Long>                       passedBaseEntityIIDs                   = new HashSet<>();
  protected long                            natureRelationshipId;
  RelationshipInheritance                   relationshipInheritance;
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    
    relationshipInheritanceConfigChangeDTO.fromJSON(jobData.getEntryData().toString());
    String relationshipCode = relationshipInheritanceConfigChangeDTO.getRelationshipId();
    batchSize = CSProperties.instance().getInt(propName("batchSize"));
    relationshipInheritance = BGProcessApplication.getApplicationContext().getBean(RelationshipInheritance.class);
    try {
      IPropertyDTO property = RDBMSUtils.getPropertyByCode(relationshipCode);
      natureRelationshipId = property.getPropertyIID();
      setEntityIIDs(natureRelationshipId);
      int totalContents = side1vsSide2EntityIIDs.size();
      nbBatches = totalContents / batchSize;
      if (nbBatches == 0 || totalContents % batchSize > 0)
        nbBatches++;
    }
    catch (Exception e) {
      e.printStackTrace();
      RDBMSLogger.instance().exception(e);
    }
    
    RDBMSLogger.instance().debug("End of initialization BGP %s / iid=%d", getService(), getIID());
    
  }
  
  private static final String GET_RELATIONS = "select side1entityiid,side2entityiid from pxp.relation where propertyiid = (?)";
  
  private void setEntityIIDs(long relationshipPropertyIID) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      String relationsQuery = String.format(GET_RELATIONS);
      PreparedStatement stmt = currentConn.prepareStatement(relationsQuery);
      stmt.setLong(1, relationshipPropertyIID);
      stmt.execute();
      IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());
      while (result.next()) {
        entityIIDs.add(result.getLong("side1entityiid"));
        if (side1vsSide2EntityIIDs.get(result.getLong("side1entityiid")) != null) {
          side1vsSide2EntityIIDs.get(result.getLong("side1entityiid")).add(result.getLong("side2entityiid"));
        }
        else {
          List<Long> side2iidList = new ArrayList<Long>();
          side2iidList.add(result.getLong("side2entityiid"));
          side1vsSide2EntityIIDs.put(result.getLong("side1entityiid"), side2iidList);
        }
      }
    });
  }
  
  @Override
  public BGPStatus runBatch() throws Exception
  {
    int currentBatchNo = getCurrentBatchNo() + 1;
    entityIIDs.removeAll(passedBaseEntityIIDs);
    Set<Long> batchEntityIIDs = new HashSet<>();
    Iterator<Long> remEntityIID = entityIIDs.iterator();
    for (int i = 0; i < batchSize; i++) {
      if (!remEntityIID.hasNext())
        break;
      batchEntityIIDs.add(remEntityIID.next());
    }
    
    List<IRelationshipInheritanceModifiedRelationshipDTO> modifiedRelationshipsList = relationshipInheritanceConfigChangeDTO
        .getModifiedRelationships();
    List<String> deletedRelationships = relationshipInheritanceConfigChangeDTO.getDeletedRelationships();
    
    List<String> changedRelationshipIds = new ArrayList<String>();
    modifiedRelationshipsList.stream().forEach(rel -> changedRelationshipIds.add(rel.getId()));
    changedRelationshipIds.addAll(deletedRelationships);
    
    IGetRelationshipInfoOnConfigChangeStartegy getRelationshipInfoOnConfigChangeStartegy = BGProcessApplication.getApplicationContext()
        .getBean(IGetRelationshipInfoOnConfigChangeStartegy.class);
    
    IGetNatureRelationshipInfoForRelationshipInheritanceResponseModel configDetails = getRelationshipInfoOnConfigChangeStartegy
        .execute(new ConfigChangeRelationshipInheritanceModel(changedRelationshipIds));
    
    processRelationshipInheritance(batchEntityIIDs, configDetails);
    
    jobData.getRuntimeData().setLongArrayField(PROCESSED_IIDS, passedBaseEntityIIDs);
    RDBMSLogger.instance().info("Batch %d: processed base entity IIDs %s", getCurrentBatchNo(), batchEntityIIDs.toString());
    jobData.getProgress().setPercentageCompletion(currentBatchNo * 100 / nbBatches);
    
    if (currentBatchNo < nbBatches && !batchEntityIIDs.isEmpty())
      return IBGProcessDTO.BGPStatus.RUNNING;
    return (jobData.getSummary().getNBOfErrors() == 0 ? IBGProcessDTO.BGPStatus.ENDED_SUCCESS : IBGProcessDTO.BGPStatus.ENDED_ERRORS);
  }
  
  private void processRelationshipInheritance(Set<Long> batchEntityIIDs,
      IGetNatureRelationshipInfoForRelationshipInheritanceResponseModel configDetails) throws Exception
  {
    for (Long baseEntityIID : batchEntityIIDs) {
      ILocaleCatalogDAO catalogDao = openUserSession().openLocaleCatalog(userSession, baseEntityIID);
      userSession.setTransactionId(UUID.randomUUID().toString());
      manageDeletedRelationships(baseEntityIID, catalogDao);
      manageModifiedRelationships(baseEntityIID, configDetails, catalogDao);
    }
  }
  
  private void manageModifiedRelationships(Long baseEntityIID,
      IGetNatureRelationshipInfoForRelationshipInheritanceResponseModel configDetails, ILocaleCatalogDAO catalogDao) throws Exception
  {
    List<Long> side2EntityIIDs = side1vsSide2EntityIIDs.get(baseEntityIID);
    List<IContentRelationshipInstanceModel> modifiedRelationshipForInheritance = new ArrayList<>();
    List<IRelationshipInheritanceModifiedRelationshipDTO> modifiedRelationships = relationshipInheritanceConfigChangeDTO
        .getModifiedRelationships();
    Map<String, IReferencedRelationshipInheritanceModel> referencedRelationships = configDetails.getReferencedRelationship();
    IRelationCoupleRecordDAO relationCoupleRecordDao = catalogDao.openRelationCoupleRecordDAO();
    
    for (Long entityIID : side2EntityIIDs) {
      IBaseEntityDTO entityDTO = catalogDao.getEntityByIID(entityIID);
      for (IRelationshipInheritanceModifiedRelationshipDTO modifiedRelationship : modifiedRelationships) {
        String modifiedRelationshipId = modifiedRelationship.getId();
        String couplingType = modifiedRelationship.getCouplingType();
        cleanExistingRelation(baseEntityIID, relationCoupleRecordDao, entityIID, modifiedRelationshipId);
        
        IReferencedRelationshipInheritanceModel referencedRelationship = referencedRelationships.get(modifiedRelationshipId);
        if (checkIfPropagableRelationshipIsEligibleInBothSideInstances(entityIID, baseEntityIID, referencedRelationship)) {
          List<String> side1ClassifierNTaxonomyCodes = fetchClassifiersOfContent(baseEntityIID);
          List<String> side2ClassifierNTaxonomyCodes = fetchClassifiersOfContent(entityIID);
          List<String> propagableRelationshipSideIds = relationshipInheritance
              .fillPropagableRelationshipSideIds(side1ClassifierNTaxonomyCodes, side2ClassifierNTaxonomyCodes, referencedRelationship);
          
          IIdAndCouplingTypeModel coupling = new IdAndCouplingTypeModel();
          coupling.setCouplingType(couplingType);
          if (couplingType.equals(CommonConstants.TIGHTLY_COUPLED)) {
            relationshipInheritance.prepareConflictAndInsertInCoupleTable(couplingType, propagableRelationshipSideIds,
                referencedRelationship.getPropertyIID(), natureRelationshipId, baseEntityIID, entityIID, catalogDao);
          }
          else if (couplingType.equals(CommonConstants.DYNAMIC_COUPLED)) {
            relationshipInheritance.prepareConflictAndAdaptRelationInCoupleTable(coupling, propagableRelationshipSideIds, referencedRelationship,
                natureRelationshipId, baseEntityIID, entityIID, catalogDao, modifiedRelationshipForInheritance);
          }
        }
      }
      relationshipInheritance.adaptRelationOnRelationshipInheritance(entityDTO, catalogDao, entityDTO.getBaseType(), modifiedRelationshipForInheritance);
    }
  }

  private void cleanExistingRelation(Long baseEntityIID, IRelationCoupleRecordDAO relationCoupleRecordDao, Long entityIID,
      String modifiedRelationshipId) throws Exception, RDBMSException
  {
    long propogableRelationshipId = RDBMSUtils.getPropertyByCode(modifiedRelationshipId).getPropertyIID();
    IRelationCoupleRecordDTO exsitingRelationCoupleRecord = new RelationCoupleRecordDTOBuilder().targetEntityId(entityIID).
        sourceEntityId(baseEntityIID).propagableRelationshipId(propogableRelationshipId).build();
    StringBuilder filterQuery = relationCoupleRecordDao.getFilterQuery(exsitingRelationCoupleRecord);
    List<IRelationCoupleRecordDTO> relationCoupleRecordDtos = relationCoupleRecordDao.fetchRelationCoupleRecord(filterQuery);
    if (!relationCoupleRecordDtos.isEmpty()) {
      for(IRelationCoupleRecordDTO relationCoupleRecordDto : relationCoupleRecordDtos) {
        StringBuilder deleteQuery = relationCoupleRecordDao.getFilterQuery(relationCoupleRecordDto);
        relationCoupleRecordDao.deleteRelationCoupleRecord(deleteQuery);
      }
    }
  }
  
  private boolean checkIfPropagableRelationshipIsEligibleInBothSideInstances(Long targetEntity, Long sourceContentId,
      IReferencedRelationshipInheritanceModel referencedRelationship) throws RDBMSException
  {
    List<String> side1ClassifierNTaxonomyCodes = fetchClassifiersOfContent(sourceContentId);
    List<String> side2ClassifierNTaxonomyCodes = fetchClassifiersOfContent(targetEntity);
    
    List<String> propagableRelationshipSide1TypesTaxonomies = new ArrayList<>();
    List<String> propagableRelationshipSide2TypesTaxonomies = new ArrayList<>();
    
    List<String> propagableRelationshipTypesTaxonomies = new ArrayList<>();
    propagableRelationshipSide1TypesTaxonomies.addAll(referencedRelationship.getSide1KlassIds());
    propagableRelationshipSide1TypesTaxonomies.addAll(referencedRelationship.getSide1TaxonomyIds());
    propagableRelationshipSide2TypesTaxonomies.addAll(referencedRelationship.getSide2KlassIds());
    propagableRelationshipSide2TypesTaxonomies.addAll(referencedRelationship.getSide2TaxonomyIds());
    propagableRelationshipTypesTaxonomies.addAll(propagableRelationshipSide1TypesTaxonomies);
    propagableRelationshipTypesTaxonomies.addAll(propagableRelationshipSide2TypesTaxonomies);
    
    boolean isSide1Applicable = !ListUtils.intersection(propagableRelationshipTypesTaxonomies, side1ClassifierNTaxonomyCodes).isEmpty();
    boolean isSide2Applicable = !ListUtils.intersection(propagableRelationshipTypesTaxonomies, side2ClassifierNTaxonomyCodes).isEmpty();
    
    if (!isSide1Applicable || !isSide2Applicable) {
      return false;
    }
    return true;
  }
  
  private List<String> fetchClassifiersOfContent(Long sourceContentId) throws RDBMSException
  {
    List<String> classifierCodes = new ArrayList<>();
    IClassifierDTO classifierCode = ConfigurationDAO.instance().getClassifierByIID(getEntityByID(sourceContentId));
    classifierCodes.add(classifierCode.getClassifierCode());
    
    ILocaleCatalogDAO catalogDao = openUserSession().openLocaleCatalog(userSession, sourceContentId);
    IBaseEntityDTO entity = catalogDao.getEntityByIID(sourceContentId);
    BaseEntityDAO entityDao = (BaseEntityDAO) catalogDao.openBaseEntity(entity);
    for (IClassifierDTO classifier : entityDao.getClassifiers()) {
      classifierCodes.add(classifier.getClassifierCode());
    }
    return classifierCodes;
  }
  
  private static final String GET_BASE_ENTITY = "select * from pxp.baseentity where baseentityiid = (?) ";
  
  private Long getEntityByID(Long entityid) throws RDBMSException
  {
    List<Long> classifieriids = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      String classifierQuery = String.format(GET_BASE_ENTITY);
      PreparedStatement stmt = currentConn.prepareStatement(classifierQuery);
      stmt.setLong(1, entityid);
      stmt.execute();
      IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());
      while (result.next()) {
        classifieriids.add(result.getLong("classifieriid"));
      }
    });
    return classifieriids.get(0);
  }
  
  private void manageDeletedRelationships(Long baseEntityIID, ILocaleCatalogDAO catalogDao) throws Exception
  {
    List<String> deletedRelationships = relationshipInheritanceConfigChangeDTO.getDeletedRelationships();
    IRelationCoupleRecordDAO relationCoupleRecordDao = catalogDao.openRelationCoupleRecordDAO();
    for (String relationshipId : deletedRelationships) {
      long propogableRelationshipId = RDBMSUtils.getPropertyByCode(relationshipId).getPropertyIID();
    IRelationCoupleRecordDTO exsitingRelationCoupleRecord = new RelationCoupleRecordDTOBuilder().sourceEntityId(baseEntityIID).
        sourceEntityId(baseEntityIID).propagableRelationshipId(propogableRelationshipId).build();
    StringBuilder filterQuery = relationCoupleRecordDao.getFilterQuery(exsitingRelationCoupleRecord);
    relationCoupleRecordDao.deleteRelationCoupleRecord(filterQuery);
    }
  }
}
