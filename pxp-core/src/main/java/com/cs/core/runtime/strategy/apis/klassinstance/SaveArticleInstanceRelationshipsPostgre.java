/*package com.cs.core.runtime.strategy.apis.klassinstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.entity.relationship.IRelationshipInstance;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.ReferencedSectionRelationshipModel;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRelationshipVersion;
import com.cs.core.runtime.interactor.model.configdetails.GetConfigDetailsForSaveRelationshipInstancesRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.IGetConfigDetailsForSaveRelationshipInstancesRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.IGetConfigDetailsForSaveRelationshipInstancesResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.instance.GetInstanceRequestStrategyModelForCustomTab;
import com.cs.core.runtime.interactor.model.instance.IContentRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.relationship.ISaveRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.relationship.ISaveRelationshipInstanceStrategyModel;
import com.cs.core.runtime.interactor.model.relationship.SaveRelationshipInstanceStrategyModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.articleinstance.ISaveArticleInstanceRelationships;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForSaveRelationshipInstancesStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.RelationshipInstanceUtil;
import com.cs.core.runtime.strategy.usecase.klassinstance.IGetKlassInstanceTypeStrategy;
import com.cs.core.runtime.strategy.usecase.klassinstance.ISaveKlassInstanceRelationshipsStrategy;
import com.cs.core.runtime.strategy.usecase.templating.IGetArticleInstanceForCustomTabStrategy;

@Service()
public class SaveArticleInstanceRelationshipsPostgre implements ISaveArticleInstanceRelationships {

  @Autowired
  protected ISaveKlassInstanceRelationshipsStrategy               saveArticleInstanceRelationshipsStrategyPostgre;

  @Autowired
  protected IGetConfigDetailsForSaveRelationshipInstancesStrategy getConfigDetailsForSaveRelationshipInstancesStrategy;

  @Autowired
  protected IGetKlassInstanceTypeStrategy                         getArticleInstanceTypeStrategyPostgre;

  @Autowired
  IGetArticleInstanceForCustomTabStrategy                         getArticleInstanceByIdPostgre;

  @Override
  public IGetKlassInstanceModel execute(ISaveRelationshipInstanceModel dataModel) throws Exception
  {
    IKlassInstanceTypeModel klassInstanceTypes = getKlassInstanceType(dataModel.getId());
    IGetConfigDetailsForSaveRelationshipInstancesResponseModel configDetails = getConfigDetails(
        dataModel, klassInstanceTypes);

    List<IContentRelationshipInstanceModel> modifiedRelationships = dataModel
        .getModifiedRelationships();
    Map<String, IReferencedSectionElementModel> referencedElements = configDetails
        .getReferencedElements();
    String sideId = getSideId(modifiedRelationships);
    ReferencedSectionRelationshipModel side = (ReferencedSectionRelationshipModel) referencedElements
        .get(sideId);
    String otherSideId = side.getRelationshipSide()
        .getTargetRelationshipMappingId();
    String side2EntityType = side.getRelationshipSide()
        .getKlassId();
    ReferencedSectionRelationshipModel otherSide = (ReferencedSectionRelationshipModel) referencedElements
        .get(otherSideId);
    String side1EntityType = otherSide.getRelationshipSide()
        .getKlassId();
    prepareExpectedRelationshipInstanceModel(modifiedRelationships, dataModel.getId(),
        dataModel.getBaseType(), false, otherSideId, side1EntityType, side2EntityType);
    ISaveRelationshipInstanceStrategyModel strategyModel = new SaveRelationshipInstanceStrategyModel();
    strategyModel.setRelationshipAdm(dataModel);
    saveArticleInstanceRelationshipsStrategyPostgre.execute(strategyModel);
    GetInstanceRequestStrategyModelForCustomTab getInstanceRequestStrategyModel = new GetInstanceRequestStrategyModelForCustomTab();
    getInstanceRequestStrategyModel.setId(dataModel.getId());
    IGetKlassInstanceModel returnModel = getArticleInstanceByIdPostgre
        .execute(getInstanceRequestStrategyModel);
    return returnModel;

  }

  private IKlassInstanceTypeModel getKlassInstanceType(String id) throws Exception
  {

    IIdParameterModel idParameterModel = new IdParameterModel(id);
    return getArticleInstanceTypeStrategyPostgre.execute(idParameterModel);
  }

  private void prepareExpectedRelationshipInstanceModel(
      List<IContentRelationshipInstanceModel> modifiedRelationships, String side1InstanceId,
      String side1BaseType, Boolean isNatureRelationship, String otherSideId,
      String side1EntityType, String side2EntityType)
  {
    for (IContentRelationshipInstanceModel iContentRelationshipInstance : modifiedRelationships) {
      String relationshipId = iContentRelationshipInstance.getRelationshipId();
      List<IRelationshipVersion> addedElements = iContentRelationshipInstance.getAddedElements();
      if (addedElements.isEmpty()) {
        continue;
      }
      String sideId = iContentRelationshipInstance.getSideId();
      String side2BaseType = iContentRelationshipInstance.getBaseType();

      for (IRelationshipVersion addedElement : addedElements) {
        IRelationshipInstance side1RelationshipInstance = RelationshipInstanceUtil
            .getRelationshipInstanceFromRelationshipVersion(addedElement, relationshipId, sideId,
                side1InstanceId, side1BaseType, side2BaseType, otherSideId, side1EntityType,
                side2EntityType);

        iContentRelationshipInstance.getAddedRelationshipInstances()
            .add(side1RelationshipInstance);
      }

    }
  }

  private String getSideId(List<IContentRelationshipInstanceModel> modifiedRelationships)
  {
    String sideId = "";
    for (IContentRelationshipInstanceModel iContentRelationshipInstance : modifiedRelationships) {

      sideId = iContentRelationshipInstance.getSideId();
      break;

    }
    return sideId;
  }

  private IGetConfigDetailsForSaveRelationshipInstancesResponseModel getConfigDetails(
      ISaveRelationshipInstanceModel dataModel, IKlassInstanceTypeModel klassInstanceTypes)
      throws Exception
  {
    IGetConfigDetailsForSaveRelationshipInstancesRequestModel configDetailsRequestModel = new GetConfigDetailsForSaveRelationshipInstancesRequestModel();
    configDetailsRequestModel
        .setRelationshipIds(getRelationshipIds(dataModel.getModifiedRelationships()));
    configDetailsRequestModel
        .setNatureRelationshipIds(getRelationshipIds(dataModel.getModifiedNatureRelationships()));
    configDetailsRequestModel.setKlassIds(new ArrayList<>(klassInstanceTypes.getTypes()));
    configDetailsRequestModel.setTaxonomyIds(klassInstanceTypes.getSelectedTaxonomyIds());
    IGetConfigDetailsForSaveRelationshipInstancesResponseModel configDetails = getConfigDetailsForSaveRelationshipInstancesStrategy
        .execute(configDetailsRequestModel);
    return configDetails;
  }

  private List<String> getRelationshipIds(
      List<IContentRelationshipInstanceModel> modifiedRelationships)
  {
    return modifiedRelationships.stream()
        .map(IContentRelationshipInstanceModel::getRelationshipId)
        .distinct()
        .collect(Collectors.toList());
  }

}
*/
