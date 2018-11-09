package controllers;

import javax.inject.Inject;

import play.Configuration;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class HomeController extends Controller {

	@Inject
	private Configuration configuration;

	public Result index() {

		return ok(index.render("Your new application is ready!!!"));
	}

}
