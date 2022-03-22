package com.cs.core.config.interactor.usecase.governancetask;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigTaskInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetAllGovernanceTask
    extends IGetConfigInteractor<IIdParameterModel, IListModel<IConfigTaskInformationModel>> {
  
}
