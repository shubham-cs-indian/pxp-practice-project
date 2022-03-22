package com.cs.core.config.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.asset.IAssetKlassSaveModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.strategy.usecase.asset.IBulkSaveAssetStrategy;

@Component
public class BulkSaveAssetsService
    extends AbstractSaveConfigService<IListModel<IAssetKlassSaveModel>, IPluginSummaryModel>
    implements IBulkSaveAssetsService {
  
  @Autowired
  IBulkSaveAssetStrategy bulkSaveAssetStrategy;
  
  @Override
  public IPluginSummaryModel executeInternal(IListModel<IAssetKlassSaveModel> klassListModel)
      throws Exception
  {
    return bulkSaveAssetStrategy.execute(klassListModel);
  }
}
