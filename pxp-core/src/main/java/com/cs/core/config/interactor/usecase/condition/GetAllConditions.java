package com.cs.core.config.interactor.usecase.condition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.condition.IConditionInformationModel;
import com.cs.core.config.interactor.model.condition.IConditionModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.condition.IGetAllConditionsStrategy;

@Service
public class GetAllConditions
    extends AbstractGetConfigInteractor<IConditionModel, IListModel<IConditionInformationModel>>
    implements IGetAllConditions {
  
  @Autowired
  IGetAllConditionsStrategy orientGetConditionsStrategy;
  
  @Override
  public IListModel<IConditionInformationModel> executeInternal(IConditionModel conditionModel)
      throws Exception
  {
    return orientGetConditionsStrategy.execute(conditionModel);
  }
}
