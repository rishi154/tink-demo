package com.rsystem.tinkdemo.aead;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.List;

import com.google.crypto.tink.CleartextKeysetHandle;
import com.google.crypto.tink.JsonKeysetWriter;
import com.google.crypto.tink.KeyTemplates;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.Mac;
import com.google.crypto.tink.mac.MacConfig;
import com.google.crypto.tink.subtle.Hex;

public class TinkMAC {

	Mac macp = null;
	byte[] input ="Hello World!".getBytes();

	public TinkMAC() {
		try {
			MacConfig.register();
			KeysetHandle keysetHandle = KeysetHandle.generateNew(KeyTemplates.get("HMAC_SHA256_128BITTAG"));
			macp = keysetHandle.getPrimitive(Mac.class);
			String keysetFilename = "my_keyset.json";
			CleartextKeysetHandle.write(keysetHandle, JsonKeysetWriter.withFile(new File(keysetFilename)));
		} catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
		}
	}

	public void generateMac() {
		byte[] mac = null;
		try {
			mac = macp.computeMac(input);
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		try (FileOutputStream stream = new FileOutputStream("outputMacFile")) {
			stream.write(Hex.encode(mac).getBytes(UTF_8));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Successfully written MAC to file");
	}
	
	
	public void verifyMac() {
		List<String> lines = null;
		try {
			lines = Files.readAllLines(Path.of("outputMacFile"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	    if (lines.size() != 1) {
	      System.out.printf("The MAC file should contain only one line, got %d", lines.size());
	      System.exit(1);
	    }
	    
	    
		byte[] mac = Hex.decode(lines.get(0).trim());
	    try {
	      macp.verifyMac(mac, input);
	      System.out.println("MAC verification successful.");
	    } catch (GeneralSecurityException ex) {
	      System.err.println("MAC verification failed.");
	      System.exit(1);
	    }
	}
}
