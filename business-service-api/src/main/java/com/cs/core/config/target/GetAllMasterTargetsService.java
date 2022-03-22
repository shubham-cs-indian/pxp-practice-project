package com.cs.core.config.target;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.target.ITargetModel;
import com.cs.core.config.strategy.usecase.target.IGetAllMasterTargetsStrategy;

@Service
public class GetAllMasterTargetsService
    extends AbstractGetConfigService<ITargetModel, IListModel<ITargetModel>>
    implements IGetAllMasterMarketsService {
  
  @Autowired
  IGetAllMasterTargetsStrategy getAllMasterTargetsStrategy;
  
  @Override
  public IListModel<ITargetModel> executeInternal(ITargetModel model) throws Exception
  {
    return getAllMasterTargetsStrategy.execute(model);
  }
}
