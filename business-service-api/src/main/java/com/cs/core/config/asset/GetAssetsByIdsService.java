package com.cs.core.config.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.asset.IGetAssetsByIdsStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class GetAssetsByIdsService
    extends AbstractGetConfigService<IIdsListParameterModel, IListModel<IKlass>>
    implements IGetAssetsByIdsService {
  
  // TODO: Either this has to be removed or orient implementation should be
  // there
  @Autowired
  IGetAssetsByIdsStrategy neo4jGetAssetsByIdsStrategy;
  
  @Override
  public IListModel<IKlass> executeInternal(IIdsListParameterModel dataModel) throws Exception
  {
    return neo4jGetAssetsByIdsStrategy.execute(dataModel);
  }
}
