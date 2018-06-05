/*
DLM2.scala

A DLM with AR(1) latent state and Gaussian observations

 */

import com.stripe.rainier.compute._
import com.stripe.rainier.core._
import com.stripe.rainier.sampler._
import com.stripe.rainier.repl._

object DLM2 {

  def main(args: Array[String]): Unit = {

    // first simulate some data from a DLM model
    implicit val rng = ScalaRNG(4)
    val n = 500 // time points
    val mu = 3.0 // AR(1) mean
    val a = 0.95 // auto-regressive parameter
    val sig = 1.0 // AR(1) SD
    val sigD = 3.0 // observational SD
    val state = Stream
      .iterate(0.0)(x => mu + (x - mu) * a + sig * rng.standardNormal)
      .take(n)
      .toVector
    val obs = state.map(_ + sigD * rng.standardNormal)

    case class Static(mu: Real, a: Real, sig: Real, sigD: Real)

    // build and fit model
    val prior = for {
      mu <- Normal(5, 10).param
      a <- Normal(1, 0.2).param
      sig <- LogNormal(0,2).param
      sigD <- LogNormal(1,4).param
      sp <- Normal(0, 50).param
    } yield (Static(mu, a, sig, sigD), List(sp))

    def addTimePoint(current: RandomVariable[(Static, List[Real])], i: Int): RandomVariable[(Static, List[Real])] = for {
      tup <- current
      static = tup._1
      states = tup._2
      os = states.head
      ns <- Normal(static.mu + static.a * (os - static.mu), static.sig).param
      _ <- Normal(ns, static.sigD).fit(obs(i))
    } yield (static, ns :: states)

    val fullModel = (0 until n).foldLeft(prior)(addTimePoint(_, _))

    val model = for {
      tup <- fullModel
      static = tup._1
      states = tup._2
    } yield Map("mu"->static.mu, "a"->static.a,
      "sig"->static.sig, "sigD"->static.sigD,
      "SP"->states.reverse.head)

    // sampling
    println("Model built. Sampling now (will take a long time)...")
    val thin=200
    val out = model.sample(HMC(5), 100000, 10000*thin,thin)
    println("Sampling finished.")

    // some diagnostic plots
    import com.cibo.evilplot.geometry.Extent
    import com.stripe.rainier.plot.EvilTracePlot._

    render(traces(out, truth = Map("mu" -> mu, "a"-> a,
      "sigD" -> sigD, "sig" -> sig, "SP"->state(0))),
           "traceplots.png",
           Extent(1200, 1400))
    render(pairs(out, truth = Map("mu" -> mu, "a"-> a,
      "sigD" -> sigD, "sig" -> sig, "SP"->state(0))),
           "pairs.png")
    println("Diagnostic plots written to disk")

  }

}

// eof
