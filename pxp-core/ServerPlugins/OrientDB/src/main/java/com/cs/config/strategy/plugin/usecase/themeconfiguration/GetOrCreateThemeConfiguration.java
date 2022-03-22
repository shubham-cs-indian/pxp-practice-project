package com.cs.config.strategy.plugin.usecase.themeconfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class GetOrCreateThemeConfiguration extends AbstractOrientPlugin {
  
  public GetOrCreateThemeConfiguration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetOrCreateThemeConfiguration/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    Vertex themeConfigurationVertex = null;
    List<String> fieldsToIgnore = new ArrayList<String>();
    fieldsToIgnore.add(IConfigResponseWithAuditLogModel.AUDIT_LOG_INFO);
    try {
      themeConfigurationVertex = UtilClass.getVertexByIndexedId(SystemLevelIds.THEME_CODE,
          VertexLabelConstants.LOGO_CONFIGURATION);
    }
    catch (NotFoundException e) {
      OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
          VertexLabelConstants.LOGO_CONFIGURATION, CommonConstants.CODE_PROPERTY);
      UtilClass.createNode(requestMap, vertexType, fieldsToIgnore);
      return requestMap;
    }
    
    Map<String, Object> themeConfiguration = UtilClass.getMapFromVertex(new ArrayList<>(),
        themeConfigurationVertex);
    return themeConfiguration;
  }
}
