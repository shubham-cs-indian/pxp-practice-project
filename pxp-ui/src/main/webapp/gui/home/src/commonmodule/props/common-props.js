import SessionProps from './session-props';
import BreadCrumbProps from './breadcrumb-props';

export default {
  sessionProps: SessionProps,
  breadCrumbProps: BreadCrumbProps,

  reset: function () {
    SessionProps.reset();
    BreadCrumbProps.reset();
  }
};
