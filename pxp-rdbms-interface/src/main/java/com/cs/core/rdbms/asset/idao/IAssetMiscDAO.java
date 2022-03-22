package com.cs.core.rdbms.asset.idao;

import java.util.List;
import java.util.Map;

import com.cs.core.rdbms.asset.idto.IAssetMiscDTO;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public interface IAssetMiscDAO {
  
  public String getAssetMiscRecordById(long baseEntityId) throws RDBMSException;
  
  public void updateAssetMiscRecordWithSharedObjectId(String sharedObjectId, long baseEntityId)
      throws RDBMSException;
  
  public void insertAssetMiscRecord(IAssetMiscDTO assetMiscDTO) throws RDBMSException;

  public String getSharedAssetLink(long id) throws RDBMSException;

  List<String> deleteSharedObjectIdForMainAssetAndItsVariantById(long assetInstanceId) throws RDBMSException;
  
  public long getTotalDownloadCount(long assetInstanceId) throws RDBMSException;
  
  public Map<String, Object> getIIdAndBaseLocaleId(String entityID, String catalogCode, String organizationCode, String endpointCode, String languageCode) throws RDBMSException;

}
