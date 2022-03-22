package com.cs.config.strategy.plugin.usecase.repair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.themeconfiguration.IThemeConfiguration;
import com.cs.core.config.interactor.model.themeconfiguration.IThemeConfigurationModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class Orient_Migration_Script_Theme_Configuration extends AbstractOrientMigration {
  
  public Orient_Migration_Script_Theme_Configuration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
    // TODO Auto-generated constructor stub
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_Theme_Configuration/*" };
  }
  
  @Override
  protected Object executeMigration(Map<String, Object> requestMap) throws Exception
  {
    Vertex themeConfigurationVertex = null;
    try {
      themeConfigurationVertex = UtilClass.getVertexByIndexedId(SystemLevelIds.THEME_CODE,
          VertexLabelConstants.LOGO_CONFIGURATION);
      themeConfigurationVertex.setProperty(IThemeConfiguration.HEADER_BACKGROUND_COLOR,
          (String)requestMap.get(IThemeConfiguration.HEADER_BACKGROUND_COLOR));
      themeConfigurationVertex.setProperty(IThemeConfiguration.HEADER_FONT_COLOR,
          (String)requestMap.get(IThemeConfiguration.HEADER_FONT_COLOR));
      themeConfigurationVertex.setProperty(IThemeConfiguration.HEADER_ICON_COLOR,
          (String)requestMap.get(IThemeConfiguration.HEADER_ICON_COLOR));
      themeConfigurationVertex.setProperty(IThemeConfiguration.DIALOG_BACKGROUND_COLOR,
          (String)requestMap.get(IThemeConfiguration.DIALOG_BACKGROUND_COLOR));
      themeConfigurationVertex.setProperty(IThemeConfiguration.DIALOG_FONT_COLOR,
          (String)requestMap.get(IThemeConfiguration.DIALOG_FONT_COLOR));
    }
    catch (NotFoundException e) {
      Map<String, Object> themeMap = new HashMap<String, Object>();
      themeMap.put(IThemeConfigurationModel.ID, requestMap.get(IThemeConfiguration.ID));
      themeMap.put(IThemeConfiguration.CODE, SystemLevelIds.THEME_CODE);
      themeMap.put(IThemeConfiguration.HEADER_BACKGROUND_COLOR,
          (String)requestMap.get(IThemeConfiguration.HEADER_BACKGROUND_COLOR));
      themeMap.put(IThemeConfiguration.HEADER_FONT_COLOR,
          (String)requestMap.get(IThemeConfiguration.HEADER_FONT_COLOR));
      themeMap.put(IThemeConfiguration.HEADER_ICON_COLOR,
          (String)requestMap.get(IThemeConfiguration.HEADER_ICON_COLOR));
      themeMap.put(IThemeConfiguration.DIALOG_BACKGROUND_COLOR,
          (String)requestMap.get(IThemeConfiguration.DIALOG_BACKGROUND_COLOR));
      themeMap.put(IThemeConfiguration.DIALOG_FONT_COLOR,
          (String)requestMap.get(IThemeConfiguration.DIALOG_FONT_COLOR));
      OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
          VertexLabelConstants.LOGO_CONFIGURATION, CommonConstants.CODE_PROPERTY);
      UtilClass.createNode(themeMap, vertexType, new ArrayList<>());
    }
    
    return null;
  }
}
