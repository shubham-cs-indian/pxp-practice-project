package com.cs.config.strategy.plugin.usecase.repair;

import java.util.HashMap;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class Orient_Migration_Script_To_Add_IsReadOnly_In_Roles extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_To_Add_IsReadOnly_In_Roles(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_To_Add_IsReadOnly_In_Roles/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String getAllRoleNodes = "SELECT FROM " + VertexLabelConstants.ENTITY_TYPE_ROLE;
    Iterable<Vertex> roleNodes = UtilClass.getGraph()
        .command(new OCommandSQL(getAllRoleNodes))
        .execute();
    
    roleNodes.forEach(roleNode -> roleNode.setProperty(IRole.IS_READ_ONLY, false));
    
    return new HashMap<>();
  }
  
}