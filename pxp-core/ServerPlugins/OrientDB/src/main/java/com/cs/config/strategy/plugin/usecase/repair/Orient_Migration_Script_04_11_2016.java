/*
 * Add taxonomy attribute to the standard klasses.
 */

package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.imprt.config.strategy.plugin.usecase.importklass.util.ImportKlassUtils;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Orient_Migration_Script_04_11_2016 extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_04_11_2016(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Vertex klassNode = null;
    List<String> klassIds = new ArrayList<>();
    klassIds.add("article");
    String sectionId = UtilClass.getUniqueSequenceId(null);
    for (String klassId : klassIds) {
      try {
        klassNode = UtilClass.getVertexByIndexedId(klassId,
            VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
      String rid = (String) klassNode.getId()
          .toString();
      Iterable<Vertex> i = UtilClass.getGraph()
          .command(new OCommandSQL(
              "select from(traverse in('Child_Of') from " + rid + " strategy BREADTH_FIRST)"))
          .execute();
      List<Vertex> klassAndChildNodes = new ArrayList<>();
      for (Vertex node : i) {
        klassAndChildNodes.add(node);
      }
      
      List<HashMap<String, Object>> sectionList = new ArrayList<>();
      HashMap<String, Object> getKlassMap = getKlass(klassNode);
      HashMap<String, Object> permissions = (HashMap<String, Object>) getKlassMap
          .get(CommonConstants.PERMISSION_PROPERTY);
      HashMap<String, Object> rolePermissions = (HashMap<String, Object>) permissions
          .get(CommonConstants.ROLE_PERMISSION_PROPERTY);
      HashMap<String, Object> rolePermissionADM = new HashMap<>();
      HashMap<String, Object> rolePermission = new HashMap<>();
      Iterator<String> roleKeySet = rolePermissions.keySet()
          .iterator();
      String roleId = null;
      while (roleKeySet.hasNext()) {
        roleId = roleKeySet.next();
        rolePermission = (HashMap<String, Object>) rolePermissions.get(roleId);
        HashMap<String, Object> sectionPermissions = (HashMap<String, Object>) rolePermission
            .get(CommonConstants.SECTION_PERMISSION_PROPERTY);
        sectionPermissions.put(sectionId, getSectionPermissionMap());
        rolePermission.put(CommonConstants.SECTION_PERMISSION_PROPERTY, sectionPermissions);
        rolePermissionADM.put(roleId, rolePermission);
      }
      HashMap<String, Object> addedSection = new HashMap<>();
      addedSection.put("id", sectionId);
      addedSection.put("label", "Taxonomy");
      addedSection.put("icon", null);
      addedSection.put("rows", 1);
      addedSection.put("columns", 1);
      addedSection.put("isCollapsed", false);
      addedSection.put("versionId", null);
      addedSection.put("versionTimestamp", null);
      addedSection.put("lastModifiedBy", null);
      
      addedSection.put("sequence",
          ((List<HashMap<String, Object>>) getKlassMap.get(CommonConstants.SECTIONS_PROPERTY))
              .size());
      addedSection.put("type", "com.cs.config.interactor.entity.Section");
      
      HashMap<String, Object> addedSectionElement = new HashMap<>();
      addedSectionElement.put("type", CommonConstants.ATTRIBUTE_PROPERTY);
      
      HashMap<String, Integer> position = new HashMap<>();
      position.put("x", 0);
      position.put("y", 0);
      
      addedSectionElement.put("startPosition", position);
      addedSectionElement.put("endPosition", position);
      addedSectionElement.put("description", null);
      addedSectionElement.put("tooltip", null);
      addedSectionElement.put("label", "Taxonomy");
      addedSectionElement.put("isMandatory", false);
      addedSectionElement.put("isInherited", null);
      addedSectionElement.put("numberOfVersionsAllowed", 0);
      addedSectionElement.put("placeholder", null);
      addedSectionElement.put("defaultValue", null);
      addedSectionElement.put("tagGroups", new ArrayList<>());
      addedSectionElement.put("attribute", getAttributeData());
      
      List<HashMap<String, Object>> sectionElements = new ArrayList<>();
      sectionElements.add(addedSectionElement);
      
      addedSection.put("elements", sectionElements);
      
      sectionList.add(addedSection);
      List<String> addedSectionIds = new ArrayList<>();
      addedSectionIds.add(sectionId);
      KlassUtils.createAndLinkSectionsToKlass(klassNode, sectionList, klassAndChildNodes,
          addedSectionIds, new ArrayList<>(), null, new ArrayList<String>());
      UtilClass.setSectionIdMap(new HashMap<String, String>());
      UtilClass.setSectionPermissionIdMap(new HashMap<String, String>());
      HashMap<String, Object> klassADM = new HashMap<>();
      getKlassMap.remove(CommonConstants.SECTIONS_PROPERTY);
      getKlassMap.put(CommonConstants.ROLE_PERMISSION_PROPERTY, rolePermissionADM);
      klassADM = getKlassMap;
      // KlassUtils.manageSectionPermissions(klassNode, klassAndChildNodes,
      // klassADM);
    }
    
    UtilClass.getGraph()
        .commit();
    HashMap<String, Object> response = new HashMap<>();
    response.put("status", "SUCCESS");
    return response;
  }
  
  private HashMap<String, Object> getAttributeData()
  {
    HashMap<String, Object> attributeMap = new HashMap<>();
    attributeMap.put("id", "taxonomyattribute");
    attributeMap.put("versionId", null);
    attributeMap.put("versionTimestamp", null);
    attributeMap.put("lastModifiedBy", null);
    attributeMap.put("type",
        "com.cs.config.interactor.entity.concrete.attribute.standard.TaxonomyAttribute");
    attributeMap.put("defaultValue", null);
    attributeMap.put("forMam", true);
    attributeMap.put("mappedTo", null);
    attributeMap.put("isDisabled", null);
    attributeMap.put("forTarget", true);
    attributeMap.put("forEditorial", true);
    attributeMap.put("forPim", true);
    attributeMap.put("description", null);
    attributeMap.put("isStandard", true);
    attributeMap.put("tooltip", null);
    attributeMap.put("placeholder", null);
    attributeMap.put("isMandatory", null);
    attributeMap.put("icon", null);
    attributeMap.put("label", "Taxonomy");
    List<String> klassType = new ArrayList<>();
    klassType.add("com.cs.config.interactor.entity.concrete.klass.ProjectKlass");
    klassType.add("com.cs.config.interactor.entity.concrete.klass.Task");
    klassType.add("com.cs.config.interactor.entity.concrete.klass.Asset");
    klassType.add("com.cs.config.interactor.entity.concrete.klass.Market");
    attributeMap.put("klassType", klassType);
    return attributeMap;
  }
  
  // @SuppressWarnings("unchecked")
  public static HashMap<String, Object> getKlass(Vertex klassNode) throws Exception
  {
    String type = klassNode.getProperty(CommonConstants.TYPE_PROPERTY);
    // HashMap<String, Object> getKlassMap = new HashMap<>();
    switch (type) {
      case CommonConstants.PROJECT_KLASS_TYPE:
      case CommonConstants.PROJECT_SET_KLASS_TYPE:
      case CommonConstants.MARKET_KLASS_TYPE:
      case CommonConstants.ASSET_KLASS_TYPE:
      case CommonConstants.MASTER_IMPORT_ARTICLE_KLASS_TYPE:
      case CommonConstants.IMPORT_SYSTEM_FILE_ARTICLE_KLASS_TYPE:
      case CommonConstants.IMPORT_SYSTEM_INSTANCE_KLASS_TYPE:
      case CommonConstants.IMPORT_SYSTEM_FILE_KLASS_TYPE:
      case CommonConstants.IMPORT_ARTICLE_KLASS_TYPE:
        return (HashMap<String, Object>) ImportKlassUtils.getKlassEntityMap(klassNode);
    }
    
    return null;
  }
  
  private static Map<String, Object> getSectionPermissionMap()
  {
    Map<String, Object> sectionPermissionNode = new HashMap<>();
    sectionPermissionNode.put(CommonConstants.IS_HIDDEN_PROPERTY, false);
    sectionPermissionNode.put(CommonConstants.IS_COLLAPSED_PROPERTY, false);
    sectionPermissionNode.put(CommonConstants.DISABLED_ELEMENTS_PROPERTY, Collections.emptyList());
    return sectionPermissionNode;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_04_11_2016/*" };
  }
}
