package com.cs.core.runtime.interactor.model.datarule;

import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;

public interface ICategoryInformationModel extends IConfigEntityTreeInformationModel {
  
  public static final String COUNT                = "count";
  public static final String CLASSIFIER_IID       = "classifierIID";
  public static final String TOTAL_CHILDREN_COUNT = "totalChildrenCount";
  public static final String IS_LAST_NODE         = "isLastNode";
  
  public Boolean getIsLastNode();
  
  public void setIsLastNode(Boolean isLastNode);
  
  long getCount();
  
  void setCount(long count);
  
  public long getClassifierIID();
  
  public void setClassifierIID(long classifierIID);
  
  public void setTotalChildrenCount(long totalChildrenCount);
  
  public Long getTotalChildrenCount();
 
}
