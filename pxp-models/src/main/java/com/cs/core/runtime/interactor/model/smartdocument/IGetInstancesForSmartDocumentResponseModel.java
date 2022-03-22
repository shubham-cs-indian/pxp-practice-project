package com.cs.core.runtime.interactor.model.smartdocument;

import java.util.List;
import java.util.Map;

import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetInstancesForSmartDocumentResponseModel extends IModel {
  
  public static String KLASS_INSTANCES                      = "klassInstances";
  public static String KLASS_INSTANCES_NOT_AVAILABLE_LABELS = "klassInstancesNotAvailableLabels";
  public static String INSTANCES_IMAGE_ATTRIBUTE            = "instancesImageAttribute";
  
  public List<IBaseEntityDTO> getKlassInstances();
  
  public void setKlassInstances(List<IBaseEntityDTO> klassInstances);
  
  public List<String> getKlassInstancesNotAvailableLabels();
  
  public void setKlassInstancesNotAvailableLabels(List<String> klassInstancesNotAvailableLabels);
  
  public Map<String, IAssetInformationModel> getInstancesImageAttribute();
  
  public void setInstancesImageAttribute(
      Map<String, IAssetInformationModel> instancesImageAttribute);
}
