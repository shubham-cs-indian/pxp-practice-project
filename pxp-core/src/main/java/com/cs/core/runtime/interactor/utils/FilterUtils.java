package com.cs.core.runtime.interactor.utils;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.attribute.INumberAttribute;
import com.cs.core.config.interactor.entity.attribute.IUnitAttribute;
import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.config.interactor.model.klass.GetFilterInfoModel;
import com.cs.core.config.interactor.model.taxonomy.ApplicableFilterModel;
import com.cs.core.config.interactor.model.taxonomy.IApplicableFilterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInformationModel;
import com.cs.core.runtime.interactor.model.searchable.ISearchHitInfoModel;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("filterUtils")
@SuppressWarnings({ "unchecked" })
public class FilterUtils {
  
  public void updateHitsInfoLabels(List<IAttribute> attributes, List<ITag> tags,
      List<IKlassInstanceInformationModel> children) throws Exception
  {
    for (IKlassInstanceInformationModel child : children) {
      for (ISearchHitInfoModel hitInfoModel : child.getHits()) {
        
        if (hitInfoModel.getType()
            .equals("attribute")) {
          for (IAttribute attribute : attributes) {
            if (attribute.getId()
                .equals(hitInfoModel.getId())) {
              String type = attribute.getType();
              hitInfoModel.setCode(attribute.getCode());
              hitInfoModel.setLabel(attribute.getLabel());
              hitInfoModel.setType(type);
              if (IUnitAttribute.class.isAssignableFrom(attribute.getClass())) {
                IUnitAttribute unitAttribute = (IUnitAttribute) attribute;
                hitInfoModel.setDefaultUnit(unitAttribute.getDefaultUnit());
                hitInfoModel.setPrecision(unitAttribute.getPrecision());
              }
              if (INumberAttribute.class.isAssignableFrom(attribute.getClass())) {
                INumberAttribute numberAttribute = (INumberAttribute) attribute;
                hitInfoModel.setPrecision(numberAttribute.getPrecision());
              }
            }
          }
        }
        
        // TODO :: not working for tags rightnow..
        if (hitInfoModel.getType()
            .equals("tag")) {
          for (ITag tag : tags) {
            if (tag.getId()
                .equals(hitInfoModel.getId())) {
              hitInfoModel.setLabel(tag.getLabel());
              hitInfoModel.setCode(tag.getCode());
              hitInfoModel.setType(tag.getTagType());
              for (ISearchHitInfoModel hitInfoChildModel : hitInfoModel.getValues()) {
                for (ITreeEntity childTag : tag.getChildren()) {
                  if (childTag.getId()
                      .equals(hitInfoChildModel.getId())) {
                    hitInfoChildModel.setLabel(((ITag) childTag).getLabel());
                    hitInfoChildModel.setCode(((ITag) childTag).getCode());
                  }
                }
              }
            }
          }
        }
      }
    }
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
      List<IApplicableFilterModel> childrensToSet = new ArrayList<>();
      List<IConfigEntityTreeInformationModel> childrens = (List<IConfigEntityTreeInformationModel>) filterInfo
          .getChildren();
      for (IConfigEntityTreeInformationModel children : childrens) {
        IApplicableFilterModel childrenFilterModel = new ApplicableFilterModel();
        childrenFilterModel.setId(children.getId());
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
    return filterInfoModel;
  }
}
