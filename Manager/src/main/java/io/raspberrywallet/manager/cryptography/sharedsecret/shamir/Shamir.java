package io.raspberrywallet.manager.cryptography.sharedsecret.shamir;

import java.math.BigInteger;
import java.security.SecureRandom;
/**
 * <p> Calculate Shamir scheme. You need t shares of n for resolve the secret </p>
 */

public class Shamir {

    /**
     * Create a share using Shamir's scheme
     *
     * @param x Shamir's shares
     * @param shamirKeys array of ShamirKeys
     * @return true is x is in k
     */
    public static boolean isRepeat(BigInteger x, ShamirKey[] shamirKeys) {
        if (shamirKeys.length == 0)
            return false;

        for (ShamirKey key : shamirKeys) {
            if (key == null)
                break;
            if (key.getX().equals(x))
                return true;
        }

        return false;
    }

    /**
     * Calculate polynomial
     *
     * @param s parameters of polynomial
     * @param x variable of polynomial
     * @param p prime number (for calulate mod)
     * @return solution of polynomial
     */
    private static BigInteger calculatePolynomial(BigInteger s[], BigInteger x, BigInteger p) {
        BigInteger result = BigInteger.ZERO;
        for (int i = 0; i < s.length; i++)
            result = result.add(s[i].multiply(x.pow(i)));

        result = result.mod(p);
        return result;
    }

    /**
     * Generate parameters of polynomial
     *
     * @param t           number of shares for solve Shamir scheme
     * @param numBits     number of bits of parameters
     * @param SecretBytes secret
     * @return parameters of polynomial
     * @throw ShamirException
     */
    public static BigInteger[] generateParams(int t, int numBits, byte[] SecretBytes) throws ShamirException {
        BigInteger secret = new BigInteger(SecretBytes);

        if (secret.bitLength() >= numBits)
            throw new ShamirException("numBits is too small");

        BigInteger s[] = new BigInteger[t];
        s[0] = secret;
        //System.out.println("s(0) = " + secret + " (secret)" );

        for (int i = 1; i < t; i++) {
            s[i] = new BigInteger(numBits, new SecureRandom());
            //System.out.println("s("+i+") = " +s[i]);
        }

        return s;
    }

    /**
     * Solve Shamir's scheme
     *
     * @param sk array of ShamirKeys
     * @return secret
     */
    public static byte[] calculateLagrange(ShamirKey[] sk) {
        BigInteger p = sk[0].getP();
        BigInteger d;
        BigInteger D;
        BigInteger c;
        BigInteger S = BigInteger.ZERO;
        for (int i = 0; i < sk.length; i++) {
            d = BigInteger.ONE;
            D = BigInteger.ONE;
            for (int j = 0; j < sk.length; j++) {
                if (j == i)
                    continue;
                d = d.multiply(sk[j].getX());
                D = D.multiply(sk[j].getX().subtract(sk[i].getX()));
            }
            c = d.multiply(D.modInverse(p)).mod(p);
            S = S.add(c.multiply(sk[i].getF())).mod(p);
        }
        return S.toByteArray();
    }

    /**
     * Generate shares
     *
     * @param n       number of shares
     * @param t       number of shares for solve Shamir scheme
     * @param numBits number of bits of shares
     * @param polynomialParams       parameters of polynomial
     * @return array of Shamir's shares
     * @throw ShamirException
     * @throw ShamirException
     */
    public static ShamirKey[] generateKeys(int n, int t, int numBits, BigInteger[] polynomialParams) throws ShamirException {
        ShamirKey[] keys = new ShamirKey[n];
        if (polynomialParams[0].bitLength() >= numBits)
            throw new ShamirException("numBits is too small");
        if (t > n)
            throw new ShamirException("number of need shares greater than number of shares");

        BigInteger prime = BigInteger.probablePrime(numBits, new SecureRandom());

        BigInteger fx, x;
        for (int i = 1; i <= n; i++) {
            do {
                x = new BigInteger(numBits, new SecureRandom());
            } while (isRepeat(x, keys));
            fx = calculatePolynomial(polynomialParams, x, prime);
            keys[i - 1] = new ShamirKey();
            keys[i - 1].setP(prime);
            keys[i - 1].setX(x);
            keys[i - 1].setF(fx);
            System.out.println(i + "-> f(" + x + ") = " + keys[i - 1].getF());
        }
        return keys;
    }


}
