package com.cs.core.bgprocess.services.asset;

import com.cs.constants.DAMConstants;
import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.dto.BGProcessDTO;
import com.cs.core.bgprocess.dto.BaseEntityPlanDTO;
import com.cs.core.bgprocess.dto.BulkAssetUploadDTO;
import com.cs.core.bgprocess.dto.BulkRelationshipCreateDTO;
import com.cs.core.bgprocess.idto.IAssetFileDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.idto.IBulkAssetUploadDTO;
import com.cs.core.bgprocess.services.data.AbstractBaseEntityProcessing;
import com.cs.core.config.interactor.model.asset.*;
import com.cs.core.data.Text;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.rdbms.config.dto.CatalogDTO;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.entity.dao.BaseEntityDAO;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.rdbms.tracking.dto.UserSessionDTO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.exception.assetserver.AssetFileTypeNotSupportedException;
import com.cs.core.runtime.interactor.exception.assetserver.ExtractedFileNotProcessedException;
import com.cs.core.runtime.interactor.exception.assetserver.ExtractedFileTypeNotSupportedException;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.pluginexception.IDevExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.dam.asset.exceptions.ExifToolException;
import com.cs.dam.runtime.assetinstance.IBulkCreateAssetInstanceService;
import com.cs.utils.dam.AssetUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONObject;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@SuppressWarnings("unchecked")
public class BulkAssetUpload extends AbstractBaseEntityProcessing {
  
  private final HashMap<Long,IAssetFileDTO> sourceAssetsMap = new HashMap<>(); // The map entity IID to assets for upload
  private IBulkAssetUploadDTO assetUploadDTO = new BulkAssetUploadDTO();
  private LocaleCatalogDAO    localeCatalogDao;

  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    // Create fake base entity iids in order to benefit from AbstractBaseEntityProcessing 
    BGProcessDTO preJobData = (BGProcessDTO) initialProcessData;
    assetUploadDTO.fromJSON(preJobData.getEntryData().toString());
    IntStream.range(0, assetUploadDTO.getAssets().size()).forEach(idx -> {
      sourceAssetsMap.put(Long.valueOf(idx + 1), assetUploadDTO.getAssets().get(idx));
    });
    
    // inject the fetched base entity iids into the entry data before calling the parent initialization
    preJobData.getEntryData().setLongArrayField(BaseEntityPlanDTO.BASE_ENTITY_IIDS, sourceAssetsMap.keySet());
    
    localeCatalogDao = new LocaleCatalogDAO(userSession, new LocaleCatalogDTO(assetUploadDTO.getLocaleID(),
        new CatalogDTO(assetUploadDTO.getCatalogCode(), assetUploadDTO.getOrganizationCode())));
    
    super.initBeforeStart(initialProcessData, userSession);
  }
  
  /**
   * Upload the asset file into the DAM repository
   * @param asset
   * @param sourcePathVsHash
   * @param storagePath
   * @param authTokenUrl
   * @return asset upload information
   * @throws Exception 
   */ 
  private Map<String, Object> uploadAsset(IAssetFileDTO asset) throws Exception
  {
    String fileSourcePath = asset.getFilePath();
    Map<String, Object> fileModelMap = new HashMap<>();
    fileModelMap.put(DAMConstants.FILE_MODEL_PATH, fileSourcePath);
    fileModelMap.put(DAMConstants.FILE_MODEL_IS_EXTRACTED, asset.getIsExtracted());
    fileModelMap.put(DAMConstants.FILE_MODEL_EXT_TYPE, asset.getExtensionType());
    fileModelMap.put(DAMConstants.FILE_MODEL_KLASS_ID, asset.getKlassID());
    fileModelMap.put(DAMConstants.FILE_MODEL_EXT_CONFIG, asset.getExtensionConfiguration());
    fileModelMap.put(DAMConstants.FILE_MODEL_PHYSICAL_CATALOG_ID, assetUploadDTO.getCatalogCode());
    fileModelMap.put(DAMConstants.FILE_MODEL_ORGANIZATION_ID, assetUploadDTO.getOrganizationCode());
    fileModelMap.put(DAMConstants.FILE_MODEL_ENDPOINT_ID, assetUploadDTO.getEndpointId());
    fileModelMap.put(DAMConstants.IS_INDESIGN_SERVER_ENABLED, asset.getIsInDesignServerEnabled());
    return AssetUtils.handleFile(fileModelMap);
  }
    
  @Override
  protected void runBaseEntityBatch(Set<Long> batchIIDs) throws RDBMSException, CSFormatException
  {
    Collection<IAssetFileDTO> assetsToUpload = new ArrayList<>();
    Set<Long> duplicateAssetIIdSet = new HashSet<>();
    IBaseEntityDAO baseEntityDAO = new BaseEntityDAO(localeCatalogDao,
        new UserSessionDTO(), new BaseEntityDTO());
    Map<String, Object> transactionDataMap = new HashMap<>();
    transactionDataMap.put(ITransactionData.ENDPOINT_ID, assetUploadDTO.getEndpointId());
    transactionDataMap.put(ITransactionData.ORGANIZATION_ID, assetUploadDTO.getOrganizationCode());
    transactionDataMap.put(ITransactionData.PHYSICAL_CATALOG_ID, assetUploadDTO.getCatalogCode());
    List<String> filesToDelete = new ArrayList<>();
    boolean isDetectDuplicate = assetUploadDTO.getDetectDuplicate();

    List<String> successfulIds = new ArrayList<>();
    List<Long> successInstanceIIds = new ArrayList<>();
    batchIIDs.forEach( batchIID -> assetsToUpload.add( sourceAssetsMap.get(batchIID)));
    try {
      List<IAssetKeysModel> assetKeysModelList = new ArrayList<>();
      Map<String, Boolean> isHashDuplicatedMap = new HashMap<>();
    
      for (IAssetFileDTO asset : assetsToUpload) {
        uploadAsset(duplicateAssetIIdSet, baseEntityDAO, transactionDataMap, filesToDelete,
            isDetectDuplicate, assetKeysModelList, isHashDuplicatedMap, asset);
      }
      
      if (!assetKeysModelList.isEmpty()) {

        IBulkCreateAssetInstanceResponseModel informationModel = createInstances(assetKeysModelList, isHashDuplicatedMap);
        successfulIds = informationModel.getSuccess();
       
        // Putting created assets successInstanceIIds of this BGP batch into Job RuntimeData data 
        successInstanceIIds = informationModel.getSuccessInstanceIIds();
        List<Long> assetInstanceIds = jobData.getRuntimeData().getArrayField(BulkRelationshipCreateDTO.SUCCESS_INSTANCE_IIDS, Long.class);
        assetInstanceIds.addAll(successInstanceIIds);
        jobData.getRuntimeData().setLongArrayField(BulkRelationshipCreateDTO.SUCCESS_INSTANCE_IIDS, assetInstanceIds);
       
        loggingForTIV(informationModel);

        if (isDetectDuplicate) {
          //TIV Duplicate check
          Map<Long, Object> tivDuplicateDetectionInfoMap = informationModel.getTivDuplicateDetectionInfoMap();
          Set<Long> tivDuplicateIIds = handleDuplicateForTIV(tivDuplicateDetectionInfoMap, baseEntityDAO, transactionDataMap);
          
          //update duplicate status
          Set<Long> baseEntityIIds = new HashSet<>();
          baseEntityIIds.addAll(duplicateAssetIIdSet);
          baseEntityIIds.addAll(informationModel.getDuplicateIIdSet());
          baseEntityIIds.addAll(tivDuplicateIIds);
          if (!baseEntityIIds.isEmpty()) {
            baseEntityDAO.markAssetsDuplicateByIIds(baseEntityIIds, true);
            for (Long baseEntityIId : baseEntityIIds) {
              localeCatalogDao.postUsecaseUpdate(baseEntityIId, IEventDTO.EventType.ELASTIC_UPDATE);
            }
          }
        }
      }
      
      logInformationForCollections(assetsToUpload, successfulIds);
    }
    catch (Exception e) {
      jobData.setStatus(BGPStatus.ENDED_EXCEPTION);
      throw new RDBMSException( 600, "ASSET ERROR", e);
    }
    finally {
      filesToDelete.forEach(file -> AssetUtils.deleteFileAndDirectory(file));
    }
    
    if (!successfulIds.isEmpty()) {
      jobData.getLog().info("%s asset(s) uploaded with success: %s", assetsToUpload.size(),
          Text.join(",", assetsToUpload.stream().map(x -> x.getFilePath()).collect(Collectors.toList())));
    }
  }

  /***
   * Log information for collections
   * @param assetsToUpload
   * @param successfulIds
   */
  private void logInformationForCollections(Collection<IAssetFileDTO> assetsToUpload, List<String> successfulIds)
  {
    List<String> collectionIds = assetUploadDTO.getCollectionIds();
    if(!collectionIds.isEmpty()) {
      if(assetsToUpload.size() == successfulIds.size()) {
        jobData.getLog().info("Assets are added into the collection(s) successfully.");
      }
      else {
        jobData.getLog().info("Failed assets are skipped while adding into the collection(s).");
      }
    }
  }

  /***
   * Upload asset to swift server and log exception and warnings, if any
   * @param duplicateAssetIIdSet
   * @param baseEntityDAO
   * @param transactionDataMap
   * @param filesToDelete
   * @param isDetectDuplicate
   * @param assetKeysModelList
   * @param isHashDuplicatedMap
   * @param asset
   */
  private void uploadAsset(Set<Long> duplicateAssetIIdSet, IBaseEntityDAO baseEntityDAO,
      Map<String, Object> transactionDataMap, List<String> filesToDelete, boolean isDetectDuplicate,
      List<IAssetKeysModel> assetKeysModelList, Map<String, Boolean> isHashDuplicatedMap,
      IAssetFileDTO asset)
  {
    Map<String, Object> uploadedAsset = null;
    try {
      uploadedAsset = uploadAsset(asset);
      String hash = (String) uploadedAsset.get(DAMConstants.HASH);
      asset.setHash(hash);
      if (asset.getExtensionConfiguration() != null && (boolean) asset.getExtensionConfiguration().get("extractMetadata")) {
        asset.setMetaData((Map<String, Object>) uploadedAsset.get(DAMConstants.METADATA));
      }
      
      if (isDetectDuplicate) {
        // Duplicate Detection
        long duplicateAssetIId = handleDuplicate(uploadedAsset, hash, baseEntityDAO, isHashDuplicatedMap, transactionDataMap);
        if (duplicateAssetIId != 0 && duplicateAssetIId != 1) {
          duplicateAssetIIdSet.add(duplicateAssetIId);
        }
      }
      assetKeysModelList.add(AssetUtils.getAssetKeysModel(asset.getFilePath(), asset.getExtensionType(), uploadedAsset));
    }
    catch (AssetFileTypeNotSupportedException e) {
      jobData.getLog().warn("The upload for the asset(s) " + FilenameUtils.getName(asset.getFilePath()) + " is restricted.");
    }
    catch (ExtractedFileTypeNotSupportedException e) {
      jobData.getLog().warn("Following assets could not be extracted " + FilenameUtils.getName(asset.getFilePath()));
    }
    catch (Exception e) {
      jobData.getLog().error("ASSET UPLOAD ERROR" + e);
    }
    finally {
      filesToDelete.add(asset.getFilePath());
      logWarnings(uploadedAsset);
    }
  }

  /***
   * Create asset instances by calling BulkCreateAssetInstanceService service
   * @param assetKeysModelList
   * @param isHashDuplicatedMap
   * @return
   * @throws Exception
   */
  private IBulkCreateAssetInstanceResponseModel createInstances(List<IAssetKeysModel> assetKeysModelList,
      Map<String, Boolean> isHashDuplicatedMap) throws Exception
  {
    IBulkCreateAssetInstanceService bulkCreateAssetInstanceService = BGProcessApplication
        .getApplicationContext().getBean(IBulkCreateAssetInstanceService.class);
    IBulkCreateAssetInstanceRequestModel dataModel = new BulkCreateAssetInstanceRequestModel();
    dataModel.setAssetKeysModelList(assetKeysModelList);
    dataModel.setCollectionIds(assetUploadDTO.getCollectionIds());
    dataModel.setIsHashDuplicatedMap(isHashDuplicatedMap);
    return bulkCreateAssetInstanceService.execute(dataModel);
  }


  /**
   * Logging the success, warnings and failure messages for TIV
   * @param informationModel
   */
  private void loggingForTIV(IBulkCreateAssetInstanceResponseModel informationModel)
  {
    List<String> tivSuccess = informationModel.getTivSuccess();
    for (String tivSuccessMsg : tivSuccess) {
      jobData.getLog().info(tivSuccessMsg);
    }
    List<String> tivFailures = informationModel.getTivFailure();
    for (String tivFailureMsg : tivFailures) {
      jobData.getLog().error(tivFailureMsg);
    }
    List<String> tivWarnings = informationModel.getTivWarning();
    for (String tivWarning : tivWarnings) {
      jobData.getLog().warn(tivWarning);
    }
  }

  /**
   * This method returns the list of duplicateIIds for which isDuplicate
   * column needs to be updated and log warning for duplicate asset if duplicate
   * detection is On.
   * 
   * @param tivDuplicateDetectionInfoMap
   * @param baseEntityDAO
   * @return list of duplicateIIds needs to be updated
   * @throws RDBMSException
   */
  private Set<Long> handleDuplicateForTIV(Map<Long, Object> tivDuplicateDetectionInfoMap,
      IBaseEntityDAO baseEntityDAO, Map<String, Object> transactionDataMap) throws RDBMSException
  {
    Set<Long> idsToReturn = new HashSet<>();
    for (long tivIId : tivDuplicateDetectionInfoMap.keySet()) {
      Map<String, String> tivInfo = (Map<String, String>) tivDuplicateDetectionInfoMap.get(tivIId);
      String hash = tivInfo.get(DAMConstants.HASH);
      
      long duplicateIId = AssetUtils.handleDuplicate(hash, baseEntityDAO, transactionDataMap, tivIId);
      if(duplicateIId != 0) {
        if (assetUploadDTO.getShouldCheckForRedundancy() && !tivDuplicateDetectionInfoMap.isEmpty()) {
          jobData.getLog().warn("TIV Asset already present in DAM. " + tivInfo.get(DAMConstants.NAME));
        }
        if(duplicateIId != 1) {
          idsToReturn.add(duplicateIId);
        }
        idsToReturn.add(tivIId);
      }
    }
    return idsToReturn;
  }

  /**
   * This method returns duplicateIId for which isDuplicate column needs to be
   * updated and log warning for duplicate asset if duplicate detection is On.
   * 
   * @param uploadedAsset
   * @param hash
   * @param baseEntityDAO
   * @param duplicateIIdSet
   * @param isHashDuplicatedMap
   * @throws RDBMSException
   */
  private long handleDuplicate(Map<String, Object> uploadedAsset, String hash, IBaseEntityDAO baseEntityDAO,
      Map<String, Boolean> isHashDuplicatedMap, Map<String, Object> transactionDataMap) throws RDBMSException
  {
    if (isHashDuplicatedMap.get(hash) != null) {
      isHashDuplicatedMap.put(hash, true);
    }
    else {
      isHashDuplicatedMap.put(hash, false);
    }
    long duplicateIId = AssetUtils.handleDuplicate(hash, baseEntityDAO, transactionDataMap, 0);
    if(isHashDuplicatedMap.get(hash) || duplicateIId != 0) {
      isHashDuplicatedMap.put(hash, true);
      if (assetUploadDTO.getShouldCheckForRedundancy()) {
        jobData.getLog().warn("Asset already present in DAM. " + uploadedAsset.get(DAMConstants.FILE_NAME));
      }
    }
    return duplicateIId;
  }

  /**
   * Logging the warnings related to metadata extraction and renditions.
   * 
   * @param uploadedAsset
   */
  private void logWarnings(Map<String, Object> uploadedAsset)
  {
    if (uploadedAsset != null) {
      IExceptionModel warnings = (IExceptionModel) uploadedAsset.get("warnings");
      if (warnings != null) {
        for (IDevExceptionDetailModel exception : warnings.getDevExceptionDetails()) {
          if (exception.getItemName() != null) {
            if (ExifToolException.class.getName().equals(exception.getExceptionClass())
                || ExtractedFileNotProcessedException.class.getName().equals(exception.getExceptionClass())) {
              jobData.getLog().warn("Failed to extract metadata for the file name: " + exception.getItemName());
            }
            else {
              jobData.getLog().warn("Failed to extract renditions for the file name: " + exception.getItemName());
            }
          }
        }
      }
    }
  }
  
  @Override
  protected String getCallbackData()
  {
    if (DAMConstants.MODE_RELATIONSHIP_BULK_UPLOAD.equals(assetUploadDTO.getMode())) {
      Map<String, Object> callBackDataMap = new HashMap<>();
      try {
        List<Long> successInstanceIIds = jobData.getRuntimeData().getArrayField(BulkRelationshipCreateDTO.SUCCESS_INSTANCE_IIDS,
            Long.class);
        callBackDataMap.put(BulkRelationshipCreateDTO.SUCCESS_INSTANCE_IIDS, successInstanceIIds);
        callBackDataMap.put(BulkRelationshipCreateDTO.SIDE_1_INSTANCE_ID, assetUploadDTO.getSide1InstanceId());
        callBackDataMap.put(BulkRelationshipCreateDTO.TAB_ID, assetUploadDTO.getTabId());
        callBackDataMap.put(BulkRelationshipCreateDTO.TAB_TYPE, assetUploadDTO.getTabType());
        callBackDataMap.put(BulkRelationshipCreateDTO.SIDE_1_BASE_TYPE, assetUploadDTO.getSide1BaseType());
        callBackDataMap.put(BulkRelationshipCreateDTO.RELATIONSHIP_ID, assetUploadDTO.getRelationshipId());
        callBackDataMap.put(BulkRelationshipCreateDTO.RELATIONSHIP_ENTITY_ID, assetUploadDTO.getRelationshipEntityId());
        callBackDataMap.put(BulkRelationshipCreateDTO.SIDE_ID, assetUploadDTO.getSideId());
        callBackDataMap.put(IUploadAssetModel.MODE, assetUploadDTO.getMode());
      }
      catch (CSFormatException e) {
        jobData.getLog().exception(e);
      }
      return new JSONObject(callBackDataMap).toString();
    }
    else {
      return "{}";
    }
  }
}
