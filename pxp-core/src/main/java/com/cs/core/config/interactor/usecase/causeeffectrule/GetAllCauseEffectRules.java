package com.cs.core.config.interactor.usecase.causeeffectrule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.causeeffectrule.ICauseEffectRulesModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.datarule.ICauseEffectRulesInformationModel;
import com.cs.core.config.strategy.usecase.causeeffectrule.IGetAllCauseEffectRulesStrategy;

@Service
public class GetAllCauseEffectRules extends
    AbstractGetConfigInteractor<ICauseEffectRulesModel, IListModel<ICauseEffectRulesInformationModel>>
    implements IGetAllCauseEffectRules {
  
  @Autowired
  IGetAllCauseEffectRulesStrategy orientGetCauseEffectRulesStrategy;
  
  @Override
  public IListModel<ICauseEffectRulesInformationModel> executeInternal(
      ICauseEffectRulesModel conditionModel) throws Exception
  {
    return orientGetCauseEffectRulesStrategy.execute(conditionModel);
  }
}
