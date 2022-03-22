package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceModel;
import com.cs.core.runtime.strategy.model.klassinstance.IKlassInstanceDiffHelperModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ICreateKlassInstancePluginModel extends IModel {
  
  public static final String KLASS_INSTANCE_INFORMATION_MODEL = "klassInstanceInformationModel";
  public static final String KLASS_INSTANCE                   = "klassInstance";
  public static final String PROPERTY_VERSIONS                = "propertyVersions";
  public static final String EVENT_INSTANCE                   = "eventInstance";
  public static final String UPDATE_SEARCHABLE_DOCUMENT_DATA  = "updateSearchableDocumentData";
  public static final String KLASS_INSTANCE_DIFF              = "klassInstanceDiff";
  
  public IKlassInstanceInformationModel getKlassInstanceInformationModel();
  
  public void setKlassInstanceInformationModel(
      IKlassInstanceInformationModel klassInstanceInformationModel);
  
  public Map<String, Object> getKlassInstance();
  
  public void setKlassInstance(Map<String, Object> klassInstance);
  
  public List<HashMap<String, Object>> getPropertyVersions();
  
  public void setPropertyVersions(List<HashMap<String, Object>> propertyVersions);
  
  public Map<String, Object> getEventInstance();
  
  public void setEventInstance(Map<String, Object> eventInstance);
  
  public IUpdateSearchableInstanceModel getUpdateSearchableDocumentData();
  
  public void setUpdateSearchableDocumentData(
      IUpdateSearchableInstanceModel updateSearchableDocumentData);
  
  public IKlassInstanceDiffHelperModel getKlassInstanceDiff();
  
  public void setKlassInstanceDiff(IKlassInstanceDiffHelperModel klassInstanceDiff);
}
