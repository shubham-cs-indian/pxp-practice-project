package com.cs.config.strategy.plugin.usecase.repair;

import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.themeconfiguration.IThemeConfiguration;
import com.cs.core.config.interactor.exception.themeconfiguration.ThemeConfigurationNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class Orient_Migration_Script_Theme_Configuration_For_19_1SR
    extends AbstractOrientMigration {
  
  public Orient_Migration_Script_Theme_Configuration_For_19_1SR(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_Theme_Configuration_For_19_1SR/*" };
  }
  
  @Override
  protected Object executeMigration(Map<String, Object> requestMap) throws Exception
  {
    Vertex themeConfigurationVertex = null;
    try {
      themeConfigurationVertex = UtilClass.getVertexByIndexedId(SystemLevelIds.THEME_CODE,
          VertexLabelConstants.LOGO_CONFIGURATION);
      themeConfigurationVertex.setProperty(IThemeConfiguration.PRIMARY_LOGO_ID,
          (String)requestMap.get(IThemeConfiguration.PRIMARY_LOGO_ID));
      themeConfigurationVertex.setProperty(IThemeConfiguration.LOGIN_SCREEN_BACKGROUND_COLOR,
          (String)requestMap.get(IThemeConfiguration.LOGIN_SCREEN_BACKGROUND_COLOR));
      themeConfigurationVertex.setProperty(IThemeConfiguration.LOGIN_SCREEN_BACKGROUND_IMAGE_KEY,
          (String)requestMap.get(IThemeConfiguration.LOGIN_SCREEN_BACKGROUND_IMAGE_KEY));
      themeConfigurationVertex.setProperty(IThemeConfiguration.LOGIN_SCREEN_BACKGROUND_THUMB_KEY,
          (String)requestMap.get(IThemeConfiguration.LOGIN_SCREEN_BACKGROUND_THUMB_KEY));
      themeConfigurationVertex.setProperty(IThemeConfiguration.LOGIN_SCREEN_FONT_COLOR,
          (String)requestMap.get(IThemeConfiguration.LOGIN_SCREEN_FONT_COLOR));
      themeConfigurationVertex.setProperty(IThemeConfiguration.LOGIN_SCREEN_TITLE,
          (String)requestMap.get(IThemeConfiguration.LOGIN_SCREEN_FONT_COLOR));
    }
    catch (NotFoundException e) {
      throw new ThemeConfigurationNotFoundException(e);
    }
    
    return null;
  }
}
