package com.honeycomb.hexagon.sample.hexagonx;

import com.honeycomb.hexagon.ServiceRegistry;

public interface DynamicServices {
    /**
     * Default category for services not specified any categories.
     */
    String CATEGORY_DEFAULT = ServiceRegistry.CATEGORY_DEFAULT;

    /**
     * Start the service when system starts.
     */
    String CATEGORY_STARTUP = "startup";

    /**
     * Start the service when logged in.
     */
    String CATEGORY_LOGIN = "login";

    /**
     * Start the service when activated.
     */
    String CATEGORY_ACTIVATED = "activated";
}
