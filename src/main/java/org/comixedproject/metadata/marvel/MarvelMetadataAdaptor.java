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
import lombok.Synchronized;
import lombok.extern.log4j.Log4j2;
import org.comixedproject.metadata.MetadataException;
import org.comixedproject.metadata.adaptors.AbstractMetadataAdaptor;
import org.comixedproject.metadata.marvel.actions.MarvelGetVolumesAction;
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

  MarvelGetVolumesAction getVolumesAction = new MarvelGetVolumesAction();

  public MarvelMetadataAdaptor() {
    super("ComiXed Marvel Scraper", PROVIDER_NAME);
  }

  @Override
  protected IssueMetadata doGetIssue(
      final String volume, final String issueNumber, final MetadataSource metadataSource)
      throws MetadataException {
    return null;
  }

  @Override
  @Synchronized
  public List<VolumeMetadata> getVolumes(
      final String seriesName, final Integer maxRecords, final MetadataSource metadataSource)
      throws MetadataException {
    log.debug("Fetching volumes from Marvel: name={}", seriesName);

    getVolumesAction.setSeries(seriesName);
    getVolumesAction.setMaxRecords(maxRecords);
    getVolumesAction.setPublicKey(
        this.getSourcePropertyByName(metadataSource.getProperties(), PROPERTY_PUBLIC_KEY, true));
    getVolumesAction.setPrivateKey(
        this.getSourcePropertyByName(metadataSource.getProperties(), PROPERTY_PRIVATE_KEY, true));

    log.debug("Executing action");
    return getVolumesAction.execute();
  }

  @Override
  public List<IssueDetailsMetadata> getAllIssues(
      final String volume, final MetadataSource metadataSource) throws MetadataException {
    return List.of();
  }

  @Override
  public IssueDetailsMetadata getIssueDetails(
      final String issueId, final MetadataSource metadataSource) throws MetadataException {
    return null;
  }

  @Override
  public String getReferenceId(final String webAddress) {
    return "";
  }
}
