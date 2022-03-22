package com.cs.di.workflow.tasks;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import com.cs.di.workflow.events.ProcessStartEvent;
import com.cs.di.workflow.model.WorkflowTaskModel;
import org.apache.commons.lang.ObjectUtils;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.rdbms.tracking.dto.UserSessionDTO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.di.common.test.DiTestUtil;
import com.cs.di.common.test.DiMockitoTestConfig;

/**
 * 
 * @author mangesh.metkari Class help to test ProcessStartEvent component
 *
 */
public class ProcessStartEventTest extends DiMockitoTestConfig {

	@Autowired
	@InjectMocks
    ProcessStartEvent processStartEvent;

	/**
	 * test the startProcessEvent with valid transactionData and valid
	 * userSessionContext
	 * 
	 * @throws IOException
	 */
	@Test
	public void testWithTxnAndSession() throws IOException {
		WorkflowTaskModel model = DiTestUtil.createTaskModel("processStartEvent",
				new String[] { "transactionData", "userSessionDTO" },
				new Object[] {  transactionData,iUserSessionDTO});
		processStartEvent.executeTask(model);
		ITransactionData transactionDataReceived = (ITransactionData) model.getWorkflowModel().getTransactionData();
		IUserSessionDTO userSessionDTOReceived = (IUserSessionDTO) model.getWorkflowModel().getUserSessionDto();
		assertTrue(ObjectUtils.equals(transactionData, transactionDataReceived)
				&& ObjectUtils.equals(iUserSessionDTO, userSessionDTOReceived));

	}

	/**
	 * test the startProcessEvent null transactionData and with valid
	 * userSessionContext
	 * 
	 * @throws IOException
	 */
	@Test
	public void testWithNullTxnAndNullSession() throws IOException {
		WorkflowTaskModel model = DiTestUtil.createTaskModel("processStartEvent",
				new String[] { "transactionData", "userSessionDTO" }, new Object[] { null, null });
		processStartEvent.executeTask(model);
		assertTrue(model.getWorkflowModel().getTransactionData() != null
				&& model.getWorkflowModel().getUserSessionDto() != null);

	}

	/**
	 * test the startProcessEvent with valid transactionData and null
	 * userSessionContext
	 * 
	 * @throws IOException
	 */
	@Test
	public void testWithTxnAndNullSession() throws IOException {
		WorkflowTaskModel model = DiTestUtil.createTaskModel("processStartEvent",
				new String[] { "transactionData", "userSessionDTO" },
				new Object[] { transactionData , null });
		processStartEvent.executeTask(model);
		ITransactionData transactionDataReceived = (ITransactionData) model.getWorkflowModel().getTransactionData();
		IUserSessionDTO userSessionDTOReceived = (IUserSessionDTO) model.getWorkflowModel().getUserSessionDto();
		assertTrue(ObjectUtils.equals(transactionData, transactionDataReceived)
				&& ObjectUtils.equals(iUserSessionDTO, userSessionDTOReceived));
	}

	/**
	 * test the startProcessEvent with null transactionData and valid
	 * userSessionContext
	 * 
	 * @throws IOException
	 */
	@Test
	public void testNullTxnAndWithSession() throws IOException {
		WorkflowTaskModel model = DiTestUtil.createTaskModel("processStartEvent",
				new String[] { "transactionData", "userSessionDTO" },
				new Object[] { null, iUserSessionDTO });
		processStartEvent.executeTask(model);
		IUserSessionDTO userSessionDTOReceived = (IUserSessionDTO) model.getWorkflowModel().getUserSessionDto();
		assertTrue(model.getWorkflowModel().getTransactionData() != null
				&& ObjectUtils.equals(iUserSessionDTO, userSessionDTOReceived));
	}

	/**
	 * tested without using mock object
	 * 
	 * @throws IOException
	 */

	@Test
	public void testWithTxnAndSessionUserInput() throws IOException {
		ITransactionData transactionData = new TransactionData();
		transactionData.setDataLanguage("en_US");
		transactionData.setUiLanguage("en_US");
		transactionData.setOrganizationId("-1");
		transactionData.setPhysicalCatalogId("pim");
		transactionData.setEndpointId("");
		transactionData.setUserId("Mangesh");
		transactionData.setUserName("admin");
		IUserSessionDTO userSessionDTO = new UserSessionDTO();
		userSessionDTO.setSessionID("di12345");
		WorkflowTaskModel model = new WorkflowTaskModel();
		model.setTaskId("processStartEvent");
		model = DiTestUtil.createTaskModel("processStartEvent", new String[] { "transactionData", "userSessionDTO" },
				new Object[] { transactionData, userSessionDTO });
		processStartEvent.executeTask(model);
		ITransactionData transactionDataReceived = (TransactionData) model.getWorkflowModel().getTransactionData();
		IUserSessionDTO userSessionDTOReceived = (IUserSessionDTO) model.getWorkflowModel().getUserSessionDto();
		assertTrue(transactionDataReceived.getUserId().equals(transactionData.getUserId())
				&& userSessionDTOReceived.getSessionID().equals(userSessionDTO.getSessionID()));

	}
}
