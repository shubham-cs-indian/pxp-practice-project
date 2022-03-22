package com.cs.core.config.interactor.usecase.causeeffectrule;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.causeeffectrule.ICauseEffectRulesModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.datarule.ICauseEffectRulesInformationModel;

public interface IGetAllCauseEffectRules extends
    IGetConfigInteractor<ICauseEffectRulesModel, IListModel<ICauseEffectRulesInformationModel>> {
  
}
