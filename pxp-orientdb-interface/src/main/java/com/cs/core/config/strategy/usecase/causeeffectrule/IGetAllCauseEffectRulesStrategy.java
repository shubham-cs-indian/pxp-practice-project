package com.cs.core.config.strategy.usecase.causeeffectrule;

import com.cs.core.config.interactor.model.causeeffectrule.ICauseEffectRulesModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.datarule.ICauseEffectRulesInformationModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllCauseEffectRulesStrategy
    extends IConfigStrategy<ICauseEffectRulesModel, IListModel<ICauseEffectRulesInformationModel>> {
  
}
