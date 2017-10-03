package de.djuelg.neuronizer.presentation.ui.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by djuelg on 04.11.2016.
 */

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new WidgetListFactory(this.getApplicationContext(), intent));
    }

}
