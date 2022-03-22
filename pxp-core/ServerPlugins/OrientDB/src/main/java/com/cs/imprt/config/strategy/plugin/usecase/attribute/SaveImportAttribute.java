package com.cs.imprt.config.strategy.plugin.usecase.attribute;

import com.cs.config.strategy.plugin.delivery.response.ResponseCarrier;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.util.ValidationUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.importattribute.ImportAttributeDuplicateMappingIdException;
import com.cs.core.config.interactor.exception.importattribute.ImportAttributeNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.constants.application.ImportConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.orientechnologies.orient.server.network.protocol.http.OHttpRequest;
import com.orientechnologies.orient.server.network.protocol.http.OHttpResponse;
import com.orientechnologies.orient.server.network.protocol.http.command.OServerCommandAuthenticatedDbAbstract;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.HashMap;

public class SaveImportAttribute extends OServerCommandAuthenticatedDbAbstract {
  
  public SaveImportAttribute(final OServerCommandConfiguration iConfiguration)
  {
    super();
  }
  
  @Override
  public boolean execute(final OHttpRequest iRequest, OHttpResponse iResponse) throws Exception
  {
    String result = "";
    String model = iRequest.content.toString();
    HashMap<String, Object> map = new HashMap<String, Object>();
    HashMap<String, Object> attributemap = new HashMap<String, Object>();
    HashMap<String, Object> returnMap = null;
    
    try {
      map = ObjectMapperUtil.readValue(model, HashMap.class);
      attributemap = (HashMap<String, Object>) map.get("attribute");
      ODatabaseDocumentTx database = (ODatabaseDocumentTx) getProfiledDatabaseInstance(iRequest);
      OrientGraph graph = new OrientGraph(database);
      UtilClass.setGraph(graph);
      Vertex vertex = null;
      
      if (ValidationUtils.validateAttributeInfo(attributemap)) {
        
        try {
          vertex = UtilClass.getVertexByIndexedId(
              (String) attributemap.get(CommonConstants.ID_PROPERTY),
              VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
        }
        catch (NotFoundException e) {
          throw new ImportAttributeNotFoundException();
        }
        vertex = UtilClass.saveNode(attributemap, vertex);
        String currentAttributeId = vertex.getProperty(CommonConstants.CODE_PROPERTY);
        String mappedAttributeId = vertex.getProperty(ImportConstants.MAPPED_TO_PROPERTY);
        if (mappedAttributeId != null && !currentAttributeId.equals(mappedAttributeId)) {
          Vertex existingAttributeVertex = existingAttributeVertex = UtilClass
              .getVertexByIndexedId(mappedAttributeId, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
          if (existingAttributeVertex != null) {
            String attributeLabel = (String) UtilClass.getValueByLanguage(existingAttributeVertex,
                CommonConstants.LABEL_PROPERTY);
            throw new ImportAttributeDuplicateMappingIdException();
          }
          vertex.setProperty(CommonConstants.CODE_PROPERTY, mappedAttributeId);
        }
        returnMap = UtilClass.getMapFromNode(vertex);
        
        graph.commit();
      }
      
      ResponseCarrier.successResponse(iResponse, returnMap);
      
    }
    catch (PluginException e) {
      ResponseCarrier.failedResponse(iResponse, e);
    }
    catch (Exception e) {
      ResponseCarrier.failedResponse(iResponse, e);
    }
    return false;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveImportAttribute/*" };
  }
}
