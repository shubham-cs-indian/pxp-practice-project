package com.cs.config.strategy.plugin.usecase.klass;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetKlasses extends AbstractOrientPlugin {
  
  public GetKlasses(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    
    String id = (String) map.get("id");
    
    List<Map<String, Object>> klassesList = KlassGetUtils.getKlassesList(id,
        VertexLabelConstants.ENTITY_TYPE_KLASS);
    
    Map<String, Object> listModel = new HashMap<>();
    listModel.put("list", klassesList);
    return listModel;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetKlasses/*" };
  }
}
