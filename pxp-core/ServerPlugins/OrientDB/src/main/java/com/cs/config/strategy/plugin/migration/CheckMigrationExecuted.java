package com.cs.config.strategy.plugin.migration;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class CheckMigrationExecuted extends AbstractOrientPlugin {
  
  public CheckMigrationExecuted(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CheckMigrationExecuted/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> ids = (List<String>) requestMap.get(IIdsListParameterModel.IDS);
    Iterable<Vertex> verticesByIds = UtilClass.getVerticesByIds(ids,
        VertexLabelConstants.MIGRATION);
    List<String> codes = UtilClass.getCodes(verticesByIds);
    ids.removeAll(codes);
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IIdsListParameterModel.IDS, ids);
    return responseMap;
  }
}
