package com.cs.config.strategy.plugin.usecase.dataintegration;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.user.UserNotFoundException;
import com.cs.core.config.interactor.model.taxonomy.ITaxonomyAndTypeIdsForUserModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GetUserPermissionForExportAll extends AbstractOrientPlugin {
  
  public GetUserPermissionForExportAll(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetUserPermissionForExportAll/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<>();
    String userId = (String) requestMap.get(CommonConstants.USER_ID);
    
    managePermissionDetails(userId, returnMap);
    
    return returnMap;
  }
  
  private void managePermissionDetails(String userId, Map<String, Object> mapToReturn)
      throws Exception
  {
    Vertex userNode = null;
    try {
      userNode = UtilClass.getVertexById(userId, VertexLabelConstants.ENTITY_TYPE_USER);
    }
    catch (NotFoundException e) {
      throw new UserNotFoundException();
    }
    
    Set<String> klassIdsHavingRP = new HashSet<>();
    Set<String> taxonomyIdsHavingRP = new HashSet<>();
    Iterable<Vertex> roleNodes = userNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_USER_IN);
    for (Vertex roleNode : roleNodes) {
      fillIdsHavingReadPermission(taxonomyIdsHavingRP, klassIdsHavingRP, roleNode);
    }
    mapToReturn.put(ITaxonomyAndTypeIdsForUserModel.TYPES, klassIdsHavingRP);
    mapToReturn.put(ITaxonomyAndTypeIdsForUserModel.TAXONOMY_IDS, taxonomyIdsHavingRP);
  }
  
  private void fillIdsHavingReadPermission(Set<String> taxonomyIdsHavingRP,
      Set<String> klassIdsHavingRP, Vertex roleNode) throws Exception
  {
    klassIdsHavingRP.addAll(GlobalPermissionUtils.getKlassIdsHavingReadPermission(roleNode));
    taxonomyIdsHavingRP.addAll(GlobalPermissionUtils.getTaxonomyIdsHavingReadPermission(roleNode));
  }
}
