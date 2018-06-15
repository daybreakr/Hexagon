package com.honeycomb.hexagon;

public abstract class HexagonComponent {
    private HexagonAssembly mAssembly;

    public void attachTo(HexagonAssembly assembly) {
        mAssembly = assembly;
    }

    protected HexagonAssembly assembly() {
        return mAssembly;
    }
}
