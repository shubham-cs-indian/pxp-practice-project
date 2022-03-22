package com.cs.config.strategy.plugin.usecase.processevent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.processevent.util.ProcessEventUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.processevent.IProcessEvent;
import com.cs.core.config.interactor.exception.validationontype.InvalidEventTypeInWorkFlowException;
import com.cs.core.config.interactor.model.processevent.IGetProcessEventModel;
import com.cs.core.exception.InvalidTypeException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.cs.workflow.base.WorkflowType;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

@SuppressWarnings("unchecked")
public class CreateProcessEvent extends AbstractOrientPlugin {
  
  public CreateProcessEvent(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> processEvent) throws Exception
  {
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.PROCESS_EVENT, CommonConstants.CODE_PROPERTY);
    
    try {
      UtilClass.validateOnType(WorkflowType.WORKFLOW_TYPE_LIST,
          (String) processEvent.get(IProcessEvent.WORKFLOW_TYPE), false);
    }
    catch (InvalidTypeException e) {
      throw new InvalidEventTypeInWorkFlowException(e);
    }
    
    UtilClass.checkDuplicateName((String) processEvent.get(CommonConstants.LABEL_PROPERTY), VertexLabelConstants.PROCESS_EVENT);  
    Vertex processEventNode = UtilClass.createNode(processEvent, vertexType, new ArrayList<>());
    Map<String, Object> processEventMapToReturn = new HashMap<String, Object>();
    HashMap<String, Object> configMap = new HashMap<String, Object>();
    processEventMapToReturn.putAll(UtilClass.getMapFromNode(processEventNode));
    ProcessEventUtils.getProcessEventNodeWithConfigInformation(processEventNode,
        (HashMap<String, Object>) processEventMapToReturn, configMap);
    processEventMapToReturn.put(IGetProcessEventModel.CONFIG_DETAILS, configMap);
    UtilClass.getGraph()
        .commit();
    return processEventMapToReturn;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateProcessEvent/*" };
  }
}
