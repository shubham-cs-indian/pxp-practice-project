package com.cs.config.strategy.plugin.usecase.rulelist;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.exception.rulelist.RuleListNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GetReferencedDataRules extends AbstractOrientPlugin {
  
  public GetReferencedDataRules(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String endpointId = (String) requestMap.get(IMulticlassificationRequestModel.ENDPOINT_ID);
    String organizationId = (String) requestMap
        .get(IMulticlassificationRequestModel.ORAGANIZATION_ID);
    String physicalCatalogId = (String) requestMap
        .get(IMulticlassificationRequestModel.PHYSICAL_CATALOG_ID);
    
    String ruleListId = null;
    Map<String, Object> returnMap = new HashMap<String, Object>();
    List<Map<String, Object>> returnList = new ArrayList<>();
    Set<String> klassIdsList = new HashSet<>();
    
    ruleListId = (String) requestMap.get("listId");
    
    UtilClass.getVertexById(ruleListId, VertexLabelConstants.RULE_LIST);
    try {
      UtilClass.getVertexByIndexedId(ruleListId, VertexLabelConstants.RULE_LIST);
    }
    catch (NotFoundException e) {
      throw new RuleListNotFoundException();
    }
    
    for (String klassId : klassIdsList) {
      List<Map<String, Object>> referencedKlassesList = null;
      Map<String, Object> klassMap = null;
      try {
        Vertex klassNode = UtilClass.getVertexByIndexedId(klassId,
            VertexLabelConstants.ENTITY_TYPE_KLASS);
        referencedKlassesList = new ArrayList<>();
        klassMap = KlassUtils.getKlassEntityReferencesMap(klassNode, true);
        klassMap.put("dataRulesOfKlass", KlassUtils.getDataRulesOfKlass(klassNode, endpointId,
            organizationId, physicalCatalogId));
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
      returnList.add(klassMap);
    }
    UtilClass.getGraph()
        .commit();
    returnMap.put("list", returnList);
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetReferencedDataRules/*" };
  }
}
