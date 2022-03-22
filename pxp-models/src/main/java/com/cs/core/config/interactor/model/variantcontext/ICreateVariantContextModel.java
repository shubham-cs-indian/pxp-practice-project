package com.cs.core.config.interactor.model.variantcontext;

import com.cs.core.config.interactor.model.propertycollection.IAddedTabModel;

public interface ICreateVariantContextModel extends IVariantContextModel {
  
  public static final String KLASS_ID = "klassId";
  public static final String TAB      = "tab";
  
  public String getKlassId();
  
  public void setKlassId(String klassId);
  
  public IAddedTabModel getTab();
  
  public void setTab(IAddedTabModel tab);
}
