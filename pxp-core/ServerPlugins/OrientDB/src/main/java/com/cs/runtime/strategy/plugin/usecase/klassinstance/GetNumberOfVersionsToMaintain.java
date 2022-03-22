package com.cs.runtime.strategy.plugin.usecase.klassinstance;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.templating.IGetNumberOfVersionsToMaintainResponseModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetNumberOfVersionsToMaintain extends AbstractOrientPlugin {
  
  public GetNumberOfVersionsToMaintain(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetNumberOfVersionsToMaintain/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> mapToReturn = new HashMap<String, Object>();
    List<String> klassIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.KLASS_IDS);
    fillNumberVersionsToMaintain(klassIds, mapToReturn);
    
    return mapToReturn;
  }
  
  protected void fillNumberVersionsToMaintain(List<String> klassIds,
      Map<String, Object> mapToReturn) throws Exception
  {
    Integer maxNumberOfVersionsToMaintain = 0;
    for (String klassId : klassIds) {
      try {
        Vertex klassVertex = UtilClass.getVertexById(klassId,
            VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
        List<String> fieldsToFetch = Arrays.asList(IKlass.NUMBER_OF_VERSIONS_TO_MAINTAIN);
        
        Map<String, Object> klassMap = UtilClass.getMapFromVertex(fieldsToFetch, klassVertex);
        Integer numberOfVersionsToMaintain = (Integer) klassMap
            .get(IKlass.NUMBER_OF_VERSIONS_TO_MAINTAIN);
        if (numberOfVersionsToMaintain > maxNumberOfVersionsToMaintain) {
          maxNumberOfVersionsToMaintain = numberOfVersionsToMaintain;
        }
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
    }
    
    mapToReturn.put(IGetNumberOfVersionsToMaintainResponseModel.NUMBER_OF_VERSIONS_TO_MAINTAIN,
        maxNumberOfVersionsToMaintain);
  }
}
