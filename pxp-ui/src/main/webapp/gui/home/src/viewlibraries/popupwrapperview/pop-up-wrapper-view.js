import React from 'react';
import { view as CustomDialogView } from '../../viewlibraries/customdialogview/custom-dialog-view';

const oEvents = {};

const oPropTypes = {};
/**
 * @class PopUpWrapper - Use for variant tag gruop dialog view.
 * @memberOf Views
 */

let PopUpWrapper = (View) => {

  if (!View) {
    throw new Error("UNDEFINED_VIEW_PASSED");
  }

  return class extends React.Component {

    static propTypes = View.propTypes;

    constructor (props) {
      super(props);
      this.className = View.name;
      this.state = {
        hasError: false,
      };
    }

    onClose = () => {
      this.props.onClose && this.props.onClose();
    };

    render () {
      let oBodyStyle = {
        padding: 0,
        maxHeight: 'none',
        overflowY: "auto",
      };
      let oContentStyle = {
        height: "90%",
        maxHeight: "none",
        width: '90%',
        maxWidth: 'none'
      };

      return (
          <CustomDialogView
              open={this.props.isDialogOpen}
              bodyStyle={oBodyStyle}
              contentStyle={oContentStyle}
              onRequestClose={this.onClose}
              title={this.props.header}
              buttonData={this.props.buttonData}
              buttonClickHandler={this.props.onDialogButtonClick}
          >
            {<View {...this.props}/>}
          </CustomDialogView>
      )
    }
  }
};

export default PopUpWrapper;
