package com.cs.config.strategy.plugin.usecase.mapping;

import com.cs.config.strategy.plugin.delivery.response.ResponseCarrier;
import com.cs.config.strategy.plugin.usecase.mapping.util.MappingUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.exception.profile.ProfileNotFoundException;
import com.cs.core.config.interactor.exception.user.UserNotFoundException;
import com.cs.core.config.interactor.model.mapping.IColumnValueTagValueMappingModel;
import com.cs.core.config.interactor.model.mapping.IConfigRuleAttributeMappingModel;
import com.cs.core.config.interactor.model.mapping.IConfigRuleTagMappingModel;
import com.cs.core.config.interactor.model.mapping.IMappingModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentEmbedded;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.orientechnologies.orient.server.network.protocol.http.OHttpRequest;
import com.orientechnologies.orient.server.network.protocol.http.OHttpResponse;
import com.orientechnologies.orient.server.network.protocol.http.command.OServerCommandAuthenticatedDbAbstract;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({ "unchecked" })
public class GetMappingByUserId extends OServerCommandAuthenticatedDbAbstract {
  
  public GetMappingByUserId(final OServerCommandConfiguration iConfiguration)
  {
    super();
  }
  
  @Override
  public boolean execute(final OHttpRequest iRequest, OHttpResponse iResponse) throws Exception
  {
    String model = iRequest.content.toString();
    HashMap<String, Object> map = new HashMap<String, Object>();
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    
    try {
      map = ObjectMapperUtil.readValue(model, HashMap.class);
      String currentUserId = (String) map.get(CommonConstants.ID_PROPERTY);
      List<String> fileHeaders = (List<String>) map.get("fileHeaders");
      
      ODatabaseDocumentEmbedded database = (ODatabaseDocumentEmbedded) getProfiledDatabaseInstance(
          iRequest);
      OrientGraph graph = new OrientGraph(database);
      UtilClass.setGraph(graph);
      
      Vertex endpoint = getEndpoint(currentUserId);
      
      if (endpoint == null) {
        throw new ProfileNotFoundException();
      }
      List<String> mappedColumns = new ArrayList<>();
      returnMap.putAll(UtilClass.getMapFromVertex(new ArrayList<>(), endpoint));
      Map<String, Object> attributeMapForIdAndLabel = new HashMap<>();
      Map<String, Object> tagMapForIdAndLabel = new HashMap<>();
      Map<String, Object> taxonomyMapForIdAndLabel = new HashMap<>();
      Map<String, Object> klassMapForIdAndLabel = new HashMap<>();
      Map<String, Object> relationshipMapForIdAndLabel = new HashMap<>();
      MappingUtils.getMapFromProfileNode(endpoint, returnMap, new ArrayList<>(),
          attributeMapForIdAndLabel, tagMapForIdAndLabel, taxonomyMapForIdAndLabel,
          klassMapForIdAndLabel, relationshipMapForIdAndLabel);
      
      fileHeaders.removeAll(mappedColumns);
      
      Set<String> finalFileHeader = new HashSet<>();
      for (String header : fileHeaders) {
        finalFileHeader.add("'" + header + "'");
      }
      List<Map<String, Object>> attributesFinalMappings = (List<Map<String, Object>>) returnMap
          .get(IMappingModel.ATTRIBUTE_MAPPINGS);
      List<Map<String, Object>> tagFinalMappings = (List<Map<String, Object>>) returnMap
          .get(IMappingModel.TAG_MAPPINGS);
      
      List<String> mappedTagId = new ArrayList<>();
      for (Map<String, Object> tagMapping : tagFinalMappings) {
        mappedTagId.add("'"
            + (String) tagMapping.get(IConfigRuleAttributeMappingModel.MAPPED_ELEMENT_ID) + "'");
      }
      
      // TODO - Class and Taxonomy mappings
      
      getSubTagsForMappedTags(tagFinalMappings, mappedTagId);
      
      autoMapProperties(finalFileHeader, attributesFinalMappings, tagFinalMappings);
      ResponseCarrier.successResponse(iResponse, returnMap);
    }
    catch (PluginException e) {
      ResponseCarrier.failedResponse(iResponse, e);
    }
    catch (Exception e) {
      ResponseCarrier.failedResponse(iResponse, e);
    }
    
    return false;
  }
  
  private void getSubTagsForMappedTags(List<Map<String, Object>> tagFinalMappings,
      List<String> mappedTagId)
  {
    Iterable<Vertex> resultIterableForMappedTags = UtilClass.getGraph()
        .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TAG + " where "
            + CommonConstants.CODE_PROPERTY + " in " + mappedTagId.toString()))
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
  
  private void autoMapProperties(Set<String> finalFileHeader,
      List<Map<String, Object>> attributesFinalMappings, List<Map<String, Object>> tagFinalMappings)
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> resultIterableForAttributes = graph
        .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE
            + " where " + CommonConstants.CODE_PROPERTY + " in " + finalFileHeader.toString()))
        .execute();
    
    Iterable<Vertex> resultIterableForTags = graph
        .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TAG + " where "
            + CommonConstants.CODE_PROPERTY + " in " + finalFileHeader.toString()))
        .execute();
    
    OrientVertexType mappingVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.PROPERTY_MAPPING, CommonConstants.CODE_PROPERTY);
    
    for (Vertex vertex : resultIterableForAttributes) {
      Map<String, Object> attributeMapping = new HashMap<>();
      String key = vertex.getProperty(CommonConstants.CODE_PROPERTY);
      Long versionId = vertex.getProperty(CommonConstants.VERSION_ID);
      attributeMapping.put(IConfigRuleAttributeMappingModel.MAPPED_ELEMENT_ID, key);
      attributeMapping.put(CommonConstants.VERSION_ID, versionId);
      attributeMapping.put(IConfigRuleAttributeMappingModel.COLUMN_NAMES, Arrays.asList(key));
      attributeMapping.put(CommonConstants.IS_IGNORED_PROPERTY, false);
      attributeMapping.put(CommonConstants.ID_PROPERTY,
          UtilClass.getUniqueSequenceId(mappingVertexType));
      attributesFinalMappings.add(attributeMapping);
    }
    for (Vertex vertex : resultIterableForTags) {
      Map<String, Object> tagMappings = new HashMap<>();
      String key = vertex.getProperty(CommonConstants.CODE_PROPERTY);
      Long versionId = vertex.getProperty(CommonConstants.VERSION_ID);
      tagMappings.put(IConfigRuleAttributeMappingModel.MAPPED_ELEMENT_ID, key);
      tagMappings.put(CommonConstants.VERSION_ID, versionId);
      tagMappings.put(IConfigRuleAttributeMappingModel.COLUMN_NAMES, Arrays.asList(key));
      tagMappings.put(CommonConstants.IS_IGNORED_PROPERTY, false);
      tagMappings.put(CommonConstants.ID_PROPERTY,
          UtilClass.getUniqueSequenceId(mappingVertexType));
      
      Map<String, Object> tagValueMappings = new HashMap<>();
      tagValueMappings.put(IColumnValueTagValueMappingModel.COLUMN_NAME, key);
      tagValueMappings.put(IColumnValueTagValueMappingModel.MAPPINGS, new ArrayList<>());
      
      List<String> unmappedTagValues = vertex.getProperty(ITag.TAG_VALUES_SEQUENCE);
      tagValueMappings.put(IColumnValueTagValueMappingModel.TAG_VALUE_IDS, unmappedTagValues);
      
      tagMappings.put(IConfigRuleTagMappingModel.TAG_VALUE_MAPPINGS,
          Arrays.asList(tagValueMappings));
      
      tagFinalMappings.add(tagMappings);
    }
  }
  
  private Vertex getEndpoint(String currentUserId) throws Exception
  {
    Vertex endpointNode = null;
    try {
      Vertex userNode = UtilClass.getVertexByIndexedId(currentUserId,
          VertexLabelConstants.ONBOARDING_USER);
      Iterable<Vertex> roleNodes = userNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.RELATIONSHIPLABEL_USER_IN);
      for (Vertex roleNode : roleNodes) {
        // FIX ME
        Iterable<Edge> endpointInRelationships = roleNode.getEdges(Direction.OUT,
            RelationshipLabelConstants.HAS_ENDPOINT);
        for (Edge endpointEdge : endpointInRelationships) {
          if (!(Boolean) endpointEdge.getProperty(CommonConstants.ENDPOINT_OWNER)) {
            endpointNode = endpointEdge.getVertex(com.tinkerpop.blueprints.Direction.IN);
          }
        }
      }
    }
    catch (NotFoundException e) {
      throw new UserNotFoundException();
    }
    
    return endpointNode;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetMappingByUserId/*" };
  }
}
