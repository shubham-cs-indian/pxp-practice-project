package com.cs.config.strategy.plugin.usecase.mapping;

import com.cs.config.strategy.plugin.model.mapping.IMappingHelperModel;
import com.cs.config.strategy.plugin.model.mapping.MappingHelperModel;
import com.cs.config.strategy.plugin.usecase.mapping.util.MappingUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.mapping.IMappingModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
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
public class CreateMapping extends AbstractOrientPlugin {
  
  List<String> fieldsToExclude = Arrays.asList(IMappingModel.ATTRIBUTE_MAPPINGS,
      IMappingModel.TAG_MAPPINGS, IMappingModel.CLASS_MAPPINGS, IMappingModel.TAXONOMY_MAPPINGS, IMappingModel.RELATIONSHIP_MAPPINGS);
  
  public CreateMapping(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    HashMap<String, Object> profileMap = new HashMap<String, Object>();
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    profileMap = (HashMap<String, Object>) map.get(CommonConstants.MAPPING);
    UtilClass.checkDuplicateName((String) profileMap.get(CommonConstants.LABEL_PROPERTY), VertexLabelConstants.PROPERTY_MAPPING);  
    List<Map<String, Object>> addedAttributesMapping = (List<Map<String, Object>>) profileMap
        .get(IMappingModel.ATTRIBUTE_MAPPINGS);
    List<Map<String, Object>> addedTagsMapping = (List<Map<String, Object>>) profileMap
        .get(IMappingModel.TAG_MAPPINGS);
    List<Map<String, Object>> addedClassesMapping = (List<Map<String, Object>>) profileMap
        .get(IMappingModel.CLASS_MAPPINGS);
    List<Map<String, Object>> addedTaxonomiesMapping = (List<Map<String, Object>>) profileMap
        .get(IMappingModel.TAXONOMY_MAPPINGS);
    
    OrientGraph graph = UtilClass.getGraph();
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.PROPERTY_MAPPING, CommonConstants.CODE_PROPERTY);
    Vertex profileNode = UtilClass.createNode(profileMap, vertexType, fieldsToExclude);
    
    saveMapping(addedAttributesMapping, addedTagsMapping, addedClassesMapping,
        addedTaxonomiesMapping, profileNode);
    IMappingHelperModel mappingHelperModel = new MappingHelperModel();
    returnMap.put(IMappingModel.CONFIG_DETAILS, mappingHelperModel.getConfigDetails());
    returnMap.putAll(UtilClass.getMapFromNode(profileNode));
    graph.commit();
    
    return returnMap;
  }
  
  private void saveMapping(List<Map<String, Object>> addedAttributesMapping,
      List<Map<String, Object>> addedTagsMapping, List<Map<String, Object>> addedClassesMapping,
      List<Map<String, Object>> addedTaxonomiesMapping, Vertex profileNode) throws Exception
  {
    MappingUtils.saveAttributeMapping(profileNode, addedAttributesMapping, new ArrayList<>(),
        new ArrayList<>());
    MappingUtils.saveTagMapping(profileNode, addedTagsMapping, new ArrayList<>(),
        new ArrayList<>());
    MappingUtils.saveClassMapping(profileNode, addedClassesMapping, new ArrayList<>(),
        new ArrayList<>());
    MappingUtils.saveTaxonomyMapping(profileNode, addedTaxonomiesMapping, new ArrayList<>(),
        new ArrayList<>());
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateMapping/*" };
  }
}
