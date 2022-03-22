package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.summary.PluginSummaryModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("bulkSaveKlassStrategy")
public class OrientDBBulkSaveKlassStrategy extends OrientDBBaseStrategy
    implements IBulkSaveKlassStrategy {
  
  @Override
  public IPluginSummaryModel execute(IListModel<IKlassSaveModel> model) throws Exception
  {
    Map<String, Object> map = new HashMap<>();
    map.put("list", model);
    return execute(BULK_SAVE_KLASSES, map, PluginSummaryModel.class);
  }
}
