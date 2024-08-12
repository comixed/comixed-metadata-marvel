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
import static org.junit.Assert.*;

import org.comixedproject.metadata.adaptors.MetadataAdaptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarvelMetadataAdaptorProviderTest {
  @InjectMocks private MarvelMetadataAdaptorProvider provider;

  @Test
  public void testPropertiesDefined() {
    assertTrue(provider.getProperties().contains(PROPERTY_PUBLIC_KEY));
    assertTrue(provider.getProperties().contains(PROPERTY_PRIVATE_KEY));
    assertTrue(provider.getProperties().contains(PROPERTY_SOURCE_DOMAIN));
  }

  @Test
  public void testCreate() {
    final MetadataAdaptor result = provider.create();

    assertNotNull(result);
  }

  @Test
  public void testSupportedReference() {
    assertFalse(provider.supportedReference(""));
  }
}
