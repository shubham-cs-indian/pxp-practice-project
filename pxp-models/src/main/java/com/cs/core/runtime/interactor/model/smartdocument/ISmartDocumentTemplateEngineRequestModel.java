package com.cs.core.runtime.interactor.model.smartdocument;

import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ISmartDocumentTemplateEngineRequestModel extends IModel {
  
  public static final String SMART_DOCUMENT_KLASS_INSTANCES_DATA_MODEL = "smartDocumentKlassInstancesDataModel";
  public static final String SMART_DOCUMENT_INPUT_STREAMS              = "smartDocumentInputStreams";
  public static final String FETCH_DATA_OBJECT                         = "fetchDataObject";
  
  public List<? extends ISmartDocumentKlassInstanceDataModel> getSmartDocumentKlassInstancesDataModel();
  
  public void setSmartDocumentKlassInstancesDataModel(
      List<? extends ISmartDocumentKlassInstanceDataModel> smartDocumentKlassInstancesDataModel);
  
  public ISmartDocumentInput getSmartDocumentByteArrays();
  
  public void setSmartDocumentByteArrays(ISmartDocumentInput smartDocumentInputStreams);
  
  public Map<String, Object> getFetchDataObject();
  public void setFetchDataObject(Map<String, Object> fetchDataObject);
}
