import CS from '../../../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import Loader from 'halogen/PulseLoader';
import EventBus from '../../../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../../../../../commonmodule/store/helper/translation-manager.js';
import { view as DashboardEndpointLastUploadTileView } from './dashboard-endpoint-last-upload-tile-view';
import { view as DashboardEndpointSummaryTileView } from './dashboard-endpoint-summary-tile-view';
import { view as DashboardEndpointAllUploadsTileView } from './dashboard-endpoint-all-uploads-tile-view';
import { view as DashboardEndpointOutboundTileView } from './dashboard-endpoint-outbound-tile-view';
import { view as OnboardingFileUploadButtonView } from '../../../view/onboarding-file-upload-button-view';
import ViewUtils from '../../../view/utils/view-utils';
import { view as ImageFitToContainerView } from '../../../../../../../viewlibraries/imagefittocontainerview/image-fit-to-container-view';
import TooltipView from '../../../../../../../viewlibraries/tooltipview/tooltip-view';
import { view as FileDragAndDropView } from '../../../../../../../viewlibraries/filedraganddropview/file-drag-and-drop-view';
import DashboardConstants from './../tack/dashboard-constants';
import EndpointTileModesDictionary from '../tack/dashboard-endpoint-tile-mode-dictionary';
import assetType from '../../../tack/coverflow-asset-type-list';
const EndpointTypes = DashboardConstants.endpointTypes;
const EndpointTileModes = DashboardConstants.endpointTileModes;
const OnBoardingImportTypes = assetType.OnBoardingImportTypes;

const oEvents = {
  DASHBOARD_ENDPOINT_TILE_MODE_CHANGED: "dashboard_endpoint_tile_mode_changed",
  DASHBOARD_ENDPOINT_TILE_CLICKED: "dashboard_endpoint_tile_clicked",
  DASHBOARD_ENDPOINT_REFRESH_CLICKED: "dashboard_endpoint_refresh_clicked"
};

const oPropTypes = {
  endpointId: ReactPropTypes.string,
  endpointLabel: ReactPropTypes.string,
  endpointType: ReactPropTypes.string,
  tileMode: ReactPropTypes.string,
  tileData: ReactPropTypes.oneOfType([
    ReactPropTypes.array,
    ReactPropTypes.object
  ]),
  tileIcon: ReactPropTypes.string,
  isEndpointLoading: ReactPropTypes.bool,
  physicalCatalogId:ReactPropTypes.array
};

// @CS.SafeComponent
class DashboardEndpointTileView extends React.Component {

  constructor (props) {
    super(props);
  };

  handleTileClicked = (oEvent) => {
    if (!oEvent.nativeEvent.dontRaise) {
      let __props = this.props;
      let oEndpointData = {
        id: __props.endpointId,
        type: __props.endpointType,
        label: __props.endpointLabel,
        tileMode: __props.tileMode,
        physicalCatalogId: __props.physicalCatalogId
      };
      EventBus.dispatch(oEvents.DASHBOARD_ENDPOINT_TILE_CLICKED, oEndpointData);
    }
  };

  handleModeDropdownChanged = (oEvent) => {
    let sMode = oEvent.target.value;
    EventBus.dispatch(oEvents.DASHBOARD_ENDPOINT_TILE_MODE_CHANGED, this.props.endpointId, sMode);
  };

  handleModeDropdownOnClick = (oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
  };

  handleRefreshEndpointClicked = (sEndpointId, sTileMode, oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    EventBus.dispatch(oEvents.DASHBOARD_ENDPOINT_REFRESH_CLICKED, sEndpointId, sTileMode);
  };

  getLoaderView = () => {
    let bIsEndpointLoading = this.props.isEndpointLoading;
    let oLoadingSection = null;

    if (bIsEndpointLoading) {
      oLoadingSection = (
          <div className="loadingAnimationContainer">
            <Loader color="#26A65B"/>
          </div>
      );
    }

    return oLoadingSection;
  };

  getModeDropdown = () => {
    let __props = this.props;
    let sEndpointType = __props.endpointType;
    if (sEndpointType === EndpointTypes.OUTBOUND) {
      return null;
    }
    let oEndpointTileModesDictionary = new EndpointTileModesDictionary();
    let aModeOptions = CS.map(oEndpointTileModesDictionary[sEndpointType], function (oMode) {
      return (<option value={oMode.id} key={oMode.id}>{oMode.label}</option>);
    });

    return <div className="dashboardTileModeSelectorWrapper">
      <select value={__props.tileMode} onChange={this.handleModeDropdownChanged}
              onClick={this.handleModeDropdownOnClick}
              className="dashboardTileModeSelector">
        {aModeOptions}
      </select>
    </div>;
  };

  getInboundTileView = () => {
    let _this = this;
    let __props = _this.props;

    switch (__props.tileMode) {
      case EndpointTileModes.UPLOAD_SUMMARY:
        return (
            <DashboardEndpointSummaryTileView tileData={__props.tileData}/>
        );

      case EndpointTileModes.ALL_UPLOADS:
        return (
            <DashboardEndpointAllUploadsTileView tileData={__props.tileData}/>
        );

      case EndpointTileModes.LAST_UPLOAD:
        return (
            <DashboardEndpointLastUploadTileView tileData={__props.tileData}/>
        );

      default:
        return null;
    }
  };

  getTileInformationView = () => {

    let __props = this.props;
    switch (__props.endpointType) {

      case EndpointTypes.INBOUND:
        return this.getInboundTileView();

      case EndpointTypes.OUTBOUND:
        return (
            <DashboardEndpointOutboundTileView tileData={__props.tileData}/>
        );

      default:
        return null;
    }

  };

  onBulkUploadIconClick = (oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
  };

  getFileUploadButtonView = () => {
    let __props = this.props;
    switch (__props.endpointType) {
      case EndpointTypes.INBOUND:
        let oExtraData = {
          endpointId: __props.endpointId,
          endpointType: __props.endpointType
        };
        return (
            <div className="bulkUploadIconWrapper" onClick={this.onBulkUploadIconClick}>
              <OnboardingFileUploadButtonView extraDataForUpload={oExtraData}/>
            </div>
        );
      case EndpointTypes.OUTBOUND:
      default:
        return null;
    }
  };

  getTileView () {
    let __props = this.props;
    let sEndpointId = __props.endpointId;
    let sHeaderLabel = __props.endpointLabel;
    let fHandleTileClicked = this.handleTileClicked;
    let sTileType = null;
    let OCustomIconView = null;
    let sIcon = this.props.tileIcon;
    let sClassName = "dashboardTileHeaderIcon";
    let sTileClassName = "dashboardTile ";
    if (__props.endpointType === EndpointTypes.INBOUND) {
      sTileType = getTranslation().INBOUND; //todo: translation
      sTileClassName += "inbound ";
    } else {
      sTileType = getTranslation().OUTBOUND;
      sTileClassName += "outbound ";
    }

    if (sIcon) {
      sIcon = ViewUtils.getIconUrl(sIcon);
      sClassName = "customIcon";
      OCustomIconView = <ImageFitToContainerView imageSrc={sIcon}/>;
    }

    return (
        <div className={sTileClassName} onClick={fHandleTileClicked}>
          {this.getLoaderView()}
          <div className="dashboardTileHeader">
            <div className={sClassName}>{OCustomIconView}</div>
            <div className="dashboardTileHeaderLabel" title={sHeaderLabel}>
              {sHeaderLabel}
            </div>
            <TooltipView placement="bottom" label={sTileType}>
              <div className="dashboardTileType">{sTileType}</div>
            </TooltipView>
            {this.getModeDropdown()}
          </div>

          <div className="endpointTileBody">
            {this.getTileInformationView()}
          </div>
          <TooltipView placement="bottom" label={getTranslation().REFRESH}>
            <div className="refreshEndpoint"
                 onClick={this.handleRefreshEndpointClicked.bind(this, sEndpointId, __props.tileMode)}></div>
          </TooltipView>
          {__props.isUploadEnableForCurrentUser ? this.getFileUploadButtonView() : null}
        </div>
    );
  }

  render () {
    let __props = this.props;
    let oTileView = this.getTileView();
    return __props.endpointType === EndpointTypes.INBOUND ?
        <FileDragAndDropView context="dashboardEndpoint"
                             id={__props.endpointId}
                             allowedFileTypes={OnBoardingImportTypes}
                             extraData={{
                               endpointId: __props.endpointId,
                               endpointType: __props.endpointType
                             }}>
          {oTileView}
          </FileDragAndDropView>
        :
        <div className="outboundEndpointTileWrapper">{oTileView}</div>;
  }
}

DashboardEndpointTileView.propTypes = oPropTypes;

export const view = DashboardEndpointTileView;
export const events = oEvents;
