package com.cs.config.strategy.plugin.usecase.textasset.util;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.Map;

public class TextAssetUtils {
  
  public static Map<String, Object> getTextAssetEntityMap(Vertex textAssetNode, Boolean shouldGetKP)
      throws Exception
  {
    HashMap<String, Object> klassEntityMap = new HashMap<>();
    klassEntityMap.putAll(UtilClass.getMapFromNode(textAssetNode));
    KlassUtils.addChildrenInfoToKlassEntityMap(textAssetNode, klassEntityMap,
        VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET);
    KlassUtils.addParentInfoToKlassEntityMap(textAssetNode, klassEntityMap);
    KlassUtils.addSectionsToKlassEntityMap(textAssetNode, klassEntityMap, shouldGetKP);
    KlassUtils.addAllowedTypes(textAssetNode, klassEntityMap);
    KlassUtils.setTreeTypeOption(textAssetNode, klassEntityMap);
    KlassUtils.addDataRules(textAssetNode, klassEntityMap);
    KlassUtils.addVariantContexts(textAssetNode, klassEntityMap);
    KlassUtils.addLifeCycleStatusTags(textAssetNode, klassEntityMap);
    KlassUtils.addKlassTasks(textAssetNode, klassEntityMap);
    KlassUtils.addKlassNature(textAssetNode, klassEntityMap);
    return klassEntityMap;
  }
}
