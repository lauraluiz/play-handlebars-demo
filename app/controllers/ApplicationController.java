package controllers;

import com.github.jknack.handlebars.*;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import handlebars.NonCachedJavaBeanValueResolver;
import models.Cart;
import models.LineItem;
import models.PageData;
import models.User;
import play.i18n.Lang;
import play.i18n.Messages;
import play.mvc.Result;
import play.twirl.api.Html;

import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static play.mvc.Controller.changeLang;
import static play.mvc.Controller.lang;
import static play.mvc.Results.ok;

public class ApplicationController {
    private final Handlebars handlebars;

    public ApplicationController() {
        final ClassPathTemplateLoader webjarsPath = new ClassPathTemplateLoader("/META-INF/resources/webjars/templates");
        final ClassPathTemplateLoader overridePath = new ClassPathTemplateLoader("/templates");
        this.handlebars = new Handlebars()
                .with(overridePath, webjarsPath)
                .registerHelper("i18n", new CustomI18nHelper(Lang.availables()));
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
        final Context context = Context.newBuilder(pageData)
                .resolver(NonCachedJavaBeanValueResolver.INSTANCE, MapValueResolver.INSTANCE)
                .build();
        context.data("locale", lang().language());
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
