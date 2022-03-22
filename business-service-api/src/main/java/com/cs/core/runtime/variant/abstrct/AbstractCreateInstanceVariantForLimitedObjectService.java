package com.cs.core.runtime.variant.abstrct;

import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPPriority;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.coupling.dto.ClassificationDataTransferDTO;
import com.cs.core.rdbms.coupling.dto.ContextualDataTransferDTO;
import com.cs.core.rdbms.coupling.dto.ContextualDataTransferGranularDTO;
import com.cs.core.rdbms.coupling.idto.IClassificationDataTransferDTO;
import com.cs.core.rdbms.coupling.idto.IContextualDataTransferDTO;
import com.cs.core.rdbms.coupling.idto.IContextualDataTransferGranularDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.interactor.exception.variants.UserNotHaveCreatePermissionForVariant;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeInfoModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceTypeInfoModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.variants.ICreateVariantForLimitedObjectRequestModel;
import com.cs.core.runtime.interactor.model.variants.ICreateVariantModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstanceInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForGetVariantInstancesInTableViewStrategy;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsWithoutPermissionsStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.VariantInstanceUtils;
import com.cs.core.runtime.strategy.usecase.configdetails.IGetConfigDetailsForContextualDataTransferStrategy;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.utils.BaseEntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@SuppressWarnings("unchecked")
@Component
public abstract class AbstractCreateInstanceVariantForLimitedObjectService<P extends ICreateVariantForLimitedObjectRequestModel, R extends IGetVariantInstancesInTableViewModel>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  protected ISessionContext                                            context;
  
  @Autowired
  protected IGetConfigDetailsWithoutPermissionsStrategy                getConfigDetailsWithoutPermissionsStrategy;
  
  @Autowired
  protected KlassInstanceUtils                                         klassInstanceUtils;
  
  @Autowired
  protected IGetConfigDetailsForGetVariantInstancesInTableViewStrategy getconfigDetailsForGetVariantInstancesInTableViewStrategy;
  
  @Autowired
  protected PermissionUtils                                            permissionUtils;
  
  @Autowired
  protected TransactionThreadData                                      transactionThread;
  
  @Autowired
  protected VariantInstanceUtils                                       variantInstanceUtils;
  
  @Autowired
  protected RDBMSComponentUtils                                        rdbmsComponentUtils;
  
  @Autowired
  protected IGetConfigDetailsForContextualDataTransferStrategy getConfigDetailsForContextualDataTransferStrategy;

  private static final String SERVICE_FOR_CDT = "CONTEXTUAL_DATA_TRANSFER_TASK";
  
  private static final String SERVICE         = "CLASSIFICATION_DATA_TRANSFER";

  protected abstract String getModuleEntityType();
  
  protected abstract Long getCounter();
  
  protected abstract BaseType getBaseType();
  
  protected abstract String getStringBaseType();

  protected abstract IGetVariantInstancesInTableViewModel executeGetTableView(IGetVariantInstanceInTableViewRequestModel tableViewRequestModel) throws Exception;
  
  public WorkflowUtils.UseCases getUsecase()
  {
    return WorkflowUtils.UseCases.UNDEFINED;
  }

  @Override
  protected R executeInternal(P createVariantForLimitedObjectRequestModel) throws Exception
  {
    ICreateVariantModel createVariantModel = createVariantForLimitedObjectRequestModel
        .getCreateVariantRequest();
    
    String immediateParentId = null;
    String parentVariantInstanceId = createVariantModel.getParentId();
    if (parentVariantInstanceId != null) {
      immediateParentId = parentVariantInstanceId;
    }
    else {
      immediateParentId = createVariantModel.getId();
    }

    String type = createVariantModel.getTypes()
        .get(0);
    createVariantModel.setType(type);
    Boolean canCreate = permissionUtils.isUserHasPermissionToCreate(type, "klass",
        getModuleEntityType());
    if (!canCreate) {
      throw new UserNotHaveCreatePermissionForVariant();
    }
    IBaseEntityDAO parentEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(Long.parseLong(createVariantModel.getParentId()));

    IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
    multiclassificationRequestModel.getKlassIds()
        .add(type);
    TransactionData transactionData = transactionThread.getTransactionData();
    multiclassificationRequestModel.setEndpointId(transactionData.getEndpointId());
    multiclassificationRequestModel.setPhysicalCatalogId(transactionData.getPhysicalCatalogId());
    multiclassificationRequestModel.setOrganizationId(transactionData.getOrganizationId());
    multiclassificationRequestModel.setUserId(transactionData.getUserId());

    // Needed for contextual data propagation
     multiclassificationRequestModel.setParentKlassIds(BaseEntityUtils.getAllReferenceTypeFromBaseEntity(parentEntityDAO));
     multiclassificationRequestModel.setParentTaxonomyIds(BaseEntityUtils.getReferenceTaxonomyIdsFromBaseEntity(parentEntityDAO.getClassifiers()));
     multiclassificationRequestModel.setUserId(transactionData.getUserId());

    IGetConfigDetailsForCustomTabModel configDetails = getConfigDetailsWithoutPermissionsStrategy
        .execute(multiclassificationRequestModel);

    String variantNames = createVariantModel.getName();
    String existingLanguageCode ="";
    /* String variantName = variantNames.substring(0, variantNames.length()-6);*/
    if (variantNames == null) {
      createVariantModel.setName(variantInstanceUtils.getNewVariantNameForCreate(configDetails.getReferencedKlasses(), type, getCounter(),
          rdbmsComponentUtils.getDataLanguage()));
    }
    else {
      if(variantNames.contains("_") && variantNames.length() > 6) {
        existingLanguageCode = variantNames.substring(variantNames.substring(0, variantNames.lastIndexOf("_")).lastIndexOf("_")+1);
      }
      if((parentEntityDAO.getBaseEntityDTO().getLocaleIds()).contains(existingLanguageCode)) {
        String  variantNameByCode = variantNames.substring(0, variantNames.length()-6);
        if(parentEntityDAO.getBaseEntityDTO().getChildLevel() > 1) {
          createVariantModel.setName("nested embedded " + variantNameByCode +"_" + rdbmsComponentUtils.getDataLanguage());
        }
        else {
          createVariantModel.setName(variantNameByCode + "_context_" + rdbmsComponentUtils.getDataLanguage());
        }
      }
      else if (parentEntityDAO.getBaseEntityDTO().getChildLevel() > 1) {
        createVariantModel.setName("nested embedded " + variantNames + "_context_" + rdbmsComponentUtils.getDataLanguage());
      }
      else {
        createVariantModel.setName(variantNames + "_context_" + rdbmsComponentUtils.getDataLanguage());
      }
    }
    IBaseEntityDTO varient =  variantInstanceUtils.createVariantBaseEntity(configDetails, createVariantModel, getBaseType());

    rdbmsComponentUtils.createNewRevision(parentEntityDAO.getBaseEntityDTO(), configDetails.getNumberOfVersionsToMaintainForParent());

    IGetVariantInstanceInTableViewRequestModel tableViewRequestModel = createVariantForLimitedObjectRequestModel
        .getTableViewRequest();

    initiateContextualDataTransfer(createVariantForLimitedObjectRequestModel, varient);

    rdbmsComponentUtils.getLocaleCatlogDAO().postUsecaseUpdate(varient.getBaseEntityIID(), IEventDTO.EventType.ELASTIC_UPDATE);
    
    initiateClassificationDataTransfer(varient.getBaseEntityIID(), varient.getNatureClassifier());
    
    R response = (R) executeGetTableView(tableViewRequestModel);
    IKlassInstanceInformationModel variantInformationModel = new KlassInstanceInformationModel();
    variantInformationModel.setId(String.valueOf(varient.getBaseEntityIID()));
    variantInformationModel.setBaseType(getStringBaseType());
    workflowUtils.executeBusinessProcessEvent(getUsecase().actionType, createVariantModel, variantInformationModel);
    return response;
  }

  private void initiateContextualDataTransfer(P createVariantForLimitedObjectRequestModel, IBaseEntityDTO varient)
      throws Exception
  {
    String contentID = createVariantForLimitedObjectRequestModel.getCreateVariantRequest().getParentId();
    IContextualDataTransferDTO dataTransferDTO = new ContextualDataTransferDTO();
    IContextualDataTransferGranularDTO bgpCouplingDTO = new ContextualDataTransferGranularDTO();
    bgpCouplingDTO.setParentBaseEntityIID((Long.parseLong(contentID)));
    bgpCouplingDTO.setVariantBaseEntityIID(varient.getBaseEntityIID());
    bgpCouplingDTO.setContextID(createVariantForLimitedObjectRequestModel.getCreateVariantRequest().getTypes().get(0));
    dataTransferDTO.setBGPCouplingDTOs(Arrays.asList(bgpCouplingDTO));
    
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.HIGH;
    dataTransferDTO.setLocaleID(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getLocaleID());
    dataTransferDTO.setCatalogCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getCatalogCode());
    dataTransferDTO.setOrganizationCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getOrganizationCode());
    dataTransferDTO.setUserId(transactionThread.getTransactionData().getUserId());
    BGPDriverDAO.instance().submitBGPProcess(rdbmsComponentUtils.getUserName(), SERVICE_FOR_CDT, "", userPriority,
        new JSONContent(dataTransferDTO.toJSON()));
  }

  protected IKlassInstanceTypeInfoModel getKlassInstanceType(IBaseEntityDAO baseEntityDAO)
      throws Exception
  {
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    IKlassInstanceTypeInfoModel klassInstanceTypeModel = new KlassInstanceTypeInfoModel();

    List<String> types = new ArrayList<>();
    types.add(baseEntityDTO.getNatureClassifier().getCode());
    klassInstanceTypeModel.setKlassIds(types);
    List<String> taxonomyIds = new ArrayList<>();;
    List<IClassifierDTO> classifiers = baseEntityDAO.getClassifiers();
    for(IClassifierDTO classifier : classifiers) {
      if(classifier.getClassifierType().equals(ClassifierType.CLASS)){
        types.add(classifier.getCode());
      }
      else {
        taxonomyIds.add(classifier.getCode());
      }
    }
    klassInstanceTypeModel.setTaxonomyIds(taxonomyIds);
    //klassInstanceTypeModel.setName(baseEntityDTO.getBaseEntityName());
    //klassInstanceTypeModel.setBaseType(IConfigMap.getBaseTypeClassPath(baseEntityDTO.getBaseType()));

    List<String> languageCodes = new ArrayList<>();
    languageCodes.add(baseEntityDTO.getBaseLocaleID());
    klassInstanceTypeModel.setLanguageCodes(languageCodes);

    return klassInstanceTypeModel;
  }

  private Map<Long, Map<String, List<String>>> getChildrenVSotherClassifiers(String parentId, String contextId) throws NumberFormatException, RDBMSException
  {
    ILocaleCatalogDAO localeCatalogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    
    List<Long> childrenIIDs = localeCatalogDAO.loadContextualEntityIIDs(Collections.singletonList(contextId), Long.valueOf(parentId));
    
    if(!childrenIIDs.isEmpty())
      return localeCatalogDAO.getEntityVSotherClassifierCodes(childrenIIDs.toArray(new Long[0]));
    
    return new HashMap<>();
  }
  
  private Map<String, IReferencedSectionElementModel> getReferencedElements(
      Map<String, Map<String, IReferencedSectionElementModel>> idVsReferencedElements)
  {
    Map<String, IReferencedSectionElementModel> referencedElements = new HashMap<>();
    
    idVsReferencedElements.values().forEach(map -> referencedElements.putAll(map));
    return referencedElements;
  }
  
  private void initiateClassificationDataTransfer(Long baseEntityIID, IClassifierDTO classifierDTO) throws Exception
  {
    IClassificationDataTransferDTO classificationDataTransfer = new ClassificationDataTransferDTO();
    ILocaleCatalogDTO localeCatalogDTO = rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO();
    classificationDataTransfer.setLocaleID(localeCatalogDTO.getLocaleID());
    classificationDataTransfer.setCatalogCode(localeCatalogDTO.getCatalogCode());
    classificationDataTransfer.setOrganizationCode(localeCatalogDTO.getOrganizationCode());
    classificationDataTransfer.setUserId(context.getUserId());
    classificationDataTransfer.setBaseEntityIID(baseEntityIID);
    classificationDataTransfer.getAddedOtherClassifiers().add(classifierDTO);

    BGPDriverDAO.instance().submitBGPProcess(context.getUserName(), SERVICE, "", BGPPriority.HIGH,
        new JSONContent(classificationDataTransfer.toJSON()));
  }
  
  /*private IBaseEntityDTO prepareDataAndCreateVariant(ICreateVariantModel createVariantModel) throws Exception
  {
    String type = createVariantModel.getTypes()
        .get(0);
    createVariantModel.setType(type);
    Boolean canCreate = permissionUtils.isUserHasPermissionToCreate(type, "klass",
        getModuleEntityType());
    if (!canCreate) {
      throw new UserNotHaveCreatePermissionForVariant();
    }
    
    IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
    multiclassificationRequestModel.getKlassIds()
        .add(type);
    TransactionData transactionData = transactionThread.getTransactionData();
    multiclassificationRequestModel.setEndpointId(transactionData.getEndpointId());
    multiclassificationRequestModel.setPhysicalCatalogId(transactionData.getPhysicalCatalogId());
    multiclassificationRequestModel.setOrganizationId(transactionData.getOrganizationId());
    multiclassificationRequestModel.setUserId(transactionData.getUserId());
    
    // Needed for contextual data propagation
     multiclassificationRequestModel.setParentKlassIds(BaseEntityUtils.getAllReferenceTypeFromBaseEntity(parentEntityDAO));
     multiclassificationRequestModel.setParentTaxonomyIds(BaseEntityUtils.getReferenceTaxonomyIdsFromBaseEntity(parentEntityDAO.getClassifiers()));
     multiclassificationRequestModel.setUserId(transactionData.getUserId());
    
    IGetConfigDetailsForCustomTabModel configDetails = getConfigDetailsWithoutPermissionsStrategy
        .execute(multiclassificationRequestModel);
    
    if (createVariantModel.getName() == null) {
      createVariantModel.setName(variantInstanceUtils
          .getNewVariantNameForCreate(configDetails.getReferencedKlasses(), type, getCounter()));
    }
    return variantInstanceUtils.createVariantBaseEntity(configDetails, createVariantModel, getBaseType());
  }*/
}
