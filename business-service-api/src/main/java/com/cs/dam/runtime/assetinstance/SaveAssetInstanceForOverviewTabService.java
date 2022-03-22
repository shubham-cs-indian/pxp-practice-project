package com.cs.dam.runtime.assetinstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.dto.AutoCreateVariantDTO;
import com.cs.core.bgprocess.idto.IAutoCreateVariantDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.attribute.NumberAttribute;
import com.cs.core.config.interactor.exception.asset.AssetKlassNotFoundException;
import com.cs.core.config.interactor.model.asset.IAssetConfigurationDetailsResponseModel;
import com.cs.core.config.interactor.model.asset.IAssetExtensionConfigurationModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableWrapperModel;
import com.cs.core.config.interactor.model.variantcontext.TechnicalImageVariantWithAutoCreateEnableWrapperModel;
import com.cs.core.data.Text;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.entity.dao.BaseEntityDAS;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceSchedule;
import com.cs.core.runtime.interactor.entity.klassinstance.IAssetInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.AttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.exception.assetserver.RenditionNotProcessedException;
import com.cs.core.runtime.interactor.model.assetinstance.AssetInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.AssetInstanceSaveModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInstanceSaveModel;
import com.cs.core.runtime.interactor.model.attribute.IModifiedContentAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.attribute.ModifiedAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceSaveModel;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.variants.DeleteVariantModel;
import com.cs.core.runtime.interactor.model.variants.IDeleteVariantModel;
import com.cs.core.runtime.interactor.utils.klassinstance.MetadataUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.PropertyRecordBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.klassinstance.AbstractSaveInstanceForTabs;
import com.cs.core.runtime.strategy.usecase.asset.IFetchAssetConfigurationDetails;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;
import com.cs.core.runtime.utils.threadPoolExecutor.ThreadPoolExecutorUtil;
import com.cs.core.runtime.variant.assetinstance.IDeleteAssetInstanceVariantsService;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.dam.downloadtracker.IResetDownloadCountService;
import com.cs.dam.runtime.assetinstance.linksharing.IDeleteAssetWithoutTIVsFromSharedSwiftServerService;
import com.cs.dam.runtime.assetinstance.linksharing.IDeleteTIVAndMainAssetFromSharedSwiftServerService;
import com.cs.dam.runtime.strategy.usecase.assetinstance.IGetConfigDetailsForAssetInstanceOverviewTabStrategy;
import com.cs.dam.runtime.util.AssetInstanceUtils;
import com.cs.utils.ExceptionUtil;
import com.cs.utils.dam.AssetUtils;

@Service
public class SaveAssetInstanceForOverviewTabService extends AbstractSaveInstanceForTabs<IAssetInstanceSaveModel, IGetKlassInstanceModel>
    implements ISaveAssetInstanceForOverviewTabService {
  
  @Autowired
  protected IGetConfigDetailsForAssetInstanceOverviewTabStrategy getConfigDetailsForAssetInstanceOverviewTabStrategy;
  
  @Autowired
  protected ThreadPoolExecutorUtil                               threadPoolTaskExecutorUtil;
  
  @Autowired
  protected IFetchAssetConfigurationDetails                      fetchAssetConfigurationDetails;
  
  @Autowired
  protected IDeleteTIVAndMainAssetFromSharedSwiftServerService   deleteTIVAndMainAssetFromSharedSwiftServerService;
  
  @Autowired
  protected IDeleteAssetWithoutTIVsFromSharedSwiftServerService  deleteAssetWithoutTIVsFromSharedSwiftServer;
  
  @Autowired
  protected IResetDownloadCountService                           resetDownloadCount;
  
  @Autowired
  protected IDeleteAssetInstanceVariantsService                  deleteAssetInstanceVariantsService;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(IAssetInstanceSaveModel klassInstancesModel) throws Exception
  {
    IGetKlassInstanceModel response = null;
    try {
      response = super.executeInternal(klassInstancesModel);
      IAssetInformationModel assetInfo = klassInstancesModel.getAssetInformation();
      if (klassInstancesModel.getIsFileUploaded()) {
        String parentId = response.getKlassInstance().getId();
        prepareDataAndCreateImageVariants(response.getConfigDetails(), assetInfo, klassInstancesModel, parentId, response);
      }
    }
    catch (KlassNotFoundException e) {
      throw new AssetKlassNotFoundException(e);
    }
    return response;
  }
  
  @Override
  public IGetConfigDetailsForCustomTabModel getConfigDetails(IMulticlassificationRequestModel model, IKlassInstanceSaveModel saveModel)
      throws Exception
  {
    switch (saveModel.getTabType()) {
      case CommonConstants.CUSTOM_TEMPLATE_TAB_BASETYPE:
        return getConfigDetailsForAssetInstanceOverviewTabStrategy.execute(model);
      default:
        return null;
    }
  }
  
  @Override
  public WorkflowUtils.UseCases getUsecase()
  {
    return WorkflowUtils.UseCases.SAVEASSET;
  }
  
  @Override
  protected IGetKlassInstanceModel executeGetKlassInstance(IGetConfigDetailsModel configDetails, IKlassInstanceSaveModel saveInstanceModel,
      IBaseEntityDAO baseEntityDAO) throws Exception
  {
    IGetKlassInstanceModel executeGetKlassInstance = super.executeGetKlassInstance(configDetails, saveInstanceModel, baseEntityDAO);
    
    AssetInstanceUtils.fillAssetInformation(executeGetKlassInstance, baseEntityDAO);
    return executeGetKlassInstance;
  }
  
  public void prepareDataAndCreateImageVariants(IGetConfigDetailsModel configDetails, IAssetInformationModel coverFlowAttribute,
      IAssetInstanceSaveModel klassInstancesModel, String parentId, IGetKlassInstanceModel returnModel) throws Exception
  {
    String instanceId = klassInstancesModel.getId();
    List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantContextsWithAutoCreateEnable = configDetails
        .getTechnicalImageVariantContextWithAutoCreateEnable();
    if (technicalImageVariantContextsWithAutoCreateEnable.isEmpty()) {
      return;
    }
    
    if (!coverFlowAttribute.getType().equals("Image")) {
      return;
    }
    IAssetConfigurationDetailsResponseModel assetModel = fetchAssetConfigurationDetails.execute(new IdParameterModel());
    String fileName = coverFlowAttribute.getFileName();
    String instanceName = FilenameUtils.getBaseName(fileName);
    String extension = FilenameUtils.getExtension(fileName);
    IAssetExtensionConfigurationModel extensionConfiguration = AssetUtils
        .getExtensionConfigurationFromList(assetModel.getExtensionConfiguration().get(CommonConstants.MAM_NATURE_TYPE_IMAGE), extension);
    if (extensionConfiguration != null && !extensionConfiguration.getExtractRendition()) {
      ExceptionUtil.addFailureDetailsToFailureObject(returnModel.getWarnings(), new RenditionNotProcessedException(), instanceId, fileName);
      return;
    }
    
    ITechnicalImageVariantWithAutoCreateEnableWrapperModel contextsWithAutoCreateEnableWrapperModel = new TechnicalImageVariantWithAutoCreateEnableWrapperModel();
    contextsWithAutoCreateEnableWrapperModel
        .setTechnicalImageVariantWithAutoCreateEnable(technicalImageVariantContextsWithAutoCreateEnable);
    contextsWithAutoCreateEnableWrapperModel.setFileName(instanceName);
    contextsWithAutoCreateEnableWrapperModel.setIsSave(true);
    contextsWithAutoCreateEnableWrapperModel.setAttribute(coverFlowAttribute);
    contextsWithAutoCreateEnableWrapperModel.setInstanceId(instanceId);
    contextsWithAutoCreateEnableWrapperModel.setParentId(parentId);
    contextsWithAutoCreateEnableWrapperModel.setAssetConfigurationModel(assetModel);
    contextsWithAutoCreateEnableWrapperModel.setThumbnailPath(klassInstancesModel.getThumbnailPath());
    contextsWithAutoCreateEnableWrapperModel.setMainAssetInstanceSourcePath(klassInstancesModel.getFilePath());
    submitBGPAutoCreateTIVProcess(contextsWithAutoCreateEnableWrapperModel, klassInstancesModel.getId());
  }
  
  @Override
  protected void saveBaseEntity(IKlassInstanceSaveModel klassInstancesModel, IBaseEntityDAO baseEntityDAO,
      IGetConfigDetailsForCustomTabModel configDetails) throws Exception
  {
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    IIdParameterModel dataModel = new IdParameterModel();
    dataModel.setId(Long.toString(baseEntityDTO.getBaseEntityIID()));
    if (configDetails.getTechnicalImageVariantContextWithAutoCreateEnable().isEmpty())
      deleteAssetWithoutTIVsFromSharedSwiftServer.execute(dataModel);
    else {
      IIdsListParameterModel idsListParameterModel = new IdsListParameterModel();
      idsListParameterModel.getIds().add(dataModel.getId());
      deleteTIVAndMainAssetFromSharedSwiftServerService.execute(idsListParameterModel);
    }
    IAssetInstanceSaveModel assetKlassInstancesModel = (IAssetInstanceSaveModel) klassInstancesModel;
    if (assetKlassInstancesModel.getIsResetDownloadCount()) {
      resetDownloadCount.execute(dataModel);
    }
    IEventInstanceSchedule modifiedEventSchedule = assetKlassInstancesModel.getModifiedEventSchedule();
    
    if (modifiedEventSchedule != null) {
      ConfigurationDAO configDAO = ConfigurationDAO.instance();
      IPropertyDTO startDatePropertyDTO = configDAO.getPropertyByCode(SystemLevelIds.START_DATE_ATTRIBUTE);
      IPropertyDTO endDatePropertyDTO = configDAO.getPropertyByCode(SystemLevelIds.END_DATE_ATTRIBUTE);
      
      List<IPropertyDTO> propertyDTO = new ArrayList<IPropertyDTO>();
      propertyDTO.add(startDatePropertyDTO);
      propertyDTO.add(endDatePropertyDTO);
      
      IBaseEntityDTO loadPropertyRecords = baseEntityDAO.loadPropertyRecords(propertyDTO.toArray(new IPropertyDTO[0]));
      
      Set<IPropertyRecordDTO> propertyRecords = loadPropertyRecords.getPropertyRecords();
      
      IAttributeInstance startDate = new AttributeInstance();
      startDate.setAttributeId(SystemLevelIds.START_DATE_ATTRIBUTE);
      startDate.setBaseType(Constants.ATTRIBUTE_INSTANCE_PROPERTY_TYPE);
      startDate.setValue(Long.toString(modifiedEventSchedule.getStartTime()));
      startDate.setValueAsNumber((modifiedEventSchedule.getStartTime()).doubleValue());
      startDate.setIid(startDatePropertyDTO.getPropertyIID());
      
      IAttributeInstance endDate = new AttributeInstance();
      endDate.setAttributeId(SystemLevelIds.END_DATE_ATTRIBUTE);
      endDate.setBaseType(Constants.ATTRIBUTE_INSTANCE_PROPERTY_TYPE);
      endDate.setValue(Long.toString(modifiedEventSchedule.getEndTime()));
      endDate.setValueAsNumber((modifiedEventSchedule.getEndTime()).doubleValue());
      endDate.setIid(endDatePropertyDTO.getPropertyIID());
      
      if (modifiedEventSchedule != null && propertyRecords.isEmpty()) {
        klassInstancesModel.getAddedAttributes().add(startDate);
        klassInstancesModel.getAddedAttributes().add(endDate);
      }
      else {
        ModifiedAttributeInstanceModel modifiedStartDate = new ModifiedAttributeInstanceModel(startDate);
        ModifiedAttributeInstanceModel modifiedEndDate = new ModifiedAttributeInstanceModel(endDate);
        klassInstancesModel.getModifiedAttributes().add(modifiedStartDate);
        klassInstancesModel.getModifiedAttributes().add(modifiedEndDate);
      }
    }
    super.saveBaseEntity(klassInstancesModel, baseEntityDAO, configDetails);
    if (!klassInstancesModel.getModifiedAttributes().isEmpty()) {
      updateExpiredStatusOfAsset(baseEntityDAO, klassInstancesModel.getModifiedAttributes(), klassInstancesModel.getEntity().getId());
    }
    IAssetInformationModel assetInformation = ((IAssetInstance) klassInstancesModel.getEntity()).getAssetInformation();
    if (assetInformation != null) {
      String previousHash = baseEntityDAO.getBaseEntityDTO().getHashCode();
      fillEntityExtension(baseEntityDAO, assetInformation);
      baseEntityDAO.updatePropertyRecords(new IPropertyRecordDTO[] {});
      
      Map<String, Object> transactionDataMap = new HashMap<>();
      TransactionData transactionData = controllerThread.getTransactionData();
      transactionDataMap.put(ITransactionData.ENDPOINT_ID, transactionData.getEndpointId());
      transactionDataMap.put(ITransactionData.ORGANIZATION_ID, transactionData.getOrganizationId());
      transactionDataMap.put(ITransactionData.PHYSICAL_CATALOG_ID, transactionData.getPhysicalCatalogId());
      if (!previousHash.equals(baseEntityDAO.getBaseEntityDTO().getHashCode())) {
        AssetUtils.updateDuplicateStatusForReplacedAsset(baseEntityDAO, previousHash, 
            transactionDataMap, 0, rdbmsComponentUtils.getLocaleCatlogDAO());
      }
    }
  }
  
  private void updateExpiredStatusOfAsset(IBaseEntityDAO baseEntityDAO, List<IModifiedContentAttributeInstanceModel> modifiedAttributes,
      String assetInstanceIID) throws NumberFormatException, RDBMSException
  {
    for (IContentAttributeInstance addedAttribute : modifiedAttributes) {
      if (addedAttribute.getAttributeId().equals(SystemLevelIds.END_DATE_ATTRIBUTE)) {
        baseEntityDAO.updateExpiryStatusForAssetByIID(Long.parseLong(assetInstanceIID), false);
      }
    }
  }
  
  @Override
  protected List<IPropertyRecordDTO> manageAddedAttributes(IKlassInstanceSaveModel klassInstancesModel,
      IGetConfigDetailsModel configDetails, PropertyRecordBuilder propertyRecordBuilder, IBaseEntityDAO baseEntityDAO) throws Exception
  {
    IAssetInformationModel assetInformation = ((IAssetInstance) klassInstancesModel.getEntity()).getAssetInformation();
    // assetObjectKey null check confirms that image is uploaded
    if (assetInformation != null && StringUtils.isNotEmpty(assetInformation.getAssetObjectKey())) {
      List<IPropertyRecordDTO> propertyRecords = new ArrayList<>();
      IAssetInstanceSaveModel assteInstanceSave = (IAssetInstanceSaveModel) klassInstancesModel;
      Map<String, Object> metadata = assteInstanceSave.getMetadata();
      ILocaleCatalogDAO localeCatlogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
      if (metadata != null && !metadata.isEmpty()) {
        List<IPropertyRecordDTO> metaDataAttributes = MetadataUtils.addMetadataAttributesToAssetInstanceAttributes(metadata, baseEntityDAO,
            (IGetConfigDetailsForCustomTabModel) configDetails, localeCatlogDAO);
        propertyRecords.addAll(metaDataAttributes);
      }
      
      IValueRecordDTO downloadCountAttributeRecord = null;
      IAttribute assetDownloadCountAttributeConfig = new NumberAttribute();
      assetDownloadCountAttributeConfig.setCode(SystemLevelIds.ASSET_DOWNLOAD_COUNT_ATTRIBUTE);
      assetDownloadCountAttributeConfig.setPropertyIID(0l);
      
      ConfigurationDAO configDAO = ConfigurationDAO.instance();
      List<IPropertyDTO> propertyDTO = new ArrayList<>();
      propertyDTO.add(configDAO.getPropertyByCode(SystemLevelIds.ASSET_DOWNLOAD_COUNT_ATTRIBUTE));
      IBaseEntityDTO loadPropertyRecords = baseEntityDAO.loadPropertyRecords(propertyDTO.toArray(new IPropertyDTO[0]));
      Set<IPropertyRecordDTO> propertyRecordDTOs = loadPropertyRecords.getPropertyRecords();
      
      if (!propertyRecordDTOs.isEmpty()) {
        IPropertyRecordDTO propertyRecordDTO = propertyRecordDTOs.iterator().next();
        IPropertyDTO assetDownloadCountProperty = propertyRecordDTO.getProperty();
        IValueRecordDTO assetDownloadCountValueRecord = (IValueRecordDTO) propertyRecordDTO;
        downloadCountAttributeRecord = propertyRecordBuilder.buildValueRecord(assetDownloadCountProperty.getPropertyIID(),
            assetDownloadCountValueRecord.getValueIID(), "0", "", null, null, assetDownloadCountAttributeConfig, PropertyType.NUMBER);
      }
      else {
        downloadCountAttributeRecord = propertyRecordBuilder.buildValueRecord(0l, 0l, "0", "", null, null,
            assetDownloadCountAttributeConfig, PropertyType.NUMBER);
      }
      downloadCountAttributeRecord.setAsNumber(0);
      propertyRecords.add(downloadCountAttributeRecord);
      
      baseEntityDAO.createPropertyRecords(propertyRecords.toArray(new IPropertyRecordDTO[] {}));
      
      // Update Duplicate Status
      long duplicateId = ((IAssetInstanceSaveModel) klassInstancesModel).getDuplicateId();
      String hashCode = baseEntityDAO.getBaseEntityDTO().getHashCode();
      if (hashCode != null && !hashCode.equals(assetInformation.getHash())) {
        AssetUtils.updateDuplicateStatus(duplicateId, baseEntityDAO, null);
      }
    }
    
    if (klassInstancesModel.getAddedAttributes().isEmpty())
      return new ArrayList<IPropertyRecordDTO>();
    
    List<IContentAttributeInstance> addedAttributes = klassInstancesModel.getAddedAttributes();
    List<IPropertyRecordDTO> valueRecords = new ArrayList<>();
    
    for (IContentAttributeInstance addedAttribute : addedAttributes) {
      if (addedAttribute.getAttributeId().equals(SystemLevelIds.START_DATE_ATTRIBUTE)
          || addedAttribute.getAttributeId().equals(SystemLevelIds.END_DATE_ATTRIBUTE)) {
        
        String propertyCode = addedAttribute.getAttributeId().equals(SystemLevelIds.START_DATE_ATTRIBUTE)
            ? SystemLevelIds.START_DATE_ATTRIBUTE
            : SystemLevelIds.END_DATE_ATTRIBUTE;
        
        IAttributeInstance instance = (IAttributeInstance) addedAttribute;
        IValueRecordDTO valueRecord = baseEntityDAO
            .newValueRecordDTOBuilder(RDBMSUtils.getPropertyByCode(propertyCode), instance.getValue()).build();
        valueRecord.setAsNumber(Double.parseDouble(instance.getValue()));
        valueRecords.add(valueRecord);
      }
      else {
        valueRecords.add(propertyRecordBuilder.updateValueRecord(addedAttribute));
      }
    }
    
    return valueRecords;
  }
  
  @Override
  protected List<IPropertyRecordDTO> manageModifiedAttributes(IKlassInstanceSaveModel klassInstancesModel,
      IGetConfigDetailsModel configDetails, PropertyRecordBuilder propertyRecordBuilder, IBaseEntityDAO baseEntityDAO) throws Exception
  {
    if (klassInstancesModel.getModifiedAttributes().isEmpty())
      return new ArrayList<IPropertyRecordDTO>();
    
    List<IModifiedContentAttributeInstanceModel> modifiedAttributes = klassInstancesModel.getModifiedAttributes();
    
    List<IPropertyRecordDTO> propertyRecordDTO = new ArrayList<>();
    
    ConfigurationDAO configDAO = ConfigurationDAO.instance();
    List<IPropertyDTO> propertyDTO = new ArrayList<IPropertyDTO>();
    propertyDTO.add(configDAO.getPropertyByCode(SystemLevelIds.START_DATE_ATTRIBUTE));
    propertyDTO.add(configDAO.getPropertyByCode(SystemLevelIds.END_DATE_ATTRIBUTE));
    IBaseEntityDTO loadPropertyRecords = baseEntityDAO.loadPropertyRecords(propertyDTO.toArray(new IPropertyDTO[0]));
    Set<IPropertyRecordDTO> propertyRecords = loadPropertyRecords.getPropertyRecords();
    
    int startValueIID = 0;
    int endValueIID = 0;
    for (IPropertyRecordDTO iPropertyRecordDTO : propertyRecords) {
      IPropertyDTO property = iPropertyRecordDTO.getProperty();
      IValueRecordDTO value = (IValueRecordDTO) iPropertyRecordDTO;
      if ((property.getCode()).equals(SystemLevelIds.START_DATE_ATTRIBUTE)) {
        startValueIID = (int) value.getValueIID();
      }
      else {
        endValueIID = (int) value.getValueIID();
      }
    }
    
    for (IModifiedContentAttributeInstanceModel modifiedAttribute : modifiedAttributes) {
      if (modifiedAttribute.getAttributeId().equals(SystemLevelIds.START_DATE_ATTRIBUTE)
          || modifiedAttribute.getAttributeId().equals(SystemLevelIds.END_DATE_ATTRIBUTE)) {
        IAttributeInstance instance = (IAttributeInstance) modifiedAttribute;
        IValueRecordDTO valueRecord = null;
        if (modifiedAttribute.getAttributeId().equals(SystemLevelIds.START_DATE_ATTRIBUTE)) {
          valueRecord = baseEntityDAO
              .newValueRecordDTOBuilder(RDBMSUtils.getPropertyByCode(SystemLevelIds.START_DATE_ATTRIBUTE), instance.getValue())
              .valueIID(startValueIID).build();
        }
        else {
          valueRecord = baseEntityDAO
              .newValueRecordDTOBuilder(RDBMSUtils.getPropertyByCode(SystemLevelIds.END_DATE_ATTRIBUTE), instance.getValue())
              .valueIID(endValueIID).build();
        }
        valueRecord.setAsNumber(Double.parseDouble(instance.getValue()));
        baseEntityDAO.updatePropertyRecords(valueRecord);
      }
      IPropertyRecordDTO updateValueRecord = propertyRecordBuilder.updateValueRecord(modifiedAttribute);
      if (updateValueRecord != null) {
        propertyRecordDTO.add(updateValueRecord);
      }
    }
    return propertyRecordDTO;
  }
  
  @Override
  protected void manageContextualProperties(IKlassInstanceSaveModel klassInstancesModel, IGetConfigDetailsModel configDetails,
      IBaseEntityDAO baseEntityDAO) throws Exception
  {
    if (((AssetInstanceSaveModel) klassInstancesModel).getIsFileUploaded()) {
      long assetInstanceId = Long.parseLong(klassInstancesModel.getId());
      List<String> contextualCodes = new ArrayList<>();
      List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantContextsWithAutoCreateEnable = configDetails
          .getTechnicalImageVariantContextWithAutoCreateEnable();
      technicalImageVariantContextsWithAutoCreateEnable.forEach(context -> {
        if(context.getType().equalsIgnoreCase(CommonConstants.IMAGE_VARIANT)) {
          contextualCodes.add(context.getId());
        }        
      });
      
      boolean canDeleteAssetFromSwiftServer = deleteMainAssetFromSwiftServer(configDetails, baseEntityDAO);
      if (!contextualCodes.isEmpty()) {
        List<Long> contextualObjectIIds = baseEntityDAO.getContextualObjectIIdsForVariantsToDelete(assetInstanceId, contextualCodes);
        if (!contextualObjectIIds.isEmpty()) {
          if (canDeleteAssetFromSwiftServer) {
            baseEntityDAO.prepareAssetPurgeDTOListForVariants(assetInstanceId, Text.join(",", contextualObjectIIds));
          }
          List<String> tivToDelete = new ArrayList<String>();
          for (Long tiv : contextualObjectIIds) {
            tivToDelete.add(tiv.toString());
          }
          IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
          Long baseEntitytIID = baseEntityDTO.getBaseEntityIID();
          IDeleteVariantModel deleteModel = new DeleteVariantModel();
          deleteModel.setContentId(baseEntitytIID.toString());
          deleteModel.setIds(tivToDelete);
          deleteModel.setShouldCreateRevision(false);
          deleteAssetInstanceVariantsService.execute(deleteModel);
        }
      }
    }
    super.manageContextualProperties(klassInstancesModel, configDetails, baseEntityDAO);
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
  protected void submitBGPAutoCreateTIVProcess( ITechnicalImageVariantWithAutoCreateEnableWrapperModel wrappedModel, String instancesId)
      throws NumberFormatException, Exception
  {
    IBaseEntityDTO baseEntityDTO = rdbmsComponentUtils.getBaseEntityDTO(Long.parseLong(instancesId));
    AutoCreateVariantDTO autoCreateVariantDTO = new AutoCreateVariantDTO();
    autoCreateVariantDTO.setBaseEntity(baseEntityDTO);
    autoCreateVariantDTO.setConfigData(ObjectMapperUtil.convertValue(wrappedModel, JSONObject.class));
    autoCreateVariantDTO.setTransaction(controllerThread.getTransactionData());
    BGPDriverDAO.instance().submitBGPProcess(rdbmsComponentUtils.getUserName(), IAutoCreateVariantDTO.AUTO_CREATE_TIV_SERVICE, "",
        IBGProcessDTO.BGPPriority.HIGH, autoCreateVariantDTO.toJSONContent());
  }
  
  /**
   * @param configDetails
   * @param entityExtension
   * @return
   * @throws Exception
   */
  private boolean checkAllConditionsToDeleteAssetsFromSwiftServer(
      IGetConfigDetailsModel configDetails, IJSONContent entityExtension)
      throws Exception
  {
    String entityExtensionAsString = entityExtension.toString();
    IAssetInformationModel assetInformationModel = (IAssetInformationModel) ObjectMapperUtil.readValue(entityExtensionAsString,
        AssetInformationModel.class);
    Boolean isUploadedFirstTime = assetInformationModel.getAssetObjectKey() != null ? false : true;
    if(isUploadedFirstTime) {
      return false;
    }
    
    return !isUploadedFirstTime;
  }
  
  /**
   * @param configDetails
   * @param baseEntityDAO
   * @return
   * @throws Exception
   */
  private boolean deleteMainAssetFromSwiftServer(IGetConfigDetailsModel configDetails,
      IBaseEntityDAO baseEntityDAO) throws Exception
  {
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    IJSONContent entityExtension = baseEntityDTO.getEntityExtension();
    boolean canDeleteAssetFromSwiftServer = checkAllConditionsToDeleteAssetsFromSwiftServer(configDetails, entityExtension);
    if (canDeleteAssetFromSwiftServer) {
      moveAssetToPurge(baseEntityDTO);
    }
    return canDeleteAssetFromSwiftServer;
  }
  
  /**
   * Insert entry into assettobepurged table.
   * @param baseEntityDTO
   * @throws RDBMSException
   */
  public void moveAssetToPurge(IBaseEntityDTO baseEntityDTO) throws RDBMSException
  {
    long baseEntityIID = baseEntityDTO.getBaseEntityIID();
    IJSONContent entityExtension = baseEntityDTO.getEntityExtension();
    String container = entityExtension.getInitField("type", "");
    String assetObjectKey = entityExtension.getInitField("assetObjectKey", "");
    String thumbKey = entityExtension.getInitField("thumbKey", "");
    String previewImageKey = entityExtension.getInitField("previewImageKey", "");
    
    if (!container.isEmpty() && !assetObjectKey.isEmpty()) {
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
        BaseEntityDAS entityDAS = new BaseEntityDAS(currentConn);
        entityDAS.moveAssetToPurge(baseEntityIID, container, assetObjectKey, thumbKey, previewImageKey);
      });
    }
  }
  
}
