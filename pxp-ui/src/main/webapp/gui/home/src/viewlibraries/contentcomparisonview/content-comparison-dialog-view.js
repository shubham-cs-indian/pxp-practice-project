import React, {Component} from "react";

import PropTypes from 'prop-types';
import {getTranslations} from '../../commonmodule/store/helper/translation-manager.js';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher';
import { view as ContentScreenDialogViewObj } from '../../screens/homescreen/screens/contentscreen/view/content-screen-dialog-view';
import ContentComparisonView from './content-comparison-view';
import oFilterPropType  from '../../screens/homescreen/screens/contentscreen/tack/filter-prop-type-constants';

const ContentScreenDialogView  = ContentScreenDialogViewObj.view;

const oEvents = {
  GOLDEN_RECORD_MATCH_MERGE_ACTION_BUTTON_CLICKED: "golden_record_match_merge_action_button_clicked"
};

class ContentComparisonDialogView extends Component {

  handleContentScreenDialogButtonClicked = (sButtonId) => {
    EventBus.dispatch(oEvents.GOLDEN_RECORD_MATCH_MERGE_ACTION_BUTTON_CLICKED, sButtonId, {
      filterType: oFilterPropType.MODULE,
      screenContext: oFilterPropType.MODULE,
    });
  };

  getContentComparisonView () {
    let oComparisonViewData = this.props.comparisonViewData;
    let sHeaderLabel = oComparisonViewData && oComparisonViewData.headerLabel || "";
    let fOnButtonClickHandler = this.handleContentScreenDialogButtonClicked;
    let aButtonData = [
      {
        id: "cancel",
        label: getTranslations().CANCEL,
        isFlat: false,
        isDisabled: false
      }
    ];

    return (
        <ContentScreenDialogView
            buttonData={aButtonData}
            title={sHeaderLabel}
            contentStyle={{height: "90%"}}
            onRequestCloseHandler={fOnButtonClickHandler.bind(this, aButtonData[0].id)}
            onButtonClickHandler={fOnButtonClickHandler}>
          <ContentComparisonView {...oComparisonViewData}/>
        </ContentScreenDialogView>
    );
  };

  render () {
    return (
        <div>
          {this.getContentComparisonView()}
          </div>)
  }
}

ContentComparisonDialogView.protoTypes = {
  comparisonViewData: PropTypes.object
};

export default ContentComparisonDialogView;
export var events = oEvents;
