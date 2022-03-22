package com.cs.core.config.target;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.interactor.model.target.ITargetModel;
import com.cs.core.config.strategy.usecase.target.IGetAllTargetsStrategy;

@Service
public class GetAllTargetsService
    extends AbstractGetConfigService<ITargetModel, IListModel<IKlassInformationModel>>
    implements IGetAllTargetsService {
  
  @Autowired
  IGetAllTargetsStrategy getAllTargetsStrategy;
  
  @Override
  public IListModel<IKlassInformationModel> executeInternal(ITargetModel model) throws Exception
  {
    return getAllTargetsStrategy.execute(model);
  }
}
