package io.github.tral909.jcrypto_service.ui;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileData;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import elemental.json.Json;
import io.github.tral909.jcrypto_service.backend.logic.SignatureService;
import io.github.tral909.jcrypto_service.component.ClearableMultiFileMemoryBuffer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Set;

@Route(value = "signature", layout = MainView.class)
public class CryptoSignatureView extends VerticalLayout {

	public CryptoSignatureView(SignatureService signatureService) {
		MemoryBuffer bufferSign = new MemoryBuffer();
		Upload uploadSign = new Upload(bufferSign);
		Div outputSign = new Div();

		// Подпись файла
		uploadSign.addSucceededListener(event -> {
			// убрать название файла после загрузки
			//upload.getElement().setPropertyJson("files", Json.createArray());
			outputSign.removeAll();

			byte[] signed = signatureService.sign(bufferSign.getInputStream());

			// ссылка для скачивания подписи
			final StreamResource resource = new StreamResource(event.getFileName() + ".sig",
					() -> new ByteArrayInputStream(signed));
			outputSign.add(new Anchor(resource, "Download signature!"));
		});

		uploadSign.addFileRejectedListener(event -> {
			// убрать название файла после загрузки
			//upload.getElement().setPropertyJson("files", Json.createArray());
			outputSign.removeAll();
			outputSign.add(event.getErrorMessage());
		});

		uploadSign.getElement().addEventListener("file-remove", event -> outputSign.removeAll());


		// Проверка подписи
		//MultiFileMemoryBuffer bufferVerifySign = new MultiFileMemoryBuffer();
		ClearableMultiFileMemoryBuffer bufferVerifySign = new ClearableMultiFileMemoryBuffer();
		Upload uploadVerifySign = new Upload(bufferVerifySign);
		Div outputVerifySign = new Div();

		uploadVerifySign.addSucceededListener(event -> {
			outputVerifySign.removeAll();

			boolean result;
			Set<String> fileNames = bufferVerifySign.getFiles();
			int files = fileNames.size();

			if (files == 2) {
				InputStream sign = null;
				InputStream data = null;
				for (String name : fileNames) {
					FileData fileData = bufferVerifySign.getFileData(name);
					if (fileData.getMimeType().equals("application/pgp-signature")) {
						sign = bufferVerifySign.getInputStream(name);
					} else {
						data = bufferVerifySign.getInputStream(name);
					}
				}

				if (sign == null || data == null) {
					// убрать сами файлы из буфера
					bufferVerifySign.clearFiles();
					// убрать названия файлов
					uploadVerifySign.getElement().setPropertyJson("files", Json.createArray());

					Span error = new Span("Signature file or data file is not recognized!");
					error.getStyle().set("color", "red");
					outputVerifySign.add(error);
					return;
				}

				result = signatureService.verifySign(data, sign);

				Span resultText = new Span("Signature status: " + (result ? "verified" : "failed"));
				resultText.getStyle().set("color", (result ? "green" : "red"));
				outputVerifySign.add(resultText);
				// убрать сами файлы из буфера
				bufferVerifySign.clearFiles();
				// убрать названия файлов
				uploadVerifySign.getElement().setPropertyJson("files", Json.createArray());
			}
		});

		uploadVerifySign.addFileRejectedListener(event -> {
			outputVerifySign.removeAll();
			outputVerifySign.add(event.getErrorMessage());
		});

		uploadVerifySign.getElement().addEventListener("file-remove", event -> {
			bufferVerifySign.clearFiles();
			outputVerifySign.removeAll();
		});


		Div signTitle = new Div();
		signTitle.add("Sign file");
		VerticalLayout signLayout = new VerticalLayout(signTitle, uploadSign, outputSign);

		Div verifyTitle = new Div();
		verifyTitle.add("Verify signature");
		VerticalLayout verifySignLayout = new VerticalLayout(verifyTitle, uploadVerifySign, outputVerifySign);

		HorizontalLayout mainLayout = new HorizontalLayout(signLayout, verifySignLayout);
		add(mainLayout);
	}
}
