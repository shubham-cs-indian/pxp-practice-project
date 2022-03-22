package com.cs.pim.runtime.articleinstance;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.standard.IConfigMap;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.services.resolvers.RuleHandler;
import com.cs.core.runtime.interactor.exception.articleinstance.UserNotHaveCreatePermissionForArticle;
import com.cs.core.runtime.interactor.model.clone.ICreateKlassInstanceSingleCloneModel;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsWithoutPermissionsStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.klassinstance.AbstractCreateInstanceSingleClone;
import com.cs.core.util.ConfigUtil;

@Service
public class CreateArticleInstanceCloneForLinkedVariantService
    extends AbstractCreateInstanceSingleClone<ICreateKlassInstanceSingleCloneModel, IGetKlassInstanceModel>
    implements ICreateArticleInstanceCloneForLinkedVariantService {

  @Autowired
  protected IGetConfigDetailsWithoutPermissionsStrategy getConfigDetailsWithoutPermissionsStrategy;

  @Autowired
  protected ConfigUtil configUtil;

  @Override
  protected IBaseEntityDTO createClone(long baseEntityIID, Collection<IPropertyDTO> propertiesToClone, ILocaleCatalogDAO localeCatlogDAO,
      ICreateKlassInstanceSingleCloneModel dataModel) throws Exception
  {
    IMulticlassificationRequestModel multiclassificationRequestModel = this.configUtil.getConfigRequestModelForGivenTypesTaxonomies(
        dataModel.getCloneData().getTypes(), dataModel.getCloneData().getTaxonomies());
    List<String> types = dataModel.getCloneData().getTypes();
    IGetConfigDetailsForCustomTabModel configDetails = getConfigDetailsWithoutPermissionsStrategy.execute(multiclassificationRequestModel);
    Set<IClassifierDTO> classifiersToBeRemoved = fillClassifierData(dataModel);
    Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses = configDetails.getReferencedKlasses();
    String natureType = referencedKlasses.entrySet()
        .stream()
        .filter(e -> e.getValue().getIsNature().equals(true) && types.contains(e.getKey()))
        .map(Map.Entry::getKey)
        .findFirst()
        .get();
    IClassifierDTO natureKlassDTO = classifiersToBeRemoved.stream()
        .filter(classifer -> classifer.getClassifierCode().equals(natureType))
        .findFirst()
        .get();
    classifiersToBeRemoved.remove(natureKlassDTO);
    String type = dataModel.getType();
    BaseType linkedVariantBaseType =  BaseType.ARTICLE;
    if (type !=null && !type.isEmpty()) {
      linkedVariantBaseType = IConfigMap.getBaseType(type);
    }
    String linkedVariantEntityID = RDBMSUtils.newUniqueID(linkedVariantBaseType.getPrefix());
    IBaseEntityDTO linkedVariantDTO = localeCatlogDAO.createManualLinkedVariant(baseEntityIID, natureKlassDTO, classifiersToBeRemoved, linkedVariantEntityID,
        linkedVariantBaseType, propertiesToClone.toArray(new IPropertyDTO[0]));
     KlassInstanceUtils.handleDefaultImage(localeCatlogDAO.openBaseEntity(linkedVariantDTO));
    RuleHandler ruleHandler = new RuleHandler();
    ruleHandler.initiateRuleHandling(rdbmsComponentUtils.getBaseEntityDAO(linkedVariantDTO), localeCatlogDAO, false,
        configDetails.getReferencedElements(), configDetails.getReferencedAttributes(), configDetails.getReferencedTags());
    
    rdbmsComponentUtils.createNewRevision(linkedVariantDTO, configDetails.getNumberOfVersionsToMaintain());

     return linkedVariantDTO;
  }

  @Override
  protected IGetKlassInstanceModel executeInternal(ICreateKlassInstanceSingleCloneModel dataModel) throws Exception
  {
    return super.executeInternal(dataModel);
  }

  @Override
  protected Exception getUserNotHaveCreatePermissionException()
  {
    return new UserNotHaveCreatePermissionForArticle();
  }

  @Override
  protected Boolean shouldCheckClonePermission()
  {
    return false;
  }
}
