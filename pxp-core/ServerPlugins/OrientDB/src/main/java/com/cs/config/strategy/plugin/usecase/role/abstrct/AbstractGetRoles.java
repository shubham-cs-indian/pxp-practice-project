package com.cs.config.strategy.plugin.usecase.role.abstrct;

import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractGetRoles extends AbstractOrientPlugin {
  
  public AbstractGetRoles(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  public HashMap<String, Object> getRoles(Map<String, Object> requestMap, String label)
      throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    
    Iterable<Vertex> i = graph
        .command(new OCommandSQL("select from " + label + " order by "
            + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
        .execute();
    List<Map<String, Object>> rolesList = new ArrayList<>();
    for (Vertex roleNode : i) {
      rolesList.add(RoleUtils.getRoleEntityMap(roleNode));
    }
    
    HashMap<String, Object> response = new HashMap<>();
    response.put("list", rolesList);
    
    return response;
  }
}
