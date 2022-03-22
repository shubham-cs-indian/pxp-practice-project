package com.cs.config.strategy.plugin.usecase.target.util;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;

public class TargetUtils {
  
  public static HashMap<String, Object> getTargetEntityMap(Vertex klassNode, Boolean shouldGetKP)
      throws Exception
  {
    HashMap<String, Object> klassEntityMap = new HashMap<>();
    klassEntityMap.putAll(UtilClass.getMapFromNode(klassNode));
    KlassUtils.addChildrenInfoToKlassEntityMap(klassNode, klassEntityMap,
        VertexLabelConstants.ENTITY_TYPE_TARGET);
    KlassUtils.addParentInfoToKlassEntityMap(klassNode, klassEntityMap);
    KlassUtils.addSectionsToKlassEntityMap(klassNode, klassEntityMap, shouldGetKP);
    KlassUtils.setTreeTypeOption(klassNode, klassEntityMap);
    KlassUtils.addDataRules(klassNode, klassEntityMap);
    KlassUtils.addVariantContexts(klassNode, klassEntityMap);
    KlassUtils.addLifeCycleStatusTags(klassNode, klassEntityMap);
    KlassUtils.addKlassTasks(klassNode, klassEntityMap);
    return klassEntityMap;
  }
}
