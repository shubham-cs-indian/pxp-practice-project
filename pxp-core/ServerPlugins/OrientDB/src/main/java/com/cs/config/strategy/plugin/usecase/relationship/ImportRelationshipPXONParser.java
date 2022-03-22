package com.cs.config.strategy.plugin.usecase.relationship;

import static com.cs.config.idto.IConfigJSONDTO.ConfigTag.couplingType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.CRC32;

import org.apache.commons.lang3.StringUtils;

import com.cs.config.standard.IStandardConfig;
import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.klass.IKlassNatureRelationship;
import com.cs.core.config.interactor.entity.klass.IKlassRelationshipSide;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.entity.relationship.ISaveRelationshipSide;
import com.cs.core.config.interactor.model.klass.IModifiedNatureRelationshipModel;
import com.cs.core.config.interactor.model.propertycollection.IAddedTabModel;
import com.cs.core.config.interactor.model.relationship.ISaveRelationshipModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.exception.onboarding.endpoint.AttributeMappingsException;
import com.tinkerpop.blueprints.Vertex;

/**
 * @author tauseef
 */
public class ImportRelationshipPXONParser {

  private static final String CLASS_CODE             = "classCode";
  private static final String CONTEXT_CODE           = "contextCode";
  private static final String COUPLINGS              = "couplings";
  private static final String RELATIONSHIP            = "relationship";
  private static final String ATTRIBUTE              = "attribute";
  private static final String TAG                    = "tag";
  private static final String ADDED_ATTRIBUTES       = "addedAttributes";
  private static final String ADDED_TAGS             = "addedTags";
  private static final String DELETED_ATTRIBUTES     = "deletedAttributes";
  private static final String DELETED_TAGS           = "deletedTags";
  private static final String MODIFIED_ATTRIBUTES    = "modifiedAttributes";
  private static final String MODIFIED_TAGS          = "modifiedTags";
  private static final String TYPE                   = "type";
  private static final String SIDE                   = "side";
  private static final String SIDE1                  = "side1";
  private static final String SIDE2                  = "side2";
  private static final String TAB                    = "tab";

  public static Vertex prepareADMForImport(Map<String, Object> newRelationMap, boolean isNatureRelationship, List<String> deletedRelationship) throws Exception {
    Vertex relationshipNode = null;
    String relationshipCode = null;
    Map<String, Object> oldSide1 = null;
    Map<String, Object> oldSide2 = null;
    Map<String, Object> oldRelationMap = null;
    boolean rExists = false;


    Map<String, Object> newSide1 = (Map<String, Object>) newRelationMap.get(IRelationship.SIDE1);
    Map<String, Object> newSide2 = (Map<String, Object>) newRelationMap.get(IRelationship.SIDE2);

    //KlassId repair

    String side1KlassId = (String) newSide1.remove(CLASS_CODE);
    newSide1.put(IRelationshipSide.KLASS_ID, side1KlassId);
    String side2KlassId = (String) newSide2.remove(CLASS_CODE);
    try {
      UtilClass.getVertexByCode(side2KlassId, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      newSide2.put(IRelationshipSide.KLASS_ID, side2KlassId);
    }catch (Exception e) {
      newSide2.put(IRelationshipSide.KLASS_ID, side1KlassId);
    }

    try {
      relationshipCode = (String) newRelationMap.get(CommonConstants.CODE_PROPERTY);
      newRelationMap.put(CommonConstants.ID_PROPERTY, relationshipCode);
      relationshipNode = UtilClass.getVertexByCode(relationshipCode,
          VertexLabelConstants.NATURE_RELATIONSHIP);

      oldRelationMap = RelationshipUtils.getRelationshipEntityMap(
          relationshipNode);
      oldSide1 = (Map<String, Object>) oldRelationMap.get(SIDE1);
      oldSide2 = (Map<String, Object>) oldRelationMap.get(SIDE2);
      String klassId  = (String) oldSide2.get(IRelationshipSide.KLASS_ID);
      if (klassId.equals(side2KlassId)) {
        rExists = true;
      }
      else {
        relationshipNode = null;
        deletedRelationship.add(relationshipCode);
      }
    }
    catch (NotFoundException ignored) {
      //added
    }

    //for nature relationship need to add extra fields
    if(isNatureRelationship) {
      prepareSideMap(newSide1);
      prepareSideMap(newSide2);
      setEntityInfo(newRelationMap, IKlassNatureRelationship.AUTO_CREATE_SETTINGS, false);
      setEntityInfo(newRelationMap, IKlassNatureRelationship.MAX_NO_OF_ITEMS, 0);
      setEntityInfo(newRelationMap, IKlassNatureRelationship.TAXONOMY_INHERITANCE_SETTING, "off");
    }else {
      newSide2.put(IRelationshipSide.KLASS_ID, side2KlassId);
    }
    
    //ContextId Repair
    //prepareADMForContext(rExists, newSide1, newSide2, oldSide1, oldSide2, isNatureRelationship);

    //Coupling repair
    prepareADMForCoupling(rExists, newRelationMap, newSide1, newSide2, oldSide1, oldSide2);

    //Tab Repair
    prepareADMForTab(rExists, newRelationMap, relationshipNode, isNatureRelationship);

    setEntityInfo(newRelationMap, IRelationship.AUTO_UPDATE, false);
    setEntityInfo(newRelationMap, IRelationship.ENABLE_AFTER_SAVE, false);
    setEntityInfo(newRelationMap, IRelationship.IS_MANDATORY, false);
    setEntityInfo(newRelationMap, IRelationship.IS_STANDARD, false);
    return relationshipNode;
  }
  
  public static Vertex prepareADMForNonNatureRelationshipImport(Map<String, Object> newRelationMap) throws Exception {
    Vertex relationshipNode = null;
    String relationshipCode = null;
    Map<String, Object> oldSide1 = null;
    Map<String, Object> oldSide2 = null;
    Map<String, Object> oldRelationMap = null;
    boolean rExists = false;

    try {
      relationshipCode = (String) newRelationMap.get(CommonConstants.CODE_PROPERTY);

      relationshipNode = UtilClass.getVertexByIndexedId(relationshipCode,
              VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP);
      
      oldRelationMap = RelationshipUtils.getRelationshipEntityMap(
          relationshipNode);
      oldSide1 = (Map<String, Object>) oldRelationMap.get(SIDE1);
      oldSide2 = (Map<String, Object>) oldRelationMap.get(SIDE2);
      rExists = true;
    }
    catch (Exception ignored) {
    }
    
    Map<String, Object> newSide1 = (Map<String, Object>) newRelationMap.get(IRelationship.SIDE1);
    Map<String, Object> newSide2 = (Map<String, Object>) newRelationMap.get(IRelationship.SIDE2);

    //KlassId repair
    
    String side1KlassId = (String) newSide1.remove(CLASS_CODE);
    newSide1.put(IRelationshipSide.KLASS_ID, side1KlassId);
    String side2KlassId = (String) newSide2.remove(CLASS_CODE);

    prepareSideMapMinimal(newSide1);
    prepareSideMapMinimal(newSide2);
    newSide2.put(IRelationshipSide.KLASS_ID, side2KlassId);
    
    //ContextId Repair
    prepareADMForContext(rExists, newSide1, newSide2, oldSide1, oldSide2, false);

    //Coupling repair
    prepareADMForNonNatureRelationshipCoupling(rExists, newRelationMap, newSide1, newSide2, oldSide1, oldSide2);

    //Tab Repair
    prepareADMForTab(rExists, newRelationMap, relationshipNode, false);

    setEntityInfo(newRelationMap, IRelationship.AUTO_UPDATE, false);
    setEntityInfo(newRelationMap, IRelationship.ENABLE_AFTER_SAVE, false);
    setEntityInfo(newRelationMap, IRelationship.IS_MANDATORY, false);
    setEntityInfo(newRelationMap, IRelationship.IS_STANDARD, false);
    return relationshipNode;
  }

  private static void prepareSideMap(Map<String, Object> newSide1)
  {
    setEntityInfo(newSide1, IRelationshipSide.ATTRIBUTES, new ArrayList<>());
    setEntityInfo(newSide1, IRelationshipSide.CARDINALITY, CommonConstants.CARDINALITY_MANY);
    setEntityInfo(newSide1, IRelationshipSide.CONTEXT_ID, null);
    setEntityInfo(newSide1, IRelationshipSide.IS_VISIBLE, true);
    setEntityInfo(newSide1, IRelationshipSide.TAGS, new ArrayList<>());
    setEntityInfo(newSide1, IRelationshipSide.RELATIONSHIPS, new ArrayList<>());
    setEntityInfo(newSide1, IRelationshipSide.ELEMENT_ID, null);
    setEntityInfo(newSide1, IRelationshipSide.LAST_MODIFIED_BY, null);
    setEntityInfo(newSide1, IRelationshipSide.VERSION_TIMESTAMP, null);
    setEntityInfo(newSide1, IRelationshipSide.VERSION_ID, null);
    setEntityInfo(newSide1, IRelationshipSide.ID, generateNewUniqueId(IStandardConfig.UniquePrefix.RELATIONSHIP.getPrefix()));
  }
  
  private static void prepareSideMapMinimal(Map<String, Object> newSide1)
  {
    setEntityInfo(newSide1, IRelationshipSide.ATTRIBUTES, new ArrayList<>());
    setEntityInfo(newSide1, IRelationshipSide.CONTEXT_ID, null);
    setEntityInfo(newSide1, IRelationshipSide.TAGS, new ArrayList<>());
    setEntityInfo(newSide1, IRelationshipSide.RELATIONSHIPS, new ArrayList<>());
    setEntityInfo(newSide1, IRelationshipSide.LAST_MODIFIED_BY, null);
    setEntityInfo(newSide1, IRelationshipSide.VERSION_TIMESTAMP, null);
    setEntityInfo(newSide1, IRelationshipSide.VERSION_ID, null);
    String generatedUniqueID = generateNewUniqueId(IStandardConfig.UniquePrefix.RELATIONSHIP.getPrefix());
    setEntityInfo(newSide1, IRelationshipSide.ID, generatedUniqueID);
    setEntityInfo(newSide1, IRelationshipSide.CODE, generatedUniqueID);
  }
  
  private static String generateNewUniqueId(String... ids)
  {
    StringBuilder sb = new StringBuilder();
    for (String id : ids) {
      CRC32 crc = new CRC32();
      crc.update(id.getBytes());
      String str = Long.toHexString(crc.getValue());
      sb.append(str);
    }
    return UUID.nameUUIDFromBytes(sb.toString()
        .getBytes())
        .toString();
  }

  private static void setEntityInfo(Map<String, Object> newSide1, String entityLabel, Object defaultValue)
  {
    Object attributes = newSide1.get(entityLabel);
    if(attributes == null) {
      newSide1.put(entityLabel, defaultValue);
    }
  }
  
  private static void prepareADMForTab(boolean rExists, Map<String, Object> newRelationMap,
      Vertex relationshipNode, boolean isNature)
      throws Exception {
    String newTabCode = (String) newRelationMap.remove(TAB);
    Map<String, Object> tabMap = new HashMap<>();
    if(StringUtils.isNotEmpty(newTabCode)) {
      try {
        UtilClass.getVertexByCode(newTabCode, VertexLabelConstants.TAB);
      }catch(NotFoundException e){
        newTabCode = getStandardTabAccToRelationshipType();
      }
    }else {
      newTabCode = getStandardTabAccToRelationshipType();
    }
    tabMap.put(CommonConstants.ID_PROPERTY, newTabCode);
    tabMap.put(CommonConstants.CODE_PROPERTY, newTabCode);
    tabMap.put(IAddedTabModel.IS_NEWLY_CREATED, false);

    if(rExists) {
      String existingTabCode = null;
      try {
        Map<String, Object> referencedTab = TabUtils.getMapFromConnectedTabNode(relationshipNode,
            Collections.singletonList(CommonConstants.CODE_PROPERTY));

        existingTabCode = (String) referencedTab.get(CommonConstants.CODE_PROPERTY);
      } catch (NotFoundException ignored) { }

      if(StringUtils.isNoneEmpty(newTabCode, existingTabCode) && !newTabCode.equals(existingTabCode)) {
        newRelationMap.put(ISaveRelationshipModel.ADDED_TAB, tabMap);
        newRelationMap.put(ISaveRelationshipModel.DELETED_TAB, existingTabCode);

      } else if (StringUtils.isEmpty(existingTabCode) && StringUtils.isNotEmpty(newTabCode)) {
        newRelationMap.put(ISaveRelationshipModel.ADDED_TAB, tabMap);

      } else if (StringUtils.isNotEmpty(existingTabCode) && StringUtils.isEmpty(newTabCode)) {
        //TODO: Handle delete tab usecase
      }
    } else {
      newRelationMap.put(CommonConstants.TAB, tabMap);
    }
  }

  private static String getStandardTabAccToRelationshipType()
  {
    return TabUtils.getStandardTabId(CommonConstants.RELATIONSHIP, VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP);
  }

  private static void prepareADMForCoupling(boolean rExists, Map<String, Object> newRelationMap,
      Map<String, Object> newSide1, Map<String, Object> newSide2, Map<String, Object> oldSide1,
      Map<String, Object> oldSide2) throws Exception
  {

    List<Map<String, Object>> side1Couplings = (List<Map<String, Object>>) newSide1.get(COUPLINGS);
    List<Map<String, Object>> side2Couplings = (List<Map<String, Object>>) newSide2.get(COUPLINGS);
    if (side1Couplings == null)
      side1Couplings = new ArrayList<>();
    if (side2Couplings == null)
      side2Couplings = new ArrayList<>();

    Map<String, Object> sideADMInfo = new HashMap<>();

    repairCouplingInfo(rExists, side1Couplings, sideADMInfo, oldSide1, SIDE1, newSide1, true);
    repairCouplingInfo(rExists, side2Couplings, sideADMInfo, oldSide2, SIDE2, newSide2, true);

    newRelationMap.putAll(sideADMInfo);
  }
  
  private static void prepareADMForNonNatureRelationshipCoupling(boolean rExists, Map<String, Object> newRelationMap,
	      Map<String, Object> newSide1, Map<String, Object> newSide2, Map<String, Object> oldSide1,
	      Map<String, Object> oldSide2) throws Exception
  {
    List<Map<String, Object>> side1Couplings = (List<Map<String, Object>>) newSide1.get(COUPLINGS);
    List<Map<String, Object>> side2Couplings = (List<Map<String, Object>>) newSide2.get(COUPLINGS);
    if (side1Couplings == null) {
      side1Couplings = new ArrayList<>();
    }
    if (side2Couplings == null) {
      side2Couplings = new ArrayList<>();
    }

    Map<String, Object> sideADMInfo = new HashMap<>();
    
    sideADMInfo.put(ADDED_ATTRIBUTES, new ArrayList<>());
    sideADMInfo.put(ADDED_TAGS, new ArrayList<>());
    sideADMInfo.put(MODIFIED_ATTRIBUTES, new ArrayList<>());
    sideADMInfo.put(MODIFIED_TAGS, new ArrayList<>());

    sideADMInfo.put(DELETED_ATTRIBUTES, new ArrayList<>());
    sideADMInfo.put(DELETED_TAGS, new ArrayList<>());

    repairCouplingInfo(rExists, side1Couplings, sideADMInfo, oldSide1, SIDE1, newSide1, false);
    repairCouplingInfo(rExists, side2Couplings, sideADMInfo, oldSide2, SIDE2, newSide2, false);
    
    newRelationMap.putAll(sideADMInfo);
  }

  private static void prepareADMForContext(boolean rExists, Map<String, Object> newSide1,
      Map<String, Object> newSide2, Map<String, Object> oldSide1, Map<String, Object> oldSide2, boolean isNature)
      throws Exception {
    String side1ContextId = (String) newSide1.remove(CONTEXT_CODE);
    String side2ContextId = (String) newSide2.remove(CONTEXT_CODE);
    
    if(StringUtils.isNotEmpty(side1ContextId))
      side1ContextId = validateContext(side1ContextId);
    if(StringUtils.isNotEmpty(side2ContextId))
    side2ContextId = validateContext(side2ContextId);

    if (rExists) {
      //For update manage added and deleted for both sides
      String oldSide1ContextId = (String) oldSide1.get(IRelationshipSide.CONTEXT_ID);
      String oldSide2ContextId = (String) oldSide2.get(IRelationshipSide.CONTEXT_ID);

      if (StringUtils.isEmpty(oldSide1ContextId) && StringUtils.isNotEmpty(side1ContextId)) {
        newSide1.put(ISaveRelationshipSide.ADDED_CONTEXT, side1ContextId);
      }
      else if (StringUtils.isNotEmpty(oldSide1ContextId) && !oldSide1ContextId.equals(side1ContextId)) {
        newSide1.put(ISaveRelationshipSide.ADDED_CONTEXT, side1ContextId);
        newSide1.put(ISaveRelationshipSide.DELETED_CONTEXT, oldSide1ContextId);
      }

      if (StringUtils.isEmpty(oldSide2ContextId) && StringUtils.isNotEmpty(side2ContextId)) {
        newSide2.put(ISaveRelationshipSide.ADDED_CONTEXT, side2ContextId);
      }
      else if (StringUtils.isNotEmpty(oldSide2ContextId) && !oldSide2ContextId.equals(side2ContextId)) {
        newSide2.put(ISaveRelationshipSide.ADDED_CONTEXT, side2ContextId);
        newSide2.put(ISaveRelationshipSide.DELETED_CONTEXT, oldSide2ContextId);
      }
    }
    else {
      //For Create
      if(isNature) {
        newSide1.put(IRelationshipSide.CONTEXT_ID, side1ContextId);
        newSide2.put(IRelationshipSide.CONTEXT_ID, side2ContextId);
      }else {
        newSide1.put(ISaveRelationshipSide.ADDED_CONTEXT, side1ContextId);
        newSide2.put(ISaveRelationshipSide.ADDED_CONTEXT, side2ContextId);
      }
      
    }
  }

  private static String validateContext(String sideContextId)
  {
    try {
      Vertex context = UtilClass.getVertexByCode(sideContextId, VertexLabelConstants.VARIANT_CONTEXT);
      String type = context.getProperty(CommonConstants.TYPE_PROPERTY);
      // allow only RELATIONSHIP VARIANT type context
      if(!CommonConstants.RELATIONSHIP_VARIANT.equals(type))
        sideContextId = null;
    }catch(Exception ignore) {
      sideContextId = null;
    }
    return sideContextId;
  }

  private static void repairCouplingInfo(boolean rExists, List<Map<String, Object>> sideCouplings,
	      Map<String, Object> sideADMInfo, Map<String, Object> oldSide, String side, Map<String, Object> newSide, boolean isNatureRelationship) throws Exception
  {
    if (rExists) {
      addCouplingInfoForModifiedRelationship(sideCouplings, oldSide, side, sideADMInfo, isNatureRelationship);
    }
    else {
      addCouplingInfoForCreateRelationship(sideCouplings, newSide, side, sideADMInfo, isNatureRelationship);
    }
      //Create/Update Relationship With Coupling
  }
  
  private static void addCouplingInfoForModifiedRelationship(List<Map<String, Object>> sideCouplings, Map<String, Object> oldSide,
      String side, Map<String, Object> sideADMInfo, boolean isNatureRelationship)
  {
    //Update Relationship Coupling
    List<Map<String, Object>> attributes = (List<Map<String, Object>>) oldSide.get(
        IRelationshipSide.ATTRIBUTES);
    List<Map<String, Object>> tags = (List<Map<String, Object>>) oldSide.get(
        IRelationshipSide.TAGS);
    List<Map<String, Object>> relationships = (List<Map<String, Object>>) oldSide.get(
        IRelationshipSide.RELATIONSHIPS);

    List<Object> addedAttributes = (List<Object>) sideADMInfo.computeIfAbsent(ADDED_ATTRIBUTES, k -> new ArrayList<>());
    List<Object> modifiedAttributes = (List<Object>) sideADMInfo.computeIfAbsent(MODIFIED_ATTRIBUTES, k -> new ArrayList<>());
    List<Object> deletedAttributes = (List<Object>) sideADMInfo.computeIfAbsent(DELETED_ATTRIBUTES, k -> new ArrayList<>());

    List<Object> addedTags = (List<Object>) sideADMInfo.computeIfAbsent(ADDED_TAGS, k -> new ArrayList<>());
    List<Object> modifiedTags = (List<Object>) sideADMInfo.computeIfAbsent(MODIFIED_TAGS, k -> new ArrayList<>());
    List<Object> deletedTags = (List<Object>) sideADMInfo.computeIfAbsent(DELETED_TAGS, k -> new ArrayList<>());

    List<Object> addedRelationships = null;
    List<Object> modifiedRelationships = null;
    List<Object> deletedRelationships = null;
    if (isNatureRelationship) {
      addedRelationships = (List<Object>) sideADMInfo.computeIfAbsent(
        IModifiedNatureRelationshipModel.ADDED_RELATIONSHIP_INHERITANCE, k -> new ArrayList<>());
      modifiedRelationships = (List<Object>) sideADMInfo.computeIfAbsent(
        IModifiedNatureRelationshipModel.MODIFIED_RELATIONSHIP_INHERITANCE, k -> new ArrayList<>());
      deletedRelationships = (List<Object>) sideADMInfo.computeIfAbsent(
        IModifiedNatureRelationshipModel.DELETED_RELATIONSHIP_INHERITANCE, k -> new ArrayList<>());
    }

    Set<String> existingAttributeIds = Stream.of(attributes)
        .flatMap(Collection::stream)
        .map(x -> (String) x.get(CommonConstants.ID_PROPERTY))
        .collect(Collectors.toSet());

    Set<String> existingTagIds = Stream.of(tags)
        .flatMap(Collection::stream)
        .map(x -> (String) x.get(CommonConstants.ID_PROPERTY))
        .collect(Collectors.toSet());

    Set<String> existingRelationshipIds = null;
    if (isNatureRelationship) {
      existingRelationshipIds = Stream.of(relationships)
        .flatMap(Collection::stream)
        .map(x -> (String) x.get(CommonConstants.ID_PROPERTY))
        .collect(Collectors.toSet());
    }

    //Create/Update Relationship With Coupling
    for (Map<String, Object> sideCoupling : sideCouplings) {
      sideCoupling.put(SIDE, side);

      String id = (String) sideCoupling.get(CommonConstants.ID_PROPERTY);

      String elementType = (String) sideCoupling.get(TYPE);

      switch (elementType) {
        case ATTRIBUTE:
          if (existingAttributeIds.contains(id)) {
            modifiedAttributes.add(sideCoupling);
            existingAttributeIds.remove(id);
          }
          else {
            validateProperty(addedAttributes, sideCoupling, id, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
          }
          break;
        case TAG:
          if (existingTagIds.contains(id)) {
            modifiedTags.add(sideCoupling);
            existingTagIds.remove(id);
          }
          else {
            validateProperty(addedTags, sideCoupling, id, VertexLabelConstants.ENTITY_TAG);
          }
          break;
        case RELATIONSHIP:
          if (isNatureRelationship) {
            if (existingRelationshipIds.contains(id)) {
              modifiedRelationships.add(sideCoupling);
              existingRelationshipIds.remove(id);
            }
            else {
              if (removeDuplicateRelationship(addedRelationships, id)) {
                validateProperty(addedRelationships, sideCoupling, id, VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP);
              }
            }
    	  }
          break;
      }
    }
    deletedAttributes.addAll(existingAttributeIds);
    deletedTags.addAll(existingTagIds);
    if (isNatureRelationship) {
      deletedRelationships.addAll(existingRelationshipIds);
    }
  }

  private static void addCouplingInfoForCreateRelationship(List<Map<String, Object>> sideCouplings, Map<String,
		  Object> newSide, String side, Map<String, Object> sideADMInfo, boolean isNatureRelationship)
      throws Exception
  {
    List<Map<String, Object>> attributes = (List<Map<String, Object>>) newSide.get(IRelationshipSide.ATTRIBUTES);
    List<Map<String, Object>> tags = (List<Map<String, Object>>) newSide.get(IRelationshipSide.TAGS);
    List<Map<String, Object>> relationships = (List<Map<String, Object>>) newSide.get(IRelationshipSide.RELATIONSHIPS);
    
    List<Object> addedAttributes = null;
    List<Object> addedTags = null;
    List<Object> addedRelationships = null;
    addedAttributes = (List<Object>) sideADMInfo.computeIfAbsent(ADDED_ATTRIBUTES, k -> new ArrayList<>());
    addedTags = (List<Object>) sideADMInfo.computeIfAbsent(ADDED_TAGS, k -> new ArrayList<>());
    if (isNatureRelationship)
      addedRelationships = (List<Object>) sideADMInfo.computeIfAbsent(IModifiedNatureRelationshipModel.ADDED_RELATIONSHIP_INHERITANCE,
          k -> new ArrayList<>());
    
    for (Map<String, Object> sideCoupling : sideCouplings) {
      sideCoupling.put(SIDE, side);
      Map<String, Object> couplingInfo = new HashMap<>();
      String id = (String) sideCoupling.get(CommonConstants.ID_PROPERTY);
      couplingInfo.put(CommonConstants.ID_PROPERTY, id) ;
      String elementType = (String) sideCoupling.get(TYPE);
      couplingInfo.put(TYPE, elementType);
      couplingInfo.put(couplingType.name(), sideCoupling.get(couplingType.name()));
      
      switch (elementType) {
        case CommonConstants.ATTRIBUTE:
          Vertex attribute = UtilClass.getVertexByCode(id, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
          if (List.of(CommonConstants.CONCATENATED_ATTRIBUTE_TYPE, CommonConstants.CALCULATED_ATTRIBUTE_TYPE)
              .contains(attribute.getProperty(IAttribute.TYPE))) {
            throw new AttributeMappingsException("Attribute of Calculated And Concatenated Type should not be flowed");
          }
          attributes.add(couplingInfo);
          validateProperty(addedAttributes, sideCoupling, id, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
          break;
        case CommonConstants.TAG:
          validateProperty(addedTags, sideCoupling, id, VertexLabelConstants.ENTITY_TAG);
          tags.add(couplingInfo);
          break;

        case CommonConstants.RELATIONSHIP:
          if(isNatureRelationship) {
            if(removeDuplicateRelationship(addedRelationships, id)) {
              validateProperty(addedRelationships, sideCoupling, id, VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP);
              relationships.add(couplingInfo);
            }
          }
          break;
      }
    }
  }


  private static void validateProperty(List<Object> addedProperty, Map<String, Object> sideCoupling, String id, String entityType)
  {
    try {
      UtilClass.getVertexByCode(id, entityType);
      addedProperty.add(sideCoupling);
    }
    catch (Exception ignore) {
      
    }
  }
  
  private static boolean removeDuplicateRelationship(List<Object> addedRelationship, String id)
  {
    for(Object relation : addedRelationship) {
      Map<String, Object> r = (Map<String, Object>) relation;
      String relationID = (String) r.get(CommonConstants.ID_PROPERTY);
      if(id.equals(relationID)){
        return false;
      }
    }
    return true;
  }
}
