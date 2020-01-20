package com.renny.simplebrowser.business.log;



public interface Logs {

    /**
     * common库
     */
    LogScheduler base = LogScheduler.instance("base");
    /**
     * h5库
     */
    LogScheduler h5 = LogScheduler.instance("h5");
    /**
     *base库
     */
    LogScheduler common = LogScheduler.instance("common");
    /**
     *network
     */
    LogScheduler network = LogScheduler.instance("network");
    /**
     * component库
     */
    LogScheduler component = LogScheduler.instance("component");
    /**
     * /**
     * 默认
     */
    LogScheduler defaults = LogScheduler.instance("defaults");

}
