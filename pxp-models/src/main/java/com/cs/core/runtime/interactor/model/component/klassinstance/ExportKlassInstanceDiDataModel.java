package com.cs.core.runtime.interactor.model.component.klassinstance;

import com.cs.core.runtime.interactor.model.dataintegration.INomenclatureDiModel;
import com.fasterxml.jackson.annotation.JsonAnyGetter;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ExportKlassInstanceDiDataModel implements IExportKlassInstanceDiDataModel {
  
  private static final long                                                 serialVersionUID = 1L;
  
  private Date                                                              timestamp;
  private String                                                            id;
  private INomenclatureDiModel                                              nomenclatures;
  private Map<String, List<String>>                                         relationships;
  private Map<String, IPropertyCollectionsWithTimestampDiModel>             propertyCollection;
  private List<Map<String, List<IPropertyCollectionsWithTimestampDiModel>>> embeddedVariants;
  private String                                                            brokerURL;
  private String                                                            destination;
  
  public String getBrokerURL()
  {
    return brokerURL;
  }
  
  public void setBrokerURL(String brokerURL)
  {
    this.brokerURL = brokerURL;
  }
  
  public String getDestination()
  {
    return destination;
  }
  
  public void setDestination(String destination)
  {
    this.destination = destination;
  }
  
  public List<Map<String, List<IPropertyCollectionsWithTimestampDiModel>>> getEmbeddedVariants()
  {
    return embeddedVariants;
  }
  
  public void setEmbeddedVariants(
      List<Map<String, List<IPropertyCollectionsWithTimestampDiModel>>> embeddedVariants)
  {
    this.embeddedVariants = embeddedVariants;
  }
  
  @Override
  public Date getTimestamp()
  {
    return timestamp;
  }
  
  @Override
  public void setTimestamp(Date timestamp)
  {
    this.timestamp = timestamp;
  }
  
  @Override
  public String getId()
  {
    return this.id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public INomenclatureDiModel getNomenclatures()
  {
    return nomenclatures;
  }
  
  @Override
  public void setNomenclatures(INomenclatureDiModel nomenclatures)
  {
    this.nomenclatures = nomenclatures;
  }
  
  @Override
  @JsonAnyGetter
  public Map<String, IPropertyCollectionsWithTimestampDiModel> getPropertyCollection()
  {
    return this.propertyCollection;
  }
  
  @Override
  public void setPropertyCollections(
      Map<String, IPropertyCollectionsWithTimestampDiModel> propertyCollection)
  {
    this.propertyCollection = propertyCollection;
  }
  
  @Override
  public Map<String, List<String>> getRelationships()
  {
    return this.relationships;
  }
  
  @Override
  public void setRelationships(Map<String, List<String>> relationShips)
  {
    this.relationships = relationShips;
  }
}
