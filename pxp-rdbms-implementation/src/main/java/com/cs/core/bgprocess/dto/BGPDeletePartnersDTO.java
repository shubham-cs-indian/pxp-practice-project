package com.cs.core.bgprocess.dto;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;

public class BGPDeletePartnersDTO extends InitializeBGProcessDTO implements IBGPDeletePartnersDTO {
  
  public static final String DELETED_PARTNERS_LIST = "deletedPartnersList";
  
  private List<String>       deletedPartnersList   = new ArrayList<>();
  private static final long  serialVersionUID      = 1L;
  
  @Override
  public List<String> getDeletedPartnersList()
  {
    return deletedPartnersList;
  }
  
  @Override
  public void setDeletedPartnersList(List<String> deletedPartnersList)
  {
    this.deletedPartnersList = deletedPartnersList;
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(super.toJSONBuffer(),JSONBuilder.newJSONStringArray(DELETED_PARTNERS_LIST, deletedPartnersList));
  }
  
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    super.fromJSON(json);
    deletedPartnersList.clear();
    json.getJSONArray(DELETED_PARTNERS_LIST).forEach((iid) -> {
      deletedPartnersList.add((String) iid);
    });
  }
  
}
