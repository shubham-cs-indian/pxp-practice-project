package com.cs.config.strategy.plugin.usecase.repair;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;


public class MigrationForDeprecateMotiveKlass extends AbstractOrientPlugin {
  
  public MigrationForDeprecateMotiveKlass(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  public String[] getNames()
  {
    return new String[] { "POST|MigrationForDeprecateMotiveKlass/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    List<String> translationCodesToBeRemoved = Arrays.asList("MOTIVE", "KLASS_MOTIVE");
    Iterable<Vertex> verticesToBeRemoved = UtilClass.getVerticesByIds(translationCodesToBeRemoved, VertexLabelConstants.UI_TRANSLATIONS);
    for (Vertex vertexToBeRemoved : verticesToBeRemoved) {
      vertexToBeRemoved.remove();
    }
    graph.commit();
    return null;
  }
  
}
