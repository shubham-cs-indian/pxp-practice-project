package com.cs.config.strategy.plugin.usecase.versioncount;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.version.IVersionCountModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetVersionCount extends AbstractOrientPlugin {
  
  public GetVersionCount(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> klassIds = (List<String>) requestMap.get(IIdsListParameterModel.IDS);
    int maxVersionValue = Integer.MIN_VALUE;
    for (String klassId : klassIds) {
      Vertex klassNode = null;
      try {
        klassNode = UtilClass.getVertexById(klassId, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
      Integer versionCount = klassNode.getProperty(IKlass.NUMBER_OF_VERSIONS_TO_MAINTAIN);
      if (versionCount > maxVersionValue) {
        maxVersionValue = versionCount;
      }
    }
    
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IVersionCountModel.Max_VERSION_COUNT, maxVersionValue);
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetVersionCount/*" };
  }
}
