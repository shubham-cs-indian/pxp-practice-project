package com.cs.core.runtime.goldenrecordbucket;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.standard.IStandardConfig.StandardProperty;
import com.cs.constants.SystemLevelIds;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IGoldenRecordDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.goldenrecordbucket.dao.GoldenRecordBucketDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.goldenrecord.bucket.IGetSourceInfoForBucketService;
import com.cs.core.runtime.interactor.exception.goldenrecord.MultipleGoldenRecordsFoundInBucket;
import com.cs.core.runtime.interactor.model.dynamichierarchy.GoldenRecordSourceInfoModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IGoldenRecordSourceInfoModel;
import com.cs.core.runtime.interactor.model.goldenrecord.GetSourceInfoForBucketResponseModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IConfigDetailsForGetSourceInfoForBucketResponseModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IGetSourceInfoForBucketResponseModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IIdBucketIdModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IRuleIdLangaugeCodesModel;
import com.cs.core.runtime.interactor.model.goldenrecord.RuleIdLangaugeCodesModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.usecase.goldenrecord.IGetConfigDetailsForGetSourceInfoForBucketStrategy;

@Component
public class GetSourceInfoForBucketService
    extends AbstractRuntimeService<IIdBucketIdModel, IGetSourceInfoForBucketResponseModel>
    implements IGetSourceInfoForBucketService {
  
  @Autowired
  protected RDBMSComponentUtils rdbmsComponentUtils;
  
  @Autowired
  IGetConfigDetailsForGetSourceInfoForBucketStrategy getConfigDetailsForGetSourceInfoForBucketStrategy;
  
  @Override
  protected IGetSourceInfoForBucketResponseModel executeInternal(IIdBucketIdModel model)
      throws Exception
  {
    GoldenRecordBucketDAO dao = new GoldenRecordBucketDAO();
    IGoldenRecordDTO goldenRecordDTO = dao.getGoldenRecordRuleAndBaseEntityIIDs(model.getBucketId(),
        model.getGoldenRecordId());
    List<Long> bucketBaseEntityIIDs = goldenRecordDTO.getLinkedBaseEntities();
    String ruleId = goldenRecordDTO.getRuleId();
    
    IGetSourceInfoForBucketResponseModel sourceInfoForBucket = getSourceInfoForBucket(bucketBaseEntityIIDs);
    sourceInfoForBucket.setRuleId(ruleId);
    IConfigDetailsForGetSourceInfoForBucketResponseModel configDetails = getConfigDetails(sourceInfoForBucket);
    sourceInfoForBucket.setConfigDetails(configDetails);
    return sourceInfoForBucket;
  }
  
  private IGetSourceInfoForBucketResponseModel getSourceInfoForBucket(List<Long> bucketBaseEntityIIDs)
      throws Exception
  {
    ILocaleCatalogDAO localeCatalogDao = rdbmsComponentUtils.getLocaleCatlogDAO();
    List<String> goldenRecordLanguages = new ArrayList<>();
    Long goldenRecordId = null;
    String goldenRecordCreationLanguage = null;
    List<IGoldenRecordSourceInfoModel> baseEntitiesInfo = new ArrayList<>();
    for (Long baseEntityIId : bucketBaseEntityIIDs) {
      IBaseEntityDTO baseEntityDTO = localeCatalogDao.getEntityByIID(baseEntityIId);
      IBaseEntityDAO baseEntityDAO = localeCatalogDao.openBaseEntity(baseEntityDTO);
      IPropertyDTO nameProperty = ConfigurationDAO.instance().getPropertyByCode(StandardProperty.nameattribute.name());
      baseEntityDAO.loadPropertyRecords(nameProperty);
      IClassifierDTO natureClassifier = baseEntityDTO.getNatureClassifier();
      Set<IClassifierDTO> otherClassifiers = baseEntityDTO.getOtherClassifiers();
      List<String> types = new ArrayList<String>();
      types.add(natureClassifier.getClassifierCode());
      List<String> localeIds = baseEntityDTO.getLocaleIds();
      for (IClassifierDTO otherClassifier : otherClassifiers) {
        if (otherClassifier.getClassifierType()
            .equals(ClassifierType.CLASS)) {
          types.add(otherClassifier.getCode());
        }
        if (types.contains(SystemLevelIds.GOLDEN_ARTICLE_KLASS)) {
          if (!goldenRecordLanguages.isEmpty()) {
            throw new MultipleGoldenRecordsFoundInBucket();
          }
          goldenRecordId = baseEntityIId;
          goldenRecordCreationLanguage = baseEntityDTO.getBaseLocaleID();
          goldenRecordLanguages.addAll(localeIds);
        }
      }
      
      IGoldenRecordSourceInfoModel goldenRecordSourceInfoModel = new GoldenRecordSourceInfoModel();
      goldenRecordSourceInfoModel.setId(baseEntityIId.toString());
      goldenRecordSourceInfoModel.setLanguageCodes(localeIds);
      String organizationCode = baseEntityDTO.getSourceOrganizationCode();
      if (organizationCode.equals("stdo")) {
        organizationCode = "-1";
      }
      goldenRecordSourceInfoModel.setOrganizationId(organizationCode);
      Set<IPropertyRecordDTO> propertyRecords = baseEntityDTO.getPropertyRecords();
      for (IPropertyRecordDTO propertyRecord : propertyRecords) {
        if (propertyRecord.getProperty().getCode().equals(StandardProperty.nameattribute.name())) {
          goldenRecordSourceInfoModel.setName(((IValueRecordDTO)propertyRecord).getValue());
        }
      }
      
      baseEntitiesInfo.add(goldenRecordSourceInfoModel);
    }
    
    IGetSourceInfoForBucketResponseModel returnMap = new GetSourceInfoForBucketResponseModel();
    if (goldenRecordId != null) {
      returnMap.setGoldenRecordId(goldenRecordId.toString());      
    }
    returnMap.setGoldenRecordLanguages(goldenRecordLanguages);
    returnMap.setCreationLanguage(goldenRecordCreationLanguage);
    returnMap.setSourceInstances(baseEntitiesInfo);
    return returnMap;
  }
  
  private IConfigDetailsForGetSourceInfoForBucketResponseModel getConfigDetails(IGetSourceInfoForBucketResponseModel returnModel) throws Exception
  {
    Set<String> languageCodes = new HashSet<>();
    Set<String> organizationIds = new HashSet<>();
    List<IGoldenRecordSourceInfoModel> sourceInstances = returnModel.getSourceInstances();
    for (IGoldenRecordSourceInfoModel sourceInstance : sourceInstances) {
      languageCodes.addAll(sourceInstance.getLanguageCodes());
      organizationIds.add(sourceInstance.getOrganizationId());
    }
    
    IRuleIdLangaugeCodesModel ruleLanguageCodesModel = new RuleIdLangaugeCodesModel();
    ruleLanguageCodesModel.setLanguageCodes(new ArrayList<>(languageCodes));
    ruleLanguageCodesModel.setOrganizationIds(new ArrayList<>(organizationIds));
    ruleLanguageCodesModel.setRuleId(returnModel.getRuleId());
    
    IConfigDetailsForGetSourceInfoForBucketResponseModel configDetails = getConfigDetailsForGetSourceInfoForBucketStrategy.execute(ruleLanguageCodesModel);
    return configDetails;
  }
}
