package com.cs.core.util;

import com.cs.config.standard.IConfigMap;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.model.configdetails.ConfigDetailsForSwitchTypeRequestModel;
import com.cs.core.config.interactor.model.configdetails.ICreateInstanceModel;
import com.cs.core.config.interactor.model.language.IGetConfigDetailsToPrepareDataForLanguageInheritanceModel;
import com.cs.core.config.interactor.model.language.ILanguageHierarchyModel;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.runtime.config.strategy.usecase.datatransfer.IGetConfigDetailsToPrepareDataForLanguageInheritanceStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForImportSwitchTypeRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForSwitchTypeRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceSaveModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.utils.BaseEntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ConfigUtil {
  
  @Autowired
  protected TransactionThreadData                                        transactionThread;
  
  @Autowired
  protected RDBMSComponentUtils                                          rdbmsComponentUtils;
  
  @Autowired
  protected IGetConfigDetailsToPrepareDataForLanguageInheritanceStrategy getConfigDetailsToPrepareDataForLanguageInheritanceStrategy;
  
  
  public IMulticlassificationRequestModel getConfigRequestModelForCreateInstance(
      ICreateInstanceModel createInstanceModel)
  {
    return getConfigRequestModelForGivenTypesTaxonomies(Arrays.asList(createInstanceModel.getType()), new ArrayList<String>());
  }

  public IMulticlassificationRequestModel getConfigRequestModelForGivenTypesTaxonomies(
      List<String> types, List<String> taxonomies)
  {
    IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
    this.setTransactionDetails(multiclassificationRequestModel);
    multiclassificationRequestModel.setShouldUseTagIdTagValueIdsMap(false);
    multiclassificationRequestModel.setKlassIds(types);
    multiclassificationRequestModel.setSelectedTaxonomyIds(taxonomies);
    return multiclassificationRequestModel;
  }
  
  public IMulticlassificationRequestModel getConfigRequestModelForCreateInstanceForSingleClone(IBaseEntityDAO baseEntityDAO) throws Exception
  {
    IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
    this.setTransactionDetails(multiclassificationRequestModel);
    this.setBaseEntityDetails(baseEntityDAO, multiclassificationRequestModel);
    return multiclassificationRequestModel;
  }
  
  public IConfigDetailsForSwitchTypeRequestModel getConfigRequestModelForTypeSwitchModel(
      IKlassInstanceTypeSwitchModel klassInstnaceTypeSwitchModel, IBaseEntityDAO baseEntityDAO)
      throws Exception
  {
    IConfigDetailsForSwitchTypeRequestModel multiclassificationRequestModel = new ConfigDetailsForSwitchTypeRequestModel();
    
    this.setTransactionDetails(multiclassificationRequestModel);
    this.setBaseEntityDetails(baseEntityDAO, multiclassificationRequestModel);
    this.setIdDetails(multiclassificationRequestModel, klassInstnaceTypeSwitchModel.getTemplateId(),
        klassInstnaceTypeSwitchModel.getTabId(), klassInstnaceTypeSwitchModel.getTypeId());
    
    multiclassificationRequestModel.setShouldUseTagIdTagValueIdsMap(false);
    multiclassificationRequestModel
        .setAddedKlassIds(klassInstnaceTypeSwitchModel.getAddedSecondaryTypes());
    multiclassificationRequestModel
        .setAddedTaxonomyIds(klassInstnaceTypeSwitchModel.getAddedTaxonomyIds());
    if (klassInstnaceTypeSwitchModel.getDeletedTaxonomyIds() != null) {
      multiclassificationRequestModel.getDeletedTaxonomyIds()
          .addAll(klassInstnaceTypeSwitchModel.getDeletedTaxonomyIds());
      multiclassificationRequestModel
          .setDeletedTaxonomyIds(klassInstnaceTypeSwitchModel.getDeletedTaxonomyIds());
    }
    ((IConfigDetailsForImportSwitchTypeRequestModel) multiclassificationRequestModel)
        .setComponentId(klassInstnaceTypeSwitchModel.getComponentId());
    // multiclassificationRequestModel.getTagIdTagValueIdsMap().putAll(klassInstanceTypeModel.getTagIdTagValueIdsMap());
    
    return multiclassificationRequestModel;
  }
  
  public IMulticlassificationRequestModel getConfigRequestModelForCustomTab(
      IGetInstanceRequestModel instanceRequestModel, IBaseEntityDAO baseEntityDAO) throws Exception
  {
    IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
    this.setTransactionDetails(multiclassificationRequestModel);
    this.setBaseEntityDetails(baseEntityDAO, multiclassificationRequestModel);
    this.setParentEntityDetails(baseEntityDAO, multiclassificationRequestModel);
    this.setIdDetails(multiclassificationRequestModel, instanceRequestModel.getTemplateId(),
        instanceRequestModel.getTabId(), instanceRequestModel.getTypeId());
    
    multiclassificationRequestModel.setShouldUseTagIdTagValueIdsMap(false);
    // multiclassificationRequestModel.getTagIdTagValueIdsMap().putAll(klassInstanceTypeModel.getTagIdTagValueIdsMap());
    
    return multiclassificationRequestModel;
  }
  
  public IMulticlassificationRequestModel getConfigRequestModelForTab(
      IKlassInstanceSaveModel klassInstanceSaveModel, IBaseEntityDAO baseEntityDAO) throws Exception
  {
    IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
    
    this.setTransactionDetails(multiclassificationRequestModel);
    this.setBaseEntityDetailsFromDTO(baseEntityDAO, multiclassificationRequestModel);
    this.setParentEntityDetails(baseEntityDAO, multiclassificationRequestModel);
    this.setIdDetails(multiclassificationRequestModel, klassInstanceSaveModel.getTemplateId(),
        klassInstanceSaveModel.getTabId(), klassInstanceSaveModel.getTypeId());
    
    multiclassificationRequestModel.setShouldUseTagIdTagValueIdsMap(false);
    
    return multiclassificationRequestModel;
  }
  
  public IMulticlassificationRequestModel getConfigRequestModelForSaveInstance(
      String templateId, String tabId, String typeId, List<String> languageCodes, IBaseEntityDAO baseEntityDAO) throws Exception
  {
    IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
    
    this.setTransactionDetails(multiclassificationRequestModel);
    this.setBaseEntityDetailsFromDTO(baseEntityDAO, multiclassificationRequestModel);
    this.setParentEntityDetails(baseEntityDAO, multiclassificationRequestModel);
    this.setIdDetails(multiclassificationRequestModel, templateId, tabId, typeId);
    
    multiclassificationRequestModel.setShouldUseTagIdTagValueIdsMap(false);
    multiclassificationRequestModel.setLanguageCodes(languageCodes);
    
    return multiclassificationRequestModel;
  }
  
  public IMulticlassificationRequestModel getConfigRequestModelForTimeline(
      IGetInstanceRequestModel getInstanceRequestModel, IBaseEntityDAO entityDAO) throws Exception
  {
    IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
    this.setIdDetails(multiclassificationRequestModel, getInstanceRequestModel.getTemplateId(),
        SystemLevelIds.TIMELINE_TAB, getInstanceRequestModel.getTypeId());
    this.setTransactionDetails(multiclassificationRequestModel);
    this.setBaseEntityDetails(entityDAO, multiclassificationRequestModel);
    this.setParentEntityDetails(entityDAO, multiclassificationRequestModel);
    multiclassificationRequestModel.setShouldUseTagIdTagValueIdsMap(false);
    return multiclassificationRequestModel;
  }
  
  public void setBaseEntityDetails(IBaseEntityDAO baseEntityDAO,
      IMulticlassificationRequestModel multiclassificationRequestModel) throws Exception
  {
    List<IClassifierDTO> classifiers = baseEntityDAO.getClassifiers();
    List<String> klassIds = BaseEntityUtils.getReferenceTypeFromClassifierDTO(classifiers);

    klassIds.add(baseEntityDAO.getBaseEntityDTO().getNatureClassifier().getClassifierCode());
    List<String> selectedTaxonomyIds = BaseEntityUtils.getReferenceTaxonomyIdsFromBaseEntity(classifiers);

    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    Set<String> languageCodes = new HashSet<>(baseEntityDTO.getLocaleIds());
    multiclassificationRequestModel.getKlassIds().addAll(klassIds);
    multiclassificationRequestModel.getSelectedTaxonomyIds().addAll(selectedTaxonomyIds);
    multiclassificationRequestModel.setLanguageCodes(new ArrayList<>(languageCodes));
    multiclassificationRequestModel.setBaseType(IConfigMap.getBaseTypeClassPath(baseEntityDAO.getBaseEntityDTO().getBaseType()));
  }

  public void setBaseEntityDetailsFromDTO(IBaseEntityDAO baseEntityDAO,
      IMulticlassificationRequestModel multiClassificationRequestModel) throws Exception
  {
    List<IClassifierDTO> classifiers = new ArrayList<>(baseEntityDAO.getBaseEntityDTO().getOtherClassifiers());
    List<String> klassIds = BaseEntityUtils.getReferenceTypeFromClassifierDTO(classifiers);

    klassIds.add(baseEntityDAO.getBaseEntityDTO().getNatureClassifier().getClassifierCode());
    List<String> selectedTaxonomyIds = BaseEntityUtils.getReferenceTaxonomyIdsFromBaseEntity(classifiers);

    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    Set<String> languageCodes = new HashSet<>(baseEntityDTO.getLocaleIds());
    multiClassificationRequestModel.getKlassIds().addAll(klassIds);
    multiClassificationRequestModel.getSelectedTaxonomyIds().addAll(selectedTaxonomyIds);
    multiClassificationRequestModel.setLanguageCodes(new ArrayList<>(languageCodes));
    multiClassificationRequestModel.setBaseType(IConfigMap.getBaseTypeClassPath(baseEntityDAO.getBaseEntityDTO().getBaseType()));
  }

  public void setParentEntityDetails(IBaseEntityDAO baseEntityDAO,
      IMulticlassificationRequestModel multiclassificationRequestModel) throws Exception
  {
    Long parentIID = baseEntityDAO.getBaseEntityDTO().getParentIID();
    if(parentIID != 0L) {
      List<IBaseEntityDTO> baseEntitiesByIIDs = this.rdbmsComponentUtils.getLocaleCatlogDAO()
          .getBaseEntitiesByIIDs(List.of(String.valueOf(parentIID)));
      IBaseEntityDTO baseEntityDTO = baseEntitiesByIIDs.get(0);
      Map<Boolean, List<String>> classifiers = BaseEntityUtils.seggregateTaxonomyAndClasses(baseEntityDTO.getOtherClassifiers());
      classifiers.get(true).add(baseEntityDTO.getNatureClassifier().getCode());
      multiclassificationRequestModel.setParentKlassIds(classifiers.get(true));
      multiclassificationRequestModel.setParentTaxonomyIds(classifiers.get(false));
    }
  }
  
  public void setTransactionDetails(
      IMulticlassificationRequestModel multiclassificationRequestModel)
  {
    TransactionData transactionData = transactionThread.getTransactionData();
    multiclassificationRequestModel.setEndpointId(transactionData.getEndpointId());
    multiclassificationRequestModel.setPhysicalCatalogId(transactionData.getPhysicalCatalogId());
    multiclassificationRequestModel.setOrganizationId(transactionData.getOrganizationId());
    multiclassificationRequestModel
        .setLanguageCodes(Arrays.asList(transactionData.getDataLanguage()));
    multiclassificationRequestModel.setUserId(transactionData.getUserId());
  }
  
  public void setIdDetails(IMulticlassificationRequestModel multiclassificationRequestModel,
      String templateId, String tabId, String TypeId)
  {
    multiclassificationRequestModel.setTemplateId(templateId);
    multiclassificationRequestModel.setTabId(tabId);
    multiclassificationRequestModel.setTypeId(TypeId);
  }
  
  /* 
   * Return : Child language of current data language to support the language inheritance
   */
  public List<String> getLanguageInheritance() throws Exception
  {
    String dataLanguage = transactionThread.getTransactionData().getDataLanguage();
    
    IIdsListParameterModel requestModel = new IdsListParameterModel();
    List<String> dataLanguageCodes = new ArrayList<>();
    dataLanguageCodes.add(dataLanguage);
    requestModel.setIds( dataLanguageCodes);
    IGetConfigDetailsToPrepareDataForLanguageInheritanceModel languageInheritanceModel = 
        getConfigDetailsToPrepareDataForLanguageInheritanceStrategy.execute(requestModel);
    
    ILanguageHierarchyModel languageHierarchyModel = languageInheritanceModel.getReferencedLanguages().get(dataLanguage);
    List<String> languageInheritance = new ArrayList<>();
    languageInheritance.add(dataLanguage);
    languageInheritance.addAll(languageHierarchyModel.getParents());
    return languageInheritance;
  }

  public IMulticlassificationRequestModel getConfigRequestModelForImport(List<String> languageCodes, IBaseEntityDAO baseEntityDAO) throws Exception
  {
    IMulticlassificationRequestModel multiClassificationRequestModel = new MulticlassificationRequestModel();

    this.setTransactionDetails(multiClassificationRequestModel);
    this.setBaseEntityDetails(baseEntityDAO, multiClassificationRequestModel);
    this.setParentEntityDetails(baseEntityDAO, multiClassificationRequestModel);

    multiClassificationRequestModel.setShouldUseTagIdTagValueIdsMap(false);
    multiClassificationRequestModel.setLanguageCodes(languageCodes);

    return multiClassificationRequestModel;
  }

}
