package com.cs.core.config.interactor.model.variantcontext;

import com.cs.core.config.interactor.model.propertycollection.AddedTabModel;
import com.cs.core.config.interactor.model.propertycollection.IAddedTabModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class CreateVariantContextModel extends VariantContextModel
    implements ICreateVariantContextModel {
  
  private static final long serialVersionUID = 1L;
  public String             klassId;
  protected IAddedTabModel  tab;
  
  @Override
  public String getKlassId()
  {
    return klassId;
  }
  
  @Override
  public void setKlassId(String klassId)
  {
    this.klassId = klassId;
  }
  
  @Override
  public IAddedTabModel getTab()
  {
    return tab;
  }
  
  @Override
  @JsonDeserialize(as = AddedTabModel.class)
  public void setTab(IAddedTabModel tab)
  {
    this.tab = tab;
  }
}
