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

package org.comixedproject.metadata.marvel.auth;

import static junit.framework.TestCase.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.DigestUtils;

@RunWith(MockitoJUnitRunner.class)
public class MarvelAuthorizationAdaptorTest {
  private static final long TEST_TIMESTAMP = System.currentTimeMillis();
  private static final String TEST_PUBLIC_KEY = "public.key";
  private static final String TEST_PRIVATE_KEY = "private.key";

  @InjectMocks private MarvelAuthorizationAdaptor adaptor;

  @Test
  public void testGetHashForRequest() {
    final String result =
        adaptor.getHashForRequest(TEST_TIMESTAMP, TEST_PUBLIC_KEY, TEST_PRIVATE_KEY);

    assertNotNull(result);
    assertFalse(result.isBlank());

    assertEquals(
        DigestUtils.md5DigestAsHex(
            String.format("%d%s%s", TEST_TIMESTAMP, TEST_PUBLIC_KEY, TEST_PRIVATE_KEY).getBytes()),
        result);
  }
}
