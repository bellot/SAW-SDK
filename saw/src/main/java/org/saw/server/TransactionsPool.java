package org.saw.server ;

import org.saw.* ;
import org.saw.transaction.* ;
import org.saw.util.net.* ;

public class TransactionsPool
{
    private final Thread[]     transactionThreads = new Thread[Parameters.TRANSACTIONS_COUNT] ;
    private final SocketsQueue socketsQueue ;

    public TransactionsPool(SocketsQueue socketsQueue)
    {
	this.socketsQueue = socketsQueue ;

	for (int i = 0 ; i < Parameters.TRANSACTIONS_COUNT ; i++ )
	    transactionThreads[i] = new Thread(new Transaction(socketsQueue)) ;
    }

    public final void start()
    {
	for (int i = 0 ; i < Parameters.TRANSACTIONS_COUNT ; i++ )
	    transactionThreads[i].start() ;
    }

    public final void stop()
    {
	for (int i = 0 ; i < Parameters.TRANSACTIONS_COUNT ; i++ )
	    socketsQueue.push(null) ;
    }

    public final void join()
    {
	for (int i = 0 ; i < Parameters.TRANSACTIONS_COUNT ; i++ ) {
	    try {
		transactionThreads[i].join() ;
	    } catch (Exception e) {}
	}
    }

}
