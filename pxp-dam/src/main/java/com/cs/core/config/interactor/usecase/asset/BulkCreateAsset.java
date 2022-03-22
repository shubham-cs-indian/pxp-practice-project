package com.cs.core.config.interactor.usecase.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.asset.IBulkCreateAssetsService;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;

@Component
public class BulkCreateAsset
    extends AbstractCreateConfigInteractor<IListModel<IAssetModel>, IPluginSummaryModel>
    implements IBulkCreateAssets {
  
  @Autowired
  IBulkCreateAssetsService bulkCreateAssetService;
  
  @Override
  public IPluginSummaryModel executeInternal(IListModel<IAssetModel> dataModel) throws Exception
  {
    return bulkCreateAssetService.execute(dataModel);
  }
}
