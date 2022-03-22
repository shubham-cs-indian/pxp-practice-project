package com.cs.di.runtime.interactor.process;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetAllProcessComponents
    extends IRuntimeInteractor<IModel, IListModel<IIdLabelModel>> {
}
