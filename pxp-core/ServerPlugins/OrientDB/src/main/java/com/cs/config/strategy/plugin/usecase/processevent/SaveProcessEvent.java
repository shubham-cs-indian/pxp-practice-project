package com.cs.config.strategy.plugin.usecase.processevent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.processevent.util.ProcessEventUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.processevent.ProcessEventNotFoundException;
import com.cs.core.config.interactor.model.processevent.IGetProcessEventModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.di.config.interactor.model.initializeworflowevent.ISaveProcessEventModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.cs.workflow.base.WorkflowConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class SaveProcessEvent extends AbstractOrientPlugin {
  
  protected List<String> fieldsToExclude = Arrays.asList(ISaveProcessEventModel.ADDED_KLASS_IDS,
      ISaveProcessEventModel.DELETED_KLASS_IDS, ISaveProcessEventModel.ADDED_USERS,
      ISaveProcessEventModel.DELETED_USERS, ISaveProcessEventModel.ADDED_ORGANIZATIONS,
      ISaveProcessEventModel.DELETED_ORGANIZATIONS, ISaveProcessEventModel.ADDED_TASKS,
      ISaveProcessEventModel.DELETED_TASKS, ISaveProcessEventModel.ADDED_ATTRIBUTE_IDS,
      ISaveProcessEventModel.DELETED_ATTRIBUTE_IDS, ISaveProcessEventModel.ADDED_TAG_IDS,
      ISaveProcessEventModel.DELETED_TAG_IDS, ISaveProcessEventModel.ADDED_CONTEXTS,
      ISaveProcessEventModel.DELETED_CONTEXTS, ISaveProcessEventModel.ADDED_RELATIONSHIPS,
      ISaveProcessEventModel.DELETED_RELATIONSHIPS, ISaveProcessEventModel.ADDED_TAXONOMY_IDS,
      ISaveProcessEventModel.DELETED_TAXONOMY_IDS,
      ISaveProcessEventModel.ADDED_KLASS_IDS_FOR_COMPONENT,
      ISaveProcessEventModel.DELETED_KLASS_IDS_FOR_COMPONENT,
      ISaveProcessEventModel.ADDED_TAXONOMY_IDS_FOR_COMPONENT,
      ISaveProcessEventModel.DELETED_TAXONOMY_IDS_FOR_COMPONENT,
      ISaveProcessEventModel.ADDED_ATTRIBUTE_IDS_FOR_COMPONENT,
      ISaveProcessEventModel.DELETED_ATTRIBUTE_IDS_FOR_COMPONENT,
      ISaveProcessEventModel.ADDED_TAG_IDS_FOR_COMPONENT,
      ISaveProcessEventModel.DELETED_TAG_IDS_FOR_COMPONENT,
      ISaveProcessEventModel.ADDED_ENDPOINT_IDS_FOR_COMPONENT,
      ISaveProcessEventModel.DELETED_ENDPOINT_IDS_FOR_COMPONENT,
      ISaveProcessEventModel.ADDED_ENDPOINT_IDS_FOR_PROCESS,
      ISaveProcessEventModel.DELETED_ENDPOINT_IDS_FOR_PROCESS, ISaveProcessEventModel.KLASS_IDS,
      ISaveProcessEventModel.TAXONOMY_IDS, ISaveProcessEventModel.ATTRIBUTE_IDS,
      ISaveProcessEventModel.TAG_IDS, ISaveProcessEventModel.ENDPOINT_IDS,
      ISaveProcessEventModel.ADDED_REFERENCES, ISaveProcessEventModel.DELETED_REFERENCES,
      ISaveProcessEventModel.ADDED_ROLES, ISaveProcessEventModel.DELETED_ROLES,
      ISaveProcessEventModel.ADDED_NON_NATURE_KLASS_IDS,ISaveProcessEventModel.DELETED_NON_NATURE_KLASS_IDS,
      ISaveProcessEventModel.ADDED_DESTINATION_ORGANIZATIONS, ISaveProcessEventModel.DELETED_DESTINATION_ORGANIZATIONS,
      ISaveProcessEventModel.EVENT_TYPE, ISaveProcessEventModel.WORKFLOW_TYPE, ISaveProcessEventModel.TRIGGERING_TYPE,
      ISaveProcessEventModel.ACTION_SUB_TYPE, ISaveProcessEventModel.USECASES);
  
  public SaveProcessEvent(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> processEventMap) throws Exception
  {
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    HashMap<String, Object> configMap = new HashMap<String, Object>();
    System.out.println(ObjectMapperUtil.writeValueAsString(processEventMap));
    String processEventId = (String) processEventMap.get(CommonConstants.ID_PROPERTY);
    
    Vertex processEventNode = null;
    try {
      processEventNode = UtilClass.getVertexByIndexedId(processEventId,
          VertexLabelConstants.PROCESS_EVENT);
      String workflowType = (String) processEventNode.getProperty(ISaveProcessEventModel.WORKFLOW_TYPE);
      List<String> fieldToExcludes = ProcessEventUtils.validateMandataryFields(processEventNode, workflowType, fieldsToExclude);
      
      UtilClass.saveNode(processEventMap, processEventNode, fieldToExcludes);
      returnMap.putAll(UtilClass.getMapFromNode(processEventNode));
      ProcessEventUtils.saveProcessReferencedElements(processEventNode, processEventMap);
      UtilClass.getGraph()
          .commit();
      ProcessEventUtils.getProcessEventNodeWithConfigInformation(processEventNode, returnMap,
          configMap);
      ProcessEventUtils.getReferencedDetailsForSearchFilterComponent(returnMap, configMap);
      returnMap.put(IGetProcessEventModel.CONFIG_DETAILS, configMap);
    }
    catch (NotFoundException e) {
      throw new ProcessEventNotFoundException();
    }
    return returnMap;
  }

  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveProcessEvent/*" };
  }
}
