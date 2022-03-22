package com.cs.core.bgprocess.services.relationshipinheritance;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.cs.bds.runtime.usecase.relationshipInheritance.RelationshipInheritance;
import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.dto.RelationshipInheritanceOnTypeSwitchDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.idto.IRelationshipInheritanceOnTypeSwitchDTO;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.strategy.usecase.relationship.IGetConfigDetailsForRelationshipInheritanceOnTypeSwitchStrategy;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dao.BaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IEntityRelationDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.constants.application.Seperators;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IConfigDetailsForRelationshipInheritanceOnTypeSwitchResponseModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IReferencedRelationshipInheritanceModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IRelationshipConflictingSource;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IRelationshipInheritanceOnTypeSwitchRequestModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.RelationshipInheritanceOnTypeSwitchRequestModel;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class InitiateRelationshipinheritanceOnTypeSwitch extends AbstractBGProcessJob implements IBGProcessJob {
  
  private static final String             BATCH_SIZE                  = "batchSize";
  private int                             batchSize;
  protected int                           nbBatches                   = 1;
  int                                     currentBatchNo              = 0;
  RelationshipInheritance                 relationshipInheritance;
  
  IRelationshipInheritanceOnTypeSwitchDTO relationshipInheritanceInfo = new RelationshipInheritanceOnTypeSwitchDTO();
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    relationshipInheritanceInfo.fromJSON(initialProcessData.getEntryData().toString());
    relationshipInheritance = BGProcessApplication.getApplicationContext().getBean(RelationshipInheritance.class);
    RDBMSLogger.instance().debug("End of initialization BGP %s / iid=%d", getService(), getIID());
    // initialize runtime data
    this.initRuntimeData();
  }
  
  @Override
  public BGPStatus runBatch() throws RDBMSException, CSFormatException, CSInitializationException, PluginException
  {
    IBGProcessDTO.BGPStatus status = null;
    IGetConfigDetailsForRelationshipInheritanceOnTypeSwitchStrategy getNatureRelationshipInfoForRelationshipInheritanceStrategy = BGProcessApplication
        .getApplicationContext().getBean(IGetConfigDetailsForRelationshipInheritanceOnTypeSwitchStrategy.class);
    try {
      IConfigDetailsForRelationshipInheritanceOnTypeSwitchResponseModel configDetail = getConfigdetail(
          getNatureRelationshipInfoForRelationshipInheritanceStrategy);
      getApplicableContentAndInitiateRelationshipInheritance(configDetail, relationshipInheritanceInfo.getContentId());
      
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
  
  private IConfigDetailsForRelationshipInheritanceOnTypeSwitchResponseModel getConfigdetail(
      IGetConfigDetailsForRelationshipInheritanceOnTypeSwitchStrategy getNatureRelationshipInfoForRelationshipInheritanceStrategy)
      throws Exception
  {
    IRelationshipInheritanceOnTypeSwitchRequestModel relationshipInheritanceRequest = new RelationshipInheritanceOnTypeSwitchRequestModel();
    relationshipInheritanceRequest.setContentId(relationshipInheritanceInfo.getContentId().toString());
    relationshipInheritanceRequest.setExistingTypes(relationshipInheritanceInfo.getExistingTypes());
    relationshipInheritanceRequest.setExistingTaxonomies(relationshipInheritanceInfo.getExistingTaxonomies());
    relationshipInheritanceRequest.setAddedTypes(relationshipInheritanceInfo.getAddedTypes());
    relationshipInheritanceRequest.setAddedTaxonomies(relationshipInheritanceInfo.getAddedTaxonomies());
    relationshipInheritanceRequest.setRemovedTypes(relationshipInheritanceInfo.getRemovedTypes());
    relationshipInheritanceRequest.setRemovedTaxonomies(relationshipInheritanceInfo.getRemovedTaxonomies());
    
    return getNatureRelationshipInfoForRelationshipInheritanceStrategy.execute(relationshipInheritanceRequest);
  }
  
  private void getApplicableContentAndInitiateRelationshipInheritance(
      IConfigDetailsForRelationshipInheritanceOnTypeSwitchResponseModel configDetail, Long sourceContentId) throws Exception
  {
    Map<String, Map<String, IRelationshipConflictingSource>> configuredRelSidesPerNatureRelSides = configDetail
        .getConfiguredRelSidesPerNatureRelSides();
    Map<String, IReferencedRelationshipInheritanceModel> referencedRelationship = configDetail.getReferencedRelationships();
    Map<String, String> natureRelSideId = configDetail.getNatureRelIdSideId();
    List<String> natureRelIds = new ArrayList<>(natureRelSideId.keySet());
    Map<String, String> natureRelSideIdsToInherit = configDetail.getNatureRelSideIdsToInherit();
    List<String> natureRelIdToInherit = new ArrayList<>(natureRelSideIdsToInherit.keySet());
    List<String> notApplicableRelIdSideIds = configDetail.getNotApplicableRelIdSideIds();
    Map<Long,List<String>> removeConflictsData = new HashMap<>();
    
    
    Map<String, Object> applicableContentsPerRelationship = new HashMap<>();
    Map<String, Object> applicableContentsPerRelationshipToInherit = new HashMap<>();
    ILocaleCatalogDAO catalogDao = openUserSession().openLocaleCatalog(userSession, sourceContentId);
    
    
    List<BaseEntityDAO> side2ContentList = getOtherSideEntity(sourceContentId, natureRelIds, catalogDao, RelationSide.SIDE_1);
    fillApplicableContentInfo(configuredRelSidesPerNatureRelSides, referencedRelationship, applicableContentsPerRelationship, natureRelIds,
        new ArrayList<>(natureRelSideId.values()), side2ContentList, notApplicableRelIdSideIds, removeConflictsData);
    
    List<BaseEntityDAO> side1ContentList = getOtherSideEntity(sourceContentId, natureRelIdToInherit, catalogDao, RelationSide.SIDE_2);
    fillApplicableContentInfo(configuredRelSidesPerNatureRelSides, referencedRelationship, applicableContentsPerRelationshipToInherit,
        natureRelIdToInherit, new ArrayList<>(natureRelSideIdsToInherit.values()), side1ContentList, new ArrayList<>(), new HashMap<>());
    
    relationshipInheritance.adaptRelationAndConflict(applicableContentsPerRelationshipToInherit, applicableContentsPerRelationship,
        sourceContentId, configuredRelSidesPerNatureRelSides, catalogDao, referencedRelationship);
    
    relationshipInheritance.removeConflictFromRecordTable(removeConflictsData, catalogDao);
  }
  
  private List<BaseEntityDAO> getOtherSideEntity(Long contentId, List<String> natureRelIds, ILocaleCatalogDAO catalogDao, RelationSide side)
      throws RDBMSException, CSFormatException
  {
    if (natureRelIds.isEmpty()) {
      return new ArrayList<>();
    }
    List<BaseEntityDAO> otherSideContents = new ArrayList<>();
    List<Long> otherSideContentIds = getOtherSideContentByNatureRelId(contentId, catalogDao, natureRelIds, side);
    for (Long otherSideContentId : otherSideContentIds) {
      IBaseEntityDTO entity = catalogDao.getEntityByIID(otherSideContentId);
      BaseEntityDAO baseEntityDAO = (BaseEntityDAO) catalogDao.openBaseEntity(entity);
      otherSideContents.add(baseEntityDAO);
    }
    return otherSideContents;
  }
  
  private List<Long> getOtherSideContentByNatureRelId(Long contentId, ILocaleCatalogDAO catalogDao, List<String> natureRelIds,
      RelationSide side) throws RDBMSException, CSFormatException
  {
    List<Long> otherSideEntityIds = new ArrayList<>();
    IBaseEntityDTO entityDTO = catalogDao.getEntityByIID(contentId);
    BaseEntityDAO baseEntityDAO = (BaseEntityDAO) catalogDao.openBaseEntity(entityDTO);
    IPropertyDTO relationProperty = ConfigurationDAO.instance().getPropertyByCode(natureRelIds.get(0)); // iterate Over natureRelids
    relationProperty.setRelationSide(side);
    IBaseEntityDTO result = baseEntityDAO.loadPropertyRecords(relationProperty);
    for (IPropertyRecordDTO sourceRelationRecord : result.getPropertyRecords()) {
      IRelationsSetDTO sourceRelation = (IRelationsSetDTO) sourceRelationRecord;
      Set<IEntityRelationDTO> relations = sourceRelation.getRelations();
      for( IEntityRelationDTO sorceRelation : relations) {
        otherSideEntityIds.add(sorceRelation.getOtherSideEntityIID());
      }
      /*  for (Iterator<IEntityRelationDTO> relation = relations.iterator(); relation.hasNext();) {
        IEntityRelationDTO sorceRelation = relation.next();
        otherSideEntityId.add(sorceRelation.getOtherSideEntityIID());
      }*/
    }
    return otherSideEntityIds;
  }
  
  private void fillApplicableContentInfo(Map<String, Map<String, IRelationshipConflictingSource>> configuredRelSidesPerNatureRelSides,
      Map<String, IReferencedRelationshipInheritanceModel> referencedRelationship, Map<String, Object> applicableContentsPerRelationship,
      List<String> natureRelIds, List<String> sideIds, List<BaseEntityDAO> side2ContentList, List<String> notApplicableRelIdSideIds,
      Map<Long, List<String>> removeConflictsData) throws RDBMSException
  {
    for (BaseEntityDAO side2Content : side2ContentList) {
      Long contentId = side2Content.getBaseEntityDTO().getBaseEntityIID();
      String nRelIdSideId = natureRelIds.get(0) + Seperators.RELATIONSHIP_SIDE_SPERATOR + sideIds.get(0); // modify 
      Map<String, Object> applicableContentsForNrel = (Map<String, Object>) applicableContentsPerRelationship.get(nRelIdSideId);
      Map<String, IRelationshipConflictingSource> configuredRelationships = configuredRelSidesPerNatureRelSides.get(nRelIdSideId);
      List<String> sourceKlassIds = new ArrayList<>();
      List<String> sourceTaxonomyIds = new ArrayList<>();
      fetchClassifiersOfContent(side2Content, sourceKlassIds, sourceTaxonomyIds);
      if (configuredRelationships != null) {
        
        for (Entry<String, IRelationshipConflictingSource> mapEntry : configuredRelationships.entrySet()) {
          IRelationshipConflictingSource configuredRelationshipInfo = (IRelationshipConflictingSource) mapEntry.getValue();
          String relationshipId = configuredRelationshipInfo.getRelationshipId();
          String relationshipSideId = configuredRelationshipInfo.getRelationshipSideId();
          Boolean isSideApplicable = checkWhetherSideApplicable(referencedRelationship, sourceKlassIds, sourceTaxonomyIds, relationshipId,
              relationshipSideId);
          if (isSideApplicable) {
            if (applicableContentsForNrel == null) {
              applicableContentsForNrel = new HashMap<>();
              applicableContentsPerRelationship.put(nRelIdSideId, applicableContentsForNrel);
            }
            fillApplicableContent(applicableContentsForNrel, contentId, relationshipId, relationshipSideId);
          }
        }
      }
      
      for (String reIdSideId : notApplicableRelIdSideIds) {
        String[] split = reIdSideId.split(Seperators.RELATIONSHIP_SIDE_SPERATOR);
        String relationshipId = split[0];
        String relationshipSideId = split[1];
        Boolean isSideApplicable = checkWhetherSideApplicable(referencedRelationship, sourceKlassIds, sourceTaxonomyIds, relationshipId,
            relationshipSideId);
        if (isSideApplicable) {
          List<String> notApplicableRelSideId = removeConflictsData.get(contentId);
          if (notApplicableRelSideId == null) {
            notApplicableRelSideId = new ArrayList<>();
            removeConflictsData.put(contentId, notApplicableRelSideId);
          }
          if(!notApplicableRelSideId.contains(relationshipSideId)) {
            notApplicableRelSideId.add(relationshipSideId);
          }
        }
      }
    }
  }
  
  private void fillApplicableContent(Map<String, Object> applicableContentsForNrel, long contentId, String relationshipId,
      String relationshipSideId)
  {
    String reIdSideId = relationshipId + Seperators.RELATIONSHIP_SIDE_SPERATOR + relationshipSideId;
    List<String> applicableContentIdsForRelationshipSide = (List<String>) applicableContentsForNrel.get(reIdSideId);
    if (applicableContentIdsForRelationshipSide == null) {
      applicableContentIdsForRelationshipSide = new ArrayList<>();
      applicableContentsForNrel.put(reIdSideId, applicableContentIdsForRelationshipSide);
    }
    applicableContentIdsForRelationshipSide.add(String.valueOf(contentId));
  }
  
  private void fetchClassifiersOfContent(BaseEntityDAO side2Content, List<String> classes, List<String> taxonomies) throws RDBMSException
  {
    List<String> classifierCodes = new ArrayList<>();
    IClassifierDTO classifierCode = ConfigurationDAO.instance().getClassifierByIID(getEntityByID(side2Content.getBaseEntityDTO().getBaseEntityIID()));
    classifierCodes.add(classifierCode.getClassifierCode());
    
    for (IClassifierDTO classifier : side2Content.getClassifiers()) {
      classifierCodes.add(classifier.getClassifierCode());
    }
    
    Map<ClassifierType, List<String>> groupedClassifiers = ConfigurationDAO.instance().groupClassifiers(classifierCodes);
    
    classes.addAll(groupedClassifiers.getOrDefault(ClassifierType.CLASS, new ArrayList<>()));
    taxonomies.addAll(Stream
        .of(groupedClassifiers.getOrDefault(ClassifierType.TAXONOMY, new ArrayList<>()).stream(),
            groupedClassifiers.getOrDefault(ClassifierType.MINOR_TAXONOMY, new ArrayList<>()).stream())
        .flatMap(i -> i).collect(Collectors.toList()));
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
  
  private Boolean checkWhetherSideApplicable(Map<String, IReferencedRelationshipInheritanceModel> referencedRelationships,
      List<String> sourceKlassIds, List<String> sourceTaxonomyIds, String relationshipId, String relationshipSideId) throws RDBMSException
  {
    List<String> sideKlassIds = new ArrayList<>();
    List<String> sideTaxonomyIds = new ArrayList<>();
    IReferencedRelationshipInheritanceModel referencedRelationship = referencedRelationships.get(relationshipId);
    IRelationshipSide side1 = referencedRelationship.getSide1();
    if (relationshipSideId.equals(side1.getElementId())) {
      sideKlassIds.addAll(referencedRelationship.getSide1KlassIds());
      sideTaxonomyIds.addAll(referencedRelationship.getSide1TaxonomyIds());
    }
    else {
      sideKlassIds.addAll(referencedRelationship.getSide2KlassIds());
      sideTaxonomyIds.addAll(referencedRelationship.getSide2TaxonomyIds());
    }
    Boolean isSideApplicable = isSideAplicable(sourceKlassIds, sideKlassIds, sourceTaxonomyIds, sideTaxonomyIds);
    return isSideApplicable;
  }
  
  public static Boolean isSideAplicable(Collection<String> sourceTypes, List<String> klassIds, List<String> sourceTaxonomyIds,
      List<String> taxonomyIds)
  {
    Boolean present = isTypeMatch(sourceTypes, klassIds);
    if (!present) {
      return isTypeMatch(sourceTaxonomyIds, taxonomyIds);
    }
    return present;
  }
  
  public static Boolean isTypeMatch(Collection<String> sourceTypes, List<String> klassIds)
  {
    Boolean present = sourceTypes.stream().filter(sourceTypeId -> (klassIds.contains(sourceTypeId))).findFirst().isPresent();
    return present;
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
