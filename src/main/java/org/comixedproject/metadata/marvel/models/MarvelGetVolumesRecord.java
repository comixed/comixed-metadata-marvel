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
 * <code>MarvelGetVolumesRecord</code> represents one record in the list of volumes returned when
 * looking for a volume.
 *
 * @author Darryl L. Pierce
 */
public class MarvelGetVolumesRecord {
  @JsonProperty("id")
  @Getter
  private String id;

  @JsonProperty("title")
  @Getter
  private String title;

  @JsonProperty("urls")
  @Getter
  private List<MarvelUrl> urls;

  @JsonProperty("startYear")
  @Getter
  private String startYear;

  @JsonProperty("comics")
  @Getter
  private MarvelVolumeComicsRecord comics;

  @JsonProperty("thumbnail")
  @Getter
  private MarvelThumbnail thumbnail;
}
