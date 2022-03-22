package com.cs.core.runtime.interactor.entity.supplierinstance;

import com.cs.core.runtime.interactor.entity.klassinstance.AbstractContentInstance;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NONE)
public class SupplierInstance extends AbstractContentInstance implements ISupplierInstance {
  
  private static final long serialVersionUID = 1L;
}
