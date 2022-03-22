package com.cs.config.strategy.plugin.usecase.getentityconfiguration;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.interactor.model.configdetails.IUsageSummary;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.EntityConfigurationConstants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetContextEntityConfiguration extends AbstractOrientPlugin {
  
  public GetContextEntityConfiguration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
    // TODO Auto-generated constructor stub
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetContextEntityConfiguration/*" };
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
      Map<String, Object> details = getContextEntityUsageSummary(id);
      GetEntityConfigurationUtils.setReferenceDataAndUsageSummary(details, referenceData,
          usageSummary);
    }
    
    responseMap.put(IGetEntityConfigurationResponseModel.REFERENCE_DATA, referenceData);
    responseMap.put(IGetEntityConfigurationResponseModel.USAGE_SUMMARY, usageSummary);
    return responseMap;
  }
  
  /**
   * Description: get usage summary of context type entity
   *
   * @author Snehal.Kurkute
   * @param id
   * @return
   * @throws Exception
   */
  private Map<String, Object> getContextEntityUsageSummary(String id) throws Exception
  {
    Map<String, Object> details = new HashMap<String, Object>();
    Map<String, Object> referenceData = new HashMap<String, Object>();
    List<Object> usedBy = new ArrayList<Object>();
    Vertex contextNode = UtilClass.getVertexByIndexedId(id, VertexLabelConstants.VARIANT_CONTEXT);
    Map<String, Object> usageSummary = UtilClass
        .getMapFromVertex(Arrays.asList(IVariantContext.CODE, IVariantContext.LABEL,
            CommonConstants.CODE_PROPERTY, IVariantContext.TYPE), contextNode);
    usageSummary.put(IUsageSummary.TOTAL_COUNT, 0);
    
    String nodeId = contextNode.getId()
        .toString();
    
    String contextType = (String) usageSummary.remove(IVariantContext.TYPE);
    String query = "";
    switch (contextType) {
      case CommonConstants.ATTRIBUTE_VARIANT_CONTEXT:
        query = GetEntityConfigurationUtils.getTwoLevelQuery(nodeId, Direction.OUT, Direction.IN,
            RelationshipLabelConstants.VARIANT_CONTEXT_OF,
            RelationshipLabelConstants.HAS_KLASS_PROPERTY);
        
        Iterable<Vertex> vertices = GetEntityConfigurationUtils.getVerticesByQuery(query);
        
        List<Vertex> taxonomy = new ArrayList<Vertex>();
        List<Vertex> klass = new ArrayList<Vertex>();
        
        for (Vertex vertex : vertices) {
          if (vertex.getProperty("@class")
              .equals(VertexLabelConstants.ENTITY_TYPE_KLASS)) {
            klass.add(vertex);
          }
          else {
            taxonomy.add(vertex);
          }
        }
        
        /* klass */
        GetEntityConfigurationUtils.elaborateKlass(klass, referenceData, usedBy, usageSummary);
        
        /* Taxonomy(Attribute Context) */
        GetEntityConfigurationUtils.setConfigurationDetails(taxonomy, referenceData, usedBy,
            usageSummary, EntityConfigurationConstants.TAXONOMY_ATTRIBUTE_CONTEXT);
        break;
      
      case CommonConstants.PRODUCT_VARIANT:
        query = GetEntityConfigurationUtils.getTwoLevelQuery(nodeId, Direction.IN, Direction.IN,
            RelationshipLabelConstants.VARIANT_CONTEXT_OF,
            RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF);
        GetEntityConfigurationUtils.elaborateKlass(
            GetEntityConfigurationUtils.getVerticesByQuery(query), referenceData, usedBy,
            usageSummary);
        break;
      
      case CommonConstants.RELATIONSHIP_VARIANT:
        GetEntityConfigurationUtils.setTwoLevelQuery(nodeId, Direction.OUT, Direction.OUT,
            referenceData, usedBy, usageSummary, RelationshipLabelConstants.RELATIONSHIP_CONTEXT_OF,
            RelationshipLabelConstants.HAS_PROPERTY, EntityConfigurationConstants.RELATIONSHIP);
        break;
      
      default:
        GetEntityConfigurationUtils.elaborateKlass(
            GetEntityConfigurationUtils.getVerticesByGraphAPI(contextNode, Direction.IN,
                RelationshipLabelConstants.VARIANT_CONTEXT_OF),
            referenceData, usedBy, usageSummary);
    }
    
    usageSummary.put(IUsageSummary.USED_BY, usedBy);
    details.put(IGetEntityConfigurationResponseModel.REFERENCE_DATA, referenceData);
    details.put(IGetEntityConfigurationResponseModel.USAGE_SUMMARY, usageSummary);
    return details;
  }
}
