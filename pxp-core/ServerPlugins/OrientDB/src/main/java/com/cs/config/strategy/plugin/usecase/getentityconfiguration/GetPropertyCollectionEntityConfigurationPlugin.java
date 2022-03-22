package com.cs.config.strategy.plugin.usecase.getentityconfiguration;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetPropertyCollectionEntityConfigurationPlugin extends AbstractOrientPlugin {
  
  public GetPropertyCollectionEntityConfigurationPlugin(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetPropertyCollectionEntityConfigurationPlugin/*" };
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
      Map<String, Object> details = getPropertyCollectionEntityUsageSummary(id);
      GetEntityConfigurationUtils.setReferenceDataAndUsageSummary(details, referenceData,
          usageSummary);
    }
    responseMap.put(IGetEntityConfigurationResponseModel.REFERENCE_DATA, referenceData);
    responseMap.put(IGetEntityConfigurationResponseModel.USAGE_SUMMARY, usageSummary);
    return responseMap;
  }
  
  /**
   * Description: get usage summary of property collection type entity
   *
   * @author Snehal.Kurkute
   * @param id
   * @return
   * @throws Exception
   */
  private Map<String, Object> getPropertyCollectionEntityUsageSummary(String id) throws Exception
  {
    Map<String, Object> details = new HashMap<String, Object>();
    Map<String, Object> referenceData = new HashMap<String, Object>();
    List<Object> usedBy = new ArrayList<Object>();
    Vertex propertyCollectionNode = (Vertex) UtilClass.getVertexByIndexedId(id,
        VertexLabelConstants.PROPERTY_COLLECTION);
    Map<String, Object> usageSummary = GetEntityConfigurationUtils
        .getUsageSummary(propertyCollectionNode);
    usageSummary.put(IUsageSummary.TOTAL_COUNT, 0);
    
    String nodeId = propertyCollectionNode.getId()
        .toString();
    
    String query = GetEntityConfigurationUtils.getTwoLevelQuery(nodeId, Direction.OUT,
        Direction.OUT, RelationshipLabelConstants.PROPERTY_COLLECTION_OF,
        RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF);
    
    Iterable<Vertex> vertices = GetEntityConfigurationUtils.getVerticesByQuery(query);
    
    List<String> linkedIds = new ArrayList<String>();
    Integer count = new Integer(0);
    
    List<Vertex> taxonomy = new ArrayList<Vertex>();
    List<Vertex> klass = new ArrayList<Vertex>();
    
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
    
    /* Taxonomy */
    GetEntityConfigurationUtils.setConfigurationDetails(taxonomy, referenceData, usedBy,
        usageSummary, EntityConfigurationConstants.TAXONOMY);
    
    /* klass */
    GetEntityConfigurationUtils.elaborateKlass(klass, referenceData, usedBy, usageSummary);
    
    /* Mapping */
    GetEntityConfigurationUtils.setTwoLevelQuery(nodeId, Direction.IN, Direction.IN, referenceData,
        usedBy, usageSummary, RelationshipLabelConstants.MAPPED_TO_ENTITY,
        RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_CONFIG_RULE, EntityConfigurationConstants.MAPPING);

    usageSummary.put(IUsageSummary.USED_BY, usedBy);
    details.put(IGetEntityConfigurationResponseModel.REFERENCE_DATA, referenceData);
    details.put(IGetEntityConfigurationResponseModel.USAGE_SUMMARY, usageSummary);
    return details;
  }
}
