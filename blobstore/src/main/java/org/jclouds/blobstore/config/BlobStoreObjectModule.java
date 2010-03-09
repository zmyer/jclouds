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
package org.jclouds.blobstore.config;

import javax.inject.Inject;
import javax.inject.Provider;

import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.domain.MutableBlobMetadata;
import org.jclouds.blobstore.domain.internal.BlobImpl;
import org.jclouds.encryption.EncryptionService;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;

/**
 * Configures the domain object mappings needed for all Blob implementations
 * 
 * @author Adrian Cole
 */
public class BlobStoreObjectModule<A, S> extends AbstractModule {
   protected final TypeLiteral<A> asyncClientType;
   protected final TypeLiteral<S> syncClientType;

   public BlobStoreObjectModule(TypeLiteral<A> asyncClientType, TypeLiteral<S> syncClientType) {
      // type erasure makes looking up a concrete impl that has externally defined type variables a
      // pain
      this.asyncClientType = asyncClientType;
      this.syncClientType = syncClientType;
   }

   /**
    * explicit factories are created here as it has been shown that Assisted Inject is extremely
    * inefficient. http://code.google.com/p/google-guice/issues/detail?id=435
    */
   @Override
   protected void configure() {
      bind(Blob.Factory.class).to(BlobFactory.class).in(Scopes.SINGLETON);
   }

   private static class BlobFactory implements Blob.Factory {
      @Inject
      EncryptionService encryptionService;
      @Inject
      Provider<MutableBlobMetadata> metadataProvider;

      public Blob create(MutableBlobMetadata metadata) {
         return new BlobImpl(encryptionService, metadata != null ? metadata : metadataProvider
                  .get());
      }
   }

   @Provides
   Blob provideBlob(Blob.Factory factory) {
      return factory.create(null);
   }

}