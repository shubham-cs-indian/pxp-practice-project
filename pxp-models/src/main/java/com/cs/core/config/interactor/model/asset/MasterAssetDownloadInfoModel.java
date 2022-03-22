package com.cs.core.config.interactor.model.asset;

import com.cs.core.runtime.interactor.model.assetinstance.ICoverFlowModel;

public class MasterAssetDownloadInfoModel extends CoverFlowModel
    implements IMasterAssetDownloadInfoModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected String          label;
  protected String          masterAssetClassId;
  protected String          masterAssetMediaName;
  
  public MasterAssetDownloadInfoModel(ICoverFlowModel coverFlowModel, String id, String label,
      String masterAssetClassId, String masterAssetMediaName)
  {
    super(coverFlowModel.getAssetObjectKey(), coverFlowModel.getFileName(),
        coverFlowModel.getType());
    this.id = id;
    this.label = label;
    this.masterAssetClassId = masterAssetClassId;
    this.masterAssetMediaName = masterAssetMediaName;
  }
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
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
  
  @Override
  public String getMasterAssetClassId()
  {
    return masterAssetClassId;
  }

  @Override
  public void setMasterAssetClassId(String masterAssetClassId)
  {
    this.masterAssetClassId = masterAssetClassId;
  }
  
  @Override
  public String getMasterAssetMediaName() {
    return this.masterAssetMediaName;
  }

  @Override
  public void setMasterAssetMediaName(String masterAssetMediaName)
  {
    this.masterAssetMediaName = masterAssetMediaName;
  }
}
