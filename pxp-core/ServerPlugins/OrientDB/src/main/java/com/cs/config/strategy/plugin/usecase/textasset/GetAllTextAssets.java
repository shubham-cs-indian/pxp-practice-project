package com.cs.config.strategy.plugin.usecase.textasset;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAllTextAssets extends AbstractOrientPlugin {
  
  public GetAllTextAssets(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    String id = (String) requestMap.get("id");
    
    List<Map<String, Object>> klassesList = KlassGetUtils.getKlassesList(id,
        VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET);
    
    Map<String, Object> listModel = new HashMap<>();
    listModel.put("list", klassesList);
    
    return klassesList;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllTextAssets/*" };
  }
}
