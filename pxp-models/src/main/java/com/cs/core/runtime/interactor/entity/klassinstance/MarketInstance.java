package com.cs.core.runtime.interactor.entity.klassinstance;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NONE)
public class MarketInstance extends AbstractContentInstance implements IMarketInstance {
  
  private static final long serialVersionUID = 1L;
}
