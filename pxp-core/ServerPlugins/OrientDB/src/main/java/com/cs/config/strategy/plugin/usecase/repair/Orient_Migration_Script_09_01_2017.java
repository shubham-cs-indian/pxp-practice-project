/*
 * (update couplingtype property and isSkipped and isMandatory property)
 */

package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.Map;

public class Orient_Migration_Script_09_01_2017 extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_09_01_2017(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = null;
    
    Iterable<Vertex> klassPropertyIterator = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.ENTITY_KLASS_PROPERTY);
    
    for (Vertex klassProperty : klassPropertyIterator) {
      klassProperty.removeProperty("variantCouplingType");
      klassProperty.removeProperty("versionCouplingType");
      klassProperty.setProperty(ISectionElement.COUPLING_TYPE, CommonConstants.LOOSELY_COUPLED);
      klassProperty.setProperty(ISectionElement.IS_MANDATORY, false);
      klassProperty.setProperty(ISectionElement.IS_SHOULD, false);
      klassProperty.setProperty(ISectionElement.IS_SKIPPED, false);
    }
    UtilClass.getGraph()
        .commit();
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_09_01_2017/*" };
  }
}
