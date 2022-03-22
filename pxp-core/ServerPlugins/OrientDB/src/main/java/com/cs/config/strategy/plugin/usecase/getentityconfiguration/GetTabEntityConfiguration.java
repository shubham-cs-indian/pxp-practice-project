package com.cs.config.strategy.plugin.usecase.getentityconfiguration;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.interactor.model.configdetails.IUsageSummary;
import com.cs.core.runtime.interactor.constants.application.EntityConfigurationConstants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetTabEntityConfiguration extends AbstractOrientPlugin {
  
  public GetTabEntityConfiguration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
    // TODO Auto-generated constructor stub
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetTabEntityConfiguration/*" };
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
      Map<String, Object> details = getTabEntityUsageSummary(id);
      GetEntityConfigurationUtils.setReferenceDataAndUsageSummary(details, referenceData,
          usageSummary);
    }
    
    responseMap.put(IGetEntityConfigurationResponseModel.REFERENCE_DATA, referenceData);
    responseMap.put(IGetEntityConfigurationResponseModel.USAGE_SUMMARY, usageSummary);
    return responseMap;
  }
  
  /**
   * Description: get usage summary of tab type entity
   *
   * @author yogesh.mandaokar
   * @param id
   * @return
   * @throws Exception
   */
  protected Map<String, Object> getTabEntityUsageSummary(String id) throws Exception
  {
    Map<String, Object> details = new HashMap<String, Object>();
    Map<String, Object> referenceData = new HashMap<String, Object>();
    List<Object> usedBy = new ArrayList<Object>();
    Vertex tabNode = UtilClass.getVertexByIndexedId(id, VertexLabelConstants.TAB);
    Map<String, Object> usageSummary = GetEntityConfigurationUtils.getUsageSummary(tabNode);
    usageSummary.put(IUsageSummary.TOTAL_COUNT, 0);
    
    Iterable<Vertex> vertices = GetEntityConfigurationUtils.getVerticesByGraphAPI(tabNode,
        Direction.IN, RelationshipLabelConstants.HAS_TAB);
    List<Vertex> propertyCollection = new ArrayList<Vertex>();
    List<Vertex> context = new ArrayList<Vertex>();
    List<Vertex> relationship = new ArrayList<Vertex>();
    for (Vertex vertex : vertices) {
      if (vertex.getProperty("@class")
          .equals(VertexLabelConstants.PROPERTY_COLLECTION)) {
        propertyCollection.add(vertex);
      }
      if (vertex.getProperty("@class")
          .equals(VertexLabelConstants.VARIANT_CONTEXT)) {
        context.add(vertex);
      }
      if (vertex.getProperty("@class")
          .equals(VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP)) {
        relationship.add(vertex);
      }
    }
    
    /* Property Collection */
    GetEntityConfigurationUtils.setConfigurationDetails(propertyCollection, referenceData, usedBy,
        usageSummary, EntityConfigurationConstants.PROPERTY_COLLECTION);
    
    /* Context */
    GetEntityConfigurationUtils.setConfigurationDetails(context, referenceData, usedBy,
        usageSummary, EntityConfigurationConstants.CONTEXT);
    
    /* Relationship */
    GetEntityConfigurationUtils.setConfigurationDetails(relationship, referenceData, usedBy,
        usageSummary, EntityConfigurationConstants.RELATIONSHIP);
    
    usageSummary.put(IUsageSummary.USED_BY, usedBy);
    details.put(IGetEntityConfigurationResponseModel.REFERENCE_DATA, referenceData);
    details.put(IGetEntityConfigurationResponseModel.USAGE_SUMMARY, usageSummary);
    return details;
  }
}
