package com.cs.di.common.test;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cs.core.services.CSProperties;

/**
 * Class is loading bean for Testing
 * using task_context.cfg.xml
 * and has common method used across the test classes
 * for Model creation
 * @author mayuri.wankhade
 *
 */
@ContextConfiguration(locations = { "classpath*:config/task_context.cfg.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class CamundaTestConfig {  
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    CSProperties.init(".//src//test//resources//config//junit-test.properties");
  }
}
