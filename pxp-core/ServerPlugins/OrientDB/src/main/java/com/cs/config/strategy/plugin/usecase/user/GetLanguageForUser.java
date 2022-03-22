package com.cs.config.strategy.plugin.usecase.user;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ILanguage;
import com.cs.core.config.interactor.exception.user.UserNotFoundException;
import com.cs.core.config.interactor.model.user.IUserLanguageModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.exception.language.LanguageNotFoundException;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.repository.language.LanguageRepository;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class GetLanguageForUser extends AbstractOrientPlugin {
  
  public GetLanguageForUser(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    String userId = null;
    HashMap<String, Object> returnMap = new HashMap<>();
    userId = (String) requestMap.get(IIdParameterModel.ID);
    try {
      Vertex userNode = UtilClass.getVertexByIndexedId(userId, VertexLabelConstants.ENTITY_TYPE_USER);
      getPreferredLanguages(returnMap, userNode);
    }
    catch (NotFoundException e) {
      throw new UserNotFoundException();
    }
    UtilClass.getGraph().commit();
    
    return returnMap;
  }

  /**
   * @param userMap
   * @param userNode
   * @throws Exception 
   */
  private void getPreferredLanguages(Map<String, Object> returnMap, Vertex userNode) throws Exception
  {
    Vertex defaultLanguageNode = getDefaultLanguageNode();

    Boolean isUILanguageChanged = getOrSetPreferredLanguage(returnMap, userNode, defaultLanguageNode,
        RelationshipLabelConstants.HAS_PREFERRED_UI_LANGUAGE, IUserLanguageModel.PREFERRED_UI_LANGUAGE,
        ILanguage.IS_USER_INTERFACE_LANGUAGE);
    Boolean isDataLanguageChanged = getOrSetPreferredLanguage(returnMap, userNode, defaultLanguageNode,
        RelationshipLabelConstants.HAS_PREFERRED_DATA_LANGUAGE, IUserLanguageModel.PREFERRED_DATA_LANGUAGE, ILanguage.IS_DATA_LANGUAGE);
    
    returnMap.put(IUserLanguageModel.IS_LANGUAGE_CHANGED, isUILanguageChanged || isDataLanguageChanged);
  }

  /**
   * @param returnMap
   * @param userNode
   * @param defaultLanguageNode
   * @param relationshipLabel
   * @param returnLabel
   * @param langProperty
   * @return
   */
  private Boolean getOrSetPreferredLanguage(Map<String, Object> returnMap, Vertex userNode,
      Vertex defaultLanguageNode, String relationshipLabel, String returnLabel, String langProperty)
  {
    Boolean isLanguageChanged = false;
    Iterator<Vertex> preferredLanguage = userNode.getVertices(Direction.OUT, relationshipLabel).iterator();
    if (preferredLanguage.hasNext()) {
      Vertex uiLangNode = preferredLanguage.next();
      if ((Boolean) uiLangNode.getProperty(langProperty)) {
        returnMap.put(returnLabel, uiLangNode.getProperty(ILanguage.CODE));
      }
      else {
        isLanguageChanged = handleLanguageChanged(returnMap, userNode, defaultLanguageNode, relationshipLabel, returnLabel);        
      }
    }
    else {
      isLanguageChanged = handleLanguageChanged(returnMap, userNode, defaultLanguageNode, relationshipLabel, returnLabel);
    }
    return isLanguageChanged;
  }

  /**
   * @param returnMap
   * @param userNode
   * @param defaultLanguageNode
   * @param relationshipLabel
   * @param returnLabel
   * @return
   */
  private boolean handleLanguageChanged(Map<String, Object> returnMap, Vertex userNode,
      Vertex defaultLanguageNode, String relationshipLabel, String returnLabel)
  {
    Iterable<Edge> edges = userNode.getEdges(Direction.OUT, relationshipLabel);
    edges.forEach(edge -> edge.remove());
    userNode.addEdge(relationshipLabel, defaultLanguageNode);
    returnMap.put(returnLabel, defaultLanguageNode.getProperty(ILanguage.CODE));
    return true;
  }

  /**
   * @return
   * @throws Exception
   * @throws LanguageNotFoundException
   */
  private Vertex getDefaultLanguageNode() throws Exception
  {
    Iterable<Vertex> language = LanguageRepository.getCurrentDefaultLanguage();
    Iterator<Vertex> languageIterator = language.iterator();
    
    if (!languageIterator.hasNext()) {
      throw new LanguageNotFoundException();
    }
    return languageIterator.next();
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetLanguageForUser/*" };
  }
}
