package io.github.tral909.jcrypto_service.ui;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "signature", layout = MainView.class)
public class CryptoSignatureView extends VerticalLayout {
    public CryptoSignatureView() {
        add(new H1("Crypto Signature page will be soon!"));
    }
}
