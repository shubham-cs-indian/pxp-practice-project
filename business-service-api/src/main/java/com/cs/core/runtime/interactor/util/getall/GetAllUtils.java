package com.cs.core.runtime.interactor.util.getall;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.constants.Constants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.config.interactor.model.instancetree.IGetInstanceTreeRequestModel;
import com.cs.core.config.interactor.model.klass.GetFilterInfoModel;
import com.cs.core.config.interactor.model.taxonomy.ApplicableFilterModel;
import com.cs.core.config.interactor.model.taxonomy.IApplicableFilterModel;
import com.cs.core.elastic.aggregation.BaseAggregation;
import com.cs.core.elastic.das.ISearchDAO;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.entity.dto.MultiplePropertyAggregationDTO;
import com.cs.core.rdbms.entity.dto.PropertyAggregationDTO;
import com.cs.core.rdbms.entity.dto.RangeAggregationDTO;
import com.cs.core.rdbms.entity.dto.SortDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.*;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.ISortDTO.SortOrder;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.IRDBMSCursor.OrderDirection;
import com.cs.core.rdbms.process.idao.IRDBMSOrderedCursor;
import com.cs.core.rdbms.process.idto.IRuleResultDTO;
import com.cs.core.rdbms.rsearch.idto.IValueCountDTO;
import com.cs.core.runtime.interactor.constants.application.CoreConstant;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeIconModel;
import com.cs.core.runtime.interactor.model.filter.ISortModel;
import com.cs.core.runtime.interactor.model.instancetree.*;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInformationModel;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.technical.icsexpress.actions.ICSEPropertyQualityFlag.QualityFlag;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Component
public class GetAllUtils {

  @Autowired
  RDBMSComponentUtils rdbmsComponentUtils;

  public IRDBMSOrderedCursor<IBaseEntityDTO> getCursor( ILocaleCatalogDAO localeCatalogDao,
      List<ISortDTO> sortOptions , String searchExpression) throws RDBMSException
  {
    LinkedHashMap<String, OrderDirection> sort = RDBMSUtils.getSortMap(sortOptions);
    IRDBMSOrderedCursor<IBaseEntityDTO> cursor = localeCatalogDao.getAllEntitiesBySearchExpression(searchExpression);
    cursor.setOrderBy(sort);
    return cursor;
  }


  public void fillForNumericAttribute(List<IApplicableFilterModel> children, Map<String, Long> valueVScount)
  {

    DoubleSummaryStatistics summaryStatistics = valueVScount.keySet()
        .stream()
        .mapToDouble(x -> Double.parseDouble(x))
        .summaryStatistics();

    Double standardDeviation = getStandardDeviation(valueVScount.keySet());
    List<Map<String, Double>> ranges = createRanges(summaryStatistics, standardDeviation);

    for (Entry<String, Long> entrySet : valueVScount.entrySet()) {
      Double value = Double.parseDouble(entrySet.getKey());
      Long count = entrySet.getValue();
      for (Map<String, Double> range : ranges) {

        Double from = range.get("from");
        Double to = range.get("to");
        Double countValue = range.get("count");
        if (from <= value && to >= value) {
          if (countValue == null) {
            range.put("count", count.doubleValue());
          }
          else {
            range.put("count", countValue + count);
          }
          break;
        }
      }
    }

    for (Map<String, Double> range : ranges) {
      IApplicableFilterModel childFilter = new ApplicableFilterModel();
      String format = String.format("%.2f-%.2f", range.get("from"), range.get("to"));
      childFilter.setId(format);
      childFilter.setLabel(format);
      childFilter.setCode(format);
      Double count = range.get("count");
      if(count == null) {
        continue;
      }
      childFilter.setCount(count.longValue());
      childFilter.setFrom(range.get("from").longValue());
      childFilter.setTo(range.get("to").longValue());
      children.add(childFilter);
    }
  }

  public List<Map<String, Double>> createRanges(DoubleSummaryStatistics summaryStatistics, Double sd)
  {
    List<Map<String, Double>> rangeList = new ArrayList<>();
    Map<String, Double> rangeMap;
    if (summaryStatistics.getMin() != Double.POSITIVE_INFINITY && summaryStatistics.getMax() != Double.NEGATIVE_INFINITY) {
      Double min = summaryStatistics.getMin();
      Double max = summaryStatistics.getMax();
      Double avg = summaryStatistics.getAverage();
      if (Double.isNaN(sd)) {
        rangeMap = new HashMap<>();
        rangeMap.put("from", min);
        rangeMap.put("to", max + 1);
        rangeList.add(rangeMap);
      }
      else {

        Double avgMinusSd = avg - sd;
        Double avgPlusSd = avg + sd;
        if (min.equals(max)) {
          rangeMap = new HashMap<>();
          rangeMap.put("from", min);
          rangeMap.put("to", max + 1);
          rangeList.add(rangeMap);
        }
        else {
          if (avgMinusSd < min) {
            rangeMap = new HashMap<>();
            rangeMap.put("from", min);
            rangeMap.put("to", avg);
            rangeList.add(rangeMap);
          }
          else {
            rangeMap = new HashMap<>();
            rangeMap.put("from", min);
            rangeMap.put("to", avgMinusSd);
            rangeList.add(rangeMap);
            rangeMap = new HashMap<>();
            rangeMap.put("from", avgMinusSd);
            rangeMap.put("to", avg);
            rangeList.add(rangeMap);
          }
          if (avgPlusSd > max) {
            rangeMap = new HashMap<>();
            rangeMap.put("from", avg);
            rangeMap.put("to", max + 1);
            rangeList.add(rangeMap);
          }
          else {
            rangeMap = new HashMap<>();
            rangeMap.put("from", avg);
            rangeMap.put("to", avgPlusSd);
            rangeList.add(rangeMap);
            rangeMap = new HashMap<>();
            rangeMap.put("from", avgPlusSd);
            rangeMap.put("to", max + 1);
            rangeList.add(rangeMap);
          }
        }
      }
    }
    return rangeList;
  }

  public void fillForLiteralAttribute(List<IApplicableFilterModel> children, Map<String, Long> valueVScount)
  {
    for (Entry<String, Long> value : valueVScount.entrySet()) {
      IApplicableFilterModel childFilter = new ApplicableFilterModel();
      childFilter.setId(value.getKey());
      childFilter.setLabel(value.getKey());
      childFilter.setCode(value.getKey());
      childFilter.setCount(value.getValue());
      children.add(childFilter);
    }
  }

  public void fillForNumberAttribute(List<IApplicableFilterModel> children, Map<String, Long> valueVScount)
  {
    for (Entry<String, Long> value : valueVScount.entrySet()) {
      IApplicableFilterModel childFilter = new ApplicableFilterModel();
      String key = value.getKey();
      childFilter.setId(key);
      childFilter.setLabel(key);
      childFilter.setCode(key);
      childFilter.setCount(value.getValue());

      String[] split = key.split("-");
      if (split.length == 2) {
        childFilter.setFrom(Double.valueOf(split[0]).longValue());
        childFilter.setTo(Double.valueOf(split[1]).longValue());
      }
      //Handling for negative values
      else if (split.length == 4) {
        childFilter.setFrom(Double.valueOf("-" + split[1]).longValue());
        childFilter.setTo(Double.valueOf("-" + split[3]).longValue());
      }
      else if (split.length == 3) {
        if (split[0].equals("")) {
          childFilter.setFrom(Double.valueOf("-" + split[1]).longValue());
          childFilter.setTo(Double.valueOf(split[2]).longValue());
        }
        else {
          childFilter.setFrom(Double.valueOf(split[0]).longValue());
          childFilter.setTo(Double.valueOf("-" + split[2]).longValue());
        }

      }
      children.add(childFilter);
    }
  }

  public void fillForTag(List<IApplicableFilterModel> children, Map<String, Long> valueVScount)
  {
    for (IApplicableFilterModel childFilter : children) {
      childFilter.setCount(valueVScount.get(childFilter.getCode()));
    }
  }
  public void fillCountsForFilter(IGetFilterInfoModel filterInfo, String searchExpression, Boolean allowChildren) throws Exception
  {
    List<IPropertyDTO> properties = new ArrayList<>();
    Map<String, IApplicableFilterModel> codeVsFilter = new HashMap<>();
    for (IApplicableFilterModel filter : filterInfo.getFilterData()) {
      codeVsFilter.put(filter.getCode(), filter);

      IPropertyDTO propertyByCode = ConfigurationDAO.instance().getPropertyByCode(filter.getCode());
      if (filter.getType().equals(CommonConstants.NUMBER_ATTRIBUTE_TYPE)) {
        IPropertyDTO property = new PropertyDTO(propertyByCode.getPropertyIID(),
            propertyByCode.getPropertyCode(), PropertyType.NUMBER);
        properties.add(property);
      }
      else if (filter.getType().equals(CommonConstants.DATE_ATTRIBUTE_TYPE)) {
        IPropertyDTO property = new PropertyDTO(propertyByCode.getPropertyIID(),
            propertyByCode.getPropertyCode(), PropertyType.MEASUREMENT);
        properties.add(property);
      }
      else {
        properties.add(propertyByCode);
      }
    }
    List<IValueCountDTO> propertyCounts = rdbmsComponentUtils.getLocaleCatlogDAO().getPropertyCount(allowChildren, searchExpression, properties);

    for (IValueCountDTO propertyCount : propertyCounts) {
      String code = propertyCount.getProperty().getCode();

      IApplicableFilterModel filter = codeVsFilter.get(code);
      List<IApplicableFilterModel> children = filter.getChildren();
      Map<String, Long> valueVScount = propertyCount.getValueVScount();

      if (valueVScount.isEmpty()) {
        codeVsFilter.remove(code);
        continue;
      }

      if (CoreConstant.NUMERIC_TYPE.contains(filter.getType())) {
        fillForNumericAttribute(children, valueVScount);
      }
      else if(IStandardConfig.TagType.AllTagTypes.contains(filter.getType())) {
        fillForTag(children, valueVScount);
      }
      else {
        fillForLiteralAttribute(children, valueVScount);
      }

    }
    filterInfo.setFilterData(new ArrayList<>(codeVsFilter.values()));
  }

  public List<INewApplicableFilterModel> getApplicableFilters(List<INewApplicableFilterModel> filterData, String searchExpression, Boolean allowChildren) throws Exception
  {
    List<IPropertyDTO> properties = new ArrayList<>();
    Map<String, INewApplicableFilterModel> codeVsFilter = new HashMap<>();
    for (INewApplicableFilterModel filter : filterData) {
      codeVsFilter.put(filter.getCode(), filter);

      IPropertyDTO propertyByCode = ConfigurationDAO.instance().getPropertyByCode(filter.getCode());
      if (filter.getType().equals(CommonConstants.NUMBER_ATTRIBUTE_TYPE)) {
        IPropertyDTO property = new PropertyDTO(propertyByCode.getPropertyIID(),
            propertyByCode.getPropertyCode(), PropertyType.NUMBER);
        properties.add(property);
      }
      else if (filter.getType().equals(CommonConstants.DATE_ATTRIBUTE_TYPE)) {
        IPropertyDTO property = new PropertyDTO(propertyByCode.getPropertyIID(),
            propertyByCode.getPropertyCode(), PropertyType.MEASUREMENT);
        properties.add(property);
      }
      else {
        properties.add(propertyByCode);
      }
    }
    List<IValueCountDTO> propertyCounts = rdbmsComponentUtils.getLocaleCatlogDAO().getPropertyCount(allowChildren, searchExpression, properties);

    for (IValueCountDTO propertyCount : propertyCounts) {
      String code = propertyCount.getProperty().getCode();
      Map<String, Long> valueVScount = propertyCount.getValueVScount();

      if (valueVScount.isEmpty()) {
        codeVsFilter.remove(code);
        continue;
      }
    }
    return new ArrayList<>(codeVsFilter.values());
  }

  //changes from ravi

  public List<IGetFilterChildrenModel> getFilterChildren(IGetFilterChildrenRequestModel model,String searchExpression, Boolean allowChildren) throws Exception
  {
    List<IPropertyDTO> properties = new ArrayList<>();

    List<IGetFilterChildrenModel> responseList = new ArrayList<IGetFilterChildrenModel>();

    IPropertyDTO propertyByCode = ConfigurationDAO.instance().getPropertyByCode(model.getReferencedProperty().getCode());
    if (model.getReferencedProperty().getType().equals(CommonConstants.NUMBER_ATTRIBUTE_TYPE)) {
      IPropertyDTO property = new PropertyDTO(propertyByCode.getPropertyIID(),
          propertyByCode.getPropertyCode(), PropertyType.NUMBER);
      properties.add(property);
    }
    else if (model.getReferencedProperty().getType().equals(CommonConstants.DATE_ATTRIBUTE_TYPE)) {
      IPropertyDTO property = new PropertyDTO(propertyByCode.getPropertyIID(),
          propertyByCode.getPropertyCode(), PropertyType.MEASUREMENT);
      properties.add(property);
    }
    else {
      properties.add(propertyByCode);
    }

    List<IValueCountDTO> propertyCounts = rdbmsComponentUtils.getLocaleCatlogDAO().getPropertyCount(allowChildren, searchExpression,
        properties);

    for (IValueCountDTO propertyCount : propertyCounts) {

      Map<String, Long> valueVsCount = propertyCount.getValueVScount();
      List<IIdLabelCodeIconModel> children = model.getReferencedProperty().getChildren();

      if (CoreConstant.NUMERIC_TYPE.contains(model.getReferencedProperty().getType())) {
        fillForNumericAttributeForChild(responseList, valueVsCount);
      }
      else if (IStandardConfig.TagType.AllTagTypes.contains(model.getReferencedProperty().getType())) {
        fillForTagForChild(responseList, children, valueVsCount);
      }
      else {
        fillForChildrenAttribute(responseList, valueVsCount,
            CoreConstant.NUMERIC_TYPE.contains(model.getReferencedProperty().getType()));
      }

    }
    return responseList;
  }

  public void fillForTagForChild(List<IGetFilterChildrenModel> responseList,List<IIdLabelCodeIconModel> children, Map<String, Long> valueVScount)
  {
    List<IGetFilterChildrenModel> zeroTagValues = new ArrayList<IGetFilterChildrenModel>();
    Map<String, IGetFilterChildrenModel> bucketMap = new HashMap<String, IGetFilterChildrenModel>();

    for (IIdLabelCodeIconModel child : children) {
      String code = child.getCode();
      IGetFilterChildrenModel childFilter = new GetFilterChildrenModel();
      childFilter.setLabel(child.getLabel());
      childFilter.setId(child.getId());
      childFilter.setCode(code);
      childFilter.setIcon(child.getIcon());
      childFilter.setIconKey(child.getIconKey());
      Long result =valueVScount.get(code);

      if(result == null) {
        childFilter.setCount(0L);
        zeroTagValues.add(childFilter);
      }
      else {
        childFilter.setCount(result);
        bucketMap.put(code, childFilter);
      }
    }

    for(String tagValueId: valueVScount.keySet()) {
      responseList.add(bucketMap.get(tagValueId));
    }
    responseList.addAll(zeroTagValues);
  }

  public void fillForNumericAttributeForChild(List<IGetFilterChildrenModel> responseList, Map<String, Long> valueVScount)
  {
    DoubleSummaryStatistics summaryStatistics = valueVScount.keySet()
        .stream()
        .mapToDouble(x -> Double.parseDouble(x))
        .summaryStatistics();

    Double standardDeviation = getStandardDeviation(valueVScount.keySet());
    List<Map<String, Double>> ranges = createRanges(summaryStatistics, standardDeviation);

    for (Entry<String, Long> entrySet : valueVScount.entrySet()) {
      Double value = Double.parseDouble(entrySet.getKey());
      Long count = entrySet.getValue();
      for (Map<String, Double> range : ranges) {

        Double from = range.get("from");
        Double to = range.get("to");
        Double countValue = range.get("count");
        if (from <= value && to >= value) {
          if (countValue == null) {
            range.put("count", count.doubleValue());
          }
          else {
            range.put("count", countValue + count);
          }
          break;
        }
      }
    }

    for (Map<String, Double> range : ranges) {
      IGetFilterChildrenModel childFilter = new GetFilterChildrenModel();
      String format = String.format("%.2f-%.2f", range.get("from"), range.get("to"));
      childFilter.setId(format);
      childFilter.setLabel(format);
      Double count = range.get("count");
      if(count == null) {
        continue;
      }
      childFilter.setCount(count.longValue());
      childFilter.setFrom(range.get("from"));
      childFilter.setTo(range.get("to"));
      responseList.add(childFilter);
    }

  }


  public void fillForChildrenAttribute(List<IGetFilterChildrenModel> responseList, Map<String, Long> valueVScount, Boolean isNumeric)
  {
    for(Entry<String,Long> value : valueVScount.entrySet())
    {
      IGetFilterChildrenModel childFilter = new GetFilterChildrenModel();
      String key = value.getKey();
      childFilter.setId(key);
      childFilter.setLabel(value.getKey());
      childFilter.setCount(value.getValue());
      childFilter.setIcon(childFilter.getIcon());
      childFilter.setIconKey(childFilter.getIconKey());

      if(isNumeric) {
        String[] split = key.split("-");
        if(split.length == 2) {
          childFilter.setFrom(Double.valueOf(split[0]));
          childFilter.setTo(Double.valueOf(split[1]));
        }
        //Handling for negative values
        else if(split.length == 4) {
          childFilter.setFrom(Double.valueOf("-" + split[1]));
          childFilter.setTo(Double.valueOf("-" +split[3]));
        }
        else if(split.length == 3) {
          if(split[0].equals("")) {
            childFilter.setFrom(Double.valueOf("-" + split[1]));
            childFilter.setTo(Double.valueOf(split[2]));
          }
          else {
            childFilter.setFrom(Double.valueOf(split[0]));
            childFilter.setTo(Double.valueOf("-" + split[2]));
          }
          
        }
      }
      responseList.add(childFilter);
    }

  }

  public List<IGetFilterChildrenModel> fillRuleViolationsCount(IAggregationResultDTO aggregationResult, Map<String, String> colorLabelMap)
  {
    List<IGetFilterChildrenModel> filterInfoModel = new ArrayList<>();
    Map<String, Long> buckets = aggregationResult.getCount();

    filterInfoModel.add( new GetFilterChildrenModel(
      CommonConstants.RED_VOILATION_FILTER,
      colorLabelMap.get(StringUtils.upperCase(CommonConstants.RED_VOILATION_FILTER)),
      buckets.get(BaseAggregation.RuleViolationAggName.redAgg.name())));

    filterInfoModel.add( new GetFilterChildrenModel(
      CommonConstants.ORANGE_VOILATION_FILTER,
      colorLabelMap.get(StringUtils.upperCase(CommonConstants.ORANGE_VOILATION_FILTER)),
      buckets.get(BaseAggregation.RuleViolationAggName.orangeAgg.name())));

    filterInfoModel.add( new GetFilterChildrenModel(
      CommonConstants.YELLOW_VOILATION_FILTER,
      colorLabelMap.get(StringUtils.upperCase(CommonConstants.YELLOW_VOILATION_FILTER)),
      buckets.get(BaseAggregation.RuleViolationAggName.yellowAgg.name())));

    filterInfoModel.add( new GetFilterChildrenModel(
      CommonConstants.GREEN_VOILATION_FILTER,
      colorLabelMap.get(StringUtils.upperCase(CommonConstants.GREEN_VOILATION_FILTER)),
      buckets.get(BaseAggregation.RuleViolationAggName.greenAgg.name())));

    return filterInfoModel;
  }

  public List<IGetFilterChildrenModel> fillCountsChildDataQualityRules(IGetFilterChildrenRequestModel model, String searchExpression,
      boolean allowChildren, Map<String, String> colorLabelMap) throws Exception
  {
    IRuleResultDTO dataQualityCount = rdbmsComponentUtils.getLocaleCatlogDAO().getDataQualityCount(allowChildren, searchExpression);
    List<IGetFilterChildrenModel> filterInfoModel = new ArrayList<>();

   // List<IIdLabelCodeModel> children = model.getReferencedProperty().getChildren();
    filterInfoModel.add(new GetFilterChildrenModel("red", colorLabelMap.get(StringUtils.upperCase(CommonConstants.RED_VOILATION_FILTER)), dataQualityCount.getNbOfFlaggedEntities(QualityFlag.$red)));
    filterInfoModel.add(new GetFilterChildrenModel("orange", colorLabelMap.get(StringUtils.upperCase(CommonConstants.ORANGE_VOILATION_FILTER)), dataQualityCount.getNbOfFlaggedEntities(QualityFlag.$orange)));
    filterInfoModel.add(new GetFilterChildrenModel("yellow", colorLabelMap.get(StringUtils.upperCase(CommonConstants.YELLOW_VOILATION_FILTER)), dataQualityCount.getNbOfFlaggedEntities(QualityFlag.$yellow)));
    filterInfoModel.add(new GetFilterChildrenModel("green", colorLabelMap.get(StringUtils.upperCase(CommonConstants.GREEN_VOILATION_FILTER)), dataQualityCount.getNbOfFlaggedEntities(QualityFlag.$green)));

    return filterInfoModel;
  }

  public IGetFilterInfoModel getFilterInfoModel(IGetFilterInformationModel filterInformationModel)
  {
    IGetFilterInfoModel filterInfoModel = new GetFilterInfoModel();
    filterInfoModel.setDefaultFilterTags(filterInformationModel.getDefaultFilterTags());
    List<IConfigEntityTreeInformationModel> tags = filterInformationModel.getFilterData()
        .getTags();
    List<IConfigEntityTreeInformationModel> attributes = filterInformationModel.getFilterData()
        .getAttributes();
    List<IConfigEntityTreeInformationModel> filterInfos = new ArrayList<>();
    filterInfos.addAll(tags);
    filterInfos.addAll(attributes);
    List<IApplicableFilterModel> applicableFilters = new ArrayList<>();
    for (IConfigEntityTreeInformationModel filterInfo : filterInfos) {
      IApplicableFilterModel filterModel = new ApplicableFilterModel();
      filterModel.setId(filterInfo.getId());
      filterModel.setLabel(filterInfo.getLabel());
      filterModel.setType(filterInfo.getType());
      filterModel.setDefaultUnit(filterInfo.getDefaultUnit());
      filterModel.setPrecision(filterInfo.getPrecision());
      filterModel.setCode(filterInfo.getCode());
      List<IApplicableFilterModel> childrensToSet = new ArrayList<>();
      List<IConfigEntityTreeInformationModel> childrens = (List<IConfigEntityTreeInformationModel>) filterInfo
          .getChildren();
      for (IConfigEntityTreeInformationModel children : childrens) {
        IApplicableFilterModel childrenFilterModel = new ApplicableFilterModel();
        childrenFilterModel.setId(children.getId());
        childrenFilterModel.setCode(children.getCode());
        childrenFilterModel.setLabel(children.getLabel());
        childrenFilterModel.setType(children.getType());
        childrensToSet.add(childrenFilterModel);
      }
      filterModel.setChildren(childrensToSet);
      applicableFilters.add(filterModel);
    }
    filterInfoModel.setFilterData(applicableFilters);
    filterInfoModel.setSortData(filterInformationModel.getSortData());
    filterInfoModel.setSearchableAttributes(filterInformationModel.getSearchableAttributes());
    filterInfoModel.setConfigDetails(filterInformationModel.getConfigDetails());
    filterInfoModel
        .setTranslatableAttributesIds(filterInformationModel.getTranslatableAttributesIds());
    return filterInfoModel;
  }

  public static Double getStandardDeviation(Collection<String> source)
  {
    double mean = source.stream()
        .mapToDouble(s -> Double.parseDouble(s))
        .average()
        .getAsDouble();

    double deviation = source.stream()
        .mapToDouble(s -> Math.pow(mean - Double.parseDouble(s), 2))
        .average()
        .getAsDouble();
    return Math.sqrt(deviation);
  }

  public void fillCountsForDataQualityRules(IGetFilterInfoModel filterInfo, String searchExpression,
      boolean allowChildren) throws Exception
  {
    IRuleResultDTO dataQualityCount = rdbmsComponentUtils.getLocaleCatlogDAO().getDataQualityCount(allowChildren, searchExpression);
    List<IApplicableFilterModel> filterData = filterInfo.getFilterData();
    IApplicableFilterModel filterInfoModel = new ApplicableFilterModel();
    filterData.add(filterInfoModel);
    filterInfoModel.setId("dataRules");
    filterInfoModel.setLabel("Rule Violations");
    filterInfoModel.setType("dataRules");
    List<IApplicableFilterModel> children = filterInfoModel.getChildren();
    children.add(new ApplicableFilterModel("isRed_aggs", "isRed_aggs", "RED", dataQualityCount.getNbOfFlaggedEntities(QualityFlag.$red)));
    children.add(new ApplicableFilterModel("isOrange_aggs", "isOrange_aggs", "ORANGE", dataQualityCount.getNbOfFlaggedEntities(QualityFlag.$orange)));
    children.add(new ApplicableFilterModel("isYellow_aggs", "isYellow_aggs", "YELLOW", dataQualityCount.getNbOfFlaggedEntities(QualityFlag.$yellow)));
    children.add(new ApplicableFilterModel("isGreen_aggs", "isGreen_aggs", "GREEN", dataQualityCount.getNbOfFlaggedEntities(QualityFlag.$green)));

   }



/*  public List<ICategoryInformationModel> fillClassifierCountInfo(Boolean allowChildren,
      String searchExpression, List<String> classifierIds, ClassifierType classifierType,
      IConfigEntityTreeInformationModel categoryInfo) throws Exception
  {
    List<ICategoryInformationModel> classifierInfo = new ArrayList<>();
    Map<String, Long> classifierCount = rdbmsComponentUtils.getLocaleCatlogDAO()
        .getClassifierCount(allowChildren, searchExpression, classifierIds, classifierType);

    Long count = classifierCount.getOrDefault(categoryInfo.getCode(), 0l);
    ICategoryInformationModel categoryInformationModel = new CategoryInformationModel(count, -1,
        categoryInfo);
    classifierInfo.add(categoryInformationModel);
    List<? extends ITreeEntity> children = new ArrayList<>(categoryInfo.getChildren());
    categoryInformationModel.setChildren(new ArrayList<>());
    fillChildrenInfo(categoryInformationModel, classifierCount, children, count);

    return classifierInfo;
  }*/



/*  public List<ITaxonomyInformationModel> fillTaxonomyCountInfo(Boolean allowChildren, String searchExpression, List<String> classifierIds,
      ClassifierType classifierType, List<IConfigEntityTreeInformationModel> categoryInfo) throws Exception
  {
    List<ITaxonomyInformationModel> classifierInfo = new ArrayList<>();
    Map<String, Long> classifierCount = rdbmsComponentUtils.getLocaleCatlogDAO().getClassifierCount(allowChildren, searchExpression, classifierIds, classifierType);

    for (IConfigEntityTreeInformationModel category : categoryInfo) {

      Long count = classifierCount.getOrDefault(category.getCode(), 0l);
      ICategoryInformationModel categoryInformationModel = new CategoryInformationModel(count, -1, category);
      ITaxonomyInformationModel taxonomyInfo = new TaxonomyInformationModel(categoryInformationModel);
      classifierInfo.add(taxonomyInfo);

      List<? extends ITreeEntity> children = new ArrayList<>(category.getChildren());
      taxonomyInfo.setChildren(new ArrayList<>());
      fillChildrenInfo(taxonomyInfo, classifierCount, children, count);
    }
    return classifierInfo;
  }*/
/*  *//*
  public Long fillChildrenInfo(ICategoryInformationModel categoryInfo, Map<String, Long> classifierCount, List<? extends ITreeEntity> children, Long count) throws RDBMSException
  {
     ListIterator<? extends ITreeEntity> iterator = children.listIterator();
    while(iterator.hasNext()) {
      IConfigEntityTreeInformationModel treeModel = (IConfigEntityTreeInformationModel) iterator.next();
      String code = treeModel.getCode();
      Long childCount = classifierCount.getOrDefault(code, 0l);
      IClassifierDTO childClassifier = ConfigurationDAO.instance().getClassifierByCode(code);
      ICategoryInformationModel categoryInformationModel = new CategoryInformationModel(childCount, childClassifier.getClassifierIID(), treeModel);
      List<? extends ITreeEntity> newChildren = new ArrayList<>(treeModel.getChildren());
      categoryInformationModel.setChildren(new ArrayList<>());
      count += fillChildrenInfo(categoryInformationModel, classifierCount, newChildren, childCount);
      categoryInfo.setCount(count);
      List<ICategoryInformationModel> childsChildren = (List<ICategoryInformationModel>) categoryInfo.getChildren();
      childsChildren.add(categoryInformationModel);
    }
    return count;
  }*/

  public void getFlatIdsList(List<? extends ITreeEntity> taxonomiesInfo, List<String> ids)
  {
    for (ITreeEntity taxonomyInfo : taxonomiesInfo) {
      String id = taxonomyInfo.getId();
      if (!id.equals(CommonConstants.TAXONOMY_HIERARCHY_ROOT) && !id.equals("-1")) {
        ids.add(id);
      }
      List<? extends ITreeEntity> taxonomyChildrenInfo = taxonomyInfo.getChildren();
      if (taxonomyChildrenInfo != null && !taxonomyChildrenInfo.isEmpty()) {
        getFlatIdsList(taxonomyInfo.getChildren(), ids);
      }
    }
  }

  public void getFlatIdsListForKlass(ITreeEntity klassInfo, List<String> ids)
  {
    String id = klassInfo.getId();
    if (!id.equals(CommonConstants.TAXONOMY_HIERARCHY_ROOT)) {
      ids.add(id);
    }
    List<? extends ITreeEntity> taxonomyChildrenInfo = klassInfo.getChildren();
    if (taxonomyChildrenInfo != null && !taxonomyChildrenInfo.isEmpty()) {
      getFlatIdsList(klassInfo.getChildren(), ids);
    }
  }

 /* public void getParentTaxxonomyCount(List<? extends ITreeEntity> taxonomiesInfo, List<String> ids)
  {
    for (ITreeEntity taxonomyInfo : taxonomiesInfo) {
      String id = taxonomyInfo.getId();
      ITreeEntity parent = taxonomyInfo.getParent();
      if(parent == null) {
        ids.add(id);
        taxonomyInfo.setChildren(new ArrayList<ITreeEntity>());
      }
    }
  }*/


  public Set<IPropertyDTO> getXrayProperties(Map<String, IAttribute> referencedAttributes , Map<String, ITag> referencedTags) throws Exception
  {
    Set<IPropertyDTO> properties = new HashSet<>();

    // reference attributes
    Set<IPropertyDTO> attributes = referencedAttributes
        .values()
        .stream()
        .map(referencedAttribute -> {
          try {
            return RDBMSUtils.newPropertyDTO(referencedAttribute);
          }
          catch (Exception e) {
            throw new RuntimeException(e);
          }
        })
        .collect(Collectors.toSet());

    // reference tags
    Set<IPropertyDTO> tags = referencedTags
        .values()
        .stream()
        .map(referencedTag -> {
          try {
            return RDBMSUtils.newPropertyDTO(referencedTag);
          }
          catch (Exception e) {
            throw new RuntimeException(e);
          }
        })
        .collect(Collectors.toSet());

    properties.addAll(attributes);
    properties.addAll(tags);

    return properties;
  }

  public void getXrayPropertyDetails(Map<String, IAttribute> referencedAttributes , Map<String, ITag> referencedTags,
      IBaseEntityDTO baseEntityDTO, Set<IPropertyDTO> properties,
      IKlassInstanceInformationModel klassInstanceInformationModel) throws Exception
  {
    if (properties != null && !properties.isEmpty()) {
      IBaseEntityDAO baseEntityDAO = this.rdbmsComponentUtils.getBaseEntityDAO(baseEntityDTO);

      IPropertyDTO[] propertiesArr = properties.toArray(new IPropertyDTO[properties.size()]);
      KlassInstanceBuilder klassInstanceBuilder = new KlassInstanceBuilder(baseEntityDAO, referencedAttributes, referencedTags, rdbmsComponentUtils,
          propertiesArr);

      klassInstanceBuilder.fillAttributeTagProperties(klassInstanceInformationModel);
    }
  }

  public List<ISortDTO> getSortOptions(IGetInstanceTreeRequestModel dataModel)
  {
    List<ISortDTO> sortOptions = new ArrayList<>();
    List<IAppliedSortModel> dataSortOptions = dataModel.getSortOptions();

    if (dataSortOptions.isEmpty()) {
      ISortDTO sortModel = new SortDTO(SystemLevelIds.LAST_MODIFIED_ATTRIBUTE,
          SortOrder.valueOf(CommonConstants.SORTORDER_DESC), true);
      sortModel.setIsLanguageDependent(true);
      sortOptions.add(sortModel);
      return sortOptions;
    }

    dataSortOptions.forEach((sort) -> {
      ISortDTO sortModel = new SortDTO(sort.getSortField(), SortOrder.valueOf(sort.getSortOrder()), sort.getIsNumeric());
      sortOptions.add(sortModel);
    });
    return sortOptions;
  }

  public List<ISortDTO> getSortOptions(List<IAppliedSortModel> dataSortOptions, Map<String, IAttribute> referencedAttributes)
  {
    List<ISortDTO> sortOptions = new ArrayList<>();

    if (dataSortOptions.isEmpty()) {
      ISortDTO sortModel = new SortDTO(SystemLevelIds.LAST_MODIFIED_ATTRIBUTE,
          SortOrder.valueOf(CommonConstants.SORTORDER_DESC), true);
      sortModel.setIsLanguageDependent(false);
      sortOptions.add(sortModel);
      return sortOptions;
    }

    dataSortOptions.forEach((sort) -> {
      IAttribute attribute = referencedAttributes.get(sort.getSortField());
      ISortDTO sortModel = new SortDTO(sort.getSortField(), SortOrder.valueOf(sort.getSortOrder()),
          CommonConstants.NUMERIC_TYPE.contains(attribute.getType()));
      sortModel.setIsLanguageDependent(attribute.getIsTranslatable());
      sortOptions.add(sortModel);
    });
    return sortOptions;
  }


  public List<ISortDTO> getSortOptionsForTableView(List<ISortModel> dataSortOptions, Map<String, IAttribute> referencedAttributes)
  {
    List<ISortDTO> sortOptions = new ArrayList<>();

    if (dataSortOptions.isEmpty()) {
      ISortDTO sortModel = new SortDTO(SystemLevelIds.LAST_MODIFIED_ATTRIBUTE,
          SortOrder.valueOf(CommonConstants.SORTORDER_DESC), true);
      sortModel.setIsLanguageDependent(false);
      sortOptions.add(sortModel);
      return sortOptions;
    }

    dataSortOptions.forEach((sort) -> {
      IAttribute attribute = referencedAttributes.get(sort.getSortField());
      ISortDTO sortModel = new SortDTO(sort.getSortField(), SortOrder.valueOf(sort.getSortOrder()),
          CommonConstants.NUMERIC_TYPE.contains(attribute.getType()));
      sortModel.setIsLanguageDependent(attribute.getIsTranslatable());
      sortOptions.add(sortModel);
    });
    return sortOptions;
  }

  public List<IGetFilterChildrenModel> getFilterChildren(IReferencedPropertyModel property, Map<String, Long> valueVsCount)
  {
    List<IGetFilterChildrenModel> responseList = new ArrayList<IGetFilterChildrenModel>();

    List<IIdLabelCodeIconModel> children = property.getChildren();

   if (IStandardConfig.TagType.AllTagTypes.contains(property.getType())) {
      fillForTagForChild(responseList, children, valueVsCount);
    }
    else {
      fillForChildrenAttribute(responseList, valueVsCount,
          CoreConstant.NUMERIC_TYPE.contains(property.getType()));
    }

    return responseList;
  }

  private void fillForNumericAttributes(List<IGetFilterChildrenModel> responseList, Map<String, Long> valueVScount) {

    for(Entry<String,Long> value : valueVScount.entrySet())
    {
      IGetFilterChildrenModel childFilter = new GetFilterChildrenModel();
      String key = value.getKey();
      childFilter.setId(key);
      childFilter.setLabel(key);
      childFilter.setCount(value.getValue());

      String[] split = key.split("-");
      Double from = Double.valueOf(split[0]);
      Double to = Double.valueOf(split[1]);
      childFilter.setFrom(from);
      childFilter.setTo(to);

      responseList.add(childFilter);
    }
  }

  public List<INewApplicableFilterModel> getApplicableFilters(ISearchDTO searchDTO, List<INewApplicableFilterModel> filterData, List<String> translatableAttributeIds) throws Exception
  {
    ISearchDAO searchDAO = rdbmsComponentUtils.getLocaleCatlogDAO().openSearchDAO(searchDTO);
    return fillApplicableFilters(filterData, translatableAttributeIds, searchDAO);
  }


  public List<INewApplicableFilterModel> fillApplicableFilters(
      List<INewApplicableFilterModel> filterData, List<String> translatableAttributeIds,
      ISearchDAO searchDAO) throws RDBMSException, IOException
  {
    List<IPropertyDTO> properties = new ArrayList<>();
    Map<String, INewApplicableFilterModel> filterInfo = new HashMap<>();
    for (INewApplicableFilterModel filter : filterData) {
      properties.add(ConfigurationDAO.instance().getPropertyByCode(filter.getCode()));
      filterInfo.put(filter.getCode(), filter);
    }
    List<String> filterable =  searchDAO.findFilterable(properties);
    filterInfo.keySet().retainAll(filterable);

    return new ArrayList<>(filterInfo.values());
  }

  public BaseType getBaseTypeByModule(String moduleId)
  {
    switch (moduleId) {
      case Constants.PIM_MODULE:
        return BaseType.ARTICLE;
      case Constants.MAM_MODULE:
        return BaseType.ASSET;
      case Constants.VIRTUAL_CATALOG_MODULE:
        return BaseType.VIRTUAL_CATALOG;
      case Constants.TEXT_ASSET_MODULE:
        return BaseType.TEXT_ASSET;
      case Constants.TARGET_MODULE:
        return BaseType.TARGET;
      case Constants.SUPPLIER_MODULE:
        return BaseType.SUPPLIER;
      default:
        return BaseType.UNDEFINED;
    }
  }

  public List<BaseType> getBaseTypeByModule(List<String> moduleIds)
  {
    List<BaseType> baseTypes = new ArrayList<BaseType>();

    for (String moduleId : moduleIds) {
      baseTypes.add(getBaseTypeByModule(moduleId));
    }
    return baseTypes;
  }


  public void fillCountsForFilter(IGetFilterInfoModel filterInfo, ISearchDAO searchDAO, List<String> translatableAttributeIds,int aggregationSize) throws Exception
  {
    List<IPropertyDTO> properties = new ArrayList<>();
    Map<String, IApplicableFilterModel> codeVsFilter = new HashMap<>();
    for (IApplicableFilterModel filter : filterInfo.getFilterData()) {
      codeVsFilter.put(filter.getCode(), filter);

      IPropertyDTO propertyByCode = ConfigurationDAO.instance().getPropertyByCode(filter.getCode());
      if (filter.getType().equals(CommonConstants.NUMBER_ATTRIBUTE_TYPE)) {
        IPropertyDTO property = new PropertyDTO(propertyByCode.getPropertyIID(),
            propertyByCode.getPropertyCode(), PropertyType.NUMBER);
        properties.add(property);
      }
      else if (filter.getType().equals(CommonConstants.DATE_ATTRIBUTE_TYPE)) {
        IPropertyDTO property = new PropertyDTO(propertyByCode.getPropertyIID(),
            propertyByCode.getPropertyCode(), PropertyType.MEASUREMENT);
        properties.add(property);
      }
      else {
        properties.add(propertyByCode);
      }
    }
    IMultiplePropertyAggregationDTO multiplePropertyAggregationDTO = new MultiplePropertyAggregationDTO();
    for (IPropertyDTO property : properties) {
      String propertyCode = property.getCode();
      boolean isTranslatable = translatableAttributeIds.contains(propertyCode);
      IPropertyAggregationDTO request;

      if (property.isNumeric()) {
        request = new RangeAggregationDTO(property, isTranslatable);
      }
      else {
        request = new PropertyAggregationDTO(property, isTranslatable);
        request.setBucketSearch("");
      }
      request.setSize(aggregationSize);
      multiplePropertyAggregationDTO.getAggregationRequests().put(propertyCode, request);
    }

    List<IValueCountDTO> propertyCounts = searchDAO.multiplePropertyAggregation(multiplePropertyAggregationDTO).getCounts();
    for (IValueCountDTO propertyCount : propertyCounts) {
      String code = propertyCount.getProperty().getCode();

      IApplicableFilterModel filter = codeVsFilter.get(code);
      List<IApplicableFilterModel> children = filter.getChildren();
      Map<String, Long> valueVSCount = propertyCount.getValueVScount();

      if (valueVSCount.isEmpty()) {
        codeVsFilter.remove(code);
        continue;
      }

      if (CoreConstant.NUMERIC_TYPE.contains(filter.getType())) {
        if (valueVSCount.values().stream().anyMatch(count -> count > 0))
        {
          fillForNumberAttribute(children, valueVSCount);
        }
        else
        {
          codeVsFilter.remove(code);
          continue;
        }
      }
      else if(IStandardConfig.TagType.AllTagTypes.contains(filter.getType())) {
        fillForTag(children, valueVSCount);
      }
      else {
        fillForLiteralAttribute(children, valueVSCount);
      }

    }
    filterInfo.setFilterData(new ArrayList<>(codeVsFilter.values()));
  }
}
