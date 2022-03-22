package com.cs.core.rdbms.entity.dto;

import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.dto.ClassifierDTO;
import com.cs.core.rdbms.config.dto.ContextDTO;
import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.junit.Test;

// @Ignore
public class ValueRecordDTOTests extends AbstractRDBMSDriverTests {
  
  static final String                    PROPERTY_ID    = "propertyID";
  static final String                    PROPERTY_NAME  = "propertyName";
  static final IPropertyDTO.PropertyType PROPERTY_TYPE  = IPropertyDTO.PropertyType.TEXT;
  static final boolean                   LANG_DEPENDENT = true;
  static final boolean                   VERSIONABLE    = true;
  static final boolean                   DEFAULT_VALUE  = true;
  static final CouplingType              COUPLING       = CouplingType.UNDEFINED;
  static final int                       VERSION        = 123;
  static final String                    HTML           = "html";
  static final double                    NUMBER         = 3.14116;
  static final String                    EXPRESSION     = "{\"expression\":\"1+2=3\"}";
  static final String                    LOCALE_ID      = "en_US";
  static final String                    VALUE          = "value";
  static long                            propertyIID;
  ContextDTO                             CONTEXT        = new ContextDTO();
  
  public ValueRecordDTOTests()
  {
  }
  
  @Test
  public void TestNominalValueRecordDTO() throws CSFormatException, RDBMSException
  {
    System.out.println("\nTest: TestNominalValueRecordDTO");
    PropertyDTO property = (PropertyDTO) ConfigurationDAO.instance()
        .createProperty(PROPERTY_NAME, PROPERTY_TYPE);
    propertyIID = property.getIID();
    ValueRecordDTO sample = new ValueRecordDTO(100022, property, VALUE);
    sample.setValueIID(40022);
    sample.setLocaleID(LOCALE_ID);
    sample.setAsHTML(HTML);
    sample.setAsNumber(NUMBER);
    String json = sample.toPXON();
    System.out.println("Before revereted; " + json);
    ValueRecordDTO sampleRev = new ValueRecordDTO();
    sampleRev.fromPXON(json);
    assert (sampleRev.getLocaleID()
        .equals(LOCALE_ID));
    String jsonRev = sampleRev.toPXON();
    System.out.println("After revereted; " + jsonRev);
  }
  
  @Test
  public void TestCoupledValueRecordDTO() throws CSFormatException
  {
    System.out.println("\n test: TestCoupledValueRecordDTO");
    PropertyDTO property = new PropertyDTO(propertyIID, PROPERTY_NAME, PROPERTY_TYPE);
    ValueRecordDTO sample = new ValueRecordDTO(100022, property, VALUE);
    sample.setValueIID(40022);
    sample.setLocaleID(LOCALE_ID);
    
    // coupling are inserted by order of priority from lowest to highest
    // Transfer coupling
    //sample.addTransferCoupling(ICSECouplingSource.Predefined.$source, property);
    //assert (sample.getCouplingType() == CouplingType.TRANSFER);
    System.out.println("Value Record with transfer initialization; " + sample.toPXON());
    // Default value coupling
    sample.addDefaultValueCoupling(
        new ClassifierDTO(4000, "Article", IClassifierDTO.ClassifierType.CLASS), property, true);
    assert (sample.getCouplingType() == CouplingType.DYN_CLASSIFICATION);
    System.out.println("Value Record with Default Value coupling; " + sample.toPXON());
    // Relationship coupling
    sample.addRelationshipCoupling(new PropertyDTO(285, "standardArticleAssetRelationship",
        IPropertyDTO.PropertyType.RELATIONSHIP), 1, property, true);
    assert (sample.getCouplingType() == CouplingType.DYN_RELATIONSHIP);
    System.out.println("Value Record with relationship coupling; " + sample.toPXON());
    // Inheritance coupling
    sample.addInheritanceCoupling(property, true);
    assert (sample.getCouplingType() == CouplingType.DYN_INHERITANCE);
    System.out.println("Value Record with Inheritance; " + sample.toPXON());
    // If another tight coupling is added, the priority remains unchanged
    sample.addRelationshipCoupling(
        new PropertyDTO(7000, "Cross-sell", IPropertyDTO.PropertyType.RELATIONSHIP), 1, property,
        false);
    assert (sample.getCouplingType() == CouplingType.DYN_INHERITANCE);
    System.out.println("Value Record with relationship coupling; " + sample.toPXON());
    
    ValueRecordDTO sampleRev = new ValueRecordDTO();
    sampleRev.fromPXON(sample.toPXON());
    System.out.println("After revereted; " + sampleRev.toPXON());
  }
  
  @Test
  public void TestCalculatedValueRecordDTO() throws CSFormatException
  {
    System.out.println("\n test: TestCalculatedValueRecordDTO");
    PropertyDTO property = new PropertyDTO(propertyIID, PROPERTY_NAME, PROPERTY_TYPE);
    ValueRecordDTO sample = new ValueRecordDTO(100022, property, VALUE);
    sample.setLocaleID(LOCALE_ID);
    String calculation = "=[Price]*(1.0 -[Rebate]) + $parent.[SupplierBonus] || ' (at retailer condition)'";
    sample.addCalculation(calculation);
    ValueRecordDTO sampleRev = new ValueRecordDTO();
    sampleRev.fromPXON(sample.toPXON());
    System.out.println("With Calculation; " + sampleRev.toPXON());
  }
}
