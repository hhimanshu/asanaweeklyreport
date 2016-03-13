package api

import java.util.Properties
import javax.mail.Message.RecipientType
import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail._

object Email {
  def apply(userName: String, password: String): Email = new Email(userName, password)
}

class Email(userName: String, password: String) {
  private val session = getSession

  def getSession: Session = {
    val props: Properties = new Properties()
    props.put("mail.smtp.host", "smtp.gmail.com")
    props.put("mail.smtp.socketFactory.port", "465")
    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
    props.put("mail.smtp.auth", "true")
    props.put("mail.smtp.port", "465")

    val authenticator: Authenticator = new Authenticator {
      override def getPasswordAuthentication: PasswordAuthentication = new PasswordAuthentication(userName, password)
    }
    Session.getDefaultInstance(props, authenticator)
  }


  def send(to: List[String], subject: String, body: String): Either[String, Unit] = {
    val message: MimeMessage = new MimeMessage(session)
    try {

      message.setFrom(new InternetAddress("minion@for-asana.com", "Asana Weekly"))
      message.setReplyTo(Array(new InternetAddress("no-reply@to-asana.com")))
      message.setSubject(subject)
      message.setContent(body, "text/html; charset=utf-8")

      to.foreach(message.addRecipients(RecipientType.BCC, _))
      Right(Transport.send(message))
    } catch {
      case e: Exception => Left(e.getMessage)
    }
  }
}
