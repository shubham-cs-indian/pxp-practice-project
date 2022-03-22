package com.cs.config.strategy.plugin.usecase.attribute;

import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAttributes extends AbstractOrientPlugin {
  
  public GetAttributes(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    
    String mode = (String) map.get("mode");
    
    OrientGraph graph = UtilClass.getGraph();
    List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
    
    Iterable<Vertex> resultIterable = null;
    
    if (mode.equals("pim")) {
      resultIterable = graph
          .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE
              + " where forPim = true order by "
              + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
          .execute();
    }
    else if (mode.equals("mam")) {
      resultIterable = graph
          .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE
              + " where forMam = true order by "
              + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
          .execute();
    }
    else if (mode.equals("target")) {
      resultIterable = graph
          .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE
              + " where forTarget = true order by "
              + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
          .execute();
    }
    else if (mode.equals("editorial")) {
      resultIterable = graph
          .command(new OCommandSQL("select from attribute where forEditorial = true order by "
              + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
          .execute();
    }
    else {
      resultIterable = graph
          .command(new OCommandSQL(
              "select from " + VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE + " order by "
                  + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
          .execute();
    }
    for (Vertex klassNode : resultIterable) {
      HashMap<String, Object> attributeMap = new HashMap<String, Object>();
      attributeMap = (HashMap<String, Object>) AttributeUtils.getAttributeMap(klassNode);
      list.add(attributeMap);
    }
    
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.put("list", list);
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAttributes/*" };
  }
}
