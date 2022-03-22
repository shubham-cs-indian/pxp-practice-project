import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { view as ImageSimpleView } from '../imagesimpleview/image-simple-view';

const oEvents = {
  LOGO_CLICKED: "logo_clicked"
};

const oPropTypes = {
  mode: ReactPropTypes.string
};
/**
 * @class LogoView - Use to Display logo in Application Header.
 * @memberOf Views
 * @property {string} [mode] - Pass Mode name of Application like which Mode we are using ex. settings,about,PIM etc.
 */

// @CS.SafeComponent
class LogoView extends React.Component {
  constructor(props) {
    super(props);
  }

  handleSearchBoxClicked =()=> {
    EventBus.dispatch(oEvents.LOGO_CLICKED, this.props.mode);
  }

  createDOMWithoutImg = () =>{
    return (
      <div className="logoImageContainer default-icon">
      </div>
    );
  };

  createDOMWithImg = () => {
    return(
      <div className="logoImageContainer">
        {/*<img src={this.props.applicationIcon}/>*/}
        <ImageSimpleView classLabel="img" imageSrc={this.props.applicationIcon}/>
      </div>
    );
  };

  render() {
    let oDomToRender = this.props.applicationIcon ? this.createDOMWithImg() : this.createDOMWithoutImg();
    return (
        <div className="logoContainer" onClick={this.handleSearchBoxClicked}>
          {oDomToRender}
        </div>
    );
  }
}

LogoView.propTypes = oPropTypes;

export const view = LogoView;
export const events = oEvents;
