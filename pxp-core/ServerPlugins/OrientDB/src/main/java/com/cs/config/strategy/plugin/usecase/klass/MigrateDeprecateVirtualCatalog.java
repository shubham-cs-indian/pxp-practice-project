package com.cs.config.strategy.plugin.usecase.klass;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.migration.IMigrateDeprecateVirtualCatalogModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class MigrateDeprecateVirtualCatalog extends AbstractOrientPlugin {
  
  public static final String       CLASSIFIER_IID         = "classifierIID";
  public static final List<String> fieldsToFetch          = Arrays.asList(CommonConstants.ID_PROPERTY, CLASSIFIER_IID);
  public static final String       getVirtualCatalogQuery = "select * from RootKlass where type = 'com.cs.core.config.interactor.entity.virtualcatalog.VirtualCatalog'";
  
  public MigrateDeprecateVirtualCatalog(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|MigrateDeprecateVirtualCatalog/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Set<String> deletedIds = new HashSet<>();
    Set<Long> deletedIIDs = new HashSet<>();
    HashMap<String, Object> response = new HashMap<>();
    
    Iterable<Vertex> virtualCatalogNodes = graph.command(new OCommandSQL(getVirtualCatalogQuery)).execute();
    for (Vertex virtualCatalogNode : virtualCatalogNodes) {
      if (virtualCatalogNode != null) {
        Map<String, Object> klassMap = UtilClass.getMapFromVertex(fieldsToFetch, virtualCatalogNode);
        deletedIds.add((String) klassMap.get(CommonConstants.ID_PROPERTY));
        deletedIIDs.add((Long) klassMap.get(CLASSIFIER_IID));
        virtualCatalogNode.remove();
      }
    }
    response.put(IMigrateDeprecateVirtualCatalogModel.KLASS_IDS, deletedIds);
    response.put(IMigrateDeprecateVirtualCatalogModel.KLASS_IIDS, deletedIIDs);
    
    // Translation vertices to be removed
    List<String> translationCodesToBeRemoved = Arrays.asList("VIRTUAL_CATALOG_CREATED_SUCCESSFULLY", "VIRTUAL_CATALOG_NOT_FOUND",
        "VIRTUALCATALOG", "VirtualCatalogInstanceNotFoundException", "UserNotHaveDeletePermissionForVirtualCatalog",
        "VIRTUAL_CATALOG_TAB", "VIRTUAL_CATALOG", "GO_TO_VIRTUAL_CATALOG");
    Iterable<Vertex> verticesToBeRemoved = UtilClass.getVerticesByIds(translationCodesToBeRemoved, VertexLabelConstants.UI_TRANSLATIONS);
    for (Vertex vertexToBeRemoved : verticesToBeRemoved) {
      vertexToBeRemoved.remove();
    }
    
    graph.commit();
    return response;
  }
}
