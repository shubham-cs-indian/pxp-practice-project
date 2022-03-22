package com.cs.core.config.interactor.model.asset;

import com.cs.core.runtime.interactor.model.assetinstance.IIdLabelCodeDownloadPermissionModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;

public class IdLabelCodeDownloadPermissionModel extends IdLabelCodeModel
    implements IIdLabelCodeDownloadPermissionModel {
  
  private static final long serialVersionUID = 1L;
  protected Boolean         canDownload;
  
  @Override
  public Boolean getCanDownload()
  {
    return canDownload;
  }
  
  @Override
  public void setCanDownload(Boolean canDownload)
  {
    this.canDownload = canDownload;
  }
}
