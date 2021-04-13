package io.github.tral909.jcrypto_service.ui;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "keys", layout = MainView.class)
public class KeysView extends VerticalLayout {
    public KeysView() {
        add(new H1("Keys page will be soon!"));
    }
}