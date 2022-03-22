package com.cs.core.config.klass;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.constants.CommonConstants;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.dto.KlassTaxonomySaveDTO;
import com.cs.core.bgprocess.dto.RelationshipDataTransferOnConfigChangeDTO;
import com.cs.core.bgprocess.dto.RelationshipInheritanceModifiedRelationshipDTO;
import com.cs.core.bgprocess.dto.RelationshipInheritanceOnConfigChangeDTO;
import com.cs.core.bgprocess.dto.RemoveAttributeVariantContextsDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPPriority;
import com.cs.core.bgprocess.idto.IKlassTaxonomySaveDTO;
import com.cs.core.bgprocess.idto.IRelationshipDataTransferOnConfigChangeDTO;
import com.cs.core.bgprocess.idto.IRelationshipInheritanceModifiedRelationshipDTO;
import com.cs.core.bgprocess.idto.IRelationshipInheritanceOnConfigChangeDTO;
import com.cs.core.config.interactor.entity.klass.IAsset;
import com.cs.core.config.interactor.entity.klass.IKlassNatureRelationship;
import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.GetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IAttributeDefaultValueCouplingTypeModel;
import com.cs.core.config.interactor.model.klass.IContentTypeIdsInfoModel;
import com.cs.core.config.interactor.model.klass.IContextKlassSavePropertiesToInheritModel;
import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityConfigDetailsModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPStrategyResponseModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.config.interactor.model.klass.IModifiedNatureRelationshipModel;
import com.cs.core.config.interactor.model.klass.IRemoveAttributeVariantContextModel;
import com.cs.core.config.interactor.model.klass.ITagDefaultValueCouplingTypeModel;
import com.cs.core.config.interactor.model.klass.ITypesListModel;
import com.cs.core.config.interactor.model.relationship.IModifiedRelationshipPropertyModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipPropertiesADMPropagationModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipPropertiesToInheritModel;
import com.cs.core.config.interactor.model.relationship.ISideInfoForRelationshipInheritanceModel;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.coupling.dto.ContextualDataTransferOnConfigChangeDTO;
import com.cs.core.rdbms.coupling.dto.ListOfContextualDataTransferOnConfigChangeDTO;
import com.cs.core.rdbms.coupling.dto.ModifiedCoupedPropertyDTO;
import com.cs.core.rdbms.coupling.dto.ModifiedPropertiesCouplingDTO;
import com.cs.core.rdbms.coupling.idao.ICouplingDAO;
import com.cs.core.rdbms.coupling.idto.IContextualDataTransferOnConfigChangeDTO;
import com.cs.core.rdbms.coupling.idto.ICouplingDTO;
import com.cs.core.rdbms.coupling.idto.IListOfContextualDataTransferOnConfigChangeDTO;
import com.cs.core.rdbms.coupling.idto.IModifiedCoupedPropertyDTO;
import com.cs.core.rdbms.coupling.idto.IModifiedPropertiesCouplingDTO;
import com.cs.core.rdbms.entity.dto.TagDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.runtime.interactor.model.bulkpropagation.ContentDiffModelToPrepareDataForBulkPropagation;
import com.cs.core.runtime.interactor.model.bulkpropagation.IContentDiffModelToPrepareDataForBulkPropagation;
import com.cs.core.runtime.interactor.model.bulkpropagation.IPropertyDiffModelForBulkPropagation;
import com.cs.core.runtime.interactor.model.bulkpropagation.PropertyDiffModelForBulkPropagation;
import com.cs.core.runtime.interactor.model.contextdatatransfer.IContextInfoForContextualDataTransferModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.relationship.ISideInfoForRelationshipDataTransferModel;
import com.cs.core.runtime.interactor.model.relationship.RelationshipPropertiesADMPropagationModel;
import com.cs.core.runtime.interactor.model.relationship.RelationshipPropertiesToInheritModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.model.couplingtype.IIdCodeCouplingTypeModel;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingBehavior;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.workflow.trigger.standard.IApplicationTriggerModel.ApplicationActionType;

@Component
@SuppressWarnings("unchecked")
public abstract class AbstractSaveKlassService<P extends IKlassSaveModel, R extends IGetKlassEntityWithoutKPModel>
    extends AbstractSaveConfigService<P, R> {
  
  private static final String KLASS_SAVE_SERVICE               = "KLASS_TAXONOMY_SAVE";
  
  private static final String RELATIONSHIP_INHERITANCE_SERVICE = "RELATIONSHIP_INHERITANCE_ON_CONFIG_CHANGE";
  
  private static final String SERVICE_FOR_CDT                  = "CONTEXTUAL_DATA_TRANSFER_ON_CONFIG_CHANGE_TASK";
  
  @Autowired
  TransactionThreadData       transactionThreadData;
  
  @Autowired
  RDBMSComponentUtils         rdbmsComponentUtils;
  
  @Autowired
  protected KlassValidations       klassValidations;
  
  /*@Autowired
  protected IGetPropagationDataForAddedRelationshipElementsStrategy  getPropagationDataForAddedRelationshipElementsStrategy;*/
  
  /*@Autowired
  protected IGetRelationshipObjectInstancesByRelationshipIdsStrategy getRelationshipObjectInstancesByRelationshipIdsStrategy;*/
  
  private static final String      SERVICE                          = "RELATIONSHIP_DATA_TRANSFER_ON_CONFIG_CHANGE";
  
  private static final BGPPriority BGP_PRIORITY                     = BGPPriority.HIGH;
  
  protected abstract IGetKlassEntityWithoutKPStrategyResponseModel executeSaveKlass(P klassModel)
      throws Exception;
  
  protected abstract IListModel<IContentTypeIdsInfoModel> getKlassInstancesByKlassAndTaxonomyIds(
      ITypesListModel klassAndTaxonomyListModel, String type) throws Exception;
  
  @Override
  protected R executeInternal(P klassSaveModel) throws Exception
  {
    this.manageAddedRelationship(klassSaveModel);
    
    klassValidations.validate(klassSaveModel);
    
    IGetKlassEntityWithoutKPStrategyResponseModel response = executeSaveKlass(klassSaveModel);
    IGetKlassEntityConfigDetailsModel configDetails = response.getConfigDetails();
    manageInheritance(response.getDefaultValuesDiff(), klassSaveModel.getType(), klassSaveModel,
        response.getDeletedPropertiesFromSource(), response.getAddedElements(),
        response.getModifiedElements(), response.getDeletedElements(),
        response.getContextKlassSavePropertiesToInherit(),
        response.getIsIndentifierAttributeChanged(),response.getRelationshipDataForTransfer());
    /*
       RMIG-7
        propagateChangesToRelationshipObjectInstance(response.getDefaultValuesDiff(),
            configDetails.getReferencedExtensionRelationshipIds(), response.getEntity()
                .getId());
    
        prepareDataForValuePropagationOnSaveKlass(response.getContextKlassSavePropertiesToDataTransfer());
    */
    /*IRemoveAttributeVariantContextModel removedAttributeVariantContexts = response.getRemovedAttributeVariantContexts();
    if (removedAttributeVariantContexts != null) {
      Map<String, List<String>> removedAttributeIdVsContextIds = removedAttributeVariantContexts
          .getRemovedAttributeIdVsContextIds();
      if (!removedAttributeIdVsContextIds.isEmpty()) {
        
        Set<String> removedAttributeContextIds = new HashSet<>();
        for (Entry<String, List<String>> entry : removedAttributeIdVsContextIds.entrySet()) {
          removedAttributeContextIds.addAll(entry.getValue());
        }
        List<IContextDTO> contextDTOs = new ArrayList<>();
        for (String removedAttributeContextId : removedAttributeContextIds) {
          IContextDTO context = new ContextDTO(removedAttributeContextId,
              ContextType.ATTRIBUTE_CONTEXT);
          contextDTOs.add(context);
        }
        
        ConfigurationDAO.instance()
            .deleteContextualObjects(contextDTOs.toArray(new IContextDTO[contextDTOs.size()]));
      }
    }*/
    
    if (klassSaveModel.getType().equals(CommonConstants.ASSET_KLASS_TYPE) && ((IAsset) klassSaveModel).getDetectDuplicate()
        && ((IAsset) klassSaveModel).getIsDetectDuplicateModified()) {
      // Submit a new BGP Process for marking duplicate assets
      executeMarkDuplicateBGPService(klassSaveModel);
    }
    executeBGPService(response, klassSaveModel);
    prepareDataForRelationshipInheritance(response.getRelationshipDataForRelationshipInheritance(), response.getDeletedNatureRelationshipIds());
    
    executeBGPServiceForContextualDataTranfer(response);
    
    IGetKlassEntityWithoutKPModel returnModel = new GetKlassEntityWithoutKPModel();
    returnModel.setEntity(response.getEntity());
    returnModel.setConfigDetails(configDetails);
    returnModel.setAuditLogInfo(response.getAuditLogInfo());
    return (R) returnModel;
  }
  
  private void executeBGPServiceForContextualDataTranfer(IGetKlassEntityWithoutKPStrategyResponseModel response)
      throws RDBMSException, Exception
  {
    List<IContextInfoForContextualDataTransferModel> contextualDataTransfer = response.getContextKlassSavePropertiesToDataTransfer();
    
    IListOfContextualDataTransferOnConfigChangeDTO contextualDataTransferDTOs = new ListOfContextualDataTransferOnConfigChangeDTO();
    ICouplingDAO couplingDAO = rdbmsComponentUtils.getLocaleCatlogDAO().openCouplingDAO();
    
    ConfigurationDAO configurationDAO = ConfigurationDAO.instance();
    
    for (IContextInfoForContextualDataTransferModel contextualDataTransferInfo : contextualDataTransfer) {
      
      List<IIdCodeCouplingTypeModel> modifiedProperties = new ArrayList<>();
      List<String> deletedProperties = new ArrayList<>();
      IContextualDataTransferOnConfigChangeDTO contextualDataTransferDTO = new ContextualDataTransferOnConfigChangeDTO();
      long classifierIID = configurationDAO.getClassifierByCode(contextualDataTransferInfo.getContextKlassId()).getClassifierIID();
      contextualDataTransferDTO.setContextualIID(classifierIID);
      
      modifiedProperties.addAll(contextualDataTransferInfo.getModifiedDependentAttributes());
      modifiedProperties.addAll(contextualDataTransferInfo.getModifiedInDependentAttributes());
      modifiedProperties.addAll(contextualDataTransferInfo.getModifiedTags());
      
      deletedProperties.addAll(contextualDataTransferInfo.getDeletedDependentAttributes());
      deletedProperties.addAll(contextualDataTransferInfo.getDeletedInDependentAttributes());
      deletedProperties.addAll(contextualDataTransferInfo.getDeletedTags());
      
      for (IIdCodeCouplingTypeModel modifiedDataTransferProperty : modifiedProperties) {
        long propertyIID = configurationDAO.getPropertyByCode(modifiedDataTransferProperty.getId()).getPropertyIID();
        List<ICouplingDTO> conflictingValues = couplingDAO
            .getConflictingValuesByCouplingSourceIID(classifierIID, propertyIID);
        
        IModifiedPropertiesCouplingDTO modifiedCouplingDTO = new ModifiedPropertiesCouplingDTO();
        if (conflictingValues.size() == 0) {
          setCouplingTypeInDTO(modifiedDataTransferProperty, propertyIID, modifiedCouplingDTO);
          contextualDataTransferDTO.getAddedProperties().add(modifiedCouplingDTO);
        }else {
          setCouplingTypeInDTO(modifiedDataTransferProperty, propertyIID, modifiedCouplingDTO);
          contextualDataTransferDTO.getModifiedProperties().add(modifiedCouplingDTO);
        }
      }
      
      for(String deletedProperty : deletedProperties) {
        contextualDataTransferDTO.getDeletedPropertyIIDs().add(configurationDAO.getPropertyByCode(deletedProperty).getPropertyIID());
      }
      
      contextualDataTransferDTOs.getContextualDataTransferOnConfigChangeDTOs().add(contextualDataTransferDTO);
    }
    
    if(!contextualDataTransferDTOs.getContextualDataTransferOnConfigChangeDTOs().isEmpty()) {
      
      IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.HIGH;
      contextualDataTransferDTOs.setLocaleID(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getLocaleID());
      contextualDataTransferDTOs.setCatalogCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getCatalogCode());
      contextualDataTransferDTOs.setOrganizationCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getOrganizationCode());
      contextualDataTransferDTOs.setUserId(transactionThread.getTransactionData().getUserId());
      BGPDriverDAO.instance().submitBGPProcess(rdbmsComponentUtils.getUserName(), SERVICE_FOR_CDT, "", userPriority,
          new JSONContent(contextualDataTransferDTOs.toJSON()));
    }
  }

  private void setCouplingTypeInDTO(IIdCodeCouplingTypeModel attribute, long propertyIID,
      IModifiedPropertiesCouplingDTO modifiedCouplingDTO)
  {
    modifiedCouplingDTO.setPropertyIID(propertyIID);
    if (CommonConstants.TIGHTLY_COUPLED.equals(attribute.getCouplingType())) {
      modifiedCouplingDTO.setCouplingType(CouplingBehavior.TIGHTLY.ordinal());
    }
    else {
      modifiedCouplingDTO.setCouplingType(CouplingBehavior.DYNAMIC.ordinal());
    }
  }
  
  
  /**
   * Submit a new BGP Process for marking duplicate asset.
   * @param klassSaveModel
   * @throws RDBMSException
   */
  private void executeMarkDuplicateBGPService(P klassSaveModel) throws RDBMSException
  {
    // TODO: Check if can be implemented with application event
    // without showing on BGP dashboard
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.LOW;
    JSONContent entryData = new JSONContent();
    String organizationId = transactionThread.getTransactionData().getOrganizationId();
    String userId = transactionThread.getTransactionData().getUserId();
    String localeId = transactionThread.getTransactionData().getUiLanguage();
    String physicalCatalogId = transactionThread.getTransactionData().getPhysicalCatalogId();
    entryData.setField(CommonConstants.ORGANIZATION_ID, organizationId);
    entryData.setField(CommonConstants.USER_ID, userId);
    entryData.setField(CommonConstants.LOCALE_ID, localeId);
    entryData.setField(CommonConstants.PHYSICAL_CATALOG_ID, physicalCatalogId);
    // submitting BGP Process
    BGPDriverDAO.instance().submitBGPProcess(context.getUserSessionDTO().getUserName(),
        ApplicationActionType.MARK_DUPLICATE_ASSETS.toString(), "", userPriority, entryData);
  }

  private void prepareDataForRelationshipInheritance(
      List<ISideInfoForRelationshipInheritanceModel> relationshipDataForRelationshipInheritance, List<String> deletedNatureRelationshipIds) throws RDBMSException, CSFormatException
  {
    for(ISideInfoForRelationshipInheritanceModel relationshipDataForRI : relationshipDataForRelationshipInheritance) {
      List<String> deletedRelationships = relationshipDataForRI.getDeletedRelationships();
      String relationshipId = relationshipDataForRI.getRelationshipId();
      String sideId = relationshipDataForRI.getSideId();
      
      IRelationshipInheritanceOnConfigChangeDTO relationshipInheritanceDTO = new RelationshipInheritanceOnConfigChangeDTO();
      List<IRelationshipInheritanceModifiedRelationshipDTO> modifiedRelationshipDTOs = new ArrayList<IRelationshipInheritanceModifiedRelationshipDTO>();
      relationshipInheritanceDTO.setRelationshipId(relationshipId);
      relationshipInheritanceDTO.setSideId(sideId);
      relationshipInheritanceDTO.setDeletedRelationships(deletedRelationships);
      relationshipInheritanceDTO.setModifiedRelationships(modifiedRelationshipDTOs);
      
      for(IModifiedRelationshipPropertyModel modifiedRelationship : relationshipDataForRI.getModifiedRelationships()) {
        IRelationshipInheritanceModifiedRelationshipDTO modifiedRelationshipDTO = new RelationshipInheritanceModifiedRelationshipDTO();
        modifiedRelationshipDTO.setId(modifiedRelationship.getId());
        modifiedRelationshipDTO.setSide(modifiedRelationship.getSide());
        modifiedRelationshipDTO.setCouplingType(modifiedRelationship.getCouplingType());
        modifiedRelationshipDTOs.add(modifiedRelationshipDTO);
      }
      IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.MEDIUM;
      BGPDriverDAO.instance().submitBGPProcess(context.getUserSessionDTO().getUserName(), RELATIONSHIP_INHERITANCE_SERVICE, "", userPriority,
          new JSONContent(relationshipInheritanceDTO.toJSON()));
    }
  }

  private void executeBGPService(IGetKlassEntityWithoutKPStrategyResponseModel response, P klassSaveModel) throws Exception
  {
    List<Long> removedContextClassifierIIDs = response.getRemovedContextClassifierIIDs();
    List<Long> updatedMandatoryPropertyIIDs = response.getUpdatedMandatoryPropertyIIDs();
    List<Long> propertyIIDsToEvaluateProductIdentifier = response.getPropertyIIDsToEvaluateProductIdentifier();
    List<Long> propertyIIDsToRemoveProductIdentifier = response.getPropertyIIDsToRemoveProductIdentifier();
    List<Long> deletedNatureRelationshipPropertyIIDs = new ArrayList<>();
    List<String> relationshipIds = klassSaveModel.getDeletedRelationships();
    List<IDefaultValueChangeModel> defaultValuesDiff = response.getDefaultValuesDiff();
    IRemoveAttributeVariantContextModel removedAttributeVariantContexts = response.getRemovedAttributeVariantContexts();
    Map<String, List<String>> removedAttributeIdVsContextIds = removedAttributeVariantContexts.getRemovedAttributeIdVsContextIds();
    List<String> addedCalculatedAttributeIds = response.getAddedCalculatedAttributeIds();
    
    if (relationshipIds.size() == 0) {
      relationshipIds.addAll(response.getDeletedNatureRelationshipIds());
    }
    if (relationshipIds.size() > 0) {
      IPropertyDTO propertyDelete = null;
      for (String relationshipId : relationshipIds) {
        propertyDelete = RDBMSUtils.getPropertyByCode(relationshipId);
        deletedNatureRelationshipPropertyIIDs.add(propertyDelete.getPropertyIID());
      }
    }
    
    
    if (!updatedMandatoryPropertyIIDs.isEmpty() || !propertyIIDsToEvaluateProductIdentifier.isEmpty()
        || !propertyIIDsToRemoveProductIdentifier.isEmpty() || !removedContextClassifierIIDs.isEmpty()
        || !deletedNatureRelationshipPropertyIIDs.isEmpty() || !klassSaveModel.getModifiedRelationships().isEmpty()
        || !defaultValuesDiff.isEmpty() || !removedAttributeIdVsContextIds.isEmpty() || !addedCalculatedAttributeIds.isEmpty()) {
      Set<String> classifierCodes = new HashSet<String>();
      
      IKlassTaxonomySaveDTO klassTaxonomySaveDTO = new KlassTaxonomySaveDTO();
      Set<IModifiedCoupedPropertyDTO> modifiedCoupledPropertyDTOs = klassTaxonomySaveDTO.getModifiedCoupledPropertyDTOs();
      
      for (IDefaultValueChangeModel defaultValueDiff : defaultValuesDiff) {
        classifierCodes.addAll(defaultValueDiff.getKlassAndChildrenIds());
        
        IModifiedCoupedPropertyDTO coupedPropertyDTO = new ModifiedCoupedPropertyDTO();
        coupedPropertyDTO.getClassifierCodes().addAll(defaultValueDiff.getKlassAndChildrenIds());
        String couplingType = defaultValueDiff.getCouplingType();
        if (couplingType.equals(CommonConstants.TIGHTLY_COUPLED)) {
          coupedPropertyDTO.setCouplingBehavior(CouplingBehavior.TIGHTLY);
          coupedPropertyDTO.setCouplingType(CouplingType.TIGHT_CLASSIFICATION);
        }
        else if (couplingType.equals(CommonConstants.DYNAMIC_COUPLED)) {
          coupedPropertyDTO.setCouplingBehavior(CouplingBehavior.DYNAMIC);
          coupedPropertyDTO.setCouplingType(CouplingType.DYN_CLASSIFICATION);
        }
        else {
          coupedPropertyDTO.setCouplingBehavior(CouplingBehavior.NONE);
          
        }
        IPropertyDTO property = RDBMSUtils.getPropertyByCode(defaultValueDiff.getEntityId());
        coupedPropertyDTO.setProperty(property);
        
        if (property.getSuperType() == SuperType.ATTRIBUTE) {
          coupedPropertyDTO.setIsDependent(((IAttributeDefaultValueCouplingTypeModel) defaultValueDiff).getIsDependent());
          coupedPropertyDTO.setValue(((IAttributeDefaultValueCouplingTypeModel) defaultValueDiff).getValue());
          String valueAsHtml = ((IAttributeDefaultValueCouplingTypeModel) defaultValueDiff).getValueAsHtml();
          if (valueAsHtml != null)
            coupedPropertyDTO.setValueAsHtml(valueAsHtml);
          coupedPropertyDTO.setValueAsNumber(((IAttributeDefaultValueCouplingTypeModel) defaultValueDiff).getValueAsNumber());
          String unitSymbol = ((IAttributeDefaultValueCouplingTypeModel) defaultValueDiff).getUnitSymbol();
          if (unitSymbol != null)
            coupedPropertyDTO.setUnitSymbol(unitSymbol);
        }
        else if (property.getSuperType() == SuperType.TAGS) {
          List<IIdRelevance> tagValues = ((ITagDefaultValueCouplingTypeModel) defaultValueDiff).getValue();
          Set<ITagDTO> coupledTagValue = coupedPropertyDTO.getTagValues();
          for (IIdRelevance tagValue : tagValues) {
            ITagDTO tagDTO = new TagDTO(tagValue.getTagId(), tagValue.getRelevance());
            coupledTagValue.add(tagDTO);
          }
        }
        
        coupedPropertyDTO.setIsCouplingTypeChanged(defaultValueDiff.getIsCouplingTypeChanged());
        coupedPropertyDTO.setIsValueChanged(defaultValueDiff.getIsValueChanged());
        modifiedCoupledPropertyDTOs.add(coupedPropertyDTO);
      }
      
     List<Long> modifiedRelationshipIIDs = new ArrayList<>();
     for (IModifiedNatureRelationshipModel relationship : klassSaveModel.getModifiedRelationships()) {
        modifiedRelationshipIIDs.add(ConfigurationDAO.instance().getPropertyByCode(relationship.getId()).getPropertyIID());
      }
     
      klassTaxonomySaveDTO.setLocaleID(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getLocaleID());
      klassTaxonomySaveDTO.setCatalogCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getCatalogCode());
      klassTaxonomySaveDTO.setOrganizationCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getOrganizationCode());
      klassTaxonomySaveDTO.setUserId(transactionThread.getTransactionData().getUserId());
      klassTaxonomySaveDTO.setClassifierCodes(classifierCodes);
      klassTaxonomySaveDTO.setUpdatedMandatoryPropertyIIDs(updatedMandatoryPropertyIIDs);
      klassTaxonomySaveDTO.setPropertyIIDsToEvaluateProductIdentifier(propertyIIDsToEvaluateProductIdentifier);
      klassTaxonomySaveDTO.setPropertyIIDsToRemoveProductIdentifier(propertyIIDsToRemoveProductIdentifier);
      klassTaxonomySaveDTO.setRemovedEmbeddedClassifierIIDs(removedContextClassifierIIDs);
      klassTaxonomySaveDTO.setSavedKlassTaxonomyClassifierIID(response.getEntity().getClassifierIID());
      klassTaxonomySaveDTO.setDeletedNatureRelationshipPropertyIIDs(deletedNatureRelationshipPropertyIIDs);
      klassTaxonomySaveDTO.setModifiedRelationshipIIDsForTaxonomyInheritance(modifiedRelationshipIIDs);
      klassTaxonomySaveDTO.setChangedClassifiersForAttributeContexts(removedAttributeVariantContexts.getChangedClassifiersForAttributeContexts());
      klassTaxonomySaveDTO.setRemoveAttributeVariantContextsDTO(removedAttributeIdVsContextIds.entrySet().stream()
          .map(entry -> new RemoveAttributeVariantContextsDTO(entry.getKey(), entry.getValue()))
          .collect(Collectors.toList()));
      klassTaxonomySaveDTO.setAddedCalculatedAttributeIds(addedCalculatedAttributeIds);

      IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.LOW;
      BGPDriverDAO.instance().submitBGPProcess(context.getUserSessionDTO().getUserName(), KLASS_SAVE_SERVICE, "", userPriority,
          new JSONContent(klassTaxonomySaveDTO.toJSON()));
    }
  }
  
  protected void manageAddedRelationship(P klassSaveModel) throws Exception
  {
    if (klassSaveModel.getAddedRelationships() != null && klassSaveModel.getAddedRelationships()
        .size() > 0) {
      IKlassNatureRelationship natureRelationship = klassSaveModel.getAddedRelationships()
          .get(0);
      natureRelationship.setCode("code_" + natureRelationship.getId()); // code
                                                                        // replace
                                                                        // with
                                                                        // id
                                                                        // because
                                                                        // old
                                                                        // nature
                                                                        // relationship
                                                                        // code
                                                                        // duplicated
      // in new nature relationship
      // 'code_' suffix added because PXON expression break in RDBMS
      IPropertyDTO createProperty = RDBMSUtils.createProperty(natureRelationship.getId(),
          natureRelationship.getCode(), PropertyType.NATURE_RELATIONSHIP);
      natureRelationship.setPropertyIID(createProperty.getPropertyIID());
    }
  }
  
  public void manageInheritance(List<IDefaultValueChangeModel> defaultValuesDiff, String type,
      IKlassSaveModel klassSaveModel, Map<String, List<String>> deletedPropertiesFromSource,
      IRelationshipPropertiesToInheritModel addedElements,
      IRelationshipPropertiesToInheritModel modifiedElements,
      IRelationshipPropertiesToInheritModel deletedElements,
      IContextKlassSavePropertiesToInheritModel contextKlassSavePropertiesToInheritModel,
      Boolean isIdentiferAttributeChanged, List<ISideInfoForRelationshipDataTransferModel> relationshipDataForTransferList) throws Exception
  {
    IContentDiffModelToPrepareDataForBulkPropagation contentDiffModelToPrepareDataForBulkPropagation = new ContentDiffModelToPrepareDataForBulkPropagation();
    Boolean isPropagationNeeded = false;
    if (!deletedPropertiesFromSource.isEmpty()) {
      contentDiffModelToPrepareDataForBulkPropagation
          .setDeletedPropertiesFromSource(deletedPropertiesFromSource);
      isPropagationNeeded = true;
    }
    if (addedElements != null) {
      List<IDefaultValueChangeModel> addedAttributes = addedElements.getAttributes();
      List<IDefaultValueChangeModel> addedTags = addedElements.getTags();
      if (!addedAttributes.isEmpty() || !addedTags.isEmpty()) {
        contentDiffModelToPrepareDataForBulkPropagation
            .setPropertiesDetailAddedToRelationship(addedElements);
        isPropagationNeeded = true;
      }
    }
    if (!defaultValuesDiff.isEmpty()) {
      IPropertyDiffModelForBulkPropagation propertyDiffModelForBulkPropagation = new PropertyDiffModelForBulkPropagation();
      propertyDiffModelForBulkPropagation.setDefaultValueDiffList(defaultValuesDiff);
      contentDiffModelToPrepareDataForBulkPropagation
          .setPropertyDiffModelForBulkPropagation(propertyDiffModelForBulkPropagation);
      isPropagationNeeded = true;  
    }
    IRelationshipPropertiesADMPropagationModel relationshipPropertiesADMPropagationModel = new RelationshipPropertiesADMPropagationModel();
    relationshipPropertiesADMPropagationModel
        .setModifiedProperties(new RelationshipPropertiesToInheritModel());
    relationshipPropertiesADMPropagationModel
        .setRemovedProperties(new RelationshipPropertiesToInheritModel());
    if (modifiedElements != null) {
      List<IDefaultValueChangeModel> modifiedAttributes = modifiedElements.getAttributes();
      List<IDefaultValueChangeModel> modifiedTags = modifiedElements.getTags();
      if (!modifiedAttributes.isEmpty() || !modifiedTags.isEmpty()) {
        relationshipPropertiesADMPropagationModel.setModifiedProperties(modifiedElements);
        contentDiffModelToPrepareDataForBulkPropagation
            .setRelationshipPropertiesADM(relationshipPropertiesADMPropagationModel);
        isPropagationNeeded = true;
      }
    }
    
    if (deletedElements != null) {
      List<IDefaultValueChangeModel> deletedAttributes = deletedElements.getAttributes();
      List<IDefaultValueChangeModel> deletedTags = deletedElements.getTags();
      if (!deletedAttributes.isEmpty() || !deletedTags.isEmpty()) {
        relationshipPropertiesADMPropagationModel.setRemovedProperties(deletedElements);
        contentDiffModelToPrepareDataForBulkPropagation
            .setRelationshipPropertiesADM(relationshipPropertiesADMPropagationModel);
        isPropagationNeeded = true;
      }
    }
    
    if (contextKlassSavePropertiesToInheritModel != null) {
      contentDiffModelToPrepareDataForBulkPropagation
          .setContextKlassSavePropertiesToInheritModel(contextKlassSavePropertiesToInheritModel);
      isPropagationNeeded = true;
    }
    
    if (isIdentiferAttributeChanged) {
      contentDiffModelToPrepareDataForBulkPropagation
          .setIsIdentifierAttributeEvaluationNeeded(true);
      isPropagationNeeded = true;
    }
    
    //TODO: BGP
    /*if (isPropagationNeeded) {
    
    }*/
    List<Long> deletedPropertyIIDs = new ArrayList<Long>();
    List<Long> side1AddedPropertyIIDs = new ArrayList<Long>();
    List<Long> side2AddedPropertyIIDs = new ArrayList<Long>();
    List<IModifiedPropertiesCouplingDTO> modifiedProperties = new ArrayList<>();
    Long relationIID = null;
    List<IModifiedNatureRelationshipModel> natureRelationships = klassSaveModel.getModifiedRelationships();
    if(!natureRelationships.isEmpty()) {
      for (IModifiedNatureRelationshipModel natureRealtionship : natureRelationships) {
        relationIID = ConfigurationDAO.instance().getPropertyByCode(natureRealtionship.getId()).getPropertyIID();
        fillAddedPropertyIIDs(natureRealtionship, side1AddedPropertyIIDs, side2AddedPropertyIIDs);
        fillModifiedPropertyIIDs(natureRealtionship, modifiedProperties);
        fillDeletedPropertyIIDs(natureRealtionship, deletedPropertyIIDs);
      }
      IRelationshipDataTransferOnConfigChangeDTO relationshipDataTransferOnConfigChange = new RelationshipDataTransferOnConfigChangeDTO();
      relationshipDataTransferOnConfigChange.setLocaleID(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getLocaleID());
      relationshipDataTransferOnConfigChange.setCatalogCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getCatalogCode());
      relationshipDataTransferOnConfigChange
      .setOrganizationCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getOrganizationCode());
      relationshipDataTransferOnConfigChange.setUserId(transactionThread.getTransactionData().getUserId());
      relationshipDataTransferOnConfigChange.setRelationshipIID(relationIID);
      relationshipDataTransferOnConfigChange.setDeletedPropertyIIDs(deletedPropertyIIDs);
      relationshipDataTransferOnConfigChange.setSide1AddedPropertyIIDs(side1AddedPropertyIIDs);
      relationshipDataTransferOnConfigChange.setSide2AddedPropertyIIDs(side2AddedPropertyIIDs);
      relationshipDataTransferOnConfigChange.setModifiedProperties(modifiedProperties);
      relationshipDataTransferOnConfigChange.setIsNature(true);
      BGPDriverDAO.instance().submitBGPProcess(context.getUserName(), SERVICE, "", BGP_PRIORITY,
          new JSONContent(relationshipDataTransferOnConfigChange.toJSON()));
    }
  }
  
  private void fillAddedPropertyIIDs(IModifiedNatureRelationshipModel natureRealtionship, List<Long> side1AddedPropertyIIDs,
      List<Long> side2AddedPropertyIIDs) throws RDBMSException
  {
    for (IModifiedRelationshipPropertyModel attribute : natureRealtionship.getAddedAttributes()) {
      if (attribute.getSide().equals("side1")) {
        long side1PropertyIID = ConfigurationDAO.instance().getPropertyByCode(attribute.getId()).getPropertyIID();
        side1AddedPropertyIIDs.add(side1PropertyIID);
      }
      else if (attribute.getSide().equals("side2")) {
        long side2PropertyIID = ConfigurationDAO.instance().getPropertyByCode(attribute.getId()).getPropertyIID();
        side2AddedPropertyIIDs.add(side2PropertyIID);
      }
    }
    
    for (IModifiedRelationshipPropertyModel tags : natureRealtionship.getAddedTags()) {
      if (tags.getSide().equals("side1")) {
        long side1PropertyIID = ConfigurationDAO.instance().getPropertyByCode(tags.getId()).getPropertyIID();
        side1AddedPropertyIIDs.add(side1PropertyIID);
      }
      else if (tags.getSide().equals("side2")) {
        long side2PropertyIID = ConfigurationDAO.instance().getPropertyByCode(tags.getId()).getPropertyIID();
        side2AddedPropertyIIDs.add(side2PropertyIID);
      }
    }
  }
  
  private void fillModifiedPropertyIIDs(IModifiedNatureRelationshipModel natureRealtionship, List<IModifiedPropertiesCouplingDTO> modifiedPropertiesDTO) throws RDBMSException
  {
    List<IModifiedRelationshipPropertyModel> modifiedProperties = natureRealtionship.getModifiedAttributes();
    modifiedProperties.addAll(natureRealtionship.getModifiedTags());
    
    for (IModifiedRelationshipPropertyModel attribute : modifiedProperties) {
      IModifiedPropertiesCouplingDTO couplingDTO = new ModifiedPropertiesCouplingDTO();
      couplingDTO.setPropertyIID(ConfigurationDAO.instance().getPropertyByCode(attribute.getId()).getPropertyIID());
      
      if(attribute.getCouplingType().equals(CommonConstants.TIGHTLY_COUPLED)) {
        couplingDTO.setCouplingSourceType(CouplingType.TIGHT_RELATIONSHIP.ordinal());
        couplingDTO.setCouplingType(CouplingBehavior.TIGHTLY.ordinal());
      }
      else {
        couplingDTO.setCouplingSourceType(CouplingType.DYN_RELATIONSHIP.ordinal());
        couplingDTO.setCouplingType(CouplingBehavior.DYNAMIC.ordinal());
      }
      modifiedPropertiesDTO.add(couplingDTO);
    }
  }
  
  private void fillDeletedPropertyIIDs(IModifiedNatureRelationshipModel natureRealtionship,
      List<Long> deletedPropertyIIDs) throws RDBMSException
  {
    
    List<IModifiedRelationshipPropertyModel> deletedProperties = natureRealtionship.getDeletedAttributes();
    deletedProperties.addAll(natureRealtionship.getDeletedTags());
    
    for (IModifiedRelationshipPropertyModel attribute : deletedProperties) {
      long DependentPropertyIID = ConfigurationDAO.instance().getPropertyByCode(attribute.getId()).getPropertyIID();
      deletedPropertyIIDs.add(DependentPropertyIID);      
    }
  }
}
