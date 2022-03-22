package com.cs.config.strategy.plugin.usecase.user.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.datarule.util.DataRuleUtils;
import com.cs.config.strategy.plugin.usecase.governancerule.GovernanceRuleUtil;
import com.cs.config.strategy.plugin.usecase.language.util.LanguageUtil;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.util.ValidationUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ILanguage;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class UserUtils {
  
  public static Map<String, Object> createUser(Map<String, Object> userMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    
    if (ValidationUtils.validateUserInfo(userMap)) {
      String preferredUILanguage = (String) userMap.remove(IUserModel.PREFERRED_UI_LANGUAGE);
      String preferredDataLanguage = (String) userMap.remove(IUserModel.PREFERRED_DATA_LANGUAGE);
      OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
          VertexLabelConstants.ENTITY_TYPE_USER, CommonConstants.CODE_PROPERTY);
      Vertex userNode = UtilClass.createNode(userMap, vertexType, new ArrayList<>());
      if (((String) userMap.get(CommonConstants.ID_PROPERTY))
          .equals(CommonConstants.ADMIN_USER_ID)) {
        userNode.setProperty(CommonConstants.CODE_PROPERTY, CommonConstants.ADMIN_USER_ID);
      }
      Map<String, Object> returnMap = new HashMap<>();
      addOrUpdatePreferredLanguages(preferredUILanguage, preferredDataLanguage, userNode, returnMap);
      returnMap.putAll(UtilClass.getMapFromNode(userNode));
      graph.commit();
      
      return returnMap;
    }
    return null;
  }
  
  /**
   * @param userNode
   */
  public static void deleteUserInRelationships(Vertex userNode)
  {
    Iterable<Edge> UserInRelationships = userNode.getEdges(com.tinkerpop.blueprints.Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_USER_IN);
    for (Edge userInRelationship : UserInRelationships) {
      userInRelationship.remove();
    }
  }
  
  /**
   * @param userNode
   */
  public static void deleteUserRelationshipsFromUserGroup(Vertex userNode)
  {
    Iterable<Edge> UserInRelationships = userNode.getEdges(com.tinkerpop.blueprints.Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_BELONGS_TO);
    for (Edge userInRelationship : UserInRelationships) {
      userInRelationship.remove();
    }
  }
  
  /**
   * @param userNode
   */
  public static void deleteNormalizationNodesAttached(Vertex userNode)
  {
    Iterable<Vertex> normalizations = userNode.getVertices(Direction.IN,
        RelationshipLabelConstants.NORMALIZATION_USER_LINK);
    for (Vertex normalization : normalizations) {
      
      String countQuery = "select count(*) from "
          + RelationshipLabelConstants.NORMALIZATION_USER_LINK + " where out = "
          + normalization.getId();
      Long userNodeCount = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
      
      if (userNodeCount == 1) {
        normalization.remove();
      }
    }
  }
  
  /**
   * @param userNode
   */
  public static void deleteRuleNodesAttached(Vertex userNode)
  {
    Iterable<Vertex> ruleNodes = userNode.getVertices(Direction.IN,
        RelationshipLabelConstants.RULE_USER_LINK);
    for (Vertex ruleNode : ruleNodes) {
      String countQuery = "select count(*) from " + RelationshipLabelConstants.RULE_USER_LINK
          + " where out = " + ruleNode.getId();
      Long userNodeCount = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
      if (userNodeCount == 1) {
        DataRuleUtils.deleteRuleNode(ruleNode);
      }
    }
  }
  
  /**
   * @param userNode
   */
  public static void deleteGovernanceRuleNodesAttached(Vertex userNode)
  {
    Iterable<Vertex> ruleNodes = userNode.getVertices(Direction.IN,
        RelationshipLabelConstants.GOVERNANCE_RULE_USER_LINK);
    for (Vertex ruleNode : ruleNodes) {
      String countQuery = "select count(*) from "
          + RelationshipLabelConstants.GOVERNANCE_RULE_USER_LINK + " where out = "
          + ruleNode.getId();
      Long userNodeCount = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
      if (userNodeCount == 1) {
        GovernanceRuleUtil.deleteRuleNode(ruleNode);
      }
    }
  }
  
  /**
   * @param userNode
   */
  public static void deleteUserNode(Vertex userNode)
  {
    deleteUserInRelationships(userNode);
    deleteUserRelationshipsFromUserGroup(userNode);
    deleteNormalizationNodesAttached(userNode);
    deleteRuleNodesAttached(userNode);
    deleteGovernanceRuleNodesAttached(userNode);
  }
  
  /**Create edges between userNode ->Preferred UI/Data language Node ,
   * setPreferred UI and Data Languages to defaultLanguage if null 
   * @param preferredUILanguage
   * @param preferredDataLanguage
   * @param userNode 
   * @param userMap 
   * @throws Exception
   */
  public static void addOrUpdatePreferredLanguagesOld(String preferredUILanguage, String preferredDataLanguage, Vertex userNode, Map<String, Object> userMap) throws Exception
  {
    Vertex defaultLanguageNode = LanguageUtil.getDefaultLanguageVertex(false);
    String defaultLanguage = defaultLanguageNode.getProperty(ILanguage.ID);
    Map <String, Object> existingUser = new HashMap<String, Object>();
    getPreferredLanguages(existingUser, userNode);
    
    
    //TODO : remove edges if already exists
    if (preferredDataLanguage != null) {
      Vertex dataLangVertex = UtilClass.getVertexById(preferredDataLanguage, VertexLabelConstants.LANGUAGE);
      userNode.addEdge(RelationshipLabelConstants.HAS_PREFERRED_DATA_LANGUAGE, dataLangVertex);
      userMap.put(IUserModel.PREFERRED_DATA_LANGUAGE, preferredDataLanguage);
    }
    else {
      userNode.addEdge(RelationshipLabelConstants.HAS_PREFERRED_DATA_LANGUAGE, defaultLanguageNode);
      userMap.put(IUserModel.PREFERRED_DATA_LANGUAGE, defaultLanguage);
    }
    
    if (preferredUILanguage != null) {
      Vertex uiLangVertex = UtilClass.getVertexById(preferredUILanguage, VertexLabelConstants.LANGUAGE);
      userNode.addEdge(RelationshipLabelConstants.HAS_PREFERRED_UI_LANGUAGE, uiLangVertex);
      userMap.put(IUserModel.PREFERRED_UI_LANGUAGE, preferredUILanguage);
    }
    else {
      userNode.addEdge(RelationshipLabelConstants.HAS_PREFERRED_UI_LANGUAGE, defaultLanguageNode);
      userMap.put(IUserModel.PREFERRED_UI_LANGUAGE, defaultLanguage);
    }
  }
  
  
  /**Create edges between userNode -> Preferred UI/Data language Node 
   * setPreferred UI and Data Languages to defaultLanguage 
   * according to existing lanuage and new lang id value.
   * @param preferredUILanguage
   * @param preferredDataLanguage
   * @param userNode 
   * @param userMap 
   * @throws Exception
   */
  public static void addOrUpdatePreferredLanguages(String newprefUILang, String newPrefDataLang, Vertex userNode, Map<String, Object> userMap) throws Exception
  {
    Vertex defaultLanguageNode = LanguageUtil.getDefaultLanguageVertex(false);
    String defaultLanguage = defaultLanguageNode.getProperty(ILanguage.ID);
  
    Iterator<Edge> existingPfUILangItr = userNode.getEdges(Direction.OUT, RelationshipLabelConstants.HAS_PREFERRED_UI_LANGUAGE).iterator();
    Iterator<Edge> existingPfDataLangItr = userNode.getEdges(Direction.OUT, RelationshipLabelConstants.HAS_PREFERRED_DATA_LANGUAGE).iterator();
    if (existingPfUILangItr.hasNext()) {
      Edge edge = existingPfUILangItr.next();
      String existingPrefUILang = edge.getVertex(Direction.IN).getProperty(ILanguage.CODE);
      if (newprefUILang != null && !newprefUILang.equals(existingPrefUILang)) {
        // Remove old UI language and add new UI language
        edge.remove();
        Vertex uiLangVertex = UtilClass.getVertexById(newprefUILang, VertexLabelConstants.LANGUAGE);
        userNode.addEdge(RelationshipLabelConstants.HAS_PREFERRED_UI_LANGUAGE, uiLangVertex);
      }
    }
    else {
      if (newprefUILang == null || newprefUILang.isEmpty()) {
        //add default ui lang
        userNode.addEdge(RelationshipLabelConstants.HAS_PREFERRED_UI_LANGUAGE, defaultLanguageNode);
      }
      else {
        // add new ui lang
        Vertex uiLangVertex = UtilClass.getVertexById(newprefUILang, VertexLabelConstants.LANGUAGE);
        userNode.addEdge(RelationshipLabelConstants.HAS_PREFERRED_UI_LANGUAGE, uiLangVertex);
      }
      
    }
    if (existingPfDataLangItr.hasNext()) {
      Edge edge = existingPfDataLangItr.next();
      String existingPrefDataLang =  edge.getVertex(Direction.IN).getProperty(ILanguage.CODE);
      if (newPrefDataLang != null && !newPrefDataLang.equals(existingPrefDataLang)) {
        // remove old Data language and add Data UIlanguage
        edge.remove();
        Vertex dataLangVertex = UtilClass.getVertexById(newPrefDataLang, VertexLabelConstants.LANGUAGE);
        userNode.addEdge(RelationshipLabelConstants.HAS_PREFERRED_DATA_LANGUAGE, dataLangVertex);
      }
    }
    else {
      if (newPrefDataLang == null || newPrefDataLang.isEmpty()) {
        //add default lang
        userNode.addEdge(RelationshipLabelConstants.HAS_PREFERRED_DATA_LANGUAGE, defaultLanguageNode);
        userMap.put(IUserModel.PREFERRED_DATA_LANGUAGE, defaultLanguage);
      }
      else {
        // add new lang
        Vertex dataLangVertex = UtilClass.getVertexById(newPrefDataLang, VertexLabelConstants.LANGUAGE);
        userNode.addEdge(RelationshipLabelConstants.HAS_PREFERRED_DATA_LANGUAGE, dataLangVertex);
      }
    }
    
    // put new pref lang in userMap.
    getPreferredLanguages(userMap, userNode);
  }
  
  /**Get user Preferred Languages from user node linked edges.
   * @param userMap
   * @param userNode
   * 
   */
  public static void getPreferredLanguages(Map<String, Object> userMap, Vertex userNode)
  {
    Iterator<Vertex> preferredUILanguage = userNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PREFERRED_UI_LANGUAGE).iterator();
    Iterator<Vertex> preferredDataLanguage = userNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PREFERRED_DATA_LANGUAGE).iterator();
    if (preferredUILanguage.hasNext()) {
      userMap.put(IUserModel.PREFERRED_UI_LANGUAGE, preferredUILanguage.next().getProperty(ILanguage.CODE));
    }
    
    if (preferredDataLanguage.hasNext()) {
      userMap.put(IUserModel.PREFERRED_DATA_LANGUAGE, preferredDataLanguage.next().getProperty(ILanguage.CODE));
    }
  }
}
