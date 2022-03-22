package com.cs.di.workflow.tasks;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.di.common.test.DiIntegrationTestConfig;
import com.cs.di.common.test.DiTestUtil;
import com.cs.di.runtime.entity.dao.IEntityViolationDAO;
import com.cs.di.runtime.utils.DiTransformationUtils;
import com.cs.di.runtime.utils.DiUtils;
import com.cs.di.workflow.model.WorkflowModel;
import com.cs.di.workflow.model.WorkflowTaskModel;

public class GetAttributesAndTagsTaskTest extends DiIntegrationTestConfig{
  
  @Autowired
  GetAttributesAndTagsTask        getAttributesAndTagsTask;
  @Mock
  protected ILocaleCatalogDAO     localeCatalogDAO;
  @Mock
  protected TransactionThreadData transactionThreadData;
  
  @Mock
  DiTransformationUtils           diTransformationUtils;
  
  @Mock
  IBaseEntityDAO                  baseEntityDAO;
  @Mock
  IEntityViolationDAO             propertyViolationDAO;
  

  
  
  @Mock
  IClassifierDTO iClassifierDTO;
 
   

  @Test
  public void testValidateMethodForValidInputParamtersForTask() throws Exception {
    Map<String, Object> inputs = new HashMap<>();
    inputs.put(getAttributesAndTagsTask.KLASS_INSTANCE_ID, "1234568");
    List<String> listOfInvalidParams = getAttributesAndTagsTask.validate(inputs);
    assertTrue(listOfInvalidParams.isEmpty());
  }
  
  @Test
  public void testValidateMethodForValidMissingInputParamtersForReadForTask() throws Exception {
    Map<String, Object> inputs = new HashMap<>();
    inputs.put(getAttributesAndTagsTask.KLASS_INSTANCE_ID, null);
    List<String> listOfInvalidParams = getAttributesAndTagsTask.validate(inputs);
    assertTrue(!listOfInvalidParams.isEmpty());
  }
  
  @Test
  public void getAttributeAndTagTest() throws IOException
  {
    WorkflowModel workflowModel = new WorkflowModel(null, DiUtils.createTransactionData(), null);
    List<String> tagsValuesToFetch= new ArrayList();
    
    tagsValuesToFetch.add("E_status");
    tagsValuesToFetch.add("Istrue");
    List<String> attributesValuesToFetch= new ArrayList();
    attributesValuesToFetch.add("addressattribute");
    WorkflowTaskModel model = DiTestUtil.createTaskModel("getAttributesAndTagsTask",
        new String[] { getAttributesAndTagsTask.KLASS_INSTANCE_ID, getAttributesAndTagsTask.ATTRIBUTES_VALUES_TO_FETCH, 
            getAttributesAndTagsTask.TAGS_VALUES_TO_FETCH },
        new Object[] {"1000028", attributesValuesToFetch, tagsValuesToFetch });
    
    model.setWorkflowModel(workflowModel);
    getAttributesAndTagsTask.executeTask(model);
    assertTrue(model.getExecutionStatusTable()
        .isErrorOccurred() != true);
  }
}
