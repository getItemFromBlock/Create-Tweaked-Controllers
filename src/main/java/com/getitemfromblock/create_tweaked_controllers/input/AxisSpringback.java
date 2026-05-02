package com.getitemfromblock.create_tweaked_controllers.input;

public class AxisSpringback {
    private static final double TOLERANCE_VALUE = 0.999;

    public boolean isExponential = true;

    // Rates are in seconds
    public float globalRate = 1.0f;
    public boolean useGlobalRate = true;
    public float riseRate = 1.0f;
    public float fallRate = 1.0f;
    public float holdTime = 0f;

    private float current = 0f;

    private float holdTimer = 0f;
    private boolean wasActive = false;

    private long lastNanoTime = -1L;

    public float smooth(float target) {
        long now = System.nanoTime();
        float dt;
        if (lastNanoTime < 0L) {
            dt = 0.05f * 20f;
        } else {
            dt = (float)((now - lastNanoTime) / 1_000_000_000.0) * 20f;
            if (dt > 10f) dt = 10f;
        }
        lastNanoTime = now;
        return smooth(target, dt);
    }

    public float smooth(float target, float dt) {
        float dtSeconds = dt / 20f;

        if (isExponential) {
            boolean active = target > 0f;

            if (active) {
                holdTimer = 0f;
                wasActive = true;
            } else {
                if (wasActive) {
                    holdTimer = holdTime;
                    wasActive = false;
                }
            }

            if (holdTimer > 0f) {
                holdTimer -= dtSeconds;
            } else {
                float rateSeconds = useGlobalRate ? globalRate : ((target >= current) ? riseRate : fallRate);
                float r = (float)(-Math.log(1.0 - TOLERANCE_VALUE) / rateSeconds);
                current += (target - current) * (1f - (float)Math.exp(-r * dtSeconds));
            }
        } else { // Linear
            float effectiveRise = 1.0f / (useGlobalRate ? globalRate : riseRate);
            float effectiveFall = 1.0f / (useGlobalRate ? globalRate : fallRate);

            boolean active = target > 0f;

            if (active) {
                holdTimer = 0f;
                wasActive = true;
                float maxDelta = effectiveRise * dtSeconds;
                float diff = target - current;
                if (Math.abs(diff) <= maxDelta)
                    current = target;
                else
                    current += Math.signum(diff) * maxDelta;
            } else {
                if (wasActive) {
                    holdTimer = holdTime;
                    wasActive = false;
                }

                if (holdTimer > 0f) {
                    holdTimer -= dtSeconds;
                } else {
                    float maxDelta = effectiveFall * dtSeconds;
                    float diff = target - current;
                    if (Math.abs(diff) <= maxDelta)
                        current = target;
                    else
                        current += Math.signum(diff) * maxDelta;
                }
            }
        }
        return current;
    }

    public void reset() {
        current = 0f;
        holdTimer = 0f;
        wasActive = false;
        lastNanoTime = -1L;
    }
}
