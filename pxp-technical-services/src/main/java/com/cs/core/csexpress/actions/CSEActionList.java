package com.cs.core.csexpress.actions;

import com.cs.core.json.JSONContent;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.actions.ICSEAction;
import com.cs.core.technical.icsexpress.actions.ICSEActionList;
import com.cs.core.technical.ijosn.IJSONContent;
import java.util.ArrayList;

/**
 * @author vallee
 */
public class CSEActionList extends ArrayList<ICSEAction> implements ICSEActionList {

  public CSEActionList() {
    super();
  }

  @Override
  public IJSONContent toJSON() throws CSFormatException {
    JSONContent json = new JSONContent();
    int no = 1;
    for (ICSEAction action : this) {
      json.setField(String.format("%d", no++), action.toJSON());
    }
    return json;
  }

  @Override
  public String toString() {
    StringBuffer list = new StringBuffer();
    this.forEach((ICSEAction action) -> {
      list.append(action.toString())
              .append(", ");
    });
    list.setLength(list.length() - 2); // remove the trailing blank and coma
    return list.toString();
  }
}
