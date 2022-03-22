package com.cs.core.config.strategy.usecase.tag;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.summary.PluginSummaryModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBBulkCreateTagsStrategy extends OrientDBBaseStrategy
    implements IBulkCreateTagsStrategy {
  
  public static final String useCase = "BulkCreateTags";
  
  @Override
  public IPluginSummaryModel execute(IListModel<ITagModel> model) throws Exception
  {
    Map<String, Object> map = new HashMap<>();
    map.put("list", model);
    return execute(useCase, map, PluginSummaryModel.class);
  }
}
