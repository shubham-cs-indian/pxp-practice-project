package com.cs.core.rdbms.entity.dto;

import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.transactionend.handlers.dto.DependencyChangeDTO;
import com.cs.core.transactionend.handlers.dto.DependencyDTO;
import com.cs.core.transactionend.handlers.dto.IDependencyChangeDTO;
import com.cs.core.transactionend.handlers.dto.IDependencyChangeDTO.Change;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class DependencyDTOTests extends AbstractRDBMSDriverTests {

  @Test
  public void test() throws CSFormatException
  {
    DependencyChangeDTO x = new DependencyChangeDTO();
    x.setEntityIID(100);
    Map<String, Change> propertiesChange = new HashMap<>();
    propertiesChange.put("name_attribute", Change.Added);
    propertiesChange.put("shortDescriptionAttribute", Change.Deleted);
    propertiesChange.put("longDescriptionAttribute", Change.Modified);
    x.setPropertiesChange(propertiesChange);
    printJSON(x);
    DependencyChangeDTO y = new DependencyChangeDTO();
    y.fromJSON(x.toJSON());
    printJSON(y);

    Map<Long, IDependencyChangeDTO> z = new HashMap<>();
    z.put(100L, x);
    DependencyDTO dependency = new DependencyDTO();
    dependency.setDependencies(z);
    dependency.setCatalogCode("pim");
    dependency.setOrganizationCode("stdo");
    dependency.setLocaleID("en_US");
    dependency.setUserId("admin");

    printJSON(dependency);

    DependencyDTO dependency2 = new DependencyDTO();
    dependency2.fromJSON(dependency.toJSON());
    printJSON(dependency2);
  }
}
