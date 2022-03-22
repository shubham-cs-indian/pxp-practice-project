package com.cs.core.bgprocess.idto;

import java.util.Set;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface ITagValueDeleteDTO extends ISimpleDTO {

  public void setTagValues(Set<String> tagValues);

  public Set<String> getTagValues();

}
