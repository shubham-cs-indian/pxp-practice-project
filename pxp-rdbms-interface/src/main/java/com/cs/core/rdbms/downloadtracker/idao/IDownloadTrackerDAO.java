package com.cs.core.rdbms.downloadtracker.idao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.dam.rdbms.downloadtracker.idto.IDownloadTrackerDTO;

public interface IDownloadTrackerDAO {
  
  public List<IDownloadTrackerDTO> fetchDownloadLogs(String sortfield,
      String sortOrder, Long size, Long from, List<Map<String, Object>> filters) throws RDBMSException;
  
  public Set<Long> insertDownloadLogs(IDownloadTrackerDTO... downloadTrackerDTOs) throws RDBMSException;
  
  public long getDownloadLogCount(List<Map<String, Object>> filters) throws RDBMSException;

  public Long getDownloadCountWithinRange(long startTime, long endTime, long assetInstanceIId)
      throws RDBMSException;

  public StringBuilder getExportLogsQuery(String sortfield, String sortOrder, List<String> idList,
      List<Map<String, Object>> gridViewFilterList);

  public void resetDownloadCount(long assetInstanceIID) throws RDBMSException;
  
  /**
   * This method returns all distinct values according to columnName from DownloadLogsInformation Table
   * @param columnName
   * @param searchText
   * @param sortOrder
   * @param size
   * @param from
   * @return
   * @throws RDBMSException
   */
  public List<IDownloadTrackerDTO> fetchDownloadLogFilterData(String columnName, String searchText,
      String sortOrder, Long size, Long from) throws RDBMSException;

  /** This method returns the count of distinct values according to columnName in DownloadLogsInformation Table
   * @param searchText
   * @return
   * @throws RDBMSException
   */
  public long getDownloadLogFilterDataCount(String columnName, String searchText) throws RDBMSException;
}
