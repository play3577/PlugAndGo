/**
 * Project: Tesuji Go Framework.<br>
 * <br>
 * <font color="#CC6600"><font size=-1> Copyright (c) 1985-2006 Mark Boon<br>
 * All rights reserved.<br>
 * <br>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * provided that the above copyright notice(s) and this permission notice appear
 * in all copies of the Software and that both the above copyright notice(s) and
 * this permission notice appear in supporting documentation.<br>
 * <br>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.<br>
 * <br>
 * <font color="#00000"><font size=+1>
 * 
 */
package tesuji.games.go.util;

import static tesuji.games.go.util.GoArray.*;
/**
 * This is a four-way cursor. It will move to each diagonal neighbour of a given
 * starting point.
 * 
 * Although using the cursor definition is more elegant, just looping over the
 * offset array is more efficient.
 */
public class DiagonalCursor
{
	/** This field is public for efficiency. */
	private static final int[] intOffset = new int[]
	{
		right(below(0)),
		right(above(0)),
		left(below(0)),
		left(above(0))
	};
	
	/** This field is public for efficiency. */
	private static final short[] shortOffset = new short[]
	{
		(short)right(below(0)),
		(short)right(above(0)),
		(short)left(below(0)),
		(short)left(above(0))
	};
	
	public static final int getNeighbour(int xy, int n)
	{
		return xy + intOffset[n];
	}
	
	public static final short getNeighbour(short xy, int n)
	{
		return (short)(xy + shortOffset[n]);
	}
}