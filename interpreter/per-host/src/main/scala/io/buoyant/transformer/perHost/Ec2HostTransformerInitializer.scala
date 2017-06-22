package io.buoyant.transformer.perHost

import com.fasterxml.jackson.annotation.JsonIgnore
import java.net.InetAddress

import io.buoyant.namer.{NameTreeTransformer, TransformerConfig, TransformerInitializer}
import io.buoyant.transformer.{Netmask, SubnetLocalTransformer}
import com.twitter.finagle.{Http, Path, Service, http}
import com.twitter.logging.Logger
import com.twitter.util.Await

class Ec2HostTransformerInitializer extends TransformerInitializer {
  val configClass = classOf[Ec2HostTransformerConfig]
  override val configId = "io.l5d.ec2host"
}

class Ec2HostTransformerConfig() extends TransformerConfig {

  //val localIpAddress = com.twitter.finagle.http.get("http://169.254.169.254/latest/meta-data/local-ipv4").asString
  val client: Service[http.Request, http.Response] = Http.newService("http://169.254.169.254")
  val request = http.Request(http.Method.Get, "/latest/meta-data/local-ipv4")
  val localIpAddress = Await.result(client(request)).contentString;
  val log = Logger.get("Ec2HostTransformerConfig")

  @JsonIgnore
  val defaultPrefix = Path.read("/io.l5d.ec2host")

  @JsonIgnore
  override def mk(): NameTreeTransformer = {
    new SubnetLocalTransformer(prefix, Seq(InetAddress.getByName(localIpAddress)), Netmask("255.255.255.255"))
  }

}