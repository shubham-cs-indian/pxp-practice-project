package com.cs.core.rdbms.revision.dao;

import com.cs.core.csexpress.CSEList;
import com.cs.core.rdbms.tracking.dto.TimelineDTO;
import com.cs.core.rdbms.tracking.idto.ITimelineDTO;
import com.cs.core.rdbms.tracking.idto.ITimelineDTO.ChangeCategory;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("static-access")
public class ObjectRevisionDAS {
  
  public Map<ChangeCategory, ICSEList> convertEventIntoRevisionEvent(TimelineDTO timeline)
      throws CSFormatException
  {
    Map<ChangeCategory, ICSEList> timeLinesData = new HashMap<>();
    for (ITimelineDTO.ChangeCategory event : timeline.getCategories()) {
      CSEList cseList = (CSEList) timeline.getElements(event);
      for (ICSEElement subelement : cseList.getSubElements()) {
        prepareTimeLineData(timeLinesData, event, subelement);
      }
    }
    return timeLinesData;
  }
  
  private void prepareTimeLineData(Map<ChangeCategory, ICSEList> timeLinesData,
      ITimelineDTO.ChangeCategory timeLineEvent, ICSEElement subelement) throws CSFormatException
  {
    if (timeLinesData.containsKey(timeLineEvent)) {
      timeLinesData.get(timeLineEvent)
          .getSubElements()
          .add(subelement);
    }
    else {
      CSEList info = new CSEList();
      info.getSubElements()
          .add(subelement);
      timeLinesData.put(timeLineEvent, info);
    }
  }
  
}
