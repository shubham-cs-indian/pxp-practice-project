package com.cs.dam.runtime.interactor.usecase.assetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.assetinstance.IBulkAssetDownloadResponseModel;
import com.cs.core.runtime.interactor.model.assetinstance.IBulkAssetDownloadWithVariantsModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.dam.runtime.assetinstance.IGetBulkAssetObjectIdService;


@Service
public class GetBulkAssetObjectId
    extends AbstractRuntimeInteractor<IBulkAssetDownloadWithVariantsModel, IBulkAssetDownloadResponseModel> implements IGetBulkAssetObjectId {

  @Autowired
  protected IGetBulkAssetObjectIdService getBulkAssetObjectIdService;

  @Override
  public IBulkAssetDownloadResponseModel executeInternal(IBulkAssetDownloadWithVariantsModel reqModel) throws Exception
  {
    return getBulkAssetObjectIdService.execute(reqModel);
  }
}
