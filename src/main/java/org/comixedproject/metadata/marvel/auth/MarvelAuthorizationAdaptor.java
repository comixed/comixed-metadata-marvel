/*
 * ComiXed - A digital comic book library management application.
 * Copyright (C) 2024, The ComiXed Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses>
 */

package org.comixedproject.metadata.marvel.auth;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.DigestUtils;

/**
 * <code>MarvelAuthorizationAdaptor</code> provides code for generating the authorization hash used
 * by Marvel's online service.
 *
 * @author Darryl L. Pierce
 */
@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MarvelAuthorizationAdaptor {
  @Getter
  private static final MarvelAuthorizationAdaptor instance = new MarvelAuthorizationAdaptor();

  /**
   * Returns the MD5 hash created from the timestamp, public, and private keys.
   *
   * @param timestamp the timestamp
   * @param publicKey the public key
   * @param privateKey the private key
   * @return the hash
   */
  public String getHashForRequest(
      final long timestamp, final String publicKey, final String privateKey) {
    log.debug(
        "Generating hashh for request: timestamp={} publicKey={} privateKey={}",
        timestamp,
        publicKey,
        privateKey);
    return DigestUtils.md5DigestAsHex(
        String.format("%s%s%s", String.valueOf(timestamp), privateKey, publicKey).getBytes());
  }
}
