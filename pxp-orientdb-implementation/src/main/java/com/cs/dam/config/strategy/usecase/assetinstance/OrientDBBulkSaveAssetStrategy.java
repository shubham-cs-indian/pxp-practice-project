package com.cs.dam.config.strategy.usecase.assetinstance;

import com.cs.core.config.interactor.model.asset.IAssetKlassSaveModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.summary.PluginSummaryModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.asset.IBulkSaveAssetStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("bulkSaveAssetStrategy")
public class OrientDBBulkSaveAssetStrategy extends OrientDBBaseStrategy
    implements IBulkSaveAssetStrategy {
  
  @Override
  public IPluginSummaryModel execute(IListModel<IAssetKlassSaveModel> model) throws Exception
  {
    Map<String, Object> map = new HashMap<>();
    map.put("list", model);
    return super.execute(OrientDBBaseStrategy.BULK_SAVE_ASSETS, map, PluginSummaryModel.class);
  }
}
