package com.cs.core.bgprocess.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.cs.core.bgprocess.idto.IBaseEntityPlanDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;

/**
 * Implementation of base entity transfer plan
 *
 * @author vallee
 */
public class BaseEntityPlanDTO extends InitializeBGProcessDTO implements IBaseEntityPlanDTO {
  
  public static final String    BASE_ENTITY_IIDS = "baseEntities";
  private final Set<Long> baseEntityIIDs   = new HashSet<>();
  private static final String    PROPERTY_IIDS    = "properties";
  private final Set<Long> propertyIIDs     = new HashSet<>();
  private static final String    ALL_PROPERTIES   = "allProperties";
  private boolean         allProperties    = false;
  
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    super.fromJSON(json);
    baseEntityIIDs.clear();
    json.getJSONArray(BASE_ENTITY_IIDS)
        .forEach((iid) -> {
          baseEntityIIDs.add((Long) iid);
        });
    propertyIIDs.clear();
    json.getJSONArray(PROPERTY_IIDS)
        .forEach((iid) -> {
          propertyIIDs.add((Long) iid);
        });
    allProperties = json.getBoolean(ALL_PROPERTIES);
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(super.toJSONBuffer(), JSONBuilder.newJSONField(ALL_PROPERTIES, allProperties),
        !baseEntityIIDs.isEmpty() ? JSONBuilder.newJSONLongArray(BASE_ENTITY_IIDS, baseEntityIIDs) : JSONBuilder.VOID_FIELD,
        !propertyIIDs.isEmpty() && allProperties == false ? JSONBuilder.newJSONLongArray(PROPERTY_IIDS, propertyIIDs)
            : JSONBuilder.VOID_FIELD);
  }
  
  @Override
  public Set<Long> getBaseEntityIIDs()
  {
    return baseEntityIIDs;
  }
  
  @Override
  public void setBaseEntityIIDs(Collection<Long> entityIIDs)
  {
    baseEntityIIDs.clear();
    baseEntityIIDs.addAll(entityIIDs);
  }
  
  @Override
  public Set<Long> getPropertyIIDs()
  {
    return propertyIIDs;
  }
  
  @Override
  public void setPropertyIIDs(Collection<Long> propIIDs)
  {
    propertyIIDs.clear();
    propertyIIDs.addAll(propIIDs);
  }
  
  @Override
  public boolean getAllProperties()
  {
    return allProperties;
  }
  
  @Override
  public void setAllProperties(boolean status)
  {
    allProperties = status;
  }
}
