package com.rsystem.tinkdemo.aead;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.CleartextKeysetHandle;
import com.google.crypto.tink.JsonKeysetWriter;
import com.google.crypto.tink.KeyTemplates;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadConfig;

public class TinkAEAD {

	Aead aead = null;
	
	public TinkAEAD() {
		try {
			AeadConfig.register();
			KeysetHandle keysetHandle = KeysetHandle.generateNew(KeyTemplates.get("AES128_GCM"));
			aead = keysetHandle.getPrimitive(Aead.class);
			String keysetFilename = "my_keyset.json";
			CleartextKeysetHandle.write(keysetHandle, JsonKeysetWriter.withFile(new File(keysetFilename)));
		} catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public byte[] encrypt(String input) {
	    try {			
		    byte[] ciphertext = aead.encrypt(input.getBytes(),null);
		    return ciphertext;
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public byte[] Decrypt(byte[] input) {
	    byte[] decrypted;
		try {
			decrypted = aead.decrypt(input, null);
			return decrypted;
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
	    return null;
	}

	public boolean encrypt(File input) {
		return true;
	}
	
	public boolean Decrypt(File input) {
		return true;
	}
}
