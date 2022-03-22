package com.cs.core.config.attributiontaxonomy;

import com.cs.bds.config.usecase.taxonomy.ISaveTaxonomy;
import com.cs.constants.CommonConstants;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.dto.EmbeddedInheritanceInTaxonomyDTO;
import com.cs.core.bgprocess.dto.KlassTaxonomySaveDTO;
import com.cs.core.bgprocess.dto.RemoveAttributeVariantContextsDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IEmbeddedInheritanceInTaxonomyDTO;
import com.cs.core.bgprocess.idto.IKlassTaxonomySaveDTO;
import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.config.interactor.model.attributiontaxonomy.GetMasterTaxonomyWithoutKPModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPStrategyResponseModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.ISaveMasterTaxonomyModel;
import com.cs.core.config.interactor.model.klass.*;
import com.cs.core.config.strategy.usecase.attributiontaxonomy.IGetChildMasterTaxonomiesForTaxonomyLevelStrategy;
import com.cs.core.config.strategy.usecase.attributiontaxonomy.ISaveMasterTaxonomyStrategy;
import com.cs.core.config.taxonomy.TaxonomyValidations;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.coupling.dto.ModifiedCoupedPropertyDTO;
import com.cs.core.rdbms.coupling.idto.IModifiedCoupedPropertyDTO;
import com.cs.core.rdbms.entity.dto.TagDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.runtime.interactor.model.contextdatatransfer.IContextInfoForContextualDataTransferModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingBehavior;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SaveMasterTaxonomyService extends AbstractSaveTagTaxonomy<ISaveMasterTaxonomyModel, IGetMasterTaxonomyWithoutKPModel>
    implements ISaveMasterTaxonomyService {
  
  @Autowired
  protected ISaveMasterTaxonomyStrategy                       saveMasterTaxonomyStrategy;
  
  @Autowired
  protected IGetChildMasterTaxonomiesForTaxonomyLevelStrategy getChildMasterTaxonomiesForTaxonomyLevelStrategy;
  
  /* @Autowired
  protected IGetInstanceCountForTypesStrategy                 getInstanceCountForTypesStrategy;*/
  
  @Autowired
  protected ISaveTaxonomy                                     saveTaxonomy;

  private static final String                                 TAXONOMY_SAVE_SERVICE = "KLASS_TAXONOMY_SAVE";
  
  @Override
  protected IGetMasterTaxonomyWithoutKPModel executeInternal(ISaveMasterTaxonomyModel model) throws Exception
  {
    return super.executeInternal(model);
  }
  
  protected IGetMasterTaxonomyWithoutKPModel executeSaveArticleTaxonomy(
      ISaveMasterTaxonomyModel saveAttributionTaxonomyModel) throws Exception
  {
    
    if (saveAttributionTaxonomyModel.getDeletedLevel() != null) {
      // TODO: Check in RDBMS if any instances exists for the deleting taxonomy
      // then do not allow to
      // delete
      /*IIdParameterModel idParameterModel = new IdParameterModel();
      idParameterModel.setId(saveAttributionTaxonomyModel.getDeletedLevel());
      
      IListModel<String> tagTaxonomyIds = getChildMasterTaxonomiesForTaxonomyLevelStrategy
          .execute(idParameterModel);
      
      IInstanceCountRequestModel instanceCountRequestModel = new InstanceCountRequestModel();
      Map<String, List<String>> taxonomyIdVsChildTaxonomyIds = new HashMap<String, List<String>>();
      taxonomyIdVsChildTaxonomyIds.put(saveAttributionTaxonomyModel.getDeletedLevel(), (List<String>) tagTaxonomyIds.getList());
      
      instanceCountRequestModel.setTaxonomyIdVsChildTaxonomyIds(taxonomyIdVsChildTaxonomyIds);
      IInstancesCountResponseModel instancesCountResponseModel = getInstanceCountForTypesStrategy
          .execute(instanceCountRequestModel);
      
      Map<String, Long> taxonomyIdVsInstancesCount = instancesCountResponseModel
          .getTypeIdVsInstancesCount();
      
      List<String> taxonomyIdsHavingInstances = taxonomyIdVsInstancesCount.entrySet()
          .stream()
          .filter(taxonomyIdVsInstanceCountEntry -> !taxonomyIdVsInstanceCountEntry.getValue().equals(0l))
          .map(taxonomyIdVsInstanceCountEntry -> taxonomyIdVsInstanceCountEntry.getKey())
          .collect(Collectors.toList());
      
      if (!taxonomyIdsHavingInstances.isEmpty()) {
        throw new InstanceExistsForTaxonomyException();
      }*/
    }

    TaxonomyValidations.validate(saveAttributionTaxonomyModel);
    IGetMasterTaxonomyWithoutKPStrategyResponseModel response = saveTaxonomy.execute(saveAttributionTaxonomyModel);
    
    List<IContextKlassModel> addedContextKlasses = saveAttributionTaxonomyModel.getAddedContextKlasses();
    
    if (!addedContextKlasses.isEmpty() && response.getIsImmediateChildPresent()
        && !(response.getEntity().getTaxonomyType().equals(CommonConstants.MINOR_TAXONOMY))) {
      inheritEmbeddedClassesToChildTaxonomyLevels(saveAttributionTaxonomyModel);
    }
    
    executeBGPService(response);
    
    prepareDataForValuePropagationOnSaveKlass(
        response.getContextKlassSavePropertiesToDataTransfer());
    
    List<IDefaultValueChangeModel> defaultValuesDiff = response.getDefaultValuesDiff();
    Map<String, List<String>> deletedPropertiesFromSource = response.getDeletedPropertiesFromSource();
    if (!defaultValuesDiff.isEmpty() || !deletedPropertiesFromSource.isEmpty()) {
      manageInheritance(defaultValuesDiff, deletedPropertiesFromSource, response.getIsIndentifierAttributeChanged());
    }
    
    IGetMasterTaxonomyWithoutKPModel returnModel = new GetMasterTaxonomyWithoutKPModel();
    returnModel.setConfigDetails(response.getConfigDetails());
    returnModel.setEntity(response.getEntity());
    returnModel.setAuditLogInfo(response.getAuditLogInfo());
    
    return (IGetMasterTaxonomyWithoutKPModel) returnModel;
  }
  
  private void executeBGPService(IGetMasterTaxonomyWithoutKPStrategyResponseModel response) throws Exception
  {
    List<Long> removedContextClassifierIIDs = response.getRemovedContextClassifierIIDs();
    List<Long> updatedMandatoryPropertyIIDs = response.getUpdatedMandatoryPropertyIIDs();
    List<Long> propertyIIDsToEvaluateProductIdentifier = response.getPropertyIIDsToEvaluateProductIdentifier();
    List<Long> propertyIIDsToRemoveProductIdentifier = response.getPropertyIIDsToRemoveProductIdentifier();
    List<IDefaultValueChangeModel> defaultValuesDiff = response.getDefaultValuesDiff();
    
    IRemoveAttributeVariantContextModel removedAttributeVariantContexts = response.getRemovedAttributeVariantContexts();
    Map<String, List<String>> removedAttributeIdVsContextIds = removedAttributeVariantContexts.getRemovedAttributeIdVsContextIds();
    List<String> addedCalculatedAttributeIds = response.getAddedCalculatedAttributeIds();
    IKlassTaxonomySaveDTO klassTaxonomySaveDTO = new KlassTaxonomySaveDTO();
    
    if (!updatedMandatoryPropertyIIDs.isEmpty()
        || !propertyIIDsToEvaluateProductIdentifier.isEmpty()
        || !propertyIIDsToRemoveProductIdentifier.isEmpty()
        || !removedContextClassifierIIDs.isEmpty() || !defaultValuesDiff.isEmpty()   || !removedAttributeIdVsContextIds.isEmpty()
        || !addedCalculatedAttributeIds.isEmpty()) {

      Set<String> classifierCodes = new HashSet<String>();
      Set<IModifiedCoupedPropertyDTO> modifiedCoupledPropertyDTOs = klassTaxonomySaveDTO.getModifiedCoupledPropertyDTOs();
     for(IDefaultValueChangeModel defaultValueDiff : defaultValuesDiff) {
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
      klassTaxonomySaveDTO.setClassifierCodes(classifierCodes);
      klassTaxonomySaveDTO.setUpdatedMandatoryPropertyIIDs(updatedMandatoryPropertyIIDs);
      klassTaxonomySaveDTO.setPropertyIIDsToEvaluateProductIdentifier(propertyIIDsToEvaluateProductIdentifier);
      klassTaxonomySaveDTO.setPropertyIIDsToRemoveProductIdentifier(propertyIIDsToRemoveProductIdentifier);
      klassTaxonomySaveDTO.setRemovedEmbeddedClassifierIIDs(removedContextClassifierIIDs);
      klassTaxonomySaveDTO.setSavedKlassTaxonomyClassifierIID(response.getEntity().getClassifierIID());
      klassTaxonomySaveDTO.setChangedClassifiersForAttributeContexts(removedAttributeVariantContexts.getChangedClassifiersForAttributeContexts());
      klassTaxonomySaveDTO.setRemoveAttributeVariantContextsDTO(removedAttributeIdVsContextIds.entrySet().stream()
              .map(entry -> new RemoveAttributeVariantContextsDTO(entry.getKey(), entry.getValue()))
              .collect(Collectors.toList()));
      klassTaxonomySaveDTO.setAddedCalculatedAttributeIds(addedCalculatedAttributeIds);
      
      TransactionData transactionData = transactionThread.getTransactionData();
      klassTaxonomySaveDTO.setLocaleID(transactionData.getDataLanguage());
      klassTaxonomySaveDTO.setCatalogCode(transactionData.getPhysicalCatalogId());
      klassTaxonomySaveDTO.setOrganizationCode(transactionData.getOrganizationId());
      klassTaxonomySaveDTO.setUserId(transactionData.getUserId());
      IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.LOW;
      
      BGPDriverDAO.instance().submitBGPProcess(context.getUserSessionDTO().getUserName(), TAXONOMY_SAVE_SERVICE, "", userPriority,
          new JSONContent(klassTaxonomySaveDTO.toJSON()));
    }
  }
  
  private void inheritEmbeddedClassesToChildTaxonomyLevels(ISaveMasterTaxonomyModel saveAttributionTaxonomyModel) throws Exception
  {
    List<IContextKlassModel> addedContextKlasses = saveAttributionTaxonomyModel.getAddedContextKlasses();
    List<String> addedContextKlassIdsList = new ArrayList<String>();
    IEmbeddedInheritanceInTaxonomyDTO taxonomyInheritanceDTO = new EmbeddedInheritanceInTaxonomyDTO();
    
    for (IContextKlassModel klass : addedContextKlasses) {
      addedContextKlassIdsList.add(klass.getContextKlassId());
    }
    
    taxonomyInheritanceDTO.setId(saveAttributionTaxonomyModel.getId());
    taxonomyInheritanceDTO.setLabel(saveAttributionTaxonomyModel.getLabel());
    taxonomyInheritanceDTO.setCode(saveAttributionTaxonomyModel.getCode());
    taxonomyInheritanceDTO.setAddedContextKlasses(addedContextKlassIdsList);
    
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.MEDIUM;
    BGPDriverDAO.instance().submitBGPProcess(context.getUserName(), "SAVE_TAXONOMY", "", userPriority,
        new JSONContent(ObjectMapperUtil.writeValueAsString(taxonomyInheritanceDTO)));
  }
  
  private void prepareDataForValuePropagationOnSaveKlass(List<IContextInfoForContextualDataTransferModel> relationshipDataForTransferList)
      throws Exception
  {
    for (IContextInfoForContextualDataTransferModel contextInfo : relationshipDataForTransferList) {
      if (!contextInfo.getDeletedDependentAttributes().isEmpty() || !contextInfo.getDeletedInDependentAttributes().isEmpty()
          || !contextInfo.getModifiedDependentAttributes().isEmpty() || !contextInfo.getModifiedInDependentAttributes().isEmpty()
          || !contextInfo.getDeletedTags().isEmpty() || !contextInfo.getModifiedTags().isEmpty()) {
        
        // Add event into BGP.
      }
    }
  }

  @Override
  public WorkflowUtils.UseCases getUsecase()
  {
    return WorkflowUtils.UseCases.SAVEATTRIBUTIONTAXONOMY;
  }
}
