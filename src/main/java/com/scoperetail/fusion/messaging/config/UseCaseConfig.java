/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.messaging.config;

/*-
 * *****
 * fusion-messaging
 * -----
 * Copyright (C) 2018 - 2021 Scope Retail Systems Inc.
 * -----
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
 * =====
 */

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class UseCaseConfig {
  private String activeConfig;
  private final List<Config> configs = new ArrayList<>();
  private String name;
  private String version;
  private HashKeyTemplate hashKeyTemplate;
  private Boolean dedupeCheck;

  public enum HashKeyTemplate {
    DOMAIN_EVENT_FTL_TRANSFORMER, DOMAIN_EVENT_VELOCITY_TRANSFORMER
  }
}
