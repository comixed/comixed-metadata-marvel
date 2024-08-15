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
 * <code>MarvelDataPayload</code> represents the payload for a response body from Marvel.
 *
 * @param <T> the result data type
 * @author Darryl L. Pierce
 */
public class MarvelDataPayload<T> {
  @JsonProperty("offset")
  @Getter
  private Integer offset;

  @JsonProperty("limit")
  @Getter
  private Integer limit;

  @JsonProperty("total")
  @Getter
  private Integer total;

  @JsonProperty("count")
  @Getter
  private Integer count;

  @JsonProperty("results")
  @Getter
  private List<T> results;
}
