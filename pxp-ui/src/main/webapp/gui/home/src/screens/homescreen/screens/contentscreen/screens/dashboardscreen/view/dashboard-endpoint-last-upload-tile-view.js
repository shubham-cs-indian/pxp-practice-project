import CS from '../../../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { getTranslations as getTranslation } from '../../../../../../../commonmodule/store/helper/translation-manager.js';
import TooltipView from './../../../../../../../viewlibraries/tooltipview/tooltip-view';
import { view as NothingFoundView } from './../../../../../../../viewlibraries/nothingfoundview/nothing-found-view';
import ViewUtils from './utils/view-utils';

const oPropTypes = {
  tileData: ReactPropTypes.object
};

// @CS.SafeComponent
class DashboardEndpointLastUploadTileView extends React.Component {

  constructor (props) {
    super(props);

    this.setRef = ( sRef, element) => {
      this[sRef] = element;
    };
  };

  handleDownloadButtonClicked = (sRefKey, oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    this[sRefKey].click();
  };

  render () {

    let _this = this;
    let oTileData = this.props.tileData;

    if (CS.isEmpty(oTileData)) {
      return (
          <NothingFoundView/>
      );
    }

    let sUploadDateString = oTileData.uploadTimestamp ? ViewUtils.getDateAttributeInTimeFormat(oTileData.uploadTimestamp) : null;
    let sFileName = getTranslation().TITLE+ ' :';
    let sSuccessCount = getTranslation().SUCCESS_ARTICLES_COUNT + ' :';
    let sFailureCount = getTranslation().FAILED_ARTICLES_COUNT + ' :';
    let sUploadDate = getTranslation().UPLOAD_DATE + ' :';
    let oDownloadButtonDOM = null;
    if (oTileData.downloadUrl) {
      let sRefKey = "downloadAnchor_inbound";
      oDownloadButtonDOM = (
          <TooltipView placement="bottom" label={getTranslation().DOWNLOAD}>
            <div className="elementValue downloadButton"
                 onClick={_this.handleDownloadButtonClicked.bind(_this, sRefKey)}>
              <a ref={_this.setRef.bind(_this,sRefKey)} href={oTileData.downloadUrl} target="_blank"/>
            </div>
          </TooltipView>
      );
    }

    return (
        <div className="inboundEndpointBody">

          <div className="informationElement">
            <div className="elementLabel">{sFileName}</div>
            <TooltipView placement="bottom" label={oTileData.fileName}>
            <div className="elementValue">{oTileData.fileName}</div>
            </TooltipView>
            {oDownloadButtonDOM}
          </div>

          <div className="informationElement">
            <div className="elementLabel">{sSuccessCount}</div>
            <div className="elementValue">{oTileData.successCount}</div>
          </div>

          <div className="informationElement">
            <div className="elementLabel">{sFailureCount}</div>
            <div className="elementValue">{oTileData.failureCount}</div>
          </div>

          <div className="informationElement">
            <div className="elementLabel">
              {getTranslation().REDVIOLATIONS}
              <div className={"elementIcon red"}/>
            </div>
            <div className="elementValue">{oTileData.redCount}</div>
          </div>

          <div className="informationElement">
            <div className="elementLabel">
              {getTranslation().ORANGEVIOLATIONS}
              <div className={"elementIcon orange"}/>
            </div>
            <div className="elementValue">{oTileData.orangeCount}</div>
          </div>

          <div className="informationElement">
            <div className="elementLabel">
              {getTranslation().YELLOWVIOLATIONS}
              <div className={"elementIcon yellow"}/>
            </div>
            <div className="elementValue">{oTileData.yellowCount}</div>
          </div>

          <div className="informationElement">
            <div className="elementLabel">{sUploadDate}</div>
            <div className="elementValue">{sUploadDateString}</div>
          </div>

        </div>
    );
  };

}

DashboardEndpointLastUploadTileView.propTypes = oPropTypes;

export const view = DashboardEndpointLastUploadTileView;
