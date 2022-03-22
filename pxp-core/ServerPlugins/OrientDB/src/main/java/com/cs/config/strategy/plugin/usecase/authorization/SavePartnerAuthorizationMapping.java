package com.cs.config.strategy.plugin.usecase.authorization;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.authorization.util.AuthorizationUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.profile.ProfileNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.di.config.model.authorization.IGetPartnerAuthorizationModel;
import com.cs.di.config.model.authorization.ISavePartnerAuthorizationModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class SavePartnerAuthorizationMapping extends AbstractOrientPlugin {
  
  protected List<String> fieldsToExclude = Arrays.asList(
      ISavePartnerAuthorizationModel.ADDED_ATRRIBUTE_MAPPINGS,
      ISavePartnerAuthorizationModel.DELETED_ATTRIBUTE_MAPPINGS,
      ISavePartnerAuthorizationModel.ADDED_TAG_MAPPINGS,
      ISavePartnerAuthorizationModel.DELETED_TAG_MAPPINGS,
      ISavePartnerAuthorizationModel.ADDED_CLASS_MAPPINGS,
      ISavePartnerAuthorizationModel.DELETED_CLASS_MAPPINGS,
      ISavePartnerAuthorizationModel.ADDED_TAXONOMY_MAPPINGS,
      ISavePartnerAuthorizationModel.DELETED_TAXONOMY_MAPPINGS,
      ISavePartnerAuthorizationModel.ADDED_CONTEXT_MAPPINGS,
      ISavePartnerAuthorizationModel.DELETED_CONTEXT_MAPPINGS,
      ISavePartnerAuthorizationModel.ADDED_RELATIONSHIP_MAPPINGS,
      ISavePartnerAuthorizationModel.DELETED_RELATIONSHIP_MAPPINGS);
  
  public SavePartnerAuthorizationMapping(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<String, Object>();
    Map<String, Object> entityMap = new HashMap<>();
    Map<String, Object> configMap = new HashMap<>();
    
    OrientGraph graph = UtilClass.getGraph();
    
    String authorizationId = (String) requestMap.get(CommonConstants.ID_PROPERTY);
    
    Vertex authorizationNode = null;
    try {
      authorizationNode = UtilClass.getVertexById(authorizationId,
          VertexLabelConstants.AUTHORIZATION_MAPPING);
    }
    catch (NotFoundException e) {
      throw new ProfileNotFoundException();
    }
    
    UtilClass.saveNode(requestMap, authorizationNode, fieldsToExclude);
    
    entityMap.putAll(UtilClass.getMapFromNode(authorizationNode));
    AuthorizationUtils.saveMapping(authorizationNode, requestMap);
    graph.commit();
    
    AuthorizationUtils.getEntityMapFromAuthorizationMappingNode(authorizationNode, entityMap);
    AuthorizationUtils.getConfigDetails(authorizationNode, configMap);
    
    returnMap.put(IGetPartnerAuthorizationModel.CONFIG_DETAILS, configMap);
    returnMap.put(IGetPartnerAuthorizationModel.ENTITY, entityMap);
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SavePartnerAuthorizationMapping/*" };
  }
}
