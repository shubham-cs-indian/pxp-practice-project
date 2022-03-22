package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Edge;
import java.util.Map;

public class Orient_Migration_To_Remove_No_Coupling_From_Data_Transfer
    extends AbstractOrientMigration {
  
  public Orient_Migration_To_Remove_No_Coupling_From_Data_Transfer(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_To_Remove_No_Coupling_From_Data_Transfer/*" };
  }
  
  @Override
  protected Object executeMigration(Map<String, Object> requestMap) throws Exception
  {
    
    String query = "select expand(OUTE('" + RelationshipLabelConstants.HAS_RELATIONSHIP_ATTRIBUTE
        + "','" + RelationshipLabelConstants.HAS_RELATIONSHIP_TAG + "')["
        + IDefaultValueChangeModel.COUPLING_TYPE + " = '" + CommonConstants.LOOSELY_COUPLED
        + "']) from " + VertexLabelConstants.KLASS_RELATIONSHIP;
    Iterable<Edge> hasRelationshipAttributeOrTagEdges = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    for (Edge hasRelationshipAttributeOrTag : hasRelationshipAttributeOrTagEdges) {
      UtilClass.getGraph()
          .removeEdge(hasRelationshipAttributeOrTag);
    }
    
    return null;
  }
}
