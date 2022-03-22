package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.sso.ISSOConfiguration;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.Map;

public class OrientMigrationForSSOConfiguration extends AbstractOrientMigration {
  
  public OrientMigrationForSSOConfiguration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|OrientMigrationForSSOConfiguration/*" };
  }
  
  @Override
  protected Object executeMigration(Map<String, Object> requestMap) throws Exception
  {
    UtilClass.getDatabase()
        .commit();
    OrientVertexType ssoConfigVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.SSO_CONFIGURATION, CommonConstants.CODE_PROPERTY);
    UtilClass.createVertexPropertyAndApplyFulLTextIndex(ssoConfigVertexType,
        new String[] { ISSOConfiguration.DOMAIN });
    return null;
  }
}
