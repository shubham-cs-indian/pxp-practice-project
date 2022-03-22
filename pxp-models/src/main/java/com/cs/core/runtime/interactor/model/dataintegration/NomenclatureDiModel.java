package com.cs.core.runtime.interactor.model.dataintegration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;
import java.util.List;

public class NomenclatureDiModel implements INomenclatureDiModel {
  
  private static final long            serialVersionUID = 1L;
  public Date                          timestamp;
  private List<IRootNomenclatureModel> rootnomenclatures;
  
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
  public List<IRootNomenclatureModel> getRootnomenclatures()
  {
    return rootnomenclatures;
  }
  
  @Override
  @JsonDeserialize(contentAs = RootNomenclatureModel.class)
  public void setRootnomenclatures(List<IRootNomenclatureModel> rootnomenclatures)
  {
    this.rootnomenclatures = rootnomenclatures;
  }
}
