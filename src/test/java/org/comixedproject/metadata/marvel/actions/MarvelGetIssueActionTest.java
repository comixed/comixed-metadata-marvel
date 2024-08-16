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

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.comixedproject.metadata.MetadataException;
import org.comixedproject.metadata.model.IssueMetadata;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RunWith(MockitoJUnitRunner.class)
public class MarvelGetIssueActionTest {
  private static final String TEST_SERIES = "spider-man";
  private static final String TEST_ISSUE_NUMBER = "17";
  private static final String TEST_PUBLIC_KEY = "The.Public.Key";
  private static final String TEST_PRIVATE_KEY = "The.Private.Key";
  private static final String TEST_BAD_RESPONSE = "This is not good data";
  private static final String TEST_GOOD_RESPONSE =
      "{\"code\":200,\"status\":\"Ok\",\"copyright\":\"© 2024 MARVEL\",\"attributionText\":\"Data provided by Marvel. © 2024 MARVEL\",\"attributionHTML\":\"<a href=\\\"http://marvel.com\\\">Data provided by Marvel. © 2024 MARVEL</a>\",\"etag\":\"1b3aa81268ab62201e0dce97abfe9cf498fae08c\",\"data\":{\"offset\":0,\"limit\":20,\"total\":2,\"count\":2,\"results\":[{\"id\":10766,\"digitalId\":0,\"title\":\"Spider-Man (1990) #1\",\"issueNumber\":1,\"variantDescription\":\"\",\"description\":\"\",\"modified\":\"2019-10-14T19:59:57-0400\",\"isbn\":\"\",\"upc\":\"\",\"diamondCode\":\"\",\"ean\":\"\",\"issn\":\"\",\"format\":\"Comic\",\"pageCount\":0,\"textObjects\":[],\"resourceURI\":\"http://gateway.marvel.com/v1/public/comics/10766\",\"urls\":[{\"type\":\"detail\",\"url\":\"http://marvel.com/comics/issue/10766/spider-man_1990_1?utm_campaign=apiRef&utm_source=763df8a7c3c0f6d3bb7fcf088bbf6ee1\"}],\"series\":{\"resourceURI\":\"http://gateway.marvel.com/v1/public/series/2069\",\"name\":\"Spider-Man (1990 - 1998)\"},\"variants\":[{\"resourceURI\":\"http://gateway.marvel.com/v1/public/comics/10767\",\"name\":\"Spider-Man (1990) #1\"}],\"collections\":[],\"collectedIssues\":[],\"dates\":[{\"type\":\"onsaleDate\",\"date\":\"1997-07-01T00:00:00-0400\"},{\"type\":\"focDate\",\"date\":\"-0001-11-30T00:00:00-0500\"}],\"prices\":[{\"type\":\"printPrice\",\"price\":0}],\"thumbnail\":{\"path\":\"http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available\",\"extension\":\"jpg\"},\"images\":[],\"creators\":{\"available\":0,\"collectionURI\":\"http://gateway.marvel.com/v1/public/comics/10766/creators\",\"items\":[],\"returned\":0},\"characters\":{\"available\":2,\"collectionURI\":\"http://gateway.marvel.com/v1/public/comics/10766/characters\",\"items\":[{\"resourceURI\":\"http://gateway.marvel.com/v1/public/characters/1009325\",\"name\":\"Norman Osborn\"},{\"resourceURI\":\"http://gateway.marvel.com/v1/public/characters/1009610\",\"name\":\"Spider-Man (Peter Parker)\"}],\"returned\":2},\"stories\":{\"available\":5,\"collectionURI\":\"http://gateway.marvel.com/v1/public/comics/10766/stories\",\"items\":[{\"resourceURI\":\"http://gateway.marvel.com/v1/public/stories/23741\",\"name\":\"Cover #23741\",\"type\":\"cover\"},{\"resourceURI\":\"http://gateway.marvel.com/v1/public/stories/23742\",\"name\":\"A Prelude in Red\",\"type\":\"interiorStory\"},{\"resourceURI\":\"http://gateway.marvel.com/v1/public/stories/23743\",\"name\":\"Post It to Peter!\",\"type\":\"letters\"},{\"resourceURI\":\"http://gateway.marvel.com/v1/public/stories/178907\",\"name\":\"cover #-1\",\"type\":\"cover\"},{\"resourceURI\":\"http://gateway.marvel.com/v1/public/stories/178908\",\"name\":\"story #-1\",\"type\":\"interiorStory\"}],\"returned\":5},\"events\":{\"available\":0,\"collectionURI\":\"http://gateway.marvel.com/v1/public/comics/10766/events\",\"items\":[],\"returned\":0}},{\"id\":10767,\"digitalId\":2515,\"title\":\"Spider-Man (1990) #1\",\"issueNumber\":1,\"variantDescription\":\"\",\"description\":\"Torment Part 1.  Writer/Artist Todd McFarlane puts his superstar stamp on Spider-Man with this classic story.  The Lizard has been revived by magic.  And now the perfect killing machine is killing once again.  As the bodies pile up, Spider-Man will have to investigate just who is terrorizing the city.\",\"modified\":\"2018-09-14T13:19:39-0400\",\"isbn\":\"\",\"upc\":\"\",\"diamondCode\":\"\",\"ean\":\"\",\"issn\":\"\",\"format\":\"Comic\",\"pageCount\":32,\"textObjects\":[{\"type\":\"issue_preview_text\",\"language\":\"en-us\",\"text\":\"TORMENT PART ONE The Lizard is back and he's looking for blood! Will Spidey be able to fend off his old foe?\"},{\"type\":\"issue_solicit_text\",\"language\":\"en-us\",\"text\":\"TORMENT PART ONE The Lizard is back and he's looking for blood! Will Spidey be able to fend off his old foe?\"},{\"type\":\"70th_winner_desc\",\"language\":\"en-us\",\"text\":\"The premier issue of Todd McFarlane's solo run with the webcrawler looks so sticky we may not be able to put it down!\"}],\"resourceURI\":\"http://gateway.marvel.com/v1/public/comics/10767\",\"urls\":[{\"type\":\"detail\",\"url\":\"http://marvel.com/comics/issue/10767/spider-man_1990_1?utm_campaign=apiRef&utm_source=763df8a7c3c0f6d3bb7fcf088bbf6ee1\"},{\"type\":\"purchase\",\"url\":\"http://comicstore.marvel.com/Spider-Man-1/digital-comic/2515?utm_campaign=apiRef&utm_source=763df8a7c3c0f6d3bb7fcf088bbf6ee1\"},{\"type\":\"reader\",\"url\":\"http://marvel.com/digitalcomics/view.htm?iid=2515&utm_campaign=apiRef&utm_source=763df8a7c3c0f6d3bb7fcf088bbf6ee1\"},{\"type\":\"inAppLink\",\"url\":\"https://applink.marvel.com/issue/2515?utm_campaign=apiRef&utm_source=763df8a7c3c0f6d3bb7fcf088bbf6ee1\"}],\"series\":{\"resourceURI\":\"http://gateway.marvel.com/v1/public/series/2069\",\"name\":\"Spider-Man (1990 - 1998)\"},\"variants\":[{\"resourceURI\":\"http://gateway.marvel.com/v1/public/comics/10766\",\"name\":\"Spider-Man (1990) #1\"}],\"collections\":[],\"collectedIssues\":[],\"dates\":[{\"type\":\"onsaleDate\",\"date\":\"1990-08-01T00:00:00-0400\"},{\"type\":\"focDate\",\"date\":\"-0001-11-30T00:00:00-0500\"},{\"type\":\"unlimitedDate\",\"date\":\"2007-11-13T00:00:00-0500\"},{\"type\":\"digitalPurchaseDate\",\"date\":\"2011-07-13T00:00:00-0400\"}],\"prices\":[{\"type\":\"printPrice\",\"price\":0},{\"type\":\"digitalPurchasePrice\",\"price\":1.99}],\"thumbnail\":{\"path\":\"http://i.annihil.us/u/prod/marvel/i/mg/6/e0/5bc76088e860c\",\"extension\":\"jpg\"},\"images\":[{\"path\":\"http://i.annihil.us/u/prod/marvel/i/mg/6/e0/5bc76088e860c\",\"extension\":\"jpg\"},{\"path\":\"http://i.annihil.us/u/prod/marvel/i/mg/f/e0/58c9aceed68a3\",\"extension\":\"jpg\"},{\"path\":\"http://i.annihil.us/u/prod/marvel/i/mg/9/90/4bc35ef309c76\",\"extension\":\"jpg\"}],\"creators\":{\"available\":3,\"collectionURI\":\"http://gateway.marvel.com/v1/public/comics/10767/creators\",\"items\":[{\"resourceURI\":\"http://gateway.marvel.com/v1/public/creators/75\",\"name\":\"Todd Mcfarlane\",\"role\":\"inker\"},{\"resourceURI\":\"http://gateway.marvel.com/v1/public/creators/1819\",\"name\":\"Rick Parker\",\"role\":\"letterer\"},{\"resourceURI\":\"http://gateway.marvel.com/v1/public/creators/1832\",\"name\":\"Bob Sharen\",\"role\":\"colorist\"}],\"returned\":3},\"characters\":{\"available\":1,\"collectionURI\":\"http://gateway.marvel.com/v1/public/comics/10767/characters\",\"items\":[{\"resourceURI\":\"http://gateway.marvel.com/v1/public/characters/1009610\",\"name\":\"Spider-Man (Peter Parker)\"}],\"returned\":1},\"stories\":{\"available\":3,\"collectionURI\":\"http://gateway.marvel.com/v1/public/comics/10767/stories\",\"items\":[{\"resourceURI\":\"http://gateway.marvel.com/v1/public/stories/23744\",\"name\":\" Spider-Man (1990) #1\",\"type\":\"cover\"},{\"resourceURI\":\"http://gateway.marvel.com/v1/public/stories/23745\",\"name\":\"Torment Part 1\",\"type\":\"interiorStory\"},{\"resourceURI\":\"http://gateway.marvel.com/v1/public/stories/206106\",\"name\":\"story from Spider-Man (1990) #1\",\"type\":\"interiorStory\"}],\"returned\":3},\"events\":{\"available\":0,\"collectionURI\":\"http://gateway.marvel.com/v1/public/comics/10767/events\",\"items\":[],\"returned\":0}}]}}";

  @InjectMocks private MarvelGetIssueAction action;
  public MockWebServer marvelServer;

  @Before
  public void setUp() throws IOException {
    marvelServer = new MockWebServer();
    marvelServer.start();

    final String hostname = String.format("http://localhost:%s", this.marvelServer.getPort());
    action.setBaseUrl(hostname);
    action.setPublicKey(TEST_PUBLIC_KEY);
    action.setPrivateKey(TEST_PRIVATE_KEY);
    action.setSeries(TEST_SERIES);
    action.setIssueNumber(TEST_ISSUE_NUMBER);
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
    action.setSeries("");

    action.execute();
  }

  @Test(expected = MetadataException.class)
  public void testExecute_noIssueNumberDefined() throws MetadataException {
    action.setIssueNumber("");

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

    final List<IssueMetadata> result = action.execute();

    assertNotNull(result);
    assertFalse(result.isEmpty());
  }
}
