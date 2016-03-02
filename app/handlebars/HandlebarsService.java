package handlebars;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.cache.HighConcurrencyTemplateCache;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.CompositeTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import i18n.CompositeI18nResolver;
import i18n.I18nResolver;
import i18n.YamlI18nResolver;
import models.PageData;
import play.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.util.Collections.singletonList;
import static play.mvc.Controller.lang;

public class HandlebarsService {
    private final Handlebars handlebars;

    public HandlebarsService(final List<Locale> locales) {
        this.handlebars = new Handlebars()
                .with(templateLoader())
                .with(new HighConcurrencyTemplateCache())
                .registerHelper("i18n", i18nHelper(locales));
    }

    public String render(final String templateName, final PageData pageData) {
        try {
            final Template template = handlebars.compile(templateName);
            final Context context = buildContext(pageData);
            return template.apply(context);
        } catch (IOException e) {
            Logger.error("Could not render template '{}'", templateName, e);
            throw new RuntimeException(e);
        }
    }

    private Context buildContext(final PageData pageData) {
        final Context context = Context.newContext(pageData);
        context.data("locales", singletonList(lang().language()));
        return context;
    }

    private static TemplateLoader templateLoader() {
        final List<TemplateLoader> templateLoaders = new ArrayList<>();
        templateLoaders.add(new ClassPathTemplateLoader("/templates")); //overrides
        templateLoaders.add(new ClassPathTemplateLoader("/META-INF/resources/webjars/templates")); //webjar
        return new CompositeTemplateLoader(templateLoaders.toArray(new TemplateLoader[templateLoaders.size()]));
    }

    private static CustomI18nHelper i18nHelper(final List<Locale> locales) {
        final List<String> bundles = singletonList("main");
        final List<I18nResolver> i18nResolvers = new ArrayList<>();
        i18nResolvers.add(YamlI18nResolver.of("i18n", locales, bundles)); //overrides
        i18nResolvers.add(YamlI18nResolver.of("META-INF/resources/webjars/i18n", locales, bundles)); //webjar
        return new CustomI18nHelper(CompositeI18nResolver.of(i18nResolvers));
    }
}
