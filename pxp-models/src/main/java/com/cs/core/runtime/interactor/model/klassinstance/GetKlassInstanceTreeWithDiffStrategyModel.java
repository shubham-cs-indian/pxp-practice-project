package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.model.assetinstance.IReferenceAssetModel;
import com.cs.core.runtime.interactor.model.assetinstance.ReferenceAssetModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

public class GetKlassInstanceTreeWithDiffStrategyModel
    implements IGetKlassInstanceTreeWithDiffStrategyModel {
  
  protected IKlassInstance                              klassInstance;
  protected IKlassInstanceDiffModel                     diffModel;
  protected Map<String, ? extends IReferenceAssetModel> referencedAssets = new HashMap<>();
  
  @Override
  public IKlassInstance getKlassInstance()
  {
    return klassInstance;
  }
  
  @Override
  public void setKlassInstance(IKlassInstance klassInstance)
  {
    this.klassInstance = klassInstance;
  }
  
  @Override
  public IKlassInstanceDiffModel getKlassInstanceDiff()
  {
    return diffModel;
  }
  
  @JsonDeserialize(as = KlassInstanceDiffModel.class)
  @Override
  public void setKlassInstanceDiff(IKlassInstanceDiffModel klassInstanceDiffModel)
  {
    this.diffModel = klassInstanceDiffModel;
  }
  
  @Override
  public Map<String, ? extends IReferenceAssetModel> getReferencedAssets()
  {
    return referencedAssets;
  }
  
  @JsonDeserialize(contentAs = ReferenceAssetModel.class)
  @Override
  public void setReferencedAssets(Map<String, ? extends IReferenceAssetModel> referencedAssets)
  {
    this.referencedAssets = referencedAssets;
  }
}
