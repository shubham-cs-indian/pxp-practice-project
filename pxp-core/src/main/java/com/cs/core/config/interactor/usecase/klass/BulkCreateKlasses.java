package com.cs.core.config.interactor.usecase.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.klass.IBulkCreateKlassesService;
import com.cs.core.config.strategy.usecase.klass.IBulkCreateKlassStrategy;

@Component
public class BulkCreateKlasses
    extends AbstractCreateConfigInteractor<IListModel<IKlassModel>, IPluginSummaryModel>
    implements IBulkCreateKlasses {
  
  @Autowired
  IBulkCreateKlassesService bulkCreateKlassService;
  
  @Override
  public IPluginSummaryModel executeInternal(IListModel<IKlassModel> klassListModel) throws Exception
  {
    return bulkCreateKlassService.execute(klassListModel);
  }
}
