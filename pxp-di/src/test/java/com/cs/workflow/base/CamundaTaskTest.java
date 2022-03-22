package com.cs.workflow.base;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.runtimeService;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.exception.NullValueException;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import com.cs.workflow.camunda.CamundaTask;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.di.common.test.CamundaTestConfig;

/**
 * @author mayuri.wankhade
 * 
 *         This class would test CamundaTask Every task is expected to pass
 *         through CamundaTask which is JavaDelegate class when a task is
 *         executed.
 * 
 *         uncomment @Test to execute the particular test case
 *
 */
@Configuration
public class CamundaTaskTest extends CamundaTestConfig {
  
  @Rule
  public ProcessEngineRule rule   = new ProcessEngineRule();
  
  @Rule
  public ExpectedException thrown = ExpectedException.none();
  
  @Autowired
  ApplicationContext       applicationContext;
  
  @Autowired
  CamundaTask              camundaTask;
  
  /**
   * Mock setup for ReceiverTask is loaded using task_context.cfg.xml Configured
   * in class BaseTaskTest
   * 
   * @throws CSInitializationException
   */
  @Before
  public void ReceiverTaskSetup()
  {
    camundaTask.tasksFactory = (TasksFactory) applicationContext.getBean("tasksFactory");
    System.out.println(camundaTask.tasksFactory );
    Mocks.register("camundaTask", camundaTask);
  }
  
  /**
   * This is to avoid test initialization error in case all test cases in the
   * given file are commented this is just a dummy call
   */
  @Test
  public void test()
  {
    System.out.println("dummy test");
  }
  
  /**
   * Valid Test Scenario This method deploy and start receiverTask for JMS Setup
   * 
   * @throws InterruptedException
   */
  @Test
  @Deployment(resources = { "bpmn/receiverTask_JMS.bpmn" })
  public void testJMSReceiverTaskWF() throws InterruptedException
  {
    try {
      ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("Process_JMS");
      System.out.println("testJMSReceiverTaskWF Complete ProcessInstanceId "
          + processInstance.getProcessInstanceId());
    }
    catch (java.lang.Exception e) {
      System.out.println("JUNIT Error " + e);
    }
    
  }
  
  /**
   * Error scenario test case This method deploy and start receiverTask for HOT
   * folder setup
   * 
   * @throws NullValueException
   */
  @Test
  @Deployment(resources = { "bpmn/receiverTask_HF.bpmn" })
  public void testHotFolderReceiverTaskWF() throws InterruptedException
  {
    try {
      RuntimeService runtimeService = rule.getRuntimeService();
      ProcessInstance processInstance = runtimeService
          .startProcessInstanceByKey("Process_HotFolder");
      System.out.println("testHotFolderReceiverTaskWF Complete ProcessInstanceId "
          + processInstance.getProcessInstanceId());
    }
    catch (java.lang.Exception e) {
      System.out.println("JUNIT Error " + e);
    }
  }
  
}
