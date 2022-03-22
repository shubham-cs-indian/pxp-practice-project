package com.cs.dam.runtime.assetinstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.SystemLevelIds;
import com.cs.core.asset.services.CommonConstants;
import com.cs.core.asset.services.VariantInstanceCreation;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.attribute.NumberAttribute;
import com.cs.core.config.interactor.model.asset.BulkCreateAssetInstanceResponseModel;
import com.cs.core.config.interactor.model.asset.IAssetConfigurationDetailsResponseModel;
import com.cs.core.config.interactor.model.asset.IAssetDocumentKeysModel;
import com.cs.core.config.interactor.model.asset.IAssetExtensionConfigurationModel;
import com.cs.core.config.interactor.model.asset.IAssetImageKeysModel;
import com.cs.core.config.interactor.model.asset.IAssetKeysModel;
import com.cs.core.config.interactor.model.asset.IAssetVideoKeysModel;
import com.cs.core.config.interactor.model.asset.IBulkCreateAssetInstanceResponseModel;
import com.cs.core.config.interactor.model.asset.ICreateAssetInstanceAfterUploadRequestModel;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.interactor.model.assetinstance.AssetInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.runtime.interactor.model.configuration.ContentIdentifierModel;
import com.cs.core.runtime.interactor.model.configuration.IContentIdentifierModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCreateVariantModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.variants.ICreateImageVariantsModel;
import com.cs.core.runtime.interactor.utils.klassinstance.CreateInstanceUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.MetadataUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.PropertyRecordBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.PropertyRecordBuilder.PropertyRecordType;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.VariantInstanceUtils;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.utils.dam.AssetUtils;
import com.cs.utils.dam.CreateImageVariantUtils;

/***
 * This service create the asset instance for the uploaded asset.
 * @author vannya.kalani
 *
 */
@Service
public class CreateAssetInstanceAfterUploadService
    extends AbstractRuntimeService<ICreateAssetInstanceAfterUploadRequestModel, IBulkCreateAssetInstanceResponseModel>
    implements ICreateAssetInstanceAfterUploadService {
  
  private static final Integer   ASSET_COUNTER = 0;
  private static final String    LOCALE_ID     = "localeId";
  
  @Autowired
  protected RDBMSComponentUtils  rdbmsComponentUtils;
  
  @Autowired
  protected VariantInstanceUtils variantInstanceUtils;
  
  @Override
  protected IBulkCreateAssetInstanceResponseModel executeInternal(ICreateAssetInstanceAfterUploadRequestModel model) throws Exception
  {
    IBulkCreateAssetInstanceResponseModel responseModel = new BulkCreateAssetInstanceResponseModel();
    List<String> failure = new ArrayList<>();
    List<String> tivSuccess = new ArrayList<>();
    List<String> tivWarning = new ArrayList<>();
    List<String> tivFailure = new ArrayList<>();
    Map<Long, Object> tivDuplicateDetectionInfoMap = new HashMap<>();
    
    IGetConfigDetailsForCustomTabModel configDetails = model.getConfigDetails();
    IAssetKeysModel assetKeyModel = model.getAssetKeysModel();
    String klassType = assetKeyModel.getKlassId();
    String newInstanceName = FilenameUtils.getBaseName(assetKeyModel.getFileName());
    
    try {
      String id = RDBMSAppDriverManager.getDriver().newUniqueID(BaseType.ASSET.getPrefix());
      model.setId(id);
      
      if (StringUtils.isEmpty(newInstanceName)) {
        newInstanceName = getDefaultInstanceNameByConfigDetails(configDetails, klassType);
        newInstanceName = newInstanceName + " " + ASSET_COUNTER;
      }
      model.setName(newInstanceName);
      
      ILocaleCatalogDAO localeCatlogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
      IBaseEntityDTO assetInstance = createAssetInstance(configDetails, model, id, localeCatlogDAO);
      localeCatlogDAO.loadLocaleIds(assetInstance);
      rdbmsComponentUtils.createNewRevision(assetInstance, configDetails.getNumberOfVersionsToMaintain());
      IBaseEntityDAO assetInstanceDao = localeCatlogDAO.openBaseEntity(assetInstance);
      fillEntityExtension(assetInstanceDao, model);
      
      //TIV Creation
      long baseEntityIID = assetInstance.getBaseEntityIID();
      List<ITechnicalImageVariantWithAutoCreateEnableModel> contextsWithAutoCreateEnableList = configDetails
          .getTechnicalImageVariantContextWithAutoCreateEnable();
      createVariantForAssets(String.valueOf(baseEntityIID), contextsWithAutoCreateEnableList, model, tivDuplicateDetectionInfoMap,
          tivSuccess, tivWarning, tivFailure);
      
      if (model.getIsDuplicate()) {
        responseModel.getDuplicateIIdSet().add(baseEntityIID);
      }

      localeCatlogDAO.postUsecaseUpdate(baseEntityIID, IEventDTO.EventType.ELASTIC_UPDATE);
      
      IKlassInstanceInformationModel klassInstanceInformationModel = KlassInstanceBuilder.getKlassInstanceInformationModel(assetInstance, rdbmsComponentUtils);
      responseModel.fillKlassInstanceInformationModel(klassInstanceInformationModel);
      
      //Embedded Variant Creation
      String userName = rdbmsComponentUtils.getUserName();
      VariantInstanceUtils.createEmbeddedVariantsWithAutoCreateEnabled(newInstanceName,
          assetInstance, klassInstanceInformationModel, contextsWithAutoCreateEnableList, transactionThread, userName);
      
      responseModel.getSuccess().add(id);
      responseModel.getSuccessInstanceIIds().add(baseEntityIID);
    }
    catch (Exception e) {
      failure.add(model.getId());
    }

    responseModel.setFailure(failure);
    responseModel.setTivSuccess(tivSuccess);
    responseModel.setTivWarning(tivWarning);
    responseModel.setTivFailure(tivFailure);
    responseModel.setTivDuplicateDetectionInfoMap(tivDuplicateDetectionInfoMap);
    return responseModel;
  }
  
  /***
   * Returns the label from referenced klasses for passed typeId
   * @param configDetails
   * @param typeId
   * @return
   */
  public String getDefaultInstanceNameByConfigDetails(IGetConfigDetailsForCustomTabModel configDetails, String typeId)
  {
    Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses = configDetails.getReferencedKlasses();
    IReferencedKlassDetailStrategyModel klassDetailModel = referencedKlasses.get(typeId);
    return klassDetailModel.getLabel();
  }
  
  /***
   * Creates asset instance and returns the BaseEntityDTO for same
   * @param configDetails
   * @param reqModel
   * @param baseEntityID
   * @param localeCatlogDAO
   * @return
   * @throws Exception
   */
  private IBaseEntityDTO createAssetInstance(IGetConfigDetailsForCustomTabModel configDetails,
      ICreateAssetInstanceAfterUploadRequestModel reqModel, String baseEntityID, ILocaleCatalogDAO localeCatlogDAO) throws Exception
  {
    IAssetKeysModel assetKeyModel = reqModel.getAssetKeysModel();
    Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses = configDetails.getReferencedKlasses();
    IReferencedKlassDetailStrategyModel referencedNatureKlassDetail = referencedKlasses.get(assetKeyModel.getKlassId());
    
    long classifierIID = referencedNatureKlassDetail.getClassifierIID();
    String code = referencedNatureKlassDetail.getCode();
    
    
    IClassifierDTO classifierDTO = localeCatlogDAO.newClassifierDTO(classifierIID, code, ClassifierType.CLASS);
    String endpointId = transactionThread.getTransactionData().getEndpointId();
    if(StringUtils.isEmpty(endpointId)) {
      transactionThread.getTransactionData().setEndpointId(reqModel.getEndpointId());
    }
    IBaseEntityDTO baseEntityDTO = localeCatlogDAO.newBaseEntityDTOBuilder(baseEntityID, BaseType.ASSET, classifierDTO)
        .endpointCode(transactionThread.getTransactionData().getEndpointId())
        .build();
    
    IBaseEntityDAO baseEntityDAO = localeCatlogDAO.openBaseEntity(baseEntityDTO);
    
    IPropertyRecordDTO[] propertyRecords = CreateInstanceUtils.createPropertyRecordInstance(baseEntityDAO,
        configDetails, reqModel, localeCatlogDAO);
    
    List<IPropertyRecordDTO> propertyRecordsList = Arrays.asList(propertyRecords);
    Map<String, Object> metadata = assetKeyModel.getMetadata();
    List<IPropertyRecordDTO> metaDataAttributes = MetadataUtils.addMetadataAttributesToAssetInstanceAttributes(
        metadata, baseEntityDAO, configDetails, localeCatlogDAO);
    
    IValueRecordDTO downloadCountAttributeRecord = getDownloadCountAttributeRecord(configDetails,
        baseEntityDAO, localeCatlogDAO);
    
    metaDataAttributes.add(downloadCountAttributeRecord);
    metaDataAttributes.addAll(propertyRecordsList);
    baseEntityDAO.createPropertyRecords(metaDataAttributes.toArray(new IPropertyRecordDTO[] {}));
    return baseEntityDTO;
  }
  
  /***
   * This method returns a ValueRecordDTO for assetDownloadCountAttribute Number Attribute with value 0
   * @param configDetails
   * @param baseEntityDAO
   * @param localeCatlogDAO
   * @return
   * @throws Exception
   */
  private IValueRecordDTO getDownloadCountAttributeRecord(IGetConfigDetailsForCustomTabModel configDetails,
      IBaseEntityDAO baseEntityDAO, ILocaleCatalogDAO localeCatlogDAO) throws Exception
  {
    PropertyRecordBuilder propertyRecordBuilder = new PropertyRecordBuilder(baseEntityDAO,
        configDetails, PropertyRecordType.DEFAULT, localeCatlogDAO);
    IAttribute assetDownloadCountAttributeConfig = new NumberAttribute();
    assetDownloadCountAttributeConfig.setCode(SystemLevelIds.ASSET_DOWNLOAD_COUNT_ATTRIBUTE);
    assetDownloadCountAttributeConfig.setPropertyIID(0l);
    IValueRecordDTO downloadCountAttributeRecord = propertyRecordBuilder.buildValueRecord(0l, 0l,
        "0", "", null, null, assetDownloadCountAttributeConfig, PropertyType.NUMBER);
    downloadCountAttributeRecord.setAsNumber(0);
    return downloadCountAttributeRecord;
  }
  
  /**
   * Fill up entity information from asset content
   * 
   * @param baseEntityDAO
   * @param requestModel
   * @throws RDBMSException
   * @throws CSFormatException
   */
  protected void fillEntityExtension(IBaseEntityDAO baseEntityDAO,
      ICreateAssetInstanceAfterUploadRequestModel requestModel) throws RDBMSException, CSFormatException
  {
    IAssetKeysModel assetKeyModel = requestModel.getAssetKeysModel();
    Map<String, String> properties = new HashMap<>();
    String assetObjectKey = assetKeyModel.getImageKey();
    String fileName = assetKeyModel.getFileName();
    String fileExtension = FilenameUtils.getExtension(fileName);
    String hash = assetKeyModel.getHash();
    String previewKey = null;
    
    String type = assetKeyModel.getType();
    switch (type) {
      case "Image":
        IAssetImageKeysModel imageKeysModel = (IAssetImageKeysModel) assetKeyModel;
        if (imageKeysModel.getHeight() != null) {
          properties.put("height", imageKeysModel.getHeight().toString());
        }
        if (imageKeysModel.getWidth() != null) {
          properties.put("width", imageKeysModel.getWidth().toString());
        }
        break;
        
      case "Video":
        IAssetVideoKeysModel videoKeysModel = (IAssetVideoKeysModel) assetKeyModel;
        String mp4Key = videoKeysModel.getMp4Key();
        if (mp4Key != null) {
          properties.put("mp4", mp4Key);
        }
        else {
          properties.put("mp4", assetObjectKey);
        }
        break;
        
      case "Document":
        IAssetDocumentKeysModel documentKeysModel = (IAssetDocumentKeysModel) assetKeyModel;
        previewKey = documentKeysModel.getPreviewKey();
        break;
        
      default:
        break;
    }
    
    properties.put("extension", fileExtension);
    properties.put("status", Integer.toString(0));
    String thumbKey = assetKeyModel.getThumbKey();
    
    Map<String, Object> imageAttr = new HashMap<>();
    imageAttr.put("assetObjectKey", assetObjectKey);
    imageAttr.put("fileName", fileName);
    imageAttr.put("properties", properties);
    imageAttr.put("thumbKey", thumbKey);
    imageAttr.put("type", type);
    imageAttr.put("previewImageKey", previewKey);
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    baseEntityDTO.setHashCode(hash);
    baseEntityDTO.setEntityExtension(JSONObject.toJSONString(imageAttr));
    baseEntityDAO.updatePropertyRecords(new IPropertyRecordDTO[] {});
  }
  
  /***
   * This method upload and create the auto-generated TIVs for Image type asset instances.
   * @param id baseEntityIId of main instance
   * @param contextsWithAutoCreateEnableList list of ITechnicalImageVariantWithAutoCreateEnableModel
   * @param model main assets upload information
   * @param tivDuplicateDetectionInfoMap
   * @param tivSuccess
   * @param tivWarning
   * @param tivFailure
   */
  private void createVariantForAssets(String id, List<ITechnicalImageVariantWithAutoCreateEnableModel> contextsWithAutoCreateEnableList,
      ICreateAssetInstanceAfterUploadRequestModel model, Map<Long, Object> tivDuplicateDetectionInfoMap, List<String> tivSuccess,
      List<String> tivWarning, List<String> tivFailure)
  {
    String path = null;
    IAssetKeysModel imageInfo = model.getAssetKeysModel();
    Map<String, IGetConfigDetailsForCreateVariantModel> configDetailsMap = model.getTivConfigDetails();
    IAssetConfigurationDetailsResponseModel assetConfigDetails = model.getAssetConfigDetails();
    String imageFileName = imageInfo.getFileName();
    String fileExtension = FilenameUtils.getExtension(imageFileName);
    TransactionData transactionThreadData = transactionThread.getTransactionData();
    
    IContentIdentifierModel requestTransactionData = new ContentIdentifierModel(transactionThreadData.getOrganizationId(),
        transactionThreadData.getPhysicalCatalogId(), transactionThreadData.getPortalId(), transactionThreadData.getLogicalCatalogId(),
        transactionThreadData.getSystemId(), transactionThreadData.getEndpointId());
    
    List<IAssetExtensionConfigurationModel> imageConfiguredExtensions = assetConfigDetails.getExtensionConfiguration().get("imageAsset");
    if (contextsWithAutoCreateEnableList.isEmpty()) return;
    if (!"Image".equals(imageInfo.getType())) return;
    IAssetExtensionConfigurationModel extensionConfiguration = AssetUtils.getExtensionConfigurationFromList(
        imageConfiguredExtensions, fileExtension);
    if (extensionConfiguration != null && !extensionConfiguration.getExtractRendition()) {
      tivWarning.add("The renditions could not be processed for the uploaded asset " + imageFileName);
      return;
    }
    
    IAssetImageKeysModel assetImageKeysModel = (IAssetImageKeysModel) imageInfo;
    String thumbnailPath = assetImageKeysModel.getThumbnailPath();
    try {
      for (ITechnicalImageVariantWithAutoCreateEnableModel contextsWithAutoCreateEnable : contextsWithAutoCreateEnableList) {
        IGetConfigDetailsForCreateVariantModel configDetailsForCreateVariantModel = configDetailsMap.get(contextsWithAutoCreateEnable.getId());
        int numberOfVersionsToMaintain = configDetailsForCreateVariantModel.getNumberOfVersionsToMaintain();
        if (CommonConstants.IMAGE_VARIANT.equals(contextsWithAutoCreateEnable.getType())) {
          String fileName = FilenameUtils.getBaseName(imageFileName);
          try {
            path = assetImageKeysModel.getFilePath();
            IAssetInformationModel assetInformation = new AssetInformationModel();
            assetInformation.setThumbKey(assetImageKeysModel.getThumbKey());
            assetInformation.setAssetObjectKey(assetImageKeysModel.getImageKey());
            assetInformation.setType(assetImageKeysModel.getType());
            assetInformation.setFileName(assetImageKeysModel.getFileName());
            
            List<ICreateImageVariantsModel> createVariantModelList = CreateImageVariantUtils.createVariantInstances(
                contextsWithAutoCreateEnable, assetInformation, path, id, transactionThreadData.getParentTransactionId(), fileName,
                configDetailsMap.get(contextsWithAutoCreateEnable.getId()), requestTransactionData, id, assetConfigDetails, tivFailure, tivWarning,
                thumbnailPath);
            
            for (ICreateImageVariantsModel createVariantModel : createVariantModelList) {
              IBaseEntityDTO variant = createVariantInstance(tivDuplicateDetectionInfoMap, tivFailure, tivSuccess, createVariantModel);
            }
          }
          catch (Exception e) {
            tivFailure.add("TIV generation failed for " + imageFileName);
          }
          finally {
            AssetUtils.deleteFileAndDirectory(path);
          }
        }
      } 
    }
    finally {
      if (thumbnailPath != null) {
        AssetUtils.deleteFileAndDirectory(thumbnailPath);
      }
    }
  }

  /***
   * This method creates variant entity and add the hash and name of entity in passed tivDuplicateDetectionInfoMap.
   * Also adds name of entities in respective success and failure objects.
   * @param tivDuplicateDetectionInfoMap
   * @param tivFailure
   * @param tivSuccess
   * @param createVariantModel
   * @return 
   */
  private IBaseEntityDTO createVariantInstance(Map<Long, Object> tivDuplicateDetectionInfoMap,
      List<String> tivFailure, List<String> tivSuccess, ICreateImageVariantsModel createVariantModel)
  {
    Map<String, Object> transactionData = new HashMap<>();
    TransactionData transactionThreadData = transactionThread.getTransactionData();
    transactionData.put(ITransactionData.ENDPOINT_ID, transactionThreadData.getEndpointId());
    transactionData.put(ITransactionData.PHYSICAL_CATALOG_ID, transactionThreadData.getPhysicalCatalogId());
    transactionData.put(ITransactionData.ORGANIZATION_ID, transactionThreadData.getOrganizationId());
    transactionData.put(ITransactionData.USER_ID, transactionThreadData.getUserId());
    transactionData.put(LOCALE_ID, transactionThreadData.getDataLanguage());
    
    try {
      long baseEntityIId = VariantInstanceCreation.getInstance().createVariantInstance(createVariantModel, transactionData);
      
      Map<String, String> tivInfo = new HashMap<>();
      tivInfo.put("hash", createVariantModel.getAssetInformation().getHash());
      tivInfo.put("name", createVariantModel.getName());
      tivDuplicateDetectionInfoMap.put(baseEntityIId, tivInfo);
      tivSuccess.add("TIV generated successfully for " + createVariantModel.getName());
      return rdbmsComponentUtils.getBaseEntityDTO(baseEntityIId);
    }
    catch (Exception ex) {
      tivFailure.add("TIV generation failed for " + createVariantModel.getName());
    }
    return null;
  }
  
  @Override
  public WorkflowUtils.UseCases getUsecase()
  {
    return WorkflowUtils.UseCases.CREATEASSET;
  }
}
