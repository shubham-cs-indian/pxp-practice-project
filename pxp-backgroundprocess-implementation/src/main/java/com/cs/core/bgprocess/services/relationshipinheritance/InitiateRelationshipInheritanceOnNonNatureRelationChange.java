package com.cs.core.bgprocess.services.relationshipinheritance;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import com.cs.bds.runtime.usecase.relationshipInheritance.RelationshipInheritance;
import com.cs.constants.CommonConstants;
import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.dto.RelationshipInheritanceInfoDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.idto.IEntityRelationshipInfoDTO;
import com.cs.core.bgprocess.idto.IRelationshipInheritanceInfoDTO;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.model.templating.GetNumberOfVersionsToMaintainResponseModel;
import com.cs.core.config.interactor.model.templating.IGetNumberOfVersionsToMaintainResponseModel;
import com.cs.core.config.strategy.usecase.relationship.IGetNatureRelationshipInfoForRelationshipInheritanceStrategy;
import com.cs.core.exception.PluginException;
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
import com.cs.core.runtime.interactor.entity.relationshipinstance.GetNatureRelationshipInfoForRelationshipInheritanceRequestModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.GetSideContentTypesByRelationshipModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IGetNatureRelationshipInfoForRelationshipInheritanceRequestModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IGetNatureRelationshipInfoForRelationshipInheritanceResponseModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IGetSideContentTypesByRelationshipModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IReferencedNatureRelationshipInheritanceModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IReferencedRelationshipInheritanceModel;
import com.cs.core.runtime.interactor.model.configuration.IIdAndCouplingTypeModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.services.CSConfigServer;
import com.cs.core.runtime.interactor.model.instance.IContentRelationshipInstanceModel;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class InitiateRelationshipInheritanceOnNonNatureRelationChange extends AbstractBGProcessJob implements IBGProcessJob {
  
  private static final String     BATCH_SIZE               = "batchSize";
  private int                     batchSize;
  protected int                   nbBatches                = 1;
  int                             currentBatchNo           = 0;
  
  IRelationshipInheritanceInfoDTO nonNatureRelationInfoDto   = new RelationshipInheritanceInfoDTO();
  Map<String, List<String>>       targetTypesTaxonomiesModel = new HashMap<>();
  Map<String, List<String>>       sourceTypesTaxonomiesModel = new HashMap<>();
  ILocaleCatalogDAO               catalogDao                 = null;
  RelationshipInheritance                   relationshipInheritance;
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    nonNatureRelationInfoDto.fromJSON(initialProcessData.getEntryData().toString());
    relationshipInheritance = BGProcessApplication.getApplicationContext().getBean(RelationshipInheritance.class);
    RDBMSLogger.instance().debug("End of initialization BGP %s / iid=%d", getService(), getIID());
    catalogDao = openUserSession().openLocaleCatalog(userSession, nonNatureRelationInfoDto.getSourceContentId());
    userSession.setTransactionId(UUID.randomUUID().toString());
  
    this.initRuntimeData();
    
  }
  
  @Override
  public BGPStatus runBatch() throws RDBMSException, CSFormatException, CSInitializationException, PluginException
  {
    IBGProcessDTO.BGPStatus status = null;
    IGetNatureRelationshipInfoForRelationshipInheritanceStrategy getNonNatureRelationshipInfoForRelationshipInheritanceStrategy = BGProcessApplication
        .getApplicationContext().getBean(IGetNatureRelationshipInfoForRelationshipInheritanceStrategy.class);
    try {
      IGetNatureRelationshipInfoForRelationshipInheritanceResponseModel configDetail = getConfigdetail(getNonNatureRelationshipInfoForRelationshipInheritanceStrategy);
    
      fetchClassifiersOfContent(nonNatureRelationInfoDto.getSourceContentId(), sourceTypesTaxonomiesModel);
      
      Map<String, IReferencedNatureRelationshipInheritanceModel> referencedNatureRelationships = configDetail
          .getReferencedNatureRelationships();
      Map<String, IReferencedRelationshipInheritanceModel> referencedRelationships = configDetail.getReferencedRelationship();
      
      Map<String, Set<String>> relationshipSideId_types = new HashMap<>();
      Map<String, String> relationshipId_side1Id = new HashMap<>();
      Map<String, String> relationshipId_side2Id = new HashMap<>();
      
      
      relationshipInheritance.fillInformationForRelationshipTranferInfo(referencedRelationships, relationshipSideId_types,  nonNatureRelationInfoDto ,relationshipId_side1Id,relationshipId_side2Id);
      
      initiateForCurrentSide(nonNatureRelationInfoDto, referencedNatureRelationships,referencedRelationships,
          relationshipSideId_types, relationshipId_side1Id);
      
      initiateForCurrentSide(nonNatureRelationInfoDto, referencedNatureRelationships,referencedRelationships,
          relationshipSideId_types, relationshipId_side2Id);
      
      initiateForOtherSide(nonNatureRelationInfoDto, referencedNatureRelationships,referencedRelationships,
          relationshipSideId_types, relationshipId_side1Id);
      
      initiateForOtherSide(nonNatureRelationInfoDto, referencedNatureRelationships,referencedRelationships,
          relationshipSideId_types, relationshipId_side2Id);
      
      
      setCurrentBatchNo(++currentBatchNo);
      
      jobData.getProgress().setPercentageCompletion(currentBatchNo * 100 / nbBatches);
      if (jobData.getProgress().getPercentageCompletion() == 100)
      {
        status = IBGProcessDTO.BGPStatus.ENDED_SUCCESS;
      }else {
        status = IBGProcessDTO.BGPStatus.ENDED_ERRORS;
      }
      
    }
    catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      RDBMSLogger.instance().exception(e);
    }
    
    return status;
  }
  
   private void initiateForOtherSide(IRelationshipInheritanceInfoDTO nonNatureRelationInfoDto,
      Map<String, IReferencedNatureRelationshipInheritanceModel> referencedNatureRelationships,
      Map<String, IReferencedRelationshipInheritanceModel> referencedRelationships, Map<String, Set<String>> relationshipSideId_types, Map<String, String> relationshipId_side1Id) throws NumberFormatException, Exception
  {
    if (relationshipId_side1Id.isEmpty()) {
      return;
    }
    List<Long> addedRemovedEntity = new ArrayList<>();
    for(IEntityRelationshipInfoDTO relationshipInfo : nonNatureRelationInfoDto.getEntityRelationshipInfo()) {
      addedRemovedEntity = new ArrayList<Long>(relationshipInfo.getAddedElements());
      addedRemovedEntity.addAll(relationshipInfo.getRemovedElements());
    
      for (Long sourceId : addedRemovedEntity) {
        List<String> sourceContentTypes = getTypesForContent(sourceId); 
        List<IGetSideContentTypesByRelationshipModel> child2ContentTypeByRelation =  getChildContentInfo(referencedNatureRelationships, 
            sourceContentTypes , sourceId);
      
        if (child2ContentTypeByRelation != null) {
          fillConflictsForSide2Contents(sourceId, referencedNatureRelationships, referencedRelationships,
              relationshipSideId_types, relationshipId_side1Id, child2ContentTypeByRelation);
        }
      }
    }
  }
  
  private void initiateForCurrentSide(IRelationshipInheritanceInfoDTO nonNatureRelationInfoDto,
      Map<String, IReferencedNatureRelationshipInheritanceModel> referencedNatureRelationships,
      Map<String, IReferencedRelationshipInheritanceModel> referencedRelationships, Map<String, Set<String>> relationshipSideId_types,
      Map<String, String> relationshipId_side1Id) throws Exception
  {
    if (relationshipId_side1Id.isEmpty()) {
      return;
    }
    Long sourceContentId = nonNatureRelationInfoDto.getSourceContentId();
    List<String> sourceContentTypes = getTypesForContent(sourceContentId); 
    List<IGetSideContentTypesByRelationshipModel> child2ContentTypeByRelation = getChildContentInfo(referencedNatureRelationships, sourceContentTypes,
        sourceContentId);
    
    if (!child2ContentTypeByRelation.isEmpty()) {
      fillConflictsForSide2Contents(sourceContentId, referencedNatureRelationships, referencedRelationships, relationshipSideId_types,
          relationshipId_side1Id, child2ContentTypeByRelation);
    }
  }

  
  private void fillConflictsForSide2Contents(Long sourceContentId,
      Map<String, IReferencedNatureRelationshipInheritanceModel> referencedNatureRelationships,
      Map<String, IReferencedRelationshipInheritanceModel> referencedRelationships, Map<String, Set<String>> relationshipSideId_types,
      Map<String, String> relationshipId_side1Id, List<IGetSideContentTypesByRelationshipModel> child2ContentTypeByRelation)
      throws NumberFormatException, Exception
  {
    for (IGetSideContentTypesByRelationshipModel modelForChild2Content : child2ContentTypeByRelation) {
      if(modelForChild2Content.getContentId() == null) {
        break;
      }
      List<IContentRelationshipInstanceModel> modifiedRelationships = new ArrayList<>();
      Map<String, IIdAndCouplingTypeModel> propagableRelationshipIdsCouplingType = relationshipInheritance.getApplicableRelatioshipCoupling(
          referencedNatureRelationships, modelForChild2Content.getRelationshipId());
      for (Entry<String, IIdAndCouplingTypeModel> propagableRelationshipIdsCouplingTypeEntry : propagableRelationshipIdsCouplingType
          .entrySet()) {
        
        String propagableRelationshipId = propagableRelationshipIdsCouplingTypeEntry.getKey();
        String propagableRelationshipSideId = relationshipId_side1Id.get(propagableRelationshipId);
        
        Set<String> applicableTypesForSide = relationshipSideId_types.get(propagableRelationshipSideId);
        if (applicableTypesForSide == null || applicableTypesForSide.isEmpty()) {
          continue;
        }
        
        IReferencedRelationshipInheritanceModel referenceRelation =  referencedRelationships.get(propagableRelationshipId);
        
        if (relationshipInheritance.isTypeMatch(targetTypesTaxonomiesModel , modelForChild2Content.getContentId().toString(), applicableTypesForSide)
            || relationshipInheritance.isTypeMatch(sourceTypesTaxonomiesModel, nonNatureRelationInfoDto.getSourceContentId().toString() , applicableTypesForSide)) {
          
          IContentRelationshipInstanceModel modifiedRelationship = prepareAndAddNewConflicts(sourceContentId, referencedNatureRelationships, modelForChild2Content,
              propagableRelationshipIdsCouplingTypeEntry, referenceRelation, propagableRelationshipSideId);
         
          
          if(modifiedRelationship!=null &&(!modifiedRelationship.getAddedElements().isEmpty() || !modifiedRelationship.getDeletedElements().isEmpty() || !modifiedRelationship.getModifiedElements().isEmpty())) {
            modifiedRelationships.add(modifiedRelationship);
          }
        }
      }
      IBaseEntityDTO entityDTO = catalogDao.getEntityByIID(modelForChild2Content.getContentId());
      
      relationshipInheritance.adaptRelationOnRelationshipInheritance( entityDTO, catalogDao,  entityDTO.getBaseType(), modifiedRelationships);
    }
  }

  private IContentRelationshipInstanceModel prepareAndAddNewConflicts(Long sourceContentId,
      Map<String, IReferencedNatureRelationshipInheritanceModel> referencedNatureRelationships, 
      IGetSideContentTypesByRelationshipModel modelForChild2Content,
      Entry<String, IIdAndCouplingTypeModel> propagableRelationshipIdsCouplingTypeEntry, IReferencedRelationshipInheritanceModel relationship,
      String propagableRelationshipSideId) throws NumberFormatException, Exception
  {
    IContentRelationshipInstanceModel modifiedRelationsip = null;
    IRelationCoupleRecordDAO relationCoupleRecordDao = catalogDao.openRelationCoupleRecordDAO();
    IIdAndCouplingTypeModel idCouplingType = propagableRelationshipIdsCouplingTypeEntry.getValue();
   
    IPropertyDTO natureRelationProperty = ConfigurationDAO.instance().getPropertyByCode(modelForChild2Content.getRelationshipId());
    
    IRelationCoupleRecordDTO exsitingRelationCoupleRecord = new RelationCoupleRecordDTOBuilder()
        .targetEntityId(modelForChild2Content.getContentId()).propagableRelationshipSideId(propagableRelationshipSideId).build();
    StringBuilder filterQuery = relationCoupleRecordDao.getFilterQuery(exsitingRelationCoupleRecord);
   
    if (idCouplingType.getCouplingType().equals(CommonConstants.TIGHTLY_COUPLED)) {
      if (!relationCoupleRecordDao.fetchRelationCoupleRecord(filterQuery).isEmpty()) {
        IRelationCoupleRecordDTO relationCoupleRecord = new RelationCoupleRecordDTOBuilder()
            .couplingType(IRelationCoupleRecordDTO.CouplingType.valueOf(idCouplingType.getCouplingType())).isResolved(false)
            .targetEntityId(modelForChild2Content.getContentId()).propagableRelationshipId(relationship.getPropertyIID()).
            sourceEntityId(sourceContentId).propagableRelationshipSideId(propagableRelationshipSideId).natureRelationshipId(natureRelationProperty.getPropertyIID()).build();
        relationCoupleRecordDao.updateConflictResolvedStatus(relationCoupleRecord);
      }else {
        IRelationCoupleRecordDTO relationCoupleRecord = new RelationCoupleRecordDTOBuilder()
            .couplingType(IRelationCoupleRecordDTO.CouplingType.valueOf(idCouplingType.getCouplingType())).isResolved(false)
            .targetEntityId(modelForChild2Content.getContentId()).propagableRelationshipId(relationship.getPropertyIID()).
            sourceEntityId(sourceContentId).propagableRelationshipSideId(propagableRelationshipSideId).natureRelationshipId(natureRelationProperty.getPropertyIID()).build();
        relationCoupleRecordDao.createRelationCoupleRecord(relationCoupleRecord);
      }
    }
    if (idCouplingType.getCouplingType().equals(CommonConstants.DYNAMIC_COUPLED)) {
      if (!relationCoupleRecordDao.fetchRelationCoupleRecord(filterQuery).isEmpty()) {
         modifiedRelationsip = relationshipInheritance.adaptRelationInTargetEntity(modelForChild2Content, sourceContentId, referencedNatureRelationships, relationship,
            propagableRelationshipSideId,catalogDao );
      }
      else {
        IRelationCoupleRecordDTO relationCoupleRecord = new RelationCoupleRecordDTOBuilder()
            .couplingType(IRelationCoupleRecordDTO.CouplingType.valueOf(idCouplingType.getCouplingType())).isResolved(true)
            .targetEntityId(modelForChild2Content.getContentId()).propagableRelationshipId(relationship.getPropertyIID()).
            sourceEntityId(sourceContentId).propagableRelationshipSideId(propagableRelationshipSideId).natureRelationshipId(natureRelationProperty.getPropertyIID()).build();
        relationCoupleRecordDao.createRelationCoupleRecord(relationCoupleRecord);
      }
    }
    return modifiedRelationsip;
   }

  private IGetNatureRelationshipInfoForRelationshipInheritanceResponseModel getConfigdetail(IGetNatureRelationshipInfoForRelationshipInheritanceStrategy getNatureRelationshipInfoForRelationshipInheritanceStrategy) throws Exception 
  {
    List<IEntityRelationshipInfoDTO> natureRelationshipInfo = nonNatureRelationInfoDto.getEntityRelationshipInfo();
    List<String> relationshipIds = new ArrayList<String>();
    for(IEntityRelationshipInfoDTO addedRemovedElemet : natureRelationshipInfo ) {
      relationshipIds.add(addedRemovedElemet.getRelationshipId());
    }
    IGetNatureRelationshipInfoForRelationshipInheritanceRequestModel configRequestModel = new GetNatureRelationshipInfoForRelationshipInheritanceRequestModel();
    configRequestModel.setRelationshipIds(relationshipIds);
    configRequestModel.setTypes(prepareSourceContentTypesForConfig());
    IGetNatureRelationshipInfoForRelationshipInheritanceResponseModel responseModel = getNatureRelationshipInfoForRelationshipInheritanceStrategy
        .execute(configRequestModel);
    return responseModel;
  }

  private List<String> prepareSourceContentTypesForConfig() throws RDBMSException 
  {
    List<String> classifierCodes = new ArrayList<>();
    List<Long> classifierIds =new ArrayList<>();
    getClassifierCodeByEntityID(nonNatureRelationInfoDto.getSourceContentId(),classifierIds);
    List<Long> addedEntites = nonNatureRelationInfoDto.getEntityRelationshipInfo().get(0).getAddedElements();
    List<Long> removeEntites = nonNatureRelationInfoDto.getEntityRelationshipInfo().get(0).getRemovedElements();
    addedEntites.forEach(entity->  {
      try {
        getClassifierCodeByEntityID(entity, classifierIds);
      }
      catch (NumberFormatException | RDBMSException e) {
        e.printStackTrace();
        RDBMSLogger.instance().exception(e);
      }
    });
    removeEntites.forEach(entity->  {
      try {
        getClassifierCodeByEntityID(entity, classifierIds);
      }
      catch (NumberFormatException | RDBMSException e) {
        e.printStackTrace();
        RDBMSLogger.instance().exception(e);
      }
    });
    
    for(Long classifierId :classifierIds) {
    IClassifierDTO classifierCode = ConfigurationDAO.instance().getClassifierByIID(classifierId);
    classifierCodes.add(classifierCode.getClassifierCode());
    }
    return classifierCodes;
  }
  

  private void fetchClassifiersOfContent(Long sourceContentId , Map<String, List<String>>sourceTypesTaxonomiesModel) throws RDBMSException
  {
    List<String> classifierCodes = new ArrayList<>();
    List<Long> classifierIds = new ArrayList<>();
    getClassifierCodeByEntityID(sourceContentId, classifierIds);
    for(Long classifierId :classifierIds) {
    IClassifierDTO classifierCode = ConfigurationDAO.instance().getClassifierByIID(classifierId);
    classifierCodes.add(classifierCode.getClassifierCode());
    }
   
    ILocaleCatalogDAO catalogDao = openUserSession().openLocaleCatalog(userSession, sourceContentId);
    IBaseEntityDTO entity = catalogDao.getEntityByIID(sourceContentId);
    BaseEntityDAO entityDao = (BaseEntityDAO) catalogDao.openBaseEntity(entity);
    for(IClassifierDTO classifier : entityDao.getClassifiers()) {
      classifierCodes.add(classifier.getClassifierCode());
    }
    
    sourceTypesTaxonomiesModel.put(sourceContentId.toString(), classifierCodes );
  }

  private List<IGetSideContentTypesByRelationshipModel> getChildContentInfo(
      Map<String, IReferencedNatureRelationshipInheritanceModel> referencedNatureRelationships,  Collection<String> types,
      Long sourceContentId) throws NumberFormatException, RDBMSException
  {
    
    List<IGetSideContentTypesByRelationshipModel> childEntityContent = new ArrayList<>();
    IGetSideContentTypesByRelationshipModel childEntityInfo = new GetSideContentTypesByRelationshipModel();
    referencedNatureRelationships.forEach((natureRelationshipId, natureRelatiosnhip) -> {
      
      IRelationshipSide side1 = natureRelatiosnhip.getSide1();
      
      // filter by types
      if (!types.contains(side1.getKlassId())) {
        return;
      }
      try {
        getOtherSideOfRelation(natureRelatiosnhip.getPropertyIID(), sourceContentId, childEntityInfo);
        childEntityInfo.setRelationshipId(natureRelatiosnhip.getCode());
        childEntityInfo.setSideId(side1.getId());
        if (childEntityInfo.getContentId() != null) {
          fetchClassifiersOfContent(childEntityInfo.getContentId(), targetTypesTaxonomiesModel);
        }
      }
      catch (RDBMSException e) {
        e.printStackTrace();
        RDBMSLogger.instance().exception(e);
      }
      childEntityContent.add(childEntityInfo);
    });
    
    return childEntityContent;
  }
  
 private static final String GET_OTHER_SIDE_OF_RELATION = "select * from pxp.relation where propertyiid = (?) and side1entityiid = (?) ";
  
  private void getOtherSideOfRelation(Long propertyiid, Long side1entityid, IGetSideContentTypesByRelationshipModel childEntityId) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      String classifierQuery = String.format(GET_OTHER_SIDE_OF_RELATION);
      PreparedStatement stmt = currentConn.prepareStatement(classifierQuery);
      stmt.setLong(1, propertyiid);
      stmt.setLong(2, side1entityid);
      stmt.execute();
      IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());
      while (result.next()) {
        childEntityId.setContentId(result.getLong("side2entityiid"));
      }
    });
  }
  
  private List<String> getTypesForContent(Long sourceContentId) throws RDBMSException
  {
    List<String> classifierCodes = new ArrayList<>();
    List<Long> classifierIds = new ArrayList<>();
    getClassifierCodeByEntityID(sourceContentId, classifierIds);
    for(Long classifierId :classifierIds) {
    IClassifierDTO classifierCode = ConfigurationDAO.instance().getClassifierByIID(classifierId);
    classifierCodes.add(classifierCode.getClassifierCode());
    }
   
    ILocaleCatalogDAO catalogDao = openUserSession().openLocaleCatalog(userSession, sourceContentId);
    IBaseEntityDTO entity = catalogDao.getEntityByIID(sourceContentId);
    BaseEntityDAO entityDao = (BaseEntityDAO) catalogDao.openBaseEntity(entity);
    for(IClassifierDTO classifier : entityDao.getClassifiers()) {
      classifierCodes.add(classifier.getClassifierCode());
    }   
    return classifierCodes;
  }
  
   private static final String GET_BASE_ENTITY = "select * from pxp.baseentity where baseentityiid = (?) ";
  
  
  private void getClassifierCodeByEntityID(Long entityid , List<Long> classifieriids ) throws RDBMSException
  {
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
