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

package org.comixedproject.metadata.marvel.actions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.comixedproject.metadata.MetadataException;
import org.comixedproject.metadata.actions.AbstractScrapingAction;
import org.comixedproject.metadata.marvel.auth.MarvelAuthorizationAdaptor;
import org.comixedproject.metadata.marvel.models.BaseMarvelResponse;
import org.comixedproject.metadata.marvel.models.MarvelDate;

/**
 * <code>AbstractMarvelScrapingAction</code> provides a foundation for creating actions for the
 * Marvel metadata service.
 *
 * @param <T> the action return type
 * @author Darryl L. Pierce
 */
@Log4j2
public abstract class AbstractMarvelScrapingAction<T> extends AbstractScrapingAction<T> {
  // 1 - base URL and port, 2 - path, 3 - query parameters, 4 - timestamp, 5 - api key
  private static final String URL_FORMAT = "%s/v1/public/%s?%s&ts=%s&apikey=%s&hash=%s";
  static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

  @Getter @Setter private String baseUrl = "https://gateway.marvel.com";
  @Getter @Setter private String publicKey;
  @Getter @Setter private String privateKey;

  protected boolean isDone(final BaseMarvelResponse<?> response) {
    final int current =
        ((response.getData().getLimit() + response.getData().getOffset())
            + response.getData().getCount());
    final Integer total = response.getData().getTotal();
    final boolean result = total <= current;
    log.trace("current={} total={} result={}", current, total, result);
    return result;
  }

  /**
   * Ensures the minimal settings are defined.
   *
   * @throws MetadataException if the settings aren't defined
   */
  protected void doCheckSetup() throws MetadataException {
    if (StringUtils.isBlank(this.publicKey)) throw new MetadataException("Public key not defined");
    if (StringUtils.isBlank(this.privateKey))
      throw new MetadataException("Private key not defined");
  }

  /**
   * Creates the URL to use.
   *
   * @param path the url path
   * @param parameters the parameters for the request
   * @return the URL
   */
  protected String doCreateUrl(final String path, final String parameters) {
    final long timestamp = System.currentTimeMillis();

    return String.format(
        URL_FORMAT,
        this.baseUrl,
        path,
        parameters,
        timestamp,
        this.getPublicKey(),
        MarvelAuthorizationAdaptor.getInstance()
            .getHashForRequest(timestamp, publicKey, privateKey));
  }

  protected Date getCoverDate(final List<MarvelDate> dates) {
    return this.findDate("focDate", dates);
  }

  protected Date getStoreDate(final List<MarvelDate> dates) {
    return this.findDate("onsaleDate", dates);
  }

  private Date findDate(final String dateType, final List<MarvelDate> dates) {
    final Optional<MarvelDate> result =
        dates.stream().filter(entry -> entry.getType().equals(dateType)).findFirst();
    if (result.isPresent()) {
      try {
        return dateFormat.parse(result.get().getDate());
      } catch (ParseException error) {
        log.error("Failed to parse date", error);
      }
    }
    return null;
  }
}
