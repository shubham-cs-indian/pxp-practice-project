package com.cs.config.strategy.plugin.usecase.target;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.target.util.TargetUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.klass.IGetKlassWithGlobalPermissionModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetTargetWithGlobalPermission extends AbstractOrientPlugin {
  
  public GetTargetWithGlobalPermission(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    String id = (String) requestMap.get(IIdParameterModel.ID);
    Map<String, Object> returnMap = new HashMap<String, Object>();
    Map<String, Object> klassMap = new HashMap<>();
    
    if (id.equals("-1")) {
      klassMap.put(CommonConstants.ID_PROPERTY, "-1");
      klassMap.put(CommonConstants.TYPE_PROPERTY, CommonConstants.MARKET_KLASS_TYPE);
      List<Map<String, Object>> childKlasses = new ArrayList<>();
      klassMap.put(CommonConstants.CHILDREN_PROPERTY, childKlasses);
      
      Iterable<Vertex> i = UtilClass.getGraph()
          .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_TARGET
              + " where outE('Child_Of').size() = 0 order by "
              + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
          .execute();
      
      for (Vertex targetNode : i) {
        childKlasses.add(TargetUtils.getTargetEntityMap(targetNode, null));
      }
      returnMap.put(IGetKlassWithGlobalPermissionModel.KLASS, klassMap);
      
    }
    else {
      Vertex targetNode = null;
      try {
        targetNode = UtilClass.getVertexByIndexedId(id, VertexLabelConstants.ENTITY_TYPE_TARGET);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
      klassMap = TargetUtils.getTargetEntityMap(targetNode, null);
      KlassUtils.addGlobalPermission(targetNode, returnMap);
      returnMap.put(IGetKlassWithGlobalPermissionModel.KLASS, klassMap);
      KlassUtils.fillReferencedAttributesAndTags(returnMap, klassMap);
      KlassUtils.fillReferencedContextDetails(returnMap);
      KlassUtils.fillContextKlassesDetails(returnMap, klassMap, targetNode);
      
      KlassUtils.fillReferencedTaskDetails(returnMap);
      KlassUtils.fillReferencedDataRuleDetails(returnMap);
    }
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetTargetWithGlobalPermission/*" };
  }
}
