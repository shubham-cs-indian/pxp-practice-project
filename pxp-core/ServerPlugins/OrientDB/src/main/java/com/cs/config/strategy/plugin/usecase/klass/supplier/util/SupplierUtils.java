package com.cs.config.strategy.plugin.usecase.klass.supplier.util;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.Map;

public class SupplierUtils {
  
  public static Map<String, Object> getSupplierEntityMap(Vertex supplierNode, Boolean shouldGetKP)
      throws Exception
  {
    HashMap<String, Object> klassEntityMap = new HashMap<>();
    klassEntityMap.putAll(UtilClass.getMapFromNode(supplierNode));
    KlassUtils.addChildrenInfoToKlassEntityMap(supplierNode, klassEntityMap,
        VertexLabelConstants.ENTITY_TYPE_SUPPLIER);
    KlassUtils.addParentInfoToKlassEntityMap(supplierNode, klassEntityMap);
    KlassUtils.addSectionsToKlassEntityMap(supplierNode, klassEntityMap, shouldGetKP);
    KlassUtils.addAllowedTypes(supplierNode, klassEntityMap);
    KlassUtils.setTreeTypeOption(supplierNode, klassEntityMap);
    KlassUtils.addDataRules(supplierNode, klassEntityMap);
    KlassUtils.addVariantContexts(supplierNode, klassEntityMap);
    KlassUtils.addLifeCycleStatusTags(supplierNode, klassEntityMap);
    KlassUtils.addKlassTasks(supplierNode, klassEntityMap);
    return klassEntityMap;
  }
}
