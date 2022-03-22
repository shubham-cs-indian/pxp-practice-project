package com.cs.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.constants.CommonConstants;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.dto.AutoCreateVariantDTO;
import com.cs.core.bgprocess.dto.BGPTaxonomyInheritanceDTO;
import com.cs.core.bgprocess.dto.EntityRelationshipInfoDTO;
import com.cs.core.bgprocess.dto.RelationshipInheritanceInfoDTO;
import com.cs.core.bgprocess.dto.RelationshipInheritanceOnNatureRelationchangeDTO;
import com.cs.core.bgprocess.dto.RelationshipInheritanceOnTypeSwitchDTO;
import com.cs.core.bgprocess.dto.TaxonomyInheritanceOnTypeSwitchDTO;
import com.cs.core.bgprocess.idto.IAutoCreateVariantDTO;
import com.cs.core.bgprocess.idto.IBGPTaxonomyInheritanceDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IEntityRelationshipInfoDTO;
import com.cs.core.bgprocess.idto.IRelationshipInheritanceInfoDTO;
import com.cs.core.bgprocess.idto.IRelationshipInheritanceOnNatureRelationchangeDTO;
import com.cs.core.bgprocess.idto.IRelationshipInheritanceOnTypeSwitchDTO;
import com.cs.core.bgprocess.idto.ITaxonomyInheritanceOnTypeSwitchDTO;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.model.asset.IAssetConfigurationDetailsResponseModel;
import com.cs.core.config.interactor.model.asset.IAssetExtensionConfigurationModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableWrapperModel;
import com.cs.core.config.interactor.model.variantcontext.TechnicalImageVariantWithAutoCreateEnableWrapperModel;
import com.cs.core.config.strategy.usecase.relationship.GetNatureRelationshipInfoForRelationshipInheritanceStrategy;
import com.cs.core.config.strategy.usecase.relationship.GetRelationshipInheritanceInfoOnNatureRelationshipChangeStartegy;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.coupling.dto.BGPCouplingDTO;
import com.cs.core.rdbms.coupling.dto.ClassificationDataTransferDTO;
import com.cs.core.rdbms.coupling.dto.ContextualDataTransferDTO;
import com.cs.core.rdbms.coupling.dto.ContextualDataTransferGranularDTO;
import com.cs.core.rdbms.coupling.dto.LanguageInheritanceDTO;
import com.cs.core.rdbms.coupling.dto.RelationshipDataTransferDTO;
import com.cs.core.rdbms.coupling.idao.ICouplingDAO;
import com.cs.core.rdbms.coupling.idto.IBGPCouplingDTO;
import com.cs.core.rdbms.coupling.idto.IClassificationDataTransferDTO;
import com.cs.core.rdbms.coupling.idto.IContextualDataTransferDTO;
import com.cs.core.rdbms.coupling.idto.IContextualDataTransferGranularDTO;
import com.cs.core.rdbms.coupling.idto.ICouplingDTO;
import com.cs.core.rdbms.coupling.idto.ILanguageInheritanceDTO;
import com.cs.core.rdbms.coupling.idto.IRelationshipDataTransferDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.relationship.dto.RelationCoupleRecordDTO.RelationCoupleRecordDTOBuilder;
import com.cs.core.rdbms.relationship.idao.IRelationCoupleRecordDAO;
import com.cs.core.rdbms.relationship.idto.IRelationCoupleRecordDTO;
import com.cs.core.runtime.interactor.entity.datarule.ITaxonomyConflict.Setting;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRelationshipVersion;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInstanceSaveModel;
import com.cs.core.runtime.interactor.model.configdetails.IGetConfigDetailsForSaveRelationshipInstancesResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.instance.IContentRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipDataTransferInfoModel;
import com.cs.core.runtime.interactor.model.relationship.RelationshipDataTransferInfoModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.usecase.asset.IFetchAssetConfigurationDetails;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.workflow.trigger.standard.IApplicationTriggerModel;
import com.cs.di.workflow.trigger.standard.IApplicationTriggerModel.ApplicationActionType;
import com.cs.utils.BaseEntityUtils;
import com.cs.utils.dam.AssetUtils;


@Component
public class DataTransferBGPModelBuilder {

  @Autowired
  protected RDBMSComponentUtils rdbmsComponentUtils;

  @Autowired
  protected TransactionThreadData transactionThread;

  @Autowired
  protected ISessionContext       context;
  
  @Autowired
  protected Long articleKlassCounter;
  
  @Autowired
  protected IFetchAssetConfigurationDetails       fetchAssetConfigurationDetails;
  
  @Autowired
  GetNatureRelationshipInfoForRelationshipInheritanceStrategy getNatureRelationshipInfoForRelationshipInheritanceStrategy;
  
  @Autowired
  GetRelationshipInheritanceInfoOnNatureRelationshipChangeStartegy getRelationshipInheritanceInfoOnNatureRelationshipChangeStartegy;

  private static final String                                      SERVICE_FOR_RELATIONSHIP_DATA_TRANSFER                          = "RELATIONSHIP_DATA_TRANSFER";
  private static final String                                      SERVICE_FOR_CLASSIFICATION_DATA_TRANSFER                        = "CLASSIFICATION_DATA_TRANSFER";
  private static final String                                      SERVICE_FOR_CONTEXTUAL_DATA_TRANSFER                            = "CONTEXTUAL_DATA_TRANSFER_TASK";
  private static final String                                      INITIATE_RELATIONSHIP_INHERITANCE_ON_TYPE_SWITCH                = "INITIATE_RELATIONSHIP_INHERITANCE_ON_TYPE_SWITCH";
  private static final String                                      INITIATE_RELATIONSHIP_INHERITANCE_ON_NON_NATURE_RELATION_CHANGE = "INITIATE_RELATIONSHIP_INHERITANCE_ON_NON_NATURE_RELATION_CHANGE";
  private static final String                                      INITIATE_RELATIONSHIP_INHERITANCE_ON_NATURE_RELATION_CHANGE     = "INITIATE_RELATIONSHIP_INHERITANCE_ON_NATURE_RELATION_CHANGE";
  private static final String                                      TAXONOMY_INHERITANCE                                            = "TAXONOMY_INHERITANCE";
  private static final String                                      TAXONOMY_INHERITANCE_ON_TYPE_SWITCH                             = "TAXONOMY_INHERITANCE_ON_TYPE_SWITCH";
  private static final String                                      SERVICE_FOR_LANG_INHERITANCE                                    = "LANGUAGE_INHERITANCE_TASK";
  private static final String                                      SERVICE_FOR_RUNTIME_CLEANUP                                     = "RUNTIME_CLEANUP";
  private static final IBGProcessDTO.BGPPriority                   BGP_PRIORITY                                                    = IBGProcessDTO.BGPPriority.HIGH;

  public void prepareDataForRelationshipDataTransfer(IBaseEntityDAO baseEntityDAO,
      List<IContentRelationshipInstanceModel> modifiedRelationships, List<IContentRelationshipInstanceModel> modifiedNatureRelationships)
      throws Exception
  {

    List<IBGPCouplingDTO> bgpCouplingDTOs = new ArrayList<>();

    for (IContentRelationshipInstanceModel modifiedRelationship : modifiedRelationships) {
      IBGPCouplingDTO bgpCouplingDTO = new BGPCouplingDTO();
      fillSourceAndTargetIIDs(baseEntityDAO, modifiedRelationship, bgpCouplingDTO);

      bgpCouplingDTO.setRelationshipId(modifiedRelationship.getRelationshipId());
      bgpCouplingDTOs.add(bgpCouplingDTO);
    }

    for (IContentRelationshipInstanceModel modifiedNatureRelationship : modifiedNatureRelationships) {
      IBGPCouplingDTO bgpCouplingDTO = new BGPCouplingDTO();
      fillSourceAndTargetIIDs(baseEntityDAO, modifiedNatureRelationship, bgpCouplingDTO);

      bgpCouplingDTO.setNatureRelationshipId(modifiedNatureRelationship.getRelationshipId());
      bgpCouplingDTOs.add(bgpCouplingDTO);
    }

    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.HIGH;
    IRelationshipDataTransferDTO entryData = new RelationshipDataTransferDTO();
    entryData.setBGPCouplingDTOs(bgpCouplingDTOs);
    ILocaleCatalogDAO localeCatlogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    ILocaleCatalogDTO localeCatalogDTO = localeCatlogDAO.getLocaleCatalogDTO();
    entryData.setLocaleID(localeCatalogDTO.getLocaleID());
    entryData.setCatalogCode(localeCatalogDTO.getCatalogCode());
    entryData.setOrganizationCode(localeCatalogDTO.getOrganizationCode());
    entryData.setUserId(transactionThread.getTransactionData().getUserId());
    BGPDriverDAO.instance()
        .submitBGPProcess(rdbmsComponentUtils.getUserName(), SERVICE_FOR_RELATIONSHIP_DATA_TRANSFER, "", userPriority, new JSONContent(entryData.toJSON()));
  }

  private void fillSourceAndTargetIIDs(IBaseEntityDAO baseEntityDAO, IContentRelationshipInstanceModel modifiedRelationship,
      IBGPCouplingDTO bgpCouplingDTO)
  {
    List<Long> sourceCouplingIIDs = new ArrayList<>();
    List<Long> addedEntityIIDs = new ArrayList<>();
    List<Long> deletedEntityIIDs = new ArrayList<>();

    sourceCouplingIIDs.add(baseEntityDAO.getBaseEntityDTO().getBaseEntityIID());
    bgpCouplingDTO.setSourceBaseEntityIIDs(sourceCouplingIIDs);
    List<IRelationshipVersion> addedElements = modifiedRelationship.getAddedElements();

    for (IRelationshipVersion element : addedElements) {
      addedEntityIIDs.add(Long.parseLong(element.getId()));
    }
    bgpCouplingDTO.setAddedEntityIIDs(addedEntityIIDs);

    List<String> deletedElements = modifiedRelationship.getDeletedElements();

    for (String deleteElement : deletedElements) {
      deletedEntityIIDs.add(Long.parseLong(deleteElement));
    }

    bgpCouplingDTO.setDeletedEntityIIDs(deletedEntityIIDs);
    bgpCouplingDTO.setSideId(modifiedRelationship.getSideId());
  }


  public void initiateClassificationDataTransfer(ILocaleCatalogDAO localeCatalogDAO, Long baseEntityIID,
      Set<IClassifierDTO> addedClassifiers, Set<IClassifierDTO> removedClassifiers) throws Exception
  {
    IClassificationDataTransferDTO classificationDataTransfer = new ClassificationDataTransferDTO();
    ILocaleCatalogDTO localeCatalogDTO = localeCatalogDAO.getLocaleCatalogDTO();
    classificationDataTransfer.setLocaleID(localeCatalogDTO.getLocaleID());
    classificationDataTransfer.setCatalogCode(localeCatalogDTO.getCatalogCode());
    classificationDataTransfer.setOrganizationCode(localeCatalogDTO.getOrganizationCode());
    classificationDataTransfer.setUserId(context.getUserId());
    classificationDataTransfer.setBaseEntityIID(baseEntityIID);
    classificationDataTransfer.setAddedOtherClassifiers(addedClassifiers);
    classificationDataTransfer.setRemovedOtherClassifiers(removedClassifiers);

    BGPDriverDAO.instance().submitBGPProcess(context.getUserName(), SERVICE_FOR_CLASSIFICATION_DATA_TRANSFER, "", BGP_PRIORITY,
        new JSONContent(classificationDataTransfer.toJSON()));
  }

  public void prepareDataForContextualDataTransferTask(long baseEntityIID, ICouplingDAO couplingDAO)
      throws Exception
  {
    List<ICouplingDTO> otherSideEntitiesForContextual = couplingDAO.getOtherSideEntitesForContextual(baseEntityIID);
    if(otherSideEntitiesForContextual.isEmpty()) {
      return;
    }

    IContextualDataTransferDTO contextualDataTransferDTO = new ContextualDataTransferDTO();
    for (ICouplingDTO contextualInstance : otherSideEntitiesForContextual) {

      IContextualDataTransferGranularDTO dataTransferDTO = new ContextualDataTransferGranularDTO();
      dataTransferDTO.setParentBaseEntityIID(contextualInstance.getSourceEntityIID());
      dataTransferDTO.setVariantBaseEntityIID(contextualInstance.getTargetEntityIID());
      String contextID = ConfigurationDAO.instance().getClassifierByIID(contextualInstance.getCouplingSourceIID()).getClassifierCode();
      dataTransferDTO.setContextID(contextID);
      contextualDataTransferDTO.getBGPCouplingDTOs().add(dataTransferDTO);
    }

    IBGProcessDTO.BGPPriority userPriority3 = IBGProcessDTO.BGPPriority.HIGH;
    contextualDataTransferDTO.setLocaleID(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getLocaleID());
    contextualDataTransferDTO.setCatalogCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getCatalogCode());
    contextualDataTransferDTO.setOrganizationCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getOrganizationCode());
    contextualDataTransferDTO.setUserId(rdbmsComponentUtils.getUserID());
    BGPDriverDAO.instance().submitBGPProcess(rdbmsComponentUtils.getUserName(), SERVICE_FOR_CONTEXTUAL_DATA_TRANSFER, "", userPriority3,
        new JSONContent(contextualDataTransferDTO.toJSON()));
  }
  
  /**
   * @param baseEntityDAO
   * @param modifiedRelationships
   * @param modifiedNatureRelationships
   * @throws Exception
   */
  public void relationshipDataCoulpingAfterTransfer(IBaseEntityDAO baseEntityDAO,
      List<IContentRelationshipInstanceModel> modifiedRelationships, List<IContentRelationshipInstanceModel> modifiedNatureRelationships)
      throws Exception
  {

    List<IBGPCouplingDTO> bgpCouplingDTOs = new ArrayList<>();
    for (IContentRelationshipInstanceModel modifiedRelationship : modifiedRelationships) {
      IBGPCouplingDTO bgpCouplingDTO = new BGPCouplingDTO();
      fillSourceAndTargetIIDs(baseEntityDAO, modifiedRelationship, bgpCouplingDTO);

      bgpCouplingDTO.setRelationshipId(modifiedRelationship.getRelationshipId());
      bgpCouplingDTOs.add(bgpCouplingDTO);
    }

    for (IContentRelationshipInstanceModel modifiedNatureRelationship : modifiedNatureRelationships) {
      IBGPCouplingDTO bgpCouplingDTO = new BGPCouplingDTO();
      fillSourceAndTargetIIDs(baseEntityDAO, modifiedNatureRelationship, bgpCouplingDTO);

      bgpCouplingDTO.setNatureRelationshipId(modifiedNatureRelationship.getRelationshipId());
      bgpCouplingDTOs.add(bgpCouplingDTO);
    }

    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.HIGH;
    IRelationshipDataTransferDTO entryData = new RelationshipDataTransferDTO();
    entryData.setBGPCouplingDTOs(bgpCouplingDTOs);
    entryData.setLocaleID(baseEntityDAO.getLocaleCatalog().getLocaleCatalogDTO().getLocaleID());
    entryData.setCatalogCode(baseEntityDAO.getLocaleCatalog().getLocaleCatalogDTO().getCatalogCode());
    entryData.setOrganizationCode(baseEntityDAO.getLocaleCatalog().getLocaleCatalogDTO().getOrganizationCode());
    entryData.setUserId(transactionThread.getTransactionData().getUserId());
    BGPDriverDAO.instance()
        .submitBGPProcess(rdbmsComponentUtils.getUserName(), SERVICE_FOR_RELATIONSHIP_DATA_TRANSFER, "", userPriority, new JSONContent(entryData.toJSON()));
  }
  
  /**
   * @param baseEntityIID
   * @param targetLocaleCatalogDAO
   * @throws Exception
   */
  public void contextualDataCoulpingAfterTransfer(long baseEntityIID, ILocaleCatalogDAO targetLocaleCatalogDAO)
      throws Exception
  {
    List<ICouplingDTO> otherSideEntitiesForContextual = targetLocaleCatalogDAO.openCouplingDAO().getOtherSideEntitesForContextual(baseEntityIID);
    if(otherSideEntitiesForContextual.isEmpty()) {
      return;
    }

    IContextualDataTransferDTO contextualDataTransferDTO = new ContextualDataTransferDTO();
    for (ICouplingDTO contextualInstance : otherSideEntitiesForContextual) {

      IContextualDataTransferGranularDTO dataTransferDTO = new ContextualDataTransferGranularDTO();
      dataTransferDTO.setParentBaseEntityIID(contextualInstance.getSourceEntityIID());
      dataTransferDTO.setVariantBaseEntityIID(contextualInstance.getTargetEntityIID());
      String contextID = ConfigurationDAO.instance().getClassifierByIID(contextualInstance.getCouplingSourceIID()).getClassifierCode();
      dataTransferDTO.setContextID(contextID);
      contextualDataTransferDTO.getBGPCouplingDTOs().add(dataTransferDTO);
    }

    IBGProcessDTO.BGPPriority userPriority3 = IBGProcessDTO.BGPPriority.HIGH;
    contextualDataTransferDTO.setLocaleID(targetLocaleCatalogDAO.getLocaleCatalogDTO().getLocaleID());
    contextualDataTransferDTO.setCatalogCode(targetLocaleCatalogDAO.getLocaleCatalogDTO().getCatalogCode());
    contextualDataTransferDTO.setOrganizationCode(targetLocaleCatalogDAO.getLocaleCatalogDTO().getOrganizationCode());
    contextualDataTransferDTO.setUserId(rdbmsComponentUtils.getUserID());
    BGPDriverDAO.instance().submitBGPProcess(rdbmsComponentUtils.getUserName(), SERVICE_FOR_CONTEXTUAL_DATA_TRANSFER, "", userPriority3,
        new JSONContent(contextualDataTransferDTO.toJSON()));
  }
  

  /**
   * Data is prepared and  
   * Triggers RelationshipInheritanceSwitchType Event
   * For Import and Transfer 
   * on Taxonomy/Class Create/Update action
   * when article is Imported
   * @param baseEntityDAO
   * @param addedClassifiers
   * @param removedClassifiers
   * @param transfer 
   * @throws Exception
   */
  public void prepareDataForRelationshipInheritanceSwitchType(IBaseEntityDAO baseEntityDAO, List<IClassifierDTO> addedClassifiers,
      List<IClassifierDTO> removedClassifiers, ApplicationActionType transfer) throws Exception
  {
    ILocaleCatalogDTO localeCatalogDTO = null;
    //To handle Transfer scenario
    if (IApplicationTriggerModel.ApplicationActionType.TRANSFER.equals(transfer)) {
      localeCatalogDTO = baseEntityDAO.getLocaleCatalog().getLocaleCatalogDTO();
    }
    else {
      localeCatalogDTO = rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO();
    }
    List<String> existingKlassIds = BaseEntityUtils.getAllReferenceTypeFromBaseEntity(baseEntityDAO);
    List<String> existingTaxonomyIds = BaseEntityUtils.getReferenceTaxonomyIdsFromBaseEntity(baseEntityDAO.getClassifiers());
    IKlassInstanceTypeSwitchModel typeSwitchModel = new KlassInstanceTypeSwitchModel();
    if (!addedClassifiers.isEmpty() || !removedClassifiers.isEmpty()) {
      List<String> addedSecondaryTypes = new ArrayList<String>();
      List<String> deletedSecondaryTypes = new ArrayList<String>();
      List<String> addedTaxonomyIds = new ArrayList<String>();
      List<String> deletedTaxonomyIds = new ArrayList<String>();
      
      fillTypesAndTaxonomyIds(addedClassifiers, addedSecondaryTypes, addedTaxonomyIds);
      
      fillTypesAndTaxonomyIds(removedClassifiers, deletedSecondaryTypes, deletedTaxonomyIds);
      
      typeSwitchModel.setKlassInstanceId(Long.toString(baseEntityDAO.getBaseEntityDTO().getBaseEntityIID()));
      typeSwitchModel.setAddedSecondaryTypes(addedSecondaryTypes);
      typeSwitchModel.setAddedTaxonomyIds(addedTaxonomyIds);
      typeSwitchModel.setDeletedSecondaryTypes(deletedSecondaryTypes);
      typeSwitchModel.setDeletedTaxonomyIds(deletedTaxonomyIds);
      //Once Data is prepared Inheritance is Initiated using call
      initiateRelationshipInheritanceSwitchType(typeSwitchModel, existingKlassIds, existingTaxonomyIds,
          typeSwitchModel.getAddedTaxonomyIds(), typeSwitchModel.getAddedSecondaryTypes(),localeCatalogDTO);
    }
  }
  
  /**
   * Using Classifiers it puts data
   * in two separate list for SecondaryType and Taxonomy
   * @param addedClassifiers
   * @param addedSecondaryTypes
   * @param addedTaxonomyIds
   */
  private void fillTypesAndTaxonomyIds(List<IClassifierDTO> addedClassifiers,
      List<String> addedSecondaryTypes, List<String> addedTaxonomyIds)
  {
    addedClassifiers.forEach(classifier -> {
      ClassifierType classifierType = classifier.getClassifierType();
      if (classifierType.equals(ClassifierType.CLASS)) {
        addedSecondaryTypes.add(classifier.getCode());
      }
      else {
        addedTaxonomyIds.add(classifier.getCode());
      }
    });
  }
  
  /**
   * It Triggers RelationshipInheritanceSwitchType Event
   * @param typeSwitchModel
   * @param existingKlassIds
   * @param existingTaxonomyIds
   * @param addedTaxonomyIds
   * @param addedTypes
   * @throws Exception
   */
  public void initiateRelationshipInheritanceSwitchType(IKlassInstanceTypeSwitchModel typeSwitchModel, List<String> existingKlassIds,
      List<String> existingTaxonomyIds, List<String> addedTaxonomyIds, List<String> addedTypes)
      throws Exception
  {
    ILocaleCatalogDTO localeCatalogDTO = rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO();
    IRelationshipInheritanceOnTypeSwitchDTO relationshipInheritanceOnTypeSwitchDTO = new RelationshipInheritanceOnTypeSwitchDTO();
    relationshipInheritanceOnTypeSwitchDTO.setContentId(Long.parseLong(typeSwitchModel.getKlassInstanceId()));
    relationshipInheritanceOnTypeSwitchDTO.setExistingTaxonomies(existingTaxonomyIds);
    relationshipInheritanceOnTypeSwitchDTO.setExistingTypes(existingKlassIds);
    relationshipInheritanceOnTypeSwitchDTO.setAddedTaxonomies(typeSwitchModel.getAddedTaxonomyIds());
    relationshipInheritanceOnTypeSwitchDTO.setAddedTypes(addedTypes);
    relationshipInheritanceOnTypeSwitchDTO.setRemovedTaxonomies(typeSwitchModel.getDeletedTaxonomyIds());
    relationshipInheritanceOnTypeSwitchDTO.setRemovedTypes(typeSwitchModel.getDeletedSecondaryTypes());
    relationshipInheritanceOnTypeSwitchDTO.setLocaleID(localeCatalogDTO.getLocaleID());
    relationshipInheritanceOnTypeSwitchDTO.setCatalogCode(localeCatalogDTO.getCatalogCode());
    relationshipInheritanceOnTypeSwitchDTO.setOrganizationCode(localeCatalogDTO.getOrganizationCode());
    relationshipInheritanceOnTypeSwitchDTO.setUserId(context.getUserId());
    
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.MEDIUM;
    BGPDriverDAO.instance().submitBGPProcess(context.getUserSessionDTO().getUserName(), INITIATE_RELATIONSHIP_INHERITANCE_ON_TYPE_SWITCH,
        "", userPriority, new JSONContent(relationshipInheritanceOnTypeSwitchDTO.toJSON()));
  }
  
  /**
   * It Triggers RelationshipInheritanceSwitchType Event
   * @param typeSwitchModel
   * @param existingKlassIds
   * @param existingTaxonomyIds
   * @param addedTaxonomyIds
   * @param addedTypes
   * @throws Exception
   */
  private void initiateRelationshipInheritanceSwitchType(IKlassInstanceTypeSwitchModel typeSwitchModel, List<String> existingKlassIds,
      List<String> existingTaxonomyIds, List<String> addedTaxonomyIds, List<String> addedTypes, ILocaleCatalogDTO localeCatalogDTO)
      throws Exception
  {    
    IRelationshipInheritanceOnTypeSwitchDTO relationshipInheritanceOnTypeSwitchDTO = new RelationshipInheritanceOnTypeSwitchDTO();
    relationshipInheritanceOnTypeSwitchDTO.setContentId(Long.parseLong(typeSwitchModel.getKlassInstanceId()));
    relationshipInheritanceOnTypeSwitchDTO.setExistingTaxonomies(existingTaxonomyIds);
    relationshipInheritanceOnTypeSwitchDTO.setExistingTypes(existingKlassIds);
    relationshipInheritanceOnTypeSwitchDTO.setAddedTaxonomies(typeSwitchModel.getAddedTaxonomyIds());
    relationshipInheritanceOnTypeSwitchDTO.setAddedTypes(addedTypes);
    relationshipInheritanceOnTypeSwitchDTO.setRemovedTaxonomies(typeSwitchModel.getDeletedTaxonomyIds());
    relationshipInheritanceOnTypeSwitchDTO.setRemovedTypes(typeSwitchModel.getDeletedSecondaryTypes());
    relationshipInheritanceOnTypeSwitchDTO.setLocaleID(localeCatalogDTO.getLocaleID());
    relationshipInheritanceOnTypeSwitchDTO.setCatalogCode(localeCatalogDTO.getCatalogCode());
    relationshipInheritanceOnTypeSwitchDTO.setOrganizationCode(localeCatalogDTO.getOrganizationCode());
    relationshipInheritanceOnTypeSwitchDTO.setUserId(context.getUserId());
    
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.MEDIUM;
    BGPDriverDAO.instance().submitBGPProcess(context.getUserSessionDTO().getUserName(), INITIATE_RELATIONSHIP_INHERITANCE_ON_TYPE_SWITCH,
        "", userPriority, new JSONContent(relationshipInheritanceOnTypeSwitchDTO.toJSON()));
  }
  
  /**
   * It Prepares IRelationshipDataTransferInfoModel 
   * using IRelationsSetDTO
   * @param relationshipDataTransferModel
   * @param relationsSetDTO
   * @param addedElement
   * @param isNature
   */
  public void fillDataForRelationshipInheritanceForAddedAndModifiedElements(List<IRelationshipDataTransferInfoModel> relationshipDataTransferModel,
      IRelationsSetDTO relationsSetDTO, List<IRelationshipVersion> addedElement, Boolean isNature)
  {
    IRelationshipDataTransferInfoModel dataTransferModel = new RelationshipDataTransferInfoModel();
    dataTransferModel.setContentId(relationsSetDTO.getEntityIID());
    if (isNature) {
      dataTransferModel.getChangedNatureRelationshipIds().add(relationsSetDTO.getProperty().getCode());
    }
    else {
      dataTransferModel.getChangedRelationshipIds().add(relationsSetDTO.getProperty().getCode());
    }

    Map<String, IEntityRelationshipInfoDTO> relationshipIdAddedDeletedElementsMap = dataTransferModel
        .getRelationshipIdAddedDeletedElementsMap();
    IEntityRelationshipInfoDTO addedRemovedElements = relationshipIdAddedDeletedElementsMap
        .get(relationsSetDTO.getProperty().getPropertyIID() + "__" + relationsSetDTO.getProperty().getRelationSide());
    if (addedRemovedElements == null) {
      addedRemovedElements = new EntityRelationshipInfoDTO();
      relationshipIdAddedDeletedElementsMap.put(
          relationsSetDTO.getProperty().getPropertyCode() + "__" + relationsSetDTO.getProperty().getRelationSide(), addedRemovedElements);
    }
    
    if (!(addedElement == null || addedElement.isEmpty())) {
      for (IRelationshipVersion elm : addedElement) {
        if (elm != null && elm.getId() != null)
          addedRemovedElements.getAddedElements().add(Long.parseLong(elm.getId()));
      }
    }
    relationshipDataTransferModel.add(dataTransferModel);
  }
  
  /**
   * It Prepares IRelationshipDataTransferInfoModel 
   * using IRelationsSetDTO for delete scenario
   * @param relationshipDataTransferModel
   * @param relationsSetDTO
   * @param addedElement
   * @param isNature
   */
  public void fillDataForRelationshipInheritanceForDeletedElements(List<IRelationshipDataTransferInfoModel> relationshipDataTransferModel,
      IRelationsSetDTO relationsSetDTO, Set<Long> deletedEntityIID, Boolean isNature)
  {
    IRelationshipDataTransferInfoModel dataTransferModel = new RelationshipDataTransferInfoModel();

    dataTransferModel.setContentId(relationsSetDTO.getEntityIID());
    if (isNature) {
      dataTransferModel.getChangedNatureRelationshipIds().add(relationsSetDTO.getProperty().getCode());
    }
    else {
      dataTransferModel.getChangedRelationshipIds().add(relationsSetDTO.getProperty().getCode());
    }

    Map<String, IEntityRelationshipInfoDTO> relationshipIdAddedDeletedElementsMap = dataTransferModel
        .getRelationshipIdAddedDeletedElementsMap();
    IEntityRelationshipInfoDTO addedRemovedElements = relationshipIdAddedDeletedElementsMap
        .get(relationsSetDTO.getProperty().getPropertyIID() + "__" + relationsSetDTO.getProperty().getRelationSide());
    if (addedRemovedElements == null) {
      addedRemovedElements = new EntityRelationshipInfoDTO();
      relationshipIdAddedDeletedElementsMap.put(
          relationsSetDTO.getProperty().getPropertyCode() + "__" + relationsSetDTO.getProperty().getRelationSide(), addedRemovedElements);
      for (Long deletedEntity : deletedEntityIID) {
        addedRemovedElements.getRemovedElements().add(deletedEntity);
      }
    }

    relationshipDataTransferModel.add(dataTransferModel);
  }
  
  /**
   * Prepare Model and Triggers RelationshipInheritance
   * for NonNature / Nature Relationship Change 
   * @param dataTransferInfo
   * @param sourceEntityId
   * @param isManuallyCreated
   * @throws Exception
   */
  public void prepareDataForRelationshipInheritance(List<IRelationshipDataTransferInfoModel> dataTransferInfo,
      long sourceEntityId, Boolean isManuallyCreated,ApplicationActionType transfer,IBaseEntityDAO baseEntityDAO) throws Exception {
    
    ILocaleCatalogDTO localeCatalogDTO = null;
    //To handle Transfer scenario
    if (IApplicationTriggerModel.ApplicationActionType.TRANSFER.equals(transfer)) {
      localeCatalogDTO = baseEntityDAO.getLocaleCatalog().getLocaleCatalogDTO();
    }
    else {
      localeCatalogDTO = rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO();
    }

    for (IRelationshipDataTransferInfoModel relationshipDataTransferInfo : dataTransferInfo) {
      if (!relationshipDataTransferInfo.getContentId().equals(sourceEntityId)) {
        continue;
      }

      IRelationshipInheritanceOnNatureRelationchangeDTO relationshipInheritanceOnNatureRelationshipChangeModel = new RelationshipInheritanceOnNatureRelationchangeDTO();
      relationshipInheritanceOnNatureRelationshipChangeModel.setSourceContentId(relationshipDataTransferInfo.getContentId());//relationsSetDTO.getEntityIID()
      Map<String, IEntityRelationshipInfoDTO> relationshipIdAddedDeletedElementsMap = relationshipDataTransferInfo
          .getRelationshipIdAddedDeletedElementsMap();
      Set<String> changedRelationshipIds = (Set<String>) relationshipDataTransferInfo.getChangedRelationshipIds();
      Set<String> changedNatureRelationshipIds = (Set<String>) relationshipDataTransferInfo.getChangedNatureRelationshipIds();
      List<IEntityRelationshipInfoDTO> natureRelationshipSaveResponse = new  ArrayList<IEntityRelationshipInfoDTO>();
      IRelationshipInheritanceInfoDTO relationshipInfoForNonNatureRelationship = new RelationshipInheritanceInfoDTO();
      relationshipInfoForNonNatureRelationship.setSourceContentId(relationshipDataTransferInfo.getContentId());

      for (Entry<String, IEntityRelationshipInfoDTO> entry : relationshipIdAddedDeletedElementsMap.entrySet()) {
        String relationshipId_sideId = entry.getKey();
        IEntityRelationshipInfoDTO entityRelationshipInfo = entry.getValue();
        String[] split = relationshipId_sideId.split("__");
        String relationshipId = split[0];
        String sideId = split[1];

        if (changedRelationshipIds.contains(relationshipId)) {
          relationshipInfoForNonNatureRelationship
              .setLocaleID(localeCatalogDTO.getLocaleID());
          relationshipInfoForNonNatureRelationship
              .setCatalogCode(localeCatalogDTO.getCatalogCode());
          relationshipInfoForNonNatureRelationship
              .setOrganizationCode(localeCatalogDTO.getOrganizationCode());
          relationshipInfoForNonNatureRelationship.setUserId(transactionThread.getTransactionData().getUserId());
          entityRelationshipInfo.setRelationshipId(relationshipId);
          entityRelationshipInfo.setSideId(sideId);
          relationshipInfoForNonNatureRelationship.getEntityRelationshipInfo().add(entityRelationshipInfo);
        }

        if (changedNatureRelationshipIds.contains(relationshipId)) {
          entityRelationshipInfo.setNatureRelationshipId(relationshipId);
          natureRelationshipSaveResponse.add(entityRelationshipInfo);
        }

      }

      if (!relationshipInfoForNonNatureRelationship.getEntityRelationshipInfo().isEmpty()) {
        IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.MEDIUM;
        BGPDriverDAO.instance().submitBGPProcess(context.getUserSessionDTO().getUserName(),
            INITIATE_RELATIONSHIP_INHERITANCE_ON_NON_NATURE_RELATION_CHANGE, "", userPriority,
            new JSONContent(relationshipInfoForNonNatureRelationship.toJSON()));
      }

      relationshipInheritanceOnNatureRelationshipChangeModel
          .setLocaleID(localeCatalogDTO.getLocaleID());
      relationshipInheritanceOnNatureRelationshipChangeModel
          .setCatalogCode(localeCatalogDTO.getCatalogCode());
      relationshipInheritanceOnNatureRelationshipChangeModel
          .setOrganizationCode(localeCatalogDTO.getOrganizationCode());
      relationshipInheritanceOnNatureRelationshipChangeModel.setUserId(transactionThread.getTransactionData().getUserId());
      relationshipInheritanceOnNatureRelationshipChangeModel.setEntityRelationshipInfo(natureRelationshipSaveResponse);
      relationshipInheritanceOnNatureRelationshipChangeModel.setIsManuallyCreated(isManuallyCreated);

      if (!relationshipInheritanceOnNatureRelationshipChangeModel.getEntityRelationshipInfo().isEmpty()) {
        IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.MEDIUM;
        BGPDriverDAO.instance().submitBGPProcess(context.getUserSessionDTO().getUserName(),
            INITIATE_RELATIONSHIP_INHERITANCE_ON_NATURE_RELATION_CHANGE, "", userPriority,
            new JSONContent(relationshipInheritanceOnNatureRelationshipChangeModel.toJSON()));
      }
    }
  } 
  /**
   * This method prepare the data  for initiate the Taxonomy Inheritance while entities are created
   * @param modifiedNatureRelationships
   * @param sourceEntityIID
   * @param configDetails
   * @throws RDBMSException
   * @throws Exception
   * @throws CSFormatException
   */
  public void prepareDataForTaxonomyInheritance(List<IContentRelationshipInstanceModel> modifiedNatureRelationships, Long sourceEntityIID,
      IModel configDetails) throws RDBMSException, Exception, CSFormatException
  {    Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships =null ;
    if(configDetails instanceof IGetConfigDetailsForSaveRelationshipInstancesResponseModel) {
      referencedNatureRelationships = ((IGetConfigDetailsForSaveRelationshipInstancesResponseModel) configDetails).getReferencedNatureRelationships();
    }else if(configDetails instanceof IGetConfigDetailsForCustomTabModel){
      referencedNatureRelationships = ((IGetConfigDetailsForCustomTabModel) configDetails).getReferencedNatureRelationships();
    }
   
    for (IContentRelationshipInstanceModel modifiedNatureRelationshipInstance : modifiedNatureRelationships) {
      String relationshipId = modifiedNatureRelationshipInstance.getRelationshipId();
      IGetReferencedNatureRelationshipModel referencedNatureRelationship = referencedNatureRelationships.get(relationshipId);
      String relationshipType = referencedNatureRelationship.getRelationshipType();
      Setting taxonomyInheritanceSetting = referencedNatureRelationship.getTaxonomyInheritanceSetting();
      if (!relationshipType.equals(CommonConstants.PRODUCT_VARIANT_RELATIONSHIP) || taxonomyInheritanceSetting.equals(Setting.off)) {
        continue;
      }
      long propertyIID = ConfigurationDAO.instance().getPropertyByCode(relationshipId).getPropertyIID();
      List<IRelationshipVersion> addedElements = modifiedNatureRelationshipInstance.getAddedElements();
      List<String> deletedElements = modifiedNatureRelationshipInstance.getDeletedElements();
      if (addedElements.isEmpty() && deletedElements.isEmpty()) {
        continue;
      }

      initiateTaxonomyInheritance(sourceEntityIID, taxonomyInheritanceSetting, propertyIID, addedElements, deletedElements);
      break;
    }
  }
  
  /**
   * responsible to initiate Taxonomy Inheritance
   * @param sourceEntityIID
   * @param taxonomyInheritanceSetting
   * @param propertyIID
   * @param addedElements
   * @param deletedElements
   * @throws Exception
   * @throws RDBMSException
   * @throws CSFormatException
   */
  public void initiateTaxonomyInheritance(Long sourceEntityIID, Setting taxonomyInheritanceSetting, long propertyIID,
      List<IRelationshipVersion> addedElements, List<String> deletedElements) throws Exception, RDBMSException, CSFormatException
  {
    IBGPTaxonomyInheritanceDTO bgpTaxonomyInheritanceDTO = new BGPTaxonomyInheritanceDTO();

    ILocaleCatalogDAO localeCatlogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    ILocaleCatalogDTO localeCatalogDTO = localeCatlogDAO.getLocaleCatalogDTO();
    bgpTaxonomyInheritanceDTO.setLocaleID(localeCatalogDTO.getLocaleID());
    bgpTaxonomyInheritanceDTO.setCatalogCode(localeCatalogDTO.getCatalogCode());
    bgpTaxonomyInheritanceDTO.setOrganizationCode(localeCatalogDTO.getOrganizationCode());
    bgpTaxonomyInheritanceDTO.setUserId(transactionThread.getTransactionData().getUserId());
    bgpTaxonomyInheritanceDTO.setSourceEntityIID(sourceEntityIID);
    bgpTaxonomyInheritanceDTO.setPropertyIID(propertyIID);
    bgpTaxonomyInheritanceDTO.setTaxonomyInheritanceSetting(taxonomyInheritanceSetting.toString());

    for (IRelationshipVersion addedElement : addedElements) {
      bgpTaxonomyInheritanceDTO.getAddedElementIIDs().add(Long.parseLong(addedElement.getId()));
    }

    for (String elementId : deletedElements) {
      bgpTaxonomyInheritanceDTO.getDeletedElementIIDs().add(Long.parseLong(elementId));
    }

    BGPDriverDAO.instance().submitBGPProcess(context.getUserSessionDTO().getUserName(), TAXONOMY_INHERITANCE, "", IBGProcessDTO.BGPPriority.LOW,
        new JSONContent(bgpTaxonomyInheritanceDTO.toJSON()));
  }
  
  /**
   * This Method responsible for initiate the Taxonomy Inheritance after Type switch
   * and submit to BGP.
   * @param typeSwitchModel
   * @param configDetails
   * @param baseEntityIID
   * @throws Exception
   */
  public void initiateTaxonomyInheritance(IKlassInstanceTypeSwitchModel typeSwitchModel,
      IGetConfigDetailsForCustomTabModel configDetails, long baseEntityIID)
      throws Exception
  {
    Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships = configDetails.getReferencedNatureRelationships();
    Map<String, IReferencedSectionElementModel> referencedElements = configDetails.getReferencedElements();
    List<String> addedTaxonomyIds = filterMinorTaxonomies(typeSwitchModel.getAddedTaxonomyIds());
    List<String> removedTaxonomyIds = filterMinorTaxonomies(typeSwitchModel.getDeletedTaxonomyIds());
    
    if(addedTaxonomyIds.isEmpty() && removedTaxonomyIds.isEmpty()) {
      return;
    }
    
    for (IGetReferencedNatureRelationshipModel natureRelationship : referencedNatureRelationships.values()) {
      String relationshipType = natureRelationship.getRelationshipType();
      if(!relationshipType.equals(CommonConstants.PRODUCT_VARIANT_RELATIONSHIP)) {
        continue;
      }
      
      String side1ElementId = natureRelationship.getSide1().getElementId();
      if(!referencedElements.containsKey(side1ElementId)) {
        break;
      }
      
      Setting taxonomyInheritanceSetting = natureRelationship.getTaxonomyInheritanceSetting();
      if(!taxonomyInheritanceSetting.equals(Setting.off)) {
        prepareAndSubmitBGProcessForTaxonomyInheritance(baseEntityIID, addedTaxonomyIds,
            removedTaxonomyIds, natureRelationship.getId(), taxonomyInheritanceSetting);
        break;
      }
    }
  }
  /**
   * This method return the taxonomyIds after excluding the Manor Taxonomy 
   * @param taxonomyIds
   * @return List of Taxonomy ids
   * @throws RDBMSException
   */
  private List<String> filterMinorTaxonomies(List<String> taxonomyIds) throws RDBMSException
  {
    List<String> filteredTaxonomyIds = new ArrayList<>();
    ConfigurationDAO configDao = ConfigurationDAO.instance();
    for (String taxonomyId : taxonomyIds) {
      IClassifierDTO classifierByCode = configDao.getClassifierByCode(taxonomyId);
      if(!classifierByCode.getClassifierType().equals(ClassifierType.MINOR_TAXONOMY)) {
        filteredTaxonomyIds.add(taxonomyId);
      }
    }
    return filteredTaxonomyIds;
  } 
  /**
   * This method is responsible for submit to background job for TaxonomyInheritance
   * @param baseEntityIID
   * @param addedTaxonomyIds
   * @param removedTaxonomyIds
   * @param relationshipId
   * @param taxonomyInheritanceSetting
   * @throws Exception
   */
  private void prepareAndSubmitBGProcessForTaxonomyInheritance(long baseEntityIID,
      List<String> addedTaxonomyIds, List<String> removedTaxonomyIds,
      String relationshipId, Setting taxonomyInheritanceSetting)
      throws Exception
  {
    ILocaleCatalogDTO localeCatalogDTO = rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO();
    ITaxonomyInheritanceOnTypeSwitchDTO dto = new TaxonomyInheritanceOnTypeSwitchDTO();
    dto.setAddedTaxonomyIds(addedTaxonomyIds);
    dto.setRemovedTaxonomyIds(removedTaxonomyIds);
    dto.setTaxonomyInheritanceSetting(taxonomyInheritanceSetting.name());
    dto.setSourceEntityIID(baseEntityIID);
    dto.setRelationshipId(relationshipId);
    dto.setLocaleID(localeCatalogDTO.getLocaleID());
    dto.setCatalogCode(localeCatalogDTO.getCatalogCode());
    dto.setOrganizationCode(localeCatalogDTO.getOrganizationCode());
    dto.setUserId(context.getUserId());    
    
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.LOW;
    BGPDriverDAO.instance()
        .submitBGPProcess(context.getUserSessionDTO()
            .getUserName(), TAXONOMY_INHERITANCE_ON_TYPE_SWITCH, "", userPriority,
            new JSONContent(dto.toJSON()));
  }
  /**
   * initiated when an article instance is created in a new language (when isBackgroundCallForLanguageInheritance is false)
   * AND in case of Type switch (when isBackgroundCallForLanguageInheritance is true)
   * @param localeCatalogDAO
   * @param baseEntityIID
   * @param dependentPropertyIIDs
   * @throws Exception
   */
  public void initiateLanguageInheritanceDataTransfer(ILocaleCatalogDAO localeCatalogDAO,
      Long baseEntityIID, List<Long> dependentPropertyIIDs, boolean isBackgroundCallForLanguageInheritance) throws Exception
  {
    ILanguageInheritanceDTO languageInheritanceDTO = new LanguageInheritanceDTO();
    ILocaleCatalogDTO localeCatalogDTO = localeCatalogDAO.getLocaleCatalogDTO();
    languageInheritanceDTO.setDependentPropertyIIDs(dependentPropertyIIDs);
    languageInheritanceDTO.setBaseEntityIID(baseEntityIID);
    if (isBackgroundCallForLanguageInheritance) {
      ICouplingDAO couplingDAO = localeCatalogDAO.openCouplingDAO();
      List<String> languageCodes = couplingDAO.getlanguageCodesByBaseEntityIIDs(baseEntityIID,
          baseEntityIID);
      languageInheritanceDTO.getLocaleIIDs().addAll(languageCodes);
    }
    else {
      languageInheritanceDTO.getLocaleIIDs().add(localeCatalogDTO.getLocaleID());
    }
    languageInheritanceDTO.setLocaleID(localeCatalogDTO.getLocaleID());
    languageInheritanceDTO.setCatalogCode(localeCatalogDTO.getCatalogCode());
    languageInheritanceDTO.setOrganizationCode(localeCatalogDTO.getOrganizationCode());
    languageInheritanceDTO.setUserId(transactionThread.getTransactionData()
        .getUserId());
    BGPDriverDAO.instance()
        .submitBGPProcess(context.getUserName(), SERVICE_FOR_LANG_INHERITANCE, "", BGP_PRIORITY,
            new JSONContent(languageInheritanceDTO.toJSON()));
  }
  
  public void initiateTaxonomyInheritanceTransferTypeSwitch(IKlassInstanceTypeSwitchModel typeSwitchModel,
      IGetConfigDetailsForCustomTabModel configDetails, long baseEntityIID,
      IBaseEntityDAO targetEntityDAO ) throws Exception
  {

    Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships = configDetails.getReferencedNatureRelationships();
    Map<String, IReferencedSectionElementModel> referencedElements = configDetails.getReferencedElements();
    List<String> addedTaxonomyIds = filterMinorTaxonomies(typeSwitchModel.getAddedTaxonomyIds());
    List<String> removedTaxonomyIds = filterMinorTaxonomies(typeSwitchModel.getDeletedTaxonomyIds());
    
    if(addedTaxonomyIds.isEmpty() && removedTaxonomyIds.isEmpty()) {
      return;
    }
    
    for (IGetReferencedNatureRelationshipModel natureRelationship : referencedNatureRelationships.values()) {
      String relationshipType = natureRelationship.getRelationshipType();
      if(!relationshipType.equals(CommonConstants.PRODUCT_VARIANT_RELATIONSHIP)) {
        continue;
      }
      
      String side1ElementId = natureRelationship.getSide1().getElementId();
      if(!referencedElements.containsKey(side1ElementId)) {
        break;
      }
      
      Setting taxonomyInheritanceSetting = natureRelationship.getTaxonomyInheritanceSetting();
      if(!taxonomyInheritanceSetting.equals(Setting.off)) {
        prepareAndSubmitBGProcessForTaxonomyInheritanceTransferTypeSwitch(baseEntityIID, addedTaxonomyIds,
            removedTaxonomyIds, natureRelationship.getId(), taxonomyInheritanceSetting,targetEntityDAO);
        break;
      }
    }
  
    
  }
  private void prepareAndSubmitBGProcessForTaxonomyInheritanceTransferTypeSwitch(long baseEntityIID,
      List<String> addedTaxonomyIds, List<String> removedTaxonomyIds,
      String relationshipId, Setting taxonomyInheritanceSetting,IBaseEntityDAO targetEntityDAO)
      throws Exception
  {
   // ILocaleCatalogDTO localeCatalogDTO = rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO();
    ILocaleCatalogDTO localeCatalogDTO =  targetEntityDAO.getLocaleCatalog().getLocaleCatalogDTO();
    ITaxonomyInheritanceOnTypeSwitchDTO dto = new TaxonomyInheritanceOnTypeSwitchDTO();
    dto.setAddedTaxonomyIds(addedTaxonomyIds);
    dto.setRemovedTaxonomyIds(removedTaxonomyIds);
    dto.setTaxonomyInheritanceSetting(taxonomyInheritanceSetting.name());
    dto.setSourceEntityIID(baseEntityIID);
    dto.setRelationshipId(relationshipId);
    dto.setLocaleID(localeCatalogDTO.getLocaleID());
    dto.setCatalogCode(localeCatalogDTO.getCatalogCode());
    dto.setOrganizationCode(localeCatalogDTO.getOrganizationCode());
    dto.setUserId(context.getUserId());    
    
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.LOW;
    BGPDriverDAO.instance()
        .submitBGPProcess(context.getUserSessionDTO()
            .getUserName(), TAXONOMY_INHERITANCE_ON_TYPE_SWITCH, "", userPriority,
            new JSONContent(dto.toJSON()));
  }

  public void prepareDataForTaxonomyInheritanceTransfer(
      List<IContentRelationshipInstanceModel> modifiedNatureRelationships, long baseEntityIID,
      IGetConfigDetailsForCustomTabModel configDetails, IBaseEntityDAO targetEntityDAO) throws CSFormatException, Exception
  {
    Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships =null ;
    if(configDetails instanceof IGetConfigDetailsForSaveRelationshipInstancesResponseModel) {
      referencedNatureRelationships = ((IGetConfigDetailsForSaveRelationshipInstancesResponseModel) configDetails).getReferencedNatureRelationships();
    }else if(configDetails instanceof IGetConfigDetailsForCustomTabModel){
      referencedNatureRelationships = ((IGetConfigDetailsForCustomTabModel) configDetails).getReferencedNatureRelationships();
    }
   
    for (IContentRelationshipInstanceModel modifiedNatureRelationshipInstance : modifiedNatureRelationships) {
      String relationshipId = modifiedNatureRelationshipInstance.getRelationshipId();
      IGetReferencedNatureRelationshipModel referencedNatureRelationship = referencedNatureRelationships.get(relationshipId);
      String relationshipType = referencedNatureRelationship.getRelationshipType();
      Setting taxonomyInheritanceSetting = referencedNatureRelationship.getTaxonomyInheritanceSetting();
      if (!relationshipType.equals(CommonConstants.PRODUCT_VARIANT_RELATIONSHIP) || taxonomyInheritanceSetting.equals(Setting.off)) {
        continue;
      }
      long propertyIID = ConfigurationDAO.instance().getPropertyByCode(relationshipId).getPropertyIID();
      List<IRelationshipVersion> addedElements = modifiedNatureRelationshipInstance.getAddedElements();
      List<String> deletedElements = modifiedNatureRelationshipInstance.getDeletedElements();
      if (addedElements.isEmpty() && deletedElements.isEmpty()) {
        continue;
      }

      initiateTaxonomyInheritanceTransfer(baseEntityIID, taxonomyInheritanceSetting, propertyIID, addedElements, deletedElements, targetEntityDAO);
      break;
    }
    
  }

  private void initiateTaxonomyInheritanceTransfer(long baseEntityIID,
      Setting taxonomyInheritanceSetting, long propertyIID,
      List<IRelationshipVersion> addedElements, List<String> deletedElements,
      IBaseEntityDAO targetEntityDAO) throws RDBMSException, CSFormatException
  {
    IBGPTaxonomyInheritanceDTO bgpTaxonomyInheritanceDTO = new BGPTaxonomyInheritanceDTO();

    bgpTaxonomyInheritanceDTO.setLocaleID(targetEntityDAO.getLocaleCatalog().getLocaleCatalogDTO().getLocaleID());
    bgpTaxonomyInheritanceDTO.setCatalogCode(targetEntityDAO.getLocaleCatalog().getLocaleCatalogDTO().getCatalogCode());
    bgpTaxonomyInheritanceDTO.setOrganizationCode(targetEntityDAO.getLocaleCatalog().getLocaleCatalogDTO().getOrganizationCode());
    bgpTaxonomyInheritanceDTO.setUserId(transactionThread.getTransactionData().getUserId());
    bgpTaxonomyInheritanceDTO.setSourceEntityIID(baseEntityIID);
    bgpTaxonomyInheritanceDTO.setPropertyIID(propertyIID);
    bgpTaxonomyInheritanceDTO.setTaxonomyInheritanceSetting(taxonomyInheritanceSetting.toString());

    for (IRelationshipVersion addedElement : addedElements) {
      bgpTaxonomyInheritanceDTO.getAddedElementIIDs().add(Long.parseLong(addedElement.getId()));
    }

    for (String elementId : deletedElements) {
      bgpTaxonomyInheritanceDTO.getDeletedElementIIDs().add(Long.parseLong(elementId));
    }

    BGPDriverDAO.instance().submitBGPProcess(context.getUserSessionDTO().getUserName(), TAXONOMY_INHERITANCE, "", IBGProcessDTO.BGPPriority.LOW,
        new JSONContent(bgpTaxonomyInheritanceDTO.toJSON()));
    
  } 
  
  /**
   * initiated during Transfer functionality for Language inheritance when an article instance is created in multiple languages
   * @param localeCatalogDAO
   * @param baseEntityIID
   * @param dependentPropertyIIDs
   * @param translationsLocaleIds
   * @throws Exception
   */
  public void initiateBulkLanguageInheritanceDataTransfer(ILocaleCatalogDAO localeCatalogDAO,
      Long baseEntityIID, List<Long> dependentPropertyIIDs, List<String> translationsLocaleIds) throws Exception
  {
    ILanguageInheritanceDTO languageInheritanceDTO = new LanguageInheritanceDTO();
    languageInheritanceDTO.setDependentPropertyIIDs(dependentPropertyIIDs);
    languageInheritanceDTO.setBaseEntityIID(baseEntityIID);
    languageInheritanceDTO.getLocaleIIDs().addAll(translationsLocaleIds);
    ILocaleCatalogDTO localeCatalogDTO = localeCatalogDAO.getLocaleCatalogDTO();
    languageInheritanceDTO.setLocaleID(localeCatalogDTO.getLocaleID());
    languageInheritanceDTO.setCatalogCode(localeCatalogDTO.getCatalogCode());
    languageInheritanceDTO.setOrganizationCode(localeCatalogDTO.getOrganizationCode());
    languageInheritanceDTO.setUserId(transactionThread.getTransactionData().getUserId());
    BGPDriverDAO.instance()
        .submitBGPProcess(context.getUserName(), SERVICE_FOR_LANG_INHERITANCE, "", BGP_PRIORITY,
            new JSONContent(languageInheritanceDTO.toJSON()));
  }

  /**
   * fetch language dependent property IIDs
   * 
   * @param configDetails
   * @return
   */
  public List<Long> fetchDependentPropertyIIDs(IGetConfigDetailsForCustomTabModel configDetails)
  {
    List<Long> dependentPropertyIIDs = new ArrayList<>();
    Map<String, IAttribute> referencedAttributes = configDetails.getReferencedAttributes();
    
    for (IAttribute referencedAttribute : referencedAttributes.values()) {
      if (referencedAttribute.getIsTranslatable()) {
        dependentPropertyIIDs.add(referencedAttribute.getPropertyIID());
      }
    }
    return dependentPropertyIIDs;
  }
  
  public TransactionThreadData getTransactionThread()
  {
    return transactionThread;
  }
  
  public void setTransactionThread(TransactionThreadData transactionThread)
  {
    this.transactionThread = transactionThread;
  }
  
  public Long getCounter()
  {
    return articleKlassCounter++;
  }
  /**
   * 
   * @param baseEntityDTO
   * @return
   * @throws Exception
   */
  public IKlassInstanceInformationModel fillklassInstanceInformationModel(
      IBaseEntityDTO baseEntityDTO) throws Exception
  {
    IKlassInstanceInformationModel klassInstanceInformationModel = KlassInstanceBuilder
        .getKlassInstanceInformationModel(baseEntityDTO, rdbmsComponentUtils);
    return klassInstanceInformationModel;
  }
  
  /**
   * initiated for data cleanup which is no longer related with content after removal of NN class/taxonomy.
   * @param baseEntityIID
   * @param localCatalogDTO 
   * @param existingTypeIds
   * @param existingTaxonomyIds
   * @param removedTypes
   * @param removedTaxonomies
   * @throws Exception
   */
  public void prepareDataForRuntimeCleanupTask(long baseEntityIID, ILocaleCatalogDTO localCatalogDTO, List<String> updatedTypeIds,
      List<String>  updatedTaxonomyIds, List<String> removedTypes, List<String> removedTaxonomies)
      throws Exception
  {
    IRelationshipInheritanceOnTypeSwitchDTO cleanupDTO = new RelationshipInheritanceOnTypeSwitchDTO();
    cleanupDTO.setContentId(baseEntityIID);
    cleanupDTO.setExistingTypes(updatedTypeIds);
    cleanupDTO.setExistingTaxonomies(updatedTaxonomyIds);
    cleanupDTO.setRemovedTypes(removedTypes);
    cleanupDTO.setRemovedTaxonomies(removedTaxonomies);
    cleanupDTO.setLocaleID(localCatalogDTO.getLocaleID());
    cleanupDTO.setCatalogCode(localCatalogDTO.getCatalogCode());
    cleanupDTO.setOrganizationCode(localCatalogDTO.getOrganizationCode());
    cleanupDTO.setUserId(rdbmsComponentUtils.getUserID());
    
    BGPDriverDAO.instance()
        .submitBGPProcess(rdbmsComponentUtils.getUserName(), SERVICE_FOR_RUNTIME_CLEANUP, "",
            IBGProcessDTO.BGPPriority.HIGH, new JSONContent(cleanupDTO.toJSON()));
  }
  
  /**
   * separates taxonomyIds and non-nature klass Ids from classifiers
   * @param classifier
   * @param typeIds
   * @param taxonomyIds
   */
  public void getTaxonomyAndTypeIds(List<IClassifierDTO> classifier, List<String> typeIds, List<String> taxonomyIds)
  {
    for (IClassifierDTO classifierDTO : classifier) {
      switch (classifierDTO.getClassifierType()) {
        case CLASS:
          typeIds.add(classifierDTO.getCode());
          break;
        case TAXONOMY:
        case HIERARCHY:
          taxonomyIds.add(classifierDTO.getCode());
          break;
        default:
          break;
      }
    }
  }

  /**
   * prepare data before initiating runtime cleanup for type switch
   * @param baseEntityDAO
   * @param otherClassifiers
   * @param removeClassifiers
   * @throws Exception
   */
  public void setDataForRuntimeCleanup(IBaseEntityDAO baseEntityDAO,
      Set<IClassifierDTO> otherClassifiers, Set<IClassifierDTO> removeClassifiers) throws Exception
  {
    List<String> updatedTaxonomies = new ArrayList<>();
    List<String> updatedklasses = new ArrayList<>();
    List<String> removedTaxonomies = new ArrayList<>();
    List<String> removedKlasses = new ArrayList<>();
    getTaxonomyAndTypeIds(otherClassifiers.stream().collect(Collectors.toList()), updatedklasses, updatedTaxonomies);
    // Add nature type to klass list as expected
    updatedklasses.add(baseEntityDAO.getBaseEntityDTO().getNatureClassifier().getCode());
    getTaxonomyAndTypeIds(removeClassifiers.stream().collect(Collectors.toList()), removedKlasses, removedTaxonomies);
    prepareDataForRuntimeCleanupTask(baseEntityDAO.getBaseEntityDTO().getBaseEntityIID(),
        baseEntityDAO.getLocaleCatalog().getLocaleCatalogDTO(), updatedklasses, updatedTaxonomies, removedKlasses, removedTaxonomies);
  }
  
  /**
   * Transfer usecase 
   * Prepare Conflict relationship using source 
   * and update same for Target
   * @param couplingType
   * @param propagableRelationshipSideIds
   * @param relationshipId
   * @param natureRelationshipId
   * @param sourceContentId
   * @param targetContentId
   * @param catalogDao
   * @throws RDBMSException
   */
  public void prepareConflictAndUpdateInCoupleTable(String couplingType, List<String> propagableRelationshipSideIds, Long relationshipId,
      Long natureRelationshipId, Long sourceContentId, Long targetContentId, ILocaleCatalogDAO catalogDao) throws RDBMSException
  {
    IRelationCoupleRecordDAO relationCoupleRecordDao = catalogDao.openRelationCoupleRecordDAO();
    for (String propagableRelationshipId : propagableRelationshipSideIds) {
      
      IRelationCoupleRecordDTO exsitingRelationCoupleRecord = new RelationCoupleRecordDTOBuilder().targetEntityId(targetContentId)
          .propagableRelationshipId(natureRelationshipId).propagableRelationshipSideId(propagableRelationshipId).build();
      StringBuilder filterQuery = relationCoupleRecordDao.getFilterQuery(exsitingRelationCoupleRecord);
      IRelationCoupleRecordDTO relationCoupleRecord = null;
      if (CommonConstants.TIGHTLY_COUPLED.equals(couplingType)) {
        relationCoupleRecord = new RelationCoupleRecordDTOBuilder().sourceEntityId(sourceContentId.longValue())
            .targetEntityId(targetContentId).natureRelationshipId(natureRelationshipId.longValue())
            .propagableRelationshipId(relationshipId.longValue()).isResolved(false).propagableRelationshipSideId(propagableRelationshipId)
            .couplingType(IRelationCoupleRecordDTO.CouplingType.valueOf(couplingType)).build();
      }
      else {
        relationCoupleRecord = new RelationCoupleRecordDTOBuilder().sourceEntityId(sourceContentId.longValue())
            .targetEntityId(targetContentId).natureRelationshipId(natureRelationshipId.longValue())
            .propagableRelationshipId(relationshipId.longValue()).isResolved(true).propagableRelationshipSideId(propagableRelationshipId)
            .couplingType(IRelationCoupleRecordDTO.CouplingType.valueOf(couplingType)).build();
        
      }
      if (relationCoupleRecordDao.fetchRelationCoupleRecord(filterQuery).isEmpty()) {
        relationCoupleRecordDao.createRelationCoupleRecord(relationCoupleRecord);
      }
      else {
        relationCoupleRecordDao.updateRelationCoupledRecord(relationCoupleRecord);
      }
    }
  }
  
  /**
   * Transfer usecase to Clear Conflict in Target
   * if source transfer Article by resolving conflict
   * @param entityIID
   * @param relationCoupleRecordDao
   * @param relationProperty
   * @throws Exception
   * @throws RDBMSException
   */
  public void clearConflictIfExistOnTargetEntity(IRelationsSetDTO relationSet) throws Exception, RDBMSException
  {
    IRelationCoupleRecordDAO relationCoupleRecordDao = rdbmsComponentUtils.getLocaleCatlogDAO().openRelationCoupleRecordDAO();
    IPropertyDTO relationProperty = ConfigurationDAO.instance().getPropertyByCode(relationSet.getProperty().getPropertyCode());
    if (isConflictExistOnTargetEntity(relationSet.getEntityIID(), relationProperty, relationCoupleRecordDao)) {
      StringBuilder filterQuery = new StringBuilder();
      IRelationCoupleRecordDTO relationCoupleRecord = new RelationCoupleRecordDTOBuilder().targetEntityId(relationSet.getEntityIID())
          .propagableRelationshipId(relationProperty.getPropertyIID()).build();
      filterQuery = relationCoupleRecordDao.getFilterQuery(relationCoupleRecord);
      relationCoupleRecordDao.deleteRelationCoupleRecord(filterQuery);
    }
  }
  
  /**
   * Transfer usecase to check if Target has conflict 
   * @param otherSideEntityIID
   * @param relationProperty
   * @param relationCoupleRecordDao
   * @return
   * @throws Exception
   */
  private boolean isConflictExistOnTargetEntity(long otherSideEntityIID, IPropertyDTO relationProperty,
      IRelationCoupleRecordDAO relationCoupleRecordDao) throws Exception
  {
    IRelationCoupleRecordDTO relationCoupleRecord = new RelationCoupleRecordDTOBuilder().targetEntityId(otherSideEntityIID)
        .propagableRelationshipId(relationProperty.getPropertyIID()).isResolved(false)
        .couplingType(IRelationCoupleRecordDTO.CouplingType.tightlyCoupled).build();
    StringBuilder filterQuery = relationCoupleRecordDao.getFilterQuery(relationCoupleRecord);
    if (!relationCoupleRecordDao.fetchRelationCoupleRecord(filterQuery).isEmpty()) {
      return true;
    }
    return false;
  }
 
  /**
   * 
   * @param configDetails
   * @param coverFlowAttribute
   * @param klassInstancesModel
   * @param parentId
   * @param returnModel
   * @throws Exception
   */
  public void prepareDataAndCreateImageVariants(IGetConfigDetailsModel configDetails, IAssetInformationModel coverFlowAttribute,
      IAssetInstanceSaveModel klassInstancesModel, String parentId, IGetKlassInstanceModel returnModel, String endpointCode) throws Exception {
   
    String instanceId = klassInstancesModel.getId();
    List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantContextsWithAutoCreateEnable = configDetails
        .getTechnicalImageVariantContextWithAutoCreateEnable();
    if (technicalImageVariantContextsWithAutoCreateEnable.isEmpty()) {
      return;
    }
    
    if (!coverFlowAttribute.getType()
        .equals("Image")) {
      return;
    }
    IAssetConfigurationDetailsResponseModel assetModel = fetchAssetConfigurationDetails
        .execute(new IdParameterModel());
    String fileName = coverFlowAttribute.getFileName();
    String instanceName = FilenameUtils.getBaseName(fileName);
    String extension = FilenameUtils.getExtension(fileName);
    IAssetExtensionConfigurationModel extensionConfiguration = AssetUtils
        .getExtensionConfigurationFromList(assetModel.getExtensionConfiguration()
            .get(CommonConstants.MAM_NATURE_TYPE_IMAGE), extension);
    if (extensionConfiguration != null && !extensionConfiguration.getExtractRendition()) {
      // ExceptionUtil.addFailureDetailsToFailureObject(returnModel.getWarnings(),
      // new RenditionNotProcessedException(), instanceId, fileName);
      return;
    }
    
    ITechnicalImageVariantWithAutoCreateEnableWrapperModel contextsWithAutoCreateEnableWrapperModel = new TechnicalImageVariantWithAutoCreateEnableWrapperModel();
    contextsWithAutoCreateEnableWrapperModel.setTechnicalImageVariantWithAutoCreateEnable(
        technicalImageVariantContextsWithAutoCreateEnable);
    contextsWithAutoCreateEnableWrapperModel.setFileName(instanceName);
    contextsWithAutoCreateEnableWrapperModel.setIsSave(true);
    contextsWithAutoCreateEnableWrapperModel.setAttribute(coverFlowAttribute);
    contextsWithAutoCreateEnableWrapperModel.setInstanceId(instanceId);
    contextsWithAutoCreateEnableWrapperModel.setParentId(parentId);
    contextsWithAutoCreateEnableWrapperModel.setAssetConfigurationModel(assetModel);
    contextsWithAutoCreateEnableWrapperModel
        .setThumbnailPath(klassInstancesModel.getThumbnailPath());
    contextsWithAutoCreateEnableWrapperModel
        .setMainAssetInstanceSourcePath(klassInstancesModel.getFilePath());
    submitBGPAutoCreateTIVProcess(contextsWithAutoCreateEnableWrapperModel,
        klassInstancesModel.getId(), endpointCode);
  }
  
  /**
   * Submit BGP Auto create Technical Image Variants process according to it class type.
   * Added this task as BGP task because thread pool start creating variants before committing current transaction 
   * @param wrappedModel
   * @param klassInstancesModel 
   * @throws Exception 
   * @throws NumberFormatException 
   */
  @SuppressWarnings("unchecked")
  protected void submitBGPAutoCreateTIVProcess(
      ITechnicalImageVariantWithAutoCreateEnableWrapperModel wrappedModel, String instancesId, String endpointCode)
      throws NumberFormatException, Exception
  {
    IBaseEntityDTO baseEntityDTO = rdbmsComponentUtils
        .getBaseEntityDTO(Long.parseLong(instancesId));
    AutoCreateVariantDTO autoCreateVariantDTO = new AutoCreateVariantDTO();
    autoCreateVariantDTO.setBaseEntity(baseEntityDTO);
    autoCreateVariantDTO.setConfigData(ObjectMapperUtil.convertValue(wrappedModel, JSONObject.class));
    String endPoindId = transactionThread.getTransactionData().getEndpointId();
    if(endpointCode != null) {
      transactionThread.getTransactionData().setEndpointId(endpointCode);
    }
    autoCreateVariantDTO.setTransaction(transactionThread.getTransactionData());
    BGPDriverDAO.instance().submitBGPProcess(rdbmsComponentUtils.getUserName(),
            IAutoCreateVariantDTO.AUTO_CREATE_TIV_SERVICE, "", IBGProcessDTO.BGPPriority.HIGH,
            autoCreateVariantDTO.toJSONContent());
    if(endpointCode != null) {
      transactionThread.getTransactionData().setEndpointId(endPoindId);
    }
  }
  
}