package com.cs.core.config.interactor.usecase.assetserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.assetstatus.IAssetUploadStatusCheckModel;
import com.cs.core.config.interactor.model.assetstatus.IAssetUploadStatusResultModel;
import com.cs.core.config.interactor.usecase.asset.IGetAssetUploadStatus;
import com.cs.core.config.strategy.usecase.swift.IGetAssetUploadStatusStrategy;

@Service
public class GetAssetUploadStatus
    extends AbstractGetConfigInteractor<IAssetUploadStatusCheckModel, IAssetUploadStatusResultModel>
    implements IGetAssetUploadStatus {
  
  @Autowired
  IGetAssetUploadStatusStrategy getAssetUploadStatusStrategy;
  
  @Override
  public IAssetUploadStatusResultModel executeInternal(IAssetUploadStatusCheckModel dataModel)
      throws Exception
  {
    return getAssetUploadStatusStrategy.execute(dataModel);
  }
}
