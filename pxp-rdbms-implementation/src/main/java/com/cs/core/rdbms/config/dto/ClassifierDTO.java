package com.cs.core.rdbms.config.dto;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * Classifier Data Transfer Object
 *
 * @author vallee
 */
public class ClassifierDTO extends RootConfigDTO implements IClassifierDTO, Serializable {
  
  private static final String                        CLASSIFIER_PREFIX = "classifier";
  private              long                          classifierIID     = 0L;
  private              IClassifierDTO.ClassifierType classifierType    = IClassifierDTO.ClassifierType.UNDEFINED;
  private              List<Long>                    hierarchyIIDs      = new ArrayList<>();

  /**
   * Enabled default constructor
   */
  public ClassifierDTO()
  {
  }
  
  /**
   * Value constructor
   *
   * @param classifierIID
   * @param classifierCode
   * @param type
   */
  public ClassifierDTO(long classifierIID, String classifierCode, ClassifierType type)
  {
    super(classifierCode);
    this.classifierIID = classifierIID;
    this.classifierType = type;
  }
  
  /**
   * This constructor is getting used at the time of creating taxonomy classifier. 
   * @param classifierIID
   * @param classifierCode
   * @param type
   * @param parentIIDs
   */
  
  public ClassifierDTO(Long classifierIID, String classifierCode, ClassifierType type, List<Long> parentIIDs)
  {
    super(classifierCode);
    this.classifierIID = classifierIID;
    this.classifierType = type;
    this.hierarchyIIDs = parentIIDs;
  }

  @Override
  public void setHierarchyIIDs(List<Long> hierarchyIIDs)
  {
    this.hierarchyIIDs = hierarchyIIDs;
  }

  @Override
  public List<Long> getHierarchyIIDs()
  {
    return hierarchyIIDs;
  }

  /**
   * Constructor from a result set
   *
   * @param parser
   * @throws java.sql.SQLException
   * @throws RDBMSException 
   */
  public ClassifierDTO(IResultSetParser parser) throws SQLException, RDBMSException
  {
    super(parser, CLASSIFIER_PREFIX);
    this.classifierIID = parser.getLong("classifierIID");
    this.classifierType = ClassifierType.valueOf(parser.getInt("classifierType"));
    try {
      this.hierarchyIIDs = Arrays.asList(parser.getLongArray("hierarchyiids"));
    }
    catch (CSFormatException e) {
      throw new RDBMSException(0, "", "Cannot convert to array of Long from given content type");
    }
  }
  

  public static String getCacheCode(String classifierCode)
  {
    return String.format("%c:%s", CSEObject.CSEObjectType.Classifier.letter(), classifierCode);
  }
  
  public static String getCacheCode(long classifierIID)
  {
    return String.format("%c:%d", CSEObject.CSEObjectType.Classifier.letter(), classifierIID);
  }
  
  /**
   * Factory method from the result of aggregated information of classifiers
   *
   * @param classifierIIDs
   *          the set of IIDs as a list of comma separated numbers
   * @return the set of built Classifiers
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public static Set<IClassifierDTO> newClassifierDTOs(String classifierIIDs)
      throws NumberFormatException, RDBMSException
  {
    Set<IClassifierDTO> result = new TreeSet<>();
    String[] iids = classifierIIDs.split(",");
    for (int i = 0; i < iids.length && !classifierIIDs.isEmpty(); i++) {
      IClassifierDTO classifier = ConfigurationDAO.instance()
          .getClassifierByIID(Long.parseLong(iids[i]));
      result.add(classifier);
    }
    return result;
  }
  
  /**
   * Extract a list of type ordinal from a series of classifier DTOs
   *
   * @param classifiers
   *          the series of classifiers
   * @return the corresponding list of type ordinals
   */
  public static Set<Integer> getClassifierTypes(IClassifierDTO... classifiers)
  {
    Set<Integer> list = new TreeSet<>();
    for (IClassifierDTO classifier : classifiers) {
      list.add(classifier.getClassifierType()
          .ordinal());
    }
    return list;
  }
  
  @Override
  public ICSEElement toCSExpressID()
  {
    CSEObject cse = new CSEObject(CSEObjectType.Classifier);
    cse.setIID(classifierIID);
    return initCSExpression(cse, classifierType.toString());
  }
  
  @Override
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException
  {
    CSEObject ccse = (CSEObject) cse;
    fromCSExpression(ccse);
    try {
     IClassifierDTO classifierByCode = ConfigurationDAO.instance().getClassifierByCode(ccse.getCode());
     classifierIID = classifierByCode.getIID();
     this.classifierType = classifierByCode.getClassifierType();
    } catch (RDBMSException ex) {
      throw new CSFormatException(String.format(
              "Classifier code %d from expression %s doesn't not exist", ccse.getCode(), cse.toString()));
    }
  }
  
  @Override
  public ClassifierType getClassifierType()
  {
    return classifierType;
  }
  
  /**
   * @param classifierType
   *          overwritten classifier type
   */
  public void setClassifierType(ClassifierType classifierType)
  {
    this.classifierType = classifierType;
  }
  
  /**
   * @param iid
   *          overwritten IID
   */
  public void setIID(long iid)
  {
    classifierIID = iid;
  }
  
  @Override
  public long getClassifierIID()
  {
    return classifierIID;
  }
}
