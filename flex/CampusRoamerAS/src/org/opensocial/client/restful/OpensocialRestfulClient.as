/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.opensocial.client.restful {

import flash.events.EventDispatcher;

import org.opensocial.client.base.*;
import org.opensocial.client.util.*;

/**
 * RESTful client is for the flash app which is running on the iframe which serving from app's own 
 * server. So the flash swf object can talk to the app server in secure way by constrict security
 * policy (e.g. Security.allowDomain("your-app-server.com")). So the swf can receive keys from the
 * app server and talk to the container api endpoint via RESTful format.
 * 
 * <p>
 * This package only contains the classes related to RESTful framework, which is parallel to the
 * javascript wrapper client in the package <code>org.opensocial.client.jswrapper</code>.
 * </p>
 * <p>
 * Those common basic data types are all in <code>org.opensocial.client.base</code> package, and
 * utilily classes are <code>org.opensocial.client.util</code> package. Both javascript wrapper 
 * client and this restful client will share them.
 * </p>
 */
public class OpensocialRestfulClient extends EventDispatcher {
  // Not implemented yet.
}

}
