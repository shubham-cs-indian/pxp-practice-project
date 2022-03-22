package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;

public interface ICollectionDataForPlannerModel extends IConfigEntityInformationModel {
  
  public static final String COUNT                       = "count";
  public static final String TOTAL_LINKED_ENTITIES_COUNT = "totalLinkedEntitiesCount";
  
  public Long getCount();
  
  public void setCount(Long count);
  
  public Long getTotalLinkedEntitiesCount();
  
  public void setTotalLinkedEntitiesCount(Long totalLinkedEntitiesCount);
}
