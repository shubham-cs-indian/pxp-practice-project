package com.cs.core.rdbms.entity.idto;

import com.cs.core.technical.rdbms.idto.IRootDTOBuilder;

public interface IUniquenessViolationDTOBuilder extends IRootDTOBuilder<IUniquenessViolationDTO>{
  
  public IUniquenessViolationDTO build();
} 
