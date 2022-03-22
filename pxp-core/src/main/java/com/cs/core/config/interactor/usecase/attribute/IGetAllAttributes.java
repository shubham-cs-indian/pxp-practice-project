package com.cs.core.config.interactor.usecase.attribute;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.attribute.IAttributeModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetAllAttributes
    extends IGetConfigInteractor<IIdParameterModel, IListModel<IAttributeModel>> {
  
}
