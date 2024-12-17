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

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.comixedproject.metadata.MetadataException;
import org.comixedproject.metadata.marvel.adaptor.SeriesNameAdaptor;
import org.comixedproject.metadata.marvel.models.MarvelGetAllIssuesQueryResponse;
import org.comixedproject.metadata.model.IssueDetailsMetadata;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * <code>MarvelGetAllIssuesAction</code> retrieves the metadata for all comics for a given volume.
 *
 * @author Darryl L. Pierce
 */
@Log4j2
public class MarvelGetAllIssuesAction
    extends AbstractMarvelScrapingAction<List<IssueDetailsMetadata>> {
  // URL:
  // https://gateway.marvel.com/v1/public/series/2069/comics?noVariants=true&apikey=763df8a7c3c0f6d3bb7fcf088bbf6ee1

  @Getter @Setter private String seriesId;

  @Override
  public List<IssueDetailsMetadata> execute() throws MetadataException {
    this.doCheckSetup();

    if (StringUtils.isBlank(this.seriesId)) throw new MetadataException("Missing series id");

    final List<IssueDetailsMetadata> result = new ArrayList<>();
    boolean done = false;

    while (!done) {
      log.trace("Generating request URL: series id={}", this.seriesId);
      final String url =
          this.doCreateUrl(String.format("series/%s/comics", this.seriesId), "noVariants=true");
      final WebClient client = this.createWebClient(url);
      final Mono<MarvelGetAllIssuesQueryResponse> request =
          client.get().uri(url).retrieve().bodyToMono(MarvelGetAllIssuesQueryResponse.class);
      MarvelGetAllIssuesQueryResponse response = null;

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
              issue -> {
                log.trace("Processing volume record: {} name={}", issue.getId(), issue.getTitle());
                final IssueDetailsMetadata entry = new IssueDetailsMetadata();
                entry.setSourceId(issue.getId());
                entry.setPublisher(PUBLISHER_NAME);
                final SeriesNameAdaptor.SeriesDetail seriesDetails =
                    SeriesNameAdaptor.getInstance().execute(issue.getSeries().getName());
                entry.setSeries(seriesDetails.getName());
                entry.setVolume(seriesDetails.getStartYear());
                entry.setIssueNumber(issue.getIssueNumber());
                entry.setTitle(issue.getTitle());
                entry.setCoverDate(this.getCoverDate(issue.getDates()));
                entry.setStoreDate(this.getStoreDate(issue.getDates()));
                result.add(entry);
              });
      done = isDone(response);
    }

    log.debug("Returning {} volume(s)", result.size());
    return result;
  }
}
