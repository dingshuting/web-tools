steal('can/util',function(can){
	can.bindAndSetup = function() {
		can.addEvent.apply(this, arguments);
		if(!this._init){
			if(!this._bindings ){
				this._bindings = 1;
				this._bindsetup && this._bindsetup();
			} else {
				this._bindings++;
			}
		}
		return this;
	};
	can.unbindAndTeardown = function(ev, handler) {
		can.removeEvent.apply(this, arguments);
		this._bindings--;
		if(!this._bindings){
			this._bindteardown && this._bindteardown();
		}
		return this;
	}
	return can;

})