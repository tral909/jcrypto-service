package io.github.tral909.jcrypto_service.component;

import com.vaadin.flow.component.upload.MultiFileReceiver;
import com.vaadin.flow.component.upload.receivers.FileData;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Копия MultiFileMemoryBuffer с возможностью очистки загруженных<br>
 * в буфер файлов {@link ClearableMultiFileMemoryBuffer#clearFiles}
 */
public class ClearableMultiFileMemoryBuffer implements MultiFileReceiver {

	private Map<String, FileData> files = new HashMap<>();

	public void clearFiles() {
		files.clear();
	}

	@Override
	public OutputStream receiveUpload(String fileName, String MIMEType) {
		ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
		files.put(fileName, new FileData(fileName, MIMEType, outputBuffer));

		return outputBuffer;
	}

	/**
	 * Get the files in memory for this buffer.
	 *
	 * @return files in memory
	 */
	public Set<String> getFiles() {
		return files.keySet();
	}

	/**
	 * Get file data for upload with file name.
	 *
	 * @param fileName
	 *            file name to get upload data for
	 * @return file data for filename or null if not found
	 */
	public FileData getFileData(String fileName) {
		return files.get(fileName);
	}

	/**
	 * Get the output stream for file.
	 *
	 * @param fileName
	 *            name of file to get stream for
	 * @return file output stream or empty stream if no file found
	 */
	public ByteArrayOutputStream getOutputBuffer(String fileName) {
		if (files.containsKey(fileName)) {
			return (ByteArrayOutputStream) files.get(fileName)
					.getOutputBuffer();
		}
		return new ByteArrayOutputStream();
	}

	/**
	 * Get the input stream for file with filename.
	 *
	 * @param filename
	 *            name of file to get input stream for
	 * @return input stream for file or empty stream if file not found
	 */
	public InputStream getInputStream(String filename) {
		if (files.containsKey(filename)) {
			return new ByteArrayInputStream(((ByteArrayOutputStream) files
					.get(filename).getOutputBuffer()).toByteArray());
		}
		return new ByteArrayInputStream(new byte[0]);
	}
}
