package com.cs.config.dto;

import com.cs.config.idto.IConfigJSONDTO;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContent;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.dto.RootConfigDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.ijosn.IJSONContent;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.collections4.BidiMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * This is the root class for all configuration DTO that implements default methods for simple flat structures
 * /!\ those methods have to be overridden by child class each time an embedded object has to be correctly converted 
 * in PXON
 * @author vallee
 */
public abstract class AbstractConfigJSONDTO extends RootConfigDTO implements IConfigJSONDTO {

  protected final JSONContent              configData = new JSONContent();
  private final BidiMap<ConfigTag, String> keyMap;
  private final Set<ConfigTag>             ignoredPXONTags;

  /**
   * @param jsonkeymap used to interchange keys between PXON format and actual format
   * @param ignoredPXONTags declared tags used in CSID
   */
  protected AbstractConfigJSONDTO(BidiMap<ConfigTag, String> jsonkeymap, Set<ConfigTag> ignoredPXONTags) {
    this.keyMap = jsonkeymap;
    this.ignoredPXONTags = ignoredPXONTags;
  }

  @Override
  public void fromJSON(String json) throws CSFormatException {
    configData.fromString(json, keyMap.keySet());
    setCode( getString( ConfigTag.code));
  }
  
  @Override
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException {
    fromCSExpression( (CSEObject)cse);
    configData.setField( ConfigTag.code.toString(), getCode());
    setCode( getString( ConfigTag.code));
    if(keyMap.containsKey(ConfigTag.type)) {
      String objectType = cse.getSpecification( ICSEElement.Keyword.$type);
      configData.setField( ConfigTag.type.toString(), objectType);
    }
  }

  @Override
  public void fromPXON(String json) throws CSFormatException {
    configData.clear();
    JSONObject pxonInput = JSONContent.StringToJSON(json);

    // Load other PXON tags and convert them into ODB tags
    for (Object pxonEntry : pxonInput.entrySet()) {
      ConfigTag configKey = keyMap.getKey( ((Entry)pxonEntry).getKey());
      if (configKey != null) {
        configData.setField( configKey.toString(), ((Entry)pxonEntry).getValue());
      }
    }

    // Load information from CSID
    JSONContentParser parser = new JSONContentParser(pxonInput);
    this.fromCSExpression(parser);
  }

  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException {
    return configData.toStringBuffer();
  }

  /**
   * return string form of pxon data
   */
  @Override
  public StringBuffer toPXONBuffer() throws CSFormatException {
    JSONContent pxonOutput = toConfigPXONContent();
//    String output = pxonOutput.toString();
//    return new StringBuffer( output.substring(1, output.length() - 1));
    return pxonOutput.toStringBuffer();
  }

  //If CSExpressId is null then csid won't be generated
  protected JSONContent toConfigPXONContent() throws CSFormatException {
    JSONContent pxonOutput = new JSONContent();
    ICSEElement icseElement = this.toCSExpressID();
    if (icseElement != null)
      pxonOutput.setField(IPXON.PXONTag.csid.toTag(), icseElement.toString());

    configData.toJSONObject().keySet().stream().filter(configKey -> {
      ConfigTag tag = ConfigTag.valueOf((String) configKey);
      return !ignoredPXONTags.contains(tag);
    }).forEach(configKey -> {
      ConfigTag tag = ConfigTag.valueOf((String) configKey);
      String pxonKey = keyMap.get(tag);
      pxonOutput.setField(pxonKey, configData.toJSONObject().get(configKey));
    });
    return pxonOutput;
  }

  abstract void fromCSExpression(JSONContentParser parser) throws CSFormatException;

  @Override
  public String toString() {
    try {
      return super.toJSON();
    }
    catch (CSFormatException e) {
      RDBMSLogger.instance().exception(e);
    }
    return null;
  }

  /**
   * Get string value
   *
   * @param key
   * @return
   */
  public String getString(ConfigTag key) {
    return configData.getInitField(key.toString(), "");
  }

  /**
   * Set string value
   *
   * @param key
   * @param value
   */
  public void setString(ConfigTag key, String value) {
    configData.setField(key.toString(), value);
  }

  /**
   * Get integer value
   *
   * @param key
   * @return
   */
  public Integer getInt(ConfigTag key) {
    return configData.getInitField(key.toString(), 0);
  }

  /**
   * Set integer value
   *
   * @param key
   * @param value
   */
  public void setInt(ConfigTag key, Integer value) {
    configData.setField(key.toString(), value);
  }

  /**
   * Get long value
   *
   * @param key
   * @return
   */
  public Long getLong(ConfigTag key) {
    return configData.getInitField(key.toString(), 0l);
  }

  /**
   * Set long value
   *
   * @param key
   * @param value
   */
  public void setLong(ConfigTag key, Long value) {
    configData.setField(key.toString(), value);
  }

  /**
   * Get boolean value
   *
   * @param key
   * @return
   */
  public Boolean getBoolean(ConfigTag key) {
    return configData.getInitField(key.toString(), false);
  }

  /**
   * Set boolean value
   *
   * @param key
   * @param value
   */
  public void setBoolean(ConfigTag key, Boolean value) {
    configData.setField(key.toString(), value);
  }

  /**
   * get JSON Content value
   *
   * @param key
   * @return
   */
  public IJSONContent getJSONContent(ConfigTag key) {
    return configData.getInitField(key.toString(), new JSONContent());
  }

  /**
   * Set JSON Contnet value
   *
   * @param key
   * @param value
   */
  public void setJSONContent(ConfigTag key, IJSONContent value) {
    configData.setField(key.toString(), value);
  }

  /**
   * @param key
   * @return array value
   */
  public JSONArray getJSONArray(ConfigTag key) {
    JSONArray jsonArray = (JSONArray) configData.toJSONObject().get(key.toString());
    return jsonArray == null ? new JSONArray() : jsonArray;
  }

  /**
   * Set JSONArray value
   *
   * @param key
   * @param value
   */
  public void setJSONArray(ConfigTag key, JSONArray value) {
    configData.toJSONObject().put(key.toString(), value);
  }
}
