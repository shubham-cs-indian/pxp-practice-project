package com.cs.core.config.interactor.usecase.target;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.target.IGetTargetsByIdsService;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class GetTargetsByIds
    extends AbstractGetConfigInteractor<IIdsListParameterModel, IListModel<IKlass>>
    implements IGetTargetsByIds {
  
  @Autowired
  IGetTargetsByIdsService getTargetsByIdsService;
  
  @Override
  public IListModel<IKlass> executeInternal(IIdsListParameterModel dataModel) throws Exception
  {
    return getTargetsByIdsService.execute(dataModel);
  }
}
