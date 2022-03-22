package com.cs.core.runtime.interactor.usecase.instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.constants.Constants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.relationship.IReferencedRelationshipProperty;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextModel;
import com.cs.core.config.strategy.usecase.task.IGetConfigDetailsForTasksTabStrategy;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dto.PropertyRecordDTO;
import com.cs.core.rdbms.entity.dto.ValueRecordDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IContextualDataDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.services.resolvers.RuleHandler;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.KlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IImageAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveGridEditPermission;
import com.cs.core.runtime.interactor.exception.variants.DuplicateVariantExistsException;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.runtime.interactor.model.attribute.IModifiedAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.attribute.IModifiedContentAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedContentTagInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceSaveModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipSidePropertiesModel;
import com.cs.core.runtime.interactor.model.taskinstance.GetTaskInstanceResponseModel;
import com.cs.core.runtime.interactor.model.templating.GetKlassInstanceForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.version.GetKlassInstanceVersionForTimeLineModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForCustomTabStrategy;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsWithoutPermissionsStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.PropertyRecordBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.PropertyRecordBuilder.PropertyRecordType;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RelationshipInstanceUtil;
import com.cs.core.runtime.interactor.utils.klassinstance.VariantInstanceUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.util.ConfigUtil;
import com.cs.core.util.VersionUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

@SuppressWarnings("unchecked")
@Component
public abstract class AbstractSaveInstanceForTabs<P extends IModel, R extends IModel> extends AbstractRuntimeInteractor<P, R> {
  
  @Autowired
  protected IGetConfigDetailsForTasksTabStrategy                         getConfigDetailsForTasksTabStrategy;
  
  @Autowired
  protected PermissionUtils                                              permissionUtils;
  
  @Autowired
  protected IGetConfigDetailsWithoutPermissionsStrategy                  getConfigDetailsWithoutPermissionsStrategy;
  
  @Autowired
  protected IGetConfigDetailsForCustomTabStrategy                        getConfigDetailsForCustomTabStrategy;
  
  @Autowired
  protected RDBMSComponentUtils                                          rdbmsComponentUtils;
  
  @Autowired
  protected ConfigUtil                                                   configUtil;
  
  @Autowired
  protected VariantInstanceUtils                                         variantInstanceUtils;
  
  @Autowired
  protected TransactionThreadData                                        controllerThread;
  
  @Autowired
  protected VersionUtils versionUtils;
  
  @Autowired
  RelationshipInstanceUtil relationshipInstanceUtil;
  
  @Autowired
  protected KlassInstanceUtils                                            klassInstanceUtils;
  
  @Override
  protected R executeInternal(P instancesModel) throws Exception
  {
    IKlassInstanceSaveModel klassInstancesModel = (IKlassInstanceSaveModel) instancesModel;
    if (klassInstancesModel.getIsGridEdit()) {
      checkGridEditPermission();
    }
    long starTime = System.currentTimeMillis();
    
    List<String>  languageInheritance = configUtil.getLanguageInheritance();
    
    IBaseEntityDAO baseEntityDAO = this.rdbmsComponentUtils.getBaseEntityDAO(Long.parseLong(klassInstancesModel.getId()),
        languageInheritance);
    
    RDBMSLogger.instance()
        .debug("NA|RDBMS|" + this.getClass()
            .getSimpleName() + "|executeInternal|getEntityByIID| %d ms", System.currentTimeMillis() - starTime);
    
    IMulticlassificationRequestModel multiclassificationRequestModel = this.configUtil.getConfigRequestModelForSaveInstance(klassInstancesModel.getTemplateId(), klassInstancesModel.getTabId(), klassInstancesModel.getTypeId(),
        klassInstancesModel.getLanguageCodes(), baseEntityDAO);
    
    long starTime1 = System.currentTimeMillis();
    IGetConfigDetailsForCustomTabModel configDetails = getConfigDetailsForSave(multiclassificationRequestModel);
    
    RDBMSLogger.instance()
        .debug("NA|OrientDB|" + this.getClass()
            .getSimpleName() + "|executeInternal|ConfigDetailsForSave| %d ms", System.currentTimeMillis() - starTime1);
    saveBaseEntity(klassInstancesModel, baseEntityDAO, configDetails);

    multiclassificationRequestModel = this.configUtil.getConfigRequestModelForTab(klassInstancesModel, baseEntityDAO);
    long starTime2 = System.currentTimeMillis();

    configDetails = getConfigDetails(multiclassificationRequestModel, klassInstancesModel);
    RDBMSLogger.instance()
        .debug("NA|OrientDB|" + this.getClass()
            .getSimpleName() + "|executeInternal|Config Details after save| %d ms", System.currentTimeMillis() - starTime2);

    IGetKlassInstanceModel returnModel = prepareDataForResponse(klassInstancesModel, configDetails, baseEntityDAO);

    boolean shouldCreateRevision = false;
    List<IContentAttributeInstance> addedAttributes = klassInstancesModel.getAddedAttributes();
    List<String> versionableAttributes = configDetails.getVersionableAttributes();
    for (IContentAttributeInstance addedAttribute : addedAttributes) {
      if (versionableAttributes.contains(addedAttribute.getAttributeId())) {
        shouldCreateRevision = true;
      }
    }

    if (!shouldCreateRevision && !klassInstancesModel.getIsSaveAndPublish()) {
      shouldCreateRevision = shouldCreateRevision(klassInstancesModel, configDetails);
    }

    if(klassInstancesModel.getIsSaveAndPublish() || shouldCreateRevision) {
      rdbmsComponentUtils.createNewRevision(baseEntityDAO.getBaseEntityDTO(), configDetails.getNumberOfVersionsToMaintain());
    }
    rdbmsComponentUtils.getLocaleCatlogDAO().postUsecaseUpdate(Long.parseLong(klassInstancesModel.getId()), IEventDTO.EventType.ELASTIC_UPDATE);
    return (R) returnModel;
  }

  /**
   * it will check should we create new revision for modified Attributes ,
   * modified tags and added tags
   * 
   * @param klassInstancesModel
   * @param configDetails
   * @return
   */
  protected boolean shouldCreateRevision(IKlassInstanceSaveModel klassInstancesModel,
      IGetConfigDetailsForCustomTabModel configDetails)
  {
    List<String> versionableAttributes = configDetails.getVersionableAttributes();
    List<IModifiedContentAttributeInstanceModel> modifiedAttributes = klassInstancesModel
        .getModifiedAttributes();
    for (IModifiedContentAttributeInstanceModel modifiedAttribute : modifiedAttributes) {
      if (versionableAttributes.contains(modifiedAttribute.getAttributeId())) {
        return true;
      }
    }
    
    List<String> versionableTags = configDetails.getVersionableTags();
    List<ITagInstance> addedTags = klassInstancesModel.getAddedTags();
    for (ITagInstance addedTag : addedTags) {
      if (versionableTags.contains(addedTag.getTagId())) {
        return true;
      }
    }
    
    List<IModifiedContentTagInstanceModel> modifiedTags = klassInstancesModel.getModifiedTags();
    for (IModifiedContentTagInstanceModel modifiedTag : modifiedTags) {
      if (versionableTags.contains(modifiedTag.getTagId())) {
        return true;
      }
    }
    return false;
  }
  
  public void prepareDataAndCreateImageVariants(IGetConfigDetailsForCustomTabModel configDetails, IImageAttributeInstance coverFlowAttribute,
      String instanceId, String parentId, IGetKlassInstanceCustomTabModel returnModel) throws Exception
  {
    
  }
  
  protected void saveBaseEntity(IKlassInstanceSaveModel klassInstancesModel, IBaseEntityDAO baseEntityDAO,
      IGetConfigDetailsForCustomTabModel configDetails) throws Exception
  {
    String defaultAssetInstanceId = klassInstancesModel.getDefaultAssetInstanceId();
    if(StringUtils.isNotEmpty(defaultAssetInstanceId)) {
      long defaultAssetInstanceIId = Long.parseLong(defaultAssetInstanceId);
      baseEntityDAO.getBaseEntityDTO().setDefaultImageIID(defaultAssetInstanceIId);
    }
    manageContextualProperties(klassInstancesModel, configDetails, baseEntityDAO);
    this.manageProperties(klassInstancesModel, configDetails, baseEntityDAO);
    RuleHandler ruleHandler = new RuleHandler();
    ruleHandler.initiateRuleHandling(baseEntityDAO, rdbmsComponentUtils.getLocaleCatlogDAO(), false,
        configDetails.getReferencedElements(), configDetails.getReferencedAttributes(), configDetails.getReferencedTags());
  }
  
  protected IGetConfigDetailsForCustomTabModel getConfigDetailsForSave(IMulticlassificationRequestModel multiclassificationRequestModel)
      throws Exception
  {
    return getConfigDetailsWithoutPermissionsStrategy.execute(multiclassificationRequestModel);
  }
  
  @SuppressWarnings("unused")
  private void getPropertyIdsToRemoveConflictsFrom(List<String> propertyIdsToRemoveConflictsFrom, IRelationshipSidePropertiesModel side1)
  {
    List<IReferencedRelationshipProperty> attributes = side1.getAttributes();
    for (IReferencedRelationshipProperty attribute : attributes) {
      propertyIdsToRemoveConflictsFrom.add(attribute.getId());
    }
    List<IReferencedRelationshipProperty> tags = side1.getTags();
    for (IReferencedRelationshipProperty tag : tags) {
      propertyIdsToRemoveConflictsFrom.add(tag.getId());
    }
  }
  
  protected IGetKlassInstanceModel prepareDataForResponse(IKlassInstanceSaveModel klassInstanceSaveModel,
      IGetConfigDetailsModel configDetails, IBaseEntityDAO baseEntityDAO) throws Exception
  {
    IGetKlassInstanceModel returnModel = executeGetKlassInstance(configDetails, klassInstanceSaveModel,
        baseEntityDAO);
    returnModel.setVariantOfLabel(klassInstanceUtils.getVariantOfLabel(baseEntityDAO, 
        ((IGetConfigDetailsForCustomTabModel)configDetails).getLinkedVariantCodes()));
    returnModel.setBranchOfLabel(
        KlassInstanceUtils.getBranchOfLabel(baseEntityDAO.getBaseEntityDTO(), rdbmsComponentUtils));
    permissionUtils.manageKlassInstancePermissions(returnModel);
    return returnModel;
  }
  
  public IGetConfigDetailsForCustomTabModel getConfigDetails(IMulticlassificationRequestModel model, IKlassInstanceSaveModel saveModel)
      throws Exception
  {
    switch (saveModel.getTabType()) {
      case CommonConstants.CUSTOM_TEMPLATE_TAB_BASETYPE:
        return getConfigDetailsForCustomTabStrategy.execute(model);
      case CommonConstants.TEMPLATE_TASKS_TAB_BASETYPE:
        return (IGetConfigDetailsForCustomTabModel) getConfigDetailsForTasksTabStrategy.execute(model);
      case CommonConstants.TEMPLATE_TIME_LINE_TAB_BASETYPE:
        return getConfigDetailsForCustomTabStrategy.execute(model);
      default:
        return null;
    }
  }
  
  protected void manageProperties(IKlassInstanceSaveModel klassInstancesModel, IGetConfigDetailsModel configDetails, IBaseEntityDAO baseEntityDAO)
      throws Exception
  {
    // manage modified properties
    manageModifiedProperties(klassInstancesModel, configDetails, baseEntityDAO);
    
    // manage added properties
    manageAddedProperties(klassInstancesModel, configDetails, baseEntityDAO);
    
    // manage deleted properties
    manageDeletedProperties(klassInstancesModel, configDetails, baseEntityDAO);
  }
  
  protected void manageModifiedProperties(IKlassInstanceSaveModel klassInstancesModel, IGetConfigDetailsModel configDetails,
      IBaseEntityDAO baseEntityDAO) throws Exception
  {
    PropertyRecordBuilder propertyRecordBuilder = new PropertyRecordBuilder(baseEntityDAO, configDetails, PropertyRecordType.UPDATE, rdbmsComponentUtils.getLocaleCatlogDAO());
    
    // manage modified attributes
    List<IPropertyRecordDTO> propertyRecordDTO = this.manageModifiedAttributes(klassInstancesModel, configDetails, propertyRecordBuilder,
        baseEntityDAO);
    
    // manage modified tags
    propertyRecordDTO.addAll(this.manageModifiedTags(klassInstancesModel, configDetails, propertyRecordBuilder));
    
    IPropertyRecordDTO[] propertyRecords = propertyRecordDTO.toArray(new IPropertyRecordDTO[propertyRecordDTO.size()]);
    
    long starTime = System.currentTimeMillis();
    
    String dataLanguage = controllerThread.getTransactionData().getDataLanguage();
    List<IPropertyRecordDTO> updatePropertyRecords = new ArrayList<>();
    List<IPropertyRecordDTO> createdPropertyRecords = new ArrayList<>();
    for(IPropertyRecordDTO propRecordDTO : propertyRecords) {
      if(propRecordDTO instanceof ValueRecordDTO) {
        ValueRecordDTO valueRecordDTO = (ValueRecordDTO) propRecordDTO;
        if (StringUtils.isEmpty(valueRecordDTO.getLocaleID()) || valueRecordDTO.getLocaleID().equals(dataLanguage))
          updatePropertyRecords.add(valueRecordDTO);
        else {
          valueRecordDTO.setLocaleID(dataLanguage);
          createdPropertyRecords.add(valueRecordDTO);
       }
      }else {
        updatePropertyRecords.add(propRecordDTO);
      }
    }
      
    baseEntityDAO.createPropertyRecords(createdPropertyRecords.toArray(new IPropertyRecordDTO[0]));
    baseEntityDAO.updatePropertyRecords(updatePropertyRecords.toArray(new IPropertyRecordDTO[0]));
    RDBMSLogger.instance()
        .debug("NA|RDBMS|" + this.getClass()
            .getSimpleName() + "|manageModifiedProperties|updatePropertyRecords| %d ms", System.currentTimeMillis() - starTime);
    
    // Handle coupling resolve
    Set<IPropertyDTO> couplingNotificationProperty = propertyRecordBuilder.getCouplingNotificationProperty();
    Set<IPropertyRecordDTO> couplingNotificationPropertyRecord = propertyRecordBuilder.getCouplingNotificationPropertyRecord();
    if (couplingNotificationProperty.size() > 0 && couplingNotificationPropertyRecord.size() > 0) {
      baseEntityDAO.loadCouplingNotifications(couplingNotificationProperty.toArray(new IPropertyDTO[couplingNotificationProperty.size()]));
      baseEntityDAO.resolveTightlyCoupledRecords(
          couplingNotificationPropertyRecord.toArray(new IPropertyRecordDTO[couplingNotificationPropertyRecord.size()]));
    }
    
  }
  
  protected void manageAddedProperties(IKlassInstanceSaveModel klassInstancesModel, IGetConfigDetailsModel configDetails,
      IBaseEntityDAO baseEntityDAO) throws Exception
  {
    PropertyRecordBuilder propertyRecordBuilder = new PropertyRecordBuilder(baseEntityDAO, configDetails, PropertyRecordType.CREATE, rdbmsComponentUtils.getLocaleCatlogDAO());
    
    // manage added attributes
    List<IPropertyRecordDTO> propertyRecordDTO = manageAddedAttributes(klassInstancesModel, configDetails, propertyRecordBuilder, baseEntityDAO);
    
    // manage added tags
    propertyRecordDTO.addAll(this.manageAddedTags(klassInstancesModel, configDetails, propertyRecordBuilder));
    IPropertyRecordDTO[] propertyRecords = propertyRecordDTO.toArray(new IPropertyRecordDTO[propertyRecordDTO.size()]);
    long starTime = System.currentTimeMillis();
    baseEntityDAO.createPropertyRecords(propertyRecords);
    RDBMSLogger.instance()
        .debug("NA|RDBMS|" + this.getClass()
            .getSimpleName() + "|manageAddedProperties|createPropertyRecords| %d ms", System.currentTimeMillis() - starTime);
  }
  
  protected List<IPropertyRecordDTO> manageModifiedAttributes(IKlassInstanceSaveModel klassInstancesModel, IGetConfigDetailsModel configDetails,
      PropertyRecordBuilder propertyRecordBuilder, IBaseEntityDAO baseEntityDAO) throws Exception
  {
    if (klassInstancesModel.getModifiedAttributes()
        .isEmpty())
      return new ArrayList<IPropertyRecordDTO>();
    
    List<IModifiedContentAttributeInstanceModel> modifiedAttributes = klassInstancesModel.getModifiedAttributes();
    
    List<IPropertyRecordDTO> propertyRecordDTO = new ArrayList<>();
    
    for (IModifiedContentAttributeInstanceModel modifiedAttribute : modifiedAttributes) {
      if (!modifiedAttribute.getAttributeId()
          .equals(Constants.ASSET_COVERFLOW_ATTRIBUTE_ID)) {
        IPropertyRecordDTO updateValueRecord = propertyRecordBuilder.updateValueRecord(modifiedAttribute);
        if((((IModifiedAttributeInstanceModel)modifiedAttribute).getModifiedContext() !=null) && (baseEntityDAO.isPropertyRecordDuplicate(updateValueRecord))) {

          throw new DuplicateVariantExistsException();
        }
        
        IContextualDataDTO context = ((IValueRecordDTO) updateValueRecord).getContextualObject();
        String contextCode = context.getContextCode();
        if (!StringUtils.isEmpty(contextCode)) {
          variantInstanceUtils.checkContextFieldsExists(context.getContextTagValues(), context,
              configDetails.getReferencedVariantContexts().getEmbeddedVariantContexts().get(contextCode));
        }
        
        if (updateValueRecord != null) {
          propertyRecordDTO.add(updateValueRecord);
        }
      }
    }
    return propertyRecordDTO;
  }
  
  protected void fillEntityExtension(IBaseEntityDAO baseEntityDAO, IAssetInformationModel assetInformation) throws JsonProcessingException
  {
    Map<String, Object> imageAttr = new HashMap<>();
    imageAttr.put(IAssetInformationModel.ASSET_OBJECT_KEY, assetInformation.getAssetObjectKey());
    imageAttr.put(IAssetInformationModel.FILENAME, assetInformation.getFileName());
    imageAttr.put(IAssetInformationModel.PROPERTIES, assetInformation.getProperties());
    imageAttr.put(IAssetInformationModel.THUMB_KEY, assetInformation.getThumbKey());
    imageAttr.put(IAssetInformationModel.TYPE, assetInformation.getType());
    imageAttr.put(IAssetInformationModel.PREVIEW_IMAGE_KEY, assetInformation.getPreviewImageKey());
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    try {
      baseEntityDTO.setEntityExtension(ObjectMapperUtil.writeValueAsString(imageAttr));
      baseEntityDTO.setHashCode(assetInformation.getHash());
    }
    catch (CSFormatException e) {
      RDBMSLogger.instance().exception(e);
    }
  }
  
  protected List<IPropertyRecordDTO> manageAddedAttributes(IKlassInstanceSaveModel klassInstancesModel, IGetConfigDetailsModel configDetails,
      PropertyRecordBuilder propertyRecordBuilder, IBaseEntityDAO baseEntityDAO) throws Exception
  {
    if (klassInstancesModel.getAddedAttributes()
        .isEmpty())
      return new ArrayList<IPropertyRecordDTO>();
    
    List<IContentAttributeInstance> addedAttributes = klassInstancesModel.getAddedAttributes();
    List<IPropertyRecordDTO> valueRecords = new ArrayList<>();
    IPropertyRecordDTO updateValueRecord = null;
    for (IContentAttributeInstance addedAttribute : addedAttributes) {
        IAttribute attributeConfig = configDetails.getReferencedAttributes().get(addedAttribute.getAttributeId());
        if(attributeConfig.getIsTranslatable()) {
          IAttributeInstance attributeInstance = (IAttributeInstance)addedAttribute;
          attributeInstance.setLanguage(controllerThread.getTransactionData().getDataLanguage());
          updateValueRecord = propertyRecordBuilder.updateValueRecord((IContentAttributeInstance)attributeInstance);
          valueRecords.add(updateValueRecord);
        }
        else {
          updateValueRecord = propertyRecordBuilder.updateValueRecord(addedAttribute);
          valueRecords.add(updateValueRecord);
        }
      
      if((((IAttributeInstance)addedAttribute).getContext() !=null) && (baseEntityDAO.isPropertyRecordDuplicate(updateValueRecord))) {
        throw new DuplicateVariantExistsException();
      }
    }
    
    return valueRecords;
  }
  
  protected List<IPropertyRecordDTO> manageModifiedTags(IKlassInstanceSaveModel klassInstancesModel, IGetConfigDetailsModel configDetails,
      PropertyRecordBuilder propertyRecordBuilder) throws Exception
  {
    if (klassInstancesModel.getModifiedTags()
        .isEmpty())
      return new ArrayList<IPropertyRecordDTO>();
    
    List<IModifiedContentTagInstanceModel> modifiedTags = klassInstancesModel.getModifiedTags();
    
    List<IPropertyRecordDTO> propertyRecordDTO = modifiedTags.stream()
        .filter(x -> ((ITagInstance) x.getEntity()).getContextInstanceId() == null)
        .map(modifiedTag -> {
          try {
            return propertyRecordBuilder.updateTagsRecord(modifiedTag);
          }
          catch (Exception e) {
            throw new RuntimeException(e);
          }
        })
        .filter(propertyRecord -> propertyRecord != null)
        .collect(Collectors.toList());
    
    return propertyRecordDTO;
  }
  
  protected List<IPropertyRecordDTO> manageAddedTags(IKlassInstanceSaveModel klassInstancesModel, IGetConfigDetailsModel configDetails,
      PropertyRecordBuilder propertyRecordBuilder) throws Exception
  {
    if (klassInstancesModel.getAddedTags()
        .isEmpty())
      return new ArrayList<IPropertyRecordDTO>();
    List<ITagInstance> addedTags = klassInstancesModel.getAddedTags();
    List<IPropertyRecordDTO> propertyRecordDTOList = addedTags.stream()
        .filter(x -> x.getContextInstanceId() == null)
        .map(addedTag -> {
          try {
            return propertyRecordBuilder.updateTagsRecord(addedTag);
          }
          catch (Exception e) {
            throw new RuntimeException(e);
          }
        })
        .filter(propertyRecord -> propertyRecord != null)
        .collect(Collectors.toList());
    return propertyRecordDTOList;
  }
  
  protected void manageDeletedProperties(IKlassInstanceSaveModel klassInstancesModel, IGetConfigDetailsModel configDetails,
      IBaseEntityDAO baseEntityDAO) throws Exception
  {
    this.manageDeletedAttributes(klassInstancesModel.getDeletedAttributes(), configDetails, baseEntityDAO);
  }
  
  protected void manageDeletedAttributes(List<String> deletedAttributes, IGetConfigDetailsModel configDetails, IBaseEntityDAO baseEntityDAO)
      throws Exception
  {
    List<IValueRecordDTO> deletedValueRecords = new ArrayList<>();
    deletedAttributes.forEach(deletedAttribute -> {
      try {
        long entityIID = PropertyRecordBuilder.getEntityIIDFromAttributeID(deletedAttribute);
        long valueIID = PropertyRecordBuilder.getValueIIDFromAttributeID(deletedAttribute);
        long propertyIID = PropertyRecordBuilder.getPropertyIIDFromAttributeID(deletedAttribute);
        // FOR IMAGE ATTRIBUTE: CURRENT LIMITATION, ATTRIBUTE GENERATED ON
        // INTERACTOR, NOT
        // MAINTAINED IN DB
        if (entityIID == 0 || valueIID == 0) {
          return;
        }
        IPropertyDTO propertyDTO = RDBMSUtils.getPropertyByIID(propertyIID);
        IValueRecordDTO valueRecordDTO = baseEntityDAO.newValueRecordDTOBuilder(propertyDTO, "")
            .localeID(controllerThread.getTransactionData().getDataLanguage()).valueIID(valueIID).build();
        deletedValueRecords.add(valueRecordDTO);
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    if (!deletedValueRecords.isEmpty()) {
      baseEntityDAO.removeValueRecords(deletedValueRecords.toArray(new IValueRecordDTO[deletedValueRecords.size()]));
    }
  }
  
  protected IGetKlassInstanceModel executeGetKlassInstance(IGetConfigDetailsModel configDetails, IKlassInstanceSaveModel saveInstanceModel,
      IBaseEntityDAO baseEntityDAO) throws Exception
  {
    KlassInstanceBuilder klassInstanceBuilder = new KlassInstanceBuilder(baseEntityDAO,
        configDetails, this.rdbmsComponentUtils);
    IKlassInstance klassInstance = klassInstanceBuilder.getKlassInstance();
    IGetKlassInstanceModel model = null;
    switch (saveInstanceModel.getTabType()) {
      case CommonConstants.CUSTOM_TEMPLATE_TAB_BASETYPE:
        model = new GetKlassInstanceForCustomTabModel();
        ((IGetKlassInstanceCustomTabModel) model).setAttributeVariantsStats(klassInstanceBuilder.getAttributeVariantsStats());
        if (!((IGetConfigDetailsForCustomTabModel)configDetails).getReferencedNatureRelationships().isEmpty()) {
          setNatureRelationship((IGetConfigDetailsForCustomTabModel)configDetails, (IGetKlassInstanceCustomTabModel) model);
        }
        relationshipInstanceUtil.executeGetRelationshipInstance((IGetKlassInstanceCustomTabModel) model, (IGetConfigDetailsForCustomTabModel)configDetails, baseEntityDAO, this.rdbmsComponentUtils);
        break;
      case CommonConstants.TEMPLATE_TASKS_TAB_BASETYPE:
        model =  new GetTaskInstanceResponseModel();
        break;
      case CommonConstants.TEMPLATE_TIME_LINE_TAB_BASETYPE:
        model = new GetKlassInstanceVersionForTimeLineModel();
        /*  ((IGetKlassInstanceVersionsForTimeLineModel)model).getVersions().
        addAll(versionUtils.getObjectRevisions(baseEntityDAO.getBaseEntityDTO().getBaseEntityIID(),
            ((IGetConfigDetailsForCustomTabModel) configDetails).getNumberOfVersionsToMaintain(), 0, false));
        */      break;
      default:
        break;
    }
    
    //on any tab we will get the context information
    model.setReferencedInstances(variantInstanceUtils.fillContextualData(configDetails, baseEntityDAO, klassInstance));
    
    model.setKlassInstance((IContentInstance) klassInstance);
    model.setConfigDetails(configDetails);
    return model;
  }
  
  public void setNatureRelationship(IGetConfigDetailsForCustomTabModel configDetails, IGetKlassInstanceCustomTabModel returnModel) throws RDBMSException, Exception
  {
    IKlassInstanceRelationshipInstance klassInstanceRelationshipInstance = new KlassInstanceRelationshipInstance();
    Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships = configDetails.getReferencedNatureRelationships();
    
    Set<Entry<String, IGetReferencedNatureRelationshipModel>> natureRelationshipsEntrySet = referencedNatureRelationships.entrySet();
    for(Entry<String, IGetReferencedNatureRelationshipModel> natureRelationshipEntrySet : natureRelationshipsEntrySet) {
      IGetReferencedNatureRelationshipModel natureRelationship = natureRelationshipEntrySet.getValue();
      klassInstanceRelationshipInstance.setRelationshipId(natureRelationship.getId());
      klassInstanceRelationshipInstance.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.RELATIONSHIP.getPrefix()));
      klassInstanceRelationshipInstance.setSideId(natureRelationship.getSide1()
          .getElementId());
      returnModel.getNatureRelationships()
          .add(klassInstanceRelationshipInstance);
    }
  }
  
  protected void manageContextualProperties(IKlassInstanceSaveModel klassInstancesModel, IGetConfigDetailsModel configDetails,
      IBaseEntityDAO baseEntityDAO) throws Exception
  {
    List<ITagInstance> addedTags = klassInstancesModel.getAddedTags();
    List<IModifiedContentTagInstanceModel> modifiedTags = klassInstancesModel.getModifiedTags();
    
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    IContextualDataDTO contextualDataDTO = baseEntityDAO.getBaseEntityDTO().getContextualObject();
    if (contextualDataDTO == null) {
      return;
    }
    
    Set<Long> linkedBaseEntityIIDs = new HashSet<Long>();
    Set<IBaseEntityDTO> contextualLinkedEntities = baseEntityDAO.getContextualLinkedEntities();
    contextualLinkedEntities.forEach((entityDTO) -> {
      linkedBaseEntityIIDs.add(entityDTO.getBaseEntityIID());
    });
    
    PropertyRecordBuilder propertyRecordBuilder = new PropertyRecordBuilder(baseEntityDAO, configDetails, PropertyRecordType.UPDATE, rdbmsComponentUtils.getLocaleCatlogDAO());
    Boolean shouldUpdateEntity = false;
    IInstanceTimeRange timeRange = klassInstancesModel.getTimeRange();
    shouldUpdateEntity = propertyRecordBuilder.setContextTimeRange(timeRange, contextualDataDTO);
    
    if (!addedTags.isEmpty()) {
      propertyRecordBuilder.createContextTags(addedTags, contextualDataDTO);
      shouldUpdateEntity = true;
    }
    if (!modifiedTags.isEmpty()) {
      propertyRecordBuilder.updateModifiedContextTags(modifiedTags, contextualDataDTO);
      shouldUpdateEntity = true;
    }
    
    List<Long> addedInstanceIds = klassInstancesModel.getAddedLinkedInstances()
        .stream()
        .map(x -> Long.parseLong(x.getId()))
        .collect(Collectors.toList());
    List<Long> deletedInstanceIds = klassInstancesModel.getDeletedLinkedInstances()
        .stream()
        .map(x -> Long.parseLong(x))
        .collect(Collectors.toList());
  
    if (!addedInstanceIds.isEmpty() || !deletedInstanceIds.isEmpty()) {
      
      linkedBaseEntityIIDs.addAll(addedInstanceIds);
      linkedBaseEntityIIDs.removeAll(deletedInstanceIds);
      contextualDataDTO.setLinkedBaseEntityIIDs(linkedBaseEntityIIDs.toArray(new Long[linkedBaseEntityIIDs.size()]));
      shouldUpdateEntity = true;
    }
    if (shouldUpdateEntity) {
      String contextCode = contextualDataDTO.getContext()
          .getContextCode();
      contextualDataDTO.setChanged(true);
      IReferencedVariantContextModel referencedContext = configDetails.getReferencedVariantContexts()
          .getEmbeddedVariantContexts()
          .get(contextCode);
      variantInstanceUtils.checkDuplicate(baseEntityDAO, baseEntityDTO.getParentIID(), referencedContext, true,  baseEntityDTO.getParentIID());
      baseEntityDAO.updatePropertyRecords(new PropertyRecordDTO[] {});
    }
  }
  
  private void checkGridEditPermission() throws Exception
  {
    IFunctionPermissionModel functionPermission = permissionUtils.getFunctionPermissionByUserId();
    if (!functionPermission.getCanGridEdit()) {
      throw new UserNotHaveGridEditPermission();
    }
  }
}
