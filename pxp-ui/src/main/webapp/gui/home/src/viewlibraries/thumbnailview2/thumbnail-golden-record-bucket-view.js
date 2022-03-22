import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager';
import MarkerClassTypeDictionary from '../../commonmodule/tack/marker-class-type-dictionary';
import ViewLibraryUtils from './../utils/view-library-utils';
import { view as TabLayoutView } from '../tablayoutview/tab-layout-view';
import ResizeSensor from 'css-element-queries/src/ResizeSensor';
import GoldenRecordBucketTabs from '../../commonmodule/tack/golden-record-bucket-tabs';
import { view as ImageSimpleView } from './../imagesimpleview/image-simple-view';
import { view as SmallTaxonomyView } from './../small-taxonomy-view/small-taxonomy-view';
import SmallTaxonomyViewModel from './../small-taxonomy-view/model/small-taxonomy-view-model';
import { view as AttributeReadOnlyView } from '../attributereadonlyview/attribute-read-only-view';
import { view as TagGroupView } from './../taggroupview/tag-group-view';
import TooltipView from '../tooltipview/tooltip-view';
import {view as ImageFitToContainerView} from "../imagefittocontainerview/image-fit-to-container-view";

const oEvents = {
  THUMBNAIL_GOLDEN_RECORD_BUCKET_VIEW_TAB_CHANGED: "thumbnail_golden_record_bucket_view_tab_changed",
  THUMBNAIL_GOLDEN_RECORD_BUCKET_VIEW_MERGE_BUTTON_CLICKED: "thumbnail_golden_record_bucket_view_merge_button_clicked",
};

const oPropTypes = {
  id: ReactPropTypes.string,
  name: ReactPropTypes.string,
  matchCount: ReactPropTypes.number,
  activeTabId: ReactPropTypes.string,
  matches: ReactPropTypes.arrayOf(ReactPropTypes.shape({
    defaultAssetInstanceId: ReactPropTypes.string,
    id: ReactPropTypes.string,
    klassesAndTaxonomies: ReactPropTypes.string,
    name: ReactPropTypes.string,
    supplier: ReactPropTypes.shape({
      id: ReactPropTypes.string,
      label: ReactPropTypes.string
    }),
    taxonomyIds: ReactPropTypes.array,
    types: ReactPropTypes.array
  })),
  attributes: ReactPropTypes.array,
  referencedAttributes: ReactPropTypes.object,
  tags: ReactPropTypes.array,
  referencedTags: ReactPropTypes.object,
  multiTaxonomyData: ReactPropTypes.object,
  klassesData: ReactPropTypes.array,
  userList: ReactPropTypes.array
};
/**
 * @class ThumbnailGoldenRecordBucketView - Use for Display golden article and merge them in the View.
 * @memberOf Views
 * @property {string} [id] - Pass id of the Article.
 * @property {string} [name] - Pass Golden record Rule Name.
 * @property {number} [matchCount] - Pass count number of match article.
 * @property {string} [activeTabId] - Pass id of active tab. (properties or matches)
 * @property {array} [matches] - Pass array of matches like id, klassesAndTaxonomies, name, partnerSource, supplier, taxonomyIds, types.
 * @property {array} [attributes] - Pass array of details of attributes in article like attributeId, createdBy,lastModifiedBy etc.
 * @property {object} [referencedAttributes] - Pass object of reference attributes in article like attributeId, createdBy,lastModifiedBy etc.
 * @property {array} [tags] - Pass array of tags use in article like id, tagId, tagValue etc.
 * @property {object} [referencedTags] - Pass object of reference tags in article like attributeId, createdBy,lastModifiedBy etc.
 * @property {object} [multiTaxonomyData] - Pass object of selected and not selected Data.
 * @property {array} [klassesData] - Pass array of data like id and label.
 * @property {array} [userList] - Pass array of users who use this article and their detail like id , name , lastName,contact
 */


// @CS.SafeComponent
class ThumbnailGoldenRecordBucketView extends React.Component {

  static propTypes = oPropTypes;

  constructor(props) {
    super(props);

    this.state = {
    };

    this.changeTab = this.changeTab.bind(this);
    this.getBodyView = this.getBodyView.bind(this);
    this.handleMergeButtonClicked = this.handleMergeButtonClicked.bind(this);

    this.thumbnailGoldenRecordBucketView = React.createRef();
    this.noUseDiv = React.createRef();
  }

  componentDidUpdate = () => {
    this.adjustThumbnailWidthAndHeight();
  };

  componentDidMount = () => {
    this.attachResizeEventListener();
    this.adjustThumbnailWidthAndHeight();
  };

  attachResizeEventListener = () => {

    var _this = this;
    //var $parentContainer = $(this.noUseDiv.current).parents('[data-container-type="thumbnailContainer"]').eq(0);

    var oParentContainer = this.thumbnailGoldenRecordBucketView["current"].parentElement;
    if (oParentContainer) {
      //var element = parentContainer.get(0);
      new ResizeSensor(oParentContainer, CS.throttle(_this.adjustThumbnailWidthAndHeight, 300));
    }
  };

  adjustThumbnailWidthAndHeight = () => {

    //var $parentContainer = $(this.noUseDiv.current).parents('[data-container-type="thumbnailContainer"]').eq(0);
    var oParentContainer = this.thumbnailGoldenRecordBucketView["current"].parentElement;
    var bAdjustWidth = true;

    if (oParentContainer) {
      var iWidth = oParentContainer.clientWidth;
      var iThumbsInARow = 1;
      var iThumbnailWidth = Math.floor(iWidth / iThumbsInARow - 20);
      while (iThumbnailWidth > 300) {
        iThumbsInARow++;
        iThumbnailWidth = Math.floor(iWidth / iThumbsInARow - 20);
      }

      iThumbnailWidth = iThumbnailWidth < 250 ? 250 : iThumbnailWidth;

      var oThumbnail = this.thumbnailGoldenRecordBucketView.current;
      if (bAdjustWidth) {
        oThumbnail.style.width = iThumbnailWidth + "px";
      }
    }
  };

/*
  getThumbnailImageView = () => {
    var sType = this.props.type;
    var oProperties = {};
    var oImageViewStyle = {};

    var oImageViewModel = new ImageViewModel(
        '',
        this.props.imageSrc,
        sType,
        "thumbnailImagePreview",
        oImageViewStyle,
        oProperties
    );

    return (<ImageView model={oImageViewModel} onLoad={this.handleImageOnLoad}/>);
  };
*/

  getEntityInfoIconView (sLabel, sValue, sClass) {
    if(CS.isEmpty(sValue)) {
      return null;
    }

    let oToolTipView = (
        <div className="entityInformationIconTooltip">
          <div className="entityInformationIconLabel">{sLabel}</div>
          <div className="parentEntityInformationLabel">{sValue}</div>
        </div>);

    let oView = (
        <div className="entityInformationIconIndicator">
          <div className={"entityInformationIcon " + sClass}/>
        </div>);

    return (
        <TooltipView label={oToolTipView} placement="bottom">
          {oView}
        </TooltipView>
    );
  };

  getContentTiles () {
    let _this = this;
    let aContentTiles = [];
    let aContentList = this.props.matches;

    CS.forEach(aContentList, function (oContent) {
      let oAssetObject = oContent.assetObject;
      let sThumbnailKeySrc = "";
      if (oAssetObject) {
        sThumbnailKeySrc = oAssetObject.thumbKeySrc;
      }

      let sKlassesAndTaxonomies = oContent.klassesAndTaxonomies;

      let aTypes = oContent.types;
      let oGoldenRecordIndicator = null;
      if (aTypes.indexOf(MarkerClassTypeDictionary.GOLDEN_RECORD) >= 0) {
        oGoldenRecordIndicator = (
            <TooltipView label={getTranslation().GOLDEN_RECORD}>
              <div className="goldenRecordIndicator"></div>
            </TooltipView>
        );
      }

      let oSupplier = oContent.supplier;
      let oSupplierSection = null;
      if (!CS.isEmpty(oSupplier)) {
        oSupplierSection = (
            <div className="contentSupplier">
              {CS.getLabelOrCode(oSupplier)}
            </div>
        );
      }
      let sTitle  = ViewLibraryUtils.getContentName(oContent);

      aContentTiles.push(
          <div key={oContent.id} className="matchingContentThumbnail">
            <div className="contentImageContainer">
              <ImageSimpleView imageSrc={sThumbnailKeySrc} classLabel="contentImage"/>
            </div>
            <div className="contentDetails">
              <div className="contentLabelContainer">
                <div className="contentLabel" title={sTitle}>
                  {sTitle}
                </div>
                {_this.getEntityInfoIconView(getTranslation().CLONE_OF, oContent.branchOfLabel, "cloneContextIcon")}
                {_this.getEntityInfoIconView(getTranslation().VARIANT_OF, oContent.variantOfLabel, "variantContextIcon")}
                {oGoldenRecordIndicator}
              </div>
              {oSupplierSection}
              <div className="contentKlassesAndTaxonomies" title={sKlassesAndTaxonomies}>
                {sKlassesAndTaxonomies}
              </div>
            </div>
          </div>
      );
    });
    return aContentTiles;
  }

  getAttributeElementView (oAttribute, oMasterAttribute) {
    let aUserList = this.props.userList;

    return (
        <AttributeReadOnlyView attributeInstance={oAttribute} masterAttribute={oMasterAttribute} userList={aUserList}/>
    );
  }

  getTagElementView (oTag, oMasterTag) {
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
        showDefaultIcon={true}
        />);
  }

  getPropertyItemView (sId, sLabel, oView, bIsForAttribute, sImageSrc, bIsForTag) {
    let sContainerClassName = "propertyItem ";
    let oIconDOM = null;
    if (bIsForAttribute) {
      sContainerClassName += "isForAttribute ";
    }
    if(bIsForAttribute || bIsForTag){
      oIconDOM = this.getIconDOM(sImageSrc);
    }
    return (
        <div className={sContainerClassName} key={sId}>
          {oIconDOM}
          <div className="propertyLabel">{sLabel}</div>
          <div className="propertyValue">{oView}</div>
        </div>
    );
  }

  getIconDOM = (sImageSrc) => {
    return (
        <div className="customIcon">
          <ImageFitToContainerView imageSrc={sImageSrc}/>
        </div>
    );
  };

  getSmallTaxonomyViewModel = (oMultiTaxonomyData) => {
    var oSelectedMultiTaxonomyList = oMultiTaxonomyData.selected;
    var oMultiTaxonomyListToAdd = oMultiTaxonomyData.notSelected;
    var sRootTaxonomyId = ViewLibraryUtils.getTreeRootId();
    var oProperties = {};
    oProperties.headerPermission = {};
    return new SmallTaxonomyViewModel('', oSelectedMultiTaxonomyList, oMultiTaxonomyListToAdd, getTranslation().ADD_TAXONOMY, 'forMultiTaxonomy', sRootTaxonomyId, oProperties)
  };

  getPropertiesView() {
    let aAttributes = this.props.attributes;
    let oReferencedAttributes = this.props.referencedAttributes;
    let aTags = this.props.tags;
    let oReferencedTags = this.props.referencedTags;

    let aPropertyViews = [];
    let that = this;

    CS.forEach(aAttributes, function (oAttribute) {
      let oMasterAttribute = oReferencedAttributes[oAttribute.attributeId];
      let sImageSrcForAttributes = ViewLibraryUtils.getIconUrl(oMasterAttribute.iconKey);
      let oAttributeView = that.getAttributeElementView(oAttribute, oMasterAttribute);
      let oPropertyView = that.getPropertyItemView(oAttribute.id, CS.getLabelOrCode(oMasterAttribute), oAttributeView, true, sImageSrcForAttributes);
      aPropertyViews.push(oPropertyView);
    });

    CS.forEach(aTags, function (oTag) {
      let oMasterTag = oReferencedTags[oTag.tagId];
      let sImageSrcForTags = ViewLibraryUtils.getIconUrl(oMasterTag.iconKey);
      let oTagView = that.getTagElementView(oTag, oMasterTag);
      let oPropertyView = that.getPropertyItemView(oTag.id, CS.getLabelOrCode(oMasterTag), oTagView, false, sImageSrcForTags, true);
      aPropertyViews.push(oPropertyView);
    });

    let aKlassesData = this.props.klassesData;
    let oKlassListView = (
        <div className="klassChipsContainer">
          {CS.map(aKlassesData, function (oKlass) {
            return (
                <div key={oKlass.id} className="klassChip">{CS.getLabelOrCode(oKlass)}</div>
            );
          })}
        </div>
    );
    aPropertyViews.push(this.getPropertyItemView("klasses", getTranslation().CLASSES, oKlassListView));

    let oMultiTaxonomyData = this.props.multiTaxonomyData;
    if (!CS.isEmpty(oMultiTaxonomyData.selected)) {
      let oSmallTaxonomyViewModel = this.getSmallTaxonomyViewModel(oMultiTaxonomyData);
      let oSmallTaxonomyView = (
          <SmallTaxonomyView
              model={oSmallTaxonomyViewModel}
              isDisabled={true}/>
      );
      aPropertyViews.push(this.getPropertyItemView("taxonomies", getTranslation().TAXONOMIES, oSmallTaxonomyView));
    }

    return (
        <div className="propertyViewsList">
          {aPropertyViews}
        </div>
    );
  }

  getBodyView() {
    if (this.props.activeTabId === GoldenRecordBucketTabs.PROPERTIES) {
      return this.getPropertiesView();
    } else {
      return this.getContentTiles();
    }
  }

  changeTab (sTabId) {
    let sBucketId = this.props.id;
    EventBus.dispatch(oEvents.THUMBNAIL_GOLDEN_RECORD_BUCKET_VIEW_TAB_CHANGED, sBucketId, sTabId);
  }

  handleMergeButtonClicked () {
    let sBucketId = this.props.id;
    EventBus.dispatch(oEvents.THUMBNAIL_GOLDEN_RECORD_BUCKET_VIEW_MERGE_BUTTON_CLICKED, sBucketId); //todo: to be handled in store
  }

  render () {

    const aTabList = [
      {
        id: GoldenRecordBucketTabs.PROPERTIES,
        label: getTranslation().PROPERTIES
      },
      {
        id: GoldenRecordBucketTabs.MATCHES,
        label: getTranslation().MATCHES
      }
    ];

    return (
        <div className="thumbnailGoldenRecordBucketView" ref={this.thumbnailGoldenRecordBucketView}>

          <div className="headerSection">

            <div className="leftSection">
              <TooltipView placement="bottom" label={this.props.name}>
                <div className="headerLabel">{this.props.name}</div>
              </TooltipView>
              <div className="matches">
                <div className="matchLabel">{getTranslation().TOTAL_MATCHES}</div>
                <div className="matchCount">{this.props.matchCount}</div>
              </div>
            </div>
            <TooltipView placement="bottom" label={getTranslation().MERGE_RECORDS}>
            <div className="mergeButton" onClick={this.handleMergeButtonClicked}></div>
            </TooltipView>
          </div>

          <div className="mainSection">
            <TabLayoutView tabList={aTabList} activeTabId={this.props.activeTabId} onChange={this.changeTab} addBorderToBody={false}>
                {this.getBodyView()}

            </TabLayoutView>
          </div>

          <div ref={this.noUseDiv}></div>

        </div>
    );

  };
}

export const view = ThumbnailGoldenRecordBucketView;
export const events = oEvents;
