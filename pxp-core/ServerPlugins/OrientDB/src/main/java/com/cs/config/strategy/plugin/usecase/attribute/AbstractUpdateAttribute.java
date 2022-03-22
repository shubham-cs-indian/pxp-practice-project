package com.cs.config.strategy.plugin.usecase.attribute;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.standard.IConfigMap;
import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.datarule.util.DataRuleUtils;
import com.cs.config.strategy.plugin.usecase.grideditablepropertylist.util.GridEditUtil;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.util.ValidationUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.attribute.ICalculatedAttribute;
import com.cs.core.config.interactor.entity.attribute.ICustomUnitAttribute;
import com.cs.core.config.interactor.exception.attribute.AttributeNotFoundException;
import com.cs.core.config.interactor.model.attribute.ISaveAttributeModel;
import com.cs.core.exception.ErrorCodes;
import com.cs.core.exception.NotFoundException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

/** @author tauseef */
public abstract class AbstractUpdateAttribute extends AbstractOrientPlugin {

  public static final List<String> fieldsToExclude =
      Arrays.asList(
          ISaveAttributeModel.SUB_TYPE,
          ISaveAttributeModel.ADDED_TAGS,
          ISaveAttributeModel.MODIFIED_TAGS,
          ISaveAttributeModel.DELETED_TAGS,
          ISaveAttributeModel.ATTRIBUTE_TAGS,
          ISaveAttributeModel.IS_TRANSLATABLE,
          ISaveAttributeModel.IS_STANDARD);
  
  public AbstractUpdateAttribute(final OServerCommandConfiguration iConfiguration) {
    super(iConfiguration);
  }

  public Map<String, Object> updateAttribute(Map<String, Object> attributeMap, List<Map<String, Object>> auditInfoList,
      List<Map<String, Object>> updatedAttributeList) throws Exception
  {
    Map<String, Object> attributeReturnMap = new HashMap<>();
    Vertex attributeNode = null;

    if (ValidationUtils.validateAttributeInfo(attributeMap)) {

      attributeMap.remove(IAttribute.SUB_TYPE);

      String code = (String) attributeMap.get(CommonConstants.CODE_PROPERTY);
      try {
        attributeNode =
            UtilClass.getVertexByIndexedId(code, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
      } catch (NotFoundException e) {
        throw new AttributeNotFoundException(
            "ATTRIBUTE_NOT_FOUND", ErrorCodes.ATTRIBUTE_NOT_FOUND_ON_SAVE);
      }

      String newType = typeChangeHandling(attributeMap, attributeNode);

      if (newType.equals(CommonConstants.CALCULATED_ATTRIBUTE_TYPE)) {
        attributeReturnMap = AttributeUtils.saveCalculatedAttribute(attributeNode, attributeMap, updatedAttributeList);
      } else if (newType.equals(CommonConstants.CONCATENATED_ATTRIBUTE_TYPE)) {
        attributeReturnMap = AttributeUtils.saveConcatenatedAttribute(attributeNode, attributeMap, updatedAttributeList);
      } else {
        if (attributeNode.getProperty("isGridEditable").equals(true)
            && attributeMap.get("isGridEditable").equals(false)) {
          GridEditUtil.removePropertyFromGridEditSequenceList(
              attributeNode.getProperty(CommonConstants.CODE_PROPERTY));
        }
        attributeNode = UtilClass.saveNode(attributeMap, attributeNode, fieldsToExclude);
        attributeReturnMap = UtilClass.getMapFromNode(attributeNode);
      }
      AuditLogUtils.fillAuditLoginfo(auditInfoList, attributeNode, Entities.PROPERTIES, Elements.ATTRIBUTE);
    }
    return attributeReturnMap;
  }
  
  protected String typeChangeHandling(Map<String, Object> attributeMap, Vertex attributeNode) throws Exception
  {
    String oldType = attributeNode.getProperty(IAttribute.TYPE);
    String newType = (String) attributeMap.get(IAttribute.TYPE);
    if (!oldType.equals(newType)) {
      PropertyType oldPropertyType = IConfigMap.getPropertyType(oldType);
      PropertyType newPropertyType = IConfigMap.getPropertyType(newType);
      if (oldPropertyType.equals(PropertyType.MEASUREMENT) && newPropertyType.equals(PropertyType.MEASUREMENT)) {
        DataRuleUtils.deleteVerticesWithInDirection(attributeNode, RelationshipLabelConstants.ENTITY_RULE_VIOLATION_LINK);
        DataRuleUtils.deleteIntermediateVerticesWithInDirection(attributeNode, RelationshipLabelConstants.ATTRIBUTE_DATA_RULE_LINK);
        DataRuleUtils.deleteVerticesWithInDirection(attributeNode, RelationshipLabelConstants.NORMALIZATION_ENTITY_LINK);
        DataRuleUtils.deleteRuleNodesLinkedToEntityNode(attributeNode, RelationshipLabelConstants.HAS_ATTRIBUTE_LINK);
        if(CommonConstants.CUSTOM_UNIT_ATTRIBUTE_TYPE.equals(oldType)) {
          attributeNode.removeProperty(ICustomUnitAttribute.DEFAULT_UNIT_AS_HTML);
        }
        
        /*if (oldType.equals(CommonConstants.CALCULATED_ATTRIBUTE_TYPE)) {
          AttributeUtils.deleteOperatorNodesAttached(attributeNode);
        }
        if (oldType.equals(CommonConstants.CONCATENATED_ATTRIBUTE_TYPE)) {
          AttributeUtils.deleteConcatenatedNodesAttached(attributeNode);
        }*/
      }
      else {
        throw new Exception("Attribute Type Can Not Be Changed");
      }
    }
    return newType;
  }
  
  // TODO : Need to checked the use of this method
  /*private void manageTypeSwitchForCertainAttributeTypes(String oldType, Vertex attributeNode)
  {
    switch (oldType) {
      case CommonConstants.HTML_TYPE_ATTRIBUTE:
        attributeNode.removeProperty(CommonConstants.VALIDATOR_PROPERTY);
        break;
      
      case CommonConstants.IMAGE_TYPE_ATTRIBUTE:
        attributeNode.removeProperty(CommonConstants.VALIDATOR_PROPERTY);
        break;
      
      case CommonConstants.CUSTOM_UNIT_ATTRIBUTE_TYPE:
        attributeNode.removeProperty(ICustomUnitAttribute.DEFAULT_UNIT_AS_HTML);
        break;
  
      case CommonConstants.CALCULATED_ATTRIBUTE_TYPE:
        attributeNode.removeProperty(ICalculatedAttribute.CALCULATED_ATTRIBUTE_TYPE);
        attributeNode.removeProperty(ICalculatedAttribute.CALCULATED_ATTRIBUTE_UNIT);
        attributeNode.removeProperty(ICalculatedAttribute.CALCULATED_ATTRIBUTE_UNIT_AS_HTML);
        break;
    }
  }*/
}
