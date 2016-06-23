package com.epicodus.pocketpomodoro.util;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class NpaLinearLayoutManager extends LinearLayoutManager {
    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

    public NpaLinearLayoutManager(Context context, AttributeSet attrs, int defStyleArray, int defStyleRes) {
        super(context, attrs, defStyleArray, defStyleRes);
    }

    public NpaLinearLayoutManager(Context context) {
        super(context);
    }
}
