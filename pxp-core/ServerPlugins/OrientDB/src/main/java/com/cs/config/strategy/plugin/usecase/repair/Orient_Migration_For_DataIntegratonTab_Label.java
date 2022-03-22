package com.cs.config.strategy.plugin.usecase.repair;

import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class Orient_Migration_For_DataIntegratonTab_Label extends AbstractOrientMigration {
  
  public Orient_Migration_For_DataIntegratonTab_Label(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_For_DataIntegratonTab_Label/*" };
  }
  
  @Override
  protected Object executeMigration(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    
    String updateUserQuery = "UPDATE " + VertexLabelConstants.DASHBOARD_TAB + " SET "
        + "label__en_US = 'Data Integration Endpoints', label__de_DE = 'Data Integration Endpoints__de_DE', label__es_ES = 'Data Integration Endpoints__es_ES', label__fr_FR = 'Data Integration Endpoints__fr_FR' WHERE code = 'dataIntegrationTab'";
    graph.command(new OCommandSQL(updateUserQuery))
        .execute();
    
    graph.commit();
    
    return "Sucess";
    
  }
  
}
