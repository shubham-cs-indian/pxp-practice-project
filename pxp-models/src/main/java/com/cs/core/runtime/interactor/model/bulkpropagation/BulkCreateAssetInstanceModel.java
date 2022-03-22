package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.runtime.interactor.model.assetinstance.CreateAssetInstanceModel;
import com.cs.core.runtime.interactor.model.assetinstance.ICreateAssetInstanceModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class BulkCreateAssetInstanceModel implements IBulkCreateAssetInstanceModel {
  
  private static final long       serialVersionUID = 1L;
  
  List<ICreateAssetInstanceModel> assetInstances   = new ArrayList<>();
  List<String>                    collectionIds    = new ArrayList<>();
  String                          label;
  
  @Override
  public List<ICreateAssetInstanceModel> getAssetInstances()
  {
    return assetInstances;
  }
  
  @JsonDeserialize(contentAs = CreateAssetInstanceModel.class)
  @Override
  public void setAssetInstances(List<ICreateAssetInstanceModel> assetInstances)
  {
    this.assetInstances = assetInstances;
  }
  
  @Override
  public List<String> getCollectionIds()
  {
    return collectionIds;
  }
  
  @Override
  public void setCollectionIds(List<String> collectionIds)
  {
    this.collectionIds = collectionIds;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
}
