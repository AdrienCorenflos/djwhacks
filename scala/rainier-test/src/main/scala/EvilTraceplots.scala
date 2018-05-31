/*
EvilTraceplots.scala

Some diagnostic plots using EvilPlot

 */


object EvilTraceplots {

  import com.cibo.evilplot.plot._
  import com.cibo.evilplot.colors._
  import com.cibo.evilplot.geometry.Extent
  import com.cibo.evilplot.plot.aesthetics.DefaultTheme._
  import com.cibo.evilplot.numeric.Point


  /**
    * Autocorrelation function
    * 
    * @param x Input data
    * @param lm Maximum lag required
    */
  def acf(x: Seq[Double], lm: Int): List[Double] = {
    val m = x.sum/x.length
    val xx = x map (_ - m)
    val v = (xx map (xi => xi*xi)).reduce(_+_)/x.length
      ((0 until lm) map ((k: Int) =>
        (((xx zip (xx drop k)) map (p => p._1*p._2)) reduce (_+_) )/(v*xx.length)
      )
      ).toList
  }

  /**
    * Generate marginal diagnostic plots for MCMC output in the form `Seq[Map[String,Double]]`, where the map is from variable name to sampled value.
    * 
    * @param out The MCMC output
    * @param truth Optional "true" values to be overlaid in the case of synthetic data
    * @param lagMax Maximum autocorrelation to be computed for the ACF plots
    * @param numBars Number of bars to use in the histograms
    */
  def traces(out: Seq[Map[String,Double]], truth: Map[String,Double] = Map(), lagMax: Int = 40, numBars: Int = 50): List[List[Plot]] = {
    val keys = out.head.keys.toList.sorted
    keys map (k => {
      val data = out.map(_(k)).zipWithIndex.map(p => Point(p._2,p._1))
      val trace = LinePlot.series(data, "Line graph", HSL(210, 100, 56)).
        xAxis().yAxis().frame().
        xLabel("Iteration").yLabel(k).title("Trace plot")
      val aplot = BarChart(acf(out.map(_(k)),lagMax)).xAxis().yAxis().frame()
      val hist = Histogram(out.map(_(k)),numBars).xAxis().yAxis().frame().
        xLabel(k).yLabel("Frequency")
        truth.get(k) match {
          case None => List(trace, aplot, hist)
          case Some(v) => List(trace.hline(v), aplot, hist.vline(v))
        }
    })
  }

  /**
    * Generate pairs of bivariate marginal plots.
    * 
    * @param out The MCMC output
    * @param truth Optional true values to be overlaid in the case of synthetic data
    * @param numBars Number of bars to use in the diagonal histograms
    */
  def pairs(out: Seq[Map[String,Double]], truth: Map[String,Double] = Map(), numBars: Int = 30): List[List[Plot]] = {
    val keys = out.head.keys.toList.sorted
    keys map (k1 => keys map (k2 => {
      if (k1 == k2) {
        val hist = Histogram(out.map(_(k1)),numBars).xAxis().yAxis().frame().
          xLabel(k1).yLabel("Frequency")
        truth.get(k1) match {
          case None => hist
          case Some(v) => hist.vline(v)
        }
      }
      else if (k1 < k2) {
        val scat = ScatterPlot(out.map(p => Point(p(k1),p(k2)))).
          xAxis().yAxis().frame().
          xLabel(k1).yLabel(k2)
        val scat1 = truth.get(k1) match {
          case None => scat
          case Some(v) => scat.vline(v)
        }
        truth.get(k2) match {
          case None => scat1
          case Some(v) => scat1.hline(v)
        }
      }
      else {
        val cont = ContourPlot(out.map(p => Point(p(k1),p(k2)))).
          xAxis().yAxis().frame().
          xLabel(k1).yLabel(k2)
        val cont1 = truth.get(k1) match {
          case None => cont
          case Some(v) => cont.vline(v)
        }
        truth.get(k2) match {
          case None => cont1
          case Some(v) => cont1.hline(v)
        }
      }
    }))
  }

}

// eof
