import MicroEvent from '../../libraries/microevent/MicroEvent';
import MomentUtils from '../../commonmodule/util/moment-utils';

class IDateStore {

  static triggerChange () {
    IDateStore.prototype.trigger('i-date-changed');
  }
}

MicroEvent.mixin(IDateStore);

MomentUtils.bind('moment-data-changed', IDateStore.triggerChange);

export default IDateStore;