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

import static org.comixedproject.metadata.marvel.MarvelMetadataAdaptor.PUBLISHER_NAME;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.comixedproject.metadata.MetadataException;
import org.comixedproject.metadata.marvel.models.MarvelGetVolumesQueryResponse;
import org.comixedproject.metadata.model.VolumeMetadata;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * <code>MarvelGetVolumesAction</code> retrieves the list of candidate volumes while scraping.
 *
 * @author Darryl L. Pierce
 */
@Log4j2
public class MarvelGetVolumesAction extends AbstractMarvelScrapingAction<List<VolumeMetadata>> {
  // The URL looks like:
  // https://gateway.marvel.com:443/v1/public/series?title=SERIES&ts=TIMESTAMP&apikey=APIKEY&hash=KEY

  @Getter @Setter private String series;
  @Getter @Setter private Integer maxRecords;

  @Override
  public List<VolumeMetadata> execute() throws MetadataException {
    this.doCheckSetup();

    if (StringUtils.isBlank(this.series)) throw new MetadataException("Missing series");

    final List<VolumeMetadata> result = new ArrayList<>();
    boolean done = false;

    final String encodedName;
    try {
      encodedName = URLEncoder.encode(this.series, StandardCharsets.UTF_8.toString());
    } catch (UnsupportedEncodingException error) {
      throw new MetadataException("Failed to encode series name", error);
    }

    while (!done) {
      log.trace("Generating request URL");
      final String url = this.doCreateUrl("series", String.format("title=%s", encodedName));
      final WebClient client = this.createWebClient(url);
      final Mono<MarvelGetVolumesQueryResponse> request =
          client.get().uri(url).retrieve().bodyToMono(MarvelGetVolumesQueryResponse.class);
      MarvelGetVolumesQueryResponse response = null;

      try {
        response = request.block();
      } catch (Exception error) {
        throw new MetadataException("Failed to get response", error);
      }

      if (response == null) {
        throw new MetadataException("Failed to receive response");
      }

      log.debug("Received: {} volume(s)", response.getData().getResults().size());
      response
          .getData()
          .getResults()
          .forEach(
              volume -> {
                log.trace(
                    "Processing volume record: {} name={}", volume.getId(), volume.getTitle());
                final VolumeMetadata entry = new VolumeMetadata();
                entry.setId(volume.getId());
                entry.setName(volume.getTitle());
                entry.setStartYear(volume.getStartYear());
                entry.setPublisher(PUBLISHER_NAME);
                entry.setIssueCount(volume.getComics().getAvailable());
                entry.setImageURL(
                    String.format(
                        "%s.%s",
                        volume.getThumbnail().getPath(), volume.getThumbnail().getExtension()));
                result.add(entry);
              });
      done = isDone(response);
    }

    log.debug("Returning {} volume(s)", result.size());
    return result;
  }
}
