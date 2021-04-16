package io.github.tral909.jcrypto_service.ui;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import elemental.json.Json;
import io.github.tral909.jcrypto_service.backend.logic.SignatureService;

import java.io.ByteArrayInputStream;

@Route(value = "signature", layout = MainView.class)
public class CryptoSignatureView extends VerticalLayout {

    public CryptoSignatureView(SignatureService signatureService) {
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        Div output = new Div();

        upload.addSucceededListener(event -> {
            // убрать название файла после загрузки
            upload.getElement().setPropertyJson("files", Json.createArray());
            output.removeAll();

            byte[] signed = signatureService.sign(buffer.getInputStream());

            // ссылка для скачивания подписи
            final StreamResource resource = new StreamResource(event.getFileName() + ".sig",
                    () -> new ByteArrayInputStream(signed));
            output.add(new Anchor(resource, "Download signature!"));
        });

        upload.addFileRejectedListener(event -> {
            // убрать название файла после загрузки
            upload.getElement().setPropertyJson("files", Json.createArray());
            output.removeAll();
            output.add(event.getErrorMessage());
        });

        upload.getElement().addEventListener("file-remove", event -> output.removeAll());

        add(upload, output);
    }
}
