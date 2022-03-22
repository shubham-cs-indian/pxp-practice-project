package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;

public class CollectionDataForPlannerModel extends ConfigEntityInformationModel
    implements ICollectionDataForPlannerModel {
  
  private static final long serialVersionUID         = 1L;
  
  protected Long            count                    = 0l;
  protected Long            totalLinkedEntitiesCount = 0l;
  
  @Override
  public Long getCount()
  {
    return count;
  }
  
  @Override
  public void setCount(Long count)
  {
    this.count = count;
  }
  
  @Override
  public Long getTotalLinkedEntitiesCount()
  {
    return totalLinkedEntitiesCount;
  }
  
  @Override
  public void setTotalLinkedEntitiesCount(Long totalLinkedEntitiesCount)
  {
    this.totalLinkedEntitiesCount = totalLinkedEntitiesCount;
  }
}
