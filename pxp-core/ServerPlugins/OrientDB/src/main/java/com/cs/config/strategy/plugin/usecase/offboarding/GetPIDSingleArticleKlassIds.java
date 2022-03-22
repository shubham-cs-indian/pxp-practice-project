package com.cs.config.strategy.plugin.usecase.offboarding;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.offboarding.ICustomExportComponentConfigModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetPIDSingleArticleKlassIds extends AbstractOrientPlugin {
  
  public GetPIDSingleArticleKlassIds(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Map<String, Object> returnMap = new HashMap<String, Object>();
    
    returnMap.put(ICustomExportComponentConfigModel.PID_SKU_KLASS_IDS,
        getKlassIds(graph, CommonConstants.PID_SKU));
    returnMap.put(ICustomExportComponentConfigModel.SINGLE_ARTICLE_KLASS_IDS,
        getKlassIds(graph, CommonConstants.SINGLE_ARTICLE));
    
    return returnMap;
  }
  
  private List<String> getKlassIds(OrientGraph graph, String type)
  {
    Iterable<Vertex> klasses = graph
        .command(new OCommandSQL("select " + CommonConstants.CODE_PROPERTY + " from "
            + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS + " where " + IKlass.NATURE_TYPE + " = '"
            + type + "'"))
        .execute();
    List<String> klassIds = new ArrayList<>();
    for (Vertex klass : klasses) {
      klassIds.add(UtilClass.getCodeNew(klass));
    }
    
    return klassIds;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetPIDSingleArticleKlassIds/*" };
  }
}
