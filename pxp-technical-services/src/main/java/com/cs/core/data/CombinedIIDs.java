package com.cs.core.data;

import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * A special class that manages combined IIDs when returning from relations creation/update
 *
 * @author vallee
 */
public class CombinedIIDs {

  private long[] contextualObjectIIDs;
  private long[] otherContextualObjectIIDs;

  /**
   * A combined IIDs is a special string corresponding to the following structure:
   * "propertyRecordIID,contextualObjectIID:extensionIID,contextualObjectIID:extensionIID,..."
   *
   * @param combined
   */
  public CombinedIIDs(String combined) throws RDBMSException {

    if (combined.isEmpty()) {
      // should never happen
      throw new RDBMSException(100, "Empty Combined IID", "PropertyRecordIID not fould");
    }
    String[] iids = combined.split(",");
    this.contextualObjectIIDs = new long[iids.length];
    this.otherContextualObjectIIDs = new long[iids.length];
    if (iids.length > 0) {
      for (int index = 0; index < iids.length; index++) {
        String[] combinedIIDs = iids[index].split(":");
        contextualObjectIIDs[index] = Long.parseLong(combinedIIDs[0]);
        otherContextualObjectIIDs[index] = Long.parseLong(combinedIIDs[1]);
      }
    }
  }

  public long[] getContextualObjectIIDs() {
    return contextualObjectIIDs;
  }

  public long[] getOtherContextualObjectIIDs() {
    return otherContextualObjectIIDs;
  }
  
  public boolean hasCombinedIIDs() {
    return contextualObjectIIDs.length > 0;
  }
}
