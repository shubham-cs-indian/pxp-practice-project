import React from 'react';
import CoreCreateButtonView from './core-create-button-view';

const oPropTypes = CoreCreateButtonView.propTypes;

const oEvents = {
  FLAT_CREATE_BUTTON_VIEW_BTN_CLICKED: "flat_create_button_view_btn_clicked"
}

class FlatCreateButtonView extends CoreCreateButtonView.view {

  constructor (props) {
    super(props);
  }

/*  handleFlatButtonClicked = () => {
    let {context: sContext, contextId: sContextId} = this.props;
    EventBus.dispatch(oEvents.FLAT_CREATE_BUTTON_VIEW_BTN_CLICKED, sContext, sContextId, {
      filterType: oFilterPropType.MODULE,
      screenContext: oFilterPropType.MODULE,
    });
  };*/

/*
  getCreateButtonMenuView = () => {
    return this.getFlatCreateButton(false, this.props.onClickHandler || this.handleFlatButtonClicked);
  }
*/

}

FlatCreateButtonView.propTypes = oPropTypes;

export const view = FlatCreateButtonView;
export const events = oEvents;
