package com.cs.core.runtime.klassinstance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.standard.IConfigMap;
import com.cs.constants.CommonConstants;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.configdetails.GetPostConfigDetailsRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetPostConfigDetailsRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetPostConfigDetailsResponseModel;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipInstanceModel;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.runtime.builder.BuilderFactory;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.exception.configuration.CardinalityException;
import com.cs.core.runtime.interactor.exception.relationshipinstance.InstanceAlreadyPresentInAnotherLinkedVariantRelationshipException;
import com.cs.core.runtime.interactor.exception.variants.DuplicateVariantExistsException;
import com.cs.core.runtime.interactor.model.configdetails.GetConfigDetailsForSaveRelationshipInstancesRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.IGetConfigDetailsForSaveRelationshipInstancesRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.IGetConfigDetailsForSaveRelationshipInstancesResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.instance.IContentRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipDataTransferInfoModel;
import com.cs.core.runtime.interactor.model.relationship.ISaveRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.relationship.ISaveRelationshipInstanceResponseModel;
import com.cs.core.runtime.interactor.model.relationship.ISaveRelationshipInstanceStrategyModel;
import com.cs.core.runtime.interactor.model.relationship.SaveRelationshipInstanceResponseModel;
import com.cs.core.runtime.interactor.model.relationship.SaveRelationshipInstanceStrategyModel;
import com.cs.core.runtime.interactor.model.taskinstance.GetTaskInstanceResponseModel;
import com.cs.core.runtime.interactor.model.templating.GetKlassInstanceForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.version.GetKlassInstanceVersionForTimeLineModel;
import com.cs.core.runtime.interactor.model.version.IGetKlassInstanceVersionsForTimeLineModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailForTaxonomyInheritanceStrategy;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForCustomTabStrategy;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForOverviewTabStrategy;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForSaveRelationshipInstancesStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RelationshipRecordBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.VariantInstanceUtils;
import com.cs.core.runtime.strategy.usecase.configdetails.IGetPostConfigDetailsStrategy;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.core.util.DataTransferBGPModelBuilder;
import com.cs.core.util.VersionUtils;
import com.cs.pim.runtime.articleinstance.ISaveInstanceRelationshipsService;

@SuppressWarnings("unchecked")
@Component
public abstract class AbstractSaveRelationshipInstances<P extends ISaveRelationshipInstanceModel, R extends IGetKlassInstanceModel>
    extends AbstractRuntimeService<ISaveRelationshipInstanceModel, IGetKlassInstanceModel>
    implements ISaveInstanceRelationshipsService {
  
  @Autowired
  protected IGetConfigDetailsForSaveRelationshipInstancesStrategy getConfigDetailsForSaveRelationshipInstancesStrategy;

  @Autowired
  protected IGetConfigDetailsForCustomTabStrategy                 getConfigDetailsForCustomTabStrategy;
  
  @Autowired
  protected IGetConfigDetailsForOverviewTabStrategy               getConfigDetailsForOverviewTabStrategy;
  
  @Autowired
  protected IGetPostConfigDetailsStrategy                         postConfigDetailsForRelationshipsStrategy;
  
  @Autowired
  protected PermissionUtils                                       permissionUtils;
  
  @Autowired
  protected RDBMSComponentUtils                                   rdbmsComponentUtils;
  
  @Autowired
  protected VersionUtils                                          versionUtils;
  
  @Autowired
  protected VariantInstanceUtils                                  variantInstanceUtils;
  
  @Autowired
  protected KlassInstanceUtils                                    klassInstanceUtils;
  
  @Autowired
  protected ISessionContext                                       context;

  @Autowired
  protected DataTransferBGPModelBuilder dataTransferBGPModelBuilder;

  @Autowired
  protected IGetConfigDetailForTaxonomyInheritanceStrategy        getConfigDetailForTaxonomyInheritanceStrategy;
  
  @Autowired
  protected TransactionThreadData transactionThread;
  
  @Autowired
  protected DataTransferBGPModelBuilder dataTransferModelBuilder;


  protected R executeInternal(P dataModel) throws Exception
  {
    Long sourceEntityIID = Long.parseLong(dataModel.getId());
    IKlassInstanceTypeModel klassInstanceTypes = executeSaveRelationship(dataModel);

    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(sourceEntityIID);
    IGetConfigDetailsForCustomTabModel configDetailsForCustomtab = getConfigDetailsForResponse(dataModel, klassInstanceTypes, baseEntityDAO);
    Boolean shouldCreateVersion = shouldCreateVersion(configDetailsForCustomtab, dataModel);
    if (shouldCreateVersion) {
      // craete new revision for relationship management
      rdbmsComponentUtils.createNewRevision(baseEntityDAO.getBaseEntityDTO(), configDetailsForCustomtab.getNumberOfVersionsToMaintain());
    }

    return (R) prepareDataForResponse(baseEntityDAO, configDetailsForCustomtab, dataModel.getTabType());
  }

  public IKlassInstanceTypeModel executeSaveRelationship(ISaveRelationshipInstanceModel dataModel)
      throws Exception
  {
    Long sourceEntityIID = Long.parseLong(dataModel.getId());
    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(sourceEntityIID);
    IKlassInstanceTypeModel klassInstanceTypes = getKlassInstanceType(baseEntityDAO);
    IGetConfigDetailsForSaveRelationshipInstancesResponseModel configDetails = getConfigDetails(
        dataModel, klassInstanceTypes);

    ISaveRelationshipInstanceStrategyModel saveRelationshipInstanceModel = new SaveRelationshipInstanceStrategyModel();
    saveRelationshipInstanceModel.setRelationshipAdm(dataModel);
    saveRelationshipInstanceModel.setConfigDetails(configDetails);
    executeSaveRelationshipInstances(saveRelationshipInstanceModel, baseEntityDAO);
    return klassInstanceTypes;
  }
  
  private void executeSaveRelationshipInstances(ISaveRelationshipInstanceStrategyModel saveRelationshipInstanceModel, IBaseEntityDAO baseEntityDAO) throws Exception
  {
    ISaveRelationshipInstanceModel dataModel = saveRelationshipInstanceModel.getRelationshipAdm();
    Long sourceEntityIID = Long.parseLong(dataModel.getId());
    Boolean isManuallyCreated = dataModel.getIsManuallyCreated();

    List<Long> entitiesWithoutDefaultImage = new ArrayList<>();
    //this.manageRelationship(saveRelationshipInstanceModel, baseEntityDAO);
    ISaveRelationshipInstanceResponseModel saveRelationInstanceResponseModel = this
        .manageRelationship(saveRelationshipInstanceModel, baseEntityDAO, entitiesWithoutDefaultImage);
    handleDefaultImage(entitiesWithoutDefaultImage, baseEntityDAO);

    dataTransferModelBuilder.prepareDataForRelationshipInheritance(saveRelationInstanceResponseModel.getDataTransferInfo(),
        baseEntityDAO.getBaseEntityDTO().getBaseEntityIID(), isManuallyCreated,null,null);

    if (!isManuallyCreated) {
      dataTransferBGPModelBuilder.prepareDataForTaxonomyInheritance(dataModel.getModifiedNatureRelationships(), sourceEntityIID, saveRelationshipInstanceModel.getConfigDetails());
    }

    List<IContentRelationshipInstanceModel> modifiedRelationships = saveRelationshipInstanceModel.getRelationshipAdm().getModifiedRelationships();
    List<IContentRelationshipInstanceModel> modifiedNatureRelationships = saveRelationshipInstanceModel.getRelationshipAdm().getModifiedNatureRelationships();

    if(!modifiedRelationships.isEmpty() || !modifiedNatureRelationships.isEmpty()){
      dataTransferBGPModelBuilder.prepareDataForRelationshipDataTransfer(baseEntityDAO, modifiedRelationships,
          modifiedNatureRelationships);
    }
  }


  

  private IGetConfigDetailsForCustomTabModel getConfigDetailsForResponse(ISaveRelationshipInstanceModel dataModel,
      IKlassInstanceTypeModel klassInstanceTypes, IBaseEntityDAO baseEntityDAO) throws Exception
  {
    IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
    multiclassificationRequestModel.setKlassIds((List<String>) klassInstanceTypes.getTypes());
    multiclassificationRequestModel.setSelectedTaxonomyIds(klassInstanceTypes.getSelectedTaxonomyIds());
    multiclassificationRequestModel.setTemplateId(dataModel.getTemplateId());
    multiclassificationRequestModel.setShouldUseTagIdTagValueIdsMap(false);
    multiclassificationRequestModel.setTabId(dataModel.getTabId());
    multiclassificationRequestModel.setTypeId(dataModel.getTypeId());
    multiclassificationRequestModel.setParentKlassIds(klassInstanceTypes.getParentKlassIds());
    multiclassificationRequestModel.setParentTaxonomyIds(klassInstanceTypes.getParentTaxonomyIds());

    TransactionData transactionData = transactionThread.getTransactionData();

    Set<String> languageCodes = new HashSet<>(baseEntityDAO.getBaseEntityDTO().getLocaleIds());
    languageCodes.add(transactionData.getDataLanguage());
    multiclassificationRequestModel.setLanguageCodes(new ArrayList<>(languageCodes));

    multiclassificationRequestModel.setEndpointId(transactionData.getEndpointId());
    multiclassificationRequestModel.setPhysicalCatalogId(transactionData.getPhysicalCatalogId());
    multiclassificationRequestModel.setOrganizationId(transactionData.getOrganizationId());
    multiclassificationRequestModel.setUserId(transactionData.getUserId());
    return getEntitySpecificConfigDetailsForResponse(dataModel, multiclassificationRequestModel);
  }

  protected IGetConfigDetailsForSaveRelationshipInstancesResponseModel getConfigDetails(ISaveRelationshipInstanceModel dataModel,
      IKlassInstanceTypeModel klassInstanceTypes) throws Exception
  {
    IGetConfigDetailsForSaveRelationshipInstancesRequestModel configDetailsRequestModel = new GetConfigDetailsForSaveRelationshipInstancesRequestModel();
    configDetailsRequestModel.setRelationshipIds(getRelationshipIds(dataModel.getModifiedRelationships()));
    configDetailsRequestModel.setNatureRelationshipIds(getRelationshipIds(dataModel.getModifiedNatureRelationships()));
    configDetailsRequestModel.setKlassIds(new ArrayList<>(klassInstanceTypes.getTypes()));
    configDetailsRequestModel.setTaxonomyIds(klassInstanceTypes.getSelectedTaxonomyIds());

    IGetConfigDetailsForSaveRelationshipInstancesResponseModel configDetails = getConfigDetailsForSaveRelationshipInstancesStrategy
        .execute(configDetailsRequestModel);
    return configDetails;
  }

  protected List<String> getRelationshipIds(List<IContentRelationshipInstanceModel> modifiedRelationships)
  {
    return modifiedRelationships.stream().map(IContentRelationshipInstanceModel::getRelationshipId).distinct().collect(Collectors.toList());
  }

  protected IGetKlassInstanceModel prepareDataForResponse(IBaseEntityDAO baseEntityDAO, IGetConfigDetailsForCustomTabModel configDetails,
      String tabType) throws Exception
  {
    IGetKlassInstanceModel returnModel = executeGetKlassInstance(baseEntityDAO, configDetails, tabType);
    permissionUtils.manageKlassInstancePermissions(returnModel);
    returnModel.setVariantOfLabel(klassInstanceUtils.getVariantOfLabel(baseEntityDAO, configDetails.getLinkedVariantCodes()));
    return returnModel;
  }

  protected IGetConfigDetailsForCustomTabModel getEntitySpecificConfigDetailsForResponse(ISaveRelationshipInstanceModel dataModel,
      IMulticlassificationRequestModel multiclassificationRequestModel) throws Exception
  {
    if (dataModel.getTabId().equals("overview_tab")) {
      return getConfigDetailsForOverviewTabStrategy.execute(multiclassificationRequestModel);
    }

    return getConfigDetailsForCustomTabStrategy.execute(multiclassificationRequestModel);
  }

  protected void executeGetRelationshipInstance(IGetKlassInstanceCustomTabModel returnModel,
      IGetConfigDetailsForCustomTabModel configDetails, IBaseEntityDAO baseEntityDAO) throws Exception
  {
    IRelationshipInstanceModel relationshipInstanceModel = BuilderFactory.newRelationshipInstanceBuilder(rdbmsComponentUtils, configDetails)
        .baseEntityDAO(baseEntityDAO).build();

    returnModel.setContentRelationships(relationshipInstanceModel.getContentRelationships());
    returnModel.setReferenceRelationshipInstanceElements(relationshipInstanceModel.getReferenceRelationshipInstanceElements());
    returnModel.setNatureRelationships(relationshipInstanceModel.getNatureRelationships());
    returnModel.setReferenceNatureRelationshipInstanceElements(relationshipInstanceModel.getReferenceNatureRelationshipInstanceElements());

    this.addKlassTypeConfigDataForRelationshipInstanceElements(returnModel, configDetails);
  }

  private void addKlassTypeConfigDataForRelationshipInstanceElements(IGetKlassInstanceCustomTabModel returnModel,
      IGetConfigDetailsForCustomTabModel configDetails) throws Exception
  {
    Set<String> klassIdsSet = new HashSet<>();
    returnModel.getReferenceRelationshipInstanceElements().values().forEach(klassInstances -> {
      klassInstances.forEach(klassInstanceInformation -> {
        klassIdsSet.addAll(klassInstanceInformation.getTypes());
      });
    });
    returnModel.getReferenceNatureRelationshipInstanceElements().values().forEach(klassInstances -> {
      klassInstances.forEach(klassInstanceInformation -> {
        klassIdsSet.addAll(klassInstanceInformation.getTypes());
      });
    });
    if (!klassIdsSet.isEmpty()) {
      List<String> klassIdsList = new ArrayList<>(klassIdsSet);
      IGetPostConfigDetailsRequestModel requestModel = new GetPostConfigDetailsRequestModel();
      requestModel.setKlassIds(klassIdsList);
      IGetPostConfigDetailsResponseModel responseModel = postConfigDetailsForRelationshipsStrategy.execute(requestModel);
      Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses = configDetails.getReferencedKlasses();

      returnModel.getConfigDetails().getReferencedKlasses();

      referencedKlasses.putAll(responseModel.getReferencedKlasses());
    }
  }

  private ISaveRelationshipInstanceResponseModel manageRelationship(
      ISaveRelationshipInstanceStrategyModel saveRelationshipInstanceModel,
      IBaseEntityDAO baseEntityDAO, List<Long> entitiesWithoutDefaultImage) throws Exception
  {
    ISaveRelationshipInstanceResponseModel contentIdRelationshipInstanceHelperMap = new SaveRelationshipInstanceResponseModel();
    this.manageModifiedRelationship(saveRelationshipInstanceModel, baseEntityDAO, contentIdRelationshipInstanceHelperMap, entitiesWithoutDefaultImage);
    return contentIdRelationshipInstanceHelperMap;
  }

  private void manageModifiedRelationship(
      ISaveRelationshipInstanceStrategyModel saveRelationshipInstanceModel,
      IBaseEntityDAO baseEntityDAO, ISaveRelationshipInstanceResponseModel contentIdRelationshipInstanceHelperMap, List<Long> entitiesWithoutDefaultImage) throws Exception
  {
    List<IRelationshipDataTransferInfoModel> relationshipDataTransferModel = new ArrayList<>();
    RelationshipRecordBuilder relationshipRecordBuilder = new RelationshipRecordBuilder(baseEntityDAO,
        saveRelationshipInstanceModel.getConfigDetails(), this.rdbmsComponentUtils);

    List<IPropertyRecordDTO> relationshipPropertyRecords = this.manageNonNatureModifiedRelationship(saveRelationshipInstanceModel,
        baseEntityDAO, relationshipRecordBuilder, relationshipDataTransferModel);

    relationshipPropertyRecords.addAll(this.manageNatureModifiedRelationship(saveRelationshipInstanceModel, baseEntityDAO,
        relationshipRecordBuilder, relationshipDataTransferModel));

    contentIdRelationshipInstanceHelperMap.setDataTransferInfo(relationshipDataTransferModel);

    IPropertyRecordDTO[] propertyRecords = relationshipPropertyRecords.toArray(new IPropertyRecordDTO[relationshipPropertyRecords.size()]);

    baseEntityDAO.updatePropertyRecords(propertyRecords);
    entitiesWithoutDefaultImage.addAll(relationshipRecordBuilder.getEntitiesWithoutDefaultImage());
  }

  private List<IPropertyRecordDTO> manageNonNatureModifiedRelationship(ISaveRelationshipInstanceStrategyModel saveRelationshipInstanceModel,
      IBaseEntityDAO baseEntityDAO, RelationshipRecordBuilder relationshipRecordBuilder,
      List<IRelationshipDataTransferInfoModel> relationshipDataTransferModel) throws Exception
  {
    List<IPropertyRecordDTO> relationshipPropertyRecords = new ArrayList<>();
    List<IContentRelationshipInstanceModel> modifiedRelationships = saveRelationshipInstanceModel.getRelationshipAdm()
        .getModifiedRelationships();

    for (IContentRelationshipInstanceModel modifiedRelationship : modifiedRelationships) {
      try {
        IPropertyRecordDTO relationshipRecord = relationshipRecordBuilder.updateRelationshipRecord(modifiedRelationship,
            relationshipDataTransferModel);
        if (relationshipRecord != null) {
          relationshipPropertyRecords.add(relationshipRecord);
        }
      }
      catch (CardinalityException e) {
        throw e;
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }

    }
    return relationshipPropertyRecords;
  }

  private List<IPropertyRecordDTO> manageNatureModifiedRelationship(ISaveRelationshipInstanceStrategyModel saveRelationshipInstanceModel,
      IBaseEntityDAO baseEntityDAO, RelationshipRecordBuilder relationshipRecordBuilder,
      List<IRelationshipDataTransferInfoModel> relationshipDataTransferModel) throws Exception
  {
    List<IPropertyRecordDTO> relationshipPropertyRecords = new ArrayList<>();
    saveRelationshipInstanceModel.getRelationshipAdm().getModifiedNatureRelationships().forEach(modifiedRelationships -> {
      try {
        IPropertyRecordDTO relationshipRecord = relationshipRecordBuilder.updateRelationshipRecord(modifiedRelationships,
            relationshipDataTransferModel);
        if (relationshipRecord != null) {
          relationshipPropertyRecords.add(relationshipRecord);
        }
      }
      catch (CardinalityException e) {
        throw e;
      }
      catch (InstanceAlreadyPresentInAnotherLinkedVariantRelationshipException e) {
        throw new InstanceAlreadyPresentInAnotherLinkedVariantRelationshipException();
      }
      catch (DuplicateVariantExistsException e) {
        throw new DuplicateVariantExistsException();
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    return relationshipPropertyRecords;
  }

  protected IKlassInstanceTypeModel getKlassInstanceType(IBaseEntityDAO baseEntityDAO) throws Exception
  {
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    KlassInstanceTypeModel klassInstanceTypeModel = new KlassInstanceTypeModel();

    List<String> types = new ArrayList<>();
    types.add(baseEntityDTO.getNatureClassifier().getCode());
    klassInstanceTypeModel.setTypes(types);
    List<String> taxonomyIds = new ArrayList<>();
    ;
    List<IClassifierDTO> classifiers = baseEntityDAO.getClassifiers();
    for (IClassifierDTO classifier : classifiers) {
      if (classifier.getClassifierType().equals(ClassifierType.CLASS)) {
        types.add(classifier.getCode());
      }
      else {
        taxonomyIds.add(classifier.getCode());
      }
    }
    klassInstanceTypeModel.setSelectedTaxonomyIds(taxonomyIds);
    klassInstanceTypeModel.setName(baseEntityDTO.getBaseEntityName());
    klassInstanceTypeModel.setBaseType(IConfigMap.getBaseTypeClassPath(baseEntityDTO.getBaseType()));

    List<String> languageCodes = new ArrayList<>();
    languageCodes.add(baseEntityDTO.getBaseLocaleID());
    klassInstanceTypeModel.setLanguageCodes(languageCodes);

    return klassInstanceTypeModel;
  }

  protected IGetKlassInstanceModel executeGetKlassInstance(IBaseEntityDAO baseEntityDAO, IGetConfigDetailsModel configDetails,
      String tabType) throws Exception
  {
    KlassInstanceBuilder klassInstanceBuilder = new KlassInstanceBuilder(baseEntityDAO, configDetails, this.rdbmsComponentUtils);
    IKlassInstance klassInstance = klassInstanceBuilder.getKlassInstance();
    // KlassInstanceBuilder.fillEntityExtensionInAssetCoverFlowAttribute((List<IContentAttributeInstance>)klassInstance.getAttributes(),
    // baseEntityDAO.getBaseEntityDTO());
    IGetKlassInstanceModel model = null;
    switch (tabType) {
      case CommonConstants.CUSTOM_TEMPLATE_TAB_BASETYPE:
        model = new GetKlassInstanceForCustomTabModel();
        ((IGetKlassInstanceCustomTabModel) model).setAttributeVariantsStats(klassInstanceBuilder.getAttributeVariantsStats());
        model.setConfigDetails(configDetails);
        executeGetRelationshipInstance((IGetKlassInstanceCustomTabModel) model, (IGetConfigDetailsForCustomTabModel) configDetails,
            baseEntityDAO);
        break;
      case CommonConstants.TEMPLATE_TASKS_TAB_BASETYPE:
        model = new GetTaskInstanceResponseModel();
        break;
      case CommonConstants.TEMPLATE_TIME_LINE_TAB_BASETYPE:
        model = new GetKlassInstanceVersionForTimeLineModel();
        ((IGetKlassInstanceVersionsForTimeLineModel) model).getVersions().addAll(versionUtils.getObjectRevisions(
            baseEntityDAO.getBaseEntityDTO().getBaseEntityIID(), ((IGetConfigDetailsForCustomTabModel) configDetails).getNumberOfVersionsToMaintain(), 0,false));
        break;
      default:
        break;
    }

    model.setReferencedInstances(variantInstanceUtils.fillContextualData(configDetails, baseEntityDAO, klassInstance));
    model.setKlassInstance((IContentInstance) klassInstance);
    model.setConfigDetails(configDetails);
    return model;
  }

  private void handleDefaultImage(List<Long> entitesWithoutDefaultImage,
      IBaseEntityDAO sourceEntityDAO) throws Exception
  {
    long sourceEntityIID = sourceEntityDAO.getBaseEntityDTO().getBaseEntityIID();
    for (long entityIID : entitesWithoutDefaultImage) {
      IBaseEntityDAO dao = (entityIID == sourceEntityIID ? sourceEntityDAO : rdbmsComponentUtils.getBaseEntityDAO(entityIID));
      KlassInstanceUtils.handleDefaultImage(dao);
    }
  }

  private Boolean shouldCreateVersion(
      IGetConfigDetailsForCustomTabModel configDetails,
      ISaveRelationshipInstanceModel datamodel)
  {
    Map<String, IReferencedRelationshipModel> referencedRelationships = configDetails.getReferencedRelationships();
    Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships = configDetails.getReferencedNatureRelationships();
    List<IContentRelationshipInstanceModel> modifiedRelationships = datamodel.getModifiedRelationships();
    List<IContentRelationshipInstanceModel> modifiedNatureRelationships = datamodel.getModifiedNatureRelationships();
    modifiedRelationships.addAll(modifiedNatureRelationships);
    for (IContentRelationshipInstanceModel modifiedRelationship : modifiedRelationships) {
      String relationshipId = modifiedRelationship.getRelationshipId();
      IReferencedRelationshipModel referencedRelationship = referencedRelationships
          .get(relationshipId);
      if(referencedRelationship != null) {
        if (!referencedRelationship.getIsLite()) {
          return true;
        }
      }
      else {
        IGetReferencedNatureRelationshipModel referencedNatureRelationship = referencedNatureRelationships.get(relationshipId);
        if(referencedNatureRelationship !=null) {
          if (!referencedNatureRelationship.getIsLite()) {
            return true;
          }
        }
      }
    }
    return false;
  }
}

