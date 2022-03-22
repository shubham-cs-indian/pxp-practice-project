package com.cs.core.bgprocess.services.config;

import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.dto.PropertyDeleteDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IPropertyDeleteDTO;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.testutil.AbstractBGProcessTests;
import com.cs.core.json.JSONContent;
import static com.cs.core.printer.QuickPrinter.printTestTitle;
import static com.cs.core.printer.QuickPrinter.println;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.entity.dto.BaseEntityIDDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

public class PropertyDeleteTest extends AbstractBGProcessTests {
  
  private static final String SERVICE = "PROPERTY_DELETE";
  
  @Before
  @Override
  public void init() throws CSInitializationException, RDBMSException, CSFormatException
  {
    super.init();
    AbstractBGProcessJob.disableCallback(); // disable callback for tests
  }
  
  @Test
  public void deletePropertyBGProcess() throws CSInitializationException, Exception
  {
    printTestTitle("runSamples " + SERVICE);
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.LOW;
    
    IPropertyDeleteDTO entryData = new PropertyDeleteDTO();
    
    Set<IPropertyDTO> deletedProperty = new HashSet<>();
    
    BaseEntityIDDTO entity = (BaseEntityIDDTO) localeCatalogDao.getEntityByIID(100030L);
    IBaseEntityDAO baseEntityDao = localeCatalogDao.openBaseEntity(entity);
    
    //Attribute property
    IPropertyDTO attributeProperty1 = baseEntityDao.newPropertyDTO(2007L, "market-price", PropertyType.NUMBER);
    deletedProperty.add(attributeProperty1);
    
    IPropertyDTO attributeProperty2 = baseEntityDao.newPropertyDTO(2008L, "ShortDescription", PropertyType.TEXT);
    deletedProperty.add(attributeProperty2);
    
    IPropertyDTO attributeProperty3 = baseEntityDao.newPropertyDTO(2009L, "LongDescription", PropertyType.TEXT);
    deletedProperty.add(attributeProperty3);
    
    IPropertyDTO attributeProperty4 = baseEntityDao.newPropertyDTO(2018L, "Model-Name", PropertyType.TEXT);
    deletedProperty.add(attributeProperty4);
    
    //Tag property
    IPropertyDTO tagProperty = baseEntityDao.newPropertyDTO(2011L, "Colors", PropertyType.TAG);
    deletedProperty.add(tagProperty);
    
    //Relation property
    IPropertyDTO relationshipProperty1 = baseEntityDao.newPropertyDTO(7002L, "Family-product", PropertyType.RELATIONSHIP);
    deletedProperty.add(relationshipProperty1);
    IPropertyDTO relationshipProperty2 = baseEntityDao.newPropertyDTO(7003L, "Similar-items", PropertyType.RELATIONSHIP);
    deletedProperty.add(relationshipProperty2);
    
    entryData.setProperties(deletedProperty);
    
    println(entryData.toJSON());
    
    BGPDriverDAO.instance()
        .submitBGPProcess("Admin", SERVICE, getTestCallbackTemplateURL(), userPriority,
            new JSONContent(entryData.toJSON()));
    
    this.runJobSample(10);
  }
  
}
