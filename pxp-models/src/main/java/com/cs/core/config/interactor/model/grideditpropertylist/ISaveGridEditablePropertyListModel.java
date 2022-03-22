package com.cs.core.config.interactor.model.grideditpropertylist;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;

import java.util.List;

public interface ISaveGridEditablePropertyListModel extends IConfigGetAllRequestModel {
  
  public static final String SEQUENCE_LIST = "sequenceList";
  
  public List<String> getSequenceList();
  
  public void setSequenceList(List<String> sequenceList);
}
