package com.cs.dam.runtime.assetinstance;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.model.asset.IAssetConfigurationDetailsResponseModel;
import com.cs.core.config.interactor.model.asset.IAssetFileModel;
import com.cs.core.config.interactor.model.attribute.IBooleanReturnModel;
import com.cs.core.config.strategy.usecase.asset.IGetAssetStrategy;
import com.cs.core.rdbms.asset.dao.AssetMiscDAO;
import com.cs.core.rdbms.downloadtracker.idao.IDownloadTrackerDAO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.abstrct.AbstractRollbackInstanceVersionService;
import com.cs.core.runtime.interactor.entity.eventinstance.EventInstanceSchedule;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceSchedule;
import com.cs.core.runtime.interactor.entity.klassinstance.IAssetInstance;
import com.cs.core.runtime.interactor.exception.assetserver.AssetFileTypeNotSupportedRollbackException;
import com.cs.core.runtime.interactor.model.assetinstance.AssetInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IGetAssetDetailsResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.versionrollback.IKlassInstanceVersionRollbackModel;
import com.cs.core.runtime.strategy.usecase.asset.IFetchAssetConfigurationDetails;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.dam.asset.exceptions.AssetAlreadyUploadedException;
import com.cs.dam.config.strategy.usecase.duplicatedetection.IGetDuplicateDetectionStatusStrategy;
import com.cs.dam.rdbms.downloadtracker.dao.DownloadTrackerDAO;
import com.cs.dam.runtime.assetinstance.linksharing.IDeleteAssetFromSharedSwiftServerService;
import com.cs.dam.runtime.strategy.usecase.assetinstance.IGetConfigDetailsForAssetInstanceOverviewTabStrategy;
import com.cs.dam.runtime.util.AssetInstanceUtils;
import com.cs.utils.ExceptionUtil;
import com.cs.utils.dam.AssetUtils;

@Service
public class RollbackAssetInstanceVersionService extends AbstractRollbackInstanceVersionService<IKlassInstanceVersionRollbackModel, IGetKlassInstanceModel>
  implements IRollbackAssetInstanceVersionService
{
  
  @Autowired
  protected IGetAssetStrategy                                    getAssetStrategy;
  
  @Autowired
  protected TransactionThreadData                                transactionThread;
  
  @Autowired
  protected ApplicationContext                                   appContext;
  
  @Autowired
  protected ThreadPoolTaskExecutor                               taskExecutor;
  
  @Autowired
  protected ISessionContext                                      context;
  
  @Autowired
  protected IFetchAssetConfigurationDetails                      fetchAssetConfigurationDetails;
  
  @Autowired
  protected IGetConfigDetailsForAssetInstanceOverviewTabStrategy getConfigDetailsForAssetInstanceOverviewTabStrategy;
  
  @Autowired
  protected IDeleteAssetFromSharedSwiftServerService             deleteAssetFromSharedSwiftServerService;
  
  @Autowired
  IGetDuplicateDetectionStatusStrategy                           getDuplicateDetectionStatusStrategy;
  
  @Override
  public IGetKlassInstanceModel execute(IKlassInstanceVersionRollbackModel dataModel)
      throws Exception
  {
    return super.executeInternal(dataModel);
  }
  
  @Override
  protected IGetConfigDetailsForCustomTabModel getConfigDetails(IMulticlassificationRequestModel configDetailsRequestModel) throws Exception
  {
    return getConfigDetailsForAssetInstanceOverviewTabStrategy.execute(configDetailsRequestModel);
  } 
  
  /**
   * Handle entity specific use case.
   */
  @Override
  protected void handleEntitySpecificUseCase(IBaseEntityDTO currentBaseEntityDTO,
      IBaseEntityDTO rollbackedBaseEntityDTO) throws Exception
  {
    validateAssetToRollback(currentBaseEntityDTO, rollbackedBaseEntityDTO);
    handleSharedLinkAndDownloadTracker(currentBaseEntityDTO, rollbackedBaseEntityDTO);
  }
  
  /**
   * Validate if asset instance is valid to be rollbacked or not.
   * @param assetConfiguration 
   * @param id
   * @param versionId
   * @param response
   * @throws Exception
   */
  private void validateAssetToRollback(IBaseEntityDTO currentBaseEntityDTO, 
      IBaseEntityDTO rollbackedBaseEntityDTO) throws Exception
  {
    // Fetch extension from rollbacked asset.
    String targetEntityExtensionAsString = rollbackedBaseEntityDTO.getEntityExtension().toString();
    IAssetInformationModel targetAssetInformation = (IAssetInformationModel) ObjectMapperUtil.readValue(targetEntityExtensionAsString, AssetInformationModel.class);
    
    if(StringUtils.isNotEmpty(targetAssetInformation.getFileName())) {
      String extension = FilenameUtils.getExtension(targetAssetInformation.getFileName());
      
      // Get nature type
      String classifierCode = rollbackedBaseEntityDTO.getNatureClassifier().getCode();
      IIdParameterModel requestModelForFetAssetConfiguration = new IdParameterModel();
      requestModelForFetAssetConfiguration.setType(classifierCode);
      
      // Fetch asset configuration
      IAssetConfigurationDetailsResponseModel assetConfiguration = fetchAssetConfigurationDetails.execute(requestModelForFetAssetConfiguration);
      
      // Check configurable extension and throw exception in case extension not supported.
      Map<String, Object> extensionConfigurationReturnMap = new HashMap<>();
      AssetUtils.getExtensionConfigurationDetails(assetConfiguration, extension, assetConfiguration.getNatureType(), extensionConfigurationReturnMap );
      if(extensionConfigurationReturnMap.get(IAssetFileModel.EXTENSION_CONFIGURATION) == null) {
        IExceptionModel failure = new ExceptionModel();
        ExceptionUtil.addFailureDetailsToFailureObject(failure, new AssetFileTypeNotSupportedRollbackException(), null, "");
        throw new AssetFileTypeNotSupportedRollbackException(failure);
      }
    }
  }

  /**
   * Handle shared link and download count at th time of rollback
   * @param currentBaseEntityDTO
   * @param rollbackedBaseEntityDTO
   * @throws Exception
   * @throws NoSuchAlgorithmException
   * @throws IOException
   * @throws RDBMSException
   * @throws CSFormatException
   */
  private void handleSharedLinkAndDownloadTracker(IBaseEntityDTO currentBaseEntityDTO, IBaseEntityDTO rollbackedBaseEntityDTO)
      throws Exception, NoSuchAlgorithmException, IOException, RDBMSException, CSFormatException
  {
    // Get current and target asset information and whether asset is going to be replaced after rollback or not
    String entityExtensionAsString = currentBaseEntityDTO.getEntityExtension().toString();
    String targetEntityExtensionAsString = rollbackedBaseEntityDTO.getEntityExtension().toString();
    IAssetInformationModel currentAssetInformationModel = (IAssetInformationModel) ObjectMapperUtil.readValue(entityExtensionAsString, AssetInformationModel.class);
    IAssetInformationModel targetAssetInformation = (IAssetInformationModel) ObjectMapperUtil.readValue(targetEntityExtensionAsString, AssetInformationModel.class);
    String targetAssetObjectKey = targetAssetInformation.getAssetObjectKey();
    String currentAssetObjectKey = currentAssetInformationModel.getAssetObjectKey();
    boolean isTargetAssetObjectKeyEmpty = StringUtils.isBlank(targetAssetObjectKey);
    boolean isCurrentAssetObjectKeyEmpty = StringUtils.isBlank(currentAssetObjectKey);
    boolean isAssetNotReplaced = isTargetAssetObjectKeyEmpty || isCurrentAssetObjectKeyEmpty || StringUtils.equals(currentAssetObjectKey, targetAssetObjectKey);
    
    // Fetch download tracker and assetmisc DAO from singleton
    IDownloadTrackerDAO downloadTrackerDAO = DownloadTrackerDAO.getInstance();
    AssetMiscDAO assetMiscDAO = AssetMiscDAO.getInstance();
    
    IIdParameterModel dataModel = new IdParameterModel();
    long assetInstanceIID = rollbackedBaseEntityDTO.getBaseEntityIID();
    List<String> assetInstanceIIDs = new ArrayList<String>();
    assetInstanceIIDs.add(Long.toString(assetInstanceIID));
    
    // If asset is replaced then delete shared link, reset download count and delete download logs.
    if (!isAssetNotReplaced) {
      rollbackedBaseEntityDTO.setHashCode(generateHashCode(targetAssetInformation));
      List<String> sharedObjectIdList = assetMiscDAO.deleteSharedObjectIdForMainAssetAndItsVariantById(assetInstanceIID);
      for (String sharedObjectId : sharedObjectIdList) {
        dataModel.setId(sharedObjectId);
        deleteAssetFromSharedSwiftServerService.execute(dataModel);
      }
      downloadTrackerDAO.resetDownloadCount(assetInstanceIID);
    }
    else {
      if (isTargetAssetObjectKeyEmpty) {
        rollbackedBaseEntityDTO.setEntityExtension(null);
        List<String> sharedObjectIdList = assetMiscDAO.deleteSharedObjectIdForMainAssetAndItsVariantById(assetInstanceIID);
        for (String sharedObjectId : sharedObjectIdList) {
          dataModel.setId(sharedObjectId);
          deleteAssetFromSharedSwiftServerService.execute(dataModel);
        }
        downloadTrackerDAO.resetDownloadCount(assetInstanceIID);
      }
      else if (isCurrentAssetObjectKeyEmpty) {
        // Needed when we rollback from asset with no image to asset with image
        rollbackedBaseEntityDTO.setHashCode(generateHashCode(targetAssetInformation));
      }
      else {
        rollbackedBaseEntityDTO.setHashCode(currentBaseEntityDTO.getHashCode());
      }
      
      // Set downloadcount property in valuerecord
      Set<IPropertyRecordDTO> rollbackEntityPropertyRecords = rollbackedBaseEntityDTO.getPropertyRecords();
      IPropertyRecordDTO assetDownloadCountProperty = getPropertyRecordDTO(rollbackEntityPropertyRecords, SystemLevelIds.ASSET_DOWNLOAD_COUNT_ATTRIBUTE);
      IPropertyRecordDTO assetDownloadCountPropertyToReplaceWith = getPropertyRecordDTO(currentBaseEntityDTO.getPropertyRecords(), SystemLevelIds.ASSET_DOWNLOAD_COUNT_ATTRIBUTE);
      if (assetDownloadCountProperty != null) {
        rollbackEntityPropertyRecords.remove(assetDownloadCountProperty);
      }
      if (assetDownloadCountPropertyToReplaceWith != null) {
       rollbackEntityPropertyRecords.add(assetDownloadCountPropertyToReplaceWith);
      }
    }
  }

  /**
   * Generate the hash code of binary file which is fetched from swift server
   * @param targetAssetInformation
   * @return
   * @throws Exception
   * @throws NoSuchAlgorithmException
   * @throws IOException
   */
  private String generateHashCode(IAssetInformationModel targetAssetInformation)
      throws Exception, NoSuchAlgorithmException, IOException
  {
    Map<String, String> assetInfo = new HashMap<String, String>();
    assetInfo.put(IAssetInformationModel.ASSET_OBJECT_KEY, targetAssetInformation.getAssetObjectKey());
    assetInfo.put(IAssetInformationModel.TYPE, targetAssetInformation.getType());
    Map<String, Object> assetFromSwift = AssetUtils.getAssetFromSwift(assetInfo);
    InputStream inputStream = (InputStream) assetFromSwift.get(IGetAssetDetailsResponseModel.INPUT_STREAM);
    String hash = AssetUtils.generateHashCodeByInputStream(inputStream);
    return hash;
  }

  /**
   * Get property record dto by propertycode
   * @param attributes
   * @return 
   */
  private IPropertyRecordDTO getPropertyRecordDTO(Set<IPropertyRecordDTO> attributes, String propertyCode)
  {
    for (IPropertyRecordDTO propertyRecordDTO : attributes) {
      String localPropertyCode = propertyRecordDTO.getProperty().getPropertyCode();
      if(propertyCode.equals(localPropertyCode)) {
        return propertyRecordDTO;
      }
    }
    return null;
  }
  
  /**
   * Fill entity specific data and warnings.
   */
  @Override
  protected void fillEntitySpecificData(IGetKlassInstanceModel klassInstance, IBaseEntityDAO currentBaseEntityDAO, IBaseEntityDTO rollbackedBaseEntityDTO) throws Exception
  {
    IBaseEntityDAO rollbackedBaseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(rollbackedBaseEntityDTO);
    IBaseEntityDTO currentBaseEntityDTO = currentBaseEntityDAO.getBaseEntityDTO();
    String targetEntityExtensionAsString = rollbackedBaseEntityDTO.getEntityExtension().toString();
    String currentEntityExtensionAsString = currentBaseEntityDTO.getEntityExtension().toString();
    IAssetInformationModel targetAssetInformation = (IAssetInformationModel) ObjectMapperUtil.readValue(targetEntityExtensionAsString, AssetInformationModel.class);
    IAssetInformationModel currentAssetInformation = (IAssetInformationModel) ObjectMapperUtil.readValue(currentEntityExtensionAsString, AssetInformationModel.class);
    String currentAssetObjectKey = currentAssetInformation.getAssetObjectKey();
    String targetAssetObjectKey = targetAssetInformation.getAssetObjectKey();
    
    // Handle duplicate detection
    if (StringUtils.isNotBlank(targetAssetObjectKey) && !targetAssetObjectKey.equals(currentAssetObjectKey)) {
      handleDuplicateDetection(klassInstance, rollbackedBaseEntityDTO, rollbackedBaseEntityDAO);
    }
    
    // Handle Asset expiry
    Map<String, Object> returnModel = handleAssetExpiry(rollbackedBaseEntityDTO, rollbackedBaseEntityDAO);
    IEventInstanceSchedule schedule =  (IEventInstanceSchedule) returnModel.get("schedule");
    boolean isExpired = (boolean) returnModel.get("isExpired");
    
    // Fill entity extension in response model
    IAssetInstance assetInstance = (IAssetInstance) klassInstance.getKlassInstance();
    AssetInstanceUtils.fillEntityExtensionInAssetCoverFlowAttribute(assetInstance, currentBaseEntityDTO, rollbackedBaseEntityDTO.getEntityExtension());   
    assetInstance.setIsExpired(isExpired);
    assetInstance.setEventSchedule(schedule);
  }

  /**
   * This method is handling asset expiry on rollback of asset instance
   * @param rollbackedBaseEntityDTO
   * @param rollbackedBaseEntityDAO
   * @param assetInstanceIID
   * @return
   * @throws RDBMSException
   */
  private Map<String, Object> handleAssetExpiry(IBaseEntityDTO rollbackedBaseEntityDTO,
      IBaseEntityDAO rollbackedBaseEntityDAO) throws RDBMSException
  {
    Map<String, Object> returnModel = new HashMap<String, Object>();
    long assetInstanceIID = rollbackedBaseEntityDTO.getBaseEntityIID();
    Set<IPropertyRecordDTO> rollbackPropertyRecords = rollbackedBaseEntityDTO.getPropertyRecords();
    IPropertyRecordDTO rollbackEndTimePropertyRecord = getPropertyRecordDTO(rollbackPropertyRecords, SystemLevelIds.END_DATE_ATTRIBUTE);
    IPropertyRecordDTO rollbackStartTimePropertyRecord = getPropertyRecordDTO(rollbackPropertyRecords, SystemLevelIds.START_DATE_ATTRIBUTE);
    IEventInstanceSchedule schedule = new EventInstanceSchedule();
    Boolean isExpired = false;
    long currentTimeMillis = System.currentTimeMillis();
    if(rollbackEndTimePropertyRecord != null ) {      
      long endTime = Long.parseLong(((IValueRecordDTO) rollbackEndTimePropertyRecord).getValue());
      schedule.setEndTime(endTime);
      if (currentTimeMillis >= endTime) {
        isExpired = true;
      }
    }
    if(rollbackStartTimePropertyRecord != null ) {      
      schedule.setStartTime(Long.parseLong(((IValueRecordDTO) rollbackStartTimePropertyRecord).getValue()));
    }
    rollbackedBaseEntityDAO.updateExpiryStatusForAssetByIID(assetInstanceIID, isExpired);
    returnModel.put("schedule", schedule);
    returnModel.put("isExpired", isExpired);
    return returnModel;
  }

  /**
   * Handle Duplicate Detection
   * @param klassInstance
   * @param rollbackedBaseEntityDTO
   * @param rollbackedBaseEntityDAO
   * @throws Exception
   * @throws RDBMSException
   */
  private void handleDuplicateDetection(IGetKlassInstanceModel klassInstance,
      IBaseEntityDTO rollbackedBaseEntityDTO, IBaseEntityDAO rollbackedBaseEntityDAO)
          throws Exception, RDBMSException
  {
    TransactionData transactionData = transactionThread.getTransactionData();
    Map<String, Object> transactionDataMap = new HashMap<String, Object>();
    IBooleanReturnModel duplicateDetectionStatus = getDuplicateDetectionStatusStrategy.execute(new VoidModel());
    long assetInstanceIID = rollbackedBaseEntityDTO.getBaseEntityIID();
    ILocaleCatalogDAO localeCatlogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    long duplicateId = 0;
    if (duplicateDetectionStatus.getDuplicateStatus()) {
        transactionDataMap.put(ITransactionData.ENDPOINT_ID, transactionData.getEndpointId());
        transactionDataMap.put(ITransactionData.ORGANIZATION_ID, transactionData.getOrganizationId());
        transactionDataMap.put(ITransactionData.PHYSICAL_CATALOG_ID, transactionData.getPhysicalCatalogId());
        duplicateId = AssetUtils.handleDuplicate(rollbackedBaseEntityDTO.getHashCode(), rollbackedBaseEntityDAO, transactionDataMap, assetInstanceIID);
        AssetUtils.updateDuplicateStatus(duplicateId, rollbackedBaseEntityDAO, localeCatlogDAO);
      if (duplicateId != 0) {
        ExceptionUtil.addFailureDetailsToFailureObject(klassInstance.getWarnings(),
            new AssetAlreadyUploadedException(), null, klassInstance.getKlassInstance().getName());
      }
    }
  }
  
}
