import React from 'react';
import CoreCreateButtonView from './core-create-button-view';

const oPropTypes = CoreCreateButtonView.propTypes;
const oEvents = CoreCreateButtonView.events;

class CreateButtonView extends CoreCreateButtonView.view {
  constructor (props) {
    super(props);
  }
}

CreateButtonView.propTypes = oPropTypes;

export const view = CreateButtonView;
export const events = oEvents;