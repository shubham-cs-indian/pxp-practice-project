package com.cs.dam.runtime.interactor.usecase.assetinstance.linksharing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.dam.runtime.assetinstance.linksharing.IGetAssetInstanceLinkService;

@Service
public class GetAssetInstanceLink extends AbstractRuntimeInteractor<IIdParameterModel, IIdParameterModel> implements IGetAssetInstanceLink {
  
  @Autowired
  protected IGetAssetInstanceLinkService getAssetInstanceLinkService;
  
  @Override
  protected IIdParameterModel executeInternal(IIdParameterModel model) throws Exception
  {
    return getAssetInstanceLinkService.execute(model);
  }
}
