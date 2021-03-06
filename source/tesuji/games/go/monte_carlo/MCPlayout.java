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

package tesuji.games.go.monte_carlo;

import tesuji.games.general.Move;

/**
 * 
 */
public class MCPlayout<MoveType extends Move>
	implements IPlayout
{
	private int[] wins = {0,0};
	private int nrMovesPlayed;
	
	private MonteCarloAdministration<MoveType> _currentAdministration;
	private MonteCarloAdministration<MoveType> _playoutAdministration;
	
	public MCPlayout(MonteCarloAdministration<MoveType> administration)
	{
		_currentAdministration = administration;
		_playoutAdministration = administration.createClone();
		initDataProvider();
	}
	
	public void reset()
	{
		wins = new int[]{0,0};
		nrMovesPlayed = 0;
	}
	
	public void copyFrom(MonteCarloAdministration<MoveType> source)
	{
		_currentAdministration.copyDataFrom(source);
	}
	
	public int playout(int nrPlayouts)
	{
		return playout(nrPlayouts,1);
	}
	
	public int playout(final int nrPlayouts, final int nrThreads)
	{
		reset();
//		_currentAdministration.initPointOwnership(nrPlayouts);
		Thread[] threads = new Thread[nrThreads];
		for (int t=0; t<nrThreads; t++)
		{
			threads[t] = new Thread(new Runnable()
				{
					public void run()
	                {
						MonteCarloAdministration<MoveType> playoutAdministration = _currentAdministration.createClone();
						for (int i = 0; i < nrPlayouts/nrThreads; i++)
						{
							playoutAdministration.copyDataFrom(_currentAdministration);
							boolean blackWins = playoutAdministration.playout();
							synchronized (wins)
							{
								if (blackWins)
									wins[0]++;
								else
									wins[1]++;
							}
							
							nrMovesPlayed += playoutAdministration.getNrSimulatedMoves();
						}
//						_currentAdministration.addPointOwnership(playoutAdministration);
	                }
				});
			threads[t].start();
		}
		for (int t=0; t<nrThreads; t++)
		{
			try
            {
	            threads[t].join();
            }
            catch (InterruptedException exception)
            {
	            // TODO Auto-generated catch block
	            exception.printStackTrace();
            }
		}
		_playoutAdministration.copyDataFrom(_currentAdministration);

		double ratio = ((double)wins[0]/((double)wins[0]+(double)wins[1]));
		int value = (int)(100.0*ratio);
		return value;
	}
	
	/**
     * @return the blackWins
     */
    public int getBlackWins()
    {
    	return wins[0];
    }

	/**
     * @return the whiteWins
     */
    public int getWhiteWins()
    {
    	return wins[1];
    }

	/**
     * @return the komi
     */
//    public double getKomi()
//    {
//    	return _currentAdministration.getKomi();
//    }
//    
//    public double getScore()
//    {
//    	return _currentAdministration.getTerritoryScore();
//    }

	/**
     * @return the nrMovesPlayed
     */
    public int getNrMovesPlayed()
    {
    	return nrMovesPlayed;
    }
    
    public boolean isConsistent()
    {
    	return _currentAdministration.isConsistent();
    }
    
    public MoveType requestMove(byte color)
    {
    	return _currentAdministration.selectSimulationMove();
    }
    
    private void initDataProvider()
    {
    }
}
