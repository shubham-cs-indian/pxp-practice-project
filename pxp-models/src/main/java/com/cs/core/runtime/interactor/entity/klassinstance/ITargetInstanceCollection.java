package com.cs.core.runtime.interactor.entity.klassinstance;

import java.util.List;

public interface ITargetInstanceCollection extends IContentInstance {
  
  public static final String MARKET_INSTANCE_IDS    = "marketInstanceIds";
  
  public List<String> getMarketInstanceIds();
  
  public void setMarketInstanceIds(List<String> marketInstanceIds);
  
}
