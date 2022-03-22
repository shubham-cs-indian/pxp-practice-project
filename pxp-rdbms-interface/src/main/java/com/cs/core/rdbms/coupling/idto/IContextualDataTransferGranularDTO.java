package com.cs.core.rdbms.coupling.idto;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IContextualDataTransferGranularDTO extends ISimpleDTO {
  
  public static final String PARENT_BASE_ENTITY_IID = "parentBaseEntityIID";
  public static final String VARIANT_BASE_ENTITYIID = "variantBaseEntityIID";
  public static final String CONTEXT_ID             = "contextID";
  public static final String DELETED_VARIANT_ENTITYIID = "deletedVariantEntityIID";
  
  public void setParentBaseEntityIID(Long parentBaseEntityIID);
  
  public Long getParentBaseEntityIID();
  
  public void setVariantBaseEntityIID(Long variantBaseEntityIID);
  
  public Long getVariantBaseEntityIID();
  
  public void setContextID(String contextID);
  
  public String getContextID();
  
  public void setDeletedVariantEntityIID(Long deletedVariantEntityIID);
  
  public Long getDeletedVariantEntityIID();
}
