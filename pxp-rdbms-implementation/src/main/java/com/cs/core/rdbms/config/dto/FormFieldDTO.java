package com.cs.core.rdbms.config.dto;

import java.util.HashMap;
import java.util.Map;

import com.cs.core.data.LocaleID;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContent;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.idto.IFormFieldDTO;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.ijosn.IJSONContent;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@SuppressWarnings("unchecked")
public class FormFieldDTO extends SimpleDTO implements IFormFieldDTO {
  
  private static final long     serialVersionUID = 1L;
  protected String              id;
  protected String              label;
  protected Map<String, Object> type;
  protected Map<String, Object> value;
  protected Map<String, String> properties;
  protected Map<String, String> values;
  
  public String getId()
  {
    return id;
  }
  
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public Map<String, Object> getType()
  {
    if (type == null)
      type = new HashMap<>();
    return type;
  }
  
  @Override
  @JsonDeserialize(as = Map.class)
  public void setType(Map<String, Object> type)
  {
    this.type = type;
  }
  
  @Override
  public Map<String, Object> getValue()
  {
    if (value == null) {
      value = new HashMap<>();
      value.put("type", new HashMap<>());
      value.put("value", "");
    }
    return value;
  }
  
  @Override
  @JsonDeserialize(as = Map.class)
  public void setValue(Map<String, Object> value)
  {
    this.value = value;
  }
  
  @Override
  public Map<String, String> getProperties()
  {
    if (properties == null)
      properties = new HashMap<>();
    return properties;
  }
  
  @Override
  public void setProperties(Map<String, String> properties)
  {
    this.properties = properties;
  }
  
  @Override
  @SuppressWarnings("unchecked")
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    Map<String, Object> valueType = (Map<String, Object>) value.get("type");
    Map<String, Object> typeValues =(Map<String, Object>) type.get("values");
    StringBuffer valuesOfType = new StringBuffer();
    if(typeValues != null) {
      typeValues.keySet().forEach(key -> {
        valuesOfType.append(JSONBuilder.newJSONField(key, (String) typeValues.get(key)));
        valuesOfType.append(",");
      });
      if (typeValues.size() > 0) {
        valuesOfType.setLength(valuesOfType.length() - 1);
      }
    }
    
    StringBuffer props = new StringBuffer();
    properties.keySet()
        .forEach(key -> {
          props.append(JSONBuilder.newJSONField(key, (String) properties.get(key)));
          props.append(",");
        });
    if (properties.size() > 0) {
      props.setLength(props.length() - 1);
    }
    String javaType = (String) valueType.get("javaType");
    String nameOfTypeField = (String) type.get("name");
    return JSONBuilder.assembleJSONBuffer(JSONBuilder.newJSONField(ID, id),
        JSONBuilder.newJSONField(LABEL, label),
        JSONBuilder.newJSONField(TYPE, JSONBuilder.assembleJSONBuffer(
            JSONBuilder.assembleJSONBuffer(JSONBuilder.newJSONField(VALUES, valuesOfType)),
            getNameOfTypeField(
                nameOfTypeField))),
        JSONBuilder.newJSONField(PROPERTIES, props),
        JSONBuilder.newJSONField(VALUE,
            JSONBuilder.assembleJSONBuffer(
                JSONBuilder.newJSONField(TYPE,
                    JSONBuilder.assembleJSONBuffer(
                        JSONBuilder.newJSONField("name", (String) valueType.get("name")),
                        JSONBuilder.newJSONField("parent", new StringBuffer()),
                        JSONBuilder.newJSONField("primitiveValueType",
                            (Boolean) valueType.get("primitiveValueType")),
                        JSONBuilder.newJSONField("abstract", (Boolean) valueType.get("abstract")),
                        JSONBuilder.newJSONField("javaType", javaType))),
                getValuWithJavaType(javaType))));
  }
  
  private StringBuffer getValuWithJavaType(String javaType)
  {
    if (value.get(VALUE) != null) {
      switch (javaType) {
        case "java.lang.Long":
        case "java.lang.Boolean":
          return new StringBuffer("\"").append(VALUE)
              .append("\":")
              .append(value.get(VALUE));
        case "java.lang.String":
          return JSONBuilder.newJSONField(VALUE, (String) value.get(VALUE));
      }
    }
    return new StringBuffer();
  }
  
  private StringBuffer getNameOfTypeField(String nameOfTypeField)
  {    
    return JSONBuilder.newJSONField("name", nameOfTypeField);    
  }

  @Override
  public void fromJSON(JSONContentParser json)
  {
    id = json.getString(ID);
    label = json.getString(LABEL);
    try {
      type = ObjectMapperUtil.readValue(json.getJSONContent(TYPE)
          .toString(), Map.class);
      properties = ObjectMapperUtil.readValue(json.getJSONContent(PROPERTIES)
          .toString(), Map.class);
      value = ObjectMapperUtil.readValue(json.getJSONContent(VALUE)
          .toString(), Map.class);
    }
    catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }

  
}
