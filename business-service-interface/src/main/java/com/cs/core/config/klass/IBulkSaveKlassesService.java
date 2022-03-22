package com.cs.core.config.klass;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;

public interface IBulkSaveKlassesService
    extends ISaveConfigService<IListModel<IKlassSaveModel>, IPluginSummaryModel> {
  
}
