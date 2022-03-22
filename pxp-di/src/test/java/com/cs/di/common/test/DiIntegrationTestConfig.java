package com.cs.di.common.test;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cs.core.services.CSProperties;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.MessageType;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;

/**
 * Class is loading bean for Testing using task_context.cfg.xml and has common
 * method used across the test classes for Model creation FOR INTEGRATION
 * TESTING USING THIS CLASS
 * 
 * @author mayuri.wankhade
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:config/test_applicationContext.xml" })
public abstract class DiIntegrationTestConfig extends AbstractRDBMSDriverTests {
  
  /*  @Configuration
  static class ContextConfiguration {
    
    @Autowired
    protected String orientDBUser;
    
    @Autowired
    protected String orientDBPassword;
    
    @Bean
    public Authenticator getAuthenticator()
    {
      System.out.println(orientDBUser);
      return new Authenticator()
      {
        
        protected PasswordAuthentication getPasswordAuthentication()
        {
          return new PasswordAuthentication(orientDBUser, orientDBPassword.toCharArray());
        }
      };
    }
  }*/
  
  @Before
  public void init() throws RDBMSException
  {
    super.init();
  }
  
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    CSProperties.init(".//src//test//resources//config//junit-test.properties");
  }
  
  /**
   * This method create the task model for testing
   * 
   * @param taskId
   * @param keys
   * @param values
   * @return WorkflowTaskModel
   */
  public WorkflowTaskModel createTaskModel(String taskId, String[] keys, Object[] values)
  {
    WorkflowTaskModel model = new WorkflowTaskModel();
    model.setTaskId(taskId);
    for (int i = 0; i < keys.length; i++) {
      model.getInputParameters()
          .put(keys[i], values[i]);
    }
    return model;
  }
  
  /**
   * This method verifies the actual message received during test case exceution
   * matches with expecetd message if YES - returns true if NO - returns false
   * 
   * @param model
   * @param messageType
   * @param code
   * @param message
   * @return true/false
   */
  public boolean verifyExecutionStatus(WorkflowTaskModel model, MessageType messageType,
      MessageCode code, String message)
  {
    Map<MessageType, ?> map = model.getExecutionStatusTable()
        .getExecutionStatusTableMap();
    List<IOutputExecutionStatusModel> list = (List<IOutputExecutionStatusModel>) map
        .get(messageType);
    for (IOutputExecutionStatusModel statusMode : list) {
      if (statusMode.toString()
          .equals(message)) {
        return true;
      }
    }
    return false;
  }
}
