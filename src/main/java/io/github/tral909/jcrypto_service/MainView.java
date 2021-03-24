package io.github.tral909.jcrypto_service;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;

@Route
@CssImport("./styles/styles.css")
public class MainView extends VerticalLayout {

    public MainView(DigestService digestService) {
        TextArea origTxt = new TextArea("Original input");
        origTxt.setPlaceholder("Text to digest ...");
        origTxt.setWidth("50%");
        origTxt.setRequired(true);

        TextArea digestText = new TextArea("Digested output");
        digestText.setWidth("50%");

        Button digestBtn = new Button("digest", e -> {
            if (StringUtils.isNotBlank(origTxt.getValue())) {
                digestText.setValue(digestService.digest(origTxt.getValue(), "MD5"));
            } else {
                digestText.clear();
            }
        });
        //digestBtn.setEnabled(false);
        digestBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        //криво работает(только по blur с textarea) сделаем валидацию потом
//        origTxt.addValueChangeListener(e ->
//            digestBtn.setEnabled(StringUtils.isNotBlank(origTxt.getValue()))
//        );

        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(origTxt, digestText, digestBtn);
    }

}