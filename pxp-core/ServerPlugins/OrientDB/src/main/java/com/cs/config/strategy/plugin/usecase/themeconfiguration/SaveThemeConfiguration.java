package com.cs.config.strategy.plugin.usecase.themeconfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.exception.themeconfiguration.ThemeConfigurationNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class SaveThemeConfiguration extends AbstractOrientPlugin {
  
  public SaveThemeConfiguration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveThemeConfiguration/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Vertex themeConfigurationVertex = null;
    List<String> fieldsToIgnore = new ArrayList<String>();
    fieldsToIgnore.add(IConfigResponseWithAuditLogModel.AUDIT_LOG_INFO);
    fieldsToIgnore.add(CommonConstants.CODE_PROPERTY);
    try {
      themeConfigurationVertex = UtilClass.getVertexById(SystemLevelIds.THEME_CODE,
          VertexLabelConstants.LOGO_CONFIGURATION);
    }
    catch (NotFoundException e) {
      throw new ThemeConfigurationNotFoundException(e);
    }
    
    UtilClass.saveNode(requestMap, themeConfigurationVertex, fieldsToIgnore);
    UtilClass.getGraph()
        .commit();
    Map<String, Object> themeConfiguration = UtilClass.getMapFromVertex(new ArrayList<>(),
        themeConfigurationVertex);
    
    return themeConfiguration;
  }
}
