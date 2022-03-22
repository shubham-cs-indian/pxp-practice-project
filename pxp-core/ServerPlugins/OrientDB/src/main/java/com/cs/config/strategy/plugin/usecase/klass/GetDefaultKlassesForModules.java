package com.cs.config.strategy.plugin.usecase.klass;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.core.config.interactor.model.configdetails.IGetDefaultKlassesModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.typeswitch.IGetAllowedTypesForModulesModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetDefaultKlassesForModules extends AbstractOrientPlugin {
  
  public GetDefaultKlassesForModules(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<HashMap<String, Object>> list = new ArrayList<>();
    list = (List<HashMap<String, Object>>) requestMap.get(IListModel.LIST);
    
    List<Map<String, Object>> childrenList = new ArrayList<Map<String, Object>>();
    for (HashMap<String, Object> map : list) {
      List<String> standardKlassIds = (List<String>) map
          .get(IGetAllowedTypesForModulesModel.STANDARD_KLASS_IDS);
      for (String standardKlassId : standardKlassIds) {
        String[] childrenKeyValues = new String[] { "isDefaultChild", "true" };
        KlassGetUtils.getNonAbstractKlassesList(standardKlassId,
            VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS, childrenKeyValues, childrenList);
      }
    }
    
    Map<String, Object> returnModel = new HashMap<>();
    returnModel.put(IGetDefaultKlassesModel.CHILDREN, childrenList);
    return returnModel;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetDefaultKlassesForModules/*" };
  }
}
