package com.cs.config.strategy.plugin.usecase.dtp.idsserver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.exception.MultipleVertexFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.dam.config.interactor.entity.idsserver.IInDesignServerInstance;
import com.cs.dam.config.interactor.model.idsserver.IINDSConfigurationTaskRequestModel;
import com.cs.dam.config.interactor.model.idsserver.IINDSConfigurationTaskResponseModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class UpdateInDesignServerInstanceData extends AbstractOrientPlugin {
  
  /**
   * Update, add, delete indesign server instance information and save loadbalancer
   * @param iConfiguration
   */
  
  public UpdateInDesignServerInstanceData(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|UpdateInDesignServerInstanceData/*" };
  }
  
  @SuppressWarnings({ "unused", "unchecked" })
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    
    if (requestMap.get(IINDSConfigurationTaskRequestModel.INDS_LOAD_BALANCER) != null) {
      saveLoadBalancer(requestMap);
    }
    
    OrientVertexType vertexType = graph
        .getVertexType(VertexLabelConstants.INDESIGN_SERVER_INSTANCE);
    // Add new server instance
    List<Map<String, Object>> addedInstances = new ArrayList<>();
    List<Map<String, Object>> serverInstancesToAdd = (List<Map<String, Object>>) requestMap
        .get(IINDSConfigurationTaskRequestModel.SERVERS_TO_ADD);
    for (Map<String, Object> serverInstance : serverInstancesToAdd) {
      
      Iterable<Vertex> vertices = graph.getVertices(VertexLabelConstants.INDESIGN_SERVER_INSTANCE,
          new String[] { "hostName", "port" }, new String[] {
              (String) serverInstance.get("hostName"), (String) serverInstance.get("port") });
      
      boolean hasNext = vertices.iterator()
          .hasNext();
      
      if (!hasNext) {
        Vertex indsInstance = UtilClass.createNode(serverInstance, vertexType,
            Arrays.asList(IInDesignServerInstance.STATUS));
        addedInstances
            .add(UtilClass.getMapFromVertex(Arrays.asList(IInDesignServerInstance.HOST_NAME,
                IInDesignServerInstance.PORT, IInDesignServerInstance.ID), indsInstance));
      }
    }
    
    // Update existing server instances.
    List<Map<String, Object>> updateInstances = new ArrayList<>();
    List<Map<String, Object>> serverInstancesToUpdate = (List<Map<String, Object>>) requestMap
        .get(IINDSConfigurationTaskRequestModel.SERVERS_TO_UPDATE);
    for (Map<String, Object> serverInstance : serverInstancesToUpdate) {
      
      Vertex indsInstance = UtilClass.getVertexById(
          (String) serverInstance.get(IInDesignServerInstance.ID),
          VertexLabelConstants.INDESIGN_SERVER_INSTANCE);
      if (indsInstance != null) {
        Map<String, Object> serverInstanceKeyMap = UtilClass.getMapFromNode(indsInstance);
        Set<String> serverInstanceKeys = new HashSet<>(serverInstanceKeyMap.keySet());
        serverInstanceKeys.remove(IInDesignServerInstance.PORT);
        serverInstanceKeyMap.put(IInDesignServerInstance.PORT,
            (String) serverInstance.get(IInDesignServerInstance.PORT));
        UtilClass.saveNode(serverInstanceKeyMap, indsInstance,
            new ArrayList<String>(serverInstanceKeys));
        updateInstances
            .add(UtilClass.getMapFromVertex(Arrays.asList(IInDesignServerInstance.HOST_NAME,
                IInDesignServerInstance.PORT, IInDesignServerInstance.ID), indsInstance));
      }
    }
    // Remove server instance.
    List<Map<String, Object>> removedInstances = new ArrayList<>();
    List<Map<String, Object>> serverInstancesToRemove = (List<Map<String, Object>>) requestMap
        .get(IINDSConfigurationTaskRequestModel.SERVERS_TO_REMOVE);
    List<String> idsOfServersToRemove = new ArrayList<>();
    for (Map<String, Object> serverInstance : serverInstancesToRemove) {
      Vertex indsInstance = UtilClass.getVertexById(
          (String) serverInstance.get(IInDesignServerInstance.ID),
          VertexLabelConstants.INDESIGN_SERVER_INSTANCE);
      if (indsInstance != null) {
        removedInstances
            .add(UtilClass.getMapFromVertex(Arrays.asList(IInDesignServerInstance.HOST_NAME,
                IInDesignServerInstance.PORT, IInDesignServerInstance.ID), indsInstance));
        idsOfServersToRemove.add((String) serverInstance.get(IInDesignServerInstance.ID));
      }
    }
    List<String> removedInstanceIds = UtilClass.deleteNode(idsOfServersToRemove,
        VertexLabelConstants.INDESIGN_SERVER_INSTANCE);
    graph.commit();
    
    Iterable<Vertex> allVertices = graph
        .getVerticesOfClass(VertexLabelConstants.INDESIGN_SERVER_INSTANCE);
    List<Map<String, Object>> allInstances = new ArrayList<>();
    Iterator<Vertex> iterator = allVertices.iterator();
    while (iterator.hasNext()) {
      allInstances.add(UtilClass.getMapFromVertex(Arrays.asList(IInDesignServerInstance.HOST_NAME,
          IInDesignServerInstance.PORT, IInDesignServerInstance.ID), iterator.next()));
    }
 
    Map<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.put(IINDSConfigurationTaskResponseModel.ADDED_SERVERS, addedInstances);
    returnMap.put(IINDSConfigurationTaskResponseModel.REMOVED_SERVERS, removedInstances);
    returnMap.put(IINDSConfigurationTaskResponseModel.UPDATED_SERVERS, updateInstances);
    returnMap.put(IINDSConfigurationTaskResponseModel.All_SERVERS, allInstances);
    return returnMap;
  }
  
  /**
   * Save load balancer information 
   * @param requestMap
   * @return
   * @throws Exception
   */
  
  @SuppressWarnings("unchecked")
  protected Vertex saveLoadBalancer(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Map<String, Object> indsLoadBalancer = (Map<String, Object>) requestMap
        .get(IINDSConfigurationTaskRequestModel.INDS_LOAD_BALANCER);
    OrientVertexType loadBalVertexType = graph
        .getVertexType(VertexLabelConstants.INDESIGN_LOAD_BALANCER_INSTANCE);
    Vertex loadbalancerVertex = null;
    try {
      loadbalancerVertex = UtilClass.getVertexById(
          VertexLabelConstants.INDESIGN_LOAD_BALANCER_INSTANCE,
          VertexLabelConstants.INDESIGN_LOAD_BALANCER_INSTANCE);
    }
    catch (NotFoundException e) {
      loadbalancerVertex = null;
    }
    catch (MultipleVertexFoundException mvfe) {
      Iterable<Vertex> vertices = graph.getVertices(
          VertexLabelConstants.INDESIGN_LOAD_BALANCER_INSTANCE,
          new String[] { CommonConstants.CID_PROPERTY },
          new String[] { VertexLabelConstants.INDESIGN_LOAD_BALANCER_INSTANCE });
      Iterator<Vertex> iterator = vertices.iterator();
      while (iterator.hasNext()) {
        graph.removeVertex(iterator.next());
      }
    }
    if (loadbalancerVertex == null) {
      indsLoadBalancer.put(CommonConstants.ID_PROPERTY,
          VertexLabelConstants.INDESIGN_LOAD_BALANCER_INSTANCE);
      indsLoadBalancer.put(CommonConstants.CODE_PROPERTY,
          VertexLabelConstants.INDESIGN_LOAD_BALANCER_INSTANCE);
      Vertex indsInstance = UtilClass.createNode(indsLoadBalancer, loadBalVertexType,
          Arrays.asList(IInDesignServerInstance.STATUS));
    }
    else {
      Map<String, Object> loadBalancerMap = UtilClass
          .getMapFromVertex(Arrays.asList(IInDesignServerInstance.HOST_NAME,
              IInDesignServerInstance.PORT, IInDesignServerInstance.ID), loadbalancerVertex);
      if (loadbalancerVertex.getProperty(IInDesignServerInstance.HOST_NAME) != indsLoadBalancer.get(IInDesignServerInstance.HOST_NAME)
          || loadbalancerVertex.getProperty(IInDesignServerInstance.PORT) != indsLoadBalancer.get(IInDesignServerInstance.PORT)) {
        loadBalancerMap.put(IInDesignServerInstance.HOST_NAME,
            indsLoadBalancer.get(IInDesignServerInstance.HOST_NAME));
        loadBalancerMap.put(IInDesignServerInstance.PORT,
            indsLoadBalancer.get(IInDesignServerInstance.PORT));
        UtilClass.saveNode(loadBalancerMap, loadbalancerVertex, Arrays.asList());
      }
    }
    graph.commit();
    return loadbalancerVertex;
    
  }
}
