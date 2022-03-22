package com.cs.core.rdbms.config.dto;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.csexpress.definition.CSEProperty;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IRecordConflictDTO;
import com.cs.core.rdbms.entity.dto.TagDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.Keyword;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

/**
 * Property Data Transfer Object
 *
 * @author vallee
 */
public class RecordConflictDTO implements IRecordConflictDTO {

  private String                    conflictSourceCode;
  private ICSECoupling.CouplingType couplingType;
  private String                    value;
  private List<ITagDTO>              tagValues;

  public RecordConflictDTO(String conflictSourceCode, ICSECoupling.CouplingType couplingType)
  {
    this.conflictSourceCode = conflictSourceCode;
    this.couplingType = couplingType;
  }

  @Override
  public String getConflictSourceCode()
  {
    return conflictSourceCode;
  }

  @Override
  public void setConflictSourceCode(String conflictSourceCode)
  {
    this.conflictSourceCode = conflictSourceCode;
  }

  @Override
  public ICSECoupling.CouplingType getCouplingType()
  {
    return couplingType;
  }

  @Override
  public void setCouplingType(ICSECoupling.CouplingType couplingType)
  {
    this.couplingType = couplingType;
  }

  @Override
  public String getValue()
  {
    return value;
  }

  @Override
  public void setValue(String value)
  {
    this.value = value;
  }

  @Override
  public List<ITagDTO> getTagValues()
  {
    return tagValues;
  }

  @Override
  public void setTagValues(List<ITagDTO> tagValues)
  {
    this.tagValues = tagValues;
  }
}

