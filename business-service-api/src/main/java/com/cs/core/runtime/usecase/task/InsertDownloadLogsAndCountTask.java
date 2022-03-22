package com.cs.core.runtime.usecase.task;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.rdbms.downloadtracker.idao.IDownloadTrackerDAO;
import com.cs.core.rdbms.driver.RDBMSAbstractDriver;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetDownloadInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetDownloadInformationWithTIVModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.dam.config.interactor.model.downloadtracker.IAssetInstanceDownloadLogsRequestModel;
import com.cs.dam.config.interactor.model.downloadtracker.IDownloadTrackerConfigurationResponseModel;
import com.cs.dam.config.strategy.usecase.downloadtracker.IGetDownloadTrackerConfigurationStrategy;
import com.cs.dam.rdbms.downloadtracker.dao.DownloadTrackerDAO;
import com.cs.dam.rdbms.downloadtracker.dto.DownloadTrackerDTO;
import com.cs.dam.rdbms.downloadtracker.idto.IDownloadTrackerDTO;

@Service
public class InsertDownloadLogsAndCountTask
    extends AbstractRuntimeService<IAssetInstanceDownloadLogsRequestModel, IIdParameterModel>
    implements IInsertDownloadLogsAndCountTask {
  


  @Autowired
  protected IGetDownloadTrackerConfigurationStrategy getDownloadTrackerConfigurationStrategy;
  
  @Autowired
  protected TransactionThreadData                    transactionThreadData;
  
  @Autowired
  RDBMSComponentUtils                                rdbmsComponentUtils;
  
  
  private static final String                        EMBEDDED_TYPE = "embeddedtype";
  private static final String                        PARENT_IID    = "parentiid";
  
  protected IIdParameterModel executeInternal(IAssetInstanceDownloadLogsRequestModel requestModel) throws Exception
  {
    
    try {
      //Get Download Tracker Info from Orient
      Map<String, Boolean> klassIdVsIsDownloadTrackerEnabledMap = new HashMap<>();
      Map<String, String> klassIdVsKlassCode = new HashMap<>();
      getKlassInfo(requestModel.getKlassIdList(), klassIdVsIsDownloadTrackerEnabledMap, klassIdVsKlassCode);
      
      //Postgres Strategy call to maintain download details for log purpose.
      List<IDownloadTrackerDTO> downloadTrackerDTOList = new ArrayList<>();
      prepareDownloadLogModel(requestModel, downloadTrackerDTOList, klassIdVsIsDownloadTrackerEnabledMap, klassIdVsKlassCode);
      IDownloadTrackerDAO downloadTrackerDAO = DownloadTrackerDAO.getInstance();
      Set<Long> assetIIDs = downloadTrackerDAO.insertDownloadLogs(downloadTrackerDTOList.toArray(new IDownloadTrackerDTO[] {}));
      
      // Elastic sync update call
      for (Long assetIID : assetIIDs) {
        rdbmsComponentUtils.getLocaleCatlogDAO().postUsecaseUpdate(assetIID, IEventDTO.EventType.ELASTIC_UPDATE);
      }
    }
    catch (Exception e) {
      //TODO : log exception
    }
    return null;
  }
  
  
  /***
   * This method will fetch isDownloadTrackerEnabled, klass code information
   * from config and populate the @param2 map by klassIds and respective value
   * of download tracker and @param3 by klassIds and respective code.
   * 
   * @param klassIds
   *          Model having masterCoverflowList and technicalCoverflowList to
   *          iterate over them to get klassIds
   * @param klassIdVsIsDownloadTrackerEnabledMap
   *          Map to be populated by klassIds and their respective value of
   *          downloadTracker
   * @param klassIdVsKlassCode
   *          Map to be populated by klassIds and their code.
   * @throws Exception
   */
  
  @SuppressWarnings("unchecked")
  private void getKlassInfo(Set<String> klassIds, Map<String, Boolean> klassIdVsIsDownloadTrackerEnabledMap,
      Map<String, String> klassIdVsKlassCode) throws Exception
  {
    IIdsListParameterModel idListModel = new IdsListParameterModel();
    idListModel.getIds().addAll(klassIds);
    
    IListModel<IDownloadTrackerConfigurationResponseModel> configResponseModel = getDownloadTrackerConfigurationStrategy.execute(idListModel);
    List<IDownloadTrackerConfigurationResponseModel> configResponseList = 
        (List<IDownloadTrackerConfigurationResponseModel>) configResponseModel.getList();
    
    configResponseList.forEach(configResposne -> {
      klassIdVsIsDownloadTrackerEnabledMap.put(configResposne.getKlassId(), configResposne.getIsDownloadTrackerEnabled());
      klassIdVsKlassCode.put(configResposne.getKlassId(), configResposne.getKlassCode());
    });
  }

/**
 * Prepare model for Postgres Strategy call (to maintain download details)
 * @param requestModel
 * @param assetInstanceDownloadLogsModel
 * @param klassIdVsKlassInfoMap
 * @param klassIdVsKlassCode
 * @throws RDBMSException 
 */
  private void prepareDownloadLogModel(IAssetInstanceDownloadLogsRequestModel requestModel,
      List<IDownloadTrackerDTO> assetInstanceDownloadLogsModel,
      Map<String, Boolean> klassIdVsKlassInfoMap, Map<String, String> klassIdVsKlassCode) throws RDBMSException
  {
    long currentTimeMillis = System.currentTimeMillis();
    TransactionData transactionData = transactionThreadData.getTransactionData();
    List<IAssetDownloadInformationWithTIVModel> assetDownloadInformationWithTIVModels = requestModel.getAssetDownloadInformationWithTIVModels();
    String userId = transactionData.getUserId();
    String downloadId = requestModel.getDownloadId();
    String comment = truncateFieldValue(requestModel.getComments(), 999);
    String userName = truncateFieldValue(transactionData.getUserName(), 250);
    
    List<Long> assetInstanceIdListGettingDownloaded = requestModel.getAssetInstanceIdListGettingDownloaded();
    Map<String, Object> parentIIDMap  = getParentIIDIfRenditionDownloaded(requestModel);
    
    Long parentIID = (Long) parentIIDMap.get(PARENT_IID);
    if (parentIID != null && parentIID != 0) {
      if (assetInstanceIdListGettingDownloaded.contains(assetDownloadInformationWithTIVModels.get(0).getAssetInstanceId())) {
        handleTIVDownloadInformation(assetDownloadInformationWithTIVModels, assetInstanceDownloadLogsModel, klassIdVsKlassInfoMap,
            klassIdVsKlassCode, userId, downloadId, comment, userName, currentTimeMillis, transactionData.getDataLanguage(), parentIID);
      }
    }
    else {
      for (IAssetDownloadInformationWithTIVModel masterAssetDownloadInfoModel : assetDownloadInformationWithTIVModels) {
        if (assetInstanceIdListGettingDownloaded.contains(masterAssetDownloadInfoModel.getAssetInstanceId())) {
          String masterAssetClassId = masterAssetDownloadInfoModel.getClassifierCode();
          fillMasterAssetInformationForDownloadTracker(assetInstanceDownloadLogsModel,
              klassIdVsKlassInfoMap, klassIdVsKlassCode, currentTimeMillis, userId, downloadId, comment,
              userName, masterAssetDownloadInfoModel, masterAssetClassId);
        }      

        Map<String, List<IAssetDownloadInformationModel>> tivDownloadInformation = masterAssetDownloadInfoModel.getTIVDownloadInformation();
        if (tivDownloadInformation!= null && !tivDownloadInformation.isEmpty()) {
          
          for (String key : tivDownloadInformation.keySet()) {
            List<IAssetDownloadInformationModel> technicalVariantInfoList = tivDownloadInformation.get(key);
            for (IAssetDownloadInformationModel technicalVariantInfo : technicalVariantInfoList) {
              if (assetInstanceIdListGettingDownloaded.contains(technicalVariantInfo.getAssetInstanceId())) {
                fillRenditionInformationForDownloadTracker(assetInstanceDownloadLogsModel,
                    klassIdVsKlassInfoMap, klassIdVsKlassCode, currentTimeMillis, userId, downloadId,
                    comment, userName, masterAssetDownloadInfoModel, key, technicalVariantInfo);
              }            
            }
          }
        }
      }
    }
      
  }
  
  /**
   * Handle TIV download log information
   * @param assetDownloadInformationWithTIVModels
   * @param assetInstanceDownloadLogsModel
   * @param klassIdVsKlassInfoMap
   * @param klassIdVsKlassCode
   * @param userId
   * @param downloadId
   * @param comment
   * @param userName
   * @param currentTimeMillis
   * @param languageCode
   * @param parentIID
   * @throws RDBMSException
   */
  @SuppressWarnings("unchecked")
  private void handleTIVDownloadInformation(List<IAssetDownloadInformationWithTIVModel> assetDownloadInformationWithTIVModels,
      List<IDownloadTrackerDTO> assetInstanceDownloadLogsModel, Map<String, Boolean> klassIdVsKlassInfoMap,
      Map<String, String> klassIdVsKlassCode, String userId, String downloadId, String comment, String userName, long currentTimeMillis,
      String languageCode, Long parentIID) throws RDBMSException
  {
    IAssetDownloadInformationWithTIVModel model = assetDownloadInformationWithTIVModels.get(0);
    String [] masterClassifierCode = {""};
    Map<String, Object> entityextension = new LinkedHashMap<String, Object>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      String query = "SELECT entityextension, classifiercode FROM pxp.baseentity  INNER JOIN pxp.classifierconfig ON pxp.baseentity.classifieriid =  pxp.classifierconfig.classifieriid "
          + "where pxp.baseentity.baseentityiid = '"+ parentIID + "'";
      PreparedStatement statement = currentConn.prepareStatement(query);
      RDBMSAbstractDriver driver = currentConn.getDriver();
      IResultSetParser result = driver.getResultSetParser(statement.executeQuery());
      while (result.next()) {
        masterClassifierCode[0] = result.getString("classifiercode");
        Map<String, Object> convertValue = new LinkedHashMap<String, Object>();;
        try {
          convertValue = ObjectMapperUtil.readValue(result.getStringFromJSON("entityextension"), LinkedHashMap.class);
        }
        catch (Exception e) {
          RDBMSLogger.instance().exception(e);
        }
        entityextension.putAll(convertValue);
      }
    }); 
    
    String masterFileName = (String) entityextension.get("fileName");
    String tivClassifierCode = model.getClassifierCode();
    String masterAssetInstanceName = getAssetName(languageCode, parentIID);
    
    String renditionFileName = model.getAssetFileName()+"."+ model.getExtension();
    assetInstanceDownloadLogsModel.add(new DownloadTrackerDTO(parentIID,
        masterAssetInstanceName, masterFileName, masterClassifierCode[0], masterClassifierCode[0],
        Long.toString(model.getAssetInstanceId()), model.getAssetInstanceName(), renditionFileName,
        tivClassifierCode , klassIdVsKlassCode.get(tivClassifierCode), userId, currentTimeMillis,
        comment, downloadId, userName));
  }

  /**
   * Get asset name of name attribute.
   * @param languageCode
   * @param parentIID
   * @return
   * @throws RDBMSException
   */
  private String getAssetName( String languageCode, Long parentIID) throws RDBMSException
  {
    String[] instanceName = {""};
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      String query = "SELECT value from pxp.valuerecord WHERE entityiid = '" + parentIID + "'" + " and localeid = '" + languageCode + "'";
      PreparedStatement statement = currentConn.prepareStatement(query);
      RDBMSAbstractDriver driver = currentConn.getDriver();
      IResultSetParser result = driver.getResultSetParser(statement.executeQuery());
      while (result.next()) {
        instanceName[0] = result.getString("value");
      }
    });
    return instanceName[0];
  }

  /**
   * Get parent iid in case of TIV download.
   * @param requestModel
   * @return
   * @throws RDBMSException
   */
  private Map<String, Object> getParentIIDIfRenditionDownloaded(
      IAssetInstanceDownloadLogsRequestModel requestModel) throws RDBMSException
  {
    Map<String, Object> responseMap = new HashMap<String, Object>();
    if (requestModel.getAssetInstanceIdListGettingDownloaded().size() == 1) {
      RDBMSConnectionManager.instance()
          .runTransaction((RDBMSConnection currentConn) -> {
            String query = "SELECT embeddedtype, parentiid FROM pxp.baseentity where baseentityiid ='"
                + requestModel.getAssetInstanceIdListGettingDownloaded().get(0) + "'";
            PreparedStatement statement = currentConn.prepareStatement(query);
            RDBMSAbstractDriver driver = currentConn.getDriver();
            IResultSetParser result = driver.getResultSetParser(statement.executeQuery());
            while (result.next()) {
              int embeddedType = result.getInt(EMBEDDED_TYPE);
              if (embeddedType > 0) {
                responseMap.put(PARENT_IID, result.getLong(PARENT_IID));
              }
            }
          });
    }
    
    return responseMap;
  }

  /**
   * Fill main asset information for download tracker
   * @param assetInstanceDownloadLogsModel
   * @param klassIdVsKlassInfoMap
   * @param klassIdVsKlassCode
   * @param currentTimeMillis
   * @param userId
   * @param downloadId
   * @param comment
   * @param userName
   * @param masterAssetDownloadInfoModel
   * @param masterAssetClassId
   */
  private void fillMasterAssetInformationForDownloadTracker(List<IDownloadTrackerDTO> assetInstanceDownloadLogsModel,
      Map<String, Boolean> klassIdVsKlassInfoMap, Map<String, String> klassIdVsKlassCode,
      long currentTimeMillis, String userId, String downloadId, String comment, String userName,
      IAssetDownloadInformationWithTIVModel masterAssetDownloadInfoModel, String masterAssetClassId)
  {
    if (klassIdVsKlassInfoMap.get(masterAssetClassId)) {
      String masterAssetInstanceName = masterAssetDownloadInfoModel.getAssetInstanceName();
      String masterAssetFileName = masterAssetDownloadInfoModel.getAssetFileName() + "." + masterAssetDownloadInfoModel.getExtension();
      assetInstanceDownloadLogsModel.add(new DownloadTrackerDTO(masterAssetDownloadInfoModel.getAssetInstanceId(),
              masterAssetInstanceName, masterAssetFileName, masterAssetClassId,
              klassIdVsKlassCode.get(masterAssetClassId), String.valueOf('0'), null, null, null,
              null, userId, currentTimeMillis, comment, downloadId, userName));
    }
  }

  /**
   * @param assetInstanceDownloadLogsModel
   * @param klassIdVsKlassInfoMap
   * @param klassIdVsKlassCode
   * @param currentTimeMillis
   * @param userId
   * @param downloadId
   * @param comment
   * @param userName
   * @param masterAssetDownloadInfoModel
   * @param classifierCode
   * @param technicalVariantInfo
   */
  private void fillRenditionInformationForDownloadTracker(
      List<IDownloadTrackerDTO> assetInstanceDownloadLogsModel,
      Map<String, Boolean> klassIdVsKlassInfoMap, Map<String, String> klassIdVsKlassCode,
      long currentTimeMillis, String userId, String downloadId, String comment, String userName,
      IAssetDownloadInformationWithTIVModel masterAssetDownloadInfoModel, String classifierCode,
      IAssetDownloadInformationModel technicalVariantInfo)
  {
    if (klassIdVsKlassInfoMap.get(classifierCode)) {
      
      String masterAssetInstanceName = masterAssetDownloadInfoModel.getAssetInstanceName ();
      String masterAssetFileName = masterAssetDownloadInfoModel.getAssetFileName() + "." + masterAssetDownloadInfoModel.getExtension();
      
      String renditionAssetInstanceName = technicalVariantInfo.getAssetInstanceName();
      String renditionAssetFileName = technicalVariantInfo.getAssetFileName() + "." + technicalVariantInfo.getExtension();
      
      String parentClassId = masterAssetDownloadInfoModel.getClassifierCode();
      String assetInstanceClassCode = StringUtils.isEmpty(klassIdVsKlassCode.get(parentClassId)) ? parentClassId : klassIdVsKlassCode.get(parentClassId);
            
      assetInstanceDownloadLogsModel.add(new DownloadTrackerDTO(masterAssetDownloadInfoModel.getAssetInstanceId(),
          masterAssetInstanceName, masterAssetFileName, parentClassId, assetInstanceClassCode,
          Long.toString(technicalVariantInfo.getAssetInstanceId()), renditionAssetInstanceName, renditionAssetFileName,
          classifierCode, klassIdVsKlassCode.get(classifierCode), userId, currentTimeMillis,
          comment, downloadId, userName));
    }
  }
  
  /**
   * Truncate string upto allowed maximum length 
   * 
   * @param fieldName
   * @param maxLengthAllowed
   * @return
   */
  private String truncateFieldValue(String fieldName, int maxLengthAllowed)
  {
    if(fieldName!=null && fieldName.length()>maxLengthAllowed){
      fieldName = fieldName.substring(0,maxLengthAllowed);
    }
    return fieldName;
  }
}
