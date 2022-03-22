import MicroEvent from '../../libraries/microevent/MicroEvent';
import NumberUtils from '../../commonmodule/util/number-util';

/**
 * @class INumberStore - Use for numberlocale view.
 */
class INumberStore {

  static triggerChange () {
    INumberStore.prototype.trigger('i-number-changed');
  }
}

MicroEvent.mixin(INumberStore);

NumberUtils.bind('number-data-changed', INumberStore.triggerChange);

export default INumberStore;