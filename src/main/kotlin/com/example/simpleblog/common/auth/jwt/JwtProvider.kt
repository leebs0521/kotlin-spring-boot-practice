package com.example.simpleblog.common.auth.jwt

import com.example.simpleblog.common.exception.ErrorCode
import io.jsonwebtoken.*
import io.jsonwebtoken.security.SecurityException
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


@Component
class JwtProvider(
    @Value("\${jwt.secretKey}")
    private val secretKey: String,

    @Value("\${jwt.access.expiration}")
    private val accessTokenExpirationPeriod: Long,

    @Value("\${jwt.refresh.expiration}")
    private val refreshTokenExpirationPeriod: Long,

    @Value("\${jwt.access.header}")
    val accessHeader: String,

    @Value("\${jwt.refresh.header}")
    val refreshHeader: String,
) {

  private val log = LoggerFactory.getLogger(this::class.java)
  val bearerPrefix = "Bearer "

  private fun getSecretKey(): SecretKey {
    return SecretKeySpec(secretKey.toByteArray(StandardCharsets.UTF_8), "HmacSHA256")
  }

  fun generateAccessToken(username: String): String {
    return Jwts.builder()
        .claim("username", username)
        .claim("category", "AccessToken")
        .issuedAt(Date(System.currentTimeMillis()))
        .expiration(Date(System.nanoTime() + accessTokenExpirationPeriod))
        .signWith(getSecretKey())
        .compact()
  }

  fun generateRefreshToken(username: String): String {
    return Jwts.builder()
        .claim("username", username)
        .claim("category", "RefreshToken")
        .issuedAt(Date(System.currentTimeMillis()))
        .expiration(Date(System.nanoTime() + refreshTokenExpirationPeriod))
        .signWith(getSecretKey())
        .compact()
  }

  fun sendAccessAndRefreshToken(response: HttpServletResponse, accessToken: String,
                                refreshToken: String) {

    setAccessTokenHeader(response, accessToken)
    setRefreshTokenCookie(response, refreshToken)
  }

  private fun setAccessTokenHeader(response: HttpServletResponse, accessToken: String) {
    response.setHeader(accessHeader, bearerPrefix + accessToken)
  }

  private fun setRefreshTokenCookie(response: HttpServletResponse, refreshToken: String) {
    val cookie: Cookie = Cookie(refreshHeader, refreshToken)
    cookie.isHttpOnly = true
    cookie.secure = true
    cookie.path = "/"
    cookie.maxAge = (refreshTokenExpirationPeriod / 1000).toInt()
    response.addCookie(cookie)
  }

  fun getUsername(token: String): String {
    return Jwts.parser()
        .verifyWith(getSecretKey())
        .build()
        .parseSignedClaims(token)
        .payload["username"].toString()
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