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

package org.comixedproject.metadata.marvel;

import static org.comixedproject.metadata.marvel.MarvelMetadataAdaptorProvider.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Synchronized;
import lombok.extern.log4j.Log4j2;
import org.comixedproject.metadata.MetadataException;
import org.comixedproject.metadata.adaptors.AbstractMetadataAdaptor;
import org.comixedproject.metadata.marvel.actions.*;
import org.comixedproject.metadata.model.IssueDetailsMetadata;
import org.comixedproject.metadata.model.IssueMetadata;
import org.comixedproject.metadata.model.VolumeMetadata;
import org.comixedproject.model.metadata.MetadataSource;

/**
 * <code>MarvelMetadataAdaptor</code> provides the adaptor to use when fetching metadata from
 * Marvel's online service.
 *
 * @author Darryl L. Pierce
 */
@Log4j2
public class MarvelMetadataAdaptor extends AbstractMetadataAdaptor {
  /** The value to use where a publisher is required. */
  public static final String PUBLISHER_NAME = "Marvel";

  static final String REFERENCE_ID_PATTERN =
      "^https?\\:\\/\\/.*(marvel\\.com)\\/comics\\/issue\\/([\\d]+).*";
  private static final int REFERENCE_ID_POSITION = 2;

  public MarvelMetadataAdaptor() {
    super("ComiXed Marvel Scraper", PROVIDER_NAME);
  }

  @Override
  @Synchronized
  protected IssueMetadata doGetIssue(
      final String volume, final String issueNumber, final MetadataSource metadataSource)
      throws MetadataException {
    return this.doGetIssue(volume, issueNumber, metadataSource, new MarvelGetIssueAction());
  }

  IssueMetadata doGetIssue(
      final String volume,
      final String issueNumber,
      final MetadataSource metadataSource,
      final MarvelGetIssueAction action)
      throws MetadataException {
    action.setSeries(volume);
    action.setIssueNumber(issueNumber);
    this.doSetCommonProperties(action, metadataSource);

    final List<IssueMetadata> result = action.execute();

    return (result.isEmpty() ? null : result.get(0));
  }

  @Override
  @Synchronized
  public List<VolumeMetadata> getVolumes(
      final String seriesName, final Integer maxRecords, final MetadataSource metadataSource)
      throws MetadataException {
    return this.getVolumes(seriesName, maxRecords, metadataSource, new MarvelGetVolumesAction());
  }

  List<VolumeMetadata> getVolumes(
      final String seriesName,
      final Integer maxRecords,
      final MetadataSource metadataSource,
      final MarvelGetVolumesAction action)
      throws MetadataException {
    log.debug("Fetching volumes from Marvel: name={}", seriesName);
    action.setSeries(seriesName);
    action.setMaxRecords(maxRecords);

    doSetCommonProperties(action, metadataSource);

    log.debug("Getting all volumes");
    return action.execute();
  }

  @Override
  public List<IssueDetailsMetadata> getAllIssues(
      final String seriesId, final MetadataSource metadataSource) throws MetadataException {
    return this.getAllIssues(seriesId, metadataSource, new MarvelGetAllIssuesAction());
  }

  List<IssueDetailsMetadata> getAllIssues(
      final String seriesId,
      final MetadataSource metadataSource,
      final MarvelGetAllIssuesAction action)
      throws MetadataException {
    log.debug("Setting series id: {}", seriesId);
    action.setSeriesId(seriesId);

    doSetCommonProperties(action, metadataSource);

    log.debug("Getting all issues for series");
    return action.execute();
  }

  @Override
  public IssueDetailsMetadata getIssueDetails(
      final String issueId, final MetadataSource metadataSource) throws MetadataException {
    return this.getIssueDetails(issueId, metadataSource, new MarvelGetIssueDetailsAction());
  }

  IssueDetailsMetadata getIssueDetails(
      final String issueId,
      final MetadataSource metadataSource,
      final MarvelGetIssueDetailsAction action)
      throws MetadataException {
    log.debug("Setting issue id: {}:", issueId);
    action.setComicId(issueId);

    this.doSetCommonProperties(action, metadataSource);

    log.debug("Getting issue details");
    return action.execute();
  }

  @Override
  public String getReferenceId(final String webAddress) {
    log.debug("Parsing web addresss: {}", webAddress);
    final Pattern pattern = Pattern.compile(REFERENCE_ID_PATTERN);
    final Matcher matches = pattern.matcher(webAddress);
    String referenceId = null;
    if (matches.matches()) {
      referenceId = matches.group(REFERENCE_ID_POSITION);
    }
    return referenceId;
  }

  private void doSetCommonProperties(
      final AbstractMarvelScrapingAction<?> action, final MetadataSource metadataSource)
      throws MetadataException {
    action.setPublicKey(
        this.getSourcePropertyByName(metadataSource.getProperties(), PROPERTY_PUBLIC_KEY, true));
    action.setPrivateKey(
        this.getSourcePropertyByName(metadataSource.getProperties(), PROPERTY_PRIVATE_KEY, true));
  }
}
