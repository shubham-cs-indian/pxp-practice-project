package com.cs.core.rdbms.coupling.dto;

import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.coupling.idto.IContextualDataTransferGranularDTO;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;

public class ContextualDataTransferGranularDTO extends SimpleDTO implements IContextualDataTransferGranularDTO{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  public Long parentBaseEntityIID;
  public Long variantBaseEntityIID;
  public String contextID;
  public Long deletedVariantEntityIID;
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(
        parentBaseEntityIID != null ? JSONBuilder.newJSONField(PARENT_BASE_ENTITY_IID, parentBaseEntityIID) : JSONBuilder.VOID_FIELD,
        variantBaseEntityIID != null ? JSONBuilder.newJSONField(VARIANT_BASE_ENTITYIID, variantBaseEntityIID) : JSONBuilder.VOID_FIELD, 
        deletedVariantEntityIID!= null ?JSONBuilder.newJSONField(DELETED_VARIANT_ENTITYIID,deletedVariantEntityIID): JSONBuilder.VOID_FIELD, 
        contextID != null ? JSONBuilder.newJSONField(CONTEXT_ID, contextID) : JSONBuilder.VOID_FIELD);
  }

  @Override
  public void fromJSON(JSONContentParser parser) throws CSFormatException
  {
    parentBaseEntityIID = parser.getLong(PARENT_BASE_ENTITY_IID);
    variantBaseEntityIID = parser.getLong(VARIANT_BASE_ENTITYIID);
    contextID = parser.getString(CONTEXT_ID);
    deletedVariantEntityIID =parser.getLong(DELETED_VARIANT_ENTITYIID);
  }

  @Override
  public void setParentBaseEntityIID(Long parentBaseEntityIID)
  {
    this.parentBaseEntityIID = parentBaseEntityIID;
  }

  @Override
  public Long getParentBaseEntityIID()
  {
    return parentBaseEntityIID;
  }

  @Override
  public void setVariantBaseEntityIID(Long variantBaseEntityIID)
  {
    this.variantBaseEntityIID = variantBaseEntityIID;
  }

  @Override
  public Long getVariantBaseEntityIID()
  {
    return variantBaseEntityIID;
  }

  @Override
  public void setContextID(String contextID)
  {
    this.contextID = contextID;
  }

  @Override
  public String getContextID()
  {
    return contextID;
  }

  @Override
  public void setDeletedVariantEntityIID(Long deletedVariantEntityIID)
  {
    this.deletedVariantEntityIID=deletedVariantEntityIID;
  }

  @Override
  public Long getDeletedVariantEntityIID()
  {
    return deletedVariantEntityIID;
  }
  
}
