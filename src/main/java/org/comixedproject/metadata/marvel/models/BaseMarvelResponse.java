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
import lombok.Getter;

/**
 * <code>BaseMarvelResponse</code> contains the common elements shared between response objects from
 * Marvel.
 *
 * @param <T> the result type
 * @author Darryl L. Pierce
 */
public class BaseMarvelResponse<T> {
  @JsonProperty("code")
  @Getter
  private Integer code;

  @JsonProperty("status")
  @Getter
  private String status;

  @JsonProperty("copyright")
  @Getter
  private String copyright;

  @JsonProperty("attributeText")
  @Getter
  private String attributeText;

  @JsonProperty("etag")
  @Getter
  private String etag;

  @JsonProperty("data")
  @Getter
  private MarvelDataPayload<T> data;
}
