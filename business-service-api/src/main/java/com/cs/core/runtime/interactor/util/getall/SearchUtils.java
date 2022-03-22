package com.cs.core.runtime.interactor.util.getall;

import com.cs.constants.CommonConstants;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idto.IFilterDTO;
import com.cs.core.rdbms.entity.idto.ISearchDTOBuilder;
import com.cs.core.runtime.interactor.model.filter.*;
import com.cs.core.runtime.interactor.model.instancetree.INewInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.ISpecialUsecaseFiltersModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstanceInTableViewRequestModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.apache.commons.lang3.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class SearchUtils {

  @Autowired
  RDBMSComponentUtils rdbmsComponentUtils;

  @Autowired
  TransactionThreadData transactionThread;

  public void fillSearchDTO(INewInstanceTreeRequestModel dataModel, Collection<String> classIdsHavingRP, Collection<String> taxonomyIdsHavingRP,
      Collection<String> translatableAttributeIds, ISearchDTOBuilder searchBuilder, Collection<String> searchableAttributeIds,
      List<String> majorTaxonomyIds) throws RDBMSException
  {
    searchBuilder.addClassesWithReadPermission(classIdsHavingRP)
    .addTaxonomiesWithReadPermission(taxonomyIdsHavingRP)
    .setEndpointCode(transactionThread.getTransactionData().getEndpointId())
    .setPagination(dataModel.getFrom(), dataModel.getSize())
    .setTranslatableAttributeCodes(translatableAttributeIds)
    .setStringToSearch(escape(dataModel.getAllSearch()))
    .addTaxonomyFilters(dataModel.getSelectedTaxonomyIds().toArray(new String[] {}))
    .addClassFilters(dataModel.getSelectedTypes().toArray(new String[] {}))
    .addKpiId(dataModel.getKpiId())
    .addMajorTaxonomyIds(majorTaxonomyIds)
    .setXrayEnabled(dataModel.getXrayEnabled());

    List<IPropertyInstanceFilterModel> attributes = dataModel.getAttributes();
    for (IPropertyInstanceFilterModel attribute : attributes) {
      String id = attribute.getId();
      IPropertyDTO propertyByCode = ConfigurationDAO.instance().getPropertyByCode(id);
      List<IFilterValueModel> mandatory = attribute.getMandatory();
      for (IFilterValueModel condition : mandatory) {
        String comparisonOperator = condition.getComparisonOperator();
        if (condition instanceof FilterValueMatchModel) {
          IFilterValueMatchModel cond = (IFilterValueMatchModel) condition;
          cond.getValue();
          searchBuilder.addFilter(propertyByCode, IFilterDTO.FilterType.valueOf(condition.getType()), IFilterDTO.ValueType.String,
              cond.getValue());
        }
        if (condition instanceof FilterValueRangeModel) {
          IFilterValueRangeModel cond = (IFilterValueRangeModel) condition;
          Range<Double> range = Range.between(cond.getFrom(), cond.getTo());
          searchBuilder.addFilter(propertyByCode, IFilterDTO.FilterType.valueOf(condition.getType()), IFilterDTO.ValueType.Range, range);
        }
      }
    }

    for (IPropertyInstanceFilterModel tag : dataModel.getTags()) {
      String id = tag.getId();
      IPropertyDTO propertyByCode = ConfigurationDAO.instance().getPropertyByCode(id);
      List<IFilterValueModel> mandatory = tag.getMandatory();
      for (IFilterValueModel condition : mandatory) {
        String comparisonOperator = condition.getComparisonOperator();
        IFilterValueRangeModel cond = (IFilterValueRangeModel) condition;
        searchBuilder.addFilter(propertyByCode, IFilterDTO.FilterType.valueOf(condition.getType()), IFilterDTO.ValueType.String,
            cond.getId());
      }
    }
    
    fillSpecialUseCaseFilters(searchBuilder, dataModel.getSpecialUsecaseFilters());

    for (String searchableAttributeId : searchableAttributeIds) {
      IPropertyDTO propertyByCode = ConfigurationDAO.instance().getPropertyByCode(searchableAttributeId);
      searchBuilder.addSearchableAttributes(searchableAttributeId, propertyByCode.isNumeric());
    }
  }
  
  /**
   * This method add special filter data in DTO.
   * @param searchBuilder
   * @param specialUsecaseFilters
   */
  private void fillSpecialUseCaseFilters(ISearchDTOBuilder searchBuilder,
      List<ISpecialUsecaseFiltersModel> specialUsecaseFilters)
  {
    // Iterate over special use case filters list from request model
    for (ISpecialUsecaseFiltersModel specialUseCaseFilter : specialUsecaseFilters) {
      List<String> appliedValues = specialUseCaseFilter.getAppliedValues();
      if (appliedValues != null && !appliedValues.isEmpty()) {
        switch (specialUseCaseFilter.getId()) {
          // Fill duplicate assets filter value
          case CommonConstants.DUPLICATE_ASSETS_FILTER:
            String appliedDuplicateValue = appliedValues.get(0);
            if (CommonConstants.DUPLICATE.equalsIgnoreCase(appliedDuplicateValue)) {
              searchBuilder.addIsDuplicateFilter(true);
            }
            break;
          
          // Fill asset expiry filter value
          case CommonConstants.ASSET_EXPIRY_FILTER:
            if (appliedValues.size() == 2) {
              break;
            }
            String appliedExpiredValue = appliedValues.get(0);
            if (CommonConstants.EXPIRED_FILTER.equalsIgnoreCase(appliedExpiredValue)) {
              searchBuilder.addAssetExpiryFilter(true);
            }
            else if (CommonConstants.NON_EXPIRED_FILTER.equalsIgnoreCase(appliedExpiredValue)) {
              searchBuilder.addAssetExpiryFilter(false);
            }
            break;
          
          // Fill rule violation filter value
          case CommonConstants.COLOR_VOILATION_FILTER:
            if (appliedValues.size() != 4) // No need to filter if all colors
                                           // are applied.
              searchBuilder.addRuleViolationFilters(appliedValues);
            break;
        }
      }
    }
  }
  
  private static String escape(String s) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c == '\\' || c == '+' || c == '-' || c == '!' || c == '(' || c == ')' || c == ':'
        || c == '^' || c == '[' || c == ']' || c == '\"' || c == '{' || c == '}' || c == '~'
        || c == '*' || c == '?' || c == '|' || c == '&' || c == '/') {
        sb.append('\\');
      }
      sb.append(c);
    }
    return sb.toString();
  }

  public void fillSearchDTOForTableView(IGetVariantInstanceInTableViewRequestModel dataModel, Collection<String> classIdsHavingRP,
      Collection<String> taxonomyIdsHavingRP, Collection<String> translatableAttributeIds, ISearchDTOBuilder searchBuilder,
      Collection<String> searchableAttributeIds, List<String> majorTaxonomyIds) throws RDBMSException
  {
    searchBuilder.addClassesWithReadPermission(classIdsHavingRP)
        .addTaxonomiesWithReadPermission(taxonomyIdsHavingRP)
        .setEndpointCode(transactionThread.getTransactionData().getEndpointId())
        .setPagination(dataModel.getFrom(), dataModel.getSize())
        .setTranslatableAttributeCodes(translatableAttributeIds)
        .setStringToSearch(escape(dataModel.getAllSearch()))
        .addMajorTaxonomyIds(majorTaxonomyIds);

    List<? extends IPropertyInstanceFilterModel> attributes = dataModel.getAttributes();

    for (IPropertyInstanceFilterModel attribute : attributes) {
      String id = attribute.getId();
      IPropertyDTO propertyByCode = ConfigurationDAO.instance().getPropertyByCode(id);
      List<IFilterValueModel> mandatory = attribute.getMandatory();
      for (IFilterValueModel condition : mandatory) {
        String comparisonOperator = condition.getComparisonOperator();
        if (condition instanceof FilterValueMatchModel) {
          IFilterValueMatchModel cond = (IFilterValueMatchModel) condition;
          cond.getValue();
          searchBuilder.addFilter(propertyByCode, IFilterDTO.FilterType.valueOf(condition.getType()), IFilterDTO.ValueType.String,
              cond.getValue());
        }
        if (condition instanceof FilterValueRangeModel) {
          IFilterValueRangeModel cond = (IFilterValueRangeModel) condition;
          Range<Double> range = Range.between(cond.getFrom(), cond.getTo());
          searchBuilder.addFilter(propertyByCode, IFilterDTO.FilterType.valueOf(condition.getType()), IFilterDTO.ValueType.Range, range);
        }
      }
    }

    for (IPropertyInstanceFilterModel tag : dataModel.getTags()) {
      String id = tag.getId();
      IPropertyDTO propertyByCode = ConfigurationDAO.instance().getPropertyByCode(id);
      List<IFilterValueModel> mandatory = tag.getMandatory();
      for (IFilterValueModel condition : mandatory) {
        String comparisonOperator = condition.getComparisonOperator();
        IFilterValueRangeModel cond = (IFilterValueRangeModel) condition;
        searchBuilder.addFilter(propertyByCode, IFilterDTO.FilterType.valueOf(condition.getType()), IFilterDTO.ValueType.String,
            cond.getId());
      }
    }

    for (String searchableAttributeId : searchableAttributeIds) {
      IPropertyDTO propertyByCode = ConfigurationDAO.instance().getPropertyByCode(searchableAttributeId);
      searchBuilder.addSearchableAttributes(searchableAttributeId, propertyByCode.isNumeric());
    }
  }
}
