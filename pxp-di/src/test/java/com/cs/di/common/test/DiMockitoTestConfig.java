package com.cs.di.common.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.cs.core.rdbms.config.dto.ContextDTO;
import com.cs.core.rdbms.config.idao.IConfigurationDAO;
import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.dto.ContextualDataDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.dto.UserSessionDTO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.runtime.utils.DiUtils;
import com.cs.di.runtime.utils.DiValidationUtil;

/**
 * Class is loading bean for Testing 
 * @author mayuri.wankhade
 *
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore({ "jdk.internal.reflect.*", "javax.management.*" })
@PrepareForTest({ DiUtils.class, DiValidationUtil.class, RDBMSUtils.class, ContextualDataDTO.class })
@SuppressStaticInitializationFor("com.cs.di.runtime.utils.DiUtils")
public abstract class DiMockitoTestConfig  {
	
  protected static IUserSessionDTO  iUserSessionDTO;
  protected static ITransactionData transactionData;
  //protected static ILocaleCatalogDAO     localeCatalogDao = null;
  
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		CSProperties.init(".//src//test//resources//config//junit-test.properties");
	}
	/**
   * Mocking method which is commonly used all over
   * 
   * @throws RDBMSException
   * @throws Exception
   */
	@BeforeClass
  public static void setUp() throws RDBMSException, Exception
  {
    PowerMockito.mockStatic(DiUtils.class);
    PowerMockito.mockStatic(RDBMSUtils.class);
    iUserSessionDTO = createUserSessionDto();
    when(DiUtils.createUserSessionDto()).thenReturn(iUserSessionDTO);
    transactionData = createTransactionData();
    when(DiUtils.createTransactionData()).thenReturn(transactionData);
    //localeCatalogDao = PowerMockito.mock(ILocaleCatalogDAO.class);
    //when(DiUtils.createLocaleCatalogDAO(iUserSessionDTO, transactionData)).thenReturn(localeCatalogDao);
    IConfigurationDAO iConfigurationDAO = PowerMockito.mock(IConfigurationDAO.class);
    PowerMockito.mockStatic(RDBMSUtils.class);
    when(RDBMSUtils.newConfigurationDAO()).thenReturn(iConfigurationDAO);
    IContextDTO iContextDTO = PowerMockito.mock(ContextDTO.class);
    when(iConfigurationDAO.createContext(any(), any())).thenReturn(iContextDTO);
    when(RDBMSUtils.getContextDTO(any(), any())).thenReturn(iContextDTO);
    IBaseEntityDTO baseEntityDTO = PowerMockito.mock(BaseEntityDTO.class);
    //when(localeCatalogDao.getEntityByID(any(String.class))).thenReturn(baseEntityDTO);
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
	
	public static IUserSessionDTO createUserSessionDto() {
		IUserSessionDTO user = new UserSessionDTO();
		user.setSessionID("1234");
		return user;
	}

}
