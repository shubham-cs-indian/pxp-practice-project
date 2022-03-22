package com.cs.config.strategy.plugin.usecase.authorization;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.authorization.util.AuthorizationUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.di.config.model.authorization.IPartnerAuthorizationModel;
import com.cs.di.config.model.authorization.IGetPartnerAuthorizationModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class CreatePartnerAuthorizationMappings extends AbstractOrientPlugin {
  
  List<String> fieldsToExclude = Arrays.asList(IPartnerAuthorizationModel.ATTRIBUTE_MAPPINGS,
      IPartnerAuthorizationModel.TAG_MAPPINGS, IPartnerAuthorizationModel.CLASS_MAPPINGS,
      IPartnerAuthorizationModel.TAXONOMY_MAPPINGS, IPartnerAuthorizationModel.CONTEXT_MAPPINGS,
      IPartnerAuthorizationModel.RELATIONSHIP_MAPPINGS);
  
  public CreatePartnerAuthorizationMappings(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> authorizationMap) throws Exception
  {
    
    Map<String, Object> returnMap = new HashMap<String, Object>();
    
    Map<String, Object> entityMap = new HashMap<>();
    Map<String, Object> configMap = new HashMap<>();
    
    OrientGraph graph = UtilClass.getGraph();
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.AUTHORIZATION_MAPPING, CommonConstants.CODE_PROPERTY);
    Vertex authorizationNode = UtilClass.createNode(authorizationMap, vertexType, fieldsToExclude);
    
    entityMap.putAll(UtilClass.getMapFromNode(authorizationNode));
    graph.commit();
    
    AuthorizationUtils.getConfigDetails(authorizationNode, configMap);
    returnMap.put(IGetPartnerAuthorizationModel.CONFIG_DETAILS, configMap);
    returnMap.put(IGetPartnerAuthorizationModel.ENTITY, entityMap);
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreatePartnerAuthorizationMappings/*" };
  }
}
