package com.abatra.android.wheelie.preferences;

import java.util.List;

public interface ListPreferencePresenter<LPI extends ListPreferenceItem> {
    void setListPreferenceItems(List<LPI> items);
}
