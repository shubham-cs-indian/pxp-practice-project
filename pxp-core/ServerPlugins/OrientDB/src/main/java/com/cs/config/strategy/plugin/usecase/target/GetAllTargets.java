package com.cs.config.strategy.plugin.usecase.target;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAllTargets extends AbstractOrientPlugin {
  
  public GetAllTargets(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    String id = (String) requestMap.get("id");
    
    List<Map<String, Object>> klassesList = KlassGetUtils.getKlassesList(id,
        VertexLabelConstants.ENTITY_TYPE_TARGET);
    
    Map<String, Object> listModel = new HashMap<>();
    listModel.put("list", klassesList);
    
    return listModel;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllTargets/*" };
  }
}
