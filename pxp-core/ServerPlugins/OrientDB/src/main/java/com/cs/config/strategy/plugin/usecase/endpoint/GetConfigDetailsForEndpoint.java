package com.cs.config.strategy.plugin.usecase.endpoint;

import com.cs.config.strategy.plugin.usecase.processevent.util.ProcessEventUtils;
import com.cs.config.strategy.plugin.usecase.util.IdentifierAttributeUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.processevent.IProcessEvent;
import com.cs.core.config.interactor.model.endpoint.IGetConfigDetailsForEndpointModel;
import com.cs.core.config.interactor.model.endpoint.IGetConfigDetailsForExportRequestModel;
import com.cs.core.config.interactor.model.processevent.IGetProcessEndpointEventModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.templating.IConfigDetailsForDataTransferModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractConfigDetails;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GetConfigDetailsForEndpoint extends AbstractConfigDetails {
  
  public GetConfigDetailsForEndpoint(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForEndpoint/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> processToReturn = new HashMap<>();
    String endpointId = (String) requestMap.get(IGetConfigDetailsForExportRequestModel.ENDPOINT_ID);
    String organizationId = (String) requestMap
        .get(IGetConfigDetailsForExportRequestModel.ORAGANIZATION_ID);
    String physicalCatalogId = (String) requestMap
        .get(IGetConfigDetailsForExportRequestModel.PHYSICAL_CATALOG_ID);
    
    Map<String, Object> process = new HashMap<>();
    String systemId = null;
    
    Vertex endpointVertex = UtilClass.getVertexById(endpointId, VertexLabelConstants.ENDPOINT);
    Iterator<Vertex> processesIterator = endpointVertex
        .getVertices(Direction.OUT, RelationshipLabelConstants.PROFILE_PROCESS_LINK)
        .iterator();
    
    if (processesIterator.hasNext()) {
      process = getProcesses(processesIterator.next(), new String());
      Iterator<Vertex> systemNodeIterator = endpointVertex
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_SYSTEM)
          .iterator();
      if (systemNodeIterator.hasNext()) {
        Vertex systemVertex = systemNodeIterator.next();
        systemId = systemVertex.getProperty(CommonConstants.CODE_PROPERTY);
      }
    }
    
    List<String> klassIds = (List<String>) requestMap
        .get(IGetConfigDetailsForExportRequestModel.KLASS_IDS);
    List<String> taxonomyIds = (List<String>) requestMap
        .get(IGetConfigDetailsForExportRequestModel.TAXONOMY_IDS);
    processToReturn.put(IGetConfigDetailsModel.TYPEID_IDENTIFIER_ATTRIBUTEIDS, new HashMap<>());
    IdentifierAttributeUtils.fillIdentifierAttributesForKlasses(processToReturn, klassIds);
    IdentifierAttributeUtils.fillIdentifierAttributesForTaxonomies(processToReturn, taxonomyIds);
    
    processToReturn.put(IGetConfigDetailsForEndpointModel.PROCESS_FLOW,
        process.get(IGetProcessEndpointEventModel.PROCESS_FLOW));
    processToReturn.put(IGetConfigDetailsForEndpointModel.ENDPOINTID, endpointId);
    processToReturn.put(IGetConfigDetailsForEndpointModel.SYSTEMID, systemId);
    processToReturn.put(IGetConfigDetailsForEndpointModel.ID,
        process.get(CommonConstants.ID_PROPERTY));
    processToReturn.put(IGetConfigDetailsForEndpointModel.DATA_RULES, new ArrayList<>());
    processToReturn.put(IGetConfigDetailsForEndpointModel.PROCESS_LABEL,
        process.get(CommonConstants.LABEL_PROPERTY));
    processToReturn.put(IGetConfigDetailsForEndpointModel.PROCESS_DEFINITION_ID,
        process.get(IProcessEvent.PROCESS_DEFINITION_ID));
    
    processToReturn.put(IConfigDetailsForDataTransferModel.KLASS_DATA_RULES, new HashMap<>());
    processToReturn.put(IConfigDetailsForDataTransferModel.REFERENCED_DATA_RULES, new HashMap<>());
    fillKlassDetails(processToReturn, klassIds, endpointId, organizationId, physicalCatalogId);
    fillTaxonomiesDetails(processToReturn, taxonomyIds, endpointId, organizationId,
        physicalCatalogId);
    
    return processToReturn;
  }
  
  private static Map<String, Object> getProcesses(Vertex processNode, String eventType)
      throws Exception
  {
    HashMap<String, Object> returnMap = new HashMap<>();
    returnMap.putAll(UtilClass.getMapFromVertex(new ArrayList<>(), processNode));
    ProcessEventUtils.getProcessEventNodeWithConfigInformation(processNode, returnMap,
        new HashMap<>());
    
    return returnMap;
  }
}
