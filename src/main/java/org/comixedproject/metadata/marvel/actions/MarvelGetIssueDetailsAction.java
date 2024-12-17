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

import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.comixedproject.metadata.MetadataException;
import org.comixedproject.metadata.marvel.MarvelMetadataAdaptor;
import org.comixedproject.metadata.marvel.adaptor.SeriesNameAdaptor;
import org.comixedproject.metadata.marvel.models.MarvelGetIssueQueryResponse;
import org.comixedproject.metadata.marvel.models.MarvelGetIssueRecord;
import org.comixedproject.metadata.marvel.models.MarvelUrl;
import org.comixedproject.metadata.model.IssueDetailsMetadata;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * <code>MarvelGetIssueDetailsAction</code> returns the details for a single issue from the Marvel
 * online database.
 *
 * @author Darryl L. Pierce
 */
@Log4j2
public class MarvelGetIssueDetailsAction
    extends AbstractMarvelScrapingAction<IssueDetailsMetadata> {
  @Getter @Setter private String comicId;

  @Override
  public IssueDetailsMetadata execute() throws MetadataException {
    this.doCheckSetup();

    if (StringUtils.isBlank(this.comicId)) throw new MetadataException("Missing comic id");

    final String url = this.doCreateUrl(String.format("comics/%s", this.comicId), "");

    final WebClient client = this.createWebClient(url);
    final Mono<MarvelGetIssueQueryResponse> request =
        client.get().uri(url).retrieve().bodyToMono(MarvelGetIssueQueryResponse.class);
    MarvelGetIssueQueryResponse response = null;

    try {
      response = request.block();
    } catch (Exception error) {
      throw new MetadataException("Failed to get response", error);
    }

    if (response == null) {
      throw new MetadataException("Failed to receive response");
    }

    if (response.getData().getResults().isEmpty()) {
      log.info("No records found");
      return null;
    }

    log.debug("Loading details from first of {} record(s)", response.getData().getResults().size());
    final MarvelGetIssueRecord detail = response.getData().getResults().get(0);
    final IssueDetailsMetadata result = new IssueDetailsMetadata();
    result.setSourceId(detail.getId());
    result.setPublisher(MarvelMetadataAdaptor.PUBLISHER_NAME);
    final SeriesNameAdaptor.SeriesDetail seriesDetails =
        SeriesNameAdaptor.getInstance().execute(detail.getSeries().getName());
    result.setSeries(seriesDetails.getName());
    result.setVolume(seriesDetails.getStartYear());
    result.setIssueNumber(detail.getIssueNumber());
    result.setCoverDate(this.getCoverDate(detail.getDates()));
    result.setStoreDate(this.getStoreDate(detail.getDates()));
    result.setDescription(detail.getDescription());
    Optional<MarvelUrl> address =
        detail.getUrls().stream().filter(entry -> entry.getType().equals(("detail"))).findFirst();
    if (address.isPresent()) {
      result.setWebAddress(address.get().getUrl());
    }
    return result;
  }
}
