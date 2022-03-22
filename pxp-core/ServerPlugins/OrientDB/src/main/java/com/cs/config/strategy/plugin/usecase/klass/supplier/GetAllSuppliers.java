package com.cs.config.strategy.plugin.usecase.klass.supplier;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAllSuppliers extends AbstractOrientPlugin {
  
  public GetAllSuppliers(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> map) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    
    String id = (String) map.get("id");
    
    List<Map<String, Object>> klassesList = KlassGetUtils.getKlassesList(id,
        VertexLabelConstants.ENTITY_TYPE_SUPPLIER);
    
    Map<String, Object> listModel = new HashMap<>();
    listModel.put("list", klassesList);
    
    graph.commit();
    
    return listModel;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllSuppliers/*" };
  }
}
