package com.cs.config.strategy.plugin.usecase.translations.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.template.ITemplate;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.config.interactor.model.translations.IGetPropertyTranslationsModel;
import com.cs.core.config.interactor.model.translations.IGetTagTranslationsModel;
import com.cs.core.config.interactor.model.translations.IRelationshipTranslationModel;
import com.cs.core.config.interactor.model.translations.ISaveStandardTranslationModel;
import com.cs.core.config.interactor.model.translations.IStandardTranslationModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.MultipleVertexFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.Seperators;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

@SuppressWarnings("unchecked")
public class TranslationsUtils {
  
  public static Map<String, Object> prepareTranslationsMap(Collection<String> languages,
      Vertex node)
  {
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> translations = new HashMap<String, Object>();
    for (String lang : languages) {
      Map<String, Object> translationMap = getTranslationMapFromVertex(lang, node);
      translations.put(lang, translationMap);
      if (lang.equals(UtilClass.getLanguage()
          .getUiLanguage())) {
        map.putAll(translationMap);
      }
    }
    map.put(IGetPropertyTranslationsModel.ID, UtilClass.getCodeNew(node));
    map.put(IGetPropertyTranslationsModel.TRANSLATION, translations);
    map.put(IGetPropertyTranslationsModel.CODE, node.getProperty(ITag.CODE));
    map.put(IGetPropertyTranslationsModel.TYPE, node.getProperty(IAttribute.TYPE));
    return map;
  }
  
  public static Map<String, Object> prepareTagTranslationsMap(Collection<String> languages,
      Vertex node)
  {
    Map<String, Object> map = prepareTranslationsMap(languages, node);
    map.put(IGetTagTranslationsModel.TAG_TYPE, node.getProperty(ITag.TAG_TYPE));
    return map;
  }
  
  public static Map<String, Object> getTranslationMapFromVertex(String lang, Vertex node)
  {
    Map<String, Object> translationMap = new HashMap<String, Object>();
    for (String field : IStandardTranslationModel.TRNASLATION_FIELDS) {
      if (field.equals(IStandardTranslationModel.LABEL) && lang.equals(CommonConstants.LANGUAGE_CODE_FOR_DEFAULT_LABEL_PROPERTY)) {
        translationMap.put(field, node.getProperty(CommonConstants.DEFAULT_LABEL));
      }
      else {
        translationMap.put(field, node.getProperty(field + Seperators.FIELD_LANG_SEPERATOR + lang));
      }
    }
    return translationMap;
  }
  
  public static void saveTranslationsInNode(Vertex node, Map<String, Object> translations)
      throws MultipleVertexFoundException
  {
    List<String> superClasses = UtilClass.getSuperClasses(node);
    String vertexType = ((OrientVertex) node).getType()
        .toString();
    for (String lang : translations.keySet()) {
      
      Map<String, Object> translationMap = (Map<String, Object>) translations.get(lang);
      for (String field : translationMap.keySet()) {
        String value = (String) translationMap.get(field);
        if (value == null) {
          continue;
        }
        
        if (!field.equals("label")) {
          node.setProperty(field + Seperators.FIELD_LANG_SEPERATOR + lang, value);
          continue;
        }
        
        if ((superClasses.contains(VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS)
            || vertexType.equals(VertexLabelConstants.ROOT_KLASS_TAXONOMY)) && !lang.equals(CommonConstants.LANGUAGE_CODE_FOR_DEFAULT_LABEL_PROPERTY)) {
          updateTemplateLabelOnKlassLabelChanged(node, value, lang);
          updateContextLabelOnKlassLabelChanged(node, value, lang);
        }
        
        if (lang.equals(CommonConstants.LANGUAGE_CODE_FOR_DEFAULT_LABEL_PROPERTY)) {
          node.setProperty(CommonConstants.DEFAULT_LABEL, value);
        }
        else {
          node.setProperty(field + Seperators.FIELD_LANG_SEPERATOR + lang, value);
        }
      }
    }
  }
  
  public static void createOrSaveTranslationsInNode(Vertex node, Map<String, Object> translations)
      throws MultipleVertexFoundException
  {
    List<String> superClasses = UtilClass.getSuperClasses(node);
    String vertexType = ((OrientVertex) node).getType()
        .toString();
    for (String lang : translations.keySet()) {
      Map<String, Object> translationMap = (Map<String, Object>) translations.get(lang);
      for (String field : translationMap.keySet()) {
        String value = (String) translationMap.get(field);
        if (value == null) {
          continue;
        }
        
        String key = field + Seperators.FIELD_LANG_SEPERATOR + lang;
        String existingValue = node.getProperty(key);
        if (existingValue != null) {
          continue;
        }
        if (!field.equals("label")) {
          node.setProperty(key, value);
          continue;
        }
        
        if (superClasses.contains(VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS)
            || vertexType.equals(VertexLabelConstants.ROOT_KLASS_TAXONOMY)) {
          updateContextLabelOnKlassLabelChanged(node, value, lang);
        }
        node.setProperty(key, value);
      }
    }
  }
  
  public static String prepareSearchOrSortField(String field, String lang)
  {
    if (lang == null || lang.equals("")) {
      lang = UtilClass.getLanguage()
          .getUiLanguage();
    }
    if (field == null || field.equals("")) {
      field = CommonConstants.LABEL_PROPERTY;
    }
    return field + Seperators.FIELD_LANG_SEPERATOR + lang;
  }
  
  public static String checkAndGetSortOrder(String sortOrder)
  {
    if (sortOrder == null || sortOrder.equals("")) {
      sortOrder = "asc";
    }
    return sortOrder;
  }
  
  public static void updateRelationshipSideLabel(Map<String, Object> side1)
  {
    String side1Label = (String) side1.remove(IRelationshipSide.LABEL);
    if (side1Label == null) {
      return;
    }
    side1.put(IRelationshipSide.LABEL + Seperators.FIELD_LANG_SEPERATOR + UtilClass.getLanguage()
        .getUiLanguage(), side1Label);
  }
  
  public static Map<String, Object> prepareRelationshipTranslationsMap(Collection<String> languages,
      Vertex node)
  {
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> translations = new HashMap<String, Object>();
    for (String lang : languages) {
      Map<String, Object> translationMap = getRelationshipTranslationMapFromVertex(lang, node);
      translations.put(lang, translationMap);
      if (lang.equals(UtilClass.getLanguage()
          .getUiLanguage())) {
        map.putAll(translationMap);
      }
    }
    map.put(IGetPropertyTranslationsModel.ID, UtilClass.getCodeNew(node));
    map.put(IGetPropertyTranslationsModel.TRANSLATION, translations);
    map.put(IGetPropertyTranslationsModel.CODE, node.getProperty(ITag.CODE));
    return map;
  }
  
  public static Map<String, Object> getRelationshipTranslationMapFromVertex(String lang,
      Vertex node)
  {
    Map<String, Object> translationMap = new HashMap<String, Object>();
    String property = null;
    if (lang.equals(CommonConstants.LANGUAGE_CODE_FOR_DEFAULT_LABEL_PROPERTY)) {
      property = CommonConstants.DEFAULT_LABEL;
    }
    else {
      property = IRelationship.LABEL + Seperators.FIELD_LANG_SEPERATOR + lang;
    }
    translationMap.put(IRelationshipTranslationModel.LABEL,
        node.getProperty(property));
    
    Map<String, Object> side1 = node.getProperty(IRelationship.SIDE1);
    translationMap.put(IRelationshipTranslationModel.SIDE_1_LABEL,
        side1.get(property));
    
    Map<String, Object> side2 = node.getProperty(IRelationship.SIDE2);
    translationMap.put(IRelationshipTranslationModel.SIDE_2_LABEL,
        side2.get(property));
    
    return translationMap;
  }
  
  public static void saveTranslations(String vertexLabel, List<String> languages,
      List<Map<String, Object>> list, IExceptionModel failure, List<Map<String, Object>> returnList)
  {
    filterOutInvalidLanguages(languages, failure);
    
    for (Map<String, Object> mapToSave : list) {
      // save
      String id = (String) mapToSave.get(ISaveStandardTranslationModel.ID);
      try {
        Vertex node = UtilClass.getVertexById(id, vertexLabel);
        Map<String, Object> translations = (Map<String, Object>) mapToSave
            .get(ISaveStandardTranslationModel.TRANSLATIONS);
        
        if(languages != null && !languages.isEmpty()) {
          translations.keySet().retainAll(languages);
        }
        
        TranslationsUtils.saveTranslationsInNode(node, translations);
        
        if(languages!= null && !languages.isEmpty()) 
        {
          Map<String, Object> map = TranslationsUtils.prepareTranslationsMap(languages, node);
          returnList.add(map);
        }
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, id, null);
      }
    }
    UtilClass.getGraph()
        .commit();
  }
  
  /**
   * Filters out the languages that aren't present in the system.
   * @param languages Translation languages.
   * @param failure Failure model to add details of invalid language.
   */
  private static void filterOutInvalidLanguages(List<String> languages, IExceptionModel failure)
  {
    if(languages == null) {
      return;
    }
    Iterator<String> iterator = languages.iterator();
    
    while(iterator.hasNext()) 
    {
      String language = iterator.next();
      
      try 
      {
        if(!language.equals(CommonConstants.LANGUAGE_CODE_FOR_DEFAULT_LABEL_PROPERTY)) {
          UtilClass.getVertexByCode(language, VertexLabelConstants.LANGUAGE);
        }
      }
      
      catch(Exception e)
      {
        iterator.remove();
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, language, null);
      }
    }
  }

  public static void updateTemplateLabelOnKlassLabelChanged(Vertex klassNode, String newLabel,
      String lang)
  {
    String oldLabel = klassNode.getProperty(IKlass.LABEL + Seperators.FIELD_LANG_SEPERATOR + lang);
    Vertex templateNode = null;
    Iterator<Edge> edgesIterator = klassNode
        .getEdges(Direction.OUT, RelationshipLabelConstants.HAS_TEMPLATE)
        .iterator();
    if (!edgesIterator.hasNext()) {
      return;
    }
    
    Edge hasTemplateEdge = edgesIterator.next();
    Boolean isInherited = hasTemplateEdge.getProperty(CommonConstants.IS_INHERITED_PROPERTY);
    if (isInherited != null && isInherited) {
      return;
    }
    
    templateNode = hasTemplateEdge.getVertex(Direction.IN);
    String templateLabel = (String) templateNode
        .getProperty(ITemplate.LABEL + Seperators.FIELD_LANG_SEPERATOR + lang);
    if (templateLabel != null && !templateLabel.equals(oldLabel)) {
      return;
    }
    
    templateNode.setProperty("label" + Seperators.FIELD_LANG_SEPERATOR + lang, newLabel);
  }
  
  public static void updateContextLabelOnKlassLabelChanged(Vertex klassNode, String newLabel,
      String lang) throws MultipleVertexFoundException
  {
    String natureType = (String) klassNode.getProperty(IKlass.NATURE_TYPE);
    if (natureType == null || natureType.isEmpty()) {
      return;
    }
    String oldLabel = klassNode.getProperty(IKlass.LABEL + Seperators.FIELD_LANG_SEPERATOR + lang);
    if (CommonConstants.CONTEXTUAL_KLASS_TYPES.contains(natureType)) {
      Vertex context = KlassUtils.getContextForKlass(klassNode);
      
      String contextLabel = (String) context
          .getProperty(IVariantContext.LABEL + Seperators.FIELD_LANG_SEPERATOR + lang);
      if (contextLabel != null && !contextLabel.equals(oldLabel)) {
        return;
      }
      context.setProperty(IVariantContext.LABEL + Seperators.FIELD_LANG_SEPERATOR + lang, newLabel);
    }
  }
  
  /**
   * Get Default language code.
   * @return
   */
  public static String getDefaultLanguage()
  {
    String query = "select from " + VertexLabelConstants.LANGUAGE + " where isDefaultLanguage = true";
    Iterable<Vertex> languageVertices = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
    String defaultLanguage = null;
    for (Vertex language : languageVertices) {
      defaultLanguage = language.getProperty(CommonConstants.CODE_PROPERTY);
    }
    return defaultLanguage;
  }
}
