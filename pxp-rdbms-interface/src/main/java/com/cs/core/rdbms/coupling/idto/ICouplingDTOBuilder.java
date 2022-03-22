package com.cs.core.rdbms.coupling.idto;

import com.cs.core.technical.rdbms.idto.IRootDTOBuilder;

public interface ICouplingDTOBuilder extends IRootDTOBuilder<ICouplingDTO>{
  
  public ICouplingDTO build();

}
