package com.cs.core.config.interactor.usecase.klass;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;

public interface IBulkSaveKlasses
    extends ISaveConfigInteractor<IListModel<IKlassSaveModel>, IPluginSummaryModel> {
  
}
