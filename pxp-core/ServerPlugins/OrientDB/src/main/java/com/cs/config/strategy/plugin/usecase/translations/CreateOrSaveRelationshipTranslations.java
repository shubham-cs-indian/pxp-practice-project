package com.cs.config.strategy.plugin.usecase.translations;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.translations.abstrct.AbstractSaveRelationshipTranslations;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;
import com.cs.core.config.interactor.entity.propertycollection.ISectionRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.model.translations.IRelationshipTranslationModel;
import com.cs.core.runtime.interactor.constants.application.Seperators;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.Map;

@SuppressWarnings("unchecked")
public class CreateOrSaveRelationshipTranslations extends AbstractSaveRelationshipTranslations {
  
  public CreateOrSaveRelationshipTranslations(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateOrSaveRelationshipTranslations/*" };
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
      
      String key = IRelationship.LABEL + Seperators.FIELD_LANG_SEPERATOR + lang;
      if (node.getProperty(key) != null) {
        continue;
      }
      
      if (label != null) {
        node.setProperty(key, label);
        if (lang.equals(defaultLanguage)) {
          node.setProperty("defaultLabel", label);
        }
        try {
          Vertex icon = UtilClass.getVertexByCode(node.getProperty(IConfigEntity.CODE), VertexLabelConstants.ENTITY_TYPE_ICON);
          icon.setProperty(key, label);
          if (lang.equals(defaultLanguage)) {
            icon.setProperty("defaultLabel", label);
          }
        }
        catch (Exception e) {
        }
      }
      
      String side1Label = (String) translationMap.get(IRelationshipTranslationModel.SIDE_1_LABEL);
      String side2Label = (String) translationMap.get(IRelationshipTranslationModel.SIDE_2_LABEL);
      
      if (side1Label != null) {
        updateSideLabel(node, side1Label, lang, IRelationship.SIDE1, defaultLanguage);
      }
      if (side2Label != null) {
        updateSideLabel(node, side2Label, lang, IRelationship.SIDE2, defaultLanguage);
      }
    }
  }
  
  /**
   * @param relationshipNode
   * @param sideLabel
   * @param lang
   * @param side
   * @param defaultLanguage
   * @throws Exception
   */
  protected void updateSideLabel(Vertex relationshipNode, String sideLabel, String lang,
      String side, String defaultLanguage) throws Exception
  {
    String property = IRelationship.LABEL + Seperators.FIELD_LANG_SEPERATOR + lang;
    
    Map<String, Object> sideMap = relationshipNode.getProperty(side);
    sideMap.put(property, sideLabel);
    if (lang.equals(defaultLanguage)) {
      sideMap.put("defaultLabel", sideLabel);
    }
    relationshipNode.setProperty(side, sideMap);
    
    String klassId = (String) sideMap.get(IRelationshipSide.KLASS_ID);
    Vertex klassNode = UtilClass.getVertexById(klassId,
        VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    
    Vertex klassPropertyNode = KlassUtils.getRespectiveKlassPropertyNode(klassNode,
        relationshipNode);
    
    Map<String, Object> relationshipSideMap = klassPropertyNode
        .getProperty(ISectionRelationship.RELATIONSHIP_SIDE);
    relationshipSideMap.put(property, sideLabel);
    if (lang.equals(defaultLanguage)) {
      relationshipSideMap.put("defaultLabel", sideLabel);
    }
    klassPropertyNode.setProperty(ISectionRelationship.RELATIONSHIP_SIDE, relationshipSideMap);
  }
}
