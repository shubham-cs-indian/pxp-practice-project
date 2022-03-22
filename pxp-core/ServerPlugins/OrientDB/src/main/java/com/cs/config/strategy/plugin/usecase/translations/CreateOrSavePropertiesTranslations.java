package com.cs.config.strategy.plugin.usecase.translations;

import com.cs.config.strategy.plugin.usecase.translations.abstrct.AbstractSavePropertiesTranslations;
import com.cs.config.strategy.plugin.usecase.translations.utils.TranslationsUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;
import com.cs.core.config.interactor.model.translations.ISaveStandardTranslationModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.MultipleVertexFoundException;
import com.cs.core.runtime.interactor.constants.application.Seperators;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class CreateOrSavePropertiesTranslations extends AbstractSavePropertiesTranslations {
  
  public CreateOrSavePropertiesTranslations(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateOrSavePropertiesTranslations/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    return super.execute(requestMap);
  }
  
  @Override
  public void saveTranslations(String vertexLabel, List<String> languages,
      List<Map<String, Object>> list, IExceptionModel failure, List<Map<String, Object>> returnList)
  {
    createOrSaveTranslations(vertexLabel, languages, list, failure, returnList);
  }
  
  private void createOrSaveTranslations(String vertexLabel, List<String> languages,
      List<Map<String, Object>> list, IExceptionModel failure, List<Map<String, Object>> returnList)
  {
    // Default Label handling
    String defaultLanguage = TranslationsUtils.getDefaultLanguage();
    for (Map<String, Object> mapToSave : list) {
      String id = (String) mapToSave.get(ISaveStandardTranslationModel.ID);
      try {
        Vertex node = UtilClass.getVertexByIndexedId(id, vertexLabel);
        Map<String, Object> translations = (Map<String, Object>) mapToSave
            .get(ISaveStandardTranslationModel.TRANSLATIONS);
        createOrSaveTranslationsInNode(node, translations, defaultLanguage);
        
        // get for response
        Map<String, Object> map = TranslationsUtils.prepareTranslationsMap(languages, node);
        returnList.add(map);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, id, null);
      }
    }
  }
  
  private void createOrSaveTranslationsInNode(Vertex node, Map<String, Object> translations, String defaultLanguage)
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
          TranslationsUtils.updateContextLabelOnKlassLabelChanged(node, value, lang);
        }
        if (lang.equals(defaultLanguage)) {
          node.setProperty(CommonConstants.DEFAULT_LABEL, value);
        }
        node.setProperty(key, value);
        try {
          Vertex icon = UtilClass.getVertexByCode(node.getProperty(IConfigEntity.CODE), VertexLabelConstants.ENTITY_TYPE_ICON);
          icon.setProperty(key, value);
          if (lang.equals(defaultLanguage)) {
            icon.setProperty(CommonConstants.DEFAULT_LABEL, value);
          }
        }
        catch (Exception e) {
        }
      }
    }
  }
}
