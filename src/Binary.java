
/**
 * Methods for converting between binary and decimal.
 *
 * @author Mariana De Luna
 */
public class Binary {

	/** Class constant defines the length of binary numbers. */
	public static final int BINARY_LENGTH = 32;

	/**
	 * Converts a two's complement binary number to signed decimal
	 *
	 * @param b
	 *            The two's complement binary number
	 * @return The equivalent decimal value
	 * @exception IllegalArgumentException
	 *                Parameter array length is not BINARY_LENGTH.
	 */
	public static long binToSDec(boolean[] b) { // test 4
		// PROGRAM 1: Student must complete this method
		// return value is a placeholder, student should replace with correct return

		// Added to starter code on February 5
		// Example of throwing an IllegalArgumentException
		// If the exception condition is true, throw the exception
		if (b.length != Binary.BINARY_LENGTH) {
			// If the condition is true, the exception will be thrown
			// and the method execution will stop.
			 throw new IllegalArgumentException("parameter array is not correct length");
		} // end if

		// If the method execution reaches this point, the exception was
		// not thrown.
		// Write the rest of the method here.

		// inverting the bits after the first one is copied from the rightmost side
		int j = 0;
		while (b[j] == false) {
			j++;
		} // end while
		if (b[j] == true) {
			j++;
			for (int i = j; i < Binary.BINARY_LENGTH; i++) {
				if (b[i] == true) {
					b[i] = false;
				} else if (b[i] == false) {
					b[i] = true;
				} // end if
			} // end for
		} // end if

		// BinToUDec
		int base = 1;
		int decNum = 0;
		for (int n = 0; n < Binary.BINARY_LENGTH; n++) {
			if (b[n] == true) {
				decNum = decNum + base;
			} // end if
			base = base * 2;
		} // end for
		decNum = decNum * -1;
		return decNum;
	} // end binToSDec()

	/**
	 * Converts an unsigned binary number to unsigned decimal
	 *
	 * @param b
	 *            The unsigned binary number
	 * @return The equivalent decimal value
	 * @exception IllegalArgumentException
	 *                Parameter array length is not BINARY_LENGTH.
	 */
	public static long binToUDec(boolean[] b) { // test 2
		// PROGRAM 1: Student must complete this method
		// return value is a placeholder, student should replace with correct return
		if (b.length < Binary.BINARY_LENGTH) {
			 throw new IllegalArgumentException("parameter array is not correct length");
		} // end if
		long base = 1;
		long decNum = 0;
		for (int j = 0; j < Binary.BINARY_LENGTH; j++) {
			if (b[j] == true) {
				decNum = decNum + base;
			} // end if
			base = base * 2;
		} // end for
		return decNum;
	} // end binToUDec()

	/**
	 * Converts a signed decimal number to two's complement binary
	 *
	 * @param d
	 *            The decimal value
	 * @exception IllegalArgumentException
	 *                Parameter is outside valid range.
	 */
	public static boolean[] sDecToBin(long d) { // test 3
		// PROGRAM 1: Student must complete this method
		// return value is a placeholder, student should replace with correct return
		boolean[] binaryNum = new boolean[Binary.BINARY_LENGTH];
		if (d >= 2300000000L) {
			 throw new IllegalArgumentException("parameter array is not correct length");
		} // end if
		if (d < 0) {
			d = d * -1; // make negative number positive
			binaryNum = uDecToBin(d);
			// invert the bits after the first one is copied
			int j=0;
			for (; j < Binary.BINARY_LENGTH; j++) {
				if(binaryNum[j]==true) {
					j++;
					break;
				}
			} // end for
			for(; j<Binary.BINARY_LENGTH; j++) {
				binaryNum[j] = !binaryNum[j];
			}
			return binaryNum;
		} else {
			binaryNum = uDecToBin(d);
			return binaryNum;
		} // end if
	} // end sDecToBin()

	/**
	 * Converts an unsigned decimal number to binary
	 *
	 * @param d
	 *            The decimal value
	 * @return The equivalent binary representation
	 * @exception IllegalArgumentException
	 *                Parameter is outside valid range.
	 */
	public static boolean[] uDecToBin(long d) { // test 2
		// PROGRAM 1: Student must complete this method
		// return value is a placeholder, student should replace with correct return
		boolean[] binaryNum = new boolean[Binary.BINARY_LENGTH];
		if (d >= 4300000000L) {
			 throw new IllegalArgumentException("parameter array is not correct length");
		} // end if
		for (int j = 0; j < Binary.BINARY_LENGTH; j++) {
			if (d % 2 == 1) {
				binaryNum[j] = true;
			} // end if
			if (d % 2 == 0) {
				binaryNum[j] = false;
			} // end if
			d = d / 2;
		} // end for
		return binaryNum;
	} // end uDecToBin()

	/**
	 * Returns a string representation of the binary number. Uses an underscore to
	 * separate each group of 4 bits.
	 *
	 * @param b
	 *            The binary number
	 * @return The string representation of the binary number.
	 * @exception IllegalArgumentException
	 *                Parameter array length is not BINARY_LENGTH.
	 */
	public static String toString(boolean[] b) {
		// PROGRAM 1: Student must complete this method
		// return value is a placeholder, student should replace with correct return
		String n = "";
		for (int j = 0; j < Binary.BINARY_LENGTH; j++) {
			if (b[j] == true) {
				n = '1' + n;
			} else if (b[j] == false) {
				n = '0' + n;
			} // end if
			if (b.length != Binary.BINARY_LENGTH) {
				 throw new IllegalArgumentException("parameter array is not correct length");
			} // end if
			if (j % 4 == 3) {
				if (j != Binary.BINARY_LENGTH - 1) {
					n = '_' + n;
				} // end if
			} // end if
		} // end for
		return n;
	} // end toString()

	/**
	 * Returns a hexadecimal representation of the unsigned binary number. Uses an
	 * underscore to separate each group of 4 characters.
	 *
	 * @param b
	 *            The binary number
	 * @return The hexadecimal representation of the binary number.
	 * @exception IllegalArgumentException
	 *                Parameter array length is not BINARY_LENGTH.
	 */
	public static String toHexString(boolean[] b) {// test 5
		// PROGRAM 1: Student must complete this method
		// return value is a placeholder, student should replace with correct return
		char[] hex = { 'F', 'E', 'D', 'C', 'B', 'A' };

		long decimal = binToUDec(b);
		String hexResult = "";
		long num;
		while (decimal > 0) {		
			num = decimal % 16;		
			if (num > 9) {
				hexResult = hex[(int) (15 - num)] + hexResult;
			} else {
				hexResult = num + hexResult;
				if (hexResult.length() -1 % 4 == 3) {
					hexResult = '_' + hexResult;
				} // end if
			} // end if
			decimal = (int) Math.round(decimal / 16 + .5);
			// because the decimal is being rounded, the program will keep running 
			// unless there is a break when decimal is equal to 1
			if (decimal == 1) {
				break;
			} // end if
		} // end while
		return hexResult;
	} // end toHexString()

}