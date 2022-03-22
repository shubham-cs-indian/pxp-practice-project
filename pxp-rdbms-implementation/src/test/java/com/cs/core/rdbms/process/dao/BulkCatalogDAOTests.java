package com.cs.core.rdbms.process.dao;

import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.process.idao.IBulkCatalogDAO;
import com.cs.core.rdbms.process.idto.IBulkReportDTO;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.util.Arrays;
import java.util.Random;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author vallee
 */
@Ignore
public class BulkCatalogDAOTests extends AbstractRDBMSDriverTests {
  
  private static final String PXON_ENTITY_1 = "{ \"csid\": \"[e>BWF5Q2YCW%ID% $nature=article $type=ARTICLE]\", \"$baselocale\":\n"
      + "\"en_US\", \"_level\": 1, \"Jcxtual\": { \"csid\": \"[x>0 $cxt=Country\n"
      + "$type=VERSION_CONTEXT]\", \"start\": 54321, \"end\": 65432,\n"
      + "\"LTag\":[{\"$tag\":\"BLUE\",\"range\":50},{\"$tag\":\"WHITE\",\"range\":-50},{\"$tag\":\"RED\",\"range\":0}]\n"
      + "}, \"_name\": \"Brandt Front-loading washing machine BWF5Q2YCW\", \"$src\":\n"
      + "\"onboarding\", \"Jext\": { \"nb\": 75343, \"myExtension\": \"the content of extension\" } }";
  
  private static final String PXON_ENTITY_2 = "{ \"csid\": \"[e>BWF5Q9YCW%ID% $nature=article $type=ARTICLE]\", \"$baselocale\":\n"
      + "\"en_US\", \"_level\": 1, \"_name\": \"Brandt Front-loading washing machine\n"
      + "BWF5Q9YCW\", \"$src\": \"onboarding\"}";
  private IBulkCatalogDAO     bulkCatalogDao;
  
  @Before
  @Override
  public void init() throws RDBMSException
  {
    super.init();
    bulkCatalogDao = userSession.openBulkCatalog(session, localeCatalogDto);
  }
  
  @Test
  public void createEntity() throws CSFormatException, RDBMSException
  {
    String ID = String.format("#%04d", (new Random()).nextInt(9999));
    BaseEntityDTO entity1 = new BaseEntityDTO();
    entity1.fromPXON(PXON_ENTITY_1.replaceAll("%ID%", ID));
    BaseEntityDTO entity2 = new BaseEntityDTO();
    entity2.fromPXON(PXON_ENTITY_2.replaceAll("%ID%", ID));
    IBulkReportDTO report = bulkCatalogDao
        .createBaseEntities(Arrays.asList(new BaseEntityDTO[] { entity1, entity2 }));
    printf("created: %d, %d\n", entity1.getBaseEntityIID(), entity2.getBaseEntityIID());
  }
  
}
