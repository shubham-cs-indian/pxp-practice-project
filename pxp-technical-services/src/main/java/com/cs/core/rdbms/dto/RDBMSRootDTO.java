package com.cs.core.rdbms.dto;

import com.cs.core.technical.rdbms.idto.IRootDTO;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A root DTO that manages an internal key from the RDBMS
 *
 * @author vallee
 */
public abstract class RDBMSRootDTO extends RootDTO implements IRootDTO {

  private long iid = 0; // The RDBMS internal key of the object

  /**
   * Default constructor for inherited DTOs only
   */
  protected RDBMSRootDTO() {
  }

  /**
   * Constructor with IID initialization for inherited DTOs
   *
   * @param iid
   */
  protected RDBMSRootDTO(long iid) {
    this.iid = iid;
  }

  @Override
  public boolean isNull() {
    return iid == 0L;
  }

  /**
   * @return the internal key of the object
   */
  public long getIID() {
    return iid;
  }

  /**
   * @param pIID overwritten internal key of the object
   */
  public void setIID(long pIID) {
    iid = pIID;
  }

  public int compareTo(Object t) {
    return new CompareToBuilder().append(iid,((RDBMSRootDTO) t).getIID()).toComparison();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(5, 7).append(iid)
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
    final RDBMSRootDTO other = (RDBMSRootDTO) obj;
    return this.iid > 0 && this.iid == other.iid;
  }
}
