package com.cs.core.config.interactor.usecase.tag;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IGetEntityModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetAvailableMasterTags
    extends IGetConfigInteractor<IIdParameterModel, IListModel<IGetEntityModel>> {
  
}
