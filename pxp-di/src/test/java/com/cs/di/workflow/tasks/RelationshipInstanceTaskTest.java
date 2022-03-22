package com.cs.di.workflow.tasks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.di.common.test.DiTestUtil;
import com.cs.di.common.test.DiMockitoTestConfig;
import com.cs.di.runtime.utils.DiUtils;
import com.cs.di.workflow.model.WorkflowTaskModel;

public class RelationshipInstanceTaskTest extends DiMockitoTestConfig{
  
  private static final String UPSERT = "UPSERT";
  private static final String READ = "READ";
  private static final String DELETE = "DELETE";
  private static final String RELATIONSHIP_ID = "standardArticleAssetRelationship";
  
  @Autowired
  @InjectMocks
  RelationshipInstanceTask relationshipInstanceTask;
  
  @Mock
  protected TransactionThreadData transactionThreadData;

  TransactionData transactionData;

//  @Before
  public void setUp2() {
    PowerMockito.mockStatic(DiUtils.class);
    transactionData = PowerMockito.mock(TransactionData.class);
    when(DiUtils.createTransactionData()).thenReturn(transactionData);
  }
  
  //@Test
  public void testValidateMethodForValidInputParamtersForUpsert() throws Exception {
    Map<String, Object> inputs = new HashMap<>();
    inputs.put(RelationshipInstanceTask.SIDE_1_IID, "100");
    inputs.put(RelationshipInstanceTask.SIDE_2_IIDS, Arrays.asList("8456"));
    inputs.put(RelationshipInstanceTask.RELATIONSHIP_CODE, "200");
    inputs.put(RelationshipInstanceTask.SOURCE_TYPE, "Product");
    inputs.put(RelationshipInstanceTask.ACTION, UPSERT);
    inputs.put(RelationshipInstanceTask.CONTEXT,
        "[\r\n" + "  {\r\n" + "    \"side2Id\": \"1000000\",\r\n" + "    \"context\": {\r\n"
            + "      \"contextId\": \"LV\",\r\n" + "      \"tags\": {\r\n" + "        \"prc\": [\r\n"
            + "          \"low\"\r\n" + "        ]\r\n" + "      },\r\n" + "      \"timeRange\": {\r\n"
            + "        \"from\": \"2020-03-02\",\r\n" + "        \"to\": \"2020-03-03\"\r\n" + "      }\r\n"
            + "    }\r\n" + "  },\r\n" + "  {\r\n" + "    \"side2Id\": \"1000008\",\r\n"
            + "    \"context\": {\r\n" + "      \"contextId\": \"LV\",\r\n" + "      \"tags\": {\r\n"
            + "        \"prc\": [\r\n" + "          \"med\"\r\n" + "        ]\r\n" + "      },\r\n"
            + "      \"timeRange\": {\r\n" + "        \"from\": \"2020-03-02\",\r\n"
            + "        \"to\": \"2020-03-03\"\r\n" + "      }\r\n" + "    }\r\n" + "  }\r\n" + "]");
    List<String> listOfValidParams = relationshipInstanceTask.validate(inputs);
    assertTrue(listOfValidParams.isEmpty());
  }
  
  //@Test
  public void testValidateMethodForValidInputParamtersForReadOrDelete() throws Exception {
    Map<String, Object> inputs = new HashMap<>();
    inputs.put(RelationshipInstanceTask.SIDE_1_IID, "${klassInstanceId}");
    inputs.put(RelationshipInstanceTask.SIDE_2_IIDS, Arrays.asList("8456"));
    inputs.put(RelationshipInstanceTask.RELATIONSHIP_CODE, "200");
    inputs.put(RelationshipInstanceTask.SOURCE_TYPE, "Product");
    inputs.put(RelationshipInstanceTask.ACTION, READ);
    List<String> listOfInvalidParams = relationshipInstanceTask.validate(inputs);
    assertTrue(listOfInvalidParams.isEmpty());
  }
  
//  @Test
  public void testValidateMethodForMissingInputParamtersForUpsert() throws Exception {
    Map<String, Object> inputs = new HashMap<>();
    inputs.put(RelationshipInstanceTask.SIDE_1_IID, "100");
    inputs.put(RelationshipInstanceTask.SIDE_2_IIDS, Arrays.asList("8456"));
    inputs.put(RelationshipInstanceTask.SOURCE_TYPE, "Product");
    inputs.put(RelationshipInstanceTask.ACTION, UPSERT);
    List<String> listOfInvalidParams = relationshipInstanceTask.validate(inputs);
    assertFalse(listOfInvalidParams.isEmpty());
  }
  
//  @Test
  public void testValidateMethodForMissingInputParamtersForReadOrDelete() throws Exception {
    Map<String, Object> inputs = new HashMap<>();
    inputs.put(RelationshipInstanceTask.SIDE_1_IID, "100");
    inputs.put(RelationshipInstanceTask.SIDE_2_IIDS, Arrays.asList("8456"));
    inputs.put(RelationshipInstanceTask.RELATIONSHIP_CODE, "200");
    inputs.put(RelationshipInstanceTask.ACTION, DELETE);
    List<String> listOfInvalidParams = relationshipInstanceTask.validate(inputs);
    assertFalse(listOfInvalidParams.isEmpty());
  }
  
  //@Test
  public void testWithValidInputForUpsert()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("RelationshipInstanceTask",
        new String[] { "transactionData", RelationshipInstanceTask.SIDE_1_IID,
            RelationshipInstanceTask.SIDE_2_IIDS,
            RelationshipInstanceTask.RELATIONSHIP_CODE,
            RelationshipInstanceTask.SOURCE_TYPE,
            RelationshipInstanceTask.ACTION,
            RelationshipInstanceTask.CONTEXT },
        new Object[] { transactionData, "1354", Arrays.asList("8456"),
            RELATIONSHIP_ID, "product", UPSERT,
            "[\r\n" + "  {\r\n" + "    \"side2Id\": \"1000000\",\r\n" + "    \"context\": {\r\n"
                + "      \"contextId\": \"LV\",\r\n" + "      \"tags\": {\r\n" + "        \"prc\": [\r\n"
                + "          \"low\"\r\n" + "        ]\r\n" + "      },\r\n" + "      \"timeRange\": {\r\n"
                + "        \"from\": \"2020-03-02\",\r\n" + "        \"to\": \"2020-03-03\"\r\n" + "      }\r\n"
                + "    }\r\n" + "  },\r\n" + "  {\r\n" + "    \"side2Id\": \"1000008\",\r\n"
                + "    \"context\": {\r\n" + "      \"contextId\": \"LV\",\r\n" + "      \"tags\": {\r\n"
                + "        \"prc\": [\r\n" + "          \"med\"\r\n" + "        ]\r\n" + "      },\r\n"
                + "      \"timeRange\": {\r\n" + "        \"from\": \"2020-03-02\",\r\n"
                + "        \"to\": \"2020-03-03\"\r\n" + "      }\r\n" + "    }\r\n" + "  }\r\n" + "]"});
    relationshipInstanceTask.executeTask(model);
    assertFalse(model.getExecutionStatusTable().isErrorOccurred());
  }
  
//  @Test
  public void testWithMissingRequiredInputForUpsert()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("RelationshipInstanceTask",
        new String[] { "transactionData", RelationshipInstanceTask.SIDE_1_IID,
            RelationshipInstanceTask.SIDE_2_IIDS,
            RelationshipInstanceTask.SOURCE_TYPE,
            RelationshipInstanceTask.ACTION },
        new Object[] { transactionData, "1354", Arrays.asList("8456"),
            "product", UPSERT });
    relationshipInstanceTask.executeTask(model);
    assertEquals(model.getExecutionStatusTable()
        .isErrorOccurred(), true);
  }
  
//  @Test
  public void testWithValidInputForReadOrDelete()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("RelationshipInstanceTask",
        new String[] { "transactionData", RelationshipInstanceTask.SIDE_1_IID,
            RelationshipInstanceTask.SIDE_2_IIDS,
            RelationshipInstanceTask.RELATIONSHIP_CODE,
            RelationshipInstanceTask.SOURCE_TYPE,
            RelationshipInstanceTask.ACTION },
        new Object[] { transactionData, "1354", Arrays.asList("8456"),
            RELATIONSHIP_ID, "product",DELETE });
    relationshipInstanceTask.executeTask(model);
    assertFalse(model.getExecutionStatusTable().isErrorOccurred());
  }
  
//  @Test
  public void testWithMissingRequiredInputForReadOrDelete()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("RelationshipInstanceTask",
        new String[] { "transactionData", RelationshipInstanceTask.SIDE_1_IID,
            RelationshipInstanceTask.SIDE_2_IIDS,
            RelationshipInstanceTask.RELATIONSHIP_CODE,
            RelationshipInstanceTask.ACTION},
        new Object[] { transactionData, "1354", Arrays.asList("8456"),
            "standardArticleAssetRelationship", READ });
    relationshipInstanceTask.executeTask(model);
    assertTrue(model.getExecutionStatusTable().isErrorOccurred());
  }
}
