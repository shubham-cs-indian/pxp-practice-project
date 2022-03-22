package com.cs.dam.runtime.interactor.model.assetinstance.linksharing;

import java.util.ArrayList;
import java.util.List;


public class BulkCreateAssetInstanceLinkRequestModel
    implements IBulkCreateAssetInstanceLinkRequestModel {
  
  private static final long serialVersionUID        = 1L;
  protected List<String>    masterAssetIds          = new ArrayList<>();
  protected List<String>    technicalVariantTypeIds = new ArrayList<>();
  protected Boolean         masterAssetShare;
  
  @Override
  public List<String> getMasterAssetIds()
  {
    return masterAssetIds;
  }
  
  @Override
  public void setMasterAssetIds(List<String> masterAssetIds)
  {
    this.masterAssetIds = masterAssetIds;
  }
  
  @Override
  public List<String> getTechnicalVariantTypeIds()
  {
    return technicalVariantTypeIds;
  }
  
  @Override
  public void setTechnicalVariantTypeIds(List<String> technicalVariantTypeIds)
  {
    this.technicalVariantTypeIds = technicalVariantTypeIds;
  }
  
  @Override
  public Boolean getMasterAssetShare()
  {
    return masterAssetShare;
  }
  
  @Override
  public void setMasterAssetShare(Boolean masterAssetShare)
  {
    this.masterAssetShare = masterAssetShare;
  }
  
}
