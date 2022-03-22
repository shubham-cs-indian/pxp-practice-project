package com.cs.core.rdbms.config.dto;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.csexpress.definition.CSEProperty;
import com.cs.core.rdbms.config.idto.IConflictingPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IRecordConflictDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.Keyword;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Property Data Transfer Object
 *
 * @author vallee
 */
public class ConflictingPropertyDTO extends PropertyDTO implements IConflictingPropertyDTO {

  protected List<IRecordConflictDTO> conflicts = new ArrayList<>();

  public ConflictingPropertyDTO(IPropertyDTO property)
  {
    super(property);
  }

  @Override
  public List<IRecordConflictDTO> getConflicts()
  {
    return conflicts;
  }

  @Override
  public void setConflicts(List<IRecordConflictDTO> conflicts)
  {
    this.conflicts = conflicts;
  }
}
