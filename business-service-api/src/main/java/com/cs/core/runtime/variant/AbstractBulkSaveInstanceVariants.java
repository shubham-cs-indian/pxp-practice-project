package com.cs.core.runtime.variant;

import com.cs.bds.config.usecase.taxonomy.ITypeSwitchInstance;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkSaveInstanceVariantsModel;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.ITemplatePermissionForGetVariantInstancesModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.klassinstance.*;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.variants.*;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForGetVariantInstancesInTableViewStrategy;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsWithoutPermissionsStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.VariantInstanceUtils;
import com.cs.core.runtime.klassinstance.AbstractSaveInstanceForTabs;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.utils.BaseEntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@SuppressWarnings("unchecked")
@Component
public abstract class AbstractBulkSaveInstanceVariants<P extends IBulkSaveInstanceVariantsModel, R extends IBulkSaveKlassInstanceVariantsResponseModel>
    extends AbstractSaveInstanceForTabs<P, R> {
  
  @Autowired
  protected ISessionContext                                            context;
  
  @Autowired
  protected IGetConfigDetailsWithoutPermissionsStrategy                getConfigDetailsWithoutPermissionsStrategy;
  
  @Autowired
  protected IGetConfigDetailsForGetVariantInstancesInTableViewStrategy getconfigDetailsForGetVariantInstancesInTableViewStrategy;
  
  @Autowired
  VariantInstanceUtils                                                 variantInstanceUtils;
  
  @Autowired
  RDBMSComponentUtils                                                  rdbmsComponentUtils;
  
  @Autowired
  protected ITypeSwitchInstance                                        typeSwitchInstance;
  
  protected abstract String getBaseType();

  protected abstract IGetVariantInstancesInTableViewModel executeGetTableView(IGetVariantInstanceInTableViewRequestModel tableViewRequestModel) throws Exception;

  @Override
  protected R executeInternal(P model) throws Exception
  {
    List<IKlassInstanceSaveModel> instancesToSave = model.getInstancesToSave();
    String parentId = model.getTableViewRequest().getParentId();
    
    IBaseEntityDAO baseEntityDAOByIID = rdbmsComponentUtils.getBaseEntityDAO(Long.parseLong(parentId));
    IKlassInstanceTypeModel klassInstanceTypeModel = new KlassInstanceTypeModel();
    
    klassInstanceTypeModel
        .setParentKlassIds(BaseEntityUtils.getAllReferenceTypeFromBaseEntity(baseEntityDAOByIID));
    klassInstanceTypeModel.setParentTaxonomyIds(
        BaseEntityUtils.getReferenceTaxonomyIdsFromBaseEntity(baseEntityDAOByIID.getClassifiers()));
    
    List<String> languageCodes = klassInstanceTypeModel.getLanguageCodes();
    
    IExceptionModel failure = executeBulkSaveInstances(instancesToSave, languageCodes);
    
    IGetVariantInstanceInTableViewRequestModel tableViewRequestModel = model.getTableViewRequest();
    IGetVariantInstancesInTableViewModel getTableResponse = executeGetTableView(tableViewRequestModel);
    IBulkSaveKlassInstanceVariantsResponseModel responseModel = new BulkSaveKlassInstanceVariantsResponseModel();
    responseModel.setSuccess(getTableResponse);
    responseModel.setFailure(failure);
    
    return (R) responseModel;
  }

  private IGetConfigDetailsForCustomTabModel getConfigDetails(
      IKlassInstanceSaveModel klassInstance,
      List<String> languageCodes, IBaseEntityDAO baseEntityDAO) throws Exception
  {
    IMulticlassificationRequestModel configModel = new MulticlassificationRequestModel();
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    
    for (IClassifierDTO classifier : baseEntityDTO.getOtherClassifiers()) {
      
      if (classifier.getClassifierType() == ClassifierType.CLASS) 
        configModel.getKlassIds().add(classifier.getClassifierCode());
      
      else if (classifier.getClassifierType() == ClassifierType.TAXONOMY) 
        configModel.getSelectedTaxonomyIds().add(classifier.getClassifierCode());
    }
    
    configModel.getKlassIds().addAll(klassInstance.getTypes());
    configModel.setTypeId(klassInstance.getTypeId());
    configModel.setTabId(klassInstance.getTabId());
    configModel.setLanguageCodes(languageCodes);
    configModel.setShouldUseTagIdTagValueIdsMap(false);
    configModel.setUserId(context.getUserId());
    
    IGetConfigDetailsForCustomTabModel configDetails = getConfigDetailsWithoutPermissionsStrategy
        .execute(configModel);
    return configDetails;
  }
  
  private IGetVariantInstancesInTableViewModel prepareDataForResponse(
      IGetVariantInstanceInTableViewRequestModel requestModel,
      IKlassInstanceTypeModel klassInstanceTypeModel) throws Exception
  {
    
    Map<Long, Map<String, List<String>>> childrensVSotherClassifiers = getChildrenVSotherClassifiers(
        requestModel.getParentId(), requestModel.getContextId());
    
    IConfigDetailsForGetVariantInstancesInTableViewRequestModel configRequestModel = new ConfigDetailsForGetVariantInstancesInTableViewRequestModel();
    configRequestModel.setInstanceIdVsOtherClassifiers(childrensVSotherClassifiers);
    configRequestModel.setUserId(context.getUserId());
    configRequestModel.setContextId(requestModel.getContextId());
    configRequestModel.setPropertyIds(requestModel.getColumnIds());
    configRequestModel.setParentKlassIds(klassInstanceTypeModel.getParentKlassIds());
    configRequestModel.setParentTaxonomyIds(klassInstanceTypeModel.getParentTaxonomyIds());
    configRequestModel.setBaseType(getBaseType());
    IConfigDetailsForGetVariantInstancesInTableViewModel configDetails = getconfigDetailsForGetVariantInstancesInTableViewStrategy
        .execute(configRequestModel);
    
    IGetVariantInstancesInTableViewRequestStrategyModel elasticRequestModel = new GetVariantInstancesInTableViewStrategyModel();
    ITemplatePermissionForGetVariantInstancesModel referencedPermissions = (ITemplatePermissionForGetVariantInstancesModel) configDetails
        .getReferencedPermissions();
    
    Map<String, IReferencedSectionElementModel> referencedElements = getReferencedElements(configDetails.getInstanceIdVsReferencedElements());
    configDetails.setReferencedElements(referencedElements);
    elasticRequestModel.setReferencedElements(referencedElements);
    
    elasticRequestModel.setAttributes(requestModel.getAttributes());
    elasticRequestModel.setKlassInstanceId(requestModel.getKlassInstanceId());
    elasticRequestModel.setContextId(requestModel.getContextId());
    elasticRequestModel.setFrom(requestModel.getFrom());
    elasticRequestModel.setParentId(requestModel.getParentId());
    elasticRequestModel.setReferencedTags(configDetails.getReferencedTags());
    elasticRequestModel.setReferencedVariantContexts(configDetails.getReferencedVariantContexts()
        .getEmbeddedVariantContexts());
    elasticRequestModel.setSize(requestModel.getSize());
    elasticRequestModel.setSortOptions(requestModel.getSortOptions());
    elasticRequestModel.setTags(requestModel.getTags());
    elasticRequestModel.setColumnIds(requestModel.getColumnIds());
    elasticRequestModel.setCurrentUserId(context.getUserId());
    elasticRequestModel.setFilterInfo(configDetails.getFilterInfo());
    elasticRequestModel.setTimeRange(requestModel.getTimeRange());
    elasticRequestModel.setEntities(new ArrayList<>(referencedPermissions.getEntitiesHavingRP()));
    elasticRequestModel.setAllSearch(requestModel.getAllSearch());
    elasticRequestModel.setKlassIdsHavingRP(referencedPermissions.getKlassIdsHavingRP());
    elasticRequestModel
        .setContextKlassIdsHavingRP(referencedPermissions.getContextKlassIdsHavingRP());
    elasticRequestModel
        .setReferencedPropertyCollections(configDetails.getReferencedPropertyCollections());
    
    IGetVariantInstancesInTableViewModel returnModel = variantInstanceUtils
        .executeGetVariantInstances(elasticRequestModel, configDetails);
    returnModel.setConfigDetails(configDetails);
    
    /*    variantInstanceUtils.skipAttributesHavingAttributeContexts(returnModel.getColumns(),
        configDetails.getReferencedElements());*/
    returnModel.setConfigDetails(configDetails);
    returnModel.setFilterInfo(configDetails.getFilterInfo());
    return returnModel;
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
  
  private IExceptionModel executeBulkSaveInstances(
      List<IKlassInstanceSaveModel> klassInstanceModels,
      List<String> languageCodes) throws Exception
  {
    
    List<String> successIds = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    
    /* From UI only one klass instance will come for save.
      Handling for Bulk is done if this service is triggered from any other source having more than 1 instance.
     */
     
    for (IKlassInstanceSaveModel instance : klassInstanceModels) {
      String instanceId = (String) instance.getId();
     
      try {
        IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(Long.parseLong(instanceId));
        IGetConfigDetailsForCustomTabModel configDetails = getConfigDetails(instance, languageCodes, baseEntityDAO);

        saveBaseEntity(instance, baseEntityDAO, configDetails);
        successIds.add(instanceId);
        
        fillClassifiersAndCallTypeSwitchRequest(baseEntityDAO.getBaseEntityDTO(), instance.getAddedTaxonomyIds(), 
            instance.getDeletedTaxonomyIds());
        rdbmsComponentUtils.getLocaleCatlogDAO().postUsecaseUpdate(Long.parseLong(instanceId),
            IEventDTO.EventType.ELASTIC_UPDATE);
      }
     
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, instanceId, instance.getName());
      }
    }
    
    String userId = context.getUserId();
    for (IKlassInstanceSaveModel klassInstanceModel : klassInstanceModels) {
      klassInstanceModel.setLastModifiedBy(userId);
    }
    return failure;
  }
  
  public IKlassInstanceTypeSwitchModel fillClassifiersAndCallTypeSwitchRequest(IBaseEntityDTO baseEntityDTO,
      List<String> addedClassifiers, List<String> removedClassifiers)
      throws Exception
  {
    IKlassInstanceTypeSwitchModel typeSwitchModel = new KlassInstanceTypeSwitchModel();
    if (!addedClassifiers.isEmpty() || !removedClassifiers.isEmpty()) {
      
      typeSwitchModel.setKlassInstanceId(Long.toString(baseEntityDTO.getBaseEntityIID()));
      typeSwitchModel.setAddedTaxonomyIds(addedClassifiers);
      typeSwitchModel.setDeletedTaxonomyIds(removedClassifiers);
      typeSwitchInstance.execute(typeSwitchModel);
    }
    return typeSwitchModel;
  }
  
}
