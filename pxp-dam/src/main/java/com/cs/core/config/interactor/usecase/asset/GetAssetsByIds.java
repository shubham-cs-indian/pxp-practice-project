package com.cs.core.config.interactor.usecase.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.asset.IGetAssetsByIdsService;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.asset.IGetAssetsByIdsStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class GetAssetsByIds
    extends AbstractGetConfigInteractor<IIdsListParameterModel, IListModel<IKlass>>
    implements IGetAssetsByIds {
  
  // TODO: Either this has to be removed or orient implementation should be
  // there
  @Autowired
  IGetAssetsByIdsService getAssetsByIdsService;
  
  @Override
  public IListModel<IKlass> executeInternal(IIdsListParameterModel dataModel) throws Exception
  {
    return getAssetsByIdsService.execute(dataModel);
  }
}
