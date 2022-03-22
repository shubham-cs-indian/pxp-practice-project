package com.cs.imprt.config.strategy.plugin.usecase.importklass.util;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class KlassGetUtils {
  
  public static List<Map<String, Object>> getKlassesList(String id, String nodeLabel,
      String[] keyValues, List<String> allowedTypes)
  {
    OrientGraph graph = UtilClass.getGraph();
    List<Map<String, Object>> klassesList = new ArrayList<>();
    Iterator<Vertex> iterator = graph
        .getVertices(nodeLabel, new String[] { CommonConstants.CODE_PROPERTY },
            new Object[] { id })
        .iterator();
    Vertex rootNode = null;
    if (iterator.hasNext()) {
      rootNode = iterator.next();
      
      String rid = (String) rootNode.getId()
          .toString();
      String query = "select from(traverse in('Child_Of') from " + rid
          + " strategy BREADTH_FIRST) ";
      if (keyValues != null && keyValues.length > 1) {
        String key = null;
        String value = null;
        query += "where ";
        for (int keyValueIndex = 0; keyValueIndex < keyValues.length; keyValueIndex++) {
          if (keyValueIndex % 2 == 0) {
            key = keyValues[keyValueIndex];
          }
          else {
            value = keyValues[keyValueIndex];
          }
          if (key != null && value != null) {
            query += key + "=\"" + value + "\" and ";
            key = null;
            value = null;
          }
        }
        query = query.substring(0, query.length() - 4);
      }
      
      if (allowedTypes != null) {
        query += " and code in " + EntityUtil.quoteIt(allowedTypes);
      }
      query += " order by " + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY)
          + " asc";
      Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query))
          .execute();
      for (Vertex klassNode : resultIterable) {
        klassesList.add(UtilClass.getMapFromNode(klassNode));
      }
    }
    return klassesList;
  }
  
  public static List<Map<String, Object>> getNonAbstractKlassesList(String id, String nodeLabel,
      String[] keyValues, List<String> allowedTypes)
  {
    OrientGraph graph = UtilClass.getGraph();
    List<Map<String, Object>> klassesList = new ArrayList<>();
    Iterator<Vertex> iterator = graph
        .getVertices(nodeLabel, new String[] { CommonConstants.CODE_PROPERTY },
            new Object[] { id })
        .iterator();
    Vertex rootNode = null;
    if (iterator.hasNext()) {
      rootNode = iterator.next();
      
      String rid = (String) rootNode.getId()
          .toString();
      String query = "select from(traverse in('Child_Of') from " + rid
          + " strategy BREADTH_FIRST) where ";
      if (keyValues != null && keyValues.length > 1) {
        String key = null;
        String value = null;
        // query+= "where ";
        for (int keyValueIndex = 0; keyValueIndex < keyValues.length; keyValueIndex++) {
          if (keyValueIndex % 2 == 0) {
            key = keyValues[keyValueIndex];
          }
          else {
            value = keyValues[keyValueIndex];
          }
          if (key != null && value != null) {
            query += key + "=\"" + value + "\" and ";
            key = null;
            value = null;
          }
        }
        // query = query.substring(0, query.length()-4);
      }
      
      query += " (isAbstract = \"false\" or isAbstract is null) ";
      
      if (allowedTypes != null) {
        query += " and code in " + EntityUtil.quoteIt(allowedTypes);
      }
      query += " order by " + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY)
          + " asc";
      Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query))
          .execute();
      for (Vertex klassNode : resultIterable) {
        klassesList.add(UtilClass.getMapFromNode(klassNode));
      }
    }
    return klassesList;
  }
  
  public static List<Map<String, Object>> getKlassesList(String id, String nodeLabel)
  {
    return getKlassesList(id, nodeLabel, null, null);
  }
}
