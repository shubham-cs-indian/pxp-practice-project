package com.cs.core.config.interactor.model.grideditpropertylist;

import com.cs.core.config.interactor.model.grid.ConfigGetAllRequestModel;

import java.util.ArrayList;
import java.util.List;

public class SaveGridEditablePropertyListModel extends ConfigGetAllRequestModel
    implements ISaveGridEditablePropertyListModel {
  
  private static final long serialVersionUID = 1L;
  
  List<String>              sequenceList;
  
  @Override
  public List<String> getSequenceList()
  {
    if (sequenceList == null) {
      sequenceList = new ArrayList<String>();
    }
    return sequenceList;
  }
  
  @Override
  public void setSequenceList(List<String> sequenceList)
  {
    this.sequenceList = sequenceList;
  }
}
