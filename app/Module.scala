import java.time.Clock

import com.google.inject.AbstractModule
import com.google.inject.Provides
import handlers.slack.Handler
import handlers.slack.PingHandler
import handlers.slack.PollAdminHandler
import handlers.slack.PollHandler
import handlers.slack.StatusHandler
import helpers.SlackClient
import play.api.Configuration
import play.api.libs.ws.WSClient
import tasks.StartPingTask

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.

 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
class Module extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[StartPingTask]).asEagerSingleton()
  }

  @Provides
  def getClock: Clock = Clock.systemUTC()

  @Provides
  def getHandlers(pingHandler: PingHandler,
                  pollAdminHandler: PollAdminHandler,
                  pollHandler: PollHandler,
                  statusHandler: StatusHandler): Seq[Handler] = Seq(
    pollAdminHandler,
    pollHandler,
    pingHandler,
    statusHandler
  )

  @Provides
  def getSlackClient(conf: Configuration, ws: WSClient): SlackClient = {
    val apiToken = conf.getString("slack.api.token").get
    val postUrl = conf.getString("slack.api.postMessageUrl").get

    new SlackClient(apiToken, postUrl, ws)
  }
}
