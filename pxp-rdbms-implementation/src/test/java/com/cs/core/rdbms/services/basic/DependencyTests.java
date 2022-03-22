package com.cs.core.rdbms.services.basic;

import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.transactionend.handlers.dto.IDeltaInfoDTO;
import com.cs.core.transactionend.handlers.dto.IDependencyDTO;
import com.cs.core.transactionend.services.handlers.DeltaExtractor;
import com.cs.core.transactionend.services.handlers.DependencyTracer;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class DependencyTests extends AbstractRDBMSDriverTests {

  @Before
  public void init() throws RDBMSException
  {
    super.init();
  }

  @Test
  public void deltaExtractorTests() throws RDBMSException
  {
    Map<Long, IDeltaInfoDTO> delta = DeltaExtractor.getDelta("120012");
    assert(delta.containsKey(100003L));
  }

  @Test
  public void dependencyTracerTests() throws RDBMSException
  {
    Map<Long, IDeltaInfoDTO> delta = DeltaExtractor.getDelta("120012");
    IDependencyDTO dependency = DependencyTracer.traceDependents(delta, "de_DE");

    assert(dependency.getDependencies().get(100001L) != null);
    assert(dependency.getDependencies().get(100004L) != null);
  }
}
