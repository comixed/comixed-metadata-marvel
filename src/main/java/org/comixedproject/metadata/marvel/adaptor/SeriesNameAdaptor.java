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

package org.comixedproject.metadata.marvel.adaptor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * <code>SeriesNameAdaptor</code> extracts details from the provided series name.
 *
 * @author Darryl L. Pierce
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
public class SeriesNameAdaptor {
  @Getter private static final SeriesNameAdaptor instance = new SeriesNameAdaptor();
  private final Pattern pattern = Pattern.compile("^(.+) \\(([\\d]{4}).*\\)");

  /**
   * Extracts the series name, start year, and optional end year from the series detail.
   *
   * @param series the series detail
   * @return
   */
  public SeriesDetail execute(final String series) {
    log.debug("Extracting details from series: {}", series);
    final Matcher matches = this.pattern.matcher(series);
    if (matches.find()) {
      final String name = matches.group(1);
      final String volume = matches.group(2);
      log.debug("Found values: name={} volume={}", name, volume);
      return new SeriesDetail(name, volume);
    }

    log.debug("No series detail extracted: returning full name");
    return new SeriesDetail(series, "");
  }

  /** <code>SeriesDetail</code> contains the details found within a series' name value. */
  @AllArgsConstructor
  public static class SeriesDetail {
    @Getter private final String name;
    @Getter private final String startYear;
  }
}
