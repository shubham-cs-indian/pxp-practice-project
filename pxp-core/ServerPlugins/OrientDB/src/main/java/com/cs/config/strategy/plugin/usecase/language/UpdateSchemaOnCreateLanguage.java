package com.cs.config.strategy.plugin.usecase.language;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.language.IUpdateSchemaOnLangaugeCreateModel;
import com.cs.core.runtime.interactor.constants.application.Seperators;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateSchemaOnCreateLanguage extends AbstractOrientPlugin {
  
  public UpdateSchemaOnCreateLanguage(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|UpdateSchemaOnCreateLanguage/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    UtilClass.getDatabase()
        .commit();
    List<String> listOfVertexLabelConstants = Arrays.asList(
        VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE,
        VertexLabelConstants.ENTITY_TAG, VertexLabelConstants.ENTITY_TYPE_ROLE,
        VertexLabelConstants.ENTITY_TYPE_USER, VertexLabelConstants.ENTITY_TYPE_TASK,
        VertexLabelConstants.DATA_RULE, VertexLabelConstants.PROPERTY_COLLECTION,
        VertexLabelConstants.VARIANT_CONTEXT,
        VertexLabelConstants.ROOT_RELATIONSHIP, VertexLabelConstants.DASHBOARD_TAB,
        VertexLabelConstants.GOVERNANCE_RULE_KPI, VertexLabelConstants.ATTRIBUTION_TAXONOMY_LEVEL,
        VertexLabelConstants.SYSTEM, VertexLabelConstants.UI_TRANSLATIONS,
        VertexLabelConstants.ENDPOINT, VertexLabelConstants.PROCESS_EVENT,
        VertexLabelConstants.PROPERTY_MAPPING, VertexLabelConstants.TAB,
        VertexLabelConstants.LANGUAGE, VertexLabelConstants.ORGANIZATION, VertexLabelConstants.RULE_LIST,
        VertexLabelConstants.SMART_DOCUMENT_PRESET, VertexLabelConstants.TEMPLATE,
        VertexLabelConstants.GOLDEN_RECORD_RULE, VertexLabelConstants.AUTHORIZATION_MAPPING,
        VertexLabelConstants.ENTITY_TYPE_ICON);
    
    String code = (String) requestMap.get(IUpdateSchemaOnLangaugeCreateModel.LANGUAGE_CODE);
    
    for (String vertexLabelConstant : listOfVertexLabelConstants) {
      OrientVertexType vertexType = UtilClass.getOrCreateVertexType(vertexLabelConstant);
      UtilClass.createVertexPropertyAndApplyFulLTextIndex(vertexType,
          CommonConstants.LABEL_PROPERTY + Seperators.FIELD_LANG_SEPERATOR + code);
    }
    
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IIdsListParameterModel.IDS, listOfVertexLabelConstants);
    
    return returnMap;
  }
}
