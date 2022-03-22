package com.cs.core.transactionend.handlers.dto;

import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContent;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;

import java.util.HashMap;
import java.util.Map;

public class DependencyChangeDTO extends SimpleDTO implements IDependencyChangeDTO {

  public static final String IS_CREATED        = "isCreated";
  public static final String ENTITY_IID        = "entityIID";
  public static final String PROPERTIES_CHANGE = "propertiesChange";

  private long                entityIID        = 0L;
  private Map<String, Change> propertiesChange = new HashMap<>();
  private boolean             isCreated        = false;

  @Override
  public boolean isCreated()
  {
    return isCreated;
  }

  @Override
  public void setCreated(boolean created)
  {
    isCreated = created;
  }

  @Override
  public Map<String, Change> getPropertiesChange()
  {
    return propertiesChange;
  }

  @Override
  public void setPropertiesChange(Map<String, Change> propertiesChange)
  {
    this.propertiesChange = propertiesChange;
  }

  public long getEntityIID()
  {
    return entityIID;
  }

  public void setEntityIID(long entityIID)
  {
    this.entityIID = entityIID;
  }

  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    this.entityIID = json.getLong(ENTITY_IID);
    this.isCreated = json.getBoolean(IS_CREATED);
    Map<String, Object> changesJson = (Map<String,Object>)json.toJSONObject();
    propertiesChange.clear();

    Map<String, String> changes = (Map<String, String>) changesJson.get(PROPERTIES_CHANGE);
    if (changes != null) {
      for (Map.Entry change : changes.entrySet()) {
        Change sectionElements = Change.valueOf(change.getValue().toString());
        propertiesChange.put(change.getKey().toString(), sectionElements);
      }
    }
  }

  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    JSONContent jsonContent = new JSONContent();
    for (Map.Entry<String, Change> property : propertiesChange.entrySet()) {
      jsonContent.setField(property.getKey(), property.getValue().name());
    }
    return JSONBuilder.assembleJSONBuffer(JSONBuilder.newJSONField(ENTITY_IID, entityIID),
        JSONBuilder.newJSONField(IS_CREATED, isCreated),
        JSONBuilder.newJSONField(PROPERTIES_CHANGE, jsonContent));
  }
}
