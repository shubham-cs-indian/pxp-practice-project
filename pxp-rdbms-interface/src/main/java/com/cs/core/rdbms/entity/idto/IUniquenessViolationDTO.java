package com.cs.core.rdbms.entity.idto;

import com.cs.core.technical.rdbms.idto.IRootDTO;
import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IUniquenessViolationDTO extends IRootDTO , ISimpleDTO{

  public static final String SOURCE_IID     = "sourceIID";
  public static final String TARGET_IID     = "targetIID";
  public static final String PROPERTY_IID   = "propertyIID";
  public static final String CLASSIFIER_IID = "classifierIID";
  
  /**
   * 
   * @return IID of current BaseEntity
   */
  public long getSourceIID();
  

  /**
   * 
   * @return IID of TargetEntity
   */
  public long getTargetIID();
  
  /**
   * 
   * @return IID of violated Property
   */
  public long getPropertyIID();
  
  /**
   * 
   * @return IID of Classifier
   */
  
  public long getClassifierIID();
  
}
