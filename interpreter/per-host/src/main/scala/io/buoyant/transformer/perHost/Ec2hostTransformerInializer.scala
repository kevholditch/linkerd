package io.buoyant.transformer.perHost

import java.net.InetAddress

import io.buoyant.namer.{NameTreeTransformer, TransformerConfig, TransformerInitializer}
import io.buoyant.transformer.{Netmask, SubnetLocalTransformer}
//import com.twitter.finagle.{ConnectionFailedException, Failure, Filter, http}

class Ec2HostTransformerInializer extends TransformerInitializer {
  val configClass = classOf[SpecificHostTransformerConfig]
  override val configId = "io.l5d.ec2host"
}

class Ec2HostTransformerConfig() extends TransformerConfig {

  //val localIpAddress = Http("http://169.254.169.254/latest/meta-data/local-ipv4").asString

 // @JsonIgnore
  override def mk(): NameTreeTransformer = {
    new SubnetLocalTransformer(prefix, Seq(InetAddress.getByName("")), Netmask("255.255.255.255"))
  }

  override def defaultPrefix = ""
}