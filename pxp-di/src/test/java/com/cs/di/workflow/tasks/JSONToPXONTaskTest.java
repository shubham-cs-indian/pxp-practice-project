package com.cs.di.workflow.tasks;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.rdbms.config.dto.CatalogDTO;
import com.cs.core.rdbms.config.dto.ClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO;
import com.cs.core.rdbms.entity.idto.IContextualDataDTO;
import com.cs.core.rdbms.entity.idto.IEntityRelationDTO;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.common.test.DiTestUtil;
import com.cs.di.common.test.DiMockitoTestConfig;
import com.cs.di.config.interactor.model.task.GetConfigDetailsByCodesResponseModel;
import com.cs.di.config.interactor.model.task.IGetConfigDetailsByCodesResponseModel;
import com.cs.di.config.strategy.usecase.task.IGetConfigDetailsByCodesStrategy;
import com.cs.di.runtime.utils.DiUtils;
import com.cs.di.workflow.model.WorkflowModel;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONToPXONTaskTest extends DiMockitoTestConfig {
	
  @InjectMocks
  @Autowired
  JSONToPXONTask   jsonToPxonTask;
  
  @Mock
  IGetConfigDetailsByCodesStrategy iGetConfigDetailsByCodesStrategy;
  
  @Mock
  ILocaleCatalogDAO localeCatalogDAO;
  
  @Mock
  IBaseEntityDAO baseEntityDAO;
  
  /**
   * Test case takes JSON as Input
   * and generate PXON using config 
   * data call for Valid JSON Data provided
   * @throws Exception
   */
  @Test
	public void testGeneratePXONWithValidData() throws Exception {
		try {
			prepareMockData();
			WorkflowTaskModel model = DiTestUtil.createTaskModel("jsonToPxonTask",
					new String[] { JSONToPXONTask.RECEIVED_DATA }, new Object[] { prepareMapFromJson() });
			WorkflowModel workflowModel = new WorkflowModel("process", DiUtils.createTransactionData(),
					DiUtils.createUserSessionDto());
			model.setWorkflowModel(workflowModel);
			jsonToPxonTask.generatePXON(model);
			assertTrue(model.getOutputParameters().get(JSONToPXONTask.PXON) != null);

		} catch (RDBMSException e) {
			e.printStackTrace();
		}

	}
  	
	  /**
	   * This method mocks DB calls and provide dummy response
	   * @throws Exception
	   * @throws RDBMSException
	   */
	private void prepareMockData() throws Exception, RDBMSException {
		// Prepare Mock Data for BaseEntityDTO
		CatalogDTO catalog = new CatalogDTO();
		catalog.setCode("pim");
		ClassifierDTO classifier = new ClassifierDTO();
		classifier.setClassifierType(ClassifierType.UNDEFINED);
		String baseLocaleID = "en_US";
		BaseEntityDTO iBaseEntityDTO = new BaseEntityDTO("45458", IBaseEntityIDDTO.BaseType.ARTICLE, baseLocaleID,
				catalog, classifier);
		
		//TODO:need to mock later 
		IRelationsSetDTO iRelationsSetDTO = PowerMockito.mock(IRelationsSetDTO.class);
		IEntityRelationDTO iEntityRelationDTO = PowerMockito.mock(IEntityRelationDTO.class);
		iEntityRelationDTO.setContextCode("Mayuri");
		IContextualDataDTO iContextualDataDTO = PowerMockito.mock(IContextualDataDTO.class);
		
		// Prepare Mock Data for GetConfigDetailsByCodesStrategy call
		IGetConfigDetailsByCodesResponseModel res = new GetConfigDetailsByCodesResponseModel();
		Map<String, Object> klass = new HashMap<String, Object>();
		Map<String, Object> klassDtl = new HashMap<String, Object>();
		klassDtl.put("isNature", true);
		klassDtl.put("classifierIID", 4545);
		klass.put("single_article", klassDtl);
		res.setKlass(klass);
		Map<String, String> taxonomy = new HashMap<String, String>();
		klass.put("taxonomy", "taxonomy");
		res.setTaxonomy(taxonomy);
		when(iGetConfigDetailsByCodesStrategy.execute(any())).thenReturn(res);
		
		// Prepare Mock Data for localeCatalogDAO and baseEntityDAO call
		long entityIID = 12345l;
		when(DiUtils.getLocaleCatalogDAO(any(), any(), any())).thenReturn(localeCatalogDAO);
		when(localeCatalogDAO.getEntityIID(any(String.class), any(String.class), any(String.class), any(String.class)))
				.thenReturn(entityIID);
		when(localeCatalogDAO.getEntityByIID(entityIID)).thenReturn(iBaseEntityDTO);
		when(localeCatalogDAO.openBaseEntity(any(IBaseEntityDTO.class))).thenReturn(baseEntityDAO);
		when(baseEntityDAO.newClassifierDTO(any(Long.class), any(String.class),
				any(IClassifierDTO.ClassifierType.class))).thenReturn(classifier);
	}
  
	@Test
	@Ignore
	public void transformWithcreateRelationships() throws IOException {

		IBaseEntityDTO iBaseEntityDTO = PowerMockito.mock(IBaseEntityDTO.class);
		IRelationsSetDTO iRelationsSetDTO = PowerMockito.mock(IRelationsSetDTO.class);
		IEntityRelationDTO iEntityRelationDTO = PowerMockito.mock(IEntityRelationDTO.class);
		IContextualDataDTO iContextualDataDTO = PowerMockito.mock(IContextualDataDTO.class);
		try {
			// when(localeCatalogDao.getEntityByID(any(String.class))).thenReturn(iBaseEntityDTO);
			when(DiUtils.getRelationsSetDTO(any(Long.class), any(IPropertyDTO.class), any(RelationSide.class),
					any(Long.class))).thenReturn(iRelationsSetDTO);
			when(DiUtils.getEntityRelationDTO(any(Long.class), any(Long.class), any(String.class)))
					.thenReturn(iEntityRelationDTO);
			when(iEntityRelationDTO.getContextualObject()).thenReturn(iContextualDataDTO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
  
  /**
   * Test with valid input parameters
   * @throws Exception
   */
  @Test
  public void testValidInputParams() throws Exception
  {
    Map<String, Object> inputFields = new HashMap<>();
    inputFields.put("RECEIVED_DATA", "data");
    List<String> validate = jsonToPxonTask.validate(inputFields);
    assertTrue(validate.isEmpty());
  }
  
  /**
   * Test with runtime input parameters
   * @throws Exception
   */
  @Test
  public void testRuntimeInputParams() throws Exception
  {
    Map<String, Object> inputFields = new HashMap<>();
    inputFields.put("RECEIVED_DATA", "${data}");
    List<String> validate = jsonToPxonTask.validate(inputFields);
    assertTrue(validate.isEmpty());
  }
  
  /**
   *Ttest with invalid input parameters
   * @throws Exception
   */
  @Test
  public void testInvalidInputParams() throws Exception
  {
    Map<String, Object> inputFields = new HashMap<>();
    inputFields.put("RECEIVED_DATA", "");
    List<String> validate = jsonToPxonTask.validate(inputFields);
    assertTrue(validate.contains("RECEIVED_DATA"));
  }
  
  /**
   * WF takes MAP as Input param 
   * hence preparing MAP using sample JSON
   * @return
   */
	private Map prepareMapFromJson() {
		Map<String, Object> map = null;
		ObjectMapper mapper = new ObjectMapper();
		String json;
		try {
			File file = new File("src//test//resources//JSON//testTaxoImport.json");
			json = new String(Files.readAllBytes(Paths.get(file.getPath())));
			map = mapper.readValue(json, Map.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
  
}
