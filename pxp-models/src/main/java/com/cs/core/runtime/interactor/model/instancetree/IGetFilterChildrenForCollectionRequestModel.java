package com.cs.core.runtime.interactor.model.instancetree;


public interface IGetFilterChildrenForCollectionRequestModel
    extends IGetFilterChildrenRequestModel {
  
  public static final String COLLECTION_ID = "collectionId";
  public static final String IS_QUICKLIST  = "isQuicklist";
  
  public String getCollectionId();
  public void setCollectionId(String collectionId);
  
  public Boolean getIsQuicklist();
  public void setIsQuicklist(Boolean isQuicklist);
  
}
