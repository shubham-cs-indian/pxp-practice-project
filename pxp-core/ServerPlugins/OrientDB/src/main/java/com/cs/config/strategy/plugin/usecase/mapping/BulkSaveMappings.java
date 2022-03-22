package com.cs.config.strategy.plugin.usecase.mapping;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.mapping.IMapping;
import com.cs.core.config.interactor.exception.bulksavemappings.BulkSaveMappingsFailedException;
import com.cs.core.config.interactor.model.mapping.IBulkSaveMappingsResponseModel;
import com.cs.core.config.interactor.model.mapping.IGetAllMappingsResponseModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BulkSaveMappings extends AbstractOrientPlugin {
  
  public BulkSaveMappings(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|BulkSaveMappings/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> mappingsList = (List<Map<String, Object>>) requestMap.get("list");
    List<Map<String, Object>> successMappingsList = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    
    for (Map<String, Object> mapping : mappingsList) {
      Vertex mappingNode = null;
      try {
        mappingNode = UtilClass.getVertexByIndexedId(
            (String) mapping.get(IConfigEntityInformationModel.ID),
            VertexLabelConstants.PROPERTY_MAPPING);
        UtilClass.saveNode(mapping, mappingNode, new ArrayList<>());
        Map<String, Object> mappingMap = UtilClass.getMapFromVertex(
            Arrays.asList(IConfigEntityInformationModel.CODE, IConfigEntityInformationModel.LABEL,
                IMapping.MAPPING_TYPE),
            mappingNode);
        successMappingsList.add(mappingMap);
      }
      catch (PluginException ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, null, null);
      }
      catch (Exception ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, null, null);
      }
    }
    if (successMappingsList.isEmpty()) {
      throw new BulkSaveMappingsFailedException(failure.getExceptionDetails(),
          failure.getDevExceptionDetails());
    }
    UtilClass.getGraph()
        .commit();
    Map<String, Object> successMap = new HashMap<>();
    successMap.put(IGetAllMappingsResponseModel.LIST, successMappingsList);
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IBulkSaveMappingsResponseModel.SUCCESS, successMap);
    return responseMap;
  }
}
