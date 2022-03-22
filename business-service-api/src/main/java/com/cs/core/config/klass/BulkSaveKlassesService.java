package com.cs.core.config.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.klass.IBulkSaveKlassesService;
import com.cs.core.config.strategy.usecase.klass.IBulkSaveKlassStrategy;

@Component
public class BulkSaveKlassesService
    extends AbstractSaveConfigService<IListModel<IKlassSaveModel>, IPluginSummaryModel>
    implements IBulkSaveKlassesService {
  
  @Autowired
  IBulkSaveKlassStrategy bulkSaveKlassStrategy;
  
  @Override
  public IPluginSummaryModel executeInternal(IListModel<IKlassSaveModel> klassListModel) throws Exception
  {
    return bulkSaveKlassStrategy.execute(klassListModel);
  }
}
