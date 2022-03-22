package com.cs.core.runtime.interactor.model.smartdocument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.runtime.interactor.model.assetinstance.AssetInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetInstancesForSmartDocumentResponseModel
    implements IGetInstancesForSmartDocumentResponseModel {
  
  private static final long                      serialVersionUID        = 1L;
  protected List<IBaseEntityDTO>                 klassInstances          = new ArrayList<>();
  protected List<String>                         klassInstancesNotAvailableLables;
  protected Map<String, IAssetInformationModel>  instancesImageAttribute = new HashMap<>();
  
  @Override
  public List<IBaseEntityDTO> getKlassInstances()
  {
    return klassInstances;
  }
  
  @Override
  public void setKlassInstances(List<IBaseEntityDTO> klassInstances)
  {
    this.klassInstances = klassInstances;
  }
  
  @Override
  public List<String> getKlassInstancesNotAvailableLabels()
  {
    return klassInstancesNotAvailableLables;
  }
  
  @Override
  public void setKlassInstancesNotAvailableLabels(List<String> klassInstancesNotAvailableLables)
  {
    this.klassInstancesNotAvailableLables = klassInstancesNotAvailableLables;
  }
  
  @Override
  public Map<String, IAssetInformationModel> getInstancesImageAttribute()
  {
    return instancesImageAttribute;
  }
  
  @Override
  @JsonDeserialize(contentAs = AssetInformationModel.class)
  public void setInstancesImageAttribute(
      Map<String, IAssetInformationModel> instancesImageAttribute)
  {
    this.instancesImageAttribute = instancesImageAttribute;
  }
}
