package com.cs.config.strategy.plugin.usecase.processevent;

import com.cs.config.strategy.plugin.usecase.processevent.util.ProcessEventUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.processevent.IProcessEvent;
import com.cs.core.runtime.interactor.model.camunda.IGetCamundaProcessDefinitionResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetProcessDefinitionById extends AbstractOrientPlugin {
  
  public GetProcessDefinitionById(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetProcessDefinitionById/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> processIds = (List<String>) requestMap.get(IIdsListParameterModel.IDS);
    List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
        IProcessEvent.PROCESS_DEFINITION, IProcessEvent.PROCESS_DEFINITION_ID);
    Map<String, Object> processDefinitionMap = new HashMap<>();
    Iterable<Vertex> processNodes = ProcessEventUtils.getProcessDefinition(processIds,
        CommonConstants.CODE_PROPERTY);
    for (Vertex processNode : processNodes) {
      if (processNode != null) {
        Map<String, Object> processMap = UtilClass.getMapFromVertex(fieldsToFetch, processNode);
        Map<String, Object> internalProcessMap = new HashMap<>();
        internalProcessMap.put(IProcessEvent.PROCESS_DEFINITION,
            (String) processMap.get(IProcessEvent.PROCESS_DEFINITION));
        internalProcessMap.put(IProcessEvent.PROCESS_DEFINITION_ID,
            (String) processMap.get(IProcessEvent.PROCESS_DEFINITION_ID));
        processDefinitionMap.put((String) processMap.get(IProcessEvent.ID), internalProcessMap);
      }
    }
    Map<String, Object> response = new HashMap<>();
    response.put(IGetCamundaProcessDefinitionResponseModel.PROCESS_DEFINITION,
        processDefinitionMap);
    UtilClass.getGraph()
        .commit();
    return response;
  }
}
