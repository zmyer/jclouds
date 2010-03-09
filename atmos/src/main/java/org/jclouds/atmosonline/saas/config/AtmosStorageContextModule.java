/**
 *
 * Copyright (C) 2009 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */
package org.jclouds.atmosonline.saas.config;

import java.net.URI;

import javax.inject.Named;
import javax.inject.Singleton;

import org.jclouds.atmosonline.saas.AtmosStorage;
import org.jclouds.atmosonline.saas.AtmosStorageAsyncClient;
import org.jclouds.atmosonline.saas.AtmosStorageClient;
import org.jclouds.atmosonline.saas.reference.AtmosStorageConstants;
import org.jclouds.blobstore.config.BlobStoreObjectModule;
import org.jclouds.http.RequiresHttp;
import org.jclouds.lifecycle.Closer;
import org.jclouds.rest.RestContext;
import org.jclouds.rest.internal.RestContextImpl;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;

@RequiresHttp
public class AtmosStorageContextModule extends AbstractModule {

   @Override
   protected void configure() {
      // for converters to work.
      install(new BlobStoreObjectModule<AtmosStorageAsyncClient, AtmosStorageClient>(
               new TypeLiteral<AtmosStorageAsyncClient>() {
               }, new TypeLiteral<AtmosStorageClient>() {
               }));
      install(new AtmosObjectModule());
   }

   @Provides
   @Singleton
   RestContext<AtmosStorageAsyncClient, AtmosStorageClient> provideContext(Closer closer,
            AtmosStorageAsyncClient async, AtmosStorageClient defaultApi,
            @AtmosStorage URI endPoint,
            @Named(AtmosStorageConstants.PROPERTY_EMCSAAS_UID) String account) {
      return new RestContextImpl<AtmosStorageAsyncClient, AtmosStorageClient>(closer, async,
               defaultApi, endPoint, account);
   }

}
