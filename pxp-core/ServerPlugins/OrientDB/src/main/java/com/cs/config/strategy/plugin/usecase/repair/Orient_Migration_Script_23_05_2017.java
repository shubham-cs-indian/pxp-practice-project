/*
 * (set default time range for embedded contexts) :
 *
 */

package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.variantcontext.IDefaultTimeRange;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Orient_Migration_Script_23_05_2017 extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_23_05_2017(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = null;
    Iterator<Vertex> contextsIterator = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.VARIANT_CONTEXT)
        .iterator();
    while (contextsIterator.hasNext()) {
      Vertex contextNode = contextsIterator.next();
      String type = contextNode.getProperty(CommonConstants.TYPE_PROPERTY);
      if (type.equals(CommonConstants.CONTEXTUAL_VARIANT)) {
        Map<String, Object> defaultTimeRange = new HashMap<>();
        defaultTimeRange.put(IDefaultTimeRange.FROM, null);
        defaultTimeRange.put(IDefaultTimeRange.TO, null);
        defaultTimeRange.put(IDefaultTimeRange.IS_CURRENT_TIME, null);
        contextNode.setProperty(IVariantContext.DEFAULT_TIME_RANGE, defaultTimeRange);
      }
    }
    UtilClass.getGraph()
        .commit();
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_23_05_2017/*" };
  }
}
