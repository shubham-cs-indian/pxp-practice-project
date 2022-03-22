package com.cs.config.strategy.plugin.usecase.configdetails.instancetree;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.configdetails.IGetPostConfigDetailsRequestModel;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IGetPostConfigDetailsForNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.instancetree.IReferencedPropertyModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public class GetPostConfigDetailsForNewInstanceTree extends AbstractOrientPlugin {
  
  public GetPostConfigDetailsForNewInstanceTree(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] {"POST|GetPostConfigDetailsForNewInstanceTree/*"};
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    UtilClass.setGraph(graph);
    List<String> attributeIds = (List<String>) requestMap.get(IGetPostConfigDetailsRequestModel.ATTRIBUTE_IDS);
    List<String> tagIds = (List<String>) requestMap.get(IGetPostConfigDetailsRequestModel.TAG_IDS);
    List<String> klassIds = (List<String>) requestMap.get(IGetPostConfigDetailsRequestModel.KLASS_IDS);
    Boolean shouldGetNonNature = (Boolean) requestMap.get(IGetPostConfigDetailsRequestModel.SHOULD_GET_NON_NATURE);
    Map<String, Object> attributesMap = new HashMap<>();
    Map<String, Object> tagsMap = new HashMap<>();
    Map<String, Object> klassInfoMap = new HashMap<>();
    Iterator<Vertex> iterator = null;
    
    //fetch attributes data
    iterator = UtilClass.getVerticesByIndexedIds(attributeIds, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE).iterator();
    while (iterator.hasNext()) {
      Vertex attributeNode = iterator.next();
      attributesMap.put(UtilClass.getCId(attributeNode) ,UtilClass.getMapFromVertex(Arrays.asList(IReferencedPropertyModel.ID, 
          IReferencedPropertyModel.CODE, IReferencedPropertyModel.CHILDREN, IReferencedPropertyModel.TYPE, IAttribute.LABEL, IAttribute.ICON), attributeNode));
    }
    
    //fetch tags data
    iterator = UtilClass.getVerticesByIndexedIds(tagIds, VertexLabelConstants.ENTITY_TAG).iterator();
    while (iterator.hasNext()) {
      Vertex tagNode = iterator.next();
      tagsMap.put(UtilClass.getCId(tagNode) ,UtilClass.getMapFromVertex(Arrays.asList(IReferencedPropertyModel.ID, 
          IReferencedPropertyModel.CODE, IReferencedPropertyModel.CHILDREN, IReferencedPropertyModel.TYPE, ITag.ICON), tagNode));
    }
    
    //fetch klasses data
    fillKlassInfoForKlassIds(klassIds, shouldGetNonNature, klassInfoMap);
    
    String userId = (String) requestMap.get(IGetPostConfigDetailsRequestModel.USER_ID);
    Vertex roleNode = RoleUtils.getRoleFromUser(userId);
    Map<String, Boolean> functionPermission = GlobalPermissionUtils.getFunctionPermission(roleNode);
    
    Map<String, Object> response = new HashMap<>();
    response.put(IGetPostConfigDetailsForNewInstanceTreeModel.REFERENCED_ATTRIBUTES, attributesMap);
    response.put(IGetPostConfigDetailsForNewInstanceTreeModel.REFERENCED_TAGS, tagsMap);
    response.put(IGetPostConfigDetailsForNewInstanceTreeModel.REFERENCED_KLASSES, klassInfoMap);
    response.put(IGetPostConfigDetailsForNewInstanceTreeModel.FUNCTION_PERMISSION, functionPermission);
    
    return response;
  }

  protected void fillKlassInfoForKlassIds(List<String> klassIds, Boolean shouldGetNonNature,
      Map<String, Object> klassInfoMap) throws Exception
  {
    KlassGetUtils.fillKlassInfoForKlassIds(klassIds, klassInfoMap, shouldGetNonNature, false);
  }

  }
