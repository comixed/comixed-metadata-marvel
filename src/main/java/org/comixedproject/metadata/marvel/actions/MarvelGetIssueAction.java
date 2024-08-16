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

import java.util.ArrayList;
import java.util.List;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.comixedproject.metadata.MetadataException;
import org.comixedproject.metadata.marvel.models.MarvelGetIssueQueryResponse;
import org.comixedproject.metadata.model.IssueMetadata;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * <code>AbstractMarvelScrapingAction</code> retrieves a single issue from Marvel's online service.
 *
 * @author Darryl L. Pierce
 */
@Log4j2
public class MarvelGetIssueAction extends AbstractMarvelScrapingAction<List<IssueMetadata>> {
  // URL:
  // https://gateway.marvel.com:443/v1/public/series/SERIES_NUMBER/comics?issueNumber=ISSUE_NUMBER&orderBy=issueNumber&limit=100&offset=1&apikey=APIKEY

  @Setter private String series;
  @Setter private String issueNumber;

  @Override
  public List<IssueMetadata> execute() throws MetadataException {
    this.doCheckSetup();

    if (StringUtils.isBlank(this.series)) throw new MetadataException("Missing series");
    if (StringUtils.isBlank(this.issueNumber)) throw new MetadataException("Missing issue number");
    boolean done = false;
    final List<IssueMetadata> result = new ArrayList<>();

    while (!done) {
      final String url =
          this.doCreateUrl(
              String.format("series/%s/comics", this.series),
              String.format("noVariants=true&formatType=comic&issueNumber=%s", this.issueNumber));

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

      response
          .getData()
          .getResults()
          .forEach(
              issue -> {
                final IssueMetadata metadata = new IssueMetadata();
                metadata.setId(issue.getId());
                metadata.setVolumeId(this.series);
                metadata.setName(issue.getSeries().getName());
                metadata.setVolumeName(issue.getSeries().getName());
                metadata.setDescription(issue.getDescription());
                metadata.setCoverDate(
                    this.doConverDate(
                        issue.getDates().stream()
                            .filter(date -> date.getType().equals("onsaleDate"))
                            .findFirst()));
                metadata.setCoverDate(
                    this.doConverDate(
                        issue.getDates().stream()
                            .filter(date -> date.getType().equals("onsaleDate"))
                            .findFirst()));
                metadata.setCoverUrl(
                    String.format(
                        "%s.%s",
                        issue.getThumbnail().getPath(), issue.getThumbnail().getExtension()));
                result.add(metadata);
              });

      done = this.isDone(response);
    }

    return result;
  }
}
