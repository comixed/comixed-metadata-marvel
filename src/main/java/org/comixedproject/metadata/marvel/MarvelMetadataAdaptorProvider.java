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

import lombok.extern.log4j.Log4j2;
import org.comixedproject.metadata.AbstractMetadataAdaptorProvider;
import org.comixedproject.metadata.MetadataAdaptorProvider;
import org.comixedproject.metadata.adaptors.MetadataAdaptor;

/**
 * <code>MarvelMetadataAdaptorProvider</code> defines a {@link MetadataAdaptorProvider} for the
 * Marvel metadata service.
 *
 * @author Darryl L. Pierce
 */
@Log4j2
public class MarvelMetadataAdaptorProvider extends AbstractMetadataAdaptorProvider {
  static final String PROVIDER_NAME = "MarvelMetadataAdaptor";
  private static final String VERSION = "0.1.0";
  private static final String HOMEPAGE = "https://github.com/comixed/comixed-metadata-marvel";

  // configuration options
  static final String PROPERTY_PUBLIC_KEY = "marvel.public-key";
  static final String PROPERTY_PRIVATE_KEY = "marvel.private-key";

  public MarvelMetadataAdaptorProvider() {
    super(PROVIDER_NAME, VERSION, HOMEPAGE);

    this.addProperty(PROPERTY_PUBLIC_KEY);
    this.addProperty(PROPERTY_PRIVATE_KEY);
  }

  /**
   * Returns an instance of the adaptor.
   *
   * @return the adaptor
   */
  @Override
  public MetadataAdaptor create() {
    log.debug("Creating an instance of the Marvel metadata adaptor");
    return new MarvelMetadataAdaptor();
  }

  /**
   * Checks if the provided URL is one pointing to Marvel's online service.
   *
   * @param webAddress the web address
   * @return true if it's from Marvel, false otherwise
   */
  @Override
  public boolean supportedReference(final String webAddress) {
    return false;
  }
}
