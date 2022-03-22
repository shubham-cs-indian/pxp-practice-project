/*package com.cs.core.runtime.strategy.db;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.klassinstance.ArticleInstanceQueryConstants;
import com.cs.core.runtime.interactor.entity.klassinstance.ArticleInstance;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceInformationModel;

public class PostgreInstanceUtil {


  public static List<IKlassInstanceInformationModel> getIKlassInstanceInformationModels(List<ArticleInstance> articleInstances) {
    List<IKlassInstanceInformationModel> infoModels = new ArrayList<>();
    for(ArticleInstance articleInstance : articleInstances) {
      infoModels.add(new KlassInstanceInformationModel(articleInstance));
    }

    return infoModels;
  }

  public static IKlassInstanceInformationModel getIKlassInstanceInformationModels(ArticleInstance articleInstance) {
	     IKlassInstanceInformationModel infoModels = new KlassInstanceInformationModel();
	
	      infoModels=(new KlassInstanceInformationModel(articleInstance));
	
	    return infoModels;
	}

  public static String getQueryForDeleteInstance(String baseType)
  {

    switch (baseType) {
      case Constants.ARTICLE_INSTANCE_BASE_TYPE:
                return  PostgreConnectionDetails.SCHEMA_AND_QUERIES.getProperty(ArticleInstanceQueryConstants.ARTICLE_DELETE_BY_IDS);

      case Constants.ASSET_INSTANCE_BASE_TYPE:
        return  PostgreConnectionDetails.SCHEMA_AND_QUERIES.getProperty(ArticleInstanceQueryConstants.ASSET_DELETE_BY_IDS);

      default:
        return null;
    }
  }

}
*/
