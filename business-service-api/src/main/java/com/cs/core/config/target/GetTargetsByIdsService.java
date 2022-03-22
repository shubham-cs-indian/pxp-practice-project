package com.cs.core.config.target;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.target.IGetTargetsByIdsStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class GetTargetsByIdsService
    extends AbstractGetConfigService<IIdsListParameterModel, IListModel<IKlass>>
    implements IGetTargetsByIdsService {
  
  @Autowired
  IGetTargetsByIdsStrategy getTargetsByIdsStrategy;
  
  @Override
  public IListModel<IKlass> executeInternal(IIdsListParameterModel dataModel) throws Exception
  {
    return getTargetsByIdsStrategy.execute(dataModel);
  }
}
