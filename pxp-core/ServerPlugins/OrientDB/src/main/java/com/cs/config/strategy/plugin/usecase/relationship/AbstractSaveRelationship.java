package com.cs.config.strategy.plugin.usecase.relationship;

import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.translations.utils.TranslationsUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.entity.relationship.ISaveRelationshipSide;
import com.cs.core.config.interactor.exception.relationship.RelationshipNotFoundException;
import com.cs.core.config.interactor.model.klass.IAddedNatureRelationshipModel;
import com.cs.core.config.interactor.model.klass.IModifiedNatureRelationshipModel;
import com.cs.core.config.interactor.model.relationship.ICreateRelationshipModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipModel;
import com.cs.core.config.interactor.model.relationship.ISaveRelationshipModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

import java.util.*;

/**
 * @author tauseef
 */
public abstract class AbstractSaveRelationship extends AbstractOrientPlugin {

  static final String CLASS_CODE             = "classCode";
  static final String CONTEXT_CODE           = "contextCode";
  static final String COUPLINGS              = "couplings";
  static final String ATTRIBUTE              = "attribute";
  static final String TAG                    = "tag";
  static final String RELATIONSHIP           = "relationship";
  static final String ADDED_ATTRIBUTES       = "addedAttributes";
  static final String ADDED_TAGS             = "addedTags";
  static final String ADDED_RELATIONSHIPS    = "addedRelationships";
  static final String DELETED_ATTRIBUTES     = "deletedAttributes";
  static final String DELETED_TAGS           = "deletedTags";
  static final String DELETED_RELATIONSHIPS  = "deletedRelationships";
  static final String MODIFIED_ATTRIBUTES    = "modifiedAttributes";
  static final String MODIFIED_TAGS          = "modifiedTags";
  static final String MODIFIES_RELATIONSHIPS = "modifiedRelationships";
  static final String TYPE                   = "type";
  static final String SIDE                   = "side";
  static final String SIDE1                  = "side1";
  static final String SIDE2                  = "side2";
  static final String TAB                    = "tab";
  static final String EXTENSION_KLASS_ID     = "extensionKlassId";

  public static List<String> propertiesToExclude      = Arrays.asList(
      ISaveRelationshipModel.ADDED_SECTIONS, ISaveRelationshipModel.DELETED_SECTIONS,
      ISaveRelationshipModel.MODIFIED_SECTIONS, ISaveRelationshipModel.ADDED_ELEMENTS,
      ISaveRelationshipModel.DELETED_ELEMENTS, ISaveRelationshipModel.MODIFIED_ELEMENTS,
      ADDED_ATTRIBUTES, DELETED_ATTRIBUTES, MODIFIED_ATTRIBUTES, ADDED_TAGS, MODIFIED_TAGS,
      DELETED_TAGS, ADDED_RELATIONSHIPS, MODIFIES_RELATIONSHIPS, DELETED_RELATIONSHIPS,
      ISaveRelationshipModel.ADDED_TAB, ISaveRelationshipModel.DELETED_TAB);
  
  public static List<String> fieldsToExcludeForCreate = Arrays.asList(IRelationshipModel.SECTIONS,
      IRelationshipModel.ADDED_ATTRIBUTES, MODIFIED_ATTRIBUTES, DELETED_ATTRIBUTES, ADDED_TAGS,
      MODIFIED_TAGS, DELETED_TAGS, ADDED_RELATIONSHIPS, MODIFIES_RELATIONSHIPS,
      DELETED_RELATIONSHIPS, IAddedNatureRelationshipModel.PROPERTY_COLLECTION,
      IAddedNatureRelationshipModel.CONTEXT_TAGS, ICreateRelationshipModel.TAB, EXTENSION_KLASS_ID,
      IModifiedNatureRelationshipModel.ADDED_RELATIONSHIP_INHERITANCE);

  public AbstractSaveRelationship(OServerCommandConfiguration iConfiguration) {
    super(iConfiguration);
  }

  public Vertex updateRelationshipFromMap(Map<String, Object> relationshipMap,
      Map<String, Map<String, Object>> sideInfoForDataTransfer) throws Exception {
    Vertex relationshipNode = null;
    String relationshipId = null;
    try {
      relationshipId = (String) relationshipMap.get(CommonConstants.ID_PROPERTY);
      relationshipNode = UtilClass.getVertexByIndexedId(relationshipId,
          VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP);
    }
    catch (NotFoundException e) {
      throw new RelationshipNotFoundException();
    }

    updateRelationshipFromNode(relationshipMap, sideInfoForDataTransfer, relationshipNode);

    return relationshipNode;
  }

  public Vertex updateRelationshipFromNode(Map<String, Object> relationshipMap,
      Map<String, Map<String, Object>> sideInfoForDataTransfer, Vertex relationshipNode)
      throws Exception {
    //Side Label Update
    Map<String, Object> side1Map = (Map<String, Object>) relationshipMap.get(IRelationship.SIDE1);
    TranslationsUtils.updateRelationshipSideLabel(side1Map);
    Map<String, Object> side2Map = (Map<String, Object>) relationshipMap.get(IRelationship.SIDE2);
    TranslationsUtils.updateRelationshipSideLabel(side2Map);

    //Coupling
    RelationshipUtils.manageADMOfProperties(relationshipNode, relationshipMap,
        sideInfoForDataTransfer);

    //Side info update with context
    RelationshipUtils.updateSideLabelsContextAndVisibility(UtilClass.getGraph(), relationshipMap,
        relationshipNode);

    Map<String, Object> side1 = (Map<String, Object>) relationshipMap.get(IRelationship.SIDE1);
    side1.remove(IRelationshipSide.ATTRIBUTES);
    side1.remove(IRelationshipSide.TAGS);
    side1.remove(ISaveRelationshipSide.ADDED_CONTEXT);
    side1.remove(ISaveRelationshipSide.DELETED_CONTEXT);
    side1.remove(COUPLINGS);

    Map<String, Object> side2 = (Map<String, Object>) relationshipMap.get(IRelationship.SIDE2);
    side2.remove(IRelationshipSide.ATTRIBUTES);
    side2.remove(IRelationshipSide.TAGS);
    side2.remove(ISaveRelationshipSide.ADDED_CONTEXT);
    side2.remove(ISaveRelationshipSide.DELETED_CONTEXT);
    side2.remove(COUPLINGS);

    manageADMForRelationshipSides(relationshipMap, relationshipNode);

    side1 = (Map<String, Object>) relationshipMap.get(IRelationship.SIDE1);
    side2 = (Map<String, Object>) relationshipMap.get(IRelationship.SIDE2);
    
    side1.remove("label");
    side2.remove("label");
    
    //do not update the relationship type
    relationshipMap.remove(IRelationship.IS_LITE);
    
    UtilClass.saveNode(relationshipMap, relationshipNode, propertiesToExclude);

    String tabIdToDelete = (String) relationshipMap.get(ISaveRelationshipModel.DELETED_TAB);
    Map<String, Object> tabMap = (Map<String, Object>) relationshipMap
        .get(ISaveRelationshipModel.ADDED_TAB);
    TabUtils.manageAddedAndDeletedTab(relationshipNode, tabMap, tabIdToDelete,
        CommonConstants.RELATIONSHIP);

    return relationshipNode;
  }

  public Vertex createNewRelationship(Map<String, Object> relationshipMap) throws Exception {
    Vertex relationshipNode;
    if ((Boolean) relationshipMap.get(IRelationship.IS_NATURE)) {
      relationshipNode = RelationshipUtils.createNatureRelationship(relationshipMap,
          fieldsToExcludeForCreate);
    }
    else {
      relationshipNode = RelationshipUtils.createRelationship(relationshipMap,
          fieldsToExcludeForCreate);
    }

    RelationshipUtils.addSectionElement(UtilClass.getGraph(), relationshipMap,
        relationshipNode);
    RelationshipUtils.manageAddedAttributes(relationshipNode, relationshipMap,
        new HashMap<>());
    RelationshipUtils.manageAddedTags(relationshipNode, relationshipMap, new HashMap<>());
    Map<String, Object> tabMap = (Map<String, Object>) relationshipMap.get(TAB);
    TabUtils.linkAddedOrDefaultTab(relationshipNode, tabMap, CommonConstants.RELATIONSHIP);
    return relationshipNode;
  }

  private void manageADMForRelationshipSides(Map<String, Object> relationshipMap,
      Vertex relationshipNode)
  {
    Map<String, Object> side1 = (Map<String, Object>) relationshipMap.get(IRelationship.SIDE1);
    Map<String, Object> side2 = (Map<String, Object>) relationshipMap.get(IRelationship.SIDE2);
    Map<String, Object> existingSide1 = relationshipNode.getProperty(IRelationship.SIDE1);
    Map<String, Object> existingSide2 = relationshipNode.getProperty(IRelationship.SIDE2);
    existingSide1.putAll(side1);
    existingSide2.putAll(side2);
    relationshipMap.put(IRelationship.SIDE1, existingSide1);
    relationshipMap.put(IRelationship.SIDE2, existingSide2);
  }

}

