import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import ScreenModeUtils from './../store/helper/screen-mode-utils';

import { view as VariantTagGroupView } from './../../../../../viewlibraries/varianttagsummaryview/variant-tag-group-view';
var getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {};

const oPropTypes = {
  variantSectionViewData: ReactPropTypes.object,
  filterContext: ReactPropTypes.object.isRequired,
  entityHeaderTagsEditData: ReactPropTypes.object
};

// @CS.SafeComponent
class EntityContextSummaryView extends React.Component {
  static propTypes = oPropTypes;

  handleButtonClick = (sButtonId) => {
    if(sButtonId === "ok") {
      this.closeVariantEditView();
    }
  };

  getVariantTagGroupView = () => {
    var oEntityHeaderTagsEditData = this.props.entityHeaderTagsEditData;

    return (<VariantTagGroupView
        canEdit={oEntityHeaderTagsEditData.canEdit}
        canDelete={oEntityHeaderTagsEditData.canDelete}
        variantSectionViewData={this.props.variantSectionViewData}
    />);
  };

  getIsContextEmpty = () => {
    var __props = this.props;
    var oVariantSectionViewData = __props.variantSectionViewData;
    var oSelectedContext = oVariantSectionViewData.selectedContext;
    var bIsTimeEnabled = oSelectedContext.isTimeEnabled;
    var aVariantTags = oSelectedContext.tags;
    return !(bIsTimeEnabled || !CS.isEmpty(aVariantTags) || !CS.isEmpty(oSelectedContext.entities));
  };

  getSummaryView = () => {
    var aVariantViews = [];
    var bIsContextEmpty = this.getIsContextEmpty();
    if (!bIsContextEmpty) {
      aVariantViews.push(this.getVariantTagGroupView());
    } else {
      aVariantViews.push(<div className="emptyContext">{getTranslation().NOTHING_TO_DISPLAY}</div>);
    }

    return aVariantViews;
  };

  render() {

    return (
        <div className="entityContextSummaryViewContainer">
          <div className="contextHeader">{getTranslation().CONTEXT_CONFIGURATION}</div>
          <div className="contextBody">{this.getSummaryView()}</div>
        </div>
    );
  }
}

export const view = EntityContextSummaryView;
export const events = oEvents;
