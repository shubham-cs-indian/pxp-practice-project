package com.cs.config.strategy.plugin.usecase.export;

import java.util.Map;

import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class ExportTopHierarchyClassList extends AbstractGetTopClassifierTaxonomylist {
  
  public ExportTopHierarchyClassList(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|ExportTopHierarchyClassList/*" };
  }
  
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    return super.execute(requestMap);
  }
  
  @Override
  public String getKlassVertexType()
  {
    return VertexLabelConstants.ENTITY_TYPE_KLASS;
  }
}
