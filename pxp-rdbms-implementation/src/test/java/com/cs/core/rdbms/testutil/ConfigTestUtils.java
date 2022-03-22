package com.cs.core.rdbms.testutil;

import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.dto.ClassifierDTO;
import com.cs.core.rdbms.config.dto.ContextDTO;
import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IContextDTO.ContextType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.ITagValueDTO;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.util.Random;

/**
 * Helper to access or create new configuration objects
 *
 * @author vallee
 */
public class ConfigTestUtils {
  
  public static String newRandomCode(String prefix)
  {
    return prefix + "#" + (new Random()).nextInt(10000);
  }
  
  public static ClassifierDTO getArticleClass() throws RDBMSException
  {
    return (ClassifierDTO) ConfigurationDAO.instance()
        .createClassifier("Article", ClassifierType.CLASS);
  }
  
  public static ClassifierDTO getNonNatureClass() throws RDBMSException
  {
    return (ClassifierDTO) ConfigurationDAO.instance()
        .createClassifier("Cross-Selling", ClassifierType.CLASS);
  }
  
  public static ClassifierDTO createRandomTaxonomy() throws RDBMSException
  {
    String taxoID = newRandomCode("Taxo");
    return (ClassifierDTO) ConfigurationDAO.instance()
        .createClassifier(taxoID, ClassifierType.TAXONOMY);
  }
  
  public static ClassifierDTO createRandomClass() throws RDBMSException
  {
    String taxoID = newRandomCode("CLASS");
    return (ClassifierDTO) ConfigurationDAO.instance()
        .createClassifier(taxoID, ClassifierType.CLASS);
  }
  
  public static PropertyDTO getTextProperty(String ID) throws RDBMSException
  {
    return (PropertyDTO) ConfigurationDAO.instance()
        .createProperty(ID, PropertyType.TEXT);
  }
  
  public static PropertyDTO createRandomTextProperty() throws RDBMSException
  {
    String ID = newRandomCode("TEXT");
    return (PropertyDTO) ConfigurationDAO.instance()
        .createProperty(ID, PropertyType.TEXT);
  }
  
  public static PropertyDTO createRandomTagProperty() throws RDBMSException
  {
    String ID = newRandomCode("TAG");
    return (PropertyDTO) ConfigurationDAO.instance()
        .createProperty(ID, PropertyType.TAG);
  }
  
  public static PropertyDTO createRandomRelationship() throws RDBMSException
  {
    String ID = newRandomCode("R");
    return (PropertyDTO) ConfigurationDAO.instance()
        .createProperty(ID, PropertyType.RELATIONSHIP);
  }
  
  public static PropertyDTO getTagsProperty(String ID) throws RDBMSException
  {
    return (PropertyDTO) ConfigurationDAO.instance()
        .createProperty(ID, PropertyType.TAG);
  }
  
  public static ContextDTO createRandomValueContext() throws RDBMSException
  {
    String ID = newRandomCode("ValueCTX");
    return (ContextDTO) ConfigurationDAO.instance()
        .createContext(ID, ContextType.ATTRIBUTE_CONTEXT);
  }
  
  public static ITagValueDTO createTagValue(String tagValueID) throws RDBMSException
  {
    return ConfigurationDAO.instance()
        .createTagValue(tagValueID, "Colors");
  }
  
  public static ContextDTO createRandomRelationshipContext() throws RDBMSException
  {
    String ID = newRandomCode("ValueCTX");
    return (ContextDTO) ConfigurationDAO.instance()
        .createContext(ID, ContextType.RELATIONSHIP_VARIANT);
  }
  
  public static ITagValueDTO createTagValue(String tagValueID, long propertyIID)
      throws RDBMSException
  {
    return ConfigurationDAO.instance()
        .createTagValue(tagValueID, propertyIID);
  }
  
  public static PropertyDTO createRandomCalculatedProperty() throws RDBMSException
  {
    String id = newRandomCode("CAL");
    return (PropertyDTO) ConfigurationDAO.instance()
        .createProperty(id, PropertyType.CALCULATED);
  }
}
