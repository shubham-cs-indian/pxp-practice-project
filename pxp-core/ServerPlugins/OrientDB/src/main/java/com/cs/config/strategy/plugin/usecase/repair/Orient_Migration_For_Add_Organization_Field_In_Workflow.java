package com.cs.config.strategy.plugin.usecase.repair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.processevent.IProcessEvent;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.workflow.base.WorkflowType;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

public class Orient_Migration_For_Add_Organization_Field_In_Workflow extends AbstractOrientMigration{

  public Orient_Migration_For_Add_Organization_Field_In_Workflow(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_For_Add_Organization_Field_In_Workflow/*" };
  }

  @Override
  protected Object executeMigration(Map<String, Object> requestMap) throws Exception
  {
    List<String> workflowTypes = Arrays.asList("SCHEDULED_WORKFLOW", "JMS_WORKFLOW", "USER_SCHEDULED_WORKFLOW");
    String query = "select from " + VertexLabelConstants.PROCESS_EVENT;
    Iterable<Vertex> processEventNodes = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
    Iterator<Vertex> iterator = processEventNodes.iterator();
    while (iterator.hasNext()) {
      Vertex processNode = iterator.next();
      String workflowType = processNode.getProperty(IProcessEvent.WORKFLOW_TYPE);
      if (workflowTypes.contains(workflowType)) {
        if(!processNode.getEdges(Direction.OUT, RelationshipLabelConstants.HAS_LINKED_ORGANIZATIONS).iterator().hasNext())
        processNode.addEdge(RelationshipLabelConstants.HAS_LINKED_ORGANIZATIONS, UtilClass.getVertexByCode("-1", VertexLabelConstants.ORGANIZATION));
      }
      else {
        processNode.setProperty(IProcessEvent.ORGANIZATIONS_IDS, new ArrayList<>());
      }
    }
    
    UtilClass.getGraph().commit();
    return null;
  }
  
}
