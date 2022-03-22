package com.cs.config.strategy.plugin.usecase.processevent;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.processevent.IProcessEvent;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.di.config.interactor.model.initializeworflowevent.IDeleteProcessEventResponseModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class DeleteProcessEvent extends AbstractOrientPlugin {
  
  public DeleteProcessEvent(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> processEventIds = (List<String>) requestMap.get("ids");
    List<String> deletedIds = new ArrayList<>();
    List<String> processDefinationIds = new ArrayList<>();
    
    for (String processEventId : processEventIds) {
      Vertex processEventNode = null ;
      try {
         processEventNode = UtilClass.getVertexByIndexedId(processEventId,
            VertexLabelConstants.PROCESS_EVENT);
      }catch(NotFoundException e) {
        //Exception is not handled at this point all transaction get rollback back.
        System.out.println("Process not found. Creation is in progress!");
      }
      if (processEventNode != null) {
        Iterable<Vertex> processEventSteps = processEventNode.getVertices(Direction.OUT,
            RelationshipLabelConstants.NEXT_STEP);
        Map<String, Vertex> deleteProcessSteps = new HashMap<>();
        deleteComponentInstances(processEventSteps, deleteProcessSteps);
        for (Vertex deleteProcessStep : deleteProcessSteps.values()) {
          deleteProcessStep.remove();
        }
        deletedIds.add(UtilClass.getCodeNew(processEventNode));
        processDefinationIds.add(processEventNode.getProperty(IProcessEvent.PROCESS_DEFINITION_ID));
        processEventNode.remove();
      }
    }
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IDeleteProcessEventResponseModel.SUCCESS, deletedIds);
    responseMap.put(IDeleteProcessEventResponseModel.PROCESS_DEFINATION_IDS, processDefinationIds);
    UtilClass.getGraph()
        .commit();
    return responseMap;
  }
  
  private void deleteComponentInstances(Iterable<Vertex> processEventSteps,
      Map<String, Vertex> deleteProcessSteps)
  {
    for (Vertex processEventStep : processEventSteps) {
      Iterable<Vertex> components = processEventStep.getVertices(Direction.OUT,
          RelationshipLabelConstants.COMPONENT_OF);
      for (Vertex component : components) {
        component.remove();
      }
      Iterable<Vertex> processEventInnerSteps = processEventStep.getVertices(Direction.OUT,
          RelationshipLabelConstants.NEXT_STEP);
      deleteProcessSteps.put(UtilClass.getCodeNew(processEventStep), processEventStep);
      if (processEventInnerSteps.iterator()
          .hasNext()) {
        deleteComponentInstances(processEventInnerSteps, deleteProcessSteps);
      }
    }
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|DeleteProcessEvent/*" };
  }
}
