package com.cs.core.config.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.klass.IBulkCreateKlassesService;
import com.cs.core.config.strategy.usecase.klass.IBulkCreateKlassStrategy;

@Component
public class BulkCreateKlassesService
    extends AbstractCreateConfigService<IListModel<IKlassModel>, IPluginSummaryModel>
    implements IBulkCreateKlassesService {
  
  @Autowired
  IBulkCreateKlassStrategy bulkCreateKlassStrategy;
  
  @Override
  public IPluginSummaryModel executeInternal(IListModel<IKlassModel> klassListModel) throws Exception
  {
    return bulkCreateKlassStrategy.execute(klassListModel);
  }
}
