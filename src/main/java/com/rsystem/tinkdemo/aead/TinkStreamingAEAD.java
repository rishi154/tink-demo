package com.rsystem.tinkdemo.aead;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.security.GeneralSecurityException;

import com.google.crypto.tink.CleartextKeysetHandle;
import com.google.crypto.tink.JsonKeysetWriter;
import com.google.crypto.tink.KeyTemplates;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.StreamingAead;
import com.google.crypto.tink.streamingaead.StreamingAeadConfig;

public class TinkStreamingAEAD {
	StreamingAead streamingAead = null;
	byte[] add = "aa".getBytes();

	public TinkStreamingAEAD() {
		try {
			StreamingAeadConfig.register();
			KeysetHandle keysetHandle = KeysetHandle.generateNew(KeyTemplates.get("AES128_GCM_HKDF_1MB"));
			streamingAead = keysetHandle.getPrimitive(StreamingAead.class);
			String keysetFilename = "my_keyset.json";
			CleartextKeysetHandle.write(keysetHandle, JsonKeysetWriter.withFile(new File(keysetFilename)));
		} catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
		}
	}

	public void encrypt() {
		try (OutputStream ciphertextStream = streamingAead
				.newEncryptingStream(new FileOutputStream("stream-cipher.txt"), add);
				InputStream plaintextStream = new FileInputStream("plainTextInputFile")) {
			byte[] chunk = new byte[1024];
			int chunkLen = 0;
			while ((chunkLen = plaintextStream.read(chunk)) != -1) {
				ciphertextStream.write(chunk, 0, chunkLen);
			}
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
		}
	}

	public void decrypt() {

		InputStream ciphertextStream = null;
		try {
			ciphertextStream = streamingAead.newDecryptingStream(new FileInputStream("stream-cipher.txt"), add);
		} catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
		}
		OutputStream plaintextStream = null;
		try {
			plaintextStream = new FileOutputStream("plainTextOutputFile");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		byte[] chunk = new byte[1024];
		int chunkLen = 0;
		try {
			while ((chunkLen = ciphertextStream.read(chunk)) != -1) {
				plaintextStream.write(chunk, 0, chunkLen);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			ciphertextStream.close();
			plaintextStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
