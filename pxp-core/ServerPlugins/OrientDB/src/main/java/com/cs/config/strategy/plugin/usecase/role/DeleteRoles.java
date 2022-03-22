package com.cs.config.strategy.plugin.usecase.role;

import com.cs.config.strategy.plugin.usecase.role.abstrct.AbstractDeleteRoles;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeleteRoles extends AbstractDeleteRoles {
  
  public DeleteRoles(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    List<String> roleIds = new ArrayList<String>();
    Map<String, Object> responseMap = new HashMap<String, Object>();
    roleIds = (List<String>) map.get(IIdsListParameterModel.IDS);
    String label = VertexLabelConstants.ENTITY_TYPE_ROLE;
    
    responseMap = delete(map, roleIds, label);
    
    return responseMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|DeleteRoles/*" };
  }
}
