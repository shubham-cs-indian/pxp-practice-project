package com.cs.dam.rdbms.downloadtracker.dao;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.constants.SystemLevelIds;
import com.cs.core.rdbms.asset.dao.AssetMiscDAS;
import com.cs.core.rdbms.asset.idto.IAssetMiscDTO;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.downloadtracker.idao.IDownloadTrackerDAO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.services.records.ValueRecordDAS;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.dam.rdbms.downloadtracker.dto.DownloadTrackerDTO;
import com.cs.dam.rdbms.downloadtracker.idto.IDownloadTrackerDTO;

public class DownloadTrackerDAO implements IDownloadTrackerDAO {
  
  private static IDownloadTrackerDAO downloadTrackerDAOinstance;
  
  public static final String         COUNT = "count";
  
  private DownloadTrackerDAO()
  {
  }
  
  public static synchronized IDownloadTrackerDAO getInstance()
  {
    if (downloadTrackerDAOinstance == null) {
      downloadTrackerDAOinstance = new DownloadTrackerDAO();
    }
    return downloadTrackerDAOinstance;
  }

  @Override
  public List<IDownloadTrackerDTO> fetchDownloadLogs(String sortfield,
      String sortOrder, Long size, Long from, List<Map<String, Object>> filters) throws RDBMSException
  {
    List<IDownloadTrackerDTO> listOfdownloadLog = new ArrayList<>();
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          DownloadTrackerDAS downloadTrackerDAS = new DownloadTrackerDAS(currentConn);
          IResultSetParser resultSet = downloadTrackerDAS.getDownloadLog(sortfield, sortOrder, size,
              from, filters);
          while (resultSet.next()) {
            listOfdownloadLog.add(new DownloadTrackerDTO(resultSet));
          }
        });

    return listOfdownloadLog;
  }

  public Set<Long> insertDownloadLogs(IDownloadTrackerDTO... downloadTrackerDTOs) throws RDBMSException
  {
    IPropertyDTO propertyDTO = ConfigurationDAO.instance().getPropertyByCode(SystemLevelIds.ASSET_DOWNLOAD_COUNT_ATTRIBUTE);
    long downloadCountPropertyIId = propertyDTO.getPropertyIID();
    Map<Long, Double> assetIdVsCountMap = new HashMap<>();
    Set<Long> assetIIDs = new HashSet<Long>();
    List<IDownloadTrackerDTO> downloadTrackerDTOListForAssetMisc = new ArrayList<>();
    downloadTrackerDTOListForAssetMisc.addAll(Arrays.asList(downloadTrackerDTOs));
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      DownloadTrackerDAS downloadTrackerDAS = new DownloadTrackerDAS(currentConn);

      PreparedStatement psLogInfo = downloadTrackerDAS.getPSForDownloadLogTable(downloadTrackerDTOs);
      downloadTrackerDAS.executePSBatch(psLogInfo);

      for(IDownloadTrackerDTO downloadTrackerDTO : downloadTrackerDTOs){
        Long renditionId = Long.parseLong(downloadTrackerDTO.getRenditionInstanceIId());
        long assetId = downloadTrackerDTO.getAssetInstanceIId();
        incrementCount(assetIdVsCountMap, Long.valueOf(assetId));
        if(renditionId!=0){
          assetId = renditionId;
          incrementCount(assetIdVsCountMap, Long.valueOf(assetId));
        }
        if(downloadTrackerDAS.getRecordFromAssetMiscTable(assetId)){
          downloadTrackerDTOListForAssetMisc.remove(downloadTrackerDTO);
          downloadTrackerDAS.updateDownloadCount(assetId, downloadTrackerDTO.getTimeStamp());
        }
      }
      for(Map.Entry<Long, Double> entry : assetIdVsCountMap.entrySet()) {
        long assetId = entry.getKey();
        Double count = entry.getValue();
        assetIIDs.add(assetId);
        ValueRecordDAS.updateRecordValueByPropertyIIdAndEntityIId(currentConn, count, downloadCountPropertyIId, assetId);
      }

      PreparedStatement psDownloadCount = downloadTrackerDAS.getPSForDownloadCountTable(
          downloadTrackerDTOListForAssetMisc.toArray(new IDownloadTrackerDTO[] {}));
      downloadTrackerDAS.executePSBatch(psDownloadCount);
    });
    
    return assetIIDs;
  }
  
  private void incrementCount(Map<Long, Double> assetIdVsCountMap, long assetInstanceIID) {
    Double count = assetIdVsCountMap.get(assetInstanceIID);
    if(count != null) {
      assetIdVsCountMap.put(assetInstanceIID, count+1);
    }
    else {
      assetIdVsCountMap.put(assetInstanceIID, (double) 1);
    }
  }
  
  @Override
  public long getDownloadLogCount(List<Map<String, Object>> filters) throws RDBMSException
  {
    long[] totalCount = { 0 };
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          DownloadTrackerDAS downloadTrackerDAS = new DownloadTrackerDAS(currentConn);
          IResultSetParser result = downloadTrackerDAS.getPSForTotalCountOfDownloadLogs(filters);
          while (result.next()) {
            totalCount[0] = result.getLong(COUNT);
          }
        });
    
    return totalCount[0];
  }

  @Override
  public Long getDownloadCountWithinRange(long startTime, long endTime, long assetInstanceIID) throws RDBMSException
  {
    long[] totalTimeRangeCount = { 0 };
    RDBMSConnectionManager.instance()
    .runTransaction((RDBMSConnection currentConn) -> {
      DownloadTrackerDAS downloadTrackerDAS = new DownloadTrackerDAS(currentConn);
      IResultSetParser result = downloadTrackerDAS.getDownloadCountWithinRange(startTime, endTime, assetInstanceIID);
      while (result.next()) {
        totalTimeRangeCount[0] = result.getLong(COUNT);
      }
    });
    return totalTimeRangeCount[0];
  }

  @Override
  public StringBuilder getExportLogsQuery(String sortfield, String sortOrder, List<String> idList,
      List<Map<String, Object>> gridViewFilterList)
  {
    String downloadLogsColumns = DownloadTrackerDAS.getDownloadLogColumns();
    StringBuilder exportLogsQuery = new StringBuilder("SELECT ").append(downloadLogsColumns)
        .append(", ")
        .append(getColumnInDateFormat(IDownloadTrackerDTO.TIME_STAMP))
        .append(" , ")
        .append(IDownloadTrackerDTO.DOWNLOAD_ID)
        .append(" FROM pxp.downloadloginformation ");
    
    // Append Filter query
    exportLogsQuery.append(DownloadTrackerDAS
        .getExportDownloadLogFilterQuery(String.join(",", idList), gridViewFilterList));
    
    // Order by query for sorting
    exportLogsQuery.append(" ORDER BY ")
        .append(sortfield)
        .append(" ")
        .append(sortOrder)
        .append(" , ")
        .append(IDownloadTrackerDTO.PRIMARY_KEY)
        .append(" DESC");
    return exportLogsQuery;
  }
  
  private String getColumnInDateFormat(String columnName)
  {
    return "to_char(to_timestamp(" + columnName + "/1000), 'DD/MM/YYYY HH24:MI:SS') AS Timestamp";
  }

  /**
   * Reset Download Count against assetInstanceId
   */
  @Override
  public void resetDownloadCount(long assetInstanceIID) throws RDBMSException
  {
    IPropertyDTO propertyDTO = ConfigurationDAO.instance().getPropertyByCode(SystemLevelIds.ASSET_DOWNLOAD_COUNT_ATTRIBUTE);
    long downloadCountPropertyIId = propertyDTO.getPropertyIID();
    RDBMSConnectionManager.instance()
    .runTransaction((RDBMSConnection currentConn) -> {
      DownloadTrackerDAS downloadTrackerDAS = new DownloadTrackerDAS(currentConn);
      IResultSetParser rs = AssetMiscDAS.getDownloadCountOfRendition(currentConn, assetInstanceIID);
      downloadTrackerDAS.resetDownloadCount(assetInstanceIID);
      if(rs.next()) {
        long renditionCount = rs.getLong(IAssetMiscDTO.DOWNLOAD_COUNT);
        ValueRecordDAS.decrementRecordValueByPropertyIIdAndEntityIId(currentConn, (double) renditionCount, downloadCountPropertyIId, assetInstanceIID);
      }
      downloadTrackerDAS.setResetValue(currentConn, assetInstanceIID);
    });
  }


  @Override
  public List<IDownloadTrackerDTO> fetchDownloadLogFilterData(String columnName, String searchText, String sortOrder, Long size, Long from)
      throws RDBMSException
  {
    List<IDownloadTrackerDTO> downloadLogFilterDataList = new ArrayList<>();

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      DownloadTrackerDAS downloadTrackerDAS = new DownloadTrackerDAS(currentConn);
      IResultSetParser result = downloadTrackerDAS.fetchDownloadLogFilterData(columnName, searchText, sortOrder, size, from);
      while (result.next()) {
        IDownloadTrackerDTO logRecord = new DownloadTrackerDTO();
        String columnValue = result.getString(columnName);
        if (columnValue.isBlank())
          continue;
        switch (columnName) {
          case DownloadTrackerDTO.USER_ID :
            logRecord.setUserId(columnValue);
            logRecord.setUserName(result.getString(DownloadTrackerDTO.USER_NAME));
            break;

          case DownloadTrackerDTO.ASSET_INSTANCE_CLASS_ID :
            logRecord.setAssetInstanceClassId(columnValue);
            break;

          case DownloadTrackerDTO.RENDITION_INSTANCE_CLASS_ID :
            logRecord.setRenditionInstanceClassId(columnValue);
            break;
        }
        downloadLogFilterDataList.add(logRecord);
      }
    });
    return downloadLogFilterDataList;
  }

  @Override
  public long getDownloadLogFilterDataCount(String columnName, String searchText) throws RDBMSException
  {
    long[] recordsCount = { 0 };

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      DownloadTrackerDAS downloadTrackerDAS = new DownloadTrackerDAS(currentConn);
      IResultSetParser result = downloadTrackerDAS.getDownloadLogFilterDataCount(columnName, searchText);
      if (result.next()) {
        recordsCount[0] = result.getLong("count");
      }
    });
    return recordsCount[0];
  } 
}
