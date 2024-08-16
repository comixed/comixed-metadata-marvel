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
 * <code>MarvelGetIssueRecord</code> represents a single record in the response body for a get issue
 * request to Marvel.
 *
 * @author Darryl L. Pierce
 */
public class MarvelGetIssueRecord {
  @JsonProperty("id")
  @Getter
  private String id;

  @JsonProperty("series")
  @Getter
  private MarvelSeries series;

  @JsonProperty("issueNumber")
  @Getter
  private String issueNumber;

  @JsonProperty("title")
  @Getter
  private String title;

  @JsonProperty("description")
  @Getter
  private String description;

  @JsonProperty("resourceURI")
  @Getter
  private String resourceURI;

  @JsonProperty("dates")
  @Getter
  private List<MarvelDate> dates;

  @JsonProperty("thumbnail")
  @Getter
  private MarvelThumbnail thumbnail;

  @JsonProperty("creators")
  @Getter
  private MarvelIssueCredits creators;

  @JsonProperty("characters")
  @Getter
  private MarvelIssueCharacters characters;

  @JsonProperty("stories")
  @Getter
  private MarvelIssueStories stories;
}
