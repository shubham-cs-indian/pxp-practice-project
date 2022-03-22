package com.cs.core.rdbms.entity.dto;

import com.cs.core.rdbms.entity.idto.ICollectionFilterDTO;

public class CollectionFilterDTO implements ICollectionFilterDTO{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private boolean           isQuickList      = false;
  private final String      collectionId;
  
  public CollectionFilterDTO(Boolean isQuickList, String collectionId)
  {
    this.isQuickList = isQuickList;
    this.collectionId = collectionId;
  }
  
  @Override
  public Boolean isQuickList()
  {
    return isQuickList;
  }

  @Override
  public String getCollectionId()
  {
    return collectionId;
  }

}
