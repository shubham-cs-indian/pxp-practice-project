package com.cs.core.config.strategy.usecase.propertycollection;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.propertycollection.IPropertyCollectionModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.summary.PluginSummaryModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("bulkCreatePropertyCollectionsStrategy")
public class OrientDBBulkCreatePropertyCollectionsStrategy extends OrientDBBaseStrategy
    implements IBulkCreatePropertyCollectionsStrategy {
  
  public static final String useCase = "BulkCreatePropertyCollections";
  
  @Override
  public IPluginSummaryModel execute(IListModel<IPropertyCollectionModel> model) throws Exception
  {
    Map<String, Object> map = new HashMap<>();
    map.put("list", model);
    return execute(useCase, map, PluginSummaryModel.class);
  }
}
