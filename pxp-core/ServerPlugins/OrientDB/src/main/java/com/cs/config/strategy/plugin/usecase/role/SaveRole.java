package com.cs.config.strategy.plugin.usecase.role;

import com.cs.config.strategy.plugin.usecase.role.abstrct.AbstractSaveRole;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class SaveRole extends AbstractSaveRole {
  
  public SaveRole(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    Map<String, Object> roleMapToReturn = new HashMap<String, Object>();
    HashMap<String, Object> roleMap = new HashMap<String, Object>();
    
    roleMap = (HashMap<String, Object>) map.get(CommonConstants.ROLE);
    String label = VertexLabelConstants.ENTITY_TYPE_ROLE;
    roleMapToReturn = saveRole(roleMap, CommonConstants.ROLE, label);
    
    return roleMapToReturn;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveRole/*" };
  }
}
