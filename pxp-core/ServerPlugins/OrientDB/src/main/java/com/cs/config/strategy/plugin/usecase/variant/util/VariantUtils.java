package com.cs.config.strategy.plugin.usecase.variant.util;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextTagsModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VariantUtils {
  
  public static int getTotalVariantCount(Vertex parentNode) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    int maxVariantNo = 0;
    String rid = (String) parentNode.getId()
        .toString();
    String query = "select " + CommonConstants.MAX + "(" + CommonConstants.VARIANT_ID
        + ") from(traverse in('" + RelationshipLabelConstants.VARIANT_OF + "') from " + rid
        + " strategy BREADTH_FIRST)";
    Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex node : resultIterable) {
      maxVariantNo = node.getProperty(CommonConstants.MAX);
    }
    return maxVariantNo;
  }
  
  public static List<String> getContextTagIds(Map<String, Object> contextMap)
  {
    List<String> contextTagIds = new ArrayList<String>();
    List<Map<String, Object>> contextTags = (List<Map<String, Object>>) contextMap
        .get(IReferencedVariantContextModel.TAGS);
    for (Map<String, Object> contextTag : contextTags) {
      String tagId = (String) contextTag.get(IReferencedVariantContextTagsModel.TAG_ID);
      contextTagIds.add(tagId);
    }
    return contextTagIds;
  }
}
