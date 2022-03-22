package com.cs.core.config.strategy.usecase.target;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.summary.PluginSummaryModel;
import com.cs.core.config.interactor.model.target.ITargetModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("bulkCreateTargetStrategy")
public class OrientDBBulkCreateTargetStrategy extends OrientDBBaseStrategy
    implements IBulkCreateTargetStrategy {
  
  public static final String useCase = "BulkCreateTargets";
  
  @Override
  public IPluginSummaryModel execute(IListModel<ITargetModel> model) throws Exception
  {
    Map<String, Object> map = new HashMap<>();
    map.put("list", model);
    return execute(useCase, map, PluginSummaryModel.class);
  }
}
