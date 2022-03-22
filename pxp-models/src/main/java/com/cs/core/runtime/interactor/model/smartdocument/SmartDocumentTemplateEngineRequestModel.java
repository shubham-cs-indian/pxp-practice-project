package com.cs.core.runtime.interactor.model.smartdocument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class SmartDocumentTemplateEngineRequestModel
    implements ISmartDocumentTemplateEngineRequestModel {
  
  private static final long                                      serialVersionUID                     = 1L;
  protected List<? extends ISmartDocumentKlassInstanceDataModel> smartDocumentKlassInstancesDataModel = new ArrayList<>();
  protected ISmartDocumentInput                                  smartDocumentInputStreams;
  protected Map<String, Object>                                  fetchDataObject                      = new HashMap<>();
  
  @Override
  public List<? extends ISmartDocumentKlassInstanceDataModel> getSmartDocumentKlassInstancesDataModel()
  {
    return smartDocumentKlassInstancesDataModel;
  }
  
  @Override
  @JsonDeserialize(contentAs = SmartDocumentKlassInstanceDataModel.class)
  public void setSmartDocumentKlassInstancesDataModel(
      List<? extends ISmartDocumentKlassInstanceDataModel> smartDocumentKlassInstanceDataModel)
  {
    this.smartDocumentKlassInstancesDataModel = smartDocumentKlassInstanceDataModel;
  }
  
  @Override
  public ISmartDocumentInput getSmartDocumentByteArrays()
  {
    return smartDocumentInputStreams;
  }
  
  @Override
  public void setSmartDocumentByteArrays(ISmartDocumentInput smartDocumentInputStreams)
  {
    this.smartDocumentInputStreams = smartDocumentInputStreams;
  }

  @Override
  public Map<String, Object> getFetchDataObject()
  {
    return fetchDataObject;
  }

  @Override
  public void setFetchDataObject(Map<String, Object> fetchDataObject)
  {
    this.fetchDataObject = fetchDataObject;
  }
}
