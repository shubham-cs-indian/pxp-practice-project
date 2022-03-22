package com.cs.core.config.interactor.usecase.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.businessapi.tag.IGetAllAssetExtensionsService;
import com.cs.core.config.interactor.model.asset.IGetAssetExtensionsModel;
import com.cs.core.config.interactor.usecase.tag.IGetAllAssetExtensions;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class GetAllAssetExtensions
    extends AbstractGetConfigInteractor<IIdsListParameterModel, IGetAssetExtensionsModel>
    implements IGetAllAssetExtensions {
  
  @Autowired
  protected IGetAllAssetExtensionsService getAllAssetExtensions;
  
  @Override
  protected IGetAssetExtensionsModel executeInternal(IIdsListParameterModel model) throws Exception
  {
    return getAllAssetExtensions.execute(model);
  }
  
}
