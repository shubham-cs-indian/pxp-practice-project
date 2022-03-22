package com.cs.core.rdbms.benchmark;

import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.dto.ValueRecordDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.rdbms.testutil.DataTestUtils;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Benchmark test with various DATA configuration loaded
 *
 * @author vallee
 */
 @Ignore
public class BaseEntityCreateBenchmarkTest extends AbstractRDBMSDriverTests {
  
  private static final int ENTITY_SIZE = 10; // Nb of entities to be created
  private static final int PROP_SIZE   = 5;
  
  @Before
  public void init() throws RDBMSException
  {
    super.init();
  }
  
  private void runBenchMark(PropertyDTO... props) throws RDBMSException
  {
    BaseEntityDTO[] entities = new BaseEntityDTO[ENTITY_SIZE];
    ValueRecordDTO[] values = new ValueRecordDTO[PROP_SIZE];
    for (int i = 0; i < ENTITY_SIZE; i++)
      entities[i] = DataTestUtils.newBaseEntity("Bench", true);
    // Start the benchmark here:
    long chrono = System.currentTimeMillis();
    for (int i = 0; i < ENTITY_SIZE; i++) {
      IBaseEntityDAO baseEntityDao = localeCatalogDao.openBaseEntity(entities[i]);
      for (int j = 0; j < PROP_SIZE; j++) {
        values[j] = (ValueRecordDTO) baseEntityDao.newValueRecordDTOBuilder(props[j],
            String.format("Value %d-%d", i, j)).localeID("en_US").build();
      }
      baseEntityDao.createPropertyRecords(values);
    }
    long stopChrono = System.currentTimeMillis() - chrono;
    println(String.format("Total time to create %d ms", stopChrono));
    double oneRecordChrono = ((double) stopChrono) / ENTITY_SIZE;
    println(String.format("Time to create 1 entity / %d properties %.3f ms", PROP_SIZE,
        oneRecordChrono));
    println("End of benchmark.");
  }
  
  @Ignore("run this test with SOMFY data only")
  @Test
  public void createBenchmarkWithSomfyConfig() throws RDBMSException
  {
    PropertyDTO[] props = new PropertyDTO[PROP_SIZE];
    props[0] = (PropertyDTO) localeCatalogDao.newPropertyDTO(1070782, "Subsidiaryspecific",
        IPropertyDTO.PropertyType.TEXT);
    props[1] = (PropertyDTO) localeCatalogDao.newPropertyDTO(1070784, "Link7",
        IPropertyDTO.PropertyType.TEXT);
    props[2] = (PropertyDTO) localeCatalogDao.newPropertyDTO(1070791, "Projectname",
        IPropertyDTO.PropertyType.TEXT);
    props[3] = (PropertyDTO) localeCatalogDao.newPropertyDTO(1070797, "Information",
        IPropertyDTO.PropertyType.TEXT);
    props[4] = (PropertyDTO) localeCatalogDao.newPropertyDTO(1070800, "D3Ecode",
        IPropertyDTO.PropertyType.TEXT);
    runBenchMark(props);
  }
  
  @Test
  public void createBenchmarkWithTestConfig() throws RDBMSException
  {
    PropertyDTO[] props = new PropertyDTO[PROP_SIZE];
    props[0] = (PropertyDTO) localeCatalogDao.newPropertyDTO(2000, "gtin",
        IPropertyDTO.PropertyType.TEXT);
    props[1] = (PropertyDTO) localeCatalogDao.newPropertyDTO(2008, "ShortDescription",
        IPropertyDTO.PropertyType.TEXT);
    props[2] = (PropertyDTO) localeCatalogDao.newPropertyDTO(2009, "LongDescription",
        IPropertyDTO.PropertyType.TEXT);
    props[3] = (PropertyDTO) localeCatalogDao.newPropertyDTO(2018, "Model-Name",
        IPropertyDTO.PropertyType.TEXT);
    props[4] = (PropertyDTO) localeCatalogDao.newPropertyDTO(2019, "Designer",
        IPropertyDTO.PropertyType.TEXT);
    runBenchMark(props);
  }
}
