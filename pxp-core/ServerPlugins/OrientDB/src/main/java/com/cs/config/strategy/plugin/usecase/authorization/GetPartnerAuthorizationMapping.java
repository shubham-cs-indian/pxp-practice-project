package com.cs.config.strategy.plugin.usecase.authorization;

import java.util.HashMap;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.authorization.util.AuthorizationUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.di.config.model.authorization.IGetPartnerAuthorizationModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetPartnerAuthorizationMapping extends AbstractOrientPlugin {
  
  public GetPartnerAuthorizationMapping(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<String, Object>();
    String authorizationMappingId = (String) requestMap.get(CommonConstants.ID_PROPERTY);
    
    Map<String, Object> configMap = new HashMap<>();
    Map<String, Object> entityMap = new HashMap<>();
    
    Vertex authorizationMappingNode = null;
    authorizationMappingNode = UtilClass.getVertexById(authorizationMappingId,
        VertexLabelConstants.AUTHORIZATION_MAPPING);
    entityMap.putAll(UtilClass.getMapFromNode(authorizationMappingNode));
   if(requestMap.containsKey("includeChildTaxonomies") && (Boolean)requestMap.get("includeChildTaxonomies")) {
     AuthorizationUtils.getEntityMapFromAuthorizationMappingWithChildNode(authorizationMappingNode, entityMap);
    }
    else {
      AuthorizationUtils.getEntityMapFromAuthorizationMappingNode(authorizationMappingNode,
          entityMap);
    }
    
    AuthorizationUtils.getConfigDetails(authorizationMappingNode, configMap);
    
    returnMap.put(IGetPartnerAuthorizationModel.CONFIG_DETAILS, configMap);
    returnMap.put(IGetPartnerAuthorizationModel.ENTITY, entityMap);
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetPartnerAuthorizationMapping/*" };
  }
}
