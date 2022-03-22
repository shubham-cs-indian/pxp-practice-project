package com.cs.core.config.interactor.usecase.condition;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.condition.IConditionInformationModel;
import com.cs.core.config.interactor.model.condition.IConditionModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;

public interface IGetAllConditions
    extends IGetConfigInteractor<IConditionModel, IListModel<IConditionInformationModel>> {
  
}
