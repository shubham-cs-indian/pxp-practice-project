package com.cs.config.strategy.plugin.usecase.language;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ILanguage;
import com.cs.core.config.interactor.model.language.IGetLanguageModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.exception.language.LanguageNotFoundException;
import com.cs.repository.language.LanguageRepository;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GetLanguage extends AbstractOrientPlugin {
  
  public GetLanguage(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetLanguage/*" };
  }
  
  private static final List<String> FIELDS_TO_FETCH            = Arrays.asList(ILanguage.LABEL,
      ILanguage.ID, ILanguage.CODE, ILanguage.LOCALE_ID, ILanguage.IS_STANDARD);
  
  private static final List<String> FIELDS_TO_FETCH_FOR_PARENT = Arrays.asList(ILanguage.ID,
      ILanguage.CODE);
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String id = (String) requestMap.get(ILanguage.ID);
    List<Map<String, Object>> children = null;
    Map<String, Object> languageMap = null;
    if (id.equals("-1")) {
      languageMap = new HashMap<>();
      children = getRootLanguages();
    }
    else {
      Vertex language = null;
      try {
        language = UtilClass.getVertexByIndexedId(id, VertexLabelConstants.LANGUAGE);
      }
      catch (NotFoundException e) {
        throw new LanguageNotFoundException(e);
      }
      
      String rid = language.getId()
          .toString();
      languageMap = UtilClass.getMapFromVertex(new ArrayList<>(), language);
      
      children = new ArrayList<>();
      
      getParent(languageMap, language);
      
      Iterable<Vertex> childrenVertices = LanguageRepository.getLanguageChildren(rid);
      
      for (Vertex child : childrenVertices) {
        Map<String, Object> childrenMap = UtilClass.getMapFromVertex(FIELDS_TO_FETCH, child);
        children.add(childrenMap);
      }
    }
    languageMap.put(ILanguage.CHILDREN, children);
    
    Map<String, Object> mapToReturn = new HashMap<>();
    mapToReturn.put(IGetLanguageModel.ENTITY, languageMap);
    return mapToReturn;
  }
  
  private void getParent(Map<String, Object> languageMap, Vertex language)
  {
    Map<String, Object> parentMap = new HashMap<>();
    Iterator<Vertex> parent = language
        .getVertices(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF)
        .iterator();
    if (parent.hasNext()) {
      parentMap = UtilClass.getMapFromVertex(FIELDS_TO_FETCH_FOR_PARENT, parent.next());
    }
    else {
      parentMap.put(ILanguage.ID, -1);
      parentMap.put(ILanguage.CODE, -1);
    }
    languageMap.put(ILanguage.PARENT, parentMap);
  }
  
  private List<Map<String, Object>> getRootLanguages() throws Exception
  {
    Iterable<Vertex> vertices = LanguageRepository.getRootLanguages();
    List<Map<String, Object>> children = new ArrayList<>();
    for (Vertex child : vertices) {
      Map<String, Object> childrenMap = UtilClass.getMapFromVertex(FIELDS_TO_FETCH, child);
      children.add(childrenMap);
    }
    return children;
  }
}
