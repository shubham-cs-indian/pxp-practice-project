import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import {getTranslations} from '../../../../../commonmodule/store/helper/translation-manager.js';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import {view as LazyContextMenuView} from '../../../../../viewlibraries/lazycontextmenuview/lazy-context-menu-view';
import {relationshipsCouplingTypes as oRelationshipsCouplingTypes} from '../../../../../commonmodule/tack/version-variant-coupling-types';
import ViewUtils from './utils/view-utils';
import ConfigDataEntitiesDictionary from '../../../../../commonmodule/tack/config-data-entities-dictionary';

const Fragment = React.Fragment;

const oEvents = {
  RELATIONSHIP_INHERITANCE_APPLY_CLICKED: "relationship_inheritance_apply_clicked",
  RELATIONSHIP_INHERITANCE_PROPERTY_COUPLING_CHANGE: "relationship_inheritance_property_coupling_change",
  RELATIONSHIP_INHERITANCE_REMOVE_PROPERTY: "relationship_inheritance_remove_property"
};

const oPropTypes = {
  id: ReactPropTypes.string,
  relationshipType: ReactPropTypes.string,
  side: ReactPropTypes.object,
  side1Label: ReactPropTypes.string,
  side2Label: ReactPropTypes.string,
  parentContext: ReactPropTypes.string,
  relationshipInheritanceData: ReactPropTypes.object,
  canAddReference: ReactPropTypes.bool
}

// @CS.SafeComponent
class RelationshipInheritanceView extends React.Component {
  static propTypes = oPropTypes;

  getSelectOptions = (aList) => {
    var iIndex = 0;
    return CS.map(aList, function (oItem) {
      return (<option value={oItem.id} key={iIndex++}>{CS.getLabelOrCode(oItem)}</option>);
    });
  };

  handleCouplingTypeChanged = (sPropertyId, sContext, oEvent) => {
    var sNewValue = oEvent.target.value;
    EventBus.dispatch(oEvents.RELATIONSHIP_INHERITANCE_PROPERTY_COUPLING_CHANGE, 'side1',  sPropertyId, sNewValue, sContext,
        this.props.id, this.props.parentContext);
  };

  handleRemovePropertyFromRelationship = (sPropertyId, sContext) => {
    EventBus.dispatch(oEvents.RELATIONSHIP_INHERITANCE_REMOVE_PROPERTY, 'side1',  sPropertyId, sContext, this.props.id, this.props.parentContext);
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
    EventBus.dispatch(oEvents.RELATIONSHIP_INHERITANCE_APPLY_CLICKED, sEntity, aSelectedIds, oReferencedData, sContext);
  };

  getCouplingDom = (oVisualElement, oInheritanceData) => {
    /** Before save properties are getting from ViewUtils and after save these values are getting from referenced relationshipInheritance**/
    let oReferencedRelationship = oInheritanceData.referencedData;
    let oRelationshipInheritanceMap = oInheritanceData.inheritanceMap;
    let oMasterItem = oReferencedRelationship[oVisualElement.id] || oRelationshipInheritanceMap[oVisualElement.id] || {};
    let aVersionVariantCouplingTypes = oRelationshipsCouplingTypes();
    let aCouplingOptions = this.getSelectOptions(aVersionVariantCouplingTypes);
    let sPropertyLabel = oMasterItem && CS.getLabelOrCode(oMasterItem);
    let sPropertyLabelForToolTip = oMasterItem.code ? sPropertyLabel + " : " + oMasterItem.code : sPropertyLabel;
    return (
        <div className="relationshipPropertyCouplingWrapper" key={oVisualElement.id}>
          <div className={"propertyTypeIcon " + oInheritanceData.context}></div>
          <div className="relationshipPropertyLabel" title={sPropertyLabelForToolTip}>{sPropertyLabel}</div>
          <div className="relationshipPropertyContainer">
            <select className="relationshipCouplingOption"
                    value={oVisualElement.couplingType}
                    onChange={this.handleCouplingTypeChanged.bind(this, oVisualElement.id, oInheritanceData.context)}>
              {aCouplingOptions}
            </select>
          </div>
          <div className="removeProperty" title={getTranslations().REMOVE}
               onClick={this.handleRemovePropertyFromRelationship.bind(this, oVisualElement.id, oInheritanceData.context)}></div>
        </div>
    );
  };

  getSideCouplingView = (aSelectedElements, oInheritanceData) => {
    let _this = this;
    let aSelectedElementCouplingView = [];
    CS.forEach(aSelectedElements, function (oElement) {
      let oCouplingDOM = _this.getCouplingDom(oElement, oInheritanceData);
      aSelectedElementCouplingView.push(oCouplingDOM)
    });
    return CS.isNotEmpty(aSelectedElements) ? aSelectedElementCouplingView : null;
  };

  getSelectedElementsView = (oSide, oRelationshipData) => {
    if (CS.isNotEmpty(oSide.relationships)) {
      return (
          <div className="propertyCouplingContainer">
            <div className="propertyCouplingHeader">
              <div className="propertyName">{getTranslations()[oRelationshipData.context.toUpperCase()]}</div>
              <div className="propertyCoupling">{getTranslations().COUPLING}</div>
            </div>
            {this.getSideCouplingView(oSide.relationships, oRelationshipData)}
          </div>
      );
    }
    ;
  }

  getRequestResponseInfoForRelationship = (sRequestURL) => {
    return {
      requestType: "customType",
      requestURL: sRequestURL,
      responsePath: ["success", "list"]
    }
  };

  getAddPropertyView = (aSelectedElements, oElementData) => {
    let splitter = ViewUtils.getSplitter();
    let sExtendedContext =
        splitter + 'side1' +
        splitter + this.props.id +
        splitter + this.props.parentContext;

    var sSearchText = ViewUtils.getEntitySearchText();
    let sClassName = oElementData.context === ConfigDataEntitiesDictionary.RELATIONSHIPS ? "addRelationship" : null;
    let sClassNameForContainer = "addRelationshipViewContainer " + sClassName;
    return (
        <div className={sClassNameForContainer}>
          <LazyContextMenuView
              context={sExtendedContext}
              className={sClassName}
              selectedItems={aSelectedElements}
              isMultiselect={true}
              onApplyHandler={this.handleOnApply.bind(this, oElementData.context)}
              showSelectedItems={true}
              showColor={true}
              useAnchorElementWidth={true}
              searchHandler={this.handleOnSearch}
              searchText={sSearchText}
              loadMoreHandler={this.handleOnLoadMore}
              disabled={false}
              requestResponseInfo={this.getRequestResponseInfoForRelationship(oElementData.requestURL)}
          />
        </div>
    );
  };

  getRelationshipInheritanceViewForSide = () => {
    let _props = this.props;
    let oSide = _props.side;
    let oRelationshipData = _props.relationshipInheritanceData;

    let aRelationshipForSide = CS.map(oSide.relationships, "id");

    let oAddRelationshipView = this.getAddPropertyView(aRelationshipForSide, oRelationshipData);
    let oSideCouplingView = this.getSelectedElementsView(oSide, oRelationshipData);

    let sOtherSideLabel = _props.side2Label;

    var oViewForSide = (
        <Fragment>
          <div className="otherSideHeader">{getTranslations().TO + " " + sOtherSideLabel}</div>
          {oAddRelationshipView}
          <div className="sideDropDownContainer">
            {oSideCouplingView}
          </div>
        </Fragment>
    );

    let sThisSideLabel = _props.side1Label;

    return (
        <div className="relationshipInheritanceSideWrapper">
          <div className="sideHeader">{getTranslations().FROM + " " + sThisSideLabel}</div>
          <div className="sideContainer">
            {oViewForSide}
          </div>
        </div>
    );
  };

  render() {
    return (
        <div className="relationshipInheritanceWrapper">
          {this.getRelationshipInheritanceViewForSide()}
        </div>
    );
  }
}

export const view = RelationshipInheritanceView;
export const events = oEvents;
