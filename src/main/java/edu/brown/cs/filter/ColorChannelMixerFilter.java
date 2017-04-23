package edu.brown.cs.filter;

/**
 * This class wraps ffmpeg's colorchannelmixer filter. Use the nested Builder class to obtain an instance of this object.
 */
public class ColorChannelMixerFilter implements Filter {

  private double rr;
  private double rg;
  private double rb;
  private double ra;

  private double gr;
  private double gg;
  private double gb;
  private double ga;

  private double br;
  private double bg;
  private double bb;
  private double ba;

  private double ar;
  private double ag;
  private double ab;
  private double aa;

  private ColorChannelMixerFilter(double rr, double rg, double rb, double ra,
                                  double gr, double gg, double gb, double ga,
                                  double br, double bg, double bb, double ba,
                                  double ar, double ag, double ab, double aa) {
    this.rr = rr;
    this.rg = rg;
    this.rb = rb;
    this.ra = ra;

    this.gr = gr;
    this.gg = gg;
    this.gb = gb;
    this.ga = ga;

    this.br = br;
    this.bg = bg;
    this.bb = bb;
    this.ba = ba;

    this.ar = ar;
    this.ag = ag;
    this.ab = ab;
    this.aa = aa;
  }

  @Override
  public String getFilterString() {
    return String.format("colorchannelmixer=" +
            "%.2f:%.2f:%.2f:%.2f:%.2f:%.2f:%.2f:%.2f:%.2f:%.2f:%.2f:%.2f:%.2f:%.2f:%.2f:%.2f",
        rr, rg, rb, ra,
        gr, gg, gb, ga,
        br, bg, bb, ba,
        ar, ag, ab, aa);
  }


  public static class Builder{

    private double rr = 1.0;
    private double rg = 0.0;
    private double rb = 0.0;
    private double ra = 0.0;

    private double gr = 0.0;
    private double gg = 1.0;
    private double gb = 0.0;
    private double ga = 0.0;

    private double br = 0.0;
    private double bg = 0.0;
    private double bb = 1.0;
    private double ba = 0.0;

    private double ar = 0.0;
    private double ag = 0.0;
    private double ab = 0.0;
    private double aa = 1.0;

    public Builder rr(double value) {
      if (validValue(value)) {
        rr = value;
      } else {
        throw new IllegalArgumentException("Can only set a value in range [-2.0, 2.0] inclusive.");
      }

      return this;
    }

    public Builder rg(double value) {
      if (validValue(value)) {
        rg = value;
      } else {
        throw new IllegalArgumentException("Can only set a value in range [-2.0, 2.0] inclusive.");
      }

      return this;
    }

    public Builder rb(double value) {
      if (validValue(value)) {
        rb = value;
      } else {
        throw new IllegalArgumentException("Can only set a value in range [-2.0, 2.0] inclusive.");
      }

      return this;
    }

    public Builder ra(double value) {
      if (validValue(value)) {
        ra = value;
      } else {
        throw new IllegalArgumentException("Can only set a value in range [-2.0, 2.0] inclusive.");
      }

      return this;
    }

    public Builder gr(double value) {
      if (validValue(value)) {
        gr = value;
      } else {
        throw new IllegalArgumentException("Can only set a value in range [-2.0, 2.0] inclusive.");
      }

      return this;
    }

    public Builder gg(double value) {
      if (validValue(value)) {
        gg = value;
      } else {
        throw new IllegalArgumentException("Can only set a value in range [-2.0, 2.0] inclusive.");
      }

      return this;
    }

    public Builder gb(double value) {
      if (validValue(value)) {
        gb = value;
      } else {
        throw new IllegalArgumentException("Can only set a value in range [-2.0, 2.0] inclusive.");
      }

      return this;
    }

    public Builder ga(double value) {
      if (validValue(value)) {
        ga = value;
      } else {
        throw new IllegalArgumentException("Can only set a value in range [-2.0, 2.0] inclusive.");
      }

      return this;
    }

    public Builder br(double value) {
      if (validValue(value)) {
        br = value;
      } else {
        throw new IllegalArgumentException("Can only set a value in range [-2.0, 2.0] inclusive.");
      }

      return this;
    }

    public Builder bg(double value) {
      if (validValue(value)) {
        bg = value;
      } else {
        throw new IllegalArgumentException("Can only set a value in range [-2.0, 2.0] inclusive.");
      }

      return this;
    }

    public Builder bb(double value) {
      if (validValue(value)) {
        bb = value;
      } else {
        throw new IllegalArgumentException("Can only set a value in range [-2.0, 2.0] inclusive.");
      }

      return this;
    }

    public Builder ba(double value) {
      if (validValue(value)) {
        ba = value;
      } else {
        throw new IllegalArgumentException("Can only set a value in range [-2.0, 2.0] inclusive.");
      }

      return this;
    }

    public Builder ar(double value) {
      if (validValue(value)) {
        ar = value;
      } else {
        throw new IllegalArgumentException("Can only set a value in range [-2.0, 2.0] inclusive.");
      }

      return this;
    }

    public Builder ag(double value) {
      if (validValue(value)) {
        ag = value;
      } else {
        throw new IllegalArgumentException("Can only set a value in range [-2.0, 2.0] inclusive.");
      }

      return this;
    }

    public Builder ab(double value) {
      if (validValue(value)) {
        ab = value;
      } else {
        throw new IllegalArgumentException("Can only set a value in range [-2.0, 2.0] inclusive.");
      }

      return this;
    }

    public Builder aa(double value) {
      if (validValue(value)) {
        aa = value;
      } else {
        throw new IllegalArgumentException("Can only set a value in range [-2.0, 2.0] inclusive.");
      }

      return this;
    }

    private boolean validValue(double value) {
      return !(-2.0 > value || 2.0 < value);
    }

    public ColorChannelMixerFilter build() {
      return new ColorChannelMixerFilter(rr, rg, rb, ra, gr, gg, gb, ga, br, bg, bb, ba, ar, ag, ab, aa);
    }

  }

}
