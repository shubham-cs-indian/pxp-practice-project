package com.cs.core.config.interactor.usecase.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.klass.IBulkSaveKlassesService;

@Component
public class BulkSaveKlasses
    extends AbstractSaveConfigInteractor<IListModel<IKlassSaveModel>, IPluginSummaryModel>
    implements IBulkSaveKlasses {
  
  @Autowired
  IBulkSaveKlassesService bulkSaveKlassService;
  
  @Override
  public IPluginSummaryModel executeInternal(IListModel<IKlassSaveModel> klassListModel) throws Exception
  {
    return bulkSaveKlassService.execute(klassListModel);
  }
}
