package com.cs.core.rdbms.asset.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cs.core.rdbms.asset.idao.IAssetMiscDAO;
import com.cs.core.rdbms.asset.idto.IAssetMiscDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class AssetMiscDAO implements IAssetMiscDAO {
  
  private static AssetMiscDAO instance       = null;
  public static final String  IS_NOT_PRESENT = "NotPresent";
  private static final String TOTAL_COUNT    = "totalCount";

  private AssetMiscDAO()
  {
    // private constructor for Singleton class.
  }
  
  public static AssetMiscDAO getInstance()
  {
    if (instance == null) {
      instance = new AssetMiscDAO();
    }
    return instance;
    
  }
  
  @Override
  public String getAssetMiscRecordById(long assetInstanceIID) throws RDBMSException
  {
    List<String> isPresentList = new ArrayList<String>();
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          AssetMiscDAS assetMiscDAS = new AssetMiscDAS(currentConn);
          IResultSetParser resultSetParser = assetMiscDAS.queryGetSharedLink(assetInstanceIID);
          if (resultSetParser.next()) {
            isPresentList.add(resultSetParser.getString(IAssetMiscDTO.SHARED_OBJECT_ID));
          }
          else {
            isPresentList.add(IS_NOT_PRESENT);
          }
        });
    return isPresentList.get(0);
    
  }
  
  @Override
  public void updateAssetMiscRecordWithSharedObjectId(String sharedObjectId, long assetInstanceIID)
      throws RDBMSException
  {
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          AssetMiscDAS assetMiscDAS = new AssetMiscDAS(currentConn);
          assetMiscDAS.queryUpdateAssetMiscByIID(sharedObjectId, assetInstanceIID);
        });
  }
  
  @Override
  public void insertAssetMiscRecord(IAssetMiscDTO insertRequest) throws RDBMSException
  {
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          AssetMiscDAS assetMiscDAS = new AssetMiscDAS(currentConn);
          assetMiscDAS.queryInsertAssetMisc(insertRequest);
        });
  }

  @Override
  public String getSharedAssetLink(long assetInstanceIID) throws RDBMSException
  {
    List<String> sharedObjectLinkList = new ArrayList<String>();
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          AssetMiscDAS assetMiscDAS = new AssetMiscDAS(currentConn);
          IResultSetParser resultSetParser = assetMiscDAS.queryGetSharedLink(assetInstanceIID);
          if (resultSetParser.next()) {
            sharedObjectLinkList.add(resultSetParser.getString(IAssetMiscDTO.SHARED_OBJECT_ID));
          }
        });
    if (sharedObjectLinkList.isEmpty()) {
      return null;
    }
    else {
      return sharedObjectLinkList.get(0);
    }
    
  }

  /**
   * Delete shared object id of main asset instance and its technical image
   * variant from assetMisc table
   */
  @Override
  public List<String> deleteSharedObjectIdForMainAssetAndItsVariantById(long assetInstanceIId)
      throws RDBMSException
  {
    List<String> sharedObjectIdList = new ArrayList<String>();
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          AssetMiscDAS assetMiscDAS = new AssetMiscDAS(currentConn);
          IResultSetParser resultSetParser = assetMiscDAS.queryGetSharedLinkOfTIVs(assetInstanceIId);
          assetMiscDAS.queryDeleteSharedObjectIdForAssetInstanceAndTechnicalImageVariants(assetInstanceIId);
          while (resultSetParser.next()) {
            String sharedObjectId = resultSetParser.getString(IAssetMiscDTO.SHARED_OBJECT_ID);
            if (StringUtils.isNotBlank(sharedObjectId)) {
              sharedObjectIdList.add(sharedObjectId);
            }
          }
        });
    return sharedObjectIdList;

  }

  @Override
  public long getTotalDownloadCount(long assetInstanceId) throws RDBMSException
  {
    List<Long> totalCountResult = new ArrayList<Long>();
    RDBMSConnectionManager.instance()
    .runTransaction((RDBMSConnection currentConn) -> {
      AssetMiscDAS assetMiscDAS = new AssetMiscDAS(currentConn);
      IResultSetParser resultSetParser = assetMiscDAS.queryGetTotalDownloadCount(assetInstanceId);
      if (resultSetParser.next()) {
        totalCountResult.add(resultSetParser.getLong(TOTAL_COUNT));
      }
    });
    return totalCountResult.get(0);
  }
  
  @Override
  public Map<String, Object> getIIdAndBaseLocaleId(String entityID, String catalogCode, String organizationCode, String endpointCode, String languageCode) throws RDBMSException
  {
    Map<String, Object> returnMap = new HashMap<String, Object>();
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
      AssetMiscDAS assetMiscDAS = new AssetMiscDAS(currentConn);
      IResultSetParser resultSetParser = assetMiscDAS.queryGetIIdAndBaseLocaleId(currentConn, entityID, catalogCode, organizationCode, endpointCode, languageCode);
      if (resultSetParser.next()) {
        returnMap.put("entityiid", resultSetParser.getLong("baseentityiid"));
        returnMap.put("languageCode", !StringUtils.isBlank(languageCode) ? languageCode : resultSetParser.getString("baselocaleid"));
      } else {
        returnMap.put("entityiid", -1l);
      }
    });
    
    return returnMap;
  }
  
}
