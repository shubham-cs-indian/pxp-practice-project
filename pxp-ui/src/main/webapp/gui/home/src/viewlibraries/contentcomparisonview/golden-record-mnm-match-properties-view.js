import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import { view as SmallTaxonomyView } from './../small-taxonomy-view/small-taxonomy-view';
import SmallTaxonomyViewModel from './../small-taxonomy-view/model/small-taxonomy-view-model';
import { view as AttributeReadOnlyView } from '../attributereadonlyview/attribute-read-only-view';
import { view as TagGroupView } from './../taggroupview/tag-group-view';
import ViewUtils from '../../screens/homescreen/screens/contentscreen/view/utils/view-utils';
import ViewLibraryUtils from './../utils/view-library-utils';
import { view as ChipsView } from '../../viewlibraries/chipsView/chips-view';
import {view as ImageFitToContainerView} from "../imagefittocontainerview/image-fit-to-container-view";

const oEvents = {};

const oPropTypes = {
  aAttributes: ReactPropTypes.array,
  aTags: ReactPropTypes.string,
  oReferencedAttributes: ReactPropTypes.string,
  oReferencedTags: ReactPropTypes.func,
  aUserList: ReactPropTypes.array,
  aKlassesData: ReactPropTypes.array,
  oMultiTaxonomyData: ReactPropTypes.object
};

function GoldenRecordMnmMatchPropertiesView ({aAttributes, aTags, oReferencedAttributes, oReferencedTags, aUserList, aKlassesData, oMultiTaxonomyData}) {
  let getAttributeElementView = (oAttribute, oMasterAttribute) => {
    return (
        <AttributeReadOnlyView attributeInstance={oAttribute} masterAttribute={oMasterAttribute} userList={aUserList}/>
    );
  }

  let getTagElementView = (oTag, oMasterTag) => {
    let oTags = {
      [oTag.id]: oTag
    };
    let oTagGroupModel = ViewLibraryUtils.getTagGroupModels(oMasterTag, {tags: oTags});
    let oProperties = oTagGroupModel.tagGroupModel.properties;
    return (<TagGroupView
        tagGroupModel={oTagGroupModel.tagGroupModel}
        tagRanges={oProperties.tagRanges}
        tagValues={oTagGroupModel.tagValues}
        disabled={true}
        singleSelect={oProperties.singleSelect}
        hideTooltip={false}
        showLabel={false}
        // extraData={oExtraData}
        masterTagList={[oMasterTag]}
        showMoreButton={false}
        showDefaultIcon={true}/>
    );
  }

  let getPropertyItemView = (sId, sLabel, oView, sContext, sImageSrc) => {
    let sContainerClassName = "propertyItem ";
    let oIconDOM = null;
    if (sContext === "attribute") {
      sContainerClassName += "isForAttribute ";
      oIconDOM = getIconDOM(sImageSrc);
    } else if(sContext === "tag") {
      sContainerClassName += "isForTag";
      oIconDOM = getIconDOM(sImageSrc);
    }
    return (
        <div className={sContainerClassName} key={sId}>
          {oIconDOM}
          <div className="propertyLabel">{sLabel}</div>
          <div className="propertyValue">{oView}</div>
        </div>
    );
  }


  let getIconDOM = (sImageSrc) => {
    return (
        <div className="customIcon">
          <ImageFitToContainerView imageSrc={sImageSrc}/>
        </div>
    );
  };

  let getSmallTaxonomyViewModel = (oMultiTaxonomyData) => {
    var oSelectedMultiTaxonomyList = oMultiTaxonomyData.selected;
    var oMultiTaxonomyListToAdd = oMultiTaxonomyData.notSelected;
    var sRootTaxonomyId = ViewLibraryUtils.getTreeRootId();
    var oProperties = {};
    oProperties.headerPermission = {};
    return new SmallTaxonomyViewModel('', oSelectedMultiTaxonomyList, oMultiTaxonomyListToAdd, getTranslation().ADD_TAXONOMY, 'forMultiTaxonomy', sRootTaxonomyId, oProperties)
  };


  let aPropertyViews = [];

  let oKlassListView = (
      <div className="klassChipsContainer">
        <ChipsView items={aKlassesData}/>
      </div>
  );
  aPropertyViews.push(getPropertyItemView("klasses", getTranslation().CLASSES, oKlassListView));

  if (!CS.isEmpty(oMultiTaxonomyData.selected)) {
    let oSmallTaxonomyViewModel = getSmallTaxonomyViewModel(oMultiTaxonomyData);
    let oSmallTaxonomyView = (
        <SmallTaxonomyView
            model={oSmallTaxonomyViewModel}
            isDisabled={true}/>
    );
    aPropertyViews.push(getPropertyItemView("taxonomies", getTranslation().TAXONOMIES, oSmallTaxonomyView));
  }

  CS.forEach(aAttributes, function (oAttribute) {
    let oMasterAttribute = oReferencedAttributes[oAttribute.attributeId];
    if (!ViewUtils.isAttributeTypeCreatedOn(oMasterAttribute.type) && !ViewUtils.isAttributeTypeLastModified(oMasterAttribute.type)) {
      let oAttributeView = getAttributeElementView(oAttribute, oMasterAttribute);
      let sImageSrcForAttributes = ViewLibraryUtils.getIconUrl(oMasterAttribute.iconKey);
      let oPropertyView = getPropertyItemView(oAttribute.id, CS.getLabelOrCode(oMasterAttribute), oAttributeView, "attribute", sImageSrcForAttributes);
      aPropertyViews.push(oPropertyView);
    }
  });

  CS.forEach(aTags, function (oTag) {
    let oMasterTag = oReferencedTags[oTag.tagId];
    let oTagView = getTagElementView(oTag, oMasterTag);
    let sImageSrcForTags = ViewLibraryUtils.getIconUrl(oMasterTag.iconKey);
    let oPropertyView = getPropertyItemView(oTag.id, CS.getLabelOrCode(oMasterTag), oTagView, "tag", sImageSrcForTags);
    aPropertyViews.push(oPropertyView);
  });


  return (
      <div className="propertyViewsList">
        {aPropertyViews}
      </div>
  );
}


GoldenRecordMnmMatchPropertiesView.propTypes = oPropTypes;

export var view = CS.SafeComponent(GoldenRecordMnmMatchPropertiesView)
export var events = oEvents;
