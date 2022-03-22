package com.cs.config.strategy.plugin.usecase.globalpermission;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionWithAllowedTemplatesModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionWithAllowedTemplatesRequestModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.exception.permission.GlobalPermissionNotFoundException;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class GetGlobalPermissionWithAllowedTemplates extends AbstractOrientPlugin {
  
  public GetGlobalPermissionWithAllowedTemplates(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetGlobalPermissionWithAllowedTemplates/*" };
  }
  
  @Override
  protected Map<String, Object> execute(Map<String, Object> requestMap) throws Exception
  {
    String natureKlassId = (String) requestMap
        .get(IGetGlobalPermissionWithAllowedTemplatesRequestModel.NATURE_KLASS_ID);
    String permissionNodeId = (String) requestMap
        .get(IGetGlobalPermissionWithAllowedTemplatesRequestModel.PERMISSION_NODE_ID);
    Set<String> allowedTemplates = new HashSet<>();
    String defaultTemplate = null;
    Map<String, Object> globalPermission = null;
    
    if (permissionNodeId != null) {
      Vertex permissionNode = null;
      try {
        permissionNode = UtilClass.getVertexById(permissionNodeId,
            VertexLabelConstants.KLASS_TAXONOMY_GLOBAL_PERMISSIONS);
      }
      catch (NotFoundException e) {
        throw new GlobalPermissionNotFoundException();
      }
      globalPermission = UtilClass.getMapFromNode(permissionNode);
      
      Iterable<Vertex> templateNodeIterable = permissionNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_ALLOWED_TEMPLATE);
      for (Vertex templateNode : templateNodeIterable) {
        allowedTemplates.add(templateNode.getProperty(CommonConstants.CODE_PROPERTY));
      }
      
      Iterator<Vertex> templateNodeIterator = permissionNode
          .getVertices(Direction.OUT,
              RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_DEFAULT_TEMPLATE)
          .iterator();
      if (templateNodeIterator.hasNext()) {
        Vertex defaultTemplateNode = templateNodeIterator.next();
        defaultTemplate = defaultTemplateNode.getProperty(CommonConstants.CODE_PROPERTY);
      }
    }
    
    if (permissionNodeId == null) {
      globalPermission = GlobalPermissionUtils.getDefaultGlobalPermission();
    }
    
    if (defaultTemplate == null && natureKlassId != null) {
      Vertex natureKlassNode = UtilClass.getVertexById(natureKlassId,
          VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      Iterator<Vertex> templateIterator = natureKlassNode
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_TEMPLATE)
          .iterator();
      if (templateIterator.hasNext()) {
        Vertex templateNode = templateIterator.next();
        defaultTemplate = templateNode.getProperty(CommonConstants.CODE_PROPERTY);
      }
    }
    
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IGetGlobalPermissionWithAllowedTemplatesModel.ID, permissionNodeId);
    returnMap.put(IGetGlobalPermissionWithAllowedTemplatesModel.ALLOWED_TEMPLATES,
        allowedTemplates);
    returnMap.put(IGetGlobalPermissionWithAllowedTemplatesModel.GLOBAL_PERMISSION,
        globalPermission);
    returnMap.put(IGetGlobalPermissionWithAllowedTemplatesModel.DEFAULT_TEMPLATE, defaultTemplate);
    
    return returnMap;
  }
}
