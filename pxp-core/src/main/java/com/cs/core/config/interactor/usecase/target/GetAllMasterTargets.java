package com.cs.core.config.interactor.usecase.target;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.target.ITargetModel;
import com.cs.core.config.target.IGetAllMasterMarketsService;

@Service
public class GetAllMasterTargets
    extends AbstractGetConfigInteractor<ITargetModel, IListModel<ITargetModel>>
    implements IGetAllMasterMarkets {
  
  @Autowired
  IGetAllMasterMarketsService getAllMasterMargetService;
  
  @Override
  public IListModel<ITargetModel> executeInternal(ITargetModel model) throws Exception
  {
    return getAllMasterMargetService.execute(model);
  }
}
