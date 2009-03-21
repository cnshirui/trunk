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

package org.opensocial.client.base {

/**
 * Wrapper of <code><j>opensocial.Person</j></code> object in javascript.
 * 
 * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.Person
 *      opensocial.Person
 * 
 * @author yiziwu@google.com (Yizi Wu)
 */
public class Person extends DataType {

  /**
   * <code><j>opensocial.Person.Field</j></code> constants.
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.Person.Field
   *      opensocial.Person.Field
   */ 
  public static const Field:ConstType = new ConstType(
      "opensocial.Person.Field", {
          ID                      : 'id',
          NAME                    : 'name',
          NICKNAME                : 'nickname',
          THUMBNAIL_URL           : 'thumbnailUrl',
          PROFILE_URL             : 'profileUrl',
          CURRENT_LOCATION        : 'currentLocation',          /* Address */
          ADDRESSES               : 'addresses',                /* Array.<Address> */
          EMAILS                  : 'emails',                   /* Array.<Email> */
          PHONE_NUMBERS           : 'phoneNumbers',             /* Array.<Phone> */
          ABOUT_ME                : 'aboutMe',
          STATUS                  : 'status',
          PROFILE_SONG            : 'profileSong',              /* Url */
          PROFILE_VIDEO           : 'profileVideo',             /* Url */
          GENDER                  : 'gender',                   /* Enum.Gender */
          SEXUAL_ORIENTATION      : 'sexualOrientation',
          RELATIONSHIP_STATUS     : 'relationshipStatus',
          AGE                     : 'age',                      /* Number */
          DATE_OF_BIRTH           : 'dateOfBirth',              /* Date */
          BODY_TYPE               : 'bodyType',                 /* BodyType */
          ETHNICITY               : 'ethnicity',
          SMOKER                  : 'smoker',                   /* Enum.Smoker */
          DRINKER                 : 'drinker',                  /* Enum.Drinker */
          CHILDREN                : 'children',
          PETS                    : 'pets',
          LIVING_ARRANGEMENT      : 'livingArrangement',
          TIME_ZONE               : 'timeZone',                 /* Number */
          LANGUAGES_SPOKEN        : 'languagesSpoken',          /* Array.<String> */
          JOBS                    : 'jobs',                     /* Array.<Orgnaization> */
          JOB_INTERESTS           : 'jobInterests',
          SCHOOLS                 : 'schools',                  /* Array.<Orgnaization> */
          INTERESTS               : 'interests',                /* Array.<String> */
          URLS                    : 'urls',                     /* Array.<Url> */
          MUSIC                   : 'music',                    /* Array.<String> */
          MOVIES                  : 'movies',                   /* Array.<String> */
          TV_SHOWS                : 'tvShows',                  /* Array.<String> */
          BOOKS                   : 'books',                    /* Array.<String> */
          ACTIVITIES              : 'activities',               /* Array.<String> */
          SPORTS                  : 'sports',                   /* Array.<String> */
          HEROES                  : 'heroes',                   /* Array.<String> */
          QUOTES                  : 'quotes',                   /* Array.<String> */
          CARS                    : 'cars',                     /* Array.<String> */
          FOOD                    : 'food',                     /* Array.<String> */
          TURN_ONS                : 'turnOns',                  /* Array.<String> */
          TURN_OFFS               : 'turnOffs',                 /* Array.<String> */
          TAGS                    : 'tags',                     /* Array.<String> */
          ROMANCE                 : 'romance',
          SCARED_OF               : 'scaredOf',
          HAPPIEST_WHEN           : 'happiestWhen',
          FASHION                 : 'fashion',
          HUMOR                   : 'humor',
          LOOKING_FOR             : 'lookingFor',               /* Array.<Enum.LookingFor> */
          RELIGION                : 'religion',
          POLITICAL_VIEWS         : 'politicalViews',
          HAS_APP                 : 'hasApp',                   /* Boolean */
          NETWORK_PRESENCE        : 'networkPresence'           /* Enum.Presence */
      });
  
  /**
   * Constructor.
   * <p>
   * NOTE: This constructor is internally used. You should not call this constructor directly 
   * outside this package.
   * </p>
   * @param rawObj The wrapped object from Js-side passed by the <code>ExternalInterface</code>.
   */
  public function Person(rawObj:Object) {
    super(rawObj);
  }

  /**
   * Gets display name for this person, guaranteed to return a useful string.
   * @return The display name.
   */
  public function getDisplayName():String {
    return getRawProperty("displayName") as String;
  }

  /**
   * Gets ID that can be permanently associated with this person.
   * @return The id string.
   */
  public function getId():String {
    return getRawProperty('id') as String;
  }

  /**
   * True if this person object represents the owner of current page. 
   * @return True If this person is owner.
   */
  public function isOwner():Boolean {
    return getRawProperty("isOwner") as Boolean;
  }

  /**
   * True if this person object represents the currently logged in user
   * @return True if this person is viewer.
   */
  public function isViewer():Boolean {
    return getRawProperty("isViewer") as Boolean;
  }

  /**
   * Returns the default person display string.
   * @return The person name by default.
   */
  override public function toString():String {
    //TODO: may not be appropriate.
    return getDisplayName();
  }
}
}
 