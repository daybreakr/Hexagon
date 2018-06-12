package com.honeycomb.hexagon.impl;

import com.honeycomb.hexagon.core.IService;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Base implementation of {@link IService} which handles the state changes.
 */
public abstract class StatedService implements IService {
    private boolean mStarted = false;
    private final AtomicBoolean mChangingState = new AtomicBoolean(false);

    @Override
    public void start() {
        changeState(true);
    }

    @Override
    public void stop() {
        changeState(false);
    }

    @Override
    public boolean isStarted() {
        return mStarted;
    }

    private void changeState(boolean start) {
        if (mStarted == start) {
            // Illegal transition, e.g. intent to start when started or stop when stopped.
            return;
        }

        if (mChangingState.compareAndSet(false, true)) {
            try {
                boolean success = false;
                try {
                    if (start) {
                        onStart();
                    } else {
                        onStop();
                    }
                    success = true;
                } catch (Exception e) {
                    // TODO: log transition errors.
                    e.printStackTrace();

                    // Consider it sopped successfully even if errors occurred.
                    if (!start) {
                        success = true;
                    }
                }
                if (success) {
                    mStarted = start;
                }
            } finally {
                mChangingState.set(false);
            }
        }
    }

    protected void onStart() {
    }

    protected void onStop() {
    }
}
