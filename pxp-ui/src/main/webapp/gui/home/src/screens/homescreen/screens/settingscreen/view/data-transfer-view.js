import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import { view as LazyContextMenuView } from '../../../../../viewlibraries/lazycontextmenuview/lazy-context-menu-view';
import AttributeTypeDictionary from '../../../../../commonmodule/tack/attribute-type-dictionary-new';
import {relAndContextCouplingTypes} from '../../../../../commonmodule/tack/version-variant-coupling-types';
import ViewUtils from './utils/view-utils';
import ConfigDataEntitiesDictionary from '../../../../../commonmodule/tack/config-data-entities-dictionary';

const oEvents = {
  DATA_TRANSFER_VIEW_SEARCH_LOAD_MORE_PROPERTIES: "data_transfer_view_search_load_more_properties",
  DATA_TRANSFER_VIEW_ADD_PROPERTIES: "data_transfer_view_add_properties",
  DATA_TRANSFER_PROPERTY_COUPLING_CHANGE: "data_transfer_property_coupling_change",
  DATA_TRANSFER_REMOVE_PROPERTY: "data_transfer_remove_property"
};

const oPropTypes = {
  relationshipId: ReactPropTypes.string,
  relationshipType: ReactPropTypes.string,
  side1: ReactPropTypes.object,
  side1Label: ReactPropTypes.string,
  side2: ReactPropTypes.object,
  side2Label: ReactPropTypes.string,
  extensionLabel: ReactPropTypes.string,
  parentContext: ReactPropTypes.string,
  referencedAttributes: ReactPropTypes.object,
  ReferencedTags: ReactPropTypes.object,
  hideOneSide: ReactPropTypes.bool,
  sShownSide: ReactPropTypes.oneOf(['side1', 'side2']),
  requestResponseInfo: ReactPropTypes.object,
};

// @CS.SafeComponent
class DataTransferView extends React.Component {
  static propTypes = oPropTypes;

  getSelectOptions = (aList) => {
    var iIndex = 0;
    return CS.map(aList, function (oItem) {
      return (<option value={oItem.id} key={iIndex++}>{CS.getLabelOrCode(oItem)}</option>);
    });
  };

  handleCouplingTypeChanged = (sSide, sPropertyId, sContext, oEvent) => {
    var sNewValue = oEvent.target.value;
    EventBus.dispatch(oEvents.DATA_TRANSFER_PROPERTY_COUPLING_CHANGE, sSide, sPropertyId, sNewValue, sContext,
        this.props.relationshipId, this.props.parentContext);
  };

  handleRemovePropertyFromRelationship = (sSide, sPropertyId, sContext) => {
    EventBus.dispatch(oEvents.DATA_TRANSFER_REMOVE_PROPERTY, sSide, sPropertyId, sContext,
        this.props.relationshipId, this.props.parentContext);
  };

  handleSearchLoadMore = (oData) => {
    EventBus.dispatch(oEvents.DATA_TRANSFER_VIEW_SEARCH_LOAD_MORE_PROPERTIES, oData);
  };

  handleOnLoadMore = (sEntity) => {
    var oData = {
      loadMore: true,
      entities: [sEntity]
    };
    this.handleSearchLoadMore(oData);
  };

  handleOnSearch = (sEntity, sSearchText) => {
    var oData = {
      searchText: sSearchText,
      entities: [sEntity]
    };
    this.handleSearchLoadMore(oData);
  };

  handleOnApply = (sEntity, aSelectedIds, oReferencedData, sContext) => {
    EventBus.dispatch(oEvents.DATA_TRANSFER_VIEW_ADD_PROPERTIES, sEntity, aSelectedIds, oReferencedData, sContext);
  };

  getCouplingDom = (sSide, sContext, oVisualElement) => {
    /** Before save properties are getting from ViewLibraryUtils and after save these values are getting from referenced tags/attributes**/
    var oReferencedTags = this.props.referencedTags;
    var oReferencedAttributes = this.props.referencedAttributes;
    var oAttributeMap = ViewUtils.getReferencedAttributes();
    var oTagMap = ViewUtils.getReferencedTags();
    var oMasterItem = {};
    if (sContext == 'attributes' || sContext == 'extensionAttributes' || sContext == 'prices') {
      oMasterItem = oReferencedAttributes[oVisualElement.id] || oAttributeMap[oVisualElement.id] || {};
    } else {
      oMasterItem = oReferencedTags[oVisualElement.id] || oTagMap[oVisualElement.id] || {};
    }

    let aVersionVariantCouplingTypes = relAndContextCouplingTypes();
    let bShowCouplingDetails = true;
    var aCouplingOptions = this.getSelectOptions(aVersionVariantCouplingTypes);
    var sPropertyLabel = oMasterItem && CS.getLabelOrCode(oMasterItem);
    let sPropertyLabelForToolTip = oMasterItem.code ? sPropertyLabel + " : " + oMasterItem.code : sPropertyLabel;
    return (
      <div className="relationshipPropertyCouplingWrapper" key={sSide + oVisualElement.id}>
        <div className={"propertyTypeIcon " + sContext}></div>
        <div className="relationshipPropertyLabel" title={sPropertyLabelForToolTip}>{sPropertyLabel}</div>
        <div className="relationshipPropertyContainer">
          {bShowCouplingDetails ?
           (<select className="relationshipCouplingOption"
                    value={oVisualElement.couplingType}
                    onChange={this.handleCouplingTypeChanged.bind(this, sSide, oVisualElement.id, sContext)}>
             {aCouplingOptions}
           </select>) : null}
        </div>
        <div className="removeProperty" title={getTranslation().REMOVE}
             onClick={this.handleRemovePropertyFromRelationship.bind(this, sSide, oVisualElement.id, sContext)}></div>
      </div>
    );
  };

  getSideCouplingView = (oSide, sSide, bForExtension) => {
    var _this = this;
    var fGetAttributeCouplingDOM = _this.getCouplingDom.bind(this, sSide, bForExtension ? 'extensionAttributes' : 'attributes');
    var fGetTagCouplingDOM = _this.getCouplingDom.bind(this, sSide, bForExtension ? 'extensionTags' : 'tags');
    var aAttributes = bForExtension ? oSide.extensionAttributes : oSide.attributes;
    var aTags = bForExtension ? oSide.extensionTags : oSide.tags;

    if(!(CS.isEmpty(aAttributes) && CS.isEmpty(aTags))) {
      return (
          <div className="propertyCouplingContainer">
            <div className="propertyCouplingHeader">
              <div className="propertyName">{getTranslation().PROPERTY}</div>
              <div className="propertyCoupling">{getTranslation().COUPLING}</div>
            </div>
            {CS.map(aAttributes, fGetAttributeCouplingDOM)}
            {CS.map(aTags, fGetTagCouplingDOM)}
          </div>
      )
    }
    return null;
  };

  getRequestResponseInfoForAttributes = () => {
    let oRequestResponseInfo = {
      requestType: "configData",
      entityName: ConfigDataEntitiesDictionary.ATTRIBUTES,
      entitiesToExclude: ['assetdownloadcountattribute']
    };
    oRequestResponseInfo.types = [];
    oRequestResponseInfo.typesToExclude = [AttributeTypeDictionary.CONCATENATED, AttributeTypeDictionary.CALCULATED];
    return oRequestResponseInfo;
  };

  getRequestResponseInfoForTags = () => {
    return {
      requestType: "configData",
      entityName: ConfigDataEntitiesDictionary.TAGS
    }
  };

  getAddPropertyView = (oData) => {
    var oAnchorOrigin = {horizontal: 'right', vertical: 'top'};
    var oTargetOrigin = {horizontal: 'right', vertical: 'bottom'};
    var sSearchText = ViewUtils.getEntitySearchText();
    return (
        <div className="addPropertyViewContainer">
          <LazyContextMenuView
              context={oData.context}
              className=" addAttribute "
              selectedItems={oData.selectedAttributes}
              excludedItems={oData.excludedAttributes}
              isMultiselect={oData.multiSelect}
              onApplyHandler={this.handleOnApply.bind(this, ConfigDataEntitiesDictionary.ATTRIBUTES)}
              showSelectedItems={true}
              showColor={true}
              anchorOrigin={oAnchorOrigin}
              targetOrigin={oTargetOrigin}
              useAnchorElementWidth={true}
              searchHandler={this.handleOnSearch.bind(this, ConfigDataEntitiesDictionary.ATTRIBUTES)}
              searchText={sSearchText}
              loadMoreHandler={this.handleOnLoadMore.bind(this, ConfigDataEntitiesDictionary.ATTRIBUTES)}
              disabled={oData.disabled}
              requestResponseInfo={CS.isEmpty(this.props.requestResponseInfo) ? this.getRequestResponseInfoForAttributes() : this.props.requestResponseInfo}
          />
          <LazyContextMenuView
              context={oData.context}
              className=" addTag "
              selectedItems={oData.selectedTags}
              excludedItems={oData.excludedTags}
              isMultiselect={oData.multiSelect}
              onApplyHandler={this.handleOnApply.bind(this, ConfigDataEntitiesDictionary.TAGS)}
              showSelectedItems={true}
              showColor={true}
              anchorOrigin={oAnchorOrigin}
              targetOrigin={oTargetOrigin}
              useAnchorElementWidth={true}
              searchHandler={this.handleOnSearch.bind(this, ConfigDataEntitiesDictionary.TAGS)}
              searchText={sSearchText}
              loadMoreHandler={this.handleOnLoadMore.bind(this, ConfigDataEntitiesDictionary.TAGS)}
              disabled={oData.disabled}
              requestResponseInfo={this.getRequestResponseInfoForTags()}
          />
        </div>
    );
  };

  getDataTransferViewForSide = (
    oSide,
    sSide,
    oOtherSide,
    sOtherSideLabel,
    bIsExtensionSelected,
  ) => {
    let _props = this.props;
    var sExtendedContext = ViewUtils.getSplitter() + sSide + ViewUtils.getSplitter() + this.props.relationshipId
        + ViewUtils.getSplitter() + this.props.parentContext;

    var oViewForSide = null;

      var aSelectedTagsForSide = oSide.tags || [];
      var aSelectedAttributesForSide = oSide.attributes || [];

      var aSelectedTagsForOtherSide = oOtherSide.tags || [];
      var aSelectedAttributesForOtherSide = oOtherSide.attributes || [];

      var oMSSData = {
        selectedAttributes: CS.map(aSelectedAttributesForSide, "id"),
        excludedAttributes: CS.map(aSelectedAttributesForOtherSide, "id"),
        selectedTags: CS.map(aSelectedTagsForSide, "id"),
        excludedTags: CS.map(aSelectedTagsForOtherSide, "id"),
        multiSelect: true,
        showIcon: true,
        context: "dataTransfer" + sExtendedContext,
        disabled: false
      };

      oViewForSide = (
          <div className="otherSideContainer">
            <div className="otherSideHeader">{getTranslation().TO + " " + sOtherSideLabel}</div>
            {this.getAddPropertyView(oMSSData)}
            <div className="sideDropDownContainer">
              {this.getSideCouplingView(oSide, sSide, false)}
            </div>
          </div>
      );

    var oViewForExtension = null;

    if (bIsExtensionSelected) {

      var aSelectedExtensionTagsForSide = oSide.extensionTags || [];
      var aSelectedExtensionAttributesForSide = oSide.extensionAttributes || [];

      var aSelectedExtensionTagsForOtherSide = oOtherSide.extensionTags || [];
      var aSelectedExtensionAttributesForOtherSide = oOtherSide.extensionAttributes || [];

      var oMSSDataForExtension = {
        selectedAttributes: CS.map(aSelectedExtensionAttributesForSide, "id"),
        excludedAttributes: CS.map(aSelectedExtensionAttributesForOtherSide, "id"),
        selectedTags: CS.map(aSelectedExtensionTagsForSide, "id"),
        excludedTags: CS.map(aSelectedExtensionTagsForOtherSide, "id"),
        selectedItems: [],
        multiSelect: true,
        showIcon: true,
        context: "dataTransferForExtension" + sExtendedContext,
        disabled: false
      };

      oViewForExtension = (
          <div className="otherSideContainer">
            <div className="otherSideHeader">{getTranslation().TO + " " + this.props.extensionLabel}</div>
            {this.getAddPropertyView(oMSSDataForExtension)}
            <div className="sideDropDownContainer">
              {this.getSideCouplingView(oSide, sSide, true)}
            </div>
          </div>
      );
    }

    let sDataTransferSideWrapperClassName = this.props.hideOneSide ? "dataTransferSideWrapper" : "dataTransferSideWrapper leftSide";
    let sThisSideLabel = sSide == 'side1' ? _props.side1Label : _props.side2Label;

    return (
        <div className={sDataTransferSideWrapperClassName}>
          <div className="sideHeader">{getTranslation().FROM + " " + sThisSideLabel}</div>
          <div className="dataTransferContainer">
            {oViewForSide}
            {oViewForExtension}
          </div>
        </div>
    );
  };

  render() {

    // [Temporary] Hide data transfer view for extension in both sides
    let oSide1View = this.props.hideOneSide && this.props.sShownSide == "side2" ? null : this.getDataTransferViewForSide(this.props.side1, 'side1', this.props.side2, this.props.side2Label,false);
    let oSide2View = this.props.hideOneSide && this.props.sShownSide == "side1" ? null : this.getDataTransferViewForSide(this.props.side2, 'side2', this.props.side1, this.props.side1Label,false);

    return (
        <div className="dataTransferWrapper">
          {oSide1View}
          {oSide2View}
        </div>
    );
  }
}

export const view = DataTransferView;
export const events = oEvents;
