package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.model.configdetails.IGetDefaultKlassesModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class GetDefaultKlassesModel implements IGetDefaultKlassesModel {
  
  private static final long              serialVersionUID   = 1L;
  protected List<IKlassInformationModel> children           = new ArrayList<>();
  protected List<String>                 abstractKlassesIds = new ArrayList<>();
  
  @Override
  public List<IKlassInformationModel> getChildren()
  {
    return children;
  }
  
  @JsonDeserialize(contentAs = KlassInformationModel.class)
  @Override
  public void setChildren(List<IKlassInformationModel> children)
  {
    this.children = children;
  }
  
  @Override
  public List<String> getAbstractKlassesIds()
  {
    return abstractKlassesIds;
  }
  
  @Override
  public void setAbstractKlassesIds(List<String> abstractKlassesIds)
  {
    this.abstractKlassesIds = abstractKlassesIds;
  }
}
