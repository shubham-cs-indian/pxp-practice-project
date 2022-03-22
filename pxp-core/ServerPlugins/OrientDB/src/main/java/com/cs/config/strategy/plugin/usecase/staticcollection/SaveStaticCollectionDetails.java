package com.cs.config.strategy.plugin.usecase.staticcollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.klass.util.SaveKlassUtil;
import com.cs.config.strategy.plugin.usecase.staticcollection.util.StaticCollectionUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.exception.staticcollection.CollectionNodeNotFoundException;
import com.cs.core.config.interactor.model.klass.IRemoveAttributeVariantContextModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.collections.IGetStaticCollectionInfoModel;
import com.cs.core.runtime.interactor.model.collections.ISaveStaticCollectionDetailsModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public class SaveStaticCollectionDetails extends AbstractOrientPlugin {
  
  public SaveStaticCollectionDetails(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveStaticCollectionDetails/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    UtilClass.setNodesForVersionIncrement(new HashSet<>());
    UtilClass.setSectionElementIdMap(new HashMap<>());
    UtilClass.setSectionIdMap(new HashMap<>());
    UtilClass.setSectionPermissionIdMap(new HashMap<>());
    Map<String, Object> saveCollectionMap = (Map<String, Object>) requestMap.get("collection");
    
    String collectionId = (String) saveCollectionMap.get(ISaveStaticCollectionDetailsModel.ID);
    Vertex collectionNode = null;
    try {
      collectionNode = UtilClass.getVertexById(collectionId, VertexLabelConstants.COLLECTION);
    }
    catch (NotFoundException e) {
      throw new CollectionNodeNotFoundException();
    }
    
    String rid = (String) collectionNode.getId()
        .toString();
    Iterable<Vertex> i = graph
        .command(new OCommandSQL(
            "select from(traverse in('" + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF
                + "') from " + rid + " strategy BREADTH_FIRST)"))
        .execute();
    List<Vertex> klassAndChildNodes = new ArrayList<>();
    for (Vertex node : i) {
      klassAndChildNodes.add(node);
    }
    Map<String, Object> removedAttributeVariantContexts = new HashMap<>();
    removedAttributeVariantContexts.put(IRemoveAttributeVariantContextModel.REMOVED_ATTRIBUTE_ID_VS_CONTEXT_IDS, new HashMap<String, List<String>>());
    removedAttributeVariantContexts.put(IRemoveAttributeVariantContextModel.CHANGED_CLASSIFIERS_FOR_ATTRIBUTE_CONTEXTS, new ArrayList<String>());
    
    List<Map<String, Object>> defaultValueChangeList = new ArrayList<>();
    Map<String, List<String>> deletedPropertyMap = new HashMap<>();
    SaveKlassUtil.manageSectionsInKlass(collectionNode, saveCollectionMap, klassAndChildNodes,
        VertexLabelConstants.COLLECTION, defaultValueChangeList, deletedPropertyMap,
        new HashMap<>(), removedAttributeVariantContexts, new ArrayList<Long>(), new ArrayList<Long>(), new ArrayList<Long>(), new ArrayList<String>());
    
    graph.commit();
    
    Map<String, Object> returnMap = new HashMap<>();
    
    StaticCollectionUtils.getStaticCollectionDetailsMap(collectionNode, returnMap);
    returnMap.put(IGetStaticCollectionInfoModel.DEFAULT_VALUES_DIFF, defaultValueChangeList);
    returnMap.put(IGetStaticCollectionInfoModel.DELETED_PROPERTIES_FROM_SOURCE, deletedPropertyMap);
    return returnMap;
  }
}
