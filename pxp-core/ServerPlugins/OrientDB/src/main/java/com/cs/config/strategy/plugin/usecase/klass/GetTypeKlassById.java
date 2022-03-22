package com.cs.config.strategy.plugin.usecase.klass;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.HashMap;
import java.util.Map;

public class GetTypeKlassById extends AbstractOrientPlugin {
  
  public GetTypeKlassById(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    
    String id = null;
    String typeKlass = null;
    OrientGraph graph = UtilClass.getGraph();
    
    id = (String) map.get(IIdParameterModel.ID);
    Vertex klassNode1 = UtilClass.getVertexByIndexedId(id,
        VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    if (klassNode1 != null) {
      typeKlass = klassNode1.getProperty(CommonConstants.TYPE_PROPERTY);
    }
    Map<String, String> returnMap = new HashMap<>();
    returnMap.put(IIdAndTypeModel.TYPE, typeKlass);
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetTypeKlassById/*" };
  }
}
