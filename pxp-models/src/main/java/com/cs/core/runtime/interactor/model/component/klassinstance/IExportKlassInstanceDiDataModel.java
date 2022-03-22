package com.cs.core.runtime.interactor.model.component.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.dataintegration.INomenclatureDiModel;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IExportKlassInstanceDiDataModel extends IModel {
  
  public static final String TIMESTAMP           = "timestamp";
  public static final String ID                  = "id";
  public static final String NOMENCLATURE        = "nomenclatures";
  public static final String RELATIONSHIPS       = "relationShips";
  public static final String PROPERTY_COLLECTION = "propertyCollection";
  public static final String EMBEDDED_VARIANTS   = "embeddedVariants";
  public static final String BROKER_URL          = "brokerURL";
  
  public Date getTimestamp();
  
  public void setTimestamp(Date timestamp);
  
  public String getId();
  
  public void setId(String id);
  
  public INomenclatureDiModel getNomenclatures();
  
  public void setNomenclatures(INomenclatureDiModel nomenclatures);
  
  public Map<String, List<String>> getRelationships();
  
  public void setRelationships(Map<String, List<String>> relationShips);
  
  public Map<String, IPropertyCollectionsWithTimestampDiModel> getPropertyCollection();
  
  public void setPropertyCollections(
      Map<String, IPropertyCollectionsWithTimestampDiModel> propertyCollection);
  
  public List<Map<String, List<IPropertyCollectionsWithTimestampDiModel>>> getEmbeddedVariants();
  
  public void setEmbeddedVariants(
      List<Map<String, List<IPropertyCollectionsWithTimestampDiModel>>> embeddedVariants);
  
  public String getBrokerURL();
  
  public void setBrokerURL(String brokerURL);
  
  public String getDestination();
  
  public void setDestination(String destination);
}
