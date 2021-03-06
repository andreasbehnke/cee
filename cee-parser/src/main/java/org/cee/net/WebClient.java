package org.cee.net;

/*
 * #%L
 * Content Extraction Engine - News Parser
 * %%
 * Copyright (C) 2013 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.net.URL;
import java.util.concurrent.Future;
import java.util.function.Function;

/**
 * Provides access to web resources. A web client manages connection state, this
 * includes authentication and cookies of HTTP connections.
 */
public interface WebClient {
    
    boolean isSupported(URL location);
    
    WebResponse openWebResponse(URL location, boolean bufferStream);
    
    <T> Future<T> processWebResponse(URL location, boolean bufferStream, Function<WebResponse, T> responseProcessor);
    
    void shutdown();
}