package com.generoso.ft.identity;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.*;

@Slf4j
public class YamlFileApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        List<PropertySource<?>> propertySources = loadPropertySources(applicationContext);
        Map<String, List<PropertySource<?>>> profileToPropertySourcesMap = createProfileToPropertySourcesMap(propertySources);
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        List<PropertySource<?>> commonPropertySources = profileToPropertySourcesMap.get(null);
        Set<PropertySource<?>> activeNonCommonPropertySourcesInPriorityOrder =
                getActiveNonCommonPropertySourcesInPriorityOrder(environment, profileToPropertySourcesMap);

        List<PropertySource<?>> propertySourcesInPriorityOrder = new LinkedList<>();
        propertySourcesInPriorityOrder.addAll(commonPropertySources);
        propertySourcesInPriorityOrder.addAll(activeNonCommonPropertySourcesInPriorityOrder);

        List<PropertySource<?>> propertySourcesInReversePriorityOrder = Lists.reverse(propertySourcesInPriorityOrder);
        propertySourcesInReversePriorityOrder.forEach(propertySource ->
                environment.getPropertySources().addFirst(propertySource));
    }

    private List<PropertySource<?>> loadPropertySources(ConfigurableApplicationContext applicationContext) {
        try {
            String resourceLocation = "classpath:/test.yml";
            Resource resource = applicationContext.getResource(resourceLocation);
            YamlPropertySourceLoader sourceLoader = new YamlPropertySourceLoader();
            return sourceLoader.load(resourceLocation, resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, List<PropertySource<?>>> createProfileToPropertySourcesMap(
            List<PropertySource<?>> propertySources) {
        Map<String, List<PropertySource<?>>> profileToPropertySourcesMap = new HashMap<>();
        propertySources.forEach(propertySource -> getProfiles(propertySource)
                .forEach(profile -> addPropertySource(profileToPropertySourcesMap, propertySource, profile)));
        return profileToPropertySourcesMap;
    }

    private void addPropertySource(Map<String, List<PropertySource<?>>> profileToPropertySourcesMap,
                                   PropertySource<?> propertySource, String profile) {
        profileToPropertySourcesMap.computeIfAbsent(profile,
                k -> new ArrayList<>()).add(propertySource);
    }

    private Iterable<String> getProfiles(PropertySource<?> propertySource) {
        Object profilesObj = propertySource.getProperty("spring.profiles");

        if (profilesObj == null) {
            return Collections.singleton(null);
        }

        return getProfiles(profilesObj);
    }

    private Iterable<String> getProfiles(Object propertySourceValue) {
        if (propertySourceValue instanceof String profilesCsv) {
            String[] profilesArray = profilesCsv.split("\\s*,");
            return Arrays.asList(profilesArray);
        } else if (propertySourceValue instanceof Collection) {
            @SuppressWarnings("unchecked")
            Collection<String> profiles = (Collection<String>) propertySourceValue;
            return profiles;
        }

        throw new UnsupportedOperationException("Unsupported type of spring profiles property source value - "
                + propertySourceValue.getClass());
    }

    private Set<PropertySource<?>> getActiveNonCommonPropertySourcesInPriorityOrder(
            ConfigurableEnvironment environment, Map<String, List<PropertySource<?>>> profileToPropertySourcesMap) {
        String[] activeProfilesArray = environment.getActiveProfiles();
        Iterable<String> activeProfiles = Collections.emptyList();

        if (activeProfilesArray.length != 0) {
            activeProfiles = Arrays.asList(activeProfilesArray);
        } else {
            List<PropertySource<?>> commonPropertySources = profileToPropertySourcesMap.get(null);
            Optional<Object> commonPropertySourcesActiveProfilesOptional = commonPropertySources.stream()
                    .map(ps -> ps.getProperty("spring.profiles.active")).filter(Objects::nonNull)
                    .findFirst();

            if (commonPropertySourcesActiveProfilesOptional.isPresent()) {
                Object commonPropertySourcesActiveProfilesObj = commonPropertySourcesActiveProfilesOptional.get();
                activeProfiles = getProfiles(commonPropertySourcesActiveProfilesObj);
                log.warn("no active profiles have explicitly set, so using the common PropertySource(s) active profile(s) {}", activeProfiles);
            }
        }

        Set<PropertySource<?>> activeNonCommonPropertySourcesInPriorityOrder = new LinkedHashSet<>();
        for (String activeProfile : activeProfiles) {
            List<PropertySource<?>> activeProfilePropertySources = profileToPropertySourcesMap.get(activeProfile);
            if (activeProfilePropertySources != null) {
                activeNonCommonPropertySourcesInPriorityOrder.addAll(activeProfilePropertySources);
            }
        }

        return activeNonCommonPropertySourcesInPriorityOrder;
    }
}
