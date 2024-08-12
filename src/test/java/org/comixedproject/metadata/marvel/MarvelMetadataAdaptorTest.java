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

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.comixedproject.metadata.MetadataException;
import org.comixedproject.metadata.model.IssueDetailsMetadata;
import org.comixedproject.metadata.model.VolumeMetadata;
import org.comixedproject.model.metadata.MetadataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarvelMetadataAdaptorTest {
  private static final Integer TEST_MAX_RECORDS = 1000;
  private static final String TEST_SERIES = "The Series";
  private static final String TEST_VOLUME = "12345";
  private static final String TEST_ISSUE_NUMBER = "17";
  private static final String TEST_ISSUE_ID = "67890";
  private static final String TEST_WEB_ADDRESS = "http://www.marvel.com";

  @InjectMocks private MarvelMetadataAdaptor adaptor;

  @Mock private MetadataSource metadataSource;

  @Test
  public void testDoGetIssue() throws MetadataException {
    assertNull(adaptor.doGetIssue(TEST_VOLUME, TEST_ISSUE_NUMBER, metadataSource));
  }

  @Test
  public void testGetVolumes() throws MetadataException {
    final List<VolumeMetadata> result =
        adaptor.getVolumes(TEST_SERIES, TEST_MAX_RECORDS, metadataSource);

    assertNotNull(result);
  }

  @Test
  public void testGetAllIssues() throws MetadataException {
    final List<IssueDetailsMetadata> result = adaptor.getAllIssues(TEST_VOLUME, metadataSource);

    assertNotNull(result);
  }

  @Test
  public void testGetIssueDetails() throws MetadataException {
    final IssueDetailsMetadata result = adaptor.getIssueDetails(TEST_ISSUE_ID, metadataSource);

    assertNull(result);
  }

  @Test
  public void testGetReferenceId() throws MetadataException {
    final String result = adaptor.getReferenceId(TEST_WEB_ADDRESS);

    assertNotNull(result);
  }
}
