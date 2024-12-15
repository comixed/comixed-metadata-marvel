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

import static org.comixedproject.metadata.marvel.MarvelMetadataAdaptorProvider.PROPERTY_PRIVATE_KEY;
import static org.comixedproject.metadata.marvel.MarvelMetadataAdaptorProvider.PROPERTY_PUBLIC_KEY;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.comixedproject.metadata.MetadataException;
import org.comixedproject.metadata.marvel.actions.MarvelGetIssueAction;
import org.comixedproject.metadata.marvel.actions.MarvelGetIssueDetailsAction;
import org.comixedproject.metadata.marvel.actions.MarvelGetVolumesAction;
import org.comixedproject.metadata.model.IssueDetailsMetadata;
import org.comixedproject.metadata.model.IssueMetadata;
import org.comixedproject.metadata.model.VolumeMetadata;
import org.comixedproject.model.metadata.MetadataSource;
import org.comixedproject.model.metadata.MetadataSourceProperty;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarvelMetadataAdaptorTest {
  private static final Integer TEST_MAX_RECORDS = 1000;
  private static final String TEST_SERIES = "The Series";
  private static final String TEST_VOLUME = "12345";
  private static final String TEST_ISSUE_NUMBER = "17";
  private static final String TEST_ISSUE_ID = "67890";
  private static final String TEST_WEB_ADDRESS = "http://www.marvel.com";
  private static final String TEST_PUBLIC_KEY = "the.public.key";
  private static final String TEST_PRIVATE_KEY = "the.private.key";

  @InjectMocks private MarvelMetadataAdaptor adaptor;
  @Mock private MarvelGetVolumesAction getVolumesAction;
  @Mock private MarvelGetIssueAction getIssueAction;
  @Mock private MarvelGetIssueDetailsAction getIssueDetailsAction;

  @Mock private MetadataSource metadataSource;
  @Mock private List<VolumeMetadata> volumeList;
  @Mock private IssueMetadata issue;
  @Mock private IssueDetailsMetadata issueDetailsMetadata;

  final Set<MetadataSourceProperty> metadataSourceProperties = new HashSet<>();

  @Before
  public void setUp() throws MetadataException {
    adaptor.getVolumesAction = getVolumesAction;
    adaptor.getIssueAction = getIssueAction;
    adaptor.getIssueDetailsAction = getIssueDetailsAction;

    Mockito.when(getVolumesAction.execute()).thenReturn(volumeList);

    Mockito.when(metadataSource.getProperties()).thenReturn(metadataSourceProperties);
    metadataSourceProperties.add(
        new MetadataSourceProperty(metadataSource, PROPERTY_PUBLIC_KEY, TEST_PUBLIC_KEY));
    metadataSourceProperties.add(
        new MetadataSourceProperty(metadataSource, PROPERTY_PRIVATE_KEY, TEST_PRIVATE_KEY));
  }

  @Test
  public void testDoGetIssue() throws MetadataException {
    final List<IssueMetadata> issueList = new ArrayList<>();
    issueList.add(issue);

    Mockito.when(getIssueAction.execute()).thenReturn(issueList);

    final IssueMetadata result = adaptor.doGetIssue(TEST_VOLUME, TEST_ISSUE_NUMBER, metadataSource);

    assertNotNull(result);
    assertSame(issue, result);

    Mockito.verify(getIssueAction, Mockito.times(1)).setSeries(TEST_VOLUME);
    Mockito.verify(getIssueAction, Mockito.times(1)).setIssueNumber(TEST_ISSUE_NUMBER);
    Mockito.verify(getIssueAction, Mockito.times(1)).setPublicKey(TEST_PUBLIC_KEY);
    Mockito.verify(getIssueAction, Mockito.times(1)).setPrivateKey(TEST_PRIVATE_KEY);
  }

  @Test
  public void testGetVolumes() throws MetadataException {
    final List<VolumeMetadata> result =
        adaptor.getVolumes(TEST_SERIES, TEST_MAX_RECORDS, metadataSource);

    assertNotNull(result);
    assertSame(volumeList, result);

    Mockito.verify(getVolumesAction, Mockito.times(1)).setSeries(TEST_SERIES);
    Mockito.verify(getVolumesAction, Mockito.times(1)).setMaxRecords(TEST_MAX_RECORDS);
    Mockito.verify(getVolumesAction, Mockito.times(1)).setPublicKey(TEST_PUBLIC_KEY);
    Mockito.verify(getVolumesAction, Mockito.times(1)).setPrivateKey(TEST_PRIVATE_KEY);
  }

  @Test
  public void testGetAllIssues() throws MetadataException {
    final List<IssueDetailsMetadata> result = adaptor.getAllIssues(TEST_VOLUME, metadataSource);

    assertNotNull(result);
  }

  @Test
  public void testGetIssueDetails() throws MetadataException {
    Mockito.when(getIssueDetailsAction.execute()).thenReturn(issueDetailsMetadata);
    final IssueDetailsMetadata result = adaptor.getIssueDetails(TEST_ISSUE_ID, metadataSource);

    assertNotNull(result);
    assertSame(issueDetailsMetadata, result);

    Mockito.verify(getIssueDetailsAction, Mockito.times(1)).setComicId(TEST_ISSUE_ID);
  }

  @Test
  public void testGetReferenceId() throws MetadataException {
    final String result = adaptor.getReferenceId(TEST_WEB_ADDRESS);

    assertNotNull(result);
  }
}
