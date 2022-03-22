package com.cs.config.dto;

import com.cs.config.idto.IConfigNormalizationDTO;
import com.cs.config.standard.IConfigClass;
import com.cs.config.standard.IConfigMap;
import com.cs.constants.CommonConstants;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContent;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.ijosn.IJSONContent;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.json.simple.JSONObject;

import java.util.*;

public class ConfigNormalizationDTO extends AbstractConfigJSONDTO implements IConfigNormalizationDTO {

  private static final long                       serialVersionUID  = 1L;
  /**
   * Tag keys and its corresponding PXON key map
   */
  private static final BidiMap<ConfigTag, String> KEY_MAP            = new DualHashBidiMap<>();
  private static final Set<ConfigTag>             IGNORED_PXON_TAGS  = new HashSet<>();
  private static final List<String>               NORMALIZATION_TYPE = Arrays.asList(CommonConstants.ATTRIBUTE, CommonConstants.TAG,
      CommonConstants.TYPE, CommonConstants.TAXONOMY, CommonConstants.TAGS);
  
  /**
   * Initializing the static key map
   */
  static {
    // Read only Properties
    KEY_MAP.put(ConfigTag.code, IPXON.PXONTag.code.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.type, IPXON.PXONTag.type.toReadOnlyTag());
    
    // update only Properties
    KEY_MAP.put(ConfigTag.entityId, IPXON.PXONTag.entityid.toTag());
    KEY_MAP.put(ConfigTag.entityAttributeType, IPXON.PXONTag.entityattributetype.toTag());
    KEY_MAP.put(ConfigTag.valueAsHTML, IPXON.PXONTag.valueashtml.toTag());
    KEY_MAP.put(ConfigTag.values, IPXON.PXONTag.values.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.tagValues, IPXON.PXONTag.tagvalues.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.attributeOperatorList, IPXON.PXONTag.attributeoperatorlist.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.calculatedAttributeUnit, IPXON.PXONTag.calcattributeunit.toTag());
    KEY_MAP.put(ConfigTag.calculatedAttributeUnitAsHTML, IPXON.PXONTag.calcattributeunitashtml.toTag());
    KEY_MAP.put(ConfigTag.transformationType, IPXON.PXONTag.transformationtype.toTag());
    KEY_MAP.put(ConfigTag.baseType, IPXON.PXONTag.basetype.toTag());
    KEY_MAP.put(ConfigTag.valueAttributeId, IPXON.PXONTag.valueattributeid.toTag());
    KEY_MAP.put(ConfigTag.attributeConcatenatedList, IPXON.PXONTag.attributeconcatenatedlist.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.findText, IPXON.PXONTag.findtext.toTag());
    KEY_MAP.put(ConfigTag.replaceText, IPXON.PXONTag.replacetext.toTag());
    KEY_MAP.put(ConfigTag.startIndex, IPXON.PXONTag.startindex.toTag());
    KEY_MAP.put(ConfigTag.endIndex, IPXON.PXONTag.endindex.toTag());
    
    IGNORED_PXON_TAGS.add(ConfigTag.baseType);
    IGNORED_PXON_TAGS.add(ConfigTag.attributeOperatorList);
    IGNORED_PXON_TAGS.add(ConfigTag.calculatedAttributeUnit);
  }
  
  public ConfigNormalizationDTO() throws CSFormatException
  {
    super(KEY_MAP, IGNORED_PXON_TAGS);
  }

  @Override
  public ICSEElement toCSExpressID() throws CSFormatException
  {
    return null;
  }

  @Override
  public String getEntityId()
  {
    return getString(ConfigTag.entityId);
  }

  @Override
  public void setEntityId(String entityId)
  {
    setString(ConfigTag.entityId, entityId);
  }

  @Override
  public String getType()
  {
    return getString(ConfigTag.type);
  }

  @Override
  public void setType(String type)
  {
   setString(ConfigTag.type, type); 
  }
  
  @Override
  public String getEntityAttributeType()
  {
    return getString(ConfigTag.entityAttributeType);
  }

  @Override
  public void setEntityAttributeType(String entityAttributeType)
  {
    setString(ConfigTag.entityAttributeType, entityAttributeType);
  }

  @Override
  public List<String> getValues()
  {
    return getJSONArray(ConfigTag.values);
  }

  @Override
  public String getValueAsHTML()
  {
    return getString(ConfigTag.valueAsHTML);
  }

  @Override
  public void setValueAsHTML(String valueAsHTML)
  {
   setString(ConfigTag.valueAsHTML, valueAsHTML); 
  }

  @Override
  public List<IJSONContent> getTagValues()
  {
    return getJSONArray(ConfigTag.tagValues);
  }

  @Override
  public List<IJSONContent> getAttributeOperatorList()
  {
    return getJSONArray(ConfigTag.attributeOperatorList);
  }

  @Override
  public String getCalculatedAttributeUnit()
  {
    return getString(ConfigTag.calculatedAttributeUnit);
  }

  @Override
  public void setCalculatedAttributeUnit(String calculatedAttributeUnit)
  {
    setString(ConfigTag.calculatedAttributeUnit, calculatedAttributeUnit);
  }

  @Override
  public String getCalculatedAttributeUnitAsHTML()
  {
    return getString(ConfigTag.calculatedAttributeUnitAsHTML);
  }

  @Override
  public void setCalculatedAttributeUnitAsHTML(String calculatedAttributeUnitAsHTML)
  {
    setString(ConfigTag.calculatedAttributeUnitAsHTML, calculatedAttributeUnitAsHTML);
  }

  @Override
  public String getTransformationType()
  {
    return getString(ConfigTag.transformationType);
  }

  @Override
  public void setTransformationType(String transformationType)
  {
    setString(ConfigTag.transformationType, transformationType);
  }

  @Override
  public String getBaseType()
  {
    return getString(ConfigTag.baseType);
  }

  @Override
  public void setBaseType(String baseType)
  {
    setString(ConfigTag.baseType, baseType);
  }

  @Override
  public String getValueAttributeId()
  {
    return getString(ConfigTag.valueAttributeId);
  }

  @Override
  public void setValueAttributeId(String valueAttributeId)
  {
    setString(ConfigTag.valueAttributeId, valueAttributeId);
  }

  @Override
  public List<IJSONContent> getAttributeConcatenatedList()
  {
    return getJSONArray(ConfigTag.attributeConcatenatedList);
  }

  @Override
  public String getFindText()
  {
    return getString(ConfigTag.findText);
  }

  @Override
  public void setFindText(String findText)
  {
    setString(ConfigTag.findText, findText);
  }

  @Override
  public String getReplaceText()
  {
    return getString(ConfigTag.replaceText);
  }

  @Override
  public void setReplaceText(String replaceText)
  {
    setString(ConfigTag.replaceText, replaceText);
  }

  @Override
  public Integer getStartIndex()
  {
    return getInt(ConfigTag.startIndex);
  }

  @Override
  public void setStartIndex(Integer startIndex)
  {
    setInt(ConfigTag.startIndex, startIndex);
  }

  @Override
  public Integer getEndIndex()
  {
    return getInt(ConfigTag.endIndex);
  }

  @Override
  public void setEndIndex(Integer endIndex)
  {
    setInt(ConfigTag.endIndex, endIndex);
  }

  @Override
  void fromCSExpression(JSONContentParser parser) throws CSFormatException
  {
    
  }

  @Override
  public void setValues(Collection<String> values)
  {
    configData.setStringArrayField(ConfigTag.values.name(), values);
  }

  @Override
  public void setTagValues(Collection<IJSONContent> tagValues)
  {
   configData.setField(ConfigTag.tagValues.name(), tagValues);    
  }

  @Override
  public void setAttributeOperatorList(List<IJSONContent> attributeOperatorList)
  {
    configData.setField(ConfigTag.attributeOperatorList.name(), attributeOperatorList);
  }

  @Override
  public void setAttributeConcatenatedList(List<IJSONContent> attributeConcatenatedList)
  {
    configData.setField(ConfigTag.attributeConcatenatedList.name(), attributeConcatenatedList);    
  }
  
  @Override
  public void fromPXON(String json) throws CSFormatException
  {
    super.fromPXON(json);
    initEntityType();
  }
  
  @Override
  public void fromJSON(String json) throws CSFormatException
  {
    super.fromJSON(json);
    JSONObject jsonInput = JSONContent.StringToJSON(json);
    configData.setField(ConfigTag.type.name(), jsonInput.get("entityType").toString());
    initEntityType();
  }

  private void initEntityType()
  {
    String type = getString(ConfigTag.type);
    if (!type.isEmpty() && !(NORMALIZATION_TYPE.contains(type.toLowerCase()))) {
      IPropertyDTO.PropertyType propertyType = IConfigMap.getPropertyType(type);
      setString(ConfigTag.type, propertyType.getSuperType().toString()); 
      String attributetype = getString(ConfigTag.entityAttributeType);
      if(attributetype.isEmpty())
        setString(ConfigTag.entityAttributeType, IConfigClass.PropertyClass.valueOfClassPath(type).name());
    }
  }
}
