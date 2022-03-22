package com.cs.core.config.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.strategy.usecase.asset.IBulkCreateAssetStrategy;

@Component
public class BulkCreateAssetService
    extends AbstractCreateConfigService<IListModel<IAssetModel>, IPluginSummaryModel>
    implements IBulkCreateAssetsService {
  
  @Autowired
  IBulkCreateAssetStrategy bulkCreateAssetStrategy;
  
  @Override
  public IPluginSummaryModel executeInternal(IListModel<IAssetModel> dataModel) throws Exception
  {
    return bulkCreateAssetStrategy.execute(dataModel);
  }
}
