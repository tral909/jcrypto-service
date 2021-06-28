package io.github.tral909.jcrypto_service.ui;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import io.github.tral909.jcrypto_service.backend.logic.DigestService;
import io.github.tral909.jcrypto_service.backend.model.DigestAlgorithm;
import org.apache.commons.lang3.StringUtils;

@Route(value = "", layout = MainView.class)
public class DigestView extends VerticalLayout {

    public DigestView(DigestService digestService) {
        ComboBox<DigestAlgorithm> algorithms = new ComboBox<>("Algorithm");
        algorithms.setItems(DigestAlgorithm.values());
        algorithms.setItemLabelGenerator(DigestAlgorithm::getName);
        algorithms.setValue(DigestAlgorithm.SHA_256);

        TextArea origTxt = new TextArea("Original input");
        origTxt.setPlaceholder("Text to digest ...");
        origTxt.setWidth("90%");
        origTxt.setRequired(true);

        TextArea digestText = new TextArea("Digested output");
        digestText.setWidth("90%");

        Button digestBtn = new Button("digest", e -> {
            if (StringUtils.isNoneBlank(origTxt.getValue())) {
                digestText.setValue(digestService.digest(origTxt.getValue(), algorithms.getValue().getName()));
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
        add(algorithms, origTxt, digestText, digestBtn);
    }
}
