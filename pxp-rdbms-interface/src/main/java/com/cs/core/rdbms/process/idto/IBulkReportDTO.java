package com.cs.core.rdbms.process.idto;

import com.cs.core.rdbms.tracking.idto.ITimelineDTO;

import java.util.List;
import java.util.Map;

/**
 * Report information returned by bulk operations
 *
 * @author vallee
 */
public interface IBulkReportDTO {

  /**
   * @return the orders of faulty records after operation
   */
  public List<Integer> getFaultyRecordOrders();

  /**
   * @return the map of event information per object IID after operation
   */
  public Map<Long, ITimelineDTO> getTimelines();
}
