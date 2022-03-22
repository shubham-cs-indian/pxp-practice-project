package com.cs.config.strategy.plugin.usecase.base.klass;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.klass.IGetKlassModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.storage.ORecordDuplicatedException;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.orientechnologies.orient.server.network.protocol.http.OHttpRequest;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class AbstractGetKlassWithLinkedKlasses extends AbstractOrientPlugin {
  
  public AbstractGetKlassWithLinkedKlasses(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  public abstract String getVertexLabel();
  
  protected void initializeGraph(OHttpRequest iRequest) throws Exception
  {
    ODatabaseDocumentTx database = (ODatabaseDocumentTx) getProfiledDatabaseInstance(iRequest);
    OrientGraph graph = new OrientGraph(database);
    UtilClass.setGraph(graph);
  }
  
  protected Map<String, Object> executeInternal(Map<String, Object> requestMap) throws Exception
  {
    String id = (String) requestMap.get(IIdParameterModel.ID);
    String processInstanceId = (String) requestMap
        .get(IMulticlassificationRequestModel.PROCESS_INSTANCE_ID);
    
    Map<String, Object> klassMap = new HashMap<>();
    if (processInstanceId != null) {
      klassMap = getKlassWithLinkedKlassFromCacheIfAvailable(requestMap, id, processInstanceId);
    }
    else {
      klassMap = getklassWithLinkedklasses(requestMap, id);
    }
    return klassMap;
  }
  
  private Map<String, Object> getKlassWithLinkedKlassFromCacheIfAvailable(
      Map<String, Object> requestMap, String id, String processInstanceId) throws Exception
  {
    
    Map<String, Object> klassMap = null;
    String configDetailCacheVertexId = UtilClass.generateUniqueId(processInstanceId, "_", id,
        "_GetKlassWithLinkedKlasses");
    
    try {
      Vertex vertex = UtilClass.getVertexByIndexedId(configDetailCacheVertexId,
          VertexLabelConstants.CONFIG_DETAIL_CACHE);
      klassMap = UtilClass.getMapFromNode(vertex);
      klassMap.remove(CommonConstants.ID_PROPERTY);
      klassMap.remove(CommonConstants.PROCESS_INSTANCE_ID);
      klassMap.remove(CommonConstants.VERSION_ID);
      klassMap.remove(CommonConstants.CODE_PROPERTY);
    }
    catch (NotFoundException e) {
      klassMap = getklassWithLinkedklasses(requestMap, id);
      
      Map<String, Object> mapToSave = new HashMap<>(klassMap);
      
      mapToSave.put(CommonConstants.ID_PROPERTY, configDetailCacheVertexId);
      mapToSave.put(CommonConstants.PROCESS_INSTANCE_ID, processInstanceId);
      
      OrientVertexType configDetailCacheVertexType = UtilClass.getOrCreateVertexType(
          VertexLabelConstants.CONFIG_DETAIL_CACHE, CommonConstants.CODE_PROPERTY);
      
      try {
        UtilClass.createNode(mapToSave, configDetailCacheVertexType, new ArrayList<>());
        UtilClass.getGraph()
            .commit();
      }
      catch (ORecordDuplicatedException ex) {
      }
    }
    return klassMap;
  }
  
  private Map<String, Object> getklassWithLinkedklasses(Map<String, Object> requestMap, String id)
      throws NotFoundException, Exception
  {
    String endpointId = (String) requestMap.get(IMulticlassificationRequestModel.ENDPOINT_ID);
    String organizationId = (String) requestMap
        .get(IMulticlassificationRequestModel.ORAGANIZATION_ID);
    String physicalCatalogId = (String) requestMap
        .get(IMulticlassificationRequestModel.PHYSICAL_CATALOG_ID);
    
    UtilClass.setReferencedKlassIdStructureMapping(new HashMap<String, Object>());
    Iterator<Vertex> iterator = UtilClass.getGraph()
        .getVertices(getVertexLabel(), new String[] { CommonConstants.CODE_PROPERTY },
            new Object[] { id })
        .iterator();
    Vertex klassNode = null;
    Map<String, Object> klassMap = null;
    if (!iterator.hasNext()) {
      throw new NotFoundException();
    }
    while (iterator.hasNext()) {
      klassNode = iterator.next();
      klassMap = KlassUtils.getKlassEntityReferencesMap(klassNode, true);
      
      klassMap.put(IGetKlassModel.DATARULES_OF_KLASS,
          KlassUtils.getDataRulesOfKlass(klassNode, endpointId, organizationId, physicalCatalogId));
      
      List<Map<String, Object>> variantsList = new ArrayList<>();
      KlassGetUtils.fillTechnicalImageVariantWithAutoCreateEnabled(klassNode, variantsList);
      klassMap.put(IGetKlassModel.TECHNICAL_IMAGEVARIANT_CONTEXT_WITH_AUTOCREATEENABLE,
          variantsList);
      
      // fillKlassTypeSpecificDetails((Map<String, Object>)
      // klassMap.get(IGetKlassModel.KLASS),
      // klassNode);
    }
    return klassMap;
  }
  
  /*
  protected void fillKlassTypeSpecificDetails(Map<String, Object> klassMap, Vertex klassNode)
  {
    // TODO Auto-generated method stub
  
  }
  */
}
