package com.cs.di.workflow.tasks;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.di.common.test.DiTestUtil;
import com.cs.di.common.test.DiMockitoTestConfig;
import com.cs.di.runtime.utils.DiUtils;
import com.cs.di.workflow.model.WorkflowModel;
import com.cs.di.workflow.model.WorkflowTaskModel;

public class SetAttributesAndTagsTaskTest extends DiMockitoTestConfig{
  
  @InjectMocks
  @Autowired
  SetAttributesAndTagsTask        setAttributesAndTagsTask;
  
  @Mock
  protected TransactionThreadData transactionThreadData;
  
  //@Before
  public void setUp2() {
    PowerMockito.mockStatic(DiUtils.class);
    transactionData = PowerMockito.mock(TransactionData.class);
    when(DiUtils.createTransactionData()).thenReturn(transactionData);
  }
  
  //@Test
  public void testValidateMethodForValidInputParamtersForTask() throws Exception {
    Map<String, Object> inputs = new HashMap<>();
    inputs.put(SetAttributesAndTagsTask.KLASS_INSTANCE_ID, "${klassInstanceId}");
    inputs.put(SetAttributesAndTagsTask.ENTITY_TYPE, "product");
    inputs.put(SetAttributesAndTagsTask.ATTRIBUTES_TYPES_MAP, new HashMap<>());
    inputs.put(SetAttributesAndTagsTask.ATTRIBUTES_VALUES_MAP, new HashMap<>());
    inputs.put(SetAttributesAndTagsTask.TAG_VALUES_MAP, new HashMap<>());
    inputs.put(SetAttributesAndTagsTask.IS_TRIGGERED_THROUGH_SCHEDULER, "false");
    List<String> listOfInvalidParams = setAttributesAndTagsTask.validate(inputs);
    assertTrue(listOfInvalidParams.isEmpty());
  }
  
  //@Test
  public void testValidateMethodForValidMissingInputParamtersForReadForTask() throws Exception {
    Map<String, Object> inputs = new HashMap<>();
    inputs.put(SetAttributesAndTagsTask.KLASS_INSTANCE_ID, null);
    inputs.put(SetAttributesAndTagsTask.ENTITY_TYPE, null);
    inputs.put(SetAttributesAndTagsTask.ATTRIBUTES_TYPES_MAP, new HashMap<>());
    inputs.put(SetAttributesAndTagsTask.ATTRIBUTES_VALUES_MAP, new HashMap<>());
    inputs.put(SetAttributesAndTagsTask.TAG_VALUES_MAP, new HashMap<>());
    inputs.put(SetAttributesAndTagsTask.IS_TRIGGERED_THROUGH_SCHEDULER, "true");
    inputs.put(SetAttributesAndTagsTask.DATA_LANGUAGE, null);
    List<String> listOfInvalidParams = setAttributesAndTagsTask.validate(inputs);
    assertTrue(!listOfInvalidParams.isEmpty());
  }
  
  //@Test
  public void updateAttributeAndTagTest() throws IOException
  {
    WorkflowModel workflowModel = new WorkflowModel(null, transactionData, null);
    Map<String, String> attributesType = new HashMap<>();
    attributesType.put("pid_attribute", CommonConstants.TEXT_ATTRIBUTE_TYPE);
    Map<String, String> attributesvalue = new HashMap<>();
    attributesvalue.put("pid_attribute", "sopan test");
    Map<String, String> tagsValue = new HashMap<>();
    tagsValue.put("statustag", "enrichmenttag");
    WorkflowTaskModel model = DiTestUtil.createTaskModel("setAttributesAndTagsTask",
        new String[] { SetAttributesAndTagsTask.ENTITY_TYPE, SetAttributesAndTagsTask.KLASS_INSTANCE_ID,
            SetAttributesAndTagsTask.ATTRIBUTES_TYPES_MAP, SetAttributesAndTagsTask.ATTRIBUTES_VALUES_MAP,
            SetAttributesAndTagsTask.TAG_VALUES_MAP, SetAttributesAndTagsTask.IS_TRIGGERED_THROUGH_SCHEDULER, SetAttributesAndTagsTask.DATA_LANGUAGE },
        new Object[] {"product", "1000029", attributesType, attributesvalue, tagsValue, "true", "en_US"});
    
    model.setWorkflowModel(workflowModel);
    setAttributesAndTagsTask.executeTask(model);
    assertTrue(model.getExecutionStatusTable()
        .isErrorOccurred() != true);
  }
}
