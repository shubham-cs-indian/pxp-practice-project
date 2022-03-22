package com.cs.core.rdbms.entity.dto;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.cs.core.rdbms.dto.RDBMSRootDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityChildrenDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;

public class BaseEntityChildrenDTO extends RDBMSRootDTO implements IBaseEntityChildrenDTO {
  
  private static final long serialVersionUID = 1L;
  private String            childrenID;
  private long              childrenIID;
  
  /**
   * Enabled default constructor
   */
  public BaseEntityChildrenDTO() {
  }
  
  /**
   * Value constructor
   * @param childrenID
   * @param childrenIID
   */
  public BaseEntityChildrenDTO(String childrenID, long childrenIID)
  {
    this.childrenID = childrenID;
    this.childrenIID = childrenIID;
  }


  @Override
  public String getChildrenID()
  {
    return childrenID;
  }

  @Override
  public void setChildrenID(String childrenID)
  {
    this.childrenID = childrenID;
  }

  @Override
  public long getChildrenIID()
  {
    return childrenIID;
  }

  @Override
  public void setChildrenIID(long childrenIID)
  {
    this.childrenIID = childrenIID;
  }

  @Override
  public ICSEElement toCSExpressID() throws CSFormatException
  {
    return null;
  }
  
  @Override
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public int hashCode() {
    return new HashCodeBuilder(5, 19).append(childrenIID)
            .build();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final BaseEntityChildrenDTO other = (BaseEntityChildrenDTO) obj;
    return this.childrenIID == other.childrenIID;
  }
}
