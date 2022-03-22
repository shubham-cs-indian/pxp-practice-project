package com.cs.config.strategy.plugin.usecase.configdetails.instancetree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.configdetails.instancetree.abstrct.AbstractGetConfigDetailsForGetNewInstanceTree;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.config.interactor.exception.context.ContextNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForEntitiesQuicklistGetRequestModel;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForGetEntitiesQuicklist extends AbstractGetConfigDetailsForGetNewInstanceTree {

  public GetConfigDetailsForGetEntitiesQuicklist(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  public String[] getNames()
  {
    return new String[] {"POST|GetConfigDetailsForGetEntitiesQuicklist/*"};
  }

  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    
    execute(requestMap, mapToReturn);
    
    return mapToReturn;
  }
  
  @Override
  protected List<String> fillAllowedEntities(Vertex roleNode, Map<String, Object> requestMap) throws Exception
  {
    List<String> moduleEntities =  (List<String>) requestMap.get(IConfigDetailsForEntitiesQuicklistGetRequestModel.ALLOWED_ENTITIES);
    List<String> allowedEntities = (List<String>) roleNode.getProperty(IRole.ENTITIES);
    if (allowedEntities.isEmpty()) {
      allowedEntities.addAll(CommonConstants.MODULE_ENTITIES);
      allowedEntities.add(CommonConstants.FILE_INSTANCE_MODULE_ENTITY);
    }
    allowedEntities.retainAll(moduleEntities);
    
    String contextId =  (String) requestMap.get(IConfigDetailsForEntitiesQuicklistGetRequestModel.CONTEXT_ID);
    Vertex contextNode = null;
    try {
      contextNode = UtilClass.getVertexById(contextId, VertexLabelConstants.VARIANT_CONTEXT);
    }
    catch (NotFoundException e) {
      throw new ContextNotFoundException();
    }
    
    List<String> entities = contextNode.getProperty(IVariantContext.ENTITIES);
    if (entities != null) {
      allowedEntities.retainAll(entities);
    }
    
    String entityId =  (String) requestMap.get(IConfigDetailsForEntitiesQuicklistGetRequestModel.ENTITY_ID);
    if(!allowedEntities.contains(entityId)) return new ArrayList<>();
    return allowedEntities;
  }
}
