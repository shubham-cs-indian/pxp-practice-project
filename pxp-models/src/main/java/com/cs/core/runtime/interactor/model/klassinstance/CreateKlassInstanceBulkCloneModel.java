package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.clone.ICreateKlassInstanceBulkCloneModel;
import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IdAndTypeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class CreateKlassInstanceBulkCloneModel implements ICreateKlassInstanceBulkCloneModel {
  
  private static final long       serialVersionUID = 1L;
  protected List<IIdAndTypeModel> contentsToClone;
  
  @Override
  public List<IIdAndTypeModel> getContentsToClone()
  {
    if (contentsToClone == null) {
      contentsToClone = new ArrayList<>();
    }
    return contentsToClone;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdAndTypeModel.class)
  public void setContentsToClone(List<IIdAndTypeModel> contentsToClone)
  {
    this.contentsToClone = contentsToClone;
  }
}
