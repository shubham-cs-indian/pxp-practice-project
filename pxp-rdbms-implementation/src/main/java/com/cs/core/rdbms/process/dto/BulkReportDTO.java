package com.cs.core.rdbms.process.dto;

import com.cs.core.rdbms.process.idto.IBulkReportDTO;
import com.cs.core.rdbms.tracking.idto.ITimelineDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author vallee
 */
public class BulkReportDTO implements IBulkReportDTO {
  
  private final List<Integer> faultyRecordOrders = new ArrayList<>();
  
  public BulkReportDTO(List<Integer> faultyRecords)
  {
    faultyRecordOrders.addAll(faultyRecords);
  }
  
  @Override
  public List<Integer> getFaultyRecordOrders()
  {
    return faultyRecordOrders;
  }
  
  @Override
  public Map<Long, ITimelineDTO> getTimelines()
  {
    throw new UnsupportedOperationException("Not supported yet."); // To change
                                                                   // body of
                                                                   // generated
                                                                   // methods,
                                                                   // choose
                                                                   // Tools |
                                                                   // Templates.
  }
  
}
