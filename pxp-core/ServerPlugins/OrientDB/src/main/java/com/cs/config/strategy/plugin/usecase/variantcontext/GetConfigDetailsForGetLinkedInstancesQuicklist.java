package com.cs.config.strategy.plugin.usecase.variantcontext;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.config.interactor.exception.context.ContextNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.variants.IConfigDetailsForGetVariantLinkedInstancesQuicklistModel;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractConfigDetails;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GetConfigDetailsForGetLinkedInstancesQuicklist extends AbstractConfigDetails {
  
  public GetConfigDetailsForGetLinkedInstancesQuicklist(
      final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForGetLinkedInstancesQuicklist/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String contextId = (String) requestMap.get(IIdParameterModel.ID);
    String userId = (String) requestMap.get(IIdParameterModel.CURRENT_USER_ID);
    
    Vertex contextNode = null;
    try {
      contextNode = UtilClass.getVertexById(contextId, VertexLabelConstants.VARIANT_CONTEXT);
    }
    catch (NotFoundException e) {
      throw new ContextNotFoundException();
    }
    
    List<String> entities = contextNode.getProperty(IVariantContext.ENTITIES);
    if (entities == null) {
      entities = new ArrayList<String>();
    }
    Vertex userInRole = RoleUtils.getRoleFromUser(userId);
    Map<String, Object> mapToReturn = new HashMap<String, Object>();
    fillReferencedPermission(mapToReturn, userInRole);
    mapToReturn.put(IConfigDetailsForGetVariantLinkedInstancesQuicklistModel.ENTITIES, entities);
    return mapToReturn;
  }
  
  protected void fillReferencedPermission(Map<String, Object> responseMap, Vertex userInRole)
      throws Exception
  {
    
    Set<String> allowedEntities = new HashSet<>();
    
    Set<String> klassIdsHavingReadPermission = GlobalPermissionUtils
        .getKlassIdsHavingReadPermission(userInRole);
    
    responseMap.put(IConfigDetailsForGetVariantLinkedInstancesQuicklistModel.KLASS_IDS_HAVING_RP,
        klassIdsHavingReadPermission);
    Set<String> taxonomyIdsHavingRP = GlobalPermissionUtils
        .getTaxonomyIdsHavingReadPermission(userInRole);
    responseMap.put(IConfigDetailsForGetVariantLinkedInstancesQuicklistModel.TAXONOMY_IDS_HAVING_RP,
        taxonomyIdsHavingRP);
    // fillEntitiesHavingReadPermission
    allowedEntities.addAll(userInRole.getProperty(IRole.ENTITIES));
    if (allowedEntities.isEmpty()) {
      allowedEntities.addAll(CommonConstants.MODULE_ENTITIES);
    }
    responseMap.put(IConfigDetailsForGetVariantLinkedInstancesQuicklistModel.ALLOWED_ENTITIES,
        allowedEntities);
  }
}
