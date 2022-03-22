package com.cs.config.strategy.plugin.usecase.translations.abstrct;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.translations.utils.TranslationsUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.propertycollection.ISectionRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.model.translations.ISaveRelationshipTranslationModel;
import com.cs.core.config.interactor.model.translations.ISaveRelationshipTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.ISaveRelationshipTranslationsResponseModel;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsRequestModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.runtime.interactor.constants.application.Seperators;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public abstract class AbstractSaveRelationshipTranslations extends AbstractOrientPlugin {
  
  protected abstract void saveTranslationsInNode(Vertex node, Map<String, Object> translations, String defaultLanguage)
      throws Exception;
  
  public AbstractSaveRelationshipTranslations(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    // Default Label handling
    String defaultLanguage = TranslationsUtils.getDefaultLanguage();
    String entityType = (String) requestMap.get(ISaveTranslationsRequestModel.ENTITY_TYPE);
    String vertexLabel = EntityUtil.getVertexLabelByEntityType(entityType);
    
    List<String> languages = (List<String>) requestMap
        .get(ISaveRelationshipTranslationsRequestModel.LANGAUGES);
    
    List<Map<String, Object>> list = (List<Map<String, Object>>) requestMap
        .get(ISaveRelationshipTranslationsRequestModel.DATA);
    IExceptionModel failure = new ExceptionModel();
    List<Map<String, Object>> returnList = new ArrayList<>();
    for (Map<String, Object> mapToSave : list) {
      // save
      String id = (String) mapToSave.get(ISaveRelationshipTranslationModel.ID);
      try {
        Vertex node = UtilClass.getVertexById(id, vertexLabel);
        Map<String, Object> translations = (Map<String, Object>) mapToSave
            .get(ISaveRelationshipTranslationModel.TRANSLATIONS);
        saveTranslationsInNode(node, translations, defaultLanguage);
        
        // get for response
        Map<String, Object> map = TranslationsUtils.prepareRelationshipTranslationsMap(languages,
            node);
        returnList.add(map);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, id, null);
      }
    }
    UtilClass.getGraph()
        .commit();
    
    Map<String, Object> responseMap = new HashMap<String, Object>();
    responseMap.put(ISaveRelationshipTranslationsResponseModel.SUCCESS, returnList);
    responseMap.put(ISaveRelationshipTranslationsResponseModel.FAILURE, failure);
    return responseMap;
  }
  
  protected void updateSideLabel(Vertex relationshipNode, String sideLabel, String lang,
      String side) throws Exception
  {
    // Default Label handling
    String property = null;
    if (lang.equals(CommonConstants.LANGUAGE_CODE_FOR_DEFAULT_LABEL_PROPERTY)) {
      property = CommonConstants.DEFAULT_LABEL;
    }
    else {
      property = IRelationship.LABEL + Seperators.FIELD_LANG_SEPERATOR + lang;
    }
    
    Map<String, Object> sideMap = relationshipNode.getProperty(side);
    sideMap.put(property, sideLabel);
    relationshipNode.setProperty(side, sideMap);
    
    String klassId = (String) sideMap.get(IRelationshipSide.KLASS_ID);
    Vertex klassNode = UtilClass.getVertexById(klassId,
        VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    
    Vertex klassPropertyNode = KlassUtils.getRespectiveKlassPropertyNode(klassNode,
        relationshipNode);
    
    Map<String, Object> relationshipSideMap = klassPropertyNode
        .getProperty(ISectionRelationship.RELATIONSHIP_SIDE);
    relationshipSideMap.put(property,
        sideLabel);
    klassPropertyNode.setProperty(ISectionRelationship.RELATIONSHIP_SIDE, relationshipSideMap);
  }
}
