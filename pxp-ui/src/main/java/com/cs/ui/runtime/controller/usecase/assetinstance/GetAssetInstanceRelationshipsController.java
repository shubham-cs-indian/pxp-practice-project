package com.cs.ui.runtime.controller.usecase.assetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.filter.KlassInstanceRelationshipsFilterModel;
import com.cs.core.runtime.interactor.model.klassinstance.GetKlassInstanceRelationshipsStrategyModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceRelationshipsStrategyModel;
import com.cs.core.runtime.interactor.usecase.assetinstance.IGetAssetInstanceRelationships;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetAssetInstanceRelationshipsController extends BaseController implements IRuntimeController {
  
  @Autowired
  IGetAssetInstanceRelationships getAssetRelationshipInstances;
  
  @RequestMapping(value = "/assetinstancesrelationships/{id}", method = RequestMethod.POST)
  public IRESTModel execute(@PathVariable String id, @RequestBody KlassInstanceRelationshipsFilterModel filterModel) throws Exception
  {
    IGetKlassInstanceRelationshipsStrategyModel model = new GetKlassInstanceRelationshipsStrategyModel();
    model.setFrom(filterModel.getFrom());
    model.setSize(filterModel.getSize());
    model.setId(id);
    model.setAttributes(filterModel.getAttributes());
    model.setTags(filterModel.getTags());
    model.setAllSearch(filterModel.getAllSearch());
    model.setGetFolders(filterModel.getGetFolders());
    model.setGetLeaves(filterModel.getGetLeaves());
    model.setSelectedRoles(filterModel.getSelectedRoles());
    model.setRelationshipId(filterModel.getRelationshipId());
    model.setBaseType(filterModel.getBaseType());
    model.setSortOptions(filterModel.getSortOptions());
    model.setIsLinked(filterModel.getIsLinked());
    model.setIsNatureRelationship(filterModel.getIsNatureRelationship());
    model.setSideId(filterModel.getSideId());
    model.setXRayAttributes(filterModel.getXRayAttributes());
    model.setXRayTags(filterModel.getXRayTags());
    return createResponse(getAssetRelationshipInstances.execute(model));
  }
}
