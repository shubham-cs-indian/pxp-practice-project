package com.cs.core.rdbms.coupling.dto;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.bgprocess.dto.InitializeBGProcessDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.coupling.idto.ILanguageInheritanceDTO;
import com.cs.core.technical.exception.CSFormatException;

public class LanguageInheritanceDTO extends InitializeBGProcessDTO implements ILanguageInheritanceDTO {
  
  /**
   * 
   */
  private static final long serialVersionUID      = 1L;
  public Long               baseEntityIID;
  public List<String>       localeIIDs            = new ArrayList<>();
  public List<Long>         dependentPropertyIIDs = new ArrayList<>();
  
  @Override
  public void setBaseEntityIID(Long baseEntityIID)
  {
    this.baseEntityIID = baseEntityIID;
  }
  
  @Override
  public Long getBaseEntityIID()
  {
    return baseEntityIID;
  }
  
  @Override
  public void setLocaleIIDs(List<String> localeIIDs)
  {
    this.localeIIDs = localeIIDs;
  }
  
  @Override
  public List<String> getLocaleIIDs()
  {
    return localeIIDs;
  }
  
  @Override
  public void setDependentPropertyIIDs(List<Long> dependentPropertyIIDs)
  {
    this.dependentPropertyIIDs = dependentPropertyIIDs;
  }
  
  @Override
  public List<Long> getDependentPropertyIIDs()
  {
    return dependentPropertyIIDs;
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(
        super.toJSONBuffer(),
        baseEntityIID != null ? JSONBuilder.newJSONField(BASE_ENTITY_IID, baseEntityIID) : JSONBuilder.VOID_FIELD,
        localeIIDs != null ? JSONBuilder.newJSONStringArray(LOCALE_IIDs, localeIIDs) : JSONBuilder.VOID_FIELD, 
            JSONBuilder.newJSONLongArray(DEPENDENT_PROPERTY_IIDS, 
                dependentPropertyIIDs));
  }

  @SuppressWarnings("unchecked")
  @Override
  public void fromJSON(JSONContentParser parser) throws CSFormatException
  {
    super.fromJSON(parser);
    baseEntityIID = parser.getLong(BASE_ENTITY_IID);
    parser.getJSONArray(LOCALE_IIDs).forEach((iid) -> {
      localeIIDs.add((String) iid);
    });
    parser.getJSONArray(DEPENDENT_PROPERTY_IIDS).forEach((iid) -> {
      dependentPropertyIIDs.add((Long) iid);
    });
  }
  
}
