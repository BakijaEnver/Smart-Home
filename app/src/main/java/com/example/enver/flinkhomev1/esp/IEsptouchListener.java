package com.example.enver.flinkhomev1.esp;
public interface IEsptouchListener {
	/**
	 * when new esptouch result is added, the listener will call
	 * onEsptouchResultAdded callback
	 * 
	 * @param result
	 *            the Esptouch result
	 */
	void onEsptouchResultAdded(IEsptouchResult result);
}
