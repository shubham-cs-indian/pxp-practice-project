import React from "react";
import ReactPropTypes from "prop-types";
import CS from "../../libraries/cs";
import TooltipView from "../tooltipview/tooltip-view";
import {getTranslations as getTranslation} from "../../commonmodule/store/helper/translation-manager";
import {view as NothingFoundView} from "../nothingfoundview/nothing-found-view";
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import PhysicalCatalogDictionary from "../../commonmodule/tack/physical-catalog-dictionary";
import MockDataPhysicalCatalogAndPortals from "../../commonmodule/tack/mock-data-physical-catalog-and-portal-types";
import EndpointTypeDictionary from "../../commonmodule/tack/endpoint-type-dictionary";

const oPropTypes = {
  contextMenuData:ReactPropTypes.object
};

const oEvents = {
  HANDLE_ITEM_CLICKED: "handle_item_clicked",
  HANDLE_TRANSFER_CHECKBOX_TOGGLED: "handle_transfer_checkbox_toggled"
};

// @CS.SafeComponent
class TransferDialogView extends React.Component {
  constructor (props) {
    super(props);

    this.state = {
      searchText: "",
      organizationList: this.props.organizationList,
      authorizationMapping: this.props.authorizationMapping,
      selectedOrganizationId: ReactPropTypes.string,
      selectedPhysicalCatlogId: ReactPropTypes.string,
      selectedAuthorizationMappingId: ReactPropTypes.string,
      disableAuthMapping: this.props.contextMenuData.disableAuthMapping
    }
  };

  clearSearchInput = () => {
    this.setState({
      searchText: "",
      organizationList: this.props.organizationList,
      authorizationMapping: this.props.authorizationMapping,
    })
  };

  handleSearchOperation = (e) => {
    // customAlertify.success(e.target.value);
    /* let aUILanguageSearchList = [];
     let aDataLanguageSearchList = [];
     let sValue = e.target.value;
     let sSearchText = sValue.toLocaleLowerCase();
     if (!CS.isEmpty(sSearchText)) {
       CS.forEach(this.props.uiLanguages, function (item) {
         let sUILanguageLabel = CS.getLabelOrCode(item);
         if (sUILanguageLabel.toLocaleLowerCase().indexOf(sSearchText) != -1) {
           aUILanguageSearchList.push(item);
         }
       });

       CS.forEach(this.props.dataLanguages, function (item) {
         let sDataLanguageLabel = CS.getLabelOrCode(item);
         if (sDataLanguageLabel.toLocaleLowerCase().indexOf(sSearchText) != -1) {
           aDataLanguageSearchList.push(item);
         }
       });

       this.setState({
         searchText: sValue,
         uiLanguages: aUILanguageSearchList,
         dataLanguages: aDataLanguageSearchList
       })
       UiAndDataLanguageInfoView.isChangeFromUI = true;
     } else {
       this.setState({
         searchText: "",
         uiLanguages: this.props.uiLanguages,
         dataLanguages: this.props.dataLanguages

       })
     }*/
  };

  getSearchBarDOM = () => {
    let oCrossIconDOM = null;
    if (!CS.isEmpty(this.state.searchText)) {
      oCrossIconDOM = (
          <TooltipView label={getTranslation().CLEAR} key="clear">
            <div className="crossIcon" onClick={this.clearSearchInput}></div>
          </TooltipView>
      );
    }
    return (
        <div className="searchInputFieldBar">
          <div className="searchIcon"></div>
          <input className="searchInput" value={this.state.searchText} placeholder={getTranslation().SEARCH}
                 onChange={this.handleSearchOperation}></input>
          {oCrossIconDOM}
        </div>
    );
  };

  handleTransferCheckboxToggled = (sContext, oEvent) => {
    this.setState({
      selectedPhysicalCatlogId: "",
      selectedOrganizationId: ""
    });
    EventBus.dispatch(oEvents.HANDLE_TRANSFER_CHECKBOX_TOGGLED, sContext);
  };

  handleItemClicked = (sContext, sSelectedId) => {
    switch (sContext) {
      case "organization":
        this.setState({
          selectedOrganizationId: sSelectedId,
          disableAuthMapping: this.props.contextMenuData.disableAuthMapping
        });
        EventBus.dispatch(oEvents.HANDLE_ITEM_CLICKED,sContext, sSelectedId);
        break;
      case "authorizationMapping":
        this.setState({
          selectedAuthorizationMappingId: sSelectedId
        });
        EventBus.dispatch(oEvents.HANDLE_ITEM_CLICKED,sContext, sSelectedId);
        break;
      case "physicalCatlog":
        this.setState({
          selectedPhysicalCatlogId: sSelectedId,
        });
        EventBus.dispatch(oEvents.HANDLE_ITEM_CLICKED, sContext, sSelectedId);
        break;
      case "dataIntegration":
        this.setState({
          selectedPhysicalCatlogId: sSelectedId,
        });
        EventBus.dispatch(oEvents.HANDLE_ITEM_CLICKED, sContext, sSelectedId);
        break;
    }
  };


  getOrganizationItems = () => {
    let __this = this;
    let sCurrentOrganizationId = this.props.contextMenuData.currentOrganizationId;
    let aRetailer = [{
      id: "retailer",
      label: getTranslation().RETAILER
    }];
    let aOrganizationLists = (sCurrentOrganizationId === "-1") ? this.props.contextMenuData.organizationList : aRetailer;
    let sSelectedOrganizationId = this.state.selectedOrganizationId;
    let aOrganizationItemsView = [];
    CS.forEach(aOrganizationLists, function (oOrganization) {
      if (oOrganization.id !== "-1") {
        let sOrganizationClass = "entityItemWrapper";
        if (sSelectedOrganizationId === oOrganization.id) {
          sOrganizationClass += " selected";
        }
        let sTitle = oOrganization.code ? oOrganization.label + ' (' + oOrganization.code + ')' : oOrganization.label;
        let sLabel = CS.getLabelOrCode(oOrganization);
        aOrganizationItemsView.push(
            <div className={sOrganizationClass} key={oOrganization.id}
                 onClick={__this.handleItemClicked.bind(__this, "organization", oOrganization.id)}>
              <TooltipView label={sTitle}>
                <div className="entityItem">
                  <div className="entityData">{sLabel}</div>
                </div>
              </TooltipView>
            </div>
        );
      }
    });
    if (CS.isEmpty(aOrganizationItemsView)) {
      aOrganizationItemsView.push(<NothingFoundView message={getTranslation().NOTHING_FOUND} key={"NOTHING_FOUND"}/>)
    }
    return (aOrganizationItemsView);
  };

  getAuthorizationMappingItems = () => {
    let __this = this;
    let aAuthorizationLists = this.props.contextMenuData.authorizationMappingList;
    let sSelectedAuthorizationMappingId = this.state.selectedAuthorizationMappingId;
    let sSelectedAuthMapId = this.props.contextMenuData.selectedAuthorizationMappingId;
    let aAuthorizationMappingItemsView = [];
    CS.forEach(aAuthorizationLists, function (oAuthorization) {

      let sAuthorizationMappingClass = "entityItemWrapper";
      if (sSelectedAuthorizationMappingId === oAuthorization.id) {
        if(!CS.isEmpty(sSelectedAuthMapId)){
          sAuthorizationMappingClass += " selected";
        }
      }
      let sTitle = oAuthorization.code ? oAuthorization.label + ' (' + oAuthorization.code + ')' : oAuthorization.label;
      let sLabel= CS.getLabelOrCode(oAuthorization);
      aAuthorizationMappingItemsView.push(
          <div className={sAuthorizationMappingClass} key={oAuthorization.id}
               onClick={__this.handleItemClicked.bind(__this, "authorizationMapping", oAuthorization.id)}>
            <TooltipView label={sTitle}>
              <div className="entityItem">
                <div className="entityData">{sLabel}</div>
              </div>
            </TooltipView>
          </div>
      );
    });
    if (CS.isEmpty(aAuthorizationMappingItemsView)) {
      aAuthorizationMappingItemsView.push(<NothingFoundView message={getTranslation().NOTHING_FOUND} key={"NOTHING_FOUND"}/>)
    }
    return (aAuthorizationMappingItemsView);
  };

  getEndpointItems = (aEndpointList) => {
    let __this = this;
    let sSelectedPhysicalCatlogId = this.state.selectedPhysicalCatlogId;
    let aEndpointItemsView = [];
    CS.forEach(aEndpointList, function (oEndpoint) {
      let sEndpointClass = "entityItemWrapper";
      if (sSelectedPhysicalCatlogId === oEndpoint.id) {
        sEndpointClass += " selected";
      }
      let sTitle = oEndpoint.code ? oEndpoint.label + ' (' + oEndpoint.code + ')' : oEndpoint.label;
      let sLabel = CS.getLabelOrCode(oEndpoint);
      aEndpointItemsView.push(
          <div className={sEndpointClass} key={oEndpoint.id}
               onClick={__this.handleItemClicked.bind(__this, "dataIntegration", oEndpoint.id)}>
            <TooltipView label={sTitle}>
              <div className="entityItem">
                <div className="entityData">{sLabel}</div>
              </div>
            </TooltipView>
          </div>
      );
    });
    return (aEndpointItemsView);
  };

  getInboundEndpointItems = () => {
    let aInboundEndpointList = this.props.contextMenuData.inboundEndpointList;
    return this.getEndpointItems(aInboundEndpointList);
  };

  getOutboundEndpointItems = () => {
    let aOutboundEndpointList = this.props.contextMenuData.outboundEndpointList;
    return this.getEndpointItems(aOutboundEndpointList);
  };

  getOrganizationSelectionDOM = () => {
    let sUILanguageListClassName = "organizationList";
    let sLabel = getTranslation().PARTNER_ORGANIZATION;
    return (
        <div className={sUILanguageListClassName}>
          <div className="entityHeader">
            {sLabel}
          </div>
            <div className="entityItemsContainer">
              {this.getOrganizationItems()}
            </div>
        </div>
    );
  };

  getAuthorizationMappingSelectionDOM = () => {
    let sUILanguageListClassName = "authorizationMappingList";
    //sUILanguageListClassName += this.props.contextMenuData.disableAuthMapping === false ? " enable" : " disable";
    let sLabel = getTranslation().AUTHORIZATION;
    return (
        <div className={sUILanguageListClassName}>
          <div className="entityHeader">
            {sLabel}
          </div>
            <div className="entityItemsContainer">
              {this.getAuthorizationMappingItems()}
            </div>
        </div>
    );
  };

  getItemsForOnboarding = (sPhysicalCatalog) => {
    let _this = this;
    let sCurrentOrganizationId = _this.props.contextMenuData.currentOrganizationId;
    let oContextMenuData = _this.props.contextMenuData;
    let sEndpointType = oContextMenuData.endpointType;
    let aAllowedPhysicalCatalogIds = oContextMenuData.allowedPhysicalCatalogIds;
    let aPhysicalCatalogs = MockDataPhysicalCatalogAndPortals.physicalCatalogs();
    let aPhysicalCatalogTypes = [];
    let aOutboundEndpointList = [];
    let aPhysicalCatalogTypesWithPermission = CS.filter(aPhysicalCatalogs, function (oPhysicalCatalogType) {
      return CS.includes(aAllowedPhysicalCatalogIds, oPhysicalCatalogType.id);
    });

    if (sPhysicalCatalog === PhysicalCatalogDictionary.ONBOARDING) {
      aPhysicalCatalogTypes = CS.filter(aPhysicalCatalogTypesWithPermission, function (oPhysicalCatalogType) {
        return oPhysicalCatalogType.id === PhysicalCatalogDictionary.PIM;
      });
    } else if (sPhysicalCatalog === PhysicalCatalogDictionary.PIM) {
      aOutboundEndpointList = _this.getOutboundEndpointItems();
      aPhysicalCatalogTypes = CS.filter(aPhysicalCatalogTypesWithPermission, function (oPhysicalCatalogType) {
        return oPhysicalCatalogType.id === PhysicalCatalogDictionary.OFFBOARDING;
      });
    } else if (sPhysicalCatalog === PhysicalCatalogDictionary.DATAINTEGRATION && sEndpointType === EndpointTypeDictionary.INBOUND_ENDPOINT) {
      aPhysicalCatalogTypes = CS.filter(aPhysicalCatalogTypesWithPermission, function (oPhysicalCatalogType) {
        return oPhysicalCatalogType.id === PhysicalCatalogDictionary.PIM || oPhysicalCatalogType.id === PhysicalCatalogDictionary.ONBOARDING;
      });
    } else if (sPhysicalCatalog === PhysicalCatalogDictionary.OFFBOARDING) {
      aOutboundEndpointList = _this.getOutboundEndpointItems();
    }

    let sSelectedPhysicalCatlogId = _this.state.selectedPhysicalCatlogId;
    let aPhysicalCatalogItemsView = [];
    CS.forEach(aPhysicalCatalogTypes, function (oPhysicalCatalog) {

      let sPhysicalCatalogClass = "entityItemWrapper";
      if (sSelectedPhysicalCatlogId === oPhysicalCatalog.id) {
        sPhysicalCatalogClass += " selected";
      }
      let sTitle = oPhysicalCatalog.code ? oPhysicalCatalog.label + ' (' + oPhysicalCatalog.code + ')' : oPhysicalCatalog.label;
      let sLabel = CS.getLabelOrCode(oPhysicalCatalog);
      aPhysicalCatalogItemsView.push(
          <div className={sPhysicalCatalogClass} key={oPhysicalCatalog.id}
               onClick={_this.handleItemClicked.bind(_this, "physicalCatlog", oPhysicalCatalog.id)}>
            <TooltipView label={sTitle}>
              <div className="entityItem">
                <div className="entityData">{sLabel}</div>
              </div>
            </TooltipView>
          </div>
      );
    });

    aPhysicalCatalogItemsView = aPhysicalCatalogItemsView.concat(aOutboundEndpointList);
    if (CS.isEmpty(aPhysicalCatalogItemsView)) {
      aPhysicalCatalogItemsView.push(<NothingFoundView message={getTranslation().NOTHING_FOUND} key={"NOTHING_FOUND"}/>)
    }
    return (aPhysicalCatalogItemsView);
  };

  getPhysicalCatalogFotTransferBetweenOrganisationDom = () => {
    let _this = this;
    let sCurrentOrganizationId = this.props.contextMenuData.currentOrganizationId;
    let oPhysicalCatalogs = MockDataPhysicalCatalogAndPortals.physicalCatalogs();
    let aPhysicalCatalogTypes = CS.filter(oPhysicalCatalogs, function (oPhysicalCatalogType) {
      if (sCurrentOrganizationId === "-1") {
        return oPhysicalCatalogType.id === PhysicalCatalogDictionary.PIM || oPhysicalCatalogType.id === PhysicalCatalogDictionary.ONBOARDING;
      }else{
        return oPhysicalCatalogType.id === PhysicalCatalogDictionary.ONBOARDING;
      }

    });
    let sSelectedPhysicalCatlogId = _this.state.selectedPhysicalCatlogId;
    let aPhysicalCatalogItemsView = [];

    CS.forEach(aPhysicalCatalogTypes, function (oPhysicalCatalog) {

      let sPhysicalCatlogClass = "entityItemWrapper";
      if (sSelectedPhysicalCatlogId === oPhysicalCatalog.id) {
        sPhysicalCatlogClass += " selected";
      }
      let sTitle = oPhysicalCatalog.code ? oPhysicalCatalog.label + ' (' + oPhysicalCatalog.code + ')' : oPhysicalCatalog.label;
      let sLabel = CS.getLabelOrCode(oPhysicalCatalog);
      aPhysicalCatalogItemsView.push(
          <div className={sPhysicalCatlogClass} key={oPhysicalCatalog.id}
               onClick={_this.handleItemClicked.bind(_this, "physicalCatlog", oPhysicalCatalog.id)}>
            <TooltipView label={sTitle}>
              <div className="entityItem">
                <div className="entityData">{sLabel}</div>
              </div>
            </TooltipView>
          </div>
      );
    });

    let aInboundEndpointList = this.getInboundEndpointItems();
    return aPhysicalCatalogItemsView.concat(aInboundEndpointList);
  };

  /**Get Physical Catlog view for Transfer Within PIM**/
  getSelectionDOMForOnboarding = (sPhysicalCatalog) => {
    let sUILanguageListClassName = "organizationList";
    let sLabel = getTranslation().DESTINATION;
    return (
        <div className={sUILanguageListClassName}>
          <div className="entityHeader">
            {sLabel}
          </div>
            <div className="entityItemsContainer">
              {this.getItemsForOnboarding(sPhysicalCatalog)}
            </div>
        </div>
    );
  };

  /**Get Physical Catlog view for Transfer Between Stages**/
  getSelectionDOMFoPhysicalCatlog = () => {
    let sUILanguageListClassName = "destinationList";
    let sLabel = getTranslation().DESTINATION;
    return (
        <div className={sUILanguageListClassName}>
          <div className="entityHeader">
            {sLabel}
          </div>
            <div className="entityItemsContainer">
              {this.getPhysicalCatalogFotTransferBetweenOrganisationDom()}
            </div>
        </div>
    );
  };

  _getLeftSideView = (sPhysicalCatalog, bIsTransferBetweenStagesEnabled) => {
    if (bIsTransferBetweenStagesEnabled) {
      return this.getSelectionDOMFoPhysicalCatlog();
    } else {
      return this.getSelectionDOMForOnboarding(sPhysicalCatalog);
    }
  };

  getTopView = () => {
    let oContextMenuData = this.props.contextMenuData;
    let bIsTransferBetweenStagesEnabled = oContextMenuData.isTransferBetweenStagesEnabled;
    let sEndpointType = oContextMenuData.endpointType;
    let sCheckBoxLabel = getTranslation().TRANSFER_BETWEEN_STAGES;
    let sCheckboxClassChecked = (bIsTransferBetweenStagesEnabled) ? "transferBetweenStagesCheckbox checkedItem" : "transferBetweenStagesCheckbox";
    sCheckboxClassChecked += (sEndpointType === EndpointTypeDictionary.OUTBOUND_ENDPOINT) ? " isDisabled" : "";
    return (
        <React.Fragment>
          <div className={sCheckboxClassChecked}
               onClick={this.handleTransferCheckboxToggled.bind(this)}></div>
          <div>{sCheckBoxLabel}</div>
        </React.Fragment>
    );
  };

  getBottomView = () => {
    let oContextMenuData = this.props.contextMenuData;
    let bIsRevisionableTransfer = oContextMenuData.isRevisionableTransfer;
    let sCheckBoxLabel = "Create revisions for non-revisionable properties";
    let sCheckboxClassChecked = (bIsRevisionableTransfer) ? "transferBetweenStagesCheckbox checkedItem" : "transferBetweenStagesCheckbox";
    return (
        <React.Fragment>
          <div className={sCheckboxClassChecked}
               onClick={this.handleTransferCheckboxToggled.bind(this, "revisionable")}></div>
          <div>{sCheckBoxLabel}</div>
        </React.Fragment>
    );
  };

  render () {
    let oContextMenuData = this.props.contextMenuData;
    let bIsTransferBetweenStagesEnabled = oContextMenuData.isTransferBetweenStagesEnabled;
    let sPhysicalCatalog = oContextMenuData.physicalCatalog;
    let sEndpointType = oContextMenuData.endpointType;
    let oDestinationView = this._getLeftSideView(sPhysicalCatalog, bIsTransferBetweenStagesEnabled);
    let oPartnerOrganizationView = (bIsTransferBetweenStagesEnabled) ? this.getOrganizationSelectionDOM() : null;
    let oAuthorizationView = this.getAuthorizationMappingSelectionDOM() ;
    let oTopView = (sEndpointType === EndpointTypeDictionary.INBOUND_ENDPOINT) ? null : this.getTopView();
    let oBottomView = this.getBottomView();

    return (
        <div className='organizationAuthMappingView'>
          <div className="organizationAuthMappingSelectionView">
            {/*{this.getSearchBarDOM()}*/}
            <div className="organizationAndAuthMappingList">
              {oTopView}
              <div className="mappingListColumnContainer">
                {oDestinationView}
                {oPartnerOrganizationView}
                {oAuthorizationView}
              </div>
            </div>
          </div>
          <div className="checkboxDOM">
          {oBottomView}
          </div>
        </div>
    );
  }
}

TransferDialogView.propTypes = oPropTypes;
export const view = TransferDialogView;
export const events = oEvents;
