package com.cs.config.strategy.plugin.usecase.translations;

import com.cs.config.strategy.plugin.usecase.translations.abstrct.AbstractSaveRelationshipTranslations;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.model.translations.IRelationshipTranslationModel;
import com.cs.core.runtime.interactor.constants.application.Seperators;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.Map;

@SuppressWarnings("unchecked")
public class SaveRelationshipTranslations extends AbstractSaveRelationshipTranslations {
  
  public SaveRelationshipTranslations(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveRelationshipTranslations/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    return super.execute(requestMap);
  }
  
  @Override
  protected void saveTranslationsInNode(Vertex node, Map<String, Object> translations, String defaultLanguage)
      throws Exception
  {
    for (String lang : translations.keySet()) {
      Map<String, Object> translationMap = (Map<String, Object>) translations.get(lang);
      
      String label = (String) translationMap.get(IRelationshipTranslationModel.LABEL);
      // Default Label handling
      if (label != null && !lang.equals(CommonConstants.LANGUAGE_CODE_FOR_DEFAULT_LABEL_PROPERTY)) {
        node.setProperty(IRelationship.LABEL + Seperators.FIELD_LANG_SEPERATOR + lang, label);
      }
      else if (label != null && lang.equals(CommonConstants.LANGUAGE_CODE_FOR_DEFAULT_LABEL_PROPERTY)) {
        node.setProperty(CommonConstants.DEFAULT_LABEL, label);
      }
      
      String side1Label = (String) translationMap.get(IRelationshipTranslationModel.SIDE_1_LABEL);
      if (side1Label != null) {
        updateSideLabel(node, side1Label, lang, IRelationship.SIDE1);
      }
      
      String side2Label = (String) translationMap.get(IRelationshipTranslationModel.SIDE_2_LABEL);
      if (side2Label != null) {
        updateSideLabel(node, side2Label, lang, IRelationship.SIDE2);
      }
    }
  }
}
