package com.cs.core.config.interactor.usecase.klass;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;

public interface IBulkCreateKlasses
    extends ICreateConfigInteractor<IListModel<IKlassModel>, IPluginSummaryModel> {
  
}
