package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IMoveInstancesToCollectionModel extends IModel {
  
  public static final String ADD_TO_COLLECTION_ID      = "addToCollectionId";
  public static final String ADDED_CONTENTS            = "addedContents";
  public static final String REMOVE_FROM_COLLECTION_ID = "removeFromCollectionId";
  
  public String getAddToCollectionId();
  
  public void setAddToCollectionId(String addToCollectionId);
  
  public List<IIdAndTypeModel> getAddedContents();
  
  public void setAddedContents(List<IIdAndTypeModel> addedContents);
  
  public String getRemoveFromCollectionId();
  
  public void setRemoveFromCollectionId(String removeFromCollectionId);
}
