package com.cs.config.strategy.plugin.usecase.processevent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.processevent.util.ProcessEventUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.processevent.IProcessEvent;
import com.cs.core.runtime.interactor.model.camunda.IGetCamundaProcessDefinitionResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("unchecked")
public class GetProcessDefinitionByProcessDefinitionId extends AbstractOrientPlugin {
  
  public GetProcessDefinitionByProcessDefinitionId(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetProcessDefinitionByProcessDefinitionId/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> processIds = (List<String>) requestMap.get(IIdsListParameterModel.IDS);
    List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY, IProcessEvent.PROCESS_DEFINITION,
        IProcessEvent.PROCESS_DEFINITION_ID, IProcessEvent.LABEL, IProcessEvent.MAPPINGS, IProcessEvent.AUTHORIZATION_MAPPING,
        IProcessEvent.ORGANIZATIONS_IDS, IProcessEvent.PHYSICAL_CATALOG_IDS);
    Map<String, Object> processDefinitionMap = new HashMap<>();
    Iterable<Vertex> processNodes = ProcessEventUtils.getProcessDefinition(processIds,
        IProcessEvent.PROCESS_DEFINITION_ID);
   
    Map<String, Object> response = new HashMap<>();
    for (Vertex processNode : processNodes) {
      if (processNode != null) {
        Map<String, Object> processMap = UtilClass.getMapFromVertex(fieldsToFetch, processNode);
        response.put(IProcessEvent.ID, processMap.get(IProcessEvent.ID));
        response.put(IProcessEvent.LABEL, processMap.get(IProcessEvent.LABEL));
        processDefinitionMap.put((String) processMap.get(IProcessEvent.PROCESS_DEFINITION_ID),
            (String) processMap.get(IProcessEvent.PROCESS_DEFINITION));
        response.put(IGetCamundaProcessDefinitionResponseModel.PROCESS_DEFINITION, processDefinitionMap);
        response.put(IGetCamundaProcessDefinitionResponseModel.MAPPING_IDS, processMap.get(IProcessEvent.MAPPINGS));
        response.put(IGetCamundaProcessDefinitionResponseModel.PARTNER_AUTHORIZATION_IDS, processMap.get(IProcessEvent.AUTHORIZATION_MAPPING));
        response.put(IGetCamundaProcessDefinitionResponseModel.ORGANIZATIONSIDS, processMap.get(IProcessEvent.ORGANIZATIONS_IDS));
        response.put(IGetCamundaProcessDefinitionResponseModel.PHYSICAL_CATALOG_IDS, processMap.get(IProcessEvent.PHYSICAL_CATALOG_IDS));
      }
    }
    
    UtilClass.getGraph().commit();
    return response;
  }
}
