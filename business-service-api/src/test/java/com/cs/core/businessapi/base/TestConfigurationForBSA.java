package com.cs.core.businessapi.base;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.cs.core.rdbms.config.dto.ContextDTO;
import com.cs.core.rdbms.config.idao.IConfigurationDAO;
import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.rdbms.entity.dto.ContextualDataDTO;
import com.cs.core.rdbms.iapp.IRDBMSAppDriver;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.SessionContext;
import com.cs.core.runtime.interactor.model.logger.InteractorData;
import com.cs.core.runtime.interactor.model.logger.InteractorThreadData;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.runtime.utils.DiUtils;
import com.cs.di.runtime.utils.DiValidationUtil;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({ "jdk.internal.reflect.*", "javax.management.*" })
@PrepareForTest({ DiUtils.class, DiValidationUtil.class, RDBMSUtils.class, ContextualDataDTO.class,RDBMSAppDriverManager.class,
  TransactionSynchronizationManager.class})
@SuppressStaticInitializationFor("com.cs.di.runtime.utils.DiUtils")
public abstract class TestConfigurationForBSA  {
  
//  protected static UserSessionDTO        iUserSessionDTO;
  protected static TransactionData       transactionData;
//  protected static LocaleCatalogDAO      localeCatalogDao = null;
  protected static TransactionThreadData transactionThreadData;
  protected static ISessionContext       context;
  protected static InteractorThreadData  interactorData;
  protected static RDBMSComponentUtils   rdbmsComponentUtils;
  
	@BeforeClass
	public static void setUp() throws RDBMSException, Exception {

		try {
			PowerMockito.mockStatic(DiUtils.class);
			PowerMockito.mockStatic(RDBMSUtils.class);
			PowerMockito.mockStatic(RDBMSUtils.class);
			PowerMockito.mockStatic(RDBMSAppDriverManager.class);

			transactionData = PowerMockito.mock(TransactionData.class);
			transactionThreadData = PowerMockito.mock(TransactionThreadData.class);
			context = PowerMockito.mock(ISessionContext.class);
			interactorData = PowerMockito.mock(InteractorThreadData.class);
			rdbmsComponentUtils = PowerMockito.mock(RDBMSComponentUtils.class);
//			iUserSessionDTO = createUserSessionDto();
			IConfigurationDAO iConfigurationDAO = PowerMockito.mock(IConfigurationDAO.class);
			IContextDTO iContextDTO = PowerMockito.mock(ContextDTO.class);
			transactionData = createTransactionData();
//			localeCatalogDao = (LocaleCatalogDAO) DiUtils.createLocaleCatalogDAO(iUserSessionDTO, transactionData);
//			when(DiUtils.createUserSessionDto()).thenReturn(iUserSessionDTO);
			transactionThreadData = PowerMockito.mock(TransactionThreadData.class);
			context = PowerMockito.mock(ISessionContext.class);
			interactorData = PowerMockito.mock(InteractorThreadData.class);
			rdbmsComponentUtils = PowerMockito.mock(RDBMSComponentUtils.class);
			when(transactionThreadData.getTransactionData()).thenReturn(transactionData);
           //when(DiUtils.createTransactionData()).thenReturn(transactionData);
			when(context.getUserId()).thenReturn("userId");
			when(context.getUserName()).thenReturn("userName");
			when(interactorData.getInteractorData()).thenReturn(new InteractorData());
          //when(DiUtils.createLocaleCatalogDAO(iUserSessionDTO, transactionData)).thenReturn(localeCatalogDao);

			when(RDBMSUtils.newConfigurationDAO()).thenReturn(iConfigurationDAO);
			when(iConfigurationDAO.createContext(any(), any())).thenReturn(iContextDTO);
			when(RDBMSUtils.getContextDTO(any(), any())).thenReturn(iContextDTO);
			when(RDBMSAppDriverManager.getDriver()).thenReturn(PowerMockito.mock(IRDBMSAppDriver.class));
			PowerMockito.mockStatic(TransactionSynchronizationManager.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
  
	public static ISessionContext createSessionContext() {
		ISessionContext sessionContext = new SessionContext();
		sessionContext.setUserId("userId");
		sessionContext.setUserName("username");
		return sessionContext;
	}
	
	public static TransactionData createTransactionData() {
		TransactionData transactionData = new TransactionData();
		transactionData.setDataLanguage("en_US");
		transactionData.setUiLanguage("en_US");
		transactionData.setEndpointId("");
		transactionData.setPhysicalCatalogId("pim");
		transactionData.setUserId("backgrounduserstandardorganization");
		transactionData.setUserName("BackgroundUser_StandardOrganization");
		transactionData.setOrganizationId("stdo");
		return transactionData;
	}

//	public static UserSessionDTO createUserSessionDto() {
//		return (UserSessionDTO) DiUtils.createUserSessionDto();
//	}
//
//	public static LocaleCatalogDAO createLocaleCatalogDAO(UserSessionDTO iUserSessionDTO,
//			TransactionData transactionData) {
//		return (LocaleCatalogDAO) DiUtils.createLocaleCatalogDAO(iUserSessionDTO, transactionData);
//	}
	 
}
