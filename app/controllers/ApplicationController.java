package controllers;

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
import models.Cart;
import models.LineItem;
import models.PageData;
import models.User;
import play.i18n.Lang;
import play.i18n.Messages;
import play.mvc.Result;
import play.twirl.api.Html;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static play.mvc.Controller.changeLang;
import static play.mvc.Controller.lang;
import static play.mvc.Results.ok;

public class ApplicationController {
    private final Handlebars handlebars;

    public ApplicationController() {
        this.handlebars = new Handlebars()
                .with(templateLoader())
                .with(new HighConcurrencyTemplateCache())
                .registerHelper("i18n", i18nHelper());
    }

    private TemplateLoader templateLoader() {
        final List<TemplateLoader> templateLoaders = new ArrayList<>();
        templateLoaders.add(new ClassPathTemplateLoader("/templates")); //overrides
        templateLoaders.add(new ClassPathTemplateLoader("/META-INF/resources/webjars/templates")); //webjar
        return new CompositeTemplateLoader(templateLoaders.toArray(new TemplateLoader[templateLoaders.size()]));
    }

    public CustomI18nHelper i18nHelper() {
        final List<String> bundles = singletonList("main");
        final List<Locale> locales = Lang.availables().stream().map(Lang::toLocale).collect(toList());
        final List<I18nResolver> i18nResolvers = new ArrayList<>();
        i18nResolvers.add(YamlI18nResolver.of("i18n", locales, bundles)); //overrides
        i18nResolvers.add(YamlI18nResolver.of("META-INF/resources/webjars/i18n", locales, bundles)); //webjar
        return new CustomI18nHelper(CompositeI18nResolver.of(i18nResolvers));
    }




    public Result index(final String languageTag) throws IOException{
        changeLang(languageTag);
        final PageData pageData = obtainPageData();

        final Template template = handlebars.compile("index");
        final Context context = buildContext(pageData);
        final String html = template.apply(context);

        return ok(new Html(html));
    }



    private Context buildContext(final PageData pageData) {
        final Context context = Context.newContext(pageData);
        context.data("locales", singletonList(lang().language()));
        return context;
    }



    private PageData obtainPageData() {
        final List<LineItem> lineItems = asList(lineItem("milk", 5), lineItem("eggs", 12), lineItem("vodka", 1));
        return obtainPageData(lineItems);
    }

    private PageData obtainPageDataWithEmptyCart() {
        return obtainPageData(emptyList());
    }

    private PageData obtainPageData(final List<LineItem> lineItems) {
        final PageData pageData = new PageData();
        pageData.setUser(new User("Jane Doe"));
        pageData.setCart(new Cart(lineItems));
        return pageData;
    }

    private LineItem lineItem(final String name, final int quantity) {
        return new LineItem(Messages.get(name), quantity, "assets/img/" + name + ".jpg");
    }
}
