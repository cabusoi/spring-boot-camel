package integration.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.UUID;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractAsset implements Serializable{

	public static final BigInteger B = BigInteger.ONE.shiftLeft(64); // 2^64
	public static final BigInteger L = BigInteger.valueOf(Long.MAX_VALUE);

	public static BigInteger convertToBigInteger(UUID id) {
		BigInteger lo = BigInteger.valueOf(id.getLeastSignificantBits());
		BigInteger hi = BigInteger.valueOf(id.getMostSignificantBits());

		// If any of lo/hi parts is negative interpret as unsigned

		if (hi.signum() < 0)
			hi = hi.add(B);

		if (lo.signum() < 0)
			lo = lo.add(B);

		return lo.add(hi.multiply(B));
	}

	public static UUID convertFromBigInteger(BigInteger x) {
		BigInteger[] parts = x.divideAndRemainder(B);
		BigInteger hi = parts[0];
		BigInteger lo = parts[1];

		if (L.compareTo(lo) < 0)
			lo = lo.subtract(B);

		if (L.compareTo(hi) < 0)
			hi = hi.subtract(B);

		return new UUID(hi.longValueExact(), lo.longValueExact());
	}

	public abstract Integer getId();

	public abstract String getSourceId();

	public abstract void setId(Integer intValue);
}