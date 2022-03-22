package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Edge;
import java.util.Map;

public class Orient_Migration_Script_Remove_No_coupling_From_Contextual_Data_Transfer
    extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_Remove_No_coupling_From_Contextual_Data_Transfer(
      final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] {
        "POST|Orient_Migration_Script_Remove_No_coupling_From_Contextual_Data_Transfer/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    String query = "Select expand(outE('"
        + RelationshipLabelConstants.CONTEXTUAL_PROPAGABLE_PROPERTIES_PROPERTY_LINK + "')["
        + IDefaultValueChangeModel.COUPLING_TYPE + " = '" + CommonConstants.LOOSELY_COUPLED
        + "']) from " + VertexLabelConstants.CONTEXTUAL_PROPAGABLE_PROPERTIES;
    
    Iterable<Edge> edges = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    for (Edge edge : edges) {
      UtilClass.getGraph()
          .removeEdge(edge);
    }
    
    return null;
  }
}
