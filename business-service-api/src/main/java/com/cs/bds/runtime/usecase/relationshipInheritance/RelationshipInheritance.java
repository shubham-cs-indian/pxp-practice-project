package com.cs.bds.runtime.usecase.relationshipInheritance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.bgprocess.idto.IEntityRelationshipInfoDTO;
import com.cs.core.bgprocess.idto.IRelationshipInheritanceInfoDTO;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;
import com.cs.core.rdbms.entity.dao.BaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IEntityRelationDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.relationship.dto.RelationCoupleRecordDTO.RelationCoupleRecordDTOBuilder;
import com.cs.core.rdbms.relationship.idao.IRelationCoupleRecordDAO;
import com.cs.core.rdbms.relationship.idto.IRelationCoupleRecordDTO;
import com.cs.core.runtime.interactor.constants.application.Seperators;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRelationshipVersion;
import com.cs.core.runtime.interactor.entity.relationship.RelationshipVersion;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IGetSideContentTypesByRelationshipModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IReferencedNatureRelationshipInheritanceModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IReferencedRelationshipInheritanceModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IRelationshipConflictingSource;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.entity.timerange.InstanceTimeRange;
import com.cs.core.runtime.interactor.model.configuration.IIdAndCouplingTypeModel;
import com.cs.core.runtime.interactor.model.instance.ContentRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.instance.IContentRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.relationship.ISaveRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.relationship.SaveRelationshipInstanceModel;
import com.cs.core.runtime.interactor.utils.klassinstance.TagInstanceUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.dam.runtime.assetinstance.ISaveAssetInstanceRelationshipsService;
import com.cs.pim.runtime.articleinstance.ISaveArticleInstanceRelationshipsService;
import com.cs.pim.runtime.targetinstance.market.ISaveMarketInstanceRelationshipsService;
import com.cs.utils.BaseEntityUtils;

@Component
public class RelationshipInheritance
{  
  @Autowired
  ISaveArticleInstanceRelationshipsService saveArticleInstanceRelationshipsService;
  
  @Autowired
  ISaveAssetInstanceRelationshipsService saveAssetInstanceRelationshipsService;
  
  @Autowired
  ISaveMarketInstanceRelationshipsService  saveMarketInstanceRelationshipsService;
  

  public void adaptRelationOnRelationshipInheritance( IBaseEntityDTO entityDTO,
      ILocaleCatalogDAO catalogDao,
      BaseType baseType, List<IContentRelationshipInstanceModel> modifiedRelationships) throws Exception
  {
    ISaveRelationshipInstanceModel dataModel = new SaveRelationshipInstanceModel();
    String entityBaseType = BaseEntityUtils.getBaseTypeString(baseType);
    dataModel.setBaseType(entityBaseType);
    dataModel.setId(String.valueOf(entityDTO.getBaseEntityIID()));
   
    dataModel.setModifiedRelationships(modifiedRelationships);
    dataModel.setTabType(CommonConstants.CUSTOM_TEMPLATE_TAB_BASETYPE);
    dataModel.setTabId(SystemLevelIds.RELATIONSHIP_TAB);
   
    if(dataModel.getModifiedRelationships()!=null && dataModel.getModifiedRelationships().size() != 0 ) {
      executeSaveRelation(dataModel, baseType);
    }
  }
  
  public void executeSaveRelation(ISaveRelationshipInstanceModel dataModel, BaseType baseType) throws Exception
  {
    switch (baseType) {
      case ARTICLE:
        saveArticleInstanceRelationshipsService.execute(dataModel);
        break;
      case ASSET:
        saveAssetInstanceRelationshipsService.execute(dataModel);
        break;
      case TARGET:
        saveMarketInstanceRelationshipsService.execute(dataModel);
        break;
    }
  }
  
  
  public void addRelationOnDynamicCoupled(Set<IEntityRelationDTO> sourceRelationsList, Set<IEntityRelationDTO> targetRelationsList,
      BaseEntityDAO baseEntityDAO, IContentRelationshipInstanceModel modifiedRelationship) throws RDBMSException
  {
    Set<IEntityRelationDTO> relationsToAdd = new HashSet<>(sourceRelationsList);
    relationsToAdd.removeAll(targetRelationsList);
    List<IRelationshipVersion> addedElements = new ArrayList<>();
    for (IEntityRelationDTO relation : relationsToAdd) {
      IRelationshipVersion addedElement = new RelationshipVersion();
      addedElement.setId(String.valueOf(relation.getOtherSideEntityIID()));
      IInstanceTimeRange instanceTimeRange = new InstanceTimeRange();
      
      instanceTimeRange.setFrom(relation.getContextualObject().getContextStartTime());
      instanceTimeRange.setTo(relation.getContextualObject().getContextEndTime());
      addedElement.setTimeRange(instanceTimeRange);
      
      addedElement.setTags(TagInstanceUtils.getTagInstance(relation, baseEntityDAO));
      addedElement.setContextId(relation.getContextualObject().getContextCode());
      addedElements.add(addedElement);
    }
    modifiedRelationship.setAddedElements(addedElements);
  }
  
  public void removeRelationOnDynamicCoupled(Set<IEntityRelationDTO> sourceRelationsList,
      Set<IEntityRelationDTO> targetRelationsList, IContentRelationshipInstanceModel modifiedRelationship) throws RDBMSException
  {
    Set<IEntityRelationDTO> relationsToRemove = new HashSet<>(targetRelationsList);
    relationsToRemove.removeAll(sourceRelationsList);
    List<String> deletedElements = new ArrayList<>();
    for (IEntityRelationDTO relation : relationsToRemove) {
      deletedElements.add(String.valueOf(relation.getOtherSideEntityIID()));
    }
    modifiedRelationship.setDeletedElements(deletedElements);
  }

  public Set<IEntityRelationDTO> getRelations(Long entityId, IReferencedRelationshipInheritanceModel referencedRelationship,
      String propagableRelationshipId, ILocaleCatalogDAO catalogDao) throws NumberFormatException, RDBMSException, CSFormatException
  {
    RelationSide relationSide = getRelationSide(referencedRelationship, propagableRelationshipId);
    IBaseEntityDTO entity = catalogDao.getEntityByIID(entityId);
    BaseEntityDAO baseEntityDAO = (BaseEntityDAO) catalogDao.openBaseEntity(entity);
    IPropertyDTO propertyDto = ConfigurationDAO.instance().getPropertyByCode(referencedRelationship.getCode());
    propertyDto.setRelationSide(relationSide);
    IBaseEntityDTO result = baseEntityDAO.loadPropertyRecords(propertyDto);
    Set<IEntityRelationDTO> relations = new HashSet<>();
    for (IPropertyRecordDTO sourceRelationRecord : result.getPropertyRecords()) {
      IRelationsSetDTO sourceRelation = (IRelationsSetDTO) sourceRelationRecord;
      relations = sourceRelation.getRelations();
    }
    return relations;
  }
  
  public RelationSide getRelationSide(IReferencedRelationshipInheritanceModel referencedRelationship,
      String propagableRelationshipSideId)
  {
    RelationSide relation = null;
    if (referencedRelationship.getSide1().getElementId().equals(propagableRelationshipSideId)) {
      relation = RelationSide.SIDE_1;
    }
    else {
      relation = RelationSide.SIDE_2;
    }
    return relation;
  }
  
  public void prepareConflictAndInsertInCoupleTable(String couplingType,  List<String> propagableRelationshipSideIds, Long relationshipId,
      Long natureRelationshipId, Long sourceContentId, Long targetContentId, ILocaleCatalogDAO catalogDao) throws RDBMSException
  {
    IRelationCoupleRecordDAO relationCoupleRecordDao = catalogDao.openRelationCoupleRecordDAO();
    for (String propagableRelationshipId : propagableRelationshipSideIds) {
      IRelationCoupleRecordDTO relationCoupleRecord = new RelationCoupleRecordDTOBuilder().sourceEntityId(sourceContentId.longValue())
          .targetEntityId(targetContentId).natureRelationshipId(natureRelationshipId.longValue())
          .propagableRelationshipId(relationshipId.longValue()).isResolved(false).propagableRelationshipSideId(propagableRelationshipId)
          .couplingType(IRelationCoupleRecordDTO.CouplingType.valueOf(couplingType)).build();
      relationCoupleRecordDao.createRelationCoupleRecord(relationCoupleRecord);
    }
  }
  

  public  void prepareConflictAndAdaptRelationInCoupleTable(IIdAndCouplingTypeModel couplingType,
      List<String> propagableRelationshipSideIds, IReferencedRelationshipInheritanceModel referencedRelationship, long natureRelationshipId,
      Long sourceContentId, Long targetContentId, ILocaleCatalogDAO catalogDao, List<IContentRelationshipInstanceModel> modifiedRelationships) throws NumberFormatException, Exception
  {
    List<IRelationCoupleRecordDTO> relationCoupleRecords = new ArrayList<>();
    
    IBaseEntityDTO entityDTO = catalogDao.getEntityByIID(targetContentId);
    
    for (String propagableRelationshipSideId : propagableRelationshipSideIds) {
      
      Set<IEntityRelationDTO> sourceRelationsList = getRelations(sourceContentId, referencedRelationship, propagableRelationshipSideId,
          catalogDao);
      Set<IEntityRelationDTO> targetRelationsList = getRelations(targetContentId, referencedRelationship, propagableRelationshipSideId,
          catalogDao);
      
      IPropertyDTO relationProperty = ConfigurationDAO.instance().getPropertyByCode(referencedRelationship.getCode());
      IRelationCoupleRecordDTO relationCoupleRecord = new RelationCoupleRecordDTOBuilder().sourceEntityId(sourceContentId.longValue())
          .targetEntityId(targetContentId).natureRelationshipId(natureRelationshipId)
          .propagableRelationshipId(referencedRelationship.getPropertyIID()).isResolved(true)
          .propagableRelationshipSideId(propagableRelationshipSideId)
          .couplingType(IRelationCoupleRecordDTO.CouplingType.valueOf(couplingType.getCouplingType())).build();
      relationCoupleRecords.add(relationCoupleRecord);
      
      BaseEntityDAO baseEntityDAO = (BaseEntityDAO) catalogDao.openBaseEntity(entityDTO);
      
      IContentRelationshipInstanceModel modifiedRelationship = new ContentRelationshipInstanceModel();
      
      modifiedRelationship.setRelationshipId(relationProperty.getCode());
      modifiedRelationship.setId(relationProperty.getPropertyCode());
      modifiedRelationship.setSideId(propagableRelationshipSideId);
      
      if(!sourceRelationsList.isEmpty()) {
        modifiedRelationship.setBaseType(BaseEntityUtils.getBaseTypeString(catalogDao.getEntityByIID( sourceRelationsList.iterator().next().getOtherSideEntityIID()).getBaseType()));
      } 
      
      addRelationOnDynamicCoupled(sourceRelationsList, targetRelationsList, baseEntityDAO, modifiedRelationship);
      
      removeRelationOnDynamicCoupled(sourceRelationsList, targetRelationsList, modifiedRelationship);
      
      modifyRelationOnDynamicCoupled(sourceRelationsList, targetRelationsList, baseEntityDAO, modifiedRelationship);
      
      if(!modifiedRelationship.getAddedElements().isEmpty() || !modifiedRelationship.getDeletedElements().isEmpty() || !modifiedRelationship.getModifiedElements().isEmpty()) {
          modifiedRelationships.add(modifiedRelationship);
      }
    }

    IRelationCoupleRecordDAO relationCoupleRecordDao = catalogDao.openRelationCoupleRecordDAO();
    relationCoupleRecordDao.createRelationCoupleRecord(relationCoupleRecords.toArray(new IRelationCoupleRecordDTO[relationCoupleRecords.size()]));
  }
  
  public void prepareConflictAndUpdateInCoupleTable(String couplingType, List<String> propagableRelationshipIds, Long relationshipId,
      Long natureRelationshipId, Long sourceContentId, Long targetContentId, ILocaleCatalogDAO catalogDao) throws RDBMSException
  {
    IRelationCoupleRecordDAO relationCoupleRecordDao = catalogDao.openRelationCoupleRecordDAO();
    for (String propagableRelationshipId : propagableRelationshipIds) {
      IRelationCoupleRecordDTO relationCoupleRecord = new RelationCoupleRecordDTOBuilder().sourceEntityId(sourceContentId.longValue())
          .targetEntityId(targetContentId).natureRelationshipId(natureRelationshipId.longValue())
          .propagableRelationshipId(relationshipId.longValue()).isResolved(false).propagableRelationshipSideId(propagableRelationshipId)
          .couplingType(IRelationCoupleRecordDTO.CouplingType.valueOf(couplingType)).build();
      relationCoupleRecordDao.updateConflictResolvedStatus(relationCoupleRecord);
    }
  }
  
  public  List<String> fillPropagableRelationshipSideIds(List<String> side1InstanceTypesTaxonomies,
      List<String> side2InstanceTypesTaxonomies, IReferencedRelationshipInheritanceModel referencedRelationship)
  {
    
    List<String> propagableRelationshipSideIds = new ArrayList<>();
    List<String> propagableRelationshipSide1TypesTaxonomies = new ArrayList<>();
    List<String> propagableRelationshipSide2TypesTaxonomies = new ArrayList<>();
    
    String side1Cardinality = referencedRelationship.getSide1().getCardinality();
    String side2Cardinality = referencedRelationship.getSide2().getCardinality();
    propagableRelationshipSide1TypesTaxonomies.addAll(referencedRelationship.getSide1KlassIds());
    propagableRelationshipSide1TypesTaxonomies.addAll(referencedRelationship.getSide1TaxonomyIds());
    propagableRelationshipSide2TypesTaxonomies.addAll(referencedRelationship.getSide2KlassIds());
    propagableRelationshipSide2TypesTaxonomies.addAll(referencedRelationship.getSide2TaxonomyIds());
    
    if (!ListUtils.intersection(propagableRelationshipSide1TypesTaxonomies, side2InstanceTypesTaxonomies).isEmpty()
        && !ListUtils.intersection(propagableRelationshipSide1TypesTaxonomies, side1InstanceTypesTaxonomies).isEmpty()
        && side1Cardinality.equals(CommonConstants.CARDINALITY_MANY)) {
      propagableRelationshipSideIds.add(referencedRelationship.getSide1().getElementId());
    }
    if (!ListUtils.intersection(propagableRelationshipSide2TypesTaxonomies, side2InstanceTypesTaxonomies).isEmpty()
        && !ListUtils.intersection(propagableRelationshipSide2TypesTaxonomies, side1InstanceTypesTaxonomies).isEmpty()
        && side2Cardinality.equals(CommonConstants.CARDINALITY_MANY)) {
      propagableRelationshipSideIds.add(referencedRelationship.getSide2().getElementId());
    }
    
    return propagableRelationshipSideIds;
  }
  
  public void modifyRelationOnDynamicCoupled(Set<IEntityRelationDTO> sourceRelationsList,
      Set<IEntityRelationDTO> targetRelationsList, BaseEntityDAO baseEntityDAO, IContentRelationshipInstanceModel modifiedRelationship) throws RDBMSException
  {
    List<IRelationshipVersion> modifiedElements = new ArrayList<>();
    for (IEntityRelationDTO sourceRelation : sourceRelationsList) {
      for (IEntityRelationDTO targetRelation : targetRelationsList) {
        if (sourceRelation.getOtherSideEntityIID() == targetRelation.getOtherSideEntityIID()) {
          
          IRelationshipVersion modifiedElement = new RelationshipVersion();
          modifiedElement.setId(String.valueOf(sourceRelation.getOtherSideEntityIID()));
          
          if (checkIfContextModified(sourceRelation, targetRelation)) {
            // Time range
            IInstanceTimeRange instanceTimeRange = new InstanceTimeRange();
            instanceTimeRange.setFrom(sourceRelation.getContextualObject().getContextStartTime());
            instanceTimeRange.setTo(sourceRelation.getContextualObject().getContextEndTime());
            modifiedElement.setTimeRange(instanceTimeRange);
            
            modifiedElement.setTags(TagInstanceUtils.getTagInstance(sourceRelation, baseEntityDAO));
            modifiedElement.setContextId(sourceRelation.getContextualObject().getContextCode());
            if (!modifiedElement.getContextId().isEmpty()) {
              modifiedElements.add(modifiedElement);
            }
          }
        }
        modifiedRelationship.setModifiedElements(modifiedElements);
      }
    }
  }

  private boolean checkIfContextModified(IEntityRelationDTO sourceRelation, IEntityRelationDTO targetRelation)
  {
    return sourceRelation.getContextualObject().getContextStartTime() != targetRelation.getContextualObject().getContextEndTime()
        || sourceRelation.getContextualObject().getContextEndTime() != targetRelation.getContextualObject().getContextEndTime()
        || sourceRelation.getContextualObject().getContextTagValues()
            .equals(targetRelation.getContextualObject().getContextTagValues());
  }
  
  public static Set<String> getMergeType(List<String> sideKlassIds, List<String> sideTaxonomyIds)
  {
    return Stream.concat(sideKlassIds.stream(), sideTaxonomyIds.stream()).collect(Collectors.toSet());
  }
  
  public void fillInformationForRelationshipTranferInfo(Map<String, IReferencedRelationshipInheritanceModel> referencedRelationships,
      Map<String, Set<String>> relationshipSideId_types, IRelationshipInheritanceInfoDTO nonNatureRelationInfoDto,
      Map<String, String> relationshipId_side1Id, Map<String, String> relationshipId_side2Id)
  {
    for (IEntityRelationshipInfoDTO relationshipInfo : nonNatureRelationInfoDto.getEntityRelationshipInfo()) {
      String relationshipId = relationshipInfo.getRelationshipId();
      String sideId = relationshipInfo.getSideId();
      
      IReferencedRelationshipInheritanceModel referencedRelationship = referencedRelationships.get(relationshipId);
      
      IRelationshipSide side1ReferenceRelationship = referencedRelationship.getSide1();
      IRelationshipSide side2ReferenceRelationship = referencedRelationship.getSide2();
      
      Set<String> side1MergeTypes = RelationshipInheritance.getMergeType(referencedRelationship.getSide1KlassIds(),
          referencedRelationship.getSide1TaxonomyIds());
      Set<String> side2MergeType = RelationshipInheritance.getMergeType(referencedRelationship.getSide2KlassIds(),
          referencedRelationship.getSide2TaxonomyIds());
      
      String side1ElementId = side1ReferenceRelationship.getElementId();
      String side2ElementId = side2ReferenceRelationship.getElementId();
      
      relationshipSideId_types.put(side1ElementId, side1MergeTypes);
      relationshipSideId_types.put(side2ElementId, side2MergeType);
      
      boolean isSide1applicable = side1ReferenceRelationship.getCardinality().equals(CommonConstants.CARDINALITY_MANY);
      boolean isSide2Applicable = side2ReferenceRelationship.getCardinality().equals(CommonConstants.CARDINALITY_MANY);
      
      if (sideId.equals(CommonConstants.RELATIONSHIP_SIDE_1)) {
        if (isSide1applicable) {
          relationshipId_side1Id.put(relationshipId, side1ElementId);
        }
        if (isSide2Applicable) {
          relationshipId_side2Id.put(relationshipId, side2ElementId);
        }
      }
      else {
        if (isSide1applicable) {
          relationshipId_side1Id.put(relationshipId, side1ElementId);
        }
        if (isSide2Applicable) {
          relationshipId_side2Id.put(relationshipId, side2ElementId);
        }
      }
    }
  }
  
  public Map<String, IIdAndCouplingTypeModel> getApplicableRelatioshipCoupling(
      Map<String, IReferencedNatureRelationshipInheritanceModel> referencedNatureRelationships, String relationshipId)
  {
    IReferencedNatureRelationshipInheritanceModel natureRelationshipInheritanceModel = referencedNatureRelationships.get(relationshipId);
    Map<String, IIdAndCouplingTypeModel> propagableRelationshipIdsCouplingType = natureRelationshipInheritanceModel
        .getPropagableRelationshipIdsCouplingType();
    return propagableRelationshipIdsCouplingType;
  }
  
  public Boolean isTypeMatch(Map<String, List<String>> source, String sourceId, Set<String> typeAndTaxonomyIds)
  {
    if (source.isEmpty()) {
      return false;
    }
    Boolean present = source.get(sourceId).stream().filter(targetklassId -> (typeAndTaxonomyIds.contains(targetklassId))).findFirst()
        .isPresent();
    return present;
    
  }
  
  public void adaptRelationAndConflict(Map<String, Object> applicableContentsPerRelationshipToInherit,
      Map<String, Object> applicableContentsPerRelationship, Long contentId,
      Map<String, Map<String, IRelationshipConflictingSource>> configuredRelSidesPerNatureRelSides, ILocaleCatalogDAO catalogDao,
      Map<String, IReferencedRelationshipInheritanceModel> referencedRelationship) throws Exception
  {
    for (Entry<String, Object> entry : applicableContentsPerRelationship.entrySet()) {
      pushConflictsInApplicableContents(contentId, configuredRelSidesPerNatureRelSides, referencedRelationship, entry, RelationSide.SIDE_1,
          catalogDao);
    }
    
    for (Entry<String, Object> entry : applicableContentsPerRelationshipToInherit.entrySet()) {
      pushConflictsInApplicableContents(contentId, configuredRelSidesPerNatureRelSides, referencedRelationship, entry, RelationSide.SIDE_2,
          catalogDao);
    }
  }
  
  @SuppressWarnings("unchecked")
  private void pushConflictsInApplicableContents(Long contentId,
      Map<String, Map<String, IRelationshipConflictingSource>> configuredRelSidesPerNatureRelSides,
      Map<String, IReferencedRelationshipInheritanceModel> referencedRelationship, Entry<String, Object> entry, RelationSide side,
      ILocaleCatalogDAO catalogDao) throws Exception
  {
    String nRelIdSide = entry.getKey();
    String[] split = nRelIdSide.split(Seperators.RELATIONSHIP_SIDE_SPERATOR);
    String natureRelId = split[0];
    Map<String, IRelationshipConflictingSource> allConfiguredRelsForNR = (Map<String, IRelationshipConflictingSource>) configuredRelSidesPerNatureRelSides
        .get(nRelIdSide);
    Map<String, List<String>> applicableContentsForNrel = (Map<String, List<String>>) entry.getValue();
    for (Map.Entry<String, List<String>> mapEntry : applicableContentsForNrel.entrySet()) {
      String relIdSideId = mapEntry.getKey();
      IRelationshipConflictingSource configuredRel = allConfiguredRelsForNR.get(relIdSideId);
      List<String> applicableContentIds = (List<String>) mapEntry.getValue();
      if (side.equals(RelationSide.SIDE_1)) {
        for (String applicableContentId : applicableContentIds) {
          IBaseEntityDTO entityDTO = catalogDao.getEntityByIID(Long.parseLong(applicableContentId));
          List<IContentRelationshipInstanceModel> ModifiedRelationships= resolveAndAdaptChanges(Long.parseLong(applicableContentId), referencedRelationship, configuredRel, contentId, natureRelId,
              catalogDao);
          adaptRelationOnRelationshipInheritance( entityDTO, catalogDao,  entityDTO.getBaseType(),ModifiedRelationships );
        }
      }
      else {
        for (String applicableContentId : applicableContentIds) {
          IBaseEntityDTO entityDTO = catalogDao.getEntityByIID(Long.parseLong(applicableContentId));
          List<IContentRelationshipInstanceModel> ModifiedRelationships = resolveAndAdaptChanges(contentId, referencedRelationship, configuredRel, Long.parseLong(applicableContentId), natureRelId,
              catalogDao);
          adaptRelationOnRelationshipInheritance( entityDTO, catalogDao,  entityDTO.getBaseType(),ModifiedRelationships );
        }
      }
    }
  }
  
  private List<IContentRelationshipInstanceModel> resolveAndAdaptChanges(Long targetEntityId,
      Map<String, IReferencedRelationshipInheritanceModel> referencedRelationship, IRelationshipConflictingSource configuredRel,
      Long sourceContentId, String natureRelId, ILocaleCatalogDAO catalogDao) throws Exception
  {
    IBaseEntityDTO entityDTO = catalogDao.getEntityByIID(targetEntityId);
    List<IContentRelationshipInstanceModel> modifiedRelationships = new ArrayList<>();
    List<IRelationCoupleRecordDTO> relationCoupleRecords = new ArrayList<>();
    IPropertyDTO relationProperty = ConfigurationDAO.instance().getPropertyByCode(configuredRel.getRelationshipId());
    IPropertyDTO natureRelationProperty = ConfigurationDAO.instance().getPropertyByCode(natureRelId);
    
    Set<IEntityRelationDTO> sourceRelationsList = getRelations(sourceContentId,
        referencedRelationship.get(configuredRel.getRelationshipId()), configuredRel.getRelationshipSideId(), catalogDao);
    Set<IEntityRelationDTO> targetRelationsList = getRelations(targetEntityId,
        referencedRelationship.get(configuredRel.getRelationshipId()), configuredRel.getRelationshipSideId(), catalogDao);
    
    IRelationCoupleRecordDAO relationCoupleRecordDao = catalogDao.openRelationCoupleRecordDAO();
    IRelationCoupleRecordDTO exsitingRelationCoupleRecord = new RelationCoupleRecordDTOBuilder().targetEntityId(targetEntityId)
        .propagableRelationshipId(relationProperty.getPropertyIID()).propagableRelationshipSideId(configuredRel.getRelationshipSideId())
        .build();
    StringBuilder filterQuery = relationCoupleRecordDao.getFilterQuery(exsitingRelationCoupleRecord);
    
    if (configuredRel.getCouplingType().equals(CommonConstants.DYNAMIC_COUPLED)) {
      
      BaseEntityDAO baseEntityDAO = (BaseEntityDAO) catalogDao.openBaseEntity(entityDTO);
      
      IContentRelationshipInstanceModel modifiedRelationship = new ContentRelationshipInstanceModel();
      
      modifiedRelationship.setRelationshipId(relationProperty.getCode());
      modifiedRelationship.setId(relationProperty.getPropertyCode());
      modifiedRelationship.setSideId(configuredRel.getRelationshipSideId());
      
      if(!sourceRelationsList.isEmpty()) {
        modifiedRelationship.setBaseType(BaseEntityUtils.getBaseTypeString(catalogDao.getEntityByIID( sourceRelationsList.iterator().next().getOtherSideEntityIID()).getBaseType()));
      } 
      
      addRelationOnDynamicCoupled(sourceRelationsList, targetRelationsList, baseEntityDAO, modifiedRelationship);
      
      removeRelationOnDynamicCoupled(sourceRelationsList, targetRelationsList, modifiedRelationship);
      
      modifyRelationOnDynamicCoupled(sourceRelationsList, targetRelationsList, baseEntityDAO, modifiedRelationship);
      
      if(!modifiedRelationship.getAddedElements().isEmpty() || !modifiedRelationship.getDeletedElements().isEmpty() || !modifiedRelationship.getModifiedElements().isEmpty()) {
        modifiedRelationships.add(modifiedRelationship);
      }
      
      IRelationCoupleRecordDTO relationCoupleRecord = new RelationCoupleRecordDTOBuilder().sourceEntityId(sourceContentId.longValue())
          .targetEntityId(targetEntityId).natureRelationshipId(natureRelationProperty.getPropertyIID())
          .propagableRelationshipId(relationProperty.getPropertyIID()).isResolved(true)
          .propagableRelationshipSideId(configuredRel.getRelationshipSideId())
          .couplingType(IRelationCoupleRecordDTO.CouplingType.valueOf(configuredRel.getCouplingType())).build();
      relationCoupleRecords.add(relationCoupleRecord);
      if (relationCoupleRecordDao.fetchRelationCoupleRecord(filterQuery).isEmpty()) {
        relationCoupleRecordDao.createRelationCoupleRecord(relationCoupleRecords.toArray(new IRelationCoupleRecordDTO[0]));
      }
      else {
        relationCoupleRecordDao.updateConflictResolvedStatus(relationCoupleRecord);
      }
    }
    else {
      if (relationCoupleRecordDao.fetchRelationCoupleRecord(filterQuery).isEmpty()) {
        prepareConflictAndInsertInCoupleTable(configuredRel.getCouplingType(),
            new ArrayList<String>(List.of(configuredRel.getRelationshipSideId())), relationProperty.getPropertyIID(),
            natureRelationProperty.getPropertyIID(), sourceContentId, targetEntityId, catalogDao);
      }
      else {
        prepareConflictAndUpdateInCoupleTable(configuredRel.getCouplingType(),
            new ArrayList<String>(List.of(configuredRel.getRelationshipSideId())), relationProperty.getPropertyIID(),
            natureRelationProperty.getPropertyIID(), sourceContentId, targetEntityId, catalogDao);
      }
    }
    return modifiedRelationships;
  }
  
  public void removeConflictFromRecordTable(Map<Long, List<String>> removeConflictsData, ILocaleCatalogDAO catalogDao)
      throws RDBMSException
  {
    IRelationCoupleRecordDAO relationCoupleRecordDao = catalogDao.openRelationCoupleRecordDAO();
    for (Map.Entry<Long, List<String>> removeConflictsMap : removeConflictsData.entrySet()) {
      for (String propagableRelationshipSideId : removeConflictsMap.getValue()) {
        StringBuilder filterQuery = new StringBuilder();
        IRelationCoupleRecordDTO relationCoupleRecord = new RelationCoupleRecordDTOBuilder().targetEntityId(removeConflictsMap.getKey())
            .propagableRelationshipSideId(propagableRelationshipSideId).build();
        filterQuery = relationCoupleRecordDao.getFilterQuery(relationCoupleRecord);
        relationCoupleRecordDao.deleteRelationCoupleRecord(filterQuery);
      }
      
    }
  }
  
  public IContentRelationshipInstanceModel adaptRelationInTargetEntity(IGetSideContentTypesByRelationshipModel modelForChild2Content, Long sourceContentId,
      Map<String, IReferencedNatureRelationshipInheritanceModel> referencedNatureRelationships,
      IReferencedRelationshipInheritanceModel relationship, String propagableRelationshipSideId, ILocaleCatalogDAO catalogDao)
      throws NumberFormatException, Exception
  {
    IBaseEntityDTO entityDTO = catalogDao.getEntityByIID(modelForChild2Content.getContentId());
    
    Set<IEntityRelationDTO> sourceRelationsList = getRelations(sourceContentId, relationship,
        propagableRelationshipSideId, catalogDao);
    Set<IEntityRelationDTO> targetRelationsList = getRelations(modelForChild2Content.getContentId(), relationship,
        propagableRelationshipSideId, catalogDao);
    
    IPropertyDTO relationProperty = ConfigurationDAO.instance().getPropertyByCode(relationship.getCode());
    
    BaseEntityDAO baseEntityDAO = (BaseEntityDAO) catalogDao.openBaseEntity(entityDTO);
    
    IContentRelationshipInstanceModel modifiedRelationship = new ContentRelationshipInstanceModel();
    
    modifiedRelationship.setRelationshipId(relationProperty.getCode());
    modifiedRelationship.setId(relationProperty.getPropertyCode());
    modifiedRelationship.setSideId(propagableRelationshipSideId);
    
    if(!sourceRelationsList.isEmpty()) {
      modifiedRelationship.setBaseType(BaseEntityUtils.getBaseTypeString(catalogDao.getEntityByIID( sourceRelationsList.iterator().next().getOtherSideEntityIID()).getBaseType()));
    } 
    
    addRelationOnDynamicCoupled(sourceRelationsList, targetRelationsList, baseEntityDAO, modifiedRelationship);
    
    removeRelationOnDynamicCoupled(sourceRelationsList, targetRelationsList, modifiedRelationship);
    
    modifyRelationOnDynamicCoupled(sourceRelationsList, targetRelationsList, baseEntityDAO, modifiedRelationship);
    
    return modifiedRelationship;
    
  }

  
}
