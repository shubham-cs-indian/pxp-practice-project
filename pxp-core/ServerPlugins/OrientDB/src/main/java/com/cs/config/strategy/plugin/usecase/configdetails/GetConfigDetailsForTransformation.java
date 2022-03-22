package com.cs.config.strategy.plugin.usecase.configdetails;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.relationship.Relationship;
import com.cs.core.config.interactor.exception.context.ContextNotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetConfigDetailsForTransformation extends AbstractOrientPlugin {
  
  public GetConfigDetailsForTransformation(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  @SuppressWarnings("unchecked")
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> contextIdList = (List<String>) requestMap.get("contextIds");
    List<String> relationshipIdList = (List<String>) requestMap.get("relationshipIds");
    
    Map<String, Object> response = new HashMap<String, Object>();
    response.put("contextMap", getContextConfigDetails(contextIdList));
    response.put("relationshipMap", getRelationshipConfigDetails(relationshipIdList));
    return response;
  }

  private Map<String, Object> getContextConfigDetails(List<String> contextIdList) throws Exception, ContextNotFoundException
  {
    Iterable<Vertex>  contextNodes = getVerticesByIds(contextIdList, VertexLabelConstants.VARIANT_CONTEXT);
    
    Map<String, Object> contextMap = new HashMap<String, Object>();
    for (Vertex contextNode : contextNodes) {
      Map<String, Object> context = VariantContextUtils.getContext(contextNode, new HashMap<String, Object>());
      contextMap.put(contextNode.getProperty(CommonConstants.CODE_PROPERTY), context);
    }
    return contextMap;
  }
  
  private Map<String, Object> getRelationshipConfigDetails(List<String> relationshipIdList) throws Exception, ContextNotFoundException
  {
    Iterable<Vertex>  relationshipNodes = getVerticesByIds(relationshipIdList, VertexLabelConstants.ROOT_RELATIONSHIP); 

    HashMap<String, Object> relationshipMap = new HashMap<String, Object>();
    for (Vertex relationshipNode : relationshipNodes) {
      relationshipMap.put(relationshipNode.getProperty(CommonConstants.CODE_PROPERTY), relationshipNode.getProperty(Relationship.IS_NATURE));
    }
    return relationshipMap;
  }
  
  private  Iterable<Vertex> getVerticesByIds(Collection<String> ids, String entityLabel) throws Exception
  {
    String query = "SELECT FROM " + entityLabel;
    if (ids != null && ids.size() != 0) {
      query += " WHERE " + CommonConstants.CODE_PROPERTY + " IN " + EntityUtil.quoteIt(ids);
    }
    return UtilClass.getGraph().command(new OCommandSQL(query)).execute();
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForTransformation/*" };
  }
}
