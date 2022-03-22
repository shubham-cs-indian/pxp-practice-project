package com.cs.core.rdbms.config.dto;

import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.rdbms.config.idto.IRootConfigDTO;
import com.cs.core.rdbms.dto.RootDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement.Keyword;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Root implementation of all configuration DTOs (sharing code has common property)
 *
 * @author vallee
 */
public abstract class RootConfigDTO extends RootDTO implements IRootConfigDTO {
  
  private String code = "";
  
  /**
   * Enabled empty constructor
   */
  protected RootConfigDTO()
  {
  }
  
  /**
   * Value constructor for inherited DTO classes
   *
   * @param code
   */
  protected RootConfigDTO(String code)
  {
    this.code = code;
  }
  
  /**
   * Copy constructor
   *
   * @param source an other configuration DTO of same type
   */
  protected RootConfigDTO(IRootConfigDTO source)
  {
    code = source.getCode();
  }
  
  /**
   * Constructor from a result set returning the code property
   *
   * @param parser
   * @param prefix the prefix used by standardization for Code
   * @throws SQLException
   */
  protected RootConfigDTO(IResultSetParser parser, String prefix) throws SQLException
  {
    code = parser.getString(String.format("%sCode", prefix));
  }
  
  /**
   * Extract a list of Codes from a series of configuration DTOs
   *
   * @param configs the series of configuration DTOs
   * @return the corresponding list of Codes
   */
  public static Set<String> getConfigCodes(IRootConfigDTO... configs)
  {
    Set<String> list = new TreeSet<>();
    for (IRootConfigDTO config : configs) {
      list.add(config.getCode());
    }
    return list;
  }
  
  /**
   * Read code from a CSE expression
   *
   * @param object
   * @throws CSFormatException
   */
  protected void fromCSExpression(CSEObject object) throws CSFormatException
  {
    code = object.getCode();
  }
  
  /**
   * Initialize tracking information with Code
   *
   * @param object the object to be initialized
   * @param type the type of configuration object
   * @return the submitted object
   */
  protected CSEObject initCSExpression(CSEObject object, String type)
  {
    object.setCode(code);
    if (!type.isEmpty()) {
      object.setSpecification(Keyword.$type, type);
    }
    return object;
  }
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  public void setCode(String code)
  {
    this.code = code;
  }
  
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder(5, 13).append(code).build();
  }
  
  @Override
  public int compareTo(Object t)
  {
    RootConfigDTO that = ((RootConfigDTO) t);
    if (!code.isEmpty() || !that.code.isEmpty()) {
      return new CompareToBuilder().append(code, that.code).toComparison();
    }
    return 0;
  }
  
  @Override
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    // at least one of the IID, ID or Code is equal
    final RootConfigDTO other = (RootConfigDTO) obj;
    return !this.code.isEmpty() && new EqualsBuilder().append(this.code, other.code).isEquals();
  }
}
