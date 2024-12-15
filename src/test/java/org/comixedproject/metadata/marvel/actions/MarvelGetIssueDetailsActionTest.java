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

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.comixedproject.metadata.MetadataException;
import org.comixedproject.metadata.model.IssueDetailsMetadata;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RunWith(MockitoJUnitRunner.class)
public class MarvelGetIssueDetailsActionTest {
  private static final String TEST_COMIC_ID = "92917";
  private static final String TEST_PUBLIC_KEY = "The.Public.Key";
  private static final String TEST_PRIVATE_KEY = "The.Private.Key";
  private static final String TEST_BAD_RESPONSE = "This is not good data";
  private static final String TEST_GOOD_RESPONSE =
      "{\"code\":200,\"status\":\"Ok\",\"copyright\":\"© 2024 MARVEL\",\"attributionText\":\"Data provided by Marvel. © 2024 MARVEL\",\"attributionHTML\":\"<a href=\\\"http://marvel.com\\\">Data provided by Marvel. © 2024 MARVEL</a>\",\"etag\":\"442cf6ba842678372b90ce41df29eafceb6d8d4a\",\"data\":{\"offset\":0,\"limit\":20,\"total\":1,\"count\":1,\"results\":[{\"id\":97135,\"digitalId\":61869,\"title\":\"Scarlet Witch (2023) #2\",\"issueNumber\":2,\"variantDescription\":\"\",\"description\":\"SCARLET WITCH BATTLES DREAMQUEEN!   Wanda Maximoff is no stranger to grief, so when Viv Vision stumbles through Wanda's door, exhausted and terrified of the nightmares playing her mother's death on repeat, Wanda dives into Viv's dreams to find the cause of the android's suffering. And it turns out Viv isn't alone in her mind… Scarlet Witch faces off against DREAMQUEEN in a reality-bending battle for Viv's freedom! PLUS! This issue includes a special super-heroic back-up story featuring Scarlet Witch and Storm celebrating Black History Month!\",\"modified\":\"2023-08-22T15:57:05-0400\",\"isbn\":\"\",\"upc\":\"75960620235500211\",\"diamondCode\":\"\",\"ean\":\"\",\"issn\":\"\",\"format\":\"Comic\",\"pageCount\":40,\"textObjects\":[],\"resourceURI\":\"http://gateway.marvel.com/v1/public/comics/97135\",\"urls\":[{\"type\":\"detail\",\"url\":\"http://marvel.com/comics/issue/97135/scarlet_witch_2023_2?utm_campaign=apiRef&utm_source=763df8a7c3c0f6d3bb7fcf088bbf6ee1\"},{\"type\":\"purchase\",\"url\":\"http://comicstore.marvel.com/Scarlet-Witch-2/digital-comic/61869?utm_campaign=apiRef&utm_source=763df8a7c3c0f6d3bb7fcf088bbf6ee1\"},{\"type\":\"reader\",\"url\":\"http://marvel.com/digitalcomics/view.htm?iid=61869&utm_campaign=apiRef&utm_source=763df8a7c3c0f6d3bb7fcf088bbf6ee1\"}],\"series\":{\"resourceURI\":\"http://gateway.marvel.com/v1/public/series/33277\",\"name\":\"Scarlet Witch (2023 - Present)\"},\"variants\":[{\"resourceURI\":\"http://gateway.marvel.com/v1/public/comics/106412\",\"name\":\"Scarlet Witch (2023) #2 (Variant)\"},{\"resourceURI\":\"http://gateway.marvel.com/v1/public/comics/106741\",\"name\":\"Scarlet Witch (2023) #2 (Variant)\"},{\"resourceURI\":\"http://gateway.marvel.com/v1/public/comics/107434\",\"name\":\"Scarlet Witch (2023) #2 (Variant)\"},{\"resourceURI\":\"http://gateway.marvel.com/v1/public/comics/107730\",\"name\":\"Scarlet Witch (2023) #2 (Variant)\"}],\"collections\":[],\"collectedIssues\":[],\"dates\":[{\"type\":\"onsaleDate\",\"date\":\"2023-02-01T00:00:00-0500\"},{\"type\":\"focDate\",\"date\":\"2022-12-19T00:00:00-0500\"},{\"type\":\"unlimitedDate\",\"date\":\"2023-01-19T00:00:00-0500\"},{\"type\":\"digitalPurchaseDate\",\"date\":\"2023-01-19T00:00:00-0500\"}],\"prices\":[{\"type\":\"printPrice\",\"price\":4.99}],\"thumbnail\":{\"path\":\"http://i.annihil.us/u/prod/marvel/i/mg/6/30/63d28f429c20a\",\"extension\":\"jpg\"},\"images\":[{\"path\":\"http://i.annihil.us/u/prod/marvel/i/mg/6/30/63d28f429c20a\",\"extension\":\"jpg\"}],\"creators\":{\"available\":9,\"collectionURI\":\"http://gateway.marvel.com/v1/public/comics/97135/creators\",\"items\":[{\"resourceURI\":\"http://gateway.marvel.com/v1/public/creators/12964\",\"name\":\"Christopher Allen\",\"role\":\"inker\"},{\"resourceURI\":\"http://gateway.marvel.com/v1/public/creators/13174\",\"name\":\"Elisabetta D'AMICO\",\"role\":\"inker\"},{\"resourceURI\":\"http://gateway.marvel.com/v1/public/creators/9408\",\"name\":\"Sara Pichelli\",\"role\":\"inker\"},{\"resourceURI\":\"http://gateway.marvel.com/v1/public/creators/12449\",\"name\":\"Russell Dauterman\",\"role\":\"penciler (cover)\"},{\"resourceURI\":\"http://gateway.marvel.com/v1/public/creators/14104\",\"name\":\"Steve Orlando\",\"role\":\"writer\"},{\"resourceURI\":\"http://gateway.marvel.com/v1/public/creators/14153\",\"name\":\"Stephanie Renee Williams\",\"role\":\"writer\"},{\"resourceURI\":\"http://gateway.marvel.com/v1/public/creators/12980\",\"name\":\"Vc Cory Petit\",\"role\":\"letterer\"},{\"resourceURI\":\"http://gateway.marvel.com/v1/public/creators/13046\",\"name\":\"Alanna Smith\",\"role\":\"editor\"},{\"resourceURI\":\"http://gateway.marvel.com/v1/public/creators/10279\",\"name\":\"Matthew Wilson\",\"role\":\"colorist\"}],\"returned\":9},\"characters\":{\"available\":1,\"collectionURI\":\"http://gateway.marvel.com/v1/public/comics/97135/characters\",\"items\":[{\"resourceURI\":\"http://gateway.marvel.com/v1/public/characters/1009562\",\"name\":\"Scarlet Witch\"}],\"returned\":1},\"stories\":{\"available\":2,\"collectionURI\":\"http://gateway.marvel.com/v1/public/comics/97135/stories\",\"items\":[{\"resourceURI\":\"http://gateway.marvel.com/v1/public/stories/215338\",\"name\":\"cover from Scarlet Witch (2022) #2\",\"type\":\"cover\"},{\"resourceURI\":\"http://gateway.marvel.com/v1/public/stories/215339\",\"name\":\"story from Scarlet Witch (2022) #2\",\"type\":\"interiorStory\"}],\"returned\":2},\"events\":{\"available\":0,\"collectionURI\":\"http://gateway.marvel.com/v1/public/comics/97135/events\",\"items\":[],\"returned\":0}}]}}";

  @InjectMocks private MarvelGetIssueDetailsAction action;

  public MockWebServer marvelServer;

  @Before
  public void setUp() throws IOException {
    marvelServer = new MockWebServer();
    marvelServer.start();

    final String hostname = String.format("http://localhost:%s", this.marvelServer.getPort());
    action.setBaseUrl(hostname);
    action.setPublicKey(TEST_PUBLIC_KEY);
    action.setPrivateKey(TEST_PRIVATE_KEY);
    action.setComicId(TEST_COMIC_ID);
  }

  @After
  public void tearDown() throws IOException {
    marvelServer.shutdown();
  }

  @Test(expected = MetadataException.class)
  public void testExecute_noPublicKeyDefined() throws MetadataException {
    action.setPublicKey("");

    action.execute();
  }

  @Test(expected = MetadataException.class)
  public void testExecute_noPrivateKeyDefined() throws MetadataException {
    action.setPrivateKey("");
    action.execute();
  }

  @Test(expected = MetadataException.class)
  public void testExecute_noSeriesDefined() throws MetadataException {
    action.setComicId("");

    action.execute();
  }

  @Test(expected = MetadataException.class)
  public void testExecute_badResponse() throws MetadataException {
    this.marvelServer.enqueue(
        new MockResponse()
            .setBody(TEST_BAD_RESPONSE)
            .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

    action.execute();
  }

  @Test
  public void testExecute() throws MetadataException {
    this.marvelServer.enqueue(
        new MockResponse()
            .setBody(TEST_GOOD_RESPONSE)
            .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

    final IssueDetailsMetadata result = action.execute();

    assertNotNull(result);
  }
}
