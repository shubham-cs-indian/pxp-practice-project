package com.cs.config.strategy.plugin.usecase.klass;

import com.cs.config.strategy.plugin.usecase.klass.util.CreateKlassUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.attribute.IAttributeModel;
import com.cs.core.config.interactor.model.klass.IKlassModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.summary.ISummaryInformationModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BulkCreateKlasses extends AbstractOrientPlugin {
  
  public BulkCreateKlasses(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    List<Map<String, Object>> klassList = new ArrayList<>();
    klassList = (List<Map<String, Object>>) map.get("list");
    List<Map<String, Object>> createdKlassList = new ArrayList<>();
    List<Map<String, Object>> failedKlassList = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    // OrientGraph graph = UtilClass.getGraph();
    UtilClass.setSectionElementIdMap(new HashMap<>());
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(CommonConstants.ENTITY_KLASS,
        CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> klass : klassList) {
      try {
        Map<String, Object> klassMap = createKlassData(klass, vertexType);
        Map<String, Object> returnAttributeMap = new HashMap<>();
        returnAttributeMap.put(ISummaryInformationModel.ID, klassMap.get(IAttributeModel.ID));
        returnAttributeMap.put(ISummaryInformationModel.LABEL, klassMap.get(IAttributeModel.LABEL));
        createdKlassList.add(returnAttributeMap);
      }
      catch (PluginException e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null,
            (String) klass.get(IKlassModel.LABEL));
        addToFailureIds(failedKlassList, klass);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null,
            (String) klass.get(IKlassModel.LABEL));
        addToFailureIds(failedKlassList, klass);
      }
    }
    Map<String, Object> result = new HashMap<>();
    result.put(IPluginSummaryModel.SUCCESS, createdKlassList);
    result.put(IPluginSummaryModel.FAILURE, failure);
    result.put(IPluginSummaryModel.FAILED_IDS, failedKlassList);
    return result;
  }
  
  public void addToFailureIds(List<Map<String, Object>> failedKlassList, Map<String, Object> klass)
  {
    Map<String, Object> failedKlassMap = new HashMap<>();
    failedKlassMap.put(ISummaryInformationModel.ID, klass.get(IKlassModel.ID));
    failedKlassMap.put(ISummaryInformationModel.LABEL, klass.get(IKlassModel.LABEL));
    failedKlassList.add(failedKlassMap);
  }
  
  @SuppressWarnings("unchecked")
  private Map<String, Object> createKlassData(Map<String, Object> klassMap,
      OrientVertexType vertexType) throws Exception
  {
    String klassId = klassMap.get(CommonConstants.ID_PROPERTY)
        .toString();
    Vertex klassNode = UtilClass.createNode(klassMap, vertexType, new ArrayList<>());
    klassNode.setProperty(CommonConstants.CODE_PROPERTY, klassId);
    
    HashMap<String, Object> validatorMap = (HashMap<String, Object>) klassMap
        .get(CommonConstants.VALIDATOR_PROPERTY);
    OrientVertexType vType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.ENTITY_TYPE_KLASS_VALIDATOR, CommonConstants.CODE_PROPERTY);
    Vertex validatorNode = UtilClass.createNode(validatorMap, vType, new ArrayList<>());
    validatorNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_VALIDATOR_OF, klassNode);
    
    klassNode.removeProperty("referencedClassIds");
    klassNode.removeProperty("validator");
    klassNode.removeProperty("referencedAttributeIds");
    klassNode.removeProperty("permission");
    klassNode.removeProperty("deletedStructures");
    klassNode.removeProperty("notificationSettings");
    klassNode.removeProperty("structureChildren");
    klassNode.removeProperty("children");
    klassNode.removeProperty("klassViewSetting");
    klassNode.removeProperty("modifiedStructures");
    klassNode.removeProperty("addedStructures");
    klassNode.removeProperty("isEnforcedTaxonomy");
    klassNode.removeProperty("parent");
    klassNode.removeProperty(CommonConstants.TREE_TYPE_OPTION_PROPERTY);
    return CreateKlassUtils.createKlassData(klassMap, klassNode,
        VertexLabelConstants.ENTITY_TYPE_KLASS);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|BulkCreateKlasses/*" };
  }
}
