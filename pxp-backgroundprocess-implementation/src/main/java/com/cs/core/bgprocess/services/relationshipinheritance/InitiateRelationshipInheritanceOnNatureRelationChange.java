package com.cs.core.bgprocess.services.relationshipinheritance;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.collections4.ListUtils;

import com.cs.bds.runtime.usecase.relationshipInheritance.RelationshipInheritance;
import com.cs.constants.CommonConstants;
import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.dto.RelationshipInheritanceOnNatureRelationchangeDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.idto.IEntityRelationshipInfoDTO;
import com.cs.core.bgprocess.idto.IRelationshipInheritanceOnNatureRelationchangeDTO;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.config.strategy.usecase.relationship.IGetRelationshipInheritanceInfoOnNatureRelationshipChangeStartegy;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
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
import com.cs.core.runtime.interactor.entity.relationshipinstance.IGetNatureRelationshipInfoForRelationshipInheritanceResponseModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IReferencedNatureRelationshipInheritanceModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IReferencedRelationshipInheritanceModel;
import com.cs.core.runtime.interactor.model.configuration.IIdAndCouplingTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.instance.IContentRelationshipInstanceModel;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class InitiateRelationshipInheritanceOnNatureRelationChange extends AbstractBGProcessJob implements IBGProcessJob {
  
  private static final String                       BATCH_SIZE            = "batchSize";
  private int                                       batchSize;
  protected int                                     nbBatches             = 1;
  int                                               currentBatchNo        = 0;
  Map<IUserSessionDTO, ILocaleCatalogDAO>           catalogDAOMap         = new HashMap<IUserSessionDTO, ILocaleCatalogDAO>();
  IRelationshipInheritanceOnNatureRelationchangeDTO natureRelationInfoDTO = new RelationshipInheritanceOnNatureRelationchangeDTO();
  RelationshipInheritance                           relationshipInheritance;

  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    natureRelationInfoDTO.fromJSON(initialProcessData.getEntryData().toString());
    RDBMSLogger.instance().debug("End of initialization BGP %s / iid=%d", getService(), getIID());
    ILocaleCatalogDAO catalogDao = openUserSession().openLocaleCatalog(userSession, natureRelationInfoDTO.getSourceContentId());
    userSession.setTransactionId(UUID.randomUUID().toString());
    catalogDAOMap.put(userSession, catalogDao);
    relationshipInheritance = BGProcessApplication.getApplicationContext().getBean(RelationshipInheritance.class);
    // initialize runtime data
    this.initRuntimeData();
  }
  
  @Override
  public BGPStatus runBatch() throws RDBMSException, CSFormatException, CSInitializationException, PluginException
  {
    IBGProcessDTO.BGPStatus status = null;

    IGetRelationshipInheritanceInfoOnNatureRelationshipChangeStartegy getRelationshipInheritanceInfoOnNatureRelationshipChangeStartegy = BGProcessApplication
        .getApplicationContext().getBean(IGetRelationshipInheritanceInfoOnNatureRelationshipChangeStartegy.class);
    try {
      IGetNatureRelationshipInfoForRelationshipInheritanceResponseModel configDetails = getRelationshipInheritanceInfoOnNatureRelationshipChangeStartegy
          .execute(prepareConfigRequestModel(natureRelationInfoDTO.getEntityRelationshipInfo()));
      Map<String, IReferencedNatureRelationshipInheritanceModel> referenceNatureRelationships = configDetails
          .getReferencedNatureRelationships();
      Map<String, IReferencedRelationshipInheritanceModel> referenceRelationship = configDetails.getReferencedRelationship();
      
      List<Long> allAddedEntities = new ArrayList<>();
      List<Long> allRemovedEntities = new ArrayList<>();

      for (IEntityRelationshipInfoDTO natureRelationshipModel : natureRelationInfoDTO.getEntityRelationshipInfo()) {
        List<Long> addedEntities = natureRelationshipModel.getAddedElements();
        List<Long> removedEntities = natureRelationshipModel.getRemovedElements();
        allAddedEntities.addAll(addedEntities);
        allRemovedEntities.addAll(removedEntities);

        Long sourceContentId = natureRelationInfoDTO.getSourceContentId();
        Boolean isManuallyCreated = natureRelationInfoDTO.getIsManuallyCreated();

        IReferencedNatureRelationshipInheritanceModel referencedNatureRelationship = referenceNatureRelationships.get(natureRelationshipModel.getNatureRelationshipId());
        Map<String, IIdAndCouplingTypeModel> propagableRelationshipIdsCouplingType = referencedNatureRelationship
            .getPropagableRelationshipIdsCouplingType();

        fillConflictForAddedEntities(allAddedEntities, referencedNatureRelationship, referenceRelationship, sourceContentId,
            propagableRelationshipIdsCouplingType, isManuallyCreated);

        fillConflictForRemovedEntities(allRemovedEntities, referencedNatureRelationship, referenceRelationship, sourceContentId);
      }
      setCurrentBatchNo(++currentBatchNo);
      
      jobData.getProgress().setPercentageCompletion(currentBatchNo * 100 / nbBatches);
      if (jobData.getProgress().getPercentageCompletion() == 100) {
        status = IBGProcessDTO.BGPStatus.ENDED_SUCCESS;
      }
      else {
        status = IBGProcessDTO.BGPStatus.ENDED_ERRORS;
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      RDBMSLogger.instance().exception(e);
    }
    return status;
  }
  
  private IIdsListParameterModel prepareConfigRequestModel(List<IEntityRelationshipInfoDTO> natureRelationshipSaveResponseModel)
  {
    IIdsListParameterModel configDataRequestModel = new IdsListParameterModel();
    List<IEntityRelationshipInfoDTO> natureRelationshipInfo = natureRelationInfoDTO.getEntityRelationshipInfo();
    List<String> natureRelationshipIds = new ArrayList<String>();
    for(IEntityRelationshipInfoDTO addedRemovedElemet : natureRelationshipInfo ) {
      natureRelationshipIds.add(addedRemovedElemet.getNatureRelationshipId());
    }
    configDataRequestModel.setIds(natureRelationshipIds);
    return configDataRequestModel;
  }

  private void fillConflictForRemovedEntities(List<Long> allRemovedEntities,
      IReferencedNatureRelationshipInheritanceModel referencedNatureRelationship,
      Map<String, IReferencedRelationshipInheritanceModel> referenceRelationship, Long sourceContentId) throws RDBMSException
  {
    for (Long removedEntity : allRemovedEntities) {
      prepareConflictAndRemoveCoupleTable(referencedNatureRelationship.getPropertyIID(), removedEntity, sourceContentId);
    }
  }
  
  private void prepareConflictAndRemoveCoupleTable(Long naturerelationshipid, Long removedEntity, Long sourceContentId)
      throws RDBMSException
  {
    ILocaleCatalogDAO catalogDao = catalogDAOMap.get(userSession);
    if (Objects.isNull(catalogDao)) {
      catalogDao = openUserSession().openLocaleCatalog(userSession, natureRelationInfoDTO.getSourceContentId());
    }
    IRelationCoupleRecordDTO relationCoupleRecord = new RelationCoupleRecordDTOBuilder().targetEntityId(removedEntity)
        .natureRelationshipId(naturerelationshipid.longValue()).build();
    IRelationCoupleRecordDAO RelationCoupleRecordDao = catalogDao.openRelationCoupleRecordDAO();
    StringBuilder filterQuery = RelationCoupleRecordDao.getFilterQuery(relationCoupleRecord);
    RelationCoupleRecordDao.deleteRelationCoupleRecord(filterQuery);
  }
  
  private void fillConflictForAddedEntities(List<Long> allAddedEntities,
      IReferencedNatureRelationshipInheritanceModel referencedNatureRelationship,
      Map<String, IReferencedRelationshipInheritanceModel> referenceRelationship, Long sourceContentId,
      Map<String, IIdAndCouplingTypeModel> propagableRelationshipIdsCouplingType, Boolean isManuallyCreated)
      throws NumberFormatException, Exception
  {
    for (Long addedEntity : allAddedEntities) {
      prepareConflictForPropagableRelationships(addedEntity, referenceRelationship, referencedNatureRelationship, sourceContentId,
          propagableRelationshipIdsCouplingType, isManuallyCreated);
    }
  }
  
  private void prepareConflictForPropagableRelationships(Long targetContentId,
      Map<String, IReferencedRelationshipInheritanceModel> referenceRelationships,
      IReferencedNatureRelationshipInheritanceModel referencedNatureRelationship, Long sourceContentId,
      Map<String, IIdAndCouplingTypeModel> propagableRelationshipIdsCouplingType, Boolean isManuallyCreated)
      throws NumberFormatException, Exception
  {
    ILocaleCatalogDAO catalogDao = openUserSession().openLocaleCatalog(userSession, sourceContentId);
    IBaseEntityDTO entityDTO = catalogDao.getEntityByIID(targetContentId);
    List<IContentRelationshipInstanceModel> modifiedRelationships = new ArrayList<>();
    for (String propagableRelationshipId : propagableRelationshipIdsCouplingType.keySet()) {
      IIdAndCouplingTypeModel couplingType = propagableRelationshipIdsCouplingType.get(propagableRelationshipId);
      String coupling = couplingType.getCouplingType();
      
      if (coupling.equals(CommonConstants.TIGHTLY_COUPLED) && isManuallyCreated) {
        continue;
      }
      
      /*
       *  check if both contents P1 and Ch1 are eligible for relationship r1.
       */
      
      if (referenceRelationships.containsKey(propagableRelationshipId)) {
        IReferencedRelationshipInheritanceModel referencedRelationship = referenceRelationships.get(propagableRelationshipId);
        if (checkIfPropagableRelationshipIsEligibleInBothSideInstances(targetContentId, sourceContentId, referencedRelationship)) {
          List<String> side1ClassifierNTaxonomyCodes = fetchClassifiersOfContent(sourceContentId);
          List<String> side2ClassifierNTaxonomyCodes = fetchClassifiersOfContent(targetContentId);
          
          List<String> propagableRelationshipSideIds = relationshipInheritance
              .fillPropagableRelationshipSideIds(side1ClassifierNTaxonomyCodes, side2ClassifierNTaxonomyCodes, referencedRelationship);
          if (coupling.equals(CommonConstants.TIGHTLY_COUPLED)) {
            relationshipInheritance.prepareConflictAndInsertInCoupleTable(couplingType.getCouplingType(), propagableRelationshipSideIds,
                referencedRelationship.getPropertyIID(), referencedNatureRelationship.getPropertyIID(), sourceContentId, targetContentId,
                catalogDao);
          }
          else if (coupling.equals(CommonConstants.DYNAMIC_COUPLED)) {
            relationshipInheritance.prepareConflictAndAdaptRelationInCoupleTable(couplingType, propagableRelationshipSideIds,
                referencedRelationship, referencedNatureRelationship.getPropertyIID(), sourceContentId, targetContentId, catalogDao, modifiedRelationships);
          }
        }
      }
    }
    relationshipInheritance.adaptRelationOnRelationshipInheritance( entityDTO, catalogDao,  entityDTO.getBaseType(), modifiedRelationships);
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
  
  protected void initRuntimeData() throws CSFormatException, CSInitializationException
  {
    if (jobData.getRuntimeData().isEmpty()) {
      // initialize batch size
      this.initBatchSize();
      
      jobData.getRuntimeData().setField(BATCH_SIZE, batchSize);
    }
    else {
      IJSONContent runtimeData = jobData.getRuntimeData();
      batchSize = runtimeData.getInitField(BATCH_SIZE, batchSize);
    }
  }
  
  protected void initBatchSize() throws CSInitializationException
  {
    batchSize = CSProperties.instance().getInt(propName("batchSize"));
    batchSize = (batchSize > 0 ? batchSize : 1); // 1 minimum is taken
  }
}
