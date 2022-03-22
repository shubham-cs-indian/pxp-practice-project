import CS from '../../../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { getTranslations as getTranslation } from '../../../../../../../commonmodule/store/helper/translation-manager.js';
import TooltipView from './../../../../../../../viewlibraries/tooltipview/tooltip-view';

const oPropTypes = {
  tileData: ReactPropTypes.object
};

// @CS.SafeComponent
class DashboardEndpointOutboundTileView extends React.Component {

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

  getFileViews = () => {
    let _this = this;
    let __props = _this.props;
    let oTileData = __props.tileData;

    let aFilesToDownload = oTileData.filesToDownload;
    let aFileViews = [];

    CS.forEach(aFilesToDownload, function (oFile, iIndex) {
      let oDownloadButtonDOM = null;
      if (oFile.downloadUrl) {
        let sRefKey = "downloadAnchor_outbound_" + iIndex;
        oDownloadButtonDOM = (
            <TooltipView placement="bottom" label={getTranslation().DOWNLOAD}>
              <div className="elementValue downloadButton outbound"
                   onClick={_this.handleDownloadButtonClicked.bind(_this, sRefKey)}>
                <a ref={_this.setRef.bind(_this,sRefKey)} href={oFile.downloadUrl} target="_blank"/>
              </div>
            </TooltipView>
        );
      }
      aFileViews.push(
          <div className="informationElement" key={iIndex}>
            <TooltipView placement="bottom" label={oFile.fileName}>
            <div className="elementLabel outbound">{oFile.fileName}</div>
            </TooltipView>
            {oDownloadButtonDOM}
          </div>
      );
    });
    return aFileViews;
  };

  render () {
    let sFileToDownload = getTranslation().FILES_TO_DOWNLOAD + ' :';
    let aFileViews = this.getFileViews();
    return (
        <div className="outboundEndpointTileBody">
          <div className="totalCountInfoContainer">{getTranslation().SUCCESSFUL_ARTICLE_EXPORTS + " : " + this.props.tileData.totalArticlesCount}</div>
          {CS.isEmpty(aFileViews) ? null : <div className="headerLabel">{sFileToDownload}</div>}
          {aFileViews}
        </div>
    );

  };

}

DashboardEndpointOutboundTileView.propTypes = oPropTypes;

export const view = DashboardEndpointOutboundTileView;
