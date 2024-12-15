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

package org.comixedproject.metadata.marvel.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

/**
 * <code>MarvelSeriesIssueCreators</code> represents all credits for a single issue when scraping a
 * series.
 *
 * @author Darryl L. Pierc
 */
public class MarvelSeriesIssueCreators {
  @JsonProperty("available")
  @Getter
  private int available;

  @JsonProperty("collectionURI")
  @Getter
  private String collectionURI;

  @JsonProperty("items")
  @Getter
  private List<MarvelCreditEntry> items;
}
