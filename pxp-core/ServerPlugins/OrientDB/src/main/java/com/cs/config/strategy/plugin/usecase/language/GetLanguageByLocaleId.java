package com.cs.config.strategy.plugin.usecase.language;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ILanguage;
import com.cs.core.config.interactor.model.language.IGetLanguageModel;
import com.cs.core.exception.MultipleVertexFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.exception.language.LanguageNotFoundException;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

import java.util.*;

public class GetLanguageByLocaleId extends AbstractOrientPlugin {
  
  public GetLanguageByLocaleId(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetLanguageByLocaleId/*" };
  }
  
  private static final List<String> FIELDS_TO_FETCH            = Arrays.asList(ILanguage.LABEL,
      ILanguage.ID, ILanguage.CODE, ILanguage.LOCALE_ID, ILanguage.IS_STANDARD);
  
  private static final List<String> FIELDS_TO_FETCH_FOR_PARENT = Arrays.asList(ILanguage.ID,
      ILanguage.CODE);
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String id = (String) requestMap.get(ILanguage.ID);
    Vertex language = null;
    try {
      language = getVertexByLocaleId(id);
    }
    catch (NotFoundException e) {
      throw new LanguageNotFoundException(e);
    }

    Map<String, Object> languageMap = UtilClass.getMapFromVertex(FIELDS_TO_FETCH, language);

    getParent(languageMap, language);

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


  public static Vertex getVertexByLocaleId(String localeId) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> vertices = graph.getVertices(VertexLabelConstants.LANGUAGE,
        new String[] { CommonConstants.LOCALE_ID }, new String[] { localeId });
    Iterator<Vertex> iterator = vertices.iterator();
    if (!iterator.hasNext()) {
      throw new NotFoundException();
    }
    Vertex vertex = iterator.next();
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    return vertex;
  }

}
