package com.cs.core.config.interactor.usecase.target;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.interactor.model.target.ITargetModel;
import com.cs.core.config.target.IGetAllTargetsService;

@Service
public class GetAllTargets
    extends AbstractGetConfigInteractor<ITargetModel, IListModel<IKlassInformationModel>>
    implements IGetAllTargets {
  
  @Autowired
  IGetAllTargetsService getAllTargetsService;
  
  @Override
  public IListModel<IKlassInformationModel> executeInternal(ITargetModel model) throws Exception
  {
    return getAllTargetsService.execute(model);
  }
}
