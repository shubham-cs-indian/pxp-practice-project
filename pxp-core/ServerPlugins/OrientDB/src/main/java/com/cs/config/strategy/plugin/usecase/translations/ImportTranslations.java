package com.cs.config.strategy.plugin.usecase.translations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.language.util.LanguageUtil;
import com.cs.config.strategy.plugin.usecase.translations.utils.TranslationsUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.propertycollection.ISectionRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.translations.IRelationshipTranslationModel;
import com.cs.core.config.interactor.model.translations.IStandardTranslationModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.MultipleVertexFoundException;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.Seperators;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

public class ImportTranslations extends AbstractOrientPlugin {
  
  private static String DEFAULT_LANG_LABEL_EMPTY_MSG = "Default language label can't be empty";
  
  public ImportTranslations(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|ImportTranslations/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> translationList = (List<Map<String, Object>>) requestMap.get(CommonConstants.LIST_PROPERTY);
    List<Map<String, Object>> successTranslationList = new ArrayList<>();
    List<Map<String, Object>> failedTranslationList = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    List<Map<String, Object>> responseMap = new ArrayList<Map<String, Object>>();
    
    Vertex defaultLanguageVertex = LanguageUtil.getDefaultLanguageVertex(true);
    String defaultLanguageCode = defaultLanguageVertex.getProperty(CommonConstants.CODE_PROPERTY);
    
    List<String> languageCodes = getAllLanguageCodes();
    languageCodes.add(CommonConstants.LANGUAGE_CODE_FOR_DEFAULT_LABEL_PROPERTY);
    
    for (Map<String, Object> translation : translationList) {
      try {
        Map<String, Object> translationMap = updateTranslation(translation, defaultLanguageCode, failedTranslationList, languageCodes);
        Map<String, Object> createdTransltionMap = new HashMap<>();
        createdTransltionMap.put(CommonConstants.CODE_PROPERTY, translation.get(CommonConstants.CODE_PROPERTY));
        successTranslationList.add(createdTransltionMap);
        responseMap.add(translationMap);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null, (String) translation.get(CommonConstants.LABEL_PROPERTY));
        addToFailureIds(failedTranslationList, translation);
      }
    }
    
    Map<String, Object> result = new HashMap<>();
    result.put(IPluginSummaryModel.SUCCESS, successTranslationList);
    result.put(IPluginSummaryModel.FAILURE, failure);
    result.put(IPluginSummaryModel.FAILED_IDS, failedTranslationList);
    return result;
  }
  
  private List<String> getAllLanguageCodes()
  {
    Iterable<Vertex> verticesOfClass = UtilClass.getGraph().getVerticesOfClass(VertexLabelConstants.LANGUAGE);
    List<String> languageCodes = new ArrayList<>();
    
    for (Vertex language : verticesOfClass) {
      languageCodes.add(language.getProperty(CommonConstants.CODE_PROPERTY));
    }
    return languageCodes;
  }
  
  private Map<String, Object> updateTranslation(Map<String, Object> translation, String defaultLanguageCode,
      List<Map<String, Object>> failedTranslationList, List<String> languageCodes) throws Exception
  {
    String entityType = (String) translation.get(ConfigTag.type.name());
    String vertexLabel = EntityUtil.getVertexLabelByEntityType(entityType);
    
    if (entityType.equals(CommonConstants.RELATIONSHIP)) {
      saveRelationTranslationsInNode(vertexLabel, translation, defaultLanguageCode, failedTranslationList, languageCodes);
    }
    else {
      saveTranslationProperties(vertexLabel, translation, defaultLanguageCode, failedTranslationList, languageCodes);
    }
    
    return translation;
  }
  
  private void saveTranslationProperties(String vertexLabel, Map<String, Object> translations, String defaultLanguageCode,
      List<Map<String, Object>> failedTranslationList, List<String> languageCodes) throws Exception
  {
    String code = (String) translations.get(CommonConstants.CODE_PROPERTY);
    Vertex node = UtilClass.getVertexByCode(code, vertexLabel);
    Map<String, Object> languageTranslation = (Map<String, Object>) translations.get(ConfigTag.languageTranslation.name());
    savePropertyTranslationsInNode(node, languageTranslation, defaultLanguageCode, failedTranslationList, languageCodes);
    UtilClass.getGraph().commit();
  }
  
  private void savePropertyTranslationsInNode(Vertex node, Map<String, Object> translations, String defaultLanguageCode,
      List<Map<String, Object>> failedTranslationList, List<String> languageCodes) throws MultipleVertexFoundException
  {
    List<String> superClasses = UtilClass.getSuperClasses(node);
    String vertexType = ((OrientVertex) node).getType().toString();
    String code = node.getProperty(CommonConstants.CODE_PROPERTY);
    for (String lang : translations.keySet()) {
      if (languageCodes.contains(lang)) {
        Map<String, Object> translationMap = (Map<String, Object>) translations.get(lang);
        for (String field : translationMap.keySet()) {
          String value = (String) translationMap.get(field);
          if (value == null) {
            continue;
          }
          
          if (field.equals(IStandardTranslationModel.LABEL) && lang.equals(CommonConstants.LANGUAGE_CODE_FOR_DEFAULT_LABEL_PROPERTY)) {
            node.setProperty(CommonConstants.DEFAULT_LABEL, value);
            continue;
          }
          
          if (!field.equals(CommonConstants.LABEL_PROPERTY)) {
            node.setProperty(field + Seperators.FIELD_LANG_SEPERATOR + lang, value);
            continue;
          }
          else {
            // default language label can't be modify with empty value
            if (checkDefaultLabel(defaultLanguageCode, lang, value)) {
              addDefaultLanguageEmptyMsg(failedTranslationList, code, lang);
              continue;
            }
            
            if (superClasses.contains(VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS)
                || vertexType.equals(VertexLabelConstants.ROOT_KLASS_TAXONOMY)) {
              TranslationsUtils.updateTemplateLabelOnKlassLabelChanged(node, value, lang);
              TranslationsUtils.updateContextLabelOnKlassLabelChanged(node, value, lang);
            }
            node.setProperty(field + Seperators.FIELD_LANG_SEPERATOR + lang, value);
          }
        }
      }
      else {
        notValidLanguageException(failedTranslationList, code, lang);
      }
    }
  }
  
  private static void notValidLanguageException(List<Map<String, Object>> failedTranslationList, String code, String lang)
  {
    Map<String, Object> exceptionMap = new HashMap<>();
    exceptionMap.put(code, lang + " Not valid language");
    failedTranslationList.add(exceptionMap);
  }
  
  private static void addDefaultLanguageEmptyMsg(List<Map<String, Object>> failedTranslationList, String code, String lang)
  {
    Map<String, Object> exceptionMap = new HashMap<>();
    exceptionMap.put(code + Seperators.FIELD_LANG_SEPERATOR + lang, DEFAULT_LANG_LABEL_EMPTY_MSG);
    failedTranslationList.add(exceptionMap);
  }
  
  private void addToFailureIds(List<Map<String, Object>> failedTranslationList, Map<String, Object> translation)
  {
    Map<String, Object> failedTranslationMap = new HashMap<>();
    failedTranslationMap.put(CommonConstants.CODE_PROPERTY, translation.get(CommonConstants.CODE_PROPERTY));
    failedTranslationList.add(failedTranslationMap);
  }
  
  protected void saveRelationTranslationsInNode(String vertextLabel, Map<String, Object> translations, String defaultLanguageCode,
      List<Map<String, Object>> failedTranslationList, List<String> languageCodes) throws Exception
  {
    String code = (String) translations.get(CommonConstants.CODE_PROPERTY);
    Vertex node = UtilClass.getVertexByCode(code, vertextLabel);
    Map<String, Object> translationObject = (Map<String, Object>) translations.get(ConfigTag.languageTranslation.name());
    for (String lang : translationObject.keySet()) {
      if (languageCodes.contains(lang)) {
        Map<String, Object> translationMap = (Map<String, Object>) translationObject.get(lang);
        
        String label = (String) translationMap.get(IRelationshipTranslationModel.LABEL);
        if (label != null) {
          boolean isDefaultLabel = checkDefaultLabel(defaultLanguageCode, lang, label);
          if (isDefaultLabel) {
            addDefaultLanguageEmptyMsg(failedTranslationList, code, lang);
          }
          else if(lang.equals(CommonConstants.LANGUAGE_CODE_FOR_DEFAULT_LABEL_PROPERTY)){
            node.setProperty(CommonConstants.DEFAULT_LABEL, label);
          }
          else {
            node.setProperty(IRelationship.LABEL + Seperators.FIELD_LANG_SEPERATOR + lang, label);
          }
        }
        
        String side1Label = (String) translationMap.get(IRelationshipTranslationModel.SIDE_1_LABEL);
        if (side1Label != null) {
          updateSideLabel(node, side1Label, lang, IRelationship.SIDE1, defaultLanguageCode, failedTranslationList, code);
        }
        
        String side2Label = (String) translationMap.get(IRelationshipTranslationModel.SIDE_2_LABEL);
        if (side2Label != null) {
          updateSideLabel(node, side2Label, lang, IRelationship.SIDE2, defaultLanguageCode, failedTranslationList, code);
        }
      }
      else {
        notValidLanguageException(failedTranslationList, code, defaultLanguageCode);
      }
    }
  }
  
  private boolean checkDefaultLabel(String defaultLanguageCode, String lang, String label)
  {
    boolean isDefaultLabel = defaultLanguageCode.equals(lang) && StringUtils.isEmpty(label);
    return isDefaultLabel;
  }
  
  protected void updateSideLabel(Vertex relationshipNode, String sideLabel, String lang, String side, String defaultLanguageCode,
      List<Map<String, Object>> failedTranslationList, String code) throws Exception
  {
    boolean isDefaultLabel = checkDefaultLabel(defaultLanguageCode, lang, sideLabel);
    if (isDefaultLabel) {
      addDefaultLanguageEmptyMsg(failedTranslationList, code + ":" + side, lang);
    }
    else {
      Map<String, Object> sideMap = relationshipNode.getProperty(side);
      sideMap.put(IRelationshipSide.LABEL + Seperators.FIELD_LANG_SEPERATOR + lang, sideLabel);
      relationshipNode.setProperty(side, sideMap);
      
      String klassId = (String) sideMap.get(IRelationshipSide.KLASS_ID);
      Vertex klassNode = UtilClass.getVertexById(klassId, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      
      Vertex klassPropertyNode = KlassUtils.getRespectiveKlassPropertyNode(klassNode, relationshipNode);
      Map<String, Object> relationshipSideMap = klassPropertyNode.getProperty(ISectionRelationship.RELATIONSHIP_SIDE);
      relationshipSideMap.put(IRelationshipSide.LABEL + Seperators.FIELD_LANG_SEPERATOR + lang, sideLabel);
      klassPropertyNode.setProperty(ISectionRelationship.RELATIONSHIP_SIDE, relationshipSideMap);
    }
  }
  
}
