package com.cs.config.strategy.plugin.usecase.authorization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.goldenrecord.IGoldenRecordRule;
import com.cs.core.config.interactor.exception.authorization.BulkSaveAuthorizationMappingFailedException;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.goldenrecord.ISaveGoldenRecordRuleModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.di.config.model.authorization.IBulkSavePartnerAuthorizationModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("unchecked")
public class BulkSavePartnerAuthorizationMapping extends AbstractOrientPlugin {
  
  public BulkSavePartnerAuthorizationMapping(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> listOfAuthorizationMappings = (List<Map<String, Object>>) requestMap
        .get(IListModel.LIST);
    List<Map<String, Object>> listOfSuccessSaveAuthorizationMappings = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    for (Map<String, Object> authorizationMappping : listOfAuthorizationMappings) {
      String authorizationMappingId = (String) authorizationMappping
          .get(ISaveGoldenRecordRuleModel.ID);
      Vertex authorizationMapping = null;
      try {
        authorizationMapping = UtilClass.getVertexById(authorizationMappingId,
            VertexLabelConstants.AUTHORIZATION_MAPPING);
        authorizationMapping = UtilClass.saveNode(authorizationMappping, authorizationMapping,
            new ArrayList<>());
        listOfSuccessSaveAuthorizationMappings.add(getAuthMapToReturn(authorizationMapping));
      }
      catch (PluginException ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, null, null);
      }
      catch (Exception ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, null, null);
      }
    }
    if (listOfSuccessSaveAuthorizationMappings.isEmpty()) {
      throw new BulkSaveAuthorizationMappingFailedException(failure.getExceptionDetails(),
          failure.getDevExceptionDetails());
    }
    UtilClass.getGraph()
        .commit();
    Map<String, Object> bulkSaveAuthorizationMappingResponse = new HashMap<String, Object>();
    bulkSaveAuthorizationMappingResponse.put(IBulkSavePartnerAuthorizationModel.SUCCESS,
        listOfSuccessSaveAuthorizationMappings);
    bulkSaveAuthorizationMappingResponse.put(IBulkSavePartnerAuthorizationModel.FAILURE,
        failure);
    return bulkSaveAuthorizationMappingResponse;
  }
  
  private Map<String, Object> getAuthMapToReturn(Vertex authorizationMappingVertex)
  {
    Map<String, Object> authorizationMap = new HashMap<>();
    authorizationMap.put(IIdLabelCodeModel.ID, UtilClass.getCodeNew(authorizationMappingVertex));
    authorizationMap.put(IIdLabelCodeModel.LABEL,
        UtilClass.getValueByLanguage(authorizationMappingVertex, IIdLabelModel.LABEL));
    authorizationMap.put(IIdLabelCodeModel.CODE,
        authorizationMappingVertex.getProperty(IGoldenRecordRule.CODE));
    return authorizationMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|BulkSavePartnerAuthorizationMapping/*" };
  }
}
