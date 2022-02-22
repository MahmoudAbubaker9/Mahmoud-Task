package com.progressoft.tools;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Objects;


public class MainScoringSummary implements ScoringSummary {

    private BigDecimal x;
    private BigDecimal TotalMean;
    private BigDecimal[] value;
    private BigDecimal StandardDeviation;
    private BigDecimal Variance;
    private BigDecimal Median;
    private BigDecimal[] sortedData;
    private BigDecimal Min;
    private BigDecimal Max;

    public MainScoringSummary(int[] value) {
        this.value = Arrays.stream(value).mapToObj(BigDecimal::new).toArray(BigDecimal[]::new);
        x = new BigDecimal(value.length);
    }

    public void mainCalculation() {
        this.mean();
        this.standardDeviation();
        this.variance();
        this.median();
        this.min();
        this.max();
    }

    /**
     *
     * @param number          numbers need to meat the requirements
     * @return a BigDecimal number whose scale is the specified value
     *
     */
    private BigDecimal ScaleNumber(BigDecimal number) {
        return number.setScale(2, RoundingMode.HALF_EVEN);
    }

    /**
     *
     * Handle the @mean calculation by this equation
     *
     */

    @Override
    public BigDecimal mean() {
        if(!Objects.equals(this.TotalMean, null)) return ScaleNumber(TotalMean);
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal number : value) {
            sum = sum.add(number);
        }
        TotalMean = sum.divide(x, RoundingMode.HALF_EVEN);
        return ScaleNumber(TotalMean);
    }

    /**
     *
     * @standardDeviation is calculated by this equation
     *
     */

    @Override
    public BigDecimal standardDeviation() {
        if (Objects.equals(this.TotalMean, null)) this.mean();
        if (!Objects.equals(this.StandardDeviation, null)) return ScaleNumber(StandardDeviation);

        BigDecimal mean = TotalMean;
        BigDecimal standDev = BigDecimal.ZERO;
        for (BigDecimal number : value) {
            standDev = standDev.add((number.subtract(mean)).pow(2));
        }

        MathContext mc = new MathContext(10);
        StandardDeviation = standDev.divide(x, RoundingMode.HALF_EVEN).sqrt(mc);
        return ScaleNumber(StandardDeviation);
    }

    /**
     *
     * @variance is calculated by this equation
     *
     */

    @Override
    public BigDecimal variance() {
        if(Objects.equals(this.StandardDeviation, null)) this.standardDeviation();
        if(!Objects.equals(this.Variance, null)) return ScaleNumber(Variance);

        Variance = StandardDeviation.pow(2);
        return ScaleNumber(Variance);
    }

    /**
     *
     * @median is calculated by this equation
     *
     */

    @Override
    public BigDecimal median() {
        if(!Objects.equals(this.Median, null)) return ScaleNumber(Median);

        sortedData = Arrays.copyOf(value, value.length);
        Arrays.sort(sortedData);
        BigDecimal median = BigDecimal.ZERO;
        if(sortedData.length % 2 > 0)
            median = sortedData[sortedData.length / 2];
        else {
            median = sortedData[sortedData.length / 2].add(sortedData[sortedData.length / 2 + 1]).divide(BigDecimal.valueOf(2), RoundingMode.HALF_EVEN);
        }
        Median = median;
        return ScaleNumber(Median);
    }

    /**
     *
     * @min is calculated by this equation
     *
     */

    @Override
    public BigDecimal min() {
        if(!Objects.equals(this.Min, null)) return ScaleNumber(Min);

        BigDecimal min = BigDecimal.ZERO;
        if(sortedData != null)
            min = sortedData[0];
        else {
            min = value[0];
            for (BigDecimal number : value) {
                min = min.min(number);
            }
        }
        Min = min;
        return ScaleNumber(Min);
    }

    /**
     *
     * @max is calculated by this equation
     *
     */

    @Override
    public BigDecimal max() {
        if(!Objects.equals(this.Max, null)) return ScaleNumber(Max);

        BigDecimal max = BigDecimal.ZERO;
        if(sortedData != null)
            max = sortedData[sortedData.length - 1];
        else {
            max = value[0];
            for (BigDecimal number : value) {
                max = max.max(number);
            }
        }
        Max = max;
        return ScaleNumber(Max);
    }
}