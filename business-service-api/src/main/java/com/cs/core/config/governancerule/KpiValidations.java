package com.cs.core.config.governancerule;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.cs.config.businessapi.base.Validations;
import com.cs.constants.Constants;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.governancerule.IBulkSaveKpiRuleRequestModel;
import com.cs.core.config.interactor.model.governancerule.ISaveKeyPerformanceIndexModel;
import com.cs.core.data.Text;
import com.cs.core.exception.InvalidDashboardTabIdException;
import com.cs.core.exception.InvalidPhysicalCatalogIdsException;

@Component
public class KpiValidations extends Validations {
  
  /**
   * This method will be executed at bulk save request call.
   * 
   * @param bulkSaveModel
   * @throws Exception
   */
  public void validate(IListModel<IBulkSaveKpiRuleRequestModel> bulkSaveModel) throws Exception
  {
    // Safety check for null pointer
    if (bulkSaveModel.getList() == null) {
      return;
    }
    
    for (IBulkSaveKpiRuleRequestModel saveModel : bulkSaveModel.getList()) {
      validate(saveModel.getCode(), saveModel.getLabel());
      validateDashboardTabId(saveModel.getAddedDashboardTabId(),
          saveModel.getDeletedDashboardTabId());
    }
  }
  
  /**
   * This method will be executed on save KPI request.
   * 
   * @param saveModel
   * @throws Exception
   */
  public void validate(ISaveKeyPerformanceIndexModel saveModel) throws Exception
  {
    validate("", saveModel.getLabel());
    validateDashboardTabId(saveModel.getAddedDashboardTabId(),
        saveModel.getDeletedDashboardTabId());
    validatePhysicalCatalogIds(saveModel.getPhysicalCatalogIds());
  }
  
  private void validatePhysicalCatalogIds(List<String> physicalCatalogIds)
      throws InvalidPhysicalCatalogIdsException
  {
    if (!physicalCatalogIds.isEmpty()) {
      List<String> phyCatlogIds = new ArrayList<String>(physicalCatalogIds);
      phyCatlogIds.removeAll(Constants.PHYSICAL_CATALOG_IDS);
      if (phyCatlogIds.size() > 0) {
        throw new InvalidPhysicalCatalogIdsException(
            String.format("physicalCatalogIds Cannot be except (%s)",
                Text.join(",", Constants.PHYSICAL_CATALOG_IDS)));
      }
    }
  }
  
  private void validateDashboardTabId(String addedDashboardTabId, String deletedDashboardTabId)
      throws InvalidDashboardTabIdException
  {
    if (addedDashboardTabId == null && deletedDashboardTabId == null) {
      // It means dashboard tab id is not changed in current request.
      return;
    }
    if (isEmpty(addedDashboardTabId)) {
      throw new InvalidDashboardTabIdException("addedDashboardTabId Cannot be empty");
    }
    if (isEmpty(deletedDashboardTabId)) {
      throw new InvalidDashboardTabIdException("deletedDashboardTabId Cannot be empty");
    }
    if (addedDashboardTabId.equals(deletedDashboardTabId)) {
      throw new InvalidDashboardTabIdException("addedDashboardTabId & deletedDashboardTabId Cannot be same");
    }
  }
  
}
