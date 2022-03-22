package com.cs.core.rdbms.revision.dto;

import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.tracking.idto.IObjectTrackingDTO;
import com.cs.core.rdbms.tracking.idto.ITimelineDTO.ChangeCategory;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEList;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author vallee
 */
public class RevisionTimelineBuilder {
  
  private final Map<ChangeCategory, ICSEList> revisionTimelineData;
  
  public RevisionTimelineBuilder()
  {
    revisionTimelineData = new HashMap<>();
  }
  
  public void addTrackingTimeline(IObjectTrackingDTO change) throws CSFormatException
  {
    Map<ChangeCategory, ICSEList> timelines = change.getTimelineData().getTimelines();
    Set<ChangeCategory> keySet = timelines.keySet();
    for (ChangeCategory key : keySet) {
      if (revisionTimelineData.get(key) != null) {
        ICSEList icseList = revisionTimelineData.get(key);
        icseList.getSubElements()
            .addAll(timelines.get(key)
                .getSubElements());
      }
      else {
        revisionTimelineData.put(key, timelines.get(key));
      }
    }
  }
  
  public JSONContent toJSON() throws CSFormatException
  {
    JSONContent content = new JSONContent();
    Set<ChangeCategory> keySet = revisionTimelineData.keySet();
    for (ChangeCategory key : keySet) {
      ICSEList icseList = revisionTimelineData.get(key);
      content.setField(key.toString(), icseList.toString());
    }
    return content;
  }

  public Map<ChangeCategory, ICSEList> getDelta()
  {
    return revisionTimelineData;
  }
}
