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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import com.scoperetail.fusion.messaging.config.Adapter.AdapterType;
import lombok.Data;

@Component
@ConfigurationProperties(prefix = "fusion")
@Data
public class FusionConfig {
  private final List<Broker> brokers = new ArrayList<>();
  private RestRetryPolicy restRetryPolicy;
  private final List<UseCaseConfig> usecases = new ArrayList<>();
  private Credentials credentials;
  private final List<MailHost> mailHosts = new ArrayList<>();

  private Map<String, Broker> brokersByBrokerIdMap = new HashMap<>(1);
  private Map<String, UseCaseConfig> usecasesByNameMap = new HashMap<>(1);
  private Map<String, Optional<Config>> activeConfigByNameMap = new HashMap<>(1);
  private Map<String, Optional<Adapter>> inboundAdapterByNameMap = new HashMap<>(1);

  @PostConstruct
  public void init() {
    brokersByBrokerIdMap =
        brokers.stream().collect(Collectors.toMap(Broker::getBrokerId, Function.identity()));
    usecasesByNameMap =
        usecases.stream().collect(Collectors.toMap(UseCaseConfig::getName, Function.identity()));
    activeConfigByNameMap =
        usecases
            .stream()
            .collect(
                Collectors.toMap(
                    UseCaseConfig::getName,
                    usecase ->
                        usecase
                            .getConfigs()
                            .stream()
                            .filter(config -> config.getName().equals(usecase.getActiveConfig()))
                            .findFirst()));
    inboundAdapterByNameMap =
        activeConfigByNameMap
            .entrySet()
            .stream()
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey,
                    entry ->
                        entry
                            .getValue()
                            .flatMap(
                                config ->
                                    config
                                        .getAdapters()
                                        .stream()
                                        .filter(
                                            adapter ->
                                                adapter
                                                    .getAdapterType()
                                                    .equals(AdapterType.INBOUND))
                                        .findFirst())));
  }

  public Optional<Broker> getBroker(final String brokerId) {
    return Optional.ofNullable(brokersByBrokerIdMap.get(brokerId));
  }

  public Optional<UseCaseConfig> getUsecase(final String usecaseName) {
    return Optional.ofNullable(usecasesByNameMap.get(usecaseName));
  }

  public Optional<Config> getActiveConfig(final String usecaseName) {
    return activeConfigByNameMap.get(usecaseName);
  }

  public Optional<Adapter> getInboundAdapter(final String usecaseName) {
    return inboundAdapterByNameMap.get(usecaseName);
  }
}
