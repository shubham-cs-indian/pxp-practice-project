package com.cs.core.config.strategy.usecase.attribute;

import com.cs.core.config.interactor.model.attribute.IAttributeModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.summary.PluginSummaryModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("bulkCreateAttributeStrategy")
public class OrientDBBulkCreateAttributeStrategy extends OrientDBBaseStrategy
    implements IBulkCreateAttributeStrategy {
  
  @Override
  public IPluginSummaryModel execute(IListModel<IAttributeModel> model) throws Exception
  {
    Map<String, Object> map = new HashMap<>();
    map.put("list", model);
    return super.execute(OrientDBBaseStrategy.BULK_CREATE_ATTRIBUTES, map,
        PluginSummaryModel.class);
  }
}
