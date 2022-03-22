package com.cs.core.bgprocess.services.asset;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.services.data.AbstractBaseEntityProcessing;
import com.cs.core.data.Text;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.driver.RDBMSAbstractDriver;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.dam.runtime.assetinstance.linksharing.IDeleteAssetFromSharedSwiftServerService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * This task will purge unused assets from swift server based on assetstobepurged table.
 * @author rahul.sehrawat
 *
 */
public class AssetPurgeFromSwiftServer extends AbstractBaseEntityProcessing {
  
  static final String GET_ALL_ASSET_KEYS_TO_PURGE     = "select DISTINCT assetObjectKey, thumbKey, previewImageKey, type from pxp.assetstobepurged "
      + "order by assetObjectKey";
  static final String GET_ALL_REVISION_RECORDS        = "select assetobjectkey from pxp.objectrevision where assetobjectkey = ? ";
  static final String GET_ALL_ARCHIVE_RECORDS         = "select assetobjectkey from pxp.baseentityarchive where assetobjectkey = ? ";
  static final String GET_ALL_BASE_ENTITY_RECORDS     = "select baseentityiid from pxp.baseentity where ismerged != true and entityextension ->> 'assetObjectKey' = ? ";
  static final String DELETE_FROM_ASSET_PURGING_TABLE = "delete from pxp.assetstobepurged where assetobjectkey = ? ";
  
  private int totalNumberOfAssets;
  private int numberOfAssetsDeleted;
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {   
    totalNumberOfAssets = 0;
    numberOfAssetsDeleted = 0;
    super.initBeforeStart(initialProcessData, userSession);
  }
  
  @Override
  protected void runBaseEntityBatch(Set<Long> batchIIDs)
      throws RDBMSException, CSFormatException, CSInitializationException, PluginException,
      JsonProcessingException, JsonParseException, JsonMappingException, IOException, Exception
  {
    Map<String, List<String>> assetToPurgeMap = getAllAssetIdsToPurge();
    Set<String> assetObjectKeys = assetToPurgeMap.keySet();
    for (String assetObjectKey : assetObjectKeys) {
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
        PreparedStatement baseEntityQuery = currentConn.prepareStatement(GET_ALL_BASE_ENTITY_RECORDS);
        baseEntityQuery.setString(1, assetObjectKey);
        RDBMSAbstractDriver driver = currentConn.getDriver();
        IResultSetParser baseEntityResult = driver.getResultSetParser(baseEntityQuery.executeQuery());
        //Check entry present in pxp.baseentity table.
        if (!baseEntityResult.next()) {
          PreparedStatement revisionQuery = currentConn.prepareStatement(GET_ALL_REVISION_RECORDS);
          revisionQuery.setString(1, assetObjectKey);
          IResultSetParser revisionResult = driver.getResultSetParser(revisionQuery.executeQuery());
          //Check entry present in pxp.objectrevision table.
          if (!revisionResult.next()) {
            PreparedStatement archiveQuery = currentConn.prepareStatement(GET_ALL_ARCHIVE_RECORDS);
            archiveQuery.setString(1, assetObjectKey);
            IResultSetParser archiveResult = driver.getResultSetParser(archiveQuery.executeQuery());
            //Check entry present in pxp.baseentityarchive table.
            if (!archiveResult.next()) {
              List<String> assetInfoList = assetToPurgeMap.get(assetObjectKey);
              String container = assetInfoList.get(0);
              //Remove container value from list.
              assetInfoList.remove(0);
              numberOfAssetsDeleted ++;
              try {
                //Delete entry from pxp.assetstobepurged table.
                deleteEntryFromAssetToBePurgedTable(currentConn, assetObjectKey);
              }
              catch (Exception e) {
              numberOfAssetsDeleted --;
              jobData.getLog().error("asset purge has been failed: id: %s container: %s Exception: ", assetObjectKey, container, Text.join(",", e.getMessage()));
              return;
              }
              //Purge main asset.
              purgeAssetFromServer(assetObjectKey, container);
              for (String idToDelete : assetInfoList) {
                numberOfAssetsDeleted ++;
                //Purge thumbKey and previewImageKey.
                purgeAssetFromServer(idToDelete, container);
              }
            }
          }
        }
      });
    }
    jobData.getLog()
    .info("Purged assets count: %d / %d", numberOfAssetsDeleted, totalNumberOfAssets);
  }
  
  /**
   * Returns a map of assetObjectKeys and its respective thumbKey,previewImageKey,type as a list.
   * @return Map<assetObjectKey,List[type,thumbKey,previewImageKey]>
   * @throws Exception
   */
  private Map<String, List<String>> getAllAssetIdsToPurge() 
      throws Exception
  {
    Map<String, List<String>> assetToPurgeMap = new HashMap<String, List<String>>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement query = currentConn.prepareStatement(GET_ALL_ASSET_KEYS_TO_PURGE);
      RDBMSAbstractDriver driver = currentConn.getDriver();
      IResultSetParser result = driver.getResultSetParser(query.executeQuery());
      while (result.next()) {
        totalNumberOfAssets ++;
        List<String> assetInfoList = new ArrayList<String>();
        String thumbKey = result.getString("thumbKey");
        String assetObjectKey = result.getString("assetObjectKey");
        String previewImageKey = result.getString("previewImageKey");
        assetInfoList.add(result.getString("type"));
        if (thumbKey != null && !thumbKey.isEmpty() && !thumbKey.equals(assetObjectKey)) {
          totalNumberOfAssets ++;
          assetInfoList.add(thumbKey);
        }
        if (previewImageKey != null && !previewImageKey.isEmpty()) {
          totalNumberOfAssets ++;
          assetInfoList.add(previewImageKey);
        }
        assetToPurgeMap.put(assetObjectKey, assetInfoList); 
      }
    });
    
    return assetToPurgeMap;
  }
  
  /**
   * Delete entry from pxp.assettobepurged table.
   */
  protected void deleteEntryFromAssetToBePurgedTable(RDBMSConnection currentConn, String assetObjectKey) 
      throws SQLException, RDBMSException {
    PreparedStatement deleteEntryQuery = currentConn.prepareStatement(DELETE_FROM_ASSET_PURGING_TABLE);
    deleteEntryQuery.setString(1, assetObjectKey);
    deleteEntryQuery.executeUpdate();
  }
  
  /**
   * Delete asset from swift server.
   * @param idToDelete
   * @param container
   */
  protected void purgeAssetFromServer(String idToDelete, String container)
  {
    IIdParameterModel idParameterModel = new IdParameterModel();
    idParameterModel.setId(idToDelete);
    idParameterModel.setType(container);
    try {
      IDeleteAssetFromSharedSwiftServerService deleteAssetFromSharedSwiftServerService = BGProcessApplication
          .getApplicationContext().getBean(IDeleteAssetFromSharedSwiftServerService.class);
      deleteAssetFromSharedSwiftServerService.execute(idParameterModel);
    }
    catch (Exception e) {
      numberOfAssetsDeleted --;
      jobData.getLog().error("asset purge has been failed: id: %s container: %s Exception: ", idParameterModel.getId(), idParameterModel.getType(), Text.join(",", e.getMessage()));
    }
    jobData.getLog().info("asset has been deleted from swift server with success: id: %s container: %s", idParameterModel.getId(), idParameterModel.getType());
  }
}
