package controllers

import com.google.inject.Singleton
import play.api.libs.json.Json
import play.api.mvc._


@Singleton
class HomeController extends Controller {

  def index: Action[AnyContent] = Action {
    Ok(Json.obj("status" -> "ok"))
  }
}
