package com.rsystem.tinkdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.rsystem.tinkdemo.aead.TinkAEAD;
import com.rsystem.tinkdemo.aead.TinkDeterministicAEAD;
import com.rsystem.tinkdemo.aead.TinkMAC;
import com.rsystem.tinkdemo.aead.TinkStreamingAEAD;

@SpringBootApplication
public class TinkDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TinkDemoApplication.class, args);

		TinkAEAD tinkaead = new TinkAEAD();
		byte[] encrypted = tinkaead.encrypt("Hello World!");
		System.out.println("Encrypted value : " + new String(encrypted));
		encrypted = tinkaead.encrypt("Hello World!");
		System.out.println("Encrypted value : " + new String(encrypted));
		System.out.println("Decrypted value : " + new String(tinkaead.Decrypt(encrypted)));
		
		System.out.println("------------------------------------------------------");
		
		TinkDeterministicAEAD tinkdaead =  new TinkDeterministicAEAD();
		encrypted = tinkdaead.encrypt("Hello World!");
		System.out.println("Encrypted value : " + new String(encrypted));
		encrypted = tinkdaead.encrypt("Hello World!");
		System.out.println("Encrypted value : " + new String(encrypted));
		System.out.println("Decrypted value : " + new String(tinkdaead.Decrypt(encrypted)));
		
		System.out.println("------------------------------------------------------");

		TinkStreamingAEAD steamAEAD = new TinkStreamingAEAD();
		steamAEAD.encrypt();
		System.out.println("Successfully written Encrypted information to file using StreamingAEAD");
		steamAEAD.decrypt();
		System.out.println("Successfully written Decrypted information to file using StreamingAEAD");
		
		System.out.println("------------------------------------------------------");
		
		
		TinkMAC tinkMac = new TinkMAC();
		tinkMac.generateMac();
		tinkMac.verifyMac();
				

	}

}
