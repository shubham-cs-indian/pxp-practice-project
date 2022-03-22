package com.cs.core.runtime.interactor.version;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.usecase.versionrollback.IGetConfigDetailForVersionRollbackStrategy;
import com.cs.core.eventqueue.idto.IEventDTO.EventType;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.entity.dao.BaseEntityDAS;
import com.cs.core.rdbms.entity.dto.ProductDeleteDTO;
import com.cs.core.rdbms.entity.dto.RelationsSetDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityChildrenDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IEntityRelationDTO;
import com.cs.core.rdbms.entity.idto.IProductDeleteDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.revision.idao.IRevisionDAO;
import com.cs.core.rdbms.revision.idto.IObjectRevisionDTO;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.KlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.instance.IContentRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.model.languageinstance.DeleteTranslationResponseModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.ISaveRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.relationship.SaveRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.templating.GetKlassInstanceForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForVersionRollbackModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.versionrollback.IKlassInstanceVersionRollbackModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForOverviewTabStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RelationshipInstanceUtil;
import com.cs.core.runtime.interactor.utils.klassinstance.TranslationInstanceUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.VariantInstanceUtils;
import com.cs.core.runtime.restore.RollbackAndRestoreUtils;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.utils.BaseEntityUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

@Component
public abstract class AbstractRollbackInstanceVersion<P extends IKlassInstanceVersionRollbackModel, R extends IGetKlassInstanceModel>
    extends AbstractRuntimeInteractor<P, R> {
  
  @Autowired
  protected KlassInstanceUtils                         klassInstanceUtils;
  
  @Autowired
  protected PermissionUtils                            permissionUtils;
  
  @Autowired
  protected IGetConfigDetailsForOverviewTabStrategy    getConfigDetailsForOverviewTabStrategy;
  
  @Autowired
  protected RDBMSComponentUtils                        rdbmsComponentUtils;
  
  @Autowired
  RelationshipInstanceUtil                             relationshipInstanceUtil;
  
  @Autowired
  protected VariantInstanceUtils                       variantInstanceUtils;
  
  @Autowired
  protected IGetConfigDetailForVersionRollbackStrategy getConfigDetailsForVersionRollbackStrategy;
  
  @Autowired
  protected TranslationInstanceUtils                   translationInstanceUtils;

  @Autowired
  protected RollbackAndRestoreUtils                    rollbackAndRestoreUtils;
  
  @Override
  protected R executeInternal(P klassInstancesRollbackModel) throws Exception
  {
    long baseEntityIID = Long.parseLong(klassInstancesRollbackModel.getKlassInstanceId());
    IBaseEntityDAO currentBaseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(baseEntityIID);
    /*IBaseEntityDTO currentBaseEntityDTO = currentBaseEntityDAO.getBaseEntityDTO();*/

    Set<IPropertyDTO> properties = (Set<IPropertyDTO>) rdbmsComponentUtils.getLocaleCatlogDAO().getAllEntityProperties(baseEntityIID);
    IBaseEntityDTO currentBaseEntityDTO = currentBaseEntityDAO.loadAllPropertyRecords(properties.toArray(new IPropertyDTO[0]));
    
    IRevisionDAO revisionDAO = rdbmsComponentUtils.getRevisionDAO();

    Set<Integer> revisionNos = new HashSet<>();
    revisionNos.add(Integer.parseInt(klassInstancesRollbackModel.getVersionId()));
    List<IObjectRevisionDTO> revisions = revisionDAO.getRevisions(baseEntityIID, revisionNos);
    IBaseEntityDTO rollbackedBaseEntityDTO = revisions.get(0).getBaseEntityDTOArchive();
    
    IKlassInstanceTypeSwitchModel typeSwitchModel = restoreTypesAndTaxonomies(currentBaseEntityDTO, rollbackedBaseEntityDTO);
    
    IMulticlassificationRequestModel configDetailsRequestModel = rollbackAndRestoreUtils.fillMultiClassificationRequestModel(currentBaseEntityDAO);
    configDetailsRequestModel.getKlassIds().addAll(typeSwitchModel.getAddedSecondaryTypes());
    configDetailsRequestModel.getSelectedTaxonomyIds().addAll(typeSwitchModel.getAddedTaxonomyIds());
    IGetConfigDetailsForVersionRollbackModel configDetails = getConfigDetailsForVersionRollback(configDetailsRequestModel);

    restoreLocaleIds(currentBaseEntityDAO, currentBaseEntityDTO, rollbackedBaseEntityDTO);

    restoreAttributesAndTags(currentBaseEntityDTO, rollbackedBaseEntityDTO, configDetails);

    rollbackAndRestoreUtils.evaluateRules(currentBaseEntityDAO, configDetails);

    restoreVariants(baseEntityIID, currentBaseEntityDAO, rollbackedBaseEntityDTO);

    restoreRelationships(baseEntityIID, currentBaseEntityDTO, rollbackedBaseEntityDTO, configDetails);

    rdbmsComponentUtils.createNewRevision(currentBaseEntityDAO.getBaseEntityDTO(), configDetails.getNumberOfVersionsToMaintain());

    rdbmsComponentUtils.getLocaleCatlogDAO().postUsecaseUpdate(baseEntityIID, EventType.ELASTIC_UPDATE);
    
    IGetConfigDetailsForCustomTabModel getRequestConfigDetails = getConfigDetailsForOverviewTabStrategy.execute(configDetailsRequestModel);

    return (R) prepareDataForResponse(getRequestConfigDetails ,currentBaseEntityDAO);
  }

  protected IGetKlassInstanceModel prepareDataForResponse(IGetConfigDetailsModel configDetails, IBaseEntityDAO baseEntityDAO) throws Exception
  {
    IGetKlassInstanceModel returnModel = executeGetKlassInstance(configDetails, baseEntityDAO);
    returnModel.setVariantOfLabel(
        klassInstanceUtils.getVariantOfLabel(baseEntityDAO, ((IGetConfigDetailsForCustomTabModel) configDetails).getLinkedVariantCodes()));
    returnModel.setBranchOfLabel(KlassInstanceUtils.getBranchOfLabel(baseEntityDAO.getBaseEntityDTO(), rdbmsComponentUtils));
    permissionUtils.manageKlassInstancePermissions(returnModel);
    return returnModel;
  }

  protected IGetKlassInstanceModel executeGetKlassInstance(IGetConfigDetailsModel configDetails, IBaseEntityDAO baseEntityDAO) throws Exception
  {
    KlassInstanceBuilder klassInstanceBuilder = new KlassInstanceBuilder(baseEntityDAO, configDetails, this.rdbmsComponentUtils);
    IKlassInstance klassInstance = klassInstanceBuilder.getKlassInstance();
    IGetKlassInstanceModel model = new GetKlassInstanceForCustomTabModel();
        ((IGetKlassInstanceCustomTabModel) model).setAttributeVariantsStats(klassInstanceBuilder.getAttributeVariantsStats());
        if (!((IGetConfigDetailsForCustomTabModel) configDetails).getReferencedNatureRelationships().isEmpty()) {
          setNatureRelationship((IGetConfigDetailsForCustomTabModel) configDetails, (IGetKlassInstanceCustomTabModel) model);
        }
        relationshipInstanceUtil.executeGetRelationshipInstance((IGetKlassInstanceCustomTabModel) model,
            (IGetConfigDetailsForCustomTabModel) configDetails, baseEntityDAO, this.rdbmsComponentUtils);
    // on any tab we will get the context information
    model.setReferencedInstances(variantInstanceUtils.fillContextualData(configDetails, baseEntityDAO, klassInstance));

    model.setKlassInstance((IContentInstance) klassInstance);
    model.setConfigDetails(configDetails);
    return model;
  }

  public void setNatureRelationship(IGetConfigDetailsForCustomTabModel configDetails, IGetKlassInstanceCustomTabModel returnModel)
      throws RDBMSException, Exception
  {
    IKlassInstanceRelationshipInstance klassInstanceRelationshipInstance = new KlassInstanceRelationshipInstance();
    Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships = configDetails.getReferencedNatureRelationships();

    Set<Entry<String, IGetReferencedNatureRelationshipModel>> natureRelationshipsEntrySet = referencedNatureRelationships.entrySet();
    for (Entry<String, IGetReferencedNatureRelationshipModel> natureRelationshipEntrySet : natureRelationshipsEntrySet) {
      IGetReferencedNatureRelationshipModel natureRelationship = natureRelationshipEntrySet.getValue();
      klassInstanceRelationshipInstance.setRelationshipId(natureRelationship.getId());
      klassInstanceRelationshipInstance.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.RELATIONSHIP.getPrefix()));
      klassInstanceRelationshipInstance.setSideId(natureRelationship.getSide1().getElementId());
      returnModel.getNatureRelationships().add(klassInstanceRelationshipInstance);
    }
  }

  private void restoreLocaleIds(IBaseEntityDAO currentBaseEntityDAO, IBaseEntityDTO currentBaseEntityDTO,
      IBaseEntityDTO rollbackedBaseEntityDTO) throws Exception
  {
    List<String> currentLocaleIds = currentBaseEntityDTO.getLocaleIds();
    List<String> rollbackedLocaleIds = rollbackedBaseEntityDTO.getLocaleIds();
    long baseEntityIID = currentBaseEntityDAO.getBaseEntityDTO().getBaseEntityIID();

    List<String> addedLocaleIds = ListUtils.subtract(rollbackedLocaleIds, currentLocaleIds);
    List<String> removedLocaleIds = ListUtils.subtract(currentLocaleIds, rollbackedLocaleIds);
    
    rollbackAndRestoreUtils.handleAddedLocaleIds(currentBaseEntityDTO, baseEntityIID, addedLocaleIds);
    
    for (String localeIdToDelete : removedLocaleIds) {
      translationInstanceUtils.handleTranslationDelete(localeIdToDelete, new ArrayList<String>(), null, new DeleteTranslationResponseModel(), currentBaseEntityDTO);
     }
  }

  private void restoreVariants(long baseEntityIID, IBaseEntityDAO currentBaseEntityDAO, IBaseEntityDTO rollbackedBaseEntityDTO)
      throws RDBMSException, Exception, CSFormatException
  {
    List<Long> currentVariantIIDs = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
      List<Long> variantIids = Arrays.asList(ArrayUtils.toObject(new BaseEntityDAS( currentConn).getAllChildrens(baseEntityIID)));
      currentVariantIIDs.addAll(variantIids);
    });
    
    Set<IBaseEntityChildrenDTO> rollbackedVariantsInfo = rollbackedBaseEntityDTO.getChildren();
    List<Long> rollbackedVariantIIDs = new ArrayList<>();

    for(IBaseEntityChildrenDTO variantInfo : rollbackedVariantsInfo) {
      rollbackedVariantIIDs.add(variantInfo.getChildrenIID());
    }

    List<Long> addedVariants = ListUtils.subtract(rollbackedVariantIIDs, currentVariantIIDs);
    List<Long> removedVariants = ListUtils.subtract(currentVariantIIDs, rollbackedVariantIIDs);

    List<String> successfulIds = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    IProductDeleteDTO entryData = new ProductDeleteDTO();

    variantInstanceUtils.deleteVariants(removedVariants, successfulIds, failure, entryData);
  }

  private void restoreAttributesAndTags(IBaseEntityDTO currentBaseEntityDTO,
      IBaseEntityDTO rollbackBaseEntityDTO, IGetConfigDetailsForVersionRollbackModel configDetails) throws Exception
  {
    Set<IPropertyRecordDTO> currentPropertyRecords = currentBaseEntityDTO.getPropertyRecords();
    Set<IPropertyRecordDTO> rollbackpropertyRecords = rollbackBaseEntityDTO.getPropertyRecords();
    Map<String, IReferencedSectionElementModel> referencedElements = configDetails.getReferencedElements();
    Map<String, IAttribute> referencedAttributes = configDetails.getReferencedAttributes();
    List<String> localeIdsToEvaluate = currentBaseEntityDTO.getLocaleIds();

    Map<String, Object> currentPropertyRecordsMap = new HashMap<>();
    Map<String, Object> rollbackPropertyRecordsMap = new HashMap<>();
    Set<String> propertyCodes = new HashSet<>();

    List<IPropertyRecordDTO> propertyRecordsToBeDeleted = new ArrayList<>();
    List<IPropertyRecordDTO> propertyRecordsToBeCreated = new ArrayList<>();
    List<IPropertyRecordDTO> propertyRecordsToBeUpdated = new ArrayList<>();
    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(currentBaseEntityDTO.getBaseEntityIID());

    rollbackAndRestoreUtils.fillPropertyRecordsInfo(currentPropertyRecords, referencedElements, localeIdsToEvaluate,
        currentPropertyRecordsMap, propertyCodes);

    rollbackAndRestoreUtils.fillPropertyRecordsInfo(rollbackpropertyRecords, referencedElements, localeIdsToEvaluate,
        rollbackPropertyRecordsMap, propertyCodes);

    for (String propertyCode : propertyCodes) {
      if (rollbackPropertyRecordsMap.get(propertyCode) instanceof IPropertyRecordDTO
          || currentPropertyRecordsMap.get(propertyCode) instanceof IPropertyRecordDTO) {
        rollbackAndRestoreUtils.fillPropertyRecords((IPropertyRecordDTO) currentPropertyRecordsMap.get(propertyCode),
            (IPropertyRecordDTO) rollbackPropertyRecordsMap.get(propertyCode), propertyRecordsToBeDeleted, propertyRecordsToBeCreated,
            propertyRecordsToBeUpdated);
      }
      else {
        rollbackAndRestoreUtils.fillpropertyRecordsFromMap(currentPropertyRecordsMap, rollbackPropertyRecordsMap, propertyRecordsToBeDeleted,
            propertyRecordsToBeCreated, propertyRecordsToBeUpdated, propertyCode, localeIdsToEvaluate);
      }
    }
    baseEntityDAO.updatePropertyRecords(propertyRecordsToBeUpdated.toArray(new IPropertyRecordDTO[0]));
    baseEntityDAO.deletePropertyRecords(propertyRecordsToBeDeleted.toArray(new IPropertyRecordDTO[0]));
    baseEntityDAO.createPropertyRecords(propertyRecordsToBeCreated.toArray(new IPropertyRecordDTO[0]));
  }

  private IKlassInstanceTypeSwitchModel restoreTypesAndTaxonomies(IBaseEntityDTO currentBaseEntityDTO, IBaseEntityDTO rollbackBaseEntityDTO)
      throws Exception
  {
    List<IClassifierDTO> eligibleRollbackedClassifiers = rollbackAndRestoreUtils.getClassifiersFromDTO(rollbackBaseEntityDTO);
    List<IClassifierDTO> currentClassifiers = rollbackAndRestoreUtils.getClassifiersFromDTO(currentBaseEntityDTO);
    
    List<IClassifierDTO> addedClassifiers = ListUtils.subtract(eligibleRollbackedClassifiers, currentClassifiers);
    List<IClassifierDTO> removedClassifiers = ListUtils.subtract(currentClassifiers, eligibleRollbackedClassifiers);
    
    return rollbackAndRestoreUtils.fillClassifiersAndCallTypeSwitchRequest(currentBaseEntityDTO, addedClassifiers, removedClassifiers);
  }

  private void restoreRelationships(Long baseEntityIID, IBaseEntityDTO currentBaseEntityDTO, IBaseEntityDTO rollbackedBaseEntityDTO, IGetConfigDetailsForVersionRollbackModel configDetails) throws Exception
  {

    BaseType baseType = currentBaseEntityDTO.getBaseType();
    ISaveRelationshipInstanceModel saveRelationshipModel = new SaveRelationshipInstanceModel();
    saveRelationshipModel.setId(baseEntityIID.toString());
    saveRelationshipModel.setBaseType(BaseEntityUtils.getBaseTypeString(baseType));
    saveRelationshipModel.setTabType(CommonConstants.CUSTOM_TEMPLATE_TAB_BASETYPE);
    saveRelationshipModel.setTabId(SystemLevelIds.RELATIONSHIP_TAB);

    prepareADMForRelationships(currentBaseEntityDTO, rollbackedBaseEntityDTO, saveRelationshipModel, configDetails);
    
    if(!saveRelationshipModel.getModifiedNatureRelationships().isEmpty()|| !saveRelationshipModel.getModifiedRelationships().isEmpty()) {
      rollbackAndRestoreUtils.executeSaveRelation(saveRelationshipModel, baseType);
    }
  }

  protected IGetConfigDetailsForVersionRollbackModel getConfigDetailsForVersionRollback(
      IMulticlassificationRequestModel configDetailsRequestModel) throws Exception
  {
    return getConfigDetailsForVersionRollbackStrategy.execute(configDetailsRequestModel);
  }

  @SuppressWarnings("unchecked")
  private void prepareADMForRelationships(IBaseEntityDTO baseEntityDTO, IBaseEntityDTO rollbackBaseEntityDTO,
      ISaveRelationshipInstanceModel saveRelationshipModel, IGetConfigDetailsForVersionRollbackModel configDetails) throws JsonProcessingException, Exception
  {
    List<IPropertyRecordDTO> rolledBackRelationSetDTOs = rollbackBaseEntityDTO.getPropertyRecords().stream()
        .filter(property -> property instanceof RelationsSetDTO).collect(Collectors.toList());
    List<IPropertyRecordDTO> currentRelationSetDTOs = baseEntityDTO.getPropertyRecords().stream()
        .filter(property -> property instanceof RelationsSetDTO).collect(Collectors.toList());

    Set<String> natureRelationIds = new HashSet<String>();

    Map<String, Object> relationIdVsRelationsMap = new HashMap<String, Object>();
    Map<String, Object> relationIdVsSideIdMap = new HashMap<String, Object>();
    rollbackAndRestoreUtils.fillRelationsAndSideIdMap(rolledBackRelationSetDTOs, relationIdVsRelationsMap, natureRelationIds, relationIdVsSideIdMap);

    Map<String, Object> currentRelationshipIdVsRelationsMap = new HashMap<String, Object>();
    Map<String, Object> currentRelationshipIdVsSideIdMap = new HashMap<String, Object>();
    rollbackAndRestoreUtils.fillRelationsAndSideIdMap(currentRelationSetDTOs, currentRelationshipIdVsRelationsMap, natureRelationIds,
        currentRelationshipIdVsSideIdMap);

    Map<String, IReferencedRelationshipModel> referencedRelationships = configDetails.getReferencedRelationships();
    Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships = configDetails.getReferencedNatureRelationships();
    Map<String, IReferencedSectionElementModel> referencedElements = configDetails.getReferencedElements();
    Map<String, ITag> referencedTags = configDetails.getReferencedTags();

    List<IContentRelationshipInstanceModel> modifiedRelationships = new ArrayList<>();
    List<IContentRelationshipInstanceModel> modifiedNatureRelationships = new ArrayList<>();

    for (Entry<String, Object> mapEntry : relationIdVsRelationsMap.entrySet()) {

      String relationId = mapEntry.getKey();
      Set<IEntityRelationDTO> relations = (Set<IEntityRelationDTO>) mapEntry.getValue();
      Set<IEntityRelationDTO> currentRelations = (Set<IEntityRelationDTO>) currentRelationshipIdVsRelationsMap.get(relationId);

      IContentRelationshipInstanceModel relationship = rollbackAndRestoreUtils.getRelationshipInstanceModel(natureRelationIds, relationIdVsSideIdMap,
          referencedRelationships, referencedNatureRelationships, referencedElements, relationId, relations);

      /*when relationship instance is not present for current version then add
      all the relations in addedElements*/
      if (currentRelationshipIdVsRelationsMap.get(relationId) == null) {
        for (IEntityRelationDTO relation : relations) {
          if (rdbmsComponentUtils.getBaseEntityDTO(relation.getOtherSideEntityIID()) != null) {
            relationship.getAddedElements().add(rollbackAndRestoreUtils.getRelationshipElement(relation, referencedTags));
          }
        }
      }
      else {
        List<Long> otherSideEntityIIDs = new ArrayList<Long>();
        List<Long> currentRelationsOtherSideEntityIIDs = new ArrayList<Long>();

        otherSideEntityIIDs.addAll(relations.stream().map(x -> x.getOtherSideEntityIID()).collect(Collectors.toList()));
        currentRelationsOtherSideEntityIIDs
            .addAll(currentRelations.stream().map(x -> x.getOtherSideEntityIID()).collect(Collectors.toList()));

        List<Long> relationsToAdd = ListUtils.subtract(otherSideEntityIIDs, currentRelationsOtherSideEntityIIDs);
        List<Long> relationsToDelete = ListUtils.subtract(currentRelationsOtherSideEntityIIDs, otherSideEntityIIDs);

        for (IEntityRelationDTO entityRelationsDTO : relations) {
          if (relationsToAdd.contains(entityRelationsDTO.getOtherSideEntityIID())
              && rdbmsComponentUtils.getBaseEntityDTO(entityRelationsDTO.getOtherSideEntityIID()) != null) {
            relationship.getAddedElements().add(rollbackAndRestoreUtils.getRelationshipElement(entityRelationsDTO, referencedTags));
          }
        }

        relationship.getDeletedElements().addAll(relationsToDelete.stream().map(x -> x.toString()).collect(Collectors.toList()));

      }
      if (natureRelationIds.contains(relationId))
        modifiedNatureRelationships.add(relationship);
      else
        modifiedRelationships.add(relationship);
    }

    for (Entry<String, Object> mapEntry : currentRelationshipIdVsRelationsMap.entrySet()) {
      String relationshipId = mapEntry.getKey();

      /*when a current version relationship instance is not present in the version to rollback then all relations
       of that relationship is to be deleted*/
      if (relationIdVsRelationsMap.get(relationshipId) == null) {

        Set<IEntityRelationDTO> relations = (Set<IEntityRelationDTO>) mapEntry.getValue();

        IContentRelationshipInstanceModel relationship = rollbackAndRestoreUtils.getRelationshipInstanceModel(natureRelationIds, currentRelationshipIdVsSideIdMap,
            referencedRelationships, referencedNatureRelationships, referencedElements, relationshipId, relations);

        List<String> relationsToDelete = relations.stream().map(x -> String.valueOf(x.getOtherSideEntityIID()))
            .collect(Collectors.toList());
        relationship.setDeletedElements(relationsToDelete);

        if (natureRelationIds.contains(relationshipId))
          modifiedNatureRelationships.add(relationship);
        else
          modifiedRelationships.add(relationship);
      }
    }

    saveRelationshipModel.setModifiedRelationships(modifiedRelationships);
    saveRelationshipModel.setModifiedNatureRelationships(modifiedNatureRelationships);

  }

  private void evaluateTags(IPropertyRecordDTO propertyRecord, IReferencedSectionElementModel referencedElement, String propertyCode,
      Map<String, Object> propertyRecords, Set<String> propertyCodes)
  {
    if (!referencedElement.getIsSkipped()) {
      propertyRecords.put(propertyCode, propertyRecord);
      propertyCodes.add(propertyCode);
    }
  }

}