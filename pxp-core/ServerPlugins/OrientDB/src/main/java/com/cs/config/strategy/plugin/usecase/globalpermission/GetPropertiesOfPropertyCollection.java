package com.cs.config.strategy.plugin.usecase.globalpermission;

import com.cs.config.strategy.plugin.delivery.response.ResponseCarrier;
import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.globalpermissions.IGetPropertiesOfPropertyCollectionModel;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.orientechnologies.orient.server.network.protocol.http.OHttpRequest;
import com.orientechnologies.orient.server.network.protocol.http.OHttpResponse;
import com.orientechnologies.orient.server.network.protocol.http.command.OServerCommandAuthenticatedDbAbstract;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.HashMap;

public class GetPropertiesOfPropertyCollection extends OServerCommandAuthenticatedDbAbstract {
  
  public GetPropertiesOfPropertyCollection(final OServerCommandConfiguration iConfiguration)
  {
    super();
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public boolean execute(final OHttpRequest iRequest, OHttpResponse iResponse) throws Exception
  {
    try {
      String model = iRequest.content.toString();
      HashMap<String, Object> map = ObjectMapperUtil.readValue(model, HashMap.class);
      ODatabaseDocumentTx database = (ODatabaseDocumentTx) getProfiledDatabaseInstance(iRequest);
      OrientGraph graph = new OrientGraph(database);
      UtilClass.setGraph(graph);
      
      HashMap<String, Object> globalPermissionMap = (HashMap<String, Object>) map
          .get("globalPermissions");
      
      String roleId = (String) globalPermissionMap.get(IGetPropertiesOfPropertyCollectionModel.ID);
      String propertyCollectionId = (String) globalPermissionMap
          .get(IGetPropertiesOfPropertyCollectionModel.PROPERTY_COLLECTION_ID);
      
      ResponseCarrier.successResponse(iResponse,
          GlobalPermissionUtils.getPropertiesPermissions(roleId, propertyCollectionId));
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
    
    return new String[] { "POST|GetPropertiesOfPropertyCollection/*" };
  }
}
