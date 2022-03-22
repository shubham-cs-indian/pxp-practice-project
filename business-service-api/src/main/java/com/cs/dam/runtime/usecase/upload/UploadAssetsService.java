package com.cs.dam.runtime.usecase.upload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.DAMConstants;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.model.asset.AssetImageKeysModel;
import com.cs.core.config.interactor.model.asset.IAssetConfigurationDetailsResponseModel;
import com.cs.core.config.interactor.model.asset.IAssetFileModel;
import com.cs.core.config.interactor.model.asset.IAssetImageKeysModel;
import com.cs.core.config.interactor.model.asset.IAssetKeysModel;
import com.cs.core.config.interactor.model.asset.IMultiPartFileInfoModel;
import com.cs.core.config.interactor.model.asset.IUploadAssetModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForRuntimeEntitiesResponseModel;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.entity.dao.BaseEntityDAO;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.rdbms.tracking.dto.UserSessionDTO;
import com.cs.core.runtime.interactor.exception.assetinstance.UserNotHaveCreatePermissionForAsset;
import com.cs.core.runtime.interactor.exception.assetserver.AssetFileTypeNotSupportedException;
import com.cs.core.runtime.interactor.exception.assetserver.BulkUploadFailedException;
import com.cs.core.runtime.interactor.exception.assetserver.UnknownAssetUploadException;
import com.cs.core.runtime.interactor.exception.assetserver.ZipFileTypeNotSupportedException;
import com.cs.core.runtime.interactor.exception.assetserver.ZipFileTypeNotSupportedInRelationshipException;
import com.cs.core.runtime.interactor.model.assetupload.IUploadAssetResponseModel;
import com.cs.core.runtime.interactor.model.assetupload.UploadAssetResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.strategy.usecase.asset.IFetchAssetConfigurationDetails;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.dam.asset.exceptions.AssetAlreadyUploadedException;
import com.cs.utils.ExceptionUtil;
import com.cs.utils.dam.AssetUtils;

/**
 * This API is used to prepare upload model, do permission checks and upload the
 * assets to swift server
 * @author vannya.kalani
 *
 */
@Service
public class UploadAssetsService implements IUploadAssetsService {
  
  @Autowired
  protected IFetchAssetConfigurationDetails fetchAssetConfigurationDetails;
  
  @Autowired
  protected PermissionUtils                 permissionUtils;
  
  @Autowired
  protected TransactionThreadData           transactionThreadData;
  
  @Override
  public IUploadAssetResponseModel execute(IUploadAssetModel requestModel) throws Exception
  {
    String mode = requestModel.getMode();
    List<IMultiPartFileInfoModel> multiPartFileInfoModelList = requestModel.getMultiPartFileInfoList();
    String klassId = requestModel.getKlassId();
    
    IUploadAssetResponseModel returnModel = new UploadAssetResponseModel();
    IExceptionModel failure = new ExceptionModel();

    // Fetch config details
    IdParameterModel idParameterModel = new IdParameterModel(klassId);
    idParameterModel.setType(klassId);
    IAssetConfigurationDetailsResponseModel assetConfigModel = fetchAssetConfigurationDetails.execute(idParameterModel);
   
    List<IAssetFileModel> fileModelList = getFileModelList(mode, multiPartFileInfoModelList, failure, assetConfigModel);
    checkCreatePermissions(fileModelList, failure);

    if (!(DAMConstants.MODE_BULK_UPLOAD.equals(mode) || DAMConstants.MODE_RELATIONSHIP_BULK_UPLOAD.equals(mode))) {
      returnModel = uploadAssetToSwift(failure, assetConfigModel, fileModelList, returnModel);
    }
    returnModel.setAssetConfigModel(assetConfigModel);
    returnModel.setFileModelList(fileModelList);
    returnModel.setFailure(failure);
    return returnModel;
  }

  /**
   * Upload assets using IAssetFileModel list to swift server, handle duplicate
   * detection, delete files written on file system and returns UploadAssetResponseModel
   * 
   * @param failure : IExceptionModel object to add failure details 
   * @param assetModel : asset configuration details
   * @param fileModelList : detail of assets to upload on swift server
   * @param returnModel : IUploadAssetResponseModel to add IAssetKeysModel list and duplicateIId
   * @return 
   * @throws BulkUploadFailedException
   */
  private IUploadAssetResponseModel uploadAssetToSwift(IExceptionModel failure, IAssetConfigurationDetailsResponseModel assetModel,
      List<IAssetFileModel> fileModelList, IUploadAssetResponseModel returnModel) throws BulkUploadFailedException
  {
    List<IAssetKeysModel> keyModelList = new ArrayList<>();
    String thumbnailPath = null;
    boolean autoCreateTIVExists = assetModel.getIsAutoCreateTIVExist();
    long duplicateAssetIId = 0;
    
    for (IAssetFileModel file : fileModelList) {
      String fileName = file.getName() + "." + file.getExtension();
      try {
        IAssetKeysModel keyModel = AssetUtils.handleFile(file);
        keyModelList.add(keyModel);
        
        if (keyModel.getClass().equals(AssetImageKeysModel.class)) {
          thumbnailPath = ((IAssetImageKeysModel) keyModel).getThumbnailPath();
          if (!autoCreateTIVExists) {
            ((IAssetImageKeysModel) keyModel).setThumbnailPath(null);
          }
        }
        
        if (assetModel.getDetectDuplicate()) {
          duplicateAssetIId = handleDuplicateDetection(failure, assetModel, fileName, keyModel.getHash());
        }
        
        failure.getDevExceptionDetails().addAll(keyModel.getWarnings().getDevExceptionDetails());
        failure.getExceptionDetails().addAll(keyModel.getWarnings().getExceptionDetails());
      }
      catch (PluginException ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, null, fileName);
      }
      catch (Exception ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, new UnknownAssetUploadException(ex), null, fileName);
      }
      finally {
        if (!autoCreateTIVExists) {
          AssetUtils.deleteFileAndDirectory(file.getPath());
          AssetUtils.deleteFileAndDirectory(thumbnailPath);
        }
      }
    }
    
    if (keyModelList.isEmpty()) {
      throw new BulkUploadFailedException(failure.getExceptionDetails(), failure.getDevExceptionDetails());
    }
    returnModel.setAssetKeysModelList(keyModelList);
    returnModel.setDuplicateIId(duplicateAssetIId);
    return returnModel;
  }

  /**
   * Check create permission for all klasses and if permission does not exists
   * then add in failure object and remove from fileModelList
   * 
   * @param fileModelList
   * @param failure
   * @throws Exception
   */
  private void checkCreatePermissions(List<IAssetFileModel> fileModelList, IExceptionModel failure) throws Exception
  {
    String userId = transactionThreadData.getTransactionData().getUserId();
    
    // add klassIds to the set
    Set<String> klassIdsSet = new HashSet<>();
    for (IAssetFileModel file : fileModelList) {
      String klassId = file.getKlassId();
      if (klassId != null) {
        klassIdsSet.add(klassId);
      }
    }
    
    // add all unique klassIds in the list
    List<String> klassIdsList = new ArrayList<>(klassIdsSet);
    
    IGetGlobalPermissionForRuntimeEntitiesResponseModel globalPermissionModel = permissionUtils
        .getGlobalPermissionsForRuntimeEntities(userId, null, klassIdsList);
    Map<String, IGlobalPermission> globalPermissions = globalPermissionModel.getGlobalPermissions();
    
    // using iterator to check create permission
    Iterator<IAssetFileModel> fileModel = fileModelList.iterator();
    while (fileModel.hasNext()) {
      IAssetFileModel assetFile = fileModel.next();
      String klassId = assetFile.getKlassId();
      if (klassId != null) {
        Boolean canCreate = globalPermissions.get(klassId).getCanCreate();
        
        // if user doesn't have create permission then prepare failure object and
        // remove fileModel from fileModelList.
        if (Boolean.FALSE.equals(canCreate)) {
          ExceptionUtil.addFailureDetailsToFailureObject(failure, new UserNotHaveCreatePermissionForAsset(), null, assetFile.getName());
          fileModel.remove();
        }
      }
    }
  }

  /**
   * Prepare IAssetFileModel for each multipart file.
   * @param mode
   * @param multiPartFileInfoModelList
   * @param failure
   * @param assetModel
   * @return
   * @throws Exception
   */
  private List<IAssetFileModel> getFileModelList(String mode, List<IMultiPartFileInfoModel> multiPartFileInfoModelList,
      IExceptionModel failure, IAssetConfigurationDetailsResponseModel assetModel) throws Exception
  {
    String extension = FilenameUtils.getExtension(multiPartFileInfoModelList.get(0).getOriginalFilename());
    if (DAMConstants.MODE_SINGLE_UPLOAD.equals(mode) && assetModel.getUploadZip() && DAMConstants.ZIP.equals(extension)) {
      throw new ZipFileTypeNotSupportedException();
    }
    
    List<IAssetFileModel> fileModelList = new ArrayList<>();
    for (IMultiPartFileInfoModel multiPartFileInfoModel : multiPartFileInfoModelList) {
      
      //Upload through relationship section : zip extension not allowed.
      extension = FilenameUtils.getExtension(multiPartFileInfoModel.getOriginalFilename());
      if (DAMConstants.MODE_RELATIONSHIP_UPLOAD.equals(mode) && DAMConstants.ZIP.equals(extension)) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, new ZipFileTypeNotSupportedInRelationshipException(),
            null, multiPartFileInfoModel.getOriginalFilename());
        continue;
      }
      
      IAssetFileModel assetFileModel = AssetUtils.getFileModel(multiPartFileInfoModel,mode, fileModelList, assetModel);
      if (assetFileModel != null) {
        if (assetFileModel.getExtensionConfiguration() == null) {
          ExceptionUtil.addFailureDetailsToFailureObject(failure, new AssetFileTypeNotSupportedException(),
              null, multiPartFileInfoModel.getOriginalFilename());
        }
        else {
          fileModelList.add(assetFileModel);
        }
      }
    }
    return fileModelList;
  }
  
  /**
   * This method returns the duplicateIId needed to update
   * and log warning for duplicate asset if duplicate detection is On.
   * @param failure
   * @param assetModel
   * @param fileName
   * @param hash
   * @return
   * @throws RDBMSException
   */
  private long handleDuplicateDetection(IExceptionModel failure,
      IAssetConfigurationDetailsResponseModel assetModel, String fileName, String hash)
      throws RDBMSException
  {
    TransactionData transactionData = transactionThreadData.getTransactionData();
    Map<String, Object> transactionDataMap = new HashMap<>();
    transactionDataMap.put(ITransactionData.ENDPOINT_ID, transactionData.getEndpointId());
    transactionDataMap.put(ITransactionData.ORGANIZATION_ID, transactionData.getOrganizationId());
    transactionDataMap.put(ITransactionData.PHYSICAL_CATALOG_ID, transactionData.getPhysicalCatalogId());
    
    IBaseEntityDAO baseEntityDAO = new BaseEntityDAO(new LocaleCatalogDAO(new UserSessionDTO(),
        new LocaleCatalogDTO()), new UserSessionDTO(), new BaseEntityDTO());
    long duplicateAssetIId = AssetUtils.handleDuplicate(hash, baseEntityDAO, transactionDataMap, 0);
    if (duplicateAssetIId != 0 && Boolean.TRUE.equals(assetModel.getDetectDuplicate())) {
      ExceptionUtil.addFailureDetailsToFailureObject(failure, new AssetAlreadyUploadedException(), null, fileName);
    }
    return duplicateAssetIId;
  }
  
}
