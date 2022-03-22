package com.cs.config.strategy.plugin.usecase.cache;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.Map;

public class DeleteConfigDetailCache extends AbstractOrientPlugin {
  
  public DeleteConfigDetailCache(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|DeleteConfigDetailCache/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    String processInstanceId = (String) requestMap.get(IIdParameterModel.ID);
    
    String query = "Select from " + VertexLabelConstants.CONFIG_DETAIL_CACHE + " where "
        + CommonConstants.PROCESS_INSTANCE_ID + " = " + EntityUtil.quoteIt(processInstanceId);
    
    Iterable<Vertex> removeableVertex = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex vertex : removeableVertex) {
      vertex.remove();
    }
    return requestMap;
  }
}
