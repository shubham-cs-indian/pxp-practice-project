package com.cs.config.strategy.plugin.usecase.endpoint;

import com.cs.config.strategy.plugin.usecase.endpoint.util.EndpointUtils;
import com.cs.config.strategy.plugin.usecase.mapping.util.MappingUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attributiontaxonomy.IMasterTaxonomy;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.exception.profile.ProfileNotFoundException;
import com.cs.core.config.interactor.model.configdetails.IConfigModelForBoarding;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.config.interactor.model.endpoint.IGetEndpointForGridModel;
import com.cs.core.config.interactor.model.mapping.IColumnValueTagValueMappingModel;
import com.cs.core.config.interactor.model.mapping.IConfigRuleAttributeMappingModel;
import com.cs.core.config.interactor.model.mapping.IConfigRuleTagMappingModel;
import com.cs.core.config.interactor.model.mapping.IGetMappingForImportRequestModel;
import com.cs.core.config.interactor.model.mapping.IMappingModel;
import com.cs.core.config.interactor.model.mapping.IRuntimeMappingModel;
import com.cs.core.runtime.interactor.constants.application.OnboardingConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({ "unchecked" })
public class GetAllMappingByEndpointId extends AbstractOrientPlugin {
  
  public GetAllMappingByEndpointId(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    /* HashMap<String, Object> endpointMap = new HashMap<String, Object>();
    String endpointId = (String) map.get(IGetMappingForImportRequestModel.MAPPING_ID);
    List<String> fileHeaders = (List<String>) map.get("fileHeaders");
    Map<String, Object> klassesTaxonomiesFromFile = (Map<String, Object>) map
        .get(IGetMappingForImportRequestModel.KLASSES_TAXONOMIES_FROM_FILE);
    
    Vertex endpoint = UtilClass.getVertexById(endpointId, VertexLabelConstants.ENDPOINT);
    if (endpoint == null) {
      throw new ProfileNotFoundException();
    }
    
    List<String> mappedColumns = new ArrayList<>();
    List<String> finalFileHeaderList = new ArrayList<>(fileHeaders);
    for (String header : fileHeaders) {
      finalFileHeader.add("\"" + header + "\"");
    }   
    List<String> klassesLabelListFromFile = (List<String>) klassesTaxonomiesFromFile
        .get(OnboardingConstants.KLASSES_FROM_FILE);
    List<String> taxonomiesLabelListFromFile = (List<String>) klassesTaxonomiesFromFile
        .get(OnboardingConstants.TAXONOMIES_FORM_FILE);
    HashMap<String, Object> getEndPointMap = new HashMap<>();
    Map<String, Object> profileMap = UtilClass.getMapFromVertex(new ArrayList<>(), endpoint);
    getEndPointMap.put(IGetEndpointForGridModel.ENDPOINT, profileMap);
    EndpointUtils.getMapFromProfileNode(endpoint, getEndPointMap, mappedColumns);
    endpointMap.putAll((Map<String, Object>) getEndPointMap.get(IGetEndpointForGridModel.ENDPOINT));
    fileHeaders.removeAll(mappedColumns);*/
    
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    /* Map<String, Object> attributeMapForIdAndLabel = new HashMap<>();
    Map<String, Object> tagMapForIdAndLabel = new HashMap<>();
    Map<String, Object> taxonomyMapForIdAndLabel = new HashMap<>();
    Map<String, Object> klassMapForIdAndLabel = new HashMap<>();
    
    Map<String, Object> configMap = new HashMap<>();
    configMap.put(IConfigModelForBoarding.ATTRIBUTES, attributeMapForIdAndLabel);
    configMap.put(IConfigModelForBoarding.TAGS, tagMapForIdAndLabel);
    configMap.put(IConfigModelForBoarding.TAXONOMY, taxonomyMapForIdAndLabel);
    configMap.put(IConfigModelForBoarding.KLASSES, klassMapForIdAndLabel);
    
    returnMap.put(IMappingModel.CONFIG_DETAILS, configMap);
    List<String> mappings = (List<String>) endpointMap.get(IEndpointModel.MAPPINGS);
    
    List<Map<String, Object>> attributesFinalMappings = new ArrayList<>();
    List<Map<String, Object>> tagFinalMappings = new ArrayList<>();
    List<Map<String, Object>> klassFinalMappings = new ArrayList<>();
    List<Map<String, Object>> taxonomyFinalMappings = new ArrayList<>();
    
    if (!mappings.isEmpty()) {
      MappingUtils.getMappings(returnMap, mappings.get(0), attributeMapForIdAndLabel, tagMapForIdAndLabel, taxonomyMapForIdAndLabel,
          klassMapForIdAndLabel);
      
      attributesFinalMappings = (List<Map<String, Object>>) returnMap.get(IMappingModel.ATTRIBUTE_MAPPINGS);
      tagFinalMappings = (List<Map<String, Object>>) returnMap.get(IMappingModel.TAG_MAPPINGS);
      klassFinalMappings = (List<Map<String, Object>>) returnMap.get(IMappingModel.CLASS_MAPPINGS);
      taxonomyFinalMappings = (List<Map<String, Object>>) returnMap.get(IMappingModel.TAXONOMY_MAPPINGS);
      
      removePropertiesNotInFile(finalFileHeaderList, attributesFinalMappings);
      removePropertiesNotInFile(finalFileHeaderList, tagFinalMappings);
      removePropertiesNotInFile(klassesLabelListFromFile, klassFinalMappings);
      removePropertiesNotInFile(taxonomiesLabelListFromFile, taxonomyFinalMappings);
    }
    else {
      returnMap.put(IMappingModel.ATTRIBUTE_MAPPINGS, attributesFinalMappings);
      returnMap.put(IMappingModel.TAG_MAPPINGS, tagFinalMappings);
      returnMap.put(IMappingModel.CLASS_MAPPINGS, klassFinalMappings);
      returnMap.put(IMappingModel.TAXONOMY_MAPPINGS, taxonomyFinalMappings);
    }
    List<String> mappedTagId = new ArrayList<>();
    for (Map<String, Object> tagMapping : tagFinalMappings) {
      mappedTagId.add("'" + (String) tagMapping.get(IConfigRuleAttributeMappingModel.MAPPED_ELEMENT_ID) + "'");
    }
    
    getSubTagsForMappedTags(tagFinalMappings, mappedTagId);
    removeMappedColumnsFromFinalList(finalFileHeaderList, attributesFinalMappings);
    removeMappedColumnsFromFinalList(finalFileHeaderList, tagFinalMappings);
    removeMappedColumnsFromFinalList(klassesLabelListFromFile, klassFinalMappings);
    removeMappedColumnsFromFinalList(taxonomiesLabelListFromFile, taxonomyFinalMappings);
    autoMapProperties(finalFileHeaderList, attributesFinalMappings, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE, attributeMapForIdAndLabel);
    autoMapProperties(finalFileHeaderList, tagFinalMappings, VertexLabelConstants.ENTITY_TAG, new HashMap<>());
    autoMapProperties(klassesLabelListFromFile, klassFinalMappings, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS, klassMapForIdAndLabel);
    autoMapProperties(taxonomiesLabelListFromFile, taxonomyFinalMappings, VertexLabelConstants.ROOT_KLASS_TAXONOMY,
        taxonomyMapForIdAndLabel);*/
    
    /*List<Map<String, Object>> classFinalMappings = (List<Map<String, Object>>) mappingvertexMap
        .get(IMappingModel.CLASS_MAPPINGS);
    
    List<Map<String, Object>> taxonomyFinalMappings = (List<Map<String, Object>>) mappingvertexMap
        .get(IMappingModel.TAXONOMY_MAPPINGS);*/
    
    // TODO - Class and Taxonomy mappings
    //returnMap.put(IRuntimeMappingModel.IS_RUNTIME_MAPPING_ENABLED, endpointMap.get(IEndpoint.IS_RUNTIME_MAPPING_ENABLED));
    return returnMap;
  }
  
  private void removeMappedColumnsFromFinalList(List<String> finalFileHeader, List<Map<String, Object>> propertyMappings)
  {
    for (Map<String, Object> propertyMapping : propertyMappings) {
      finalFileHeader.removeAll((List<String>) propertyMapping.get(IConfigRuleAttributeMappingModel.COLUMN_NAMES));
    }
  }
  
  private void getSubTagsForMappedTags(List<Map<String, Object>> tagFinalMappings, List<String> mappedTagId)
  {
    Iterable<Vertex> resultIterableForMappedTags = UtilClass.getGraph()
        .command(new OCommandSQL(
            "select from " + VertexLabelConstants.ENTITY_TAG + " where " + CommonConstants.CODE_PROPERTY + " in " + mappedTagId.toString()))
        .execute();
    for (Vertex vertex : resultIterableForMappedTags) {
      String key = vertex.getProperty(CommonConstants.CODE_PROPERTY);
      List<String> mappedTagValues = vertex.getProperty(ITag.TAG_VALUES_SEQUENCE);
      for (Map<String, Object> tagMapping : tagFinalMappings) {
        if (key.equals(tagMapping.get(IConfigRuleAttributeMappingModel.MAPPED_ELEMENT_ID))) {
          List<Map<String, Object>> tagVlauemappingList = (List<Map<String, Object>>) tagMapping
              .get(IConfigRuleTagMappingModel.TAG_VALUE_MAPPINGS);
          if (!tagVlauemappingList.isEmpty()) {
            Map<String, Object> tagValueMapping = ((List<Map<String, Object>>) tagMapping
                .get(IConfigRuleTagMappingModel.TAG_VALUE_MAPPINGS)).get(0);
            tagValueMapping.put(IColumnValueTagValueMappingModel.TAG_VALUE_IDS, mappedTagValues);
          }
          else {
            Map<String, Object> tagValueMappings = new HashMap<>();
            tagValueMappings.put(IColumnValueTagValueMappingModel.COLUMN_NAME,
                ((List<String>) tagMapping.get(IConfigRuleTagMappingModel.COLUMN_NAMES)).get(0));
            tagValueMappings.put(IColumnValueTagValueMappingModel.MAPPINGS, new ArrayList<>());
            tagValueMappings.put(IColumnValueTagValueMappingModel.TAG_VALUE_IDS, mappedTagValues);
            tagVlauemappingList.add(tagValueMappings);
          }
        }
      }
    }
  }
  
  protected void autoMapProperties(List<String> finalFileHeader, List<Map<String, Object>> entitiesFinalMappings, String entity,
      Map<String, Object> mapForConfigDetails)
  {
    String finalFileHeaderString = EntityUtil.quoteIt(finalFileHeader);
    OrientGraph graph = UtilClass.getGraph();
    autoMapProperty(finalFileHeader, entitiesFinalMappings, finalFileHeaderString, graph, CommonConstants.CODE_PROPERTY, entity,
        mapForConfigDetails);
    finalFileHeaderString = EntityUtil.quoteIt(finalFileHeader);
    autoMapProperty(finalFileHeader, entitiesFinalMappings, finalFileHeaderString, graph, CommonConstants.CODE_PROPERTY, entity,
        mapForConfigDetails);
    finalFileHeaderString = EntityUtil.quoteIt(finalFileHeader);
    autoMapProperty(finalFileHeader, entitiesFinalMappings, finalFileHeaderString, graph,
        EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY), entity, mapForConfigDetails);
    finalFileHeaderString = EntityUtil.quoteIt(finalFileHeader);
  }
  
  protected void autoMapProperty(List<String> finalFileHeader, List<Map<String, Object>> entityFinalMappings, String finalFileHeaderString,
      OrientGraph graph, String property, String entity, Map<String, Object> mapForConfigDetails)
  {
    String query = "select from " + entity + " where " + property + " in " + finalFileHeaderString;
    if (entity.equals(VertexLabelConstants.ENTITY_TAG)) {
      query += " and " + ITag.TAG_TYPE + " is not null";
    }
    if (entity.equals(VertexLabelConstants.ROOT_KLASS_TAXONOMY)) {
      query += " and (" + IMasterTaxonomy.IS_TAXONOMY + " is null or " + IMasterTaxonomy.IS_TAXONOMY + "= true)";
    }
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.PROPERTY_MAPPING, CommonConstants.CODE_PROPERTY);
    Iterable<Vertex> resultIterableForEntity = graph.command(new OCommandSQL(query)).execute();
    Map<String, Object> propertyMapping = new HashMap<>();
    List<String> duplicateList = new ArrayList<>();
    for (Vertex vertex : resultIterableForEntity) {
      Map<String, Object> entityMapping = new HashMap<>();
      
      String key = vertex.getProperty(property);
      String idProperty = (String) vertex.getProperty(CommonConstants.CODE_PROPERTY);
      Long versionId = vertex.getProperty(CommonConstants.VERSION_ID);
      
      entityMapping.put(IConfigRuleAttributeMappingModel.MAPPED_ELEMENT_ID, idProperty);
      entityMapping.put(CommonConstants.VERSION_ID, versionId);
      entityMapping.put(IConfigRuleAttributeMappingModel.COLUMN_NAMES, Arrays.asList(key));
      entityMapping.put(CommonConstants.IS_IGNORED_PROPERTY, false);
      
      entityMapping.put(CommonConstants.CODE_PROPERTY, UtilClass.getUniqueSequenceId(vertexType));
      
      if (entity.equals(VertexLabelConstants.ENTITY_TAG)) {
        Map<String, Object> tagValueMappings = new HashMap<>();
        tagValueMappings.put(IColumnValueTagValueMappingModel.COLUMN_NAME, key);
        tagValueMappings.put(IColumnValueTagValueMappingModel.MAPPINGS, new ArrayList<>());
        
        List<String> unmappedTagValues = vertex.getProperty(ITag.TAG_VALUES_SEQUENCE);
        tagValueMappings.put(IColumnValueTagValueMappingModel.TAG_VALUE_IDS, unmappedTagValues);
        
        entityMapping.put(IConfigRuleTagMappingModel.TAG_VALUE_MAPPINGS, Arrays.asList(tagValueMappings));
      }
      else {
        Map<String, Object> entityDetails = new HashMap<>();
        entityDetails.put(IConfigEntityInformationModel.ID, idProperty);
        entityDetails.put(IConfigEntityInformationModel.LABEL,
            (String) UtilClass.getValueByLanguage(vertex, CommonConstants.LABEL_PROPERTY));
        entityDetails.put(IConfigEntityInformationModel.CODE, vertex.getProperty(CommonConstants.CODE_PROPERTY));
        mapForConfigDetails.put(idProperty, entityDetails);
      }
      if (propertyMapping.get(key) == null) {
        propertyMapping.put(key, entityMapping);
      }
      else {
        duplicateList.add(key);
      }
    }
    for (String propertyValue : propertyMapping.keySet()) {
      if (!duplicateList.contains(propertyValue)) {
        entityFinalMappings.add((Map<String, Object>) propertyMapping.get(propertyValue));
        finalFileHeader.remove(propertyValue);
      }
    }
  }
  
  private void removePropertiesNotInFile(List<String> finalFileHeader, List<Map<String, Object>> propertyMappings)
  {
    List<Map<String, Object>> nonFileHeaderList = new ArrayList<>();
    for (Map<String, Object> propertyMapping : propertyMappings) {
      List<String> columnNames = (List<String>) propertyMapping.get("columnNames");
      if (!finalFileHeader.containsAll(columnNames)) {
        nonFileHeaderList.add(propertyMapping);
      }
    }
    
    if (nonFileHeaderList.size() != 0) {
      propertyMappings.removeAll(nonFileHeaderList);
    }
  }
  
  public String[] getNames()
  {
    return new String[] { "POST|GetAllMappingByEndpointId/*" };
  }
}
