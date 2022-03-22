package com.cs.imprt.config.strategy.plugin.usecase.tag;

import com.cs.config.strategy.plugin.delivery.response.ResponseCarrier;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.util.ValidationUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.importtag.ImportTagDuplicateMappingIdException;
import com.cs.core.config.interactor.exception.importtag.ImportTagNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.constants.application.ImportConstants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.orientechnologies.orient.server.network.protocol.http.OHttpRequest;
import com.orientechnologies.orient.server.network.protocol.http.OHttpResponse;
import com.orientechnologies.orient.server.network.protocol.http.command.OServerCommandAuthenticatedDbAbstract;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SaveImportTag extends OServerCommandAuthenticatedDbAbstract {
  
  public SaveImportTag(final OServerCommandConfiguration iConfiguration)
  {
    super();
  }
  
  @Override
  public boolean execute(final OHttpRequest iRequest, OHttpResponse iResponse) throws Exception
  {
    String model = iRequest.content.toString();
    HashMap<String, Object> tagMap = new HashMap<String, Object>();
    HashMap<String, Object> map = new HashMap<String, Object>();
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    
    try {
      map = ObjectMapperUtil.readValue(model, HashMap.class);
      tagMap = (HashMap<String, Object>) map.get("tag");
      ODatabaseDocumentTx database = (ODatabaseDocumentTx) getProfiledDatabaseInstance(iRequest);
      OrientGraph graph = new OrientGraph(database);
      UtilClass.setGraph(graph);
      
      if (ValidationUtils.validateTagInfo(tagMap)) {
        
        String tagId = (String) tagMap.get(CommonConstants.ID_PROPERTY);
        Vertex tagNode = null;
        try {
          tagNode = UtilClass.getVertexByIndexedId(tagId, VertexLabelConstants.ENTITY_TAG);
        }
        catch (NotFoundException e) {
          throw new ImportTagNotFoundException();
        }
        
        UtilClass.saveNode(tagMap, tagNode);
        tagNode.removeProperty("defaultValue");
        tagNode.removeProperty("tagValues");
        
        Map<String, Object> defaultTagMap = (Map<String, Object>) tagMap
            .get(CommonConstants.DEFAULT_VALUE_PROPERTY);
        
        if (defaultTagMap != null) {
          
          boolean isSameTagExists = false;
          String defaultTagId = (String) defaultTagMap.get("id");
          Iterator<Edge> i = tagNode
              .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_DEFAULT_TAG_OF)
              .iterator();
          
          Edge defaultTagRelationship = null;
          while (i.hasNext()) {
            defaultTagRelationship = i.next();
          }
          
          if (defaultTagRelationship != null) {
            Vertex defaultTag = defaultTagRelationship.getVertex(Direction.OUT);
            String existingDefaultTagId = (String) defaultTag
                .getProperty(CommonConstants.CODE_PROPERTY);
            if (existingDefaultTagId.equals(defaultTagId)) {
              isSameTagExists = true;
            }
            else {
              defaultTagRelationship.remove();
            }
          }
          
          if (defaultTagId != null && !isSameTagExists) {
            Vertex defaultTagValue = UtilClass.getVertexByIndexedId(defaultTagId,
                VertexLabelConstants.ENTITY_TAG);
            defaultTagValue.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_DEFAULT_TAG_OF,
                tagNode);
          }
        }
        
        String tagTypeId = (String) tagMap.get(CommonConstants.TAG_TYPE_PROPERTY);
        if (tagTypeId != null) {
          
          boolean isSameTagTypeExists = false;
          Iterator<Edge> edgeIterator = tagNode
              .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_TAG_TYPE_OF)
              .iterator();
          Edge tagTypeRelationship = null;
          while (edgeIterator.hasNext()) {
            tagTypeRelationship = edgeIterator.next();
          }
          
          if (tagTypeRelationship != null) {
            Vertex tagTypeNode = tagTypeRelationship.getVertex(Direction.OUT);
            String existingTagTypeId = (String) tagTypeNode
                .getProperty(CommonConstants.CODE_PROPERTY);
            if (existingTagTypeId.equals(tagTypeId)) {
              isSameTagTypeExists = true;
            }
            else {
              tagTypeRelationship.remove();
              
              if (existingTagTypeId.equals(CommonConstants.CUSTOM_TAG_TYPE_ID)) {
                TagUtils.deleteCustomTypeValuesFromtag(tagNode, graph);
              }
            }
          }
          if (!isSameTagTypeExists) {
            TagUtils.createAndlinkTagAndTagType(tagMap, tagNode);
          }
          
          if (tagTypeId.equals(CommonConstants.CUSTOM_TAG_TYPE_ID)) {
            List<HashMap<String, Object>> customTagValues = (List<HashMap<String, Object>>) tagMap
                .get(CommonConstants.TAG_VALUES);
            TagUtils.createTagCustomTypeValues(tagNode, customTagValues, graph);
          }
        }
        
        // implementation for mappedTo field in tag
        String currentTagId = tagNode.getProperty(CommonConstants.CODE_PROPERTY);
        String mappedTagId = tagNode.getProperty(ImportConstants.MAPPED_TO_PROPERTY);
        if (mappedTagId != null && !currentTagId.equals(mappedTagId)) {
          Iterable<Vertex> existingTagsWithSameMappingId = graph.getVertices(
              VertexLabelConstants.ENTITY_TAG, new String[] { CommonConstants.CODE_PROPERTY },
              new Object[] { mappedTagId });
          if (existingTagsWithSameMappingId.iterator()
              .hasNext()) {
            Vertex existingTagVertex = existingTagsWithSameMappingId.iterator()
                .next();
            String tagLabel = (String) UtilClass.getValueByLanguage(existingTagVertex,
                CommonConstants.LABEL_PROPERTY);
            throw new ImportTagDuplicateMappingIdException();
          }
          tagNode.setProperty(CommonConstants.CODE_PROPERTY, mappedTagId);
        }
        
        graph.commit();
        returnMap = TagUtils.getTagMap(tagNode, false);
      }
      
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
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveImportTag/*" };
  }
}
