package com.cs.core.config.goldenrecord;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.config.interactor.entity.goldenrecord.IGoldenRecordRule;
import com.cs.core.config.interactor.model.goldenrecord.IGetGoldenRecordRuleModel;
import com.cs.core.config.interactor.model.goldenrecord.ISaveGoldenRecordRuleModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.physicalcatalog.util.PhysicalCatalogUtils;
import com.cs.core.config.strategy.usecase.goldenrecord.ISaveGoldenRecordRuleStrategy;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.entity.idto.IGoldenRecordDTO;
import com.cs.core.runtime.instancetree.GoldenRecordUtils;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelNatureTypeModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;

@Service
public class SaveGoldenRecordRuleService extends AbstractSaveConfigService<ISaveGoldenRecordRuleModel, IGetGoldenRecordRuleModel>
    implements ISaveGoldenRecordRuleService {
  
  @Autowired
  protected ISaveGoldenRecordRuleStrategy saveGoldenRecordRuleStrategy;
  
  @Autowired
  protected GoldenRecordRuleValidations   goldenRecordRuleValidations;

  @Autowired
  protected GoldenRecordUtils             goldenRecordUtils;
  
  private static final String             SAVE_GOLDEN_RECORD_RULE_SERVICE = "SAVE_GOLDEN_RECORD_RULE_SERVICE";
  
  /*@Autowired
  protected IGetKlassInstancesByKlassAndTaxonomyIdsStrategy getAllInstancesByKlassAndTaxonomyIdsStrategy;*/
  
  @Override
  public IGetGoldenRecordRuleModel executeInternal(ISaveGoldenRecordRuleModel model) throws Exception
  {
    // API level validations
    goldenRecordRuleValidations.validate(model);
    
    model.setAvailablePhysicalCatalogs(PhysicalCatalogUtils.getAvailablePhysicalCatalogs());
    IGetGoldenRecordRuleModel goldenRecordRuleDetail = saveGoldenRecordRuleStrategy.execute(model);
    evaluateGoldenRecordRuleBuckets(goldenRecordRuleDetail);
    
    return goldenRecordRuleDetail;
  }
  
  private void evaluateGoldenRecordRuleBuckets(IGetGoldenRecordRuleModel goldenRecordRuleDetail) throws Exception
  {
    IGoldenRecordRule goldenRecordRule = goldenRecordRuleDetail.getGoldenRecordRule();
    Boolean needToEvaluateGoldenRecordRuleBucket = goldenRecordRuleDetail.getNeedToEvaluateGoldenRecordRuleBucket();
    if(!needToEvaluateGoldenRecordRuleBucket) {
      return;
    }
    List<Long> classifierIids = new ArrayList<Long>();
    Map<String, IIdLabelNatureTypeModel> referencedKlasses = goldenRecordRuleDetail.getReferencedKlasses();
    Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies = goldenRecordRuleDetail.getReferencedTaxonomies();
    
    for (String klassId : goldenRecordRule.getKlassIds()) {
      classifierIids.add(referencedKlasses.get(klassId).getIid());
    }
    
    for (String taxonomyId : goldenRecordRule.getTaxonomyIds()) {
      classifierIids.add(referencedTaxonomies.get(taxonomyId).getClassifierIID());
    }
    Set<Long> baseEntityIids = new HashSet<Long>(RDBMSUtils.getBaseentityIIDsForTypes(classifierIids));
    IGoldenRecordDTO goldenRecordDTO = goldenRecordUtils.prepareGoldenRecordDTO(goldenRecordRule, baseEntityIids);
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.MEDIUM;
    BGPDriverDAO.instance().submitBGPProcess(context.getUserSessionDTO().getUserName(), SAVE_GOLDEN_RECORD_RULE_SERVICE, "", userPriority,
        new JSONContent(goldenRecordDTO.toJSON()));
    
  }
}
