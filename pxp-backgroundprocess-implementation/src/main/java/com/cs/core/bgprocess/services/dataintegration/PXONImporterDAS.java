package com.cs.core.bgprocess.services.dataintegration;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.dto.BGPLog;
import com.cs.core.bgprocess.services.dataintegration.PXONImporterBlocksMap.ImportBlockInfo;
import com.cs.core.bgprocess.services.dataintegration.PXONImporterBlocksMap.ImportBlockStatus;
import com.cs.core.bgprocess.services.datarules.DataRuleUtil;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.propertycollection.ISectionAttribute;
import com.cs.core.config.interactor.entity.propertycollection.ISectionTag;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.config.strategy.usecase.klass.GetConfigDetailsWithoutPermissionsStrategy;
import com.cs.core.dataintegration.idto.IPXONImporterPlanDTO;
import com.cs.core.pxon.parser.PXONFileParser;
import com.cs.core.rdbms.config.dto.CatalogDTO;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dao.BaseEntityDAO;
import com.cs.core.rdbms.entity.dto.*;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.*;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.EmbeddedType;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.rdbms.tracking.dto.UserSessionDTO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.instancetree.GoldenRecordUtils;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRelationshipVersion;
import com.cs.core.runtime.interactor.entity.relationship.RelationshipVersion;
import com.cs.core.runtime.interactor.model.assetinstance.AssetInstanceSaveModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInstanceSaveModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForBulkPropagationResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.instance.ContentRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.instance.IContentRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipDataTransferInfoModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsWithoutPermissionsStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.PropertyRecordBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.PropertyRecordBuilder.PropertyRecordType;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.VariantInstanceUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.util.ConfigUtil;
import com.cs.core.util.DataTransferBGPModelBuilder;
import com.cs.di.runtime.business.process.utils.TriggerBusinessProcessWFUtils;
import com.cs.di.workflow.trigger.standard.IBusinessProcessTriggerModel.BusinessProcessActionType;
import com.cs.di.workflow.trigger.standard.IBusinessProcessTriggerModel.Usecase;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class PXONImporterDAS {
  
  public IUserSessionDTO                  userSession;

  final static DataTransferBGPModelBuilder dataTransferModelBuilder = BGProcessApplication.getApplicationContext().getBean(
      DataTransferBGPModelBuilder.class);

  public PXONImporterDAS(IUserSessionDTO userSession) {
    this.userSession = userSession;
  }

  static ConfigUtil configUtil = BGProcessApplication.getApplicationContext().getBean(ConfigUtil.class);

  static IGetConfigDetailsWithoutPermissionsStrategy getConfigDetails = BGProcessApplication.getApplicationContext().getBean(
      GetConfigDetailsWithoutPermissionsStrategy.class);

  static RDBMSComponentUtils rdbmsComponentUtils = BGProcessApplication.getApplicationContext().getBean(RDBMSComponentUtils.class);

  static GoldenRecordUtils goldenRecordUtils = BGProcessApplication.getApplicationContext().getBean(GoldenRecordUtils.class);

  public void createORUpdateBaseEntity(PXONFileParser pxonFileParser, BaseEntityDTO baseEntity, ImportBlockInfo baseEntityBlock,
      Map<ImportBlockIdentifier, ImportBlockInfo> baseEntityMetaList, BGPLog bgpLog, IPXONImporterPlanDTO importerPlanDTO) throws Exception
  {
    //entity import request is for create
    BusinessProcessActionType action = BusinessProcessActionType.AFTER_CREATE ; 
    Set<IPropertyRecordDTO> attributeTagRelationRecords = new HashSet<>();
    RDBMSLogger.instance().info("Processing start on baseEntityID %S", baseEntity.getBaseEntityID() );
    bgpLog.progress("Processing start on baseEntityID %S %d", baseEntity.getBaseEntityID(), System.currentTimeMillis());
    LocaleCatalogDAO localeCatalogDao = getLocaleCatalogDAO(baseEntity);
    userSession.setTransactionId(UUID.randomUUID().toString());
    
    String targetOrgCode = importerPlanDTO.getOrganizationCode().equals(IStandardConfig.STANDARD_ORGANIZATION_CODE)
        ? IStandardConfig.STANDARD_ORGANIZATION_RCODE : importerPlanDTO.getOrganizationCode();
    long entityIID = localeCatalogDao.getEntityIID(baseEntity.getBaseEntityID(), importerPlanDTO.getCatalogCode(), targetOrgCode,
        baseEntity.getEndpointCode());
    IBaseEntityDTO sourceEntityDTO = null;
    if(entityIID != -1)
    {
      sourceEntityDTO = localeCatalogDao.getBaseEntityDTOByIID(entityIID);
      baseEntity.setIID(sourceEntityDTO.getBaseEntityIID());
      baseEntity.setDefaultImageIID(sourceEntityDTO.getDefaultImageIID());
    //entity import request is for update
      action = BusinessProcessActionType.AFTER_SAVE;
    }
    BaseEntityDAO baseEntityDAO = getBaseEntityDAO(baseEntity, localeCatalogDao);
    Set<IPropertyRecordDTO> targetPropertyRecords = new HashSet<>();
    Set<RelationsSetDTO> relationSetRecords = new HashSet<>();

    List<String> propertyRecordCodes = separatePropertyRecords(baseEntity, targetPropertyRecords, relationSetRecords);
    Set<IClassifierDTO> otherClassifiers = baseEntity.getOtherClassifiers();
    List<IClassifierDTO> addedRemovedClassifier = new ArrayList<>();
    ImportBlockStatus checkStatus = baseEntityBlock.getStatus();

    IMulticlassificationRequestModel configRequestModelForImport = configUtil.getConfigRequestModelForImport(
        localeCatalogDao.getLocaleInheritanceSchema(), baseEntityDAO);

    IGetConfigDetailsForCustomTabModel configDetails = getConfigDetails.execute(configRequestModelForImport);
    List<Long> dependentPropertyIIDs = dataTransferModelBuilder.fetchDependentPropertyIIDs(configDetails);
    
    if (checkStatus == ImportBlockStatus.PENDING) {
      // Assumption if BE have IID that indicates it is already been created
      Boolean isDuplicateContextAllowed = getIsDuplicateContextAllowed(baseEntity, configDetails);
      if (sourceEntityDTO == null) {
        // as entity not created so there is no value records and tags record no
        // need to check same for Classifier
        List<IPropertyRecordDTO> additionalPropertyRecords = createPropertyRecordInstance(baseEntityDAO,
            configDetails, propertyRecordCodes);
        targetPropertyRecords.addAll(additionalPropertyRecords);
        
        Set<IPropertyRecordDTO> readOnlyProperties = getReadOnlyProperties(targetPropertyRecords, configDetails);
        
        for (IPropertyRecordDTO record : readOnlyProperties) {
          String propertyCode = record.getProperty().getCode();
          if (record instanceof ValueRecordDTO) {
            ISectionAttribute refrencedAttribut = (ISectionAttribute) configDetails.getReferencedElements().get(propertyCode);
            String defaultValue = refrencedAttribut.getDefaultValue();
            ((ValueRecordDTO) record).setValue(defaultValue);
            if (record.getProperty().getPropertyType().name() == "HTML")
              ((ValueRecordDTO) record).setAsHTML(defaultValue);
          }
          else if (record instanceof TagsRecordDTO) {
            ISectionTag refrencedTag = (ISectionTag) configDetails.getReferencedElements().get(propertyCode);
            List<TagDTO> tagList = new ArrayList<TagDTO>();
            for (IIdRelevance value : refrencedTag.getDefaultValue()) {
              TagDTO tag = new TagDTO();
              tag.setTagValueCode(value.getTagId());
              tag.setRange(100);
              tagList.add(tag);
            }
            ((TagsRecordDTO) record).setTags((tagList.toArray(new ITagDTO[tagList.size()])));
          }
        }
        baseEntityDAO.createPropertyRecords(targetPropertyRecords.toArray(new IPropertyRecordDTO[0]));
        baseEntityBlock.setStatus( ImportBlockStatus.IN_PROGRESS);
        baseEntityDAO.addClassifiers(otherClassifiers.toArray(new IClassifierDTO[0]));
        if(!otherClassifiers.isEmpty()){
          //TODO : for Create scenario RelationshipInheritanceSwitchType use case
          //Prepare Model & Initiate Relationship Inheritance Switch Type Event using 
          /*dataTransferModelBuilder.prepareDataForRelationshipInheritanceSwitchType(baseEntityDAO,
              otherClassifiers.stream().collect(Collectors.toList()), List.of());*/
          //Prepare Model & Initiate ClassificationDataTransfer
          dataTransferModelBuilder.initiateClassificationDataTransfer(baseEntityDAO.getLocaleCatalog(),
              baseEntityDAO.getBaseEntityDTO().getBaseEntityIID(),otherClassifiers, Set.of());
          if (!dependentPropertyIIDs.isEmpty()) {
            dataTransferModelBuilder.initiateLanguageInheritanceDataTransfer(localeCatalogDao,
                baseEntity.getBaseEntityIID(), dependentPropertyIIDs, true);
          }
        }
        // add children into baseEntity
        if (!StringUtils.isEmpty(baseEntity.getParentID())) {
          long parentEntityIID = localeCatalogDao.getEntityIID(baseEntity.getParentID(), importerPlanDTO.getCatalogCode(), targetOrgCode,
              baseEntity.getEndpointCode());
          if (parentEntityIID != -1) {
            addChildren(pxonFileParser, baseEntity, baseEntityMetaList, localeCatalogDao, bgpLog, importerPlanDTO,
                isDuplicateContextAllowed, parentEntityIID);
          }
        }
        
        // add Relation Entity
        Set<IRelationsSetDTO> relations = processRelationship(configDetails, pxonFileParser, baseEntityMetaList, localeCatalogDao, baseEntityDAO,
            relationSetRecords, baseEntity, bgpLog, importerPlanDTO);

        List<IContentRelationshipInstanceModel> modifiedRelationships = new ArrayList<>();
        List<IContentRelationshipInstanceModel > modifiedNatureRelationships = new ArrayList<>();
        fillCreatedRelationships(configDetails, relations, modifiedRelationships, modifiedNatureRelationships);
        if(!modifiedRelationships.isEmpty() || !modifiedNatureRelationships.isEmpty()){
          dataTransferModelBuilder.prepareDataForTaxonomyInheritance(modifiedNatureRelationships,  baseEntityDAO.getBaseEntityDTO().getBaseEntityIID(), configDetails);
          dataTransferModelBuilder.prepareDataForRelationshipDataTransfer(baseEntityDAO, modifiedRelationships, modifiedNatureRelationships);
        }
        
        IKlassInstanceInformationModel klassInstanceInformationModel = dataTransferModelBuilder
            .fillklassInstanceInformationModel(baseEntity);
        IReferencedKlassDetailStrategyModel referencedKlass = configDetails.getReferencedKlasses()
            .get(klassInstanceInformationModel.getTypes()
                .get(0));
        String newInstanceName = referencedKlass.getLabel() + dataTransferModelBuilder.getCounter();
        
        List<ITechnicalImageVariantWithAutoCreateEnableModel> contextsWithAutoCreateEnableModel = configDetails
            .getTechnicalImageVariantContextWithAutoCreateEnable();
        VariantInstanceUtils.createEmbeddedVariantsWithAutoCreateEnabled(newInstanceName,
            baseEntity, klassInstanceInformationModel, contextsWithAutoCreateEnableModel,
            dataTransferModelBuilder.getTransactionThread(), localeCatalogDao.getUserSessionDTO()
                .getUserName());
        if (baseEntityDAO.getBaseEntityDTO().getBaseType().name().equals( BaseType.ASSET.name())) {
          IAssetInstanceSaveModel klassInstancesModel = new AssetInstanceSaveModel();
          klassInstancesModel.setId(klassInstanceInformationModel.getId());
          try {
            klassInstancesModel.setThumbnailPath(klassInstanceInformationModel.getAssetInformation()
                .getThumbnailPath());
              klassInstancesModel.setFilePath(klassInstanceInformationModel.getAssetInformation()
                .getFilePath());
              IGetKlassInstanceModel response = null;
              dataTransferModelBuilder.prepareDataAndCreateImageVariants(configDetails,
                klassInstanceInformationModel.getAssetInformation(), klassInstancesModel,
                klassInstanceInformationModel.getOriginalInstanceId(), response, baseEntity.getEndpointCode());
          }catch (Exception e) {
            RDBMSLogger.instance().info("Asset Information Not Present");
          }
      }
    }

      else {
        baseEntityBlock.setStatus( ImportBlockStatus.IN_PROGRESS);
        
         if (StringUtils.isNotEmpty(baseEntity.getHashCode()) || baseEntity.getDefaultImageIID() != 0
            || baseEntity.getEntityExtension().isEmpty()) {
          baseEntity.setChanged(true);
        }
         
        if (baseEntity.getBaseEntityIID() == 0L)
          baseEntity.setIID(sourceEntityDTO.getBaseEntityIID());

        //To create translation
        createTranslation(baseEntity, localeCatalogDao, baseEntityDAO);
        
        //for language inheritance
        if (!dependentPropertyIIDs.isEmpty()) {
          dataTransferModelBuilder.initiateLanguageInheritanceDataTransfer(localeCatalogDao, baseEntity.getBaseEntityIID(), dependentPropertyIIDs,false);
        }  
        // TODO: fetch all properties from config details not from rdbms component.
        Collection<IPropertyDTO> allEntityProperties = localeCatalogDao.getAllEntityProperties(baseEntity.getBaseEntityIID());
        Set<IPropertyDTO> propertySet = new HashSet<>(allEntityProperties);
        
        baseEntityDAO.getBaseEntityDTO().getContextualObject().setChanged(true);

        // added for PXP-F-DEV-19302 : AFTER PROPERTY SAVE scenario
        TriggerBusinessProcessWFUtils.fetchPropertyRecordsToModifyCreate(attributeTagRelationRecords, baseEntityDAO, targetPropertyRecords,
            propertySet);
        
        //Added to handle read-only and dynamically coupled Attributes and Tags 
        Set<IPropertyRecordDTO> readOnlyProperties = getReadOnlyProperties(attributeTagRelationRecords, configDetails);
        Set<IPropertyDTO> propertySetToIgnore = new HashSet<>();
        for (IPropertyRecordDTO propertyRecord : readOnlyProperties) {
          propertySetToIgnore.add(propertyRecord.getProperty());
        }
        
        createORUpdatePropertyRecords(baseEntityDAO, targetPropertyRecords, propertySet, propertySetToIgnore);
        
        // Add or remove classifier
        addOrRemoveClassifier(baseEntityDAO, otherClassifiers,addedRemovedClassifier,configDetails,dependentPropertyIIDs);
        
        // add children into baseEntity
        //TODO Pass parentIId 
        /*if (StringUtils.isNotEmpty(baseEntity.getParentID())) {
          addChildren(pxonFileParser, baseEntity, baseEntityMetaList, localeCatalogDao, bgpLog, importerPlanDTO, 
              isDuplicateContextAllowed);
        }*/
        List<IContentRelationshipInstanceModel> modifiedRelationships = new ArrayList<>();
        List<IContentRelationshipInstanceModel> modifiedNatureRelationships = new ArrayList<>();
        //Separate record which are need to update and which need to create
        Set<RelationsSetDTO> createRecords = new HashSet<>();
        Set<RelationsSetDTO> modifyRecords = new HashSet<>();
        getUpdateAndCreatedRecord(relationSetRecords, propertySet, createRecords, modifyRecords);

        Set<IRelationsSetDTO> modifiedRelationRecord = new HashSet<>();
        processModifiedRelation(pxonFileParser, baseEntityMetaList, localeCatalogDao, modifyRecords, modifiedRelationRecord,baseEntity, bgpLog, importerPlanDTO);
        fillUpdatedRelationships(configDetails, attributeTagRelationRecords, modifiedRelationRecord, modifiedRelationships, modifiedNatureRelationships);
        baseEntityDAO.updatePropertyRecords(modifiedRelationRecord.toArray(new IRelationsSetDTO[0]));

        Set<IRelationsSetDTO> createdRelationRecord = processRelationship(configDetails, pxonFileParser, baseEntityMetaList, localeCatalogDao, baseEntityDAO,
            createRecords, baseEntity, bgpLog, importerPlanDTO);
        fillCreatedRelationships(configDetails, createdRelationRecord, modifiedRelationships, modifiedNatureRelationships);
        if (!modifiedRelationships.isEmpty() || !modifiedNatureRelationships.isEmpty()) {
          dataTransferModelBuilder.prepareDataForTaxonomyInheritance(modifiedNatureRelationships,
              baseEntity.getBaseEntityIID(), configDetails);
          dataTransferModelBuilder.prepareDataForRelationshipDataTransfer(baseEntityDAO,
              modifiedRelationships, modifiedNatureRelationships);
        }
        //added for PXP-F-DEV-19302 : AFTER RELATIONSHIP SAVE scenario
        attributeTagRelationRecords.addAll(modifiedRelationRecord);
        attributeTagRelationRecords.addAll(createdRelationRecord);
      }
      new TriggerBusinessProcessWFUtils().triggerBusinessProcessWF(baseEntity, action,userSession,attributeTagRelationRecords,
          addedRemovedClassifier, otherClassifiers.stream().collect(Collectors.toList()), Usecase.IMPORT);
    }
    // Prepare and initiate data rule handling
    Map<String, Object> requestModel = DataRuleUtil.prepareConfigBulkPropagtionRequestModel(baseEntity, IStandardConfig.StandardUser.admin.toString());
    IConfigDetailsForBulkPropagationResponseModel configDetailsforBulkPropagation = DataRuleUtil.getConfigDetailsForBulkPropagtion(requestModel, baseEntity.getBaseLocaleID());
    DataRuleUtil.propagateRule(baseEntityDAO, configDetailsforBulkPropagation);
    
    rdbmsComponentUtils.createNewRevision(baseEntity, configDetails.getNumberOfVersionsToMaintain());

    goldenRecordUtils.initiateEvaluateGoldenRecordBucket(baseEntityDAO.getBaseEntityDTO());
    localeCatalogDao.postUsecaseUpdate(baseEntity);
    baseEntityBlock.setStatus( ImportBlockStatus.DONE);
    bgpLog.info("Processing done for baseEntityID %S", baseEntity.getBaseEntityID());
    RDBMSLogger.instance().info("Processing done for baseEntityID %S", baseEntity.getBaseEntityID());
 }

  private Set<IPropertyRecordDTO> getReadOnlyProperties(Set<IPropertyRecordDTO> attributeTagRelationRecords,
      IGetConfigDetailsForCustomTabModel configDetails)
  {
    Set<IPropertyRecordDTO> propertySetToIgnore = new HashSet<IPropertyRecordDTO>();
    
    Map<String, IAttribute> refAttribute = configDetails.getReferencedAttributes();
    Map<String, IReferencedSectionElementModel> refElements = configDetails.getReferencedElements();
    for (IPropertyRecordDTO record : attributeTagRelationRecords) {
      IReferencedSectionElementModel refElement = refElements.get(record.getProperty().getCode());
      if (refElement != null) {
        String couplingType = refElement.getCouplingType();
        if (record instanceof ValueRecordDTO) {
          IAttribute property = refAttribute.get(record.getProperty().getCode());
          if ((property.getIsDisabled() != null && property.getIsDisabled()) || couplingType.equals(CommonConstants.DYNAMIC_COUPLED)) {
            // Properties that are read-only or dynamically coupled are added to this Set
            propertySetToIgnore.add(record);
          }
        }
        else if (record instanceof TagsRecordDTO) {
          if (couplingType.equals(CommonConstants.DYNAMIC_COUPLED)) {
            propertySetToIgnore.add(record);
          }
        }
      }
      
    }
    return propertySetToIgnore;
  }

  private Boolean getIsDuplicateContextAllowed(BaseEntityDTO baseEntity,
      IGetConfigDetailsForCustomTabModel configDetails)
  {
    Map<String, IReferencedVariantContextModel> variantContexts = configDetails.getReferencedVariantContexts().getEmbeddedVariantContexts();
    String contextCode = baseEntity.getContextualObject().getContextCode();
    if (variantContexts.get(contextCode) != null)
      return variantContexts.get(contextCode).getIsDuplicateVariantAllowed();
    return false;
  }

  private void fillUpdatedRelationships(IGetConfigDetailsForCustomTabModel configDetails, Set<IPropertyRecordDTO> propertyRecordDTOs,
      Set<IRelationsSetDTO> modifiedRelationRecords, List<IContentRelationshipInstanceModel> modifiedRelationships,
      List<IContentRelationshipInstanceModel> modifiedNatureRelationships)
  {
    Map<String, IRelationsSetDTO> currentRelationRecords = new HashMap<>();
    for (IPropertyRecordDTO propertyRecordDTO : propertyRecordDTOs) {
      if (propertyRecordDTO instanceof IRelationsSetDTO) {
        currentRelationRecords.put(propertyRecordDTO.getProperty().getPropertyCode(), (IRelationsSetDTO) propertyRecordDTO);
      }
    }

    Map<String, IRelationsSetDTO> modifiedRelationRecordsMap = new HashMap<>();
    for (IPropertyRecordDTO propertyRecordDTO : modifiedRelationRecords) {
      if (propertyRecordDTO instanceof IRelationsSetDTO) {
        modifiedRelationRecordsMap.put(propertyRecordDTO.getProperty().getPropertyCode(), (IRelationsSetDTO) propertyRecordDTO);
      }
    }

    for (IRelationsSetDTO relationshipRecord : currentRelationRecords.values()) {
      IContentRelationshipInstanceModel model = new ContentRelationshipInstanceModel();

      String propertyCode = relationshipRecord.getProperty().getPropertyCode();
      model.setRelationshipId(propertyCode);
      IReferencedRelationshipModel referencedRelationship = configDetails.getReferencedRelationships().get(propertyCode);
      if (referencedRelationship == null) {
        referencedRelationship = configDetails.getReferencedNatureRelationships().get(propertyCode);
      }
      IRelationshipSide relationshipSide = relationshipRecord.getSide().equals(IPropertyDTO.RelationSide.SIDE_1) ?
          referencedRelationship.getSide1() : referencedRelationship.getSide2();
      model.setSideId(relationshipSide.getElementId());

      fillModifiedAndDeletedRelations(currentRelationRecords, modifiedRelationRecordsMap, relationshipRecord, model);

      if (referencedRelationship.getIsNature()) {
        modifiedNatureRelationships.add(model);
      }
      else {
        modifiedRelationships.add(model);
      }
    }
  }

  private void fillModifiedAndDeletedRelations(Map<String, IRelationsSetDTO> currentRelationRecords, Map<String, IRelationsSetDTO> modifiedRelationRecordsMap,
      IRelationsSetDTO relationshipRecord, IContentRelationshipInstanceModel model)
  {
    String relationshipCode = relationshipRecord.getProperty().getPropertyCode();
    if (!modifiedRelationRecordsMap.containsKey(relationshipCode)) {
      model.setDeletedElements(relationshipRecord.getRelations().stream().map(x -> String.valueOf(x.getOtherSideEntityIID())).collect(Collectors.toList()));
    }
    else {
      IRelationsSetDTO importRelations = modifiedRelationRecordsMap.get(relationshipCode);
      IRelationsSetDTO currentRelations = currentRelationRecords.get(relationshipCode);

      Set<IEntityRelationDTO> deletedRelations = SetUtils.difference(currentRelations.getRelations(), importRelations.getRelations());
      Set<IEntityRelationDTO> addedRelations = SetUtils.difference(importRelations.getRelations(), currentRelations.getRelations());

      model.setDeletedElements(deletedRelations.stream().map(x -> String.valueOf(x.getOtherSideEntityIID())).collect(Collectors.toList()));
      model.setAddedElements(addedRelations.stream()
          .map(x -> new RelationshipVersion(String.valueOf(x.getOtherSideEntityIID())))
          .collect(Collectors.toList()));
    }
  }

    private  void fillCreatedRelationships(IGetConfigDetailsForCustomTabModel configDetails, Set<IRelationsSetDTO> relations,
        List<IContentRelationshipInstanceModel> modifiedRelationships, List<IContentRelationshipInstanceModel> modifiedNatureRelationships)
        throws Exception
    {
      for (IRelationsSetDTO relationSet : relations) {
        List<IRelationshipDataTransferInfoModel> relationshipDataTransferModel = new ArrayList<IRelationshipDataTransferInfoModel>();
        IContentRelationshipInstanceModel model = new ContentRelationshipInstanceModel();
        String relationshipId = relationSet.getProperty().getPropertyCode();
        model.setRelationshipId(relationshipId);
        
        for (IEntityRelationDTO relationship : relationSet.getRelations()) {
          IRelationshipVersion relation = new RelationshipVersion();
          relation.setId(String.valueOf(relationship.getOtherSideEntityIID()));
          relation.setContextId(String.valueOf(relationship.getContextualObject().getContext().getContextCode()));
          model.getAddedElements().add(relation);
        }
        
        IReferencedRelationshipModel referencedRelationship = configDetails.getReferencedRelationships().get(relationshipId);
        if (referencedRelationship == null) {
          referencedRelationship = configDetails.getReferencedNatureRelationships().get(relationshipId);
        }
        IRelationshipSide side = relationSet.getSide().equals(IPropertyDTO.RelationSide.SIDE_1) ? referencedRelationship.getSide1()
            : referencedRelationship.getSide2();
        model.setSideId(side.getElementId());
        
        if (referencedRelationship.getIsNature()) {
          modifiedNatureRelationships.add(model);
          dataTransferModelBuilder.fillDataForRelationshipInheritanceForAddedAndModifiedElements(relationshipDataTransferModel, relationSet,
              !(model.getAddedElements()==null||model.getAddedElements().isEmpty())?model.getAddedElements():null, true);
          dataTransferModelBuilder.prepareDataForRelationshipInheritance(relationshipDataTransferModel, relationSet.getEntityIID(), false,null,null);
        }
        else {
          modifiedRelationships.add(model);
          dataTransferModelBuilder.fillDataForRelationshipInheritanceForAddedAndModifiedElements(relationshipDataTransferModel, relationSet,
              !(model.getAddedElements()==null||model.getAddedElements().isEmpty())?model.getAddedElements():null, false);
          dataTransferModelBuilder.prepareDataForRelationshipInheritance(relationshipDataTransferModel, relationSet.getEntityIID(), false,null,null);
        }
      }
    }

  private List<IPropertyRecordDTO> createPropertyRecordInstance(IBaseEntityDAO baseEntityDAO,
        IGetConfigDetailsForCustomTabModel configDetails, List<String> propertyRecordCodes)
        throws Exception
  {
    PropertyRecordBuilder propertyRecordBuilder = new PropertyRecordBuilder(baseEntityDAO,
        configDetails, PropertyRecordType.DEFAULT, rdbmsComponentUtils.getLocaleCatlogDAO());
    // Create Attribute
    List<IPropertyRecordDTO> propertyRecords = this.createAttributePropertyRecordInstance(
        propertyRecordBuilder, configDetails, propertyRecordCodes);
    // Create tag
    propertyRecords.addAll(this.createTagPropertyRecordInstance(propertyRecordBuilder,
            configDetails, propertyRecordCodes));

    return propertyRecords;
  }
  
  private List<IPropertyRecordDTO> createAttributePropertyRecordInstance(
      PropertyRecordBuilder propertyRecordBuilder, IGetConfigDetailsForCustomTabModel configDetails, List<String> propertyRecordCodes)
  {
    List<IPropertyRecordDTO> attributePropertyRecords = new ArrayList<>();
    configDetails.getReferencedAttributes()
        .values()
        .forEach(referencedAttribute -> {
          try {
          if (!propertyRecordCodes.contains(referencedAttribute.getCode())) {
              IPropertyRecordDTO dto = propertyRecordBuilder.createValueRecord(referencedAttribute);
              if (dto != null) {
                attributePropertyRecords.add(dto);
              }
          }
          }
          catch (Exception e) {
            RDBMSLogger.instance().exception(e);
          }
        });
    
    return attributePropertyRecords;
  }
  
  protected List<IPropertyRecordDTO> createTagPropertyRecordInstance(
        PropertyRecordBuilder propertyRecordBuilder, IGetConfigDetailsForCustomTabModel configDetails,
        List<String> propertyRecordCodes)
  {
    List<IPropertyRecordDTO> tagPropertyRecords = new ArrayList<>();
    configDetails.getReferencedTags()
        .values()
        .forEach(referencedTags -> {
          try {
            if (!propertyRecordCodes.contains(referencedTags.getCode())) {
              IPropertyRecordDTO dto = propertyRecordBuilder.createTagsRecord(referencedTags);
              if (dto != null) {
                tagPropertyRecords.add(dto);
              }
            }
          }
          catch (Exception e) {
            RDBMSLogger.instance().exception(e);
          }
        });
    
    return tagPropertyRecords;
  }


  private void createTranslation(BaseEntityDTO baseEntity, LocaleCatalogDAO localeCatalogDao, BaseEntityDAO baseEntityDAO)
      throws RDBMSException
  {
    localeCatalogDao.loadLocaleIds(baseEntity);
    List<String> localeIds = baseEntity.getLocaleIds();
    if(!localeIds.contains(localeCatalogDao.getLocaleCatalogDTO().getLocaleID())) {
      baseEntityDAO.createLanguageTranslation();
    }
  }

  private void getUpdateAndCreatedRecord(Set<RelationsSetDTO> relationSetRecords,
      Set<IPropertyDTO> propertySet, Set<RelationsSetDTO> createRecords,
      Set<RelationsSetDTO> modifyRecords)
  {
    for (RelationsSetDTO record : relationSetRecords) {
      if (propertySet.contains(record.getProperty())) {
        record.setChanged(true);
        modifyRecords.add(record);
      }
      else
        createRecords.add(record);
    }
  }

  private void processModifiedRelation(PXONFileParser pxonFileParser, Map<ImportBlockIdentifier, ImportBlockInfo> baseEntityMetaList,
      LocaleCatalogDAO localeCatalogDao, Set<RelationsSetDTO> modifyRecords, Set<IRelationsSetDTO> modifiedRelationRecord,BaseEntityDTO baseEntity, BGPLog bgpLog,
      IPXONImporterPlanDTO importerPlanDTO) throws Exception
  {
    List<Long>  fetchAllSide2ForAsset = new ArrayList<>();
    for (RelationsSetDTO modifyRecord : modifyRecords) {
      RelationsSetDTO relationRecord = new RelationsSetDTO(modifyRecord.getEntityIID(),
          modifyRecord.getProperty(), modifyRecord.getSide());
      relationRecord.setChanged(true);
      Set<IEntityRelationDTO> relations = modifyRecord.getRelations();
      for (IEntityRelationDTO relation : relations) {
        String otherSideEntityID = relation.getOtherSideEntityID();
        BaseEntityDTO otherSideEntityDTO = (BaseEntityDTO) localeCatalogDao.getBaseEntityDTOByID(otherSideEntityID);
        ImportBlockIdentifier importBlockIdentifier = new ImportBlockIdentifier();
        importBlockIdentifier.setIdentifier(otherSideEntityID);
        importBlockIdentifier.setLanguage(baseEntity.getBaseLocaleID());
        ImportBlockInfo otherSideMetaInfo = baseEntityMetaList.get(importBlockIdentifier);
        if (otherSideEntityDTO == null) {
          if (otherSideMetaInfo != null) {
            otherSideEntityDTO = new BaseEntityDTO();
            otherSideEntityDTO.fromPXON(getPXONBlockFromFile(pxonFileParser, otherSideMetaInfo));
            createORUpdateBaseEntity(pxonFileParser, otherSideEntityDTO, otherSideMetaInfo, baseEntityMetaList, bgpLog, importerPlanDTO);
            relation.setOtherSideEntityIID(otherSideEntityDTO.getBaseEntityIID());
            if (!relation.getContextualObject().isNull()) {
              relation.getContextualObject().setChanged(true);
            }
            relationRecord.getRelations().add(relation);
          }
          else {
            bgpLog.progress("baseEntity not present in database and also not in import data of baseEntityID %S", otherSideEntityID);
          }
        } else {
          if (otherSideMetaInfo != null) {
            ImportBlockStatus status = otherSideMetaInfo.getStatus();
            if (status != ImportBlockStatus.IN_PROGRESS) {
              relation.setOtherSideEntityIID(otherSideEntityDTO.getBaseEntityIID());
              if (!relation.getContextualObject().isNull()) {
                relation.getContextualObject().setChanged(true);
              }
              relationRecord.getRelations().add(relation);
            }
            else {
              RDBMSLogger.instance().info("Already initiate this relation");
            }
          }
          else {
            relation.setOtherSideEntityIID(otherSideEntityDTO.getBaseEntityIID());
            if (!relation.getContextualObject().isNull()) {
              relation.getContextualObject().setChanged(true);
            }
            relationRecord.getRelations().add(relation);
          }
        }
        if (BaseType.ASSET.equals(otherSideEntityDTO.getBaseType()) && (!otherSideEntityDTO.getEntityExtension().isEmpty())) {
          fetchAllSide2ForAsset.add(relation.getOtherSideEntityIID());
        }
      }
      modifiedRelationRecord.add(relationRecord);
    }
    //check if defaultImageIID is set correctly
    if (fetchAllSide2ForAsset.isEmpty()) {
      baseEntity.setDefaultImageIID(0);
      baseEntity.setChanged(true);
    }
    else {
      if (!(fetchAllSide2ForAsset.contains(baseEntity.getDefaultImageIID()))) {
        baseEntity.setDefaultImageIID(fetchAllSide2ForAsset.get(0));
        baseEntity.setChanged(true);
      }
    }
  }
  private Set<IRelationsSetDTO> processRelationship(IGetConfigDetailsForCustomTabModel configDetails, PXONFileParser pxonFileParser, Map<ImportBlockIdentifier, ImportBlockInfo> baseEntityMetaList,
      LocaleCatalogDAO localeCatalogDao, BaseEntityDAO baseEntityDAO, Set<RelationsSetDTO> relationSetRecords, BaseEntityDTO baseEntity,
      BGPLog bgpLog, IPXONImporterPlanDTO importerPlanDTO) throws Exception
  {
    Set<IRelationsSetDTO> relationSetDTOS = new HashSet<>();
    for (RelationsSetDTO relationsSetDTO : relationSetRecords) {
      Set<IEntityRelationDTO> relations = relationsSetDTO.getRelations();
      String relationshipId = relationsSetDTO.getProperty().getPropertyCode();
      IReferencedRelationshipModel referencedRelationship = configDetails.getReferencedRelationships().get(relationshipId);
      if (referencedRelationship == null) {
        referencedRelationship = configDetails.getReferencedNatureRelationships().get(relationshipId);
      }
      String classifierCode = baseEntity.getNatureClassifier().getClassifierCode();
      String side1KlassId = referencedRelationship.getSide1().getKlassId();
      boolean isSide1 = side1KlassId.equals(classifierCode);
      IRelationsSetDTO relationSets = new RelationsSetDTO(baseEntity.getBaseEntityIID(), relationsSetDTO.getProperty(),
          relationsSetDTO.getSide());
      if (referencedRelationship.getIsNature() && !(isSide1)) {
        return relationSetDTOS;
      }
      for (IEntityRelationDTO relation : relations) {
        String otherSideEntityID = relation.getOtherSideEntityID();
        BaseEntityDTO otherSideEntityDTO = (BaseEntityDTO) localeCatalogDao
            .getBaseEntityDTOByID(otherSideEntityID);
        if(!relation.getContextualObject().isNull()) {
          relation.getContextualObject().setChanged(true);
          relation.getContextualObject().getContext().setChanged(true);
        }
        
        ImportBlockIdentifier importBlockIdentifier = new ImportBlockIdentifier();
        importBlockIdentifier.setIdentifier(otherSideEntityID);
        importBlockIdentifier.setLanguage(baseEntity.getBaseLocaleID());
        ImportBlockInfo otherSideEntityMetaInfo = baseEntityMetaList.get(importBlockIdentifier);
        // If otherSideEntityMetaInfo is not found on the basis of
        // importBlockIdentifier then simply iterate the map and find by id
        if (otherSideEntityMetaInfo == null) {
          Optional<Map.Entry<ImportBlockIdentifier, ImportBlockInfo>> optional = baseEntityMetaList.entrySet().stream().filter(entry -> {
            ImportBlockIdentifier identifierKey = entry.getKey();
            if (identifierKey.getIdentifier().equals(importBlockIdentifier.getIdentifier()))
              return true;
            else
              return false;
          }).findFirst();
          if (optional.isPresent()) {
            otherSideEntityMetaInfo = optional.get().getValue();
          }
        }
        // Other side entity for create available in the file
        if (otherSideEntityDTO == null && otherSideEntityMetaInfo != null) {
          if (otherSideEntityMetaInfo.getStatus() == ImportBlockStatus.PENDING) {
            otherSideEntityDTO = new BaseEntityDTO();
            otherSideEntityDTO.fromPXON(getPXONBlockFromFile(pxonFileParser, otherSideEntityMetaInfo));
            createORUpdateBaseEntity(pxonFileParser, otherSideEntityDTO, otherSideEntityMetaInfo, baseEntityMetaList, bgpLog, importerPlanDTO);
            relation.setOtherSideEntityIID(otherSideEntityDTO.getBaseEntityIID());
            relationSets.getRelations().add(relation);
          }
        }
        // Other side entity exists into DB
        else if (otherSideEntityDTO != null) {
          if (otherSideEntityMetaInfo != null) {
            // If the other entity is itself in progress then it will take care
            // of its own in the recursion if IN_PROGRESS means already
            // processing
            if (otherSideEntityMetaInfo.getStatus() != ImportBlockStatus.IN_PROGRESS) {
              relation.setOtherSideEntityIID(otherSideEntityDTO.getBaseEntityIID());
              relationSets.getRelations()
                  .add(relation);
            }
          }
          else {
            relation.setOtherSideEntityIID(otherSideEntityDTO.getBaseEntityIID());
            relationSets.getRelations()
                .add(relation);
          }
        }
        else {
          bgpLog.progress("CanNot create this Relation because other side Entity is not present in database and also not present in import data");
        }
        if(otherSideEntityDTO != null && baseEntity.getDefaultImageIID() == 0) {
            if (BaseType.ASSET.equals(otherSideEntityDTO.getBaseType())&& (!otherSideEntityDTO.getEntityExtension().isEmpty())) {
              baseEntity.setDefaultImageIID(otherSideEntityDTO.getBaseEntityIID());
              baseEntity.setChanged(true);
              Set<IRelationsSetDTO> relationSetDTOSUpdate = new HashSet<>();
              relationSetDTOSUpdate.add(relationSets);
              baseEntityDAO.updatePropertyRecords(relationSetDTOSUpdate.toArray(new IPropertyRecordDTO[0]));
            }
        }
      }
      if (!relationSets.isEmpty()) {
        relationSetDTOS.add(relationSets);
      }
    }
    Set<IPropertyRecordDTO> records = new HashSet<>(baseEntityDAO.getBaseEntityDTO().getPropertyRecords());
    if(!relationSetDTOS.isEmpty()){
      IBaseEntityDTO baseEntityDTO = baseEntityDAO.createPropertyRecords(relationSetDTOS.toArray(new IPropertyRecordDTO[0]));
      baseEntityDTO.getPropertyRecords().addAll(records);
    }
    return relationSetDTOS;
  }

  private void addOrRemoveClassifier(BaseEntityDAO baseEntityDAO,
      Set<IClassifierDTO> otherClassifiers,List<IClassifierDTO> addedRemovedClassifier, IGetConfigDetailsForCustomTabModel configDetails,List<Long> dependentPropertyIIDs) throws Exception
  {
    List<IClassifierDTO> classifiers = baseEntityDAO.getClassifiers();
    Set<IClassifierDTO> createClassifiers = new HashSet<>(otherClassifiers);
    createClassifiers.removeAll(classifiers);
    baseEntityDAO.addClassifiers(createClassifiers.toArray(new IClassifierDTO[0]));
    if (!createClassifiers.isEmpty()) {
      addedRemovedClassifier.addAll(new ArrayList<>(createClassifiers));
    }
    Set<IClassifierDTO> removeClassifiers = new HashSet<>(classifiers);
    removeClassifiers.removeAll(otherClassifiers);
    if (!removeClassifiers.isEmpty())
    {
      baseEntityDAO.removeClassifiers(removeClassifiers.toArray(new IClassifierDTO[0]));
      addedRemovedClassifier.addAll(new ArrayList<>(removeClassifiers));
      removeClassificationProperties(baseEntityDAO);
      dataTransferModelBuilder.setDataForRuntimeCleanup(baseEntityDAO, otherClassifiers, removeClassifiers);
    }
    if (!createClassifiers.isEmpty() || !removeClassifiers.isEmpty()) {
      // Prepare Model & Initiate Relationship Inheritance Switch Type Event using
      dataTransferModelBuilder.prepareDataForRelationshipInheritanceSwitchType(baseEntityDAO,
          createClassifiers.stream().collect(Collectors.toList()), removeClassifiers.stream().collect(Collectors.toList()),null);
      // Prepare Model & Initiate ClassificationDataTransfer using
      dataTransferModelBuilder.initiateClassificationDataTransfer(baseEntityDAO.getLocaleCatalog(), baseEntityDAO.getBaseEntityDTO().getBaseEntityIID(), createClassifiers, removeClassifiers);
      IKlassInstanceTypeSwitchModel typeSwitchModel = new KlassInstanceTypeSwitchModel();
      List<String> addedTaxonomyIds = getTaxonomiesIdsFromClassifier(createClassifiers);
      List<String> removedTaxonomyIds = getTaxonomiesIdsFromClassifier(removeClassifiers);
      typeSwitchModel.setAddedTaxonomyIds(addedTaxonomyIds);
      typeSwitchModel.setDeletedTaxonomyIds(removedTaxonomyIds);
      dataTransferModelBuilder.initiateTaxonomyInheritance(typeSwitchModel, configDetails,
          baseEntityDAO.getBaseEntityDTO().getBaseEntityIID());
      if (!dependentPropertyIIDs.isEmpty()) {
        dataTransferModelBuilder.initiateLanguageInheritanceDataTransfer(baseEntityDAO.getLocaleCatalog(),  baseEntityDAO.getBaseEntityDTO().getBaseEntityIID(), dependentPropertyIIDs,true);
      }
    }
  }
 
  /**
   * responsible for collect the  Taxonomy Ids.
   * @param createClassifiers
   * @return list of Taxonomy Ids
   */
  private List<String> getTaxonomiesIdsFromClassifier(Set<IClassifierDTO> createClassifiers)
  { List<String> getAddedTaxonomyIds = new ArrayList<String>();
    for(IClassifierDTO classifierDTO:createClassifiers) {
      getAddedTaxonomyIds.add(classifierDTO.getCode());
    }
    return getAddedTaxonomyIds;
    
  }

  @SuppressWarnings("unused")
  private void addChildren(PXONFileParser pxonFileParser, BaseEntityDTO baseEntity, Map<ImportBlockIdentifier, ImportBlockInfo> baseEntityMetaList,
      LocaleCatalogDAO localeCatalogDao, BGPLog bgpLog, IPXONImporterPlanDTO importerPlanDTO, Boolean isDuplicateVariantAllowed, long parentEntityIID) throws Exception
  {
    ImportBlockIdentifier importBlockIdentifier = new ImportBlockIdentifier();
    importBlockIdentifier.setIdentifier(baseEntity.getParentID());
    importBlockIdentifier.setLanguage(baseEntity.getBaseLocaleID());
    ImportBlockInfo baseEntityMetaInfo = baseEntityMetaList.get(importBlockIdentifier);
    IBaseEntityDTO parentEntityDTO = localeCatalogDao.getBaseEntityDTOByIID(parentEntityIID);
    if (parentEntityDTO == null && baseEntityMetaInfo != null) {
      if (baseEntityMetaInfo.getStatus() == ImportBlockStatus.PENDING) {
        BaseEntityDTO parentEntity = new BaseEntityDTO();
        parentEntity.fromPXON(getPXONBlockFromFile(pxonFileParser, baseEntityMetaInfo));
        createORUpdateBaseEntity(pxonFileParser, parentEntity, baseEntityMetaInfo, baseEntityMetaList, bgpLog, importerPlanDTO);
      }
      addChildrenToBaseEntity(baseEntity.getParentID(), localeCatalogDao,
          baseEntity.getBaseEntityID(), baseEntity.getEmbeddedType(), bgpLog);
    }
    else if (parentEntityDTO != null && parentEntityDTO.getBaseEntityIID() >= 0L) {
      BaseEntityDAO parentEntityDAO = getBaseEntityDAO(parentEntityDTO, localeCatalogDao);
      IBaseEntityDTO childEntityDTO = localeCatalogDao.getBaseEntityDTOByIID(baseEntity.getBaseEntityIID());
      List<IBaseEntityIDDTO> children = parentEntityDAO.getChildren(baseEntity.getEmbeddedType());
      if (!children.contains(childEntityDTO)) {
        if (!isDuplicateVariantAllowed) {
          for(IBaseEntityIDDTO c:children) { 
           if(c.getContextualObject().getContextTagValues().equals(childEntityDTO.getContextualObject().getContextTagValues())) {
             bgpLog.progress("parent has duplicate context for childEntityID %S", baseEntity.getParentID());
             return;
           }
          }
        }
        bgpLog.progress("Child added for : childEntityIID = %d and parentEntityIID = %d",
            childEntityDTO.getBaseEntityIID(),parentEntityDTO.getBaseEntityIID());
        parentEntityDAO.addChildren(baseEntity.getEmbeddedType(), childEntityDTO);
      }
      else {
        bgpLog.progress("parent is already added for childEntityID %S", baseEntity.getParentID());
      }
    }
    else {
      bgpLog.progress("Parent is not present in database and also not in import data for childEntityID %S",
              baseEntity.getParentID());
    }
    dataTransferModelBuilder.prepareDataForContextualDataTransferTask(baseEntity.getBaseEntityIID(), localeCatalogDao.openCouplingDAO());
  }
  
  @SuppressWarnings("unused")
  private void addChildren_new(PXONFileParser pxonFileParser, BaseEntityDTO baseEntity, Map<ImportBlockIdentifier, ImportBlockInfo> baseEntityMetaList,
      LocaleCatalogDAO localeCatalogDao, BGPLog bgpLog, IPXONImporterPlanDTO importerPlanDTO, Boolean isDuplicateVariantAllowed, long parentEntityIID) throws Exception
  {
    IBaseEntityDTO parentEntityDTO = localeCatalogDao.getBaseEntityDTOByIID(parentEntityIID);
    if (parentEntityDTO != null && parentEntityDTO.getBaseEntityIID() >= 0L) {
      BaseEntityDAO parentEntityDAO = getBaseEntityDAO(parentEntityDTO, localeCatalogDao);
      parentEntityDAO.addChildren(baseEntity.getEmbeddedType(), baseEntity);
    }
    else {
      bgpLog.progress("Parent is not present in database for childEntityID %S", baseEntity.getParentID());
    }
    dataTransferModelBuilder.prepareDataForContextualDataTransferTask(baseEntity.getBaseEntityIID(), localeCatalogDao.openCouplingDAO());
  }

  private void addChildrenToBaseEntity(String parentEntityID, LocaleCatalogDAO localeCatalogDao,
      String childrenEntityID, EmbeddedType embeddedType, BGPLog bgpLog) throws RDBMSException
  {
    IBaseEntityDTO parentEntityDTO = localeCatalogDao.getBaseEntityDTOByID(parentEntityID);
    BaseEntityDAO parentEntityDAO = getBaseEntityDAO(parentEntityDTO, localeCatalogDao);
    IBaseEntityDTO childEntityDTO = localeCatalogDao.getBaseEntityDTOByID(childrenEntityID);
    bgpLog.progress("Child added for : childEntityIID = %d and parentEntityIID = %d",
        childEntityDTO.getBaseEntityIID(),parentEntityDTO.getBaseEntityIID());
    // If child DTO has parentIID that means it is already linked to any parent
    // entity
    if (childEntityDTO.getParentIID() == 0L)
      parentEntityDAO.addChildren(embeddedType, childEntityDTO);
    else
      bgpLog.progress("CAN NOT MODIFY the parent IID Once Assign for childEntityIID %d",
              childEntityDTO.getBaseEntityIID());
  }

  private void createORUpdatePropertyRecords(BaseEntityDAO baseEntityDAO,
      Set<IPropertyRecordDTO> valueTagRecords, Set<IPropertyDTO> propertySet, Set<IPropertyDTO> propertySetToIgnore) throws RDBMSException, CSFormatException
  {
    Set<IPropertyRecordDTO> createRecords = new HashSet<>();
    Set<IPropertyRecordDTO> modifyRecords = new HashSet<>();
    Set<Long> contextValueIID = new HashSet<>();

    Map<String, List<IPropertyRecordDTO>> existingRecords = new HashMap<>();

    List<IPropertyDTO> propertiesToLoad = new ArrayList<>();
    for(IPropertyRecordDTO record : valueTagRecords) {
      if (record instanceof ValueRecordDTO) {
        ValueRecordDTO valueRecord = (ValueRecordDTO) record;
        if (valueRecord.getValueIID() == 0) {
          propertiesToLoad.add(valueRecord.getProperty());
        }
      }
    }
    long baseEntityIID = baseEntityDAO.getBaseEntityDTO().getBaseEntityIID();
    Map<Long, Set<IPropertyRecordDTO>> propertyRecordsForEntities = rdbmsComponentUtils.getLocaleCatlogDAO()
        .getPropertyRecordsForEntities(Set.of(baseEntityIID),
            propertiesToLoad.toArray(new IPropertyDTO[0]));

    Set<IPropertyRecordDTO> propertyRecords = propertyRecordsForEntities.get(baseEntityIID);
    for (IPropertyRecordDTO propertyRecord : propertyRecords){

      List<IPropertyRecordDTO> valueRecords = existingRecords
          .computeIfAbsent(propertyRecord.getProperty().getPropertyCode(), x -> new ArrayList<>());
      valueRecords.add(propertyRecord);
    }

    for (IPropertyRecordDTO record : valueTagRecords) {
      IPropertyDTO property = record.getProperty();
      if (propertySet.contains(property)) {
        if (!propertySetToIgnore.contains(property)) {
          record.setChanged(true);
          if (record instanceof ValueRecordDTO) {
            ValueRecordDTO valueRecord = (ValueRecordDTO) record;
            if (valueRecord.getValueIID() == 0) {
              List<IPropertyRecordDTO> records = existingRecords.get(valueRecord.getProperty().getPropertyCode());
              boolean isAdded = setIIDToModifiedValueRecords(records, createRecords, modifyRecords, record, contextValueIID,
                  valueRecord);
              if (!isAdded)
                createRecords.add(valueRecord);
            }//process all data only if ValueIID is 0
            else {
              if (!valueRecord.getContextualObject().getContextCode().isEmpty()) {
                valueRecord.getContextualObject().setChanged(true);
                contextValueIID.add(valueRecord.getValueIID());
              }
              modifyRecords.add(record);
            }
          }
          else {
            //tag modification
            modifyRecords.add(record);
          }
        }
      }
      else {
        createRecords.add(record);
      }
    }
    baseEntityDAO.createPropertyRecords(createRecords.toArray(new IPropertyRecordDTO[0]));
    baseEntityDAO.updatePropertyRecords(modifyRecords.toArray(new IPropertyRecordDTO[0]));
    
  }

  private boolean setIIDToModifiedValueRecords(List<IPropertyRecordDTO> existingRecords, Set<IPropertyRecordDTO> createRecords,
      Set<IPropertyRecordDTO> modifyRecords, IPropertyRecordDTO record, Set<Long> contextValueIID,
      ValueRecordDTO valueRecord)
  {
    for(IPropertyRecordDTO propertyRecord : existingRecords) {
      ValueRecordDTO loadedValueRecord = (ValueRecordDTO) propertyRecord;
      long valueIID = loadedValueRecord.getValueIID();
      loadedValueRecord.setValueIID(0);
      if(valueRecord.getContextualObject().isNull()) {
        loadedValueRecord.setContextualData((ContextualDataDTO) valueRecord.getContextualObject());
        //check language and update accordingly
        if(loadedValueRecord.equals(valueRecord)) {
          valueRecord.setValueIID(valueIID);
          modifyRecords.add(record);
        }else {
          // not present in this language
          createRecords.add(record);
        }
        return true;
      }else {
        if(!contextValueIID.contains(valueIID)) {
          valueRecord.getContextualObject().setChanged(true);
          valueRecord.setValueIID(valueIID);
          modifyRecords.add(valueRecord);
          contextValueIID.add(valueIID);
          return true;
        }// no duplicate record is update in case of context
      }// context loop
    }//to set valueIID
    return false;
  }
  
  private BaseEntityDAO getBaseEntityDAO(IBaseEntityDTO baseEntity,
      LocaleCatalogDAO localeCatalogDao)
  {
    return new BaseEntityDAO(localeCatalogDao, (UserSessionDTO) userSession, baseEntity);
  }

  LocaleCatalogDAO getLocaleCatalogDAO(IBaseEntityDTO baseEntity)
  {
    ILocaleCatalogDTO localeCatalog = baseEntity.getLocaleCatalog();
    return new LocaleCatalogDAO(userSession, new LocaleCatalogDTO(baseEntity.getBaseLocaleID(),
        new CatalogDTO(localeCatalog.getCatalogCode(), localeCatalog.getOrganizationCode())));
  }

  private List<String> separatePropertyRecords(IBaseEntityDTO baseEntity,
      Set<IPropertyRecordDTO> valueTagRecords, Set<RelationsSetDTO> relationSetRecords)
  {
    Set<IPropertyRecordDTO> propertyRecords = baseEntity.getPropertyRecords();
    long baseEntityIID = baseEntity.getBaseEntityIID();
    List<String> propertyRecordCodes = new ArrayList<>();
    for (IPropertyRecordDTO propRecord : propertyRecords) {
      PropertyRecordDTO prop = (PropertyRecordDTO) propRecord;
      prop.setIID(baseEntityIID);
      if (propRecord instanceof IRelationsSetDTO)
        relationSetRecords.add((RelationsSetDTO) propRecord);
      else {
        propertyRecordCodes.add(propRecord.getProperty().getCode());
        valueTagRecords.add(propRecord);
      }
    }
    return propertyRecordCodes;
  }
  
  private String getPXONBlockFromFile(PXONFileParser pxonFileParser,
      ImportBlockInfo baseEntityMetaInfo) throws IOException
  {
    long offset = baseEntityMetaInfo.getStartPos();
    long length = baseEntityMetaInfo.getEndPos();
    return pxonFileParser.getBlock(offset, length);
  }
  

  /**
   * Remove classification properties after remove of classifier from base property
   * @param baseEntityDAO
   * @throws Exception
   * @throws RDBMSException
   * @throws CSFormatException
   */
  private void removeClassificationProperties(BaseEntityDAO baseEntityDAO)
      throws Exception, RDBMSException, CSFormatException
  {
    // Get config details after removal of classification
    IMulticlassificationRequestModel configRequestModelForImport = configUtil
        .getConfigRequestModelForImport(baseEntityDAO.getLocaleCatalog().getLocaleInheritanceSchema(), baseEntityDAO);
    IGetConfigDetailsForCustomTabModel configDetails = getConfigDetails.execute(configRequestModelForImport);
    
    // Prepare config attribute and tag ids list
    Map<String, IAttribute> referencedAttributes = configDetails.getReferencedAttributes();
    Map<String, ITag> referencedTags = configDetails.getReferencedTags();
    Set<String> attributeAndTagIds = new HashSet<>();
    if (!MapUtils.isEmpty(referencedAttributes)) {
      attributeAndTagIds.addAll(referencedAttributes.keySet());
    }
    if (!MapUtils.isEmpty(referencedTags)) {
      attributeAndTagIds.addAll(referencedTags.keySet());
    }

    IBaseEntityDTO baseEntityDTO = baseEntityDAO.loadAllPropertyRecords();
    Set<IPropertyRecordDTO> propertyRecords = baseEntityDTO.getPropertyRecords();

    List<IPropertyRecordDTO> propertyRecordsToDelete = new ArrayList<>();
    for (IPropertyRecordDTO propertyRecord : propertyRecords) {
      if (!attributeAndTagIds.contains(propertyRecord.getProperty().getPropertyCode())) {
        propertyRecordsToDelete.add(propertyRecord);
      }
    }
    baseEntityDAO.deletePropertyRecords(propertyRecordsToDelete.toArray(new IPropertyRecordDTO[0]));
  }
}
