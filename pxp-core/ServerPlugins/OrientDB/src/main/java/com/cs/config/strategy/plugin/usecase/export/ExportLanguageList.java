package com.cs.config.strategy.plugin.usecase.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ILanguage;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class ExportLanguageList extends AbstractOrientPlugin {
  
  public ExportLanguageList(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  @SuppressWarnings("unchecked")
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    HashMap<String, Object> responseMap = new HashMap<>();
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    
    List<String> languageCodes = (List<String>) requestMap.get("itemCodes");
    StringBuilder codeQuery = UtilClass.getTypeQueryWithoutANDOperator(languageCodes, ILanguage.CODE);
    StringBuilder condition = EntityUtil.getConditionQuery(codeQuery);
    
    String query = "select from " + VertexLabelConstants.LANGUAGE + condition + " order by "
        + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc";
    Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query)).execute();
    
    for (Vertex languageNode : resultIterable) {
      list.add(getLanguageMap(languageNode));
    }
    
    responseMap.put("list", list);
    
    return responseMap;
  }
  
  private static Map<String, Object> getLanguageMap(Vertex language)
  {
    Map<String, Object> rootMap = new HashMap<>();
    rootMap = UtilClass.getMapFromVertex(new ArrayList<>(), language);
    Iterable<Vertex> parentVertices = language.getVertices(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    String parentCode = "-1";
    for (Vertex parentVertex : parentVertices) {
      parentCode = UtilClass.getCodeNew(parentVertex);
    }
    rootMap.put("parentCode", parentCode);
    return rootMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|ExportLanguageList/*" };
  }
}
