package com.cs.core.rdbms.asset.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;

import com.cs.core.rdbms.asset.idto.IAssetMiscDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSDataAccessService;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.dam.rdbms.downloadtracker.idto.IDownloadTrackerDTO;

public class AssetMiscDAS extends RDBMSDataAccessService {
  
  public AssetMiscDAS(RDBMSConnection connection)
  {
    super(connection);
  }

  public void queryUpdateAssetMiscByIID(String sharedObjectId, long assetInstanceIId)
      throws RDBMSException, SQLException
  {
    StringBuilder STORE_SHARED_OBJECT_ID_QUERY = new StringBuilder("UPDATE  pxp.assetmisc SET ")
        .append(IAssetMiscDTO.SHARED_OBJECT_ID)
        .append("= '")
        .append(sharedObjectId)
        .append("', ")
        .append(IAssetMiscDTO.SHARED_TIME_STAMP)
        .append(" = ")
        .append(System.currentTimeMillis())
        .append(" WHERE (")
        .append(IAssetMiscDTO.ASSET_INSTANCE_IID)
        .append("= ")
        .append(assetInstanceIId)
        .append(" AND ")
        .append(IAssetMiscDTO.RENDITION_INSTANCE_IID)
        .append("= 0) OR ")
        .append(IAssetMiscDTO.RENDITION_INSTANCE_IID)
        .append("= ")
        .append(assetInstanceIId);
    PreparedStatement statement = currentConnection.prepareStatement(STORE_SHARED_OBJECT_ID_QUERY);
    statement.executeUpdate();
  }
  
  public void queryInsertAssetMisc(IAssetMiscDTO requestMap) throws SQLException, RDBMSException
  {
    
    StringBuilder INSERT_RECORD_INTO_ASSET_MISC = new StringBuilder("INSERT INTO pxp.assetmisc(")
        .append(IAssetMiscDTO.PRIMARY_KEY)
        .append(",")
        .append(IAssetMiscDTO.ASSET_INSTANCE_IID)
        .append(",")
        .append(IAssetMiscDTO.RENDITION_INSTANCE_IID)
        .append(",")
        .append(IAssetMiscDTO.DOWNLOAD_TIME_STAMP)
        .append(",")
        .append(IAssetMiscDTO.DOWNLOAD_COUNT)
        .append(",")
        .append(IAssetMiscDTO.SHARED_OBJECT_ID)
        .append(",")
        .append(IAssetMiscDTO.SHARED_TIME_STAMP)
        .append(") VALUES(nextval('pxp.assetmisctableprimarykey'), ")
        .append(requestMap.getAssetInstanceIId())
        .append(",")
        .append(requestMap.getRenditionInstanceIId())
        .append(",0,0,'")
        .append(requestMap.getSharedObjectId())
        .append("', ")
        .append(requestMap.getSharedTimeStamp())
        .append(")");
    PreparedStatement statement = currentConnection.prepareStatement(INSERT_RECORD_INTO_ASSET_MISC);
    statement.executeUpdate();
    
  }
  
  public IResultSetParser queryGetSharedLink(long assetInstanceIId)
      throws RDBMSException, SQLException
  {
    StringBuilder GET_SHARED_OBJECT_FORM_ASSET_MISC = new StringBuilder("SELECT ")
        .append(IAssetMiscDTO.SHARED_OBJECT_ID)
        .append(" from pxp.assetmisc where (")
        .append(IAssetMiscDTO.ASSET_INSTANCE_IID)
        .append("= ")
        .append(assetInstanceIId)
        .append(" and ")
        .append(IAssetMiscDTO.RENDITION_INSTANCE_IID)
        .append(" = 0 )")
        .append(" or ")
        .append(IAssetMiscDTO.RENDITION_INSTANCE_IID)
        .append("= ")
        .append(assetInstanceIId);
    PreparedStatement statement = currentConnection
        .prepareStatement(GET_SHARED_OBJECT_FORM_ASSET_MISC);
    return driver.getResultSetParser(statement.executeQuery());
  }
  
  public void queryDeleteSharedObjectIdForAssetInstanceAndTechnicalImageVariants(
      long assetInstanceIId) throws RDBMSException, SQLException
  {
    StringBuilder GET_SHARED_OBJECT_IDS_LIST = new StringBuilder("UPDATE").append(" pxp.assetmisc ")
        .append("SET ")
        .append(IAssetMiscDTO.SHARED_OBJECT_ID)
        .append("= '")
        .append("' where ")
        .append(IAssetMiscDTO.ASSET_INSTANCE_IID)
        .append(" = ? ")
        .append(" OR ")
        .append(IDownloadTrackerDTO.RENDITION_INSTANCE_IID)
        .append(" =  ? ");
    PreparedStatement statement = currentConnection.prepareStatement(GET_SHARED_OBJECT_IDS_LIST);
    statement.setLong(1, assetInstanceIId);
    statement.setLong(2, assetInstanceIId);
    statement.executeUpdate();
  }
  
  public IResultSetParser queryGetSharedLinkOfTIVs(long assetInstanceIId)
      throws RDBMSException, SQLException
  {
    StringBuilder GET_TIV_SHARED_OBJECT_IDS_FROM_ASSET_MISC = new StringBuilder("SELECT ")
        .append(IAssetMiscDTO.SHARED_OBJECT_ID)
        .append(" from pxp.assetmisc where ")
        .append(IAssetMiscDTO.ASSET_INSTANCE_IID)
        .append(" = ? ")
        .append(" OR ")
        .append(IDownloadTrackerDTO.RENDITION_INSTANCE_IID)
        .append(" =  ? ");
    PreparedStatement statement = currentConnection.prepareStatement(GET_TIV_SHARED_OBJECT_IDS_FROM_ASSET_MISC);
    statement.setLong(1, assetInstanceIId);
    statement.setLong(2, assetInstanceIId);
    return driver.getResultSetParser(statement.executeQuery());
  }

  public IResultSetParser queryGetTotalDownloadCount(long assetInstanceIId)
      throws RDBMSException, SQLException
  {
    StringBuilder GET_TOTAL_DOWNLOAD_COUNT = new StringBuilder("SELECT sum (")
        .append(IAssetMiscDTO.DOWNLOAD_COUNT)
        .append(") AS totalCount from pxp.assetmisc where ")
        .append(IAssetMiscDTO.ASSET_INSTANCE_IID)
        .append(" = ")
        .append(assetInstanceIId)
        .append(" OR ")
        .append(IAssetMiscDTO.RENDITION_INSTANCE_IID)
        .append(" = ")
        .append(assetInstanceIId);
    PreparedStatement statement = currentConnection.prepareStatement(GET_TOTAL_DOWNLOAD_COUNT);
    return driver.getResultSetParser(statement.executeQuery());
  }
  
  /**
   * Get Download Count of Rendition
   * @param connection
   * @param renditionInstanceIID
   * @return
   * @throws RDBMSException
   * @throws SQLException
   */
  public static IResultSetParser getDownloadCountOfRendition(RDBMSConnection connection,
      long renditionInstanceIID) throws RDBMSException, SQLException
  {
    StringBuilder query = new StringBuilder("Select ").append(IAssetMiscDTO.DOWNLOAD_COUNT)
        .append(" from pxp.assetmisc WHERE ")
        .append(IAssetMiscDTO.RENDITION_INSTANCE_IID)
        .append("= ")
        .append(renditionInstanceIID);
    PreparedStatement statement = connection.prepareStatement(query);
    return connection.getDriver()
        .getResultSetParser(statement.executeQuery());
  }
  
  /**
   * 
   * @param connection
   * @param entityID
   * @param catalogCode
   * @param organizationCode
   * @param endpointCode
   * @param languageCode
   * @return
   * @throws RDBMSException
   * @throws SQLException
   */
  public IResultSetParser queryGetIIdAndBaseLocaleId(RDBMSConnection connection,
      String entityID, String catalogCode, String organizationCode, String endpointCode,
      String languageCode) throws RDBMSException, SQLException
  {
    StringBuilder query = new StringBuilder("select be.baseentityiid,be.baselocaleid from pxp.baseentity be left outer join "
        + "pxp.baseentitylocaleidlink belil using(baseentityiid) where be.baseentityid = ? and be.catalogcode = ? and be.organizationcode = ? "
        + "and be.endpointcode " + (endpointCode.isEmpty() ? "is null": "= '"+ endpointCode + "'"));
    if (!StringUtils.isBlank(languageCode)) {
      query.append(" and (be.baselocaleid = '" +  languageCode + "' or belil.localeid = '" + languageCode + "')");
    }
    PreparedStatement statement = connection.prepareStatement(query);
    statement.setString(1, entityID);
    statement.setString(2, catalogCode);
    statement.setString(3, organizationCode);
    
    return connection.getDriver()
        .getResultSetParser(statement.executeQuery());
  }
}
