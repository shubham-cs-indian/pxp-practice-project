package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedAttribute;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedOperator;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedTagOperator;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ILanguage;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.config.interactor.model.concatenated.IGetConcatenatedAttributesAndKlassesModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.Seperators;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.repository.language.LanguageRepository;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Get_Concatenated_Attributes_And_Klasses extends AbstractOrientPlugin {
  
  public Get_Concatenated_Attributes_And_Klasses(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Get_Concatenated_Attributes_And_Klasses/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> responseMap = new HashMap<String, Object>();
    
    String query = "select * from " + VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE + " where "
        + CommonConstants.TYPE_PROPERTY + " in \"" + CommonConstants.CONCATENATED_ATTRIBUTE_TYPE
        + "\"";
    Iterable<Vertex> concatenatedAttributeVertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Map<String, Object> concatenatedAttributeMap = new HashMap<String, Object>();
    Map<String, Object> klassesAndTaxonomiesHavingConcatenatedAttributePropertiesMap = new HashMap<String, Object>();
    
    Map<String, Object> referencedTags = new HashMap<String, Object>();
    
    for (Vertex concatenatedAttribute : concatenatedAttributeVertices) {
      String attributeId = UtilClass.getCodeNew(concatenatedAttribute);
      Map<String, Object> attributeMap = AttributeUtils
          .getConcatenatedAttribute(concatenatedAttribute);
      fillReferencedTags(referencedTags, attributeMap);
      concatenatedAttributeMap.put(attributeId,
          attributeMap.get(IConcatenatedAttribute.ATTRIBUTE_CONCATENATED_LIST));
      
      String getKlassesAndTaxonomiesQuery = "select " + CommonConstants.CODE_PROPERTY
          + " from (select expand(in('" + RelationshipLabelConstants.HAS_KLASS_PROPERTY
          + "')) from (select expand(in('" + RelationshipLabelConstants.HAS_PROPERTY + "')) from "
          + VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE + " where "
          + CommonConstants.CODE_PROPERTY + " in \"" + attributeId + "\"))";
      
      Iterable<Vertex> klassAndTaxonomiesVertices = UtilClass.getGraph()
          .command(new OCommandSQL(getKlassesAndTaxonomiesQuery))
          .execute();
      
      for (Vertex klassVertex : klassAndTaxonomiesVertices) {
        String klassId = UtilClass.getCodeNew(klassVertex);
        
        if (klassesAndTaxonomiesHavingConcatenatedAttributePropertiesMap.get(klassId) == null) {
          klassesAndTaxonomiesHavingConcatenatedAttributePropertiesMap.put(klassId,
              new ArrayList<String>());
        }
        List<String> attributeIdsList = (List<String>) klassesAndTaxonomiesHavingConcatenatedAttributePropertiesMap
            .get(klassId);
        attributeIdsList.add(attributeId);
      }
    }
    
    responseMap.put(IGetConcatenatedAttributesAndKlassesModel.CONCATENATED_ATTRIBUTES,
        concatenatedAttributeMap);
    responseMap.put(
        IGetConcatenatedAttributesAndKlassesModel.KLASSES_AND_TAXONOMIES_HAVING_CONCATENATED_ATTRIBUTES,
        klassesAndTaxonomiesHavingConcatenatedAttributePropertiesMap);
    responseMap.put(IGetConcatenatedAttributesAndKlassesModel.REFERENCED_TAGS, referencedTags);
    
    return responseMap;
  }
  
  @SuppressWarnings("unchecked")
  private void fillReferencedTags(Map<String, Object> referencedTags,
      Map<String, Object> attributeMap) throws Exception
  {
    List<Map<String, Object>> attributeOperatorList = (List<Map<String, Object>>) attributeMap
        .get(IConcatenatedAttribute.ATTRIBUTE_CONCATENATED_LIST);
    List<String> languagesToFetch = new ArrayList<>(
        Arrays.asList(ILanguage.IS_USER_INTERFACE_LANGUAGE, ILanguage.IS_DATA_LANGUAGE));
    List<String> languages = getLanguages(languagesToFetch);
    
    for (Map<String, Object> operator : attributeOperatorList) {
      String type = (String) operator.get(IConcatenatedOperator.TYPE);
      if (type.equals(CommonConstants.TAG)) {
        String operatorTagId = (String) operator.get(IConcatenatedTagOperator.TAG_ID);
        if (operatorTagId != null && !referencedTags.containsKey(operatorTagId)) {
          Vertex operatorTagNode;
          try {
            operatorTagNode = UtilClass.getVertexById(operatorTagId,
                VertexLabelConstants.ENTITY_TAG);
            
          }
          catch (NotFoundException e) {
            throw new TagNotFoundException();
          }
          Map<String, Object> tagMap = getTagMap(operatorTagNode, languages);
          referencedTags.put(operatorTagId, tagMap);
        }
      }
    }
  }
  
  private Map<String, Object> getTagMap(Vertex tagNode, List<String> uiLanguages)
  {
    final List<String> fieldsToFetch = Arrays.asList(ITag.IS_FOR_RELEVANCE,
        ITag.TAG_VALUES_SEQUENCE, ITag.LABEL, ITag.ICON, ITag.TYPE,
        CommonConstants.CODE_PROPERTY, ITag.CODE);
    OrientGraph graph = UtilClass.getGraph();
    HashMap<String, Object> tagMap = new HashMap<>();
    tagMap.putAll(UtilClass.getMapFromVertex(fieldsToFetch, tagNode));
    
    if (tagMap.get(ITag.TYPE) == null) {
      tagMap.put(ITag.TYPE, CommonConstants.TAG_TYPE);
    }
    
    List<Map<String, Object>> children = new ArrayList<>();
    tagMap.put(CommonConstants.CHILDREN_PROPERTY, children);
    
    String rid = tagNode.getId()
        .toString();
    
    Iterable<Vertex> resultIterable = graph
        .command(new OCommandSQL("select expand(in ('Child_Of')) from " + rid))
        .execute();
    
    for (Vertex childTagNode : resultIterable) {
      HashMap<String, Object> childTagMap = new HashMap<>();
      childTagMap.put(ITag.ID, childTagNode.getProperty(CommonConstants.CODE_PROPERTY));
      for (String language : uiLanguages) {
        String languageConvertedLabelField = ITag.LABEL + Seperators.FIELD_LANG_SEPERATOR
            + language;
        String languageConvertedLabel = childTagNode.getProperty(languageConvertedLabelField);
        if (languageConvertedLabel != null) {
          childTagMap.put(languageConvertedLabelField, languageConvertedLabel);
        }
      }
      children.add(childTagMap);
    }
    
    return tagMap;
  }
  
  private List<String> getLanguages(List<String> languagesToFetch) throws Exception
  {
    Map<String, Object> languageMap = new HashMap<>();
    Set<String> languageCodes = new HashSet<>();
    final List<String> fieldsToFetch = Arrays.asList(ILanguage.CODE);
    
    for (String languageToFetch : languagesToFetch) {
      Iterable<Vertex> languageVertices = LanguageRepository.getDataOrUILanguages(languageToFetch);
      for (Vertex language : languageVertices) {
        languageMap = UtilClass.getMapFromVertex(fieldsToFetch, language, "");
        languageCodes.add((String) languageMap.get(ILanguage.CODE));
      }
    }
    return new ArrayList<>(languageCodes);
  }
}
