package com.cs.config.strategy.plugin.usecase.klass.supplier;

import com.cs.config.strategy.plugin.usecase.klass.supplier.util.SupplierUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
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
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetSupplierWithGlobalPermission extends AbstractOrientPlugin {
  
  public GetSupplierWithGlobalPermission(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> map) throws Exception
  {
    String id = (String) map.get(IIdParameterModel.ID);
    OrientGraph graph = UtilClass.getGraph();
    Map<String, Object> rootNode = new HashMap<>();
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    
    if (id.equals("-1")) {
      rootNode.put(CommonConstants.ID_PROPERTY, "-1");
      rootNode.put(CommonConstants.TYPE_PROPERTY, CommonConstants.SUPPLIER_KLASS_TYPE);
      List<Map<String, Object>> childKlasses = new ArrayList<>();
      rootNode.put(CommonConstants.CHILDREN_PROPERTY, childKlasses);
      
      Iterable<Vertex> i = graph
          .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_SUPPLIER
              + " where outE('Child_Of').size() = 0 order by "
              + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
          .execute();
      
      for (Vertex klassNode : i) {
        HashMap<String, Object> klassEntityMap = new HashMap<String, Object>();
        klassEntityMap.putAll(UtilClass.getMapFromNode(klassNode));
        childKlasses.add(klassEntityMap);
      }
      returnMap.put(IGetKlassWithGlobalPermissionModel.KLASS, rootNode);
    }
    else {
      try {
        Vertex supplierNode = UtilClass.getVertexByIndexedId(id,
            VertexLabelConstants.ENTITY_TYPE_SUPPLIER);
        rootNode = SupplierUtils.getSupplierEntityMap(supplierNode, null);
        KlassUtils.addGlobalPermission(supplierNode, returnMap);
        returnMap.put(IGetKlassWithGlobalPermissionModel.KLASS, rootNode);
        KlassUtils.fillReferencedAttributesAndTags(returnMap, rootNode);
        KlassUtils.fillReferencedContextDetails(returnMap);
        KlassUtils.fillContextKlassesDetails(returnMap, rootNode, supplierNode);
        
        KlassUtils.fillReferencedTaskDetails(returnMap);
        KlassUtils.fillReferencedDataRuleDetails(returnMap);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
    }
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetSupplierWithGlobalPermission/*" };
  }
}
