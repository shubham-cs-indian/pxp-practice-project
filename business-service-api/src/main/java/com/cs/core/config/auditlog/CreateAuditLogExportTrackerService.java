package com.cs.core.config.auditlog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.cs.config.businessapi.base.AbstractConfigImportExportService;
import com.cs.core.config.auditlog.ICreateAuditLogExportTrackerService;
import com.cs.core.config.auditlog.IGetGridAuditLogInfoService;
import com.cs.core.runtime.strategy.offboarding.IOffboardingInstancesToXLSXStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.model.auditlog.IAuditLogModel;
import com.cs.constants.CommonConstants;
import com.cs.constants.DAMConstants;
import com.cs.core.config.interactor.model.asset.AssetFileModel;
import com.cs.core.config.interactor.model.asset.IAssetFileModel;
import com.cs.core.config.interactor.model.asset.IAssetServerDetailsModel;
import com.cs.core.config.interactor.model.asset.IAssetUploadDataModel;
import com.cs.core.config.interactor.model.auditlog.IGetGridAuditLogRequestModel;
import com.cs.core.config.interactor.model.auditlog.IGetGridAuditLogResponseModel;
import com.cs.core.config.strategy.usecase.language.IGetAllAuditLogLabelStrategy;
import com.cs.core.exception.AuditLogsNotFoundException;
import com.cs.core.rdbms.auditlog.idao.IAuditLogExportDAO;
import com.cs.core.rdbms.auditlog.idto.IAuditLogExportDTO;
import com.cs.core.rdbms.auditlog.idto.IAuditLogExportDTO.Status;
import com.cs.core.runtime.interactor.constants.application.OffboardingConstants;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.core.runtime.interactor.model.klassinstance.IWriteInstancesToXLSXFileModel;
import com.cs.core.runtime.interactor.model.klassinstanceexport.WriteInstancesToXLSXFileModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.usecase.asset.IFetchAssetConfigurationDetails;
import com.cs.core.services.CSDAMServer;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.utils.dam.AssetUtils;

@Service
public class CreateAuditLogExportTrackerService extends AbstractConfigImportExportService<IGetGridAuditLogRequestModel, IModel>
    implements ICreateAuditLogExportTrackerService {
  
  @Autowired
  ISessionContext                               context;
  
  @Autowired
  protected IAssetServerDetailsModel            assetServerDetails;
  
  @Autowired
  protected String                              assetFilesPath;
  
  @Autowired
  protected IGetAllAuditLogLabelStrategy        getAllAuditLogLabelStrategy;
  
  @Value("${auditLogProperties.auditLogExportBatch}")
  protected long                                batchSize;
  
  @Value("${auditLogProperties.exportFolderPath}")
  String                                        exportFolderPath;
  
  @Autowired
  protected IGetGridAuditLogInfoService         getGridAuditLogInfoService;
  
  @Autowired
  protected IOffboardingInstancesToXLSXStrategy offboardingInstancesToXLSXStrategy;
  
  @Autowired
  protected IFetchAssetConfigurationDetails     fetchAssetConfigurationDetails;
  
  @Override
  protected IModel executeInternal(IGetGridAuditLogRequestModel requestModel) throws Exception
  {
    IAuditLogExportDAO auditLogExportDAO = RDBMSUtils.newUserSessionDAO().newAuditLogExportDAO();
    IAuditLogExportDTO auditLogExportDTO = createAuditLogExportEntry(auditLogExportDAO);
    
    prepareXLSXFileandUploadToServer(requestModel, auditLogExportDAO, auditLogExportDTO);
    
    return new VoidModel();
  }
  
  private IModel prepareXLSXFileandUploadToServer(IGetGridAuditLogRequestModel requestModel,
      IAuditLogExportDAO auditLogExportDAO, IAuditLogExportDTO auditLogExportDTO)
      throws RDBMSException
  {
    int status;
    try {
      fetchAuditLogsAndPrepareToWriteXLSXSheet(auditLogExportDTO, requestModel);
      status = Status.COMPLETED.getCode();
    }
    catch (AuditLogsNotFoundException e) {
      auditLogExportDAO.deleteAuditLogExportTracker("'"+ auditLogExportDTO.getAssetId()+"'");
      return new VoidModel();
    }
    catch (Exception e) {
      status = Status.FAILED.getCode();
    }
    
    deleteFile(auditLogExportDTO.getFileName());
    updateAuditLogExportracker(auditLogExportDTO,auditLogExportDAO, status);
    
    return new VoidModel();
  }
  
  private void updateAuditLogExportracker(IAuditLogExportDTO auditLogExportDTO,
      IAuditLogExportDAO auditLogExportDAO, int status) throws RDBMSException
  {
    IAuditLogExportDTO auditLogExportDTOForUpdate = auditLogExportDAO.newAuditLogExportDTOBuilder()
                                                                     .assetId(auditLogExportDTO.getAssetId())
                                                                     .status(status)
                                                                     .setEndTime(System.currentTimeMillis())
                                                                     .build();
    auditLogExportDAO.updateAuditLogExportTracker(auditLogExportDTOForUpdate);
  }

  private void deleteFile(String fileName)
  {
    File fileToBeRemoved = new File(
        exportFolderPath + "\\" + fileName + OffboardingConstants.XLSX_FILE_EXTENSION);
    if (fileToBeRemoved.exists()) {
      fileToBeRemoved.delete();
    }
  }
  
  private void fetchAuditLogsAndPrepareToWriteXLSXSheet(IAuditLogExportDTO auditLogExportDTO,
      IGetGridAuditLogRequestModel requestModel) throws Exception
  {
    Map<String, Object> uiTranslation = getAllAuditLogLabelStrategy.execute(null).getAdditionalProperties();
    IWriteInstancesToXLSXFileModel exportXLSXDataModel = null;
    
    Long noOfRecords = 0l;
    Long size = 0l;
    int count = 0;
    
    do {
      requestModel.setFrom(size);
      size += batchSize;
      requestModel.setSize(size);
      IGetGridAuditLogResponseModel auditLogData = getGridAuditLogInfoService.execute(requestModel);
      noOfRecords = auditLogData.getCount();
      if (noOfRecords == 0) {
        throw new AuditLogsNotFoundException();
      }
      else {
        List<IAuditLogModel> auditLogList = auditLogData.getAuditLogList();
        exportXLSXDataModel = createDataToWriteInSheets(auditLogExportDTO, auditLogList, uiTranslation, count);
        offboardingInstancesToXLSXStrategy.execute(exportXLSXDataModel);
        count = +1;
      }
      
    }
    while (size < noOfRecords);
    
    uploadFileToServer(exportXLSXDataModel, auditLogExportDTO);
  }
  
  private void uploadFileToServer(IWriteInstancesToXLSXFileModel exportXLSXDataModel,
      IAuditLogExportDTO auditLogExportDTO) throws Exception
  {
    IAssetFileModel assetFileModel = getAssetFileModel(exportXLSXDataModel.getfilePath());
    uploadTheDocument(assetFileModel, auditLogExportDTO.getAssetId());
  }
  
  public void uploadTheDocument(IAssetFileModel assetFileModel, String assetKey)
      throws Exception
  {
    Map<String, Object> uploadMap = new HashMap<>();
    uploadMap.put(IAssetUploadDataModel.STORAGE_URL, assetServerDetails.getStorageURL());
    uploadMap.put(IAssetUploadDataModel.CONTAINER, DAMConstants.SWIFT_CONTAINER_DOCUMENT);
    uploadMap.put(IAssetUploadDataModel.ASSET_BYTES, assetFileModel.getBytes());
    uploadMap.put("assetObjectKey", assetKey);

    Map<String, String> assetDataMap = new HashMap<>();
    assetDataMap.put(DAMConstants.REQUEST_HEADER_AUTH_TOKEN, assetServerDetails.getAuthToken());
    assetDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_NAME, assetFileModel.getName() + OffboardingConstants.XLSX_FILE_EXTENSION);
    assetDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_FORMAT, OffboardingConstants.XLSX_FILE_EXTENSION);
    String originalContentType = DAMConstants.CONTENT_TYPE_APPLICATION + OffboardingConstants.XLSX_FILE_EXTENSION;
    assetDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_CONTENT_TYPE, originalContentType);
    assetDataMap.put(DAMConstants.REQUEST_HEADER_CONTENT_TYPE, originalContentType);
    assetDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_TYPE, DAMConstants.TYPE_ORIGINAL);
    
    uploadMap.put("assetData", assetDataMap);
    CSDAMServer.instance().uploadAsset(uploadMap);
    AssetUtils.deleteFileAndDirectory(assetFileModel.getPath());
  }

  public IAssetFileModel getAssetFileModel(String filePath)
      throws FileNotFoundException, IOException
  {
    IAssetFileModel assetFileModel = null;
    if (filePath != null) {
      File file = new File(filePath);
      String absoluteFilePath = file.getAbsolutePath();
      Path path = Paths.get(absoluteFilePath);
      byte[] bytes = Files.readAllBytes(path);
      
      assetFileModel = new AssetFileModel(CommonConstants.SWIFT_CONTAINER_DOCUMENT, bytes,
          file.getName(), OffboardingConstants.XLSX_FILE_EXTENSION, absoluteFilePath, null, null,
          null, null, null, null, null);
      
      File writeAssetFileOnServer = writeAssetFileOnServer(file.getName());
      String sourcePath = writeAssetFileOnServer.getAbsolutePath();
      FileOutputStream fos = new FileOutputStream(writeAssetFileOnServer);
      fos.write(bytes);
      fos.close();
      assetFileModel.setPath(sourcePath);
    }
    return assetFileModel;
  }
  
  public File writeAssetFileOnServer(String originalFilename)
  {
    String directoryPath = null;
    
    if (!assetFilesPath.isEmpty()) {
      directoryPath = assetFilesPath + "/" + UUID.randomUUID().toString();
    } else {
      directoryPath = UUID.randomUUID().toString();
    }
    
    File directory = new File(directoryPath);
    directory.mkdirs();
    File file = new File(directory, originalFilename);
    
    return file;
  }
  
  private IAuditLogExportDTO createAuditLogExportEntry(IAuditLogExportDAO auditLogExportDAO)
      throws Exception
  {
    String assetId = UUID.randomUUID().toString();
    String userName = context.getUserName();
    String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_" + userName;
    
    IAuditLogExportDTO auditLogExportDTO = auditLogExportDAO.newAuditLogExportDTOBuilder()
                                                            .assetId(assetId)
                                                            .fileName(fileName)
                                                            .userName(userName)
                                                            .startTime(System.currentTimeMillis())
                                                            .status(Status.IN_PROGRESS.getCode())
                                                            .build();
    auditLogExportDAO.createAuditLogExportTracker(auditLogExportDTO);
    return auditLogExportDTO;
  }
  
  private IWriteInstancesToXLSXFileModel createDataToWriteInSheets(
      IAuditLogExportDTO auditLogExportDTO, List<IAuditLogModel> auditLogList,
      Map<String, Object> uiTranslation, int sheetCount) throws Exception
  {
    IWriteInstancesToXLSXFileModel exportXLSXDataModel = new WriteInstancesToXLSXFileModel();
    String workbookName = auditLogExportDTO.getFileName();
    LinkedHashSet<String> headerList = getHeaderList(uiTranslation);
    String[] headerToWrite = headerList.toArray(new String[headerList.size()]);
    List<String[]> dataToWrite = prepareMapToWriteIntoExcel(auditLogList, uiTranslation);
    getOrCreateDirectory();
    exportXLSXDataModel = setXSLXFileModelProperties(headerToWrite, dataToWrite, workbookName, sheetCount);
    return exportXLSXDataModel;
  }
  
  private void getOrCreateDirectory()
  {
    File directory = new File(exportFolderPath);
    if (!directory.exists()) {
      directory.mkdir();
    }
  }
  
  protected LinkedHashSet<String> getHeaderList(Map<String, Object> uiTranslation)
  {
    LinkedHashSet<String> headerList = new LinkedHashSet<String>();
    headerList.add(uiTranslation.get(CommonConstants.AUDIT_LOG_ACIVITY_NUMBER).toString());
    headerList.add(uiTranslation.get(CommonConstants.AUDIT_LOG_USERNAME).toString());
    headerList.add(uiTranslation.get(CommonConstants.AUDIT_LOG_ACTIVITY).toString());
    headerList.add(uiTranslation.get(CommonConstants.AUDIT_LOG_ENTITY).toString());
    headerList.add(uiTranslation.get(CommonConstants.AUDIT_LOG_ELEMENT).toString());
    headerList.add(uiTranslation.get(CommonConstants.AUDIT_LOG_TYPE).toString());
    headerList.add(uiTranslation.get(CommonConstants.AUDIT_LOG_ELEMENT_NAME).toString());
    headerList.add(uiTranslation.get(CommonConstants.AUDIT_LOG_CODE).toString());
    headerList.add(uiTranslation.get(CommonConstants.AUDIT_LOG_TIMESTAMP).toString());
    headerList.add(uiTranslation.get(CommonConstants.AUDIT_LOG_IP_ADDRESS).toString());
    return headerList;
  }
  
  private List<String[]> prepareMapToWriteIntoExcel(List<IAuditLogModel> auditLogList,
      Map<String, Object> uiTranslation) throws Exception
  {
    List<String[]> dataToWrite = new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    for (IAuditLogModel auditLog : auditLogList) {
      LinkedList<String> logList = new LinkedList<>();
      logList.add(auditLog.getActivityId());
      logList.add(auditLog.getUserName());
      String activity = auditLog.getActivity();
      logList.add(validateAndGet(activity, uiTranslation));
      String entityType = auditLog.getEntityType();
      logList.add(validateAndGet(entityType, uiTranslation));
      String element = auditLog.getElement();
      logList.add(validateAndGet(element, uiTranslation));
      String type = auditLog.getElementType();
      logList.add(validateAndGet(type, uiTranslation));
      logList.add(auditLog.getElementName());
      logList.add(auditLog.getElementCode());
      logList.add(sdf.format(auditLog.getDate()));
      logList.add(auditLog.getIpAddress());
      
      String[] instanceDataToWrite = logList.toArray(new String[logList.size()]);
      dataToWrite.add(instanceDataToWrite);
    }
    return dataToWrite;
  }
  
  private String validateAndGet(String item, Map<String, Object> uiTranslation)
  {
    return (item != null && !item.isEmpty()) ? (uiTranslation.get(item) != null ? uiTranslation.get(item).toString() : "") : "";
  }
  
  private IWriteInstancesToXLSXFileModel setXSLXFileModelProperties(String[] headerToWrite,
      List<String[]> dataToWrite, String workBookName, int sheetCount)
  {
    IWriteInstancesToXLSXFileModel exportXLSXDataModel = new WriteInstancesToXLSXFileModel();
    String filePath = exportFolderPath + "//" + workBookName + OffboardingConstants.XLSX_FILE_EXTENSION;
    exportXLSXDataModel.setSheetName("AL_Sheet_" + sheetCount);
    exportXLSXDataModel.setfileName(workBookName);
    exportXLSXDataModel.setFileInstanceIdForExport(String.valueOf(System.currentTimeMillis()));
    exportXLSXDataModel.setHeaderRowNumber(1);
    exportXLSXDataModel.setfilePath(filePath);
    exportXLSXDataModel.setDataRowNumber(2);
    exportXLSXDataModel.setHeaderToWrite(headerToWrite);
    exportXLSXDataModel.setDataToWrite(dataToWrite);
    return exportXLSXDataModel;
  }
}
