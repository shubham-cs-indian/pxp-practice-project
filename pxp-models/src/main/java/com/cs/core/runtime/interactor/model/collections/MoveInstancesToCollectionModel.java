package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IdAndTypeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class MoveInstancesToCollectionModel implements IMoveInstancesToCollectionModel {
  
  private static final long       serialVersionUID = 1L;
  
  protected String                addToCollectionId;
  protected List<IIdAndTypeModel> addedContents;
  protected String                removeFromCollectionId;
  
  @Override
  public String getAddToCollectionId()
  {
    return addToCollectionId;
  }
  
  @Override
  public void setAddToCollectionId(String addToCollectionId)
  {
    this.addToCollectionId = addToCollectionId;
  }
  
  @Override
  public List<IIdAndTypeModel> getAddedContents()
  {
    return addedContents;
  }
  
  @JsonDeserialize(contentAs = IdAndTypeModel.class)
  @Override
  public void setAddedContents(List<IIdAndTypeModel> addedContents)
  {
    this.addedContents = addedContents;
  }
  
  @Override
  public String getRemoveFromCollectionId()
  {
    return removeFromCollectionId;
  }
  
  @Override
  public void setRemoveFromCollectionId(String removeFromCollectionId)
  {
    this.removeFromCollectionId = removeFromCollectionId;
  }
}
