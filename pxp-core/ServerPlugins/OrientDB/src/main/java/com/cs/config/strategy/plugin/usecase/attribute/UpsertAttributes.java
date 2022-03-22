package com.cs.config.strategy.plugin.usecase.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.importPXON.util.ImportUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.attribute.ICalculatedAttribute;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedAttribute;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.di.workflow.tasks.ITransformationTask;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

/**
 * @author tauseef
 */
@SuppressWarnings("unchecked")
public class UpsertAttributes extends AbstractUpdateAttribute {

  public UpsertAttributes(OServerCommandConfiguration iConfiguration) {
    super(iConfiguration);
  }
  
  private final  String ALLOWED_RTE_ICONS = "allowedRTEIcons";
  
  @Override protected Object execute(Map<String, Object> requestMap) throws Exception {
    List<Map<String, Object>> listOfAttributes = (List<Map<String, Object>>) requestMap
        .get(IListModel.LIST);
    List<Map<String, Object>> attributeList = prepareAttributeList(listOfAttributes);
    IExceptionModel failure = new ExceptionModel();
    Map<String, Integer> formulaSizes = (Map<String, Integer>) requestMap.get(CommonConstants.ATTRIBUTE_FORMULA_CODE_SIZE);
    List<Map<String, Object>> failedAttributeList = new ArrayList<>();
    List<Map<String, Object>> createdAttributeList = new ArrayList<>();
    for (Map<String, Object> attributeMap : attributeList) {
      UtilClass.validateIconExistsForImport(attributeMap);
      addValidatorMapForHTMLAttribute(attributeMap);
      try {
        try {
          String code = (String) attributeMap.get(CommonConstants.CODE_PROPERTY);
          Vertex attributeNode = UtilClass.getVertexByIndexedId(code, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
          if (formulaSizes.containsKey(code)) {
            Integer size = formulaSizes.get(code);
            handleCalAndConAttributeExpressionChange(attributeMap, size, attributeNode);
          }
          attributeMap = updateAttribute(attributeMap, new ArrayList<>(), new ArrayList<>());
        }
        catch (NotFoundException e) {
          createAttribute(attributeMap, failure);
        }
        UtilClass.getGraph().commit();
        ImportUtils.addSuccessImportedInfo(createdAttributeList, attributeMap);
      }
      catch (Exception ex) {
        ImportUtils.logExceptionAndFailureIDs(failure, failedAttributeList, attributeMap, ex);
      }
    }

    Map<String, Object> responseMap = ImportUtils.prepareImportResponseMap(failure, createdAttributeList, failedAttributeList);
    return responseMap;
  }

  private List<Map<String, Object>> prepareAttributeList(List<Map<String, Object>> listOfAttributes)
  {
    List<Map<String, Object>> conAndcalculatedattribute = new ArrayList<>();
    List<Map<String, Object>> attributeList = new ArrayList<>();
    String attributeType = ConfigTag.subType.name();
    for(Map<String,Object> attributeMap : listOfAttributes) {
      String attributeTypeValue = (String) attributeMap.get(attributeType);
     // tagMap.put(ITagModel.IS_ROOT, true);
      if(!(ITransformationTask.CALCULATED_ATTRIBUTE_TYPE.equals(attributeTypeValue) || ITransformationTask.CONCATENATED_ATTRIBUTE_TYPE.equals(attributeTypeValue))) {
        attributeList.add(attributeMap);
      }else {
        conAndcalculatedattribute.add(attributeMap);
      }
    }
    
    attributeList.addAll(conAndcalculatedattribute);
    return attributeList;
  }
  private void handleCalAndConAttributeExpressionChange(Map<String, Object> attributeMap, int size, Vertex attributeNode)
  {
    String type = attributeNode.getProperty(CommonConstants.TYPE_PROPERTY);
    if(CommonConstants.CALCULATED_ATTRIBUTE_TYPE.equals(type) && size != 0) {
      List<Map<String, Object>> attributeOperators = (List<Map<String, Object>>) attributeMap.get(ICalculatedAttribute.ATTRIBUTE_OPERATOR_LIST);
      if(attributeOperators.isEmpty()) {
        Map<String, Object> calculatedAttribute = AttributeUtils.getCalculatedAttribute(attributeNode);
        attributeMap.put(ICalculatedAttribute.ATTRIBUTE_OPERATOR_LIST, calculatedAttribute.get(ICalculatedAttribute.ATTRIBUTE_OPERATOR_LIST));
      }
    }
    else if(CommonConstants.CONCATENATED_ATTRIBUTE_TYPE.equals(type) && size != 0) {
      List<Map<String, Object>> attributeConcatenated = (List<Map<String, Object>>) attributeMap.get(IConcatenatedAttribute.ATTRIBUTE_CONCATENATED_LIST);
      if(attributeConcatenated.isEmpty()) {
        Map<String, Object> calculatedAttribute = AttributeUtils.getConcatenatedAttribute(attributeNode);
        attributeMap.put(IConcatenatedAttribute.ATTRIBUTE_CONCATENATED_LIST,  calculatedAttribute.get(IConcatenatedAttribute.ATTRIBUTE_CONCATENATED_LIST));
      }
    }
  }

  private void addValidatorMapForHTMLAttribute(Map<String, Object> attributeMap)
  {
    String type = (String) attributeMap.get(IAttribute.TYPE);
    if(CommonConstants.HTML_TYPE_ATTRIBUTE.equals(type)) {
      Map<String, Object> validator = new HashMap<>();
      List<String> allowedStyles = (List<String>) attributeMap.get(ConfigTag.allowedStyles.toString());
      allowedStyles = allowedStyles == null ? new ArrayList<>() : allowedStyles;
      validator.put(ALLOWED_RTE_ICONS, allowedStyles);
      attributeMap.put(CommonConstants.VALIDATOR_PROPERTY, validator);
    }
  }
  
  private void createAttribute(Map<String, Object> attributeMap, IExceptionModel failure) {
    OrientGraph graph = UtilClass.getGraph();
    String code = (String) attributeMap.get(IAttribute.CODE);
    String label = (String) attributeMap.get(IAttribute.LABEL);
    try {
      AttributeUtils.createAttribute(attributeMap, graph);
    } catch (Exception ex) {
      ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, code, label);
    }
  }

  @Override
  public String[] getNames() {
    return new String[] {"POST|UpsertAttributes/*"};
  }
}
