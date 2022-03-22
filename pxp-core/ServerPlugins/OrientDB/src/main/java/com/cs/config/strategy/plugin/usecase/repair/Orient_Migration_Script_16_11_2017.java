package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.Iterator;
import java.util.Map;

public class Orient_Migration_Script_16_11_2017 extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_16_11_2017(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_16_11_2017/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = null;
    
    Iterator<Vertex> kPIterator = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.ENTITY_KLASS_PROPERTY)
        .iterator();
    
    while (kPIterator.hasNext()) {
      Vertex klassProperty = kPIterator.next();
      klassProperty.removeProperty("isFilterable");
      klassProperty.removeProperty("isSortable");
    }
    UtilClass.getGraph()
        .commit();
    return returnMap;
  }
}
