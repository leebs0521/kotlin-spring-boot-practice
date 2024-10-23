package com.example.simpleblog.common.auth

import com.example.simpleblog.common.exception.ErrorCode
import io.jsonwebtoken.*
import io.jsonwebtoken.security.SecurityException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


@Component
class JwtManger(

) {

  private val log = LoggerFactory.getLogger(this::class.java)
  private val secretKey: String = "testSecretKey20230327testSecretKey20230327testSecretKey20230327"
  private val claimEmail = "email"
  private val claimPassword = "password"
  private val expireTime = 1000 * 60 * 60
  val accessTokenHeader = "Authorization"
  val bearerPrefix = "Bearer "

  private fun getSecretKey(): SecretKey {
    return SecretKeySpec(secretKey.toByteArray(StandardCharsets.UTF_8), "HmacSHA256")
  }

  fun generateAccessToken(principal: PrincipalDetails): String {
    return Jwts.builder()
        .subject(principal.username)
        .expiration(Date(System.nanoTime() + expireTime))
        .claim(claimEmail, principal.username)
        .claim(claimPassword, principal.password)
        .signWith(getSecretKey())
        .compact()
  }

  fun getMemberEmail(token: String): String {
    return Jwts.parser()
        .verifyWith(getSecretKey())
        .build()
        .parseSignedClaims(token)
        .payload[claimEmail].toString()
  }

  fun validateToken(token: String): Boolean {
    try {
      val claims: Jws<Claims> = Jwts.parser()
          .verifyWith(getSecretKey())
          .build()
          .parseSignedClaims(token)
      return !claims.payload.expiration.before(Date())
    } catch (e: SecurityException) {
      log.info("SecurityException")
      throw JwtException(ErrorCode.WRONG_TYPE_TOKEN.message)
    } catch (e: MalformedJwtException) {
      log.info("MalformedJwtException")
      throw JwtException(ErrorCode.UNSUPPORTED_TOKEN.message)
    } catch (e: ExpiredJwtException) {
      log.info("ExpiredJwtException")
      throw JwtException(ErrorCode.EXPIRED_TOKEN.message)
    } catch (e: IllegalArgumentException) {
      log.info("IllegalArgumentException")
      throw JwtException(ErrorCode.UNKNOWN_ERROR.message)
    }
  }
}