import React from 'react';
import CS from '../../../libraries/cs';
import ReactPropTypes from 'prop-types';
import { getTranslations as getTranslation } from '../../../commonmodule/store/helper/translation-manager.js';
import TooltipView from '../../tooltipview/tooltip-view';
import ViewUtils from '../../utils/view-library-utils';
import { view as ImageFitToContainerView } from './../../imagefittocontainerview/image-fit-to-container-view';

const oEvents = {};

const oPropTypes = {
  isRecentlyUpdated: ReactPropTypes.bool,
  isNewlyCreated: ReactPropTypes.bool,
  creationLanguageData: ReactPropTypes.object
};
/**
 * @class ThumbnailInformationIndicatorIconsView - Use for Display Thumbnail Information Icon in Product.
 * @memberOf Views
 * @property {bool} [isRecentlyUpdated] -  boolean value for isRecentlyUpdated or not.
 * @property {bool} [isNewlyCreated] -  boolean value for isNewlyCreated or not.
 * @property {object} [creationLanguageData] -  an object which contain creationLanguageData.
 */

class ThumbnailInformationIndicatorIconsView extends React.Component {

  getRecentlyUpdatedDom = () => {
    let oRecentlyUpdatedIndicator = null;
    let bIsRecentlyUpdated = this.props.isRecentlyUpdated;
    if (bIsRecentlyUpdated) {
      oRecentlyUpdatedIndicator =
          <TooltipView label={getTranslation().RECENTLY_UPDATED}>
            <div className="thumbnailInformationIndicatorsIcon recentlyUpdatedContent"> U</div>
          </TooltipView>
    }
    return oRecentlyUpdatedIndicator;
  };

  getNewlyCreatedDom = () => {
    let oNewlyCreatedIndicator = null;
    let bIsNewlyCreated = this.props.isNewlyCreated;
    if (bIsNewlyCreated) {
      oNewlyCreatedIndicator =
          <TooltipView label={getTranslation().NEW}>
            <div className="thumbnailInformationIndicatorsIcon newlyCreatedContent"> N</div>
          </TooltipView>
    }

    return oNewlyCreatedIndicator;
  };

  getCreationLanguageIcon = () => {
    let oCreationLanguageDOM = null;
    let oCreationLanguageData = this.props.creationLanguageData;
    if (CS.isEmpty(oCreationLanguageData)) {
      return null;
    }
    let sIcon = oCreationLanguageData.iconKey;
    if (sIcon) {
      let sIconURL = ViewUtils.getIconUrl(sIcon);
      oCreationLanguageDOM = (
          <div className="thumbnailInformationIndicatorsIcon creationLanguageIcon">
            <ImageFitToContainerView imageSrc={sIconURL}/>
          </div>
      )
    } else {
      let sLabel = oCreationLanguageData.abbreviation;
      sLabel = sLabel[0] + (sLabel[1] || "");
      oCreationLanguageDOM = <div className="thumbnailInformationIndicatorsIcon creationLanguageInitials">{sLabel}</div>;
    }

    return (
        <TooltipView label={CS.getLabelOrCode(oCreationLanguageData)}>
          {oCreationLanguageDOM}
        </TooltipView>
    )
  };


  render () {
    let oRecentlyUpdatedIndicator = this.getRecentlyUpdatedDom();
    let oNewlyCreatedIndicator = this.getNewlyCreatedDom();
    let oCreationLanguageIndicator = this.getCreationLanguageIcon();

    if (oRecentlyUpdatedIndicator || oNewlyCreatedIndicator || oCreationLanguageIndicator) {
      return (
          <div className="thumbnailInformationIndicators">
            {oRecentlyUpdatedIndicator}
            {oNewlyCreatedIndicator}
            {oCreationLanguageIndicator}
          </div>
      )
    }

    return null;
  };
}

export const view = ThumbnailInformationIndicatorIconsView;
export const events = oEvents;
