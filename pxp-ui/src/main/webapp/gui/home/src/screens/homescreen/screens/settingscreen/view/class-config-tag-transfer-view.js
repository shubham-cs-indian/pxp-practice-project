import CS from "../../../../../libraries/cs";
import React from "react";
import ReactPropTypes from "prop-types";
import EventBus from "../../../../../libraries/eventdispatcher/EventDispatcher";
import ViewUtils from "./utils/view-utils";
import SectionLayout from "../tack/class-config-layout-data";
import {view as CommonConfigSectionView} from "../../../../../viewlibraries/commonconfigsectionview/common-config-section-view.js";
import ConfigDataEntitiesDictionary from "../../../../../commonmodule/tack/config-data-entities-dictionary";
import {view as LazyMSSView} from "../../../../../viewlibraries/lazy-mss-view/lazy-mss-view";
import SettingUtils from "./../store/helper/setting-utils";

const oEvents = {
  HANDLE_REMOVE_KLASS_CLICKED: "handle_remove_klass_clicked",
};

const oPropTypes = {
  activeClass: ReactPropTypes.object,
  propertyMSSModel: ReactPropTypes.object,
  referencedData: ReactPropTypes.object,
  property: ReactPropTypes.string,
  section: ReactPropTypes.string,
  context: ReactPropTypes.string,
};

// @CS.SafeComponent
class ClassConfigTagTransferView extends React.Component {
  static propTypes = oPropTypes;

  handleRemoveClassClicked = (sClassId) => {
    EventBus.dispatch(oEvents.HANDLE_REMOVE_KLASS_CLICKED, this.props.property, sClassId, this.props.context)
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

  handleOnApply = (sContext, aSelectedItems, oReferencedData) => {
    EventBus.dispatch("multi_select_popover_closed", aSelectedItems, sContext, oReferencedData);
  };

  handleOnSearch = (sEntity, sSearchText) => {
    var oData = {
      searchText: sSearchText,
      entities: [sEntity]
    };
    this.handleSearchLoadMore(oData);
  };

  getLazyMSSViewModel = (aSelectedItems, oReferencedData, sContextKey, oReqResObj, bIsMultiselect, bIsDisabled, bCannotRemove) => {
    let bIsMultiSelectTemp = bIsMultiselect == undefined || bIsMultiselect == null ? true : bIsMultiselect;
    return {
      cannotRemove: bCannotRemove,
      disabled: bIsDisabled,
      label: "",
      selectedItems: aSelectedItems,
      context: sContextKey,
      disableCross: false,
      hideTooltip: true,
      isMultiSelect: bIsMultiSelectTemp,
      referencedData: oReferencedData,
      requestResponseInfo: oReqResObj
    }
  }

  getTagTransferView = (sClassId, oReferencedData) => {
    var sContext = "dtpClassConfigMarketTags" + SettingUtils.getSplitter() + sClassId;
    let oReferencedKlassData = oReferencedData.referencedKlasses[sClassId];
    let oRequestResponseModel = {
      requestType: "customType",
      requestURL: "config/getTagsByMarket",
      responsePath: ["success", "list"],
      customRequestModel: {marketKlassId: sClassId}
    };

    let oViewForSide = (
        <LazyMSSView
            isMultiSelect={true}
            disabled={false}
            selectedItems={oReferencedKlassData.selectedTags}
            context={sContext}
            cannotRemove={false}
            onApply={this.handleOnApply.bind(this, sContext)}
            searchHandler={this.handleOnSearch.bind(this, ConfigDataEntitiesDictionary.TAGS)}
            searchText={""}
            loadMoreHandler={this.handleOnLoadMore.bind(this, ConfigDataEntitiesDictionary.TAGS)}
            referencedData={oReferencedData.referencedTags}
            requestResponseInfo={oRequestResponseModel}
        />);

    return oViewForSide;
  };

  getDeleteKlassView = (bIsDeleteKlassEnabled, sId) => {
    let deleteKlassView;
    if (bIsDeleteKlassEnabled) {
      deleteKlassView = (<div className='classConfigClassesDelete'
                              onClick={this.handleRemoveClassClicked.bind(this, sId)}/>);
    }
    return deleteKlassView;
  };

  getView = (sId) => {
    let oSectionLayout = new SectionLayout();
    var sSectionId = this.props.section;
    let sSplitter = ViewUtils.getSplitter();
    let oReferencedData = this.props.referencedData;
    let sCommonConfigContext = `classConfig${sSectionId.charAt(0).toUpperCase()}${sSplitter}${sId}`;
    let oTagTransferView = this.getTagTransferView(sId, oReferencedData);

    let oModel = {
      marketLabel: CS.getLabelOrCode(oReferencedData.referencedKlasses[sId]),
      tagTransferProperties: oTagTransferView
    };
    let oDOM = (<div className={"classConfigSelectedClassesContainer"} key={sId}>
      { this.getDeleteKlassView(this.props.isDeleteKlassEnabled, sId) }
      <CommonConfigSectionView context={sCommonConfigContext}
                               sectionLayout={oSectionLayout["classBasicInformation"]}
                               data={oModel}
                               disabledFields={["marketLabel"]}/>
    </div>);

    return oDOM;
  };

  getMarketsView = () => {
    var oActiveClass = this.props.activeClass;
    let _this = this;
    let oProperty = this.props.property;
    let aDOM = [];
    if (CS.isArray(oActiveClass[oProperty])) {
      CS.forEach(oActiveClass[oProperty], function (sId) {
        aDOM.push(_this.getView(sId));
      });
    } else if (oActiveClass[oProperty]) {
      aDOM.push(this.getView(oActiveClass[oProperty]));
    }
    return aDOM;
  };

  render () {
    var oActiveClass = this.props.activeClass;
    let oSectionLayout = new SectionLayout();

    let oMarketView = (<div className="commonConfigSection">{this.getMarketsView()}</div>);

    return (
        <div className='addClass' key="addClass">
          <CommonConfigSectionView context="class" sectionLayout={oSectionLayout["classBasicInformation"]}
                                   data={this.props.propertyMSSModel} disabledFields={[]}/>
          {oActiveClass[this.props.property].length > 0 && oMarketView}
        </div>);
  }
}

export const view = ClassConfigTagTransferView;
export const events = oEvents;
