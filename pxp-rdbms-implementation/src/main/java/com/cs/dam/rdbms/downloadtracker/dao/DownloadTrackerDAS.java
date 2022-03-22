package com.cs.dam.rdbms.downloadtracker.dao;

import java.sql.BatchUpdateException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cs.core.data.Text;
import com.cs.core.rdbms.asset.idto.IAssetMiscDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSDataAccessService;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.dam.rdbms.downloadtracker.dto.DownloadTrackerDTO;
import com.cs.dam.rdbms.downloadtracker.idto.IDownloadTrackerDTO;

public class DownloadTrackerDAS extends RDBMSDataAccessService {
  
  public static final String INSERT_DOWNLOAD_LOG_INFORMATION = 
      "INSERT INTO downloadloginformation VALUES(nextval('DownloadLogInformationTablePrimaryKey'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
  public static final String INSERT_ASSET_MISC = "INSERT INTO assetmisc VALUES(nextval('AssetMiscTablePrimaryKey'),?,?,?,?)";
  public static final String TOTAL_COUNT_DOWNLOAD_LOG_INFORMATION_QUERY = "Select count(*) FROM downloadloginformation";

  public static final String GRID_VIEW_LIST_FILTER_MODEL       = "com.cs.dam.config.interactor.model.downloadtracker.GridViewListFilterModel";
  public static final String GRID_VIEW_TEXT_FILTER_MODEL       = "com.cs.dam.config.interactor.model.downloadtracker.GridViewTextFilterModel";
  public static final String GRID_VIEW_TIME_RANGE_FILTER_MODEL = "com.cs.dam.config.interactor.model.downloadtracker.GridViewTimeRangeFilterModel";
  
  public DownloadTrackerDAS(RDBMSConnection connection)
  {
    super(connection);
  }

  public PreparedStatement getPSForDownloadLogTable(IDownloadTrackerDTO... downloadTrackerDTOs) throws RDBMSException, SQLException
  {
    PreparedStatement psLogInfo = currentConnection.prepareStatement(INSERT_DOWNLOAD_LOG_INFORMATION);
    for(IDownloadTrackerDTO downloadTrackerDTO : downloadTrackerDTOs){
      psLogInfo.setLong(1, downloadTrackerDTO.getAssetInstanceIId());
      psLogInfo.setString(2, downloadTrackerDTO.getAssetInstanceName());
      psLogInfo.setString(3, downloadTrackerDTO.getAssetFileName());
      psLogInfo.setString(4, downloadTrackerDTO.getAssetInstanceClassId());
      psLogInfo.setString(5, downloadTrackerDTO.getAssetInstanceClassCode());
      psLogInfo.setLong(6, Long.parseLong(downloadTrackerDTO.getRenditionInstanceIId()));
      psLogInfo.setString(7, downloadTrackerDTO.getRenditionInstanceName());
      psLogInfo.setString(8, downloadTrackerDTO.getRenditionFileName());
      psLogInfo.setString(9, downloadTrackerDTO.getRenditionInstanceClassId());
      psLogInfo.setString(10, downloadTrackerDTO.getRenditionInstanceClassCode());
      psLogInfo.setString(11, downloadTrackerDTO.getUserId());
      psLogInfo.setLong(12, downloadTrackerDTO.getTimeStamp());
      psLogInfo.setString(13, downloadTrackerDTO.getComment());
      psLogInfo.setString(14, downloadTrackerDTO.getDownloadId());
      psLogInfo.setString(15, downloadTrackerDTO.getUserName());
      psLogInfo.setBoolean(16, false);
      psLogInfo.addBatch();
    }
    return psLogInfo;
  }
  
  public PreparedStatement getPSForDownloadCountTable(IDownloadTrackerDTO... downloadTrackerDTOs)
      throws RDBMSException, SQLException
  {
    PreparedStatement psDownloadCount = currentConnection.prepareStatement(INSERT_ASSET_MISC);
    for (IDownloadTrackerDTO downloadTrackerDTO : downloadTrackerDTOs) {
      psDownloadCount.setLong(1, downloadTrackerDTO.getAssetInstanceIId());
      psDownloadCount.setLong(2, Long.parseLong(downloadTrackerDTO.getRenditionInstanceIId()));
      psDownloadCount.setLong(3, downloadTrackerDTO.getTimeStamp());
      psDownloadCount.setLong(4, 1);
      psDownloadCount.addBatch();
    }
    return psDownloadCount;
  }
  
  public void insertDownloadLogs(IDownloadTrackerDTO... downloadTrackerDTOs) throws RDBMSException, SQLException
  {
    PreparedStatement psLogInfo = getPSForDownloadLogTable(downloadTrackerDTOs);
    PreparedStatement psDownloadCount = getPSForDownloadCountTable(downloadTrackerDTOs);
    try {
      psLogInfo.executeBatch();
      psDownloadCount.executeBatch();
    }
    catch (BatchUpdateException e) {
      throw e.getNextException();
    }
    finally{
      psLogInfo.close();
      psDownloadCount.close();
    }
  }
  
  public void executePSBatch(PreparedStatement preparedStatement) throws SQLException
  {
    try {
      preparedStatement.executeBatch();
    }
    catch (BatchUpdateException e) {
      throw e.getNextException();
    }
    finally{
      preparedStatement.close();
    }
  }
  
  protected void updateDownloadCount(long assetId,
      long timeStamp) throws RDBMSException, SQLException
  {
    StringBuilder updateCountQuery = new StringBuilder("UPDATE assetmisc SET ")
        .append("downloadCount")
        .append(" = ").append("downloadCount").append(" +1, ")
        .append("downloadTimeStamp").append(" = ").append(timeStamp)
        .append(" WHERE (").append(IDownloadTrackerDTO.ASSET_INSTANCE_IID)
        .append(" = ")
        .append(assetId)
        .append(" AND ")
        .append(IAssetMiscDTO.RENDITION_INSTANCE_IID)
        .append(" = 0 ) OR ")
        .append(IDownloadTrackerDTO.RENDITION_INSTANCE_IID)
        .append(" = ").append(assetId);
    
    PreparedStatement psForUpdate = currentConnection.prepareStatement(updateCountQuery.toString());
    try{
      psForUpdate.executeUpdate();
    }
    catch(SQLException e){
      throw e.getNextException();
    }
    finally{
      psForUpdate.close();
    }
  }

  protected boolean getRecordFromAssetMiscTable(long assetInstanceIID) throws SQLException, RDBMSException
  {

    StringBuilder checkIfRowExistQuery = new StringBuilder("SELECT ")
        .append(IDownloadTrackerDTO.PRIMARY_KEY)
        .append(" FROM pxp.assetmisc WHERE (")
        .append(IDownloadTrackerDTO.ASSET_INSTANCE_IID)
        .append(" = ")
        .append(assetInstanceIID)
        .append(" AND ")
        .append(IAssetMiscDTO.RENDITION_INSTANCE_IID)
        .append(" = 0  ) OR ")
        .append(IDownloadTrackerDTO.RENDITION_INSTANCE_IID)
        .append(" = ")
        .append(assetInstanceIID);
    
    PreparedStatement ps = currentConnection.prepareStatement(checkIfRowExistQuery.toString());
    try (ResultSet rs = ps.executeQuery();)
    {
      if(rs.next()) {
        return true;
      }
    }
    catch (SQLException e) {
      throw e.getNextException();
    }
    finally{
      ps.close();
    }
    return false;
  }

  public IResultSetParser getDownloadLog(String sortfield, String sortOrder, Long size, Long from,
      List<Map<String, Object>> filters) throws RDBMSException, SQLException
  {
    StringBuilder query = new StringBuilder();
    String downloadLogColumns = getDownloadLogColumns();
    query.append("Select " + downloadLogColumns + " , " + IDownloadTrackerDTO.TIME_STAMP + ", "
        + IDownloadTrackerDTO.DOWNLOAD_ID + ", " + IDownloadTrackerDTO.IS_RESET);
    query.append(" FROM downloadloginformation");
    query.append(getFilterQueries(filters));
    query.append(" ORDER BY " + sortfield + " " + sortOrder);
    query.append(
        ", " + IDownloadTrackerDTO.PRIMARY_KEY + " DESC" + " OFFSET " + from + " LIMIT " + size);
    PreparedStatement queryPS = currentConnection.prepareStatement(query.toString());
    
    try {
      queryPS.execute();
      return currentConnection.getResultSetParser(queryPS.getResultSet());
    }
    catch (SQLException e) {
      throw e.getNextException();
    }
  }

  public static String getDownloadLogColumns()
  {
    String downloadLogColumns = IDownloadTrackerDTO.PRIMARY_KEY + ", "
            + IDownloadTrackerDTO.ASSET_INSTANCE_IID + ", " 
            + IDownloadTrackerDTO.ASSET_INSTANCE_NAME + ", "
            + IDownloadTrackerDTO.ASSET_FILE_NAME + ", "
            + IDownloadTrackerDTO.ASSET_INSTANCE_CLASS_ID + ", "
            + IDownloadTrackerDTO.ASSET_INSTANCE_CLASS_CODE + ", "
            + "CASE WHEN " + IDownloadTrackerDTO.RENDITION_INSTANCE_IID + " = 0 THEN '' ELSE "
            + IDownloadTrackerDTO.RENDITION_INSTANCE_IID + "::text END , "
            + IDownloadTrackerDTO.RENDITION_INSTANCE_NAME + ", "
            + IDownloadTrackerDTO.RENDITION_FILE_NAME + ", " 
            + IDownloadTrackerDTO.RENDITION_INSTANCE_CLASS_ID + ", " 
            + IDownloadTrackerDTO.RENDITION_INSTANCE_CLASS_CODE + ", "
            + IDownloadTrackerDTO.USER_ID + ", " + IDownloadTrackerDTO.USER_NAME + ", "
            + IDownloadTrackerDTO.COMMENT;
    return downloadLogColumns;
  }
  
  protected static String getFilterQueries(List<Map<String, Object>> filters)
  {
    if(filters.isEmpty()){
      return "";
    }
    
    StringBuilder filterQuery = new StringBuilder(" WHERE 1 = 1 ");
    for(Map<String, Object> filter : filters) {
      String filterType = (String) filter.get("filterType");
      String filterField = (String) filter.get("filterField");
      filterQuery.append(" AND ");
      switch(filterType) {
        case GRID_VIEW_LIST_FILTER_MODEL :
          filterQuery.append(filterField + " IN " + filter.get("filterValues"));
          break;
        case GRID_VIEW_TEXT_FILTER_MODEL :
          filterQuery.append(filterField + " ilike '%"+ filter.get("filterValues") + "%' ");
          break;
        case GRID_VIEW_TIME_RANGE_FILTER_MODEL :
          filterQuery.append(filterField + " BETWEEN " +  filter.get("filterValues"));
          break;
        default:
          filterQuery.append(filterField + " like '%"+ filter.get("filterValues") + "%' ");
      }
    }
    return filterQuery.toString();
  }
  
  public IResultSetParser getPSForTotalCountOfDownloadLogs(List<Map<String, Object>> filters) throws RDBMSException, SQLException
  {
    StringBuilder query = new StringBuilder(TOTAL_COUNT_DOWNLOAD_LOG_INFORMATION_QUERY);
    query.append(getFilterQueries(filters));
    PreparedStatement psLogInfo = currentConnection.prepareStatement(query);
    psLogInfo.execute();

    return currentConnection.getResultSetParser(psLogInfo.getResultSet());
  }

  public IResultSetParser getDownloadCountWithinRange(Long startTime, Long endTime, long assetInstanceIID)
      throws RDBMSException, SQLException
  {
    StringBuilder QUERY_GET_DOWNLOAD_COUNT_WITHIN_TIME_RANGE = 

        new StringBuilder("Select count(*) FROM pxp.downloadloginformation where ( ")
        .append(IDownloadTrackerDTO.ASSET_INSTANCE_IID)
        .append(" = ")
        .append(assetInstanceIID)
        .append(" OR ")
        .append(IDownloadTrackerDTO.RENDITION_INSTANCE_IID)
        .append(" = ")
        .append(assetInstanceIID)
        .append(" ) AND ")
        .append(IDownloadTrackerDTO.TIME_STAMP)
        .append(" BETWEEN ")
        .append(startTime)
        .append(" AND ")
        .append(endTime)
        .append(" AND isReset = false");
    PreparedStatement statement = currentConnection
        .prepareStatement(QUERY_GET_DOWNLOAD_COUNT_WITHIN_TIME_RANGE);
    return driver.getResultSetParser(statement.executeQuery());
  }
  
  public static String getExportDownloadLogFilterQuery(String idList, List<Map<String, Object>> filters)
  {
    String filterQueryAsString = getFilterQueries(filters);
    StringBuilder filterQuery = new StringBuilder(filterQueryAsString);
    if (!StringUtils.isEmpty(idList)) {
      if(!StringUtils.isEmpty(filterQuery))
        filterQuery.append(" AND ");
        else
          filterQuery.append(" WHERE ");
      
      filterQuery.append(IDownloadTrackerDTO.PRIMARY_KEY)
          .append(" IN (")
          .append(idList)
          .append(")");
    }
    return filterQuery.toString();
  }

  /**
   * Reset Download Count against assetInstanceId
   * @param assetInstanceIID
   * @throws RDBMSException
   * @throws SQLException
   */
  public void resetDownloadCount(long assetInstanceIID) throws RDBMSException, SQLException
  {
    StringBuilder STORE_SHARED_OBJECT_ID_QUERY = new StringBuilder("UPDATE  pxp.assetmisc SET ")
        .append(IAssetMiscDTO.DOWNLOAD_COUNT)
        .append("= ")
        .append(0)
        .append(" WHERE ")
        .append(IAssetMiscDTO.ASSET_INSTANCE_IID)
        .append("= ")
        .append(assetInstanceIID)
        .append(" OR ")
        .append(IAssetMiscDTO.RENDITION_INSTANCE_IID)
        .append("= ")
        .append(assetInstanceIID);
    PreparedStatement statement = currentConnection.prepareStatement(STORE_SHARED_OBJECT_ID_QUERY);
    statement.executeUpdate();
  }

  /**
   * This method sets isReset value in download log information table as true.
   * This is called in case asset is replaced or TIV is deleted.
   * @param currentConn
   * @param assetInstanceIID
   * @throws RDBMSException
   * @throws SQLException
   */
  public void setResetValue(RDBMSConnection currentConn, long assetInstanceIID) throws RDBMSException, SQLException
  {
    StringBuilder SET_RESET_VALUE_QUERY = new StringBuilder("UPDATE  pxp.downloadloginformation SET ")
        .append(IDownloadTrackerDTO.IS_RESET)
        .append(" = true where ")
        .append(IDownloadTrackerDTO.ASSET_INSTANCE_IID)
        .append(" = ")
        .append(assetInstanceIID)
        .append(" OR ")
        .append(IDownloadTrackerDTO.RENDITION_INSTANCE_IID)
        .append(" = ")
        .append(assetInstanceIID);
        
    PreparedStatement statement = currentConnection.prepareStatement(SET_RESET_VALUE_QUERY);
    statement.executeUpdate();
  }
  
  /**
   * This method returns all distinct values of passed columnName from DownloadLogsInformation Table
   * @param columnName
   * @param searchText
   * @param sortOrder
   * @param size
   * @param from
   * @return
   * @throws RDBMSException
   * @throws SQLException
   */
  public IResultSetParser fetchDownloadLogFilterData(String columnName, String searchText, String sortOrder, Long size, Long from)
      throws RDBMSException, SQLException
  {
    StringBuilder fetchDownloadLogUsers = new StringBuilder("SELECT DISTINCT ON (" + columnName + ") " + columnName);
    if (DownloadTrackerDTO.USER_ID.equals(columnName)) {
      fetchDownloadLogUsers.append(", "+ DownloadTrackerDTO.USER_NAME);
    }
    fetchDownloadLogUsers.append(" FROM pxp.downloadloginformation");
    if (searchText != null && !searchText.isEmpty() && DownloadTrackerDTO.USER_ID.equals(columnName)) {
      fetchDownloadLogUsers.append(" where username ilike '%" + searchText + "%'").append(" ORDER BY " + columnName + " ")
      .append(sortOrder).append(" LIMIT ").append(size).append(" OFFSET ").append(from);
    }

    PreparedStatement queryStatement = currentConnection.prepareStatement(fetchDownloadLogUsers);
    return driver.getResultSetParser(queryStatement.executeQuery());
  }

  /**
   * This method returns the count of distinct values of passed columnName in DownloadLogsInformation Table
   * @param columnName
   * @param searchText
   * @return
   * @throws RDBMSException
   * @throws SQLException
   */
  public IResultSetParser getDownloadLogFilterDataCount(String columnName, String searchText) throws RDBMSException, SQLException
  {
    StringBuilder countQuery = new StringBuilder("SELECT  COUNT ( DISTINCT " + columnName + " ) FROM pxp.downloadloginformation");
    if (searchText != null && !searchText.isEmpty() && DownloadTrackerDTO.USER_ID.equals(columnName)) {
      countQuery.append(" where " + columnName + " ilike '%" + searchText + "%' ");
    }
    PreparedStatement queryStatement = currentConnection.prepareStatement(countQuery);
    return driver.getResultSetParser(queryStatement.executeQuery());
  }
}
