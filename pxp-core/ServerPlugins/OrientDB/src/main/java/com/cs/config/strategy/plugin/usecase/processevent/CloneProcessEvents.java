package com.cs.config.strategy.plugin.usecase.processevent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.processevent.util.ProcessEventUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.processevent.IProcessEvent;
import com.cs.core.config.interactor.exception.attribute.BulkSaveAttributeFailedException;
import com.cs.core.config.interactor.exception.validationontype.InvalidEventTypeInWorkFlowException;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.processevent.IGetProcessEventModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.InvalidTypeException;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.di.config.interactor.model.initializeworflowevent.IBulkProcessEventSaveResponseModel;
import com.cs.di.config.interactor.model.initializeworflowevent.ICloneProcessEventModel;
import com.cs.di.config.interactor.model.initializeworflowevent.IGetGridProcessEventsResponseModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.cs.workflow.base.EventType;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

@SuppressWarnings("unchecked")
public class CloneProcessEvents extends AbstractOrientPlugin {
  
  protected static List<String>     fieldsToExclude = Arrays
      .asList(ICloneProcessEventModel.ORIGINAL_ENTITY_ID,ICloneProcessEventModel.IS_EXECUTABLE);
  
  protected static OrientVertexType vertexType;
  
  public CloneProcessEvents(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CloneProcessEvents/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> processEventMap;
    Map<String, Object> configMap = new HashMap<String, Object>();
    List<Map<String, Object>> listOfProcessEvents = (List<Map<String, Object>>) requestMap
        .get(IListModel.LIST);
    List<Map<String, Object>> listOfSuccessProcessEvents = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    
    for (Map<String, Object> processEvent : listOfProcessEvents) {
      processEventMap = new HashMap<String, Object>();
      try {
        Vertex destProcessEventNode = cloneProcessEvent(processEvent);
        
        // Preparing return map
        processEventMap.putAll(UtilClass.getMapFromNode(destProcessEventNode));
        ProcessEventUtils.getConfigInformationForProcessInGrid(destProcessEventNode,
            processEventMap, configMap);
        listOfSuccessProcessEvents.add(processEventMap);
        
      }
      catch (InvalidTypeException e) {
        throw new InvalidEventTypeInWorkFlowException(e);
      }
      catch (PluginException ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex,
            (String) processEvent.get(ICloneProcessEventModel.ORIGINAL_ENTITY_ID), null);
      }
      catch (Exception ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex,
            (String) processEvent.get(ICloneProcessEventModel.ORIGINAL_ENTITY_ID), null);
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
    successMap.put(IGetGridProcessEventsResponseModel.COUNT, listOfSuccessProcessEvents.size());
    
    Map<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.put(IBulkProcessEventSaveResponseModel.SUCCESS, successMap);
    returnMap.put(IBulkProcessEventSaveResponseModel.FAILURE, failure);
    return returnMap;
  }
  
  /**
   * @param processEvent
   *          - Process to be cloned
   * @throws InvalidTypeException
   * @throws Exception
   * @return cloned vertex
   */
  public static Vertex cloneProcessEvent(Map<String, Object> processEvent)
      throws InvalidTypeException, Exception
  {
    // Get or Create vertex type
    if (vertexType == null) {
      vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.PROCESS_EVENT,
          CommonConstants.CODE_PROPERTY);
    }
    
    UtilClass.validateOnType(EventType.EVENT_TYPE_LIST,
        (String) processEvent.get(IProcessEvent.EVENT_TYPE), true);
    
    String srcId = (String) processEvent.get(ICloneProcessEventModel.ORIGINAL_ENTITY_ID);
    
    Vertex srcProcessEventNode = UtilClass.getVertexById(srcId, VertexLabelConstants.PROCESS_EVENT);
    
    // Creating process event node
    Vertex destProcessEventNode = UtilClass.createNode(processEvent, vertexType, fieldsToExclude);
    
    // Linking entities to process event node
    prepareLinkEntity(srcProcessEventNode, destProcessEventNode);
    return destProcessEventNode;
  }
  
  /**
   * This method creates a replica of edges between new process and the entity
   *
   * @param srcProcessEventNode
   *          - Source Process Event Node
   * @param destProcessEventNode
   *          - Destination Process Event Node
   * @throws Exception
   */
  private static void prepareLinkEntity(Vertex srcProcessEventNode, Vertex destProcessEventNode)
      throws Exception
  {
    UtilClass.copyEdgeWithoutProperties(srcProcessEventNode, destProcessEventNode,
        RelationshipLabelConstants.HAS_LINKED_KLASSES, Direction.OUT);
    UtilClass.copyEdgeWithoutProperties(srcProcessEventNode, destProcessEventNode,
        RelationshipLabelConstants.HAS_LINKED_NON_NATURE_KLASSES, Direction.OUT);
    UtilClass.copyEdgeWithoutProperties(srcProcessEventNode, destProcessEventNode,
        RelationshipLabelConstants.HAS_LINKED_USERS, Direction.OUT);
    UtilClass.copyEdgeWithoutProperties(srcProcessEventNode, destProcessEventNode,
        RelationshipLabelConstants.HAS_LINKED_ORGANIZATIONS, Direction.OUT);
    UtilClass.copyEdgeWithoutProperties(srcProcessEventNode, destProcessEventNode,
        RelationshipLabelConstants.HAS_LINKED_TASKS, Direction.OUT);
    UtilClass.copyEdgeWithoutProperties(srcProcessEventNode, destProcessEventNode,
        RelationshipLabelConstants.HAS_LINKED_ATTRIBUTES, Direction.OUT);
    UtilClass.copyEdgeWithoutProperties(srcProcessEventNode, destProcessEventNode,
        RelationshipLabelConstants.HAS_LINKED_TAGS, Direction.OUT);
    UtilClass.copyEdgeWithoutProperties(srcProcessEventNode, destProcessEventNode,
        RelationshipLabelConstants.HAS_LINKED_CONTEXTS, Direction.OUT);
    UtilClass.copyEdgeWithoutProperties(srcProcessEventNode, destProcessEventNode,
        RelationshipLabelConstants.HAS_LINKED_RELATIONSHIPS, Direction.OUT);
    UtilClass.copyEdgeWithoutProperties(srcProcessEventNode, destProcessEventNode,
        RelationshipLabelConstants.HAS_LINKED_KLASSES_FOR_COMPONENT, Direction.OUT);
    UtilClass.copyEdgeWithoutProperties(srcProcessEventNode, destProcessEventNode,
        RelationshipLabelConstants.HAS_LINKED_TAXONOMIES_FOR_COMPONENT, Direction.OUT);
    UtilClass.copyEdgeWithoutProperties(srcProcessEventNode, destProcessEventNode,
        RelationshipLabelConstants.HAS_LINKED_AUTHORIZATION_MAPPING, Direction.OUT);
    UtilClass.copyEdgeWithoutProperties(srcProcessEventNode, destProcessEventNode,
        RelationshipLabelConstants.HAS_LINKED_KLASSES_FOR_PROCESS, Direction.OUT);
    UtilClass.copyEdgeWithoutProperties(srcProcessEventNode, destProcessEventNode,
        RelationshipLabelConstants.HAS_LINKED_TAXONOMIES_FOR_PROCESS, Direction.OUT);
    UtilClass.copyEdgeWithoutProperties(srcProcessEventNode, destProcessEventNode,
        RelationshipLabelConstants.HAS_LINKED_ATTRIBUTES_FOR_PROCESS, Direction.OUT);
    UtilClass.copyEdgeWithoutProperties(srcProcessEventNode, destProcessEventNode,
        RelationshipLabelConstants.HAS_LINKED_TAGS_FOR_PROCESS, Direction.OUT);
    UtilClass.copyEdgeWithoutProperties(srcProcessEventNode, destProcessEventNode,
        RelationshipLabelConstants.HAS_LINKED_ENDPOINTS_FOR_COMPONENT, Direction.OUT);
    UtilClass.copyEdgeWithoutProperties(srcProcessEventNode, destProcessEventNode,
        RelationshipLabelConstants.HAS_LINKED_ENDPOINTS_FOR_PROCESS, Direction.OUT);
    UtilClass.copyEdgeWithoutProperties(srcProcessEventNode, destProcessEventNode,
        RelationshipLabelConstants.HAS_LINKED_NON_NATURE_KLASSES_FOR_PROCESS, Direction.OUT);

  }
}
