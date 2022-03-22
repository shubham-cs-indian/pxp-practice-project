package com.cs.core.bgprocess.dto;

import java.util.List;

import com.cs.core.bgprocess.idto.IInitializeBGProcessDTO;

public interface IBGPDeletePartnersDTO extends IInitializeBGProcessDTO {
  
  public List<String> getDeletedPartnersList();
  
  public void setDeletedPartnersList(List<String> deletedPartnersList);
  
}
