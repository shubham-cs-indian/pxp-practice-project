package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.runtime.interactor.model.assetinstance.ICreateAssetInstanceModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IBulkCreateAssetInstanceModel extends IModel {
  
  public static final String ASSETINSTANCES = "assetInstances";
  public static final String COLLECTIONIDS  = "collectionIds";
  public static final String LABEL          = "label";
  
  public List<ICreateAssetInstanceModel> getAssetInstances();
  
  public void setAssetInstances(List<ICreateAssetInstanceModel> assetInstances);
  
  public List<String> getCollectionIds();
  
  public void setCollectionIds(List<String> collectionIds);
  
  public String getLabel();
  
  public void setLabel(String label);
}
