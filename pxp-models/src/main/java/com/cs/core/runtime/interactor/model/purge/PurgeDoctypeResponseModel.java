package com.cs.core.runtime.interactor.model.purge;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class PurgeDoctypeResponseModel implements IPurgeDoctypeResponseModel {
  
  private static final long                  serialVersionUID = 1L;
  protected Map<String, IPurgeResponseModel> docType;
  
  @JsonDeserialize(contentAs = PurgeResponseModel.class)
  @Override
  public Map<String, IPurgeResponseModel> getDocType()
  {
    return docType;
  }
  
  @Override
  public void setDocType(Map<String, IPurgeResponseModel> docType)
  {
    this.docType = docType;
  }
}
