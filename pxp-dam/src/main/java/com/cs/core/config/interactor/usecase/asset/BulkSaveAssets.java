package com.cs.core.config.interactor.usecase.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.asset.IBulkSaveAssetsService;
import com.cs.core.config.interactor.model.asset.IAssetKlassSaveModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;

@Component
public class BulkSaveAssets
    extends AbstractSaveConfigInteractor<IListModel<IAssetKlassSaveModel>, IPluginSummaryModel>
    implements IBulkSaveAssets {
  
  @Autowired
  IBulkSaveAssetsService bulkSaveAssetService;
  
  @Override
  public IPluginSummaryModel executeInternal(IListModel<IAssetKlassSaveModel> klassListModel)
      throws Exception
  {
    return bulkSaveAssetService.execute(klassListModel);
  }
}
