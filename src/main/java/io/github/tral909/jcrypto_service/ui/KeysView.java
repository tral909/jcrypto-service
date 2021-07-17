package io.github.tral909.jcrypto_service.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import io.github.tral909.jcrypto_service.backend.dto.PrivateKeyOutDto;
import io.github.tral909.jcrypto_service.backend.service.KeysService;
import org.apache.commons.lang3.StringUtils;

@Route(value = "keys", layout = MainView.class)
public class KeysView extends VerticalLayout {
	public KeysView(KeysService keysService) {
		Div keysBlock = new Div();
		for (PrivateKeyOutDto dto : keysService.getPrivateKeys()) {
			keysBlock.add(createKeyDiv(dto.getId(), dto.getName()));
		}

		TextField newKeyNameField = new TextField();
		Button createKeyBtn = new Button("Create", e -> {
			String value = newKeyNameField.getValue();
			if (StringUtils.isNoneBlank(value)) {
				Long id = keysService.generatePrivateKey(value);
				newKeyNameField.clear();
				keysBlock.add(createKeyDiv(id, value));
			}
		});
		createKeyBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		Span keyTitle = new Span("Create new key with name: ");
		keyTitle.addClassName("create-key-title");
		add(keysBlock, keyTitle, newKeyNameField, createKeyBtn);
	}

	private Div createKeyDiv(Long id, String name) {
		return new Div(new Span("ID: " +id + ". " + name));
	}
}