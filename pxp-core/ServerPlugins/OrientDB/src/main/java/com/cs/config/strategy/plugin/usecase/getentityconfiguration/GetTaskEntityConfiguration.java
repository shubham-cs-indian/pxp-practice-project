package com.cs.config.strategy.plugin.usecase.getentityconfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.interactor.model.configdetails.IUsageSummary;
import com.cs.core.runtime.interactor.constants.application.EntityConfigurationConstants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

public class GetTaskEntityConfiguration extends AbstractOrientPlugin  {
  public GetTaskEntityConfiguration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
    // TODO Auto-generated constructor stub
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetTaskEntityConfiguration/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> Ids = (List<String>) requestMap.get(IGetEntityConfigurationRequestModel.IDS);
    Map<String, Object> responseMap = new HashMap<String, Object>();
    Map<String, Object> referenceData = new HashMap<String, Object>();
    List<Map<String, Object>> usageSummary = new ArrayList<Map<String, Object>>();
    for (String id : Ids) {
      Map<String, Object> details = getTaskEntityUsageSummary(id);
      GetEntityConfigurationUtils.setReferenceDataAndUsageSummary(details, referenceData,
          usageSummary);
    }
    
    responseMap.put(IGetEntityConfigurationResponseModel.REFERENCE_DATA, referenceData);
    responseMap.put(IGetEntityConfigurationResponseModel.USAGE_SUMMARY, usageSummary);
    return responseMap;
  }
  
  /**
   * Description: get usage summary of task type entity
   *
   * @author vidhi.patel
   * @param id
   * @return
   * @throws Exception
   */
  private Map<String, Object> getTaskEntityUsageSummary(String id) throws Exception
  {
    Map<String, Object> details = new HashMap<String, Object>();
    Map<String, Object> referenceData = new HashMap<String, Object>();
    List<Object> usedBy = new ArrayList<Object>();
    Vertex taskNode = (Vertex) UtilClass.getVertexByIndexedId(id,
        VertexLabelConstants.ENTITY_TYPE_TASK);
    Map<String, Object> usageSummary = GetEntityConfigurationUtils.getUsageSummary(taskNode);
    usageSummary.put(IUsageSummary.TOTAL_COUNT, 0);
    
    String nodeId = taskNode.getId()
        .toString();
    
    Iterable<Vertex> vertices = GetEntityConfigurationUtils.getVerticesByGraphAPI(taskNode,
        Direction.IN, RelationshipLabelConstants.HAS_TASK);
    List<String> linkedIds = new ArrayList<String>();
    Integer count = new Integer(0);
    
    List<Vertex> klass = new ArrayList<Vertex>();
    List<Vertex> taxonomy = new ArrayList<Vertex>();
    
    for (Vertex vertex : vertices) {
      Map<String, Object> configDetail = GetEntityConfigurationUtils.getUsageSummary(vertex);
      String cid = (String) configDetail.get(IIdLabelCodeModel.ID);
      linkedIds.add(cid);
      referenceData.put(cid, configDetail);
      count++;
      
      if (vertex.getProperty("@class")
          .equals(VertexLabelConstants.ATTRIBUTION_TAXONOMY)) {
        taxonomy.add(vertex);
      }
      else {
        klass.add(vertex);
      }
    }
    
    /* Klass */
    GetEntityConfigurationUtils.elaborateKlass(klass, referenceData, usedBy, usageSummary);
    
    /* Taxonomy */
    GetEntityConfigurationUtils.setConfigurationDetails(taxonomy, referenceData, usedBy,
        usageSummary, EntityConfigurationConstants.TAXONOMY);
    
    usageSummary.put(IUsageSummary.USED_BY, usedBy);
    details.put(IGetEntityConfigurationResponseModel.REFERENCE_DATA, referenceData);
    details.put(IGetEntityConfigurationResponseModel.USAGE_SUMMARY, usageSummary);
    return details;
  }
}
