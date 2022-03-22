package com.cs.di.workflow.tasks;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.cs.constants.DAMConstants;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPPriority;
import com.cs.core.config.interactor.model.asset.IUploadAssetModel;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.di.workflow.model.WorkflowTaskModel;

/**
 * This class is responsible for getting callback data from workflow parameters
 * and trigger a new BGP for create relationship
 * @author jamil.ahmad
 *
 */
@Component("bulkCreateRelationshipTask")
public class BulkCreateRelationshipTask extends AbstractBGPTask {
  public static final String      CALLBACK_URL_BODY_DATA       = "callBackBodyData";
 
  public static final List<String> INPUT_LIST  = Arrays.asList(SERVICE, PRIORITY, CALLBACK_URL_BODY_DATA );
  
  public static final List<String> OUTPUT_LIST = Arrays.asList(JOB_ID, EXECUTION_STATUS);
  
  @Override
  @SuppressWarnings("unchecked")
  public void executeTask(WorkflowTaskModel model)
  {
    String priority = model.getInputParameters().get(PRIORITY).toString().toUpperCase();
    model.getInputParameters().put(PRIORITY, BGPPriority.valueOf(priority));
    
    Map<String,String> callBackDataMap = (Map<String, String>) model.getInputParameters().get(CALLBACK_URL_BODY_DATA);
    String mode= callBackDataMap.get(IUploadAssetModel.MODE);
    if (DAMConstants.MODE_RELATIONSHIP_BULK_UPLOAD.equals(mode)) {
      // filling transactionData into service data 
      ITransactionData transactionData = model.getWorkflowModel().getTransactionData();
      callBackDataMap.put(Constants.LOCALE_ID, transactionData.getDataLanguage());
      callBackDataMap.put(Constants.CATALOG_CODE, transactionData.getPhysicalCatalogId());
      callBackDataMap.put(Constants.ORGANIZATION_CODE, transactionData.getOrganizationId());
      callBackDataMap.put(Constants.USER_ID, transactionData.getUserId());
      Map<String, Object> serviceData = new HashMap<String, Object>();
      serviceData.put(SERVICE_DATA, new JSONObject(callBackDataMap).toString());
      model.getInputParameters().put(SERVICE_DATA, serviceData);
      super.executeTask(model);
    }
  }
  
  @Override
  public List<String> getInputList()
  {
    return INPUT_LIST;
  }
  
  @Override
  public List<String> getOutputList()
  {
    return OUTPUT_LIST;
  }
  
  @Override
  public List<String> validate(Map<String, Object> inputFields)
  {
    return null;
  }
  
}
