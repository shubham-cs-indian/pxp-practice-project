package com.cs.config.strategy.plugin.usecase.processevent;

import com.cs.config.strategy.plugin.usecase.processevent.util.ProcessEventUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.attribute.BulkSaveAttributeFailedException;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.processevent.IGetProcessEventModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.di.config.interactor.model.initializeworflowevent.IBulkProcessEventSaveResponseModel;
import com.cs.di.config.interactor.model.initializeworflowevent.IGetGridProcessEventsResponseModel;
import com.cs.di.config.interactor.model.initializeworflowevent.ISaveProcessEventModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({ "unchecked" })
public class BulkSaveProcessEvent extends AbstractOrientPlugin {
  
  public BulkSaveProcessEvent(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  protected List<String> propertiesToExclude = Arrays.asList(ISaveProcessEventModel.ADDED_KLASS_IDS,
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
      ISaveProcessEventModel.ADDED_ENDPOINT_IDS_FOR_PROCESS,
      ISaveProcessEventModel.DELETED_ENDPOINT_IDS_FOR_PROCESS,
      ISaveProcessEventModel.ADDED_ENDPOINT_IDS_FOR_COMPONENT,
      ISaveProcessEventModel.DELETED_ENDPOINT_IDS_FOR_COMPONENT, ISaveProcessEventModel.KLASS_IDS,
      ISaveProcessEventModel.TAXONOMY_IDS, ISaveProcessEventModel.ATTRIBUTE_IDS,
      ISaveProcessEventModel.TAG_IDS, ISaveProcessEventModel.ENDPOINT_IDS,
      ISaveProcessEventModel.ADDED_REFERENCES, ISaveProcessEventModel.DELETED_REFERENCES,
      ISaveProcessEventModel.ADDED_ROLES, ISaveProcessEventModel.DELETED_ROLES, 
      ISaveProcessEventModel.ADDED_NON_NATURE_KLASS_IDS, ISaveProcessEventModel.DELETED_NON_NATURE_KLASS_IDS,
      ISaveProcessEventModel.EVENT_TYPE, ISaveProcessEventModel.WORKFLOW_TYPE, ISaveProcessEventModel.TRIGGERING_TYPE,
      ISaveProcessEventModel.ACTION_SUB_TYPE);
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|BulkSaveProcessEvent/*" };
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    List<Map<String, Object>> listOfProcessEvents = (List<Map<String, Object>>) requestMap
        .get(IListModel.LIST);
    List<Map<String, Object>> listOfSuccessProcessEvents = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    Map<String, Object> configMap = ProcessEventUtils.createConfigDetails();
    
    for (Map<String, Object> processEventMap : listOfProcessEvents) {
      try {
        String processEventId = (String) processEventMap.get(CommonConstants.ID_PROPERTY);
        
        Vertex processEventNode = UtilClass.getVertexByCode(processEventId,
              VertexLabelConstants.PROCESS_EVENT);
        String workflowType = processEventNode.getProperty(ISaveProcessEventModel.WORKFLOW_TYPE);
        List<String> fieldsToExclude = ProcessEventUtils.validateMandataryFields(processEventNode, workflowType, propertiesToExclude);
        UtilClass.saveNode(processEventMap, processEventNode, fieldsToExclude);
        ProcessEventUtils.saveProcessReferencedElements(processEventNode,
            (HashMap<String, Object>) processEventMap);
        
        Map<String, Object> returnMap = new HashMap<String, Object>();
        
        returnMap.putAll(UtilClass.getMapFromNode(processEventNode));
        ProcessEventUtils.getConfigInformationForProcessInGrid(processEventNode, returnMap,
            configMap);
        listOfSuccessProcessEvents.add(returnMap);
      }
      catch (PluginException ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, null, null);
      }
      catch (Exception ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, null, null);
      }
    }
    
    if (listOfSuccessProcessEvents.isEmpty()) {
      throw new BulkSaveAttributeFailedException(failure.getExceptionDetails(),
          failure.getDevExceptionDetails());
    }
    
    UtilClass.getGraph()
        .commit();
    Map<String, Object> successMap = new HashMap<>();
    successMap.put(IGetGridProcessEventsResponseModel.PROCESS_EVENTS_LIST,
        listOfSuccessProcessEvents);
    successMap.put(IGetProcessEventModel.CONFIG_DETAILS, configMap);
    
    Map<String, Object> bulkSaveProcessEventsResponse = new HashMap<String, Object>();
    bulkSaveProcessEventsResponse.put(IBulkProcessEventSaveResponseModel.SUCCESS, successMap);
    bulkSaveProcessEventsResponse.put(IBulkProcessEventSaveResponseModel.FAILURE, failure);
    return bulkSaveProcessEventsResponse;
  }
}
