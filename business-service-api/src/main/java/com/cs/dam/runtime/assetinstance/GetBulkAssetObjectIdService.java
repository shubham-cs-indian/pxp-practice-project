package com.cs.dam.runtime.assetinstance;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.test.DirectoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.DAMConstants;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.asset.AssetServerDetailsModel;
import com.cs.core.config.interactor.model.asset.GetAssetDetailsRequestModel;
import com.cs.core.config.interactor.model.asset.IAssetServerDetailsModel;
import com.cs.core.config.interactor.model.asset.IGetAssetDetailsRequestModel;
import com.cs.core.config.interactor.model.asset.IGetAssetDetailsStrategyModel;
import com.cs.core.config.interactor.model.configdetails.CoverFlowModel;
import com.cs.core.config.strategy.usecase.swift.IAuthenticateAssetServerStrategy;
import com.cs.core.config.strategy.usecase.swift.IGetAssetFromServerStrategy;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.model.assetinstance.AssetDownloadModel;
import com.cs.core.runtime.interactor.model.assetinstance.BulkAssetDownloadResponseModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetDownloadInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetDownloadInformationWithTIVModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetDownloadModel;
import com.cs.core.runtime.interactor.model.assetinstance.IBulkAssetDownloadResponseModel;
import com.cs.core.runtime.interactor.model.assetinstance.IBulkAssetDownloadWithVariantsModel;
import com.cs.core.runtime.interactor.model.assetinstance.ICoverFlowModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.usecase.asset.IGetDAMConfigurationDetailsStrategy;
import com.cs.core.runtime.usecase.task.IInsertDownloadLogsAndCountTask;
import com.cs.core.runtime.utils.threadPoolExecutor.ThreadPoolExecutorUtil;
import com.cs.core.services.CSProperties;
import com.cs.dam.config.interactor.model.downloadtracker.AssetInstanceDownloadLogsRequestModel;
import com.cs.dam.config.interactor.model.downloadtracker.IAssetInstanceDownloadLogsRequestModel;
import com.cs.dam.runtime.interactor.usecase.downloadtracker.IGetDownloadCountForOverviewTabService;
import com.cs.dam.runtime.util.AssetInstanceUtils;
import com.cs.runtime.interactor.model.downloadtracker.GetDownloadCountRequestModel;
import com.cs.runtime.interactor.model.downloadtracker.IGetDownloadCountRequestModel;
import com.cs.runtime.interactor.model.downloadtracker.IGetDownloadCountResponseModel;

@Service
public class GetBulkAssetObjectIdService
    extends AbstractRuntimeService<IBulkAssetDownloadWithVariantsModel, IBulkAssetDownloadResponseModel>
    implements IGetBulkAssetObjectIdService {
  
  @Autowired
  protected ISessionContext                        springSessionContext;
  
  @Autowired
  protected IAssetServerDetailsModel               assetServerDetails;
  
  @Autowired
  protected IGetAssetFromServerStrategy            getAssetFromServerStrategy;
  
  @Autowired
  protected IAuthenticateAssetServerStrategy       authenticateAssetServerStrategy;
  
  @Autowired
  protected ThreadPoolExecutorUtil                 threadPoolTaskExecutorUtil;
  
  @Autowired
  protected IInsertDownloadLogsAndCountTask        insertDownloadLogsAndCount;
  
  @Autowired
  protected AssetInstanceUtils                     assetInstanceUtils;
  
  @Autowired
  protected IGetDownloadCountForOverviewTabService getDownloadCountForOverviewTabService;

  @Override
  public IBulkAssetDownloadResponseModel executeInternal(IBulkAssetDownloadWithVariantsModel requestModel) throws Exception
  {

    IBulkAssetDownloadResponseModel returnModel = new BulkAssetDownloadResponseModel();
    IAssetDownloadModel bulkAssetDownloadModel = new AssetDownloadModel();
    List<IAssetDownloadInformationWithTIVModel> downloadInformation = requestModel.getDownloadInformation();
    List<Long> assetInstanceIdListGettingDownloaded = new ArrayList<Long>();
    IExceptionModel failure = new ExceptionModel();
   
    Set<String> klassIdList = new HashSet<String>();
    
    try {
      // If one main asset instance is selected to download
      if (downloadInformation != null && !downloadInformation.isEmpty()) {
        saveAssets(bulkAssetDownloadModel, requestModel, klassIdList, assetInstanceIdListGettingDownloaded);
      }
      else {
        return returnModel;
      }
      if (klassIdList.isEmpty()) {
        return returnModel;
      }
      
      IAssetInstanceDownloadLogsRequestModel assetInstanceDownloadLogsRequestModel = new AssetInstanceDownloadLogsRequestModel();
      assetInstanceDownloadLogsRequestModel.setComments(requestModel.getComments());
      assetInstanceDownloadLogsRequestModel.setDownloadId(UUID.randomUUID().toString());
      assetInstanceDownloadLogsRequestModel.setAssetDownloadInformationWithTIVModels(requestModel.getDownloadInformation());
      assetInstanceDownloadLogsRequestModel.setKlassIdList(klassIdList);
      assetInstanceDownloadLogsRequestModel.setAssetInstanceIdListGettingDownloaded(assetInstanceIdListGettingDownloaded);
      
      if (requestModel.getIsDownloadedFromInstance() && requestModel.getShouldTrackDownloads()) {
        IGetDownloadCountRequestModel downloadCountRequestModel = new GetDownloadCountRequestModel();
        IAssetDownloadInformationWithTIVModel downloadInfo = downloadInformation.get(0);
        downloadCountRequestModel.setAssetInstanceId(Long.toString(downloadInfo.getAssetInstanceId()));
        downloadCountRequestModel.setTimeRange(requestModel.getDownloadWithinTimeRange());
        insertDownloadLogsAndCount.execute(assetInstanceDownloadLogsRequestModel);
        IGetDownloadCountResponseModel downloadCountResponse = getDownloadCountForOverviewTabService.execute(downloadCountRequestModel);
        bulkAssetDownloadModel.setDownloadCountWithinRange(downloadCountResponse.getDownloadCountWithinRange());
        bulkAssetDownloadModel.setTotalDownloadCount(downloadCountResponse.getTotalDownloadCount());
      } 
      else {
        threadPoolTaskExecutorUtil.prepareRequestModel(assetInstanceDownloadLogsRequestModel, IInsertDownloadLogsAndCountTask.class.getName());
      }
    }
    catch (Exception e) {
      //TODO: need to handle exception
      returnModel.setFailure(failure);
    }
    
    returnModel.setSuccess(bulkAssetDownloadModel);
    return returnModel;
    
  }
  
  /**
   * Handle assets to download in NFS
   * 
   * @param modelToReturn
   * @param reqModel
   * @param klassIdList
   * @param assetInstanceIdListGettingDownloaded 
   * @return
   * @throws Exception
   */
  private void saveAssets(IAssetDownloadModel modelToReturn, IBulkAssetDownloadWithVariantsModel reqModel, 
      Set<String> klassIdList, List<Long> assetInstanceIdListGettingDownloaded) throws Exception
  {

    Map<String, String> assetsGettingDownloaded = new HashMap<String, String>();
    Set<String> tempFilePaths = new HashSet<String>();
    boolean isSingleDirectoryForAllAsset = false;
    boolean createSeparateFolderForEachAsset = reqModel.getShouldCreateSeparateFolders();  
    String assetFilesPath = CSProperties.instance().getString("nfs.file.path");
    String id = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.RANDOM.getPrefix());
    modelToReturn.setId(id);
    String path = assetFilesPath + DAMConstants.PATH_DELIMINATOR + id;
    File dir = new File(path);
    dir.mkdir();
    
    boolean checkIsSingleAsset = checkIsSingleAsset(reqModel, path);
    
    if (!checkIsSingleAsset  && createSeparateFolderForEachAsset) {
      for (IAssetDownloadInformationWithTIVModel assetToDownload : reqModel.getDownloadInformation()) {
        createSeparateFolderForEachAsset(path, tempFilePaths, assetToDownload, klassIdList,
            assetsGettingDownloaded, assetInstanceIdListGettingDownloaded);        
      }
      isSingleDirectoryForAllAsset = false;
    }
    else {
      for (IAssetDownloadInformationWithTIVModel assetToDownload : reqModel.getDownloadInformation()) {
        putAllAssetsInSingleFolder(path, tempFilePaths, assetToDownload, klassIdList, 
            assetsGettingDownloaded, assetInstanceIdListGettingDownloaded);
      }
      isSingleDirectoryForAllAsset = true;
    }
    if (checkIsSingleAsset) {
      addFilePathToSessionContext(reqModel.getDownloadInformation().get(0), path);
    }
    
    handleZipCreation(reqModel, path, dir, tempFilePaths, assetsGettingDownloaded, isSingleDirectoryForAllAsset);
  }
  
  /**
   * Handle zip creation for multiple images to download otherwise don't allow
   * to create zip
   * 
   * @param reqModel
   * @param id
   * @param dir
   * @param tempFilePaths
   * @param assetsGettingDownloaded
   * @param isSingleDirectoryForAllAsset
   * @throws FileNotFoundException
   * @throws IOException
   */
  private void handleZipCreation(IBulkAssetDownloadWithVariantsModel reqModel, String id, File dir,
      Set<String> tempFilePaths, Map<String, String> assetsGettingDownloaded,
      boolean isSingleDirectoryForAllAsset) throws FileNotFoundException, IOException
  {
    if (assetsGettingDownloaded.size() == 1 && !isSingleDirectoryForAllAsset) {
     
      String key = assetsGettingDownloaded.keySet().iterator().next();
      String fileNameWithExtension = assetsGettingDownloaded.get(key);
      try (InputStream is = new FileInputStream(key);
          BufferedInputStream input = new BufferedInputStream(is);
          BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(id + DAMConstants.PATH_DELIMINATOR + fileNameWithExtension))) {       
        springSessionContext.putInIdFilePathMap(id, id + DAMConstants.PATH_DELIMINATOR + fileNameWithExtension);
        byte[] buffer = new byte[8192];
        for (int length = 0; (length = input.read(buffer)) > 0;) {
          output.write(buffer, 0, length);
        }
      }
    }
    
    if (tempFilePaths.size() == 1) {
      tempFilePaths = new HashSet<String>();  
    }
    
    if (assetsGettingDownloaded.size() > 1) {
      extractDirectoryToZip(reqModel, id, dir, tempFilePaths);
    }
    
    for (String tempFilePath : tempFilePaths) {
      File fileToDelete = new File(tempFilePath);
      if (fileToDelete.isDirectory()) {
        DirectoryUtils.deleteDirectoryContents(fileToDelete);
      }
      fileToDelete.delete();
    }
  }
  
  /**
   * Extract directory content to zip
   * 
   * @param reqModel
   * @param id
   * @param dir
   * @param tempFilePaths
   * @throws IOException
   */
  private void extractDirectoryToZip(IBulkAssetDownloadWithVariantsModel reqModel, String id, File dir, Set<String> tempFilePaths) throws IOException
  {
    String downloadFileName = reqModel.getDownloadFileName();
    String folderName = (downloadFileName != null && !downloadFileName.isEmpty()) ? reqModel.getDownloadFileName(): "PXP Asset";
    folderName = assetInstanceUtils.removeRestrictedFileNameChars(folderName);
    String zipFilePath = id + DAMConstants.PATH_DELIMINATOR + folderName + ".zip";
    zipDirectory(dir, zipFilePath);
    springSessionContext.putInIdFilePathMap(id, zipFilePath);
  }
  
  /**
   * To create separate folders corresponding to each main asset and tiv klass
   * of respective renditions
   * 
   * @param id
   * @param tempFilePaths
   * @param assetToDownload
   * @param klassIdList
   * @param assetsGettingDownloaded
   * @param assetInstanceIdListGettingDownloaded 
   * @throws Exception
   */
  private void createSeparateFolderForEachAsset(String id, Set<String> tempFilePaths,
      IAssetDownloadInformationWithTIVModel assetToDownload, Set<String> klassIdList,
      Map<String, String> assetsGettingDownloaded, List<Long> assetInstanceIdListGettingDownloaded) throws Exception
  {
    String technicalVariantKlassFolderPath;
    Map<String, List<IAssetDownloadInformationModel>> tivDownloadInfo = assetToDownload.getTIVDownloadInformation();
    String mainAssetFileName = StringUtils.isNotEmpty(assetToDownload.getFileNameToDownload()) ? 
        assetToDownload.getFileNameToDownload() : assetToDownload.getAssetInstanceName();
        
    // create main asset folder using asset instance name
    String mainAssetPath = getUniquePath(id + DAMConstants.PATH_DELIMINATOR + mainAssetFileName);
    createDirectoryByPath(mainAssetPath);
    tempFilePaths.add(mainAssetPath);
    
    // Don't allow to download main asset for either canDownload is false or assetObjectKey is empty
    if (assetToDownload.getCanDownload()
        && StringUtils.isNotEmpty(assetToDownload.getAssetObjectKey())) {
      klassIdList.add(assetToDownload.getClassifierCode());
      assetInstanceIdListGettingDownloaded.add(assetToDownload.getAssetInstanceId());
      downloadAssetInstance(mainAssetPath, assetToDownload, assetsGettingDownloaded);
    }
    
    if (tivDownloadInfo != null && !tivDownloadInfo.isEmpty()) {
      for (String klassKey : tivDownloadInfo.keySet()) {
        List<IAssetDownloadInformationModel> technicalVariantInfoList = tivDownloadInfo.get(klassKey);
        if (technicalVariantInfoList != null && !technicalVariantInfoList.isEmpty()) {
          
          // Create tiv folders by the name of corresponding klasses respectively
          technicalVariantKlassFolderPath = getUniquePath(
              mainAssetPath + DAMConstants.PATH_DELIMINATOR + technicalVariantInfoList.get(0).getAssetKlassName());
          createDirectoryByPath(technicalVariantKlassFolderPath);
          
          for (IAssetDownloadInformationModel technicalVariantInfo : technicalVariantInfoList) {
            
            // Don't allow to download tiv for either canDownload is false or assetObjectKey is empty
            if (technicalVariantInfo.getCanDownload() && StringUtils.isNotEmpty(technicalVariantInfo.getAssetObjectKey())) {
              klassIdList.add(klassKey);
              assetInstanceIdListGettingDownloaded.add(technicalVariantInfo.getAssetInstanceId());
              tempFilePaths.add(technicalVariantKlassFolderPath);
              downloadAssetInstance(technicalVariantKlassFolderPath, technicalVariantInfo, assetsGettingDownloaded);
            }
          }
        }
      }
    }
  }
  
  /**
   * To put all assets and their renditions in one folder
   * 
   * @param id
   * @param tempFilePaths
   * @param assetToDownload
   * @param klassIdList
   * @param assetsGettingDownloaded
   * @param assetInstanceIdListGettingDownloaded 
   * @throws Exception
   */
  private void putAllAssetsInSingleFolder(String id, Set<String> tempFilePaths,
      IAssetDownloadInformationWithTIVModel assetToDownload, Set<String> klassIdList,
      Map<String, String> assetsGettingDownloaded, List<Long> assetInstanceIdListGettingDownloaded) throws Exception
  {
    Map<String, List<IAssetDownloadInformationModel>> tivDownloadInfo = assetToDownload.getTIVDownloadInformation();
    
    if (tivDownloadInfo != null && !tivDownloadInfo.isEmpty()) {
      for (String klassKey : tivDownloadInfo.keySet()) {
        List<IAssetDownloadInformationModel> technicalVariantInfoList = tivDownloadInfo.get(klassKey);
        for (IAssetDownloadInformationModel technicalVariantInfo : technicalVariantInfoList) {
          
          // Don't allow to download tiv for either canDownload is false or
            // assetObjectKey is empty
          if (technicalVariantInfo.getCanDownload() && StringUtils.isNotEmpty(technicalVariantInfo.getAssetObjectKey())) {
            klassIdList.add(klassKey);
            assetInstanceIdListGettingDownloaded.add(technicalVariantInfo.getAssetInstanceId());
            tempFilePaths.add(getUniquePathWithExtension(id, technicalVariantInfo.getFileNameToDownload(), technicalVariantInfo.getExtension()));
            downloadAssetInstance(id, technicalVariantInfo, assetsGettingDownloaded);
          }
        }
      }
    }
    
    // Don't allow to download main asset for either canDownload is false or assetObjectKey is empty
    if (assetToDownload.getCanDownload() && StringUtils.isNotEmpty(assetToDownload.getAssetObjectKey())) {
      klassIdList.add(assetToDownload.getClassifierCode());
      assetInstanceIdListGettingDownloaded.add(assetToDownload.getAssetInstanceId());
      tempFilePaths.add(getUniquePathWithExtension(id, assetToDownload.getFileNameToDownload(), assetToDownload.getExtension()));
      downloadAssetInstance(id, assetToDownload, assetsGettingDownloaded);
    }
  }
  
  /**
   * Generate directory for user defined file path
   * 
   * @param filePath
   */
  private void createDirectoryByPath(String filePath)
  {
    File dir = new File(filePath);
    if (!dir.exists())
      dir.mkdir();
  }
  
  /**
   * Download instance after filling the asset instance cover flow information
   * 
   * @param container
   * @param assetInstanceFolderPath
   * @param assetInstanceToDownloadInfo
   * @param assetsGettingDownloaded
   * @throws Exception
   */
  private void downloadAssetInstance(String assetInstanceFolderPath,
      IAssetDownloadInformationModel assetInstanceToDownloadInfo,
      Map<String, String> assetsGettingDownloaded) throws Exception
  {
    String assetObjectKey = assetInstanceToDownloadInfo.getAssetObjectKey();
    String fileName = assetInstanceToDownloadInfo.getFileNameToDownload();
    String container = assetInstanceToDownloadInfo.getContainer();
    ICoverFlowModel assetInstanceCoverFlow = new CoverFlowModel(assetObjectKey, fileName, container);
    downloadCoverFlowInstance(assetInstanceCoverFlow, assetInstanceFolderPath, assetInstanceToDownloadInfo.getExtension(), assetsGettingDownloaded);
  }
  
  /**
   * Get unique file path with extension
   * 
   * @param filepath
   * @param filename
   * @param extension
   * @return
   */
  private String getUniquePathWithExtension(String filepath, String filename, String extension)
  {
    String localFilePath = filepath + DAMConstants.PATH_DELIMINATOR  + filename + DAMConstants.DOT_SEPERATOR + extension;
    File dir = new File(localFilePath);
    int count = 1;
    while (dir.exists()) {
      localFilePath = filepath + DAMConstants.PATH_DELIMINATOR + filename + " (" + count + ")." + extension;
      dir = new File(localFilePath);
      count++;
    }
    return localFilePath;
  }
  
  /**
   * Get unique file path without extension
   * 
   * @param path
   * @return
   */
  private String getUniquePath(String path)
  {
    String filepath = path;
    File dir = new File(filepath);
    int count = 1;
    while (dir.exists()) {
      filepath = path + " (" + count + ")";
      dir = new File(filepath);
      count++;
    }
    return filepath;
  }
  
  /**
   * Check whether is single asset or not for download
   * @param path 
   * @param instance
   * @param tivInformation
   * @return
   */
  private boolean checkIsSingleAsset(IBulkAssetDownloadWithVariantsModel reqModel, String path)
  {
    List<IAssetDownloadInformationWithTIVModel> downloadInformation = reqModel.getDownloadInformation();
    int noOfInstances = downloadInformation.size();
    if (noOfInstances > 1) {
      return false;
    }
    IAssetDownloadInformationWithTIVModel instance = reqModel.getDownloadInformation().get(0);
    Map<String, List<IAssetDownloadInformationModel>> tivInformation = instance.getTIVDownloadInformation();
    // Check whether TIVs are present or not
    boolean checkIsTIVNotEmpty = tivInformation != null && !tivInformation.isEmpty();
    boolean downloadMainAsset = StringUtils.isNotEmpty(instance.getAssetObjectKey());
    int totalTIVs= 0;
    if (checkIsTIVNotEmpty) {
      for(List<IAssetDownloadInformationModel> tivInfo :tivInformation.values()) {
        totalTIVs += tivInfo.size();
      }
    }    
    checkIsTIVNotEmpty = checkIsTIVNotEmpty && totalTIVs > 0;
    
    // Check for condition where either single main asset without tivs or one tiv without main asset
    boolean checkForSingleAsset = (downloadMainAsset && !checkIsTIVNotEmpty)
        || (!downloadMainAsset && checkIsTIVNotEmpty && totalTIVs == 1);
    return checkForSingleAsset;
  }
  
  /**
   * Add file path in session context in the scenario of single asset download
   * @param instance
   * @param path
   */
  private void addFilePathToSessionContext(IAssetDownloadInformationWithTIVModel instance, String path)
  {
    Map<String, List<IAssetDownloadInformationModel>> tivInformation = instance.getTIVDownloadInformation();
    boolean downloadMainAsset = StringUtils.isNotEmpty(instance.getAssetObjectKey());

    if (downloadMainAsset) {
      springSessionContext.putInIdFilePathMap(path, path + DAMConstants.PATH_DELIMINATOR
          + instance.getFileNameToDownload() + DAMConstants.DOT_SEPERATOR + instance.getExtension());
    }
    else {
      IAssetDownloadInformationModel singleTIVAssetInstance = null;
      for (List<IAssetDownloadInformationModel> tivInfo : tivInformation.values()) {
        if (!tivInfo.isEmpty()) {
          singleTIVAssetInstance = tivInfo.get(0);
          break;
        }
      } 
      springSessionContext.putInIdFilePathMap(path, path + DAMConstants.PATH_DELIMINATOR
          + singleTIVAssetInstance.getFileNameToDownload() + DAMConstants.DOT_SEPERATOR + singleTIVAssetInstance.getExtension());
    }
  }
  
  /**
   * Zip creation
   * 
   * @param dir
   * @param zipDirName
   */
  private void zipDirectory(File dir, String zipDirName)
  {
    try {
      List<String> filesListInDir = populateFilesList(dir);
      FileOutputStream fos = new FileOutputStream(zipDirName);
      ZipOutputStream zos = new ZipOutputStream(fos);
      for (String filePath : filesListInDir) {
        ZipEntry ze = new ZipEntry(filePath.substring(dir.getAbsolutePath().length() + 1, filePath.length()));
        zos.putNextEntry(ze);
        FileInputStream fis = new FileInputStream(filePath);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = fis.read(buffer)) > 0) {
          zos.write(buffer, 0, len);
        }
        zos.closeEntry();
        fis.close();
      }
      zos.close();
      fos.close();
    }
    catch (IOException e) {
      RDBMSLogger.instance().exception(e);
    }
  }
  
  /**
   * Get all file path in the list
   * 
   * @param dir
   * @return
   * @throws IOException
   */
  private List<String> populateFilesList(File dir) throws IOException
  {
    List<String> filesListInDir = new ArrayList<String>();
    File[] files = dir.listFiles();
    for (File file : files) {
      if (file.isFile())
        filesListInDir.add(file.getAbsolutePath());
      else
        filesListInDir.addAll(populateFilesList(file));
    }
    return filesListInDir;
  }
  
  /**
   * Get asset from swift server
   * 
   * @param dataModel
   * @return
   * @throws Exception
   */
  public IGetAssetDetailsStrategyModel getAssetFromServer(IGetAssetDetailsRequestModel dataModel)
      throws Exception
  {
    dataModel.setAssetServerDetails(assetServerDetails);
    IGetAssetDetailsStrategyModel assetContentsModel = getAssetFromServerStrategy.execute(dataModel);
    Integer responseCode = assetContentsModel.getResponseCode();
    if (responseCode == 200) {
      return assetContentsModel;
    }
    else if (responseCode == 401) {
      IAssetServerDetailsModel assetServerDetailsFromStrategy = authenticateAssetServerStrategy.execute(null);
      assetServerDetails = new AssetServerDetailsModel(assetServerDetailsFromStrategy);
      dataModel.setAssetServerDetails(assetServerDetails);
      return getAssetFromServerStrategy.execute(dataModel);
    }
    return null;
  }
  
  /**
   * This method will download the asset instance by the assetObjectKey and
   * type, passed in @param1, from swift server and stores on the given path of
   * file system
   *
   * @param instance - object of ICoverFlowModel having instance details to be downloaded
   * @param filePath - the folder path on which instance should be downloaded
   * @param filename - name by which instance should be downloaded, if null it will be downloaded by its language specific name
   * @param assetsGettingDownloaded
   * @throws Exception
   */
  private void downloadCoverFlowInstance(ICoverFlowModel instance, String filePath,
      String extension, Map<String, String> assetsGettingDownloaded) throws Exception
  {
    createDirectoryByPath(filePath);
    InputStream is = null;
    String key = instance.getAssetObjectKey();
    String container = instance.getType();
    String filename = instance.getFileName();
    
    IGetAssetDetailsRequestModel dataModel = new GetAssetDetailsRequestModel();
    dataModel.setContainer(container);
    dataModel.setAssetKey(key);
    IGetAssetDetailsStrategyModel contents = getAssetFromServer(dataModel);
    BufferedInputStream input = null;
    BufferedOutputStream output = null;
    is = contents.getInputStream();
    try {
      String tempFilePath = getUniquePathWithExtension(filePath, filename, extension);
      input = new BufferedInputStream(is);
      assetsGettingDownloaded.put(tempFilePath, filename + DAMConstants.DOT_SEPERATOR + extension);
      output = new BufferedOutputStream(new FileOutputStream(tempFilePath));
      byte[] buffer = new byte[8192];
      for (int length = 0; (length = input.read(buffer)) > 0;) {
        output.write(buffer, 0, length);
      }
    }
    finally {
      if (output != null)
        try {
          output.close();
        }
        catch (IOException logOrIgnore) {
        }
      if (input != null)
        try {
          input.close();
        }
        catch (IOException logOrIgnore) {
        }
      if (is != null)
        try {
          is.close();
        }
        catch (IOException logOrIgnore) {
        }
    }
  }
}
