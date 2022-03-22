package com.cs.core.runtime.interactor.usecase.taxonomy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.cs.core.runtime.interactor.model.targetinstance.IGetTargetKlassesModel;
import com.cs.core.runtime.interactor.model.taxonomy.IGetTaxonomyTreeForQuicklistModel;
import com.cs.core.runtime.interactor.model.templating.IConfigDetailsForHierarchyRelationshipQuicklistModel;
import com.cs.core.runtime.interactor.usecase.instance.AbstractKlassTaxonomyForRelationshipQuickList;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForKlassTaxonomyRelationshipQuicklistStrategy;

@Service
public class GetKlassTaxonomyForRelationshipQuicklist extends
    AbstractKlassTaxonomyForRelationshipQuickList<IGetTaxonomyTreeForQuicklistModel, ICategoryInformationModel>
    implements IGetKlassTaxonomyForRelationshipQuicklist {
  
  /*@Autowired
  protected IGetKlassTaxonomyContentCountForQuicklistStrategy              getKlassTaxonomyContentCountForQuicklistStrategy;
  */
  @Autowired
  protected IGetConfigDetailsForKlassTaxonomyRelationshipQuicklistStrategy getConfigDetailsForKlassTaxonomyRelationshipQuicklistStrategy;
  
  @Override
  protected IConfigDetailsForHierarchyRelationshipQuicklistModel getConfigDetails(
      IGetTargetKlassesModel model, IGetTaxonomyTreeForQuicklistModel dataModel) throws Exception
  {
    return getConfigDetailsForKlassTaxonomyRelationshipQuicklistStrategy.execute(model);
  }
  
  @Override
  protected ICategoryInformationModel executeGetTaxonomyContentCountForQuickList(
      IGetTaxonomyTreeForQuicklistModel model) throws Exception
  {
    return /*getKlassTaxonomyContentCountForQuicklistStrategy.execute(model)*/ null;
  }
  
}
