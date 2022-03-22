package com.cs.core.runtime.interactor.usecase.workflow.processinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.runtime.interactor.model.assetinstance.IGetAssetDetailsResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Component
public class GetBGPJobLogData extends AbstractRuntimeInteractor<IIdParameterModel, IGetAssetDetailsResponseModel>
    implements IGetBGPJobLogData {
  
  @Autowired
  protected IGetBGPJobLogDataService getBGPJobLogDataService;
  
  @Override
  public IGetAssetDetailsResponseModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getBGPJobLogDataService.execute(dataModel);
  }
}
