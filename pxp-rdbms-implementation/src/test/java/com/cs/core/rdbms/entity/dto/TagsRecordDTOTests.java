package com.cs.core.rdbms.entity.dto;

import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.rdbms.testutil.ConfigTestUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.junit.Test;

/**
 * @author umesh.kirdat
 * @ignore marked it, removed it in development phase if need
 */
// @Ignore
public class TagsRecordDTOTests extends AbstractRDBMSDriverTests {
  
  static final boolean      VERSIONABLE        = true;
  static final boolean      DEFAULT_VALUE      = true;
  static final CouplingType COUPLING           = CouplingType.UNDEFINED;
  static final String       COUPLING_EXTENSION = "{\"1\":1,\"2\":2,\"abc\":[\"x\",\"y\"]}";
  static final int          VERSION            = 123;
  
  public TagsRecordDTOTests()
  {
  }
  
  @Test
  public void test() throws CSFormatException, RDBMSException
  {
    PropertyDTO property = ConfigTestUtils.createRandomTagProperty();
    TagsRecordDTO sample = new TagsRecordDTO(201, property);
    String json = sample.toPXON();
    System.out.println("TagsRecordDTO:" + json);
    TagsRecordDTO sampleRev = new TagsRecordDTO();
    sampleRev.fromPXON(json);
    assert (sampleRev.getProperty()
        .getIID() == property.getPropertyIID());
    String jsonRev = sampleRev.toPXON();
    System.out.println("TagsRecordDTO:" + jsonRev);
    // assertEquals(jsonRev, json);
    
  }
}
