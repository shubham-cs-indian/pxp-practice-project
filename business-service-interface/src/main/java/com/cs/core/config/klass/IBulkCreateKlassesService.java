package com.cs.core.config.klass;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;

public interface IBulkCreateKlassesService
    extends ICreateConfigService<IListModel<IKlassModel>, IPluginSummaryModel> {
  
}
