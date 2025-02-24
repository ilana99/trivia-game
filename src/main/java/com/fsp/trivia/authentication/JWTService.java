package com.fsp.trivia.authentication;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JWTService {

	@Value("${jwt.secret}")
	private String key;
	
	
	public SecretKey key() {
		return Keys.hmacShaKeyFor(key.getBytes());
	}

		
	public String generateJWT(String username) {
		return Jwts.builder() // (1)

				.header() // (2) optional
				.keyId("aKeyId")
				.and()
				.issuer("ravallumTrivia")
				.subject(username) // (3) JSON Claims, or
				// .content(aByteArray, "text/plain") // any byte[] content, with media type
				.issuedAt(new Date())

				.signWith(key()) // (4) if signing, or
				// .encryptWith(key, keyAlg, encryptionAlg) // if encrypting

				.compact(); // (5)
	}

	public String checkUsername(String jwt) {
		try {
			return Jwts.parser()
					// .keyLocator(keyLocator)
					.verifyWith(key())
					.build()
					.parseSignedClaims(jwt)
					.getPayload()
					.getSubject();
		} catch (JwtException ex) {
			System.err.println("Invalid JWT: " + ex.getMessage());
			throw new IllegalArgumentException("Invalid JWT token.");
		}

	}

	public Boolean validateToken(String token) {
		try {
			Jwts
			.parser()
			.verifyWith(key())
			.build()
			.parseSignedClaims(token);
			return true;
		} catch (MalformedJwtException e) {
			System.err.println("Invalid JWT token: {}" + e.getMessage());
		} catch (ExpiredJwtException e) {
			System.err.println("JWT token is expired: {}" + e.getMessage());
		} catch (UnsupportedJwtException e) {
			System.err.println("JWT token is unsupported: {}" + e.getMessage());
		} catch (IllegalArgumentException e) {
			System.err.println("JWT claims string is empty: {}" + e.getMessage());
		}

		return false;
	}
}
