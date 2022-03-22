/*
 * (product types branch merge into master : so we need nature type in klass and
 * context type in contexts)
 *
 */

package com.cs.config.strategy.plugin.usecase.repair;

import java.util.Iterator;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class Orient_Migration_Script_03_02_2017 extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_03_02_2017(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = null;
    
    // For contexts
    Iterator<Vertex> iterator = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.VARIANT_CONTEXT)
        .iterator();
    while (iterator.hasNext()) {
      Vertex context = iterator.next();
      context.setProperty(IVariantContext.TYPE, "contextualVariant");
    }
    
    // For klass
    iterator = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS)
        .iterator();
    while (iterator.hasNext()) {
      Vertex klass = iterator.next();
      String id = UtilClass.getCodeNew(klass);
      if (id.equals(SystemLevelIds.ARTICLE)) {
        klass.setProperty(IKlass.IS_NATURE, true);
        klass.setProperty(IKlass.NATURE_TYPE, "singleArticle");
      }
      else {
        klass.setProperty(IKlass.IS_NATURE, false);
        klass.setProperty(IKlass.NATURE_TYPE, "");
      }
      klass.removeProperty("variantContexts");
    }
    UtilClass.getGraph()
        .commit();
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_03_02_2017/*" };
  }
}
