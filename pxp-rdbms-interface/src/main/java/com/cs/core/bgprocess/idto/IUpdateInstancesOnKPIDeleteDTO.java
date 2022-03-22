package com.cs.core.bgprocess.idto;

import java.util.List;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IUpdateInstancesOnKPIDeleteDTO extends ISimpleDTO {

  public void setRuleCodes(List<String> ruleCodes);

  public List<String> getRuleCodes();

}