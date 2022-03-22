package com.cs.config.strategy.plugin.datadump;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.attribute.IUnitAttribute;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

public class GetConfigDetailsForDataDump extends AbstractOrientPlugin {
  
  private static final List<String> FIELDS_TO_FETCH_FOR_ATTRIBUTE  = Arrays.asList(CommonConstants.CODE_PROPERTY, IAttribute.PROPERTY_IID,
      IAttribute.IS_TRANSLATABLE, IAttribute.RENDERER_TYPE, IUnitAttribute.DEFAULT_UNIT, IAttribute.TYPE);
  
  private static final List<String> FIELDS_TO_FETCH_FOR_TAG_OR_REL = Arrays.asList(CommonConstants.CODE_PROPERTY, IAttribute.PROPERTY_IID);
  
  private static final List<String> FIELDS_TO_FETCH_FOR_KLASS      = Arrays.asList(CommonConstants.CODE_PROPERTY, IKlass.CLASSIFIER_IID,
      IKlass.IS_NATURE, IKlass.NATURE_TYPE, IKlass.CONTEXT_ID);
  
  private static final List<String> FIELDS_TO_FETCH_FOR_TAXONOMY   = Arrays.asList(CommonConstants.CODE_PROPERTY, ITaxonomy.CLASSIFIER_IID);
  
  public GetConfigDetailsForDataDump(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForDataDump/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> mapToReturn = new HashMap<String, Object>();
    fetchAttributes(mapToReturn);
    fetchTags(mapToReturn);
    fetchKlasses(mapToReturn, Arrays.asList(VertexLabelConstants.ENTITY_TYPE_KLASS, VertexLabelConstants.ENTITY_TYPE_ASSET));
    fetchTaxonomies(mapToReturn);
    fetchRelationhips(mapToReturn);
    return mapToReturn;
  }
  
  /**
   * @param mapToReturn
   */
  private void fetchRelationhips(Map<String, Object> mapToReturn)
  {
    List<Object> relationshipList = new ArrayList<Object>();
    String relationshipsQuery = "select  from " + VertexLabelConstants.ROOT_RELATIONSHIP;
    Iterable<Vertex> relationships = UtilClass.getVerticesFromQuery(relationshipsQuery);
    
    for (Vertex relationship : relationships) {
      Map<String, Object> relationshipMap = UtilClass.getMapFromVertex(FIELDS_TO_FETCH_FOR_TAG_OR_REL, relationship);
      relationshipMap.remove("code");
      relationshipList.add(relationshipMap);
    }
    mapToReturn.put("relationship", relationshipList);
  }

  /**
   * @param mapToReturn
   */
  private void fetchTaxonomies(Map<String, Object> mapToReturn)
  {
    List<Object> taxonomyList = new ArrayList<Object>();
    String taxonomiesQuery = "select  from " + VertexLabelConstants.ATTRIBUTION_TAXONOMY
        + " where isTaxonomy = true";
    Iterable<Vertex> taxonomies = UtilClass.getVerticesFromQuery(taxonomiesQuery);
    
    for (Vertex taxonomy : taxonomies) {
      Map<String, Object> taxonomyMap = UtilClass.getMapFromVertex(FIELDS_TO_FETCH_FOR_TAXONOMY, taxonomy);
      taxonomyMap.remove("code");
      taxonomyList.add(taxonomyMap);
    }
    mapToReturn.put("taxonomy", taxonomyList);
  }
  
  /**
   * @param mapToReturn
   * @param klassList 
   */
  private void fetchKlasses(Map<String, Object> mapToReturn, List<String> klassTList)
  {
    List<Object> klassList = new ArrayList<Object>();
    Map<String, Object> contextMap = new HashMap<String, Object>();
    for (String vertexLabel : klassTList) {
      String klassesQuery = "select from " + vertexLabel;
      Iterable<Vertex> klasses = UtilClass.getVerticesFromQuery(klassesQuery);
      
      for (Vertex klass : klasses) {
        Map<String, Object> klassMap = UtilClass.getMapFromVertex(FIELDS_TO_FETCH_FOR_KLASS, klass);
        if ("embedded".equals((String) klassMap.get(IKlass.NATURE_TYPE))) {
          Iterable<Vertex> variants = klass.getVertices(Direction.OUT, RelationshipLabelConstants.VARIANT_CONTEXT_OF);
          for (Vertex variant : variants) {
            contextMap.put((String) klassMap.get("code"), (String) variant.getProperty("code"));
          }
        }
        klassMap.remove(IKlass.NATURE_TYPE);
        klassMap.remove(IKlass.CONTEXT_ID);
        klassMap.remove("code");
        klassList.add(klassMap);
      }
    }
    mapToReturn.put("klass", klassList);
    mapToReturn.put("context", contextMap);
  }
  
  /**
   * @param mapToReturn
   */
  private void fetchTags(Map<String, Object> mapToReturn)
  {
    List<Object> tagList = new ArrayList<Object>();
    String tagsQuery = "select from " + VertexLabelConstants.ENTITY_TAG + " where propertyIID > 0 and tagType in "
        + EntityUtil.quoteIt(Arrays.asList(CommonConstants.YES_NEUTRAL_TAG_TYPE_ID, CommonConstants.BOOLEAN_TAG_TYPE_ID,
            CommonConstants.LISTING_TYPE_ID, CommonConstants.STATUS_TAG_TYPE_ID, CommonConstants.STATUS_TYPE_ID));
    Iterable<Vertex> tags = UtilClass.getVerticesFromQuery(tagsQuery);
    
    for (Vertex tag : tags) {
      Map<String, Object> tagMap = UtilClass.getMapFromVertex(FIELDS_TO_FETCH_FOR_TAG_OR_REL, tag);
      tagMap.remove("code");
      tagList.add(tagMap);
    }
    mapToReturn.put("tag", tagList);
  }
  
  /**
   * @param mapToReturn
   */
  private void fetchAttributes(Map<String, Object> mapToReturn)
  {
    List<Object> attrList = new ArrayList<Object>();
    List<String> dateAttrs = new ArrayList<String>();
    List<String> htmlAttrs = new ArrayList<String>();
    List<String> numberAttrs = new ArrayList<String>();
    List<String> measurementAttrs = new ArrayList<String>();
    List<String> langtAttrs = new ArrayList<String>();
    List<String> measurementTypes = com.cs.core.asset.services.CommonConstants.UNIT_ATTRIBUTE_TYPES;
    String attributesQuery = "select from " + VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE + " where propertyIID > 0";
    Iterable<Vertex> attributes = UtilClass.getVerticesFromQuery(attributesQuery);
    
    for (Vertex attribute : attributes) {
      Map<String, Object> attributeMap = UtilClass.getMapFromVertex(FIELDS_TO_FETCH_FOR_ATTRIBUTE, attribute);
      String code = (String) attributeMap.get("code");
      switch ((String) attributeMap.get(IAttribute.RENDERER_TYPE)) {
        case "HTML":
          htmlAttrs.add(code);
          break;
        case "DATE":
          dateAttrs.add(code);
          break;
        case "NUMBER":
          numberAttrs.add(code);
          break;
        default:
          if (measurementTypes.contains((String) attributeMap.get(IAttribute.TYPE))) {
            measurementAttrs.add(code);
          }
          break;
      }
      attributeMap.remove(IAttribute.TYPE);
      attributeMap.remove(IAttribute.RENDERER_TYPE);
      attributeMap.remove("code");
      Boolean isLang = (Boolean) attributeMap.get(IAttribute.IS_TRANSLATABLE);
      if (isLang != null && isLang) {
        langtAttrs.add(code);
      }
      attrList.add(attributeMap);
    }
    mapToReturn.put("attr", attrList);
    mapToReturn.put("dateAttrs", dateAttrs);
    mapToReturn.put("htmlAttrs", htmlAttrs);
    mapToReturn.put("numberAttrs", numberAttrs);
    mapToReturn.put("measurementAttrs", measurementAttrs);
    mapToReturn.put("langAttrs", langtAttrs);
  }
}
