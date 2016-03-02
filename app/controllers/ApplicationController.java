package controllers;

import handlebars.HandlebarsService;
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
import java.util.Locale;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static play.mvc.Controller.changeLang;
import static play.mvc.Results.ok;

public class ApplicationController {
    private final HandlebarsService handlebarsService;

    public ApplicationController() {
        final List<Locale> locales = Lang.availables().stream().map(Lang::toLocale).collect(toList());
        this.handlebarsService = new HandlebarsService(locales);
    }


    public Result index(final String languageTag) throws IOException{
        changeLang(languageTag);
        final PageData pageData = obtainPageData();
        final String html = handlebarsService.render("index", pageData);
        return ok(new Html(html));
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
