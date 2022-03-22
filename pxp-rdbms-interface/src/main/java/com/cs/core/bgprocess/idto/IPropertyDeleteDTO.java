package com.cs.core.bgprocess.idto;

import java.util.Set;

import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IPropertyDeleteDTO extends ISimpleDTO {

  public Set<IPropertyDTO> getProperties();

  public void setProperties(Set<IPropertyDTO> properties);

}
