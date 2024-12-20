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

package org.comixedproject.metadata.marvel.adaptor;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SeriesNameAdaptorTest {
  private static final String TEST_SERIES_NAME = "Spider-Woman";
  private static final String TEST_START_YEAR = "2009";
  private static final String TEST_END_YEAR = "2014";
  private static final String TEST_SERIES_WITH_START_YEAR_ONLY =
      String.format("%s (%s)", TEST_SERIES_NAME, TEST_START_YEAR);
  private static final String TEST_SERIES_WITH_START_AND_END =
      String.format("%s (%s - %s)", TEST_SERIES_NAME, TEST_START_YEAR, TEST_END_YEAR);
  private static final String TEST_SERIES_WITH_NO_MATCHES =
      String.format("%s %s", TEST_SERIES_NAME, TEST_SERIES_NAME);

  @InjectMocks private SeriesNameAdaptor adaptor;

  @Test
  public void testExecute_startYearOnly() {
    final SeriesNameAdaptor.SeriesDetail result = adaptor.execute(TEST_SERIES_WITH_START_YEAR_ONLY);

    assertNotNull(result);
    assertEquals(TEST_SERIES_NAME, result.getName());
    assertEquals(TEST_START_YEAR, result.getStartYear());
  }

  @Test
  public void testExecute_noMatchesFound() {
    final SeriesNameAdaptor.SeriesDetail result = adaptor.execute(TEST_SERIES_WITH_NO_MATCHES);

    assertNotNull(result);
    assertEquals(TEST_SERIES_WITH_NO_MATCHES, result.getName());
    assertEquals("", result.getStartYear());
  }

  @Test
  public void testExecute() {
    final SeriesNameAdaptor.SeriesDetail result = adaptor.execute(TEST_SERIES_WITH_START_AND_END);

    assertNotNull(result);
    assertEquals(TEST_SERIES_NAME, result.getName());
    assertEquals(TEST_START_YEAR, result.getStartYear());
  }
}
