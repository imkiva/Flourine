package com.imkiva.flourine.core.geometric;

import com.imkiva.flourine.core.misc.FastMath;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author kiva
 * @date 2018/9/24
 */
public class VectorTest {

    @Test
    public void add() {
        Vector a = Vector.of(1, 2, 3);
        Vector b = Vector.of(4, 5, 6);
        Vector expected = Vector.of(5, 7, 9);
        Assert.assertEquals(expected, a.add(b));
    }

    @Test
    public void subtract() {
        Vector a = Vector.of(1, 2, 3);
        Vector b = Vector.of(4, 5, 6);
        Vector expected = Vector.of(-3, -3, -3);
        Assert.assertEquals(expected, a.subtract(b));
    }

    @Test
    public void multiply() {
        Vector a = Vector.of(1, 2, 3);
        Assert.assertEquals(Vector.of(3, 6, 9), a.multiply(3));
        Assert.assertEquals(Vector.of(1.5f, 3, 4.5f), a.multiply(1.5f));
        Assert.assertEquals(Vector.of(0.5f, 1, 1.5f), a.multiply(0.5f));
        Assert.assertEquals(Vector.of(0.3f, 0.6f, 0.9f), a.multiply(0.3f));
    }

    @Test
    public void innerProduct() {
        Vector a = Vector.of(1, 2, 3);
        Vector b = Vector.of(4, 5, 6);
        Assert.assertEquals(32f, a.innerProduct(b), FastMath.FLOAT_TOLERANCE);
    }

    @Test
    public void crossProduct() {
        Vector a = Vector.of(1, 2, 3);
        Vector b = Vector.of(4, 5, 6);
        Vector expected = Vector.of(-3, 6, -3);
        Assert.assertEquals(expected, a.crossProduct(b));
    }

    @Test
    public void cosineOf() {
        Vector a = Vector.of(0, 1, 1);
        Vector b = Vector.of(1, 1, 0);
        Assert.assertEquals(0.5f,
                a.cosineOf(b),
                FastMath.FLOAT_TOLERANCE);
    }

    @Test
    public void sineOf() {
        Vector a = Vector.of(0, 1, 1);
        Vector b = Vector.of(1, 1, 0);
        Assert.assertEquals(FastMath.sqrt(3) / 2,
                a.sineOf(b),
                FastMath.FLOAT_TOLERANCE);
    }

    @Test
    public void isParallelTo() {
        Vector a = Vector.of(0, 1, 1);
        Vector c = Vector.of(0, 5, 5);
        Assert.assertTrue(a.isParallelTo(c));

        Vector b = Vector.of(1, 1, 0);
        Assert.assertFalse(a.isParallelTo(b));
    }

    @Test
    public void isPerpendicularTo() {
        Vector a = Vector.of(4, 12, 5);
        Vector c = Vector.of(-1, 5, -11.2f);
        Assert.assertTrue(a.isPerpendicularTo(c));

        Vector b = Vector.of(1, 1, 0);
        Assert.assertFalse(a.isParallelTo(b));
    }

    @Test
    public void reverse() {
        Vector a = Vector.of(231.2f, 2313, 112.4f);
        Assert.assertEquals(Vector.of(-231.2f, -2313, -112.4f), a.reverse());
    }

    @Test
    public void getModulus() {
        Vector a = Vector.of(4, 12, 5);
        Assert.assertEquals(FastMath.sqrt(185), a.getModulus(), FastMath.FLOAT_TOLERANCE);

        Vector b = Vector.of(1, 1, 0);
        Assert.assertEquals(FastMath.sqrt(2), b.getModulus(), FastMath.FLOAT_TOLERANCE);
    }
}