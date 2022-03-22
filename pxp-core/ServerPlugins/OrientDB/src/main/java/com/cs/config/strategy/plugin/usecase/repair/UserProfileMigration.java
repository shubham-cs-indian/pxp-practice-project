package com.cs.config.strategy.plugin.usecase.repair;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.cs.config.strategy.plugin.usecase.language.util.LanguageUtil;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class UserProfileMigration extends AbstractOrientPlugin {
  
  private static final String ABOUT_CODE_KEY    = "ABOUT";
  private static final String HELP_CODE_KEY     = "HELP";
  private static final String USER_SAVE_MESSAGE = "USER_SUCCESSFULLY_SAVED";
  
  public UserProfileMigration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|UserProfileMigration/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    
    // Add preferred UI and Data languages for existing users.
    Vertex defaultLanguageNode = LanguageUtil.getDefaultLanguageVertex(false);
    String query = "select * from " + VertexLabelConstants.ENTITY_TYPE_USER;
    Iterable<Vertex> userVertices = graph.command(new OCommandSQL(query)).execute();
    for (Vertex userNode : userVertices) {
      userNode.addEdge(RelationshipLabelConstants.HAS_PREFERRED_DATA_LANGUAGE, defaultLanguageNode);
      userNode.addEdge(RelationshipLabelConstants.HAS_PREFERRED_UI_LANGUAGE, defaultLanguageNode);
    }

    // Update Translations.
    List<String> classCodes = Arrays.asList(ABOUT_CODE_KEY, HELP_CODE_KEY, USER_SAVE_MESSAGE);
    // Map of translation values of "ABOUT" before and after migration with respect to its language
    Map<String, Map<String, String>> translationsForAbout = new HashMap<String, Map<String, String>>();
    translationsForAbout.put("en_US", Collections.singletonMap("About", "About PXP"));
    translationsForAbout.put("en_UK", Collections.singletonMap("About", "About PXP"));
    translationsForAbout.put("es_ES", Collections.singletonMap("Acerca de", "Acerca de PXP"));
    translationsForAbout.put("fr_FR", Collections.singletonMap("Sur", "Sur PXP"));
    translationsForAbout.put("de_DE", Collections.singletonMap("Über", "Über PXP"));
    
    // Map of translation values of "HELP" before and after migration with respect to its language
    Map<String, Map<String, String>> translationsForHelp = new HashMap<String, Map<String, String>>();
    translationsForHelp.put("en_US", Collections.singletonMap("Help", "Support"));
    translationsForHelp.put("en_UK", Collections.singletonMap("Help", "Support"));
    translationsForHelp.put("es_ES", Collections.singletonMap("Ayuda", "Apoyo"));
    translationsForHelp.put("fr_FR", Collections.singletonMap("Aidez-moi", "Soutien"));
    translationsForHelp.put("de_DE", Collections.singletonMap("Hilfe", "Unterstützung"));
    
    // Map of translation values of "USER_SAVE_MESSAGE" before and after migration with respect to its language
    Map<String, Map<String, String>> translationsForUserMessage = new HashMap<String, Map<String, String>>();
    translationsForUserMessage.put("en_US", Collections.singletonMap("User successfully saved !!!", "User successfully saved"));
    translationsForUserMessage.put("en_UK", Collections.singletonMap("User successfully saved !!!", "User successfully saved"));
    translationsForUserMessage.put("es_ES", Collections.singletonMap("Usuario guardado correctamente !!!", "Usuario guardado correctamente"));
    translationsForUserMessage.put("fr_FR", Collections.singletonMap("L'utilisateur a bien été enregistré !!!", "L'utilisateur a bien été enregistré"));
    translationsForUserMessage.put("de_DE", Collections.singletonMap("Benutzer erfolgreich gespeichert !!!", "Benutzer erfolgreich gespeichert"));
    
    // Get All UI translations Vertex nodes for given code properties
    StringBuilder queryForUItranslations = new StringBuilder("Select From ")
        .append(VertexLabelConstants.UI_TRANSLATIONS)
        .append(" where ")
        .append(CommonConstants.CODE_PROPERTY)
        .append(" in ")
        .append(EntityUtil.quoteIt(classCodes));
    
    Iterable<Vertex> uiTranslationsVertexNodes = graph.command(new OCommandSQL(queryForUItranslations.toString())).execute();    
    for (Vertex uiTranslationVertexNode : uiTranslationsVertexNodes) {
      String codeKey = uiTranslationVertexNode.getProperty(CommonConstants.CODE_PROPERTY);
      switch (codeKey) {
        case ABOUT_CODE_KEY:
          updateUITranslationVertexNode(translationsForAbout, uiTranslationVertexNode);
          break;
        case HELP_CODE_KEY:
          updateUITranslationVertexNode(translationsForHelp, uiTranslationVertexNode);
        case USER_SAVE_MESSAGE:
          updateUITranslationVertexNode(translationsForUserMessage, uiTranslationVertexNode);
        default:
          break;
      }
    }
    graph.commit();
    return null;
  }
  
  /**
   * Update labels of UI translation Vertex Node
   * 
   * @param codeKey
   * @param translationsForCodeKeys
   * @param uiTranslationVertexNode
   */
  private void updateUITranslationVertexNode(Map<String, Map<String, String>> translationsForCodeKeys, Vertex uiTranslationVertexNode)
  {
      for (Entry<String, Map<String, String>> langTranslation : translationsForCodeKeys.entrySet()) {
        String propertyKey = "label__" + langTranslation.getKey();
        Map<String, String> oldVsNewTranslation = langTranslation.getValue();
        
        // If userdefined translation value and old value are same then only we need to update label of uiTranslationVertexNode.        
        setNewTranslationValueToVertexNode(uiTranslationVertexNode, propertyKey, oldVsNewTranslation);  
        
        // If userdefined default translation value and old value are same then only we need to b update default label of uiTranslationVertexNode.
        setNewTranslationValueToVertexNode(uiTranslationVertexNode, CommonConstants.DEFAULT_LABEL, oldVsNewTranslation);
      }
    }

  /**
   * Update property of uiTranslationVertexNode
   * @param uiTranslationVertexNode
   * @param propertyKey
   * @param oldVsNewTranslation
   */
  private void setNewTranslationValueToVertexNode(Vertex uiTranslationVertexNode, String propertyKey, Map<String, String> oldVsNewTranslation)
  {
    String uiTranslationCurrentValue = uiTranslationVertexNode.getProperty(propertyKey);
    if(uiTranslationCurrentValue != null) {
      String newTranslationValue =  oldVsNewTranslation.get(uiTranslationCurrentValue);          
      if (newTranslationValue != null) {
        uiTranslationVertexNode.setProperty(propertyKey, newTranslationValue);
      }
    }
  }  
}
