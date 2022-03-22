package com.cs.config.strategy.plugin.usecase.dtp.idsserver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.tinkerpop.blueprints.Vertex;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.dam.config.interactor.entity.idsserver.IInDesignServerInstance;
import com.cs.dam.config.interactor.model.idsserver.IINDSPingTaskRequestModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

/**
 * Get details of all InDesign server instances
 * @author mrunali.dhenge
 *
 */

public class GetAllInDesignServerInstances extends AbstractOrientPlugin {
  
  public GetAllInDesignServerInstances(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllInDesignServerInstances/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    Map<String, Object> returnMap = new HashMap<String, Object>();
    Map<String, Object> loadBalancerMap = new HashMap<>();
    loadBalancerMap.put(IInDesignServerInstance.PORT, "");
    loadBalancerMap.put(IInDesignServerInstance.HOST_NAME, "");
    loadBalancerMap.put(IInDesignServerInstance.STATUS, "");
    try {
      
      Vertex loadBalancerConfig = UtilClass.getVertexById(
          VertexLabelConstants.INDESIGN_LOAD_BALANCER_INSTANCE,
          VertexLabelConstants.INDESIGN_LOAD_BALANCER_INSTANCE);
      loadBalancerMap = UtilClass.getMapFromVertex(Arrays.asList(IInDesignServerInstance.HOST_NAME,
          IInDesignServerInstance.PORT, IInDesignServerInstance.ID), loadBalancerConfig);
      
    }
    catch (NotFoundException e) {
      // No need throw exception here
    }
    returnMap.put(IINDSPingTaskRequestModel.INDS_LOAD_BALANCER, loadBalancerMap);
    
    Iterable<Vertex> iterable = UtilClass.getGraph()
        .command(new OCommandSQL("select from " + VertexLabelConstants.INDESIGN_SERVER_INSTANCE))
        .execute();
    
    List<Map<String, Object>> indesignServerInstances = new ArrayList<>();
    
    for (Vertex indsInstanceNode : iterable) {
      indesignServerInstances
          .add(UtilClass.getMapFromVertex(Arrays.asList(IInDesignServerInstance.HOST_NAME,
              IInDesignServerInstance.PORT, IInDesignServerInstance.ID), indsInstanceNode));
    }
    
    returnMap.put(IINDSPingTaskRequestModel.SERVERS_TO_PING, indesignServerInstances);
    
    return returnMap;
  }
  
}
