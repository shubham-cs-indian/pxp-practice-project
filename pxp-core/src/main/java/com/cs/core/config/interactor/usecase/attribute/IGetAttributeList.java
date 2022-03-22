package com.cs.core.config.interactor.usecase.attribute;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IAttributeInfoModel;

public interface IGetAttributeList
    extends IGetConfigInteractor<IIdParameterModel, IListModel<IAttributeInfoModel>> {
  
}
