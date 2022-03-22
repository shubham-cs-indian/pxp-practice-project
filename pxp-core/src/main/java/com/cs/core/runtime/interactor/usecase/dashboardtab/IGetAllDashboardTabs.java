package com.cs.core.runtime.interactor.usecase.dashboardtab;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetAllDashboardTabs
    extends IGetConfigInteractor<IModel, IListModel<IIdLabelCodeModel>> {
}
