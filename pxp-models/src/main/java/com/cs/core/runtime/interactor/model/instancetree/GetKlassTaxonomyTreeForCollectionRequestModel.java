package com.cs.core.runtime.interactor.model.instancetree;

public class GetKlassTaxonomyTreeForCollectionRequestModel extends GetKlassTaxonomyTreeRequestModel 
      implements IGetKlassTaxonomyTreeForCollectionRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          collectionId;
  protected Boolean         isQuicklist      = false;
  
  @Override
  public String getCollectionId()
  {
    return collectionId;
  }
  
  @Override
  public void setCollectionId(String collectionId)
  {
    this.collectionId = collectionId;
  }

  @Override
  public Boolean getIsQuicklist()
  {
    return isQuicklist;
  }

  @Override
  public void setIsQuicklist(Boolean isQuicklist)
  {
    this.isQuicklist = isQuicklist;
  }
 
}
