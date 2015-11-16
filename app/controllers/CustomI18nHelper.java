package controllers;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;
import play.Logger;
import play.i18n.Lang;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static java.util.Collections.*;

public class CustomI18nHelper implements Helper<String> {
    public static final String BUNDLE = "messages";
    private final Map<String, Map<String, Object>> languageToFileContentMap = new HashMap<>();

    public CustomI18nHelper(final List<Lang> languages) {
        languages.forEach(language -> {
            final String path = generatePath(language.language());
            try {
                languageToFileContentMap.put(language.language(), loadFileContents(path));
            } catch (IOException e) {
                Logger.error("Failed loading i18n file " + path, e);
            }
        });
    }

    @Override
    public CharSequence apply(final String context, final Options options) throws IOException {
        final String language = options.context.get("locale").toString();
        final String resolvedValue = resolve(language, context);
        return replaceParameters(options, resolvedValue);
    }

    private static String generatePath(final String languageTag) {
        return "META-INF/resources/webjars/locales/" + languageTag + "/" + BUNDLE + ".yaml";
    }

    @Nullable
    private String resolve(final String language, final String key) {
        final Map<String, Object> fileContent = languageToFileContentMap.getOrDefault(language, emptyMap());
        final String[] pathSegments = StringUtils.split(key, '.');
        return resolve(fileContent, pathSegments, 0);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private static String resolve(final Map<String, Object> fileContent, final String[] pathSegments, final int index) {
        return Optional.ofNullable(fileContent.get(pathSegments[index]))
                .map(resolved -> {
                    if (resolved instanceof String) {
                        return (String) resolved;
                    } else if (pathSegments.length == index) {
                        return null;
                    } else if (resolved instanceof Map) {
                        return resolve((Map<String, Object>) resolved, pathSegments, index + 1);
                    } else {
                        return null;
                    }
                }).orElse(null);
    }

    private String replaceParameters(final Options options, final String resolvedValue) {
        String parametersReplaced = StringUtils.defaultString(resolvedValue);
        for (final Map.Entry<String, Object> entry : options.hash.entrySet()) {
            if (entry.getValue() != null) {
                parametersReplaced = parametersReplaced.replace("__" + entry.getKey() + "__", entry.getValue().toString());
            }
        }
        return parametersReplaced;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> loadFileContents(final String path) throws IOException {
        try {
            final InputStream inputStream = getResourceAsStream(path);
            return (Map<String, Object>) new Yaml().loadAs(inputStream, Map.class);
        } catch (final YAMLException e) {
            throw new IOException(e);
        }
    }

    private static InputStream getResourceAsStream(final String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }
}