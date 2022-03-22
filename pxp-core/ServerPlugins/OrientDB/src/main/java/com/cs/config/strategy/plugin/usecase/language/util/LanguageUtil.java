package com.cs.config.strategy.plugin.usecase.language.util;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ILanguage;
import com.cs.core.config.interactor.exception.language.DefaultLanguageNotFoundException;
import com.cs.core.config.interactor.exception.language.MultipleDefaultLanguageFoundException;
import com.cs.core.config.interactor.model.language.ILanguageModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LanguageUtil {
  
  public static Vertex getDefaultLanguageVertex(Boolean isExceptionRequired) throws Exception
  {
    String query = "SELECT FROM " + VertexLabelConstants.LANGUAGE + " WHERE "
        + ILanguage.IS_DEFAULT_LANGUAGE + " = " + true;
    Iterable<Vertex> languageNodes = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Iterator<Vertex> iterator = languageNodes.iterator();
    Vertex languageNode = null;
    if (!iterator.hasNext()) {
      if (!isExceptionRequired) {
        throw new DefaultLanguageNotFoundException();
      }
      else {
        return null;
      }
    }
    
    languageNode = iterator.next();
    
    if (iterator.hasNext()) {
      throw new MultipleDefaultLanguageFoundException();
    }
    
    return languageNode;
  }
  
  public static Map<String, Object> getLanguageMap(Vertex language)
  {
    List<Map<String, Object>> children1 = new ArrayList<>();
    Iterable<Vertex> childrenVertices = language.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for (Vertex child : childrenVertices) {
      Map<String, Object> childrenMap = UtilClass.getMapFromVertex(new ArrayList<>(), child);
      children1.add(childrenMap);
    }
    Map<String, Object> rootMap = new HashMap<>();
    rootMap = UtilClass.getMapFromVertex(new ArrayList<>(), language);
    rootMap.put(ILanguageModel.CHILDREN, children1);
    return rootMap;
  }
  
  public static List<String> getReverseTreeLocaleCode(String localeCode)
  {
    List<String> localeCodes = new ArrayList<String>();
    String query = "SELECT " + CommonConstants.CODE_PROPERTY
        + " FROM (TRAVERSE OUT(\"Child_Of\") FROM (SELECT FROM " + VertexLabelConstants.LANGUAGE
        + " WHERE " + CommonConstants.CODE_PROPERTY + " = \"" + localeCode + "\"))";
    
    Iterable<Vertex> languageNodes = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Iterator<Vertex> iterator = languageNodes.iterator();
    
    Vertex language = null;
    while (iterator.hasNext()) {
      language = iterator.next();
      Object value = language.getProperty(CommonConstants.CODE_PROPERTY);
      localeCodes.add((String) value);
    }
    
    return localeCodes;
  }
  
}
