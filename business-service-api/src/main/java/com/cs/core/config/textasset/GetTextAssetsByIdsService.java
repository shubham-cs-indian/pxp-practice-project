package com.cs.core.config.textasset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.textasset.IGetTextAssetsByIdsStrategy;
import com.cs.core.config.textasset.IGetTextAssetsByIdsService;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Component
public class GetTextAssetsByIdsService
    extends AbstractGetConfigService<IIdsListParameterModel, IListModel<IKlass>>
    implements IGetTextAssetsByIdsService {
  
  @Autowired
  IGetTextAssetsByIdsStrategy getTextAssetsByIdsStrategy;
  
  @Override
  public IListModel<IKlass> executeInternal(IIdsListParameterModel dataModel) throws Exception
  {
    return getTextAssetsByIdsStrategy.execute(dataModel);
  }
}
