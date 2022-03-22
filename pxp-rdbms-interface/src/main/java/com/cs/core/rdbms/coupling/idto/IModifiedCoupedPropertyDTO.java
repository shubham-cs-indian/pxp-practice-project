package com.cs.core.rdbms.coupling.idto;

import java.util.Set;

import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingBehavior;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.rdbms.idto.ISimpleDTO;

/**
 * This DTO represent information related to coupled attribute/tags configuation
 * changed in class.
 * 
 * @author Janak.Gurme
 *
 */
public interface IModifiedCoupedPropertyDTO extends ISimpleDTO {
  
  public static final String COUPLING_BEHAVIOUR       = "couplingBehaviour";
  public static final String COUPLING_TYPE            = "couplingType";
  public static final String PROPERTY                 = "proeprty";
  public static final String CLASSIFIER_CODES         = "classifiersCodes";
  public static final String VALUE                    = "value";
  public static final String VALUE_AS_HTML            = "valueAsHtml";
  public static final String VALUE_AS_NUMBER          = "valueAsNumber";
  public static final String UNIT_SYMBOL              = "unitSymbol";
  public static final String IS_DEPENDENT             = "isDependent";
  public static final String IS_VALUE_CHANGED         = "isValueChanged";
  public static final String IS_COUPLING_TYPE_CHANGED = "isCouplingTypeChanged";
  public static final String TAG_VALUES               = "tagValues";
  
  /**
   * coupling behaviour of property
   * 
   * @param couplingBehavior
   */
  public void setCouplingBehavior(CouplingBehavior couplingBehavior);
  
  /**
   * 
   * @return coupling behaviour
   */
  public CouplingBehavior getCouplingBehavior();
  
  /**
   * 
   * @param couplingType of proeprty
   */
  public void setCouplingType(CouplingType couplingType);
  
  /**
   * 
   * @return coupling type
   */
  public CouplingType getCouplingType();
  
  /**
   * 
   * @param property modified coupled property
   */
  public void setProperty(IPropertyDTO property);
  
  /**
   * 
   * @return property DTO
   */
  public IPropertyDTO getProperty();
  
  /**
   * 
   * @param classifierCodes in which this property has been changed.
   */
  public void setClassifierCodes(Set<String> classifierCodes);
  
  /**
   * 
   * @return classifierCodes
   */
  public Set<String> getClassifierCodes();
  
  /**
   * 
   * @param value, default value of property.
   */
  public void setValue(String value);
  
  /**
   * 
   * @return value
   */
  public String getValue();
  
  /**
   * 
   * @param valueAsHtml, default valueAsHtml of property
   */
  public void setValueAsHtml(String valueAsHtml);
  
  /**
   * 
   * @return value as html
   */
  public String getValueAsHtml();
  
  /**
   * 
   * @param valueAsNumber, default valueAsNumber of property
   */
  public void setValueAsNumber(double valueAsNumber);
  
  /**
   * 
   * @return value as number
   */
  public double getValueAsNumber();
  
  /**
   * 
   * @param unitSymbol, default unitSymbol property.
   */
  public void setUnitSymbol(String unitSymbol);
  
  /**
   * 
   * @return unitSymbol
   */
  public String getUnitSymbol();
  
  /**
   * 
   * @param isDependent, property is language dependent or not
   */
  public void setIsDependent(Boolean isDependent);
  
  /**
   * 
   * @return isDependent
   */
  public Boolean isDependent();
  
  /**
   * true if default value changed
   * 
   * @param isValueChanged
   */
  public void setIsValueChanged(Boolean isValueChanged);
  
  /**
   * 
   * @return isValueChanged
   */
  public Boolean isValueChanged();
  
  /**
   * true if default coupling type changed
   * 
   * @param isCouplingTypeChanged
   */
  public void setIsCouplingTypeChanged(Boolean isCouplingTypeChanged);
  
  /**
   * 
   * @return isCouplingTypeChanged
   */
  public Boolean isCouplingTypeChanged();
  
  /**
   * default values for tags
   * 
   * @param tagValues
   */
  public void setTagValues(Set<ITagDTO> tagValues);
  
  /**
   * 
   * @return tagValues
   */
  public Set<ITagDTO> getTagValues();
  
}
