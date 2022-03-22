package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.instance.ICreateKlassInstancePluginModel;
import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceModel;
import com.cs.core.runtime.interactor.model.searchable.UpdateSearchableInstanceModel;
import com.cs.core.runtime.strategy.model.klassinstance.IKlassInstanceDiffHelperModel;
import com.cs.core.runtime.strategy.model.klassinstance.KlassInstanceDiffHelperModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateKlassInstancePluginModel implements ICreateKlassInstancePluginModel {
  
  private static final long                serialVersionUID = 1L;
  protected IKlassInstanceInformationModel klassInstanceInformationModel;
  protected Map<String, Object>            klassInstance;
  protected List<HashMap<String, Object>>  propertyVersions;
  protected Map<String, Object>            eventInstance;
  protected IUpdateSearchableInstanceModel updateSearchableDocumentData;
  protected IKlassInstanceDiffHelperModel  klassInstanceDiff;
  
  @Override
  public Map<String, Object> getEventInstance()
  {
    return eventInstance;
  }
  
  @JsonDeserialize(as = HashMap.class)
  @Override
  public void setEventInstance(Map<String, Object> eventInstance)
  {
    this.eventInstance = eventInstance;
  }
  
  @Override
  public IKlassInstanceInformationModel getKlassInstanceInformationModel()
  {
    return klassInstanceInformationModel;
  }
  
  @JsonDeserialize(as = KlassInstanceInformationModel.class)
  @Override
  public void setKlassInstanceInformationModel(
      IKlassInstanceInformationModel klassInstanceInformationModel)
  {
    this.klassInstanceInformationModel = klassInstanceInformationModel;
  }
  
  @Override
  public Map<String, Object> getKlassInstance()
  {
    return klassInstance;
  }
  
  @JsonDeserialize(as = HashMap.class)
  @Override
  public void setKlassInstance(Map<String, Object> klassInstance)
  {
    this.klassInstance = klassInstance;
  }
  
  @Override
  public List<HashMap<String, Object>> getPropertyVersions()
  {
    return propertyVersions;
  }
  
  @JsonDeserialize(as = ArrayList.class)
  @Override
  public void setPropertyVersions(List<HashMap<String, Object>> propertyVersions)
  {
    this.propertyVersions = propertyVersions;
  }
  
  @Override
  public IUpdateSearchableInstanceModel getUpdateSearchableDocumentData()
  {
    return updateSearchableDocumentData;
  }
  
  @Override
  @JsonDeserialize(as = UpdateSearchableInstanceModel.class)
  public void setUpdateSearchableDocumentData(
      IUpdateSearchableInstanceModel updateSearchableDocumentData)
  {
    this.updateSearchableDocumentData = updateSearchableDocumentData;
  }
  
  @Override
  public IKlassInstanceDiffHelperModel getKlassInstanceDiff()
  {
    return klassInstanceDiff;
  }
  
  @Override
  @JsonDeserialize(as = KlassInstanceDiffHelperModel.class)
  public void setKlassInstanceDiff(IKlassInstanceDiffHelperModel klassInstanceDiff)
  {
    this.klassInstanceDiff = klassInstanceDiff;
  }
}
