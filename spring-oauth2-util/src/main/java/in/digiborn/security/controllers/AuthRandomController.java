package in.digiborn.security.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import in.digiborn.security.models.AuthRandom;

@RestController
@RequestMapping("/auth-random")
public class AuthRandomController {

    @GetMapping
    public ResponseEntity<AuthRandom> getAuthRandom() throws NoSuchAlgorithmException {
        String codeVerifier = generateCodeVerifier();
        String codeChallenge = generateCodeChallenge(codeVerifier);
        AuthRandom authRandom = new AuthRandom(codeVerifier, codeChallenge);
        return ResponseEntity.ok(authRandom);
    }

    private String generateCodeVerifier() {
        SecureRandom sr = new SecureRandom();
        byte[] code = new byte[32];

        sr.nextBytes(code);
        return Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(code);
    }

    private String generateCodeChallenge(String codeVerifier) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(codeVerifier.getBytes());
        return Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(digest);

    }
}
