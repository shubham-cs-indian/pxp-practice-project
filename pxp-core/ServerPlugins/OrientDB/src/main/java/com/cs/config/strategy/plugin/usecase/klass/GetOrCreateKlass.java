package com.cs.config.strategy.plugin.usecase.klass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.klass.util.CreateKlassUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.SaveKlassUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.util.ValidationUtils;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.exception.validationontype.InvalidNatureTypeKlassException;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.exception.InvalidTypeException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class GetOrCreateKlass extends AbstractOrientPlugin {
  
  public GetOrCreateKlass(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    List<Map<String, Object>> klassMaplist = new ArrayList<>();
    List<Map<String, Object>> finalReturnlist = new ArrayList<>();
    klassMaplist = (List<Map<String, Object>>) map.get(IListModel.LIST);
    OrientGraph graph = UtilClass.getGraph();
    
    for (int i = 0; i < klassMaplist.size(); i++) {
      Map<String, Object> klassmap = (HashMap<String, Object>) klassMaplist.get(i);
      if (ValidationUtils.validateKlassInfo(klassmap)) {
        
        String klassId = (String) klassmap.get(IKlass.ID);
        try {
          UtilClass.getVertexByIndexedId(klassId, VertexLabelConstants.ENTITY_TYPE_KLASS);
          continue;
        }
        catch (NotFoundException e) {
          
        }
        
        String type = (String) klassmap.get(IKlass.NATURE_TYPE);
        OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
            VertexLabelConstants.ENTITY_TYPE_KLASS, CommonConstants.CODE_PROPERTY);
        try {
          UtilClass.validateOnType(Constants.NATURE_TYPES_LIST_FOR_KLASS, type, true);
        }
        catch (InvalidTypeException e) {
          throw new InvalidNatureTypeKlassException(e);
        }
        Vertex klassNode = createKlassNode(klassmap, vertexType, fieldsToExclude);
        CreateKlassUtils.inheritTreeTypeOption(klassmap, klassNode,
            VertexLabelConstants.ENTITY_TYPE_KLASS);
        Map<String, Object> parentKlassMap = (Map<String, Object>) klassmap.get(IKlass.PARENT);
        String parentId = parentKlassMap.get(IKlass.ID)
            .toString();
        
        KlassUtils.createTaxonomy(klassmap, klassNode, CommonConstants.ENTITY_KLASS);
        KlassUtils.createSectionNodes(klassmap, klassNode, VertexLabelConstants.ENTITY_TYPE_KLASS);
        
        boolean isInheriting = !parentId.equals("-1"); // &&
                                                       // !isParentStandardKlass;
        KlassUtils.createParentChildLink(klassNode, CommonConstants.ENTITY_KLASS, klassmap,
            isInheriting);
        
        List<String> statusTagIds = new ArrayList<>();
        statusTagIds.add(SystemLevelIds.LIFE_STATUS_TAG_ID);
        statusTagIds.add(SystemLevelIds.LISTING_STATUS_TAG_ID);
        SaveKlassUtil.addLifeCycleStatusTag(statusTagIds, klassNode);
        
        List<Vertex> klassAndChildNodes = new ArrayList<Vertex>();
        klassAndChildNodes.add(klassNode);
        
        graph.commit();
        Map<String, Object> returnKlassMap = (HashMap<String, Object>) KlassUtils
            .getKlassEntityMap(klassNode);
        finalReturnlist.add(returnKlassMap);
      }
    }
    return finalReturnlist;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetOrCreateKlass/*" };
  }
  
  private Vertex createKlassNode(Map<String, Object> klassMap, OrientVertexType vertexType,
      List<String> fieldsToExclude) throws Exception
  {
    Vertex klassNode = null;
    if (klassMap.get(IKlass.ID) != null) {
      String klassId = klassMap.get(IKlass.ID)
          .toString();
      klassNode = UtilClass.createNode(klassMap, vertexType, fieldsToExclude);
      klassNode.setProperty(CommonConstants.CODE_PROPERTY, klassId);
    }
    else {
      klassNode = UtilClass.createNode(klassMap, vertexType, fieldsToExclude);
    }
    return klassNode;
  }
  
  List<String> fieldsToExclude = Arrays.asList("referencedClassIds", "validator",
      "referencedAttributeIds", "permission", "deletedStructures", "notificationSettings",
      "structureChildren", "children", "klassViewSetting", "modifiedStructures", "addedStructures",
      "isEnforcedTaxonomy", "parent", CommonConstants.TREE_TYPE_OPTION_PROPERTY, "referencedTags");
}
