package com.cs.config.strategy.plugin.usecase.tag;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cs.config.interactor.model.auditlog.IAuditLogModel;
import com.cs.config.strategy.plugin.usecase.datarule.util.DataRuleUtils;
import com.cs.config.strategy.plugin.usecase.grideditablepropertylist.util.GridEditUtil;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.util.ValidationUtils;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.config.interactor.model.tag.ISaveTagModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.config.interactor.model.template.IModifiedSequenceModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

/**
 * @author tauseef
 */
public abstract class AbstractUpdateTag extends AbstractOrientPlugin {

  public static List<String> fieldsToExclude = Arrays.asList(ISaveTagModel.CHILDREN,
      ISaveTagModel.DEFAULT_VALUE, ISaveTagModel.TAG_VALUES, ISaveTagModel.PARENT,
      ISaveTagModel.ALLOWED_TAGS);

  public AbstractUpdateTag(final OServerCommandConfiguration iConfiguration) {
    super(iConfiguration);
  }

  public Map<String, Object> updateTag(Map<String, Object> tagMap, List<Map<String, Object>> auditInfoList) throws Exception {
    Map<String, Object> tagReturnMap = new HashMap<>();
    tagMap.remove(ISaveTagModel.TAG_VALUES_SEQUENCE);
    tagMap.remove(ISaveTagModel.TYPE);
    if (ValidationUtils.validateTagInfo(tagMap)) {

      String tagCode = (String) tagMap.get(CommonConstants.CODE_PROPERTY);
      Vertex tagNode = null;
      try {
        tagNode = UtilClass.getVertexByCode(tagCode, VertexLabelConstants.ENTITY_TAG);
      }
      catch (NotFoundException e) {
        throw new TagNotFoundException();
      }
      
      Vertex parentTagNode = null;
      Iterator<Vertex> parentVertics = tagNode.getVertices(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF).iterator();
      if (parentVertics.hasNext()) {
        parentTagNode = parentVertics.next();
      }
      Boolean isParentExist = parentTagNode != null;

      if (!TagUtils.hasParent(tagNode)) {
        String oldTagType = tagNode.getProperty(CommonConstants.TAG_TYPE_PROPERTY);
        String newTagType = (String) tagMap.get(CommonConstants.TAG_TYPE_PROPERTY);
        String oldLabel = (String) UtilClass.getValueByLanguage(tagNode, ITag.LABEL);
        String newLabel = (String) tagMap.get(ITag.LABEL);
        if (!oldTagType.equals(newTagType)) {
          DataRuleUtils.deleteVerticesWithInDirection(tagNode,
              RelationshipLabelConstants.ENTITY_RULE_VIOLATION_LINK);
          DataRuleUtils.deleteIntermediateVerticesWithInDirection(tagNode,
              RelationshipLabelConstants.TAG_DATA_RULE_LINK);
          DataRuleUtils.deleteVerticesWithInDirection(tagNode,
              RelationshipLabelConstants.NORMALIZATION_ENTITY_LINK);

          if (newTagType.equals(SystemLevelIds.BOOLEAN_TAG_TYPE_ID)) {
            DataRuleUtils.deleteVerticesWithInDirection(tagNode,
                RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
            TagUtils.createDefaultBooleanChild(tagNode);
          }
          else if (oldTagType.equals(SystemLevelIds.BOOLEAN_TAG_TYPE_ID)) {
            DataRuleUtils.deleteVerticesWithInDirection(tagNode,
                RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
          }
          if (newTagType.equals(SystemLevelIds.MASTER_TAG_TYPE_ID)) {
            tagMap.put(ITagModel.IS_MULTI_SELECT, true);
          }
        }
        else if (oldTagType.equals(SystemLevelIds.BOOLEAN_TAG_TYPE_ID) && !oldLabel.equals(newLabel)) {
          Iterator<Vertex> vertices = tagNode.getVertices(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF).iterator();
          Vertex childNode = vertices.next();
          childNode.setProperty(ITag.LABEL, newLabel);
        }
      }
      
      // linked and unlinked master tag
      updateLinkedMasterTag(tagMap, tagNode);

      // Setting isGridEditable for master tag if not available
      if (tagNode.getProperty("isGridEditable") == null) {
        tagNode.setProperty("isGridEditable", false);
      }
      if (tagNode.getProperty("isGridEditable").equals(true) && tagMap.get("isGridEditable")
          .equals(false)) {
        GridEditUtil.removePropertyFromGridEditSequenceList(
            tagNode.getProperty(CommonConstants.CODE_PROPERTY));
      }

      UtilClass.saveNode(tagMap, tagNode, fieldsToExclude);

      // update default tag Value
      updateDefaultTagValue(tagMap, tagNode);

      String tagTypeId = (String) tagMap.get(CommonConstants.TAG_TYPE_PROPERTY);
      if (tagTypeId != null) {

        boolean isSameTagTypeExists = false;
        Iterator<Edge> edgeIterator = tagNode.getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_TAG_TYPE_OF).iterator();
        Edge tagTypeRelationship = null;
        while (edgeIterator.hasNext()) {
          tagTypeRelationship = edgeIterator.next();
        }

        if (tagTypeRelationship != null) {
          Vertex tagTypeNode = tagTypeRelationship.getVertex(Direction.OUT);
          String existingTagTypeId = (String) tagTypeNode.getProperty(CommonConstants.CODE_PROPERTY);
          if (existingTagTypeId.equals(tagTypeId)) {
            isSameTagTypeExists = true;
          }
          else {
            tagTypeRelationship.remove();

            if (existingTagTypeId.equals(CommonConstants.CUSTOM_TAG_TYPE_ID)) {
              TagUtils.deleteCustomTypeValuesFromtag(tagNode, UtilClass.getGraph());
            }
          }
        }
        if (!isSameTagTypeExists) {
          TagUtils.createAndlinkTagAndTagType(tagMap, tagNode);
        }

        if (tagTypeId.equals(CommonConstants.CUSTOM_TAG_TYPE_ID)) {
          List<HashMap<String, Object>> customTagValues = (List<HashMap<String, Object>>) tagMap.get(
              CommonConstants.TAG_VALUES);
          TagUtils.createTagCustomTypeValues(tagNode, customTagValues, UtilClass.getGraph());
        }
      }
      // for changing the sequence of tagValues into the tagGroup
      Map<String, Object> modifiedSequenceProperty = (Map<String, Object>) tagMap.get(ISaveTagModel.MODIFIED_SEQUENCE);
      if (modifiedSequenceProperty != null) {
        setModifiedSequenceToTagGroup(tagNode, modifiedSequenceProperty);
      }
      
      Vertex tagVertexForAuditInfo = isParentExist ? parentTagNode : tagNode;
      Map<String, Object> auditLogInfoModelMap = AuditLogUtils.prepareAndGetAuditLogInfoModel(
          tagVertexForAuditInfo, Entities.PROPERTIES, Elements.TAGS, (String) tagVertexForAuditInfo
              .getProperty(EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY)));
      if(isParentExist) {
        auditLogInfoModelMap.put(IAuditLogModel.ACTIVITY, ServiceType.UPDATED);
      }
      auditInfoList.add(auditLogInfoModelMap);
      tagReturnMap = TagUtils.getTagMap(tagNode, false);
    }

    return tagReturnMap;
  }

  public void updateDefaultTagValue(Map<String, Object> tagMap, Vertex tagNode)
  {
    Map<String, Object> defaultTagMap = (Map<String, Object>) tagMap.get(CommonConstants.DEFAULT_VALUE_PROPERTY);

    boolean isSameTagExists = false;
    Iterator<Edge> i = tagNode.getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_DEFAULT_TAG_OF).iterator();

    Edge defaultTagRelationship = null;
    while (i.hasNext()) {
      defaultTagRelationship = i.next();
    }
    if (defaultTagMap != null) {
      String defaultTagId = (String) defaultTagMap.get("id");
      if (defaultTagRelationship != null) {
        Vertex defaultTag = defaultTagRelationship.getVertex(Direction.OUT);
        String existingDefaultTagId = (String) defaultTag.getProperty(CommonConstants.CODE_PROPERTY);
        if (existingDefaultTagId.equals(defaultTagId)) {
          isSameTagExists = true;
        }
        else {
          defaultTagRelationship.remove();
        }
      }

      if (StringUtils.isNotEmpty(defaultTagId) && !isSameTagExists) {
        Vertex defaultTagValue = UtilClass.getGraph()
            .getVertices(VertexLabelConstants.ENTITY_TAG,
                new String[] { CommonConstants.CODE_PROPERTY }, new Object[] { defaultTagId })
            .iterator()
            .next();
        defaultTagValue.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_DEFAULT_TAG_OF,
            tagNode);
      }
    }
    else if (defaultTagRelationship != null) {
      defaultTagRelationship.remove();
    }
  }

  private void setModifiedSequenceToTagGroup(Vertex tagNode,
      Map<String, Object> modifiedSequenceProperty) throws Exception {
    String tagValueId = (String) modifiedSequenceProperty.get(IModifiedSequenceModel.ID);
    Integer tagValueSequence = (Integer) modifiedSequenceProperty.get(IModifiedSequenceModel.SEQUENCE);
    Vertex parentNode = TagUtils.getParentTag(tagNode);
    if (parentNode != null) {
      List<String> sequenceProperty = parentNode.getProperty(ITagModel.TAG_VALUES_SEQUENCE);
      sequenceProperty.remove(tagValueId);
      sequenceProperty.add(tagValueSequence, tagValueId);
      parentNode.setProperty(ITagModel.TAG_VALUES_SEQUENCE, sequenceProperty);
    }
  }
  
  public void updateLinkedMasterTag(Map<String, Object> tagMap, Vertex tagNode) throws Exception
  {
    String linkedMasterTagId = (String) tagMap.remove(ITaxonomy.LINKED_MASTER_TAG_ID);
    if (linkedMasterTagId != null && !linkedMasterTagId.isEmpty()) {
      Iterator<Edge> masterTagEdgeIterator = tagNode.getEdges(Direction.IN,
          RelationshipLabelConstants.MASTER_TAG_OF).iterator();
      if (masterTagEdgeIterator.hasNext()) {
        Edge masterTagOfEdge = masterTagEdgeIterator.next();
        Vertex masterTagNode = masterTagOfEdge.getVertex(Direction.OUT);
        String masterTagId = UtilClass.getCodeNew(masterTagNode);
        if (!masterTagId.equals(linkedMasterTagId)) {
          masterTagOfEdge.remove();
          Vertex newMasterTagNode = UtilClass.getVertexById(linkedMasterTagId,
              VertexLabelConstants.ENTITY_TAG);
          newMasterTagNode.addEdge(RelationshipLabelConstants.MASTER_TAG_OF, tagNode);
        }
      }
      else {
        Vertex newMasterTagNode = UtilClass.getVertexById(linkedMasterTagId,
            VertexLabelConstants.ENTITY_TAG);
        newMasterTagNode.addEdge(RelationshipLabelConstants.MASTER_TAG_OF, tagNode);
      }
    }
    else {
      // Already link with master but now its removed
      Iterator<Edge> masterTagEdgeIterator = tagNode.getEdges(Direction.IN,
          RelationshipLabelConstants.MASTER_TAG_OF).iterator();
      if (masterTagEdgeIterator.hasNext()) {
        Edge masterTagAndTagNodeEdge = masterTagEdgeIterator.next();
        Vertex masterTagVertex = masterTagAndTagNodeEdge.getVertex(Direction.OUT);
        removeChildMasterTagLink(masterTagVertex);
        masterTagAndTagNodeEdge.remove();
      }
    }
  }

  private void removeChildMasterTagLink(Vertex masterTagVertex) {
    Iterable<Vertex> childVertices = masterTagVertex.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for (Vertex childVertex : childVertices) {
      Iterable<Edge> childEdgeForTags = childVertex.getEdges(Direction.OUT,
          RelationshipLabelConstants.MASTER_TAG_OF);
      for (Edge childEdgeForTag : childEdgeForTags) {
        childEdgeForTag.remove();
      }
    }
  }
}