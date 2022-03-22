import React from 'react';
import ReactPropTypes from 'prop-types';
import {SnackbarProvider, withSnackbar} from 'notistack';

class CustomSnackbarView extends React.Component {

  constructor (oProps) {
    super(oProps);
    oProps.showMessage(this.props.enqueueSnackbar);
    oProps.hideMessage(this.props.closeSnackbar);
  }

  render () {
    return (null);
  }
}

CustomSnackbarView.propTypes = {
  enqueueSnackbar: ReactPropTypes.func.isRequired,
  closeSnackbar: ReactPropTypes.func.isRequired,
};

const CustomSnackbarViewWithEnque = withSnackbar(CustomSnackbarView);

function IntegrationNotistack (oProps) {

  let showMessage = oProps.showMessage;
  let hideMessage = oProps.hideMessage;


  return (
    <SnackbarProvider maxSnack={3}
                      anchorOrigin={{
                        vertical: 'bottom',
                        horizontal: 'right',
                      }}
    >
      <CustomSnackbarViewWithEnque showMessage={showMessage} hideMessage={hideMessage}/>
    </SnackbarProvider>
  );
}

export default IntegrationNotistack;
