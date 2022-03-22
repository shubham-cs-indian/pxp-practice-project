package com.cs.config.strategy.plugin.usecase.translations;

import com.cs.config.strategy.plugin.usecase.translations.utils.TranslationsUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.translations.ICreateStaticLabelTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.IStandardTranslationModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.Seperators;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class GetOrCreateStaticTranslations extends AbstractOrientPlugin {
  
  public GetOrCreateStaticTranslations(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetOrCreateStaticTranslations/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    // Default Label handling
    String defaultLanguage = TranslationsUtils.getDefaultLanguage();
    
    Map<String, Map<String, Object>> staticTranslations = (Map<String, Map<String, Object>>) requestMap
        .get(ICreateStaticLabelTranslationsRequestModel.STATIC_TRANSLATIONS);
    
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.UI_TRANSLATIONS, CommonConstants.CODE_PROPERTY);
    for (String key : staticTranslations.keySet()) {
      Map<String, Object> translationMap = (Map<String, Object>) staticTranslations.get(key);
      translationMap.put(CommonConstants.CODE_PROPERTY, key);
      try {
        Vertex translationVertex = UtilClass.getVertexById(key,
            VertexLabelConstants.UI_TRANSLATIONS);
        HashMap<String, Object> staticTranslationMap = getMapFromNode(translationVertex);
        Set<String> propertiesToSave = translationMap.keySet();
        Set<String> existingProperties = staticTranslationMap.keySet();
        for (String property : propertiesToSave) {
          if (property.equals(CommonConstants.CODE_PROPERTY)) {
            continue;
          }
          if (property.equals(CommonConstants.SCREENS)) {
            translationVertex.setProperty(property, (List<String>) translationMap.get(property));
            continue;
          }
          if (!existingProperties.contains(property)) {
            translationVertex.setProperty(property, (String) translationMap.get(property));
          }
        }
      }
      catch (NotFoundException e) {
        Vertex vertex = UtilClass.createNode(translationMap, vertexType, Arrays.asList());
        // Default Label handling
        if (defaultLanguage != null) {
          String label = (String) translationMap.get(IStandardTranslationModel.LABEL + Seperators.FIELD_LANG_SEPERATOR + defaultLanguage);
          if (label != null && !label.isEmpty()) {
            vertex.setProperty(CommonConstants.DEFAULT_LABEL, label);
          }
        }
      }
    }
    
    UtilClass.getGraph()
        .commit();
    
    return new HashMap<>();
  }

  private static HashMap<String, Object> getMapFromNode(Vertex node)
  {
    
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    for (String key : node.getPropertyKeys()) {
      if (key.equals(CommonConstants.CODE_PROPERTY)) {
        returnMap.put(CommonConstants.ID_PROPERTY, node.getProperty(key));
      }
      returnMap.put(key, node.getProperty(key));
    }
    return returnMap;
  }
}
